/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataEncryptTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: DataEncryptTest
 * @author: Administrator   
 * @date: 2019年8月20日 下午4:03:30 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName: DataEncryptTest
 * @Description: DataEncryptTest
 * @author: Administrator
 * @date: 2019年8月20日 下午4:03:30
 */
public class DataEncryptTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataEncrypt#controlLinkEncrypt(java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testControlLinkEncrypt() throws Exception
    {
        String act = DataEncrypt.controlLinkEncrypt("300032", "127.0.0.1", "8804", "1467");
        String exp = "7e385d9a8c3671a7c1fa2234d9dbb70b";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataEncrypt#controlOrderEncrypt(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testControlOrderEncrypt() throws Exception
    {
        String act = DataEncrypt.controlOrderEncrypt("30012", "1", "1", "descrcn", "descren", "iecfixed");
        String exp = "29c514b257baf4e69331d95565b72a9d";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataEncrypt#controlOrderEncrypt(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testControlOrderEncrypt1() throws Exception
    {
        String act = DataEncrypt.controlOrderEncrypt("123", "233", "peltext", "30012", "Iec.sjdk", "iecpathdescrcn", "iecpathdescren");
        String exp = "80b4b350009c086ced3daf4699da4d07";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

}
