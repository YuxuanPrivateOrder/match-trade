package com.yuxuan66.fast.match.service.match.impl;

import com.yuxuan66.fast.match.entity.dto.Order;
import com.yuxuan66.fast.match.enums.OrderType;
import com.yuxuan66.fast.match.factory.MatchStrategyFactory;
import com.yuxuan66.fast.match.service.match.AbstractOrderMatchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 限价订单匹配
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Component
public class GtcMatchService extends AbstractOrderMatchService implements InitializingBean {
    @Override
    public Order startMatch(Order matchOrder) {
        return null;
    }

    @Override
    public Order afterTakerMatch(Order matchOrder) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MatchStrategyFactory.register(OrderType.GTC,this);
    }
}
