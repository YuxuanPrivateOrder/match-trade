package com.yuxuan66.ecmc.support.cache.key;


import com.yuxuan66.ecmc.support.cache.key.modules.*;

/**
 * 缓存Key
 *
 * @author Sir丶雨轩
 * @since 2022/9/13
 */
public interface CacheKey extends UploadCacheKey, AliyunCacheKey, WebCacheKey, MailCacheKey, SmsCacheKey,CaptchaCacheKey,DefaultCacheKey {


    /**
     * 系统核心配置Key
     */
    String CONFIG = "SYS_CONFIG";


   
}
