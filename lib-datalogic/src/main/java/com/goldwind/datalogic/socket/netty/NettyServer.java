package com.goldwind.datalogic.socket.netty;

import java.util.concurrent.TimeUnit;

import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.socket.handler.FrameDecoder;
import com.goldwind.datalogic.socket.handler.HeartBeatServerHandler;
import com.goldwind.datalogic.socket.handler.IMessageHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty服务端
 * 
 * @author 张超
 *
 */
public class NettyServer implements Runnable
{
    /**
     * 错误日志
     */
    private static Log logger = Log.getLog(NettyServer.class);
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
    /**
     * 超时时间(单位：秒)
     */
    private long timeout = 60;
    /**
     * 判断端口被占用后是否退出程序
     */
    private boolean isExit = true;

    public NettyServer(int port, IMessageHandler handler)
    {
        this.port = port;
        this.handler = handler;
    }

    public NettyServer(int port, IMessageHandler handler, boolean isExit, long timeout)
    {
        this.port = port;
        this.handler = handler;
        this.isExit = isExit;
        this.timeout = timeout;
    }

    public NettyServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter)
    {
        this.port = port;
        this.handler = handler;
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.delimiter = delimiter;
    }

    public NettyServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter, boolean isExit)
    {
        this.port = port;
        this.handler = handler;
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.delimiter = delimiter;
        this.isExit = isExit;
    }

    public NettyServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter, boolean isExit, long timeout)
    {
        this.port = port;
        this.handler = handler;
        this.maxFrameLength = maxFrameLength;
        this.stripDelimiter = stripDelimiter;
        this.delimiter = delimiter;
        this.isExit = isExit;
        this.timeout = timeout;
    }

    /**
     * 启动socket服务
     */
    public void run()
    {
        try
        {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel(SocketChannel socketChannel)
                {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    // 将IdleStateHandler放在decryptHandler之前，目的是为了防止检测不到接收心跳导致踢断正常连接
                    pipeline.addLast(new IdleStateHandler(timeout, 0, 0, TimeUnit.SECONDS));

                    // 正常业务
                    if (maxFrameLength > 0 && delimiter != null)
                    {
                        pipeline.addLast(new DelimiterBasedFrameDecoder(maxFrameLength, stripDelimiter, Unpooled.copiedBuffer(delimiter.getBytes())));
                    }
                    else
                    {
                        pipeline.addLast(new FrameDecoder());
                    }
                    pipeline.addLast(new ByteArrayEncoder());
                    pipeline.addLast(new ByteArrayDecoder());
                    pipeline.addLast(handler);
                    // 心跳超时机制
                    pipeline.addLast(new HeartBeatServerHandler());
                }
            });
            ChannelFuture f = null;
            try
            {
                f = bootstrap.bind(port).sync();
                f.channel().closeFuture().sync();
            }
            catch (Exception e)
            {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
            finally
            {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
        catch (Exception w)
        {
            logger.info("Some wrong happened , program will exit. Port : " + port);
            logger.error("Some wrong happened , program will exit. Port : " + port, w);
            if (isExit)
            {
                System.exit(0);
            }
        }
    }
}
