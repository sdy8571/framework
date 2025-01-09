package com.framework.socket.core.handle;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.framework.common.pojo.Result;
import com.framework.socket.core.domain.BaseSocketReq;
import com.framework.socket.core.domain.CommandEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sdy
 * @description
 * @date 2024/12/16
 */
@Slf4j
public class WSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        this.doChannelRead(ctx, msg.text());
    }

    private void doChannelRead(ChannelHandlerContext ctx, String message) {
        // 转换参数
        BaseSocketReq request = JSONUtil.toBean(message, BaseSocketReq.class);
        // 匹配指令
        CommandEnum command = CommandEnum.match(request.getCode());
        // 创建返回结果
        if (command == null) {
            // 调用自定义服务
            doChannelRead(ctx, request);
        } else {
            Result result;
            switch (command) {
                case CONNECTION:
                    result = ConnectionHandler.execute(ctx, request);
                    break;
                default: result = Result.error(CommandEnum.ERROR.getCode(), "请求数据异常");
            }
            ctx.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(result)));
        }
//        ctx.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(result)));
//        ctx.channel().writeAndFlush(result);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("读取错误，", cause);
        ctx.close();
    }

    public void doChannelRead(ChannelHandlerContext ctx, BaseSocketReq request) {
        MessageHandleLoader loader = SpringUtil.getBean("messageHandleLoader");
        if (loader != null) {
            loader.doChannelRead(ctx, request);
        }
    }

}
