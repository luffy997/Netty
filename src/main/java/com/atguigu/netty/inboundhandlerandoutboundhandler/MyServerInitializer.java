package com.atguigu.netty.inboundhandlerandoutboundhandler;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //入站的handler进行解码  MyByteToLongDecoder
        // pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyLongToByteEncoder2());
        //出战的handler 进行编码
        pipeline.addLast(new MyLongToByteEncoder());
        //自定义的handler 处理业务逻辑
        pipeline.addLast(new MyServerHandler());

    }
}
