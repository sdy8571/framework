package com.framework.sequence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author sdy
 * @date 2020/1/3
 * @Version 1.0
 * @Description
 */
@Configuration
@ConfigurationProperties(prefix = "sequence")
@Data
public class SequenceConfig {

    private String available;
    private List<SequenceModel> sequenceConfigs;

}
