package com.goldwind.datalogic.socket.handler;

import com.goldwind.dataaccess.exception.DataAsException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * server端连接心跳
 * 
 * @author Wangdashu
 *
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter
{
    /**
     * 超时检查次数
     */
    private int lossConnectCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
    {
        // System.out.println("已经5秒未收到客户端的消息了！");
        if (evt instanceof IdleStateEvent)
        {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
            {
                lossConnectCount++;
                if (lossConnectCount > 1)
                {
                    // System.out.println("关闭这个不活跃通道！" + lossConnectCount);
                    ctx.channel().close();
                    ctx.close();
                }
            }
            else if (event.state() == IdleState.WRITER_IDLE)
            {
                // System.out.println("关闭这个不活跃通道！");
                // 写超时直接断开连接
                ctx.channel().close();
                ctx.close();
            }
        }
        else
        {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws DataAsException
    {
        // 设置客户端回复时循环时间为初始值
        lossConnectCount = 0;
        // System.out.println("我又活跃了");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws DataAsException
    {
        ctx.close();
    }
}
