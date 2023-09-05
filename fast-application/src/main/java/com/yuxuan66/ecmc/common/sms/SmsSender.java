package com.yuxuan66.ecmc.common.sms;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.yuxuan66.ecmc.common.mq.RabbitMQProducer;
import com.yuxuan66.ecmc.common.sms.log.entity.SmsSendLog;

/**
 * 短信发送器, 实现了短信日志记录功能
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
public abstract class SmsSender {

    /**
     * 发送短信
     * @param phone 手机号
     * @param template 短信模板编号
     * @param message 短信内容
     * @param scene 短信场景
     */
   public abstract void send(String phone,String template, String message,String scene);

    /**
     * 记录短信发送记录
     * @param resp 发送短信的响应
     * @param phone 手机号
     * @param message 短信内容
     * @param signName 短信签名
     * @param template 短信模板编号
     * @param operator 短信运营商
     * @param implClassName 短信发送器实现类名
     */
    protected void logSmsSentRecord(String resp, String phone, String message, String signName, String template, String operator, String implClassName,String scene) {
        // 记录短信发送记录
        RabbitMQProducer.sendMessage(SmsConst.SMS_LOG_QUEUE, JSONObject.toJSONString(new SmsSendLog(resp, phone, message, signName, template, operator, implClassName,scene)));
    }
}
