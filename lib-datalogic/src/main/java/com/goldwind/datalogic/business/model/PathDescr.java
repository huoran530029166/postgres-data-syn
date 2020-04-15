package com.goldwind.datalogic.business.model;

/**
 * 特殊IecPath指定描述对象模型
 * 
 */
public class PathDescr
{
    /**
     * 协议号
     */
    private int protocolid;
    /**
     * iec量
     */
    private String iecpath;
    /**
     * iec值
     */
    private String iecvalue;
    /**
     * 中文描述
     */
    private String cn;
    /**
     * 英文描述
     */
    private String en;
    /**
     * 类型
     */
    private String faulttype;

    // set-get方法
    public int getProtocolid()
    {
        return protocolid;
    }

    public void setProtocolid(int protocolid)
    {
        this.protocolid = protocolid;
    }

    public String getIecpath()
    {
        return iecpath;
    }

    public void setIecpath(String iecpath)
    {
        this.iecpath = iecpath;
    }

    public String getIecvalue()
    {
        return iecvalue;
    }

    public void setIecvalue(String iecvalue)
    {
        this.iecvalue = iecvalue;
    }

    public String getCn()
    {
        return cn;
    }

    public void setCn(String cn)
    {
        this.cn = cn;
    }

    public String getEn()
    {
        return en;
    }

    public void setEn(String en)
    {
        this.en = en;
    }

    public String getFaulttype()
    {
        return faulttype;
    }

    public void setFaulttype(String faulttype)
    {
        this.faulttype = faulttype;
    }

    /**
     * 依据传进的参数获取中英文解释
     * 
     * @param language
     * @return
     */
    public String getDescription(String language)
    {
        if ("en".equals(language))
        {
            return this.en;
        }
        else
        {
            return this.cn;
        }
    }

    /**
     * 覆写toString
     * 
     * @return String 返回值
     */
    @Override
    public String toString()
    {
        return "protocolid:" + this.protocolid + ";iecpath:" + this.iecpath + ";iecvalue:" + this.iecvalue + ";cn:" + this.cn + ";en:" + this.en + ";faulttype:" + this.faulttype;
    }
}
