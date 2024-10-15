package com.framework.mq.core.listener;

import cn.hutool.core.util.TypeUtil;
import cn.hutool.system.SystemUtil;
import com.framework.mq.core.message.AbstractDelayTaskMessage;
import com.framework.mq.core.utils.ClientUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.lang.reflect.Type;

/**
 * @author sdy
 * @description
 * @date 2024/9/19
 */
@Slf4j
public abstract class AbstractRedissonDelayMessageListener<T extends AbstractDelayTaskMessage> implements ApplicationRunner {

    /**
     * 消息类型
     */
    private final Class<T> messageType;

    private final String queueName;

    @SneakyThrows
    protected AbstractRedissonDelayMessageListener() {
        this.messageType = getMessageClass();
        this.queueName = messageType.getDeclaredConstructor().newInstance().getQueueName();
        log.info("==========> 监听队列 ：" + queueName);
    }

    /**
     * 通过解析类上的泛型，获得消息类型
     *
     * @return 消息类型
     */
    @SuppressWarnings("unchecked")
    private Class<T> getMessageClass() {
        Type type = TypeUtil.getTypeArgument(getClass(), 0);
        if (type == null) {
            throw new IllegalStateException(String.format("类型(%s) 需要设置消息类型", getClass().getName()));
        }
        return (Class<T>) type;
    }

    @Override
    public void run(ApplicationArguments args) {
        new Thread(() -> {
            RBlockingQueue<T> blockingQueue = ClientUtils.getClient().getBlockingQueue(queueName);
            try {
                while(true) {
                    T task = blockingQueue.take();
                    // 消费业务逻辑
                    onMessage(task);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }, getConsumerName()).start();
    }

    private String getConsumerName() {
        return queueName + "-Consumer@" + SystemUtil.getCurrentPID();
    }

    protected abstract void onMessage(T message);

}
