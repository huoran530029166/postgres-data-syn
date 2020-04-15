package com.goldwind.datalogic.business;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.NetCommDef.PlcFileType;

/**
 * 文件名管理类
 * 
 * @author 曹阳
 *
 */
public class FileNameManage
{
    /**
     * 根据ftp路径或本地路径返回路径符号
     * 
     * @param path
     *            路径
     * @return 路径符号
     */
    public static String getPathSymbol(String path)
    {
        return "/";
    }

    /**
     * 获取设备id，判断路径是否使用风场
     * 
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @return 设备id
     */
    public static String getFileDevId(String wfId, String deviceId)
    {
        if (!BusinessDef.isFILEPATHUSEWF())
        {
            return deviceId;
        }
        else
        {
            return wfId + "." + deviceId;
        }
    }

    /**
     * 解析文件名称
     * 
     * @param fileName
     *            文件名
     * @return hashmap，包括wfId,deviceId,dataDate
     * @throws DataAsException
     *             自定义异常
     */
    public static HashMap<String, String> parseFileName(String fileName) throws DataAsException
    {
        HashMap<String, String> returnData = new HashMap<String, String>();
        try
        {
            String wfId;
            String deviceId;
            String dataDate = null;
            String[] arr = fileName.substring(0, fileName.lastIndexOf(".")).split("_");
            if (BusinessDef.isFILEPATHUSEWF())
            {
                String[] devArr = arr[1].split("\\.");
                wfId = devArr[0];
                deviceId = devArr[1];
            }
            else
            {
                wfId = "";
                deviceId = arr[1];
            }

            if (arr.length > 2)
            {
                dataDate = arr[2];
            }
            returnData.put("wfId", wfId);
            returnData.put("deviceId", deviceId);
            returnData.put("dataDate", dataDate);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileNameManage_parseFileName_1", new String[] { "fileName" }, new Object[] { fileName }, exp);
        }
        return returnData;
    }

    /**
     * 根据文件名得到plc文件类型
     * 
     * @param fileName
     *            plc文件名
     * @return plc文件类型
     */
    public static PlcFileType getPlcFileTypeByName(String fileName)
    {
        if (fileName.isEmpty())
        {
            return PlcFileType.Unknown;
        }
        String[] arr = fileName.substring(0, fileName.lastIndexOf(".")).split("_");
        PlcFileType pft = PlcFileType.Unknown;
        switch (arr[0].toLowerCase()) {
            case "plcb":
                pft = PlcFileType.B;
                break;
            case "plcfh":
                pft = PlcFileType.Fhtml;
                break;
            case "plcft":
                pft = PlcFileType.Ftxt;
                break;
            case "plcm":
                pft = PlcFileType.M;
                break;
            case "plca":
                pft = PlcFileType.Action;
                break;
            case "plco":
                pft = PlcFileType.Operation;
                break;
            case "plchfb":
                pft = PlcFileType.Hfb;
                break;
            case "plcw":
                pft = PlcFileType.W;
                break;
            case "plce":
                pft = PlcFileType.E;
                break;
            case "plcc":
                pft = PlcFileType.C;
                break;
            case "plcfc":
                pft = PlcFileType.Fc;
                break;
            case "plct":
                pft = PlcFileType.T;
                break;
            case "plcs":
                pft = PlcFileType.S;
                break;
            default:
                pft = PlcFileType.Unknown;
        }
        return pft;
    }

    /**
     * 得到十分钟文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 十分钟文件路径
     */
    public static String getTenMinFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "TenMinutes";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到十分钟文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 十分钟文件名称
     */
    public static String getTenMinFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getTenMinFilePath(rootDir, wfId, dataDate) + symbol + "ten_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到十分钟文件名称
     * 
     * @param rootDir
     *            根目录
     * @param fileName
     *            文件名
     * @return 十分钟文件名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getTenMinFileName(String rootDir, String fileName) throws DataAsException
    {
        HashMap<String, String> hm = parseFileName(fileName);
        String wfId = hm.get("wfId");
        String deviceId = hm.get("deviceId");
        String dataDate = hm.get("dataDate");
        return getTenMinFileName(rootDir, wfId, deviceId, dataDate);
    }

