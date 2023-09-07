package com.yuxuan66.ecmc.support.log;

import com.alibaba.fastjson.JSONObject;
import com.yuxuan66.ecmc.common.mq.RabbitMQProducer;
import com.yuxuan66.ecmc.common.utils.AopUtil;
import com.yuxuan66.ecmc.common.utils.WebUtil;
import com.yuxuan66.ecmc.common.utils.entity.IPInfo;
import com.yuxuan66.ecmc.common.utils.ip.IPUtil;
import com.yuxuan66.ecmc.consts.QueueConst;
import com.yuxuan66.ecmc.modules.system.entity.dto.LoginDto;
import com.yuxuan66.ecmc.modules.system.entity.dto.SmsLoginDto;
import com.yuxuan66.ecmc.support.exception.BizException;
import com.yuxuan66.ecmc.support.log.annotation.LogLogin;
import com.yuxuan66.ecmc.support.log.entity.LoginMQDto;
import com.yuxuan66.ecmc.support.log.enums.LoginMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

/**
 * 系统内日志组件，使用AOP拦截指定注解
 *
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
@Aspect
@Component
public class FastLog {

    /**
     * 拦截登陆日志
     *
     * @param joinPoint 切点
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around("@annotation(com.yuxuan66.ecmc.support.log.annotation.LogLogin)")
    public Object logLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AopUtil.getMethod(joinPoint);
        LogLogin logLoginAnnotation = method.getAnnotation(LogLogin.class);
        // 登陆后返回Token
        String loginToken = null;
        // 是否登陆成功
        // 登陆账号
        String loginAccount = null;
        String loginPhone = null;
        // 登陆IP
        String loginIp = WebUtil.getRemoteIP();
        // 拼接需要的数据，发送消息到mq，后台保存日志，修改用户登陆信息等等
        // 获取方法参数
        Object loginParams = joinPoint.getArgs()[0];
        // 获取登陆账号
        switch (logLoginAnnotation.value()) {
            case PASSWORD -> {
                if (loginParams instanceof LoginDto loginDto) {
                    loginAccount = loginDto.getUsername();
                }
            }
            case PHONE -> {
                if (loginParams instanceof SmsLoginDto loginDto) {
                    loginPhone = loginDto.getPhone();
                }
            }
        }
        String params = JSONObject.toJSONString(loginParams);
        // 处理请求头
        Enumeration<String> headerNames =  WebUtil.getRequest().getHeaderNames();
        StringBuilder headersString = new StringBuilder();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = WebUtil.getRequest().getHeaders(headerName);

            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                headersString.append(headerName).append(": ").append(headerValue).append("\n");
            }
        }
        try {
            loginToken = joinPoint.proceed().toString();
            RabbitMQProducer.sendMessage(QueueConst.LOG_LOGIN, new LoginMQDto(loginToken, null, loginAccount, loginPhone, loginIp, true,params, headersString.toString()));
            return loginToken;
        } catch (BizException e) {
            RabbitMQProducer.sendMessage(QueueConst.LOG_LOGIN, new LoginMQDto(loginToken, e.getMessage(), loginAccount, loginPhone, loginIp, false,params,headersString.toString()));
            throw e;
        }


    }

}
