package com.goldwind.datalogic.business.model;

import java.io.Serializable;

import com.goldwind.datalogic.utils.ControlProcessDef.ReturnType;

/**
 * 拓传数据
 * 
 * @author wangruibo
 *
 */
public class ExtensionData implements Serializable
{
    /**
     * 序列化id
     */
    private static final long serialVersionUID = 1260535248208829285L;

    /**
     * 拓传后数据
     */
    private String extData;

    /**
     * 设备id
     */
    private String wtid;

    /**
     * 拓传ip
     */
    private String extIp;
    /**
     * 拓传端口
     */
    private int extPort;

    /**
     * 是否过隔离拓传(1--经过隔离；0--不经过隔离)
     */
    private int isIsolation;

    /**
     * 指令唯一码
     */
    private String controlId;

    /**
     * 是否为最后一级拓传节点
     */
    private boolean isLast;
    /**
     * 是否为流数据
     */
    private boolean stream;

    /**
     * 拓传数据
     *
     * @param extIp
     *            拓传ip
     * @param extPort
     *            拓传端口
     * @param isIsolation
     *            下级拓传是否需要经过隔离
     * @param extData
     *            拓传数据
     */
    public ExtensionData(String extIp, int extPort, int isIsolation, String extData)
    {
        this.extIp = extIp;
        this.extPort = extPort;
        this.isIsolation = isIsolation;
        this.extData = extData;
    }
    //
    // /**
    // * 拓传数据
    // *
    // * @param extIp
    // * 拓传ip
    // * @param extPort
    // * 拓传端口
    // * @param isIsolation
    // * 下级拓传是否经过隔离
    // * @param extData
    // * 拓传数据
    // * @param channelId
    // * 上级通道
    // */
    // public ExtensionData(String extIp, int extPort, int isIsolation, String extData, String channelId)
    // {
    // this.extIp = extIp;
    // this.extPort = extPort;
    // this.isIsolation = isIsolation;
    // this.extData = extData;
    // this.channelId = channelId;
    // }
    //
    // /**
    // * 拓传数据
    // *
    // * @param extIp
    // * 拓传ip
    // * @param extPort
    // * 拓传端口
    // * @param isIsolation
    // * 下级拓传是否经过隔离
    // * @param extData
    // * 拓传数据
    // * @param channelId
    // * 拓传通道
    // * @param controlId
    // * 指令唯一码
    // * @param returnType
    // * 返回类型，0代表同步返回，1代表异步返回
    // */
    // public ExtensionData(String extIp, int extPort, int isIsolation, String extData, String channelId, String controlId, ReturnType returnType)
    // {
    // this.extIp = extIp;
    // this.extPort = extPort;
    // this.isIsolation = isIsolation;
    // this.extData = extData;
    // this.channelId = channelId;
    // this.controlId = controlId;
    // this.returnType = returnType;
    // }

    /**
     * 拓传数据
     * 
     * @param extIp
     *            拓传ip
     * @param extPort
     *            拓传端口
     * @param isIsolation
     *            下级拓传是否经过隔离
     * @param extData
     *            拓传数据
     * @param channelId
     *            拓传通道
     * @param controlId
     *            指令唯一码
     * @param returnType
     *            返回类型，0代表同步返回，1代表异步返回
     * @param isLast
     *            是否为最后一级拓传节点
     */
    public ExtensionData(String extIp, int extPort, int isIsolation, String extData, String controlId, boolean isLast)
    {
        this.extIp = extIp;
        this.extPort = extPort;
        this.isIsolation = isIsolation;
        this.extData = extData;
        this.controlId = controlId;
        this.isLast = isLast;
    }

    /**
     * 拓传数据
     * 
     * @param extIp
     *            拓传ip
     * @param extPort
     *            拓传端口
     * @param isIsolation
     *            下级拓传是否经过隔离
     * @param extData
     *            拓传数据
     * @param channelId
     *            拓传通道
     * @param controlId
     *            指令唯一码
     * @param returnType
     *            返回类型，0代表同步返回，1代表异步返回
     * @param isLast
     *            是否为最后一级拓传节点
     * @param stream
     *            流数据标志
     * @param wtid
     *            设备id
     */
    public ExtensionData(String extIp, int extPort, int isIsolation, String extData, String channelId, String controlId, ReturnType returnType, boolean isLast, boolean stream, String wtid)
    {
        this.extIp = extIp;
        this.extPort = extPort;
        this.isIsolation = isIsolation;
        this.extData = extData;
        this.controlId = controlId;
        this.isLast = isLast;
        this.stream = stream;
        this.wtid = wtid;
    }

    /**
     * 拓传数据
     * 
     * @param extIp
     *            拓传ip
     * @param extPort
     *            拓传端口
     * @param isIsolation
     *            下级拓传是否经过隔离
     * @param extData
     *            拓传数据
     * @param channelId
     *            拓传通道
     * @param controlId
     *            指令唯一码
     * @param returnType
     *            返回类型，0代表同步返回，1代表异步返回
     * @param isLast
     *            是否为最后一级拓传节点
     * @param stream
     *            流数据标志
     * @param wtid
     *            设备id
     */
    public ExtensionData(String extIp, int extPort, int isIsolation, String extData, String controlId, boolean isLast, boolean stream, String wtid)
    {
        this.extIp = extIp;
        this.extPort = extPort;
        this.isIsolation = isIsolation;
        this.extData = extData;
        this.controlId = controlId;
        this.isLast = isLast;
        this.stream = stream;
        this.wtid = wtid;
    }

    public String getExtData()
    {
        return extData;
    }

    public String getExtIp()
    {
        return extIp;
    }

    public int getExtPort()
    {
        return extPort;
    }

    public int getIsIsolation()
    {
        return isIsolation;
    }

    public void setExtData(String extData)
    {
        this.extData = extData;
    }

    public void setExtIp(String extIp)
    {
        this.extIp = extIp;
    }

    public void setExtPort(int extPort)
    {
        this.extPort = extPort;
    }

    public void setIsIsolation(int isIsolation)
    {
        this.isIsolation = isIsolation;
    }

    public String getControlId()
    {
        return controlId;
    }

    public void setControlId(String controlId)
    {
        this.controlId = controlId;
    }

    public boolean isLast()
    {
        return isLast;
    }

    public void setLast(boolean isLast)
    {
        this.isLast = isLast;
    }

    public boolean isStream()
    {
        return stream;
    }

    public void setStream(boolean stream)
    {
        this.stream = stream;
    }

    public String getWtid()
    {
        return wtid;
    }

    public void setWtid(String wtid)
    {
        this.wtid = wtid;
    }
}
