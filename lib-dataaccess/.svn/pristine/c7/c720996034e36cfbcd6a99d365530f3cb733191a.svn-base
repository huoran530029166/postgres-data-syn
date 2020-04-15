package com.goldwind.dataaccess;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 数据验证工具类
 *
 * @author shenlf
 *
 */
public class DataValid
{
    //ip地址的正则表达式
    private static String IPREGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";


    /**
     * 验证数据是否为空
     *
     * @param data
     *            数据
     * @throws DataAsException
     *             自定义异常
     */
    public static void checkEmpty(String data) throws DataAsException
    {
        if (StringUtils.isEmpty(data))
        {
            DataAsExpSet.throwExpByResCode("DataValid_checkEmpty_1", null, null, null);
        }
    }

    /**
     * 验证数据是否为空
     *
     * @param data
     *            数据
     * @return 验证成功返回true
     * @throws DataAsException
     *             自定义异常
     */
    public static boolean checkEmptyR(String data)
    {
        if (StringUtils.isEmpty(data))
        {
            return false;
        }
        return true;
    }

    /**
     * 检查数据长度
     *
     * @param data
     *            数值
     * @param length
     *            长度
     * @throws DataAsException
     *             自定义异常
     */
    public static void checkLength(String data, int length) throws DataAsException
    {
        if (StringUtils.isNotEmpty(data))
        {
            if (data.length() > length)
            {
                DataAsExpSet.throwExpByResCode("DataValid_checkLength_1", null, null, null);
            }
        }
    }

    /**
     * 验证数据是否为数字格式
     *
     * @param data
     *            数值
     * @param min
     *            最小值
     * @param max
     *            最大值
     * @throws DataAsException
     *             自定义异常
     */
    public static void checkNumData(String data, long min, long max) throws DataAsException
    {
        long d = 0;
        try
        {
            d = Long.valueOf(data);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataValid_checkNumData_1", new String[] { "data", "min", "max" }, new Object[] { data, min, max }, e);
        }

        if (d < min || d > max)
        {
            DataAsExpSet.throwExpByResCode("DataValid_checkNumData_2", new String[] { "data", "min", "max" }, new Object[] { data, min, max }, null);
        }
    }

    /**
     * 验证数据是否为数字格式
     *
     * @param data
     *            数值
     * @return 比较结果
     */
    public static boolean checkNumDataR(String data)
    {
        long d = 0;
        try
        {
            d = Long.valueOf(data.trim());
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 比较数值数据
     *
     * @param data1
     *            数值1
     * @param data2
     *            数值2
     * @throws DataAsException
     *             自定义异常
     */
    public static void compareNumData(String data1, String data2) throws DataAsException
    {
        checkNumData(data1, Long.MIN_VALUE, Long.MAX_VALUE);
        checkNumData(data2, Long.MIN_VALUE, Long.MAX_VALUE);
        if (Long.valueOf(data1).longValue() >= Long.valueOf(data2).longValue())
        {
            DataAsExpSet.throwExpByResCode("DataValid_compareNumData_1", new String[] { "data1", "data2" }, new Object[] { data1, data2 }, null);
        }
    }

    /**
     * 验证数据是否为整数格式
     *
     * @param data
     * @param min
     * @param max
     * @throws DataAsException
     */
    public static void checkIntData(String data, int min, int max) throws DataAsException
    {
        int d = 0;
        try
        {
            d = Integer.valueOf(data);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataValid_checkIntData_1", new String[] { "data", "min", "max" }, new Object[] { data, min, max }, e);
        }

        if (d < min || d > max)
        {
            DataAsExpSet.throwExpByResCode("DataValid_checkIntData_2", new String[] { "data", "min", "max" }, new Object[] { data, min, max }, null);
        }
    }

    /**
     * 验证数据是否为整数格式
     *
     * @param data
     * @param min
     * @param max
     * @return
     */
    public static boolean checkIntDataR(String data, int min, int max)
    {
        try
        {
            int d = Integer.valueOf(data.trim()).intValue();
            if (d < min || d > max)
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 检查网络地址
     *
     * @param data
     *            网络地址
     * @return 检查结果
     */
    public static boolean checkNetAddress(String data)
    {
        try
        {
            String[] arr = data.split(":");
            //判断是否为空和是否为3段
            if (StringUtils.isEmpty(arr[0]) || arr[0].split("\\.").length <= 2)
            {
                return false;
            }
            //判断是否为ip地址
            if (!arr[0].matches(IPREGEX))
            {
                return false;
            }
            //判断端口
            if (arr.length > 1 && !checkIntDataR(arr[1], 1, 65535))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    /**
     * 验证IP
     *
     * @param ip
     *            IP地址
     * @return 是否是IP地址（和上面一个功能重复）
     */
    @Deprecated
    public static boolean checkIp(String ip)
    {
        return ip.matches(IPREGEX);
    }

    /**
     * 验证ip
     *
     * @param ip
     *            IP地址
     * @throws DataAsException
     *             异常
     */
    public static void isIp(String ip) throws DataAsException
    {
        try
        {
            if (ip.split("\\.").length != 4)
            {
                throw new Exception("IP is not the correct format.");
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataValid_isIp_1", new String[] { "ip" }, new Object[] { ip }, e);
        }
        if (!ip.matches(IPREGEX))
        {
            DataAsExpSet.throwExpByResCode("DataValid_isIp_1", new String[] { "ip" }, new Object[] { ip }, null);
        }
    }

    /**
     * 检查盘符
     *
     * @param data
     *            数据
     * @return 是否
     * @throws DataAsException
     *             异常
     */
    public static boolean checkDiskSymbol(String data) throws DataAsException
    {
        //是否为空字符串或者是有长度为2
        if (!checkEmptyR(data) || data.length() != 2)
        {
            return false;
        }
        //第一个字符是否为a-z
        if (data.toLowerCase().toCharArray()[0] < 'a' || data.toLowerCase().toCharArray()[0] > 'z')
        {
            return false;
        }
        //第二个字符是否为：
        if (data.toCharArray()[1] != ':')
        {
            return false;
        }
        return true;
    }

    /**
     * 检查文件名
     *
     * @param data
     *            数据
     * @return 是否
     * @throws DataAsException
     *             异常
     */
    public static boolean checkFileNameChar(String data) throws DataAsException
    {
        //是否为控制符串
        if (!checkEmptyR(data))
        {
            return false;
        }
        //这个只针对windows 对linux文件也不建议使用这些特殊字符
        if (data.indexOf('/') >= 0 || data.indexOf('\\') >= 0 || data.indexOf(':') >= 0 || data.indexOf('*') >= 0 || data.indexOf('?') >= 0 || data.indexOf('"') >= 0 || data.indexOf('<') >= 0
                || data.indexOf('>') >= 0 || data.indexOf('|') >= 0)
        {
            return false;
        }
        return true;
    }
}
