package com.goldwind.datalogic.businesslogic.model;

/**
 * 不同类型 损失电量
 * 
 * @author 谭璟
 *
 */
public class Losstime
{
    private String  deviceId; 
    
    /**
     * 损失电量类型
     */
    private int setid;

    /**
     * 理论电量
     */
    private double theory;

    /**
     * 实际电量
     */
    private double real;

    /**
     * 时间长
     */
    private double count;

    /**
     * 普通损失电量
     */
    private double loss;

    /**
     * 利用标杆机发电量得到的损失电量
     */
    private double bmrkloss;

    /**
     * 发电量
     */
    private double elec;

    /**
     * 时间 时间精确到一分钟
     */
    private String time;

    /**
     * 开始时间
     */
    private String startttime;

    /**
     * 结束时间
     */
    private String endtime;

    /**
     * 全场标杆机组发电量
     */
    private double wfidbmark;

    /**
     * 时间段 对应发电量
     */
    private WfidElec wfidelec;
    
    /**
     * 逆变器类型
     */
    private String inveType;

    /**
     * 构造函数
     * 
     * @param setid
     *            损失电量类型
     * @param theory
     *            理论发电量
     * @param real
     *            实际发电量
     * @param count
     *            时长
     * @param loss
     *            理论损失电量
     * @param bmrkloss
     *            实际损失电量
     */
    public Losstime(String wtid,int setid, double theory, double real, double count, double loss, double bmrkloss)
    {
        this.deviceId=wtid;
        this.setid = setid;
        this.theory = theory;
        this.real = real;
        this.count = count;
        this.loss = loss;
        this.bmrkloss = bmrkloss;
    }

    /**
     * 默认构造函数
     */
    public Losstime()
    {

    }

    /**
     * 损失电量类型
     */
    public int getSetid()
    {
        return setid;
    }

    /**
     * 损失电量类型
     */
    public void setSetid(int setid)
    {
        this.setid = setid;
    }

    /**
     * 理论电量
     */
    public double getTheory()
    {
        return theory;
    }

    /**
     * 理论电量
     */
    public void setTheory(double theory)
    {
        this.theory = theory;
    }

    /**
     * 实际电量
     */
    public double getReal()
    {
        return Math.max(0, real);      
    }

    /**
     * 实际电量
     */
    public void setReal(double real)
    {
        this.real = real;
    }

    /**
     * 时间长
     */
    public double getCount()
    {
        return this.count;
    }

    /**
     * 时间长
     */
    public void setCount(double count)
    {
        this.count = count;
    }

    /**
     * 普通损失电量
     */
    public double getLoss()
    {
        return  Math.max(0.0, loss);
    }

    /**
     * 普通损失电量
     */
    public void setLoss(double loss)
    {
        this.loss = loss;   
    }

    /**
     * 利用标杆机发电量得到的损失电量
     */
    public double getBmrkloss()
    {
        return Math.max(0.0, bmrkloss) ;
    }

    /**
     * 标杆机组损失电量
     */
    public void setBmrkloss(double bmrkloss)
    {
        this.bmrkloss = bmrkloss;            
    }

    /**
     * 时间 时间精确到一分钟
     */
    public String getTime()
    {
        return time;
    }

    /**
     * 时间 时间精确到一分钟
     */
    public void setTime(String time)
    {
        this.time = time;
    }

    /**
     * 开始时间
     */
    public String getStartttime()
    {
        return startttime;
    }

    /**
     * 开始时间
     */
    public void setStartttime(String startttime)
    {
        this.startttime = startttime;
    }

    /**
     * 结束时间
     */
    public String getEndtime()
    {
        return endtime;
    }

    /**
     * 结束时间
     */
    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    
    /**
     * 发电量
     */
    public double getElec()
    {
        return elec;
    }

    
    /**
     * set发电量
     */
    public void setElec(double elec)
    {
        this.elec = elec;
    }

    /**
     * 全场标杆机组发电量
     */
    public double getWfidbmark()
    {
        return wfidbmark;
    }

    /**
     * 全场标杆机组发电量
     */
    public void setWfidbmark(double wfidbmark)
    {
        this.wfidbmark = wfidbmark;
    }

    /**
     * 时间段 对应发电量
     */
    public WfidElec getWfidelec()
    {
        return wfidelec;
    }


    public void setWfidelec(WfidElec wfidelec)
    {
        this.wfidelec = wfidelec;
    }
    

    @Override
    public String toString()
    {
        return "Losstime [deviceid="+deviceId+",setid=" + setid + ", theory=" + theory + ", real=" + real + ", count=" + count + ", loss=" + loss + ", bmrkloss=" + bmrkloss + ", elec=" + elec + ", time=" + time
                + ", startttime=" + startttime + ", endtime=" + endtime + ", wfidbmark=" + wfidbmark + ", wfidelec=" + wfidelec + "]";
    }

    public String getInveType()
    {
        return inveType;
    }

    public void setInveType(String inveType)
    {
        this.inveType = inveType;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId()
    {
        return deviceId;
    }

    /**
     * @param deviceId the deviceId to set
     */
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
}
