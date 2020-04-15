/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: FifteenData.java 
 * @Prject: SCADA_lib-datalogic
 * @Package: com.goldwind.datalogic.business.model 
 * @Description: 十五分钟模型
 * @author: Administrator   
 * @date: 2019年5月24日 下午1:39:56 
 * @version: V1.0   
 */
package com.goldwind.datalogic.business.model;

import com.goldwind.datalogic.business.BusinessDef.DataBaseType;

/**
 * @ClassName: FifteenData
 * @Description: 十五分钟数据模型
 * @author czr
 * @date: 2019年5月24日 下午1:39:56
 */
public class FifteenDataModel
{
    /**
     * 风场id
     */
    private int wfId;
    /**
     * 时间
     */
    private String rectime;
    /**
     * 风速(15分钟整点的1分钟平均风速)
     */
    private double windSpeed;
    /**
     * 实际发电功率（15分钟整点的1分钟合计功率）
     */
    private double realPower;
    /**
     * 实际发电功率变化率(%)（（当时15分钟合计实际功率-上一15分钟时刻合计实际功率）/上一15分钟时刻合计实际功率）
     */
    private Double realPowerRate;
    /**
     * 限电功率（15分钟整点的1分钟合计理论功率（theorypower）-15分钟整点的1分钟合计实际功率（realpower））
     */
    private double limitPower;
    /**
     * 可用发电功率（15分钟整点的1分钟理论功率（theorypower））
     */
    private double usePower;
    /**
     * 温度（15分钟整点的1分钟平均温度（envitemp））
     */
    private double enviTemp;
    /**
     * 限电标识（15分钟整点的1分钟限电状态（limitStatus））
     */
    private String limitStatus;

    public int getWfId()
    {
        return wfId;
    }

    public void setWfId(int wfId)
    {
        this.wfId = wfId;
    }

    public String getRectime()
    {
        return rectime;
    }

    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public double getWindSpeed()
    {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed)
    {
        this.windSpeed = windSpeed;
    }

    public double getRealPower()
    {
        return realPower;
    }

    public void setRealPower(double realPower)
    {
        this.realPower = realPower;
    }

    public Double getRealPowerRate()
    {
        return realPowerRate;
    }

    public void setRealPowerRate(Double realPowerRate)
    {
        this.realPowerRate = realPowerRate;
    }

    public double getLimitPower()
    {
        return limitPower;
    }

    public void setLimitPower(double limitPower)
    {
        this.limitPower = limitPower;
    }

    public double getUsePower()
    {
        return usePower;
    }

    public void setUsePower(double usePower)
    {
        this.usePower = usePower;
    }

    public double getEnviTemp()
    {
        return enviTemp;
    }

    public void setEnviTemp(double enviTemp)
    {
        this.enviTemp = enviTemp;
    }

    /**
     * @Title: deepClone
     * @Description: 深度拷贝
     * @return 十五分钟数据
     * @return: FifteenDataModel十五分钟数据类型
     */
    public FifteenDataModel deepClone()
    {
        FifteenDataModel fifteenDataModelClone = new FifteenDataModel();
        fifteenDataModelClone.setEnviTemp(this.getEnviTemp());
        fifteenDataModelClone.setLimitPower(this.getLimitPower());
        fifteenDataModelClone.setLimitStatus(this.getLimitStatus());
        fifteenDataModelClone.setRealPower(this.getRealPower());
        fifteenDataModelClone.setRealPowerRate(this.getRealPowerRate());
        fifteenDataModelClone.setRectime(this.getRectime());
        fifteenDataModelClone.setUsePower(this.getUsePower());
        fifteenDataModelClone.setWfId(this.getWfId());
        fifteenDataModelClone.setWindSpeed(this.getWindSpeed());
        return fifteenDataModelClone;
    }

    /**
     * 获取insert语句
     * 
     * @Title: getInsertSql
     * @Description: 获取insert语句先删后插
     * @return sql语句
     * @return: String 返回sql语句
     */
    public String getInsertSql()
    {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from " + DataBaseType.FIFTEENDATA.getValue().toLowerCase() + " where wfid = " + this.getWfId() + " and rectime ='" + this.getRectime() + "';");
        sql.append("insert into " + DataBaseType.FIFTEENDATA.getValue().toLowerCase() + "(wfid, rectime, windspeed, realpower, realpowerrate, limitpower, usepower, envitemp, limitstatus) values (")
                .append(this.getWfId() + ", '" + this.getRectime() + "'," + this.getWindSpeed() + "," + this.getRealPower() + "," + this.getRealPowerRate() + "," + this.getLimitPower() + ","
                        + this.getUsePower() + "," + this.getEnviTemp());
        if (this.getLimitStatus() != null)
        {
            sql.append(",'" + this.getLimitStatus() + "'");
        }
        else
        {
            sql.append("," + this.getLimitStatus());
        }
        sql.append(")");
        return sql.toString();

    }

    public String getLimitStatus()
    {
        return limitStatus;
    }

    public void setLimitStatus(String limitStatus)
    {
        this.limitStatus = limitStatus;
    }
}
