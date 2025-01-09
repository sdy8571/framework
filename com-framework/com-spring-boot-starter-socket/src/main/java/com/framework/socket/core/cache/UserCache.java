package com.framework.socket.core.cache;

import cn.hutool.core.util.RandomUtil;
import com.framework.redis.core.cache.CacheHash;
import com.framework.socket.core.domain.User;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
@Slf4j
public class UserCache {

    private static final String USER_CACHE_HASH_KEY = "user:cache:socket";

    public static void addUser(String token, Channel channel) {
        if (CacheHash.hasKey(USER_CACHE_HASH_KEY, token)) {
            log.info("用户已连接socket服务端，请勿重复连接");
            return;
        }
        // todo 需要更具实际用户进行操作，现给一个默认值
        User user = new User(token, RandomUtil.randomLong());
        user.setChannel(channel);
        // 保存缓存
        CacheHash.set(USER_CACHE_HASH_KEY, token, user);
    }

    public static User getUserByToken(String token) {
        if (!CacheHash.hasKey(USER_CACHE_HASH_KEY, token)) {
            return null;
        }
        return (User) CacheHash.get(USER_CACHE_HASH_KEY, token);
    }

    public static void delUserByToken(String token) {
        if (!CacheHash.hasKey(USER_CACHE_HASH_KEY, token)) {
            return;
        }
        CacheHash.remove(USER_CACHE_HASH_KEY, token);
    }

}
