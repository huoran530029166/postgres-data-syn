package com.goldwind.datalogic.business.model;

import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 数据库warnlog表映射类，将来用于统一生产warnlog报文
 * 
 * @author 33359
 *
 */
public class WarnLogBean
{
    private String systemId;

    private String levelId;

    private String recTime;

    private String wfId;

    private String deviceid;

    private String warnInfo;

    /**
     * 0 事件告警，1 状态告警开始，2 状态告警结束
     */
    private int flag;

    private String info;

    private String typeId;

    private String iecVal;

    private int objectType;

    private String stateVal;

    private String warnid;

    private int opertype;

    /**
     * 0-没被删除 1-被删除 2-屏蔽 3-数量超限 4-频繁告警
     */
    private int isdel;

    @Override
    public String toString()
    {
        return "(warnlog" + "|" + this.systemId + "|" + this.levelId + "|" + this.recTime + "|" + this.wfId + "|" + this.deviceid + "|" + this.warnInfo + "|" + this.warnid + "|" + this.flag + "|"
                + this.info + "|" + this.typeId + "|" + this.iecVal + "|" + this.objectType + "|" + this.stateVal + "|" + this.opertype + "|" + this.isdel + ")";
    }

    /**
     * @return the systemId
     */
    public String getSystemId()
    {
        return systemId;
    }

    /**
     * @param systemId
     *            the systemId to set
     */
    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }

    /**
     * @return the levelId
     */
    public String getLevelId()
    {
        return levelId;
    }

    /**
     * @param levelId
     *            告警级别 0-提示，1-警告，2-故障
     */
    public void setLevelId(String levelId)
    {
        this.levelId = levelId;
    }

    /**
     * @return the recTime
     */
    public String getRecTime()
    {
        return recTime;
    }

    /**
     * @param recTime
     *            the recTime to set
     */
    public void setRecTime(String recTime)
    {
        this.recTime = recTime;
    }

    /**
     * @return the wfId
     */
    public String getWfId()
    {
        return wfId;
    }

    /**
     * @param wfId
     *            the wfId to set
     */
    public void setWfId(String wfId)
    {
        this.wfId = wfId;
    }

    /**
     * @return the deviceid
     */
    public String getDeviceid()
    {
        return deviceid;
    }

    /**
     * @param deviceid
     *            the deviceid to set
     */
    public void setDeviceid(String deviceid)
    {
        this.deviceid = deviceid;
    }

    /**
     * @return the warnInfo
     */
    public String getWarnInfo()
    {
        return warnInfo;
    }

    /**
     * @param warnInfo
     *            the warnInfo to set
     */
    public void setWarnInfo(String warnInfo)
    {
        this.warnInfo = warnInfo;
    }

    /**
     * @return the flag
     */
    public int getFlag()
    {
        return flag;
    }

    /**
     * @param flag
     *            状态标志 0 事件，1状态 2 状态结束
     */
    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    /**
     * @return the typeId
     */
    public String getTypeId()
    {
        return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    /**
     * @return the iecVal
     */
    public String getIecVal()
    {
        return iecVal;
    }

    /**
     * @param iecVal
     *            the iecVal to set
     */
    public void setIecVal(String iecVal)
    {
        this.iecVal = iecVal;
    }

    /**
     * @return the objectType
     */
    public int getObjectType()
    {
        return objectType;
    }

    /**
     * @param objectType
     *            设置 告警对象类型，0 场站告警 1设备告警 2 其它告警。
     */
    public void setObjectType(int objectType)
    {
        this.objectType = objectType;
    }

    /**
     * @return the stateVal
     */
    public String getStateVal()
    {
        return stateVal;
    }

    /**
     * @param stateVal
     *            the stateVal to set
     */
    public void setStateVal(String stateVal)
    {
        this.stateVal = stateVal;
    }

    /**
     * @return the warnid
     */
    public String getWarnid()
    {
        return warnid;
    }

    /**
     * @param warnid
     *            the warnid to set
     */
    public void setWarnid(String warnid)
    {
        this.warnid = warnid;
    }

    /**
     * @return the info
     */
    public String getInfo()
    {
        return info;
    }

    /**
     * @param info
     *            the info to set
     */
    public void setInfo(String info)
    {
        this.info = info;
    }

    /**
     * @return the opertype
     */
    public int getOpertype()
    {
        return opertype;
    }

    /**
     * @param opertype
     *            the opertype to set
     * @throws DataAsException
     */
    public void setOpertype(int opertype) throws DataAsException
    {
        this.opertype = opertype;
        this.warnid = this.wfId + "-" + this.deviceid + "-" + DataAsFunc.parseDateTime(this.recTime).getTimeInMillis() + "-" + this.info + "-" + opertype;
    }

    /**
     * @return the isdel
     */
    public int getIsdel()
    {
        return isdel;
    }

    /**
     * @param isdel
     *            the isdel to set
     */
    public void setIsdel(int isdel)
    {
        this.isdel = isdel;
    }
}
