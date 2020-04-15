/**   
 * Copyright © 2018 金风科技. All rights reserved.
 * 
 * @Title: DataAsMemory.java 
 * @Prject: lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: 报错内存加载
 * @author: Administrator   
 * @date: 2018年7月24日 上午10:20:41 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @ClassName: DataAsMemory
 * @Description: 报错内存加载
 * @author czr
 * @date: 2018年7月24日 上午10:20:41
 */
public class DataAsMemory
{
    /**
     * KEY :language_country 资源文件内存
     */
    private static final HashMap<String, Map<String, String>> DATAEXPMEMORY = new HashMap<>(20);

    /**
     * 包路径
     */
    private static final String PACKAGEFRONT = "com.goldwind";

    /**
     * 资源文件包名
     */
    private static final String RESOURCEFILE = "resourcefile/Language";

    /**
     * @Title: syncDataExpMemoryOut
     * @Description: 同步异常内存
     * @param filePath
     *            文件路径
     * @param names
     *            子包名称
     * @return map
     * @throws Exception
     *             异常
     * @return: Map<String,String>
     */
    public Map<String, String> syncDataExpMemoryOut(String filePath, DataAsDef.PACKAGE_NAME... names) throws Exception
    {
        return syncDataExpMemory(filePath, names);
    }

    /**
     * @Title: syncDataExpMemory
     * @Description: 同步异常内存
     * @param filePath
     *            文件路径
     * @param names
     *            子包名称
     * @return map
     * @throws Exception
     *             异常
     * @return: Map<String,String>
     */
    private Map<String, String> syncDataExpMemory(String filePath, DataAsDef.PACKAGE_NAME... names) throws Exception
    {
        Map<String, String> repeatMap = new HashMap<String, String>(20);
        for (DataAsDef.PACKAGE_NAME name : names)
        {
            // 获取文件路径
            String fileResPath = PACKAGEFRONT + "." + name.toString().toLowerCase() + "." + RESOURCEFILE;
            for (DataAsDef.LANGUAGE l : DataAsDef.LANGUAGE.values())
            {
                Locale locale = new Locale(l.getLanguage(), l.getCountry());
                repeatMap.putAll(setMap(fileResPath, locale, l.getCharset()));

            }
        }
        return repeatMap;
    }

    /**
     * 读取配置文件
     * 
     * @Title: readProperty1
     * @Description: 读取配置文件
     * @param fileResPath
     *            路径
     * @param locale
     *            本地化
     * @param charset
     *            编码
     * @return 重复的数据
     * @return: Map<String,String>
     * @throws UnsupportedEncodingException
     */
    private Map<String, String> readProperty1(String fileResPath, Locale locale, Charset charset) throws UnsupportedEncodingException
    {
        Map<String, String> map = new HashMap<>(20);
        ResourceBundle msgBundle = ResourceBundle.getBundle(fileResPath, locale);
        Enumeration<String> enum1 = msgBundle.getKeys();
        // 将ResourceBundle转成hashmap
        while (enum1.hasMoreElements())
        {
            String key = enum1.nextElement();
            String msg = new String(msgBundle.getString(key).getBytes(charset), StandardCharsets.UTF_8);
            map.put(key, msg);
        }
        return map;

    }

    /**
     * 读取proper文件
     * 
     * @Title: readProperty2
     * @Description: 读取proper文件
     * @param filePath
     *            路径
     * @return map
     * @throws Exception
     *             异常
     * @return: Map<String,String>
     */
    private Map<String, String> readProperty2(String filePath) throws Exception
    {
        // 返回map
        Map<String, String> map = new HashMap<>(20);
        // 读取property文件
        Properties prop = new Properties();
        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        prop.load(in);

        // 将Properties转成map
        Set<Entry<Object, Object>> entrySet = prop.entrySet();
        for (Entry<Object, Object> entry : entrySet)
        {
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
        return map;
    }

    /**
     * 依据条件获取文件名称
     * 
     * @Title: getFileName
     * @Description: 依据条件获取文件名称
     * @param path
     *            文件目录
     * @param fileInName
     *            包含的名称
     * @return 返回文件名称
     * @return: List<String>
     */
    private List<String> getFileName(String path, String fileInName)
    {
        // 判断是否存在
        File f = new File(path);
        // 不存在返回null
        if (!f.exists())
        {
            return null;
        }
        File[] fa = f.listFiles();
        // 列表长度是否0
        if (fa.length == 0)
        {
            return null;
        }
        // 将符合名称放置到list中
        List<String> fileNames = new ArrayList<String>(2);
        for (int i = 0; i < fa.length; i++)
        {
            File fs = fa[i];
            if (fs.getName().indexOf(fileInName) >= 0)
            {
                fileNames.add(fs.getName());
            }
        }
        return fileNames;

    }

    /**
     * 设置map
     * 
     * @Title: setMap
     * @Description: 设置map
     * @param fileResPath
     *            文件路径
     * @param locale
     *            本地化属性
     * @param charset
     *            编码
     * @return 返回map
     * @throws Exception
     *             异常
     * @return: Map<String,String>
     */
    private Map<String, String> setMap(String fileResPath, Locale locale, Charset charset) throws Exception
    {
        // 返回map
        Map<String, String> resultMap = new HashMap<String, String>(20);

        // 读取配置文件
        Map<String, String> m = readProperty1(fileResPath, locale, charset);
        // 设置key
        String key = locale.getLanguage() + "_" + locale.getCountry();

        if (getDataExpMemory().containsKey(key))
        {
            Map<String, String> oldMap = getDataExpMemory().get(key);
            if (!m.isEmpty())
            {
                for (String key1 : m.keySet())
                {
                    if (oldMap.containsKey(key1))
                    {
                        resultMap.put(key1, m.get(key1));
                    }
                    else
                    {
                        oldMap.put(key1, m.get(key1));
                    }
                }
            }
        }
        else
        {
            getDataExpMemory().put(key, m);
        }
        return resultMap;
    }

    public static HashMap<String, Map<String, String>> getDataExpMemory()
    {
        return DATAEXPMEMORY;
    }

}
