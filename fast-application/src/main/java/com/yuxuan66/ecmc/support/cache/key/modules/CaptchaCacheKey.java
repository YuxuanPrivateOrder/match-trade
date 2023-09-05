package com.yuxuan66.ecmc.support.cache.key.modules;

/**
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
public interface CaptchaCacheKey {

    /**
     * 验证码-手机-注册
     */
    String CAPTCHA_PHONE_REGISTER_PRE = "captcha:phone:register:";

    /**
     * 验证码-手机-登陆
     */
    String CAPTCHA_PHONE_LOGIN_PRE = "captcha:phone:login:";

    /**
     * 验证码-图片验证码
     */
    String CAPTCHA_IMG_PRE = "captcha:img:";


    /**
     * 验证码过期时间
     */
    String CAPTCHA_EXPIRE_TIME = "captcha.expire";}
