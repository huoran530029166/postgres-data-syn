package com.goldwind.datalogic.business;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbAssistant;
import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.database.SQLiteOper;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.file.FileAssistant;
import com.goldwind.datalogic.business.BusinessDef.ConditionType;
import com.goldwind.datalogic.business.BusinessDef.SynchroState;

/**
 * 同步管理
 * 
 * @author 曹阳
 *
 */
public abstract class SynchroManager
{
    public SynchroManager(DbOperBase remoteDbOper, String localDbPath, int waitTime, Log logger)
    {
        this.setRemoteDbOper(remoteDbOper);
        this.localDbPath = localDbPath;
        this.waitTime = waitTime * 1000L;
        this.logger = logger;
    }

    /**
     * 远程关系库操作对象
     */
    private DbOperBase remoteDbOper;
    /**
     * 本地数据库路径
     */
    private String localDbPath;
    /**
     * 同步周期,单位：秒
     */
    private long waitTime = 120;

    /**
     * 同步周期次数 全表
     */
    private int allSynchroCycle = 5;
    /**
     * 同步周期次数 风机
     */
    private int wfSynchroCycle = 5;
    /**
     * 同步周期次数 告警
     */
    private int warnSynchroCycle = 5;
    /**
     * 同步周期次数 消息
     */
    private int noticeSynchroCycle = 5;
    /**
     * 同步周期次数 其他
     */
    private int otherSynchroCycle = 5;
    /**
     * 日志工具
     */
    private Log logger;

    /**
     * 长度
     */
    private int lenght = 10000;

    /**
     * 计数map
     */
    private ConcurrentHashMap<String, String> versionMap = new ConcurrentHashMap<String, String>();

    /**
     * 记录本地库的版本号
     */
    private static ConcurrentHashMap<String, Long> localVersionMap = new ConcurrentHashMap<String, Long>();

    /**
     * 启动同步线程的标志
     */
    private static boolean syncStart = true;

    /**
     * 是否第一次更新VersionMap
     */
    private static boolean firstLoadVersionMap = true;

    /**
     * 本地库最后一次更新时间
     */
    private static long LOCALLASTTIME = System.currentTimeMillis();

    // 对内存版本号初始化
    static
    {
        for (ConditionType type : ConditionType.values())
        {
            localVersionMap.put(type.toString(), 0L);
        }
    }

    /**
     * 对比远程关系库和本地库数据版本
     * 
     * @param remoteDbOper
     *            远程关系库操作对象
     * @param localDbPath
     *            本地库路径
     * @return 版本是否相同
     */
    public boolean compareDataVersion(DbOperBase remoteDbOper, String localDbPath)
    {
        return compareDataVersionByType(remoteDbOper, localDbPath, ConditionType.dataCondition);
    }

    /**
     * 全同步本地数据
     * 
     * @param schema
     *            数据库模式
     * @param localDbOper
     *            本地库操作对象
     * @throws Exception
     *             异常
     */
    public void synchroLocalData(String schema, SQLiteOper localDbOper) throws Exception
    {
        logger.debug("同步数据库模式" + schema + "开始");

        String configSql = "select tablename from pg_tables where tablename!='dataconfig' and tablename!='substation_image_record' and tablename!='operation_tick_print_template' and lower(schemaname) = '"
                + schema + "' union all select tablename from pg_tables where tablename='dataconfig' and lower(schemaname) = '" + schema + "'";
        try
        {
            getRemoteDbOper().openConn();
            ResultSet configTables = getRemoteDbOper().getResultSet(configSql, null);

            List<String> tableList = new ArrayList<String>();
            while (configTables.next())
            {
                tableList.add(configTables.getString("tablename"));
            }

            for (String tableName : tableList)
            {
                synchroTable(schema + ".\"" + tableName + "\"", localDbOper);
            }
        }
        finally
        {
            getRemoteDbOper().closeConn();
        }

        logger.debug("同步数据库模式" + schema + "结束");
    }

    /**
     * @Title: syncroMemoryData
     * @Description: 同步内存数据
     * @param dbOperBase
     *            连接对象
     * @param type
     *            同步类型
     * @param useLocalDb
     *            是否使用本地库
     * @throws DataAsException
     *             异常
     * @return 同步结果返回
     * @return: boolean 同步是否成功失败
     */
    protected abstract boolean syncroMemoryData(DbOperBase dbOperBase, ConditionType type, boolean useLocalDb) throws DataAsException;

