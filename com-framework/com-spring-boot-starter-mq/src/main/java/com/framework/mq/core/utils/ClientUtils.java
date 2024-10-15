package com.framework.mq.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.redisson.api.RedissonClient;

/**
 * @author sdy
 * @description
 * @date 2024/10/14
 */
public class ClientUtils {

    private static final RedissonClient CLIENT = SpringUtil.getBean(RedissonClient.class);

    public static RedissonClient getClient() {
        return CLIENT;
    }

}
