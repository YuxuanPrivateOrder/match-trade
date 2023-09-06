package com.yuxuan66.ecmc.modules.system.rest;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.map.MapUtil;
import com.yuxuan66.ecmc.modules.system.entity.User;
import com.yuxuan66.ecmc.modules.system.entity.dto.LoginDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.UpdatePasswordDto;
import com.yuxuan66.ecmc.modules.system.entity.query.UserQuery;
import com.yuxuan66.ecmc.modules.system.service.UserService;
import com.yuxuan66.ecmc.support.base.BaseController;
import com.yuxuan66.ecmc.support.base.resp.Ps;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sir丶雨轩
 * @since 2022/12/6
 */
@RequestMapping(path = "/user")
@RestController
public class UserController extends BaseController<UserService> {



    /**
     * 分页查询用户列表
     * @param userQuery 查询条件
     * @return 用户列表
     */
    @GetMapping
    public Ps list(UserQuery userQuery){
        return baseService.list(userQuery);
    }

    /**
     * 导出用户列表
     * @param userQuery 查询条件
     */
    @GetMapping(path = "/download")
    public void download(UserQuery userQuery){
        baseService.download(userQuery);
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping(path = "/getUserInfo")
    public Rs getUserInfo(){
        return Rs.ok(baseService.getUserInfo());
    }

    /**
     * 获取当前登录用户的权限代码
     * @return 权限代码
     */
    @GetMapping(path = "/getPermCode")
    public Rs getPermCode(){
        return Rs.ok(baseService.getPermCode());
    }

    /**
     * 判断指定字段是否有用户使用
     *
     * @param field 字段
     * @param value 数据
     * @param id    id
     * @return 是否使用
     */
    @SaIgnore
    @GetMapping(path = "/checkFieldExist/{field}/{value}")
    public Rs checkFieldExist(@PathVariable String field,@PathVariable String value, Long id) {
        return Rs.ok(baseService.checkFieldExist(field,value,id));
    }
    /**
     * 添加一个用户
     *
     * @param user 用户
     */
    @PostMapping
    public Rs add(@RequestBody User user){
        baseService.add(user);
        return Rs.ok();
    }

    /**
     * 编辑一个用户
     * @param user 用户
     */
    @PutMapping
    public Rs edit(@RequestBody User user){
        baseService.edit(user);
        return Rs.ok();
    }

    /**
     * 删除用户
     * @param ids 用户id
     */
    @DeleteMapping
    public Rs del(@RequestBody Set<Long> ids){
        baseService.del(ids);
        return Rs.ok();
    }

}
