package com.goldwind.dataaccess.security;

import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 解密操作
 * 
 * @author 曹阳
 *
 */
public class DecryptClass
{
    /**
     * 使用默认密钥解密
     * 
     * @param data
     *            密文
     * @return 明文
     * @throws DataAsException
     *             自定义异常
     */
    public static String decrypt(String data) throws DataAsException
    {
        return decrypt(data, SecurityDef.DECKEY);
    }

    /**
     * 使用指定的密钥进行解密
     * 
     * @param data
     *            密文
     * @param skey
     *            密钥
     * @return 明文
     * @throws DataAsException
     *             自定义异常
     */
    public static String decrypt(String data, String skey) throws DataAsException
    {
        if (skey.isEmpty() || skey.length() > 8)
        {
            DataAsExpSet.throwExpByResCode("DecryptClass_decrypt_1", new String[] { "data", "key" }, new Object[] { data, skey }, null);
        }
        if (skey.length() < 8)
        {
            skey = DataAsFunc.padLeft(skey, 8);
        }
        Decoder base64De = Base64.getDecoder();
        byte[] byteMing = null;
        byte[] byteMi = null;
        String val = "";
        try
        {
            byteMi = base64De.decode(data);
            Key key = EncryptClass.setKey(skey, "UTF8");
            byteMing = getDesCode(byteMi, key);
            val = new String(byteMing, "UTF8");
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DecryptClass_decrypt_2", new String[] { "data", "key" }, new Object[] { data, skey }, exp);
        }
        finally
        {
            base64De = null;
            byteMing = null;
            byteMi = null;
        }
        return val;
    }

    /**
     * 使用指定的密钥进行解密
     * 
     * @param byteD
     *            密文字节数组
     * @param key
     *            密钥
     * @return 明文字节数组
     * @throws Exception
     *             异常
     */
    private static byte[] getDesCode(byte[] byteD, Key key) throws Exception
    {
        Cipher cipher;
        byte[] byteFina = null;
        try
        {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byteFina = cipher.doFinal(byteD);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            cipher = null;
        }
        return byteFina;
    }
}
