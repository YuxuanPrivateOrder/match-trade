package com.yuxuan66.ecmc.common.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * 系统通用工具类
 * @author Sir丶雨轩
 * @since 2022/12/10
 */
public final class Lang {

    /**
     * 获取当前时间戳对象
     * @return 时间戳对象
     */
    public static Timestamp getNowTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取指定类的指定索引泛型类型
     * @param clazz 类
     * @param index 索引
     * @return 泛型
     */
    public static Type getGenericType(Class<?> clazz, int index){
        ParameterizedType parameterizedType = (ParameterizedType)clazz.getGenericSuperclass();
        return parameterizedType.getActualTypeArguments()[index];
    }
}
