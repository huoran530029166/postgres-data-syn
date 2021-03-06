package com.goldwind.dataaccess.database;

import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.exception.DataAsException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库处理基类
 * 
 * @author 张超
 *
 */
public class DbOperBase implements Cloneable, Closeable
{
    /**
     * 输出日志
     */
    private static Log logger = Log.getLog(DbOperBase.class);

    /**
     * 数据库类型
     */
    private DatabaseType databaseType;
    /**
     * 数据库连接字符串
     */
    private String connUrl;
    /**
     * 数据库用户
     */
    private String connUser;
    /**
     * 数据库密码
     */
    private String connPassword;
    /**
     * 数据库连接
     */
    private Connection conn;
    /**
     * 是否使用连接池
     */
    private boolean usePool;
    /**
     * 连接池
     */
    private static ComboPooledDataSource pool;
    /**
     * 连接池数据库类型
     */
    private static DatabaseType poolDbType;
    /**
     * PreparedStatement
     */
    private PreparedStatement ps;

    /**
     * 结果集
     */
    private ResultSet rs = null;

    /**
     * 超时时间
     */
    private static int timeOut = 60;

    /**
     * 插入超时时间
     */
    private static int insertTimeOut = 0;

    /**
     * sockettime 比查询时间多的时间
     */
    private static final int SOCKETTIME = 20;

    // /**
    // * 现有的连接数
    // */
    // public static ConcurrentHashMap<String, Integer> connectionCountHashMap = new ConcurrentHashMap<String, Integer>(50);
    // /**
    // * 连接对应的方法
    // */
    // public static ConcurrentHashMap<String, String> connectionClasssHashMap = new ConcurrentHashMap<String, String>(50);
    // /**
    // * 现有的连接数1
    // */
    // public static ConcurrentHashMap<String, Integer> connectionCountHashMap1 = new ConcurrentHashMap<String, Integer>();
    // /**
    // * 删除连接对应的方法
    // */
    // public static ConcurrentHashMap<String, String> connectionClasssHashMap2 = new ConcurrentHashMap<String, String>();

    /**
     * 同步锁
     */
    // private static String LOCK = "lock";

    /**
     * 连接数据计数
     */
    // public static volatile AtomicInteger countInt = new AtomicInteger(0);

    protected String getConnUrl()
    {
        return connUrl;
    }

    protected void setConnUrl(String connUrl)
    {
        this.connUrl = connUrl;
    }

    public Connection getConn()
    {
        return conn;
    }

    protected void setConn(Connection conn)
    {
        this.conn = conn;
    }

    protected boolean isUsePool()
    {
        return usePool;
    }

    public DatabaseType getDatabaseType()
    {
        return databaseType;
    }

    public DbOperBase(DatabaseType databaseType, String connUrl, String connUser, String connPassword)
    {
        this.usePool = false;
        this.databaseType = databaseType;
        this.connUrl = connUrl;
        this.connUser = connUser;
        this.connPassword = connPassword;
    }

    public DbOperBase()
    {
        this.usePool = true;
    }

