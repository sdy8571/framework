package com.framework.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sdy
 * @description
 * @date 2024/12/15
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = {"com.framework.socket"})
public class SocketAutoConfiguration {

    public SocketAutoConfiguration() {
        log.info("=== socket 模块加载 ===");
    }

}
