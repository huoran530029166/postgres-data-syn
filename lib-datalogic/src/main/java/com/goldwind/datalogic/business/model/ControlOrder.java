package com.goldwind.datalogic.business.model;

/**
 * 控制指令实体类
 *
 * @author 冯春源
 */
public class ControlOrder
{
    /**
     * 控制指令标识
     */
    private String orderflag;
    /**
     * 协议号
     */
    private int protocolid;
    /**
     * 指令中文描述
     */
    private String cn;
    /**
     * 指令英文描述
     */
    private String en;
    /**
     * 指令名称 返回自适应中英文描述
     */
    // private String name;
    /**
     * 参数中文描述
     */
    private String paramcn;
    /**
     * 参数英文描述
     */
    private String paramen;
    /**
     * 参数描述 返回自适应中英文描述
     */
    // private String param;
    /**
     * 控制指令类型
     */
    private int ordertype;

    /**
     * 依据传进的参数获取中英文解释
     * 
     * @param language
     *            语言
     * @return 翻译好的控制指令描述
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

    public String getOrderflag()
    {
        return orderflag;
    }

    public void setOrderflag(String orderflag)
    {
        this.orderflag = orderflag;
    }

    public int getProtocolid()
    {
        return protocolid;
    }

    public void setProtocolid(int protocolid)
    {
        this.protocolid = protocolid;
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

    public String getParamcn()
    {
        return paramcn;
    }

    public void setParamcn(String paramcn)
    {
        this.paramcn = paramcn;
    }

    public String getParamen()
    {
        return paramen;
    }

    public void setParamen(String paramen)
    {
        this.paramen = paramen;
    }

    public int getOrdertype()
    {
        return ordertype;
    }

    public void setOrdertype(int ordertype)
    {
        this.ordertype = ordertype;
    }
}
