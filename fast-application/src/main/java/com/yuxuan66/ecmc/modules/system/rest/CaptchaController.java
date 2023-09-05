package com.yuxuan66.ecmc.modules.system.rest;

import cn.hutool.core.lang.Assert;
import com.yuxuan66.ecmc.common.sms.SmsScene;
import com.yuxuan66.ecmc.modules.system.entity.dto.SendSmsDto;
import com.yuxuan66.ecmc.modules.system.service.CaptchaService;
import com.yuxuan66.ecmc.modules.system.service.UserService;
import com.yuxuan66.ecmc.support.base.BaseController;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 验证码
 *
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@RestController
@RequestMapping(path = "/captcha")
@RequiredArgsConstructor
public class CaptchaController extends BaseController<CaptchaService> {

    private final UserService userService;


    /**
     * 生成图片验证码返回base64和uuid
     *
     * @return base64和uuid
     */
    @GetMapping(path = "/imgCaptcha")
    public Rs imgCaptcha() {
        return Rs.ok(baseService.imgCaptcha());
    }

    /**
     * 发送登陆系统的验证码
     *
     * @param sendSmsDto 手机号和图片验证码
     */
    @GetMapping(path = "/smsLogin")
    public Rs smsLogin(SendSmsDto sendSmsDto) {
        Assert.isTrue(userService.checkFieldExist("phone", sendSmsDto.getPhone(), null), () -> BizException.of("该手机号尚未注册"));
        baseService.sendSms(sendSmsDto, CacheKey.SMS_TEMPLATE_ALI_LOGIN, SmsScene.LOGIN);
        return Rs.ok();
    }

    /**
     * 发送注册验证码
     *
     * @param sendSmsDto 手机号和图片验证码
     * @return Rs
     */
    @GetMapping(path = "/smsRegister")
    public Rs smsRegister(SendSmsDto sendSmsDto) {
        Assert.isFalse(userService.checkFieldExist("phone", sendSmsDto.getPhone(), null), () -> BizException.of("该手机号已经注册"));
        baseService.sendSms(sendSmsDto, CacheKey.SMS_TEMPLATE_ALI_REGISTER, SmsScene.REGISTER);
        return Rs.ok();
    }

    /**
     * 发送找回密码验证码
     *
     * @param sendSmsDto 手机号和图片验证码
     * @return Rs
     */
    @GetMapping(path = "/smsChangePassword")
    public Rs smsChangePassword(SendSmsDto sendSmsDto) {
        // 判断用户是否存在
        Assert.isTrue(userService.checkFieldExist("phone", sendSmsDto.getPhone(), null), () -> BizException.of("该手机号尚未注册"));
        baseService.sendSms(sendSmsDto, CacheKey.SMS_TEMPLATE_ALI_PASSWORD, SmsScene.PASSWORD);
        return Rs.ok();
    }
}
