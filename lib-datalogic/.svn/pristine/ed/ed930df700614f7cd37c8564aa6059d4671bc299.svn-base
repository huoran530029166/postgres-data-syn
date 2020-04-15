package com.goldwind.datalogic.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * 组播
 * 
 * @author 张超
 *
 */
public class Multicast
{
    /**
     * 组播端口
     */
    private MulticastSocket multicastSocket;

    /**
     * 组播地址
     */
    private InetAddress mcastaddr;

    /**
     * 组播缓冲区大小
     */
    private int buffersize = 65535;
    /**
     * 组播数据接收超时时间
     */
    private int overTime = 2000;

    /**
     * 接收的buffer
     */
    private byte[] buffer1 = new byte[buffersize];
    /**
     * 接收结果对象
     */
    private DatagramPacket datagramPacket1 = new DatagramPacket(buffer1, buffersize);
    /**
     * 编码格式
     */
    private String chartName = "utf-8";
    /**
     * 接收数据总长度
     */
    private long BUFFNUM = 0;

    public long getBuffnum()
    {
        return BUFFNUM;
    }

    /**
     * 接收数据总长度
     */
    private long buffnummax = Long.MAX_VALUE - 65536;

    public Multicast(String ip, int port) throws IOException
    {
        multicastSocket = new MulticastSocket(port);
        mcastaddr = InetAddress.getByName(ip);
        multicastSocket.joinGroup(mcastaddr);
    }

    public Multicast(String ip, int port, String inf) throws IOException
    {
        multicastSocket = new MulticastSocket(port);
        multicastSocket.setInterface(InetAddress.getByName(inf));
        mcastaddr = InetAddress.getByName(ip);
        multicastSocket.joinGroup(mcastaddr);
    }

    public Multicast(String ip, int port, int buffersize) throws IOException
    {
        multicastSocket = new MulticastSocket(port);
        mcastaddr = InetAddress.getByName(ip);
        multicastSocket.joinGroup(mcastaddr);
        this.buffersize = buffersize;
    }

    /**
     * 发送组播消息
     * 
     * @param message
     *            消息
     * @throws IOException
     *             IO异常
     */
    public void send(String message) throws IOException
    {
        byte[] buffer = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, mcastaddr, multicastSocket.getLocalPort());
        multicastSocket.send(datagramPacket);
    }

    /**
     * 接收组播消息
     * 
     * @return 消息
     * @throws IOException
     *             IO异常
     */
    public String receive() throws IOException
    {
        String message = null;
        multicastSocket.setSoTimeout(overTime);
        multicastSocket.receive(datagramPacket1);
        message = new String(datagramPacket1.getData(), 0, datagramPacket1.getLength(), chartName);
        BUFFNUM += datagramPacket1.getLength();
        if (BUFFNUM >= buffnummax)
        {
            BUFFNUM = 0;
        }
        return message;
    }

    /**
     * 接收组播消息
     * 
     * @param length
     *            最大长度
     * @return 消息
     * @throws IOException
     *             IO异常
     */
    public String receive(int length) throws IOException
    {
        String message = null;
        byte[] buffer = new byte[length];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, length);
        multicastSocket.setSoTimeout(overTime);
        multicastSocket.receive(datagramPacket);
        message = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), chartName);

        return message;
    }

    /**
     * 接收组播消息
     * 
     * @return 消息
     * @throws IOException
     *             IO异常
     */
    public DatagramPacket getData() throws IOException
    {
        byte[] buffer = new byte[buffersize];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffersize);
        multicastSocket.receive(datagramPacket);

        return datagramPacket;
    }

    /**
     * 关闭组播
     * 
     * @throws IOException
     *             IO异常
     */
    public void close() throws IOException
    {
        multicastSocket.leaveGroup(mcastaddr);
        multicastSocket.close();
    }
}
