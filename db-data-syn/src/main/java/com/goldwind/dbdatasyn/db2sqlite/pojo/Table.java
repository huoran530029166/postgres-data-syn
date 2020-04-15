package com.goldwind.dbdatasyn.db2sqlite.pojo;

import java.util.List;

/**
 * 表 类
 *
 * @author huoran
 */
public class Table
{
    /**
     * 模式名
     */
    private String schemaName;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表 主键集合
     */
    private List<Pk> pks;
    /**
     * 主键是否包含协议号标识
     */
    private boolean pkProFlag;
    /**
     * 协议号 集合 (当主键包含协议号时,该字段表示表内的所有协议号集合)
     */
    private List<Integer> protocolids;
    /**
     * 表 列集合
     */
    private List<Col> cols;

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

    public List<Pk> getPks()
    {
        return pks;
    }

    public void setPks(List<Pk> pks)
    {
        this.pks = pks;
    }

    public boolean isPkProFlag()
    {
        return pkProFlag;
    }

    public void setPkProFlag(boolean pkProFlag)
    {
        this.pkProFlag = pkProFlag;
    }

    public List<Integer> getProtocolids()
    {
        return protocolids;
    }

    public void setProtocolids(List<Integer> protocolids)
    {
        this.protocolids = protocolids;
    }

    public List<Col> getCols()
    {
        return cols;
    }

    public void setCols(List<Col> cols)
    {
        this.cols = cols;
    }

    @Override public String toString()
    {
        return "Table{" + "schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + ", pks=" + pks + ", pkProFlag=" + pkProFlag + ", protocolids=" + protocolids + ", cols=" + cols
                + '}';
    }
}
