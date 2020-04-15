package com.goldwind.dataaccess.security;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;

/**
 * 
 * @author 曹阳
 *
 */
public class EncryptClass
{
    /**
     * MD5加密
     * 
     * @param data
     *            加密字符串
     * @return md5加密后字符串
     * @throws UnsupportedEncodingException
     *             不支持编码异常
     * @throws NoSuchAlgorithmException
     *             未找到算法异常
     */
    public static String encryptMD5(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException
    {
        String val = null;
        val = new String(data.getBytes(DataAsDef.getCNCHARSET()), DataAsDef.getCNCHARSET());
        MessageDigest md = MessageDigest.getInstance("MD5");
        val = byteToString(md.digest(val.getBytes(DataAsDef.getCNCHARSET())));
        return val;
    }

    /**
     * 数字字符串数组
     */
    private static final String[] STRDIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 字节转字符串
     * 
     * @param bByte
     *            字节
     * @return 字符串
     */
    private static String byteToArrayString(byte bByte)
    {
        int iRet = bByte;
        if (iRet < 0)
        {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return STRDIGITS[iD1] + STRDIGITS[iD2];
    }

    /**
     * 字节数组转字符串
     * 
     * @param bByte
     *            字节数组
     * @return 字符串
     */
    private static String byteToString(byte[] bByte)
    {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++)
        {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 可反向解密的加密
     * 
     * @param data
     *            明文
     * @return 密文
     * @throws Exception
     *             异常
     */
    public static String reEncrypt(String data) throws Exception
    {
        return reEncrypt(data, SecurityDef.DECKEY);
    }

    /**
     * 使用指定的key进行加密,此加密可反向解密
     * 
     * @param data
     *            明文
     * @param skey
     *            密钥
     * @return 密文
     * @throws Exception
     *             异常
     */
    public static String reEncrypt(String data, String skey) throws Exception
    {
        if (skey == null || skey.isEmpty() || skey.length() > 8)
        {
            DataAsExpSet.throwExpByResCode("EncryptClass_reEncrypt_1", new String[] { "data", "key" }, new Object[] { data, skey }, null);
        }
        if (skey.length() < 8)
        {
            skey = DataAsFunc.padLeft(skey, 8);
        }
        Encoder base64en = Base64.getEncoder();
        byte[] byteMing = data.getBytes("UTF8");
        Key key = setKey(skey, "UTF8");
        byte[] byteMi = getEncCode(byteMing, key);
        return new String(base64en.encode(byteMi));
    }

    /**
     * 根据日期加密字符串数组
     * 
     * @param dataArray
     *            明文数组
     * @param kind
     *            密钥
     * @return 密文
     * @throws Exception
     *             异常
     */
    public static String encryptByDate(String[] dataArray, int kind) throws Exception
    {
        if (dataArray == null)
        {
            DataAsExpSet.throwExpByResCode("EncryptClass_encryptByDate_1", new String[] { "dataArray", "kind" }, new Object[] { dataArray, kind }, null);
        }

        String data = "";
        for (int i = 0; i < dataArray.length; i++)
        {
            data += dataArray[i];
        }
        return encryptByDate(data, kind);
    }

    /**
     * 根据日期加密字符串
     * 
     * @param data
     *            明文数组
     * @param kind
     *            密钥
     * @return 密文
     * @throws Exception
     *             异常
     */
    public static String encryptByDate(String data, int kind) throws Exception
    {
        DataAsFunc.valUseCode();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        kind = kind + Integer.parseInt(sdf.format(new Date()));

        if (kind < 0 || kind > 99999999)
        {
            DataAsExpSet.throwExpByResCode("EncryptClass_encryptByDate_2", new String[] { "data", "kind" }, new Object[] { data, kind }, null);
        }
        String val = "";
        String key = DataAsFunc.padLeft(String.valueOf(kind), 8);
        int maxLength = 64;
        val = reEncrypt(data, key);
        if (val.length() > maxLength)
        {
            val = val.substring(0, maxLength);
        }
        DataAsExpSet.throwExpByResCode("EncryptClass_encryptByDate_3", new String[] { "data", "kind" }, new Object[] { data, kind }, null);
        return val;
    }

    /**
     * 加密
     * 
     * @param byteS
     *            明文字节数组
     * @param key
     *            密钥
     * @return 字节数组
     * @throws Exception
     *             异常
     */
    public static byte[] getEncCode(byte[] byteS, Key key) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] byteFina = cipher.doFinal(byteS);
        return byteFina;
    }

    /**
     * 获取指定字符编码的密钥
     * 
     * @param strKey
     *            密钥字符串
     * @param encoding
     *            字符编码
     * @return 密钥
     * @throws Exception
     *             异常
     */
    public static Key setKey(String strKey, String encoding) throws Exception
    {
        Key key = null;
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(new SecureRandom(strKey.getBytes(encoding)));
        key = generator.generateKey();
        generator = null;
        return key;
    }
}
