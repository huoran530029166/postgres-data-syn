package com.goldwind.datalogic.socket.socketinterface;

import java.net.DatagramPacket;

/**
 * TCP数据处理接口
 * 
 * @author xjs 2019-12-6 10:02:44
 *
 */
public interface SocketDataInterface
{
    /**
     * 发送socket信息
     * 
     * @param msg
     *            数据
     * @param srcDataTime
     *            数据时间
     * @return 处理结果
     */
    public String sendTCPData(String msg, String srcDataTime);

    /**
     * 处理组播信息
     * 
     * @param msg
     *            数据信息
     * @param packet
     *            udp包
     */
    public void sendGroupData(String msg, DatagramPacket packet);
}
