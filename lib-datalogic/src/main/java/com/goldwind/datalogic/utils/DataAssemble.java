package com.goldwind.datalogic.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsDef.SystemPromptType;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.ControlProcessDef.ReturnType;
import com.goldwind.datalogic.utils.NetCommDef.CommState;
import com.goldwind.datalogic.utils.NetCommDef.PlcFileType;
import com.goldwind.datalogic.utils.NetCommDef.ServiceManageType;
import com.goldwind.datalogic.utils.NetCommDef.TableDataOperType;
import com.goldwind.datalogic.utils.NetCommDef.TaskCycType;

/**
 * 数据装配工具
 *
 * @author 曹阳
 */
public class DataAssemble
{

    // 装配网络无方向数据
    /**
     * 随机字节
     */
    private final static char[] randomChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    /**
     * 装配增加服务任务的消息
     *
     * @param userId
     *            用户id
     * @param monitorId
     *            监控机id
     * @param deviceList
     *            设备列表
     * @param proId
     *            端口
     * @param orderType
     *            命令类型
     * @param orderFlg
     *            命令标志
     * @param paramList
     *            参数里诶包
     * @param cycType
     *            任务周期类型
     * @param beginTime
     *            开始时间
     * @return 命令字符串
     */
    public static String addSrvTaskAsm(String userId, String monitorId, String[] deviceList, int proId, int orderType, String orderFlg, String[] paramList, TaskCycType cycType, Date beginTime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        String val = "addtask(" + userId + "|" + monitorId + "|";
        val = dataAsmArray(val, deviceList) + "|" + proId + "|" + orderType + "." + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + cycType.ordinal() + "|" + sdf.format(beginTime) + ")";
        return val;
    }

    /**
     * 装配删除服务任务的消息
     *
     * @param taskId
     *            任务id
     * @param userId
     *            用户id
     * @return 命令字符串
     */
    public static String delSrvTaskAsm(String taskId, String userId)
    {
        return "deltask(" + taskId + "|" + userId + ")";
    }

    /**
     * 装配停止服务任务的消息
     *
     * @param taskId
     *            任务id
     * @param userId
     *            用户id
     * @return 命令字符串
     */
    public static String stopSrvTaskAsm(String taskId, String userId)
    {
        return "stoptask(" + taskId + "|" + userId + ")";
    }

    /**
     * 装配服务历史数据通讯状态的消息
     *
     * @param deviceId
     *            设备id
     * @param state
     *            通讯状态
     * @return 命令字符串
     */
    public static String srvHisDataComStaAsm(int deviceId, CommState state)
    {
        return "(hisdatacomstate|" + deviceId + "|" + state.getValue() + ")";
    }

    /**
     * 装配大数据
     *
     * @param dataName
     *            数据名称
     * @param packCount
     *            包数量
     * @param zipData
     *            是否压缩
     * @return 命令字符串
     */
    public static String largeDataAsm(String dataName, int packCount, boolean zipData)
    {
        String zip = (zipData) ? "1" : "0";
        return "(largedata|" + dataName + "|" + packCount + "|" + zip + ")";
    }

    /**
     * 装配系统提示
     *
     * @param netPromptType
     *            系统提示类型
     * @param descr
     *            描述
     * @return 命令字符串
     */
    public static String systemPromptAsm(SystemPromptType netPromptType, String descr)
    {
        return "(prompt|" + netPromptType.getValue() + "|" + descr + ")";
    }

    /**
     * 装配服务器信息
     *
     * @param ip
     *            ip地址
     * @return 命令字符串
     */
    public static String serverInfoAsm(String ip)
    {
        return "(serverinfo|" + ip + ")";
    }

    /**
     * 装配网络环境测试数据
     *
     * @param length
     *            测试数据长度
     * @return 命令字符串
     * @throws NoSuchAlgorithmException
     *             未定义异常
     */
    public static String netTestDataAsm(int length) throws NoSuchAlgorithmException
    {
        StringBuilder sb = new StringBuilder(length);
        Random rd = SecureRandom.getInstanceStrong();
        for (int i = 0; i < length; i++)
        {
            sb.append(randomChar[rd.nextInt(62)]);
        }
        return "(nettest|" + sb.toString() + ")";
    }

    // 装配网络无方向数据结束

    // 装配网络上行数据

    /**
     * 装配沉积数据
     *
     * @param data
     *            沉积数据
     * @param dataTime
     *            数据时间
     * @return 命令字符串
     */
    public static String sedimentDataAsm(String data, String dataTime)
    {
        return "(sediment|" + dataTime + "|" + data + ")";
    }

    /**
     * 装配日数据
     *
     * @param deviceId
     *            设备id
     * @param datas
     *            日数据
     * @return 命令字符串
     */
    public static String deviceDayDataAsm(String deviceId, String[] datas)
    {
        return "(daydata|" + deviceId + "|" + ArrayOper.arrayToString(datas, ",") + ")";
    }

    /**
     * 组装用户数据
     *
     * @param operType
     *            操作类型
     * @param tableName
     *            表名
     * @param wfId
     *            风场id
     * @param unitList1
     *            用户数据单元数组1
     * @param unitList2
     *            用户数据单元数组2
     * @return 命令字符串
     */
    public static String userDataAsm(TableDataOperType operType, String tableName, String wfId, List<UserDataUnit> unitList1, List<UserDataUnit> unitList2)
    {
        String val = "(userdata|";
        switch (operType) {
            case Ins:
                val += "ins|";
                break;
            case Upd:
                val += "upd|";
                break;
            case Del:
                val += "del|";
                break;
            default:
                break;
        }
        val += tableName + "|" + wfId + "|";

        if (unitList1 != null)
        {
            for (int i = 0; i < unitList1.size(); i++)
            {
                val += unitList1.get(i).toString();
                if (i < unitList1.size() - 1)
                {
                    val += "~";
                }
            }
        }
        val += "|";

        if (unitList2 != null)
        {
            for (int i = 0; i < unitList2.size(); i++)
            {
                val += unitList2.get(i).toString();
                if (i < unitList2.size() - 1)
                {
                    val += "~";
                }
            }
        }
        val += ")";
        return val;
    }

    /**
     * 用户数据转换为上级用户数据
     *
     * @param data
     *            命令字符串
     * @return 转换后的命令字符串
     */
    public static String userDataToUpUserData(String data)
    {
        if (data != null)
        {
            return data.replace("(userdata|", "(upuserdata|");
        }
        else
        {
            return null;
        }
    }

    /**
     * 上级用户数据转换为用户数据
     *
     * @param data
     *            命令字符串
     * @return 转换后的命令字符串
     */
    public static String upUserDataToUserData(String data)
    {
        if (data != null)
        {
            return data.replace("(upuserdata|", "(userdata|");
        }
        else
        {
            return null;
        }
    }

    /**
     * 上级SQL数据转换为SQL数据
     *
     * @param data
     *            命令字符串
     * @return 转换后的命令字符串
     */
    public static String upSqlDataToSqlData(String data)
    {
        if (data != null)
        {
            return data.replace("(upsqldata|", "(sqldata|");
        }
        else
        {
            return null;
        }
    }

