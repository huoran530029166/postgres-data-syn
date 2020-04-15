package com.goldwind.datalogic.business.model;

/**
 * 数据库表名模型
 *
 * @author huoran
 */
public class TableName
{
    /**
     * 模式名
     */
    private String schemaName;
    /**
     * 表名
     */
    private String tableName;

    public String getSchemaName()
    {
        return schemaName;
    }

    public void setSchemaName(String schemaName)
    {
        this.schemaName = schemaName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableObj()
    {
        return this.schemaName + "@sep@" + this.tableName;
    }

    @Override public String toString()
    {
        return "Table{" + "schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + '}';
    }
}
