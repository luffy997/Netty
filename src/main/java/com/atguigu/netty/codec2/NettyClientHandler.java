package com.atguigu.netty.codec2;

import com.atguigu.netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //随机的发送Students 或者 Worker 对象
        int random = new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage=null;

        if (0 == random){  //发送一个Students对象

           myMessage= MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType).setStudent(MyDataInfo.Student.newBuilder().setId(5).setName("路飞").build()).build();
        }else {  //发送一个Wordker对象

            myMessage= MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType).setWorker(MyDataInfo.Worker.newBuilder().setAge(5).setName("最强的").build()).build();
        }
        ctx.writeAndFlush(myMessage);

    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf= (ByteBuf) msg;
        System.out.println("服务器回复的信息："+ byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
