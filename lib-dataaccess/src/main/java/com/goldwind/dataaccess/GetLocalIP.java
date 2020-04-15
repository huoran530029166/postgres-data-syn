package com.goldwind.dataaccess;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 获取本地IP
 * 
 * @author 冯春源
 *
 */
public class GetLocalIP
{
    /**
     * 获取本地IP
     * 
     * @throws DataAsException
     *             自定义异常
     * @return 本地IP地址
     */
    public static String getIp() throws DataAsException
    {
        String ip = "127.0.0.1";
        try
        {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            while (allNetInterfaces.hasMoreElements())
            {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp())
                {
                    continue;
                }
                else
                {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements())
                    {
                        inetAddress = addresses.nextElement();
                        if (inetAddress != null && inetAddress instanceof Inet4Address)
                        {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        }
        catch (SocketException e)
        {
            DataAsExpSet.throwExpByResCode("GetLocalIP_getIp_1", null, null, e);
        }
        return ip;
    }

}
