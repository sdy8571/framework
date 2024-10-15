package com.framework.sequence;

import com.framework.sequence.config.SequenceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author sdy
 * @date 2020/1/4
 * @Version 1.0
 * @Description
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(SequenceConfig.class)
@ComponentScan(basePackages = {"com.framework.sequence"})
@ConditionalOnProperty(name = "available", prefix = "sequence", havingValue = "true")
@ConditionalOnClass(DataSource.class)
public class SequenceAutoConfiguration {

}

