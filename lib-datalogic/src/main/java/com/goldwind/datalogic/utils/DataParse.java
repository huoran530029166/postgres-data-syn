package com.goldwind.datalogic.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsDef.SystemPromptType;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.DateAsDef;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.model.ExtensionData;
import com.goldwind.datalogic.utils.ControlProcessDef.ReturnType;
import com.goldwind.datalogic.utils.NetCommDef.CommState;
import com.goldwind.datalogic.utils.NetCommDef.NetDataDirection;
import com.goldwind.datalogic.utils.NetCommDef.NetDownDataType;
import com.goldwind.datalogic.utils.NetCommDef.NetNoDirDataType;
import com.goldwind.datalogic.utils.NetCommDef.NetUpDataType;
import com.goldwind.datalogic.utils.NetCommDef.PlcFileType;
import com.goldwind.datalogic.utils.NetCommDef.ServiceManageType;
import com.goldwind.datalogic.utils.NetCommDef.TableDataOperType;
import com.goldwind.datalogic.utils.NetCommDef.TaskCycType;

/**
 * 消息解析
 * 
 * @author 曹阳
 *
 */
public class DataParse// NOSONAR
{

    // 判断数据方向及类型
    /**
     * 得到数据传输的方向
     * 
     * @param netData
     *            消息
     * @return 数据传输方向枚举
     */
    public static NetDataDirection getDataDirection(String netData)
    {
        NetDataDirection dataDirection = NetDataDirection.UNKNOWN;
        String left = "";
        String right = "";
        if (netData.length() > 0)
        {
            if (!netData.startsWith("(")) // 左边数据
            {
                int index = netData.indexOf("(");
                if (index > 0)
                {
                    left = netData.substring(0, index);
                }
            }
            else // 右边数据
            {
                int index = netData.indexOf("|");
                if (index > 0)
                {
                    right = netData.substring(1, index);
                }
                else
                {
                    right = netData.substring(1, netData.length() - 1);
                }
            }

            if (left.length() > 0)
            {
                if ("comtest".equals(left) || "addtask".equals(left) || "deltask".equals(left) || "stoptask".equals(left) || "getsrvmemdatanum".equals(left) || "getwtdatetime".equals(left)
                        || "getconfig".equals(left) || "getprocondition".equals(left) || "getproconfig".equals(left) || "getsrvcommdatanum".equals(left) || "getsrvmaintaininfo".equals(left)
                        || "getsoftadapterinfo".equals(left) || "getmemoryversion".equals(left) || "getqueuesize".equals(left) || "stop".equals(left) || "getconfigcondition".equals(left)
                        || "getconfigdata".equals(left) || "getguarpow".equalsIgnoreCase(left))
                {
                    dataDirection = NetDataDirection.NODIRECTION;
                }
                if ("wtcontrolorder".equals(left) || "wtothercontrolorder".equals(left) || "setdatavalues".equals(left) || "getpackdata".equals(left) || "getprotectdata".equals(left)
                        || "setprotectdata".equals(left))
                {
                    dataDirection = NetDataDirection.DOWN;
                }
            }
            else
            {
                if ("largedata".equals(right) || "nettest".equals(right) || "hisdatacomstate".equals(right) || "start".equals(right) || "serverinfo".equals(right) || "sendfile".equals(right)
                        || "getdspsrvdata".equals(right) || "getdspsrvdatasingle".equals(right) || "getdspsrvvirtualdata".equals(right) || "getweatherdata".equals(right)
                        || "gettyphoondata".equals(right) || "settyphoonlevel".equals(right) || "getbffiledata".equals(right) || "downloadwavefile".equals(right) || "excludcnf".equals(right)
                        || "uploadconfigdata".equals(right) || "savefile".equals(right) || "downloadfile".equals(right) || "protectstream".equals(right) || "wait".equals(right)
                        || "finish".equals(right) || "uploadfile".equals(right) || "comtestquery".equals(right) || "modifydbinfo".equals(right) || "getgzlblist".equals(right)
                        || "getgzlb".equals(right))
                // || "ok".equalsIgnoreCase(right) || "busy".equalsIgnoreCase(right) || "test".equalsIgnoreCase(right) || right.toLowerCase().startsWith("(error")
                {
                    dataDirection = NetDataDirection.NODIRECTION;
                }
                else
                {
                    dataDirection = NetDataDirection.UP;
                }
            }
        }

        return dataDirection;
    }

    /**
     * 得到向上网络数据的详细类型
     * 
     * @param netData
     *            消息
     * @return 向上数据类型
     */
    public static NetUpDataType getUpDataType(String netData)
    {
        String pRightData = "";
        int index = netData.indexOf("|");
        if (index > 0)
        {
            pRightData = netData.substring(1, index);
        }
        String rightData = (pRightData.isEmpty()) ? getParenthRightData(netData) : pRightData;
        NetUpDataType ndy = NetUpDataType.Unknown;
        switch (rightData) {
            case "wman":
                ndy = NetUpDataType.DevMainData;
                break;
            case "sediment":
                ndy = NetUpDataType.DevSedimentData;
                break;
            case "realtimedata":
                ndy = NetUpDataType.DevRealtimeData;
                break;
            case "changesave":
                ndy = NetUpDataType.DevChangeSaveData;
                break;
            case "onedata":
                ndy = NetUpDataType.DevOneData;
                break;
            case "one":
                ndy = NetUpDataType.ONEMINDATA;
                break;
            case "fivedata":
                ndy = NetUpDataType.FIVEMINDATA;
                break;
            case "newfivedata":
                ndy = NetUpDataType.NewFiveData;
                break;
            case "tendata":
                ndy = NetUpDataType.DevTenData;
                break;
            case "runlog":
                ndy = NetUpDataType.RunLog;
                break;
            case "powercurve":
                ndy = NetUpDataType.DevPowerCurve;
                break;
            case "falutdata":
                ndy = NetUpDataType.DevFaultData;
                break;
            case "statedata":
                ndy = NetUpDataType.DevStateData;
                break;
            case "pack":
                ndy = NetUpDataType.DevPackData;
                break;
            case "comstate":
                ndy = NetUpDataType.DevComState;
                break;
            case "alarmdata":
                ndy = NetUpDataType.DevAlarmData;
                break;
            case "wtcontrol":
                ndy = NetUpDataType.OrderLog;
                break;
            case "elecplan":
                ndy = NetUpDataType.ElecPlan;
                break;
            case "oldwman":
                ndy = NetUpDataType.OLDDEVMAINDATA;
                break;
            case "oldrealtimedata":
                ndy = NetUpDataType.OLDDEVREALTIMEDATA;
                break;
            case "oldfivedata":
                ndy = NetUpDataType.OLDDEVFIVEDATA;
                break;
            case "oldtendata":
                ndy = NetUpDataType.OLDDEVTENDATA;
                break;
            case "olddaydata":
                ndy = NetUpDataType.OLDDEVDAYDATA;
                break;
            case "oldpowercurve":
                ndy = NetUpDataType.OLDDEVPOWERCURVE;
                break;
            case "oldalarmdata":
                ndy = NetUpDataType.OLDDEVALARMDATA;
                break;
            case "oldcomstate":
                ndy = NetUpDataType.OLDDEVCOMSTATE;
                break;
            case "oldfalutdata":
                ndy = NetUpDataType.OLDDEVFAULTDATA;
                break;
            case "oldstatedata":
                ndy = NetUpDataType.OLDDEVSTATEDATA;
                break;
            case "prompt":
                ndy = NetUpDataType.SystemPrompt;
                break;
            case "userdata":
                ndy = NetUpDataType.UserData;
                break;
            case "upuserdata":
                ndy = NetUpDataType.UpUserData;
                break;
            case "sqldata":
                ndy = NetUpDataType.SqlData;
                break;
            case "upsqldata":
                ndy = NetUpDataType.UpSqlData;
                break;
            case "daydata":
                ndy = NetUpDataType.DevDayData;
                break;
            case "iecscr":
                ndy = NetUpDataType.IECscr; // 20140810 jzy Scada预警状态
                break;
            case "frontversion":
                ndy = NetUpDataType.PreVersion;
                break;
            case "protocolversion":
                ndy = NetUpDataType.DevVersion;
                break;
            case "mainlog":
                ndy = NetUpDataType.MainLog;
                break;
            case "warnlog":
                ndy = NetUpDataType.WarnLog;
                break;
            case "warnend":
                ndy = NetUpDataType.WarnEnd;
                break;
            case "userbehavior":
                ndy = NetUpDataType.UserbehaviorData;
                break;
            case "eventdata":
                ndy = NetUpDataType.EventData;
                break;
            case "uploadproductdata":
                ndy = NetUpDataType.UploadProductData;
                break;
            case "uploadserverdata":
                ndy = NetUpDataType.UploadServerData;
                break;
            case "json":
                ndy = NetUpDataType.Json;
                break;
            case "protectdata":
                ndy = NetUpDataType.ProtectData;
                break;
            case "protectcalllog":
                ndy = NetUpDataType.ProtectCallLog;
                break;
            case "protectsetlog":
                ndy = NetUpDataType.ProtectSetLog;
                break;
            case "protectstring":
                ndy = NetUpDataType.ProtectString;
                break;
            case "protectstream":
                ndy = NetUpDataType.ProtectStream;
                break;
            case "timedata":
                ndy = NetUpDataType.TimeData;
                break;
            case "controlresult":
                ndy = NetUpDataType.ControlResult;
                break;
            case "gpsdata":
                ndy = NetUpDataType.GPSData;
                break;
            case "mondata":
                ndy = NetUpDataType.MonData;
                break;
            case "gouserdata":
                ndy = NetUpDataType.GoUserData;
                break;
            case "newrunlog":
                ndy = NetUpDataType.NewRunLog;
                break;
            case "serverstate":
                ndy = NetUpDataType.ServerState;
                break;
            case "virtualtelemetry":
                ndy = NetUpDataType.VirtualTelemetry;
                break;
            case "controlstate":
                ndy = NetUpDataType.DevControlState;
                break;
            case "xllgldata":
                ndy = NetUpDataType.XllglData;
                break;
            case "monitorjson":
                ndy = NetUpDataType.MONITORJSON;
                break;
            default:
                break;
        }
        return ndy;
    }

