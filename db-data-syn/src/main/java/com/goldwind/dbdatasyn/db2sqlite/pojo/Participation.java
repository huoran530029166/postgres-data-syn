package com.goldwind.dbdatasyn.db2sqlite.pojo;

import com.goldwind.dbdatasyn.db2sqlite.utils.Enums;

/**
 * 表、模式 入参
 */
public class Participation
{
    /**
     * 入参名称 表格式为schemaName.tableName;模式格式为schemaName
     */
    private String name;
    /**
     * 入参类型 表 or 模式
     */
    private Enums.ParticipationType type;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Enums.ParticipationType getType()
    {
        return type;
    }

    public void setType(Enums.ParticipationType type)
    {
        this.type = type;
    }
}
