package com.goldwind.datalogic.business.model;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.NetCommDef.WfDeviceType;

/**
 * 电场信息
 * 
 * @author 王瑞博
 *
 */
public class WfInfo
{
    /**
     * 电场编号
     */
    private String wfId;

    /**
     * 电厂名称
     */
    private String wfName;

    /**
     * 海拔
     */
    private BigDecimal height;

    /**
     * 时间差
     */
    private int timeDifference;

    /**
     * 线路
     */
    private String[] lines;

    /**
     * 功率
     */
    private String[] powers;

    /**
     * 设备列表
     */
    private ArrayList<DeviceInfo> deviceList;

    /**
     * 风机数量
     */
    private String wtcount;

    /**
     * 风场类型
     */
    private String wfType;

    /**
     * 风场信息
     * 
     * @param pWfId
     * @param pWfName
     * @param pHeight
     * @param timeDifference
     * @param pLines
     * @param powers
     * @param wtcount
     * @throws DataAsException
     *             自定义异常
     */
    public WfInfo(String pWfId, String pWfName, BigDecimal pHeight, String timeDifference, String[] pLines, String[] powers, String wtcount, String wfType) throws DataAsException
    {
        this.wfId = pWfId;
        this.wfName = pWfName;
        this.height = pHeight;
        setTimeDifference(timeDifference);
        this.lines = pLines;
        this.powers = powers;
        this.deviceList = new ArrayList<>();
        this.wtcount = wtcount;
        this.wfType = wfType;
    }

    /**
     * 是否有风机
     * 
     * @return boolean量
     */
    public boolean hasWindTurbine()
    {
        for (int i = 0; i < deviceList.size(); i++)
        {
            if (WfDeviceType.WindTurbine.toString().equals((deviceList.get(i).getDeviceType())))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置时差
     * 
     * @param timeDifference
     *            时间差
     * @throws DataAsException
     *             自定义异常
     */
    private void setTimeDifference(String timeDifference)
    {
        if (timeDifference == null || timeDifference.isEmpty())
        {
            this.timeDifference = 0;
        }
        else
        {
            this.timeDifference = Integer.parseInt(timeDifference);
        }
    }

    public String getWfId()
    {
        return wfId;
    }

    public void setWfId(String wfId)
    {
        this.wfId = wfId;
    }

    public String getWfName()
    {
        return wfName;
    }

    public void setWfName(String wfName)
    {
        this.wfName = wfName;
    }

    public BigDecimal getHeight()
    {
        return height;
    }

    public void setHeight(BigDecimal height)
    {
        this.height = height;
    }

    public int getTimeDifference()
    {
        return timeDifference;
    }

    public void setTimeDifference(int timeDifference)
    {
        this.timeDifference = timeDifference;
    }

    public String[] getLines()
    {
        return lines;
    }

    public void setLines(String[] lines)
    {
        this.lines = lines;
    }

    public String[] getPowers()
    {
        return powers;
    }

    public void setPowers(String[] powers)
    {
        this.powers = powers;
    }

    public ArrayList<DeviceInfo> getDeviceList()
    {
        return deviceList;
    }

    public void setDeviceList(ArrayList<DeviceInfo> deviceList)
    {
        this.deviceList = deviceList;
    }

    public String getWtcount()
    {
        return wtcount;
    }

    public void setWtcount(String wtcount)
    {
        this.wtcount = wtcount;
    }

    public String getWfType()
    {
        return wfType;
    }

    public void setWfType(String wfType)
    {
        this.wfType = wfType;
    }
}
