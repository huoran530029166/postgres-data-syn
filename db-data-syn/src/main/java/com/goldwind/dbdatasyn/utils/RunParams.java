package com.goldwind.dbdatasyn.utils;

public class RunParams
{
    /**
     * PG数据库连接URL
     */
    private static String database_url;
    /**
     * PG数据库用户名
     */
    private static String username;
    /**
     * PG数据库密码
     */
    private static String password;
    /**
     * 连接池最大连接数
     */
    private static int max_pool_size;
    /**
     * 连接池最小连接数
     */
    private static int min_pool_size;
    /**
     * 数据库连接超时时间
     */
    private static int db_outtime;
    /**
     * 数据库空闲连接最大时长
     */
    private static int max_idletime;
    /**
     * 本地库地址
     */
    private static String sqlitePath;

    public static String getSqlitePath()
    {
        return sqlitePath;
    }

    public static void setSqlitePath(String sqlitePath)
    {
        RunParams.sqlitePath = sqlitePath;
    }

    public static String getDatabase_url()
    {
        return database_url;
    }

    public static void setDatabase_url(String database_url)
    {
        RunParams.database_url = database_url;
    }

    public static String getUsername()
    {
        return username;
    }

    public static void setUsername(String username)
    {
        RunParams.username = username;
    }

    public static String getPassword()
    {
        return password;
    }

    public static void setPassword(String password)
    {
        RunParams.password = password;
    }

    public static int getMax_pool_size()
    {
        return max_pool_size;
    }

    public static void setMax_pool_size(int max_pool_size)
    {
        RunParams.max_pool_size = max_pool_size;
    }

    public static int getMin_pool_size()
    {
        return min_pool_size;
    }

    public static void setMin_pool_size(int min_pool_size)
    {
        RunParams.min_pool_size = min_pool_size;
    }

    public static int getDb_outtime()
    {
        return db_outtime;
    }

    public static void setDb_outtime(int db_outtime)
    {
        RunParams.db_outtime = db_outtime;
    }

    public static int getMax_idletime()
    {
        return max_idletime;
    }

    public static void setMax_idletime(int max_idletime)
    {
        RunParams.max_idletime = max_idletime;
    }
}
