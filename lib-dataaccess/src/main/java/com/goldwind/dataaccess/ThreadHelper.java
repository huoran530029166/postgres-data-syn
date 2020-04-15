package com.goldwind.dataaccess;


public class ThreadHelper
{
    private static Log logger = Log.getLog(ThreadHelper.class);
    
    private ThreadHelper()
    {
      
    }
    
    /**
     * 公共休眠函数
     * @param millionSenconds 毫秒
     */
    public static void sleep(long millionSenconds)
    {
        try
        {
            Thread.sleep(millionSenconds);
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }
}
