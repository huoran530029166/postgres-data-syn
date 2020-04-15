package com.goldwind.dbdatasyn.db2sqlite.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.database.DbAssistant;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dbdatasyn.db2sqlite.factory.Factory;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Participation;
import com.goldwind.dbdatasyn.db2sqlite.pojo.SynResult;
import com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface.IConvert;
import com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface.ISynData;

import com.goldwind.dbdatasyn.db2sqlite.factory.Factory;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Participation;
import com.goldwind.dbdatasyn.db2sqlite.pojo.SynResult;
import com.goldwind.dbdatasyn.db2sqlite.service.PgSynDataService;
import com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface.IConvert;
import com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface.ISynData;
import com.goldwind.dbdatasyn.pojo.DbConInfo;
import com.goldwind.dbdatasyn.utils.Enum;

public class Init
{
    /**
     * 数据库连接
     */
    public static DbOperBase dbOperBase;

    /**
     * PG数据库 TO SQLITE数据库 初始化方法,
     *
     * @param dbOperType     数据库类型
     * @param participations 需要同步的模式、表入参
     * @param dbConInfo      数据库连接信息
     * @param sqlitePath     本地库地址
     * @throws Exception 异常
     */
    public static SynResult init(Enum.DbOperType dbOperType, List<Participation> participations, DbConInfo dbConInfo, String sqlitePath) throws Exception
    {
        //转换业务类对象
        IConvert iConvert = Factory.makeIConvertService(dbOperType);
        //数据同步业务类对象
        ISynData iSynData = Factory.makeISynDataService(dbOperType);
        //加载本地库地址
        iConvert.sqlitePathConvert(sqlitePath);
        //加载PG数据库连接信息
        iConvert.dbConInfoConvert(dbConInfo);
        //创建数据库连接
        dbOperBase = new DbOperBase(DbAssistant.DatabaseType.Postgre, com.goldwind.dbdatasyn.utils.RunParams.getDatabase_url(), com.goldwind.dbdatasyn.utils.RunParams.getUsername(),
                com.goldwind.dbdatasyn.utils.RunParams.getPassword());

        //        try
        //        {
        //            //初始化数据库连接池
        //            DbOperBase.initPool(DbAssistant.DatabaseType.Postgre, RunParams.getDatabase_url(), RunParams.getUsername(), RunParams.getPassword(), RunParams.getMax_pool_size(),
        //                    RunParams.getMin_pool_size(), RunParams.getDb_outtime());
        //            DbOperBase.setPoolTime(RunParams.getMax_idletime(), null, null);
        //        }
        //        catch (Exception e)
        //        {
        //            // 捕获执行中发生异常 向上抛出
        //            DataAsExpSet.throwExpByMsg("初始化数据库连接池失败", null, null, e);
        //        }

        //加载需要同步的模式名、表名
        iConvert.participationsConvert(participations);

        //创建sqlite数据文件副本文件
        iSynData.createSqliteFileCopy();

        //生成同步数据库表名信息表
        iSynData.createTableNamesTable();
        //同步数据
        Map<String, HashMap<Integer, Exception>> errs = iSynData.synAllData();

        //从副本数据文件切换至主文件
        iSynData.copyToResultFile();

        SynResult result = new SynResult();
        boolean resultFlag;
        if (errs.isEmpty())
        {
            resultFlag = true;
        }
        else
        {
            resultFlag = false;
        }
        result.setReuslt(resultFlag);
        result.setErrs(errs);
        return result;
    }
}
