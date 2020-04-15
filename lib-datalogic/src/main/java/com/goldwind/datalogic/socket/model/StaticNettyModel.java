package com.goldwind.datalogic.socket.model;

import com.goldwind.datalogic.socket.handler.NettyPoolHandler;

import io.netty.channel.ChannelHandlerContext;

/*
 * Netty静态长连接模型
 * */
public class StaticNettyModel
{
    /*
     * ip
     * */
    private String ip;
    /*
     * 端口
     * */
    private int port;
    /*
     * 连接对象
     * */
    private ChannelHandlerContext ctx;
    /*
     * 返回值
     * */
    private String result;

    private NettyPoolHandler handler;

    public NettyPoolHandler getHandler()
    {
        return handler;
    }

    public void setHandler(NettyPoolHandler handler)
    {
        this.handler = handler;
    }

    public ChannelHandlerContext getCtx()
    {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx)
    {
        this.ctx = ctx;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    @Override public String toString()
    {
        return "StaticNettyModel{" + "ip='" + ip + '\'' + ", port=" + port + ", ctx=" + ctx + ", result='" + result + '\'' + ", handler=" + handler + '}';
    }
}
