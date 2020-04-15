package com.goldwind.datalogic.businesslogic.model;

/**
 * 风场对应的 发电量/理论功率
 * 
 * @author 谭璟
 *
 */
public class WfidElec
{
    /**
     * 时间
     */
    private String rectime;

    /**
     * 限电时间段 发电量
     */
    private double wfidelec;

    /**
     * 限电时间段 理论发电量
     */
    private double wfidtheroy;

    public WfidElec()
    {

    }

    /**
     * 带参数构造函数
     * 
     * @param rectime
     *            时间
     * @param wfidtheroy
     *            全场理论
     * @param wfidelec
     *            全场发电量
     */
    public WfidElec(String rectime, double wfidtheroy, double wfidelec)
    {
        this.rectime = rectime;
        this.wfidtheroy = wfidtheroy;
        this.wfidelec = wfidelec;
    }

    public String getRectime()
    {
        return rectime;
    }

    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public double getWfidelec()
    {
        return wfidelec;
    }

    public void setWfidelec(double wfidelec)
    {
        this.wfidelec = wfidelec;
    }

    public double getWfidtheroy()
    {
        return wfidtheroy;
    }

    public void setWfidtheroy(double wfidtheroy)
    {
        this.wfidtheroy = wfidtheroy;
    }
}
