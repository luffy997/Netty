package com.atguigu.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {
    public static void main(String[] args) throws Exception {

        //科幻段需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        //创建客户端启动对象
        //注意客户端使用的不是 ServerBootstrap 而不是Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        try{
            //设置相关参数
            bootstrap.group(group)  //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的线程组的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //在pinpline中加入ProtoBufferCoder
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });
            System.out.println("客户端 ok...");

            //启动客户端去连接服务器端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();

        }


    }
}