    /**
     * 设置参数
     * 
     * @param databaseType
     *            数据库类型
     * @param url
     *            数据库连接地址
     * @param user
     *            用户名
     * @param password
     *            密码
     * @param maxPoolSize
     *            最大连接数
     * @param minPoolSize
     *            最小连接数
     * @param outTime
     *            超时时间
     * @throws DataAsException
     *             自定义加载异常
     */
    public static void initPool(DatabaseType databaseType, String url, String user, String password, int maxPoolSize, int minPoolSize, int outTime) throws PropertyVetoException
    {
        // try
        // {
        // 当outtime不不为0 tInsertTimeOut为0的情况需要设置
        if (outTime != 0 && getInsertTimeOut() == 0)
        {
            setInsertTimeOut(outTime);
        }
        if (getPOOL() == null)
        {
            int socketTimeOut = outTime + SOCKETTIME;
            DbOperBase.poolDbType = databaseType;
            setPOOL(new ComboPooledDataSource());
            // socketTimeOut的超时时间 postgre单位：s；mysql单位：ms
            switch (databaseType) {
                case Postgre:
                    getPOOL().setDriverClass("org.postgresql.Driver");
                    if (outTime != 0)
                    {
                        url = url + "?" + "connectTimeout=" + socketTimeOut + "&socketTimeout=" + socketTimeOut;
                    }
                    break;
                case MySql:
                    getPOOL().setDriverClass("com.mysql.cj.jdbc.Driver");
                    if (outTime != 0)
                    {
                        url = url + "?" + "connectTimeout=" + socketTimeOut * 1000 + "&socketTimeout=" + socketTimeOut * 1000;
                    }
                    break;
                default:
                    break;
            }
            getPOOL().setJdbcUrl(url);
            getPOOL().setUser(user);
            getPOOL().setPassword(password);
            getPOOL().setMaxPoolSize(maxPoolSize);
            getPOOL().setMinPoolSize(minPoolSize);
            getPOOL().setMaxIdleTime(30);
            getPOOL().setMaxConnectionAge(86400);
            getPOOL().setCheckoutTimeout(10000);// 获取连接等待超时时间
            // getPOOL().setMaxStatements(10);
            // getPOOL().setMaxStatementsPerConnection(20);
            setTimeOut(outTime);
        }
        // }
        // catch (PropertyVetoException e)
        // {
        // DataAsExpSet.throwExpByResCode("1", new String[] { "url", "user", "password", "maxPoolSize", "minPoolSize", "outTime" },
        // new Object[] { url, user, password, maxPoolSize, minPoolSize, outTime }, e, "06#001#0001");
        // }
    }

    /**
     * 设置参数
     * 
     * @param databaseType
     *            数据库类型
     * @param url
     *            数据库连接地址
     * @param user
     *            用户名
     * @param password
     *            密码
     * @param maxPoolSize
     *            最大连接数
     * @param minPoolSize
     *            最小连接数
     * @throws PropertyVetoException
     *             驱动加载异常
     */
    public static void initPool(DatabaseType databaseType, String url, String user, String password, int maxPoolSize, int minPoolSize) throws PropertyVetoException
    {
        if (getPOOL() == null)
        {
            DbOperBase.poolDbType = databaseType;
            setPOOL(new ComboPooledDataSource());
            switch (databaseType) {
                case Postgre:
                    getPOOL().setDriverClass("org.postgresql.Driver");
                    break;
                case MySql:
                    getPOOL().setDriverClass("com.mysql.jdbc.Driver");
                    break;
                default:
                    break;
            }
            getPOOL().setJdbcUrl(url);
            getPOOL().setUser(user);
            getPOOL().setPassword(password);
            getPOOL().setMaxPoolSize(maxPoolSize);
            getPOOL().setMinPoolSize(minPoolSize);
            getPOOL().setMaxIdleTime(30);
            getPOOL().setMaxConnectionAge(86400);
            getPOOL().setCheckoutTimeout(10000);// 获取连接等待超时时间
        }
    }

    /**
     * 对时间进行设置
     * 
     * @param maxIdleTime
     *            最大空闲时间
     * @param maxConnectionAge
     *            最大连接时间
     * @param checkoutTimeout
     *            连接等待超时时间
     */
    public static void setPoolTime(Integer maxIdleTime, Integer maxConnectionAge, Integer checkoutTimeout)
    {

        if (getPOOL() != null)
        {
            // 设置最大空闲时间
            if (maxIdleTime != null)
            {
                getPOOL().setMaxIdleTime(maxIdleTime);
            }

            // 最大连接时间
            if (maxConnectionAge != null)
            {
                getPOOL().setMaxConnectionAge(maxConnectionAge);
            }

            // 连接等待超时时间
            if (checkoutTimeout != null)
            {
                getPOOL().setCheckoutTimeout(checkoutTimeout);
            }

            getPOOL().setBreakAfterAcquireFailure(false);
        }
    }

