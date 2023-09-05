package com.yuxuan66.ecmc.common.sms;

import cn.hutool.extra.spring.SpringUtil;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;
import jdk.jfr.Name;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
@Component
public class SmsSenderFactory {

    /**
     * 短信发送器提供者
     */
    private final Map<String, SmsSender> smsSenders = new HashMap<>();

    /**
     * 初始化短信发送器
     */
    @PostConstruct
    public void initSmsSender(){
        SpringUtil.getBeansOfType(SmsSender.class).forEach((key, value) -> smsSenders.put(value.getClass().getAnnotation(Name.class).value(), value));
    }

    /**
     * 获取短信发送器
     * @return 短信发送器
     */
    public SmsSender getSmsSender( ){
        // 获取短信发送器名称
        String name = ConfigKit.get(CacheKey.SMS_OPERATOR);
        // 如果没有配置短信发送器，则抛出异常
        if(!smsSenders.containsKey(name)){
            throw new BizException("短信发送失败，无法初始化短信发送器，请检查后台配置项。");
        }
        return smsSenders.get(name);
    }

}
