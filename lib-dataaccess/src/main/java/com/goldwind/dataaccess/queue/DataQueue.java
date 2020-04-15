package com.goldwind.dataaccess.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.goldwind.dataaccess.Log;

/**
 * 数据队列
 * 
 * @author 张超
 *
 */
public class DataQueue
{

    /**
     * 数据队列
     */
    private BlockingQueue<Object> dataQueues;

    public DataQueue()
    {
        dataQueues = new LinkedBlockingQueue<>();
    }

    /**
     * 得到数据数量
     * 
     * @return 数据数量
     */
    public int getDataSize()
    {
        return dataQueues.size();
    }

    /**
     * 添加数据,队列已满则等待
     * 
     * @param data
     *            数据
     * @throws InterruptedException
     *             zhongduan
     */
    public void putData(Object data) throws InterruptedException
    {
        dataQueues.put(data);
    }

    /**
     * 添加数据,队列已满返回false
     * 
     * @param data
     *            数据
     * @return 是否添加成功
     */
    public boolean offerData(Object data)
    {
        return dataQueues.offer(data);
    }

    /**
     * 从队列中取一个数据，如果没有则等待
     * 
     * @return 数据
     * @throws InterruptedException
     *             异常信息
     */
    public Object takeData() throws InterruptedException
    {
        return dataQueues.take();
    }

    /**
     * 从队列中取一个数据，如果没有返回空
     * 
     * @return 数据
     */
    public Object pollData()
    {
        return dataQueues.poll();
    }

    /**
     * 从队列中取一个数据，如果没有则等待一段时间
     * 
     * @param timeout
     *            等待时间
     * @return 数据
     * @throws InterruptedException
     *             异常信息
     */
    public Object pollData(long timeout) throws InterruptedException
    {
        return dataQueues.poll(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 从队列中移除一个数据
     * 
     * @param data
     *            数据
     */
    public boolean removeData(String data)
    {
        return dataQueues.remove(data);
    }

    /**
     * 清除队列中的数据
     * 
     */
    public void clearData()
    {
        dataQueues.clear();
    }
}
