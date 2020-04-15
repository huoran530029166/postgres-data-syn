/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: ReturnParseTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: ReturnParseTest
 * @author: Administrator   
 * @date: 2019年8月24日 下午1:11:37 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.util.HashMap;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.NetCommDef.CommState;

/** 
 * @ClassName: ReturnParseTest 
 * @Description: ReturnParseTest
 * @author: Administrator
 * @date: 2019年8月24日 下午1:11:37  
 */
public class ReturnParseTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSuccessFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSuccessFlg()
    {
        boolean act=ReturnParse.checkSuccessFlg("(OK)");
        Assert.assertTrue(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSuccessFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSuccessFlg1()
    {
        boolean act=ReturnParse.checkSuccessFlg("(ok)");
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetDevMainData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testParseGetDevMainData()throws DataAsException
    {
        String netData="(d1,d2,0)";
        ReturnParse r=new ReturnParse();
        HashMap<String, Object> map=r.parseGetDevMainData("32223", netData);
        Assert.assertThat(map, Matchers.hasEntry("commState", CommState.Connect));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetDevPackData(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseGetDevPackData() throws DataAsException
    {
        String netData="(d1,d2,对)";
        String[] exp={"d1","d2","对"};
        ReturnParse r=new ReturnParse();
        String[] act= r.parseGetDevPackData("deviceId", netData);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetDevData(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseGetDevData() throws DataAsException
    {
        String netData="(d1,d2,对)";
        String[] exp={"d1","d2","对"};
        ReturnParse r=new ReturnParse();
        String[] act= r.parseGetDevData("deviceId", netData);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetDevCacheData(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseGetDevCacheData() throws DataAsException
    {
        String netData="(d1,d2,对)";
        String[] exp={"d1","d2","对"};
        ReturnParse r=new ReturnParse();
        String[] act= r.parseGetDevCacheData("deviceId", netData);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetDevFltFName(java.lang.String)}.
     * @throws DataAsException 
     */
    @Test
    public void testParseGetDevFltFName() throws DataAsException
    {
        String netData="(filename1,filename2,对)";
        String[] exp={"filename1","filename2","对"};
        ReturnParse r=new ReturnParse();
        String[] act= r.parseGetDevFltFName( netData);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetPlanOrder(java.lang.String)}.
     */
    @Test
    public void testParseGetPlanOrder()
    {
        String netData="( filename1,filename2,对)";
        String exp="filename1,filename2,对";
        ReturnParse r=new ReturnParse();
        String act= r.parseGetPlanOrder( netData);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetPlanData(java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseGetPlanData() throws DataAsException
    {
        String netData="( filename1 , filename2 ,对)";
        String[] exp={"filename1","filename2","对"};
        ReturnParse r=new ReturnParse();
        String[] act= r.parseGetPlanData( netData);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#parseGetPreIni(java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseGetPreIni() throws DataAsException
    {
        String netData="(filename1,filename2,对|filename2,filename2,对|filename3,filename2,对)";
        String[] exp={"filename1","filename2","filename3"};
        ReturnParse r=new ReturnParse();
        HashMap<String, Object> map= r.parseGetPreIni( netData);
        Assert.assertThat(map, Matchers.hasEntry("sections", exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#removeErrorFlg(java.lang.String)}.
     */
    @Test
    public void testRemoveErrorFlg()
    {
        String netData="(error:1234)";
        String exp="1234";
        String act= ReturnParse.removeErrorFlg( netData);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#removeErrorFlg(java.lang.String)}.
     */
    @Test
    public void testRemoveErrorFlg1()
    {
        String netData="(error1234)";
        String exp="error1234";
        String act= ReturnParse.removeErrorFlg( netData);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckErrorFlg()
    {
        String netData="(ok)";
        boolean act=ReturnParse.checkErrorFlg(netData);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckErrorFlg1()
    {
        String netData="(error:123)";
        boolean act=ReturnParse.checkErrorFlg(netData);
        Assert.assertTrue(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckErrorFlg2()
    {
        String netData="((error123))";
        boolean act=ReturnParse.checkErrorFlg(netData);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSubgetErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSubgetErrorFlg()
    {
        String netData="123";
        boolean act=ReturnParse.checkSubgetErrorFlg(netData);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSubgetErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSubgetErrorFlg1()
    {
        String netData="(error";
        boolean act=ReturnParse.checkSubgetErrorFlg(netData);
        Assert.assertFalse(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSubgetErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSubgetErrorFlg2()
    {
        String netData="(error)";
        boolean act=ReturnParse.checkSubgetErrorFlg(netData);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkSubgetErrorFlg(java.lang.String)}.
     */
    @Test
    public void testCheckSubgetErrorFlg3()
    {
        String netData="";
        boolean act=ReturnParse.checkSubgetErrorFlg(netData);
        Assert.assertTrue(act);
    }
    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkFinishFlg(java.lang.String)}.
     */
    @Test
    public void testCheckFinishFlg()
    {
        String netData="(finish)";
        boolean act=ReturnParse.checkFinishFlg(netData);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.ReturnParse#checkWaitFlg(java.lang.String)}.
     */
    @Test
    public void testCheckWaitFlg()
    {
        String netData= "(wait)";
        boolean act=ReturnParse.checkWaitFlg(netData);
        Assert.assertTrue(act);
    }

}
