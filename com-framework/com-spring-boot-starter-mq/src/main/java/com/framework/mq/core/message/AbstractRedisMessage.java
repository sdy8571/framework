package com.framework.mq.core.message;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/9/12
 */

@Data
public abstract class AbstractRedisMessage {

    private Map<String, String> headers = new HashMap<>();

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

}
