/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataValidTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年8月5日 下午7:53:11 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

/** 
 * @ClassName: DataValidTest 
 * @Description: 单元测试
 * @author: Administrator
 * @date: 2019年8月5日 下午7:53:11  
 */
public class DataValidTest
{


    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkEmptyR(java.lang.String)}.
     */
    @Test
    public void testCheckEmptyR()
    {
        Assert.assertFalse(DataValid.checkEmptyR(null));
        Assert.assertFalse(DataValid.checkEmptyR(""));
    }


    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkNumDataR(java.lang.String)}.
     */
    @Test
    public void testCheckNumDataR()
    {
        Assert.assertTrue(DataValid.checkNumDataR("0123"));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkNetAddress(java.lang.String)}.
     */
    @Test
    public void testCheckNetAddress()
    {
        String ip="255.250.10.1";
        Assert.assertTrue(DataValid.checkNetAddress(ip));
        String ip1="127.0.1";
        Assert.assertFalse(DataValid.checkNetAddress(ip1));
        String ipPort="10.80.5.123:8900";
        Assert.assertTrue(DataValid.checkNetAddress(ipPort));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkIp(java.lang.String)}.
     */
    @Test
    public void testCheckIp()
    {
        String ip="0.250.10.1";
        Assert.assertFalse(DataValid.checkIp(ip));
        String ip1="127.0.1";
        Assert.assertFalse(DataValid.checkIp(ip1));
    }


    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkDiskSymbol(java.lang.String)}.
     * @throws DataAsException 
     */
    @Test
    public void testCheckDiskSymbol() throws DataAsException
    {
        String driveD="d:";
        Assert.assertTrue(DataValid.checkDiskSymbol(driveD));
        String driveDH="dh:";
        Assert.assertFalse(DataValid.checkDiskSymbol(driveDH));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DataValid#checkFileNameChar(java.lang.String)}.
     * @throws DataAsException 
     */
    @Test
    public void testCheckFileNameChar() throws DataAsException
    {
        String fileName="<file";
        Assert.assertFalse(DataValid.checkFileNameChar(fileName));
    }

}
