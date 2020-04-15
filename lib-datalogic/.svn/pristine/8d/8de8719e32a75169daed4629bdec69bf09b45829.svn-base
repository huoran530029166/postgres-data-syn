/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: SendUpEventUtilTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: SendUpEventUtilTest
 * @author: Administrator   
 * @date: 2019年8月24日 下午1:56:46 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName: SendUpEventUtilTest
 * @Description: SendUpEventUtilTest
 * @author: Administrator
 * @date: 2019年8月24日 下午1:56:46
 */
public class SendUpEventUtilTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfo(java.lang.String)}.
     */
    @Test
    public void testParseSendUpInfoString()
    {
        String netData = "(protectdata|4|statid|devid|cpuid|regionid|groupid|itemid|fun|inf|time|data|identityid(uuid)|ordernum|iscache|protocolid)";
        Map<String, Object> map = SendUpEventUtil.parseSendUpInfo(netData);
        Assert.assertThat(map, Matchers.hasEntry("identityId", "identityid(uuid)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfo(java.lang.String[])}.
     */
    @Test
    public void testParseSendUpInfoStringArray()
    {
        String netData = "protectdata|4|statid|devid|cpuid|regionid|groupid|itemid|fun|inf|time|data|identityid(uuid)|ordernum|iscache|";
        String[] array = netData.split("\\|");
        Map<String, Object> map = SendUpEventUtil.parseSendUpInfo(array);
        Assert.assertNull(map);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfo(java.lang.String[])}.
     */
    @Test(expected=IllegalStateException.class)
    public void testParseSendUpInfoStringArrayForException()
    {
        String netData = "protectdata|3|statid|devid|cpuid|regionid|groupid|itemid|fun|inf|time|data|identityid(uuid)|ordernum|iscache|protocolid";
        String[] array = netData.split("\\|");
        SendUpEventUtil.parseSendUpInfo(array);
    }
    
//    /**
//     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfoForJava(java.lang.String)}.
//     */
//    @Test
//    public void testParseSendUpInfoForJavaString()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfoForJava(java.lang.String[])}.
//     */
//    @Test
//    public void testParseSendUpInfoForJavaStringArray()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.utils.SendUpEventUtil#parseSendUpInfo(com.goldwind.datalogic.utils.ProtectSendUpEvent)}.
//     */
//    @Test
//    public void testParseSendUpInfoProtectSendUpEvent()
//    {
//        fail("Not yet implemented");
//    }

}
