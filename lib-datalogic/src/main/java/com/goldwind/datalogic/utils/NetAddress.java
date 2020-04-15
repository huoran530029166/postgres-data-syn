package com.goldwind.datalogic.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;

public class NetAddress
{
    /**
     * 输出日志
     */
    private static Log logger = Log.getLog(NetAddress.class);

    private InetAddress ip;
    /// <summary>
    /// 网络IP
    /// </summary>

    private int port;
    /// <summary>
    /// 端口
    /// </summary>

    private String address;
    /// <summary>
    /// 网络地址
    /// </summary>

    private boolean commState;
    /// <summary>
    /// 通讯状态
    /// </summary>

    public InetAddress getIp()
    {
        return ip;
    }

    public void setIp(InetAddress ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public boolean isCommState()
    {
        return commState;
    }

    public void setCommState(boolean commState)
    {
        this.commState = commState;
    }

    public NetAddress(InetAddress ip, int port) throws DataAsException
    {
        this.address = ip.toString();
        this.ip = ip;
        checkPort(port);
        this.port = port;
        this.commState = false;
    }

    public NetAddress(String address, int port) throws DataAsException
    {
        this.address = address;
        checkPort(port);
        this.port = port;
        this.commState = false;
    }

    /// <summary>
    /// 验证是否唯一
    /// </summary>
    /// <param name="netAddressList">NetAddress对象数组</param>
    /// <returns>boolean</returns>
    public static boolean checkUnique(NetAddress[] netAddressList)
    {
        for (int i = 0; i < netAddressList.length; i++)
        {
            for (int j = i + 1; j < netAddressList.length; j++)
            {
                if (netAddressList[i].toString().equals(netAddressList[j].toString()))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /// <summary>
    /// 解析地址
    /// </summary>
    /// <param name="address">地址</param>
    /// <param name="port">端口号</param>
    /// <returns>NetAddress对象数组</returns>
    public static NetAddress[] parseAddress(String address, String port) throws DataAsException
    {
        NetAddress[] val = new NetAddress[] {};
        if (address.isEmpty() && port.isEmpty())
        {
            return val;
        }

        List<NetAddress> lVal = new ArrayList<>();
        try
        {
            String[] addressArray = address.split(",");
            String[] portArray = port.split(",");

            if (addressArray.length != portArray.length)
            {
                DataAsExpSet.throwExpByMsg("2", new String[] { "address", "port" }, new Object[] { address, port }, new Exception("address and port length not same"));
            }
            for (int i = 0; i < addressArray.length; i++)
            {
                NetAddress netAddress = new NetAddress(addressArray[i], Integer.parseInt(portArray[i]));
                lVal.add(netAddress);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByMsg("2", new String[] { "address", "port" }, new Object[] { address, port }, exp);
        }
        return lVal.toArray(val);
    }

    /// <summary>
    /// 验证端口
    /// </summary>
    /// <param name="port">端口号</param>
    public static void checkPort(int port) throws DataAsException
    {
        if (port <= 0 || port > 65535)
        {
            DataAsExpSet.throwExpByMsg("1", new String[] { "port" }, new Object[] { port }, new Exception("port out of rang"));
        }
    }

    /// <summary>
    /// 生成键
    /// </summary>
    /// <param name="ip">IP地址</param>
    /// <param name="port">端口号</param>
    /// <returns>String</returns>
    public static String genKey(String ip, int port)
    {
        return ip + ":" + port;
    }

    /// <summary>
    /// 转换地址到ip
    /// </summary>
    /// <param name="errorInfo">转换错误信息</param>
    /// <returns>boolean</returns>
    public boolean addressToIp()
    {
        InetAddress destIp = null;
        try
        {
            destIp = InetAddress.getByName(this.address);
        }
        catch (UnknownHostException e)
        {
            logger.error(e);
            return false;
        }

        this.ip = destIp;
        return true;
    }

    /// <summary>
    /// 转换为字符串
    /// </summary>
    /// <returns>String</returns>
    public String toString()
    {
        return genKey(this.address, this.port);
    }

    public String toDirectory()
    {
        return this.address + "_" + port;
    }

    /**
     * 判断传入的字符串是否符合ip规范
     * 
     * @param addr
     *            需要校验的ip地址
     * @return 是否符合ip地址规范
     */
    public static boolean isIP(String addr)
    {
        if (addr == null || addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.matches();
    }
}
