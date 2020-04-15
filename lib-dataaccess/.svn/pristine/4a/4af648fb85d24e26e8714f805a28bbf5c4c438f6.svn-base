package com.goldwind.dataaccess;

import org.apache.commons.codec.binary.Base64;

/**
 * 十六进制和byte[]类型相互转化(主要是将数据保存至文件过反向隔离用)
 * 
 * @author 王瑞博
 *  
 */
public class ParseHexAndByte
{   
    /**
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 
     * @param src
     *            byte[] data
     * 
     * @return hex string
     */
    public static String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0)
        {
            return null;
        }
        for (int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     * 
     * @param hexString
     *            the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString)
    {
        if (hexString == null || hexString.equals(""))
        {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++)
        {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | (charToByte(hexChars[pos + 1]) & 0xff));
        }
        return d;
    }

    /**
     * Convert char to byte
     * 
     * @param c
     *            char
     * @return byte
     */
    private static byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * base64字符串转byte[],新方法，此方法比byte[]转16进制省空间
     * 
     * @param base64Str
     *            base64SStr
     * @return byte[]
     */
    public static byte[] base64String2ByteFun(String base64Str)
    {

        return Base64.decodeBase64(base64Str);

    }

    /**
     * byte[]转base64，新方法，此方法比byte[]转16进制省空间
     * 
     * @param b
     *            byte[]
     * @return 字符串
     */
    public static String byte2Base64StringFun(byte[] b)
    {

        return Base64.encodeBase64String(b);

    }

}
