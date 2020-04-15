package com.goldwind.datalogic.socket.model;

/**
 * socket通讯状态检查
 * 
 * @author 王瑞博
 *
 */
public class TransAd
{
    /**
     * 通讯ip
     */
    private String ip;

    /**
     * 通讯端口
     */
    private int port;

    /**
     * 是否经过隔离
     */
    private boolean isolation;
    /**
     * 是否转发原始数据
     */
    private boolean israwdata;

    /**
     * 是否转发压缩数据
     */
    private boolean iszip;

    /**
     * 转发地址model
     * 
     * @param ip
     *            转发地址
     * @param port
     *            转发端口
     * @param isolation
     *            是否过隔离
     * @param israwdata
     *            是否转发原始数据
     * @param iszip
     *            是否压缩数据
     */
    public TransAd(String ip, int port, boolean isolation, boolean israwdata, boolean iszip)
    {
        this.ip = ip;
        this.port = port;
        this.isolation = isolation;
        this.israwdata = israwdata;
        this.iszip = iszip;

    }

    /**
     * 转发地址model
     * 
     * @param ip
     *            转发地址
     * @param port
     *            转发端口
     * @param isolation
     *            是否过隔离
     */
    public TransAd(String ip, int port, boolean isolation)
    {
        this.ip = ip;
        this.port = port;
        this.isolation = isolation;
        this.israwdata = false;
        this.iszip = true;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
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

    public boolean isIsolation()
    {
        return isolation;
    }

    public void setIsolation(boolean isolation)
    {
        this.isolation = isolation;
    }

    public boolean isRawdata()
    {
        return israwdata;
    }

    public boolean iszip()
    {
        return iszip;
    }

    public void setIsrawdata(boolean israwdata)
    {
        this.israwdata = israwdata;
    }

    public void setIszip(boolean iszip)
    {
        this.iszip = iszip;
    }
}
