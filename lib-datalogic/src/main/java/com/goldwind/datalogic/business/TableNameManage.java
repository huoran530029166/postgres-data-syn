package com.goldwind.datalogic.business;

import java.sql.ResultSet;
import java.util.HashMap;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.BusinessDef.MonitorDataType;
import com.goldwind.datalogic.business.BusinessDef.PartType;
import com.goldwind.datalogic.business.model.DevProInfo;
import com.goldwind.datalogic.business.model.ParrtionMetaInfo;
import com.goldwind.datalogic.utils.MemoryData;
import com.goldwind.datalogic.utils.NetCommDef.WfDeviceType;

/**
 * 表名管理
 * 
 * @author 曹阳
 *
 */
public class TableNameManage
{
    /**
     * 是否读取
     */
    private static boolean LOADFLAG = false;
    /**
     * 读取对象
     */
    private static Object LOADOBJ = new Object();
    /**
     * 设备协议信息
     */
    private static HashMap<String, DevProInfo> DEVPROINFODICT;
    /**
     * 是否分电场存储
     */
    private static boolean FARMSTORAGE = false;

    /**
     * 加载设备协议信息
     * 
     * @param dbOper
     *            数据库连接
     * @throws DataAsException
     *             自定义异常
     */
    private static void loadDevProInfo(DbOperBase dbOper) throws DataAsException
    {
        String devId = "";
        try
        {
            synchronized (LOADOBJ)
            {
                if (!LOADFLAG)
                {
                    DEVPROINFODICT = new HashMap<String, DevProInfo>();
                    String sql = "select distinct a.wfid as wfid, a.wtid as wtid, a.protocolid as protocolid, b.devicetype as devicetype, c.viewflag as viewflag "
                            + "from config.wtinfo a, config.wttypeinfo b, config.devicetypeinfo c where a.protocolid = b.protocolid and b.devicetype = c.id";

                    ResultSet dt = dbOper.getResultSet(sql, null);
                    while (dt.next())
                    {
                        String wfId = dt.getString(1);
                        devId = dt.getString(2);
                        String protocolid = dt.getString(3);
                        String deviceType = dt.getString(4);
                        boolean viewflag = "1".equals(dt.getString(5));
                        DEVPROINFODICT.put(devId, new DevProInfo(wfId, devId, protocolid, deviceType, viewflag));
                    }
                    LOADFLAG = true;
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_loadDevProInfo_1", new String[] { "devId" }, new Object[] { devId }, exp);
        }
    }

    /**
     * 得到设备数据表名称(数据维护用)
     * 
     * @param devId
     *            设备id
     * @param dataType
     *            数据类型
     * @param dbOper
     *            数据库连接
     * @return 表名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDevDataTableName(String devId, MonitorDataType dataType, DbOperBase dbOper) throws DataAsException
    {
        String val = "";
        try
        {

            loadDevProInfo(dbOper);
            DevProInfo devProInfo = DEVPROINFODICT.get(devId);

            if (FARMSTORAGE && checkFarmStorage(dataType)) // 按风场存储数据
            {
                if (dataType == MonitorDataType.FIVEMINDATA || dataType == MonitorDataType.StatisticData || dataType == MonitorDataType.DayData)
                {
                    // 是否按协议建视图
                    if (devProInfo.isViewFlag())
                    {
                        val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId() + "_" + devProInfo.getDevType() + "_" + devProInfo.getProtocolId();
                    }
                    else
                    {
                        val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId() + "_" + devProInfo.getDevType();
                    }
                    return val;
                }
                else
                {
                    val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId();
                }
            }
            else
            {
                val = dataType.toString().toLowerCase();

                // 判断设备类型是否为升压站，如果是，则十分钟数据表和日数据表使用【原表名_1】
                if (MemoryData.getDeviceInfoList().containsKey(devId) && WfDeviceType.TransSubstation.getResult() == Integer.parseInt(MemoryData.getDeviceInfoList().get(devId).getDeviceType())
                        && (dataType == MonitorDataType.StatisticData || dataType == MonitorDataType.DayData || dataType == MonitorDataType.ONEMINDATA || dataType == MonitorDataType.FIVEMINDATA
                                || dataType == MonitorDataType.FIFTEENMINDATA))
                {
                    val = val + "_1";
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_getDevDataTableName_1", new String[] { "devId", "protocolId" }, new Object[] { devId, dataType }, exp);
        }
        return val;
    }

    /**
     * 得到设备数据表名称(数据入库用 )
     * 
     * @param devId
     *            设备id
     * @param dataType
     *            数据类型
     * @param dbOper
     *            数据库连接
     * @return 表名
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDevDataTableNameToDb(String devId, MonitorDataType dataType, DbOperBase dbOper) throws DataAsException
    {
        String val = "";
        try
        {
            loadDevProInfo(dbOper);
            DevProInfo devProInfo = DEVPROINFODICT.get(devId);

            if (FARMSTORAGE && checkFarmStorage(dataType)) // 按风场存储数据
            {
                if (dataType == MonitorDataType.FIVEMINDATA || dataType == MonitorDataType.StatisticData || dataType == MonitorDataType.DayData || dataType == MonitorDataType.ONEMINDATA)
                {
                    if (devProInfo.isViewFlag())
                    {
                        val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId() + "_" + devProInfo.getDevType() + "_" + devProInfo.getProtocolId();
                    }
                    else
                    {
                        val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId() + "_" + devProInfo.getDevType();
                    }
                    return val;
                }
                else
                {
                    val = "farm." + dataType.toString().toLowerCase() + "_" + devProInfo.getWfId();
                }
            }
            else
            {
                val = dataType.toString().toLowerCase();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_getDevDataTableName_1", new String[] { "devId", "protocolId" }, new Object[] { devId, dataType }, exp);
        }
        return val;
    }

    /**
     * 得到设备数据视图名称
     * 
     * @param devId
     *            设备id
     * @param dataType
     *            数据类型
     * @param dbOper
     *            数据库连接
     * @return 视图名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDevDataViewName(String devId, MonitorDataType dataType, DbOperBase dbOper) throws DataAsException
    {
        String val = "";

        try
        {
            checkViewDataType(dataType);
            loadDevProInfo(dbOper);

            DevProInfo devProInfo = DEVPROINFODICT.get(devId);

            if (FARMSTORAGE)
            {
                val = "farm." + dataType.toString().toLowerCase() + "_view_" + devProInfo.getWfId() + "_" + devProInfo.getDevType();
            }
            else
            {
                val = dataType.toString().toLowerCase() + "_view_" + devProInfo.getDevType();
            }

            if (devProInfo.isViewFlag())
            {
                val += "_" + devProInfo.getProtocolId();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_getDevDataViewName_1", new String[] { "devId", "dataType" }, new Object[] { devId, dataType }, exp);
        }
        return val;
    }

    /**
     * 检查视图数据类型
     * 
     * @param dataType
     *            数据类型
     * @throws DataAsException
     *             自定义异常
     */
    private static void checkViewDataType(MonitorDataType dataType) throws DataAsException
    {
        if (dataType != MonitorDataType.FIVEMINDATA && dataType != MonitorDataType.StatisticData && dataType != MonitorDataType.DayData)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_checkViewDataType_1", new String[] { "dataType" }, new Object[] { dataType }, null);
        }
    }

    /**
     * 查询风场数据视图名称
     * 
     * @param wfId
     *            风场id
     * @param dataType
     *            数据类型
     * @return 视图名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getWtDataViewName(String wfId, MonitorDataType dataType) throws DataAsException
    {
        checkViewDataType(dataType);

        if (FARMSTORAGE)
        {
            return "farm." + dataType.toString().toLowerCase() + "_view_" + wfId + "_0";
        }
        else
        {
            return dataType.toString().toLowerCase() + "_view_0";
        }
    }

    /**
     * 到风场数据表名称,如果是协议数据,只返回风机的数据表
     * 
     * @param wfId
     *            风场id
     * @param dataType
     *            数据类型
     * @return 表名
     */
    public static String getWfDataTableName(String wfId, MonitorDataType dataType)
    {
        if (FARMSTORAGE && checkFarmStorage(dataType))
        {
            if (dataType == MonitorDataType.FIVEMINDATA || dataType == MonitorDataType.StatisticData || dataType == MonitorDataType.DayData)
            {
                return "farm." + dataType.toString().toLowerCase() + "_" + wfId + "_0";
            }
            else
            {
                return "farm." + dataType.toString().toLowerCase() + "_" + wfId;
            }
        }
        else
        {
            return dataType.toString().toLowerCase();
        }
    }

    /**
     * 检测数据类型是否支持按风场存储
     * 
     * @param dataType
     *            数据类型
     * @return 是否支持
     */
    public static boolean checkFarmStorage(MonitorDataType dataType)
    {
        switch (dataType) {
            case DayData:
            case FarmDayData:
            case FarmHisPowCurve:
            case FarmLimitPower:
            case FarmOneData:
            case FarmPowCurve:
            case FarmTenData:
            case FIVEMINDATA:
            case HisPowCurve:
            case HisWindFrq:
            case HisWindDur:
            case HisWindRose:
            case LossElecTotal:
            case OneData:
            case PowCurve:
            case RunLog:
            case StatisticData:
            case WtAlarmInfo:
            case WtComStateInfo:
            case WtErrorInfo:
            case WtStatusInfo:
            case PlcFileRec:
            case EnergyUse_Data:
            case DataStat:
                return true;
            default:
                return false;
        }
    }

    /**
     * 根据风场id和时间得到分区表名称（获取的是最底层的分区表，即有孙子表时返回的是孙子表的表名）
     * 
     * @param wfid
     *            风场id
     * @param rectime
     *            时间（格式：yyyy-MM DataAsDef.MOTHFORMATSTR）
     * @param tableName
     *            主表名
     * @return 分区表名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getSubTableName(String wfid, String rectime, String tableName) throws DataAsException
    {
        // 分区表名称（默认不分区）
        String subTableName = tableName;

        try
        {
            // 组装分区表名（例：datamtstate_632809、statisticdata_632809_2020_04）
            ParrtionMetaInfo parrtionMetaInfo = MemoryData.getParrtionMetaInfos().get(tableName);
            if (parrtionMetaInfo != null)
            {
                // list分区
                if (PartType.LISTPARTITION == parrtionMetaInfo.getPartType())
                {
                    subTableName = parrtionMetaInfo.getTabSchema() + "." + tableName + "_" + wfid;
                }
                // 复合分区
                else if (PartType.COMPOUNDPARTITION == parrtionMetaInfo.getPartType())
                {
                    subTableName = parrtionMetaInfo.getTabSchema() + "." + tableName + "_" + wfid + "_" + rectime.substring(0, 7).replace("-", "_");
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("TableNameManage_getsubTableName_1", new String[] { "wfid", "rectime", "tableName" }, new Object[] { wfid, rectime, tableName }, exp);
        }

        return subTableName;
    }

    /**
     * 得到所有分区表名称到内存
     * 
     * @param wfid
     *            风场id
     * @param rectime
     *            时间（格式：yyyy-MM DataAsDef.MOTHFORMATSTR）
     * @param tableName
     *            主表名
     * @return 分区表名称
     * @throws DataAsException
     *             自定义异常
     */
    public static void getAllSubTableNamesToRAM(String wfid, String rectime, String tableName) throws DataAsException
    {

    }
}
