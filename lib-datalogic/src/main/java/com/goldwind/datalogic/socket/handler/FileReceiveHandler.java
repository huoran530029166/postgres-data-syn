package com.goldwind.datalogic.socket.handler;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.DataParse;
import com.goldwind.datalogic.utils.NetCommDef;
import com.goldwind.datalogic.utils.NetCommDef.NetNoDirDataType;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

/**
 * 文件接收处理器
 * 
 * @author 张超
 *
 */
@Sharable
public abstract class FileReceiveHandler extends IMessageHandler
{
    /**
     * 错误日志
     */
    private static Log logger = Log.getLog(FileReceiveHandler.class);

    /**
     * 是否是大数据
     */
    private static ConcurrentHashMap<ChannelId, Boolean> bigDataFlg = new ConcurrentHashMap<>();
    /**
     * 大数据包数量
     */
    private static ConcurrentHashMap<ChannelId, Integer> packCount = new ConcurrentHashMap<>();
    /**
     * 已接收到的大数据包数量
     */
    private static ConcurrentHashMap<ChannelId, Integer> recPackCount = new ConcurrentHashMap<>();
    /**
     * 大数据是接收完成
     */
    private static ConcurrentHashMap<ChannelId, Boolean> bigDataEnd = new ConcurrentHashMap<>();
    /**
     * 文件保存路径
     */
    private static ConcurrentHashMap<ChannelId, String> sendName = new ConcurrentHashMap<>();
    /**
     * 是否隔离
     */
    protected boolean islation;

    /**
     * 数据接收处理
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, byte[] msg)// NOSONAR
    {
        ChannelId channelId = ctx.channel().id();
        String val = NetCommDef.NETSUCCEEDFLAG;
        String log = DataAsExpSet.getMsgByResCode("FileReceiveHandler_messageReceived_1").replace("@beginTime", new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(new Date()));

        try
        {

            if (bigDataFlg.containsKey(channelId) && bigDataFlg.get(channelId))
            {
                if (!recPackCount.containsKey(channelId))
                {
                    recPackCount.put(channelId, 1);
                }
                else
                {
                    recPackCount.put(channelId, recPackCount.get(channelId) + 1);
                }
                HashMap<String, Object> hp = DataParse.parseBigData(msg);
                // 数据包接收完成
                if ((int) hp.get("packNum") == packCount.get(channelId))
                {
                    bigDataFlg.put(channelId, false);
                    bigDataEnd.put(channelId, true);

                    // 若传输过程中丢包，则返回错误信息
                    if (recPackCount.get(channelId) < packCount.get(channelId))
                    {
                        val = NetCommDef.NETNODEFINEFLAG;
                    }
                    else
                    {
                        // 文件接收后处理(最后一个包)
                        runFuncAfterReceive((byte[]) hp.get("packData"), sendName.get(channelId), packCount.get(channelId), true);
                        packCount.remove(channelId);
                        recPackCount.remove(channelId);
                        bigDataEnd.remove(channelId);
                        bigDataFlg.remove(channelId);
                        sendName.remove(channelId);
                    }

                    // 记录维护日志
                    log = log.replace("@endTime", new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(new Date()));
                    logger.info(log);
                }
                else
                {
                    // 文件接收后处理
                    runFuncAfterReceive((byte[]) hp.get("packData"), sendName.get(channelId), recPackCount.get(channelId), false);
                }
            }
            else
            {
                // 具体处理数据
                String message = new String(msg, StandardCharsets.UTF_8);
                val = dealData(message, channelId);
            }
        }
        catch (Exception e)
        {
            logger.error("FileReceive_messageReceived_1" + e.getClass().getName(), e);
        }

        if (val != null)
        {
            if (!islation)// 判断是否通过隔离
            {
                ctx.channel().writeAndFlush(val.getBytes());
            }
            else
            {
                byte[] byteVal = null;
                if (val.equals(NetCommDef.NETNODEFINEFLAG) || val.equals(NetCommDef.NETBUSYFLAG))
                {
                    byteVal = NetCommDef.NETISOLATIONERROR;
                }
                else if (val.equals(NetCommDef.NETSUCCEEDFLAG) || val.equals(NetCommDef.NETISOLATSUCFLG))
                {
                    byteVal = NetCommDef.NETISOLATIONSUC;
                }
                ctx.channel().writeAndFlush(byteVal);
            }
        }
    }

    /**
     * 处理数据
     * 
     * @param msg
     *            数据
     * @param channelId
     *            通道Id
     * @return 返回值
     * @throws DataAsException
     *             异常信息
     */
    public String dealData(String msg, ChannelId channelId) throws DataAsException
    {
        String val = "";
        String message = msg.trim();

        if (message.length() < 100000)
        {
            if (message.equals(NetCommDef.NETFINISHFLAG))
            {
                // 若最后一个数据包丢失，则返回错误信息
                if (recPackCount.contains(channelId))
                {
                    val = NetCommDef.NETNODEFINEFLAG;
                }
                else
                {
                    val = NetCommDef.NETSUCCEEDFLAG;
                }
            }
            else
            {
                NetNoDirDataType dataType = DataParse.getNoDirectDataType(message);
                if (NetNoDirDataType.SendFile.equals(dataType))
                {
                    try
                    {
                        bigDataFlg.put(channelId, true);// 开启接收大数据标志
                        bigDataEnd.put(channelId, false); // 关闭大数据接收完成标志
                        HashMap<String, Object> bigdata = DataParse.parseSendFileOrder(message);// 解析发送文件的指令
                        packCount.put(channelId, (int) bigdata.get("packCount"));
                        sendName.put(channelId, (String) bigdata.get("fileName"));
                        val = NetCommDef.NETSUCCEEDFLAG;
                    }
                    catch (Exception e)
                    {
                        DataAsExpSet.throwExpByResCode("FileReceiveHandler_dealData_1", new String[] { "message" }, new Object[] { message }, e);
                    }
                }
                else if (NetNoDirDataType.OldSrvComTest.equals(dataType))
                {
                    val = "1";
                }
            }
        }
        return val;
    }

    /**
     * 文件接收后处理函数
     * 
     * @param data
     *            文件流
     * @param fileName
     *            保存文件路径（可以是ftp路径，也可以是普通路径）
     * @param num
     *            文件的第几个包
     * @param endFlg
     *            文件结束标志
     * @throws DataAsException
     *             自定义异常
     */
    public abstract void runFuncAfterReceive(byte[] data, String fileName, int num, boolean endFlg) throws DataAsException;
}
