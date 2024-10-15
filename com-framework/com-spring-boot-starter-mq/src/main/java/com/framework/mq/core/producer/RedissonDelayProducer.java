package com.framework.mq.core.producer;

import cn.hutool.core.lang.UUID;
import com.framework.mq.core.message.AbstractDelayTaskMessage;
import com.framework.mq.core.utils.ClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author sdy
 * @description
 * @date 2024/9/19
 */
@Component
public class RedissonDelayProducer {

    /**
     * 添加延迟任务
     * @param message 数据
     * @param <T> 类型
     */
    public <T extends AbstractDelayTaskMessage> String send(T message) {
        if (message.getCreateAt() == null) {
            message.setCreateAt(System.currentTimeMillis());
        }
        if (message.getDelayTime() == null) {
            message.setDelayTime(1);
        }
        if (message.getTimeUnit() == null) {
            message.setTimeUnit(TimeUnit.SECONDS);
        }
        if (StringUtils.isBlank(message.getTaskId())) {
            message.setTaskId(UUID.fastUUID().toString());
        }
        RBlockingQueue<T> blockingQueue = ClientUtils.getClient().getBlockingQueue(message.getQueueName());
        RDelayedQueue<T> delayedQueue = ClientUtils.getClient().getDelayedQueue(blockingQueue);
        delayedQueue.offer(message, message.getDelayTime(), message.getTimeUnit());
        // 返回任务编号
        return message.getTaskId();
    }

}
