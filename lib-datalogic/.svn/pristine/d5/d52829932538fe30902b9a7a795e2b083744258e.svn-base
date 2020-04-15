package com.goldwind.datalogic.utils;

import java.util.HashMap;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.NetCommDef.CommState;

/**
 * 返回信息解析
 *
 * @author 曹阳
 */
public class ReturnParse
{
    /**
     * 判断网络返回数据中是否有成功标志
     *
     * @param netData 消息
     * @return 是否有成功标志
     */
    public static boolean checkSuccessFlg(String netData)
    {
        return netData != null && (netData.indexOf(NetCommDef.NETSUCCEEDFLAG.toLowerCase()) > -1 || netData.indexOf(NetCommDef.NETSUCCEEDFLAG.toUpperCase()) > -1);
    }

    /**
     * 解析向前置要主轮询数据的返回结果
     *
     * @param deviceId 设备id
     * @param netData  消息
     * @return hashmap，包括val,commState
     * @throws DataAsException 自定义异常
     */
    public HashMap<String, Object> parseGetDevMainData(String deviceId, String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<>();
        String[] val = new String[0];
        try
        {
            CommState commState = CommState.Unknown;
            val = DataParse.removeParenth(netData).split(",");

            CommState[] css = CommState.values();
            for (CommState cs : css)
            {
                if (cs.getValue() == Integer.parseInt(val[val.length - 1]))
                {
                    commState = cs;
                }
            }
            returnData.put("val", val);
            returnData.put("commState", commState);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetDevMainData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData; // (d1,d2,……,comstate)
    }

    /**
     * 解析向前置要设备包数据的返回结果
     *
     * @param deviceId 设备id
     * @param netData  消息
     * @return 包数据的返回结果
     * @throws DataAsException 自定义异常
     */
    public String[] parseGetDevPackData(String deviceId, String netData) throws DataAsException
    {
        String[] val = new String[0];
        try
        {
            val = DataParse.removeParenth(netData).split(",");
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetDevPackData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return val; // (d1,d2,……)
    }

    /**
     * 解析向前置要设备数据的返回结果
     *
     * @param deviceId 设备id
     * @param netData  消息
     * @return 设备数据
     * @throws DataAsException 自定义异常
     */
    public String[] parseGetDevData(String deviceId, String netData) throws DataAsException
    {
        String[] val = new String[0];
        try
        {
            val = DataParse.removeParenth(netData).split(",");
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetDevData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return val; // (d1,d2,……)
    }

    /**
     * 解析向前置要设备内存数据的返回结果
     *
     * @param deviceId 设备id
     * @param netData  消息
     * @return 设备内存数据
     * @throws DataAsException 自定义异常
     */
    public String[] parseGetDevCacheData(String deviceId, String netData) throws DataAsException
    {
        String[] val = new String[0];
        try
        {
            val = DataParse.removeParenth(netData).split(",");
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetDevCacheData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return val; // (d1,d2,……)
    }

    /**
     * 解析得到设备故障文件名称的结果
     *
     * @param netData 消息
     * @return 故障文件名称
     * @throws DataAsException 自定义异常
     */
    public String[] parseGetDevFltFName(String netData) throws DataAsException
    {
        String[] val = new String[0];
        try
        {
            val = DataParse.removeParenth(netData).split(",");
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetDevFltFName_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return val; // (filename1,filename2,……)
    }

    /**
     * 解析得到订单编号的结果
     *
     * @param netData 消息
     * @return 订单编号
     */
    public String parseGetPlanOrder(String netData)
    {
        return DataParse.removeParenth(netData).trim();
    }

    /**
     * 解析得到订单数据的结果
     *
     * @param netData 消息
     * @return 订单数据
     * @throws DataAsException 自定义异常
     */
    public String[] parseGetPlanData(String netData) throws DataAsException
    {
        String[] val = new String[0];
        try
        {
            val = DataParse.removeParenth(netData).split(",");
            for (int i = 0; i < val.length; i++)
            {
                val[i] = val[i].trim();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetPlanData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return val; // (d1,d2,……)
    }

    /**
     * 解析得到前置配置文件的结果
     *
     * @param netData 消息
     * @return hashmap，包括sections,keys,values
     * @throws DataAsException 自定义异常
     */
    public HashMap<String, Object> parseGetPreIni(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<>();
        try
        {
            String[] sections;
            String[] keys;
            String[] values;
            String[] list = DataParse.removeParenth(netData).split("\\|");
            sections = new String[list.length];
            keys = new String[list.length];
            values = new String[list.length];
            for (int i = 0; i < list.length; i++)
            {
                String[] arr = list[i].split(",");
                sections[i] = arr[0].trim();
                keys[i] = arr[1].trim();
                values[i] = arr[2].trim();
            }
            returnData.put("sections", sections);
            returnData.put("keys", keys);
            returnData.put("values", values);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("ReturnParse_parseGetPreIni_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 移除网络数据中的错误标志(包括左右括号)
     *
     * @param netData 消息
     * @return 移除后的消息
     */
    public static String removeErrorFlg(String netData)
    {
        netData = DataParse.removeParenth(netData);
        if (netData == null || netData.isEmpty())
        {
            return netData;
        }
        if (netData.length() >= 6 && "error:".equals(netData.substring(0, 6).toLowerCase()))
        {
            return netData.replaceFirst(netData.substring(0, 6), "");
        }
        return netData;
    }

    /**
     * 判断网络数据中是否有错误标志
     *
     * @param netData 消息
     * @return 是否有错误标志
     */
    public static boolean checkErrorFlg(String netData)
    {
        if (netData == null || netData.isEmpty())
        {
            return false;
        }
        if (netData.length() >= 7 && ("(error:".equals(netData.substring(0, 7).toLowerCase()) || "((error".equals(netData.substring(0, 7).toLowerCase())))
        {
            return true;
        }
        return false;
    }

    /**
     * 保信主站 判断网络数据中是否有错误标志
     *
     * @param netData 消息
     * @return 是否有错误标识
     */
    public static boolean checkSubgetErrorFlg(String netData)
    {
        if (netData == null || netData.isEmpty())
        {
            return true;
        }
        if (netData.length() >= 7 && ("(error".equals(netData.substring(0, 6).toLowerCase())))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断网络返回数据是否有完成标志
     *
     * @param netData 消息
     * @return 是否有完成标志
     */
    public static boolean checkFinishFlg(String netData)
    {
        return netData != null && netData.equalsIgnoreCase(NetCommDef.NETFINISHFLAG);
    }

    /**
     * 判断网络返回数据是否有完成标志
     *
     * @param netData 消息
     * @return 是否有完成标志
     */
    public static boolean checkWaitFlg(String netData)
    {
        return netData != null && netData.equalsIgnoreCase(NetCommDef.NETWAITFLAG);
    }

    /**
     * 判断网络返回数据是否以成功标志开头
     *
     * @param netData 消息
     * @return 判断结果
     */
    public static boolean checkSuccessFlgStart(String netData)
    {
        return netData != null && (netData.startsWith(NetCommDef.NETSUCCEEDFLAGSTART.toLowerCase()) || netData.startsWith(NetCommDef.NETSUCCEEDFLAGSTART.toUpperCase()));
    }
}
