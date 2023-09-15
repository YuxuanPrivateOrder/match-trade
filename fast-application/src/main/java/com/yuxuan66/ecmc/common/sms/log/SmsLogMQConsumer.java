package com.yuxuan66.ecmc.common.sms.log;

import com.yuxuan66.ecmc.consts.QueueConst;
import org.springframework.stereotype.Component;

/**
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Component
public class SmsLogMQConsumer {

    public void processMessage(String message) {
        // 处理接收到的消息
        System.out.println("Received message: " + message);
    }
}
