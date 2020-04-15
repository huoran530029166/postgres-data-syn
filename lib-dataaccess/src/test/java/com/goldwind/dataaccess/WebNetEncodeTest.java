/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: WebNetEncodeTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年8月27日 下午7:28:11 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/** 
 * @ClassName: WebNetEncodeTest 
 * @Description: WebNetEncodeTest
 * @author: Administrator
 * @date: 2019年8月27日 下午7:28:11  
 */
public class WebNetEncodeTest   
{

    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#toHexString(java.lang.String)}.
     */
    @Test
    public void testToHexStringNull()
    {
        String str=null;
        String  act=WebNetEncode.toHexString(str);
        String exp="";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#toHexString(java.lang.String)}.
     */
    @Test
    public void testToHexString()
    {
        String str="a123";
        String  act=WebNetEncode.toHexString(str);
        String exp="0x61313233";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#toStringHex(java.lang.String)}.
     */
    @Test
    public void testToStringHex()
    {
        String hex="0x61313233";
        String act=WebNetEncode.toStringHex(hex);
        String exp="a123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#bytesToHexString(byte[])}.
     */
    @Test
    public void testBytesToHexString()
    {
        String str="a123";
        String act=WebNetEncode.bytesToHexString(str.getBytes());
        String exp="61313233";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#encodeHexStr(int, java.lang.String)}.
     */
    @Test
    public void testEncodeHexStrGBK()
    {
        String str="a123";
        String act=WebNetEncode.encodeHexStr(15,str);
        String exp="61313233";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#encodeHexStr(int, java.lang.String)}.
     */
    @Test
    public void testEncodeHexStrISO()
    {
        String str="a123";
        String act=WebNetEncode.encodeHexStr(3,str);
        String exp="61313233";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#encodeHexStr(int, java.lang.String)}.
     */
    @Test
    public void testEncodeHexStrUTF()
    {
        String str="a123";
        String act=WebNetEncode.encodeHexStr(8,str);
        String exp="0061003100320033";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.dataaccess.WebNetEncode#encodeHexStr(int, java.lang.String)}.
     */
    @Test
    public void testEncodeHexStrASCII()
    {
        String str="a123";
        String act=WebNetEncode.encodeHexStr(7,str);
        String exp="61313233";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
}
