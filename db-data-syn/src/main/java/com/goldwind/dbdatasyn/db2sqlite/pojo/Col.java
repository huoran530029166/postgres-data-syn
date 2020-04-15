package com.goldwind.dbdatasyn.db2sqlite.pojo;

/**
 * 表 列 类
 *
 * @author huoran
 */
public class Col
{
    /**
     * 所属表的模式名
     */
    private String schemaName;
    /**
     * 所属表名
     */
    private String tableName;
    /**
     * 列名
     */
    private String colName;
    /**
     * 列 类型
     */
    private String colType;

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

    public String getColName()
    {
        return colName;
    }

    public void setColName(String colName)
    {
        this.colName = colName;
    }

    public String getColType()
    {
        return colType;
    }

    public void setColType(String colType)
    {
        this.colType = colType;
    }

    @Override public String toString()
    {
        return "Col{" + "schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + ", colName='" + colName + '\'' + ", colType='" + colType + '\'' + '}';
    }
}
