package com.yuxuan66.ecmc.common.utils;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Aop操作工具类
 * @author Sir丶雨轩
 * @since 2023/9/7
 */
public class AopUtil {


    /**
     * 获取目标方法
     * @param joinPoint 切点
     * @return 目标方法
     * @throws NoSuchMethodException 异常
     */
    public static Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        // 获取目标方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取目标方法参数类型
        Class<?>[] parameterTypes = new Class[joinPoint.getArgs().length];
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            parameterTypes[i] = joinPoint.getArgs()[i].getClass();
        }
        // 获取目标方法
        return joinPoint.getTarget().getClass().getMethod(methodName, parameterTypes);
    }

    /**
     * 获取目标方法注解
     * @param joinPoint 切点
     * @param annotationClass 注解类
     * @return 注解
     * @throws NoSuchMethodException 异常
     */
    public static Annotation getAnnotation(ProceedingJoinPoint joinPoint, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        return getMethod(joinPoint).getAnnotation(annotationClass);
    }

}
