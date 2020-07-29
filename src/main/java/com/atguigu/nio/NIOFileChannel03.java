package com.atguigu.nio;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){  //循环读取

            //重要的操作！！
            byteBuffer.clear();//清空数据 复位

            int read = fileChannel01.read(byteBuffer);
            System.out.println("read="+read);

            if (read == -1){  //读完
                break;
            }
            //将buffer的数据写入到fileChannel02  -->2.txt

            //反转
            byteBuffer.flip();

            fileChannel02.write(byteBuffer);

        }

        fileInputStream.close();
        fileOutputStream.close();

    }
}