    /**
     * 得到网络无方向数据的类型
     * 
     * @param netData
     *            消息
     * @return 无方向数据类型
     */
    public static NetNoDirDataType getNoDirectDataType(String netData)
    {
        String pLeftData = "";
        String pRightData = "";
        if (!netData.startsWith("(")) // 左边数据
        {
            int index = netData.indexOf("(");
            if (index > 0)
            {
                pLeftData = netData.substring(0, index);
            }
        }
        else // 右边数据
        {
            int index = netData.indexOf("|");
            if (index > 0)
            {
                pRightData = netData.substring(1, index);
            }
            else
            {
                int len = netData.length();
                pRightData = netData.substring(1, len - 1);
            }
        }
        String leftData = (pLeftData.length() == 0 && pRightData.length() == 0) ? getParenthLeftData(netData) : pLeftData;
        String rightData = (pLeftData.length() == 0 && pRightData.length() == 0) ? getParenthRightData(netData) : pRightData;
        NetNoDirDataType nndd = NetNoDirDataType.Unknown;
        switch (leftData) {
            case "comtest":
                nndd = NetNoDirDataType.OldSrvComTest;
                break;
            case "getwtdatetime":
                nndd = NetNoDirDataType.GetSysDateTime;
                break;
            case "getconfig":
                nndd = NetNoDirDataType.GetPreConfig;
                break;
            case "addtask":
                nndd = NetNoDirDataType.AddSrvTask;
                break;
            case "deltask":
                nndd = NetNoDirDataType.DelSrvTask;
                break;
            case "stoptask":
                nndd = NetNoDirDataType.StopSrvTask;
                break;
            case "getsrvmemdatanum":
                nndd = NetNoDirDataType.GetSrvMemDataNum;
                break;
            case "getproconfig":
                nndd = NetNoDirDataType.GetProConfig;
                break;
            case "getprocondition":
                nndd = NetNoDirDataType.GetProCondition;
                break;
            case "getsrvcommdatanum":
                nndd = NetNoDirDataType.GetSrvCommDataNum;
                break;
            case "getsrvmaintaininfo":
                nndd = NetNoDirDataType.GetSrvMaintainInfo;
                break;
            case "getsoftadapterinfo":
                nndd = NetNoDirDataType.GetSoftAdapterInfo;
                break;
            case "getmemoryversion":
                nndd = NetNoDirDataType.GetMemoryVersion;
                break;
            case "getqueuesize":
                nndd = NetNoDirDataType.GetQueueSize;
                break;
            case "stop":
                nndd = NetNoDirDataType.Stop;
                break;
            case "excludcnf":
                nndd = NetNoDirDataType.Excludcnf;
                break;
            case "getconfigcondition":
                nndd = NetNoDirDataType.GetConfigVersion;
                break;
            case "getconfigdata":
                nndd = NetNoDirDataType.GetConfigData;
                break;
            case "getguarpow":
                nndd = NetNoDirDataType.GETGUARPOW;
                break;
            default:
                break;
        }

        switch (rightData) {
            case "largedata":
                nndd = NetNoDirDataType.LargeData;
                break;
            case "start":
                nndd = NetNoDirDataType.PreStart;
                break;
            case "hisdatacomstate":
                nndd = NetNoDirDataType.SrvHisDataComState;
                break;
            case "serverinfo":
                nndd = NetNoDirDataType.ServerInfo;
                break;
            case "sendfile":
                nndd = NetNoDirDataType.SendFile;
                break;
            case "nettest":
                nndd = NetNoDirDataType.NetTest;
                break;
            case "getdspsrvdata":
                nndd = NetNoDirDataType.GetDspsrvData;
                break;
            case "getdspsrvdatasingle":
                nndd = NetNoDirDataType.GetDspsrvDataSingle;
                break;
            case "getdspsrvvirtualdata":
                nndd = NetNoDirDataType.GetDspsrvVirtualData;
                break;
            case "getweatherdata":
                nndd = NetNoDirDataType.GetWeatherData;
                break;
            case "settyphoonlevel":
                nndd = NetNoDirDataType.SetTyphoonLevel;
                break;
            case "gettyphoondata":
                nndd = NetNoDirDataType.GetTyphoonData;
                break;
            case "getbffiledata":
                nndd = NetNoDirDataType.GetBfFileData;
                break;
            case "excludcnf":
                nndd = NetNoDirDataType.Excludcnf;
                break;
            case "uploadconfigdata":
                nndd = NetNoDirDataType.UploadConfigData;
                break;
            case "downloadwavefile":
                nndd = NetNoDirDataType.DownloadWaveFile;
                break;
            case "downloadfile":
                nndd = NetNoDirDataType.DownloadFile;
                break;
            case "savefile":
                nndd = NetNoDirDataType.FileSave;
                break;
            case "protectstream":
                nndd = NetNoDirDataType.ProtectStream;
                break;
            case "finish":
                nndd = NetNoDirDataType.Finish;
                break;
            case "wait":
                nndd = NetNoDirDataType.Wait;
                break;
            case "ok":
                nndd = NetNoDirDataType.OK;
                break;
            case "test":
                nndd = NetNoDirDataType.Test;
                break;
            case "busy":
                nndd = NetNoDirDataType.Busy;
                break;
            case "uploadfile":
                nndd = NetNoDirDataType.UploadFile;
                break;
            case "comtestquery":
                nndd = NetNoDirDataType.ComtestQuery;
                break;
            case "modifydbinfo":
                nndd = NetNoDirDataType.ModifyDbInfo;
                break;
            case "getgzlblist":
                nndd = NetNoDirDataType.GETGZLBLIST;
                break;
            case "getgzlb":
                nndd = NetNoDirDataType.GETGZLB;
                break;
            default:
                break;
        }

        if (rightData.startsWith("error"))
        {
            nndd = NetNoDirDataType.Error;
        }

        return nndd;
    }

    /**
     * 得到向下网络数据的详细类型
     * 
     * @param netData
     *            消息
     * @return 向下数据类型
     */
    public static NetDownDataType getDownDataType(String netData)
    {
        String pLeftData = "";
        if (!netData.startsWith("(")) // 左边数据
        {
            int index = netData.indexOf("(");
            if (index > 0)
            {
                pLeftData = netData.substring(0, index);
            }
        }
        int index = netData.indexOf(")(");
        if (index < 0)
        {
            index = netData.indexOf(")");
        }
        if (index < 0)
        {
            return NetDownDataType.Unknown;
        }
        String leftData = (pLeftData.length() == 0) ? getParenthLeftData(netData) : pLeftData;
        NetDownDataType nddt = NetDownDataType.Unknown;
        switch (leftData) {
            case "getwman":
                nddt = NetDownDataType.GetDevMainData;
                break;
            case "getpackdata":
                nddt = NetDownDataType.GetDevPackData;
                break;
            case "getdevicedata":
                nddt = NetDownDataType.GetDevData;
                break;
            case "getcachedata":
                nddt = NetDownDataType.GetDevCacheData;
                break;
            case "wtcontrolorder":
                nddt = NetDownDataType.DevControlOrder;
                break;
            case "wtothercontrolorder":
                nddt = NetDownDataType.DevOtherControlOrder;
                break;
            case "setdatavalues":
                nddt = NetDownDataType.SetDevDataValues;
                break;
            case "setmultdatavalues":
                nddt = NetDownDataType.SetDevMultDataValues;
                break;
            case "setotherdatavalues":
                nddt = NetDownDataType.SetDevOtherDataValues;
                break;
            case "setwtdatetime":
                int length = removeParenth(netData).split("\\|", -1).length;
                if (length == 6)
                {
                    nddt = NetDownDataType.SetPreAllDevTime;
                    break;
                }
                if (length == 7)
                {
                    nddt = NetDownDataType.SetDevTime;
                    break;
                }
                nddt = NetDownDataType.Unknown;
                break;
            case "writeconfig":
                nddt = NetDownDataType.WirtePreConfig;
                break;
            case "writeini":
                nddt = NetDownDataType.WritePreIni;
                break;
            case "getplcfilename":
                nddt = NetDownDataType.GetPlcFileName;
                break;
            case "getplcfiledata":
                nddt = NetDownDataType.GetPlcFileData;
                break;
            case "resetservice":
                nddt = NetDownDataType.RestartPre;
                break;
            case "readini":
                nddt = NetDownDataType.ReadPreIni;
                break;
            case "getini":
                nddt = NetDownDataType.GetPreIni;
                break;
            case "getsharedir":
                nddt = NetDownDataType.GetPreUpgradeDir;
                break;
            case "filecopy":
                nddt = NetDownDataType.PreFileCopy;
                break;
            case "access":
                nddt = NetDownDataType.PreAccessUpdate;
                break;
            case "rptdataread":
                nddt = NetDownDataType.PreRptDataRead;
                break;
            case "rptdatadel":
                nddt = NetDownDataType.PreRptDataDel;
                break;
            case "rptdatareadcount":
                nddt = NetDownDataType.PreRptDataReadCount;
                break;
            case "realtimedataread":
                nddt = NetDownDataType.PreRealtimeDataRead;
                break;
            case "realtimedatadel":
                nddt = NetDownDataType.PreRealtimeDataDel;
                break;
            case "realtimedatareadcount":
                nddt = NetDownDataType.PreRealtimeDataReadCount;
                break;
            case "getplanorder":
                nddt = NetDownDataType.GetPlanOrder;
                break;
            case "getplandata":
                nddt = NetDownDataType.GetPlanData;
                break;
            case "getplandatanoid":
                nddt = NetDownDataType.GetPlanDataNoId;
                break;
            case "getdiskspace":
                nddt = NetDownDataType.GetPreDiskSpace;
                break;
            case "getcpu":
                nddt = NetDownDataType.GetPreCpu;
                break;
            case "getmemory":
                nddt = NetDownDataType.GetPreMemory;
                break;
            case "getversion":
                nddt = NetDownDataType.GetPreVersion;
                break;
            case "getdatetime":
                nddt = NetDownDataType.GetPreTime;
                break;
            case "comtest":
                nddt = NetDownDataType.PreComTest;
                break;
            case "servicetest":
                nddt = NetDownDataType.SrvComTest;
                break;
            case "servicemanage":
                nddt = NetDownDataType.ServiceManage;
                break;
            case "setprotectdata":
                nddt = NetDownDataType.SetProtectData;
                break;
            case "getprotectdata":
                nddt = NetDownDataType.GetProtectData;
                break;
            default:
                if (leftData.indexOf(NetCommDef.BROADCASTORDFLAG) == 0)
                {
                    nddt = NetDownDataType.BroadcastOrder;
                    break;
                }
                break;
        }
        return nddt;
    }
    // 判断数据方向及类型结束

    // 解析向上的数据
    /**
     * 解析主轮询数据(支持单包和多包)
     * 
     * @param netData
     *            消息
     * @return 瞬态数据包信息(hashmap)，包括数据类型(NetUpDataType)、设备ID(String)、数据(String[])、数据质量(String[])、本包数据在主轮询IEC列表中的起始位置（String,单包返回空）
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseMainData(String netData) throws DataAsException
    {
        NetUpDataType dataType = NetUpDataType.Unknown;
        String deviceId;
        String[] mainDatas;
        String position = "";// 本包数据在主轮询IEC列表中的起始位置
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);

            // 取得数据类型
            if ("wman".equals(arr[0].trim()))
            {
                dataType = NetUpDataType.DevMainData;
            }
            else if ("wman_split".equals(arr[0].trim()))
            {
                dataType = NetUpDataType.DevMainData_Split;
            }

            // 取得设备ID
            deviceId = arr[1].trim();

            // 取得数据
            mainDatas = arr[2].split(",", -1);
            for (int i = 0; i < mainDatas.length; i++)
            {
                mainDatas[i] = mainDatas[i].trim();
            }

            // 取得数据质量字段，2017-3-10增加
            if (arr.length > 3)
            {
                returnData.put("qualities", arr[3].split(",", -1));
            }

            // 取得本包数据在主轮询IEC列表中的起始位置，2019-2-22增加
            if (arr.length > 4)
            {
                position = arr[4];
            }

            returnData.put("dataType", dataType);
            returnData.put("deviceId", deviceId);
            returnData.put("mainDatas", mainDatas);
            returnData.put("position", position);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseMainData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析包数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,packName和packDatas字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parsePackData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String packName;
            String[] packDatas;
            String[] arr = removeParenth(netData).split("\\|", -1);
            packName = arr[1].trim();
            deviceId = arr[2].trim();
            packDatas = arr[3].split(",", -1);
            for (int i = 0; i < packDatas.length; i++)
            {
                packDatas[i] = packDatas[i].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("packName", packName);
            returnData.put("packDatas", packDatas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parsePackData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析设备通讯状态数据 (comstate|wtid|通讯状态|ordernum|iscache|protocolid)
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId和CommState通讯状态枚举
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseComState(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            CommState state = CommState.Unknown;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            int sta = Integer.parseInt(arr[2].trim());
            CommState[] css = CommState.values();
            for (CommState cs : css)
            {
                if (cs.getValue() == sta)
                {
                    state = cs;
                    break;
                }
            }
            returnData.put("deviceId", deviceId);
            returnData.put("state", state);
            if (arr.length == 6)
            {
                returnData.put("ordernum", arr[3]);
                returnData.put("iscache", arr[4]);
                returnData.put("protocolid", arr[5]);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseComState_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析历史实时数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId和realtimeDatas字符串数组、qualities数据质量字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseRealtimeData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String[] realtimeDatas;
            String[] qualities = new String[] {};// 数据质量
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            realtimeDatas = arr[2].split(",", -1);

            for (int i = 0; i < realtimeDatas.length; i++)
            {
                realtimeDatas[i] = realtimeDatas[i].trim();
            }
            if (arr.length > 3)
            {
                qualities = arr[3].split(",", -1);
                for (int i = 0; i < qualities.length; i++)
                {
                    qualities[i] = qualities[i].trim();
                }
            }
            returnData.put("deviceId", deviceId);
            returnData.put("realtimeDatas", realtimeDatas);
            returnData.put("qualities", qualities);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseRealtimeData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    // /**
    // * 新解析历史实时数据
    // *
    // * @param netData
    // * 消息
    // * @return hashmap，包括deviceId和realtimeDatas、qualities数据质量字符串数组
    // * @throws DataAsException
    // * 自定义异常
    // */
    // public static HashMap<String, Object> parseRealtimeDataToAl(String netData) throws DataAsException
    // {
    // HashMap<String, Object> returnData = new HashMap<String, Object>();
    // try
    // {
    // String deviceId;
    // ArrayList<Object> realtimeDatas;
    // ArrayList<Object> qualities = new ArrayList<>();// 数据质量

    // String[] arr = removeParenth(netData).split("\\|", -1);

    // deviceId = arr[1].trim();
    // realtimeDatas = ArrayOper.splitToArrrayList(arr[2], ",");
    //
    // // for (int i = 0; i < realtimeDatas.length; i++)
    // // {
    // // realtimeDatas[i] = realtimeDatas[i].trim();
    // // }
    // if (arr.length > 3)
    // {
    // qualities = ArrayOper.splitToArrrayList(arr[3], ",");
    // // for (int i = 0; i < qualities.length; i++)
    // // {
    // // qualities[i] = qualities[i].trim();
    // // }
    // }
    // returnData.put("deviceId", deviceId);
    // returnData.put("realtimeDatas", realtimeDatas);
    // returnData.put("qualities", qualities);
    // }
    // catch (Exception exp)
    // {
    // DataAsExpSet.throwExpByResCode("DataParse_parseRealtimeData_1", new String[] { "netData" }, new Object[] { netData }, exp);
    // }
    // return returnData;
    // }

