package com.yuxuan66.ecmc.modules.jys.rest;

import com.yuxuan66.ecmc.modules.jys.service.QtUsersService;
import com.yuxuan66.ecmc.support.base.BaseController;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2023/9/15
 */
@RestController
@RequestMapping(path = "/qtUsers")
public class QtUsersController extends BaseController<QtUsersService> {

    /**
     * 查询用户数据
     * @param search 查询条件
     * @return 用户数据
     */
    @GetMapping(path = "/data/{search}")
    public Rs data(@PathVariable String search){
        return Rs.ok(baseService.data(search));
    }
}
