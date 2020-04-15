package com.goldwind.dbdatasyn.sqlite2db.utils;

import java.util.ArrayList;
import java.util.List;

import com.goldwind.dbdatasyn.sqlite2db.pojo.Table;

/**
 * 本地库同步到关系库 项目运行参数类
 *
 * @author huoran
 */
public class RunParams
{
    /**
     * 错误日志本地库地址
     */
    private static String sqliteErrPath;

    /**
     * 删除标志
     */
    private static boolean delFalg;

    /**
     * 需要同步的关系库表对象
     */
    private static List<Table> tables = new ArrayList<>();

    /**
     * 需要同步的关系库表SEP名集合(无序)
     */
    private static List<String> tableSepNames = new ArrayList<>();

    public static List<String> getTableSepNames()
    {
        return tableSepNames;
    }

    public static void setTableSepNames(List<String> tableSepNames)
    {
        RunParams.tableSepNames = tableSepNames;
    }

    public static String getSqliteErrPath()
    {
        return sqliteErrPath;
    }

    public static void setSqliteErrPath(String sqliteErrPath)
    {
        RunParams.sqliteErrPath = sqliteErrPath;
    }

    public static boolean isDelFalg()
    {
        return delFalg;
    }

    public static void setDelFalg(boolean delFalg)
    {
        RunParams.delFalg = delFalg;
    }

    public static List<Table> getTables()
    {
        return tables;
    }

    public static void setTables(List<Table> tables)
    {
        RunParams.tables = tables;
    }
}
