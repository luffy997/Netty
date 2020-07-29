package com.atguigu.netty.codec2;

import com.atguigu.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;


/***
 * 1.自定义一个Handeler 需要继承netty 规定号的某个HandlerAdapter
 * 2.这时我们定义一个Handler,才能成为一个handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    //读取数据实际(这里我们可以读取客户端发送的信息)
    /*
    1.ChannelHandlerContext ctx：上下文对象，含有管道pipeline,通道channel，地址
    2.Object msg ：客户端发送的数据 默认Object
     */

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType ==MyDataInfo.MyMessage.DataType.StudentType){

            MyDataInfo.Student student= msg.getStudent();
            System.out.println("学生id="+student.getId()+"学生名字="+student.getName());
        }else if (dataType ==MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker=msg.getWorker();
            System.out.println("工人年龄="+worker.getAge()+"工人名字="+worker.getName());

        }else {
            System.out.println("传输的类型不正确");
        }

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
