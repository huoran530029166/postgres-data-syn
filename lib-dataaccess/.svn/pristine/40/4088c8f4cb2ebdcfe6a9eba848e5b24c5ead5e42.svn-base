package com.goldwind.dataaccess;

/**
 * 服务运行状态，是否运行
 * 
 * @author wangruibo 2019年12月8日
 */
public class ServiceState// NOSONAR
{
    /**
     * 程序是否在运行
     */
    private static volatile boolean running = true;

    /**
     * 获取服务运行状态
     * 
     * @return
     */
    public static boolean getServiceState()
    {
        return running;
    }

    /**
     * 设置服务运行状态
     * 
     * @param state
     *            程序状态
     */
    public static void setServiceState(boolean state)
    {
        if (!state)
        {
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        ServiceState.running = state;
    }

}
