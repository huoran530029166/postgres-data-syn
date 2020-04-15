package com.goldwind.datalogic.utils;

import java.net.Socket;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.socket.netty.SyncClient;

/**
 * 网络通信工具集
 *
 * @author 曹阳
 */
public class NetCommFunc
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(NetCommFunc.class);

    /**
     * 检查用户数据
     *
     * @param dataArr
     *            用户数据数组
     * @throws DataAsException
     *             自定义异常
     */
    public static void checkUserData(String[] dataArr) throws DataAsException
    {
        for (int i = 0; i < dataArr.length; i++)
        {
            String data = dataArr[i];
            if (data == null || data.indexOf('$') >= 0 || data.indexOf('|') >= 0 || data.indexOf('^') >= 0 || data.indexOf('~') >= 0)
            {
                DataAsExpSet.throwExpByResCode("NetCommFunc_checkUserData_1", new String[] { "data" }, new Object[] { data }, null);
            }
        }
    }

    /**
     * 发送socket数据
     *
     * @param data
     *            数据
     * @param ip
     *            目标IP
     * @param port
     *            目标端口
     * @param delimiter
     *            分割符
     * @param timeout
     *            超时时间
     * @return 返回结果
     * @throws DataAsException
     *             自定义异常
     */
    public static String sendData(String data, String ip, int port, String delimiter, long timeout) throws DataAsException
    {
        String backData = SyncClient.sendMessage(data, ip, port, delimiter, timeout);
        if ("unconnected".equalsIgnoreCase(backData))
        {
            DataAsExpSet.throwExpByResCode("NetCommFunc_sendData_1", new String[] { "data", "ip", "port" }, new Object[] { data, ip, port }, null);
        }
        return backData;
    }

    /**
     * 发送socket数据（只带请求数据超时时间）
     *
     * @param data
     *            数据
     * @param ip
     *            目标IP
     * @param port
     *            目标端口
     * @param delimiter
     *            分割符
     * @param timeout
     *            请求数据超时时间(单位：毫秒)
     * @param stripDelimiter
     *            是否去除分割符
     * @return 返回结果
     * @throws DataAsException
     *             自定义异常
     */
    public static String sendData(String data, String ip, int port, String delimiter, boolean stripDelimiter, long timeout) throws DataAsException
    {
        String backData = "";
        byte[] byteArray = SyncClient.sendMessage(data.getBytes(), ip, port, delimiter, stripDelimiter, timeout);
        try
        {
            if (byteArray != null)
            {
                backData = new String(byteArray, "UTF-8");
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        if ("unconnected".equalsIgnoreCase(backData))
        {
            DataAsExpSet.throwExpByResCode("NetCommFunc_sendData_1", new String[] { "data", "ip", "port" }, new Object[] { data, ip, port }, null);
        }
        return backData;
    }

    /**
     * 发送socket数据（带请求连接超时时间和请求数据超时时间）
     *
     * @param data
     *            数据
     * @param ip
     *            目标IP
     * @param port
     *            目标端口
     * @param delimiter
     *            分割符
     * @param stripDelimiter
     *            是否去除分割符
     * @param connectTimeout
     *            请求连接超时时间(单位：毫秒)
     * @param timeout
     *            请求数据超时时间(单位：毫秒)
     * @return 返回结果
     * @throws DataAsException
     *             自定义异常
     */
    public static String sendData(String data, String ip, int port, String delimiter, boolean stripDelimiter, int connectTimeout, long timeout) throws DataAsException
    {
        String backData = "";
        byte[] byteArray = SyncClient.sendMessage(data.getBytes(), ip, port, delimiter, stripDelimiter, connectTimeout, timeout);
        try
        {
            if (byteArray != null)
            {
                backData = new String(byteArray, "UTF-8");
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        if ("unconnected".equalsIgnoreCase(backData))
        {
            DataAsExpSet.throwExpByResCode("NetCommFunc_sendData_1", new String[] { "data", "ip", "port" }, new Object[] { data, ip, port }, null);
        }
        return backData;
    }

    /**
     * 检查tcp连接是否可用
     *
     * @param tcpClient
     *            TCP连接客户端
     * @return 是否可用
     */
    public static boolean checkTcpClient(Socket tcpClient)
    {
        return !(tcpClient == null || !tcpClient.isConnected());
    }

    /**
     * 关闭tcp连接
     *
     * @param tcpClient
     *            TCP连接客户端
     */
    public static void closeTcpClient(Socket tcpClient)
    {
        if (tcpClient != null)
        {
            try
            {
                tcpClient.close();
                tcpClient = null;
            }
            catch (Exception e)
            {
            }

        }
    }
}
