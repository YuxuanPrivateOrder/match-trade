package com.yuxuan66.fast.match.service.match.impl;

import com.yuxuan66.fast.match.MatchEngine;
import com.yuxuan66.fast.match.entity.dto.Order;
import com.yuxuan66.fast.match.enums.OrderState;
import com.yuxuan66.fast.match.enums.OrderType;
import com.yuxuan66.fast.match.factory.MatchStrategyFactory;
import com.yuxuan66.fast.match.service.OrderBookService;
import com.yuxuan66.fast.match.service.match.AbstractOrderMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Component
@RequiredArgsConstructor
public class MtcMatchService extends AbstractOrderMatchService implements InitializingBean {

    private final OrderBookService orderBookService;
    private final MatchEngine matchEngine;

    /**
     * 开始撮合订单
     * @param matchOrder 撮合订单
     * @return 撮合后订单数据
     */
    @Override
    public Order startMatch(Order matchOrder) {
        return matchEngine.match(matchOrder);
    }

    /**
     * 撮合完成后的操作
     * @param matchOrder 撮合订单
     * @return 撮合订单
     */
    @Override
    public Order afterTakerMatch(Order matchOrder) {
        // 如果撮合后订单还是委托中或部分成交，添加至订单薄
        if(matchOrder.getOrderState() == OrderState.SOME_DEAL || matchOrder.getOrderState() == OrderState.ORDER){
            // TODO 维护行情中心的数据
            orderBookService.addOrder(matchOrder);
        }

        return matchOrder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MatchStrategyFactory.register(OrderType.MTC,this);
    }
}
