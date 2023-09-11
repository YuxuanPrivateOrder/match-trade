package com.yuxuan66.ecmc.modules.system.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuxuan66.ecmc.cache.redis.RedisKit;
import com.yuxuan66.ecmc.common.utils.FileUtil;
import com.yuxuan66.ecmc.common.utils.PasswordUtil;
import com.yuxuan66.ecmc.common.utils.StpUtil;
import com.yuxuan66.ecmc.config.RsaProperties;
import com.yuxuan66.ecmc.modules.system.entity.*;
import com.yuxuan66.ecmc.modules.system.entity.consts.UserStatus;
import com.yuxuan66.ecmc.modules.system.entity.dto.*;
import com.yuxuan66.ecmc.modules.system.entity.query.UserQuery;
import com.yuxuan66.ecmc.modules.system.mapper.RoleMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UserMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UsersRolesMapper;
import com.yuxuan66.ecmc.support.base.BaseService;
import com.yuxuan66.ecmc.support.base.resp.Ps;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;
import com.yuxuan66.ecmc.support.log.annotation.LogLogin;
import com.yuxuan66.ecmc.support.log.enums.LoginMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author Sir丶雨轩
 * @since 2022/12/6
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends BaseService<User, UserMapper> {

    private final RedisKit redisKit;
    private final CaptchaService captchaService;
    private final ReentrantLock lock = new ReentrantLock();
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UsersRolesMapper usersRolesMapper;


    /**
     * 判断用户状态并保存登陆信息
     *
     * @param user 用户
     */
    private void checkStatus$Login(User user) {
        // 判断用户状态
        if (user.getStatus() != UserStatus.NORMAL) {
            throw new BizException("您的账户已经被" + user.getStatus().getName() + ", 请联系系统管理员.");
        }
        StpUtil.login(user);
    }

    /**
     * 用户登录
     *
     * @param loginDto 登录信息
     * @return token
     */
    @LogLogin
    public String login(LoginDto loginDto) {

        User user = query().eq("username", loginDto.getUsername()).one();
        // RSA解密前端传递密码
        RSA rsa = new RSA(RsaProperties.privateKey, null);
        String password = rsa.decryptStr(loginDto.getPassword(), KeyType.PrivateKey);
        // 密码校验
        if (user == null || !PasswordUtil.validatePassword(password, user.getPassword())) {
            throw new BizException("用户名密码输入错误");
        }
        checkStatus$Login(user);

        return StpUtil.getTokenValue();
    }

    /**
     * 手机验证码登陆
     *
     * @param smsLoginDto 登陆信息
     * @return 标准返回
     */
    @LogLogin(LoginMethod.PHONE)
    public String login(SmsLoginDto smsLoginDto) {
        // 判断图片验证码
        captchaService.checkImgCaptcha(smsLoginDto);
        // 判断手机验证码是否正确
        String code = redisKit.getAndDel(CacheKey.CAPTCHA_PHONE_LOGIN_PRE + smsLoginDto.getPhone());
        if (!code.equals(smsLoginDto.getCode())) {
            throw new BizException("短信验证码错误");
        }
        // 判断用户是否存在
        User user = query().eq("phone", smsLoginDto.getPhone()).one();

        if (user == null) {
            throw new BizException("手机号尚未注册，请先注册用户");
        }

        checkStatus$Login(user);

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
     *
     * @param user 用户
     */
    public void edit(User user) {
        User old = getById(user.getId());
        if (!user.getPassword().equals(old.getPassword())) {
            user.setPassword(PasswordUtil.createHash(user.getPassword()));
        }
        usersRolesMapper.delete(new QueryWrapper<UsersRoles>().eq("user_id", user.getId()));
        for (Long roleId : user.getRoleIds()) {
            new UsersRoles(user.getId(), roleId).insert();
        }
        user.updateById();
    }

    /**
     * 删除用户
     *
     * @param ids 用户id
     */
    public void del(Set<Long> ids) {
        usersRolesMapper.delete(new QueryWrapper<UsersRoles>().in("user_id", ids));
        // TODO 移除用户关联的角色等信息
        removeBatchByIds(ids);
    }

    /**
     * 重置密码
     *
     * @param updatePasswordDto 重置密码信息
     */
    public void retrievePassword(UpdatePasswordDto updatePasswordDto) {
        // 校验图形验证码
        captchaService.checkImgCaptcha(updatePasswordDto);
        // 校验手机验证码
        captchaService.checkSmsCode(updatePasswordDto);
        // 修改用户密码
        User user = query().eq("phone", updatePasswordDto.getPhone()).one();
        Assert.notNull(user, () -> BizException.of("当前手机号尚未注册用户"));
        user.setPassword(PasswordUtil.createHash(updatePasswordDto.getNewPassword()));
        user.updateById();
    }

    /**
     * 注册用户
     *
     * @param registerDto 注册信息
     */
    public void register(RegisterDto registerDto) {
        // 校验图形验证码
        captchaService.checkImgCaptcha(registerDto);
        // 校验手机验证码
        captchaService.checkSmsCode(registerDto);
        // 创建一个新用户
        try {
            lock.lock();
            // 判断用户是否存在
            Assert.isFalse(checkFieldExist("phone", registerDto.getPhone(), null), () -> BizException.of("当前手机号已经注册"));
            // 判断账号是否存在
            Assert.isFalse(checkFieldExist("username", registerDto.getAccount(), null), () -> BizException.of("当前账号已经注册"));
            // 创建新用户
            User user = new User();
            user.setUsername(registerDto.getAccount());
            user.setNickName(registerDto.getAccount());
            user.setAvatar(ConfigKit.get(CacheKey.DEFAULT_USER_AVATAR));
            user.setPhone(registerDto.getPhone());
            user.setPassword(PasswordUtil.createHash(registerDto.getPassword()));
            user.insert();
            // 创建用户角色
            String roleId = ConfigKit.get(CacheKey.DEFAULT_USER_ROLE);
            Assert.notBlank(roleId, () -> BizException.of("系统未配置默认用户角色"));
            new UsersRoles(user.getId(), Long.parseLong(roleId)).insert();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 获取当前登录的用户
     * @return 用户
     */
    public User getLoginUser(){
        User user = getById(StpUtil.getLoginId());
        // 对手机号和邮箱进行*号处理
        user.setPhone(user.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        user.setEmail(user.getEmail().replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4"));
        return user;
    }
    /**
     * 编辑当前登录用户的基本信息
     * @param user 用户
     */
    public void editLogin(User user){
        user.setId(StpUtil.getLoginId());
        user.updateById();
        StpUtil.login(getById(user.getId()));
    }
    /**
     * 导出用户列表
     *
     * @param userQuery 查询条件
     */
    public void download(UserQuery userQuery) {
        FileUtil.exportExcel(preDownload(baseMapper.listUser(userQuery)));
    }


}
