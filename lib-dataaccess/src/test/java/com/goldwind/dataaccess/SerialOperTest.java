///**   
// * Copyright © 2019 金风科技. All rights reserved.
// * 
// * @Title: SerialOperTest.java 
// * @Prject: SOAM-lib-dataaccess
// * @Package: com.goldwind.dataaccess 
// * @Description: TODO
// * @author: Administrator   
// * @date: 2019年8月7日 下午8:10:24 
// * @version: V1.0   
// */
//package com.goldwind.dataaccess;
//
//import static org.junit.Assert.*;
//
//import java.io.EOFException;
//import java.io.IOException;
//
//import org.hamcrest.Matchers;
//import org.hamcrest.core.AnyOf;
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.goldwind.dataaccess.exception.DataAsException;
//
///**
// * @ClassName: SerialOperTest
// * @Description: SerialOperTest
// * @author: Administrator
// * @date: 2019年8月7日 下午8:10:24
// */
//public class SerialOperTest
//{
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.SerialOper#writeObject(java.lang.Object, java.lang.String)}.
//     * 
//     * @throws Exception
//     */
//    @Test
//    public void testWriteObject() throws Exception
//    {
//        SerialOper.writeObject("StringTest", "./testFile.txt");
//        Assert.assertThat(new String(), Matchers.anything());
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.SerialOper#readObject(java.lang.String)}.
//     * 
//     * @throws IOException
//     * @throws DataAsException
//     * @throws EOFException
//     */
//    @Test
//    public void testReadObject() throws EOFException, DataAsException, IOException
//    {
//        String act = (String) SerialOper.readObject("./testFile.txt");
//        Assert.assertThat("StringTest", Matchers.equalTo(act));
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.SerialOper#kryoWriteObj(java.lang.Object, java.lang.String)}.
//     * 
//     * @throws DataAsException
//     */
//    @Test
//    public void testKryoWriteObj() throws DataAsException
//    {
//        SerialOper.kryoWriteObj("testKryo", "./testKryo.txt");
//        Assert.assertThat(new String(), Matchers.anything());
//    }
//
//    /**
//     * Test method for {@link com.goldwind.dataaccess.SerialOper#kryoReadObj(java.lang.String, java.lang.Class)}.
//     * 
//     * @throws DataAsException
//     */
//    @Test
//    public void testKryoReadObj() throws DataAsException
//    {
//        String act = (String) SerialOper.kryoReadObj("./testKryo.txt", String.class);
//        Assert.assertThat("testKryo", Matchers.equalTo(act));
//    }
//
//}
