/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: NetAddressTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: NetAddressTest
 * @author: Administrator   
 * @date: 2019年8月22日 下午5:51:34 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.net.InetAddress;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

/** 
 * @ClassName: NetAddressTest 
 * @Description: NetAddressTest
 * @author: Administrator
 * @date: 2019年8月22日 下午5:51:35  
 */
public class NetAddressTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#checkUnique(com.goldwind.datalogic.utils.NetAddress[])}.
     * @throws DataAsException 异常
     */
    @Test
    public void testCheckUnique() throws DataAsException
    {
        NetAddress netAddress=new NetAddress("127.0.0.1", 8804);
        NetAddress netAddress1=new NetAddress("127.0.0.4", 8804);
        NetAddress netAddress2=new NetAddress("127.0.0.1", 8802);
        NetAddress[] netAddressList={netAddress,netAddress1,netAddress2};
        boolean b=NetAddress.checkUnique(netAddressList);
        Assert.assertTrue(b);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#checkUnique(com.goldwind.datalogic.utils.NetAddress[])}.
     * @throws DataAsException 异常
     */
    @Test
    public void testCheckUnique1() throws DataAsException
    {
        NetAddress netAddress=new NetAddress("127.0.0.1", 8804);
        NetAddress netAddress1=new NetAddress("127.0.0.4", 8804);
        NetAddress netAddress2=new NetAddress("127.0.0.1", 8802);
        NetAddress[] netAddressList={netAddress,netAddress1,netAddress2,netAddress};
        boolean b=NetAddress.checkUnique(netAddressList);
        Assert.assertFalse(b);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#parseAddress(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseAddress() throws DataAsException
    {
        NetAddress[] val = new NetAddress[] {};
        String address="";
        String port="";
        NetAddress[] act=NetAddress.parseAddress(address, port);
        Assert.assertArrayEquals(val, act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#parseAddress(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test(expected = DataAsException.class)
    public void testParseAddress1() throws DataAsException
    {
        String address="127.0.0.1,127.0.0.3,127.0.0.5";
        String port="8804,8805";
        NetAddress.parseAddress(address, port);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#parseAddress(java.lang.String, java.lang.String)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testParseAddress2() throws DataAsException
    {
        NetAddress netAddress=new NetAddress("127.0.0.1", 8804);
        NetAddress netAddress1=new NetAddress("127.0.0.3", 8805);
        NetAddress netAddress2=new NetAddress("127.0.0.5", 8806);
        NetAddress[] exp = new NetAddress[] {netAddress,netAddress1,netAddress2};
        String address="127.0.0.1,127.0.0.3,127.0.0.5";
        String port="8804,8805,8806";
        NetAddress[] act=NetAddress.parseAddress(address, port);
        Assert.assertThat(act[1].toString(), Matchers.equalTo(exp[1].toString()));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#checkPort(int)}.
     * @throws DataAsException 异常
     */
    @Test(expected = DataAsException.class)
    public void testCheckPort() throws DataAsException
    {
        NetAddress.checkPort(70000);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#checkPort(int)}.
     * @throws DataAsException 异常
     */
    @Test
    public void testCheckPort1() throws DataAsException
    {
        NetAddress.checkPort(3000);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#genKey(java.lang.String, int)}.
     */
    @Test
    public void testGenKey()
    {
        String act=NetAddress.genKey("127.0.0.1", 8804);
        String exp="127.0.0.1:8804";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#addressToIp()}.
     * @throws Exception 异常
     */
    @Test
    public void testAddressToIp() throws Exception
    {
        InetAddress inetAddress=InetAddress.getByAddress(new byte[]{127,0,0,1});;
        NetAddress netAddress=new NetAddress(inetAddress, 8804);
        boolean act=netAddress.addressToIp();
        Assert.assertFalse(act);;
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#toString()}.
     * @throws Exception 异常
     */
    @Test
    public void testToString() throws Exception
    {
        InetAddress inetAddress=InetAddress.getByAddress(new byte[]{127,0,0,1});;
        NetAddress netAddress=new NetAddress(inetAddress, 8804);
        String act=netAddress.toString();
        String exp="/127.0.0.1:8804";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.NetAddress#toDirectory()}.
     * @throws Exception 异常
     */
    @Test
    public void testToDirectory() throws Exception
    {
        InetAddress inetAddress=InetAddress.getByAddress(new byte[]{127,0,0,1});;
        NetAddress netAddress=new NetAddress(inetAddress, 8804);
        String act=netAddress.toDirectory();
        String exp="/127.0.0.1_8804";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
}
