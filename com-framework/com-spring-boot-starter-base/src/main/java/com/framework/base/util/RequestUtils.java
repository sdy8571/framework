package com.framework.base.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sdy
 * @description
 * @date 2024/6/18
 */
@Slf4j
public class RequestUtils {

    public static void writeResponse(Object result) {
        // 获取请求
        HttpServletRequest request = getRequest();
        log.info("============================ 【请求出参】开始 ============================");
        log.info("请求路径：{}", request.getRequestURI());
        log.info("响应参数：{}", JSONUtil.toJsonStr(result));
        log.info("============================ 【请求出参】结束 ============================");
    }

    public static String writeRequest(ProceedingJoinPoint proceedingJoinPoint, boolean showLog) {
        // 获取入参
        String requestArgs = handleRequestArgs(proceedingJoinPoint);
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        // 获取请求
        HttpServletRequest request = getRequest();
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (showLog) {
            log.info("============================ 【请求入参】开始 ============================");
            log.info("请求方式：{}:{}:{}", method, uri, signatureMethod.getName());
            log.info("请求参数：{}", requestArgs);
            log.info("============================ 【请求入参】结束 ============================");
        }
        return StringUtils.joinWith("|", ip, method, uri, signature.getMethod().getName(), requestArgs);
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    private static String handleRequestArgs(JoinPoint joinPoint) {
        // 请求参数
        List<Object> params = new ArrayList<>();
        // 请求参数
        Object[] args = joinPoint.getArgs();
        for (Object ob : args) {
            params.add(RequestUtils.filterParam(ob));
        }
        return JSONUtil.toJsonStr(params);
    }

    private static Object filterParam(Object ob) {
        if (ob == null) {
            return "NULL Object";
        }
        Object res;
        if (HttpServletResponse.class.isAssignableFrom(ob.getClass())) {
            res = "HttpServletResponse Object";
        } else if (HttpServletRequest.class.isAssignableFrom(ob.getClass())) {
            res = "HttpServletRequest Object";
        } else if (MultipartFile.class.isAssignableFrom(ob.getClass())) {
            res = "MultipartFile Object";
        } else if (File.class.isAssignableFrom(ob.getClass())) {
            res = "File Object";
        } else if (InputStream.class.isAssignableFrom(ob.getClass())) {
            res = "InputStream Object";
        } else if (OutputStream.class.isAssignableFrom(ob.getClass())) {
            res = "OutputStream Object";
        } else if (InputStreamSource.class.isAssignableFrom(ob.getClass())) {
            res = "InputStreamSource Object";
        } else if (ob instanceof MultipartFile[]) {
            res = "MultipartFile Array";
        } else if (ob instanceof File[]) {
            res = "File Array";
        } else {
            res = ob;
        }
        return res;
    }

}
