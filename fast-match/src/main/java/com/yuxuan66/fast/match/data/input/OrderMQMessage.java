package com.yuxuan66.fast.match.data.input;

import com.alibaba.fastjson.JSONObject;
import com.yuxuan66.fast.match.consts.MatchConst;
import com.yuxuan66.fast.match.entity.dto.OkOrderResult;
import com.yuxuan66.fast.match.entity.dto.Order;
import com.yuxuan66.fast.match.enums.OrderType;
import com.yuxuan66.fast.match.factory.MatchStrategyFactory;
import com.yuxuan66.fast.match.service.match.AbstractOrderMatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 接收订单MQ消息
 * @author Sir丶雨轩
 * @since 2023/9/13
 */
@Slf4j
@Component
public class OrderMQMessage {

    /**
     * 新订单
     * @param order 订单数据
     */
    public void newOrder(String orderStr) {
       try{
           log.debug("接收到新订单 {}",orderStr);
           Order order = JSONObject.parseObject(orderStr,Order.class);

           // 根据订单类型获取撮合策略
           AbstractOrderMatchService service = MatchStrategyFactory.getByOrderType(OrderType.of(order.getOrderType()));
           if(service == null){
               log.error("未找到订单类型为 {} 的撮合策略, 订单UUID: {}",order.getOrderType(),order.getUuid());
               return;
           }
           // 执行撮合策略
           service.match(order);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public void okOrder(String orderStr) {
        try{
            log.debug("撮合成功订单 {}",orderStr);
            OkOrderResult okOrderResult = JSONObject.parseObject(orderStr, OkOrderResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
