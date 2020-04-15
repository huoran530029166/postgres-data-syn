package com.goldwind.dataaccess.database;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis Key - Value内存数据库 操作
 * 
 * @author 谭璟
 *
 */
public class JedisOperBase
{

    /**
     * 端口号
     */
    private String ip;

    /**
     * 端口号
     */
    private int port = 6379;

    /**
     * 访问密码
     */
    private static final String AUTH = "123456";

    /**
     * 最大连接数
     */
    private static int maxActive = 8;

    /**
     * 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
     */
    private static int maxIdle = 8;

    /**
     * 最大等待时间
     */
    private static final int MAX_WAIT = 3000;

    /**
     * 超时时间
     */
    private static final int TIMEOUT = 60000;

    /**
     * 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
     */
    private static boolean testOnBorrow = false;

    /**
     * 连接池对象
     */
    private JedisPool jedisPool = null;

    /**
     * redis过期时间小时
     */
    public static final int EXRP_HOUR = 60 * 60; // 一小时

    /**
     * redis过期时间天
     */
    public static final int EXRP_DAY = 60 * 60 * 24; // 一天

    /**
     * redis过期时间月
     */
    public static final int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月

    /**
     * 初始化Redis连接池
     */
    private void initialPool()
    {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(testOnBorrow);
        jedisPool = new JedisPool(config, ip, port, TIMEOUT, AUTH);
    }

    /**
     * 在多线程环境同步初始化
     */
    private synchronized void poolInit()
    {
        if (jedisPool == null)
        {
            initialPool();
        }
    }

    /**
     * 同步获取Jedis实例
     * 
     * @return Jedis 对象
     */
    public synchronized Jedis getJedis()
    {
        if (jedisPool == null)
        {
            poolInit();
        }
        Jedis jedis = null;
        if (jedisPool != null)
        {
            jedis = jedisPool.getResource();
        }
        return jedis;
    }

    /**
     * 释放jedis资源
     * 
     * @param jedis
     *            对象
     */
    public void returnResource(final Jedis jedis)
    {
        if (jedis != null && jedisPool != null)
        {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 设置 String
     * 
     * @param key
     *            键值
     * @param value
     *            value
     */
    public synchronized void setString(String key, String value)
    {
        value = StringUtils.isEmpty(value) ? "" : value;
        getJedis().set(key, value);
    }

    /**
     * 设置 过期时间
     * 
     * @param key
     *            键值
     * @param seconds
     *            以秒为单位
     * @param value
     *            value
     */
    public synchronized void setString(String key, int seconds, String value)
    {
        value = StringUtils.isEmpty(value) ? "" : value;
        getJedis().setex(key, seconds, value);
    }

    /**
     * 获取String值
     * 
     * @param key
     *            键值
     * @return value value值
     */
    public synchronized Object getString(String key)
    {
        if (getJedis() == null || !getJedis().exists(key))
        {
            return null;
        }
        return getJedis().get(key);
    }
}
