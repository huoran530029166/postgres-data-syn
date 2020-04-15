package com.goldwind.dataaccess.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;

import com.goldwind.dataaccess.DataAsDef.ApplicationType;
import com.goldwind.dataaccess.Log;

/**
 * socket文件传输服务端
 * 
 * @author Administrator
 *
 */
public class SocketFileServer
{
    /**
     * 输出日志
     */
    private static Log logger = Log.getLog(SocketFileServer.class);
    /**
     * 可服务工具密钥
     */
    private final String serviceableToolPWD = getMd5(ApplicationType.ServiceableTool + getMd5(ApplicationType.ServiceableTool.getName()));
    /**
     * 指标工具密钥
     */
    private final String quotaToolPWD = getMd5(ApplicationType.QuotaTools + getMd5(ApplicationType.QuotaTools.getName()));
    /**
     * 其他工具密钥
     */
    private final String otherPWD = getMd5(ApplicationType.OTHERS + getMd5(ApplicationType.OTHERS.getName()));
    /**
     * 监听端口
     */
    private int port;
    /**
     * 缓冲区大小，kb
     */
    private int bufSize = 1024 * 32;// kb
    /**
     * 超时时间，秒
     */
    private int timeOut = 5;// second

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

    public void setBufSize(int bufSize)
    {
        this.bufSize = bufSize * 1024;
    }

    public SocketFileServer(int port)
    {
        this.port = port;
    }

