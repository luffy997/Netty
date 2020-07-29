package com.atguigu.netty.protocoltcp;

public class MessageProtocol {
//协议包
    private int len; //关键
    private  byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
