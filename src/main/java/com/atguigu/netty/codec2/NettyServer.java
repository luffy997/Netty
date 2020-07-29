package com.atguigu.netty.codec2;


import com.atguigu.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;


public class NettyServer {
    public static void main(String[] args) throws Exception {

        //创建BoosGroup WorkerGroup
        //说明
        //1.创建两个线程组boosgroup workgroup
        //2.boosgroup 只是处理连接请求，真正的和客户端业务处理，只会交给workgroup完成
        //3.两个都是无限循环
        //4.boosGroup workerGroup含有的子线程(NioEventGroup)的个数
        //默认 cpu的核数 * 2
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程来进行设置
            bootstrap.group(boosGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)  //使用NioSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列。，等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象
                        //给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //在pipline加入ProtoBufDecoder
                            //指定对哪种对象进行解码
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder",new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });//给我们的workGroup 的EventGroup设置管道处理器
            System.out.println("服务器 is ready");

            //绑定一个端口并且同步，生成一个ChannelFuture 对象
            //启动服务器（绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf 注册监听器 监控我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("监听端口6668");
                    }else {
                        System.out.println("监听端口6668失败");
                    }
                }
            });
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            boosGroup.shutdownGracefully(); //优雅的关闭
            workerGroup.shutdownGracefully();
        }

    }
}
