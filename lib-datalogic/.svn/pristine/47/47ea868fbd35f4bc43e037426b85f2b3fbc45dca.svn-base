package com.goldwind.datalogic.socket.netty;

import java.util.HashMap;
import java.util.Map;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.handler.NettyPoolHandler;
import com.goldwind.datalogic.socket.model.StaticNettyModel;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/*
 * Netty自定义连接池
 * */
public class NettyPool
{
    /*
     * 静态连接池 key:ip:port value:连接模型
     */
    private static Map<String, StaticNettyModel> NETTYPOOL = new HashMap<>();

    public static void setNETTYPOOL(Map<String, StaticNettyModel> NETTYPOOL)
    {
        NettyPool.NETTYPOOL = NETTYPOOL;
    }

    public static Map<String, StaticNettyModel> getNETTYPOOL()
    {
        return NETTYPOOL;
    }

    /*
     * 创建client
     */
    public static void CreatNettyPoolClient(String ip, int port, String delimiter, boolean stripDelimiter)
    {
        Thread NettyClient = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
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
                        pipeline.addLast(new NettyPoolHandler(ip, port));
                    }
                });
                try
                {
                    Channel channel = bootstrap.connect(ip, port).sync().channel();
                    channel.closeFuture().sync();
                }
                catch (Exception e)
                {
                }
                finally
                {
                    group.shutdownGracefully();
                }
            }
        });
        NettyClient.start();
    }

    /**
     * 关闭cLient
     *
     * @param ip
     *            ip
     * @param port
     *            端口
     * @return 成功标识
     * @throws DataAsException
     *             异常
     */
    public static boolean CloseTcpCon(String ip, int port) throws DataAsException
    {
        boolean result = false;
        try
        {
            String key = ip + ":" + port;
            StaticNettyModel model = NettyPool.getNETTYPOOL().get(key);
            if (model != null)
            {
                model.getCtx().close();
                model.getHandler().getObservable().deleteObservers();
            }
            NettyPool.getNETTYPOOL().remove(key);
            result = true;
        }
        catch (Exception e)
        {
            throw e;
        }
        return result;
    }

}
