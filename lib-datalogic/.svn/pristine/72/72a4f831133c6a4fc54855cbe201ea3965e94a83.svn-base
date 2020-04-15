package com.goldwind.datalogic.business.model;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.StringUtil;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.BusinessFunc;

/**
 * 设备信息
 * 
 * @author 王瑞博
 *
 */
public class DeviceInfo
{
    /**
     * 风场编号
     */
    private String wfId;

    /**
     * 风机编号
     */
    private String id;

    /**
     * 风机类型
     */
    private String wttype;

    /**
     * 风机名称
     */
    private String name;

    /**
     * 协议编号
     */
    private String protocolId;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 上级设备
     */
    private String parentId;

    /**
     * 前置机ip
     */
    private String preAdapterIp;

    /**
     * 前置机用户名
     */
    private String preAdapterUser;

    /**
     * 前置机密码
     */
    private String preAdapterPassword;

    /**
     * 前置机所属服务的ip
     */
    private String serviceIp;

    /**
     * 服务端口
     */
    private int servicePort;

    /**
     * plc的ip
     */
    private String plcIp;

    /**
     * plc的用户名
     */
    private String plcName;

    /**
     * plc用户
     */
    private String plcUser;

    /**
     * plc密码
     */
    private String plcPassword;

    /**
     * 协议中正常状态
     */
    private String normalState;

    /**
     * plc类型（102为电能表协议）
     */
    private String plcflag;

    /**
     * 设备额定功率
     */
    private String standardpower;
    /**
     * 天润风场
     */
    private String trwfid;
    /**
     * 天润风机
     */
    private String trwtid;
    /**
     * 类型分组(光伏：组串式还是集中式)
     */
    private String wttypedefine;

    /**
     * 前置端口id
     */
    private String portId;

    /**
     * 前置端口名称
     */
    private String portName;

    /**
     * 前置端口内容
     */
    private String portContent;

    /**
     * 添加空参构造函数
     */
    public DeviceInfo()
    {

    }

    public DeviceInfo(String wfId, String id, String wttype, String name, String protocolId, String deviceType, String parentId, String preAdapterIp, String preAdapterUser, String preAdapterPassword,
            String serviceIp, String servicePort, String plcIp, String plcUser, String plcPassword, String normalState, String standardpower) throws DataAsException
    {
        try
        {
            this.wfId = wfId;
            this.id = id;
            this.wttype = wttype;
            this.name = name;
            this.protocolId = protocolId;
            this.deviceType = deviceType;
            this.parentId = parentId;
            this.preAdapterIp = preAdapterIp;
            this.preAdapterUser = preAdapterUser;
            this.preAdapterPassword = preAdapterPassword;
            this.serviceIp = serviceIp;
            this.servicePort = (servicePort == null) || (servicePort).isEmpty() ? -1 : Integer.parseInt(servicePort);
            this.plcIp = plcIp;
            this.plcUser = plcUser;
            this.plcPassword = plcPassword;
            this.normalState = normalState;
            this.setStandardpower(standardpower);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DeviceInfo_DeviceInfo_1", new String[] { "id", "deviceType", "servicePort" }, new Object[] { id, deviceType, servicePort }, e);
        }
    }

