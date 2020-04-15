package com.goldwind.datalogic.businesslogic.model;

import com.goldwind.datalogic.business.BusinessFunc;

/**
 * 设备信息 可扩展
 * 
 * @author 谭璟
 *
 */
public class DeviceInfoObject
{
    /**
     * 风场ID
     */
    private String wfid;

    /**
     * 风机ID
     */
    private String wtid;

    /**
     * 正常标识
     */
    private String nomalstate;

    /**
     * 风机功率
     */
    private String standerpower;

    /**
     * 光伏类型
     */
    private String inverType;

    /**
     * 默认构造函数
     */
    public DeviceInfoObject()
    {

    }

    /**
     * 带参数构造函数
     * 
     * @param wfid
     *            风场
     * @param wtid
     *            风机
     * @param protocolid
     *            协议
     */
    public DeviceInfoObject(String wfid, String wtid, String nomalstate)
    {
        this.wfid = wfid;
        this.wtid = wtid;
        this.nomalstate = nomalstate;
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

    public String getProtocolid(String rectime)
    {
        return BusinessFunc.getWtProtocolidByRectime(this.wtid, rectime);
    }

    public String getNomalstate()
    {
        return nomalstate;
    }

    public void setNomalstate(String nomalstate)
    {
        this.nomalstate = nomalstate;
    }

    public String getStanderpower()
    {
        return standerpower;
    }

    public void setStanderpower(String standerpower)
    {
        this.standerpower = standerpower;
    }

    public String getInverType()
    {
        return inverType;
    }

    public void setInverType(String inverType)
    {
        this.inverType = inverType;
    }
}
