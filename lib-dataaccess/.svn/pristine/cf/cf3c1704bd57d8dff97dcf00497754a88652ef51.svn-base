/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: StringBufferUtilTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年8月7日 下午8:21:48 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/** 
 * @ClassName: StringBufferUtilTest 
 * @Description: StringBufferUtilTest
 * @author: Administrator
 * @date: 2019年8月7日 下午8:21:48  
 */
public class StringBufferUtilTest   
{

    /**
     * Test method for {@link com.goldwind.dataaccess.StringBufferUtil#sbReplaceAll(java.lang.StringBuffer, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSbReplaceAll()
    {
        StringBuffer sb=new StringBuffer("shdjdssjkfsf");
        StringBufferUtil.sbReplaceAll(sb, "s", "==");
        String expStr="==hdjd====jkf==f";
        Assert.assertThat(sb.toString(), Matchers.equalTo(expStr));
    }

}
