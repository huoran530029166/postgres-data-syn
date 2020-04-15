package com.goldwind.datalogic.business.model;

/**
 * 用户级配置信息
 * 
 * @author 冯春源
 *
 */
public class UsercnfInfo
{

    /**
     * 风速超限值
     */
    private String windSpeedLimit;
    /**
     * 功率超限值
     */
    private String powerLimit;
    /**
     * 功率不到位值
     */
    private String powerLackValue;
    /**
     * 告警频率
     */
    private String alarmPeriod;
    /**
     * 有功计划值IEC路径
     */
    private String planValueIecPath;
    /**
     * 变流器有功功率IEC路径
     */
    private String windPowerIecPath;
    /**
     * 超限预警开关
     */
    private Boolean flg;
    /**
     * 升压站遥控同时验证返回和变位信息开关
     */
    private Boolean transSubstation;

    public String getWindSpeedLimit()
    {
        return windSpeedLimit;
    }

    public void setWindSpeedLimit(String windSpeedLimit)
    {
        this.windSpeedLimit = windSpeedLimit;
    }

    public String getPowerLimit()
    {
        return powerLimit;
    }

    public void setPowerLimit(String powerLimit)
    {
        this.powerLimit = powerLimit;
    }

    public String getPowerLackValue()
    {
        return powerLackValue;
    }

    public void setPowerLackValue(String powerLackValue)
    {
        this.powerLackValue = powerLackValue;
    }

    public String getAlarmPeriod()
    {
        return alarmPeriod;
    }

    public void setAlarmPeriod(String alarmPeriod)
    {
        this.alarmPeriod = alarmPeriod;
    }

    public String getPlanValueIecPath()
    {
        return planValueIecPath;
    }

    public void setPlanValueIecPath(String planValueIecPath)
    {
        this.planValueIecPath = planValueIecPath;
    }

    public String getWindPowerIecPath()
    {
        return windPowerIecPath;
    }

    public void setWindPowerIecPath(String windPowerIecPath)
    {
        this.windPowerIecPath = windPowerIecPath;
    }

    public Boolean getFlg()
    {
        return flg;
    }

    public void setFlg(Boolean flg)
    {
        this.flg = flg;
    }

    public Boolean getTransSubstation()
    {
        return transSubstation;
    }

    public void setTransSubstation(Boolean transSubstation)
    {
        this.transSubstation = transSubstation;
    }

    public UsercnfInfo(String windSpeedLimit, String powerLimit, String powerLackValue, String alarmPeriod, String planValueIecPath, String windPowerIecPath, String flg, String transSubstation)
    {
        this.windSpeedLimit = windSpeedLimit;
        this.powerLimit = powerLimit;
        this.powerLackValue = powerLackValue;
        this.alarmPeriod = alarmPeriod;
        this.planValueIecPath = planValueIecPath;
        this.windPowerIecPath = windPowerIecPath;
        this.flg = ("1".equals(flg)) ? true : false;
        this.transSubstation = ("0".equals(transSubstation)) ? false : true;
    }
}
