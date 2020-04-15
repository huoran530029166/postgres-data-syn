package com.goldwind.dataaccess.file;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;

import com.goldwind.dataaccess.Downprocess;
import com.goldwind.dataaccess.Log;

/**
 * 
 * @author caoyang
 *
 */
public class DownloadThread
{

    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(DownloadThread.class);
    /**
     * 跳过文件长度
     */
    private long seek;
    /**
     * 接收文件长度
     */
    private long dataSize;
    /**
     * 下载进度对象
     */
    private Downprocess dp;
    /**
     * 线程序号
     */
    private int index;
    /**
     * socket客户端
     */
    private Socket socket;
    /**
     * 下载标志
     */
    private boolean runFlag = true;
    /**
     * 下载线程
     */
    private Thread thread;
    /**
     * 
     */
    private DownloadThread dt;

    private String password;

    public DownloadThread(Socket client, long seek, long dataSize, Downprocess dp, int index, String pwd)
    {
        this.seek = seek;
        this.dataSize = dataSize;
        this.dp = dp;
        this.socket = client;
        this.index = index;
        dt = this;
        this.password = pwd;
    }

    /**
     * 
     * @return 线程
     */
    private Runnable download()
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                try (Socket client = new Socket(socket.getInetAddress(), socket.getPort()); RandomAccessFile raf = new RandomAccessFile(dp.getTmpFile(), "rw"))
                {
                    client.setSoTimeout(1000 * dp.getTimeOut());
                    InputStream is = client.getInputStream();
                    OutputStream os = client.getOutputStream();
                    PrintWriter out = new PrintWriter(os, false);
                    long lastSize = dataSize % dp.getBufSize();
                    long loopSize = dataSize / dp.getBufSize();
                    if (lastSize > 0)
                    {
                        loopSize++;
                    }

                    out.println("(download|2|" + dp.getRemoteFile() + "|" + seek + "|" + loopSize + "|" + lastSize + "|" + password + ")");
                    out.flush();
                    DataInputStream dis = new DataInputStream(is);
                    byte[] buf = new byte[dp.getBufSize()];
                    long total = 0;
                    for (int i = 0; i < loopSize; i++)
                    {
                        int len = 0;
                        int dataLen = dis.readInt();
                        len = dis.read(buf, 0, buf.length);
                        while (len < dataLen)
                        {
                            len += dis.read(buf, len, dataLen - len);
                        }
                        raf.seek(seek + total);
                        raf.write(buf, 0, len);
                        total += len;
                        dp.getProcess()[index] += len;
                        if (!runFlag)
                        {
                            break;
                        }
                    }
                    if (runFlag)
                    {
                        dp.getHm().put(dt, true);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("DownloadThread_download_0" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);

                    try
                    {
                        dp.pauseDownload();
                    }
                    catch (Exception e1)
                    {
                        LOGGER.error("DownloadThread_download_1" + e1.getClass().getName() + "_" + e1.getLocalizedMessage(), e1);
                    }
                }
            }
        };
    }

    /**
     * 
     */
    public void start()
    {
        thread = new Thread(download());
        thread.start();
    }

    public boolean getRunFlag()
    {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag)
    {
        this.runFlag = runFlag;
    }

    public boolean isAlive()
    {
        return thread.isAlive();
    }

    public Thread getThread()
    {
        return thread;
    }

}
