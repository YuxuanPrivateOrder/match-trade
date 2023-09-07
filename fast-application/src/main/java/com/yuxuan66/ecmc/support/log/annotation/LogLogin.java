package com.yuxuan66.ecmc.support.log.annotation;

import com.yuxuan66.ecmc.support.log.enums.LoginMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志模块登陆
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogLogin {

    /**
     * 登陆方法
     * @return 登陆方法
     */
    LoginMethod value() default LoginMethod.PASSWORD;



}
