package com.framework.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan({"com.framework.auth"})
public class AutoConfiguration {

    public AutoConfiguration() {
        log.info("=== 授权模块加载 ===");
    }

}