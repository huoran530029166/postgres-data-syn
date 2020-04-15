package com.goldwind.datalogic.business.model;

/**
 * 
 * @author caoyang
 *
 */
public class FormulaElement
{
    /**
     * 设备ID
     */
    private String devId;
    /**
     * iec路径
     */
    private String iecPath;

    public FormulaElement(String formula)
    {
        String[] tmp = formula.split(":");
        this.devId = tmp[0].substring(1);
        this.iecPath = tmp[1].substring(0, tmp[1].length() - 1);
    }

    /**
     * iec值
     */
    private Double value;

    public String getDevId()
    {
        return devId;
    }

    public void setDevId(String devId)
    {
        this.devId = devId;
    }

    public String getIecPath()
    {
        return iecPath;
    }

    public void setIecPath(String iecPath)
    {
        this.iecPath = iecPath;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

}
