package com.goldwind.dataaccess;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * 日志类
 * 
 * @author 张超
 *
 */
public class Log
{
    /**
     * 用slf4j 写日志
     */
    private LocationAwareLogger logger;

    /**
     * 空字符串
     */
    private static final Object[] EMPTY_ARRAY = new Object[] {};
    /**
     * 本身class名称
     */
    private static final String FQCN = Log.class.getName();

    /**
     * 日志记录周期
     */
    private static long logPeriod = 24 * 3600 * 1000L;
    /**
     * 循环删除周期
     */
    private static long period = 60 * 60 * 1000L;
    /**
     * 警告记录时间
     */
    private static ConcurrentHashMap<String, Long> warnTimes = new ConcurrentHashMap<>();
    /**
     * 错误记录时间
     */
    private static ConcurrentHashMap<String, Long> errorTimes = new ConcurrentHashMap<>();

    /**
     * 是否启动清理线程
     */
    private static boolean running = true;

    /**
     * @Title: getLocationAwareLogger 
     * @Description: 获取上层的className （未用到  先保留 ）
     * @param depth 深度
     * @return LocationAwareLogger
     * @return: LocationAwareLogger
     */
    private static LocationAwareLogger getLocationAwareLogger(final int depth)
    {
        String className = sun.reflect.Reflection.getCallerClass(depth).getName();
        return (LocationAwareLogger) LoggerFactory.getLogger(className);
    }

    /**
     * 初始化日志
     * 
     * @param configFilename
     *            配置文件
     * @param logPreiod
     *            记录周期
     * @throws JoranException 异常
     */
    public static void initLog(String configFilename, long logPreiod) throws JoranException
    {
        File file = new File(configFilename);
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.reset();
        joranConfigurator.doConfigure(file);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        logPeriod = logPreiod;
    }

    public Log(Logger logger)
    {
        this.logger = (LocationAwareLogger) logger;
    }

    /**
     * 得到Log对象
     * 
     * @param clazz
     *            类名
     * @return Log对象
     */
    public static Log getLog(Class<?> clazz)
    {
        return new Log(LoggerFactory.getLogger(clazz));
    }

    /**
     * 得到Log对象
     * 
     * @param name
     *            日志
     * @return Log对象
     */
    public static Log getLog(String name)
    {
        return new Log(LoggerFactory.getLogger(name));
    }

    public Logger getLogger()
    {
        return logger;
    }

    /**
     * @Title: isInfoEnabled
     * @Description: 重写logger的方法
     * @return boolean
     * @return: boolean 返回值
     */
    public boolean isInfoEnabled()
    {
        return logger.isInfoEnabled();
    }

