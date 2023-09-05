package com.yuxuan66.ecmc.common.aliyun;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.ICredentialProvider;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;

/**
 *
 * 阿里云凭证提供者工厂
 * @author Sir丶雨轩
 * @since 2023/9/4
 */
public class AliCredentialProviderFactory {

    /**
     * 获取凭证提供者
     * @return 凭证提供者
     */
    public static ICredentialProvider get(){
        return StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(ConfigKit.get(CacheKey.ALI_ACCESS_KEY_ID))
                .accessKeySecret(ConfigKit.get(CacheKey.ALI_ACCESS_KEY_SECRET))
                .build());
    }
}
