package com.goldwind.dbdatasyn.sqlite2db.pojo;

/**
 * 主键 类
 *
 * @author huoran
 */
public class Pk
{
    /**
     * 主键名称
     */
    private String PkName;
    /**
     * 主键所属表的模式名
     */
    private String schemaName;
    /**
     * 主键所属表名
     */
    private String tableName;
    /**
     * 主键列名
     */
    private String colName;
    /**
     * 主键列 类型
     */
    private String colType;

    public String getPkName()
    {
        return PkName;
    }

    public void setPkName(String pkName)
    {
        PkName = pkName;
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

    @Override public String toString()
    {
        return "Pk{" + "PkName='" + PkName + '\'' + ", schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + ", colName='" + colName + '\'' + ", colType='" + colType + '\'' + '}';
    }
}
