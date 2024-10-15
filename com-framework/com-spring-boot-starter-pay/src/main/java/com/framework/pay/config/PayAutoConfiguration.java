//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.framework.pay.config;

import com.framework.pay.core.client.PayClientFactory;
import com.framework.pay.core.client.impl.PayClientFactoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author sdy
 * @description
 * @date 2024/7/31
 */
@Slf4j
@AutoConfiguration
public class PayAutoConfiguration {

    public PayAutoConfiguration() {
        log.info("=== 支付模块加载 ===");
    }

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
