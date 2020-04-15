package com.goldwind.dataaccess;

/**
 * 字符串工具类
 * 
 * @author 冯春源
 */
public final class StringUtil
{

    private StringUtil()
    {
    }

    /**
     * 检查给定的字符序列不为null,长度不为0 注意: 仅包含空白字符串的序列将返回true.
     * 
     * @param str
     *            待检查的字符序列
     * @return 如果字符序列不为null,并且有长度,将返回true
     * @see #hasText(String)
     */
    public static boolean hasLength(CharSequence str)
    {
        return str != null && str.length() > 0;
    }

    /**
     * 检查给定的字符串不为null,长度不为0 注意: 空白字符串将返回true.
     * 
     * @param str
     *            待检查的字符串
     * @return 如果字符串不为null,并且有长度,将返回true
     * @see #hasLength(CharSequence)
     */
    public static boolean hasLength(String str)
    {
        return hasLength((CharSequence) str);
    }

    /**
     * 检查给定的字符序列是否有实际的文本
     * 
     * @param str
     *            待检查的字符序列
     * @return 如果字符序列不为null,长度大于0,并且不是仅包含空白字符
     * @see Character#isWhitespace
     */
    public static boolean hasText(CharSequence str)
    {
        if (!hasLength(str))
        {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查给定的字符串是否有实际的文本
     * 
     * @param str
     *            待检查的字符串
     * @return 如果字符串不为null,长度大于0,并且不是仅包含空白字符
     * @see #hasText(CharSequence)
     */
    public static boolean hasText(String str)
    {
        return hasText((CharSequence) str);
    }

    /**
     * 利用JAVA自带的函数判断字符串是否是纯数字
     * 
     * @param str
     *            传入的字符串
     * @return 判断结果
     */
    public static boolean isNumeric(String str)
    {
        if (str == null || str.isEmpty())
        {
            return false;
        }

        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 取得一个字符串中，指定字符出现的次数
     * 
     * @param str
     *            字符串
     * @param str
     *            指定字符
     * @return 指定字符出现的次数
     */
    public static int countString(String st, String m)
    {
        int count = 0;
        while (st.indexOf(m) >= 0)
        {
            st = st.substring(st.indexOf(m) + m.length());
            count++;
        }
        return count;
    }
    
    /**
     * 正则校验ip是否合法
     * xjs 2020-1-7 14:08:22
     * @param ipstr
     * @return
     */
    public static boolean isIpv4(String  ipstr)
    {
        if (!hasText(ipstr))
        {
           return false; 
        }
        String regIp = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9]).(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d).(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d).(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return ipstr.matches(regIp);        
    }
}
