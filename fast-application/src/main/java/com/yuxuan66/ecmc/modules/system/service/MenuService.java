package com.yuxuan66.ecmc.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.yuxuan66.ecmc.common.utils.StpUtil;
import com.yuxuan66.ecmc.common.utils.tree.TreeUtil;
import com.yuxuan66.ecmc.modules.system.entity.Menu;
import com.yuxuan66.ecmc.modules.system.entity.RolesMenus;
import com.yuxuan66.ecmc.modules.system.entity.User;
import com.yuxuan66.ecmc.modules.system.entity.consts.MenuType;
import com.yuxuan66.ecmc.modules.system.entity.dto.MenuDto;
import com.yuxuan66.ecmc.modules.system.mapper.MenuMapper;
import com.yuxuan66.ecmc.modules.system.mapper.RolesMenusMapper;
import com.yuxuan66.ecmc.modules.system.mapper.UserMapper;
import com.yuxuan66.ecmc.support.base.BaseService;
import io.undertow.client.ClientConnection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2022/12/9
 */
@Service
public class MenuService extends BaseService<Menu, MenuMapper> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RolesMenusMapper rolesMenusMapper;
    /**
     * 构建前台需要的菜单树
     * @return 菜单树
     */
    public List<Menu> build(){
        User user = userMapper.findUserById(StpUtil.getUser(User.class).getId());
        return new TreeUtil<Menu>().menuList(user.getMenus().stream().filter(item->item.getType()!= MenuType.BUTTON).toList());
    }

    /**
     * 查询整个菜单树
     * @return 菜单树
     */
    public List<Menu> tree(){
        return new TreeUtil<Menu>().menuList(query().orderByAsc("sort").list());
    }

    /**
     * 添加/修改一个菜单
     * @param menu 菜单
     */
    public void addOrEdit(Menu menu){
        menu.insertOrUpdate();
    }

    /**
     * 批量删除菜单
     * @param ids 菜单id
     */
    public void del(Set<Long> ids){
        removeBatchByIds(ids);
        rolesMenusMapper.delete(new QueryWrapper<RolesMenus>().in("menu_id",ids));
    }
}
