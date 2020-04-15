package com.goldwind.dataaccess;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 
 * @author 曹阳
 *
 */
public class ResourceFileFactory
{
    private ResourceFileFactory()
    {
    }

    /**
     * 资源文件列表
     */
    private static Map<String, ResourceBundle> MSGRESOURCEMAP = new HashMap<String, ResourceBundle>();

    /**
     * 获取资源文件
     * 
     * @param fileResPath
     *            资源文件路径
     * @param locale
     *            本地语言环境,为空时默认当前语言环境
     * @return ResourceBundle 资源文件
     */
    public static ResourceBundle getResourceFile(String fileResPath, Locale locale)
    {
        ResourceBundle msgResObj = MSGRESOURCEMAP.get(fileResPath);
        if (msgResObj == null)
        {
            if (locale != null)
            {
                msgResObj = ResourceBundle.getBundle(fileResPath, locale);
            }
            else
            {
                msgResObj = ResourceBundle.getBundle(fileResPath);
            }

            MSGRESOURCEMAP.put(fileResPath, msgResObj);
        }
        return msgResObj;
    }

    /**
     * 获取国际化资源信息
     *
     * @param fileResPath
     *            资源文件路径
     * @param key
     *            键
     * @return 值
     */
    public static String getMessage(String fileResPath, String key)
    {
        return getMessage(fileResPath, Locale.getDefault(), key);
    }

    /**
     * 获取国际化资源信息
     *
     * @param fileResPath
     *            资源文件路径
     * @param locale
     *            国际化信息
     * @param key
     *            键
     * @return 值
     */
    public static String getMessage(String fileResPath, Locale locale, String key)
    {
        Locale lo = locale;
        if (null == lo)
        {
            lo = Locale.getDefault();
        }
        ResourceBundle bundle = getResourceFile(fileResPath, lo);
        if (null != bundle)
        {
            if (bundle.containsKey(key))
            {
                return bundle.getString(key);
            }
        }
        return null;
    }
}
