/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DynamicRunTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @author: Administrator   
 * @date: 2019年8月7日 下午5:04:16 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName: DynamicRunTest       
 * @Description: DynamicRunTest
 * @author Administrator
 * @date: 2019年8月7日 下午5:04:16
 */
public class DynamicRunTest 
{

    /**
     * Test method for {@link com.goldwind.dataaccess.DynamicRun#dynamicExpression(java.lang.String)}.
     */
    @Test
    public void testDynamicExpression()
    {
        String expStr = "(5+2)*3/9";
        double exp = 2.333d;
        double act = DynamicRun.dynamicExpression(expStr);
        Assert.assertEquals(exp, act, 0.01d);

        String expStr1 = "(5+2)*3%5";
        double exp1 = 1.0d;
        double act1 = DynamicRun.dynamicExpression(expStr1);
        Assert.assertEquals(exp1, act1, 0.01d);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DynamicRun#logicExpression(java.lang.String)}.
     */
    @Test
    public void testLogicExpression()
    {
        String expStr = "1 >0 |1>0&1>0";
        Assert.assertTrue(DynamicRun.logicExpression(expStr));

        String expStr1 = "2>0|3<0 & 5<0";
        Assert.assertFalse(DynamicRun.logicExpression(expStr1));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DynamicRun#otherCompareExpression(java.lang.String)}.
     */
    @Test
    public void testOtherCompareExpression()
    {
        String expStr = "5∈[1,2,5] & 1≠2";
        Assert.assertTrue(DynamicRun.otherCompareExpression(expStr));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DynamicRun#isDoubleEqual(double, double)}.
     */
    @Test
    public void testIsDoubleEqual()
    {
        double exp = 3.01d;
        double act = 3.01d;
        Assert.assertTrue(DynamicRun.isDoubleEqual(exp, act));
    }

    @Test
    public void testIsDoubleCompare()
    {
        double exp = 3.01d;
        double act = 3.015d;
        Assert.assertTrue(DynamicRun.isDoubleCompare(exp, act, -1) < 0);

        double exp1 = 3.01d;
        double act1 = 3.014d;
        Assert.assertTrue(DynamicRun.isDoubleCompare(exp1, act1, 2) == 0);
    }
}
