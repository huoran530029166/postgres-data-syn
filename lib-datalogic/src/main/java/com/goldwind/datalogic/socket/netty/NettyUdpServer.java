package com.goldwind.datalogic.socket.netty;

import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.socket.handler.IUdpMessageHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * netty服务端
 * 
 * @author 张超
 *
 */
public class NettyUdpServer implements Runnable
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(NettyClient.class);
    /**
     * 服务器端口号
     */
    private int port;
    /**
     * 处理器
     */
    private IUdpMessageHandler handler;

    public NettyUdpServer(int port, IUdpMessageHandler handler)
    {
        this.port = port;
        this.handler = handler;
    }

    /**
     * 启动socket服务
     */
    public void run()
    {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true).handler(handler);

        try
        {
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            group.shutdownGracefully();
        }
    }
}
