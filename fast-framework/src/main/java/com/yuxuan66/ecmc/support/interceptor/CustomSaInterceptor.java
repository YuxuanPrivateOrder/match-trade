package com.yuxuan66.ecmc.support.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;

/**
 * @author Sir丶雨轩
 * @since 2022/12/6
 */
public class CustomSaInterceptor extends SaInterceptor {

    /**
     * 自定义SaToken的权限拦截
     */
    public CustomSaInterceptor() {
        super(handler -> {
            SaRouter.match("/**")
                    .notMatch("/login*")
                    .notMatch("*.*")
                    // 验证码相关的放开鉴权
                    .notMatch("/captcha/*")
                    // 放开公开的API
                    .check(r -> StpUtil.checkLogin());
        });
    }
}
