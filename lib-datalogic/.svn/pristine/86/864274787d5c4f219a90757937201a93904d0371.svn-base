package com.goldwind.datalogic.socket.handler;

import java.util.List;

import com.goldwind.dataaccess.exception.DataAsException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 同步消息处理器
 * 
 * @author 张超
 *
 */
public class SyncHandler extends ChannelInboundHandlerAdapter
{
    /**
     * 发送消息
     */
    private byte[] message;
    /**
     * 返回字节消息
     */
    private List<Object> result;

    public SyncHandler(byte[] message, List<Object> result)
    {
        this.message = message;
        this.result = result;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws DataAsException
    {
        ctx.channel().writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws DataAsException
    {
        result.add(msg);
        ReferenceCountUtil.release(msg);
        ctx.close();
    }
}