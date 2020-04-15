package com.goldwind.datalogic.business;

import java.io.File;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 文件名取得类
 * 
 * @author 冯春源
 *
 */
public class GetFileNamePath
{
    /**
     * 取得未归档瞬态数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     */
    public static String getUnArchiveRealDataFileName(String rootDir, String deviceId, String dataDate)
    {
        String ymd = dataDate.replace("-", "").substring(0, 8);
        String val = rootDir + "/" + "UnArchive" + "/" + "RealtimeData" + "/" + ymd + "/" + "real_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;

        // 判断未归档文件是否存在
        if (!(new File(val).exists()))
        {
            return null;
        }

        return val;
    }

    /**
     * 取得已归档瞬态数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArchivedRealDataFileName(String rootDir, String deviceId, String dataDate) throws DataAsException
    {
        String archivedFileName = null;
        try
        {
            String ymd = dataDate.replace("-", "").substring(0, 8);
            String unArchiveFileName = rootDir + "/" + "UnArchive" + "/" + "RealtimeData" + "/" + ymd + "/" + "real_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;

            // 通过未归档文件的名称得到归档文件的名称
            archivedFileName = FileNameManage.getArcFileName(unArchiveFileName);

            // 判断归档文件是否存在
            if (!(new File(archivedFileName).exists()))
            {
                return null;
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("GetFileNamePath_getArchivedRealDataFileName_1", null, null, e);
        }
        return archivedFileName;
    }

    /**
     * 取得未归档虚拟测点数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     */
    public static String getUnArchiveVirtualDataFileName(String rootDir, String deviceId, String dataDate)
    {
        String ymd = dataDate.replace("-", "").substring(0, 8);
        String val = rootDir + "/" + "UnArchive" + "/" + "VirtualTelemetry" + "/" + ymd + "/" + "virtualtelemetry_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;

        // 判断未归档文件是否存在
        if (!(new File(val).exists()))
        {
            return null;
        }

        return val;
    }

    /**
     * 取得未归档新一分钟数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     */
    public static String getUnArchiveOneMinDataFileName(String rootDir, String deviceId, String dataDate)
    {
        String ymd = dataDate.replace("-", "").substring(0, 8);
        String val = rootDir + "/" + "UnArchive" + "/" + "OneMinData" + "/" + ymd + "/" + "onemindata_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;

        // 判断未归档文件是否存在
        if (!(new File(val).exists()))
        {
            return null;
        }

        return val;
    }

    /**
     * 取得已归档新一分钟数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArchivedOneMinDataFileName(String rootDir, String deviceId, String dataDate) throws DataAsException
    {
        String archivedFileName = null;
        try
        {
            String ymd = dataDate.replace("-", "").substring(0, 8);
            String unArchiveFileName = rootDir + "/" + "UnArchive" + "/" + "OneMinData" + "/" + ymd + "/" + "onemindata_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;
            // 通过未归档文件的名称得到归档文件的名称
            archivedFileName = FileNameManage.getArcFileName(unArchiveFileName);

            // 判断归档文件是否存在
            if (!(new File(archivedFileName).exists()))
            {
                return null;
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("GetFileNamePath_getArchivedOneMinDataFileName_1", null, null, e);
        }
        return archivedFileName;
    }

    /**
     * 取得已归档虚拟测点数据文件的全路径名称
     * 
     * @param rootDir
     *            根目录
     * @param deviceId
     *            设备id
     * @param dataDate
     *            数据日期
     * @return 文件全路径名称
     * @throws DataAsException
     *             自定义异常
     */
    public static String getArchivedVirtualDataFileName(String rootDir, String deviceId, String dataDate) throws DataAsException
    {
        String archivedFileName = null;
        try
        {
            String ymd = dataDate.replace("-", "").substring(0, 8);
            String unArchiveFileName = rootDir + "/" + "UnArchive" + "/" + "VirtualTelemetry" + "/" + ymd + "/" + "virtualtelemetry_" + deviceId + "_" + ymd + DataAsDef.LOCALDBFILEEXTNAME;

            // 通过未归档文件的名称得到归档文件的名称
            archivedFileName = FileNameManage.getArcFileName(unArchiveFileName);

            // 判断归档文件是否存在
            if (!(new File(archivedFileName).exists()))
            {
                return null;
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("GetFileNamePath_getArchivedVirtualDataFileName_1", null, null, e);
        }
        return archivedFileName;
    }
}
