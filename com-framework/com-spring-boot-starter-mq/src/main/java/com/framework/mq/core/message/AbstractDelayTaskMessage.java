package com.framework.mq.core.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.TimeUnit;

/**
 * @author sdy
 * @description
 * @date 2024/9/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractDelayTaskMessage extends AbstractRedisMessage {

    @JsonIgnore
    public abstract String getQueueName();

    public Integer delayTime;

    private Long createAt;

    private TimeUnit timeUnit;

    private String taskId;

}
