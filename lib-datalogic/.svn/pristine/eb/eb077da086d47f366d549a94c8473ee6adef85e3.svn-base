package com.goldwind.datalogic.socket.netty;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.handler.SyncUUIDHandler;
import com.goldwind.datalogic.utils.NetCommDef;

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

public class SyncUUIDClient
{

    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(NettyClient.class);

    /**
     * 发送socket数据
     *
     * @param data           数据
     * @param ip             目标IP
     * @param port           目标端口
     * @param delimiter      分割符
     * @param timeout        超时时间
     * @param stripDelimiter 是否去除分割符
     * @param uuid           通道唯一码
     * @return 返回结果
     * @throws DataAsException 自定义异常
     */
    public static String sendData(String data, String ip, int port, String delimiter, boolean stripDelimiter, long timeout, String uuid) throws DataAsException
    {
        String tmp = "";
        byte[] byteArray = SyncUUIDClient.sendMessage(data.getBytes(), ip, port, delimiter, stripDelimiter, timeout, uuid);
        String backData = "";
        try
        {
            if (byteArray != null)
            {
                backData = new String(byteArray, "UTF-8");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error(e);
        }
        if ("unconnected".equalsIgnoreCase(backData))
        {
            DataAsExpSet.throwExpByResCode("NetCommFunc_sendData_1", new String[] { "data", "ip", "port" }, new Object[] { data, ip, port }, null);
        }
        else
        {
            try
            {
                if (NetCommDef.isSENDTOISOLATION())
                {
                    if (NetCommDef.NETISOLATIONSUC[0] == backData.getBytes()[0])
                    {
                        tmp = NetCommDef.NETISOLATSUCFLG;
                    }
                    else
                    {
                        tmp = NetCommDef.NETISOLATERRFLAG;
                    }
                }
                else
                {
                    tmp = backData;
                }
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("NetCommFunc_sendData_2", new String[] { "data", "ip", "port" }, new Object[] { data, ip, port }, e);
            }
        }
        return tmp;
    }

    /**
     * 向服务端发送一条需要响应的消息
     *
     * @param message        消息
     * @param ip             服务器IP地址
     * @param port           服务器端口号
     * @param delimiter      分割符
     * @param stripDelimiter 是否去除分割符
     * @param timeout        超时时间
     * @param uuid           通道唯一码
     * @return 响应消息
     */
    public static byte[] sendMessage(byte[] message, String ip, int port, String delimiter, boolean stripDelimiter, long timeout, String uuid)
    {
        List<Object> result = new ArrayList<>();
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>()
        {
            @Override public void initChannel(SocketChannel socketChannel)
            {
                ChannelPipeline pipeline = socketChannel.pipeline();
                if (delimiter != null && !delimiter.equals(""))
                {
                    pipeline.addLast(new DelimiterBasedFrameDecoder(100 * 1024 * 1024, stripDelimiter, Unpooled.copiedBuffer(delimiter.getBytes())));
                }
                pipeline.addLast(new ByteArrayEncoder());
                pipeline.addLast(new ByteArrayDecoder());
                pipeline.addLast(new SyncUUIDHandler(message, result, uuid));
            }
        });
        try
        {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            channel.closeFuture().await(timeout);
        }
        catch (Exception e)
        {
            result.add("unconnected".getBytes());
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
