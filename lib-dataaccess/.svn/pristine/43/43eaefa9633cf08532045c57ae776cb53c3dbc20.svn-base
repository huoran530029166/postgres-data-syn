/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: ArrayOperTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年7月17日 下午5:58:43 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * @ClassName: ArrayOperTest
 * @Description: ArrayOperTest
 * @author Administrator
 * @date: 2019年7月17日 下午5:58:43
 */
public class ArrayOperTest
{
    // 初始化列表
    public static ArrayList<Object> al = new ArrayList<Object>();

    /**
     * @Title: beaforeClass
     * @Description: 初始化函数
     * @return: void
     */
    @BeforeClass
    public static void beaforeClass()
    {
        al.add("a");
        al.add(2);
    }

    /**
     * @Title: testArrayListToArray
     * @Description: 测试tArrayListToArray
     * @return: void
     */
    @Test
    public void testArrayListToArray()
    {

        String[] alStr = ArrayOper.arrayListToArray(al);
        // 期望数组
        String[] expect = new String[2];
        expect[0] = "a";
        expect[1] = "2";
        Assert.assertArrayEquals(expect, alStr);
    }

    /**
     * @Title: testArrayListToString
     * @Description: 测试ArrayListToString
     * @return: void
     */
    @Test
    public void testArrayListToString()
    {
        String rs = ArrayOper.arrayListToString(al, ",");
        Assert.assertTrue(rs.equals("a,2"));
    }

    /**
     * @Title: testArrayToString
     * @Description: 测试ArrayToString
     * @return: void
     */
    @Test
    public void testArrayToString()
    {
        String[] alStr = ArrayOper.arrayListToArray(al);
        String rs = ArrayOper.arrayToString(alStr, ",");
        Assert.assertTrue(rs.equals("a,2"));
    }

    /**
     * @Title: testNewAarrayToString
     * @Description: 测试AarrayToString
     * @return: void
     */
    @Test
    public void testNewAarrayToString()
    {
        Object[] srcArray = { "a", 2 };
        String rs = ArrayOper.newAarrayToString(srcArray, ",");
        Assert.assertTrue(rs.equals("a,2"));
    }

    /**
     * @Title: testArrayToStrWithSymbol
     * @Description: 测试ArrayToStrWithSymbol
     * @return: void
     */
    @Test
    public void testArrayToStrWithSymbol()
    {
        Object[] srcArray = { "a", 2 };
        String rs = ArrayOper.arrayToStrWithSymbol(srcArray, ",");
        Assert.assertTrue(rs.equals("'a','2'"));
    }

    /**
     * @Title: testNewArrayToStrWithSymbol
     * @Description: 测试ArrayToStrWithSymbol
     * @return: void
     */
    @Test
    public void testNewArrayToStrWithSymbol()
    {
        String[] srcArray = { "a", "2" };
        String rs = ArrayOper.newArrayToStrWithSymbol(srcArray, ",");
        Assert.assertTrue(rs.equals("'a','2'"));
    }

    /**
     * @Title: testClearEmptyItem
     * @Description: 测试ClearEmptyItem
     * @return: void
     */
    @Test
    public void testClearEmptyItem()
    {
        String[] srcArray = { "a", "", "2", "", "3" };
        String[] expect = { "a", "2", "3" };
        String[] rs = ArrayOper.clearEmptyItem(srcArray);
        Assert.assertArrayEquals(expect, rs);
    }

    /**
     * @Title: testFindDataInArray
     * @Description: 测试FindDataInArray
     * @return: void
     */
    @Test
    public void testFindDataInArray()
    {
        int data = 2;
        int[] datas = { 1, 2, 3 };
        int i = ArrayOper.findDataInArray(data, datas);
        Assert.assertEquals(1, i);
    }

    /**
     * @Title: testFindDataInArray1
     * @Description: 测试FindDataInArray
     * @return: void
     */
    @Test
    public void testFindDataInArray1()
    {
        int data = 2;
        int[] datas = { 1, 2, 3, 2 };
        int i = ArrayOper.findDataInArray(data, datas);
        Assert.assertEquals(1, i);
    }

