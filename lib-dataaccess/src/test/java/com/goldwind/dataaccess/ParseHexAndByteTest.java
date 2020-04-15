/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: ParseHexAndByteTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年8月7日 下午7:35:16 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/** 
 * @ClassName: ParseHexAndByteTest 
 * @Description: ParseHexAndByteTest
 * @author: Administrator
 * @date: 2019年8月7日 下午7:35:16  
 */
public class ParseHexAndByteTest    
{

    /**
     * Test method for {@link com.goldwind.dataaccess.ParseHexAndByte#bytesToHexString(byte[])}.
     */
    @Test
    public void testBytesToHexString()
    {
        String expStr="a12A";
        //对应的数值为97(a)49(1)50(2)65(A)
        String act="61313241";
        Assert.assertThat(ParseHexAndByte.bytesToHexString(expStr.getBytes()), Matchers.equalTo(act));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.ParseHexAndByte#hexStringToBytes(java.lang.String)}.
     */
    @Test
    public void testHexStringToBytes()
    {
        String exp="61313241";
        String act="a12A";
        Assert.assertArrayEquals(ParseHexAndByte.hexStringToBytes(exp), act.getBytes());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.ParseHexAndByte#base64String2ByteFun(java.lang.String)}.
     */
    @Test
    public void testBase64String2ByteFun()
    {
        String expStr="Z01W";
        byte[] bytes=ParseHexAndByte.base64String2ByteFun(expStr);
        Assert.assertThat(expStr, Matchers.equalTo(ParseHexAndByte.byte2Base64StringFun(bytes)));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.ParseHexAndByte#byte2Base64StringFun(byte[])}.
     */
    @Test
    public void testByte2Base64StringFun()
    {
        String expStr="a0a1";
        byte[] bytes=ParseHexAndByte.base64String2ByteFun(expStr);
        Assert.assertThat(expStr, Matchers.equalTo(ParseHexAndByte.byte2Base64StringFun(bytes)));
    }

}
