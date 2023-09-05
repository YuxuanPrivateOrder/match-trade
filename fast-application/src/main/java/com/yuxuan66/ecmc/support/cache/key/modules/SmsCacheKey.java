package com.yuxuan66.ecmc.support.cache.key.modules;

/**
 * 短信相关配置key
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
public interface SmsCacheKey {

    /**
     * 短信运营商
     */
    String SMS_OPERATOR = "sms.operator";

    /**
     * 短信验证码的位数
     */
    String SMS_LEN = "sms.len";

    /**
     * 短信签名
     */
    String SMS_SIGN = "sms.sign";



    /**
     * 阿里云的短信模版ID-登陆
     */
    String SMS_TEMPLATE_ALI_LOGIN = "sms.template.ali.login";
    /**
     * 阿里云的短信模版ID-注册
     */
    String SMS_TEMPLATE_ALI_REGISTER = "sms.template.ali.register";
    /**
     * 阿里云的短信模版ID-修改密码
     */
    String SMS_TEMPLATE_ALI_PASSWORD = "sms.template.ali.password";
}