    /**
     * 组装上级SQL数据
     *
     * @param tableName
     *            表名称
     * @param wfId
     *            风场id
     * @param sql
     *            SQL语句
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String upSqlDataAsm(String tableName, String wfId, String sql) throws DataAsException
    {
        String val = "";
        try
        {
            NetCommFunc.checkUserData(new String[] { "tableName", "wfId", "sql" });
            String pTableName = (tableName == null) ? "" : tableName;
            String pWfId = (wfId == null) ? "" : wfId;
            val = "(upsqldata|" + pTableName + "|" + pWfId + "|" + sql + ")";
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataAssemble_upSqlDataAsm_1", new String[] { "tableName", "wfId", "sql" }, new Object[] { tableName, wfId, sql }, exp);
        }
        return val;
    }

    // 装配网络上行数据结束

    // 装配网络下行数据

    /**
     * 装配向前置要主轮询数据
     *
     * @param deviceId
     *            设备id
     * @return 命令字符串
     */
    public static String getDevMainDataAsm(int deviceId)
    {
        return "getwman(" + deviceId + ")";
    }

    /**
     * 装配向前置要包数据
     *
     * @param deviceId
     *            设备id
     * @param packName
     *            包名称
     * @param returnType
     *            返回类型
     * @return 命令字符串
     */
    public static String getDevPackDataAsm(int deviceId, String packName, int returnType)
    {
        return "getpackdata(" + packName + "|" + deviceId + "|" + returnType + ")";
    }

    /**
     * 装配向前置要包数据
     *
     * @param deviceId
     *            设备id
     * @param packName
     *            包名称
     * @param returnType
     *            返回类型
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String getDevPackDataAsm(int deviceId, String packName, int returnType, String controlpath)
    {
        return "getpackdata(" + packName + "|" + deviceId + "|" + returnType + "|" + controlpath + ")";
    }

    /**
     * 装备向前置要设备数据
     *
     * @param deviceId
     *            设备id
     * @param packName
     *            包名称
     * @param paths
     *            路径名数组
     * @return 命令字符串
     */
    public static String getDevDataAsm(int deviceId, String packName, String[] paths)
    {
        String val = "getdevicedata(" + packName + "|" + deviceId + "|";
        val = dataAsmArray(val, paths) + ")";
        return val;
    }

    /**
     * 装备向前置要内存数据
     *
     * @param deviceId
     *            设备id
     * @param paths
     *            路径名数组
     * @return 命令字符串
     */
    public static String getDevCacheDataAsm(int deviceId, String[] paths)
    {
        String val = "getcachedata(" + deviceId + "|";
        val = dataAsmArray(val, paths) + ")";
        return val;
    }

    /**
     * 装配得到订单编号的指令,如果设备编号列表为空,表示为单台设备
     *
     * @param deviceIds
     *            设备id数组
     * @param paths
     *            路径名数组
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String getPlanOrderAsm(int[] deviceIds, String[] paths) throws DataAsException
    {
        String val = "getplanorder(";
        try
        {
            String[] orderPaths = null;
            if (deviceIds != null && deviceIds.length > 0)
            {
                orderPaths = new String[deviceIds.length];
                for (int i = 0; i < deviceIds.length; i++)
                {
                    orderPaths[i] = deviceIds[i] + "." + paths[i];
                }
            }
            else
            {
                orderPaths = paths;
            }
            val = dataAsmArray(val, orderPaths) + ")";
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataAssemble_getPlanOrderAsm_1", new String[] { "deviceIds", "paths" }, new Object[] { deviceIds, paths }, exp);
        }
        return val; // GetPlanOrder(wtid.path,wtid.path,wtid.path)
    }

    /**
     * 装配得到订单数据的的指令,单台设备
     *
     * @param deviceId
     *            设备id
     * @param orderNo
     *            订单号
     * @return 命令字符串
     */
    public static String getPlanDataAsm(int deviceId, String orderNo)
    {
        return "getplandata(" + deviceId + "|" + orderNo + ")";
    }

    /**
     * 装配得到订单数据的的指令,多台设备
     *
     * @param planNo
     *            订单号
     * @return 命令字符串
     */
    public static String getPlanDataAsm(String planNo)
    {
        return "getplandatanoid(" + planNo + ")";
    }

    /**
     * 装配设备控制指令的消息（同步无参不拓传）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String devControlOrderAsm(String deviceIds, String orderFlg, String userId, String[] infos, String controlid, ReturnType returntype)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid|
        back = dataAsmArray(back, infos) + "|" + controlid + "|" + returntype.getResult() + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…|controlid|returntype)
    }

    /**
     * 装配设备控制指令的消息（同步无参不拓传，无感知）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devControlOrderAsmNS(String deviceIds, String orderFlg, String userId, String[] infos)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid|
        back = dataAsmArray(back, infos) + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…)
    }

    /**
     * 装配设备控制指令的检查消息（同步无参不拓传，无感知）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devControlOrderAsmTestNS(String deviceIds, String orderFlg, String userId, String[] infos)
    {
        String back = "wtcontrolordertest(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // wtcontrolordertest(wtid|nflg|userid|
        back = dataAsmArray(back, infos) + ")";
        return back;// wtcontrolordertest(wtid|flag|userid|info1,info2…)
    }

    /**
     * 装配设备控制指令的消息（异步无参不拓传）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String devControlOrderUUIDAsm(String deviceIds, String orderFlg, String userId, String[] infos, String uuid, ReturnType returntype)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid)
        back = dataAsmArray(back, infos) + "|" + uuid + "|" + returntype.getResult() + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…|controlid|returntype)
    }

    /**
     * 装配设备控制指令的消息（异步无参不拓传，无感知）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devControlOrderUUIDAsmNS(String deviceIds, String orderFlg, String userId, String[] infos)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid)
        back = dataAsmArray(back, infos) + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…)
    }

    /**
     * 装配设备控制指令的消息（同步无参拓传）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String devControlOrderAsm(String deviceIds, String orderFlg, String userId, String[] infos, String controlid, ReturnType returntype, String controlpath)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid|
        back = dataAsmArray(back, infos) + "|" + controlid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…|controlid|returntype|controlpath)
    }

    /**
     * 装配设备控制指令的消息（异步无参拓传）
     *
     * @param deviceIds
     *            设备id
     * @param orderFlg
     *            命令标志
     * @param userId
     *            用户id
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String devControlOrderUUIDAsm(String deviceIds, String orderFlg, String userId, String[] infos, String uuid, ReturnType returntype, String controlpath)
    {
        String back = "wtcontrolorder(" + deviceIds + "|" + orderFlg + "|" + userId + "|"; // WtControlOrder(wtid|nflg|userid)
        back = dataAsmArray(back, infos) + "|" + uuid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return back;// wtcontrolorder(wtid|flag|userid|info1,info2…|controlid|returntype|controlpath)
    }

    /**
     * 装备设备带参数控制指令的消息(同步有参不拓传)
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderAsm(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos, String controlid, ReturnType returntype)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + "|" + controlid + "|" + returntype.getResult() + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2….|controlid|returntype)
    }

    /**
     * 装备设备带参数控制指令的消息(同步有参不拓传，无感知)
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderAsmNS(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2…)
    }

    /**
     * 装备设备带参数控制指令的检查消息(同步有参不拓传，无感知)
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderAsmTestNS(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos)
    {
        String val = "wtothercontrolordertest(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + ")";
        return val;// wtothercontrolordertest(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2…)
    }

    /**
     * 装备设备带参数控制指令的消息（异步有参不拓传）
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderUUIDAsm(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos, String uuid, ReturnType returntype)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + "|" + uuid + "|" + returntype.getResult() + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2….|controlid|returntype)
    }

    /**
     * 装备设备带参数控制指令的消息（异步有参不拓传，无感知）
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderUUIDAsmNS(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2…)
    }

    /**
     * 装备设备带参数控制指令的消息(同步有参拓传)
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderAsm(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos, String controlid, ReturnType returntype, String controlpath)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + "|" + controlid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2….|controlid|returntype|controlpath)
    }

    /**
     * 装备设备带参数控制指令的消息（异步有参拓传）
     *
     * @param deviceIds
     *            设备id数组
     * @param orderFlg
     *            命令标志
     * @param userId
     *            系统编号.用户编号
     * @param paramList
     *            参数列表
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String devOtherCtrlOrderUUIDAsm(String deviceIds, String orderFlg, String userId, String[] paramList, String[] infos, String uuid, ReturnType returntype, String controlpath)
    {
        String val = "wtothercontrolorder(" + deviceIds + "|" + orderFlg + "|";
        val = dataAsmArray(val, paramList) + "|" + userId + "|";
        val = dataAsmArray(val, infos) + "|" + uuid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return val; // WtOtherControlOrder(sWtid|nFlg|param1,param2….|systemId.userId|info1,info2….|controlid|returntype|controlpath)
    }

    /**
     * 拼接异常发送指令
     *
     * @param wtid
     *            风机ID
     * @param nFlg
     *            指令flg
     * @param userid
     *            用户Id
     * @param result
     *            返回结果
     * @param paramList
     *            参数数组,value或param1,param2
     * @param infos
     *            控制原因
     * @param controlType
     *            控制类型
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 异常发送指令
     */
    public static String tomessageByCtr(int wtid, String nFlg, String userid, String result, String[] paramList, String[] infos, String controlType, String controlid, ReturnType returntype)
    {
        String mes = "(wtcontrol|" + wtid + "|" + nFlg + "|" + userid + "|" + result + "|";
        mes = dataAsmArray(mes, paramList) + "|";
        mes = dataAsmArray(mes, infos) + "|" + controlType + "|" + controlid + "|" + returntype.getResult() + ")";
        return mes;// (wtcontrol|wtid|flag|userid|result|value或param1,param2…|info1,info2…|controltype|controlid|returntype)
    }

