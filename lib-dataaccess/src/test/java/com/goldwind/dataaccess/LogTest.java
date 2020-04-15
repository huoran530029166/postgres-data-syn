///**   
// * Copyright © 2019 金风科技. All rights reserved.
// * 
// * @Title: LogTest.java 
// * @Prject: SOAM-lib-dataaccess
// * @Package: com.goldwind.dataaccess 
// * @Description: TODO
// * @author: Administrator   
// * @date: 2019年8月7日 下午6:57:32 
// * @version: V1.0   
// */
//package com.goldwind.dataaccess;
//
//import static org.junit.Assert.*;
//
//import java.util.Map;
//
//import org.junit.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.hamcrest.Matchers.*;  
//
///** 
// * @ClassName: LogTest 
// * @Description: TODO
// * @author: Administrator
// * @date: 2019年8月7日 下午6:57:32  
// */
//public class LogTest
//{
//    private static Log LOGGER = Log.getLog(LogTest.class);
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#initLog(java.lang.String, long)}.
//     */
//    @BeforeClass
//    public void init()
//    {
//        String rootPath = System.getProperty("user.dir").replace("\\", "/");
//        Log.initLog(rootPath + "/config/log4j_windows.properties", 24 * 3600 * 1000);
//    }
//
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#warn(java.lang.String)}.
//     */
//    @Test
//    public void testWarn()
//    {
//        LOGGER.warn("testWarn");
//        Assert.assertThat( Log.getWARNTIMES(),hasKey("testWarn"));  
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#error(java.lang.String)}.
//     */
//    @Test
//    public void testErrorString()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#error(java.lang.Exception)}.
//     */
//    @Test
//    public void testErrorException()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#error(java.lang.String, java.lang.Exception)}.
//     */
//    @Test
//    public void testErrorStringException()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#startLogCleanT()}.
//     */
//    @Test
//    public void testStartLogCleanT()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#isRunning()}.
//     */
//    @Test
//    public void testIsRunning()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#setRunning(boolean)}.
//     */
//    @Test
//    public void testSetRunning()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#main(java.lang.String[])}.
//     */
//    @Test
//    public void testMain()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#getWARNTIMES()}.
//     */
//    @Test
//    public void testGetWARNTIMES()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.Log#getERRORTIMES()}.
//     */
//    @Test
//    public void testGetERRORTIMES()
//    {
//        fail("Not yet implemented");
//    }
//
//}
