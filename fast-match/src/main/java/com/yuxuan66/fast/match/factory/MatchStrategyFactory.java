package com.yuxuan66.fast.match.factory;

import com.yuxuan66.fast.match.enums.OrderType;
import com.yuxuan66.fast.match.service.match.AbstractOrderMatchService;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 撮合服务工厂
 * @author Sir丶雨轩
 * @since 2023/9/11
 */
public class MatchStrategyFactory {

    /**
     * 撮合服务集合
     */
    private static final Map<Integer, AbstractOrderMatchService> services = new ConcurrentHashMap<>();


    /**
     * 获取撮合服务
     * @param orderType 订单类型
     * @return 撮合服务
     */
    public static AbstractOrderMatchService getByOrderType(OrderType orderType) {
        return services.get(orderType.getCode());
    }

    /**
     * 注册撮合服务
     * @param orderType 订单类型
     * @param orderMatchService 撮合服务
     */
    public static void register(OrderType orderType, AbstractOrderMatchService orderMatchService) {
        Assert.notNull(orderType, "orderType can't be null");
        services.put(orderType.getCode(), orderMatchService);
    }
}
