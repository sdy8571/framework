package com.framework.banner.config;

import com.framework.banner.core.BannerApplicationRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author sdy
 * @description
 * @date 2024/11/25
 */
@Slf4j
@AutoConfiguration
public class BannerAutoConfiguration {

    @Bean
    public BannerApplicationRunner bannerApplicationRunner() {
        log.info("=== banner模块加载 ===");
        return new BannerApplicationRunner();
    }

}
