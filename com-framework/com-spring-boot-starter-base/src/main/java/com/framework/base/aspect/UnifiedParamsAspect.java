/*
 * Copyright (C) 2017-2021
 * All rights reserved, Designed By 深圳中科鑫智科技有限公司
 * Copyright authorization contact 18814114118
 */
package com.framework.base.aspect;

import com.framework.base.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class UnifiedParamsAspect {

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {

        // 打印入参
        RequestUtils.writeRequest(proceedingJoinPoint, true);
        // 修改
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
        }
        RequestUtils.writeResponse(result);
        return result;
    }


}
