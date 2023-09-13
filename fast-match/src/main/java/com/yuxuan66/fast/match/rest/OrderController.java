package com.yuxuan66.fast.match.rest;

import cn.dev33.satoken.annotation.SaIgnore;
import com.yuxuan66.ecmc.support.base.BaseController;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import com.yuxuan66.fast.match.entity.dto.Order;
import com.yuxuan66.fast.match.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@SaIgnore
@RequestMapping(path = "/order")
@RestController
public class OrderController extends BaseController<OrderService> {


    /**
     * 添加一个新订单 提交到消息队列<br/>
     *
     * @param order 订单信息
     */
    @PostMapping
    public Rs addOrder(@RequestBody Order order){
        baseService.addOrder(order);
        return Rs.ok();
    }
}
