/**
 *
 */
package com.goldwind.dataaccess;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.goldwind.dataaccess.file.DownloadThread;

/**
 * @author jincheng 创建时间：2017年4月17日 下午2:38:40
 */
public class Downprocess implements Serializable
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(Downprocess.class);
    /**
     * 
     */
    private static final long serialVersionUID = -6889463035681480566L;
    /**
     * 远程文件
     */
    private String remoteFile;
    /**
     * 本地文件
     */
    private String localFile;
    /**
     * 本地临时文件
     */
    private String tmpFile;
    /**
     * 文件总大小(kb)
     */
    private long cout;

    /**
     * key：文件 value：每个子线程完成的数量
     */
    private long[] process;
    /**
     * 是否下载完成
     */
    private boolean success;
    /**
     * 下载线程数
     */
    private int threadCount = 5;
    /**
     * 下载超时时间
     */
    private int timeOut = 30;
    /**
     * 线程状态列表
     */
    private ConcurrentHashMap<DownloadThread, Boolean> hm;
    /**
     * 缓冲区大小，kb
     */
    private int bufSize = 1024 * 32;
    /**
     * 文件MD5
     */
    private String fileMD5;

    public Downprocess()
    {

    }

    public Downprocess(String rFile, String lFile)
    {
        this.remoteFile = rFile;
        this.localFile = lFile;
        this.success = false;
        this.hm = new ConcurrentHashMap<DownloadThread, Boolean>();
    }

    /**
     * 暂停下载
     */
    public void pauseDownload() throws Exception
    {
        if (hm != null)
        {
            hm.forEachKey(1, dt -> {
                dt.setRunFlag(false);
                dt.getThread().interrupt();
            });
            if (isAllInterrupted())
            {
                HashMap<String, long[]> tmp = new HashMap<>();
                tmp.put(fileMD5, process);
                SerialOper.writeObject(tmp, tmpFile + ".log");
            }
        }
    }

    /**
     * 判断哈希表中的线程是否都已结束
     * 
     * @return 是否
     */
    public boolean isAllInterrupted()
    {
        boolean back = false;
        while (true)
        {
            int count = 0;
            Enumeration<DownloadThread> e = hm.keys();
            while (e.hasMoreElements())
            {
                DownloadThread t = e.nextElement();
                if (!t.isAlive())
                {
                    count++;
                }
            }
            if (count == hm.size())
            {
                back = true;
                break;
            }
            try
            {
                Thread.sleep(1);
            }
            catch (Exception e1)
            {
                LOGGER.error("Downprocess_isAllInterrupted_"+e1.getClass().getName()+"_"+e1.getLocalizedMessage(),e1);
            }
        }
        return back;
    }

    public long getCout()
    {
        return cout;
    }

    public void setCout(long cout)
    {
        this.cout = cout;
    }

    /**
     * 返回已下载字节
     * 
     * @return 已下载字节
     */
    public long getAlready()
    {
        long tt = 0;
        for (int i = 0; i < threadCount; i++)
        {
            tt += process[i];
        }
        return tt;
    }

    public long[] getProcess()
    {
        return process;
    }

    public void setProcess(long[] process)
    {
        this.process = process;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public int getThreadCount()
    {
        return threadCount;
    }

    /**
     * 
     * @param threadCount
     *            线程数
     */
    public void setThreadCount(int threadCount)
    {
        this.threadCount = threadCount;
        if (this.process == null)
        {
            this.process = new long[threadCount];
        }
    }

    public int getTimeOut()
    {
        return timeOut;
    }

    public void setTimeOut(int timeOut)
    {
        this.timeOut = timeOut;
    }

    public ConcurrentHashMap<DownloadThread, Boolean> getHm()
    {
        return hm;
    }

    public void setHm(ConcurrentHashMap<DownloadThread, Boolean> hm)
    {
        this.hm = hm;
    }

    public int getBufSize()
    {
        return bufSize;
    }

    public void setBufSize(int bufSize)
    {
        this.bufSize = bufSize;
    }

    public String getRemoteFile()
    {
        return remoteFile;
    }

    public void setRemoteFile(String remoteFile)
    {
        this.remoteFile = remoteFile;
    }

    public String getLocalFile()
    {
        return localFile;
    }

    public void setLocalFile(String localFile)
    {
        this.localFile = localFile;
    }

    public String getTmpFile()
    {
        return tmpFile;
    }

    public void setTmpFile(String tmpFile)
    {
        this.tmpFile = tmpFile;
    }

    public String getFileMD5()
    {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5)
    {
        this.fileMD5 = fileMD5;
    }

}
