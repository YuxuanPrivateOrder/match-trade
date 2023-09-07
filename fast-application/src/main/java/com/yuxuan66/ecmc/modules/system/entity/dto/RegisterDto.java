package com.yuxuan66.ecmc.modules.system.entity.dto;

import com.yuxuan66.ecmc.modules.system.entity.dto.BaseSmsCaptchaDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 账号注册需要的信息
 * @author Sir丶雨轩
 * @since 2023/9/6
 */
@Data
@RequiredArgsConstructor
public class RegisterDto extends BaseSmsCaptchaDto {

    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;

}
