package com.goldwind.dataaccess.rtdb.golden.oper;

import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.rtdb.service.impl.ServerImplPool;

/**
 * 庚顿连接池类
 * 
 * @author shenlf
 *
 */
public class GoldenServerPool
{
    /**
     * 数据库类型
     */
    private static DatabaseType POOLDBTYPE;

    public static DatabaseType getPoolDbType()
    {
        return POOLDBTYPE;
    }

    /**
     * 实时库ip
     */
    private static String IP;

    public static String getIp()
    {
        return IP;
    }

    public static int getPort()
    {
        return PORT;
    }

    /**
     * 实时库端口，默认6327
     */
    private static int PORT;
    /**
     * 实时库用户名
     */
    private static String USERNAME;
    /**
     * 实时库密码
     */
    private static String PWD;
    /**
     * 连接池的连接数
     */
    private static int POOLSIZE;
    /**
     * 连接池的最大连接数
     */
    private static int POOLMAXSIZE;
    /**
     * 代理认证的key(大数据连接用，开启Socks5代理的连接方式，避免HTTP检查)
     */
    private static String SOCKSKEY;
    /**
     * 目标服务器用户名(大数据连接用，开启Socks5代理的连接方式，避免HTTP检查)
     */
    private static String TARGETUSER;
    /**
     * 本地代理端口(大数据连接用，开启Socks5代理的连接方式，避免HTTP检查)
     */
    private static int LOCALPROXYPORT;
    /**
     * 保存和查询测点时，是否转换为小写(此配置是为了保存和查询的时候兼容三版的实时库，即连接五版实时库时-true,连接三版实时库时-false)
     */
    private static boolean LOWERCASEFLG;

    /**
     * 初始化连接池
     * 
     * @param ip
     *            实时库ip
     * @param port
     *            实时库端口
     * @param userName
     *            实时库用户名
     * @param pwd
     *            实时库密码
     * @param poolSize
     *            连接池的连接数
     * @param poolMaxSize
     *            连接池的最大连接数
     * @param databaseType
     *            数据库类型
     * @param socksKey
     *            代理认证的key
     * @param targetUser
     *            目标服务器用户名
     * @param localProxyPort
     *            本地代理端口
     * @param lowercaseFlg
     *            保存和查询测点时，是否转换为小写(此配置是为了保存和查询的时候兼容三版的实时库，即连接五版实时库时-true,连接三版实时库时-false)
     */
    public static void initPool(String ip, int port, String userName, String pwd, int poolSize, int poolMaxSize, DatabaseType databaseType, String socksKey, String targetUser, int localProxyPort,
            boolean lowercaseFlg)
    {
        GoldenServerPool.POOLDBTYPE = databaseType;
        GoldenServerPool.IP = ip;
        GoldenServerPool.PORT = port;
        GoldenServerPool.USERNAME = userName;
        GoldenServerPool.PWD = pwd;
        GoldenServerPool.POOLSIZE = poolSize;
        GoldenServerPool.POOLMAXSIZE = poolMaxSize;
        GoldenServerPool.SOCKSKEY = socksKey;
        GoldenServerPool.TARGETUSER = targetUser;
        GoldenServerPool.LOCALPROXYPORT = localProxyPort;
        GoldenServerPool.LOWERCASEFLG = lowercaseFlg;
    }

    // 取得Golden数据库连接池
    public static ServerImplPool getPool()
    {
        return GoldenServerPoolHandler.POOL;
    }

    private static class GoldenServerPoolHandler
    {
        /**
         * 创建连接池
         */
        private static ServerImplPool POOL = new ServerImplPool(IP, PORT, USERNAME, PWD, POOLSIZE, POOLMAXSIZE);
    }

    public static String getSOCKSKEY()
    {
        return SOCKSKEY;
    }

    public static void setSOCKSKEY(String sOCKSKEY)
    {
        SOCKSKEY = sOCKSKEY;
    }

    public static String getTARGETUSER()
    {
        return TARGETUSER;
    }

    public static void setTARGETUSER(String tARGETUSER)
    {
        TARGETUSER = tARGETUSER;
    }

    public static int getLOCALPROXYPORT()
    {
        return LOCALPROXYPORT;
    }

    public static void setLOCALPROXYPORT(int lOCALPROXYPORT)
    {
        LOCALPROXYPORT = lOCALPROXYPORT;
    }

    public static boolean getLOWERCASEFLG()
    {
        return LOWERCASEFLG;
    }

    public static void setLOWERCASEFLG(boolean lowercaseFlg)
    {
        LOWERCASEFLG = lowercaseFlg;
    }
}
