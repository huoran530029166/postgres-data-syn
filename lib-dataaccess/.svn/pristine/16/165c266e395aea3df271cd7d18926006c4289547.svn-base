package com.goldwind.dataaccess.file;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.Downprocess;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.SerialOper;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.security.EncryptClass;

/**
 * socket文件传输客户端
 *
 * @author Administrator
 *
 */
public class SocketFileClient
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(SocketFileClient.class);
    /**
     * 缓冲区大小，kb
     */
    private int bufSize = 1024 * 32;
    /**
     * 服务端ip
     */
    private String remoteIp;
    /**
     * 服务端端口
     */
    private int remotePort;
    /**
     * 线程数
     */
    private int threadCount = 5;

    /**
     * 超时时间，秒
     */
    private int timeOut = 5;
    /**
     * 密钥
     */
    private final String key = "ServiceableTool";

    private final String password = "c1366031266d72a715259490e8ab92da";

    public int getTimeOut()
    {
        return timeOut;
    }

    public void setTimeOut(int timeOut)
    {
        this.timeOut = timeOut;
    }

    public int getBufSize()
    {
        return bufSize;
    }

    public void setBufSize(int size)
    {
        this.bufSize = size * 1024;
    }

    public int getThreadCount()
    {
        return threadCount;
    }

    public void setThreadCount(int threadCount)
    {
        this.threadCount = threadCount;
    }

    public SocketFileClient(String remoteIp, int remotePort)
    {
        this.remoteIp = remoteIp;
        this.remotePort = remotePort;
    }

    /**
     * 查询已下载进度
     * 
     * @param remoteFilePath
     *            目标文件存放路径+文件名
     * @param localFilePath
     *            本地文件存放路径+文件名
     * @return 下载进度对象
     * @throws Exception
     */
    public Downprocess getAlreadyDownprocess(String remoteFilePath, String localFilePath) throws Exception
    {

        Long fileSize = 0L;
        Downprocess dp = new Downprocess(remoteFilePath, localFilePath);
        dp.setThreadCount(threadCount);
        String tmpFile = localFilePath + DataAsDef.BUFFTEMPEXTNAME;
        dp.setTmpFile(tmpFile);
        try (Socket client = new Socket(remoteIp, remotePort);)
        {
            client.setSoTimeout(1000 * timeOut);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            // 发送下载请求
            out.println("(download|1|" + remoteFilePath + ")");
            out.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true)
            {

                String message = br.readLine();
                if (message == null)
                {
                    break;
                }

                if (message.startsWith("(") && message.endsWith(")"))
                {
                    String msg = message.substring(1, message.length() - 1);
                    if (msg != null && !msg.equals("fault"))
                    {
                        String size = msg.split("\\|")[0];
                        String fMd5 = "";
                        if (msg.split("\\|").length > 2)
                        {
                            fMd5 = msg.split("\\|")[1];
                        }
                        fileSize = Long.parseLong(size);
                        // RandomAccessFile raf = new RandomAccessFile(tmpFile, "rw");
                        // raf.setLength(fileSize);
                        // raf.close();
                        dp.setCout(fileSize);
                        dp.setFileMD5(fMd5);
                        HashMap<String, long[]> process = (HashMap<String, long[]>) SerialOper.readObject(tmpFile + ".log");
                        if (process != null)
                        {
                            dp.setProcess(process.get(dp.getFileMD5()));
                        }
                    }
                    break;
                }

            }
        }
        return dp;

    }

    /**
     * 下载文件客户端
     *
     * @param remoteFilePath
     *            目标文件存放路径+文件名
     * @param localFilePath
     *            本地文件路径+文件名
     * @param dp
     *            下载进度
     * @return 是否下载成功
     */
    public boolean downLoad(String remoteFilePath, String localFilePath, Downprocess dp)
    {
        dp.setBufSize(bufSize);
        dp.setThreadCount(threadCount);
        dp.setSuccess(false);

        // 开始接收文件
        if (dp.getCout() > 0)
        {
            try (Socket client = new Socket(remoteIp, remotePort);)
            {
                ConcurrentHashMap<DownloadThread, Boolean> threadStatus = dp.getHm();
                Long dataSize = dp.getCout() / threadCount;
                String pwd = EncryptClass.encryptMD5(key + password);
                if (dataSize < bufSize * threadCount)
                {
                    DownloadThread dt = new DownloadThread(client, 0 + dp.getProcess()[0], dp.getCout() - dp.getProcess()[0], dp, 0, pwd);
                    threadStatus.put(dt, false);
                    dt.start();
                }
                else
                {
                    Long lastDataSize = dp.getCout() % threadCount;
                    for (int i = 0; i < threadCount; i++)
                    {
                        long seek = dataSize * i + dp.getProcess()[i];
                        if (i == threadCount - 1)
                        {
                            DownloadThread dt = new DownloadThread(client, seek, (dataSize + lastDataSize - dp.getProcess()[i]), dp, i, pwd);
                            threadStatus.put(dt, false);
                            dt.start();
                        }
                        else
                        {
                            DownloadThread dt = new DownloadThread(client, seek, (dataSize - dp.getProcess()[i]), dp, i, pwd);
                            threadStatus.put(dt, false);
                            dt.start();
                        }
                    }

                } // 文件下载完成校验
                if (dp.isAllInterrupted())
                {
                    if (!threadStatus.containsValue(false))
                    {
                        FileAssistant.copyFileOperation(dp.getTmpFile(), localFilePath);
                        FileAssistant.deleteFile(dp.getTmpFile());
                        FileAssistant.deleteFile(dp.getTmpFile() + ".log");
                        dp.setSuccess(true);
                    }
                }
            }
            catch (NumberFormatException e)
            {
                LOGGER.error(e);
            }
            catch (DataAsException e)
            {
                LOGGER.error(e);
            }
            catch (IOException e)
            {
                LOGGER.error(e);
            }
            catch (NoSuchAlgorithmException e)
            {
                LOGGER.error(e);
            }
        }
        return dp.isSuccess();
    }

    /**
     * 下载文件客户端
     *
     * @param remoteFilePath
     *            目标文件存放路径+文件名
     * @param localFilePath
     *            本地文件路径+文件名
     * @return 是否下载成功
     * @throws Exception
     */
    public boolean downLoad(String remoteFilePath, String localFilePath) throws Exception
    {
        return downLoad(getAlreadyDownprocess(remoteFilePath, localFilePath));
    }

    /**
     * 根据下载进度下载
     * 
     * @param dp
     *            下载进度
     * @return 是否下载成功
     */
    public boolean downLoad(Downprocess dp)
    {
        return downLoad(dp.getRemoteFile(), dp.getLocalFile(), dp);
    }

    /**
     * 上传文件
     *
     * @param remoteFilePath
     *            目标文件存放路径+文件名
     * @param localFilePath
     *            本地文件路径+文件名
     * @return 是否上传成功
     * @throws Exception
     */
    public boolean upload(String remoteFilePath, String localFilePath) throws Exception
    {
        boolean isSuccess = false;
        long fileSize = 0L;
        try (Socket client = new Socket(remoteIp, remotePort); RandomAccessFile raf = new RandomAccessFile(localFilePath, "r");)
        {
            fileSize = raf.length();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println("(upload|1|" + remoteFilePath + "|" + fileSize + ")");
            out.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String message = br.readLine();
            if ("(ok)".equals(message))
            {
                ConcurrentHashMap<Thread, Boolean> threadMap = new ConcurrentHashMap<Thread, Boolean>();
                Long dataSize = fileSize / threadCount;
                if (dataSize < bufSize)
                {
                    Thread t = new Thread(putFileThread(remoteFilePath, localFilePath, 0, fileSize, threadMap));
                    threadMap.put(t, false);
                    t.start();
                }
                else
                {
                    Long lastDataSize = fileSize % threadCount;
                    for (int i = 0; i < threadCount; i++)
                    {
                        if (i == threadCount - 1)
                        {
                            Thread t = new Thread(putFileThread(remoteFilePath, localFilePath, dataSize * i, (dataSize + lastDataSize), threadMap));
                            threadMap.put(t, false);
                            t.start();
                        }
                        else
                        {
                            Thread t = new Thread(putFileThread(remoteFilePath, localFilePath, dataSize * i, dataSize, threadMap));
                            threadMap.put(t, false);
                            t.start();
                        }
                    }
                }

                // System.out.println("begin==" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                // 文件上传完成校验
                if (isAllInterrupted(threadMap))
                {
                    if (!threadMap.containsValue(false))
                    {
                        isSuccess = true;
                    }
                }

                // System.out.println("end==" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            }
        }
        return isSuccess;

    }

    /**
     * 发送文件线程
     *
     * @param remoteFile
     *            目标文件存放路径+文件名
     * @param localFile
     *            本地文件路径+文件名
     * @param seek
     *            目标文件跳过长度
     * @param dataSize
     *            线程接收长度
     * @param hm
     *            线程状态表
     * @return 线程
     */
    private Runnable putFileThread(String remoteFile, String localFile, long seek, long dataSize, ConcurrentHashMap<Thread, Boolean> hm)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {

                try (Socket client = new Socket(remoteIp, remotePort); RandomAccessFile raf = new RandomAccessFile(localFile, "r");)
                {
                    byte[] buf = new byte[bufSize];
                    client.setSoTimeout(1000 * timeOut);
                    InputStream is = client.getInputStream();
                    OutputStream os = client.getOutputStream();
                    long lastSize = dataSize % bufSize;
                    long loopSize = dataSize / bufSize;
                    if (lastSize > 0)
                    {
                        loopSize++;
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    PrintWriter out = new PrintWriter(os, false);
                    DataOutputStream dos = new DataOutputStream(os);
                    out.println("(upload|2|" + remoteFile + "|" + seek + "|" + loopSize + "|" + EncryptClass.encryptMD5(key + password) + ")");
                    out.flush();
                    String back = br.readLine();
                    long total = 0L;
                    if ("(ok)".equals(back))
                    {
                        for (int i = 0; i < loopSize; i++)
                        {

                            int len = 0;
                            raf.seek(seek + total);
                            if (i == loopSize - 1 && lastSize > 0)
                            {
                                len = raf.read(buf, 0, Integer.parseInt(String.valueOf(lastSize)));
                            }
                            else
                            {
                                len = raf.read(buf, 0, buf.length);
                            }
                            dos.writeInt(len);
                            dos.flush();
                            dos.write(buf, 0, len);
                            dos.flush();
                            total += len;
                        }
                    }
                    hm.put(Thread.currentThread(), true);
                }
                catch (Exception e)
                {
                    LOGGER.error("SocketFileClient_putFileThread_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                }
            }

        };
    }

    /**
     * 判断哈希表中的线程是否都已结束
     *
     * @param hm
     *            线程表
     * @return 是否
     */
    private boolean isAllInterrupted(ConcurrentHashMap<Thread, Boolean> hm)
    {
        boolean back = false;
        while (true)
        {
            int count = 0;
            Enumeration<Thread> e = hm.keys();
            while (e.hasMoreElements())
            {
                Thread t = e.nextElement();
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
        }
        return back;
    }
}