    /**
     * 同步线程
     * 
     * @return 线程
     */
    private Runnable synchroThread()
    {
        return new Runnable()
        {

            @Override
            public void run()
            {
                // waitTime > -1替换true
                while (waitTime > -1)
                {
                    try
                    {
                        Thread.sleep(waitTime);
                        if (compareDataVersion(getRemoteDbOper(), localDbPath))
                        {
                            countTables();
                            // BusinessDef.setLOCALDBSYNCHROSTATE(SynchroState.Start);
                            sychroLocalDataStart();
                            // BusinessDef.setLOCALDBSYNCHROSTATE(SynchroState.Stop);
                            // BusinessDef.setMEMORYDATASYNCHROSTATE(SynchroState.Start);
                            // syncroMemoryData(remoteDbOper, ConditionType.dataCondition, false);
                            // BusinessDef.setMEMORYDATASYNCHROSTATE(SynchroState.Stop);
                        }
                    }
                    catch (Exception e)
                    {
                        logger.error("同步线程异常", e);
                    }
                }

            }

        };
    }

    /**
     * 开始同步
     * 
     * @throws Exception
     *             异常
     */
    private void sychroLocalDataStart() throws Exception
    {
        // 先同步到临时文件，同步完成后进行文件替换
        FileAssistant.deleteFile(localDbPath + "_tmp");
        SQLiteOper soper = new SQLiteOper(localDbPath + "_tmp");
        synchroLocalData("config", soper);
        synchroLocalData("custom", soper);
        // 不删除源文件会出现copy不成功的情况，所以将源文件删除
        FileAssistant.deleteFile(localDbPath);
        FileAssistant.copyFile(localDbPath + "_tmp", localDbPath);
        FileAssistant.deleteFile(localDbPath + "_tmp");
    }

