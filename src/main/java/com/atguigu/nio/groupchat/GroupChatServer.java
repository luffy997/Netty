package com.atguigu.nio.groupchat;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try{
            //得到选择器
            selector = Selector.open();
            //serverSocketChannel
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将listenChannel 注册到selector三
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //监听
    public  void listen(){
        try{
            //循环处理
            while (true){
               int conut= selector.select();
               if (conut >0){  //有事件处理
                   //遍历得到的selectorKey
                   Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                   while (iterator.hasNext()){
                       //取出selectionKey
                       SelectionKey key = iterator.next();

                       //监听到的accept
                       if (key.isAcceptable()){
                           SocketChannel sc = listenChannel.accept();
                           sc.configureBlocking(false);
                           //将该sc 注册到seletor上
                           sc.register(selector,SelectionKey.OP_READ);
                           //提升
                           System.out.println(sc.getRemoteAddress()+"上线");


                       }if (key.isReadable()){  //通道发送read事件，通道是可读状态
                           //处理读（专门写方法）
                           readData(key);

                       }
                       //当前的key 删除 防止重复处理
                       iterator.remove();
                   }

               }else {
                   System.out.println("等待。。。。");
               }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    //读取客户端消息
    private  void readData(SelectionKey key){

        //定义一个socketChannel
        SocketChannel channel=null;
        try{
            //取到关联的Channel
            channel = (SocketChannel) key.channel();
            //创建缓冲buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值做处理
            if (count >0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出信息
                System.out.println("from 客户端："+msg);

                //向其它的客户端(去掉自己)转发消息，专门写一个方法来处理
                sendInfoOtherCliients(msg,channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() +"离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //转发消息给其他客户（通道）
    private void sendInfoOtherCliients(String msg,SocketChannel self ) throws IOException {
        System.out.println("服务器转发信息。。。");
        //遍历所有注册到selelctor 上的socketChannel，并排除self
        for (SelectionKey key : selector.keys() ){
            
            //通过key 取出对应通道
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof  SocketChannel && targetChannel != self){

                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 写入到通道中
                dest.write(buffer);

            }
        }
    }
    public static void main(String[] args) {

        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
