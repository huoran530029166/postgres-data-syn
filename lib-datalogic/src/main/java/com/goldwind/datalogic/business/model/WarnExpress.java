package com.goldwind.datalogic.business.model;

/**
 * 定制告警计算公式配置
 * 
 * @author 冯春源
 * 
 */
public class WarnExpress
{
    /**
     * 系统id,如中央监控,基础平台
     */
    private int systemId;
    /**
     * 告警码
     */
    private String code;
    /**
     * 风场id
     */
    private int wfid;
    /**
     * 用于定制告警，触发定制告警的计算公式
     */
    private String express;
    /**
     * 参数替换值
     */
    private String configval;
    /**
     * 达到告警条件并持续一段时间则告警，单位：分钟
     */
    private String continuetime;
    /**
     * 告警周期，单位：分钟
     */
    private String warnperiod;

    public int getSystemId()
    {
        return systemId;
    }

    public void setSystemId(int systemId)
    {
        this.systemId = systemId;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getWfid()
    {
        return wfid;
    }

    public void setWfid(int wfid)
    {
        this.wfid = wfid;
    }

    public String getExpress()
    {
        return express;
    }

    public void setExpress(String express)
    {
        this.express = express;
    }

    public String getConfigval()
    {
        return configval;
    }

    public void setConfigval(String configval)
    {
        this.configval = configval;
    }

    public String getContinuetime()
    {
        return continuetime;
    }

    public void setContinuetime(String continuetime)
    {
        this.continuetime = continuetime;
    }

    public String getWarnperiod()
    {
        return warnperiod;
    }

    public void setWarnperiod(String warnperiod)
    {
        this.warnperiod = warnperiod;
    }
}
