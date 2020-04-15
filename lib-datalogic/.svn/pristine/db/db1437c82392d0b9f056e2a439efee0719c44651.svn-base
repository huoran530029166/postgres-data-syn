package com.goldwind.datalogic.businesslogic.model;

import com.goldwind.datalogic.business.BusinessFunc;

/**
 * 计算损失电量用的一分钟数据对象
 * @author 33359 2019-9-4 11:20:59
 *
 */
public class OneDataLossElec
{
    private String wfid;
    
    /**
     * 风机
     */
    private String wtid;
    
    /**
     * 时间
     */
    private String rectime; 
    
    /**
     * 风速
     */
    private double windspeed;
    
    /**
     * 环境温度
     */
    private double envitemp;

    /**
     * 理论功率
     */
    private double theorypower;

    /**
     * 实际功率
     */
    private double realpower;

    /**
     * 自测损失电量
     */
    private double elec;

    /**
     * 实际发电量
     */
    private double realloselec;

    private String wtstatus;

    private String limitstatus;

    private String faultcode;
    
    private String stopcode;

    private String limitcode;
    
    /**
     * 空气密度
     */
    private double density;
    
    private double endelec;
    
    private double beginelec;
    
    public OneDataLossElec(String deviceid,String time) 
    {
        this.wtid=deviceid;
        this.rectime=time;
    }

    /**
     * @return the wtid
     */
    public String getWtid()
    {
        return wtid;
    }


    /**
     * @return the rectime
     */
    public String getRectime()
    {
        return rectime;
    }


    /**
     * @return the theorypower
     */
    public double getTheorypower()
    {
        return theorypower;
    }

    /**
     * @param theorypower
     *            the theorypower to set
     */
    public void setTheorypower(double theorypower)
    {
        this.theorypower = theorypower;
    }

    /**
     * @return the realpower
     */
    public double getRealpower()
    {
        return realpower;
    }

    /**
     * @param realpower
     *            the realpower to set
     */
    public void setRealpower(double realpower)
    {
        this.realpower = realpower;
    }

    /**
     * @return the losselec
     */
    public double getElec()
    {
        return elec;
    }

    /**
     * @param losselec
     *            the losselec to set
     */
    public void setElec(double losselec)
    {
        this.elec = losselec;
    }

    /**
     * @return the realloselec
     */
    public double getRealloselec()
    {
        return realloselec;
    }

    /**
     * @param realloselec
     *            the realloselec to set
     */
    public void setRealloselec(double realloselec)
    {
        this.realloselec = realloselec;
    }

    /**
     * @return the wtstatus
     */
    public String getWtstatus()
    {
        return wtstatus;
    }

    /**
     * @param wtstatus
     *            the wtstatus to set
     */
    public void setWtstatus(String wtstatus)
    {
        this.wtstatus = wtstatus;
    }

    /**
     * @return the limitstatus
     */
    public String getLimitstatus()
    {
        return limitstatus;
    }

    /**
     * @param limitstatus
     *            the limitstatus to set
     */
    public void setLimitstatus(String limitstatus)
    {
        this.limitstatus = limitstatus;
    }

    /**
     * @return the faultcode
     */
    public String getFaultcode()
    {
        return faultcode;
    }

    /**
     * @param faultcode
     *            the faultcode to set
     */
    public void setFaultcode(String faultcode)
    {
        this.faultcode = faultcode;
    }

    /**
     * @return the stopcode
     */
    public String getStopcode()
    {
        return stopcode;
    }

    /**
     * @param stopcode
     *            the stopcode to set
     */
    public void setStopcode(String stopcode)
    {
        this.stopcode = stopcode;
    }

    /**
     * @return the limitcode
     */
    public String getLimitcode()
    {
        return limitcode;
    }

    /**
     * @param limitcode
     *            the limitcode to set
     */
    public void setLimitcode(String limitcode)
    {
        this.limitcode = limitcode;
    }

    /**
     * @return the windspeed
     */
    public double getWindspeed()
    {
        return windspeed;
    }

    /**
     * @param windspeed the windspeed to set
     */
    public void setWindspeed(double windspeed)
    {
        this.windspeed = windspeed;
    }

    /**
     * @return the envitemp
     */
    public double getEnvitemp()
    {
        return envitemp;
    }

    /**
     * @param envitemp the envitemp to set
     */
    public void setEnvitemp(double envitemp)
    {
        this.envitemp = envitemp;
    }
    
    /**
     * 获取该一分钟数据使用的协议ID
     * @return
     */
    public String getProtocolId() 
    {
       return BusinessFunc.getWtProtocolidByRectime(this.getWtid(), this.getRectime());
    }

    /**
     * @return the density
     */
    public double getDensity()
    {
        return density;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density)
    {
        this.density = density;
    }

    /**
     * @return the endelec
     */
    public double getEndelec()
    {
        return endelec;
    }

    /**
     * @param endelec the endelec to set
     */
    public void setEndelec(double endelec)
    {
        this.endelec = endelec;
    }

    /**
     * @return the beginelec
     */
    public double getBeginelec()
    {
        return beginelec;
    }

    /**
     * @param beginelec the beginelec to set
     */
    public void setBeginelec(double beginelec)
    {
        this.beginelec = beginelec;
    }

    /**
     * @return the wfid
     */
    public String getWfid()
    {
        return wfid;
    }

    /**
     * @param wfid the wfid to set
     */
    public void setWfid(String wfid)
    {
        this.wfid = wfid;
    }
}
