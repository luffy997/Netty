package com.atguigu.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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


        //读取从客户端发送的StudentPOJO.student
        StudentPOJO.Student student=(StudentPOJO.Student)msg;
        System.out.println("客户端发送的数据 id="+student.getId()+"名字="+student.getName());

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