    /**
     * 记录调试信息
     * 
     * @param message
     *            调试信息
     */
    public void debug(String message)
    {
        logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, message, EMPTY_ARRAY, null);
    }

    /**
     * 记录提示信息
     * 
     * @param message
     *            提示信息
     */
    public void info(String message)
    {
        logger.log(null, FQCN, LocationAwareLogger.INFO_INT, message, EMPTY_ARRAY, null);
    }

    /**
     * 记录警告信息
     * 
     * @param message
     *            警告信息
     */
    public void warn(String message)
    {
        // 如果message为null的情况直接log后结束
        if (null == message)
        {
            logger.log(null, FQCN, LocationAwareLogger.WARN_INT, message, EMPTY_ARRAY, null);
            return;
        }
        // 判断map中是否存在或者时间戳是否大于配置的时间差
        if (!getWarnTimesMap().containsKey(message) || System.currentTimeMillis() - getWarnTimesMap().get(message) > logPeriod)
        {
            getWarnTimesMap().put(message, System.currentTimeMillis());
            logger.log(null, FQCN, LocationAwareLogger.WARN_INT, message, EMPTY_ARRAY, null);
        }
    }

    /**
     * 记录错误信息
     * 
     * @param message
     *            错误信息
     */
    public void error(String message)
    {
        // 如果message为null的情况直接log后结束
        if (null == message)
        {
            logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, message, EMPTY_ARRAY, null);
            return;
        }
        // 判断map中是否存在或者时间戳是否大于配置的时间差
        if (!getErrorTimesMap().containsKey(message) || System.currentTimeMillis() - getErrorTimesMap().get(message) > logPeriod)
        {
            getErrorTimesMap().put(message, System.currentTimeMillis());
            logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, message, EMPTY_ARRAY, null);
        }
    }

    /**
     * 记录错误信息
     * 
     * @param e
     *            错误信息
     */
    public void error(Exception e)
    {
        // 在异常为null或者e.getLocalizedMessage()时直接log后结束
        if (null == e || null == e.getLocalizedMessage())
        {
            logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, null, EMPTY_ARRAY, e);
            return;
        }
        // 判断是否在map中存在或者时间差超过配置的时间戳
        if (!getErrorTimesMap().containsKey(e.getLocalizedMessage()) || System.currentTimeMillis() - getErrorTimesMap().get(e.getLocalizedMessage()) > logPeriod)
        {
            getErrorTimesMap().put(e.getLocalizedMessage(), System.currentTimeMillis());
            logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, e.getLocalizedMessage(), EMPTY_ARRAY, e);
        }

    }

    /**
     * 记录错误信息
     * 
     * @param key
     *            错误信息索引
     * @param e
     *            错误信息
     */
    public void error(String key, Exception e)
    {
        // 如果key为null直接log后结束
        if (null == key)
        {
            logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, key, EMPTY_ARRAY, e);
            return;
        }
        // 判断map中是否存在key或者时间差大于配置的时间戳
        if (!getErrorTimesMap().containsKey(key) || System.currentTimeMillis() - getErrorTimesMap().get(key) > logPeriod)
        {
            getErrorTimesMap().put(key, System.currentTimeMillis());
            // 判断e不为null
            if (e != null)
            {
                logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, key + e.getClass().getName(), EMPTY_ARRAY, e);
            }
            else
            {
                logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, key, EMPTY_ARRAY, e);
            }
        }
    }

    private class ClearThread extends Thread
    {
        /**
         * run方法
         */
        public void run()
        {
            while (isRunning())
            {
                // 设置日志内存大小为10w，超过10w则重新生成日志内存记录
                if (getErrorTimesMap().size() > 100000)
                {
                    getErrorTimesMap().clear();
                }
                if (getWarnTimesMap().size() > 100000)
                {
                    getWarnTimesMap().clear();
                }
                long nowTime = System.currentTimeMillis();
                // 此处加锁的目的是防止遍历hashmap的过程中map被锁定
                // 遍历errormap
                Iterator<Entry<String, Long>> iter = getErrorTimesMap().entrySet().iterator();
                while (iter.hasNext())
                {
                    Entry<String, Long> entry = iter.next();
                    long val = entry.getValue();

                    if (nowTime - val > logPeriod)
                    {
                        iter.remove();
                    }
                }

                // 遍历warnmap
                Iterator<Entry<String, Long>> iters = getWarnTimesMap().entrySet().iterator();
                while (iters.hasNext())
                {
                    Entry<String, Long> entry = iters.next();
                    long val = entry.getValue();

                    if (nowTime - val > logPeriod)
                    {
                        iters.remove();
                    }

                }
                try
                {
                    Thread.sleep(period);
                }
                catch (Exception e)
                {
                    logger.log(null, FQCN,  LocationAwareLogger.ERROR_INT, "Log_ClearThread_run" + "_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), EMPTY_ARRAY, e);
                }
            }
        }

    }

    /**
     * 启动清理线程
     * 
     */
    public void startLogCleanT()
    {
        Thread s = new ClearThread();
        s.setName("logCleanThread");
        s.start();
    }

    public static boolean isRunning()
    {
        return running;
    }

    public static void setRunning(boolean running)
    {
        Log.running = running;
    }

    public static Map<String, Long> getWarnTimesMap()
    {
        return warnTimes;
    }

    public static Map<String, Long> getErrorTimesMap()
    {
        return errorTimes;
    }

    public static void main(String[] args)
    {
        String msg = null;
        Exception e = null;
        Log logger = Log.getLog(Log.class);
        logger.startLogCleanT();
        try
        {
            for (int i = 0; i < 20000; i++)
            {
                logger.warn(null);
                logger.warn(i + "");
                TimeUnit.MILLISECONDS.sleep(100);
                logger.error(e);
                TimeUnit.MILLISECONDS.sleep(100);
                logger.error(msg);
                logger.error(i + "");
                TimeUnit.MILLISECONDS.sleep(100);
                logger.error(msg, e);
                logger.error(i + "", e);
                getWarnTimesMap().forEach((k, v) -> System.out.println("key:" + k + "  val:" + v));
            }
        }
        catch (InterruptedException e1)
        {
            logger.error("Interrupted!" + e1.getLocalizedMessage(), e1);
            Thread.currentThread().interrupt();
        }
        catch (Exception e2)
        {
            logger.error(e2.getLocalizedMessage(), e2);
        }
    }
}
