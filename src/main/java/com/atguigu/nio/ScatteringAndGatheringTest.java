package com.atguigu.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到buffer时，可采用buffer数组，依次写入  {分散}
 * Gathering：从buffer读取数据时，可采用buffer数组，依次读取 {聚合}
 * */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {

        //使用 ServerSocketChannel 和 socketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket 并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建一个buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 9;  //从客户接收8个字节
        while (true){
            int byteRead = 0;

            while (byteRead <messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead +=1;  //累计读取到的字节数
                System.out.println("byteRead="+byteRead);
                //使用流打印，看看当前的buffer的position 和 limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position="+buffer.position()+". limit="+buffer.limit()).forEach(System.out::println);
            }

            //将所有的buffer进行filp
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据读出，显示回客户端
            long byteWirte = 0;
            while (byteWirte < messageLength){
                socketChannel.write(byteBuffers);
                byteWirte +=1;
            }

            //将所有的buffer复位
            Arrays.asList(byteBuffers).forEach(byteBuffer -> {
                byteBuffer.clear();
            });

            System.out.println("byteRead="+ byteRead +"byteWrite=" +byteWirte+",messageLength"+messageLength);
        }

    }
}
