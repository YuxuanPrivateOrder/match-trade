package com.yuxuan66.ecmc.common.mq;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * RabbitMQ 生产者，用于发送消息到 RabbitMQ 队列或交换机。
 */
public class RabbitMQProducer {


    /**
     * 发送消息到指定的 RabbitMQ 队列或交换机。
     *
     * @param queueName 目标队列或交换机的名称
     * @param message   要发送的消息内容
     */
    public static void sendMessage(String queueName, Object message) {
        SpringUtil.getBean(RabbitTemplate.class).convertAndSend(queueName, JSONObject.toJSONString(message));
    }

}