    /**
     * 启动监听
     * 
     */
    public void startListening()
    {

        try (ServerSocket sc = new ServerSocket(port);)
        {
            while (1 > 0)
            {
                Socket server = sc.accept();
                server.setSoTimeout(1000 * timeOut);
                new Thread(serverhandler(server)).start();
            }
        }
        catch (IOException e)
        {
            logger.error("SocketFileServer_startListening_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);

        }
    }

    /**
     * 处理连接请求线程
     * 
     * @param server
     *            客户端
     * @return 线程
     */
    private Runnable serverhandler(Socket server)
    {
        return new Runnable()
        {

            @Override
            public void run()
            {
                InputStream ins = null;
                OutputStream ops = null;
                boolean connStatus = true;
                try
                {
                    ins = server.getInputStream();
                    ops = server.getOutputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(ins));
                    DataOutputStream out = new DataOutputStream(ops);
                    PrintWriter pw = new PrintWriter(ops, false);
                    RandomAccessFile raf = null;
                    while (connStatus)
                    {
                        String message = null;
                        message = in.readLine();
                        if (message == null)
                        {
                            break;
                        }
                        if (message.startsWith("(") && message.endsWith(")"))
                        {
                            message = message.substring(1, message.length() - 1);
                            String[] args = message.split("\\|");
                            String type = args[0];
                            if ("download".equals(type))
                            {
                                String step = args[1];
                                switch (step) {
                                    case "1":
                                        String filePath = args[2];
                                        if (filePath != null && !filePath.isEmpty())
                                        {
                                            try
                                            {
                                                if (raf == null)
                                                {
                                                    raf = new RandomAccessFile(filePath, "r");
                                                }
                                                pw.println("(" + raf.length() + "|" + FileAssistant.getMd5ByFile(new File(filePath)) + ")");
                                                pw.flush();
                                                raf.close();
                                                connStatus = false;
                                            }
                                            catch (Exception e)
                                            {
                                                pw.println("(fault)");
                                                pw.flush();
                                                logger.error("SocketFileClient_serverhandler_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                                            }
                                        }
                                        break;
                                    case "2":
                                        byte[] buf = new byte[bufSize];
                                        String fileName = args[2];
                                        String seek = args[3];
                                        String loopSize = args[4];
                                        String lastSize = args[5];
                                        try
                                        {
                                            if (raf == null)
                                            {
                                                raf = new RandomAccessFile(fileName, "r");
                                            }
                                            raf.seek(Long.parseLong(seek));
                                            int len = 0;
                                            for (int i = 0; i < Integer.parseInt(loopSize); i++)
                                            {
                                                if (i == Integer.parseInt(loopSize) - 1)
                                                {
                                                    len = raf.read(buf, 0, Integer.parseInt(lastSize));
                                                }
                                                else
                                                {
                                                    len = raf.read(buf, 0, buf.length);
                                                }
                                                out.writeInt(len);
                                                out.flush();
                                                out.write(buf, 0, len);
                                                out.flush();
                                            }
                                            connStatus = false;
                                        }
                                        catch (Exception e)
                                        {
                                            logger.error("SocketFileClient_serverhandler1_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                                        }
                                        break;
                                    default:
                                        break;
                                }

                            }
                            else if ("upload".equals(type))
                            {
                                String step = args[1];
                                String fileName = args[2];
                                switch (step) {
                                    case "1":
                                        long fileSize = Long.parseLong(args[3]);
                                        try
                                        {
                                            File file = new File(fileName);
                                            if (file != null && !file.exists())
                                            {
                                                File dir = file.getParentFile();
                                                dir.mkdirs();
                                                raf = new RandomAccessFile(fileName, "rw");
                                                raf.setLength(fileSize);
                                                raf.close();
                                            }
                                            else
                                            {
                                                System.gc();
                                                file.delete();
                                            }
                                        }
                                        catch (IOException e)
                                        {
                                            pw.println("(fault)");
                                            pw.flush();
                                            logger.error("SocketFileClient_serverhandler2_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                                        }
                                        pw.println("(ok)");
                                        pw.flush();
                                        connStatus = false;
                                        break;
                                    case "2":
                                        byte[] buf = new byte[bufSize];
                                        long seek = Long.parseLong(args[3]);// 跳过文件长度
                                        String loopSize = args[4];// 包数量
                                        if (args.length > 5 && authenticate(args[5]))
                                        {
                                            pw.println("(ok)");
                                            pw.flush();
                                            DataInputStream dis = new DataInputStream(ins);
                                            try
                                            {
                                                if (raf == null)
                                                {
                                                    raf = new RandomAccessFile(fileName, "rw");
                                                }
                                                raf.seek(seek);
                                                for (int i = 0; i < Integer.parseInt(loopSize); i++)
                                                {
                                                    int dataLen = dis.readInt();
                                                    int len = dis.read(buf, 0, buf.length);
                                                    if (len < dataLen)
                                                    {
                                                        while (len < dataLen)
                                                        {
                                                            len += dis.read(buf, len, dataLen - len);
                                                        }
                                                    }
                                                    raf.write(buf, 0, dataLen);
                                                }
                                                connStatus = false;
                                            }
                                            catch (IOException e)
                                            {
                                                logger.error("SocketFileClient_serverhandler3_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                                            }
                                            finally
                                            {
                                                dis.close();
                                            }
                                        }
                                        else
                                        {
                                            pw.println("(fault)");
                                            pw.flush();
                                        }

                                        break;
                                    default:
                                        break;
                                }
                            }
                            else if ("excute".equals(type))
                            {
                                String path = args[1];
                                String params = args[2];
                                Process process = null;
                                StringBuffer stringBuffer = new StringBuffer();
                                BufferedReader bufferedReader = null;
                                String[] command1 = { "/bin/sh", "-c", "chmod -R 777 " + path };
                                String[] command2 = { "/bin/sh", "-c", path + " " + params };
                                process = Runtime.getRuntime().exec(command1);
                                process.waitFor();
                                process = Runtime.getRuntime().exec(command2);
                                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
                                process.waitFor();

                                String line = null;
                                while (bufferedReader != null && (line = bufferedReader.readLine()) != null)
                                {
                                    stringBuffer.append(line).append("\r\n");
                                }
                                pw.println(process.exitValue() + "|" + stringBuffer);
                                pw.flush();
                            }
                        }
                    }

                    if (server != null)
                    {
                        server.close();
                    }
                    if (raf != null)
                    {
                        raf.close();
                    }
                }
                catch (IOException e2)
                {
                    logger.error("SocketFileClient_serverhandler4_" + e2.getClass().getName() + "_" + e2.getLocalizedMessage(), e2);
                }
                catch (Exception e)
                {
                    logger.error("SocketFileClient_serverhandler5_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
                }
            }

        };
    }

    /**
     * 判断秘钥是否正确
     * 
     * @param pwd
     *            加密串
     * @return 比较结果
     */
    private boolean authenticate(String pwd)
    {
        return serviceableToolPWD.equals(pwd) || quotaToolPWD.equals(pwd) || otherPWD.equals(pwd);
    }

    /**
     * 获取MD5加密串
     * 
     * @param str
     *            字符串
     * @return MD5加密串
     */
    private String getMd5(String str)
    {
        StringBuilder stringbuffer = new StringBuilder();
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] resultBytes = messageDigest.digest();
            stringbuffer = new StringBuilder(2 * resultBytes.length);
            for (int l = 0; l < resultBytes.length; l++)
            {
                char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
                char c0 = hexDigits[(resultBytes[l] & 0xf0) >> 4];// 取字节中高 4 位的数字转换
                // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
                char c1 = hexDigits[resultBytes[l] & 0xf];// 取字节中低 4 位的数字转换
                stringbuffer.append(c0);
                stringbuffer.append(c1);
            }
        }
        catch (Exception e)
        {
            logger.error("SocketFileClient_serverhandler5_" + e.getClass().getName() + "_" + e.getLocalizedMessage(), e);
        }

        return stringbuffer.toString().trim();
    }
}
