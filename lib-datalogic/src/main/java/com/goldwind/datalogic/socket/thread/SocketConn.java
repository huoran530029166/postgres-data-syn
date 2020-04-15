package com.goldwind.datalogic.socket.thread;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.socket.SocketComm;
import com.goldwind.datalogic.socket.model.TransAd;
import com.goldwind.datalogic.utils.NetCommDef;

/**
 * socket连接通讯监测
 * 
 * @author 王瑞博
 *
 */
public class SocketConn implements Runnable
{
    /**
     * 错误日志
     */
    private static Log logger = Log.getLog(SocketConn.class);

    /**
     * 转发地址信息
     */
    private TransAd transAd;

    /**
     * 通讯状态
     */
    private HashMap<TransAd, Boolean> transMAP = new HashMap<>();

    /**
     * 场站id
     */
    private String wfid;
    /**
     * ip
     */
    private String ip;
    /**
     * 传输数据类型
     */
    private String dataType;

    /**
     * 终止线程标志
     */
    private static volatile boolean exit = false;

    public static void setExit(boolean exitFlg)
    {
        exit = exitFlg;
    }

    /**
     * 通讯测试
     * 
     * @param transAd
     */
    public SocketConn(TransAd transAd, HashMap<TransAd, Boolean> transMAP)
    {
        this.transAd = transAd;
        this.transMAP = transMAP;
    }

    public SocketConn(TransAd transAd, HashMap<TransAd, Boolean> transMAP, String wfid, String ip, String dataType)
    {
        this.transAd = transAd;
        this.transMAP = transMAP;
        this.ip = ip;
        this.wfid = wfid;
        this.dataType = dataType;
    }

    @Override
    public void run()
    {
        while (!exit)
        {
            try (Socket tcpClient = SocketComm.createSocketConn(transAd.getIp(), transAd.getPort());)
            {
                String heartBeat = "comtest()";
                if (!StringUtils.isBlank(wfid))
                {
                    // 注册心跳报文comtest(wfid|ip|serviceType|dataType)
                    heartBeat = "comtest(" + wfid + "|" + ip + "|2|" + dataType + ")";
                }

                // 得到ip和端口
                boolean commtest = false;
                // 发送通讯测试标志
                byte[] result = SocketComm.sendData(tcpClient, heartBeat.getBytes(StandardCharsets.UTF_8));
                // 不经过隔离
                if (Arrays.equals(result, NetCommDef.NETISOLATSUCFLG.getBytes()) && !transAd.isIsolation())
                {
                    commtest = true;
                }
                // 经过隔离
                if (Arrays.equals(result, NetCommDef.NETISOLATIONSUC) && transAd.isIsolation())
                {
                    commtest = true;
                }
                // LOGGER.info("ip:" + transAd.getIp() + " port:" + transAd.getPort() + " comtest:" + commtest);
                transMAP.put(transAd, commtest);
            }
            catch (Exception exp)
            {
                logger.error(DataAsExpSet.getExpMsgByResCode("SocketConn_run_1", new String[] { "ip", "port" }, new Object[] { transAd.getIp(), transAd.getPort() }, exp));
            }

            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}
