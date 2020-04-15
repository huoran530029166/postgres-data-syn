package com.goldwind.datalogic.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 处理数据时间
 * 
 * @author wangruibo 2019年12月9日
 */
public class DataTimeManage// NOSONAR
{
    /**
     * "dataTime"常量
     */
    private static String DATATIMESTR = "dataTime";

    /**
     * 得到一分钟数据时间
     * 
     * @param dataTime
     *            数据时间
     * @return val 数据时间
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDataOneMinute(String dataTime) throws DataAsException
    {
        String val = "";

        try
        {
            Calendar calendar = Calendar.getInstance();
            if (!(dataTime.isEmpty() || dataTime == null))
            {
                calendar = DataAsFunc.parseDateTime(dataTime);
            }

            int getSecends = calendar.get(Calendar.SECOND);
            // 如果超过最大秒数,为下一个一分钟数据
            if (getSecends >= ControlProcessDef.ONEMINUTEDATAMAXSECOND)
            {
                calendar.add(Calendar.MINUTE, 1);
            }
            calendar.add(Calendar.MINUTE, -1);
            val = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(calendar.getTime());
            val = val.substring(0, 17) + "00.000";
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataTimeManage_getDataOneMinute_1", new String[] { DATATIMESTR }, new Object[] { dataTime }, e);
        }

        return val;
    }

    /**
     * 得到五分钟数据时间
     * 
     * @param dataTime
     *            数据时间
     * @return val 数据时间
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDataFiveMinute(String dataTime) throws DataAsException
    {
        String val = "";

        try
        {
            Calendar calendar = Calendar.getInstance();
            if (!(dataTime.isEmpty() || dataTime == null))
            {
                calendar = DataAsFunc.parseDateTime(dataTime);
            }
            if ((calendar.get(Calendar.MINUTE) % 5) >= ControlProcessDef.FIVEMINUTEDATAMAXMINUTE)
            {
                calendar.add(Calendar.MINUTE, 5);
            }

            calendar.add(Calendar.MINUTE, -5);
            int minute = calendar.get(Calendar.MINUTE) / 5 * 5;
            val = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(calendar.getTime());
            val = val.substring(0, 14) + minute + ":00.000";
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataTimeManage_getDataTenMinute_1", new String[] { DATATIMESTR }, new Object[] { dataTime }, e);
        }
        return val;
    }

    /**
     * 得到十分钟数据时间
     * 
     * @param dataTime
     *            数据时间
     * @return val 数据时间
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDataTenMinute(String dataTime) throws DataAsException
    {
        String val = "";

        try
        {
            Calendar calendar = Calendar.getInstance();
            if (!(dataTime.isEmpty() || dataTime == null))
            {
                calendar = DataAsFunc.parseDateTime(dataTime);
            }
            if ((calendar.get(Calendar.MINUTE) % 10) >= ControlProcessDef.TENMINUTEDATAMAXMINUTE)
            {
                calendar.add(Calendar.MINUTE, 10);
            }

            calendar.add(Calendar.MINUTE, -10);
            val = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(calendar.getTime());
            val = val.substring(0, 15) + "0:00.000";
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataTimeManage_getDataTenMinute_1", new String[] { DATATIMESTR }, new Object[] { dataTime }, e);
        }
        return val;
    }

    /**
     * 得到日数据时间
     * 
     * @param dataTime
     *            时间
     * @return 时间
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDataDate(String dataTime) throws DataAsException
    {
        String val = "";

        try
        {
            Calendar calendar = Calendar.getInstance();
            if (dataTime != null && !dataTime.isEmpty())
            {
                calendar = DataAsFunc.parseDateTime(dataTime);
            }

            if ((calendar.get(Calendar.HOUR_OF_DAY)) >= ControlProcessDef.DAYDATAMAXHOUR) // 如果超过最大小时数,为下一日数据
            {
                calendar.add(Calendar.DATE, 1);
            }

            calendar.add(Calendar.DATE, -1);
            val = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(calendar.getTime());
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataTimeManage_getDataDate_1", new String[] { DATATIMESTR }, new Object[] { dataTime }, e);
        }
        return val;
    }

}
