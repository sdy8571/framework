package com.framework.socket.core.server;

import com.framework.socket.core.handle.WSocketHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.util.StringUtils;

/**
 * @author sdy
 * @description
 * @date 2024/12/18
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {

    private final String basePath;

    public WSServerInitializer(String basePath) {
        this.basePath = StringUtils.hasText(basePath) ? basePath : "/";
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // WebSocket是基于HTTP协议，所以要有HTTP编解码器
        pipeline.addLast(new HttpServerCodec())
                // 提供对写大数据流的支持
                .addLast(new ChunkedWriteHandler())
                // 对HttpMessage进行聚合，聚合成FullHttpRequest或者FullHttpResponse 该handler使用率极高 1024 *64 为最大消息长度
                .addLast(new HttpObjectAggregator(1024 * 64))
                /**
                 * WebSocket 服务器处理的协议，用于指定给客户端连接访问的路由：/
                 * 本handler会帮你处理一些繁重的复杂工作，比如：握手动作 handshaking (close,ping,pong)     ping + pong = 心跳检测
                 * 对于WebSocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                 */
                .addLast(new WebSocketServerProtocolHandler(basePath))
                // 自定义handler
                .addLast(new WSocketHandler());
    }

}
