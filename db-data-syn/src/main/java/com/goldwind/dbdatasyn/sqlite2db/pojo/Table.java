package com.goldwind.dbdatasyn.sqlite2db.pojo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 表 模型类
 *
 * @author huoran
 */
public class Table implements Comparable<Table>
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
     * 是否是外键主表标志
     */
    private boolean isFkMainTable = false;

    /**
     * 如果该表是外键主表,该项表示对应的外键子表项,
     * MAP的key为1、schemaName:表示模式名;2、tableName：表示表名。
     */
    private List<Map<String, String>> fkChildTables = new ArrayList<>();

    /**
     * 是否为外键子表标志
     */
    private boolean isFkChildTable = false;

    /**
     * 如果该表是外键子表,该项标识对应的外键主表项
     * MAP的key为1、schemaName:表示模式名;2、tableName：表示表名。
     */
    private List<Map<String, String>> fkMainTables = new ArrayList<>();
    /**
     * 缺失外键子表标志
     * 在该表本地库不存在数据,关系库存在数据时,不生成删除语句,在sqlite.err中记录异常信息
     */
    private boolean fkChildMissFlag = false;
    /**
     * 外键关系的排序字段
     */
    private int sortNum = 0;
    /**
     * 用于计算外间关系排序数值的集合
     */
    private List<Map<String, String>> sortFkMains = new ArrayList<>();
    /**
     * 表对象的关系库列信息
     */
    private LinkedHashMap<String, Col> colDbMap = new LinkedHashMap<>();
    /**
     * 表对象的本地库列信息
     */
    private LinkedHashMap<String, Col> colSqliteMap = new LinkedHashMap<>();

    /**
     * 关系库表 主键集合
     */
    private List<Pk> pks = new ArrayList<>();
    /**
     * 关系库表主键是否包含协议号标识
     */
    private boolean pkProFlag = false;
    /**
     * 关系库协议号 集合 (当主键包含协议号时,该字段表示表内的所有协议号集合)
     */
    private List<Integer> protocolids = new ArrayList<>();

    /**
     * 本地库协议号集合(当主键包含协议号时,该字段表示本地库表内所有的协议号集合)
     */
    private List<Integer> protocolidsSqlite = new ArrayList<>();

    public List<Integer> getProtocolidsSqlite()
    {
        return protocolidsSqlite;
    }

    public void setProtocolidsSqlite(List<Integer> protocolidsSqlite)
    {
        this.protocolidsSqlite = protocolidsSqlite;
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

    public LinkedHashMap<String, Col> getColDbMap()
    {
        return colDbMap;
    }

    public void setColDbMap(LinkedHashMap<String, Col> colDbMap)
    {
        this.colDbMap = colDbMap;
    }

    public LinkedHashMap<String, Col> getColSqliteMap()
    {
        return colSqliteMap;
    }

    public void setColSqliteMap(LinkedHashMap<String, Col> colSqliteMap)
    {
        this.colSqliteMap = colSqliteMap;
    }

    public int getSortNum()
    {
        return sortNum;
    }

    public void setSortNum(int sortNum)
    {
        this.sortNum = sortNum;
    }

    public List<Map<String, String>> getSortFkMains()
    {
        return sortFkMains;
    }

    public void setSortFkMains(List<Map<String, String>> sortFkMains)
    {
        this.sortFkMains = sortFkMains;
    }

    public boolean isFkChildTable()
    {
        return isFkChildTable;
    }

    public void setFkChildTable(boolean fkChildTable)
    {
        isFkChildTable = fkChildTable;
    }

    public List<Map<String, String>> getFkMainTables()
    {
        return fkMainTables;
    }

    public void setFkMainTables(List<Map<String, String>> fkMainTables)
    {
        fkMainTables = fkMainTables;
    }

    public boolean isFkChildMissFlag()
    {
        return fkChildMissFlag;
    }

    public void setFkChildMissFlag(boolean fkChildMissFlag)
    {
        this.fkChildMissFlag = fkChildMissFlag;
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

    public boolean isFkMainTable()
    {
        return isFkMainTable;
    }

    public void setFkMainTable(boolean fkMainTable)
    {
        isFkMainTable = fkMainTable;
    }

    public List<Map<String, String>> getFkChildTables()
    {
        return fkChildTables;
    }

    public void setFkChildTables(List<Map<String, String>> fkChildTables)
    {
        fkChildTables = fkChildTables;
    }

    /**
     * 获取该表的sep字符串表名
     *
     * @return sep字符串表名
     */
    public String getTableSepName()
    {
        return this.getSchemaName() + "@sep@" + this.getTableName();
    }

    /**
     * 排序规则,根据外键关系排序数值 顺序排序
     *
     * @param o 比对对象
     * @return 排序结果
     */
    @Override public int compareTo(Table o)
    {
        return this.getSortNum() - o.getSortNum();
    }

    @Override public String toString()
    {
        return "Table{" + "schemaName='" + schemaName + '\'' + ", tableName='" + tableName + '\'' + ", isFkMainTable=" + isFkMainTable + ", fkChildTables=" + fkChildTables + ", isFkChildTable="
                + isFkChildTable + ", fkMainTables=" + fkMainTables + ", fkChildMissFlag=" + fkChildMissFlag + ", sortNum=" + sortNum + ", sortFkMains=" + sortFkMains + ", colDbMap=" + colDbMap
                + ", colSqliteMap=" + colSqliteMap + ", pks=" + pks + ", pkProFlag=" + pkProFlag + ", protocolids=" + protocolids + ", protocolidsSqlite=" + protocolidsSqlite + '}';
    }
}
