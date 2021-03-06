package com.goldwind.datalogic.businesslogic.model;

import com.goldwind.dataaccess.DynamicRun;

public class EnergyEfficiencyDetailBean
{
    /**
     * 电场id
     */
    private int wfid;

    /*
     *设备id 
     */
    private int wtid;

    /**
     * 时间，精确到分钟
     */
    private String rectime;

    /**
     * 实际发电量
     */
    private double realelec;
    
    /**
     * 电网限电损失电量
     */
    private double powerLimitLoss=0.0D;

    /**
     * 保证电量
     */
    private double guarpow;

    private String wtstatus;
    
    /**
     * 数据是否有效
     */
    private boolean valid;
    
    /**
     * 空气密度
     */
    private double density;   
    
    private double beginelec;
    
    private double endelec;
    
    public EnergyEfficiencyDetailBean(int vWfid, int vWtid, String vRectime)
    {
        this.wfid = vWfid;
        this.wtid = vWtid;
        this.rectime = vRectime;
    }

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
     * @return the realelec
     */
    public double getRealelec()
    {
        return realelec;
    }

    /**
     * @param realelec
     *            the realelec to set
     */
    public void setRealelec(double realelec)
    {
        this.realelec = realelec;
    }

    /**
     * @return the guarpow
     */
    public double getGuarpow()
    {
        return guarpow;
    }

    /**
     * @param guarpow
     *            the guarpow to set
     */
    public void setGuarpow(double guarpow)
    {
        this.guarpow = guarpow;
    }

        
    /**
     * @return the powerLimitLoss
     */
    public double getPowerLimitLoss()
    {
        return powerLimitLoss;
    }

    
    /**
     * @param powerLimitLoss the powerLimitLoss to set
     */
    public void setPowerLimitLoss(double powerLimitLoss)
    {
        this.powerLimitLoss = powerLimitLoss;
    }      

    /**
     * 应发电量
     * @return
     */
    public double getThoryPowerElec() 
    {
        return this.realelec+this.powerLimitLoss;
    }
    
    /**
     * 能效系数
     * @return
     */
    public double getEffic() 
    {
        if(DynamicRun.isDoubleEqual(getGuarpow(), 0.0D))
        {
            return 0.0D;
        }
        else 
        {
            return getThoryPowerElec()/getGuarpow();
        }
    }
    
    @Override
    public String toString()
    {
        return "{"
                + "wfid:"+this.getWfid()
                + ",wtid:"+this.getWtid()                
                + ",rectime:"+this.getRectime()
                + ",beginelec:"+this.getBeginelec()
                + ",endelec:"+this.getEndelec()
                + ",density:"+this.getDensity()
                + ",elec:"+this.getRealelec()
                + ",limitloss:"+this.getPowerLimitLoss()
                + ",guarpow:"+this.getGuarpow()
                + ",effic:"+this.getEffic()
                + "}";
    }

    /**
     * @return the wtstatus
     */
    public String getWtstatus()
    {
        return wtstatus;
    }

    /**
     * @param wtstatus the wtstatus to set
     */
    public void setWtstatus(String wtstatus)
    {
        this.wtstatus = wtstatus;
    }

    /**
     * @return the valid
     */
    public boolean isValid()
    {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid)
    {
        this.valid = valid;
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
}
