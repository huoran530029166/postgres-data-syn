package com.goldwind.dataaccess.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * INI文件操作
 * 
 * @author 曹阳
 *
 */
public class IniFileOper
{
    /**
     * 配置文件
     */
    private static Properties iniFile = new Properties();
    /**
     * 文件路径
     */
    private String filePath;

    public IniFileOper(String filePath) throws IOException
    {
        setFilePath(filePath);
        if (iniFile.isEmpty())
        {
            iniFile.load(new FileInputStream(filePath));
        }
    }

    /**
     * 重新加载INI文件
     * 
     * @throws IOException
     *             i/o异常
     */
    public void reLoadIniFile() throws IOException
    {
        if (!iniFile.isEmpty())
        {
            iniFile.clear();
            iniFile.load(new FileInputStream(getFilePath()));
        }
    }

    /**
     * 设置键值
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @throws IOException
     *             i/o异常
     */
    public synchronized void setProperty(String key, String value) throws IOException
    {
        iniFile.setProperty(key, value);
        FileOutputStream fos = new FileOutputStream(getFilePath());
        iniFile.store(fos, null);
        PrintWriter pw = new PrintWriter(fos);
        pw.flush();
        pw.close();
    }

    /**
     * 读取键值
     * 
     * @param key
     *            键
     * @return 值
     */
    public String getProperty(String key)
    {
        return iniFile.getProperty(key);
    }

    /**
     * 得到所有的key和value
     */
    public Map<String, String> getAllValue()
    {
        Map<String, String> hp = new HashMap<>();
        Iterator<String> it = iniFile.stringPropertyNames().iterator();
        while (it.hasNext())
        {
            // it.next()--得到key的方法
            String key = it.next();
            hp.put(key, iniFile.getProperty(key));
        }
        return hp;
    }

    public Properties getKey()
    {
        return iniFile;
    }

    /**
     * @return the filePath
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

}
