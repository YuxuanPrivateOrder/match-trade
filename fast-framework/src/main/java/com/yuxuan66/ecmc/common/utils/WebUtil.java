package com.yuxuan66.ecmc.common.utils;

import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Sir丶雨轩
 * @since 2022/12/6
 */
public class WebUtil {

    /**
     * 获取request对象
     * @return request
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
    /**
     * 获取response对象
     * @return response
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 根据当前请求判断是否是移动端
     * @return 是否是移动端
     */
    public static boolean isMobile(){
        return UserAgentUtil.parse(getRequest().getHeader("User-Agent")).isMobile();
    }


}