    /**
     * 拼接异常发送指令（无感知）
     *
     * @param wtid
     *            风机ID
     * @param nFlg
     *            指令flg
     * @param userid
     *            用户Id
     * @param result
     *            返回结果
     * @param paramList
     *            参数数组,value或param1,param2
     * @param infos
     *            控制原因
     * @param controlType
     *            控制类型
     * @return 异常发送指令
     */
    public static String tomessageByCtr(int wtid, String nFlg, String userid, String result, String[] paramList, String[] infos, String controlType)
    {
        String mes = "(wtcontrol|" + wtid + "|" + nFlg + "|" + userid + "|" + result + "|";
        mes = dataAsmArray(mes, paramList) + "|";
        mes = dataAsmArray(mes, infos) + "|" + controlType + ")";
        return mes;// (wtcontrol|wtid|flag|userid|result|value或param1,param2…|info1,info2…|controltype|controlid|returntype)
    }

    /**
     * 拼接异常发送指令(无感知)
     *
     * @param wtid
     *            风机ID
     * @param nFlg
     *            指令flg
     * @param userid
     *            用户Id
     * @param result
     *            返回结果
     * @param paramList
     *            参数数组,value或param1,param2
     * @param infos
     *            控制原因
     * @param controlType
     *            控制类型
     * @return 异常发送指令
     */
    public static String tomessageByCtrNS(int wtid, String nFlg, String userid, String result, String[] paramList, String[] infos, String controlType)
    {
        String mes = "(wtcontrol|" + wtid + "|" + nFlg + "|" + userid + "|" + result + "|";
        mes = dataAsmArray(mes, paramList) + "|";
        mes = dataAsmArray(mes, infos) + "|" + controlType + ")";
        return mes;// (wtcontrol|wtid|flag|userid|result|value或param1,param2…|info1,info2…|controltype)
    }