    public DeviceInfo(String wfId, String id, String wttype, String name, String protocolId, String deviceType, String parentId, String preAdapterIp, String preAdapterUser, String preAdapterPassword,
            String serviceIp, String servicePort, String plcIp, String plcUser, String plcPassword, String normalState, String standardpower, String plcflag, String trwfid, String trwtid,
            String wttypedefine) throws DataAsException
    {
        try
        {
            this.wfId = wfId;
            this.id = id;
            this.wttype = wttype;
            this.name = name;
            this.protocolId = protocolId;
            this.deviceType = deviceType;
            this.parentId = parentId;
            this.preAdapterIp = preAdapterIp;
            this.preAdapterUser = preAdapterUser;
            this.preAdapterPassword = preAdapterPassword;
            this.serviceIp = serviceIp;
            this.servicePort = (servicePort == null) || (servicePort).isEmpty() ? -1 : Integer.parseInt(servicePort);
            this.plcIp = plcIp;
            this.plcUser = plcUser;
            this.plcPassword = plcPassword;
            this.normalState = normalState;
            this.setStandardpower(standardpower);
            this.plcflag = plcflag;
            this.trwfid = trwfid;
            this.trwtid = trwtid;
            this.wttypedefine = wttypedefine;
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DeviceInfo_DeviceInfo_1", new String[] { "id", "deviceType", "servicePort" }, new Object[] { id, deviceType, servicePort }, e);
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

    public String getId()
    {
        return id;
    }

    public void setWtType(String wttype)
    {
        this.wttype = wttype;
    }

    public String getWtType()
    {
        return wttype;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getWtInfoProtocolId()
    {
        return protocolId;
    }

    public void setWtInfoProtocolId(String protocolId)
    {
        this.protocolId = protocolId;
    }

    /**
     * 根据数据时间获取该条数据对应的协议编号
     * 
     * @param rectime
     *            时间(传入参数为null或空，则返回wtinfo表中对应协议id)
     * @return 协议编号
     */
    public String getProtocolId(String rectime)
    {
        String protocolID= BusinessFunc.getWtProtocolidByRectime(this.id, rectime);
        if (StringUtil.hasText(protocolID))
        {
            return protocolID;
        }
        return this.protocolId;
    }

    /**
     * 根据数据时间 判断该条数据对应的协议编号是否时最新协议
     * 
     * @param rectime
     *            时间(传入参数为null或空，则返回false)
     * @return 协议编号
     */
    public boolean isNewProtocolId(String rectime)
    {
        return BusinessFunc.isNewProtocolidByRectime(this.id, rectime);
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getPreAdapterIp()
    {
        return preAdapterIp;
    }

    public void setPreAdapterIp(String preAdapterIp)
    {
        this.preAdapterIp = preAdapterIp;
    }

    public String getPreAdapterUser()
    {
        return preAdapterUser;
    }

    public void setPreAdapterUser(String preAdapterUser)
    {
        this.preAdapterUser = preAdapterUser;
    }

    public String getPreAdapterPassword()
    {
        return preAdapterPassword;
    }

    public void setPreAdapterPassword(String preAdapterPassword)
    {
        this.preAdapterPassword = preAdapterPassword;
    }

    public String getServiceIp()
    {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp)
    {
        this.serviceIp = serviceIp;
    }

    public int getServicePort()
    {
        return servicePort;
    }

    public void setServicePort(int servicePort)
    {
        this.servicePort = servicePort;
    }

    public String getPlcIp()
    {
        return plcIp;
    }

    public void setPlcIp(String plcIp)
    {
        this.plcIp = plcIp;
    }

    public String getPlcName()
    {
        return plcName;
    }

    public void setPlcName(String plcName)
    {
        this.plcName = plcName;
    }

    public String getPlcUser()
    {
        return plcUser;
    }

    public void setPlcUser(String plcUser)
    {
        this.plcUser = plcUser;
    }

    public String getPlcPassword()
    {
        return plcPassword;
    }

    public void setPlcPassword(String plcPassword)
    {
        this.plcPassword = plcPassword;
    }

    public String getNormalState()
    {
        return normalState;
    }

    public void setNormalState(String normalState)
    {
        this.normalState = normalState;
    }

    public String getStandardpower()
    {
        return standardpower;
    }

    public void setStandardpower(String standardpower)
    {
        this.standardpower = standardpower;
    }

    public String getPlcflag()
    {
        return plcflag;
    }

    public void setPlcflag(String plcflag)
    {
        this.plcflag = plcflag;
    }

    public String getTrwfid()
    {
        return trwfid;
    }

    public void setTrwfid(String trwfid)
    {
        this.trwfid = trwfid;
    }

    public String getTrwtid()
    {
        return trwtid;
    }

    public void setTrwtid(String trwtid)
    {
        this.trwtid = trwtid;
    }

    public String getWttypedefine()
    {
        return wttypedefine;
    }

    public void setWttypedefine(String wttypedefine)
    {
        this.wttypedefine = wttypedefine;
    }

    /**
     * @return the portId
     */
    public String getPortId()
    {
        return portId;
    }

    /**
     * @param portId
     *            the portId to set
     */
    public void setPortId(String portId)
    {
        this.portId = portId;
    }

    /**
     * @return the portName
     */
    public String getPortName()
    {
        return portName;
    }

    /**
     * @param portName
     *            the portName to set
     */
    public void setPortName(String portName)
    {
        this.portName = portName;
    }

    /**
     * @return the portContent
     */
    public String getPortContent()
    {
        return portContent;
    }

    /**
     * @param portContent
     *            the portContent to set
     */
    public void setPortContent(String portContent)
    {
        this.portContent = portContent;
    }
}
