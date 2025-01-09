package com.framework.redis.core.cache;

import cn.hutool.extra.spring.SpringUtil;
import com.framework.common.utils.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author sdy
 * @description
 * @date 2024/12/24
 */
@Slf4j
public class AbstractCache {

    protected static final String KEY_PREFIX_SEPARATOR = "::";

    protected static StringRedisTemplate stringRedisTemplate() {
        return SpringUtil.getBean("stringRedisTemplate");
    }

    protected static String createKey(String... keys) {
        // 校验keys
        AssertUtils.isNull(keys);
        // 组装key
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key).append(KEY_PREFIX_SEPARATOR);
        }
        // 回退最后一个分隔符
        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

}
