package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *decode 会根据接收到的数据被调用多次，直到确定没有新的元素被添加到list
     * 或者是ByteBuf 没有更多的可读字节为主
     * 如果list 不为空，就会将list内容传递给下一个channelinboundhandler 处理，该处理器的方法也会被调用多次
     * @param ctx 上下文对象
     * @param in  入站的ByteBuf
     * @param out  List集合，将解码后的数据传给下一个handler处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        System.out.println("MyByteToLongDecoder 被调用");
        //因为Long 8字节  需要判断有8哥字节才能读取一个long
        if (in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
