/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: GetLocalIPTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年8月7日 下午6:50:18 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * @ClassName: GetLocalIPTest
 * @Description: GetLocalIPTest
 * @author: Administrator
 * @date: 2019年8月7日 下午6:50:18
 */
public class GetLocalIPTest
{

    /**
     * Test method for {@link com.goldwind.dataaccess.GetLocalIP#getIp()}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testGetIp() throws DataAsException
    {
        String ip=GetLocalIP.getIp();
        Assert.assertThat(ip,Matchers.anything());
    }

}