    /**
     * 新解析历史实时数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId和realtimeDatas、qualities数据质量字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    @Deprecated
    public static HashMap<String, Object> newParseRealtimeData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String[] realtimeDatas;
            String[] qualities = new String[] {};// 数据质量
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            realtimeDatas = ArrayOper.splitToArrrayList(arr[2], ",");
            // 数据质量
            if (arr.length > 3)
            {
                qualities = ArrayOper.splitToArrrayList(arr[3], ",");
            }
            returnData.put("deviceId", deviceId);
            returnData.put("realtimeDatas", realtimeDatas);
            returnData.put("qualities", qualities);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseRealtimeData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析一分钟数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,windSpeed,power,theoryPower, deviceStatus,limitStatus,faultCode,stopCode,enviTemp,beginElec,endElec,limitcode
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseOneData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId = "";
            String windSpeed = "";
            String power = "";
            String theoryPower = "";
            String deviceStatus = "";
            String limitStatus = "";
            String faultCode = "null";
            String stopCode = "null";
            String enviTemp = "null";
            String beginElec = "null";
            String endElec = "null";
            String limitcode = "";// 20170327新加停机模式字--wrb

            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            String[] dataArr = arr[2].split(",", -1);
            windSpeed = dataArr[0].trim();
            power = dataArr[1].trim();
            theoryPower = dataArr[2].trim();
            deviceStatus = dataArr[3].trim();
            limitStatus = dataArr[4].trim();
            if (dataArr.length > 5)
            {
                faultCode = dataArr[5].trim();
            }
            if (dataArr.length > 6)
            {
                stopCode = dataArr[6].trim();
            }

            if (dataArr.length > 7)
            {
                enviTemp = dataArr[7].trim().isEmpty() ? "null" : dataArr[7].trim();
            }
            if (dataArr.length > 8)
            {
                beginElec = dataArr[8].trim().isEmpty() ? "null" : dataArr[8].trim();
            }
            if (dataArr.length > 9)
            {
                endElec = dataArr[9].trim().isEmpty() ? "null" : dataArr[9].trim();
            }
            if (dataArr.length > 10)
            {
                limitcode = dataArr[10].trim().isEmpty() ? "null" : dataArr[10].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("windSpeed", windSpeed);
            returnData.put("power", power);
            returnData.put("theoryPower", theoryPower);
            returnData.put("deviceStatus", deviceStatus);
            returnData.put("limitStatus", limitStatus);
            returnData.put("faultCode", faultCode);
            returnData.put("stopCode", stopCode);
            returnData.put("enviTemp", enviTemp);
            returnData.put("beginElec", beginElec);
            returnData.put("endElec", endElec);
            returnData.put("limitcode", limitcode);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseOneData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析十分钟数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId和tenDatas字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseTenData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String[] tenDatas;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            tenDatas = arr[2].split(",", -1);
            for (int i = 0; i < tenDatas.length; i++)
            {
                if (tenDatas[i].length() > 0)
                {
                    tenDatas[i] = tenDatas[i].trim();
                }
            }
            returnData.put("deviceId", deviceId);
            returnData.put("tenDatas", tenDatas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseTenData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析102协议电能表数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId、recTime和timeDatas字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseTimeData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String recTime;
            String[] timeDatas;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            recTime = arr[2].trim();
            timeDatas = arr[3].split(",", -1);
            for (int i = 0; i < timeDatas.length; i++)
            {
                if (timeDatas[i].length() > 0)
                {
                    timeDatas[i] = timeDatas[i].trim();
                }
            }
            returnData.put("deviceId", deviceId);
            returnData.put("recTime", recTime);
            returnData.put("timeDatas", timeDatas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseTimeData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析功率曲线
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId、speed、power和temp
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parsePowerCurve(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String speed;
            String power;
            String temp;
            String totalnum = "";
            String usenum = "";
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1];
            String[] data = arr[2].split(",", -1);
            speed = data[0].trim();
            power = data[1].trim();
            temp = data[2].trim();
            if (arr.length > 3)
            {
                String[] num = arr[3].split(",", -1);
                totalnum = num[0].trim();
                usenum = num[1].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("speed", speed);
            returnData.put("power", power);
            returnData.put("temp", temp);
            returnData.put("totalnum", totalnum);
            returnData.put("usenum", usenum);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parsePowerCurve_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析故障数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,faultData,[relationInfo,stateData,id]中括号中的数据,sourcecode,如果报文中有则返回值，没有为null
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseFaultData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String faultData;
            String relationInfo = null;
            String stateData = null;
            String id = null;
            String sourcecode = "";// 3s机组故障源码
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            faultData = arr[2].trim();
            if (arr.length > 3)
            {
                relationInfo = arr[3].trim();
            }

            if (arr.length > 4)
            {
                stateData = arr[4].trim();
            }

            if (arr.length > 5)
            {
                id = arr[5].trim();
            }
            if (arr.length > 6)
            {
                sourcecode = arr[6].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("faultData", faultData);
            returnData.put("relationInfo", relationInfo);
            returnData.put("stateData", stateData);
            returnData.put("id", id);
            returnData.put("sourcecode", sourcecode);

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseFaultData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析状态数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId和stateData
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseStateData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String stateData;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            stateData = arr[2].trim();
            returnData.put("deviceId", deviceId);
            returnData.put("stateData", stateData);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseStateData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析警告数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,alarmData,[relationInfo],[errorid]如果消息有则返回值，没有为null
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseAlarmData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String relationInfo = null;
            String[] arr = removeParenth(netData).split("\\|", -1);
            String deviceId = arr[1].trim();
            String alarmData = arr[2].trim();
            String errorid = null;
            String statedata = "";
            String sourcecode = "";

            if (arr.length > 3)
            {
                relationInfo = arr[3].trim();
                // 20170303增加wrb
                statedata = arr[4].trim();
            }
            if (arr.length > 5)
            {
                // 20170214增加
                errorid = arr[5].trim();
            }
            if (arr.length > 6)
            {
                // 20170609增加，3s机组故障源码
                sourcecode = arr[6].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("alarmData", alarmData);
            returnData.put("relationInfo", relationInfo);
            returnData.put("errorid", errorid);
            returnData.put("statedata", statedata);
            returnData.put("sourcecode", sourcecode);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseAlarmData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析系統事件数据（金风3s机组）
     * 
     * @author wangruibo
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,eventData,eventIec,statedata,evnetCode,sourcecode
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseEventData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String deviceId = arr[1].trim();
            String eventData = arr[2].trim();
            String eventIec = "";
            String statedata = "";
            String evnetCode = "";
            String sourcecode = "";

            if (arr.length > 3)
            {
                eventIec = arr[3].trim();
                statedata = arr[4].trim();
            }
            if (arr.length > 5)
            {
                evnetCode = arr[5].trim();
            }
            if (arr.length > 6)
            {
                // 3s机组事件原码
                sourcecode = arr[6].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("eventData", eventData);
            returnData.put("eventIec", eventIec);
            returnData.put("statedata", statedata);
            returnData.put("evnetCode", evnetCode);
            returnData.put("sourcecode", sourcecode);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseEventData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析前置要求配置表的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括tableName和ip
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetConfig(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String tableName = arr[0].trim().toLowerCase();
            String ip = arr[1].trim();
            returnData.put("tableName", tableName);
            returnData.put("ip", ip);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetConfig_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 析前置要求协议版本号的指令
     * 
     * @param netData
     *            消息
     * @return 协议版本号
     */
    public static String parseGetProCondition(String netData)
    {
        return removeParenth(netData).trim();
    }

    /**
     * 解析前置要求协议配置表的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括tableName和protocolId
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetProConfig(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String tableName = arr[0].trim().toLowerCase();
            String protocolId = arr[1].trim();
            returnData.put("tableName", tableName);
            returnData.put("protocolId", protocolId);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetProConfig_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析前置要求配置文件的指令
     * 
     * @param netData
     *            消息
     * @return 文件名
     */
    public static String parseGetIni(String netData)
    {
        return removeParenth(netData);
    }

