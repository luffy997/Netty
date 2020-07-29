package com.atguigu.netty.websocket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();//8个NioEventLoopGroup

        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))  //在boosgroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();

                            //因为是基于http协议的，使用http的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写的，添加ChunkedWrite 处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /**
                             * 1.http的数据在传输过程中是分段的，HttpObjectAggregator --> 可以将多个段聚合起来
                             * 2.这就是为什么，当浏览器发送大量数据时，就会发生多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 1，对于webSocket，它的数据以帧 形式传递
                             * 2.可以看到webSocketFrame 下面有六个子类
                             * 3.浏览器请求时： ws://localhost:7000/hello 表示请求的url
                             * 4.WebSocketServerProtocolHandler 核心功能：将一个http协议升级成ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //处理业务逻辑自定义handler
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    }

