package com.yuxuan66.fast.match;

import cn.hutool.core.util.StrUtil;
import com.yuxuan66.ecmc.cache.redis.RedisKit;
import com.yuxuan66.ecmc.common.mq.RabbitMQProducer;
import com.yuxuan66.fast.match.consts.MatchConst;
import com.yuxuan66.fast.match.entity.dto.OkOrderResult;
import com.yuxuan66.fast.match.entity.dto.Order;
import com.yuxuan66.fast.match.enums.OrderState;
import com.yuxuan66.fast.match.enums.OrderType;
import com.yuxuan66.fast.match.service.OrderBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心撮合匹配引擎
 *
 * @author Sir丶雨轩
 * @since 2023/9/11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEngine {

    private final RedisKit redis;
    private final OrderBookService orderBookService;


    /**
     * 是否可以交易
     * @param taker 发起订单
     * @param maker 待撮合订单
     * @return 是否可以交易
     */
    private boolean isTransaction(Order taker,Order maker){
        if (taker.getOrderType() == OrderType.MTC.getCode()) {
            // taker price source maker price
            taker.setPrice(maker.getPrice());
            // 是市价买
            if (taker.isBuy()) {
                taker.setNoDealNum(Math.floorDiv(taker.getNoDealAmount(), maker.getPrice()));
            }
            return true;
        }
        // is buy
        if (taker.isBuy()) {
            return Long.compare(taker.getPrice(), maker.getPrice()) > -1;
        } else {
            return Long.compare(taker.getPrice(), maker.getPrice()) < 1;
        }
    }

    /**
     * 订单撮合
     *
     * @param order 订单
     * @return 撮合后订单
     */
    public Order match(Order order) {
        // 获取当前股票的订单薄，买单类型跟当前相反，按照价格排序获取前100条
        List<Order> orderList = orderBookService.getOrders(order.getKey(), !order.isBuy(), 100);
        // 撮合订单
        for (Order matchOrder : orderList) {
            /*检测锁, 正在进行的话, 切换订单*/
            if (StrUtil.isNotBlank(redis.get("match:" + matchOrder.getUuid()))){
                log.info("订单: {} ,正在进行撮合, 切换订单",matchOrder.getUuid());
                continue;
            }
            // 判断订单是否可以交易
            if (!isTransaction(order, matchOrder)) {
                return order;
            }
            // 给正在处理订单加锁
            redis.lock(order.getUuid(),60L);
            int contrast = Long.compare(order.getNoDealNum(), matchOrder.getNoDealNum());
            // 成交数量
            long dealNum = contrast < 0 ? order.getNoDealNum() : matchOrder.getNoDealNum();
            // 处理撮合结果
            makerHandle(matchOrder, contrast, dealNum);

            OkOrderResult okOrderResult = new OkOrderResult(order,matchOrder,dealNum);

            // 处理 matchOrder的缓存
            List<Order> bookServiceOrders = orderBookService.getOrders(order.getKey());
            for (Order serviceOrder : bookServiceOrders) {
                if(matchOrder.getOrderState() == OrderState.ALL_DEAL){
                    // 数组中删除
                    bookServiceOrders.remove(matchOrder);
                    break;
                }
                serviceOrder.setOrderState(matchOrder.getOrderState());
                serviceOrder.setDealNum(matchOrder.getDealNum());
                serviceOrder.setNoDealNum(matchOrder.getNoDealNum());
                serviceOrder.setDealAmount(matchOrder.getDealAmount());
                serviceOrder.setNoDealAmount(matchOrder.getNoDealAmount());
            }
            // 处理 order的缓存
            takerHandle(order, matchOrder.getPrice(), dealNum, contrast);
            RabbitMQProducer.sendMessage(MatchConst.OK_ORDER,okOrderResult);
            // 释放锁
            if (order.getOrderState() == OrderState.ALL_DEAL) {
                break;
            }
        }

        return order;
    }


    /**
     *  处理taker撮合结果
     *
     * @param taker   计价单
     * @param contrast taker对比maker的数量
     * @param dealNum 交易数量
     */
    public static void takerHandle(Order taker, long dealPrice, long dealNum, int contrast) {
        if (contrast > 0) {
            // taker 有余
            taker.setOrderState(OrderState.SOME_DEAL);
        } else if (contrast == 0) {
            // 都无剩余
            taker.setOrderState(OrderState.ALL_DEAL);
            // 发送maker，和trade
        } else {
            // maker有剩余
            taker.setOrderState(OrderState.ALL_DEAL);
            // 发送maker，和trade
        }
        // calculate taker
        taker.setDealNum(taker.getDealNum() + dealNum);
        taker.setNoDealNum(taker.getNoDealNum() - dealNum);
        taker.setDealAmount(taker.getDealAmount() + (dealPrice * dealNum));
        taker.setNoDealAmount(taker.getNoDealAmount() - (dealPrice * dealNum));
    }


    /**
     *  处理maker撮合结果
     *
     * @param matchOrder  订单
     * @param contrast taker对比maker的数量
     * @param dealNum 交易数量
     */
    private static void makerHandle(Order matchOrder, int contrast, long dealNum) {
        if (contrast > 0) {
            // taker 有余
            matchOrder.setOrderState(OrderState.ALL_DEAL);
        } else if (contrast == 0) {
            // 都无剩余
            matchOrder.setOrderState(OrderState.ALL_DEAL);
            // 发送maker，和trade
        } else {
            // maker有剩余
            matchOrder.setOrderState(OrderState.SOME_DEAL);
            // 发送maker，和trade
        }
        // calculate maker
        matchOrder.setDealNum(matchOrder.getDealNum() + dealNum);
        matchOrder.setNoDealNum(matchOrder.getNoDealNum() - dealNum);
        matchOrder.setDealAmount(matchOrder.getDealAmount() + (matchOrder.getPrice() * dealNum));
        matchOrder.setNoDealAmount(matchOrder.getNoDealAmount() - (matchOrder.getPrice() * dealNum));
    }


}
