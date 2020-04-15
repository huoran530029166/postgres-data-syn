package com.goldwind.datalogic.socket;

import com.goldwind.datalogic.socket.handler.IMessageHandler;
import com.goldwind.datalogic.socket.handler.IUdpMessageHandler;
import com.goldwind.datalogic.socket.handler.IWebMessageHandler;
import com.goldwind.datalogic.socket.netty.NettyServer;
import com.goldwind.datalogic.socket.netty.NettyUdpServer;
import com.goldwind.datalogic.socket.netty.NettyWebServer;

/**
 * socket服务端
 * 
 * @author 张超
 *
 */
public class SocketServer
{
    /**
     * 启动socket服务
     * 
     * @param port
     *            端口号
     * @param handler
     *            处理器
     * @return 服务线程
     */
    public static Thread startServer(int port, IMessageHandler handler)
    {
        Thread thread = new Thread(new NettyServer(port, handler));
        thread.start();

        return thread;
    }

    /**
     * 启动socket服务
     * 
     * @param port
     *            端口号
     * @param handler
     *            处理器
     * @param isExit
     *            出现异常时是否退出
     * @param timeout
     *            心跳检查机制
     * @return 服务线程
     */
    public static Thread startServer(int port, IMessageHandler handler, boolean isExit, long timeout)
    {
        Thread thread = new Thread(new NettyServer(port, handler, isExit, timeout));
        thread.start();

        return thread;
    }

    /**
     * 启动socket服务
     * 
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
     * @return 服务线程
     */
    public static Thread startServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter)
    {
        Thread thread = new Thread(new NettyServer(port, handler, maxFrameLength, stripDelimiter, delimiter));
        thread.start();

        return thread;
    }

    /**
     * 启动socket服务
     * 
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
     * @param isExit
     *            在端口被占用时，是否退出程序
     * @param timeout
     *            超时踢断连接时间
     * @return 服务线程
     */
    public static Thread startServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter, boolean isExit, long timeout)
    {
        Thread thread = new Thread(new NettyServer(port, handler, maxFrameLength, stripDelimiter, delimiter, isExit, timeout));
        thread.start();

        return thread;
    }

    /**
     * 启动socket服务
     * 
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
     * @param isExit
     *            在端口被占用时，是否退出程序
     * @return 服务线程
     */
    public static Thread startServer(int port, IMessageHandler handler, int maxFrameLength, boolean stripDelimiter, String delimiter, boolean isExit)
    {
        Thread thread = new Thread(new NettyServer(port, handler, maxFrameLength, stripDelimiter, delimiter, isExit));
        thread.start();

        return thread;
    }

    /**
     * 启动udp服务
     * 
     * @param port
     *            端口号
     * @param handler
     *            处理器
     * @return 服务线程
     */
    public static Thread startUdpServer(int port, IUdpMessageHandler handler)
    {
        Thread thread = new Thread(new NettyUdpServer(port, handler));
        thread.start();

        return thread;
    }

    /**
     * 启动websocket服务
     * 
     * @param port
     *            端口号
     * @param handler
     *            处理器
     */
    public static void startWebServer(int port, IWebMessageHandler handler)
    {
        new Thread(new NettyWebServer(port, handler)).start();
    }
}