    /**
     * 打开连接
     * 
     * @throws Exception
     *             异常
     */
    public void openConn() throws Exception
    {
        if (usePool)
        {
            if (getPOOL() != null)
            {
                if (conn == null || conn.isClosed())
                {
                    // // synchronized (LOCK.intern())
                    // // {
                    conn = getPOOL().getConnection();
                    // String uuid = UUID.randomUUID().toString();
                    // countInt.addAndGet(1);
                    //
                    // String threadName = Thread.currentThread().getName();
                    //
                    // StackTraceElement[] ste = Thread.currentThread().getStackTrace();
                    // String className = ste[1].getClassName() + "." + ste[1].getMethodName() + ";" + ste[2].getClassName() + "." + ste[2].getMethodName() + ";" + ste[3].getClassName() + "_"
                    // + ste[3].getMethodName();
                    // connectionAdd(threadName, className, uuid);
                    // // }
                }
                databaseType = poolDbType;
            }
        }
        else
        {
            if (databaseType != null)
            {
                switch (databaseType) {
                    case Postgre:
                        Class.forName("org.postgresql.Driver");
                        break;
                    case MySql:
                        Class.forName("com.mysql.jdbc.Driver");
                        break;
                    case SqlServer:
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        break;
                    case Access:
                        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                        this.connUrl += ";jackcessOpener=com.goldwind.dataaccess.database.CryptCodecOpener";
                        break;
                    case Sgcc:
                        Class.forName("sgcc.nds.jdbc.driver.NdsDriver");
                        break;
                    default:
                        break;
                }
                this.conn = DriverManager.getConnection(connUrl, connUser, connPassword);
            }
        }
    }

    /**
     * 查询数据
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数
     * @return 结果集合
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public ResultSet getResultSet(String sql, Object[] params) throws DataAsException
    {
        try
        {
            sql = DbAssistant.convertSqlByDb(sql, databaseType);
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(getTimeOut());
            if (params != null && params.length != 0)
            {
                for (int i = 0; i < params.length; i++)
                {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DbOperBase_getResultSet", new String[] { "sql", "params" }, new Object[] { sql, params }, e, "06#002#0001");
        }
        return rs;
    }

    /**
     * 查询数据
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数
     * @return 结果集合
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public ResultSet getUpdateableRs(String sql, Object[] params) throws DataAsException
    {
        try
        {
            sql = DbAssistant.convertSqlByDb(sql, databaseType);
            ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setQueryTimeout(getInsertTimeOut());
            if (params != null && params.length != 0)
            {
                for (int i = 0; i < params.length; i++)
                {
                    ps.setObject(i + 1, params[i]);
                }
            }
            rs = ps.executeQuery();
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DbOperBase_getUpdateableRs", new String[] { "sql", "params" }, new Object[] { sql, params }, e, "06#003#0001");
        }
        return rs;
    }

    /**
     * 添加、修改或删除数据
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数
     * @return 成功行数
     * @throws DataAsException
     *             自定义异常
     */
    public int excute(String sql, Object[] params) throws SQLException, DataAsException
    {
        int num = 0;
        try
        {
            sql = DbAssistant.convertSqlByDb(sql, databaseType);
            ps = conn.prepareStatement(sql);
            ps.setQueryTimeout(getInsertTimeOut());
            if (params != null && params.length != 0)
            {
                for (int i = 0; i < params.length; i++)
                {
                    ps.setObject(i + 1, params[i]);
                }
            }
            num = ps.executeUpdate();
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DbOperBase_excute", new String[] { "sql", "params" }, new Object[] { sql, params }, e, "06#003#0001");
        }
        return num;
    }

