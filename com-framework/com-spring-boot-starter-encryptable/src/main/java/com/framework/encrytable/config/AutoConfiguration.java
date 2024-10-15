package com.framework.encrytable.config;

import cn.hutool.core.io.resource.ResourceUtil;
import com.framework.encrytable.encrypt.ImplEncryptablePropertyFilter;
import com.framework.encrytable.encrypt.ImplEncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyFilter;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableEncryptableProperties
public class AutoConfiguration {

    public AutoConfiguration() {
        log.info("=== 属性加密模块加载 ===");
    }

    @Bean(name = "encryptablePropertyFilter")
    public EncryptablePropertyFilter encryptablePropertyFilter() {
        return new ImplEncryptablePropertyFilter();
    }

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        String password = ResourceUtil.readUtf8Str("META-INF/key/password.txt");
        return new ImplEncryptablePropertyResolver(password);
    }
}