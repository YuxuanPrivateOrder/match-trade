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
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取response对象
     *
     * @return response
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * 根据当前请求判断是否是移动端
     *
     * @return 是否是移动端
     */
    public static boolean isMobile() {
        return UserAgentUtil.parse(getRequest().getHeader("User-Agent")).isMobile();
    }


    /**
     * 从HttpServletRequest对象中获取客户端的真实IP地址。
     *
     * @return 客户端的真实IP地址
     */
    public static String getRemoteIP() {
        HttpServletRequest request = getRequest();
        // 获取 X-Forwarded-For 标头的值，通常包含一个或多个IP地址
        String ip = request.getHeader("x-forwarded-for");

        // 如果 X-Forwarded-For 标头不存在或为空，则尝试获取 Proxy-Client-IP 标头
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        // 如果 Proxy-Client-IP 标头不存在或为空，则尝试获取 WL-Proxy-Client-IP 标头
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        // 如果以上标头都不存在或为空，则直接获取客户端IP地址
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个IP地址的情况，通常第一个IP是真实客户端IP
        if (ip != null && ip.indexOf(",") > 0) {
            String[] parts = ip.split(",");
            for (String part : parts) {
                if (!part.isEmpty() && !"unknown".equalsIgnoreCase(part.trim())) {
                    ip = part.trim();
                    break;
                }
            }
        }

        // 如果IP地址是IPv6的本地回环地址，则替换为IPv4的本地回环地址
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }


}
