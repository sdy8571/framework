/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.framework.base.aspect;

import cn.hutool.crypto.SecureUtil;
import com.framework.base.exception.GlobalErrorCodeConstants;
import com.framework.base.pojo.Result;
import com.framework.base.annotation.NotRepeatSubmit;
import com.framework.base.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class NotRepeatSubmitAspect {

    private static final String NAME_LOCAL = "local";
    private static final String NAME_REDIS = "redis";
    private static final Map<String, Long> lockMap;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    static {
        lockMap = new HashMap<>(6);
    }

    @Pointcut("@annotation(com.framework.base.annotation.NotRepeatSubmit)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 打印入参
        String requestArgs = RequestUtils.writeRequest(proceedingJoinPoint, false);
        log.info("防重关键字==>{}", requestArgs);
        // 计算 md5
        String reqMd5 = SecureUtil.md5(requestArgs);
        // 获取配置
        NotRepeatSubmit annotation = handleMethod(proceedingJoinPoint);
        long expire = annotation.value();
        String type = annotation.type();
        //超时时间：10秒，最好设为常量
        long currentTime = System.currentTimeMillis();
        long expireTime = currentTime + expire;
        if (NAME_LOCAL.equals(type)) {
            Long lockTime = lockMap.get(reqMd5);
            if (lockTime == null || lockTime <= currentTime) {
                // 放过，不做防重
                lockMap.put(reqMd5, expireTime);
            } else {
                return Result.error(GlobalErrorCodeConstants.TOO_MANY_REQUESTS);
            }
        } else if (NAME_REDIS.equals(type)) {
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(reqMd5))) {
                return Result.error(GlobalErrorCodeConstants.TOO_MANY_REQUESTS);
            } else {
                stringRedisTemplate.opsForValue().set(reqMd5, reqMd5, expire, TimeUnit.SECONDS);
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private NotRepeatSubmit handleMethod(ProceedingJoinPoint proceedingJoinPoint) {
        // 获取配置
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(NotRepeatSubmit.class);
    }

}
