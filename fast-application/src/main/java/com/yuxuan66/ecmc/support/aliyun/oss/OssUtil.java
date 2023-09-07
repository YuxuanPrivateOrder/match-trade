package com.yuxuan66.ecmc.support.aliyun.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.yuxuan66.ecmc.support.cache.ConfigKit;
import com.yuxuan66.ecmc.support.cache.key.CacheKey;
import com.yuxuan66.ecmc.support.exception.BizException;

import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * 阿里云OSS工具类
 *
 * @author Sir丶雨轩
 * @since 2022/12/13
 */
public class OssUtil {


    /**
     * 获取OSS存储bucket
     *
     * @return bucket
     */
    public static String getBucketName() {
        return ConfigKit.get(CacheKey.ALI_OSS_BUCKET);
    }

    /**
     * 创建一个OSS操作对象
     *
     * @return oss操作对象
     */
    private static OSS createOss() {
        return new OSSClientBuilder().build(ConfigKit.get(CacheKey.ALI_OSS_ENDPOINT), ConfigKit.get(CacheKey.ALI_ACCESS_KEY_ID), ConfigKit.get(CacheKey.ALI_ACCESS_KEY_SECRET));
    }

    /**
     * 判断文件夹是否存在并创建
     *
     * @param path 文件夹
     */
    public static void checkAndCreatePath(String path) {
        OSS oss = createOss();
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        try {
            String bucketName = getBucketName();
            boolean found = oss.doesObjectExist(bucketName, path);
            if (!found) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, new ByteArrayInputStream("".getBytes()));
                oss.putObject(putObjectRequest);
            }
        } catch (Exception e) {
            throw new BizException("文件上传失败", e);
        } finally {
            if (oss != null) {
                oss.shutdown();
            }
        }
    }

    /**
     * 上传文件流到oss
     *
     * @param fileBytes 文件流
     * @param fileName  文件名
     */
    public static void upload(byte[] fileBytes, String fileName) {
        OSS oss = createOss();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(getBucketName(), fileName, new ByteArrayInputStream(fileBytes));
            oss.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BizException("文件上传失败", e);
        } finally {
            if (oss != null) {
                oss.shutdown();
            }
        }
    }
}
