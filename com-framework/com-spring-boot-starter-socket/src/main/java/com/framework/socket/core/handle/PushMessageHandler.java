package com.framework.socket.core.handle;

import cn.hutool.json.JSONUtil;
import com.framework.common.utils.AssertUtils;
import com.framework.socket.core.cache.UserCache;
import com.framework.socket.core.domain.User;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
public class PushMessageHandler {

    public static void push(String token, Object msg) {
        User user = UserCache.getUserByToken(token);
        // 校验用户
        AssertUtils.isNull(user);
        // 校验渠道是否为空
        AssertUtils.isNull(user.getChannel());
        // 推送消息
        push(user.getChannel(), msg);
    }

    public static void push(Long userId, Object msg) {
        User user = UserCache.getUserByToken(userId + "");
        // 校验用户
        AssertUtils.isNull(user);
        // 校验渠道是否为空
        AssertUtils.isNull(user.getChannel());
        // 推送消息
        push(user.getChannel(), msg);
    }

    private static void push(Channel ch, Object msg) {
        // 发送数据
        String data = JSONUtil.toJsonStr(msg);
        ch.writeAndFlush(new TextWebSocketFrame(data));
    }

}
