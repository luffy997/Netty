package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip ="+ctx.channel().remoteAddress());
        System.out.println("收到服务器消息="+msg);

    }

    //重写ChannelActive 发送数据


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
       // ctx.writeAndFlush(Unpooled.copiedBuffer(""));
        ctx.writeAndFlush(123456L);
        //分析
        //1."abcdabcdabcdabcd" 16个字节
        //2.该方法处理器的前一个handler 是 MyLongToByteEncoder
        //3.MyLongToByteEncoder 父类 MessageToByteEncoder
        //4.父类  MessageToByteEncoder
       // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

    }
}
