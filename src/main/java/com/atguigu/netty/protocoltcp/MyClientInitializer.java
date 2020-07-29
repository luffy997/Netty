package com.atguigu.netty.protocoltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageEncoder());//客户端出去  先编码
        pipeline.addLast(new MyMessageDecoder()); //客户端接收 解码  顺序不能反
        pipeline.addLast(new MyClientHandler());
    }
}