    /**
     * 启动线程
     * 
     * @param isDivideTable
     * @return
     */
    public Runnable sychroLocalDataStartThread(boolean isDivideTable)
    {
        return new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    if (isDivideTable)
                    {

                        sychroDspLocalData();
                    }
                    else
                    {
                        sychroLocalDataStart();
                    }
                }
                catch (Exception e)
                {
                    logger.error("sychroLocalDataStart error", e);
                }

            }

        };

    }

    /**
     * 查询同步表数量
     * 
     * @throws Exception
     *             异常
     */
    private void countTables() throws Exception
    {
        BusinessDef.setSYNCHROTABLENUMBER(0);
        BusinessDef.setSYNCHROTABLELESSNUMBER(0);
        String sql = "select count(*) as counter from pg_tables where lower(schemaname) in('config','custom')";
        try
        {
            getRemoteDbOper().openConn();
            ResultSet configTables = getRemoteDbOper().getResultSet(sql, null);
            if (configTables.next())
            {
                int counter = configTables.getInt("counter");
                BusinessDef.setSYNCHROTABLENUMBER(counter);
                BusinessDef.setSYNCHROTABLELESSNUMBER(counter);
            }
        }
        finally
        {
            getRemoteDbOper().closeConn();
        }
    }

    /**
     * 开始同步
     * 
     */
    public Runnable start()
    {
        return synchroThread();
    }

    /**
     * 根据同步类型比较版本
     * 
     * @param remoteDbOper
     *            远程关系库操作对象
     * @param localDbPath
     *            本地库路径
     * @param type
     *            同步类型
     * @return 是否同步
     */
    private synchronized boolean compareDataVersionByType(DbOperBase remoteDbOper, String localDbPath, ConditionType type)
    {
        logger.debug("比较版本开始");
        File tempFile = new File(localDbPath);
        if (!tempFile.exists())
        {
            return true;
        }
        String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
        SQLiteOper localDbOper = null;
        String remoteDataVersion = "";
        String localDataVersion = "";
        try
        {
            localDbOper = new SQLiteOper(localDbPath);
            remoteDbOper.openConn();
            localDbOper.openConn();
            ResultSet remoteRs = remoteDbOper.getResultSet(selectVersion, null);
            ResultSet localRs = localDbOper.getResultSet(DbAssistant.convertSqlByDb(selectVersion, DatabaseType.SQLite), null);
            if (remoteRs.next())
            {
                remoteDataVersion = remoteRs.getString("version");
            }
            if (localRs.next())
            {
                localDataVersion = localRs.getString("version");
            }
        }
        catch (Exception e)
        {
            if (e instanceof SQLException)
            {
                StackTraceElement[] ste = e.getStackTrace();
                for (StackTraceElement s : ste)
                {
                    if (s.getClassName().indexOf("org.sqlite") != -1)
                    {
                        return true;
                    }
                }
            }
        }
        finally
        {
            try
            {
                if (localDbOper != null)
                {
                    localDbOper.closeConn();
                }
                remoteDbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error("compareDataVersionByType error", e);
            }
        }
        logger.debug("比较版本结束，结果为：" + !remoteDataVersion.equalsIgnoreCase(localDataVersion));
        return !remoteDataVersion.equalsIgnoreCase(localDataVersion);

    }

    /**
     * 同步指定表
     * 
     * @param tableName
     *            需同步表
     * @param localDbOper
     *            本地库路径
     * @throws Exception
     *             异常
     */
    private void synchroTable(String tableName, SQLiteOper localDbOper) throws Exception
    {
        if (tableName != null)
        {
            logger.debug("同步" + tableName + "开始");

            // 产生基础sql
            String sql = "select * from " + tableName;
            if ("config.propaths".equals(tableName) || "config.pathdescr".equals(tableName))
            {
                sql = sql + " where protocolid in (select protocolid from config.wtinfo group by protocolid)";
            }

            // 同步创建表
            syncCreateTable(tableName, localDbOper, sql);

            // 数据插入
            syncData(tableName, localDbOper, sql);

            BusinessDef.setSYNCHROTABLELESSNUMBER(BusinessDef.getSYNCHROTABLELESSNUMBER() - 1);
            logger.debug("同步" + tableName + "结束");
        }
    }

    /**
     * @Title: syncCreateTable
     * @Description: 同步创建表操作
     * @param tableName
     *            表名
     * @param localDbOper
     *            本地库连接
     * @param sql
     *            执行sql
     * @return: void
     * @throws Exception
     *             异常
     */
    public void syncCreateTable(String tableName, SQLiteOper localDbOper, String sql) throws Exception
    {
        try
        {
            // 打开本地库和关系库
            getRemoteDbOper().openConn();
            localDbOper.openConn();
            try
            {
                // 删除本地库中对应的表
                localDbOper.excute("drop table " + tableName, null);
            }
            catch (Exception e)
            {

            }

            // 只查询一条数据
            String sqlLimit = sql + " limit 1";

            // 获取表结构
            ResultSet rs = getRemoteDbOper().getResultSet(sqlLimit, null);
            localDbOper.excute(DbAssistant.tableToDefSql(tableName, rs, DatabaseType.SQLite, getRemoteDbOper().getConn()), null);
        }
        finally
        {
            // 关闭本地库和关系库
            localDbOper.closeConn();
            getRemoteDbOper().closeConn();
        }
    }

    /**
     * @Title: syncData
     * @Description: 同步数据
     * @param tableName
     *            表名
     * @param localDbOper
     *            本地库连接
     * @param sql
     *            语句
     * @throws Exception
     *             异常
     * @return: void
     */
    public void syncData(String tableName, SQLiteOper localDbOper, String sql) throws Exception
    {
        int count = 0;
        try
        {
            // 打开本地库和关系库
            getRemoteDbOper().openConn();

            String sqlCount = sql.replace("*", "count(*) as count ");
            ResultSet rsCount = getRemoteDbOper().getResultSet(sqlCount, null);

            while (rsCount.next())
            {
                count = rsCount.getInt("count");
            }
        }
        finally
        {
            getRemoteDbOper().closeConn();
        }
        // logger.info("关系库-->" + sql);
        // int sqliteCount = 0;
        // 关闭关系库

        int num = (count - 1) / lenght;
        for (int i = 0; i < num + 1; i++)
        {
            try
            {
                // 打开数据库连接
                getRemoteDbOper().openConn();
                localDbOper.openConn();

                // 组织执行的sql
                String excuteSql = sql;
                // 没有ORDER BY 数据库会以优化的查询进行排序 会出现意想不到问题
                if (count > lenght)
                {
                    // 获取表的主键
                    String schem = null;
                    String tName = tableName;
                    if (tableName.indexOf(".") >= 0)
                    {
                        String[] tables = tableName.split("\\.");
                        schem = tables[0];
                        tName = tables[1];
                    }
                    Connection conn = getRemoteDbOper().getConn();
                    DatabaseMetaData dbmd = conn.getMetaData();
                    ResultSet rs = dbmd.getPrimaryKeys(conn.getCatalog(), schem, tName);// 获取主键
                    int j = 0;
                    while (rs.next())
                    {
                        if (j == 0)
                        {
                            excuteSql += " ORDER BY " + rs.getString("COLUMN_NAME");
                        }
                        else
                        {
                            excuteSql += " ," + rs.getString("COLUMN_NAME");
                        }
                        j++;
                    }

                    // 有主键的情况
                    if (j > 0)
                    {
                        excuteSql += " LIMIT " + lenght + " OFFSET " + (i * lenght);
                    }
                    else
                    {
                        // 没有主键的情况直接获取所有数据 （在config中建议如果超过1W数据量的表需要进行建立主键），直接结束for循环
                        i = num + 1;
                    }
                }

                // 关系库查询
                ResultSet rs = getRemoteDbOper().getResultSet(excuteSql, null);

                // logger.info("表---->" + tableName + "sql--->" + excuteSql);

                if (localDbOper.isMemoryMode())
                {
                    String insSqls = DbAssistant.tableToInsSqlbyParam(tableName, rs);
                    ResultSetMetaData rmeta = rs.getMetaData();
                    while (rs.next())
                    {
                        Object[] values = new Object[rmeta.getColumnCount()];
                        for (int j = 1; j <= rmeta.getColumnCount(); j++)
                        {
                            values[j - 1] = rs.getObject(j);
                        }
                        localDbOper.excute(insSqls, values);
                    }
                }
                else
                {
                    // 特殊处理dataconfig 不需要转化
                    if (tableName.indexOf("config.dataconfig") >= 0)
                    {
                        tableName = DbAssistant.convertSqlByDb(tableName, localDbOper.getDatabaseType());
                        String[] insertSqls = DbAssistant.tableToInsSql(tableName, rs);
                        localDbOper.excuteBatchSql2(insertSqls, false);
                    }
                    else
                    {
                        String[] insertSqls = DbAssistant.tableToInsSql(tableName, rs);
                        // if (count > lenght)
                        // {
                        // logger.info("表--->" + tableName + " 第" + i + "次---------->" + insertSqls[0]);
                        // logger.info("表--->" + tableName + " 第" + i + "次---------->" + insertSqls[insertSqls.length - 1]);
                        // }
                        localDbOper.excuteBatchSql(insertSqls);
                        // sqliteCount += insertSqls.length;
                    }
                }
            }
            finally
            {
                // 关闭数据库连接
                localDbOper.closeConn();
                getRemoteDbOper().closeConn();
            }

        }

        // logger.info("表--->" + tableName + "关系库--->" + count + "本地库--->" + sqliteCount);
    }

    /**
     * 根据同步类型同步本地库
     * 
     * @param type
     *            同步类型
     * @param soper
     *            本地库操作对象
     * @throws Exception
     *             异常
     */
    public synchronized void synchroTableByType(ConditionType type, SQLiteOper soper) throws Exception
    {

        // 获取配置中的同步表
        String configSql = "select remark as tables,val from config.dataconfig where name='" + type.toString() + "'";
        String tables = "";
        String valFromDb = "";
        try
        {
            getRemoteDbOper().openConn();
            ResultSet configTables = getRemoteDbOper().getResultSet(configSql, null);
            if (configTables.next())
            {
                tables = configTables.getString("tables");
                valFromDb = configTables.getString("val");
            }
        }
        finally
        {
            getRemoteDbOper().closeConn();
        }

        // 依据表名进行同步
        if (StringUtils.isNotBlank(tables))
        {
            String[] tableNames = tables.split(",");
            for (String tab : tableNames)
            {
                synchroTable(tab, soper);
            }
        }
        if (ConditionType.dataCondition.equals(type))
        {
            synchroTable("config.dataconfig", soper);
        }
        else
        {
            soper.openConn();
            String updateSql = "UPDATE config.dataconfig SET  val='" + valFromDb + "' WHERE name='" + type.toString() + "'";
            soper.excute(updateSql, null);
            soper.closeConn();
        }
    }

    /**
     * 开始同步
     * 
     * @throws Exception
     *             异常
     */
    private void sychroDspLocalData() throws Exception
    {
        // 先同步到临时文件，同步完成后进行文件替换
        FileAssistant.deleteFile(localDbPath + "_tmp");
        SQLiteOper soper = new SQLiteOper(localDbPath + "_tmp");
        synchroTableByType(ConditionType.dataCondition, soper);
        FileAssistant.deleteFile(localDbPath);// 不删除源文件会出现copy不成功的情况，所以将源文件删除
        FileAssistant.copyFile(localDbPath + "_tmp", localDbPath);
        FileAssistant.deleteFile(localDbPath + "_tmp");
    }

    /**
     * 根据指定同步类型同步线程
     * 
     * @return 线程
     */
    private Runnable synchroThreadByType()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                int allAlready = 0;
                int wfAlready = 0;
                int warnAlready = 0;
                int noticeAlready = 0;
                int otherAlready = 0;

                SQLiteOper so = new SQLiteOper(localDbPath);
                logger.info("syncMemory Memory start!!");
                while (isSyncStart())
                {
                    try
                    {
                        Thread.sleep(waitTime);
                        // 全表同步
                        if (compareMemoryDataVersionByTypeFromRemoteDb(getRemoteDbOper(), ConditionType.dataCondition))
                        {
                            allAlready = syncMemory(ConditionType.dataCondition, allAlready, allSynchroCycle, so);
                        }
                        // 风机
                        if (compareMemoryDataVersionByTypeFromRemoteDb(getRemoteDbOper(), ConditionType.WfDeviceCondition))
                        {
                            wfAlready = syncMemory(ConditionType.WfDeviceCondition, wfAlready, wfSynchroCycle, so);
                        }
                        // 告警同步
                        if (compareMemoryDataVersionByTypeFromRemoteDb(getRemoteDbOper(), ConditionType.WarnCondition))
                        {
                            warnAlready = syncMemory(ConditionType.WarnCondition, warnAlready, warnSynchroCycle, so);
                        }
                        // 消息同步
                        if (compareMemoryDataVersionByTypeFromRemoteDb(getRemoteDbOper(), ConditionType.NoticeCondition))
                        {
                            noticeAlready = syncMemory(ConditionType.NoticeCondition, noticeAlready, noticeSynchroCycle, so);
                        }
                        // 其他同步
                        if (compareMemoryDataVersionByTypeFromRemoteDb(getRemoteDbOper(), ConditionType.OtherCondition))
                        {
                            otherAlready = syncMemory(ConditionType.OtherCondition, otherAlready, otherSynchroCycle, so);
                        }
                    }
                    catch (Exception e)
                    {
                        logger.error("sync Tread Exception", e);
                    }
                }
            }
        };
    }

    /**
     * 同步库线程
     * 
     * @return
     */
    private Runnable synchroLocalDbThreadByType()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {

                SQLiteOper so = new SQLiteOper(localDbPath);
                logger.info("syncMemoryTable start!!");
                try
                {
                    sychroLocalDataStart();
                }
                catch (Exception e1)
                {
                    logger.error("sync Tread LocalDb Exception（firstTime）", e1);
                }
                while (isSyncStart())
                {
                    try
                    {
                        Thread.sleep(waitTime);
                        // 全表同步
                        boolean datacondition = compareMemoryDataVersionByType(getRemoteDbOper(), localDbPath, ConditionType.dataCondition);
                        if (datacondition)
                        {
                            synchroTableByType(ConditionType.dataCondition, so);
                        }
                        // 风机
                        if (compareMemoryDataVersionByType(getRemoteDbOper(), localDbPath, ConditionType.WfDeviceCondition) && !datacondition)
                        {
                            synchroTableByType(ConditionType.WfDeviceCondition, so);
                        }
                        // 告警同步
                        if (compareMemoryDataVersionByType(getRemoteDbOper(), localDbPath, ConditionType.WarnCondition) && !datacondition)
                        {
                            synchroTableByType(ConditionType.WarnCondition, so);
                        }
                        // 消息同步
                        if (compareMemoryDataVersionByType(getRemoteDbOper(), localDbPath, ConditionType.NoticeCondition) && !datacondition)
                        {
                            synchroTableByType(ConditionType.NoticeCondition, so);
                        }
                        // 其他同步
                        if (compareMemoryDataVersionByType(getRemoteDbOper(), localDbPath, ConditionType.OtherCondition) && !datacondition)
                        {
                            synchroTableByType(ConditionType.OtherCondition, so);
                        }

                    }
                    catch (Exception e)
                    {
                        logger.error("sync Tread LocalDb Exception", e);
                    }
                }
            }
        };
    }

    /**
     * @Title: checkViewChange
     * @Description: 检查关系库中的version是否已发生变化
     * @param remoteDbOper
     *            关系库连接
     * @param type
     *            类型
     * @return 返回
     * @throws Exception
     *             异常
     * @return: boolean
     */
    private boolean checkViewChange(DbOperBase remoteDbOper, ConditionType type) throws Exception
    {
        // 获取关系库中的version
        String remoteDataVersion = null;
        boolean resultB = false;
        String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
        try
        {
            remoteDbOper.openConn();
            ResultSet remoteRs = remoteDbOper.getResultSet(selectVersion, null);
            if (remoteRs.next())
            {
                remoteDataVersion = remoteRs.getString("version");
            }
        }
        finally
        {
            remoteDbOper.closeConn();
        }
        // 只有在versionMap中有这个version才返回true，其他情况都返回false
        if (remoteDataVersion != null && versionMap.containsKey(type.toString()))
        {
            String mapVersion = versionMap.get(type.toString());
            if (mapVersion.equals(remoteDataVersion))
            {
                resultB = true;
            }
        }
        versionMap.put(type.toString(), remoteDataVersion);
        return resultB;
    }

    /**
     * @Title: syncMemory
     * @Description: 同步内存方法
     * @param type
     *            同步类型
     * @param already
     *            计数数据
     * @param synchroCycle
     *            同步基数
     * @param so
     *            本地库
     * @return 返回计数数据
     * @throws Exception
     *             异常
     * @return: int
     */
    private int syncMemory(ConditionType type, int already, int synchroCycle, SQLiteOper so) throws Exception
    {

        if (checkViewChange(getRemoteDbOper(), type))
        {
            already++;
        }
        else
        {
            already = 0;
        }
        if (already >= synchroCycle)
        {

            // 同步内存
            boolean syncResult = syncMemoryStart(type, false);
            already = 0;
        }
        return already;
    }

    /**
     * 部分同步
     * 
     * @return 返回是否同步成功
     * @throws DataAsException
     *             异常
     */
    public boolean dspStart() throws DataAsException
    {
        // 第一次同步内存步骤：
        // 同步内存 首先关系库同步 本库同步
        BusinessDef.setMEMORYDATASYNCHROSTATE(SynchroState.Start);
        boolean syncRs = false;
        // 开始的时候直接重新创建视图
        syncRs = syncMemoryStart(ConditionType.dataCondition, true);
        BusinessDef.setMEMORYDATASYNCHROSTATE(SynchroState.Stop);
        // 启动两个线程 1、同步内存
        if (syncRs)
        {
            setFirstLoadVersionMap(false);
            // 启动同步线程
            new Thread(synchroThreadByType()).start();
        }
        return syncRs;
    }

    /**
     * 同步本地库线程
     * 
     */
    public void syncLocalDb() throws DataAsException
    {
        new Thread(synchroLocalDbThreadByType()).start();
    }

    /**
     * 同步内存版本号
     * 
     * @param dbOper
     *            数据库对象
     */
    private void syncLocalVersion(DbOperBase dbOper, ConditionType type)
    {
        if (type == ConditionType.dataCondition || isFirstLoadVersionMap())
        {
            // 将本地库中的condition的val放置到内存中（为解决有condition同步了，其他condition不同步问题）
            syncLocalVersionMap(ConditionType.dataCondition, dbOper);
            syncLocalVersionMap(ConditionType.WfDeviceCondition, dbOper);
            syncLocalVersionMap(ConditionType.WarnCondition, dbOper);
            syncLocalVersionMap(ConditionType.NoticeCondition, dbOper);
            syncLocalVersionMap(ConditionType.OtherCondition, dbOper);
        }
        else if (type == ConditionType.WfDeviceCondition)
        {
            syncLocalVersionMap(ConditionType.WfDeviceCondition, dbOper);
            syncLocalVersionMap(ConditionType.WarnCondition, dbOper);
            syncLocalVersionMap(ConditionType.NoticeCondition, dbOper);
            syncLocalVersionMap(ConditionType.OtherCondition, dbOper);
        }
        else
        {
            syncLocalVersionMap(type, dbOper);
        }
    }

    /**
     * @Title: getDbOperByPath
     * @Description: 依据本地库路径产生本地库dbOper
     * @return 返回
     * @return: DbOperBase 文件路径存在返回dbOper 不存在返回null
     */
    private DbOperBase getDbOperByPath()
    {
        // 判断是否存在
        File tempFile = new File(localDbPath);
        if (!tempFile.exists())
        {
            return null;
        }
        DbOperBase localDbOper = new SQLiteOper(localDbPath);
        return localDbOper;
    }

    /**
     * @Title: syncMemoryStart
     * @Description: 开始同步机制
     * @param type
     *            类型
     * @param isUseLocalDb
     *            是否使用本地库同步内存
     * @return 是否同步成功
     * @throws DataAsException
     *             异常
     * @return: boolean 同步成功
     */
    private boolean syncMemoryStart(ConditionType type, boolean isUseLocalDb) throws DataAsException
    {
        // 关系库同步
        boolean remoteDbResult = syncroMemoryData(getRemoteDbOper(), type, false);
        if (!remoteDbResult && isUseLocalDb)
        {
            // 失败后本地库同步
            DbOperBase localDbOper = getDbOperByPath();
            if (localDbOper == null)
            {
                remoteDbResult = false;
            }
            else
            {
                remoteDbResult = syncroMemoryData(localDbOper, type, true);
                if (remoteDbResult)
                {
                    syncLocalVersion(localDbOper, type);
                }
            }
        }
        else
        {
            syncLocalVersion(getRemoteDbOper(), type);
        }
        return remoteDbResult;
    }

    /**
     * 同步本地版本号
     * 
     * @param type
     *            版本类型
     * @param dbOper
     *            数据库对象
     */
    private void syncLocalVersionMap(ConditionType type, DbOperBase dbOper)
    {
        try
        {
            dbOper.openConn();
            String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
            ResultSet localRs = dbOper.getResultSet(selectVersion, null);
            while (localRs.next())
            {
                String version = localRs.getString("version");
                if (version != null && !version.isEmpty())
                {
                    long localDataVersion = Long.parseLong(version);
                    localVersionMap.put(type.toString(), localDataVersion);
                }
            }
            dbOper.closeConn();
        }
        catch (Exception e)
        {
            logger.error("syncLocalVersionMap errror", e);
        }
        finally
        {
            if (dbOper != null)
            {
                try
                {
                    dbOper.closeConn();
                }
                catch (SQLException e)
                {
                    logger.error("syncLocalVersionMap errror1", e);
                }
            }
        }
    }

    /**
     * 将数据库中版本号和本地库的数据比较
     * 
     * @param remoteDbOper
     *            关系库的连接
     * @param type
     *            版本类型
     * @param localDbPath
     *            本地库连接
     * @return 是否和内存中相同
     */
    private boolean compareMemoryDataVersionByType(DbOperBase remoteDbOper, String localDbPath, ConditionType type)
    {
        File tempFile = new File(localDbPath);
        if (!tempFile.exists())
        {
            return true;
        }
        String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
        SQLiteOper localDbOper = null;
        long remoteDataVersion = 0L;
        long localDataVersion = 0L;
        try
        {
            // 获取本地库和关系库中的版本号
            localDbOper = new SQLiteOper(localDbPath);
            remoteDbOper.openConn();
            localDbOper.openConn();
            ResultSet remoteRs = remoteDbOper.getResultSet(selectVersion, null);
            ResultSet localRs = localDbOper.getResultSet(DbAssistant.convertSqlByDb(selectVersion, DatabaseType.SQLite), null);
            if (remoteRs.next())
            {
                remoteDataVersion = Long.parseLong(remoteRs.getString("version"));
            }
            if (localRs.next())
            {
                localDataVersion = Long.parseLong(localRs.getString("version"));
            }
        }
        catch (Exception e)
        {
            if (e instanceof SQLException)
            {
                StackTraceElement[] ste = e.getStackTrace();
                for (StackTraceElement s : ste)
                {
                    if (s.getClassName().indexOf("org.sqlite") != -1)
                    {
                        return true;
                    }
                }
            }
        }
        finally
        {
            try
            {
                if (localDbOper != null)
                {
                    localDbOper.closeConn();
                }
                remoteDbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error("compareMemoryDataVersionByType error" + e);
            }
        }

        return remoteDataVersion > localDataVersion;
    }

    /**
     * 内存版本号比较关系库版本号
     * 
     * @param remoteDbOper
     *            关系库对象
     * @param type
     *            类型
     * @return 关系库版本号大于内存版本号
     */
    private boolean compareMemoryDataVersionByTypeFromRemoteDb(DbOperBase remoteDbOper, ConditionType type)
    {
        String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
        long remoteDataVersion = 0L;
        try
        {
            remoteDbOper.openConn();
            ResultSet remoteRs = remoteDbOper.getResultSet(selectVersion, null);
            if (remoteRs.next())
            {
                remoteDataVersion = Long.parseLong(remoteRs.getString("version"));
            }
        }
        catch (Exception e)
        {
            logger.error("compareMemoryDataVersionByTypeFromRemoteDb error" + e);
            return false;
        }
        finally
        {
            try
            {
                remoteDbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error("compareMemoryDataVersionByTypeFromRemoteDb1 error" + e);
            }
        }
        long versionMap = 0L;
        if (localVersionMap != null && localVersionMap.containsKey(type.toString()))
        {
            versionMap = localVersionMap.get(type.toString());
        }
        return remoteDataVersion > versionMap;
    }

    /**
     * 将本地库的版本号读取到内存中
     * 
     * @param localDbPath
     *            本地库地址
     * @param type
     *            版本类型
     */
    // private void updateVersion2Map(ConditionType type)
    // {
    // String selectVersion = "select val as version from config.dataconfig where name='" + type.toString() + "'";
    // long localDataVersion = 0L;
    // try
    // {
    // remoteDbOper.openConn();
    // ResultSet localRs = remoteDbOper.getResultSet(selectVersion, null);
    // if (localRs.next())
    // {
    // localDataVersion = Long.parseLong(localRs.getString("version"));
    // }
    // remoteDbOper.closeConn();
    // localVersionMap.put(type.toString(), localDataVersion);
    // }
    // catch (Exception e)
    // {
    // logger.error(e);
    // }
    // finally
    // {
    // try
    // {
    // if (remoteDbOper != null)
    // {
    // remoteDbOper.closeConn();
    // }
    // }
    // catch (SQLException e)
    // {
    // logger.error(e);
    // }
    // }
    // }

    public static boolean isSyncStart()
    {
        return syncStart;
    }

    public static void setSyncStart(boolean syncStart)
    {
        SynchroManager.syncStart = syncStart;
    }

    public static boolean isFirstLoadVersionMap()
    {
        return firstLoadVersionMap;
    }

    public static void setFirstLoadVersionMap(boolean firstLoadVersionMap)
    {
        SynchroManager.firstLoadVersionMap = firstLoadVersionMap;
    }

    public DbOperBase getRemoteDbOper()
    {
        return remoteDbOper;
    }

    public void setRemoteDbOper(DbOperBase remoteDbOper)
    {
        this.remoteDbOper = remoteDbOper;
    }

    public static long getLocalLastTime()
    {
        return LOCALLASTTIME;
    }

    public static void setLocalLastTime(long localLastTime)
    {
        SynchroManager.LOCALLASTTIME = localLastTime;
    }
}
