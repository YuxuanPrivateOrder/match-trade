package com.yuxuan66.ecmc.modules.system.entity.dto;

import lombok.Data;

/**
 * 手机号登陆信息
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Data
public class SmsLoginDto extends BaseImgCaptchaDto{

    /**
     * 手机号
     */
    private String phone;
    /**
     * 验证码
     */
    private String code;
}
