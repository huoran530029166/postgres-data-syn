package com.goldwind.datalogic.socket.handler;

import com.goldwind.dataaccess.exception.DataAsException;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;

/**
 * websocket消息处理器
 * 
 * @author 张超
 *
 */
@Sharable
public abstract class IWebMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws DataAsException
    {
        messageReceived(ctx, msg.text());
        ReferenceCountUtil.release(msg);
    }

    /**
     * 处理接收的消息
     * 
     * @param ctx
     *            通道
     * @param msg
     *            消息
     */
    protected abstract void messageReceived(ChannelHandlerContext ctx, String msg);// NOSONAR
}
