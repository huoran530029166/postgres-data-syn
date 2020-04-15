package com.goldwind.datalogic.businesslogic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单台风机 损失电量bean
 * 
 * @author 谭璟
 *
 */
public class Losselec
{
    /**
     * 风场
     */
    private String wfid;

    /**
     * 风机
     */
    private String wtid;

    /**
     * 开始时间
     */
    private String starttime;

    /**
     * 结束时间
     */
    private String endtime;

    /**
     * 不同类型对应不同损失电量
     */
    private Map<Integer, Losstime> energyUseDataHash = new HashMap<>();

    /**
     * 针对 时间段数据损失电量集合
     */
    private List<Losstime> losstimeList  = new ArrayList<>();

    /**
     * 针对一分钟故障停机数据集合
     */
    private List<Losstime> energyUseOneDataList = new ArrayList<>();

    /**
     * 构造函数
     * 
     * @param wfid
     *            风场ID
     * @param wtid
     *            风机ID
     */
    public Losselec(String wfid, String wtid, String starttime, String endtime)
    {
        this.wfid = wfid;
        this.wtid = wtid;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getWfid()
    {
        return wfid;
    }

    public String getWtid()
    {
        return wtid;
    }

    /**
     * 开始时间
     */
    public String getStarttime()
    {
        return starttime;
    }

    /**
     * 结束时间
     */
    public String getEndtime()
    {
        return endtime;
    }

    /**
     * 不同类型对应不同损失电量
     */
    public Map<Integer, Losstime> getEnergyUseDataHash()
    {
        return energyUseDataHash;
    }
    
    /**
     * 不同类型对应不同损失电量
     */
    public Map<Integer, Losstime> getHashobj()
    {
        return energyUseDataHash;
    }

    /**
     * 不同类型对应不同损失电量
     */
    public void setEnergyUseDataHash(Map<Integer, Losstime> hashobj)
    {
        this.energyUseDataHash = hashobj;
    }

    /**
     * 针对 时间段数据损失电量集合
     */
    public List<Losstime> getLosstimeList()
    {
        return losstimeList;
    }

    /**
     * 针对 时间段数据损失电量集合
     */
    public List<Losstime> getListobj()
    {
        return losstimeList;
    }    
    
    /**
     * 针对 时间段数据损失电量集合
     */
    public void setLosstimeList(List<Losstime> listobj)
    {
        this.losstimeList = listobj;
    }

    @Override
    public String toString()
    {
        return "Losselec [wfid=" + wfid + ", wtid=" + wtid + ", starttime=" + starttime + ", endtime=" + endtime + "]";
    }

    /**
     * 针对一分钟故障停机数据集合
     */
    public List<Losstime> getEnergyUseOneDataList()
    {
        return energyUseOneDataList;
    }

    /**
     * 针对一分钟故障停机数据集合
     */
    public void setEnergyUseOneDataList(List<Losstime> listminobj)
    {
        this.energyUseOneDataList = listminobj;
    }    
}