    /**
     * 得到新增一分钟文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 新增一分钟文件路径
     */
    public static String getOneMinDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "OneMinData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 8);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到五分钟文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 新增五分钟文件路径
     */
    public static String getFiveMinDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "FiveMinData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到故障录波文件路径
     * 
     * @param rootDir
     *            根目录
     * 
     * @return 故障录波文件路径
     */
    public static String getGZLBFilePath(String rootDir)
    {
        String symbol = getPathSymbol(rootDir);

        return rootDir + symbol + "gzlb";
    }

    /**
     * 得到新增一分钟文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 新增一分钟文件名称
     */
    public static String getOneMinDataFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getOneMinDataFilePath(rootDir, wfId, dataDate) + symbol + "oneMin_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 8) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到五分钟文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 新增五分钟文件名称
     */
    public static String getFiveMinDataFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getFiveMinDataFilePath(rootDir, wfId, dataDate) + symbol + "fiveMin_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到归档的十分钟文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 归档十分钟文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcTenMinFileName(String rootDir, String wfId, String deviceId, String dataDate) throws DataAsException
    {
        return getArcFileName(getTenMinFileName(rootDir, wfId, deviceId, dataDate));
    }

    /**
     * 得到功率曲线文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getPowCurveFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getPowCurveFilePath(rootDir, wfId, dataDate) + symbol + "pow_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到功率曲线文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getPowCurveFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "PowerCurve";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }

        return val;
    }

    /**
     * 得到归档的功率曲线文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 功率曲线文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcPowCurFName(String rootDir, String wfId, String deviceId, String dataDate) throws DataAsException
    {
        return getArcFileName(getPowCurveFileName(rootDir, wfId, deviceId, dataDate));
    }

    /**
     * 得到设备故障文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getDevFaultFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getDevFaultFilePath(rootDir, wfId, dataDate) + symbol + "devflt_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到设备故障文件名称
     * 
     * @param rootDir
     *            根目录
     * @param fileName
     *            文件名
     * @return 故障文件名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDevFaultFileName(String rootDir, String fileName) throws DataAsException
    {
        HashMap<String, String> hm = parseFileName(fileName);
        String wfId = hm.get("wfId");
        String deviceId = hm.get("deviceId");
        String dataDate = hm.get("dataDate");
        return getDevFaultFileName(rootDir, wfId, deviceId, dataDate);
    }

    /**
     * 得到设备故障文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 故障文件路径
     */
    public static String getDevFaultFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "DeviceFault";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到归档的设备故障文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcDevFltFileName(String rootDir, String wfId, String deviceId, String dataDate) throws DataAsException
    {
        return getArcFileName(getDevFaultFileName(rootDir, wfId, deviceId, dataDate));
    }

