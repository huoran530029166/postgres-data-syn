package com.goldwind.datalogic.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.goldwind.dataaccess.security.EncryptClass;

/**
 * 数据加密类
 *
 * @author 冯春源
 */
public class DataEncrypt// NOSONAR
{
    /**
     * 控制信息加密-控制链路认证(V5.3.300.0 添加协议号)
     * 
     * @param deviceid
     *            设备编号
     * @param ip
     *            前置IP
     * @param port
     *            前置端口
     * @return 加密后的信息
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             异常
     */
    public static String controlLinkEncrypt(String deviceid, String ip, String port, String protocolid) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        return EncryptClass.encryptMD5(deviceid + ip + port + protocolid);
    }

    /**
     * 控制信息加密-控制指令认证
     * 
     * @param protocolid
     *            协议编号
     * @param ordertype
     *            指令类别
     * @param orderflg
     *            指令标志
     * @param descrcn
     *            中文描述
     * @param descren
     *            英文描述
     * @param iecfixed
     *            控制默认值
     * @return 加密后的信息
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             异常
     */
    public static String controlOrderEncrypt(String protocolid, String ordertype, String orderflg, String descrcn, String descren, String iecfixed)
            throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        return EncryptClass.encryptMD5(protocolid + ordertype + orderflg + descrcn + descren + iecfixed);
    }

    /**
     * 控制信息加密-组态图元控制认证
     * 
     * @param graphinfoid
     *            图编号
     * @param pelid
     *            图元编号
     * @param peltext
     *            图元文本
     * @param deviceid
     *            设备编号
     * @param iecpath
     *            IEC路径
     * @param iecpathdescrcn
     *            IEC路径中文描述
     * @param iecpathdescren
     *            IEC路径英文描述
     * @return 加密后的信息
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             异常
     */
    public static String controlOrderEncrypt(String graphinfoid, String pelid, String peltext, String deviceid, String iecpath, String iecpathdescrcn, String iecpathdescren)
            throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        return EncryptClass.encryptMD5(graphinfoid + pelid + peltext + deviceid + iecpath + iecpathdescrcn + iecpathdescren);
    }

}
