package com.goldwind.datalogic.businesslogic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 风机对应的样板风机
 * 
 * @author 谭璟
 *
 */
public class DeviceEbaWtid
{
    /**
     * 样板风机 参照的 风机
     */
    private double windspeed;

    /**
     * 样板风机集合
     */
    private List<String> wtidList = new ArrayList<>();

    /**
     * 传递函数集合
     */
    private List<Double> functranskList = new ArrayList<>();

    public DeviceEbaWtid()
    {

    }

    /**
     * 带参数构造函数
     * 
     * @param windspeed
     *            风速
     * @param wtids
     *            风机集合
     * @param values
     *            传递函数集合
     */
    public DeviceEbaWtid(double windspeed, List<String> wtids, List<Double> values)
    {
        this.windspeed = windspeed;
        this.wtidList = wtids;
        this.functranskList = values;
    }

    public double getWindspeed()
    {
        return windspeed;
    }

    public void setWindspeed(double windspeed)
    {
        this.windspeed = windspeed;
    }

    public List<String> getWtidList()
    {
        return wtidList;
    }
    
    public List<Double> getFunctranskList()
    {
        return functranskList;
    }

}
