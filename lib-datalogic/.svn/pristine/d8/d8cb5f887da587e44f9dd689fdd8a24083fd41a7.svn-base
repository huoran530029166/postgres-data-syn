/**
 * 
 */
package com.goldwind.datalogic.businesslogic.model;

/**
 * 按环境条件区间统计发电量-上海申能
 * 
 * @author 33359
 *
 */
public class DayRangePower
{
    /**
     * 电场id
     */
    private int wfid;

    /**
     * 风机id
     */
    private int wtid;

    /**
     * 日期
     */
    private String rectime;

    /**
     * 风速区间 或 辐照度区间
     */
    private int area;

    /**
     * 中心风速 或 中心辐照度
     */
    private double center;

    /**
     * 平均风速 或 平均辐照度
     */
    private double avgvalue;

    /**
     * 平均功率
     */
    private double avgpower;

    private double avgtheorypower;

    /**
     * 担保功率
     */
    private double tbpower;

    /**
     * 持续时间（min）
     */
    private double timelength;

    /**
     * 统计发电量
     */
    private double statisticse;

    /**
     * 标准发电量
     */
    private double standelec;

    /**
     * 理论发电量
     */
    private double theoryelec;

    /**
     * @return the wfid
     */
    public int getWfid()
    {
        return wfid;
    }

    /**
     * @param wfid
     *            the wfid to set
     */
    public void setWfid(int wfid)
    {
        this.wfid = wfid;
    }

    /**
     * @return the wtid
     */
    public int getWtid()
    {
        return wtid;
    }

    /**
     * @param wtid
     *            the wtid to set
     */
    public void setWtid(int wtid)
    {
        this.wtid = wtid;
    }

    /**
     * @return the rectime
     */
    public String getRectime()
    {
        return rectime;
    }

    /**
     * @param rectime
     *            the rectime to set
     */
    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    /**
     * @return 中心风速 或 中心辐照度
     */
    public double getCenter()
    {
        return center;
    }

    /**
     * @param center
     *            中心风速 或 中心辐照度
     */
    public void setCenter(double center)
    {
        this.center = center;
    }

    /**
     * @return 理论发电量
     */
    public double getTheoryelec()
    {
        return theoryelec;
    }

    /**
     * @param theoryelec
     *            the theoryelec to set
     */
    public void setTheoryelec(double theoryelec)
    {
        this.theoryelec = theoryelec;
    }

    /**
     * @return 标准发电量
     */
    public double getStandelec()
    {
        return standelec;
    }

    /**
     * @param standelec
     *            标准发电量
     */
    public void setStandelec(double standelec)
    {
        this.standelec = standelec;
    }

    /**
     * @return the 统计发电量
     */
    public double getStatisticse()
    {
        return statisticse;
    }

    /**
     * @param statisticse
     *            统计发电量
     */
    public void setStatisticse(double statisticse)
    {
        this.statisticse = statisticse;
    }

    /**
     * @return the 持续时间（min）
     */
    public double getTimelength()
    {
        return timelength;
    }

    /**
     * @param timelength
     *            the timelength to set
     */
    public void setTimelength(double timelength)
    {
        this.timelength = timelength;
    }

    /**
     * @return the 担保功率
     */
    public double getTbpower()
    {
        return tbpower;
    }

    /**
     * @param tbpower
     *            the tbpower to set
     */
    public void setTbpower(double tbpower)
    {
        this.tbpower = tbpower;
    }

    /**
     * @return the 平均风速 或 平均辐照度
     */
    public double getAvgvalue()
    {
        return avgvalue;
    }

    /**
     * @param 平均风速
     *            或 平均辐照度 the avgvalue to set
     */
    public void setAvgvalue(double avgvalue)
    {
        this.avgvalue = avgvalue;
    }

    /**
     * @return the 平均功率
     */
    public double getAvgpower()
    {
        return avgpower;
    }

    /**
     * @param 平均功率
     *            the avgpower to set
     */
    public void setAvgpower(double avgpower)
    {
        this.avgpower = avgpower;
    }

    /**
     * @return 风速区间 或 辐照度区间
     */
    public int getArea()
    {
        return area;
    }

    /**
     * @param area
     *            风速区间 或 辐照度区间
     */
    public void setArea(int area)
    {
        this.area = area;
    }

    public String getInsertSql()
    {
        String tbpowerString = tbpower < 0 ? "null" : String.valueOf(tbpower);
        String standelecString = tbpower < 0 ? "null" : String.valueOf(standelec);
        return "INSERT INTO public.day_range_power" + "(wfid, wtid, rectime, area, center, avgvalue,  avgpower,avgtheorypower, tbpower, timelength, statisticselec, standelec, theoryelec, createtime)"
                + "VALUES(" + this.wfid + ", " + this.wtid + ", '" + this.rectime + "', " + this.area + ", " + this.center + ", " + this.avgvalue + "," + this.avgpower + "," + this.avgtheorypower
                + ", " + tbpowerString + ", " + this.timelength + ", " + this.statisticse + ", " + standelecString + ", " + this.theoryelec + ", now());";
    }

    /**
     * @return the avgtheorypower
     */
    public double getAvgtheorypower()
    {
        return avgtheorypower;
    }

    /**
     * @param avgtheorypower
     *            the avgtheorypower to set
     */
    public void setAvgtheorypower(double avgtheorypower)
    {
        this.avgtheorypower = avgtheorypower;
    }

}
