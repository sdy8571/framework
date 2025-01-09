package com.framework.socket.core.handle;

import cn.hutool.json.JSONUtil;
import com.framework.common.pojo.Result;
import com.framework.socket.core.domain.BaseSocketReq;
import com.framework.socket.core.domain.CommandEnum;
import com.framework.socket.core.service.MsgHandleService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author sdy
 * @description
 * @date 2024/12/23
 */
@Component
public class MessageHandleLoader {

    @Resource
    private List<MsgHandleService> msgHandleServices;

    public void doChannelRead(ChannelHandlerContext ctx, BaseSocketReq request) {
        Optional<MsgHandleService> optional = msgHandleServices.stream().filter(s -> s.getCode() == request.getCode()).findFirst();
        if (!optional.isPresent()) {
            ctx.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(notExistsCode())));
            return;
        }
        // 根据指令调用服务
        Result<?> result = optional.get().handle(request);
        // 响应结果返回
        ctx.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(result)));
    }

    private Result<?> notExistsCode() {
        return Result.error(CommandEnum.ERROR.getCode(), "消息指令有误，请确认后重新");
    }

}
