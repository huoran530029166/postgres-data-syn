package com.goldwind.datalogic.utils;

import com.goldwind.dataaccess.queue.DataQueue;

/**
 * 共享队列，目的是为了将基础包中产生的告警上送至数据处理
 * 
 * @author Wangdashu
 *
 */
public class UtilQueue
{
    /**
     * 公共队列
     */
    private static DataQueue UtilQueue = new DataQueue();

    public static DataQueue getUtilQueue()
    {
        return UtilQueue;
    }

    public static void setUtilQueue(DataQueue utilQueue)
    {
        UtilQueue = utilQueue;
    }
}
