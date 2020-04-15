

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
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.postgresql.jdbc.PgConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbAssistant;
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
//    /**
//     * 输出日志
//     */
//    private static Log LOG = Log.getLog("data");
    
    /**
     * 错误日志
     */
    private static Log LOG = Log.getLog(DbOperBase.class);

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
    private static ComboPooledDataSource POOL;
    /**
     * 连接池数据库类型
     */
    private static DatabaseType POOLDBTYPE;

    /**
     * PreparedStatement
     */
    private PreparedStatement ps;

    private ResultSet rs;

    private static final int c = 10;
    
    private static final int d = 20;

    /**
     * 现有的连接数
     */
    public static ConcurrentHashMap<String, Integer> connectionCountHashMap = new ConcurrentHashMap<String, Integer>();
    /**
     * 连接对应的方法
     */
    public static ConcurrentHashMap<String, String> connectionClasssHashMap = new ConcurrentHashMap<String, String>();
    /**
     * 现有的连接数1
     */
    public static ConcurrentHashMap<String, Integer> connectionCountHashMap1 = new ConcurrentHashMap<String, Integer>();
    /**
     * 删除连接对应的方法
     */
    public static ConcurrentHashMap<String, String> connectionClasssHashMap2 = new ConcurrentHashMap<String, String>();

    public static String a = "1234";

    public static volatile AtomicInteger countInt = new AtomicInteger(0);

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
     * @throws PropertyVetoException
     *             驱动加载异常
     */
    public static void initPool(DatabaseType databaseType, String url, String user, String password, int maxPoolSize, int minPoolSize) throws PropertyVetoException
    {
        if (POOL == null)
        {
            DbOperBase.POOLDBTYPE = databaseType;
            POOL = new ComboPooledDataSource();
            switch (databaseType)
            {
                case Postgre:
                    POOL.setDriverClass("org.postgresql.Driver");
                    break;
                case MySql:
                    POOL.setDriverClass("com.mysql.jdbc.Driver");
                    break;
                default:
                    break;
            }
            POOL.setJdbcUrl(url);
            POOL.setUser(user);
            POOL.setPassword(password);
            POOL.setMaxPoolSize(maxPoolSize);
            POOL.setMinPoolSize(minPoolSize);
            POOL.setMaxIdleTime(20);
            POOL.setMaxConnectionAge(86400);
            POOL.setCheckoutTimeout(10000);// 获取连接等待超时时间
            POOL.setBreakAfterAcquireFailure(false);
            POOL.setIdleConnectionTestPeriod(10);
        }
    }

    /**
     * 打开连接
     * 
     * @throws Exception
     *             异常
     */
    public synchronized void openConn() throws Exception
    {
        synchronized (a.intern())
        {
            if (usePool)
            {
                if (POOL != null)
                {
                    if (conn == null || conn.isClosed())
                    {

                        conn = POOL.getConnection();
                        String uuid = UUID.randomUUID().toString();
                        countInt.addAndGet(1);

                        String threadName = Thread.currentThread().getName();
                        long s = System.currentTimeMillis();

                        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
                        String className = ste[1].getClassName() + "." + ste[1].getMethodName() + ";" + ste[2].getClassName() + "." + ste[2].getMethodName() + ";" + ste[3].getClassName() + "_"
                            + ste[3].getMethodName();
                        waitAdd(threadName, className, uuid);
                        long e = System.currentTimeMillis();
                        if (POOL.getNumConnections() > c)
                        {
                            LOG.info("线程：" + threadName + ", 开始时间：" + s);
                            LOG.info(" 数量：" + countInt);
                            LOG.info("线程：" + threadName + ", 结束时间：" + e);
                        }

                        // if (POOL.getNumBusyConnections() > 30)
                        // {
                        // String uuid = UUID.randomUUID().toString();
                        // String sql = "select state,query from pg_stat_activity where client_addr='10.80.31.166' and datname='v5new' and application_name!='NAVICAT'";
                        // ResultSet rs = getResultSet(sql, null);
                        // while (rs.next())
                        // {
                        // LOG.info("(" + uuid + ") state:" + rs.getString("state") + ",query:" + rs.getString("query"));
                        // }
                        // }
                    }

                    databaseType = POOLDBTYPE;

                }

            }
            else
            {
                if (databaseType != null)
                {
                    switch (databaseType)
                    {
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
    }

    /**
     * add
     * 
     * @param threadName
     * @param className
     */
    private synchronized static void waitAdd(String threadName, String className, String uuid)
    {

        // 连接数量
        if (connectionCountHashMap.containsKey(threadName))
        {
            int count = connectionCountHashMap.get(threadName);
            connectionCountHashMap.put(threadName, ++count);
        }
        else
        {
            connectionCountHashMap.put(threadName, 1);
        }
        // 连接数对应的类
        if (connectionClasssHashMap.containsKey(threadName))
        {
            String clazzs = connectionClasssHashMap.get(threadName) + "------" + className;
            connectionClasssHashMap.put(threadName, clazzs);
        }
        else
        {
            connectionClasssHashMap.put(threadName, className);
        }
        // TODO 测试
        try
        {
            boolean t = false;
            int threadCount = 0;
            for (String threadName2 : connectionCountHashMap.keySet())
            {
                threadCount += connectionCountHashMap.get(threadName2);
                // if(threadCount==POOL.getNumBusyConnections()){
                // t=true;
                // }
                if (connectionCountHashMap.get(threadName2) > 3)
                {
                    t = true;
                }
            }
            if (POOL.getNumBusyConnections() > d)
            {
                LOG.info("open(" + uuid + ")-------总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections() + ", 计算的连接数：" + threadCount
                    + ", 单个线程是否超过3个连接:" + t);
                for (String threadName1 : connectionCountHashMap.keySet())
                {
                    LOG.info("线程名称(" + uuid + ")：" + threadName1 + ",占用连接个数：" + connectionCountHashMap.get(threadName1));
                    Set<String> set1 = new HashSet<String>();
                    String name = connectionClasssHashMap.get(threadName1);
                    String[] nameA = name.split("------");
                    for (String n : nameA)
                    {
                        set1.add(n);
                    }
                    LOG.info("类名称(" + uuid + "):");
                    for (String n : set1)
                    {
                        String[] ss2A = n.split(";");
                        LOG.info(ss2A[1] + "---->" + ss2A[2]);
                    }
                    LOG.info("");
                    // LOG.info("删除类名称(" + uuid + ")：" + connectionClasssHashMap2.get(threadName1));
                }

                // System.out.println("open(" + uuid + ")-------总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections());
                // for (String threadName1 : connectionCountHashMap.keySet())
                // {
                // System.out.println("线程名称(" + uuid + ")：" + threadName1 + ",占用连接个数：" + connectionCountHashMap.get(threadName1));
                // System.out.println("类名称(" + uuid + ")：" + connectionClasssHashMap.get(threadName1));
                // }
            }
        }
        catch (SQLException e)
        {
            LOG.error(e);
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
    public ResultSet getResultSet(String sql, Object[] params) throws SQLException, DataAsException
    {
        sql = DbAssistant.convertSqlByDb(sql, databaseType);
        ps = conn.prepareStatement(sql);
        if (params != null && params.length != 0)
        {
            for (int i = 0; i < params.length; i++)
            {
                ps.setObject(i + 1, params[i]);
            }
        }
        rs = ps.executeQuery();
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
    public ResultSet getUpdateableRs(String sql, Object[] params) throws SQLException, DataAsException
    {
        sql = DbAssistant.convertSqlByDb(sql, databaseType);
        ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        if (params != null && params.length != 0)
        {
            for (int i = 0; i < params.length; i++)
            {
                ps.setObject(i + 1, params[i]);
            }
        }
        rs = ps.executeQuery();
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
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    public int excute(String sql, Object[] params) throws SQLException, DataAsException
    {
        sql = DbAssistant.convertSqlByDb(sql, databaseType);
        ps = conn.prepareStatement(sql);
        if (params != null && params.length != 0)
        {
            for (int i = 0; i < params.length; i++)
            {
                ps.setObject(i + 1, params[i]);
            }
        }
        return ps.executeUpdate();
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
        if (params != null && params.length != 0)
        {
            for (int i = 0; i < params.length; i++)
            {
                ps.setObject(i + 1, params[i]);
            }
        }
        if (ps.executeUpdate() > 0)
        {
            ResultSet rs1 = ps.getGeneratedKeys();
            if (rs.next())
            {
                id = rs1.getLong(1);
            }
        }
        return id;
    }

    /**
     * 忽略重复数据使用事务批量执行SQL语句
     * 
     * @param sqlArray
     *            sql数组
     * @throws SQLException
     *             执行异常
     * @return 第一次出错的sql所在数组中的位置,正常则返回-2
     * @throws DataAsException
     *             自定义异常
     */
    public int excuteBatchSql(String[] sqlArray) throws SQLException, DataAsException
    {
        // long starttime = System.currentTimeMillis();
        int i = -1;
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
                    ps.executeUpdate();
                }
            }
            catch (SQLException e)
            {
                // long starttime = System.currentTimeMillis();
                rollback();
                // LOG.info("++++++++++回滚所用时间:" + (System.currentTimeMillis() - starttime));
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
        // long endtime = System.currentTimeMillis();
        // if (endtime - starttime > 5000)
        // {
        // LOG.info("=========" + Thread.currentThread().getId() + "批量执行一次时间:" + (endtime - starttime) + ",其中sql是:" + Arrays.toString(sqlArray));
        // }
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
                    ps = conn.prepareStatement(sql);
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
    public HashMap<Integer, Exception> runBatchSql(String[] sqlArray) throws SQLException, DataAsException
    {
        conn.setAutoCommit(true);
        HashMap<Integer, Exception> hm = new HashMap<Integer, Exception>();
        for (int i = 0; i < sqlArray.length; i++)
        {
            try
            {
                // long starttime = System.currentTimeMillis();

                String sql = sqlArray[i];
                if (!"".equalsIgnoreCase(sql) && sql.length() != 0 && null != sql)
                {
                    sql = DbAssistant.convertSqlByDb(sql, databaseType);
                    ps = conn.prepareStatement(sqlArray[i]);
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
    public synchronized void closeConn() throws SQLException
    {
        synchronized (a.intern())
        {
            String uid = null;
            if (usePool)
            {
                if (POOL != null)
                {
                    if (POOL.getNumConnections() > c)
                    {
                        uid = UUID.randomUUID().toString();
                        LOG.info("关闭连接前个数统计-----------(" + uid + "),总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections());
                    }
                }
            }
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
            if (conn != null && !conn.isClosed())
            {

                if (usePool)
                {
                    if (POOL != null)
                    {
                        countInt.decrementAndGet();

                        String threadName = Thread.currentThread().getName();
                        long s = System.currentTimeMillis();

                        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
                        String className = ste[1].getClassName() + "." + ste[1].getMethodName() + ";" + ste[2].getClassName() + "." + ste[2].getMethodName() + ";" + ste[3].getClassName() + "_"
                            + ste[3].getMethodName();
                        waitMouse(threadName, className);
                        long e = System.currentTimeMillis();

                        if (POOL.getNumConnections() > c)
                        {
                            LOG.info("结束线程：" + threadName + ", 开始时间：" + s);
                            LOG.info(" 数量：" + countInt);
                            LOG.info("线程：" + threadName + ", 结束时间：" + e);
                        }

                    }
                }

                conn.close();

                conn = null;
                if (usePool)
                {
                    if (POOL != null)
                    {
                        if (uid != null)
                        {
                            try
                            {
                                Thread.sleep(20L);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            LOG.info("关闭连接后个数统计-----------(" + uid + "),总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections());
                        }
                    }
                }
            }

        }

    }

    private synchronized static void waitMouse(String threadName, String className)
    {

        if (connectionCountHashMap.containsKey(threadName))
        {
            int count = connectionCountHashMap.get(threadName);
            connectionCountHashMap.put(threadName, --count);
        }
        // 连接数对应的类
        if (connectionClasssHashMap2.containsKey(threadName))
        {
            String clazzs = connectionClasssHashMap2.get(threadName) + "------" + className;
            connectionClasssHashMap2.put(threadName, clazzs);
        }
        else
        {
            connectionClasssHashMap2.put(threadName, className);
        }
        // // TODO 测试
        // try
        // {
        // if (POOL.getNumBusyConnections() > 30)
        // {
        // LOG.info("总连接数：" + POOL.getNumConnections() + ",使用连接数：" + POOL.getNumBusyConnections() + ",空闲连接数：" + POOL.getNumIdleConnections());
        // for (String threadName1 : connectionCountHashMap.keySet())
        // {
        // LOG.info("线程名称：" + threadName1 + ",占用连接个数：" + connectionCountHashMap.get(threadName1));
        // LOG.info("类名称：" + connectionClasssHashMap.get(threadName1));
        // }
        // }
        // }
        // catch (SQLException e)
        // {
        // LOG.error(e);
        // }

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
     * 插入带大对象的数据（仅支持postgres数据库）
     * 
     * @param sql
     *            插入语句
     * @param params
     *            参数
     * @return 执行成功条数
     */
    public int insertOid(String sql, Object[] params)
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
                    pgConn = (PgConnection) DriverManager.getConnection(POOL.getJdbcUrl(), POOL.getUser(), POOL.getPassword());

                }
                else
                {
                    pgConn = (PgConnection) DriverManager.getConnection(connUrl, connUser, connPassword);
                }
            }
            pgConn.setAutoCommit(false);
            ps = pgConn.prepareStatement(sql);
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
                        continue;
                    }
                    if (params[i] instanceof ByteArrayInputStream)
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
                        continue;
                    }
                    else if (params[i] instanceof Integer)
                    {
                        ps.setInt(i + 1, (int) params[i]);
                        continue;
                    }
                    else if (params[i] instanceof java.util.Date)
                    {
                        ps.setDate(i + 1, new java.sql.Date(((java.util.Date) params[i]).getTime()));
                        continue;
                    }
                    else
                    {
                        ps.setObject(i + 1, params[i]);
                        continue;
                    }
                }
            }
            re = ps.executeUpdate();
            pgConn.setAutoCommit(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (pgConn != null)
            {
                try
                {
                    pgConn.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return re;
    }

    @Override
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
            e.printStackTrace();
        }
    }

}
