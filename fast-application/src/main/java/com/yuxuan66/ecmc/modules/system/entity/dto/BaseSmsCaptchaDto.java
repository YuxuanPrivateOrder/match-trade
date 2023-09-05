package com.yuxuan66.ecmc.modules.system.entity.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Sir丶雨轩
 * @since 2023/9/5
 */
@Data
@RequiredArgsConstructor
public class BaseSmsCaptchaDto  extends BaseImgCaptchaDto{

    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String smsCode;
}
