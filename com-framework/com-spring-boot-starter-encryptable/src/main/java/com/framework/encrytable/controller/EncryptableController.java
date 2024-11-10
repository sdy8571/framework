package com.framework.encrytable.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.framework.base.pojo.Result;
import com.framework.encrytable.encrypt.ImplEncryptablePropertyResolver;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sdy
 * @description
 * @date 2024/11/5
 */
@RestController
public class EncryptableController {

    private final ImplEncryptablePropertyResolver resolver = SpringUtil.getBean("encryptablePropertyResolver");

    @PostMapping("/encrypt/{password}")
    public Result<String> encrypt(@PathVariable("password") String password) {
        String encryptResult = resolver.resolveEncryptValue(password);
        return Result.success(encryptResult);
    }

    @PostMapping("/decrypt/{password}")
    public Result<String> decrypt(@PathVariable("password") String password) {
        String encryptResult = resolver.resolvePropertyValue(password);
        return Result.success(encryptResult);
    }

}
