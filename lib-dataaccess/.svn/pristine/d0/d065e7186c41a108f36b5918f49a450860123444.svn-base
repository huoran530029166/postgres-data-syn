/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: StringUtilTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: StringUtilTest
 * @author: Administrator   
 * @date: 2019年8月27日 下午5:13:57 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.junit.Test;

import junit.framework.Assert;

/** 
 * @ClassName: StringUtilTest 
 * @Description: StringUtilTest
 * @author: Administrator
 * @date: 2019年8月27日 下午5:13:57  
 */
public class StringUtilTest 
{

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasLength(java.lang.CharSequence)}.
     */
    @Test
    public void testHasLengthCharSequenceNull()
    {
        String str=null;
        boolean act=StringUtil.hasLength((CharSequence)str);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasLength(java.lang.CharSequence)}.
     */
    @Test
    public void testHasLengthCharSequenceEmpty()
    {
        String str="";
        boolean act=StringUtil.hasLength((CharSequence)str);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasLength(java.lang.CharSequence)}.
     */
    @Test
    public void testHasLengthCharSequence()
    {
        String str="123";
        boolean act=StringUtil.hasLength((CharSequence)str);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasLength(java.lang.String)}.
     */
    @Test
    public void testHasLengthString()
    {
        String str="234";
        boolean act=StringUtil.hasLength(str);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasText(java.lang.CharSequence)}.
     */
    @Test
    public void testHasTextCharSequenceNull()
    {
        String str=null;
        boolean act=StringUtil.hasText((CharSequence)str);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasText(java.lang.CharSequence)}.
     */
    @Test
    public void testHasTextCharSequence()
    {
        String str="123";
        boolean act=StringUtil.hasText((CharSequence)str);
        Assert.assertTrue(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasText(java.lang.CharSequence)}.
     */
    @Test
    public void testHasTextCharSequenceAllSpace()
    {
        String str="    ";
        boolean act=StringUtil.hasText((CharSequence)str);
        Assert.assertFalse(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#hasText(java.lang.String)}.
     */
    @Test
    public void testHasTextString()
    {
        String str=" 1   ";
        boolean act=StringUtil.hasText((CharSequence)str);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#isNumeric(java.lang.String)}.
     */
    @Test
    public void testIsNumericNull()
    {
        String str=null;
        boolean act=StringUtil.isNumeric(str);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#isNumeric(java.lang.String)}.
     */
    @Test
    public void testIsNumericAlphabet()
    {
        String str="a123";
        boolean act=StringUtil.isNumeric(str);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#isNumeric(java.lang.String)}.
     */
    @Test
    public void testIsNumericOther()
    {
        //小数点
        String str="12.3";
        boolean act=StringUtil.isNumeric(str);
        Assert.assertFalse(act);
        //空格
        str="12 3";
        act=StringUtil.isNumeric(str);
        Assert.assertFalse(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.StringUtil#isNumeric(java.lang.String)}.
     */
    @Test
    public void testIsNumeric()
    {
        //小数点
        String str="123";
        boolean act=StringUtil.isNumeric(str);
        Assert.assertTrue(act);
    }
    
    @Test
    public void testIp4() 
    {
        String ip="192.168.1.117";
        Assert.assertTrue(StringUtil.isIpv4(ip));
        ip="qewqrqewr";
        Assert.assertTrue(!StringUtil.isIpv4(ip));
        ip="";
        Assert.assertTrue(!StringUtil.isIpv4(ip));
        ip=null;
        Assert.assertTrue(!StringUtil.isIpv4(ip));
    }
}