    /**
     * 添加数据并返回自增值
     * 
     * @param sql
     *            sql语句
     * @param params
     *            参数
     * @return 自增值
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public long insert(String sql, Object[] params) throws SQLException, DataAsException
    {
        long id = 0;
        sql = DbAssistant.convertSqlByDb(sql, databaseType);
        ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setQueryTimeout(getInsertTimeOut());
        try (ResultSet rst = ps.getGeneratedKeys();)
        {
            if (params != null && params.length != 0)
            {
                for (int i = 0; i < params.length; i++)
                {
                    ps.setObject(i + 1, params[i]);
                }
            }
            if (ps.executeUpdate() > 0 && rst.next())
            {
                id = rst.getLong(1);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DbOperBase_insert", new String[] { "sql", "params" }, new Object[] { sql, params }, e, "06#003#0001");
        }
        return id;
    }

    /**
     * 据使用事务批量执行SQL语句（不忽略重复数据），推荐使用excuteBatchSqlThrowErr方法
     * 
     * @param sqlArray
     *            sql数组
     * @throws SQLException
     *             执行异常
     * @return 正常则返回-2
     * @throws DataAsException
     *             自定义异常
     */
    @Deprecated
    public boolean excuteBatchSql(String[] sqlArray) throws SQLException, DataAsException
    {
        transaction();
        for (String sql : sqlArray)
        {
            try (Statement stm = conn.createStatement();)
            {
                if (null != sql && !"".equals(sql) && sql.length() > 0)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    stm.executeUpdate(sql);
                }
            }
            catch (SQLException e)
            {
                rollback();
                throw e;
            }
        }
        commit();
        return true;
    }

    /**
     * 据使用事务批量执行SQL语句，抛出异常
     * 
     * @param sqlArray
     *            sql数组
     * @throws SQLException
     *             执行异常
     * @throws DataAsException
     *             自定义异常 自定义异常
     */
    public void excuteBatchSqlThrowErr(String[] sqlArray) throws SQLException, DataAsException
    {
        transaction();
        for (String sql : sqlArray)
        {
            try
            {
                if (null != sql && !"".equals(sql) && sql.length() > 0)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (SQLException e)
            {
                rollback();
                throw e;
            }
        }
        commit();
    }

    /**
     * 忽略重复数据使用事务批量执行SQL语句(本地库同步专用)
     * 
     * @param sqlArray
     *            数组
     * @return 返回结果
     * @throws SQLException
     *             异常
     * @throws DataAsException
     *             异常
     */
    public int excuteBatchSqlForsqlite(String[] sqlArray) throws SQLException, DataAsException
    {
        int i = -1;
        transaction();
        for (String sql : sqlArray)
        {
            i++;
            Statement stm = null;
            try
            {
                if (!StringUtils.isEmpty(sql))
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    stm = conn.createStatement();
                    stm.executeUpdate(sql);
                }
            }
            catch (SQLException e)
            {
                rollback();
                if (!DbAssistant.checkDbRepeatKeyExp(databaseType, e))
                {
                    throw e;
                }
                else
                {
                    return i;
                }
            }
            finally
            {
                stm.close();
            }
        }
        commit();
        return -2;
    }

    /**
     * 不处理异常信息进行入库操作
     * 
     * @param sqlArray
     *            sql数组
     * @return 出错位置以及错误信息
     * @throws SQLException
     *             sql异常
     * @throws DataAsException
     *             自定义异常
     */
    public HashMap<Integer, Exception> excuteBatchSqlNew(String[] sqlArray) throws SQLException, DataAsException
    {
        // long starttime = System.currentTimeMillis();
        int i = -1;
        HashMap<Integer, Exception> eh = new HashMap<>();
        transaction();
        for (String sql : sqlArray)
        {
            i++;
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (Exception e)
            {
                rollback();
                eh.put(i, e);
                return eh;
            }
        }
        commit();
        return eh;
    }

    /**
     * 带有savepoint的进行入库操作
     * 
     * @param sqlArray
     *            sql数组
     * @return 出错位置以及错误信息
     * @throws SQLException
     *             sql异常
     * @throws DataAsException
     *             自定义异常
     */
    public HashMap<Integer, Exception> excuteBatchSqlSavePoint(String[] sqlArray) throws SQLException, DataAsException
    {
        int i = -1;
        HashMap<Integer, Exception> eh = new HashMap<>();
        transaction();
        Savepoint savepoint = null;
        for (String sql : sqlArray)
        {
            i++;
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                    savepoint = conn.setSavepoint(String.valueOf(i));
                }
            }
            catch (Exception e)
            {
                if (savepoint != null)
                {
                    rollback(savepoint);
                    commit();
                }
                else
                {
                    rollback();
                }
                eh.put(i, e);
                return eh;
            }
        }
        commit();
        return eh;
    }

    /**
     * 忽略错误批量入库操作
     * 
     * @param sqlArray
     *            sql数组
     * @return 出错位置以及错误信息
     * @throws SQLException
     *             sql异常
     * @throws DataAsException
     *             自定义异常
     */
    public HashMap<Integer, Exception> excuteBatchSqlIgnorErr(String[] sqlArray) throws SQLException, DataAsException
    {
        // long starttime = System.currentTimeMillis();
        int i = -1;
        HashMap<Integer, Exception> eh = new HashMap<>();
        transaction();
        for (String sql : sqlArray)
        {
            i++;
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (Exception e)
            {
                // rollback();
                if (!DbAssistant.checkDbRepeatKeyExp(databaseType, e))
                {
                    eh.put(i, e);
                }
            }
        }
        commit();
        return eh;
    }

    /**
     * 忽略错误批量入库操作
     * 
     * @param sqlArray
     *            sql数组
     * @return 出错位置以及错误信息
     * @throws SQLException
     *             sql异常
     * @throws DataAsException
     *             自定义异常
     */
    public HashMap<Integer, Exception> excuteBatchSqlIgnorErrWithOutdataBase(String[] sqlArray) throws SQLException, DataAsException
    {
        // long starttime = System.currentTimeMillis();
        int i = -1;
        HashMap<Integer, Exception> eh = new HashMap<>();
        transaction();
        for (String sql : sqlArray)
        {
            i++;
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (Exception e)
            {
                // rollback();
                if (!DbAssistant.checkDbRepeatKeyExp(databaseType, e))
                {
                    eh.put(i, e);
                }
            }
        }
        commit();
        return eh;
    }

    /**
     * 忽略重复数据使用事务批量执行SQL语句 判断是否需要转化
     * 
     * @param sqlArray
     *            sql数组
     * @param tranf
     *            是否需要转化 true需要
     * @return 第一次出错的sql所在数组中的位置,正常则返回-2
     * @throws SQLException
     *             执行异常
     * @throws DataAsException
     *             自定义异常
     */
    public int excuteBatchSql2(String[] sqlArray, boolean tranf) throws SQLException, DataAsException
    {
        int i = -1;
        transaction();
        for (String sql : sqlArray)
        {
            i++;
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    if (tranf)
                    {
                        sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    }
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (SQLException e)
            {
                rollback();
                if (!DbAssistant.checkDbRepeatKeyExp(databaseType, e))
                {
                    throw e;
                }
                else
                {
                    return i;
                }
            }
        }
        commit();
        return -2;
    }

    /**
     * 不忽略重复错误使用事务批量执行SQL语句
     * 
     * @param sqlArray
     *            sql数组
     * @throws SQLException
     *             执行异常
     * @throws DataAsException
     *             自定义异常
     */
    public void excuteBatchSqls(String[] sqlArray) throws SQLException, DataAsException
    {
        transaction();
        for (String sql : sqlArray)
        {
            try
            {
                if (!"".equals(sql) && sql.length() > 0 && null != sql)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    // Statement ste=conn.createStatement();
                    // ste.addBatch(sql);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
            }
            catch (SQLException e)
            {
                rollback();
                throw e;
            }
        }
        commit();
    }

    /**
     * 不使用事务批量执行SQL语句
     * 
     * @param sqlArray
     *            sql数组
     * @return 异常集合
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public void runBatchSql(String[] sqlArray) throws SQLException, DataAsException
    {
        conn.setAutoCommit(true);
        for (int i = 0; i < sqlArray.length; i++)
        {

            String sql = sqlArray[i];
            if (null != sql && !"".equalsIgnoreCase(sql) && sql.length() != 0)
            {
                sql = DbAssistant.convertSqlByDb(sql, databaseType);
                ps = conn.prepareStatement(sql);
                ps.setQueryTimeout(getInsertTimeOut());
                ps.executeUpdate();
            }
        }
    }

    /**
     * 不使用事务批量执行SQL语句，返回map
     * 
     * @param sqlArray
     *            sql数组
     * @return 异常集合
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public HashMap<Integer, Exception> runBatchSqlwhithMap(String[] sqlArray) throws SQLException, DataAsException
    {
        conn.setAutoCommit(true);
        HashMap<Integer, Exception> hm = new HashMap<Integer, Exception>();
        for (int i = 0; i < sqlArray.length; i++)
        {
            try
            {
                // long starttime = System.currentTimeMillis();

                String sql = sqlArray[i];
                if (null != sql && !"".equalsIgnoreCase(sql) && sql.length() != 0)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sql);
                    ps.setQueryTimeout(getInsertTimeOut());
                    ps.executeUpdate();
                }
                // long endtime = System.currentTimeMillis();
                // if (endtime - starttime > 1000)
                // {
                // LOG.info("+_+_+_+_+_+_+_+" + Thread.currentThread().getId() + "单次执行一次时间:" + (endtime - starttime) + ",其中sql是:" + sql);
                // }
            }
            catch (SQLException e)
            {
                hm.put(i, e);
            }
        }
        return hm;
    }

    /**
     * 使用事务批量执行更新SQL语句
     * 
     * @param sqlArray
     *            sql数组
     * @return 执行结果
     * @throws SQLException
     *             执行异常
     * @throws DataAsException
     *             自定义异常
     */
    public boolean updateBatchSql(String[] sqlArray) throws SQLException, DataAsException
    {
        transaction();
        for (String sql : sqlArray)
        {
            try
            {
                sql = DbAssistant.convertSqlByDb(sql, databaseType);
                ps = conn.prepareStatement(sql);
                ps.setQueryTimeout(getInsertTimeOut());
                if (ps.executeUpdate() == 0)
                {
                    rollback();
                    return false;
                }
            }
            catch (SQLException e)
            {
                rollback();
                throw e;
            }
        }
        commit();
        return true;
    }

    /**
     * 关闭连接
     * 
     * @throws SQLException
     *             数据库异常
     */
    public void closeConn() throws SQLException
    {
        try
        {
            if (rs != null)
            {
                rs.close();
                rs = null;
            }
            if (ps != null)
            {
                ps.close();
                ps = null;
            }
        }
        catch (Exception e)
        {
            logger.error("DbOperBase_closeConn_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
        }
        if (conn != null && !conn.isClosed())
        {
            // synchronized (LOCK)
            // {
            // if (usePool)
            // {
            // if (getPOOL() != null)
            // {
            // countInt.decrementAndGet();
            //
            // String threadName = Thread.currentThread().getName();
            //
            // StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            // String className = ste[1].getClassName() + "." + ste[1].getMethodName() + ";" + ste[2].getClassName() + "." + ste[2].getMethodName() + ";" + ste[3].getClassName() + "_"
            // + ste[3].getMethodName();
            // // 连接数减少
            // connectionMouse(threadName, className);
            // }
            // }
            // }
            conn.close();
            conn = null;
        }
    }

    /**
     * 
     * @Title: closeResultSet
     * @Description: 关闭结果集
     * @throws SQLException
     *             异常
     * @return: void
     */
    public void closeResultSet() throws SQLException
    {
        if (rs != null)
        {
            rs.close();
            rs = null;
        }
    }

    /**
     * 开启事务
     * 
     * @throws SQLException
     *             数据库异常
     */
    public void transaction() throws SQLException
    {
        conn.setAutoCommit(false);
    }

    /**
     * 提交事务
     * 
     * @throws SQLException
     *             数据库异常
     */
    public void commit() throws SQLException
    {
        conn.commit();
        conn.setAutoCommit(true);
    }

    /**
     * 回滚事务
     * 
     * @throws SQLException
     *             数据库异常
     */
    public void rollback() throws SQLException
    {
        conn.rollback();
        conn.setAutoCommit(true);
    }

    /**
     * 带savepoint事务的回滚
     * 
     * @throws SQLException
     *             数据库异常
     */
    public void rollback(Savepoint savepoint) throws SQLException
    {
        conn.rollback(savepoint);
        // conn.setAutoCommit(true);
    }

    /**
     * 插入带大对象的数据（仅支持postgres数据库）
     * 
     * @param sql
     *            插入语句
     * @param params
     *            参数
     * @return 执行成功条数
     * @throws Exception
     *             自定义异常
     */
    public int insertOid(String sql, Object[] params) throws Exception
    {
        PgConnection pgConn = null;
        int re = 0;
        try
        {
            if (databaseType.equals(DatabaseType.Postgre))
            {
                Class.forName("org.postgresql.Driver");
                if (usePool)
                {
                    pgConn = (PgConnection) DriverManager.getConnection(getPOOL().getJdbcUrl(), getPOOL().getUser(), getPOOL().getPassword());

                }
                else
                {
                    pgConn = (PgConnection) DriverManager.getConnection(connUrl, connUser, connPassword);
                }
            }
            pgConn.setAutoCommit(false);
            ps = pgConn.prepareStatement(sql);
            ps.setQueryTimeout(getInsertTimeOut());
            if (params != null && params.length != 0)
            {
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i] instanceof FileInputStream)
                    {
                        FileInputStream fis = (FileInputStream) params[i];
                        LargeObjectManager lobj = pgConn.getLargeObjectAPI();
                        long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
                        LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
                        byte[] buf = new byte[8192];
                        int s = 0;
                        while ((s = fis.read(buf)) != -1)
                        {
                            obj.write(buf, 0, s);
                        }
                        fis.close();
                        obj.close();
                        ps.setLong(i + 1, oid);
                    }
                    else if (params[i] instanceof ByteArrayInputStream)
                    {
                        ByteArrayInputStream fis = (ByteArrayInputStream) params[i];
                        LargeObjectManager lobj = pgConn.getLargeObjectAPI();
                        long oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
                        LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
                        int s = 0;
                        byte[] temp = new byte[1024];
                        while ((s = fis.read(temp)) != -1)
                        {
                            obj.write(temp, 0, s);
                        }
                        fis.close();
                        obj.close();
                        ps.setLong(i + 1, oid);
                    }
                    else if (params[i] instanceof Integer)
                    {
                        ps.setInt(i + 1, (int) params[i]);
                    }
                    else if (params[i] instanceof java.util.Date)
                    {
                        // 时间精确到秒 ——fcy 2018.11.15
                        String time = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format((java.util.Date) params[i]);
                        ps.setTimestamp(i + 1, new java.sql.Timestamp(DataAsFunc.parseDateTime(time).getTime().getTime()));
                        // ps.setTimestamp(i + 1, new java.sql.Timestamp(((java.util.Date) params[i]).getTime()));
                        continue;
                    }
                    else
                    {
                        ps.setObject(i + 1, params[i]);
                    }

                }
            }
            re = ps.executeUpdate();
            pgConn.setAutoCommit(true);
        }
        finally
        {
            if (pgConn != null)
            {
                pgConn.close();
            }
        }
        return re;
    }

    @Override
    @SuppressWarnings("squid:S2975")
    public DbOperBase clone() throws CloneNotSupportedException
    {
        return (DbOperBase) super.clone();
    }

    @Override
    public void close() throws IOException
    {
        try
        {
            closeConn();
        }
        catch (SQLException e)
        {
            logger.error(e);
        }
    }

    /**
     * add
     * 
     * @param threadName
     * @param className
     */
    // private synchronized static void connectionAdd(String threadName, String className, String uuid)
    // {
    //
    // // 连接数量
    // if (connectionCountHashMap.containsKey(threadName))
    // {
    // int count = connectionCountHashMap.get(threadName);
    // connectionCountHashMap.put(threadName, ++count);
    // }
    // else
    // {
    // connectionCountHashMap.put(threadName, 1);
    // }
    // // 连接数对应的类
    // if (connectionClasssHashMap.containsKey(threadName))
    // {
    // String clazzs = connectionClasssHashMap.get(threadName) + "------" + className;
    // connectionClasssHashMap.put(threadName, clazzs);
    // }
    // else
    // {
    // connectionClasssHashMap.put(threadName, className);
    // }
    // TODO 测试
    // try
    // {
    // boolean t = false;
    // int threadCount = 0;
    // for (String threadName2 : connectionCountHashMap.keySet())
    // {
    // threadCount += connectionCountHashMap.get(threadName2);
    // if (connectionCountHashMap.get(threadName2) > 3)
    // {
    // t = true;
    // }
    // }
    // if (POOL.getNumBusyConnections() > d)
    // {
    // LOG.info("open(" + uuid + ")-------总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections() + ", 计算的连接数：" + threadCount
    // + ", 单个线程是否超过3个连接:" + t);
    // for (String threadName1 : connectionCountHashMap.keySet())
    // {
    // LOG.info("线程名称(" + uuid + ")：" + threadName1 + ",占用连接个数：" + connectionCountHashMap.get(threadName1));
    // Set<String> set1 = new HashSet<String>();
    // String name = connectionClasssHashMap.get(threadName1);
    // String[] nameA = name.split("------");
    // for (String n : nameA)
    // {
    // set1.add(n);
    // }
    // LOG.info("类名称(" + uuid + "):");
    // for (String n : set1)
    // {
    // String[] ss2A = n.split(";");
    // LOG.info(ss2A[1] + "---->" + ss2A[2]);
    // }
    // }
    //
    // }
    // }
    // catch (SQLException e)
    // {
    // LOGGER.error("DbOperBase_connectionAdd_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
    // }

    // }

    /**
     * 减少连接数
     * 
     * @param threadName
     * @param className
     */
    // private synchronized static void connectionMouse(String threadName, String className)
    // {
    //
    // if (connectionCountHashMap.containsKey(threadName))
    // {
    // int count = connectionCountHashMap.get(threadName);
    // connectionCountHashMap.put(threadName, --count);
    // }
    // // 连接数对应的类
    // if (connectionClasssHashMap2.containsKey(threadName))
    // {
    // String clazzs = connectionClasssHashMap2.get(threadName) + "------" + className;
    // connectionClasssHashMap2.put(threadName, clazzs);
    // }
    // else
    // {
    // connectionClasssHashMap2.put(threadName, className);
    // }
    //
    // }

    public static ComboPooledDataSource getPOOL()
    {
        return pool;
    }

    public static void setPOOL(ComboPooledDataSource pOOL)
    {
        pool = pOOL;
    }

    public static int getInsertTimeOut()
    {
        return insertTimeOut;
    }

    public static void setInsertTimeOut(int insertTimeOut)
    {
        DbOperBase.insertTimeOut = insertTimeOut;
    }

    public static int getTimeOut()
    {
        return timeOut;
    }

    public static void setTimeOut(int timeOut)
    {
        DbOperBase.timeOut = timeOut;
    }

}
