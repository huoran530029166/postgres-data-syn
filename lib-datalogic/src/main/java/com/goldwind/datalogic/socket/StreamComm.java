package com.goldwind.datalogic.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.goldwind.dataaccess.file.FileAssistant;
import com.goldwind.datalogic.utils.DataAssemble;
import com.goldwind.datalogic.utils.DataParse;
import com.goldwind.datalogic.utils.NetCommDef;

/**
 * socket数据处理类
 *
 * @author 张超
 *
 */
public class StreamComm
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamComm.class);
    /**
     * 通讯缓存区大小
     */
    private static final int TCPSENDBUFFERSIZE = 8192;

    /**
     * 发送文件
     *
     * @param tcpClient
     *            socket连接
     * @param srcFileName
     *            源文件名称
     * @param sendFileName
     *            发送文件名称
     * @param isolation
     *            是否过隔离
     * @return 是否发送成功
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static boolean sendFile(Socket tcpClient, String srcFileName, String sendFileName, boolean isolation) throws IOException, NoSuchAlgorithmException
    {
        boolean success = false;
        FileInputStream fileStream = null;
        try
        {
            File file = new File(srcFileName);
            if (file.exists())
            {
                fileStream = new FileInputStream(file);
                // 拼接大数据转发标志
                String dataflag = DataAssemble.sendFileOrderAsm(sendFileName, getSendPackCount(fileStream), FileAssistant.getMd5ByFile(file));
                // 转发大数据标志
                if (StreamComm.sendNotice(tcpClient, dataflag.getBytes("UTF-8"), isolation))
                {
                    success = StreamComm.sendStream(tcpClient, fileStream, true, isolation);
                }
            }
        }
        finally
        {
            if (fileStream != null)
            {
                fileStream.close();
                fileStream = null;
            }
        }

        return success;
    }

    /**
     * 发送文件(通用)
     *
     * @param tcpClient
     *            socket连接
     * @param srcFileName
     *            源文件名称
     * @param sendFileName
     *            发送文件名称
     * @param sendFileType
     *            发送文件类型
     * @param dataTime
     *            时间
     * @param isolation
     *            是否过隔离
     * @return 是否发送成功
     * @throws IOException
     *             IO异常
     * @throws NoSuchAlgorithmException
     *             异常
     */
    public static boolean sendFile(Socket tcpClient, String srcFileName, String sendFileName, String sendFileType, String dataTime, boolean isolation) throws IOException, NoSuchAlgorithmException
    {
        boolean success = false;
        FileInputStream fileStream = null;
        try
        {
            File file = new File(srcFileName);
            fileStream = new FileInputStream(file);
            // 拼接大数据转发标志
            String dataflag = DataAssemble.saveFileOrderAsm(sendFileType, sendFileName, dataTime, getSendPackCount(fileStream), FileAssistant.getMd5ByFile(file));
            // 转发大数据标志
            if (StreamComm.sendNotice(tcpClient, dataflag.getBytes("UTF-8"), isolation))
            {
                success = StreamComm.sendStream(tcpClient, fileStream, true, isolation);
            }
        }
        finally
        {
            if (fileStream != null)
            {
                fileStream.close();
                fileStream = null;
            }
        }

        return success;
    }

    /**
     * 发送通知
     *
     * @param tcpClient
     *            socket连接
     * @param netData
     *            通知
     * @param isolation
     *            是否过隔离
     * @return 是否发送成功
     * @throws IOException
     *             IO异常
     */
    public static boolean sendNotice(Socket tcpClient, byte[] netData, boolean isolation) throws IOException
    {
        boolean success = false;

        OutputStream out = tcpClient.getOutputStream();
        out.write(netData);
        out.flush();

        while (true)
        {
            InputStream in = tcpClient.getInputStream();
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] result = new byte[1000];
            int num;
            if ((num = in.read(result)) != -1)
            {
                swapStream.write(result, 0, num);
                result = swapStream.toByteArray();
                if (!isolation && Arrays.equals(result, NetCommDef.NETWAITBUFFER))
                {
                    out.write(NetCommDef.NETSUCCEEDBUFFER);
                    out.flush();
                }
                else
                {
                    if (issuccess(result, isolation))
                    {
                        success = true;
                    }
                    break;
                }
            }
            else
            {
                break;
            }
        }

        return success;
    }

    /**
     * 发送数据流
     *
     * @param tcpClient
     *            socket连接
     * @param sendStream
     *            数据流
     * @param finshFlg
     *            是否需要完成标志
     * @param isolation
     *            是否过隔离
     * @return 是否发送成功
     * @throws IOException
     *             IO异常
     */
    public static boolean sendStream(Socket tcpClient, InputStream sendStream, boolean finshFlg, boolean isolation) throws IOException
    {
        boolean success = false;

        byte[] data = new byte[TCPSENDBUFFERSIZE - 8];
        int packCount = getSendPackCount(sendStream);
        int packNo = 1;
        // LOGGER.info("Send file to data disposal total count:" + packCount);
        while (packNo <= packCount)
        {
            int num = sendStream.read(data);
            if (num != -1)
            {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                outStream.write(data, 0, num);
                // 转发数据
                byte[] result = SocketComm.sendData(tcpClient, DataParse.packbyte(packNo, outStream.toByteArray()));
                // LOGGER.info("total count:" + packCount + ",packNo:" + packNo + ",Send file to data disposal result:" + (result == null ? null : new String(result, StandardCharsets.UTF_8)));
                if (!issuccess(result, isolation))// 如果没有返回成功标志，则跳出发包循环
                {
                    // LOGGER.error("Send file to data disposal failed." + (result == null ? "" : new String(result, StandardCharsets.UTF_8)));
                    break;
                }
                if (packNo == packCount)// 发送结束则发送完成标志
                {
                    if (finshFlg)
                    {
                        result = SocketComm.sendData(tcpClient, NetCommDef.NETFINISHBUFFER);// 转发完成标志
                        success = issuccess(result, isolation);
                        // if (!success)
                        // {
                        // LOGGER.error("Send file to data disposal failed." + (result == null ? "" : new String(result, StandardCharsets.UTF_8)));
                        // }
                    }
                    else
                    {
                        success = true;
                    }
                }
            }
            packNo++;
        }
        // LOGGER.info("Send file to data disposl:" + success);
        return success;
    }

    /**
     * 得到发送包数量
     *
     * @param stream
     *            数据流对象
     * @return 发送包数量
     * @throws IOException
     *             IO异常
     */
    public static int getSendPackCount(InputStream stream) throws IOException
    {
        int packnum;
        int size = stream.available();
        if (size % (TCPSENDBUFFERSIZE - 8) > 0)
        {
            packnum = (size / (TCPSENDBUFFERSIZE - 8)) + 1;
        }
        else
        {
            packnum = size / (TCPSENDBUFFERSIZE - 8);
        }
        return packnum;
    }

    /**
     * 根据地址和返回值判断转发是否成功
     *
     * @param resultMessage
     *            返回标志
     * @param isolation
     *            是否过隔离
     * @return 是否成功
     */
    private static boolean issuccess(byte[] resultMessage, boolean isolation)
    {
        boolean transflg = false;
        if (isolation)// 经过隔离
        {
            if (Arrays.equals(resultMessage, NetCommDef.NETISOLATIONSUC))
            {
                transflg = true;
            }
        }
        else if (Arrays.equals(resultMessage, NetCommDef.NETSUCCEEDBUFFER))// 未经过隔离
        {
            transflg = true;
        }
        return transflg;
    }
}
