package com.yuxuan66.ecmc.modules.system.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.pig4cloud.captcha.SpecCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import com.yuxuan66.ecmc.common.sms.SmsScene;
import com.yuxuan66.ecmc.common.sms.SmsSenderFactory;
import com.yuxuan66.ecmc.modules.system.entity.dto.SendSmsDto;
import com.yuxuan66.ecmc.support.base.resp.Rs;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.StaticComp;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码服务类
 *
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    /**
     * 短信发送器工厂
     */
    private final SmsSenderFactory smsSenderFactory;

    /**
     * 生成图片验证码返回base64和uuid
     * @return base64和uuid
     */
    public Map<String,Object> imgCaptcha(){
        Captcha captcha = new SpecCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(4);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = IdUtil.simpleUUID();
        // 保存
        Map<String, Object> imgResult = new HashMap<>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};

        StaticComp.redisKit.set(CacheKey.CAPTCHA_IMG_PRE + uuid, result, ConfigKit.get(CacheKey.CAPTCHA_EXPIRE_TIME, Long.class));

        return imgResult;
    }

    /**
     * 校验图片验证码
     * @param uuid uuid
     * @param imgCode 图片验证码
     */
    public void checkImgCaptcha(String uuid,String imgCode){
        if(!StaticComp.redisKit.exist(CacheKey.CAPTCHA_IMG_PRE + uuid)){
            log.debug("图片验证码已过期,uuid:{}",uuid);
            throw new BizException("图片验证码输入错误");
        }
        String code = StaticComp.redisKit.getAndDel(CacheKey.CAPTCHA_IMG_PRE + uuid);
        if(!code.equalsIgnoreCase(imgCode)){
            log.debug("图片验证码输入错误,uuid:{},code:{},inputCode:{}",uuid,code,imgCode);
            throw new BizException("图片验证码输入错误");
        }
    }

    /**
     * 生成指定位数的短信验证码。
     *
     * @return 生成的验证码
     */
    public String generateSMSVerificationCode() {
        // 获取短信验证码的位数
        int captchaLength = ConfigKit.get(CacheKey.SMS_LEN, Integer.class);
        // 生成指定位数的验证码
        return String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, captchaLength - 1)));
    }
    /**
     * 校验图片验证码并生成发送的Map信息
     * @param sendSmsDto 手机号和图片验证码
     * @param templateKey 模版key
     * @param smsScene 短信场景
     */
    public void sendSms(SendSmsDto sendSmsDto,String templateKey,SmsScene smsScene) {
        checkImgCaptcha(sendSmsDto.getUuid(), sendSmsDto.getImgCode());
        // 根据配置生成指定位数的验证码
        String code = generateSMSVerificationCode();
        Map<String, Object> templateData = MapUtil.newHashMap(2);
        templateData.put("code", code);
        templateData.put("product", ConfigKit.get(CacheKey.PROJECT_NAME));
        smsSenderFactory.getSmsSender().send(sendSmsDto.getPhone(), ConfigKit.get(templateKey), JSONObject.toJSONString(templateData), smsScene.getDescription());
        // 保存此次发送记录
        StaticComp.redisKit.set(CacheKey.CAPTCHA_PHONE_LOGIN_PRE + sendSmsDto.getPhone(), templateData.get("code").toString(),ConfigKit.get(CacheKey.CAPTCHA_EXPIRE_TIME,Long.class));
    }




}
