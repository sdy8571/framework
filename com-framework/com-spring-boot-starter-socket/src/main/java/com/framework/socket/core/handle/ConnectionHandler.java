package com.framework.socket.core.handle;

import com.framework.common.pojo.Result;
import com.framework.socket.core.cache.UserCache;
import com.framework.socket.core.domain.BaseSocketReq;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sdy
 * @description
 * @date 2024/12/19
 */
@Slf4j
public class ConnectionHandler {

    public static Result<String> execute(ChannelHandlerContext ctx, BaseSocketReq request) {
        // 添加用户
        UserCache.addUser(request.getToken(), ctx.channel());
        log.info("客户端连接成功");
        return Result.success("连接服务器成功！");
    }

}
