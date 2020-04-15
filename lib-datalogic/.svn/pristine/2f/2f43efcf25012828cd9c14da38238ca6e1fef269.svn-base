package com.goldwind.datalogic.business.model;

/**
 * 控制指令故障码
 * 
 * @author 冯春源
 *
 */
public class ControlCode
{
    /**
     * 设备类型ID
     */
    private int devicetype;
    /**
     * 故障码
     */
    private String faultcode;
    /**
     * 中文描述
     */
    private String cn;
    /**
     * 英文描述
     */
    private String en;
    
    public ControlCode() 
    {
        
    }

    public int getDevicetype()
    {
        return devicetype;
    }

    public void setDevicetype(int devicetype)
    {
        this.devicetype = devicetype;
    }

    public String getFaultcode()
    {
        return faultcode;
    }

    public void setFaultcode(String faultcode)
    {
        this.faultcode = faultcode;
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

    /**
     * 依据传进的参数获取中英文解释
     * 
     * @param language
     *            语言
     * @return 翻译好的控制指令故障码
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

    @Override
    public String toString()
    {
        return "ControlCode [devicetype=" + devicetype + ", faultcode=" + faultcode + ", cn=" + cn + ", en=" + en + "]";
    }

}