    /**
     * @Title: testFindDataInArray2
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testFindDataInArray2()
    {
        int data = 2;
        int[] datas = { 1, 4, 3, 5 };
        int i = ArrayOper.findDataInArray(data, datas);
        Assert.assertEquals(-1, i);
    }

    /**
     * 
     * @Title: testFindDataInArray3
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testFindDataInArray3()
    {
        String data = "1";
        String[] datas = { "2", "1", "3" };
        int i = ArrayOper.findDataInArray(data, datas);
        Assert.assertEquals(1, i);
    }

    /**
     * @Title: testFindDataInArray4
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testFindDataInArray4()
    {
        String data = "a";
        String[] datas = { "A", "b", "c" };
        int i = ArrayOper.findDataInArray(data, datas, false);
        Assert.assertEquals(-1, i);
        i = ArrayOper.findDataInArray(data, datas, true);
        Assert.assertEquals(0, i);
    }

    /**
     * @Title: testInsItemInArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testInsItemInArray()
    {
        String data = "a";
        String[] datas = { "A", "b", "c" };
        String[] expect = { "A", "a", "b", "c" };
        String[] act = ArrayOper.insItemInArray(datas, data, 1);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testIntArrayToStrArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testIntArrayToStrArray()
    {
        int[] srcArray = { 1, 2, 3 };
        String[] expect = { "1", "2", "3" };
        String[] act = ArrayOper.intArrayToStrArray(srcArray);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testLowerTrimArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testLowerTrimArray()
    {
        String[] array = { "A ", "b ", "C " };
        String[] expect = { "a", "b", "c" };
        String[] act = ArrayOper.lowerTrimArray(array);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testRemoveItemInArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testRemoveItemInArray()
    {
        String[] srcArray = { "A ", "b ", "C " };
        String[] expect = { "A ", "b " };
        String[] act = ArrayOper.removeItemInArray(srcArray, 2);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testStrArrayToIntArray
     * @Description: 测试
     * @throws DataAsException
     * @return: void
     */
    @Test
    public void testStrArrayToIntArray() throws DataAsException
    {
        String[] srcArray = { "1", "2", "3" };
        int[] expect = { 1, 2, 3 };
        int[] act = ArrayOper.strArrayToIntArray(srcArray);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testStrArrayToDouble
     * @Description: 测试
     * @throws DataAsException
     * @return: void
     */
    @Test
    public void testStrArrayToDouble() throws DataAsException
    {
        String[] srcArray = { "1.2", "2.3", "3.5" };
        double[] expect = { 1.2d, 2.3d, 3.5d };
        double[] act = ArrayOper.strArrayToDouble(srcArray);
        Assert.assertArrayEquals(expect, act, 0);
    }

    /**
     * @Title: testTrimArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testTrimArray()
    {
        String[] srcArray = { "A ", "b ", "C " };
        String[] expect = { "A", "b", "C" };
        String[] act = ArrayOper.trimArray(srcArray);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testUnionArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testUnionArray()
    {
        String[] srcArray = { "a", "f", "d" };
        String[] srcArray2 = { "c", "e", "b", "a" };
        String[] expect = { "a", "f", "d", "c", "e", "b", "a" };
        String[] act = ArrayOper.unionArray(srcArray, srcArray2, true, false);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testCutArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testCutArray()
    {
        String[] srcArray = { "a", "f", "d" };
        String[] act = ArrayOper.cutArray(srcArray, 3, 4);
        String[] expect = {};
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testCutArray1
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testCutArray1()
    {
        String[] srcArray = { "a", "f", "d" };
        String[] act = ArrayOper.cutArray(srcArray, 1, 2);
        String[] expect = { "f" };
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testAvgArray
     * @Description: 测试
     * @throws DataAsException
     * @return: void
     */
    @Test
    public void testAvgArray() throws DataAsException
    {
        String[] srcArray = { "3", "6", "7" };
        int i = ArrayOper.avgArray(srcArray);
        Assert.assertEquals(5, i);
    }

    /**
     * @Title: testAvgArrayDouble
     * @Description: 测试
     * @throws DataAsException
     * @return: void
     */
    @Test
    public void testAvgArrayDouble() throws DataAsException
    {
        String[] srcArray = { "3", "6", "7" };
        double i = ArrayOper.avgArrayDouble(srcArray);
        Assert.assertEquals(5, i, 1);
    }

    /**
     * @Title: testResizeArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testResizeArray()
    {
        Object[] srcArray = { "3", "6", "7", 8, 10 };
        Object[] expect = { "3", "6", "7", 8 };
        Object[] act = (Object[]) ArrayOper.resizeArray(srcArray, 4);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testFormatDouble2
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testFormatDouble2()
    {
        double d = 2.34534d;
        double act = ArrayOper.formatDouble2(d);
        Assert.assertEquals(2.35, act, 0.01);
    }

    /**
     * FindDataInArray
     * 
     * @Title: testMap2Array
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testMap2Array()
    {
        Map<String, String> hm = new HashMap<String, String>();
        hm.put("1", "a");
        hm.put("2", "a");
        String[] expect = { "1=a", "2=a" };
        String[] act = ArrayOper.map2Array(hm);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testRemoveDataInSArray
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testRemoveDataInSArray()
    {
        String[] srcArray = { "a", "f", "d" };
        String[] expect = { "a", "f" };
        String[] act = ArrayOper.removeDataInSArray(srcArray, 2);
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testArraysReplace
     * @Description: 测试
     * @return: void
     */
    @Test
    public void testArraysReplace()
    {
        String[] srcArray = { "a", "f", "d", "a" };
        String[] expect = { "d", "f", "d", "d" };
        String[] act = ArrayOper.arraysReplace(srcArray, "a", "d");
        Assert.assertArrayEquals(expect, act);
    }

    /**
     * @Title: testUnionArrayNew
     * @Description: 测试UnionArrayNew
     * @return: void
     */
    @Test
    public void testUnionArrayNew()
    {
        String[] srcArray = { "a", "f", "d", "a" };
        String[] destArray = { "c", "d", "a" };
        String[] expect = { "a", "c", "d", "f" };
        String[] act = ArrayOper.unionArrayNew(srcArray, destArray, false, true);
        Assert.assertArrayEquals(expect, act);
    }
}