    /**
     * 解析变位存储数据
     * 
     * @param netData
     *            信息
     * @return hashmap，包括deviceId,iecPath,changeSaveData,[rectime],soetime,quality
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseChangeSaveData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String iecPath;
            String changeSaveData;
            String rectime = "";
            String soetime = "";
            String quality = "";
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            iecPath = arr[2].trim();
            changeSaveData = arr[3].trim();
            if (arr.length > 4)
            {
                rectime = arr[4].trim();
            }
            if (arr.length > 5)// 20171116增加升压站soe时间
            {
                soetime = arr[5].trim();
            }
            if (arr.length > 6)// 20180403增加升压站数据质量
            {
                quality = arr[6].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("iecPath", iecPath);
            returnData.put("changeSaveData", changeSaveData);
            returnData.put("rectime", rectime);
            returnData.put("soetime", soetime);
            returnData.put("quality", quality);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseChangeSaveData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 移除沉积数据标志
     * 
     * @param netData
     *            消息
     * @return hashmap，包括dataTime和srcData
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> removeSedimentFlg(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String dataTime = DateAsDef.getNow();
            String srcData = netData;
            // 判断是否沉积数据
            if (netData.trim().toLowerCase().startsWith("(sediment"))
            // if (netData.trim().contains("(sediment"))
            {
                String data = removeParenth(netData);
                String[] arr = data.split("\\|", -1);
                dataTime = arr[1].trim();
                srcData = "(" + removeParenth(data).trim() + ")";
                returnData.put("dataTime", dataTime);
                returnData.put("srcData", srcData);
            }
            else
            {
                returnData.put("dataTime", dataTime);
                returnData.put("srcData", srcData);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_removeSedimentFlg_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析Socket指令日志
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,orderFlg,systemId,userId,descr,parms,info,controltype,controlid,returntype,ordernum,iscache,protocolid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseSocketOrderLog(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {

            String deviceId;
            String orderFlg;
            String systemId;
            String userId;
            String descr;
            String parms = "";
            String info = "";// 20171215增加。--wrb
            String controltype = "";// 20180116增加.--wrb
            String controlid = "";// 20181105增加.--fcy
            String returntype = "";// 20181105增加.--fcy
            String ordernum = "";
            String iscache = "";
            String protocolid = "";
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1];
            String tmp = "";

            orderFlg = arr[2].trim();
            tmp = arr[3].trim();
            descr = arr[4].trim();
            if (arr.length > 5)
            {
                parms = arr[5];
            }
            if (arr.length > 6)
            {
                info = arr[6];
            }
            if (arr.length > 7)
            {
                controltype = arr[7];
            }
            // 判断控制指令是否含有ordernum|iscache|protocolid字段
            if (arr.length < 10)
            {
                if (arr.length > 8)
                {
                    controlid = arr[8];
                }
                if (arr.length > 9)
                {
                    returntype = arr[9];
                }
            }
            if (arr.length > 10)
            {
                if (arr.length > 8)
                {
                    ordernum = arr[8];
                }
                if (arr.length > 9)
                {
                    iscache = arr[9];
                }
                if (arr.length > 10)
                {
                    protocolid = arr[10];
                }
                if (arr.length > 11)
                {
                    controlid = arr[11];
                }
                if (arr.length > 12)
                {
                    returntype = arr[12];
                }
            }

            if (!"".equals(tmp) && !"0".equals(tmp))
            {
                systemId = tmp.split("\\.").length > 1 ? tmp.split("\\.")[0] : "";
                userId = tmp.split("\\.").length > 1 ? tmp.split("\\.")[1] : "";
            }
            else
            {
                systemId = "";
                userId = "";
            }
            returnData.put("deviceId", deviceId);
            returnData.put("orderFlg", orderFlg);
            returnData.put("systemId", systemId);
            returnData.put("userId", userId);
            returnData.put("descr", descr);
            returnData.put("parms", parms);
            returnData.put("info", info);
            returnData.put("controltype", controltype);
            returnData.put("controlid", controlid);
            returnData.put("returntype", returntype);
            returnData.put("ordernum", ordernum);
            returnData.put("iscache", iscache);
            returnData.put("protocolid", protocolid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseSocketOrderLog_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析电量计划
     * 
     * @param netData
     *            消息
     * @return hashmap，包括wfId,recTime,updateTime,planValue,flag
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseElecPlan(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<>();
        try
        {
            String wfId;
            String recTime;
            String updateTime;
            String planValue;
            String flag;
            String[] arr = removeParenth(netData).split("\\|", -1);
            wfId = arr[1].trim();
            recTime = arr[2].trim();
            updateTime = arr[3].trim();
            planValue = arr[4].trim();
            flag = arr[5].trim();
            returnData.put("wfId", wfId);
            returnData.put("recTime", recTime);
            returnData.put("updateTime", updateTime);
            returnData.put("planValue", planValue);
            returnData.put("flag", flag);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseElecPlan_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析运行日志
     * 
     * @param netData
     *            消息
     * @return hashmap，包括systemId,typeId,recTime,wfId,objectId,logCode,guid和relationDatas字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseRunLog(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<>();
        try
        {
            String systemId;
            String typeId;
            String recTime;
            String wfId;
            String objectId;
            String logCode;
            String guid;
            String[] relationDatas = new String[0];
            String[] arr = removeParenth(netData).split("\\|", -1);
            systemId = arr[1].trim();
            typeId = arr[2].trim();
            recTime = arr[3].trim();
            wfId = arr[4].trim();
            objectId = arr[5].trim();
            logCode = arr[6].trim();
            if (arr.length > 7)
            {
                guid = arr[7].trim();
                returnData.put("guid", guid);
            }

            if (arr.length > 8)
            {
                relationDatas = arr[8].trim().split(",", -1);
            }
            returnData.put("systemId", systemId);
            returnData.put("typeId", typeId);
            returnData.put("recTime", recTime);
            returnData.put("wfId", wfId);
            returnData.put("objectId", objectId);
            returnData.put("logCode", logCode);
            returnData.put("relationDatas", relationDatas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseRunLog_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析系统提示
     * 
     * @param netData
     *            消息
     * @return hashmap，包括systemPromptType,descr
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseSystemPrompt(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<>();
        try
        {
            SystemPromptType systemPromptType = null;
            String descr;
            String[] arr = removeParenth(netData).split("\\|", -1);
            SystemPromptType[] spts = SystemPromptType.values();
            for (SystemPromptType spt : spts)
            {
                if (spt.getValue() == Integer.parseInt(arr[1].trim()))
                {
                    systemPromptType = spt;
                }
            }
            descr = arr[2].trim();
            returnData.put("systemPromptType", systemPromptType);
            returnData.put("descr", descr);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseSystemPrompt_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析数据中的设备信息
     * 
     * @param netData
     *            消息
     * @return hashmap，包括wfId和deviceId
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseDeviceInfo(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String wfId;
            String deviceId;
            String[] arr = DataParse.removeParenth(netData).split("\\|", -1);
            if (arr[0].trim().indexOf("old") == 0) // 旧版数据
            {
                wfId = arr[2].trim();
                deviceId = arr[1].trim();
            }
            else
            {
                wfId = "";
                deviceId = arr[1].trim();
            }
            returnData.put("wfId", wfId);
            returnData.put("deviceId", deviceId);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseDeviceInfo_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 尝试解析数据中的设备信息
     * 
     * @param netData
     *            消息
     * @return hashmap，包括wfId和deviceId
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> tryParseDeviceInfo(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            HashMap<String, Object> tmp = new HashMap<String, Object>();
            String data = netData;
            if (netData.indexOf("(sediment") == 0)
            {
                tmp = removeSedimentFlg(data);
            }
            returnData = parseDeviceInfo((String) tmp.get("srcData"));
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_tryParseDeviceInfo_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析用户数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括operType操作类型枚举,tableName,wfId,unitList1,unitList2
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseUserData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            TableDataOperType operType = null;
            String tableName;
            String wfId;
            List<UserDataUnit> unitList1 = new ArrayList<>();
            List<UserDataUnit> unitList2 = new ArrayList<>();
            String[] arr = ArrayOper.trimArray(DataParse.removeParenth(netData).split("\\|", -1));
            switch (arr[1]) {
                case "ins":
                    operType = TableDataOperType.Ins;
                    break;
                case "upd":
                    operType = TableDataOperType.Upd;
                    break;
                case "del":
                    operType = TableDataOperType.Del;
                    break;
                default:
                    DataAsExpSet.throwExpByResCode("DataParse_parseUserData_1", new String[] { "netData" }, new Object[] { netData }, null);
                    break;
            }
            tableName = arr[2];
            wfId = arr[3];
            unitList1.clear();
            if (!arr[4].isEmpty())
            {
                String[] temp = ArrayOper.trimArray(arr[4].split("~"));
                for (int i = 0; i < temp.length; i++)
                {
                    unitList1.add(UserDataUnit.createUnit(temp[i]));
                }
            }
            unitList2.clear();
            if (!arr[5].isEmpty())
            {
                String[] temp = ArrayOper.trimArray(arr[5].split("~"));
                for (int i = 0; i < temp.length; i++)
                {
                    unitList2.add(UserDataUnit.createUnit(temp[i]));
                }
            }
            returnData.put("operType", operType);
            returnData.put("tableName", tableName);
            returnData.put("wfId", wfId);
            returnData.put("unitList1", unitList1);
            returnData.put("unitList2", unitList2);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseUserData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 转换用户数据到sql语句
     * 
     * @param operType
     *            操作类型
     * @param tableName
     *            表名
     * @param unitList1
     *            用户数据1
     * @param unitList2
     *            用户数据2
     * @return SQL语句
     * @throws DataAsException
     *             自定义异常
     */
    public static String convertUserDataToSql(TableDataOperType operType, String tableName, List<UserDataUnit> unitList1, List<UserDataUnit> unitList2) throws DataAsException
    {
        String sql = "";
        try
        {
            switch (operType) {
                case Ins:// 增加
                    StringBuilder s1 = new StringBuilder();
                    StringBuilder s2 = new StringBuilder();
                    for (int i = 0; i < unitList1.size(); i++)
                    {
                        s1.append(unitList1.get(i).getCol());
                        s2.append(unitList1.get(i).getVal());
                        if (i < unitList1.size() - 1)
                        {
                            s1.append(", ");
                            s2.append(", ");
                        }
                    }
                    sql = "insert into " + tableName + "(" + s1.toString() + ") values (" + s2.toString() + ")";
                    break;
                case Upd:// 升级
                    s1 = new StringBuilder();
                    for (int i = 0; i < unitList1.size(); i++)
                    {
                        s1.append(unitList1.get(i).getCol() + " = " + unitList1.get(i).getVal());
                        if (i < unitList1.size() - 1)
                        {
                            s1.append(", ");
                        }
                    }

                    s2 = new StringBuilder();
                    if (unitList2.size() > 0)
                    {
                        s2.append(" where ");
                        for (int i = 0; i < unitList2.size(); i++)
                        {
                            s2.append(unitList2.get(i).getCol() + " = " + unitList2.get(i).getVal());
                            if (i < unitList2.size() - 1)
                            {
                                s2.append(" and ");
                            }
                        }
                    }
                    sql = "update " + tableName + " set " + s1.toString() + s2.toString();
                    break;
                case Del:// 删除
                    s1 = new StringBuilder();
                    if (unitList1.size() > 0)
                    {
                        s1.append(" where ");
                        for (int i = 0; i < unitList1.size(); i++)
                        {
                            s1.append(unitList1.get(i).getCol() + " = " + unitList1.get(i).getVal());
                            if (i < unitList1.size() - 1)
                            {
                                s1.append(" and ");
                            }
                        }
                    }
                    sql = "delete from " + tableName + s1.toString();
                    break;
                default:
                    break;
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_convertUserDataToSql_1", null, null, exp);
        }
        return sql;
    }

    /**
     * 解析上传的sql语句
     * 
     * @param netData
     *            消息
     * @return hashmap，包括tableName,wfId和sql
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseUpSqlData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String tableName;
            String wfId;
            String sql;
            String[] arr = ArrayOper.trimArray(DataParse.removeParenth(netData).split("\\|", -1));
            tableName = arr[1];
            wfId = arr[2];
            sql = arr[3];
            returnData.put("tableName", tableName);
            returnData.put("wfId", wfId);
            returnData.put("sql", sql);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseUpSqlData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 将上传的sql语句转换为sql语句
     * 
     * @param netData
     *            消息
     * @return string,sql消息
     * @throws DataAsException
     *             自定义异常
     */
    public static String parseUpSqlDataToSql(String netData) throws DataAsException
    {
        // 防止数据处理安装
        try
        {
            String flag;
            String tableName;
            String wfId;
            String sql;
            String[] arr = ArrayOper.trimArray(netData.split("\\|", -1));
            flag = arr[0];
            tableName = arr[1];
            wfId = arr[2];
            sql = arr[3];
            flag = flag.replace("upsqldata", "sqldata");
            netData = flag + "|" + tableName + "|" + wfId + "|" + sql;
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseUpSqlData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return netData;
    }

    /**
     * 解析SCADA预警状态数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,mainDatas字符串数组
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseIECscrData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String[] mainDatas;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            mainDatas = arr[2].split(",", -1);
            returnData.put("deviceId", deviceId);
            returnData.put("mainDatas", mainDatas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseIECscrData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析前置版本数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括wfId,preIp,preVersion
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parsePreVersion(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String wfId;
            String preIp;
            String preVersion;
            String[] arr = removeParenth(netData).split("\\|", -1);
            wfId = arr[1].trim();
            preIp = arr[2].trim();
            preVersion = arr[3].trim();
            returnData.put("wfId", wfId);
            returnData.put("preIp", preIp);
            returnData.put("preVersion", preVersion);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parsePreVersion_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析设备版本数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,proId,proVersion,preIp
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseDevVersion(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String proId;
            String proVersion;
            String preIp;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            proId = arr[2].trim();
            proVersion = arr[3].trim();
            if (arr.length > 4)
            {
                preIp = arr[4].trim();
                returnData.put("preIp", preIp);
            }
            returnData.put("deviceId", deviceId);
            returnData.put("proId", proId);
            returnData.put("proVersion", proVersion);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseDevVersion_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析状态日志数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括machineid,warnid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseMainLog(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String warnid = "";
            String machineid = arr[1].trim();
            if (arr.length > 2)
            {
                warnid = arr[2] != null ? arr[2].trim() : "";
            }
            returnData.put("machineid", machineid);
            returnData.put("warnid", warnid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseMainLog_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析告警日志数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括systemid,levelid,rectime,wfid,objectid,logcode,warnid,flag,info,iecval,objecttype,stateval,opertype,isdel
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseWarnLog(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String systemid = arr[1].trim();
            String levelid = arr[2].trim();
            String rectime = arr[3].trim();
            String wfid = arr[4].trim();
            String objectid = arr[5].trim();
            String logcode = arr[6].trim();
            String warnid = arr[7].trim();
            String flag = arr[8].trim();
            if (arr.length > 9)
            {
                String info = arr[9].trim();
                returnData.put("info", info);
            }
            if (arr.length > 10)
            {
                String typeid = arr[10].trim();
                returnData.put("typeid", typeid);
            }

            // 添加iecval、objecttype字段（20180710--wrb）
            if (arr.length > 11)
            {
                String iecval = arr[11].trim();
                returnData.put("iecval", iecval);
            }
            if (arr.length > 12)
            {
                String objecttype = arr[12].trim();
                returnData.put("objecttype", objecttype);
            }

            // 添加上一条数据结束标志。--wrb20180821
            if (arr.length > 13)
            {
                String stateval = arr[13].trim();
                returnData.put("stateval", stateval);
            }
            // 添加操作类型。--wrb
            if (arr.length > 14)
            {
                String opertype = arr[14].trim().length() == 0 ? "0" : arr[14].trim();
                returnData.put("opertype", opertype);
            }

            // 添加删除标记位。--fcy 0-没被删除 1-被删除 2-屏蔽 3-数量超限 4-频繁告警
            if (arr.length > 15)
            {
                String isdel = arr[15].trim().length() == 0 ? "0" : arr[15].trim();
                returnData.put("isdel", isdel);
            }

            returnData.put("systemid", systemid);
            returnData.put("levelid", levelid);
            returnData.put("rectime", rectime);
            returnData.put("wfid", wfid);
            returnData.put("objectid", objectid);
            returnData.put("logcode", logcode);
            returnData.put("warnid", warnid);
            returnData.put("flag", flag);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseWarnLog_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析告警日志结束数据
     * 
     * @param netData
     *            消息
     * @return hashmap，包括endtime,warnid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseWarnEnd(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            String endtime = arr[1].trim();
            String warnid = arr[2].trim();
            returnData.put("endtime", endtime);
            returnData.put("warnid", warnid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseWarnEnd_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }
    // 解析向上的数据结束

    // 解析向下的数据

    /**
     * 解析要求得到plc文件名称的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,beginDate,endDate,plcFileType,plcIp,plcUser,plcPassword
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetPlcFileName(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            Calendar beginDate;
            Calendar endDate;
            PlcFileType plcFileType = PlcFileType.Unknown;
            String plcIp;
            String plcUser;
            String plcPassword;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[0].trim();
            beginDate = DataAsFunc.parseDateTime(arr[1].trim());
            endDate = DataAsFunc.parseDateTime(arr[2].trim());
            PlcFileType[] pfys = PlcFileType.values();
            for (PlcFileType pfy : pfys)
            {
                if (pfy.getValue() == Integer.parseInt(arr[3].trim()))
                {
                    plcFileType = pfy;
                }
            }
            plcIp = arr[4].trim();
            plcUser = arr[5].trim();
            plcPassword = arr[6].trim();
            returnData.put("deviceId", deviceId);
            returnData.put("beginDate", beginDate);
            returnData.put("endDate", endDate);
            returnData.put("plcFileType", plcFileType);
            returnData.put("plcIp", plcIp);
            returnData.put("plcUser", plcUser);
            returnData.put("plcPassword", plcPassword);

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetPlcFileName_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析要求得到plc文件数据的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,fileName,plcIp,plcUser,plcPassword
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetPlcFileData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String fileName;
            String plcIp;
            String plcUser;
            String plcPassword;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[0].trim();
            fileName = arr[1].trim();
            plcIp = arr[2].trim();
            plcUser = arr[3].trim();
            plcPassword = arr[4].trim();
            returnData.put("deviceId", deviceId);
            returnData.put("fileName", fileName);
            returnData.put("plcIp", plcIp);
            returnData.put("plcUser", plcUser);
            returnData.put("plcPassword", plcPassword);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetPlcFileData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析服务管理指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括manageType,paramList
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseServiceManage(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            ServiceManageType manageType = ServiceManageType.RESTARTSERVICE;
            String[] paramList;
            String[] arr = removeParenth(netData).split("\\|", -1);
            ServiceManageType[] smts = ServiceManageType.values();
            for (ServiceManageType smt : smts)
            {
                if (smt.getValue() == Integer.parseInt(arr[0].trim()))
                {
                    manageType = smt;
                }
            }
            paramList = arr[1].split(",", -1);
            returnData.put("manageType", manageType);
            returnData.put("paramList", paramList);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseServiceManage_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    // 解析向下的数据结束

    // 解析无方向数据

    /**
     * 解析前置初始化启动的指令
     * 
     * @param netData
     *            消息
     * @return 服务ip地址
     * @throws DataAsException
     *             自定义异常
     */
    public static String parsePreAdapterStart(String netData) throws DataAsException
    {
        String serviceIp = null;
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            serviceIp = arr[1].trim();
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parsePreAdapterStart_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return serviceIp;
    }

    /**
     * 解析历史数据通讯状态
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,state
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseHisDataComState(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            CommState state = CommState.Unknown;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            CommState[] css = CommState.values();
            for (CommState cs : css)
            {
                if (cs.getValue() == Integer.parseInt(arr[2].trim()))
                {
                    state = cs;
                }

            }
            returnData.put("deviceId", deviceId);
            returnData.put("state", state);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseHisDataComState_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析增加任务的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括userId,monitorId,deviceIds,proId,orderFlg,paramList,cycType,beginTime
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseAddTask(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String userId;
            String monitorId;
            String[] deviceIds;
            String proId;
            String orderFlg;
            String[] paramList;
            TaskCycType cycType = TaskCycType.OnlyOnce;
            String beginTime;
            String[] arr = removeParenth(netData).split("\\|", -1);
            userId = arr[0].trim();
            monitorId = arr[1].trim();
            deviceIds = arr[2].trim().split(",", -1);
            for (int i = 0; i < deviceIds.length; i++)
            {
                deviceIds[i] = deviceIds[i].trim();
            }
            TaskCycType[] tcts = TaskCycType.values();
            for (TaskCycType tct : tcts)
            {
                if (tct.ordinal() == Integer.parseInt(arr[6].trim()))
                {
                    cycType = tct;
                }
            }
            proId = arr[3].trim();
            orderFlg = arr[4].trim();
            paramList = arr[5].trim().split(",", -1);
            paramList = ArrayOper.trimArray(paramList);
            beginTime = arr[7].trim();
            returnData.put("userId", userId);
            returnData.put("monitorId", monitorId);
            returnData.put("deviceIds", deviceIds);
            returnData.put("proId", proId);
            returnData.put("orderFlg", orderFlg);
            returnData.put("paramList", paramList);
            returnData.put("cycType", cycType);
            returnData.put("beginTime", beginTime);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseAddTask_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析删除任务的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括taskId,userid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseDelTask(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String taskId;
            String userid;
            String[] arr = removeParenth(netData).split("\\|", -1);
            taskId = arr[0].trim();
            userid = arr[1].trim();
            returnData.put("taskId", taskId);
            returnData.put("userid", userid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseDelTask_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析停止任务的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括taskId,userid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseStopTask(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String taskId;
            String userid;
            String[] arr = removeParenth(netData).split("\\|", -1);
            taskId = arr[0].trim();
            userid = arr[1].trim();
            returnData.put("taskId", taskId);
            returnData.put("userid", userid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseStopTask_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析大数据
     * 
     * @param netData
     *            消息
     * @return hashmap，dataName,packCount,zipData
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseLargeData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String dataName;
            int packCount;
            boolean zipData;
            String[] arr = removeParenth(netData).split("\\|", -1);
            dataName = arr[1].trim();
            packCount = Integer.parseInt(arr[2].trim());
            if ("0".equals(arr[3].trim()))
            {
                zipData = false;
            }
            else
            {
                zipData = true;
            }
            returnData.put("dataName", dataName);
            returnData.put("packCount", packCount);
            returnData.put("zipData", zipData);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseLargeData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析发送文件的指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括fileName,packCount
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseSendFileOrder(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String fileName;
            int packCount;
            String[] arr = removeParenth(netData).split("\\|", -1);
            if (!"sendfile".equals(arr[0]))
            {
                DataAsExpSet.throwExpByResCode("DataParse_parseSendFileOrder_1", null, null, null);
            }
            fileName = arr[1].trim();
            packCount = Integer.parseInt(arr[2].trim());
            returnData.put("fileName", fileName);
            returnData.put("packCount", packCount);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseSendFileOrder_2", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析发送文件的指令(通用)
     * 
     * @param netData
     *            消息
     * @return hashmap，包括fileName,packCount
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseSaveFileOrder(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String fileName;
            String fileType;
            String dataTime;
            int packCount;
            String[] arr = removeParenth(netData).split("\\|", -1);
            if (!"savefile".equals(arr[0]))
            {
                DataAsExpSet.throwExpByResCode("DataParse_parseSaveFileOrder_1", null, null, null);
            }
            fileType = arr[1].trim();
            fileName = arr[2].trim();
            dataTime = arr[3].trim();
            packCount = Integer.parseInt(arr[4].trim());

            returnData.put("fileType", fileType);
            returnData.put("fileName", fileName);
            returnData.put("dataTime", dataTime);
            returnData.put("packCount", packCount);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseSaveFileOrder_2", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析组播指令日志
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId,orderType,orderFlg,paramList,systemId,userId,descr,succeed
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGroupOrderLog(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String orderType;
            String orderFlg;
            String[] paramList;
            String systemId;
            String userId;
            String descr;
            boolean succeed;
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1];
            String tmp = "";
            String[] tmpArr = arr[2].trim().split("\\.");
            orderType = tmpArr[0].trim();
            orderFlg = tmpArr[1].trim();
            if (!arr[3].trim().isEmpty())
            {
                paramList = arr[3].trim().split(",", -1);
            }
            else
            {
                paramList = new String[0];
            }

            tmp = arr[4].trim();
            descr = arr[5].trim();
            if (!"0.0".equals(tmp))
            {
                systemId = tmp.split("\\.")[0];
                userId = tmp.split("\\.")[1];
            }
            else
            {
                systemId = "";
                userId = "";
            }

            if (ReturnParse.checkSuccessFlg(descr))
            {
                succeed = true;
            }
            else
            {
                succeed = false;
            }
            returnData.put("deviceId", deviceId);
            returnData.put("orderType", orderType);
            returnData.put("orderFlg", orderFlg);
            returnData.put("paramList", paramList);
            returnData.put("systemId", systemId);
            returnData.put("userId", userId);
            returnData.put("descr", descr);
            returnData.put("succeed", succeed);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGroupOrderLog_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析查询软适配信息指令
     * 
     * @param netData
     *            消息
     * @return hashmap，包括objId,dataType,paramList
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetSoftAdapInfo(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String objId;
            String dataType;
            String[] paramList;
            String[] arr = removeParenth(netData).split("\\|", -1);
            objId = arr[0].trim();
            dataType = arr[1].trim();
            paramList = arr[2].trim().split(",", -1);
            returnData.put("objId", objId);
            returnData.put("dataType", dataType);
            returnData.put("paramList", paramList);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetSoftAdapterInfo_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    // 解析无方向数据结束
    // 其他函数
    /**
     * 得到左括号右边的数据,以|为截止
     * 
     * @param netData
     *            消息
     * @return 左括号右边的数据
     */
    public static String getParenthRightData(String netData)
    {
        if (netData.length() == 0)
        {
            return "";
        }
        int begin = netData.indexOf("(");
        if (begin >= 0)
        {
            begin = begin + 1;
            int end = netData.indexOf("|");
            if (end < 0)
            {
                end = netData.indexOf(")");
            }
            if (end < 0)
            {
                end = netData.length() - begin;
            }

            return netData.substring(begin, end).trim();
        }
        else
        {
            return "";
        }
    }

    /**
     * 得到左括号左边的数据
     * 
     * @param netData
     *            消息
     * @return 左括号左边的数据
     */
    public static String getParenthLeftData(String netData)
    {
        if (netData.length() == 0)
        {
            return "";
        }

        int left = netData.indexOf("(");
        if (left > 0)
        {
            return netData.substring(0, left).trim();
        }
        else
        {
            return "";
        }
    }

    /**
     * 去除左右括号
     * 
     * @param netData
     *            消息
     * @return 去除左右括号后的消息
     */
    public static String removeParenth(String netData)
    {
        if (netData == null || netData.isEmpty())
        {
            return "";
        }

        int left = netData.indexOf("(") + 1;
        int right = netData.lastIndexOf(")");
        if (right < 0)
        {
            right = netData.length();// - left;
            // right = netData.length() - left;
        }
        // else
        // {
        // right = right - left;
        // }
        String netdata = netData.substring(left, right).trim();

        return netdata;
        // return netData.substring(left, right).trim();
    }

    /**
     * 移除一对符号
     * 
     * @param data
     *            消息
     * @param leftSymbol
     *            左符号
     * @param rightSymbol
     *            右符号
     * @return 移除符号后的消息
     */
    @Deprecated
    public static String removePairSymbol(String data, String leftSymbol, String rightSymbol)
    {
        if (data.length() == 0 || leftSymbol.length() == 0 || rightSymbol.length() == 0)
        {
            return "";
        }
        int left = data.indexOf(leftSymbol);
        if (left >= 0)
        {
            if (left < data.length() - 1)
            {
                left++;
            }
            else
            {
                return "";
            }
        }
        else
        {
            left = 0;
        }

        int right = data.lastIndexOf(rightSymbol);
        if (right < 0)
        {
            right = data.length() - left;
        }
        else if (right <= left)
        {
            return "";
        }
        else
        {
            right = right - left;
        }
        return data.substring(left, right).trim();
    }

    // 其他函数结束
    /**
     * int转byte[]
     * 
     * @param i
     *            int
     * @return byte[]
     */
    public static byte[] intToByteArray(int i)
    {
        byte[] result = new byte[4];
        // 由低位到高位
        result[0] = (byte) (i & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        result[3] = (byte) ((i >> 24) & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     * 
     * @param bytes
     *            byte[]
     * @return int
     */
    public static int byteArrayToInt(byte[] bytes)
    {
        int value = 0;
        // 由低位到高位
        for (int i = 0; i < 4; i++)
        {
            int shift = i * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * byte[]合并
     * 
     * @param byte1
     *            byte[]1
     * @param byte2
     *            byte[]2
     * @return 新byte[]
     */
    public static byte[] byteMerger(byte[] byte1, byte[] byte2)
    {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    /**
     * byte[]分割
     * 
     * @param byte1
     *            原始byte
     * @param start
     *            切割起始位置
     * @param end
     *            切割结束位置
     * @return 切割完成的byte
     */
    public static byte[] byteCut(byte[] byte1, int start, int end)
    {
        // 判断起始位置是否大于结束位置，如果是，则返回空byte
        if (start > end - 1)
        {
            return new byte[0];
        }
        else
        {
            byte[] byte2 = new byte[end - start];
            for (int j = 0; j < byte2.length; j++)
            {
                byte2[j] = byte1[start + j];
            }
            return byte2;
        }
    }

    /**
     * 组装包数据
     * 
     * @param head
     *            包头，即第几个包
     * @param body
     *            内容
     * @return 组装后的包,包含包头，大小，内容
     */
    public static byte[] packbyte(int head, byte[] body)
    {
        byte[] packbyte = null;
        byte[] headbyte = DataParse.intToByteArray(head);
        byte[] len = DataParse.intToByteArray(body.length);
        packbyte = DataParse.byteMerger(DataParse.byteMerger(headbyte, len), body);
        return packbyte;
    }

    /**
     * 解析包数据
     * 
     * @param msg
     *            整包数据
     * 
     * @return packNum--包号；packLen--数据长度；packData--数据
     * @throws DataAsException
     *             异常信息
     */
    public static HashMap<String, Object> parseBigData(byte[] msg) throws DataAsException
    {
        HashMap<String, Object> hp = new HashMap<String, Object>();
        try
        {

            // 得到包号
            byte[] num = new byte[4];
            System.arraycopy(msg, 0, num, 0, 4);

            int packNum = DataParse.byteArrayToInt(num);
            // int packNum = Integer.parseInt(new
            // String(DataParse.intToByteArray(4), "UTF-8"));

            // 得到数据长度
            byte[] len = new byte[4];
            System.arraycopy(msg, 4, len, 0, 4);
            int packLen = DataParse.byteArrayToInt(len);

            // 得到数据
            byte[] packData = new byte[packLen];
            System.arraycopy(msg, 8, packData, 0, packLen);

            hp.put("packNum", packNum);
            hp.put("packLen", packLen);
            hp.put("packData", packData);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseBigData_1", null, null, exp);
        }
        return hp;
    }

    /**
     * 解析用户行为数据
     * 
     * @param netData
     *            数据
     * @return hashmap，包括opertype,operfunc,rectime,endtime,remark,userid,wfid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseUserBehaviorData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String opertype = "";
            String operfunc = "";
            String rectime = "";
            String endtime = "";
            String remark = "";
            String userid = "";
            String wfid = "";

            String[] arr = removeParenth(netData).split("\\|", -1);
            opertype = arr[1];
            operfunc = arr[2];
            rectime = arr[3];
            endtime = arr[4];
            remark = arr[5];
            userid = arr[6];
            wfid = arr[7];

            returnData.put("opertype", opertype);
            returnData.put("operfunc", operfunc);
            returnData.put("rectime", rectime);
            returnData.put("endtime", endtime);
            returnData.put("remark", remark);
            returnData.put("userid", userid);
            returnData.put("wfid", wfid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseUserBehaviorData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析服务器信息数据
     * 
     * @param netData
     *            服务器信息数据
     * @return ip--服务器ip;system--服务器系统;code--服务器机器码;disc--硬盘信息;cpu--cpu信息;memory--内存信息;agent--代理版本
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseServiceInfoData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String ip = "";
        String system = "";
        String code = "";
        String disc = "";
        String cpu = "";
        String memory = "";
        String agent = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            ip = arr[1];
            system = arr[2];
            code = arr[3];
            disc = arr[4];
            cpu = arr[5];
            memory = arr[6];
            agent = arr[7];

            returnData.put("ip", ip);
            returnData.put("system", system);
            returnData.put("code", code);
            returnData.put("disc", disc);
            returnData.put("cpu", cpu);
            returnData.put("memory", memory);
            returnData.put("agent", agent);

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseServiceInfoData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析wtcontrol数据
     * 
     * @param netData
     *            wtcontrol数据
     * @return wtid,flag,userid,info
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseWtControlData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String wtid = "";
        String flag = "";
        String userid = "";
        String info = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            wtid = arr[0];
            flag = arr[1];
            userid = arr[2];
            if (arr.length > 3)
            {
                info = arr[3];
            }

            returnData.put("wtid", wtid);
            returnData.put("flag", flag);
            returnData.put("userid", userid);
            returnData.put("info", info);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseWtControlData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析WtOtherControl数据
     * 
     * @param netData
     *            WtOtherControl数据
     * @return wtid,flag,parms,userid,info
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseWtOtherControlData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String wtid = "";
        String flag = "";
        String parm = "";
        String userid = "";
        String info = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            wtid = arr[0];
            flag = arr[1];
            parm = arr[2];
            userid = arr[3];
            if (arr.length > 4)
            {
                info = arr[4];
            }

            returnData.put("wtid", wtid);
            returnData.put("flag", flag);
            returnData.put("parm", parm);
            returnData.put("userid", userid);
            returnData.put("info", info);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseWtOtherControlData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析setdatavalue数据
     * 
     * @param netData
     *            setdatavalue数据
     * @return wtid,path,value,userid,controltype,info
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseSetDataValueData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String wtid = "";
        String path = "";
        String value = "";
        String userid = "";
        String controltype = "";
        String info = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            wtid = arr[0];
            path = arr[1];
            value = arr[2];
            userid = arr[3];
            controltype = arr[4];
            if (arr.length > 5)
            {
                info = arr[5];
            }

            returnData.put("wtid", wtid);
            returnData.put("path", path);
            returnData.put("value", value);
            returnData.put("userid", userid);
            returnData.put("controltype", controltype);
            returnData.put("info", info);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseSetDataValueData_1", null, null, exp);
        }

        return returnData;
    }

    // /**
    // * 解析需要拓传的数据，并数据数据类型返回数据
    // *
    // * @param data
    // * 原始数据
    // * @return 拓展数据
    // */
    // public static ExtensionData extData(String data, String channelId)
    // {
    // String extData = data;
    // // 拆分原始数据，获取所需拓传的下层节点
    // String ip = "";
    // int port = 0;
    // int isolation = 0;
    // // 是否为最后一级拓传节点
    // boolean isLast = false;
    // // 下级拓传信息
    // String firstAd = getFiretAddMsg(data).get("firstAd");
    // // 指令唯一码
    // String controlId = getFiretAddMsg(data).get("controlId");
    //
    // if (firstAd != null && firstAd.length() > 0)
    // {
    // String[] firstAdds = firstAd.split(":", -1);
    // if (firstAdds != null && firstAdds.length == 3)
    // {
    // ip = firstAdds[0];
    // port = Integer.parseInt(firstAdds[1]);
    // isolation = Integer.parseInt(firstAdds[2]);
    // if (!NetAddress.isIP(ip))
    // {
    // return null;
    // }
    // }
    // else
    // {
    // return null;
    // }
    // // 将原始数据中的数据下级拓传信息移除
    // if (data != null)
    // {
    // extData = data.replace(firstAd + ",", "");
    // }
    //
    // if (extData.equals(data))
    // {
    // extData = data.replace("|" + firstAd, "");
    // isLast = true;
    // }
    // }
    //
    // ExtensionData ext = new ExtensionData(ip, port, isolation, extData, channelId, controlId, isLast);
    // return ext;
    //
    // }

    /**
     * 解析需要拓传的数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     * @param dataType
     *            设备类型
     * @param wtid
     *            设备id
     */
    public static ExtensionData extOldData(String data, NetDownDataType dataType, String wtid)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 是否为最后一级拓传节点
        boolean isLast = false;
        boolean stream = false;
        // 下级拓传信息
        String firstAd = "";
        // 指令唯一码
        String controlId = "";

        // 解析数据
        HashMap<String, String> msg = getOldFirstAddMsg(data, dataType);

        firstAd = msg.get("firstAd");
        if (firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3 && NetAddress.isIP(firstAdds[0]))
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);

                // 将原始数据中的数据下级拓传信息移除
                if (data != null)
                {
                    extData = data.replace(firstAd + ",", "");
                }
                // 判断是否原始数据
                if (extData.equals(data))
                {
                    extData = data.replace("|" + firstAd, "");
                    isLast = true;
                }
                controlId = msg.get("controlId");
                stream = "0".equals(msg.get("stream")) ? false : true;
            }
        }
        ExtensionData ext = new ExtensionData(ip, port, isolation, extData, controlId, isLast, stream, wtid);
        return ext;
    }

    /**
     * 获取到下层拓传信息
     * 
     * @param data
     *            原始数据
     * @return 数据中拓传信息，包括第一层拓传路径firstAd、返回类型returnType、指令唯一码controlId
     */
    private static HashMap<String, String> getOldFirstAddMsg(String data, NetDownDataType dataType)
    {
        HashMap<String, String> hp = new HashMap<>();
        // 拓传路径
        String firstAd = "";
        // 指令唯一码
        String controlId = "";
        String stream = "0";
        data = removeParenth(data);
        String[] datas = data.split("\\|", -1);

        if (datas.length > 0)
        {
            firstAd = datas[datas.length - 1].split(",", -1)[0];
        }

        // 判断保信主站
        if ((dataType == NetDownDataType.GetProtectData && datas.length < 15) || (dataType == NetDownDataType.SetProtectData && datas.length < 16))
        {
            controlId = "";
        }
        else
        {
            if (datas.length > 1)
            {
                controlId = datas[datas.length - 2];
            }
        }
        if (dataType == NetDownDataType.GetProtectData)
        {
            stream = datas[11];
        }

        hp.put("firstAd", firstAd);
        hp.put("controlId", controlId);
        hp.put("stream", stream);
        return hp;
    }

    /**
     * 解析需要拓传的数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @param dataType
     *            数据类型
     * @param channelId
     *            通道id
     * @param wtid
     *            设备id
     * @return 拓展数据
     */
    public static ExtensionData extOldData(String data, NetDownDataType dataType, String channelId, String wtid)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 是否为最后一级拓传节点
        boolean isLast = false;
        boolean stream = false;

        HashMap<String, String> Msg = getOldFiretAddMsg(data, dataType);
        // 下级拓传信息
        String firstAd = Msg.get("firstAd");
        // 拓传类型
        String returnType = Msg.get("returnType");
        // 指令唯一码
        String controlId = Msg.get("controlId");
        // 是否返回流数据
        stream = "0".equals(Msg.get("stream")) ? false : true;

        if (firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
                if (!NetAddress.isIP(ip))
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
            // 将原始数据中的数据下级拓传信息移除
            if (data != null)
            {
                extData = data.replace(firstAd + ",", "");
            }

            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
                isLast = true;
            }
        }

        ExtensionData ext = null;
        switch (returnType) {
            case "0":
                ext = new ExtensionData(ip, port, isolation, extData, channelId, controlId, ReturnType.Syn, isLast, stream, wtid);
                break;
            case "1":
                ext = new ExtensionData(ip, port, isolation, extData, channelId, controlId, ReturnType.Asyn, isLast, stream, wtid);
                break;
            default:
                ext = new ExtensionData(ip, port, isolation, extData, channelId, controlId, ReturnType.Syn, isLast, stream, wtid);
                break;
        }
        return ext;
    }

    /**
     * 获取到下层拓传信息
     * 
     * @param data
     *            原始数据
     * @return 数据中拓传信息，包括第一层拓传路径firstAd、返回类型returnType、指令唯一码controlId
     */
    private static HashMap<String, String> getOldFiretAddMsg(String data, NetDownDataType dataType)
    {
        HashMap<String, String> hp = new HashMap<>();
        // 拓传路径
        String firstAd = "";
        // 返回结果方式
        String returnType = "0";
        // 指令唯一码
        String controlId = "";
        String stream = "0";
        data = removeParenth(data);
        String[] datas = data.split("\\|", -1);

        if (datas.length > 0)
        {
            firstAd = datas[datas.length - 1].split(",", -1)[0];
        }

        // 判断保信主站
        if ((dataType == NetDownDataType.GetProtectData && datas.length < 15) || (dataType == NetDownDataType.SetProtectData && datas.length < 16))
        {
            controlId = "";
            returnType = "0";
        }
        else
        {
            if (datas.length > 1)
            {
                controlId = datas[datas.length - 2];
                if (!"".equals(controlId))
                {
                    returnType = "1";
                }
            }
        }
        if (dataType == NetDownDataType.GetProtectData)
        {
            stream = datas[11];
        }

        hp.put("firstAd", firstAd);
        hp.put("returnType", returnType);
        hp.put("controlId", controlId);
        hp.put("stream", stream);
        return hp;
    }

    /**
     * 解析需要拓传的wtothercontrol数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     */
    public static ExtensionData extWtOtherControlData(String data)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;

        // 下级拓传信息
        String firstAd = getFiretAddMsg(data).get("firstAd");
        if (data != null && firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
            }
            // 将原始数据中的数据下级拓传信息移除
            if (data != null)
            {
                extData = data.replace(firstAd + ",", "");
            }
            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
            }
        }
        return new ExtensionData(ip, port, isolation, extData);
    }

    /**
     * 解析需要拓传的setdatavalue数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     */
    public static ExtensionData extSetDataValueData(String data)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 下级拓传信息
        String firstAd = getFiretAddMsg(data).get("firstAd");
        if (data != null && firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
            }
            // 将原始数据中的数据下级拓传信息移除
            if (data != null)
            {
                extData = data.replace(firstAd + ",", "");
            }
            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
            }
        }
        return new ExtensionData(ip, port, isolation, extData);
    }

    /**
     * 解析需要拓传的getpackdata数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     */
    public static ExtensionData extGetPackData(String data)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 下级拓传信息
        String firstAd = getFiretAddMsg(data).get("firstAd");
        if (data != null && firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
            }
            // 将原始数据中的数据下级拓传信息移除
            extData = data.replace(firstAd + ",", "");
            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
            }
        }
        return new ExtensionData(ip, port, isolation, extData);
    }

    /**
     * 获取到下层拓传信息
     * 
     * @param data
     *            原始数据
     * @return 数据中拓传信息，包括第一层拓传路径firstAd、返回类型returnType、指令唯一码controlId
     */
    private static HashMap<String, String> getFiretAddMsg(String data)
    {
        HashMap<String, String> hp = new HashMap<>();
        // 拓传路径
        String firstAd = "";
        // 返回结果方式
        String returnType = "";
        // 指令唯一码
        String controlId = "";
        data = removeParenth(data);
        String[] datas = data.split("\\|", -1);

        if (datas.length > 0)
        {
            firstAd = datas[datas.length - 1].split(",", -1)[0];
        }
        if (datas.length > 1)
        {
            returnType = datas[datas.length - 2];
        }
        if (datas.length > 2)
        {
            controlId = datas[datas.length - 3];
        }
        hp.put("firstAd", firstAd);
        hp.put("returnType", returnType);
        hp.put("controlId", controlId);
        return hp;
    }

    // /**
    // * 解析通讯协议数据
    // *
    // * @param netData
    // * 数据
    // * @return hashmap,包括datatype,statid,devid,cpuid,groupid,itemid,fun,inf,time,data,ordernum,iscache,protocolid
    // * @throws DataAsException
    // * 自定义异常
    // */
    // public static HashMap<String, Object> parseProtectData(String netData) throws DataAsException
    // {
    // HashMap<String, Object> returnData = new HashMap<String, Object>();
    // String datatype = "";
    // String statid = "";
    // String devid = "";
    // String cpuid = "";
    // String groupid = "";
    // String itemid = "";
    // String fun = "";
    // String inf = "";
    // String time = "";
    // String data = "";
    // String ordernum = "";
    // String iscache = "";
    // String protocolid = "";
    //
    // try
    // {
    // String[] arr = removeParenth(netData).split("\\|", -1);
    // datatype = arr[1];
    // statid = arr[2];
    // devid = arr[3];
    // cpuid = arr[4];
    // groupid = arr[5];
    // itemid = arr[6];
    // fun = arr[7];
    // inf = arr[8];
    // time = arr[9];
    // data = arr[10];
    // ordernum = arr[11];
    // iscache = arr[12];
    // protocolid = arr[13];
    //
    // returnData.put("datatype", datatype);
    // returnData.put("statid", statid);
    // returnData.put("devid", devid);
    // returnData.put("cpuid", cpuid);
    // returnData.put("groupid", groupid);
    // returnData.put("itemid", itemid);
    // returnData.put("fun", fun);
    // returnData.put("inf", inf);
    // returnData.put("time", time);
    // returnData.put("data", data);
    // returnData.put("ordernum", ordernum);
    // returnData.put("iscache", iscache);
    // returnData.put("protocolid", protocolid);
    // }
    // catch (Exception exp)
    // {
    // DataAsExpSet.throwExpByResCode("DataParse_parseProtectData_1", null, null, exp);
    // }
    //
    // return returnData;
    // }

    /**
     * 解析文件上送信息数据
     * 
     * @param netData
     *            原始数据
     * @return hashmap,包括 callid,calltype,packcount,iszip,savefile,parms,uuid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseProtectStreamData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String callid = "";
        String calltype = "";
        String packcount = "";
        String iszip = "";
        String savefile = "";
        String parms = "";
        String uuid = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            callid = arr[1];
            calltype = arr[2];
            packcount = arr[3];
            iszip = arr[4];
            savefile = arr[5];
            parms = arr[6];

            returnData.put("callid", callid);
            returnData.put("calltype", calltype);
            returnData.put("packcount", packcount);
            returnData.put("iszip", iszip);
            returnData.put("savefile", savefile);
            returnData.put("parms", parms);

            if (arr.length > 7)
            {
                uuid = arr[7];
                returnData.put("uuid", uuid);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseProtectStramData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析召唤日志
     * 
     * @param netData
     *            原始数据
     * @return hashmap,包含calltype,calllid,userid,rectime,msg,logtype,statid,devid,cupid,regionid,groupid,grouptype,infos,returntype,dataformat,savefile,ordernum,iscache,protocolid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseProtectCallLogData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String calltype = "";
        String calllid = "";
        String userid = "";
        String rectime = "";
        String msg = "";
        String logtype = "";
        String statid = "";
        String devid = "";
        String cupid = "";
        String regionid = "";
        String groupid = "";
        String grouptype = "";
        String infos = "";
        String returntype = "";
        String dataformat = "";
        String savefile = "";
        String ordernum = "";
        String iscache = "";
        String protocolid = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            calltype = arr[1];
            calllid = arr[2];
            userid = arr[3];
            rectime = arr[4];
            msg = arr[5];
            logtype = arr[6];
            statid = arr[7];
            devid = arr[8];
            cupid = arr[9];
            regionid = arr[10];
            groupid = arr[11];
            grouptype = arr[12];
            infos = arr[13];
            returntype = arr[14];
            dataformat = arr[15];
            savefile = arr[16];
            ordernum = arr[17];
            iscache = arr[18];
            protocolid = arr[19];

            returnData.put("calltype", calltype);
            returnData.put("calllid", calllid);
            returnData.put("userid", userid);
            returnData.put("rectime", rectime);
            returnData.put("msg", msg);
            returnData.put("logtype", logtype);
            returnData.put("statid", statid);
            returnData.put("devid", devid);
            returnData.put("cupid", cupid);
            returnData.put("regionid", regionid);
            returnData.put("groupid", groupid);
            returnData.put("grouptype", grouptype);
            returnData.put("infos", infos);
            returnData.put("returntype", returntype);
            returnData.put("dataformat", dataformat);
            returnData.put("savefile", savefile);
            returnData.put("ordernum", ordernum);
            returnData.put("iscache", iscache);
            returnData.put("protocolid", protocolid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseProtectCallLogData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 控制日志解析
     * 
     * @param netData
     *            原始数据
     * @return hashmap,包含setid,userid,rectime,msg,logtype,settype,setmodel,statid,devid,cupid,regionid,groupid,grouptype,itemid,valuetype,value,info,ordernum,returntype,iscache,protocolid
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseProtectSetLogData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String setid = "";
        String userid = "";
        String rectime = "";
        String msg = "";
        String logtype = "";
        String settype = "";
        String setmodel = "";
        String statid = "";
        String devid = "";
        String cupid = "";
        String regionid = "";
        String groupid = "";
        String grouptype = "";
        String itemid = "";
        String valuetype = "";
        String value = "";
        String info = "";
        String returntype = "";
        String ordernum = "";
        String iscache = "";
        String protocolid = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            setid = arr[1];
            userid = arr[2];
            rectime = arr[3];
            msg = arr[4];
            logtype = arr[5];
            settype = arr[6];
            setmodel = arr[7];
            statid = arr[8];
            devid = arr[9];
            cupid = arr[10];
            regionid = arr[11];
            groupid = arr[12];
            grouptype = arr[13];
            itemid = arr[14];
            valuetype = arr[15];
            value = arr[16];
            info = arr[17];
            returntype = arr[18];
            ordernum = arr[19];
            iscache = arr[20];
            protocolid = arr[21];

            returnData.put("setid", setid);
            returnData.put("userid", userid);
            returnData.put("rectime", rectime);
            returnData.put("msg", msg);
            returnData.put("logtype", logtype);
            returnData.put("settype", settype);
            returnData.put("setmodel", setmodel);
            returnData.put("statid", statid);
            returnData.put("devid", devid);
            returnData.put("cupid", cupid);
            returnData.put("regionid", regionid);
            returnData.put("groupid", groupid);
            returnData.put("grouptype", grouptype);
            returnData.put("itemid", itemid);
            returnData.put("valuetype", valuetype);
            returnData.put("value", value);
            returnData.put("info", info);
            returnData.put("returntype", returntype);
            returnData.put("ordernum", ordernum);
            returnData.put("iscache", iscache);
            returnData.put("protocolid", protocolid);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseProtectSetLogData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析召唤指令报文数据
     * 
     * @param netData
     *            原始网络报文数据
     * @return hashmap,包含calltype,callid,userid,statid,devid,cpuid,regionid,groupid,grouptype,info,returntype,returntype,dataformat,savefile
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGetProtectData(String netData) throws DataAsException
    {
        // getprotectdata(calltype|callid|userid|statid|devid|cpuid|regionid|groupid|grouptype|info1,info2…|returntype|dataformat|savefile)
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String calltype = "";
        String callid = "";
        String userid = "";
        String statid = "";
        String devid = "";
        String cpuid = "";
        String regionid = "";
        String groupid = "";
        String grouptype = "";
        String info = "";
        String returntype = "";
        String dataformat = "";
        String savefile = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            calltype = arr[0];
            callid = arr[1];
            userid = arr[2];
            statid = arr[3];
            devid = arr[4];
            cpuid = arr[5];
            regionid = arr[6];
            groupid = arr[7];
            grouptype = arr[8];
            info = arr[9];
            returntype = arr[10];
            dataformat = arr[11];
            savefile = arr[12];

            returnData.put("calltype", calltype);
            returnData.put("callid", callid);
            returnData.put("userid", userid);
            returnData.put("statid", statid);
            returnData.put("devid", devid);
            returnData.put("cpuid", cpuid);
            returnData.put("regionid", regionid);
            returnData.put("groupid", groupid);
            returnData.put("grouptype", grouptype);
            returnData.put("info", info);
            returnData.put("returntype", returntype);
            returnData.put("dataformat", dataformat);
            returnData.put("savefile", savefile);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGetProtectData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 控制指令报文数据
     * 
     * @param netData
     *            原始数据
     * @return hashmap,包含setid,userid,settype,setmodel,statid,devid,cpuid,regionid,groupid,grouptype,itemid,valuetype,value,info
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseProtectSetData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();

        // setprotectdata(setid|userid|settype|setmodel|statid|devid|cpuid|regionid|groupid|grouptype|itemid|valuetype|value|info1,info2…)
        String setid = "";
        String userid = "";
        String settype = "";
        String setmodel = "";
        String statid = "";
        String devid = "";
        String cpuid = "";
        String regionid = "";
        String groupid = "";
        String grouptype = "";
        String itemid = "";
        String valuetype = "";
        String value = "";
        String info = "";

        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            setid = arr[0];
            userid = arr[1];
            settype = arr[2];
            setmodel = arr[3];
            statid = arr[4];
            devid = arr[5];
            cpuid = arr[6];
            regionid = arr[7];
            groupid = arr[8];
            grouptype = arr[9];
            itemid = arr[10];
            valuetype = arr[11];
            value = arr[12];
            info = arr[13];

            returnData.put("setid", setid);
            returnData.put("userid", userid);
            returnData.put("settype", settype);
            returnData.put("setmodel", setmodel);
            returnData.put("statid", statid);
            returnData.put("devid", devid);
            returnData.put("cupid", cpuid);
            returnData.put("regionid", regionid);
            returnData.put("groupid", groupid);
            returnData.put("grouptype", grouptype);
            returnData.put("itemid", itemid);
            returnData.put("valuetype", valuetype);
            returnData.put("value", value);
            returnData.put("info", info);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseProtectSetLogData_1", null, null, exp);
        }

        return returnData;
    }

    /**
     * 解析需要拓传的召唤指令数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     */
    public static ExtensionData extGetProtectData(String data)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 下级拓传信息
        String firstAd = getFiretAddMsg(data).get("firstAd");
        if (firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
            }
            // 将原始数据中的数据下级拓传信息移除
            extData = data.replace(firstAd + ",", "");
            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
            }
        }
        return new ExtensionData(ip, port, isolation, extData);
    }

    /**
     * 解析需要拓传的控制指令数据，并数据数据类型返回数据
     * 
     * @param data
     *            原始数据
     * @return 拓展数据
     */
    public static ExtensionData extSetProtectData(String data)
    {
        String extData = data;
        // 拆分原始数据，获取所需拓传的下层节点
        String ip = "";
        int port = 0;
        int isolation = 0;
        // 下级拓传信息
        String firstAd = getFiretAddMsg(data).get("firstAd");
        if (firstAd != null && firstAd.length() > 0)
        {
            String[] firstAdds = firstAd.split(":", -1);
            if (firstAdds != null && firstAdds.length == 3)
            {
                ip = firstAdds[0];
                port = Integer.parseInt(firstAdds[1]);
                isolation = Integer.parseInt(firstAdds[2]);
            }
            // 将原始数据中的数据下级拓传信息移除
            extData = data.replace(firstAd + ",", "");
            if (extData.equals(data))
            {
                extData = data.replace("|" + firstAd, "");
            }
        }
        return new ExtensionData(ip, port, isolation, extData);
    }

    /**
     * 解析告警服务组播的服务器状态包
     * 
     * @param serverstate
     *            服务器状态包
     * @return 服务器状态信息:srvid-服务器的硬件编号,state-服务器状态 0 正常 1 数据中断 2 网络中断
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, String> parseServerState(String serverstate) throws DataAsException
    {
        HashMap<String, String> returnData = new HashMap<String, String>();
        String srvid = "";
        String state = "";
        try
        {
            String[] arr = removeParenth(serverstate).split("\\|", -1);
            srvid = arr[1].trim();
            state = arr[2].trim();
            returnData.put("srvid", srvid);
            returnData.put("state", state);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseServerState_1", new String[] { "serverstate" }, new Object[] { serverstate }, exp);
        }
        return returnData;
    }

    /**
     * 解析控制返回结果（包含拓传过程错误结果）
     * 
     * @param netData
     *            controlresult数据
     * @return result,controlid,returntype
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseControlresultData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        String result = "";
        String controlid = "";
        String returntype = "";

        try
        {
            if (netData != null && !netData.isEmpty())
            {
                // 去除左右括号
                String data = removeParenth(netData);

                String[] arr = data.split("\\|", -1);
                if (data.startsWith("controlresult"))
                {
                    if (arr.length > 1)
                    {
                        result = "(" + arr[1] + ")";
                    }

                    if (arr.length > 2)
                    {
                        controlid = arr[2];
                    }

                    if (arr.length > 3)
                    {
                        returntype = arr[3];
                    }
                }
                else
                {
                    result = netData;
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseControlresultData_1", null, null, exp);
        }

        returnData.put("result", result);
        returnData.put("controlid", controlid);
        returnData.put("returntype", returntype);

        return returnData;
    }

    /**
     * 处理结果数据
     * 
     * @param netData
     *            结果数据
     * @return 包含result,controlId
     */
    public static HashMap<String, String> parseResultData(String netData)
    {
        HashMap<String, String> returnData = new HashMap<>();
        String result = "";
        String controlId = "";
        if (netData != null && !netData.isEmpty())
        {
            // 去除左右括号
            String data = removeParenth(netData);
            String[] arr = data.split("\\|", -1);
            if (arr.length > 1)
            {
                controlId = arr[arr.length - 1];
                result = netData.replace("|" + controlId, "");
            }
        }

        returnData.put("result", result);
        returnData.put("controlId", controlId);
        return returnData;
    }

    /**
     * 解析功率曲线
     * 
     * @param netData
     *            消息
     * @return hashmap，包括deviceId、speed、power和temp 20180508wrb添加totalnum(数据总条数)、usenum(有效个数)
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseEnvTemperatureCurve(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String deviceId;
            String speed;
            String power;
            String temp;
            String totalnum = "";
            String usenum = "";
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1];
            String[] data = arr[2].split(",", -1);

            speed = data[0].trim();
            power = data[1].trim();
            temp = data[2].trim();
            if (arr.length > 3)
            {
                String[] num = arr[3].split(",", -1);
                totalnum = num[0].trim();
                usenum = num[1].trim();
            }
            returnData.put("deviceId", deviceId);
            returnData.put("speed", speed);
            returnData.put("power", power);
            returnData.put("temp", temp);
            returnData.put("totalnum", totalnum);
            returnData.put("usenum", usenum);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseEnvTemperatureCurve_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析gps数据
     * 
     * @author wangruibo
     * @param netData
     *            消息
     * @return hashmap，包括time、company、userid、username、wfid、lat、lng
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGPSData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            // 数据格式是 (gpsdata|time|company|userid|username|wfid|lat|lng)
            String[] arr = removeParenth(netData).split("\\|", -1);
            // 时间
            String rectime = arr[1].trim();
            // 公司代码
            String company = arr[2].trim();
            // 用户代码
            String userid = arr[3].trim();
            // 用户名称
            String username = arr[4].trim();
            // 风场名称
            // String wfid = arr[5].trim();
            // 纬度
            String lat = arr[6].trim();
            // 经度
            String lng = arr[7].trim();
            returnData.put("fulldata", netData);
            returnData.put("rectime", rectime);
            returnData.put("company", company);
            returnData.put("userid", userid);
            returnData.put("username", username);
            // returnData.put("wfid", wfid); //Go+上传的wfid无效，不使用 xjs 2019-5-22 11:34:48
            returnData.put("lat", lat);
            returnData.put("lng", lng);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGPSData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析监控数据
     * 
     * @author wangruibo
     * @param netData
     *            消息
     * @return hashmap，包括objectid、rectime、d1,d2,d3… 其中d1,d2,d3…为String[]形式返回
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseMonData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            // 数据格式是 (mondata|objectid|rectime|d1,d2,d3…)
            String[] arr = removeParenth(netData).split("\\|", -1);
            // 监控对象代码
            String objectid = arr[1].trim();
            // 时间
            String rectime = arr[2].trim();
            // 数据
            String[] datas = arr[3].trim().split(",", -1);

            returnData.put("rectime", rectime);
            returnData.put("objectid", objectid);
            returnData.put("datas", datas);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseMonData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析go+上送用户数据
     * 
     * @author wangruibo
     * @param netData
     *            消息
     * @return hashmap，包括userid、realname、mobile、dept、position、company、wfid、delflag(1--delete;0--add or update)
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseGoUserData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            // 数据格式是 (gouserdata|userid|realname|mobile|dept|position|company|wfid|delflag)
            // (gouserdata|go+用户id|姓名|电话|部门|职位|公司|所属风场id|删除标记（1为删除，0为更新或增加）)
            String[] arr = removeParenth(netData).split("\\|", -1);
            // 用户id
            String userid = arr[1].trim();
            // 用户名称
            String realname = arr[2].trim();
            // 电话
            String mobile = arr[3].trim();
            // 部门
            String dept = arr[4].trim();
            // 职位
            String position = arr[5].trim();
            // 公司
            String company = arr[6].trim();
            // 风场id
            String groupId = arr[7].split(",", -1)[0].trim();
            // 删除标志
            String delflag = "0";
            if (arr.length >= 9)
            {
                // 删除标志
                delflag = arr[8].trim();
            }
            returnData.put("userid", userid);
            returnData.put("username", realname);
            returnData.put("mobile", mobile);
            returnData.put("dept", dept);
            returnData.put("positions", position);
            returnData.put("company", company);
            returnData.put("groupid", groupId);
            returnData.put("delflag", delflag);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseGoUserData_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析虚拟遥测点数据
     * 
     * @param netData
     *            信息
     * @return hashmap，包括deviceId;rectime;iecPath;data
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseVirtualTelemetryData(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            // (virtualtelemetry|deviceid|rectime|iecpath|virtualdata)
            String deviceId;
            String iecPath;
            String data;
            String rectime = "";
            String[] arr = removeParenth(netData).split("\\|", -1);
            deviceId = arr[1].trim();
            rectime = arr[2].trim();
            iecPath = arr[3].trim();
            data = arr[4].trim();
            returnData.put("deviceId", deviceId);
            returnData.put("rectime", rectime);
            returnData.put("iecPath", iecPath);
            returnData.put("data", data);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseVirtualTelemetry_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析程序通讯状态数据
     * 
     * @param netData
     *            消息
     * @return hashmap key--wfId,ip,serviceType,dataType
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, Object> parseComtest(String netData) throws DataAsException
    {
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        try
        {
            String[] arr = removeParenth(netData).split("\\|", -1);
            if (arr.length > 1)
            {
                String wfId = arr[0].trim();
                String ip = arr[1].trim();
                String serviceType = arr[2].trim();
                String dataType = arr[3].trim();
                returnData.put("wfId", wfId);
                returnData.put("ip", ip);
                returnData.put("serviceType", serviceType);
                returnData.put("dataType", dataType);
            }

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseComtest_1", new String[] { "netData" }, new Object[] { netData }, exp);
        }
        return returnData;
    }

    /**
     * 解析pfr文件列表数据
     * 
     * @param ffrFileData
     *            (ffrfile|wtid|userid|filename1,filename2,filename3…|uuid)
     * @return wtId--wtId;userId--userId;fileName--(List<String>)fileName;uuid--uuid
     * @throws DataAsException
     *             自定义异常
     */
    public static Map<String, Object> parseFfrFileData(String ffrFileData) throws DataAsException
    {
        Map<String, Object> returnData = new HashMap<>();
        try
        {
            String[] arr = removeParenth(ffrFileData).split("\\|", -1);
            if (arr.length > 1)
            {
                String wtId = arr[0].trim();
                String userId = arr[1].trim();
                List<String> fileName = Arrays.asList(arr[2].trim().split(","));
                String uuid = arr[3].trim();
                returnData.put("wtId", wtId);
                returnData.put("userId", userId);
                returnData.put("fileName", fileName);
                returnData.put("uuid", uuid);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseFfrFileData_1", new String[] { "fileName" }, new Object[] { ffrFileData }, exp);
        }
        return returnData;
    }

    /**
     * 解析pfr文件流包头数据
     * 
     * @param ffrfiledatastream
     *            (ffrfiledatastream|wtid|calltype|packcount|zipflag|param1,param2…|uuid)
     * @return wtId--wtId;callType--callType;packCount--packCount;zipflag--zipflag;parms--parms;uuid-uuid;
     * @throws DataAsException
     *             自定义异常
     */
    public static Map<String, Object> parseFfrFileDataStream(String ffrfiledatastream) throws DataAsException
    {
        Map<String, Object> returnData = new HashMap<>();
        try
        {
            // (ffrfiledatastream|wtid|calltype|packcount|zipflag|param1,param2…|uuid)
            String[] arr = removeParenth(ffrfiledatastream).split("\\|", -1);
            if (arr.length > 1)
            {
                String wtId = arr[0].trim();
                String callType = arr[1].trim();
                int packCount = Integer.parseInt(arr[2].trim());
                boolean zipflag = Integer.parseInt(arr[3].trim()) == 0 ? false : true;
                String parms = arr[4].trim();
                String uuid = arr[5].trim();
                returnData.put("wtId", wtId);
                returnData.put("callType", callType);
                returnData.put("packCount", packCount);
                returnData.put("zipflag", zipflag);
                returnData.put("parms", parms);
                returnData.put("uuid", uuid);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseFfrFileDataStream_1", new String[] { "ffrfiledatastream" }, new Object[] { ffrfiledatastream }, exp);
        }
        return returnData;
    }

    // (ffrcalllog|wtid|calltype|userid|rectime|msg|logtype|info1,info2…|uuid)

    /**
     * 解析pfr文件召唤日志数据
     * 
     * @param ffrcalllog
     *            (ffrcalllog|wtid|calltype|userid|rectime|msg|logtype|info1,info2…|uuid)
     * @return wtId--wtId;callType--callType;userId--userId;recTime--recTime;msg--msg;logType-logType;info--info;uuid--uuid
     * @throws DataAsException
     *             自定义异常
     */
    public static Map<String, Object> parseFfrCallLog(String ffrcalllog) throws DataAsException
    {
        Map<String, Object> returnData = new HashMap<>();
        try
        {
            // (ffrcalllog|wtid|calltype|userid|rectime|msg|logtype|info1,info2…|uuid)
            String[] arr = removeParenth(ffrcalllog).split("\\|", -1);
            if (arr.length > 1)
            {
                String wtId = arr[0].trim();
                String callType = arr[1].trim();
                String userId = arr[2].trim();
                String recTime = arr[3].trim();
                String msg = arr[4].trim();
                String logType = arr[5].trim();
                String info = arr[6].trim();
                String uuid = arr[7].trim();
                returnData.put("wtId", wtId);
                returnData.put("callType", callType);
                returnData.put("userId", userId);
                returnData.put("recTime", recTime);
                returnData.put("msg", msg);
                returnData.put("logType", logType);
                returnData.put("info", info);
                returnData.put("uuid", uuid);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseFfrCallLog_1", new String[] { "ffrcalllog" }, new Object[] { ffrcalllog }, exp);
        }
        return returnData;
    }

    /**
     * 解析网络设备对象数据
     * 
     * @param monitorjson
     *            (monitorjson|rectime|jsondata)
     * @return rectime--rectime;jsondata--jsaondata(List<String>)
     * @throws DataAsException
     *             自定义异常
     */
    public static Map<String, Object> parseMonitorJson(String monitorjson) throws DataAsException
    {
        Map<String, Object> returnData = new HashMap<>();
        try
        {
            // (monitorjson|rectime|jsondata)
            String[] arr = removeParenth(monitorjson).split("\\|", -1);
            if (arr.length > 1)
            {
                String rectime = arr[1].trim();
                String data = arr[2].trim();
                // 判断数据是是否包含在括号中，如果包含则解析，不包含表示不满足格式
                List<String> jsondata = data.matches("\\[(.*?)]") ? JSONArray.parseArray(data, String.class) : new ArrayList<>();
                returnData.put("rectime", rectime);
                returnData.put("jsondata", jsondata);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseMonitorJson_1", new String[] { "ffrcalllog" }, new Object[] { monitorjson }, exp);
        }
        return returnData;
    }
}
