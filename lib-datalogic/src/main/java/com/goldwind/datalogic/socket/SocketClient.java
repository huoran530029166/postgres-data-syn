package com.goldwind.datalogic.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.handler.IMessageHandler;
import com.goldwind.datalogic.socket.netty.NettyClient;

/**
 * socket客户端
 * 
 * @author 张超
 *
 */
public class SocketClient
{
    /**
     * 连接socket服务
     * 
     * @param ip
     *            IP地址
     * @param port
     *            端口号
     * @param handler
     *            处理器
     */
    public static void connectServer(String ip, int port, IMessageHandler handler)
    {
        new Thread(new NettyClient(ip, port, handler)).start();
    }

    /**
     * 连接socket服务
     * 
     * @param ip
     *            IP地址
     * @param port
     *            端口号
     * @param handler
     *            处理器
     * @param maxFrameLength
     *            最大长度
     * @param stripDelimiter
     *            是否去除分割符
     * @param delimiter
     *            分割符
     */
    public static void connectServer(String ip, int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter)
    {
        new Thread(new NettyClient(ip, port, handler, maxFrameLength, stripDelimiter, delimiter)).start();
    }

    /**
     * 通过Udp发送消息
     * 
     * @param message
     *            消息
     * @param ip
     *            IP地址
     * @param port
     *            端口号
     * @throws DataAsException
     *             异常
     * @throws IOException
     *             异常
     */
    public static void sendUdpMessage(byte[] message, String ip, int port) throws DataAsException, IOException
    {
        DatagramSocket sendSocket = new DatagramSocket();
        try
        {
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, InetAddress.getByName(ip), port);
            sendSocket.send(sendPacket);
        }
        finally
        {
            sendSocket.close();
        }
    }
}
