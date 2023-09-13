package com.yuxuan66.fast.match.service;

import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.IdUtil;
import com.yuxuan66.ecmc.cache.redis.RedisKit;
import com.yuxuan66.ecmc.common.mq.RabbitMQProducer;
import com.yuxuan66.fast.match.consts.MatchConst;
import com.yuxuan66.fast.match.entity.Residue;
import com.yuxuan66.fast.match.entity.dto.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 订单服务
 *
 * @author Sir丶雨轩
 * @since 2023/9/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RedisKit redis;

    /**
     * 添加一个新订单 提交到消息队列<br/>
     *
     * @param order 订单信息
     */
    public void addOrder(Order order) {
        TimeInterval interval = new TimeInterval();

        // 给订单添加一个唯一标识，用于后续对订单进行校验
        order.setUuid(IdUtil.simpleUUID());
        // 总金额 = 单价 * 数量
        order.setSumPrice(order.getPrice() * order.getNum());
        order.setNoDealNum(order.getNum());
        order.setNoDealAmount(order.getNoDealNum() * order.getPrice());
        order.setDealNum(0L);
        order.setDealAmount(0L);

        if (order.isBuy()) {
            Residue residue = new Residue();
            residue.setTotal(BigDecimal.valueOf(order.getSumPrice()));
            residue.setSurplus(BigDecimal.ZERO);
            residue.insert();
        }
        // 锁住正在运行的订单，加锁时间180s
        redis.lock(order.getUuid(), 180L);
        // 提交订单到消息队列
        RabbitMQProducer.sendMessage(MatchConst.INPUT_NEW_ORDER, order);
        // 记录日志
        log.info("添加订单耗时：{}ms", interval.intervalMs());

    }
}
