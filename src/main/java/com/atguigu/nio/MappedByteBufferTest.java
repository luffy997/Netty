package com.atguigu.nio;


import java.io.RandomAccessFile;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 说明：
 * 1.MappedByteBuffer 可以让文件直接在内存（对外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1：FileChannel.MapMode.READ_WRITE 使用读写模式
         * 参数2: 0 可以直接修改的起始位置
         * 参数3：5 映射内存的大小（不是索引）  即将1.txt 的多少个字节映射到内存
         * 5个字节 最多修改的下标范围就是0-4
         * 可以直接修改的范围是0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        mappedByteBuffer.put(5, (byte) 'Y');  //IndexOutOfBoundsException

        randomAccessFile.close();
        System.out.println("修改成功");
    }
}
