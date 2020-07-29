package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        //创建一个ByteBuf
        //1.创建对象 该对象包含一个数组arr ，是一个byte{10}
        //2.在netty的buffer不需要使用filp进行反转
        //3.底层维护了readerIndex 和 writeIndex 和 capacity 将buffer分成三个区域
        //0----readerindex 已经读取的区域
        //readerindex---writerIndex  可读的区域
        //writeIndex -- capacity 可写的区域
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("capacity:"+buffer.capacity());
        //输出
        System.out.println("getByte读取--------------------");
        for (int i =0 ;i < buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }
        System.out.println("readByte读取--------------------");
        for (int i =0 ;i < buffer.capacity();i++){
            System.out.println(buffer.readByte());
        }
        System.out.println("执行完毕");
    }
}
