/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataQueueTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess.queue 
 * @Description: DataQueueTest
 * @author: Administrator   
 * @date: 2019年8月27日 下午10:14:06 
 * @version: V1.0   
 */
package com.goldwind.dataaccess.queue;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName: DataQueueTest
 * @Description: DataQueueTest
 * @author: Administrator
 * @date: 2019年8月27日 下午10:14:06
 */
public class DataQueueTest
{
    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#getDataSize()}.
     */
    @Test
    public void testGetDataSize()
    {
        DataQueue dataQueue = new DataQueue();
        int act = dataQueue.getDataSize();
        Assert.assertEquals(0, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#putData(java.lang.Object)}.
     * @throws InterruptedException 
     */
    @Test
    public void testPutData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        Assert.assertEquals(1, dataQueue.getDataSize());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#offerData(java.lang.Object)}.
     * @throws InterruptedException 
     */
    @Test
    public void testOfferData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        dataQueue.putData(str);
        dataQueue.offerData(str);
        Assert.assertEquals(3, dataQueue.getDataSize());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#takeData()}.
     * @throws InterruptedException 
     */
    @Test
    public void testTakeData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        str = "234";
        dataQueue.putData(str);
        str = "345";
        dataQueue.offerData(str);
        String act = (String) dataQueue.takeData();
        // 期望值
        String exp = "123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#pollData()}.
     * @throws InterruptedException 
     */
    @Test
    public void testPollData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        str = "234";
        dataQueue.putData(str);
        str = "345";
        dataQueue.offerData(str);
        String act = (String) dataQueue.pollData();
        // 期望值
        String exp = "123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#pollData(long)}.
     * @throws InterruptedException 
     */
    @Test
    public void testPollDataLong() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        str = "234";
        dataQueue.putData(str);
        str = "345";
        dataQueue.offerData(str);
        String act = (String) dataQueue.pollData(123);
        // 期望值
        String exp = "123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#removeData(java.lang.String)}.
     * @throws InterruptedException 
     */
    @Test
    public void testRemoveData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        str = "234";
        dataQueue.putData(str);
        str = "345";
        dataQueue.offerData(str);
        // 移除数据
        dataQueue.removeData("345");
        Assert.assertEquals(2, dataQueue.getDataSize());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.queue.DataQueue#clearData()}.
     * @throws InterruptedException 
     */
    @Test
    public void testClearData() throws InterruptedException
    {
        DataQueue dataQueue = new DataQueue();
        String str = "123";
        dataQueue.putData(str);
        str = "234";
        dataQueue.putData(str);
        str = "345";
        dataQueue.offerData(str);
        dataQueue.clearData();
        Assert.assertEquals(0, dataQueue.getDataSize());
    }

}
