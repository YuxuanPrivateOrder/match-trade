package com.yuxuan66.ecmc.support.interceptor;

import com.yuxuan66.ecmc.support.exception.BizException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 演示模式拦截器, 避免影响演示的数据
 *
 * @author Sir丶雨轩
 * @since 2023/9/11
 */
@AllArgsConstructor
public class PreviewInterceptor implements HandlerInterceptor {

    /**
     * 是否是演示模式
     */
    private final Boolean preview;
    private final String[] whitelist = {"/login*"};

    /**
     * 判断指定请求是否是白名单内
     * @param request 请求信息
     * @return 是否是白名单
     */
    private boolean isRequestInWhitelist(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();

        for (String pattern : whitelist) {
            String regex = pattern.replace("*", ".*");
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(requestUrl);

            if (m.matches()) {
                return true; // 如果找到匹配项，则认为请求地址在白名单中
            }
        }

        return false; // 如果没有找到匹配项，则认为请求地址不在白名单中
    }

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        if (preview) {
            // 演示模式下，不允许访问非Get
            if (!request.getMethod().equalsIgnoreCase("get") && !isRequestInWhitelist(request)) {
                throw new BizException("演示模式下,不允许操作");
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
