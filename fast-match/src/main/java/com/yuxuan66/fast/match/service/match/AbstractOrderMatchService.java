package com.yuxuan66.fast.match.service.match;

import com.yuxuan66.ecmc.cache.redis.RedisKit;
import com.yuxuan66.fast.match.entity.dto.Order;

import javax.annotation.Resource;

/**
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
public abstract class AbstractOrderMatchService {

    @Resource
    private  RedisKit redis;

    public final Order match(Order order) {
        // step1: 操作对手盘及产生trade信息
        Order matchOrder = this.startMatch(order);
        redis.unlock(order.getUuid());
        // step2: 操作当前订单盘口
        return this.afterTakerMatch(matchOrder);
    }

    /**
     * 开始撮合
     * 操作对手盘，及产生trade信息
     *
     * @param matchOrder 撮合订单
     * @return 撮合订单
     */
    public abstract Order startMatch(Order matchOrder);

    /**
     * 撮合后
     * 操作当前订单盘口
     *
     * @param matchOrder 撮合订单
     * @return 撮合订单
     */
    public abstract Order afterTakerMatch(Order matchOrder);
}
