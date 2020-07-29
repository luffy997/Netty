package com.atguigu.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) throws Exception {

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new TestHttpServerHandler());

            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

            channelFuture.channel().closeFuture().sync();


        }finally {
            boosGroup.shutdownGracefully(); //优雅的关闭
            workerGroup.shutdownGracefully();
        }
    }
}
