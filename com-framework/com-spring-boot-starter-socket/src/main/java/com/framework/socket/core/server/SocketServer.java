package com.framework.socket.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sdy
 * @description
 * @date 2024/12/15
 */
@Slf4j
public class SocketServer {

    public static void start(int port) {
        start(port, "/");
    }

    public static void start(int port, String basePath) {
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WSServerInitializer(basePath));
            bootstrap.bind(port).sync();
            log.info("=== socket 服务端启动成功 ===");
        } catch (Exception ex) {
            log.error("=== socket 服务端启动失败 ===");
            log.error(ex.getMessage(), ex);
        }
    }

}
