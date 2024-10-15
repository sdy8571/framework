package com.framework.tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author sdy
 * @description
 * @date 2024/9/11
 */
@Slf4j
@Configuration
public class TenantAutoConfiguration {

    public TenantAutoConfiguration() {
        log.info("=== 租户模块加载 ===");
    }

}
