/// **
// * Copyright © 2019 金风科技. All rights reserved.
// *
// * @Title: EncryptClassTest.java
// * @Prject: SOAM-lib-dataaccess
// * @Package: com.goldwind.dataaccess.security
// * @Description: EncryptClassTest
// * @author: Administrator
// * @date: 2019年8月27日 下午9:39:11
// * @version: V1.0
// */
// package com.goldwind.dataaccess.security;
//
// import java.io.UnsupportedEncodingException;
// import java.security.NoSuchAlgorithmException;
//
// import org.hamcrest.Matchers;
// import org.junit.Assert;
// import org.junit.Test;
//
/// **
// * @ClassName: EncryptClassTest
// * @Description: EncryptClassTest
// * @author: Administrator
// * @date: 2019年8月27日 下午9:39:11
// */
// public class EncryptClassTest
// {
//
// /**
// * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#encryptMD5(java.lang.String)}.
// *
// * @throws NoSuchAlgorithmException
// * 异常
// * @throws UnsupportedEncodingException
// * 异常
// */
// @Test
// public void testEncryptMD5() throws UnsupportedEncodingException, NoSuchAlgorithmException
// {
// String str = "123456";
// String act = EncryptClass.encryptMD5(str);
// String exp = "e10adc3949ba59abbe56e057f20f883e";
// Assert.assertThat(act, Matchers.equalTo(exp));
// }
//
// /**
// * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#encryptMD5(java.lang.String)}.
// *
// * @throws NoSuchAlgorithmException
// * 异常
// * @throws UnsupportedEncodingException
// * 异常
// */
// @Test(expected = NullPointerException.class)
// public void testEncryptMD5Null() throws UnsupportedEncodingException, NoSuchAlgorithmException
// {
// String str = null;
// EncryptClass.encryptMD5(str);
// }
//
// /**
// * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#encryptMD5(java.lang.String)}.
// *
// * @throws NoSuchAlgorithmException
// * 异常
// * @throws UnsupportedEncodingException
// * 异常
// */
// @Test
// public void testEncryptMD5Long() throws UnsupportedEncodingException, NoSuchAlgorithmException
// {
// String str = "12435356467474757757575755858868696997070707=7r=#^";
// String act = EncryptClass.encryptMD5(str);
// String exp = "d1e7b8d40fdc2d2738fd74123a0031eb";
// Assert.assertThat(act, Matchers.equalTo(exp));
// }
//
// // /**
// // * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#reEncrypt(java.lang.String)}.
// // * @throws Exception 异常
// // */
// // @Test
// // public void testReEncryptString() throws Exception
// // {
// // String str="123456";
// // String act=EncryptClass.reEncrypt(str);
// // String exp="Mtjwc5lfWcs=";
// // Assert.assertThat(act, Matchers.equalTo(exp));
// // }
//
// // /**
// // * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#reEncrypt(java.lang.String, java.lang.String)}.
// // * @throws Exception 异常
// // */
// // @Test(expected=NullPointerException.class)
// // public void testReEncryptStringStringNull() throws Exception
// // {
// // String data="123456";
// // String skey=null;
// // EncryptClass.reEncrypt(data, skey);
// // }
//
// /**
// * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#reEncrypt(java.lang.String, java.lang.String)}.
// *
// * @throws Exception
// * 异常
// */
// @Test(expected = Exception.class)
// public void testReEncryptStringStringLong() throws Exception
// {
// String data = "123456";
// String skey = "123456789";
// EncryptClass.reEncrypt(data, skey);
// }
//
// // /**
// // * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#reEncrypt(java.lang.String, java.lang.String)}.
// // * @throws Exception 异常
// // */
// // @Test
// // public void testReEncryptStringString() throws Exception
// // {
// // String data="123456";
// // String skey="123456";
// // String act=EncryptClass.reEncrypt(data, skey);
// // String exp="ahiomtTZStY=";
// // Assert.assertThat(act, Matchers.equalTo(exp));
// // }
// // /**
// // * Test method for {@link com.goldwind.dataaccess.security.EncryptClass#encryptByDate(java.lang.String[], int)}.
// // * @throws Exception
// // */
// // @Test(expected=NullPointerException.class)
// // public void testEncryptByDateStringArrayIntNull() throws Exception
// // {
// // String[] dataArray=null;
// // int kind=12;
// // EncryptClass.encryptByDate(dataArray, kind);
// // }
// }
