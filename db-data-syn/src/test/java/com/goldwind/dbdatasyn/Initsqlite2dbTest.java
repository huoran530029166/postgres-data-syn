package com.goldwind.dbdatasyn;

import com.goldwind.dbdatasyn.pojo.DbConInfo;
import com.goldwind.dbdatasyn.sqlite2db.init.Init;
import com.goldwind.dbdatasyn.sqlite2db.pojo.SynResult;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Table;
import com.goldwind.dbdatasyn.sqlite2db.utils.RunParams;
import com.goldwind.dbdatasyn.utils.Enum;

/**
 * 本地库数据同步到关系库测试类
 *
 * @author huoran
 */
public class Initsqlite2dbTest
{
    public static void main(String[] args) throws Exception
    {
        //数据库连接信息入参
        DbConInfo dbConInfo = new DbConInfo();
        dbConInfo.setDatabase_url("jdbc:postgresql://localhost:5432/local_syn");
        dbConInfo.setUsername("postgres");
        dbConInfo.setPassword("123456");
        //        dbConInfo.setDatabase_url("jdbc:postgresql://10.80.5.109:8101/qhsoam_dev");
        //        dbConInfo.setUsername("pguser");
        //        dbConInfo.setPassword("pguser");
        dbConInfo.setMax_pool_size(200);
        dbConInfo.setMin_pool_size(10);
        dbConInfo.setDb_outtime(600);
        dbConInfo.setMax_idletime(60);

        String sqlitePath = "D:/db_data_dev.sqlite";
        String sqliteErrPath = "D:/db_data_dev_err.sqlite";

        SynResult synResult = Init.init(Enum.DbOperType.POSTGRES, dbConInfo, sqlitePath, sqliteErrPath, true, 60, "/");
        System.out.println("同步结果:" + synResult);

        //        System.out.println(RunParams.getTableSepNames());
        //        System.out.println(RunParams.getTables());

        //        for (Table table : RunParams.getTables())
        //        {
        //            System.out.println(table.getSchemaName() + "," + table.getTableName() + "------------------------------------");
        //            table.getColDbMap().values().forEach(col -> System.out.println("dbColList:  " + col.getColName() + ":" + col.getColType()));
        //            table.getColSqliteMap().values().forEach(col -> System.out.println("sqliteColList:  " + col.getColName() + ":" + col.getColType() + "," + col.getColExcuteType()));
        //        }
        //        for (Table table : RunParams.getTables())
        //        {
        //            System.out.println("--------------" + table.getSchemaName() + "." + table.getTableName() + "-------------------");
        //            table.getPks().forEach(pk -> System.out.println(pk.getColName() + "   " + pk.getColType()));
        //            System.out.println("isPkProFlag : " + table.isPkProFlag());
        //            table.getProtocolidsSqlite().forEach(pro -> System.out.println(pro));
        //        }

    }
}
