package com.goldwind.dataaccess;

/**
 * boolean工具类
 * 
 * @author 李壮壮
 * @date 2020年4月7日
 */
public class BooleanUtil
{
    /**
     * 字符串常量false
     */
    static String falseStr = "false";

    /**
     * 字符串常量true
     */
    static String trueStr = "true";

    /**
     * true和false转1和0
     * 
     * @param param
     *            iecvalue
     * @return 0或1
     */
    public static String getIntString(String param)
    {
        String value = param;
        if (trueStr.equalsIgnoreCase(param))
        {
            value = "1";
        }
        else if (falseStr.equalsIgnoreCase(param))
        {
            value = "0";
        }
        return value;
    }

    /**
     * 布尔类型转换成true,false字符串
     * 
     * @param param
     *            布尔类型
     * @return 字符串类型的true,false
     */
    public static String getBooleanString(Boolean param)
    {
        String value = "";
        if (Boolean.TRUE.equals(param))
        {
            value = trueStr;
        }
        else if (Boolean.FALSE.equals(param))
        {
            value = falseStr;
        }
        return value;
    }

    /**
     * "true"和"false"或者"0和"1"转换成布尔值
     * 
     * @param param
     *            iecvalue
     * @return 0或1
     */
    public static boolean parseBoolean(String param)
    {
        String value = param;
        if (trueStr.equalsIgnoreCase(param) || "1".equals(param))
        {
            value = "1";
        }
        else if (falseStr.equalsIgnoreCase(param) || "0".equals(param))
        {
            value = "0";
        }
        return "1".equals(value);
    }

}
