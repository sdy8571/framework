package com.framework.redis.core.cache;

import cn.hutool.json.JSONUtil;
import com.framework.common.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sdy
 * @description
 * @date 2024/12/24
 */
@Slf4j
public class CacheHash extends AbstractCache {

    public static <T> void set(String key, String hashKey,  T t) {
        // 校验key
        AssertUtils.isBlank(key);
        // 保存缓存
        stringRedisTemplate().opsForHash().put(key, hashKey, t);
        log.info("添加缓存成功");
    }

    public static Object get(String key, String hashKey) {
        // todo 校验是否存在key
        if (!hasKey(key, hashKey)) {
            return null;
        }
        // 获取对象
        return stringRedisTemplate().opsForHash().get(key, hashKey);
    }

    public static Map<Object, Object> getEntries(String key) {
        // todo
        hasKey(key);
        // 返回对象
        return stringRedisTemplate().opsForHash().entries(key);
    }

    public static void remove(String key, String... hashKey) {
        // 校验是否存在key
        List<String> delKeys = new ArrayList<>();
        for (String hashKeyValue : hashKey) {
            if (hasKey(key, hashKeyValue)) {
                delKeys.add(hashKeyValue);
            }
        }
        if (CollectionUtils.isEmpty(delKeys)) {
            return;
        }
        // 删除数据
        Long res = stringRedisTemplate().opsForHash().delete(key, delKeys.toArray());
        log.info("成功删除{}个缓存", res);
    }

    public static boolean hasKey(String key) {
        AssertUtils.isBlank(key);
        return Boolean.TRUE.equals(stringRedisTemplate().hasKey(key));
    }

    public static boolean hasKey(String key, String hashKey) {
        // 校验key
        AssertUtils.isBlank(key);
        // 验证是否存在key
        return Boolean.TRUE.equals(stringRedisTemplate().opsForHash().hasKey(key, hashKey));
    }

}
