package com.goldwind.datalogic.business.model;

import java.util.Calendar;

/**
 * 设备历史协议变化信息
 * 
 * @author 冯春源
 *
 */
public class WtHisProtocolInfo
{
    /**
     * 自增ID
     */
    private int id;

    /**
     * 风场ID
     */
    private String wfid;

    /**
     * 设备ID
     */
    private String wtid;

    /**
     * 协议编号
     */
    private String protocolId;

    /**
     * 协议生效开始时间
     */
    private Calendar begintime;

    /**
     * 协议生效结束时间
     */
    private Calendar endtime;

    /**
     * 修改类型，1 可服务工具 2 数据处理
     */
    private int updtype;

    public WtHisProtocolInfo(int id, String wfid, String wtid, String protocolId, Calendar begintime, Calendar endtime, int updtype)
    {
        this.id = id;
        this.wfid = wfid;
        this.wtid = wtid;
        this.protocolId = protocolId;
        this.begintime = begintime;
        this.endtime = endtime;
        this.updtype = updtype;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getWfid()
    {
        return wfid;
    }

    public void setWfid(String wfid)
    {
        this.wfid = wfid;
    }

    public String getWtid()
    {
        return wtid;
    }

    public void setWtid(String wtid)
    {
        this.wtid = wtid;
    }

    public String getProtocolId()
    {
        return protocolId;
    }

    public void setProtocolId(String protocolId)
    {
        this.protocolId = protocolId;
    }

    public Calendar getBegintime()
    {
        return begintime;
    }

    public void setBegintime(Calendar begintime)
    {
        this.begintime = begintime;
    }

    public Calendar getEndtime()
    {
        return endtime;
    }

    public void setEndtime(Calendar endtime)
    {
        this.endtime = endtime;
    }

    public int getUpdtype()
    {
        return updtype;
    }

    public void setUpdtype(int updtype)
    {
        this.updtype = updtype;
    }
}
