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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2022/12/9
 */
@Service
@Transactional
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
     * @param menuIds 菜单id
     */
    public void del(List<Long> menuIds){
        // 1. 创建一个集合来存储需要删除的菜单ID及其子菜单ID
        Set<Long> menuAndChildIds = new HashSet<>(menuIds);

        // 2. 递归查找子菜单ID并添加到集合中
        findChildIds(menuIds, menuAndChildIds);

        // 3. 删除菜单及其子菜单
        removeBatchByIds(menuAndChildIds);

        // 4. 删除角色对应的菜单
        rolesMenusMapper.delete(new QueryWrapper<RolesMenus>().in("menu_id", menuIds));
    }

    /**
     * 递归查找子菜单ID并添加到集合中
     * @param menuIds 菜单ID
     * @param menuAndChildIds 菜单ID及其子菜单ID
     */
    private void findChildIds(List<Long> menuIds, Set<Long> menuAndChildIds) {
        for (Long menuId : menuIds) {
            List<Long> childIds = query().eq("pid", menuId).list().stream().map(Menu::getId).toList();
            if (!childIds.isEmpty()) {
                menuAndChildIds.addAll(childIds);
                findChildIds(childIds, menuAndChildIds);
            }
        }
    }
}
