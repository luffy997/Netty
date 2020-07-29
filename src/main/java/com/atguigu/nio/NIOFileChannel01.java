package com.atguigu.nio;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str="Hello,尚硅谷";
        //创建一个输出流 -->channel
        FileOutputStream fileOutputStream = new FileOutputStream("f:\\file01.txt");

        //通过fileOutputStream 获取对应的 FileChannel
        //fileChannel  真实  类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将str放入到byteBuffer中
        byteBuffer.put(str.getBytes());

        //对byteBuffer进行反转
        byteBuffer.flip();

        //将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();

    }
}
