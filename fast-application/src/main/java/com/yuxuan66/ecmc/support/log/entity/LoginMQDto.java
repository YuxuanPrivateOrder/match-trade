package com.yuxuan66.ecmc.support.log.entity;

import com.yuxuan66.ecmc.support.log.enums.LoginMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登陆日志保存dto
 *
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginMQDto {
    /**
     * 登陆Token
     */
    private String loginToken;
    /**
     * 登陆失败错误原因
     */
    private String errorMsg;
    /**
     * 登陆账号
     */
    private String loginAccount;
    /**
     * 登陆手机号
     */
    private String loginPhone;
    /**
     * 登陆IP
     */
    private String loginIp;
    /**
     * 是否登陆成功
     */
    private boolean loginSuccess;

    /**
     * 请求参数
     */
    private String params;
    /**
     * 请求头
     */
    private String header;


}
