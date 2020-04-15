package com.goldwind.dbdatasyn.db2sqlite.utils;

import java.util.ArrayList;
import java.util.List;

import com.goldwind.dbdatasyn.db2sqlite.pojo.Participation;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Table;

/**
 * PG数据库 to SQLITE库 全局参数类
 *
 * @author huoran
 */
public class RunParams
{
    /**
     * 程序入参对象集合
     */
    private static List<Participation> participations = new ArrayList<>();
    /**
     * 需要同步的表信息全集合
     */
    private static List<Table> tables = new ArrayList<>();

    public static List<Participation> getParticipations()
    {
        return participations;
    }

    public static void setParticipations(List<Participation> participations)
    {
        RunParams.participations = participations;
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
