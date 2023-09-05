package com.yuxuan66.ecmc.modules.system.entity.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Sir丶雨轩
 * @since 2023/9/5
 */
@Data
@RequiredArgsConstructor
public class UpdatePasswordDto extends BaseSmsCaptchaDto {


    /**
     * 新密码
     */
    private String newPassword;

}
