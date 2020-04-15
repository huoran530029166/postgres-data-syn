package com.goldwind.datalogic.socket.netty;

import java.util.ArrayList;
import java.util.List;

import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.socket.handler.FrameDecoder;
import com.goldwind.datalogic.socket.handler.SyncHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * netty客户端
 * 
 * @author 张超
 *
 */
public class SyncClient
{
    /**
     * 日志
     */
    private static Log logger = Log.getLog(SyncClient.class);

    /**
     * unconnectedStr
     */
    private static String unConnectedStr = "unconnected";

    /**
     * 向服务端发送一条需要响应的消息
     * 
     * @param message
     *            消息
     * @param ip
     *            服务器IP地址
     * @param port
     *            服务器端口号
     * @param delimiter
     *            分割符
     * @param timeout
     *            超时时间
     * @return 响应消息
     */
    public static String sendMessage(String message, String ip, int port, String delimiter, long timeout)
    {
        String result = "";
        try
        {
            byte[] byteArray = sendMessage(message.getBytes(), ip, port, delimiter, true, timeout);
            if (byteArray != null)
            {
                result = new String(byteArray, "UTF-8");
            }
        }
        catch (Exception e)
        {
            logger.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    /**
     * 向服务端发送一条需要响应的消息
     * 
     * @param message
     *            消息
     * @param ip
     *            服务器IP地址
     * @param port
     *            服务器端口号
     * @param delimiter
     *            分割符
     * @param stripDelimiter
     *            是否去除分割符
     * @param timeout
     *            超时时间
     * @return 响应消息
     */
    public static byte[] sendMessage(byte[] message, String ip, int port, String delimiter, boolean stripDelimiter, long timeout)
    {
        List<Object> result = new ArrayList<>();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                if (delimiter != null && !delimiter.equals(""))
                {
                    pipeline.addLast(new DelimiterBasedFrameDecoder(100 * 1024 * 1024, stripDelimiter, Unpooled.copiedBuffer(delimiter.getBytes())));
                }
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(new SyncHandler(message, result));
            }
        });
        try
        {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            channel.closeFuture().await(timeout);
        }
        catch (Exception e)
        {
            result.add(unConnectedStr.getBytes());
        }
        finally
        {
            group.shutdownGracefully();
        }

        if (!result.isEmpty())
        {
            return (byte[]) result.get(0);
        }
        return null;
    }

    /**
     * 向服务端发送一条需要响应的消息（带请求连接超时时间和请求数据超时时间）
     * 
     * @param message
     *            消息
     * @param ip
     *            服务器IP地址
     * @param port
     *            服务器端口号
     * @param delimiter
     *            分割符
     * @param stripDelimiter
     *            是否去除分割符
     * @param timeout
     *            超时时间
     * @return 响应消息
     */
    public static byte[] sendMessage(byte[] message, String ip, int port, String delimiter, boolean stripDelimiter, int connectTimeout, long timeout)
    {
        List<Object> result = new ArrayList<>();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                if (delimiter != null && !delimiter.equals(""))
                {
                    pipeline.addLast(new DelimiterBasedFrameDecoder(100 * 1024 * 1024, stripDelimiter, Unpooled.copiedBuffer(delimiter.getBytes())));
                }
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(new SyncHandler(message, result));
            }
        });
        try
        {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            channel.closeFuture().await(timeout);
        }
        catch (Exception e)
        {
            result.add(unConnectedStr.getBytes());
        }
        finally
        {
            group.shutdownGracefully();
        }

        if (!result.isEmpty())
        {
            return (byte[]) result.get(0);
        }
        return null;
    }

    /**
     * 向服务端发送一条需要响应的消息（不带分隔符）
     * 
     * @param message
     *            消息
     * @param ip
     *            服务器IP地址
     * @param port
     *            服务器端口号
     * @param timeout
     *            超时时间
     * @return 响应消息
     */
    public static byte[] sendMessage(byte[] message, String ip, int port, long timeout)
    {
        List<Object> result = new ArrayList<>();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override
            public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new FrameDecoder());
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(new SyncHandler(message, result));
            }
        });
        try
        {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            channel.closeFuture().await(timeout);
        }
        catch (Exception e)
        {
            result.add(unConnectedStr.getBytes());
        }
        finally
        {
            group.shutdownGracefully();
        }

        if (!result.isEmpty())
        {
            return (byte[]) result.get(0);
        }
        return null;
    }
}
