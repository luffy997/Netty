package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\路飞\\Desktop\\壁纸\\头像.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\路飞\\Desktop\\壁纸\\七仔.jpg");

        //获取各个流对应的channnel
        FileChannel sourceCH = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferForm 完成拷贝
        destCh.transferFrom(sourceCH,0,sourceCH.size());

        //关闭
        sourceCH.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