    /**
     * 得到设备状态文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getDevStatusFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getDevStatusFilePath(rootDir, wfId, dataDate) + symbol + "devstatus_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到设备状态文件名称
     * 
     * @param rootDir
     *            根目录
     * @param fileName
     *            文件名
     * @return 文件名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDevStatusFileName(String rootDir, String fileName) throws DataAsException
    {
        HashMap<String, String> hm = parseFileName(fileName);
        String wfId = hm.get("wfId");
        String deviceId = hm.get("deviceId");
        String dataDate = hm.get("dataDate");
        return getDevStatusFileName(rootDir, wfId, deviceId, dataDate);
    }

    /**
     * 得到设备状态文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getDevStatusFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "DeviceStatus";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到归档的设备状态文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcDevStaFileName(String rootDir, String wfId, String deviceId, String dataDate) throws DataAsException
    {
        return getArcFileName(getDevStatusFileName(rootDir, wfId, deviceId, dataDate));
    }

    /**
     * 得到瞬态数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getRealDataFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getRealDataFilePath(rootDir, wfId, dataDate) + symbol + "real_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 8) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到瞬态数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @param fileName
     *            文件名
     * @return 文件名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getRealDataFileName(String rootDir, String fileName) throws DataAsException
    {
        HashMap<String, String> hm = parseFileName(fileName);
        String wfId = hm.get("wfId");
        String deviceId = hm.get("deviceId");
        String dataDate = hm.get("dataDate");
        return getRealDataFileName(rootDir, wfId, deviceId, dataDate);
    }

    /**
     * 得到瞬态数据文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getRealDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "RealtimeData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 8);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到用户行为数据表名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getUserBehaviorFileName(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getUserBehaviorDataFilePath(rootDir, wfId, dataDate) + symbol + "user_" + wfId + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到用户行为数据文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getUserBehaviorDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "UserBehaviorData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到虚拟测点数据表名称
     * 
     * @param rootDir
     *            根目录
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getVirtualTelemetryFileName(String rootDir, String wtId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getVirtualTelemetryFilePath(rootDir, wtId, dataDate) + symbol + "virtualtelemetry_" + wtId + "_" + dataDate.replace("-", "").substring(0, 8) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到虚拟遥测量测点数据文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wtId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getVirtualTelemetryFilePath(String rootDir, String wtId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "VirtualTelemetry";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 8);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wtId;
            }
        }
        return val;
    }

    /**
     * 得到告警数据表名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getWarnLogFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        if (dataDate == null)
        {
            dataDate = "";
        }
        return getWarnLogDataFilePath(rootDir, wfId, dataDate) + symbol + "warnlog_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
    }

    /**
     * 得到告警数据文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getWarnLogDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "WarnLog";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到变位数据表名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getChangeDataFileName(String rootDir, String wfId, String deviceId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        if (dataDate == null)
        {
            dataDate = "";
        }
        return getChangeDataFilePath(rootDir, wfId, dataDate) + symbol + "changedata_" + getFileDevId(wfId, deviceId) + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
    }

    /**
     * 得到告警数据文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getChangeDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "ChangeData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到原始故障数据表名称
     *
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getFaultDataFileName(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getFaultDataFilePath(rootDir, wfId, dataDate) + symbol + "fault_" + wfId + "_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到原始故障数据文件路径
     *
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getFaultDataFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "FaultDataFile";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 得到原始告警结束数据表名称
     *
     * @param rootDir
     *            根目录
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getWarnEndDataFileName(String rootDir, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getWarnEndDataFilePath(rootDir, dataDate) + symbol + "warnend_" + dataDate.replace("-", "").substring(0, 6) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到原始告警结束数据文件路径
     *
     * @param rootDir
     *            根目录
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getWarnEndDataFilePath(String rootDir, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "WarnEndData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 6);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol;
            }
        }
        return val;
    }

    /**
     * 得到数据库故障数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getDbFltDataFileName(String rootDir) throws InterruptedException
    {
        String fileName = rootDir + "/FaultData/DatabaseFault/dbflt_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME);
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(1000);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到文件故障数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getFileFltDataFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME);
        String fileName = rootDir + "/FaultData/FileFault/fileflt_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(1000);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到通讯故障数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getCommFltDataFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME);
        String fileName = rootDir + "/FaultData/CommFault/commflt_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(1000);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到跳变文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getJumpTimeFileName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME);
        String fileName = rootDir + "/FaultData/JumpTime/jumptime_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(1000);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到故障文件路径
     * 
     * @param rootDir
     *            根目录
     * @return 文件路径
     */
    public static String getBadFilePath(String rootDir)
    {
        return rootDir + "/FaultData/BadFile";
    }

