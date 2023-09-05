package com.yuxuan66.ecmc.common.sms.log.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * 短信发送日志实体类
 */
@Data
@AllArgsConstructor
public class SmsSendLog {

    /**
     * 响应信息
     */
    private String resp;

    /**
     * 接收短信的手机号码
     */
    private String phone;

    /**
     * 短信消息内容
     */
    private String message;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信模板编号
     */
    private String templateId;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 实现类名称
     */
    private String implClassName;
    /**
     * 发送场景
     */
    private String scene;
}
