package com.framework.base.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan({"com.framework.base"})
public class BaseAutoConfiguration {

    public BaseAutoConfiguration() {
        log.info("=== 基础模块加载 ===");
    }

}