    /**
     * 得到转发缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @param subDirectory
     *            转发目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getTransBuffFileName(String rootDir, String subDirectory) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/TransBuffer/" + subDirectory + "/trsbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到历史远控文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getHisSynchFileName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME);
        String fileName = rootDir + "/BufferData/RemoteSynch/" + "hissyn_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(1000);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到实时数据库缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称
     * @throws InterruptedException
     *             异常
     */
    public static String getRtDbBuffFileName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/RtDbBuffer/" + "rtdbbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到数据库数据缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getSaveDbBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/DatabaseBuffer/dbbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到实时数据库数据缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             休眠异常
     */
    public static String getSaveRtDbBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/RtDbBuffer/rtdbbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 功率控制操作日志文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getOperLogFileName(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getOperLogFilePath(rootDir, wfId, dataDate) + symbol + "operlog_" + wfId + "_" + dataDate.replace("-", "").substring(0, 8) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到功率控制文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getOperLogFilePath(String rootDir, String wfId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "OperLog";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 8);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wfId;
            }
        }
        return val;
    }

    /**
     * 一分钟数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     */
    public static String getOneMinFileName(String rootDir, String wtId, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = null;
        if (dataDate != null && !dataDate.isEmpty())
        {
            val = getOneMinFilePath(rootDir, wtId, dataDate) + symbol + "onemindata_" + wtId + "_" + dataDate.replace("-", "").substring(0, 8) + DataAsDef.LOCALDBFILEEXTNAME;
        }
        return val;
    }

    /**
     * 得到功率控制文件路径
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件路径
     */
    public static String getOneMinFilePath(String rootDir, String wtid, String dataDate)
    {
        String symbol = getPathSymbol(rootDir);
        String val = rootDir + symbol + "UnArchive" + symbol + "OneMinData";
        if (dataDate != null && !dataDate.isEmpty())
        {
            val += symbol + dataDate.replace("-", "").substring(0, 8);
            if (BusinessDef.isFILEPATHUSEWF())
            {
                val += symbol + wtid;
            }
        }
        return val;
    }

    /**
     * 得到warneend缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getWarnEndBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/WarnEndBuffer/warnendbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到upload缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getUploadBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/UploadBuffer/Uploadbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到缓存文件名称(升压站告警)
     * 
     * @param rootDir
     *            根目录
     * @param subDirectory
     *            子目录
     * 
     * @throws InterruptedException
     *             异常
     * @return 文件名称，全路径
     */
    public static String getWebTransBufffileName(String rootDir, String subDirectory) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/WebTransBuffer/" + subDirectory + "/trsbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到缓存文件名称(升压站告警)
     * 
     * @param rootDir
     *            根目录
     * 
     * @throws InterruptedException
     *             异常
     * @return 文件名称，全路径
     */
    public static String getSubscribeBufffileName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/SubscribeBuffer/SubTrsbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到强隔离缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getStrongBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/BufferData/MySqlBuffer/MySqlbuf_@dateTime" + DataAsDef.LOCALDBFILEEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到ddl缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     */
    public static String getDbDdlBuffFileName(String rootDir)
    {
        return rootDir + "/BufferData/DatabaseBuffer/ddlbuf" + DataAsDef.LOCALDBFILEEXTNAME;
    }

    /**
     * 得到任务缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     */
    public static String getTaskBuffFileName(String rootDir)
    {
        return rootDir + "/BufferData/TaskBuffer/taskbuf" + DataAsDef.LOCALDBFILEEXTNAME;
    }

    /**
     * 得到数据提供服务缓存文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     */
    public static String getDataPrvBuffFName(String rootDir)
    {
        return rootDir + "/DataPrvBuffer/dataprvbuf" + DataAsDef.LOCALDBFILEEXTNAME;
    }

    /**
     * 得到故障数据文件根路径
     * 
     * @param rootDir
     *            根目录
     * @return 文件跟路径
     */
    public static String getFaultDataFilePath(String rootDir)
    {
        return rootDir + "/FaultData";
    }

    /**
     * 得到归档的瞬态数据文件名称
     * 
     * @param rootDir
     *            根目录
     * @param wfId
     *            风场id
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcRealDataFName(String rootDir, String wfId, String deviceId, String dataDate) throws DataAsException
    {
        return getArcFileName(getRealDataFileName(rootDir, wfId, deviceId, dataDate));
    }

    /**
     * 通过未归档文件的名称得到归档文件的名称
     * 
     * @param unArchiveFileName
     *            未归档文件名称
     * @return 归档文件名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcFileName(String unArchiveFileName) throws DataAsException
    {
        String val = "";
        try
        {
            String symbol = getPathSymbol(unArchiveFileName);
            String fileName = unArchiveFileName.substring(unArchiveFileName.replace("\\", "/").lastIndexOf("/"), unArchiveFileName.length());
            String year = fileName.split("_")[2].substring(0, 4);
            val = unArchiveFileName.replace("UnArchive", "HistoryFile" + symbol + year).replace(DataAsDef.LOCALDBFILEEXTNAME, DataAsDef.LOCARCDBFILEEXTNAME);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileNameManage_getArcFileName_1", new String[] { "unArchiveFileName" }, new Object[] { unArchiveFileName }, exp);
        }
        return val;
    }

    /**
     * 通过未归档路径得到归档路径,未归档路径应该是除文件名之外全路径
     * 
     * @param unArchiveFileDir
     *            未归档文件路径
     * @return 除文件名之外全路径
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArcFileDir(String unArchiveFileDir) throws DataAsException
    {
        String val = "";
        try
        {
            String symbol = getPathSymbol(unArchiveFileDir);
            if (unArchiveFileDir.substring(unArchiveFileDir.length() - 1) == symbol)
            {
                val = unArchiveFileDir.substring(0, unArchiveFileDir.length() - 1);
            }
            else
            {
                val = unArchiveFileDir;
            }

            String[] arr = val.split(symbol);
            String year = "";

            if (BusinessDef.isFILEPATHUSEWF())
            {
                year = arr[arr.length - 2].substring(0, 4);
            }
            else
            {
                year = arr[arr.length - 1].substring(0, 4);
            }

            val = unArchiveFileDir.replace("UnArchive", "HistoryFile" + symbol + year);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileNameManage_getArcFileDir_1", new String[] { "unArchiveFileDir" }, new Object[] { unArchiveFileDir }, exp);
        }
        return val;
    }

    /**
     * 得到归档文件路径
     * 
     * @param rootDir
     *            根目录
     * @return 文件路径
     */
    public static String getArcFileRootDir(String rootDir)
    {
        return rootDir + "/HistoryFile";
    }

    /**
     * 上一临时文件名
     */
    private static String prevTempFileName = "";

    /**
     * 获取临时文件名
     * 
     * @return 临时文件名
     * @throws DataAsException
     *             自定义异常
     * @throws InterruptedException
     *             异常
     */
    public static synchronized String getTempFileName() throws DataAsException, InterruptedException
    {
        String val = "";
        int n = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        // 使文件名不重复
        val = "file" + sdf.format(new Date()) + ".tmp";
        synchronized (val.intern())
        {
            while (prevTempFileName.equals(val))
            {
                val.wait(1);
                val = "file" + sdf.format(new Date()) + ".tmp";
                if (n++ > 1000)
                {
                    throw new DataAsException("Temp file name request timeout.");
                }
            }
        }
        prevTempFileName = val;
        return val;
    }

    /**
     * 得到过反向隔离tcp数据txt文件名称
     * 
     * @param rootDir
     *            根路径
     * @return 文件全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getSaveReTcpBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/temp/tcp/tcpbuf_@dateTime" + Thread.currentThread().getId() + DataAsDef.BUFFTXTEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到过反向隔离udp数据txt文件名称
     * 
     * @param rootDir
     *            根目录
     * @return 文件全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getSaveReUdpBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/temp/udp/udpbuf_@dateTime" + Thread.currentThread().getId() + DataAsDef.BUFFTXTEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

    /**
     * 得到操作票处理文件名称
     *
     * @param rootDir
     *            根目录
     * @return 文件名称，全路径
     * @throws InterruptedException
     *             异常
     */
    public static String getTicketBuffFName(String rootDir) throws InterruptedException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL);
        String fileName = rootDir + "/temp/ticket/TicketData_@dateTime" + DataAsDef.BUFFTXTEXTNAME;
        String val = fileName.replace("@dateTime", sdf.format(new Date()));
        File f = new File(fileName);
        if (f.exists())
        {
            Thread.sleep(10);
            val = fileName.replace("@dateTime", sdf.format(new Date()));
        }
        return val;
    }

}