    /**
     * 装配更改参数的消息(同步赋值不拓传)
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String setDevDataValuesAsm(String deviceIds, String path, String value, String userId, String controlType, String[] infos, String controlid, ReturnType returntype)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + "|" + controlid + "|" + returntype.getResult() + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…|controlid|returntype)
    }

    /**
     * 装配更改参数的消息(同步赋值不拓传，无感知)
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String setDevDataValuesAsmNS(String deviceIds, String path, String value, String userId, String controlType, String[] infos)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…)
    }

    /**
     * 装配更改参数的检查消息(同步赋值不拓传，无感知)
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String setDevDataValuesAsmTestNS(String deviceIds, String path, String value, String userId, String controlType, String[] infos)
    {
        String back = "setdatavaluestest(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        back = dataAsmArray(back, infos) + ")";
        return back;// setdatavaluestest(wtid|path|value|userid|controltype|info1,info2…)
    }

    /**
     * 装配更改参数的消息（异步赋值不拓传）
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @return 命令字符串
     */
    public static String setDevDataValuesUUIDAsm(String deviceIds, String path, String value, String userId, String controlType, String[] infos, String uuid, ReturnType returntype)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + "|" + uuid + "|" + returntype.getResult() + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…|controlid|returntype)
    }

    /**
     * 装配更改参数的消息（异步赋值不拓传，无感知）
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String setDevDataValuesUUIDAsmNS(String deviceIds, String path, String value, String userId, String controlType, String[] infos)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…)
    }

    /**
     * 装配更改参数的消息(同步赋值拓传)
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @param controlid
     *            控制唯一码
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String setDevDataValuesAsm(String deviceIds, String path, String value, String userId, String controlType, String[] infos, String controlid, ReturnType returntype,
            String controlpath)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + "|" + controlid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…|controlid|returntype|controlpath)
    }

    /**
     * 装配更改参数的消息（异步赋值拓传）
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @param uuid
     *            唯一标识
     * @param returntype
     *            结果返回类型，0代表同步返回，1代表异步返回
     * @param controlpath
     *            控制路径
     * @return 命令字符串
     */
    public static String setDevDataValuesUUIDAsm(String deviceIds, String path, String value, String userId, String controlType, String[] infos, String uuid, ReturnType returntype, String controlpath)
    {
        String back = "setdatavalues(" + deviceIds + "|" + path + "|" + value + "|" + userId + "|" + controlType + "|";
        // back = dataAsmArray(back, params) + "|";
        back = dataAsmArray(back, infos) + "|" + uuid + "|" + returntype.getResult() + "|" + controlpath + ")";
        return back; // setdatavalues(wtid|path|value|userid|controltype|info1,info2…|controlid|returntype|controlpath)
    }

    /**
     * 设定设备多个参数
     *
     * @param deviceIds
     *            设备id
     * @param paths
     *            路径数组
     * @param values
     *            值数组
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String setDevMultDataValAsm(String deviceIds, String[] paths, String[] values) throws DataAsException
    {
        if (paths == null || values == null || paths.length == 0 || paths.length != values.length)
        {
            DataAsExpSet.throwExpByResCode("DataAssemble_setDevMultDataValuesAsm_1", new String[] { "deviceIds", "paths", "values" }, new Object[] { deviceIds, paths, values }, null);
        }

        String val = "setmultdatavalues(" + deviceIds + "|";
        for (int i = 0; i < paths.length; i++)
        {
            val += paths[i] + "=" + values[i];
            if (i < paths.length - 1)
            {
                val += ",";
            }
        }
        val += ")"; // Setmultdatavalues(wtid|IEC路径1=设定值1,IEC路径2=数据2,……)
        return val;
    }

    /**
     * 装配其它更改参数的消息
     *
     * @param deviceIds
     *            设备id
     * @param path
     *            路径
     * @param flg
     *            标志
     * @param values
     *            值数组
     * @return 命令字符串
     */
    public static String setDevOthDataValAsm(String deviceIds, String path, String flg, String[] values)
    {
        String val = "setotherdatavalues(" + deviceIds + "|" + path + "|" + flg + "|";
        val = dataAsmArray(val, values) + ")";
        return val; // SetOtherDataValues(wtid|path|nflg|value1,values2,value3…)
    }

    /**
     * 装配设置设备时间的消息
     *
     * @param deviceId
     *            设备id
     * @return 命令字符串
     */
    public static String setDevTimeAsm(int deviceId)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        String time = sdf.format(new Date());
        String year = time.substring(0, 4);
        String month = time.substring(5, 2);
        String day = time.substring(8, 2);
        String hour = time.substring(11, 2);
        String minute = time.substring(14, 2);
        String second = time.substring(17, 2); // SetWtDateTime(wtid|year|month|day|hour|minute|second)
        return "setwtdatetime(" + deviceId + "|" + year + "|" + month + "|" + day + "|" + hour + "|" + minute + "|" + second + ")";
    }

    /**
     * 装配设置前置管理的所有设备的时间的消息
     *
     * @return 命令字符串
     */
    public static String setDevTimeAsm()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        String time = sdf.format(new Date());
        String year = time.substring(0, 4);
        String month = time.substring(5, 2);
        String day = time.substring(8, 2);
        String hour = time.substring(11, 2);
        String minute = time.substring(14, 2);
        String second = time.substring(17, 2); // SetWtDateTime(year|month|day|hour|minute|second)
        return "setwtdatetime(" + year + "|" + month + "|" + day + "|" + hour + "|" + minute + "|" + second + ")";
    }

    /**
     * 装配更改前置数据库配置的消息
     *
     * @param sql
     *            SQL语句
     * @return 命令字符串
     */
    public static String writePreConfigAsm(String sql)
    {
        return "wirteconfig(" + sql + ")"; // WirteConfig(sql)
    }

    /**
     * 装配更改前置文件配置的消息
     *
     * @param section
     *            配置文件分块
     * @param key
     *            键
     * @param value
     *            值
     * @return 命令字符串
     */
    public static String writePreIniAsm(String section, String key, String value)
    {
        return "writeini(" + section + "|" + key + "|" + value + ")"; // WriteIni(section| key|value)
    }

    /**
     * 装配得到plc文件名称的消息
     *
     * @param deviceId
     *            设备id
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @param plcFileType
     *            plc文件类型
     * @param plcIp
     *            plc地址
     * @param plcUser
     *            plc用户名
     * @param plcPassword
     *            plc密码
     * @param serviceIp
     *            服务ip
     * @param servicePort
     *            服务端口
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String getPlcFileNameAsm(String deviceId, Date beginDate, Date endDate, PlcFileType plcFileType, String plcIp, String plcUser, String plcPassword, String serviceIp, int servicePort)
            throws DataAsException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATEFORMATSTR);
        String val = "getplcfilename(" + deviceId + "|" + sdf.format(beginDate) + "|" + sdf.format(endDate) + "|" + plcFileType.getValue() + "|" + plcIp + "|" + plcUser + "|" + plcPassword + ")";
        return downDataAsmIp(val, serviceIp, servicePort); // getplcfilename(wtid|begindate|filetype|ip|user|password)
    }

    /**
     * 装配得到plc文件数据的消息
     *
     * @param deviceId
     *            设备id
     * @param fileName
     *            文件名称
     * @param plcIp
     *            plc地址
     * @param plcUser
     *            plc用户名
     * @param plcPassword
     *            plc密码
     * @param serviceIp
     *            服务ip
     * @param servicePort
     *            服务端口
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String getPlcFileDataAsm(String deviceId, String fileName, String plcIp, String plcUser, String plcPassword, String serviceIp, int servicePort) throws DataAsException
    {
        String val = "getplcfiledata(" + deviceId + "|" + fileName + "|" + plcIp + "|" + plcUser + "|" + plcPassword + ")";
        return downDataAsmIp(val, serviceIp, servicePort); // getplcfiledata(wtid|filename|ip|user|password)
    }

    /**
     * 装配重启前置的消息
     *
     * @param startType
     *            启动类型
     * @param paramList
     *            参数列表
     * @return 命令字符串
     */
    public static String restartPreAsm(String startType, String[] paramList)
    {
        String val = "resetservice(" + startType; // resetservice(nflg)
        if (paramList != null && paramList.length > 0)
        {
            for (int i = 0; i < paramList.length; i++)
            {
                val += "|" + paramList[i];
            }
        }
        val += ")";
        return val;
    }

    /**
     * 装配得到前置升级文件路径的信息
     *
     * @return 命令字符串
     */
    public static String getPreUpgradeDirAsm()
    {
        return "getsharedir()";
    }

    /**
     * 装配读取前置文件配置的消息
     *
     * @param section
     *            配置文件分块
     * @param key
     *            键
     * @return 命令字符串
     */
    public static String readPreIniAsm(String section, String key)
    {
        return "readini(" + section + "|" + key + ")"; // readini(section|key)
    }

    /**
     * 读取前置文件配置所有数据的消息
     *
     * @param fileName
     *            文件名
     * @return 命令字符串
     */
    public static String getPreIniAsm(String fileName)
    {
        return "getini(" + fileName + ")";
    }

    /**
     * 装配要求前置拷贝程序文件的消息
     *
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名
     * @return 命令字符串
     */
    public static String preFileCopyAsm(String filePath, String fileName)
    {
        return "filecopy(" + filePath + "|" + fileName + ")"; // filecopy(共享路径|文件名称)
    }

    /**
     * 装配要求前置更新配置库文件的消息
     *
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名
     * @return 命令字符串
     */
    public static String preAccessUpdateAsm(String filePath, String fileName)
    {
        return "access(" + filePath + "|" + fileName + ")"; // access(共享路径|文件名称)
    }

    /**
     * 装配读取前置存储文件的消息
     *
     * @param rowNum
     *            行号
     * @return 命令字符串
     */
    public static String readPreFileDataAsm(int rowNum)
    {
        return "rptdataread(" + rowNum + ")"; // rptdataread(行号)
    }

    /**
     * 装配删除前置存储文件的消息
     *
     * @param rowNum
     *            行号
     * @return 命令字符串
     */
    public static String delPreFileDataAsm(int rowNum)
    {
        return "rptdatadel(" + rowNum + ")"; // rptdatadel(行号)
    }

    /**
     * 装配读取前置存储文件行数的消息
     *
     * @return 行数
     */
    public static String getPreFileRowCount()
    {
        return "rptdatareadcount()"; // rptdatareadcount()
    }

    /**
     * 装配读取前置实时数据的消息
     *
     * @param rowNum
     *            行号
     * @return 命令字符串
     */
    public static String readPreRealtimeData(int rowNum)
    {
        return "realtimedataread(" + rowNum + ")"; // realtimedataread(行号)
    }

    /**
     * 装配删除前置实时数据的消息
     *
     * @param rowNum
     *            行号
     * @return 命令字符串
     */
    public static String delPreRealtimeData(int rowNum)
    {
        return "realtimedatadel(" + rowNum + ")"; // realtimedatadel(行号)
    }

    /**
     * 装配读取前置实时数据行数的消息
     *
     * @return 命令字符串
     */
    public static String getPreRTRowCount()
    {
        return "realtimedatareadcount()"; // realtimedatareadcount()
    }

    /**
     * 装配读取前置磁盘空间的消息
     *
     * @param diskName
     *            盘符名称
     * @return 命令字符串
     */
    public static String getPreDiskSpaceAsm(String diskName)
    {
        return "getdiskspace(" + diskName + ")";
    }

    /**
     * 装配读取前置cpu的消息
     *
     * @return 命令字符串
     */
    public static String getPreCpuAsm()
    {
        return "getcpu()";
    }

    /**
     * 装配读取前置内存信息的消息
     *
     * @return 命令字符串
     */
    public static String getPreMemoryAsm()
    {
        return "getmemory()";
    }

    /**
     * 装配读取前置版本的消息
     *
     * @return 命令字符串
     */
    public static String getPreVersionAsm()
    {
        return "getversion()";
    }

    /**
     * 装配读取前置时间的消息
     *
     * @return 命令字符串
     */
    public static String getPreTimeAsm()
    {
        return "getdatetime()";
    }

    /**
     * 装配测试前置状态的消息
     *
     * @return 命令字符串
     */
    public static String preTestAsm()
    {
        return "comtest()";
    }

    /**
     * 装配测试服务状态的消息
     *
     * @param serviceIp
     *            服务ip
     * @param servicePort
     *            服务端口
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String serviceTestAsm(String serviceIp, int servicePort) throws DataAsException
    {
        String val = "servicetest()";
        return downDataAsmIp(val, serviceIp, servicePort);
    }

    /**
     * 装配服务管理的消息
     *
     * @param serviceIp
     *            服务ip
     * @param servicePort
     *            服务端口
     * @param operType
     *            服务管理类型
     * @param paramList
     *            参数列表
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String serviceManageAsm(String serviceIp, int servicePort, ServiceManageType operType, String[] paramList) throws DataAsException
    {
        String val = "servicemanage(" + operType.getValue(); // servicemanage(type|param1,param2)
        val += "|";
        if (paramList != null && paramList.length > 0)
        {
            val += ArrayOper.arrayToString(paramList, ",");
        }
        val += ")";
        return downDataAsmIp(val, serviceIp, servicePort);
    }

    /**
     * 装配向设备发送通用指令的消息
     *
     * @param deviceId
     *            设备id
     * @param userId
     *            用户id
     * @param orderName
     *            命令名称
     * @param orderParams
     *            命令参数
     * @return 命令字符串
     */
    public static String sendDevCommonOrder(int deviceId, String userId, String orderName, String[] orderParams)
    {
        String val = orderName + "(" + deviceId + "|" + userId + "|";
        val = dataAsmArray(val, orderParams) + ")";
        return val; // ordername(wtid|userid|param1, param2,param3...)
    }

    /**
     * 装配向前置发送通用指令的消息
     *
     * @param userId
     *            用户id
     * @param orderName
     *            命令名称
     * @param orderParams
     *            命令参数
     * @return 命令字符串
     */
    public static String sendPreCommonOrder(String userId, String orderName, String[] orderParams)
    {
        String val = orderName + "(" + userId + "|";
        val = dataAsmArray(val, orderParams) + ")";
        return val; // ordername(userid|param1,paramw2,param3...)
    }

    /**
     * 装配发送文件的指令
     *
     * @param sendName
     *            文件名称
     * @param packCount
     *            包大小
     * @param md5
     *            文件MD5
     * @return 命令字符串
     */
    public static String sendFileOrderAsm(String sendName, int packCount, String md5)
    {
        return "(sendfile|" + sendName.trim() + "|" + packCount + "|" + md5 + ")";
    }

    /**
     * 装配发送文件的指令(通用)
     *
     * @param sendType
     *            文件类型
     * @param sendName
     *            文件名称
     * @param dataTime
     *            召唤时间
     * @param packCount
     *            包大小
     * @param md5
     *            文件md5码
     * @return 返回拼接的字符串
     */
    public static String saveFileOrderAsm(String sendType, String sendName, String dataTime, int packCount, String md5)
    {
        return "(savefile|" + sendType.trim() + "|" + sendName.trim() + "|" + dataTime.trim() + "|" + packCount + "|" + md5 + ")";
    }

    /**
     * 拼装下载文件数据（通用）
     *
     * @param fileType
     *            文件类型
     * @param fileName
     *            文件名称
     * @param dataTime
     *            时间
     * @return 拼接字符串
     */
    public static String downloadFile(String fileType, String fileName, String dataTime)
    {
        return "(downloadfile|" + fileType.trim() + "|" + fileName.trim() + "|" + dataTime.trim() + ")";
    }

    /**
     * 组播指令到Socket指令
     *
     * @param order
     *            组播指令
     * @return socket指令
     */
    public static String groupOrdToSocketOrd(String order)
    {
        return order.substring(NetCommDef.BROADCASTORDFLAG.length());
    }

    /**
     * socket指令到组播指令
     *
     * @param order
     *            socket指令
     * @return 组播指令
     */
    public static String socketOrdToGroupOrd(String order)
    {
        return NetCommDef.BROADCASTORDFLAG + order;
    }

    // 装配网络下行数据结束

    /**
     * 装配数组
     *
     * @param netData
     *            字符串
     * @param arrayData
     *            字符串数组
     * @return 字符串拼接字符串数组
     */
    public static String dataAsmArray(String netData, String[] arrayData)
    {
        String val = netData;
        if (arrayData != null)
        {
            StringJoiner sj = new StringJoiner(",");
            for (String s : arrayData)
            {
                sj.add(s);
            }
            val += sj;
        }
        return val;

    }

    /**
     * 装配下行数据的服务ip
     *
     * @param netData
     *            消息数据
     * @param serviceIp
     *            服务ip
     * @param servicePort
     *            服务端口
     * @return 命令字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String downDataAsmIp(String netData, String serviceIp, int servicePort) throws DataAsException
    {
        String val = "";
        try
        {
            int index = serviceIp.indexOf(',');
            if (index > 0)
            {
                val = netData + "(" + serviceIp.substring(index + 1).trim() + ":" + servicePort + ")";
            }
            else
            {
                val = netData;
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataAssemble_downDataAsmIp_1", new String[] { "netData", "serviceIp", "servicePort" }, new Object[] { netData, serviceIp, servicePort }, exp);
        }
        return val;
    }

    /**
     * 装配历史瞬态数据查询指令(用于分析-自定义趋势分析-短趋势)
     *
     * @param begintime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param points
     *            测点集合
     * @return 指令
     */
    public static String analysisRealTimeOrder(Date begintime, Date endtime, List<String> points)
    {
        return analysisDataOrder(begintime, endtime, points, "realtimedata");
    }

    /**
     * 装配历史瞬态数据查询指令(用于分析-自定义趋势分析-短趋势)
     *
     * @param begintime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param points
     *            测点集合
     * @return 指令
     */
    public static String analysisOneMinOrder(Date begintime, Date endtime, List<String> points)
    {
        return analysisDataOrder(begintime, endtime, points, "onemindata");
    }

    /**
     * 装配历史瞬态数据查询指令(用于分析-自定义趋势分析-短趋势)
     *
     * @param begintime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param points
     *            测点集合
     * @return 指令
     */
    public static String analysisDataOrder(Date begintime, Date endtime, List<String> points, String dataType)
    {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = sdf.format(begintime);
        String endTime = sdf.format(endtime);
        builder.append("(getdspsrvdata|" + dataType + "|");
        builder.append(beginTime + "|");
        builder.append(endTime + "|");
        for (String point : points)
        {
            builder.append(point + "|");
        }
        String order = builder.substring(0, builder.length() - 1);
        order += ")";
        return order;
    }

    /**
     * EBA可排除项配置操作指令拼接方法
     *
     * @param ebatype
     *            EBA操作类型 0:insert 1:update 2:delete
     * @param cnfs
     *            配置字符串集合
     * @return 指令
     */
    public static String ebaExcludOrder(int ebatype, List<String> cnfs)
    {
        StringBuilder builder = new StringBuilder();
        String type = null;
        switch (ebatype) {
            case 0:
                type = "insert";
                break;
            case 1:
                type = "update";
                break;
            case 2:
                type = "delete";
                break;
            default:
        }
        builder.append("(excludcnf|" + type + "||");
        StringBuilder cnfbuilder = new StringBuilder();
        for (String cnf : cnfs)
        {
            cnfbuilder.append("{");
            cnfbuilder.append(cnf);
            cnfbuilder.append("},");
        }
        cnfbuilder.deleteCharAt(cnfbuilder.length() - 1);
        builder.append(cnfbuilder + ")");
        return builder.toString();
    }

    /**
     * 装配保信主站相关数据
     *
     * @param deviceId
     *            设备id
     * @param packName
     *            指令类型
     * @param returnType
     *            返回类型
     * @return 命令字符串
     */
    public static String getClassDataAsm(int deviceId, String packName, int returnType)
    {
        return "getclassdata(" + deviceId + "|" + packName + "|" + returnType + ")";
    }

    /**
     * 装载召唤波形文件列表
     *
     * @param wtid
     *            设备id
     * @param begintime
     *            开始时间
     * @param endtime
     *            结束时间
     * @return 命令字符串
     */
    public static String getWavefileList(String wtid, String begintime, String endtime)
    {
        return "getwavefilelist(" + wtid + "|" + begintime + "|" + endtime + ")";
    }

    /**
     * 装载召唤波形文件列表
     *
     * @param wtid
     *            设备id
     * @param filename
     *            文件名
     * @return 命令字符串
     */
    public static String getWavefileData(String wtid, String filename)
    {
        return "getwavefiledata(" + wtid + "|" + filename + ")";
    }

    /**
     * runlog转warnlog
     *
     * @param runlog
     *            运行日志
     * @return 警告日志
     * @throws DataAsException
     *             自定义异常
     */
    public static String runlog2warnlog(String runlog) throws DataAsException
    {
        HashMap<String, Object> hm = DataParse.parseRunLog(runlog);
        if (!hm.get("systemId").equals("3"))
        {
            return null;
        }
        StringBuilder warnlog = new StringBuilder("(warnlog|");
        warnlog.append(hm.get("systemId")).append("|");
        warnlog.append("0|");// levelid,默认提示
        warnlog.append(hm.get("recTime")).append("|");
        warnlog.append(hm.get("wfId")).append("|");
        warnlog.append(hm.get("objectId")).append("|");
        warnlog.append(hm.get("logCode")).append("|");
        warnlog.append(hm.get("guid")).append("|");// warnid
        warnlog.append("0||");// flag，默认事件类告警和info
        warnlog.append(hm.get("typeId")).append(")");
        return warnlog.toString();
    }

    /**
     * warnlog转runlog
     *
     * @param warnlog
     *            警告日志
     * @return 运行日志
     * @throws DataAsException
     *             自定义异常
     */
    public static String warnlog2runlog(String warnlog) throws DataAsException
    {
        HashMap<String, Object> hm = DataParse.parseWarnLog(warnlog);
        if (!hm.get("systemid").equals("3"))
        {
            return null;
        }
        StringBuilder runlog = new StringBuilder("(runlog|");
        runlog.append(hm.get("systemid")).append("|");
        runlog.append(hm.get("typeid")).append("|");
        runlog.append(hm.get("rectime")).append("|");
        runlog.append(hm.get("wfid")).append("|");
        runlog.append(hm.get("objectid")).append("|");
        runlog.append(hm.get("logcode")).append("|");
        runlog.append(hm.get("warnid")).append("|)");// guid
        return runlog.toString();

    }

    /**
     * 装配更改参数的消息 setprotectdata
     *
     * @param deviceIds
     *            设备id
     * @param name
     *            名称
     * @param value
     *            值
     * @param userId
     *            系统编号.用户编号
     * @param controlType
     *            控制类型
     * @param infos
     *            控制原因
     * @return 命令字符串
     */
    public static String setProtectDataAsm(String deviceIds, String name, String value, String userId, String controlType, String[] infos)
    {
        String back = "setprotectdata(" + deviceIds + "|" + name + "|" + value + "|" + userId + "|" + controlType + "|";
        back = dataAsmArray(back, infos) + ")";
        return back; // setprotectdata(wtid|name|value|userid|controltype|info1,info2…)
    }

    /**
     * 装载保信主站召唤信息方法getprotectdata
     *
     * @param wtid
     *            设备id
     * @param getType
     *            读取类型
     * @param groupNo
     *            组号
     * @param groupType
     *            组类型
     * @return 命令字符串
     */
    public static String getprotectdata(String wtid, String getType, String groupNo, String groupType)
    {
        return "getprotectdata(" + wtid + "|" + getType + "|" + groupNo + "|" + groupType + ")";
        // getprotectdata(wtid|getType|groupNo|groupType)
    }

    /**
     * 装载控制指令方法setprotectdata
     *
     * @param setid
     *            控制编号，标识控制的唯一性
     * @param userid
     *            控制的用户编号
     * @param settype
     *            控制类型，0 修改条目、1000 装置复归，1001广播对时
     * @param setmodel
     *            控制方式，0 预置、1取消预置、2执行
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            cpu编号
     * @param regionid
     *            定值区号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型，同召唤指令
     * @param itemid
     *            条目编号
     * @param valuetype
     *            值类型，1代表字符串、3代表短整形、7代表短浮点、9代表双点信息
     * @param value
     *            控制值
     * @param infos
     *            关联信息
     * @param uuid
     *            同步的情况下，红色为空;异步的情况下，需要返回uuid（null或空，则不拼）
     * @param controlpath
     *            拓传路径（null或空，则不拼）
     * @return 命令字符串
     */
    public static String setprotectdata(String setid, String userid, String settype, String setmodel, String statid, String devid, String cpuid, String regionid, String groupid, String grouptype,
            String itemid, String valuetype, String value, String[] infos, String uuid, String controlpath)
    {
        String back = "setprotectdata(" + setid + "|" + userid + "|" + settype + "|" + setmodel + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|"
                + itemid + "|" + valuetype + "|" + value + "|";
        back = dataAsmArray(back, infos);
        if (null != uuid && !uuid.isEmpty())
        {
            back = back + "|" + uuid;
        }
        if (null != controlpath && !controlpath.isEmpty())
        {
            back = back + "|" + controlpath;
        }
        return back + ")";
    }

    /**
     * 装载控制指令方法setprotectdata(无感知)
     *
     * @param setid
     *            控制编号，标识控制的唯一性
     * @param userid
     *            控制的用户编号
     * @param settype
     *            控制类型，0 修改条目、1000 装置复归，1001广播对时
     * @param setmodel
     *            控制方式，0 预置、1取消预置、2执行
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            cpu编号
     * @param regionid
     *            定值区号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型，同召唤指令
     * @param itemid
     *            条目编号
     * @param valuetype
     *            值类型，1代表字符串、3代表短整形、7代表短浮点、9代表双点信息
     * @param value
     *            控制值
     * @param infos
     *            关联信息
     * @return 命令字符串
     */
    public static String setprotectdataNS(String setid, String userid, String settype, String setmodel, String statid, String devid, String cpuid, String regionid, String groupid, String grouptype,
            String itemid, String valuetype, String value, String[] infos)
    {
        String back = "setprotectdata(" + setid + "|" + userid + "|" + settype + "|" + setmodel + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|"
                + itemid + "|" + valuetype + "|" + value + "|";
        back = dataAsmArray(back, infos) + ")";
        return back;
    }

    /**
     * 装载控制日志方法protectsetlog
     *
     * @param setid
     *            控制编号，标识控制的唯一性
     * @param userid
     *            控制的用户编号
     * @param rectime
     *            日志时间
     * @param msg
     *            控制结果
     * @param logtype
     *            日志类型，0 软适配日志、1 前置日志
     * @param settype
     *            控制类型，0 修改条目、1000 装置复归，1001广播对时
     * @param setmodel
     *            控制方式，0 预置、1取消预置、2执行
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            cpu编号
     * @param regionid
     *            定值区号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型，同召唤指令
     * @param itemid
     *            条目编号
     * @param valuetype
     *            值类型，1代表字符串、3代表短整形、7代表短浮点、9代表双点信息
     * @param value
     *            控制值
     * @param infos
     *            关联信息
     * @param ordernum
     *            日志编号
     * @param iscache
     *            是否缓存
     * @param protocolid
     *            协议编号
     * @return 命令字符串
     */
    public static String protectsetlog(String setid, String userid, String rectime, String msg, String logtype, String settype, String setmodel, String statid, String devid, String cpuid,
            String regionid, String groupid, String grouptype, String itemid, String valuetype, String value, String[] infos, String ordernum, String iscache, String protocolid)
    {
        String back = "(protectsetlog|" + setid + "|" + userid + "|" + rectime + "|" + msg + "|" + logtype + "|" + settype + "|" + setmodel + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid
                + "|" + groupid + "|" + grouptype + "|" + itemid + "|" + valuetype + "|" + value + "|";
        back = dataAsmArray(back, infos) + "|" + ordernum + "|" + iscache + "|" + protocolid + ")";
        return back;
    }

    /**
     * 装载控制日志方法protectsetlog(无感知)
     *
     * @param setid
     *            控制编号，标识控制的唯一性
     * @param userid
     *            控制的用户编号
     * @param rectime
     *            日志时间
     * @param msg
     *            控制结果
     * @param logtype
     *            日志类型，0 软适配日志、1 前置日志
     * @param settype
     *            控制类型，0 修改条目、1000 装置复归，1001广播对时
     * @param setmodel
     *            控制方式，0 预置、1取消预置、2执行
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            cpu编号
     * @param regionid
     *            定值区号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型，同召唤指令
     * @param itemid
     *            条目编号
     * @param valuetype
     *            值类型，1代表字符串、3代表短整形、7代表短浮点、9代表双点信息
     * @param value
     *            控制值
     * @param infos
     *            关联信息
     * @param uuid
     *            uuid
     * @param ordernum
     *            日志编号
     * @param iscache
     *            是否缓存
     * @param protocolid
     *            协议编号
     * @return 命令字符串
     */
    public static String protectsetlogNS(String setid, String userid, String rectime, String msg, String logtype, String settype, String setmodel, String statid, String devid, String cpuid,
            String regionid, String groupid, String grouptype, String itemid, String valuetype, String value, String[] infos, String uuid, String ordernum, String iscache, String protocolid)
    {
        String back = "(protectsetlog|" + setid + "|" + userid + "|" + rectime + "|" + msg + "|" + logtype + "|" + settype + "|" + setmodel + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid
                + "|" + groupid + "|" + grouptype + "|" + itemid + "|" + valuetype + "|" + value + "|";
        back = dataAsmArray(back, infos) + "|" + uuid + "|" + ordernum + "|" + iscache + "|" + protocolid + ")";
        return back;
    }

    /**
     * falutdata转warnlog数据
     *
     * @param falutData
     *            故障数据
     * @param rectime
     *            时间
     * @param wfid
     *            风场id
     * @return warnlog数据
     * @throws DataAsException
     *             异常
     */
    public static String falutdata2warnlog(String falutData, String rectime, String wfid) throws DataAsException
    {
        // String warnlog = "";

        HashMap<String, Object> hm = DataParse.parseFaultData(falutData);
        StringBuilder warnlog = new StringBuilder("(warnlog|1|2|");// 故障的systemid为风机，设置为1，级别为故障，设置为2
        warnlog.append(rectime + "|" + wfid + "|");
        warnlog.append(hm.get("wtid")).append("|");
        warnlog.append(hm.get("faultData")).append("|");
        warnlog.append(hm.get("id")).append("|");
        warnlog.append("1||)");// 状态类告警，设置为1
        return warnlog.toString();
    }

    /**
     * alarmdata转warnlog数据
     *
     * @param alarmData
     *            告警数据
     * @param rectime
     *            时间
     * @param wfid
     *            风场id
     * @return warnlog数据
     * @throws DataAsException
     *             异常
     */
    public static String alarmdata2warnlog(String alarmData, String rectime, String wfid) throws DataAsException
    {
        // String warnlog = "";

        HashMap<String, Object> hm = DataParse.parseAlarmData(alarmData);
        StringBuilder warnlog = new StringBuilder("(warnlog|1|2|");// 故障的systemid为风机，设置为1，级别为故障，设置为2
        warnlog.append(rectime + "|" + wfid + "|");
        warnlog.append(hm.get("wtid")).append("|");
        warnlog.append(hm.get("alarmData")).append("|");
        warnlog.append(hm.get("id")).append("|");// uuid
        warnlog.append("1||)");// 状态类告警，设置为1
        return warnlog.toString();
    }

    /**
     * eventdata转warnlog数据
     *
     * @param eventdata
     *            告警数据
     * @param rectime
     *            时间
     * @param wfid
     *            风场id
     * @return warnlog数据
     * @throws DataAsException
     *             异常
     */
    public static String eventdata2warnlog(String eventdata, String rectime, String wfid) throws DataAsException
    {

        HashMap<String, Object> hm = DataParse.parseEventData(eventdata);
        StringBuilder warnlog = new StringBuilder("(warnlog|1|0|");// 事件的systemid为风机，设置为1，级别为事件，设置为0？？？？？
        warnlog.append(rectime + "|" + wfid + "|");
        warnlog.append(hm.get("wtid")).append("|");
        warnlog.append(hm.get("eventData")).append("|");
        warnlog.append(hm.get("id")).append("|");// uuid
        warnlog.append("0||)");// 事件类告警，设置为0？？？？
        return warnlog.toString();
    }

    /**
     * 装配保信主站 召唤指令(包含拓传)
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户编号
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @param controlpath
     *            拓传路径
     * @return 报文
     * @throws DataAsException
     *             异常
     */
    public static String getprotectdata(int calltype, String callid, String userid, String statid, String devid, String cpuid, String regionid, String groupid, int grouptype, String[] infos,
            int returntype, int dataformat, String savefile, String controlpath) throws DataAsException
    {
        String back = "getprotectdata(" + calltype + "|" + callid + "|" + userid + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile + "|" + controlpath + ")";
        return back;
    }

    /**
     * 装配保信主站 召唤指令(包含拓传)
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户编号
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @param controlpath
     *            拓传路径
     * @return 报文
     * @throws DataAsException
     *             异常
     */
    public static String getprotectdata(int calltype, String callid, String userid, String statid, String devid, String cpuid, String regionid, String groupid, String grouptype, String[] infos,
            int returntype, int dataformat, String savefile, String uuid, String controlpath) throws DataAsException
    {
        String back = "getprotectdata(" + calltype + "|" + callid + "|" + userid + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile;

        if (null != uuid && !uuid.isEmpty())
        {
            back = back + "|" + uuid;
        }
        if (null != controlpath && !controlpath.isEmpty())
        {
            back = back + "|" + controlpath;
        }

        return back + ")";
    }

    /**
     * 装配保信主站 召唤指令(不包含拓传)
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户编号
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @return 报文
     */
    public static String getprotectdata(int calltype, String callid, String userid, String statid, String devid, String cpuid, String regionid, String groupid, int grouptype, String[] infos,
            int returntype, int dataformat, String savefile)
    {
        String back = "getprotectdata(" + calltype + "|" + callid + "|" + userid + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile + ")";
        return back;
    }

    /**
     * 装配保信主站 召唤指令(不包含拓传，无感知)
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户编号
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @return 报文
     */
    public static String getprotectdataNS(int calltype, String callid, String userid, String statid, String devid, String cpuid, String regionid, String groupid, String grouptype, String[] infos,
            int returntype, int dataformat, String savefile)
    {
        String back = "getprotectdata(" + calltype + "|" + callid + "|" + userid + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|" + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile + ")";
        return back;
    }

    /**
     * 保信召唤日志
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户id
     * @param rectime
     *            日志时间
     * @param msg
     *            召唤结果
     * @param logtype
     *            日志类型
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @param ordernum
     *            顺序
     * @param iscache
     *            是否缓存
     * @param protocolid
     *            协议号
     * @return
     */
    public static String protectcalllog(int calltype, String callid, String userid, String rectime, String msg, int logtype, String statid, String devid, String cpuid, String regionid, String groupid,
            String grouptype, String[] infos, int returntype, int dataformat, String savefile, String ordernum, String iscache, String protocolid)
    {
        String back = "(protectcalllog|" + calltype + "|" + callid + "|" + userid + "|" + rectime + "|" + msg + "|" + logtype + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|"
                + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile + "|" + ordernum + "|" + iscache + "|" + protocolid + ")";
        return back;
    }

    /**
     * 保信召唤日志（无感知）
     *
     * @param calltype
     *            召唤类型
     * @param callid
     *            召唤编号
     * @param userid
     *            用户id
     * @param rectime
     *            日志时间
     * @param msg
     *            召唤结果
     * @param logtype
     *            日志类型
     * @param statid
     *            子站编号
     * @param devid
     *            装置编号
     * @param cpuid
     *            CPU编号
     * @param regionid
     *            定值区编号
     * @param groupid
     *            组编号
     * @param grouptype
     *            组类型
     * @param infos
     *            关联信息
     * @param returntype
     *            返回类型
     * @param dataformat
     *            数据类型
     * @param savefile
     *            流文件路径
     * @param ordernum
     *            顺序
     * @param iscache
     *            是否缓存
     * @param protocolid
     *            协议号
     * @return
     */
    public static String protectcalllogNS(int calltype, String callid, String userid, String rectime, String msg, int logtype, String statid, String devid, String cpuid, String regionid,
            String groupid, String grouptype, String[] infos, int returntype, int dataformat, String savefile, String ordernum, String iscache, String protocolid)
    {
        String back = "(protectcalllog|" + calltype + "|" + callid + "|" + userid + "|" + rectime + "|" + msg + "|" + logtype + "|" + statid + "|" + devid + "|" + cpuid + "|" + regionid + "|"
                + groupid + "|" + grouptype + "|";
        back = dataAsmArray(back, infos) + "|" + returntype + "|" + dataformat + "|" + savefile + "|" + ordernum + "|" + iscache + "|" + protocolid + ")";
        return back;
    }

    /**
     * 装备控制返回结果的消息
     *
     * @param result
     *            控制结果
     * @param controlid
     *            控制唯一码uuid
     * @param returntype
     *            结果返回类型
     * @return 命令字符串
     */
    public static String controlResultAsm(String result, String controlid, ReturnType returntype)
    {
        return "(controlresult|" + DataParse.removeParenth(result) + "|" + controlid + "|" + returntype.getResult() + ")";// (controlresult | 控制结果| controlid | returntype)
    }

    /**
     * 装备控制返回结果的消息(无感知)
     *
     * @param result
     *            控制结果
     * @return 命令字符串
     */
    public static String controlResultAsmNS(String result)
    {
        return "(controlresult|" + DataParse.removeParenth(result) + ")";// (controlresult | 控制结果)
    }

    /**
     * 装配控制指令数据
     * 
     * @param controlorder
     *            控制指令
     * @param extMsg
     *            拓传路由信息
     * @return 装配好的控制指令
     */
    public static String extensionOrderAsm(String order, UUID uuid, String extMsg)
    {
        return order.substring(0, order.length() - 1) + "|" + uuid + "|" + extMsg + ")";
    }

    /**
     * 拼接召唤pfr录波文件列表指令
     * 
     * @param wfid
     *            场站id
     * @param userid
     *            用户id，数据处理默认为100；
     * @param info
     *            关联信息，键值对形式进行设置，此处设置为召唤录播文件时间区间，格式为：beingtime=xxx,endtime=xxx，如果需要召唤所有录播文件列表，则info字段为空
     * @return getffrfiledata(calltype|wtid|userid|info1,info2…|uuid) calltype：3000,为召唤文件列表标志； wtid：一次调频设备id； userid：用户id，数据处理默认为100；
     *         info：关联信息，键值对形式进行设置，此处设置为召唤录播文件时间区间，格式为：beingtime=xxx,endtime=xxx，如果需要召唤所有录播文件列表，则info字段为空； uuid：指令唯一码。
     * 
     * 
     */

    public static String getCallFileListOrder(String wtid, String userid, String info)
    {
        return "getffrfiledata(30000|" + wtid + "|" + userid + "|" + info + "|" + UUID.randomUUID() + ")";
    }

    /**
     * 拼接召唤pfr录波文件指令
     * 
     * @param wfid
     *            场站id
     * @param userid
     *            用户id，数据处理默认为100；
     * @param info
     *            关联信息，键值对形式进行设置，此处设置为召唤录播文件名称，格式为：file1=xxx, file2=xxx，该info字段不能为空
     * @return getffrfiledata(calltype|wtid|userid|info1,info2…|uuid) calltype：3001,为召唤FFR录波文件标志； wtid：一次调频设备id； userid：用户id，数据处理默认为100； info：关联信息，键值对形式进行设置，此处设置为召唤录播文件名称，格式为：file1=xxx,
     *         file2=xxx，该info字段不能为空，前期版本实现一个指令只召唤一个文件，后期如果召唤文件过多等原因导致召唤缓慢，或者有其他需求，再支持一条指令召唤多个文件； uuid：指令唯一码。
     * 
     */

    public static String getCallFileOrder(String wtid, String userid, String info)
    {
        return "getffrfiledata(30001|" + wtid + "|" + userid + "|" + info + "|" + UUID.randomUUID() + ")";
    }
}
