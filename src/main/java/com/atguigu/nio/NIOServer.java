package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {

        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selecotot对象
        Selector selector = Selector.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(666));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel 注册到 selector 关心 事件为OP_aACCept
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("注册后的selectionKey 数量:"+selector.keys().size());

        //循环等待客户端连接
        while (true){

            //等待1秒，如果没有事件发生，就返回
            if(selector.select(1000) == 0){ //没有事件发生
                System.out.println("服务器等待1秒，无连接");
                continue;
            }

            //如果返回的不是0,就获取到相关的selecttionKey集合
            //1.如果返回的>0, 表示已经获取到关注事件
            //2.selector.selectorKeys() 返回关注事件的集合
            //3.通过selectionKeys 可以反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量："+selectionKeys.size());

            //遍历selectionKeys，使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                //获取到selectionKeys
                SelectionKey key = keyIterator.next();
                //根据key对应的通道 发生的事件 做相应的处理 读写
                if (key.isAcceptable()){ //如果是OP_ACCEPT 有新的客户端连接我
                    //给该客户端生成一个SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成一个sockChannel"+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector  关注事件为OP_READ 同时给SocketChannel
                    //关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后注册到的selectionKey 数量:"+selector.keys().size());  //2,3,4


                }if (key.isReadable()){  //发生的OP_READ
                    //通过key 反向获取到对应的channel
                    SocketChannel channel = (SocketChannel)key.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端"+new String(buffer.array()));

                }
                //手动从集合中移除当前的selectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
