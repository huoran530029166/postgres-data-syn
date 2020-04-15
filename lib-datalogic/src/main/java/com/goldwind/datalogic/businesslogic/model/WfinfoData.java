package com.goldwind.datalogic.businesslogic.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风场 信息 包含 测风塔 风机 风机对应的协议
 * 
 * @author 谭璟
 *
 */
public class WfinfoData
{
    /**
     * 风场
     */
    private String wfid = "";

    /**
     * 测风塔
     */
    private List<String> windTowerList = new ArrayList<>();

    /**
     * 风机
     */
    private List<String> windTurbineList = new ArrayList<>();

    /**
     * 风机对应协议
     */
    private Map<String, String> wtidProtocolidHash = new HashMap<>();

    /**
     * 录入功率 曲线
     */
    private Map<Double, Double> hashpower = new HashMap<>();

    /**
     * 风机对应的功率
     */
    private Map<String, Double> wtidpower = new HashMap<>();

    /**
     * 默认构造函数
     */
    public WfinfoData()
    {

    }

    public WfinfoData(String wfid)
    {
        this.wfid = wfid;
    }

    public String getWfid()
    {
        return wfid;
    }

    public void setWfid(String wfid)
    {
        this.wfid = wfid;
    }

    public List<String> getWindTowerList()
    {
        return windTowerList;
    }

    public void setWindTowerList(List<String> listwind)
    {
        this.windTowerList = listwind;
    }

    public List<String> getWindTurbineList()
    {
        return windTurbineList;
    }

    public void setWindTurbineList(List<String> listwtid)
    {
        this.windTurbineList = listwtid;
    }

    public Map<String, String> getWtidProtocolidHash()
    {
        return wtidProtocolidHash;
    }

    public void setWtidProtocolidHash(Map<String, String> hash)
    {
        this.wtidProtocolidHash = hash;
    }

    public Map<Double, Double> getHashpower()
    {
        return hashpower;
    }

    public void setHashpower(Map<Double, Double> hashpower)
    {
        this.hashpower = hashpower;
    }

    public Map<String, Double> getWtidpower()
    {
        return wtidpower;
    }

    public void setWtidpower(Map<String, Double> wtidpower)
    {
        this.wtidpower = wtidpower;
    }
}
