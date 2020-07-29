package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {

        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello world!", Charset.forName("utf-8"));

        //使用相关的方法
        if (byteBuf.hasArray()){  //true

            byte[] content = byteBuf.array();

            //将content转成字符串处理
            System.out.println(new String(content,Charset.forName("utf-8")));

            System.out.println("byteBuf="+byteBuf);

            System.out.println(byteBuf.arrayOffset());  //0
            System.out.println(byteBuf.readerIndex()); //0
            System.out.println(byteBuf.writerIndex()); //13
            System.out.println(byteBuf.capacity()); //13

            System.out.println(byteBuf.getByte(0));//对len  无影响
            System.out.println(byteBuf.readByte());  //读了一个 len=11

            int len = byteBuf.readableBytes(); //可读的字节数 12
            System.out.println("len="+len);

            //for循环取出各个字节
            for(int i =0;i < len ;i ++){
                System.out.println((char) byteBuf.getByte(i));
            }
            //按照某个范围读取
            System.out.println(byteBuf.getCharSequence(0,4,Charset.forName("utf-8")));
            System.out.println(byteBuf.getCharSequence(4,6,Charset.forName("utf-8")));



        }
    }
}
