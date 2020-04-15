package com.goldwind.datalogic.socket.handler;

import com.goldwind.dataaccess.exception.DataAsException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

/**
 * socket消息处理器
 * 
 * @author 张超
 *
 */
@Sharable
public abstract class IUdpMessageHandler extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws DataAsException
    {
        ByteBuf buf = ((DatagramPacket) msg).content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        messageReceived(ctx, req);
        ReferenceCountUtil.release(msg);
    }

    /**
     * 处理接收的消息
     * 
     * @param ctx
     *            通道
     * @param msg
     *            字符串消息
     */
    protected abstract void messageReceived(ChannelHandlerContext ctx, byte[] msg);// NOSONAR
}
