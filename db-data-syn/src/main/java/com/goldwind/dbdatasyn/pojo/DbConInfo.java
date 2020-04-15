package com.goldwind.dbdatasyn.pojo;

public class DbConInfo
{
    /**
     * PG数据库连接URL
     */
    private String database_url;
    /**
     * PG数据库用户名
     */
    private String username;
    /**
     * PG数据库密码
     */
    private String password;
    /**
     * 连接池最大连接数
     */
    private int max_pool_size;
    /**
     * 连接池最小连接数
     */
    private int min_pool_size;
    /**
     * 数据库连接超时时间
     */
    private int db_outtime;
    /**
     * 数据库空闲连接最大时长
     */
    private int max_idletime;

    public String getDatabase_url()
    {
        return database_url;
    }

    public void setDatabase_url(String database_url)
    {
        this.database_url = database_url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getMax_pool_size()
    {
        return max_pool_size;
    }

    public void setMax_pool_size(int max_pool_size)
    {
        this.max_pool_size = max_pool_size;
    }

    public int getMin_pool_size()
    {
        return min_pool_size;
    }

    public void setMin_pool_size(int min_pool_size)
    {
        this.min_pool_size = min_pool_size;
    }

    public int getDb_outtime()
    {
        return db_outtime;
    }

    public void setDb_outtime(int db_outtime)
    {
        this.db_outtime = db_outtime;
    }

    public int getMax_idletime()
    {
        return max_idletime;
    }

    public void setMax_idletime(int max_idletime)
    {
        this.max_idletime = max_idletime;
    }
}
