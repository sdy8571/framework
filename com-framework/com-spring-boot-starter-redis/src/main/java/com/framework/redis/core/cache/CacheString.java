package com.framework.redis.core.cache;

import cn.hutool.json.JSONUtil;
import com.framework.common.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author sdy
 * @description
 * @date 2024/12/24
 */
@Slf4j
public class CacheString extends AbstractCache {

    public static <T> void set(String key, T t) {
        // 校验key
        AssertUtils.isBlank(key);
        // 保存缓存
        String value = JSONUtil.toJsonStr(t);
        stringRedisTemplate().opsForValue().set(key, value);
        log.info("添加缓存成功");
    }

    public static <T> void setSet(String key, T t) {
        stringRedisTemplate().opsForSet().add(key, JSONUtil.toJsonStr(t));
    }

    public static <T> void set(String key, T t, long timeout) {
        set(key, t, timeout, TimeUnit.SECONDS);
    }

    public static <T> void set(String key, T t, long timeout, TimeUnit timeUnit) {
        // 校验key
        AssertUtils.isBlank(key);
        // 保存数据
        String value = JSONUtil.toJsonStr(t);
        stringRedisTemplate().opsForValue().set(key, value, timeout, timeUnit);
        log.info("添加缓存成功");
    }

    public static String get(String key) {
        // 校验是否存在key
        if (!hasKey(key)) {
            return null;
        }
        // 获取对象
        return stringRedisTemplate().opsForValue().get(key);
    }

    public static void remove(String key) {
        // 校验是否存在key
        if(!hasKey(key)) {
            return;
        }
        // 删除数据
        if (Boolean.TRUE.equals(stringRedisTemplate().delete(key))) {
            log.info("删除缓存成功");
        }
    }

    public static boolean hasKey(String key) {
        // 校验key
        AssertUtils.isBlank(key);
        // 验证是否存在key
        return Boolean.TRUE.equals(stringRedisTemplate().hasKey(key));
    }

}
