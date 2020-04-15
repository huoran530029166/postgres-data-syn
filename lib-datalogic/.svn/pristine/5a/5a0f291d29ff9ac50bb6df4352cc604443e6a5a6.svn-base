package com.goldwind.datalogic.business.model;

/**
 * 设备组信息
 * 
 * @author limin
 *
 */
public class GroupInfo
{
    /**
     * 组编号,如果是电场,则是电场编号;如果是进线、期,必须含有电场编号前缀
     */
    private String id;
    /**
     * 上级编号
     */
    private String parentId;
    /**
     * 组名称,同一级下不允许重复
     */
    private String name;
    /**
     * 类型编号1是风机进线、2是期、3是风场、6是区域、9是组织
     */
    private int typeId;
    /**
     * 索引编号，触发器自动维护，在树形查询时使用
     */
    private String indexId;
    /**
     * 
     */
    private int sort;
    /**
     * 
     */
    private int capability;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getParentId()
    {
        return parentId;
    }

    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getTypeId()
    {
        return typeId;
    }

    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }

    public String getIndexId()
    {
        return indexId;
    }

    public void setIndexId(String indexId)
    {
        this.indexId = indexId;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public int getCapability()
    {
        return capability;
    }

    public void setCapability(int capability)
    {
        this.capability = capability;
    }
}
