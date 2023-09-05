package com.yuxuan66.ecmc.common.sms.impl;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import com.yuxuan66.ecmc.common.aliyun.AliCredentialProviderFactory;
import com.yuxuan66.ecmc.common.sms.SmsSender;
import com.yuxuan66.ecmc.modules.system.entity.Config;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;
import darabonba.core.client.ClientOverrideConfiguration;
import jdk.jfr.Name;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 阿里云短信发送器
 *
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Slf4j
@Name("aliyun")
@Component
public class AliSmsSender extends SmsSender {

    /**
     * 发送短信到指定手机号码。
     *
     * @param phone   接收短信的手机号码
     * @param template 短信模板编号
     * @param message 短信消息内容
     * @param scene 短信场景
     */
    public void send(String phone,String template, String message,String scene) {
        try {
            // 从配置中获取短信签名和模板编号
            String signName = ConfigKit.get(CacheKey.SMS_SIGN);

            // 调用阿里云短信服务发送短信
            SendSmsResponse response = sendSmsUsingAliyun(phone, signName, template, message);

            // 消息队列记录短信发送记录
            super.logSmsSentRecord(new Gson().toJson(response),phone,message,signName,template,this.getClass().getAnnotation(Name.class).value(),getClass().getName(),scene);
        } catch (InterruptedException | ExecutionException e) {
            log.error("短信发送失败",e);
            // 发送短信失败时抛出业务异常
            throw new BizException("服务器错误,短信发送失败. ", e.getMessage());
        }
    }

    /**
     * 使用阿里云短信服务发送短信。
     *
     * @param phone    接收短信的手机号码
     * @param signName 短信签名
     * @param template 短信模板编号
     * @param message  短信消息内容
     * @return 发送短信的响应
     * @throws InterruptedException 发送过程中可能的中断异常
     * @throws ExecutionException   发送过程中可能的执行异常
     */
    private SendSmsResponse sendSmsUsingAliyun(String phone, String signName, String template, String message)
            throws InterruptedException, ExecutionException {
        // 创建阿里云短信客户端
        AsyncClient client = createAliyunSmsClient();

        // 构建发送短信请求
        SendSmsRequest sendSmsRequest = buildSendSmsRequest(phone, signName, template, message);

        // 发送短信并异步获取响应
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);

        SendSmsResponse smsResponse = response.get();
        // 关闭短信客户端
        client.close();

        // 返回发送短信的响应
        return smsResponse;
    }

    /**
     * 创建阿里云短信客户端。
     *
     * @return 阿里云短信客户端
     */
    private AsyncClient createAliyunSmsClient() {
        return AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(AliCredentialProviderFactory.get())
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();
    }

    /**
     * 构建发送短信请求。
     *
     * @param phone    接收短信的手机号码
     * @param signName 短信签名
     * @param template 短信模板编号
     * @param message  短信消息内容
     * @return 构建好的发送短信请求
     */
    private SendSmsRequest buildSendSmsRequest(String phone, String signName, String template, String message) {
        return SendSmsRequest.builder()
                .phoneNumbers(phone)
                .signName(signName)
                .templateCode(template)
                .templateParam(message)
                .build();
    }


}
