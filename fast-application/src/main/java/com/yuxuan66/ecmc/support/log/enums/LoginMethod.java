package com.yuxuan66.ecmc.support.log.enums;

import lombok.Getter;

/**
 * 定义登陆方法枚举
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Getter
public enum LoginMethod {
    PASSWORD("密码登陆"),
    PHONE("手机验证码登陆");
    private final String desc;

    // 生成构造方法
    LoginMethod(String desc) {
        this.desc = desc;
    }
}
