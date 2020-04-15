package com.goldwind.datalogic.business.model;

import java.sql.Timestamp;

/**
 * 消息过滤规则
 * 
 * @author 冯春源
 *
 */
public class NoticeSortInfo
{
    /**
     * 自增id
     */
    private String noid;
    /**
     * 告警系统id，告警一级分类，-1所有分类，0-公共平台，1-风电，3-功率控制，101-光伏，102，升压站
     */
    private String systemId;
    /**
     * 设备类型，-1-所有，0-场站告警，1-设备告警，2-其它告警
     */
    private String objecttype;
    /**
     * 告警对象(多个对象用逗号隔开)
     */
    private String objectid;
    /**
     * 设备类型，0-设备，1-电场，2-区域，3-集团
     */
    private String devicetype;
    /**
     * 告警级别，-1-所有级别，0-提示，1-警告，2-故障
     */
    private String levelid;
    /**
     * 告警关联信息
     */
    private String info;
    /**
     * 二级分类id
     */
    private String typeId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 修改用户id
     */
    private String modifyUser;
    /**
     * 修改时间
     */
    private Timestamp modifyTime;
    /**
     * 过滤类型，0-屏蔽，1-订阅，2-风格
     */
    private String filtertype;
    /**
     * 组织机构id
     */
    private String groupid;

    public String getNoId()
    {
        return noid;
    }

    public void setNoId(String noid)
    {
        this.noid = noid;
    }

    public String getSystemId()
    {
        return systemId;
    }

    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }

    public String getObjecttype()
    {
        return objecttype;
    }

    public void setObjecttype(String objecttype)
    {
        this.objecttype = objecttype;
    }

    public String getObjectId()
    {
        return objectid;
    }

    public void setObjectId(String objectid)
    {
        this.objectid = objectid;
    }

    public String getDevicetype()
    {
        return devicetype;
    }

    public void setDevicetype(String devicetype)
    {
        this.devicetype = devicetype;
    }

    public String getLevelId()
    {
        return levelid;
    }

    public void setLevelId(String levelid)
    {
        this.levelid = levelid;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getTypeId()
    {
        return typeId;
    }

    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getModifyUser()
    {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser)
    {
        this.modifyUser = modifyUser;
    }

    public Timestamp getModifyTime()
    {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime)
    {
        this.modifyTime = modifyTime;
    }

    public String getFiltertype()
    {
        return filtertype;
    }

    public void setFiltertype(String filtertype)
    {
        this.filtertype = filtertype;
    }

    public String getGroupId()
    {
        return groupid;
    }

    public void setGroupId(String groupid)
    {
        this.groupid = groupid;
    }
}
