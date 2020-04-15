package com.goldwind.datalogic.socket.netty;

import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.socket.handler.IMessageHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * netty客户端
 * 
 * @author 张超
 *
 */
public class NettyClient implements Runnable
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(NettyUdpServer.class);
    /**
     * 服务器IP地址
     */
    private String ip;
    /**
     * 服务器端口号
     */
    private int port;
    /**
     * 处理器
     */
    private IMessageHandler handler;
    /**
     * 最大长度
     */
    private int maxFrameLength;
    /**
     * 是否去除分割符
     */
    private boolean stripDelimiter;
    /**
     * 分割符
     */
    private String delimiter;

    public NettyClient(String ip, int port, IMessageHandler handler)
    {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
    }

    public NettyClient(String ip, int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter)
    {
        this.ip = ip;
        this.port = port;
        this.handler = handler;
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.delimiter = delimiter;
    }

    /**
     * 连接socket服务
     */
    public void run()
    {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                if (maxFrameLength > 0 && delimiter != null)
                {
                    pipeline.addLast(new DelimiterBasedFrameDecoder(maxFrameLength, stripDelimiter, Unpooled.copiedBuffer(delimiter.getBytes())));
                }
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(handler);
            }
        });

        try
        {
            ChannelFuture f = bootstrap.connect(ip, port).sync();
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
