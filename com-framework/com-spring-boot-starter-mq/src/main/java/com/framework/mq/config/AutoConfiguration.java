package com.framework.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan({"com.framework.mq"})
public class AutoConfiguration {

    public AutoConfiguration() {
        log.info("=== redisson 延迟消息队列模块加载 ===");
    }

}