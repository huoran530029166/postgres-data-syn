package com.goldwind.datalogic.socket.handler;

import java.io.UnsupportedEncodingException;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.model.NettyHandlerObservable;
import com.goldwind.datalogic.socket.model.StaticNettyModel;
import com.goldwind.datalogic.socket.netty.NettyPool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/*
 * Netty连接池
 * */
public class NettyPoolHandler extends ChannelInboundHandlerAdapter
{
    /*
     * ip
     */
    private String ip;
    /*
     * 端口
     */
    private int port;
    /*
     * 被观察者对象
     */
    private NettyHandlerObservable observable;

    public NettyHandlerObservable getObservable()
    {
        return observable;
    }

    public NettyPoolHandler(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
        this.observable = new NettyHandlerObservable();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws DataAsException
    {
        String key = ip + ":" + port;
        StaticNettyModel model = new StaticNettyModel();
        model.setCtx(ctx);
        model.setIp(ip);
        model.setPort(port);
        model.setResult(new String());
        model.setHandler(this);
        NettyPool.getNETTYPOOL().put(key, model);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException
    {
        String data = new String((byte[]) msg, "utf-8");
        ReferenceCountUtil.release(msg);
        this.observable.changed(data);
        this.observable.deleteObservers();
    }

}
