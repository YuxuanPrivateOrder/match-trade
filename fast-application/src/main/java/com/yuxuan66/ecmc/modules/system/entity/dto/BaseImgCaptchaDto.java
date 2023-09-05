package com.yuxuan66.ecmc.modules.system.entity.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 需要图片验证码的dto
 * @author Sir丶雨轩
 * @since 2023/9/5
 */
@Data
@RequiredArgsConstructor
public class BaseImgCaptchaDto {

    /**
     * uuid
     */
    private String uuid;
    /**
     * 图片验证码
     */
    private String imgCode;
}
