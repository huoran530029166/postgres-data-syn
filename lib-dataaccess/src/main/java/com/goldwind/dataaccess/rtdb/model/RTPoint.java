package com.goldwind.dataaccess.rtdb.model;

import org.apache.commons.lang.StringUtils;

/**
 * 实时库测点实体类
 * 
 * @author shenlf
 *
 */
public class RTPoint
{
    /**
     * 测点id
     */
    private int id;
    /**
     * 测点名称
     */
    private String tagName;
    /**
     * 测点全名称
     */
    private String tableTagName;
    /**
     * 测点描述
     */
    private String desc;
    /**
     * 测点的数值类型。 只读属性，在创建测点时指定。
     */
    private RTDataType dataType;
    /**
     * 测点类别（classof=0表示基本点,classof=1表示采集点；classof=2表示计算点；classof=3表示采集计算点）
     */
    private int classof;

    /**
     * 方程式（用于测点为计算点时）
     */
    private String equation;

    /**
     * 计算触发机制(trigger=0表示无触发,trigger=1表示事件触发，trigger=2表示周期触发，trigger=3表示定时触发)
     */
    private int trigger;

    /**
     * 算结果时间戳参考（用于计算点） 0：表示 采用计算时间作为计算结果 1：表示采用输入测点中的最晚时间作为计算结果时间戳 2：表示采用输入测点中的最早时间作为计算结果时间戳
     */
    private int timecopy;

    /**
     * 计算周期，单位为秒；用于计算点中的定时触发
     */
    private int period;
    /**
     * 测点表名
     */
    private String tableName;

    public RTPoint(String tagName, RTDataType dataType, int classof, String equation, int trigger)
    {
        this.tagName = tagName;
        this.dataType = dataType;
        this.classof = classof;
        this.equation = equation;
        this.trigger = trigger;
    }

    public RTPoint(String tagName, RTDataType dataType)
    {
        super();
        this.tagName = tagName;
        this.dataType = dataType;
    }

    public RTPoint()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTagName()
    {
        return tagName;
    }

    /**
     * 设置测点名称
     * 
     * @param tagName
     *            测点名称
     */
    public void setTagName(String tagName)
    {
        if (!StringUtils.isBlank(tagName))
        {
            this.tagName = tagName.trim();
        }
    }

    public String getTableTagName()
    {
        return tableTagName;
    }

    /**
     * 设置测点全名称
     * 
     * @param tableTagName
     *            测点全名称
     */
    public void setTableTagName(String tableTagName)
    {
        if (!StringUtils.isBlank(tableTagName))
        {
            this.tableTagName = tableTagName.trim();
        }
    }

    public RTDataType getDataType()
    {
        return dataType;
    }

    public void setDataType(RTDataType dataType)
    {
        this.dataType = dataType;
    }

    public int getClassof()
    {
        return classof;
    }

    public void setClassof(int classof)
    {
        this.classof = classof;
    }

    public String getEquation()
    {
        return equation;
    }

    /**
     * 设置方程式
     * 
     * @param equation
     *            方程式
     */
    public void setEquation(String equation)
    {
        if (!StringUtils.isBlank(equation))
        {
            this.equation = equation.trim();
        }
    }

    public int getTrigger()
    {
        return trigger;
    }

    public void setTrigger(int trigger)
    {
        this.trigger = trigger;
    }

    public int getTimecopy()
    {
        return timecopy;
    }

    public void setTimecopy(int timecopy)
    {
        this.timecopy = timecopy;
    }

    public int getPeriod()
    {
        return period;
    }

    public void setPeriod(int period)
    {
        this.period = period;
    }

    public String getTableName()
    {
        return tableName;
    }

    /**
     * 设置测点表
     * 
     * @param tableName
     *            测点表
     */
    public void setTableName(String tableName)
    {
        if (!StringUtils.isBlank(tableName))
        {
            this.tableName = tableName.trim();
        }
    }

    public String getDesc()
    {
        return desc;
    }

    /**
     * 设置测点描述
     * 
     * @param desc
     *            测点描述
     */
    public void setDesc(String desc)
    {
        if (!StringUtils.isBlank(desc))
        {
            this.desc = desc.trim();
        }
    }
}
