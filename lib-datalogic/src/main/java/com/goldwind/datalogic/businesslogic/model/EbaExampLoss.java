package com.goldwind.datalogic.businesslogic.model;

/**
 * EBA 损失 电量 接口
 * 
 * @author 谭璟
 *
 */
public class EbaExampLoss
{
    /**
     * 风机Id
     */
    private String wtid;

    /**
     * 时间
     */
    private String rectime;

    /**
     * 停机模式字
     */
    private int stopcode;

    /**
     * 责任人ID
     */
    private int respid;

    /**
     * 损失电量
     */
    private double losselec;

    /**
     * 方案类型
     */
    private int flag;

    /**
     * 风速
     */
    private double windspeed;

    /**
     * 发电量
     */
    private double kind = 0.0;

    /**
     * 故障码
     */
    private String windfalutcode = "";

    /**
     * 默认构造函数
     */
    public EbaExampLoss()
    {

    }

    /**
     * 带参数 构造函数
     * 
     * @param wtid
     *            风机
     * @param rectime
     *            时间
     * @param stopcode
     *            停机模式字
     * @param losselec
     *            损失电量
     */
    public EbaExampLoss(String wtid, String rectime, int stopcode, double losselec)
    {
        this.wtid = wtid;
        this.rectime = rectime;
        this.stopcode = stopcode;
        this.losselec = losselec;
    }

    public String getWtid()
    {
        return wtid;
    }

    public void setWtid(String wtid)
    {
        this.wtid = wtid;
    }

    public String getRectime()
    {
        return rectime;
    }

    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public int getStopcode()
    {
        return stopcode;
    }

    public void setStopcode(int stopcode)
    {
        this.stopcode = stopcode;
    }

    public int getRespid()
    {
        return respid;
    }

    public void setRespid(int respid)
    {
        this.respid = respid;
    }

    public double getLosselec()
    {
        return losselec;
    }

    public void setLosselec(double losselec)
    {
        this.losselec = losselec;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public double getWindspeed()
    {
        return windspeed;
    }

    public void setWindspeed(double windspeed)
    {
        this.windspeed = windspeed;
    }

    public double getKind()
    {
        return kind;
    }

    public void setKind(double kind)
    {
        this.kind = kind;
    }

    public String getWindfalutcode()
    {
        return windfalutcode;
    }

    public void setWindfalutcode(String windfalutcode)
    {
        this.windfalutcode = windfalutcode;
    }

    @Override
    public String toString()
    {
        return "EbaExampLoss [wtid=" + wtid + ", rectime=" + rectime + ", stopcode=" + stopcode + ", respid=" + respid + ", losselec=" + losselec + ", flag=" + flag + ", windspeed=" + windspeed
                + ", kind=" + kind + ", windfalutcode=" + windfalutcode + "]";
    }
}
