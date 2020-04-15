/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataAsMemoryTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: DataAsMemory单元测试
 * @author: Administrator   
 * @date: 2019年7月29日 上午10:07:43 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @ClassName: DataAsMemoryTest
 * @Description: DataAsMemory单元测试
 * @author Administrator
 * @date: 2019年7月29日 上午10:07:43
 */
public class DataAsMemoryTest
{
    /**
     * @return
     * @Title: testInit
     * @Description: 初始化
     * @throws Exception
     *             异常
     * @return: void 无返回
     */
    @BeforeClass
    public static void testInit() throws Exception
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        DataAsMemory dataMemory = new DataAsMemory();
        dataMemory.syncDataExpMemoryOut(rootPath, DataAsDef.PACKAGE_NAME.DATAACCESS);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DataAsMemory#syncDataExpMemoryOut(java.lang.String, com.goldwind.dataaccess.DataAsDef.PACKAGE_NAME[])}.
     * 
     * @throws Exception
     */
    @Test
    public void testSyncDataExpMemoryOut()
    {
        Locale locale = new Locale("zh", "CN");
        Map<String, String> map = DataAsMemory.getDataExpMemory().get(locale.getLanguage() + "_" + locale.getCountry());
        String act = map.get("DbAssistant_tableToDelSql_1");
        String exp = "将数据表转换为删除sql语句失败.";
        Assert.assertArrayEquals(exp.getBytes(), act.getBytes());
    }
}
