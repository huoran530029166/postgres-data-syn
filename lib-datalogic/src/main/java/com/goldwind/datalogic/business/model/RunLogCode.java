package com.goldwind.datalogic.business.model;

/**
 * 告警代码明细
 * 
 * @author 冯春源
 * 
 */
public class RunLogCode
{
    /**
     * 系统id,如中央监控,基础平台
     */
    private int systemId;
    /**
     * 代号
     */
    private String code;
    /**
     * 中文描述
     */
    private String descrCn;
    /**
     * 英文描述
     */
    private String descrEn;
    /**
     * 优先级
     */
    private int priorityLevel;
    /**
     * 显示类型
     */
    private String showType;
    /**
     * 显示颜色
     */
    private String showColor;
    /**
     * 告警编号
     */
    private String warnId;
    /**
     * 用于定制告警，对象类型,表示此告警码所关联的对象类型0 场站告警 1 设备告警 2 其它告警 自定义告警：默认为空
     */
    private int objecttype;
    /**
     * 升压站告警类别
     */
    private String iecType;

    /**
     * 升压站对应告警数值
     */
    private String iecValue;
    /**
     * 控制下发或控制结果
     */
    private String operType;
    /**
     * 用于定制告警，是否启用， 0 不启用 1 启用 2 根据关联配置项确定是否启用 3 根据系统的配置的设备确定
     */
    private int isEnable;
    /**
     * 用于定制告警，关联配置项
     */
    private String configKey;
    /**
     * 用于定制告警，关联的设备协议编号，表示定值告警是否和协议关联
     */
    private String proId;
    /**
     * 用于定制告警，触发定制告警的计算公式
     */
    private String express;
    /**
     * 达到告警条件并持续一段时间则告警，单位：分钟
     */
    private String continuetime;
    /**
     * 告警周期，单位：分钟
     */
    private String warnperiod;
    /**
     * 参数替换值
     */
    private String configval;
    /**
     * 应用组织（多个组织id中间用逗号隔开）
     */
    private String applygroups;

    /**
     * 设备typeid
     */
    private String typeId;

    /**
     * 构造函数
     */
    public RunLogCode()
    {

    }

    public String getTypeId()
    {
        return typeId;
    }

    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    public int getSystemId()
    {
        return systemId;
    }

    public void setSystemId(int systemId)
    {
        this.systemId = systemId;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescrCn()
    {
        return descrCn;
    }

    public void setDescrCn(String descrCn)
    {
        this.descrCn = descrCn;
    }

    public String getDescrEn()
    {
        return descrEn;
    }

    public void setDescrEn(String descrEn)
    {
        this.descrEn = descrEn;
    }

    /**
     * 根据本地语言信息获取描述
     * 
     * @param language
     *            语言
     * @return 日志描述
     */
    public String getDescr(String language)
    {
        if ("en".equals(language))
        {
            return this.descrEn;
        }
        else
        {
            return this.descrCn;
        }
    }

    public int getPriorityLevel()
    {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel)
    {
        this.priorityLevel = priorityLevel;
    }

    public String getShowType()
    {
        return showType;
    }

    public void setShowType(String showType)
    {
        this.showType = showType;
    }

    public String getShowColor()
    {
        return showColor;
    }

    public void setShowColor(String showColor)
    {
        this.showColor = showColor;
    }

    public String getWarnId()
    {
        return warnId;
    }

    public void setWarnId(String warnId)
    {
        this.warnId = warnId;
    }

    public int getObjecttype()
    {
        return objecttype;
    }

    public void setObjecttype(int objecttype)
    {
        this.objecttype = objecttype;
    }

    public String getIecValue()
    {
        return iecValue;
    }

    public void setIecValue(String iecValue)
    {
        this.iecValue = iecValue;
    }

    public String getIecType()
    {
        return iecType;
    }

    public void setIecType(String iecType)
    {
        this.iecType = iecType;
    }

    public String getOperType()
    {
        return operType;
    }

    public void setOperType(String operType)
    {
        this.operType = operType;
    }

    public int getIsEnable()
    {
        return isEnable;
    }

    public void setIsEnable(int isEnable)
    {
        this.isEnable = isEnable;
    }

    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    public String getProId()
    {
        return proId;
    }

    public void setProId(String proId)
    {
        this.proId = proId;
    }

    public String getExpress()
    {
        return express;
    }

    public void setExpress(String express)
    {
        this.express = express;
    }

    public String getContinuetime()
    {
        return continuetime;
    }

    public void setContinuetime(String continuetime)
    {
        this.continuetime = continuetime;
    }

    public String getWarnperiod()
    {
        return warnperiod;
    }

    public void setWarnperiod(String warnperiod)
    {
        this.warnperiod = warnperiod;
    }

    public String getConfigval()
    {
        return configval;
    }

    public void setConfigval(String configval)
    {
        this.configval = configval;
    }

    public String getApplyGroups()
    {
        return applygroups;
    }

    public void setApplyGroups(String applygroups)
    {
        this.applygroups = applygroups;
    }

}
