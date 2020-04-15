/// **
// * Copyright © 2019 金风科技. All rights reserved.
// *
// * @Title: NetCommFuncTest.java
// * @Prject: SOAM-lib-datalogic
// * @Package: com.goldwind.datalogic.utils
// * @Description: TODO
// * @author: Administrator
// * @date: 2019年8月24日 下午12:46:02
// * @version: V1.0
// */
// package com.goldwind.datalogic.utils;
//
// import static org.junit.Assert.*;
//
// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.PrintWriter;
// import java.net.InetSocketAddress;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.net.UnknownHostException;
//
// import org.junit.AfterClass;
// import org.junit.BeforeClass;
// import org.junit.Test;
//
// import com.goldwind.dataaccess.exception.DataAsException;
//
// import junit.framework.Assert;
//
/// **
// * @ClassName: NetCommFuncTest
// * @Description: NetCommFuncTest
// * @author: Administrator
// * @date: 2019年8月24日 下午12:46:02
// */
// public class NetCommFuncTest
// {
//// //提出公共的
//// public static ServerSocket server = null;
////
//// @BeforeClass
//// public void socketService()
//// {
//// try
//// {
//// try
//// {
//// server = new ServerSocket(5209);
//// // b)指定绑定的端口，并监听此端口。
//// System.out.println("服务器启动成功");
//// // 创建一个ServerSocket在端口5209监听客户请求
//// }
//// catch (Exception e)
//// {
//// System.out.println("没有启动监听：" + e);
//// // 出错，打印出错信息
//// }
//// Socket socket = null;
//// try
//// {
//// socket = server.accept();
//// // 2、调用accept()方法开始监听，等待客户端的连接
//// // 使用accept()阻塞等待客户请求，有客户
//// // 请求到来则产生一个Socket对象，并继续执行
//// }
//// catch (Exception e)
//// {
//// System.out.println("Error." + e);
//// // 出错，打印出错信息
//// }
//// // 3、获取输入流，并读取客户端信息
//// String line;
//// BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//// // 由Socket对象得到输入流，并构造相应的BufferedReader对象
//// PrintWriter writer = new PrintWriter(socket.getOutputStream());
//// // 由Socket对象得到输出流，并构造PrintWriter对象
//// BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//// // 由系统标准输入设备构造BufferedReader对象
//// System.out.println("Client:" + in.readLine());
//// // 在标准输出上打印从客户端读入的字符串
//// line = br.readLine();
//// // 从标准输入读入一字符串
//// // 4、获取输出流，响应客户端的请求
//// while (!line.equals("end"))
//// {
//// // 如果该字符串为 "bye"，则停止循环
//// writer.println(line);
//// // 向客户端输出该字符串
//// writer.flush();
//// // 刷新输出流，使Client马上收到该字符串
//// System.out.println("Server:" + line);
//// // 在系统标准输出上打印读入的字符串
//// System.out.println("Client:" + in.readLine());
//// // 从Client读入一字符串，并打印到标准输出上
//// line = br.readLine();
//// // 从系统标准输入读入一字符串
//// } // 继续循环
////
//// // 5、关闭资源
//// // 关闭Socket输出流
//// writer.close();
//// // 关闭Socket输入流
//// in.close();
//// // 关闭Socket
//// socket.close();
////
//// }
//// catch (Exception e)
//// {
//// // 出错，打印出错信息
//// System.out.println("Error." + e);
//// }
////
//// }
////
//// @AfterClass
//// public void socketServiceClose() throws IOException{
//// // 关闭ServerSocket
//// server.close();
//// }
//
// /**
// * Test method for {@link com.goldwind.datalogic.utils.NetCommFunc#checkUserData(java.lang.String[])}.
// *
// * @throws DataAsException
// * 异常
// */
// @Test(expected = Exception.class)
// public void testCheckUserData() throws DataAsException
// {
// String[] dataArr = { "123", "~123" };
// NetCommFunc.checkUserData(dataArr);
// }
//
//// /**
//// * Test method for {@link com.goldwind.datalogic.utils.NetCommFunc#sendData(java.lang.String, java.lang.String, int, java.lang.String, long)}.
//// */
//// @Test
//// public void testSendDataStringStringIntStringLong()
//// {
//// }
////
//// /**
//// * Test method for {@link com.goldwind.datalogic.utils.NetCommFunc#sendData(java.lang.String, java.lang.String, int, java.lang.String, boolean, long)}.
//// */
//// @Test
//// public void testSendDataStringStringIntStringBooleanLong()
//// {
//// }
//
// /**
// * Test method for {@link com.goldwind.datalogic.utils.NetCommFunc#checkTcpClient(java.net.Socket)}.
// * @throws IOException
// * @throws UnknownHostException
// */
// @Test
// public void testCheckTcpClient() throws UnknownHostException, IOException
// {
// Socket tcpClient = new Socket();
// boolean act = NetCommFunc.checkTcpClient(tcpClient);
// Assert.assertFalse(act);
// }
//
//// /**
//// * Test method for {@link com.goldwind.datalogic.utils.NetCommFunc#closeTcpClient(java.net.Socket)}.
//// */
//// @Test
//// public void testCloseTcpClient()
//// {
//// }
//
// }
