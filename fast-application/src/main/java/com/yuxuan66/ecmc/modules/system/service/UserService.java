package com.yuxuan66.ecmc.modules.system.service;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.ecmc.common.utils.FileUtil;
import com.yuxuan66.ecmc.common.utils.PasswordUtil;
import com.yuxuan66.ecmc.common.utils.StpUtil;
import com.yuxuan66.ecmc.config.RsaProperties;
import com.yuxuan66.ecmc.modules.system.entity.Menu;
import com.yuxuan66.ecmc.modules.system.entity.Role;
import com.yuxuan66.ecmc.modules.system.entity.User;
import com.yuxuan66.ecmc.modules.system.entity.UsersRoles;
import com.yuxuan66.ecmc.modules.system.entity.consts.UserStatus;
import com.yuxuan66.ecmc.modules.system.entity.dto.LoginDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.UserInfoDto;
import com.yuxuan66.ecmc.modules.system.entity.query.UserQuery;
import com.yuxuan66.ecmc.modules.system.mapper.RoleMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UserMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UsersRolesMapper;
import com.yuxuan66.ecmc.support.base.BaseService;
import com.yuxuan66.ecmc.support.base.resp.Ps;
import com.yuxuan66.ecmc.support.exception.BizException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2022/12/6
 */
@Service
public class UserService extends BaseService<User, UserMapper> {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UsersRolesMapper usersRolesMapper;

    /**
     * 用户登录
     *
     * @param loginDto 登录信息
     * @return token
     */
    public String login(LoginDto loginDto) {

        User user = query().eq("username", loginDto.getUsername()).one();
        // RSA解密前端传递密码
        RSA rsa = new RSA(RsaProperties.privateKey, null);
        String password = rsa.decryptStr(loginDto.getPassword(), KeyType.PrivateKey);
        // 密码校验
        if (user == null || !PasswordUtil.validatePassword(password, user.getPassword())) {
            throw new BizException("用户名密码输入错误");
        }
        // 判断用户状态
        if (user.getStatus() != UserStatus.NORMAL) {
            throw new BizException("您的账户已经被" + user.getStatus().getName() + ", 请联系系统管理员.");
        }
        StpUtil.login(user);

        return StpUtil.getTokenValue();
    }

    /**
     * 分页查询用户列表
     *
     * @param userQuery 查询条件
     * @return 用户列表
     */
    public Ps list(UserQuery userQuery) {
        List<User> userList = baseMapper.listUser(userQuery);
        for (User user : userList) {
            user.setRoleNames(String.join(",", user.getRoles().stream().map(Role::getName).toList()));
            user.setRoleIds(user.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        }
        return Ps.ok(baseMapper.countUser(userQuery), userList);
    }

    /**
     * 获取当前登录用户的详细信息
     *
     * @return 用户信息
     */
    public UserInfoDto getUserInfo() {
        UserInfoDto userInfoDto = new UserInfoDto();
        // 当前登录用户
        User user = StpUtil.getUser(User.class);
        BeanUtils.copyProperties(user, userInfoDto);
        userInfoDto.setUserId(user.getId());
        userInfoDto.setRealName(user.getNickName());
        userInfoDto.setRoles(roleMapper.selectRoleByUserId(user.getId()));
        return userInfoDto;
    }

    /**
     * 获取当前登录用户的权限代码
     *
     * @return 权限代码
     */
    public List<String> getPermCode() {
        User user = baseMapper.findUserById(StpUtil.getUser(User.class).getId());
        return user.getMenus().stream().map(Menu::getPermission).toList();
    }

    /**
     * 判断指定字段是否有用户使用
     *
     * @param field 字段
     * @param value 数据
     * @param id    id
     * @return 是否使用
     */
    public boolean checkFieldExist(String field, String value, Long id) {
        return query().eq(field, value).ne(id != null, "id", id).count() > 0;
    }

    /**
     * 添加一个用户
     *
     * @param user 用户
     */
    public void add(User user) {
        user.setPassword(PasswordUtil.createHash(user.getPassword()));
        user.insert();
        for (Long roleId : user.getRoleIds()) {
            new UsersRoles(user.getId(), roleId).insert();
        }
    }

    /**
     * 编辑一个用户
     * @param user 用户
     */
    public void edit(User user){
        User old = getById(user.getId());
        if(!user.getPassword().equals(old.getPassword())){
            user.setPassword(PasswordUtil.createHash(user.getPassword()));
        }
        usersRolesMapper.delete(new QueryWrapper<UsersRoles>().eq("user_id",user.getId()));
        for (Long roleId : user.getRoleIds()) {
            new UsersRoles(user.getId(), roleId).insert();
        }
        user.updateById();
    }

    /**
     * 删除用户
     * @param ids 用户id
     */
    public void del(Set<Long>ids){
        usersRolesMapper.delete(new QueryWrapper<UsersRoles>().in("user_id",ids));
        removeBatchByIds(ids);
    }

    /**
     * 导出用户列表
     * @param userQuery 查询条件
     */
    public void download(UserQuery userQuery){
        FileUtil.exportExcel(preDownload(baseMapper.listUser(userQuery)));
    }

}
