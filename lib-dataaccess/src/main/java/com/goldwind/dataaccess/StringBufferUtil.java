/**   
 * Copyright © 2018 金风科技. All rights reserved.
 * 
 * @Title: StringBufferUtil.java 
 * @Prject: lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2018年5月8日 上午11:41:01 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

/**
 * @ClassName: StringBufferUtil
 * @Description: StringBuffer工具类
 * @author: Administrator
 * @date: 2018年5月8日 上午11:41:01
 */
public class StringBufferUtil
{
    /**
     * @Title: sbReplaceAll
     * @Description: StringBuffer的字符替换
     * @param sb 替换的StringBuffer
     * @param oldStr 需要替换的老字符串
     * @param newStr 新的字符串
     * @return
     * @return: StringBuffer
     */
    public static void sbReplaceAll(StringBuffer sb, String oldStr, String newStr)
    {
        //获取一地个需要替换字符串的位置
        int i = sb.indexOf(oldStr);
        //替换字符串的长度
        int oldLen = oldStr.length();
        int newLen = newStr.length();
        while (i > -1)
        {
            //删除需要替换的字符串
            sb.delete(i, i + oldLen);
            //插入新的字符串
            sb.insert(i, newStr);
            i = sb.indexOf(oldStr, i + newLen);
        }
    }
}
