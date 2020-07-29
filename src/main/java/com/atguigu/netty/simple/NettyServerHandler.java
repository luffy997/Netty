package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;


/***
 * 1.自定义一个Handeler 需要继承netty 规定号的某个HandlerAdapter
 * 2.这时我们定义一个Handler,才能成为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据实际(这里我们可以读取客户端发送的信息)
    /*
    1.ChannelHandlerContext ctx：上下文对象，含有管道pipeline,通道channel，地址
    2.Object msg ：客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //这里有一个非常耗费时间的业务--> 异步执行 -->提交该channel
        //NIOEventLoop 到taskQueue

        //解决方案：1. 用户程序自定义的普通任务

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("异常信息：" + e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端喵2", CharsetUtil.UTF_8));
                System.out.println("go on...");
            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("异常信息：" + e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端喵3", CharsetUtil.UTF_8));
                System.out.println("go on...");
            }
        });

        //用户自定义定时任务，-->该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    System.out.println("异常信息：" + e.getMessage());
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端喵4", CharsetUtil.UTF_8));
                System.out.println("go on...");
            }
        },5, TimeUnit.SECONDS);
        System.out.println("go on...");



//        System.out.println("服务器读取线程"+ Thread.currentThread().getName());
//        System.out.println("server ctx ="+ctx);
//        System.out.println("看看channel 和 pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链表，出栈入栈问题
//
//
//        //将msg 转成一个ByteBuffer
//        //ByteBuf 是Netty提供的，不是NIO的ByteBuffer
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送信息是："+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());

    }

    //读取数据完毕

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush()  将数据写入到缓冲并刷新
        //一般讲，我们对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端喵1", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
