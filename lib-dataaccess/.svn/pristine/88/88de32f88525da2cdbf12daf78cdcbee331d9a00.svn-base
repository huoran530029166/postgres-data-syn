package com.goldwind.dataaccess.rtdb.golden.oper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.rtdb.golden.common.GoldenUtils;
import com.goldwind.dataaccess.rtdb.model.DataEntity;
import com.goldwind.dataaccess.rtdb.model.RTCalculationMode;
import com.goldwind.dataaccess.rtdb.model.RTHistorianMode;
import com.goldwind.dataaccess.rtdb.model.RTPoint;
import com.rtdb.api.enums.RtdbType;
import com.rtdb.api.model.RtdbData;
import com.rtdb.enums.Quality;
import com.rtdb.enums.RtdbHisMode;
import com.rtdb.enums.ValueType;
import com.rtdb.model.BlobData;
import com.rtdb.model.DatetimeData;
import com.rtdb.model.DoubleData;
import com.rtdb.model.Entity;
import com.rtdb.model.IntData;
import com.rtdb.model.StringData;
import com.rtdb.model.SummaryEntity;
import com.rtdb.service.impl.HistorianImpl;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.impl.SnapshotImpl;
import com.rtdb.service.inter.Historian;
import com.rtdb.service.inter.Snapshot;

/**
 * 读取数据操作类
 * 
 * @author Administrator
 *
 */
public class DataOperation
{
    /**
     * 庚顿历史数据服务
     */
    private Historian his = null;
    /**
     * 庚顿快照数据服务
     */
    private Snapshot snap = null;
    /**
     * 测点表名
     */
    private String tableName;

    public DataOperation(String tableName)
    {
        this.tableName = tableName;
    }

    /**
     * 建立连接
     * 
     * @throws DataAsException
     *             自定义异常
     */
    public void open() throws DataAsException
    {
        try
        {
            his = new HistorianImpl(GoldenServerPool.getPool().getServerImpl());
            snap = new SnapshotImpl(GoldenServerPool.getPool().getServerImpl());
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_DataOperation_2", new String[] { "tableName" }, new Object[] { tableName }, e);
        }
    }

    /**
     * 关闭连接
     * 
     * @throws DataAsException
     *             自定义异常
     */
    public void disConn() throws DataAsException
    {
        if (his != null)
        {
            try
            {
                his.close();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("DataOperation_disConn_1", new String[] {}, new Object[] {}, e);
            }
        }
        if (snap != null)
        {
            try
            {
                snap.close();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("DataOperation_disConn_2", new String[] {}, new Object[] {}, e);
            }
        }
        // System.out.println("DataOperation 释放连接！");
    }

    /**
     * 写入历史数据
     * 
     * @param list
     *            DataEntity数据集合
     * @return 写入成功数量
     * @throws DataAsException
     *             自定义异常
     */
    public int putData(List<DataEntity> list) throws DataAsException
    {
        int result = 0;
        if (list.isEmpty())
        {
            return result;
        }
        ServerImpl putDataServer = null;
        Historian putDataHis = null;
        try
        {
            putDataServer = GoldenServerPool.getPool().getServerImpl();
            putDataHis = new HistorianImpl(putDataServer);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_putData_1", new String[] { "list" }, new Object[] { list }, e);
        }

        List<RtdbData> rtdbDataList = new ArrayList<RtdbData>();
        List<BlobData> blobDataList = new ArrayList<BlobData>();
        List<DatetimeData> datetimeDataList = new ArrayList<DatetimeData>();
        for (DataEntity entity : list)
        {
            try
            {
                RtdbData data = new RtdbData();
                data.setId(entity.getId());
                data.setDate(entity.getTime());
                data.setQuality((short) 0);
                switch (entity.getType()) {
                    case RT_BOOL:
                    case RT_INT16:
                    case RT_INT64:
                    case RT_INT32:
                    case RT_INT8:
                    case RT_UINT16:
                    case RT_UINT32:
                    case RT_UINT8:
                        data.setState(Long.parseLong(entity.getValue().toString()));
                        data.setGoldenType(RtdbType.RTDB_UINT8);
                        rtdbDataList.add(data);
                        break;
                    case RT_FLOAT:
                    case RT_DOUBLE:
                        data.setValue(Double.parseDouble(entity.getValue().toString()));
                        data.setGoldenType(RtdbType.RTDB_REAL64);
                        rtdbDataList.add(data);
                        break;
                    case RT_STRING:
                        if (!String.valueOf(entity.getValue()).isEmpty())
                        {
                            BlobData blobData = new BlobData();
                            blobData.setId(entity.getId());
                            blobData.setDatetime(entity.getTime());
                            blobData.setBlob((String.valueOf(entity.getValue())).getBytes());
                            blobData.setLen((String.valueOf(entity.getValue())).getBytes().length);
                            blobData.setQuality(Quality.GOOD);
                            blobDataList.add(blobData);
                        }
                        break;
                    case RT_DATETIME:
                        DatetimeData datetimeData = new DatetimeData();
                        datetimeData.setDateTime(entity.getTime());
                        datetimeData.setId(entity.getId());
                        datetimeData.setQuality((short) 0);
                        datetimeData.setValue(entity.getTime());
                        datetimeData.setMs((short) 1);
                        datetimeDataList.add(datetimeData);
                        break;
                    default:
                        break;
                }
            }
            catch (Exception e)
            {
                // DataAsExpSet.throwExpByResCode("DataOperation_putData_4", new String[] { "list" }, new Object[] { list }, e);
                // System.err.println(entity.getTagName() + "value=" + entity.getValue().toString() + " 封装参数错误");
                continue;
            }
        }

        try
        {
            if (!rtdbDataList.isEmpty())
            {
                RtdbData[] rtdbDatas = new RtdbData[rtdbDataList.size()];
                result += putDataHis.putArchivedValues(rtdbDataList.toArray(rtdbDatas));
                // result += his.putArchivedValues(rtdbDataList.toArray(rtdbDatas));
            }
            // if (!rtdbDataList.isEmpty())
            // {
            // for (RtdbData data : rtdbDataList)
            // {
            // result += his.putArchivedValues(new RtdbData[] { data });
            // }
            // }
            if (!blobDataList.isEmpty())
            {
                // result += his.putArchivedBlobValues(blobDataList);
                result += putDataHis.putArchivedBlobValues(blobDataList);
            }
            if (!datetimeDataList.isEmpty())
            {
                // result += his.putArchivedDatetimeValues(datetimeDataList);
                result += putDataHis.putArchivedDatetimeValues(datetimeDataList);
            }

        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_putData_2", new String[] { "list" }, new Object[] { list }, e);
        }
        finally
        {
            try
            {
                // GoldenServerPool.getPool().releaseServerImpl(putDataServer);
                putDataHis.close();
                // System.out.println("释放写入数据连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("DataOperation_putData_3", new String[] { "list" }, new Object[] { list }, e);
            }
        }
        return result;
    }

    /**
     * 获取一段时间内的历史数据
     * 
     * @param points
     *            测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getData(List<RTPoint> points, Date startTime, Date endTime) throws DataAsException
    {
        if (points == null || points.size() <= 0 || endTime.before(startTime))
        {
            return null;
        }
        Map<String, List<DataEntity>> resultMap = getHistoryData(points, startTime, endTime);
        return resultMap;

    }

    /**
     * 根据测点集合获取一段时间内的历史数据
     * 
     * @param points
     *            测点集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getHistoryData(List<RTPoint> points, Date startTime, Date endTime) throws DataAsException
    {
        if (points == null || points.size() <= 0 || endTime.before(startTime))
        {
            return null;
        }
        Map<String, List<DataEntity>> result = new HashMap<String, List<DataEntity>>();
        for (RTPoint point : points)
        {
            switch (point.getDataType()) {
                case RT_BOOL:
                    result.put(point.getTagName(), getIntHistory(point.getId(), startTime, endTime, true));
                    break;
                case RT_INT16:
                case RT_INT64:
                case RT_INT32:
                case RT_INT8:
                case RT_UINT16:
                case RT_UINT32:
                case RT_UINT8:
                    result.put(point.getTagName(), getIntHistory(point.getId(), startTime, endTime, false));
                    break;
                case RT_FLOAT:
                case RT_DOUBLE:
                    result.put(point.getTagName(), getDoubleHistory(point.getId(), startTime, endTime));
                    break;
                case RT_STRING:
                    result.put(point.getTagName(), getStringHistory(point.getId(), startTime, endTime));
                    break;
                case RT_DATETIME:
                    result.put(point.getTagName(), getDateTimeHistory(point.getId(), startTime, endTime));
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    /**
     * 获取指定时间段int或bool类型的历史数据
     * 
     * @param id
     *            测点id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param isBool
     *            是否为bool值，true:bool类型；false:表示int类型
     * @return 历史值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getIntHistory(int id, Date startTime, Date endTime, boolean isBool) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        int count = 0;
        try
        {
            count = his.getArchivedValuesCount(id, startTime, endTime);
            if (count <= 0)
            {
                return list;
            }
            List<IntData> dataList = his.getIntArchivedValues(id, count, startTime, endTime);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }
            for (IntData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                if (isBool)
                {
                    entity.setValue((data.getValue() == 1 ? true : false));
                }
                else
                {
                    entity.setValue(data.getValue());
                }
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getIntHistory_1", new String[] { "id", "startTime", "endTime", "isBool" }, new Object[] { id, startTime, endTime, isBool }, e);
        }
        return list;

    }

    /**
     * 获取指定时间段浮点类型的历史数据
     * 
     * @param id
     *            测点id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getDoubleHistory(int id, Date startTime, Date endTime) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        int count = 0;
        try
        {
            count = his.getArchivedValuesCount(id, startTime, endTime);
            if (count <= 0)
            {
                return list;
            }
            List<DoubleData> dataList = his.getDoubleArchivedValues(id, count, startTime, endTime);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }
            for (DoubleData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getDoubleHistory_1", new String[] { "id", "startTime", "endTime" }, new Object[] { id, startTime, endTime }, e);
        }
        return list;
    }

    /**
     * 获取指定时间段String类型的历史数据
     * 
     * @param id
     *            测点id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getStringHistory(int id, Date startTime, Date endTime) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        int count = 0;
        try
        {
            count = his.getArchivedValuesCount(id, startTime, endTime);
            if (count <= 0)
            {
                return list;
            }
            List<StringData> dataList = his.getArchivedStringValues(id, count, startTime, endTime);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }
            for (StringData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality().getNum() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDatetime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getStringHistory_1", new String[] { "id", "startTime", "endTime" }, new Object[] { id, startTime, endTime }, e);
        }
        return list;
    }

    /**
     * 获取指定时间段Date类型的历史数据
     * 
     * @param id
     *            测点id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getDateTimeHistory(int id, Date startTime, Date endTime) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        int count = 0;
        try
        {
            count = his.getArchivedValuesCount(id, startTime, endTime);
            if (count <= 0)
            {
                return list;
            }
            List<DatetimeData> dataList = his.getArchivedDatetimeValues(id, startTime, endTime, count);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }
            for (DatetimeData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getDateTimeHistory_1", new String[] { "id", "startTime", "endTime" }, new Object[] { id, startTime, endTime }, e);
        }
        return list;
    }

    /**
     * 获取断面数据
     * 
     * @param points
     *            测点信息
     * @param historianMode
     *            历史数据取值模式
     * @param dateTime
     *            时间
     * @return 断面数据
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, DataEntity> getCrossSectionData(List<RTPoint> points, RTHistorianMode historianMode, Date dateTime) throws DataAsException
    {
        if (points == null || points.size() <= 0)
        {
            return null;
        }
        Map<Integer, String> idToNameMap = new HashMap<Integer, String>();

        Map<ValueType, List<Integer>> idsTypeMap = groupIdsByType(points, idToNameMap);

        if (idsTypeMap == null || idsTypeMap.isEmpty())
        {
            return null;
        }
        RtdbHisMode mode = GoldenUtils.translateMode(historianMode);
        List<DataEntity> resultList = getCrossDataById(idsTypeMap, dateTime, mode);
        if (resultList == null || resultList.isEmpty())
        {
            return null;
        }
        Map<String, DataEntity> resultMap = buildCrossData(idToNameMap, resultList);
        return resultMap;

    }

    /**
     * 按测点类型分类
     * 
     * @param points
     *            测点属性
     * @param idToNameMap
     *            id对应测点名称的映射
     * @return type对应测点id的集合
     */
    private Map<ValueType, List<Integer>> groupIdsByType(List<RTPoint> points, Map<Integer, String> idToNameMap)
    {
        Map<ValueType, List<Integer>> resultMap = new HashMap<ValueType, List<Integer>>();
        List<Integer> intIds = new ArrayList<Integer>();
        List<Integer> doubleIds = new ArrayList<Integer>();
        List<Integer> stringIds = new ArrayList<Integer>();
        List<Integer> datetimesIds = new ArrayList<Integer>();
        for (RTPoint point : points)
        {
            switch (point.getDataType()) {
                case RT_BOOL:
                case RT_INT16:
                case RT_INT64:
                case RT_INT32:
                case RT_INT8:
                case RT_UINT16:
                case RT_UINT32:
                case RT_UINT8:
                    intIds.add(point.getId());
                    break;
                case RT_FLOAT:
                case RT_DOUBLE:
                    doubleIds.add(point.getId());
                    break;
                case RT_STRING:
                    stringIds.add(point.getId());
                    break;
                case RT_DATETIME:
                    datetimesIds.add(point.getId());
                    break;
                default:
                    break;
            }
            idToNameMap.put(point.getId(), point.getTagName());
        }
        if (!intIds.isEmpty())
        {
            resultMap.put(ValueType.RTDB_INT32, intIds);
        }

        if (!doubleIds.isEmpty())
        {
            resultMap.put(ValueType.RTDB_REAL32, doubleIds);
        }
        if (!stringIds.isEmpty())
        {
            resultMap.put(ValueType.RTDB_STRING, stringIds);
        }
        if (!datetimesIds.isEmpty())
        {
            resultMap.put(ValueType.RTDB_DateTime, datetimesIds);
        }

        return resultMap;
    }

    /**
     * 根据id获取断面数据(只有数据值类型才有断面数据)
     * 
     * @param idsTypeMap
     *            测点id类型分类集合
     * @param time
     *            时间戳
     * @param mode
     *            历史数据取值模式
     * @return 断面历史值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getCrossDataById(Map<ValueType, List<Integer>> idsTypeMap, Date time, RtdbHisMode mode) throws DataAsException
    {
        if (idsTypeMap == null || idsTypeMap.isEmpty())
        {
            return null;
        }
        List<DataEntity> result = new ArrayList<DataEntity>();
        for (ValueType type : idsTypeMap.keySet())
        {
            List<Integer> ids = idsTypeMap.get(type);
            switch (type) {
                case RTDB_INT32:
                    List<DataEntity> intDataList = getIntCrossData(ids, mode, time);
                    if (intDataList != null && !intDataList.isEmpty())
                    {
                        result.addAll(intDataList);
                    }
                    break;
                case RTDB_REAL32:
                    List<DataEntity> doubleDataList = getDoubleCrossData(ids, mode, time);
                    if (doubleDataList != null && !doubleDataList.isEmpty())
                    {
                        result.addAll(doubleDataList);
                    }
                    break;
                default:
                    // System.err.println(type + " 该类型不支持断面数据的获取");
                    break;
            }
        }
        return result;
    }

    /**
     * 获取int类型的断面历史数据
     * 
     * @param ids
     *            测点ids
     * @param mode
     *            历史取值模式
     * @param time
     *            时间戳
     * @return 历史断面数据
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getIntCrossData(List<Integer> ids, RtdbHisMode mode, Date time) throws DataAsException
    {
        if (ids == null || ids.isEmpty())
        {
            return null;
        }
        Date[] dates = new Date[ids.size()];
        int[] idArray = new int[ids.size()];
        List<DataEntity> list = new ArrayList<DataEntity>();
        for (int i = 0; i < ids.size(); i++)
        {
            dates[i] = time;
            idArray[i] = ids.get(i);
        }
        try
        {
            List<IntData> dataList = his.getIntCrossSectionValues(idArray, dates, mode);
            if (dataList == null || dataList.isEmpty())
            {
                return null;
            }

            for (IntData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getIntCrossData_1", new String[] { "ids", "mode", "time" }, new Object[] { ids, mode, time }, e);
        }
        return list;
    }

    /**
     * 获取double类型的断面历史数据
     * 
     * @param ids
     *            测点ids
     * @param mode
     *            历史取值模式
     * @param time
     *            时间戳
     * @return 历史断面数据
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getDoubleCrossData(List<Integer> ids, RtdbHisMode mode, Date time) throws DataAsException
    {
        if (ids == null || ids.isEmpty())
        {
            return null;
        }
        Date[] dates = new Date[ids.size()];
        int[] idArray = new int[ids.size()];
        List<DataEntity> list = new ArrayList<DataEntity>();
        for (int i = 0; i < ids.size(); i++)
        {
            dates[i] = time;
            idArray[i] = ids.get(i);
        }
        try
        {
            List<DoubleData> dataList = his.getFloatCrossSectionValues(idArray, dates, mode);
            if (dataList == null || dataList.isEmpty())
            {
                return null;
            }

            for (DoubleData data : dataList)
            {
                // 过滤创建值(质量码为create)
                if (data.getQuality() == 2)
                {
                    continue;
                }
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getDoubleCrossData_1", new String[] { "ids", "mode", "time" }, new Object[] { ids, mode, time }, e);
        }
        return list;
    }

    /**
     * 拼接tagName->dataEntity的历史断面数据Map
     * 
     * @param idToNameMap
     *            id与tagName的键值对
     * @param resultList
     *            数据集合
     * @return 历史断面数据Map
     */
    private Map<String, DataEntity> buildCrossData(Map<Integer, String> idToNameMap, List<DataEntity> resultList)
    {
        Map<String, DataEntity> resultMap = new HashMap<String, DataEntity>();
        for (DataEntity data : resultList)
        {
            data.setTagName(idToNameMap.get(data.getId()));
            resultMap.put(idToNameMap.get(data.getId()), data);
        }
        return resultMap;
    }

    /**
     * 获取一段时间内的插值
     * 
     * @param pointMap
     *            测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 历史数据插值
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getDataByTimeSpan(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        return getDataMap(pointMap, startTime, endTime, timeSpan);
    }

    /**
     * 获取一段时间内的插值
     * 
     * @param pointMap
     *            测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param count
     *            条数
     * @return 历史数据插值
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getDataByCount(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int count) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty() || endTime.before(startTime))
        {
            return null;
        }

        int timeSpan = (int) ((endTime.getTime() - startTime.getTime()) / count);

        return getIntervalHistoryData(pointMap, timeSpan, count, startTime);
    }

    /**
     * 根据需要间隔行数，读取一段时间内,根据数量查询的历史插值数据(只支持整型和浮点型)
     * 
     * @param pointMap
     *            测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 历史数据插值
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getDataMap(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty() || endTime.before(startTime))
        {
            return null;
        }

        int intervalCount = (int) ((endTime.getTime() - startTime.getTime()) / timeSpan);

        return getIntervalHistoryData(pointMap, timeSpan, intervalCount, startTime);
    }

    /**
     * 获取int、float类型的等间隔历史插值
     * 
     * @param pointMap
     *            测点属性Map
     * @param timeSpan
     *            等隔时间（毫秒）
     * @param intervalCount
     *            条数
     * @param startTime
     *            开始时间
     * @return 测点名称对应的等间隔历史数据
     * @throws DataAsException
     *             自定义异常
     */
    private Map<String, List<DataEntity>> getIntervalHistoryData(Map<String, RTPoint> pointMap, int timeSpan, int intervalCount, Date startTime) throws DataAsException
    {

        Map<String, List<DataEntity>> result = new HashMap<String, List<DataEntity>>();

        for (String tableTagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tableTagName);
            switch (point.getDataType()) {
                case RT_INT8:
                case RT_INT16:
                case RT_INT32:
                case RT_UINT8:
                case RT_UINT16:
                case RT_UINT32:
                case RT_INT64:
                    result.put(point.getTagName(), this.getIntIntervalProData(point.getId(), timeSpan, intervalCount, startTime, false));
                    break;
                case RT_FLOAT:
                case RT_DOUBLE:
                    result.put(point.getTagName(), this.getFloatIntervalProData(point.getId(), timeSpan, intervalCount, startTime));
                    break;
                case RT_BOOL:
                    result.put(point.getTagName(), this.getIntIntervalProData(point.getId(), timeSpan, intervalCount, startTime, true));
                    break;
                default:
                    // System.err.println(tagName + " 该类型不支持插值的获取");
                    break;
            }
        }

        return result;
    }

    /**
     * 获取int类型的等间隔历史插值
     * 
     * @param id
     *            测点id
     * @param timeSpan
     *            等隔时间（毫秒）
     * @param intervalCount
     *            条数
     * @param startTime
     *            开始时间
     * @param isBool
     *            是否是boolean类型
     * @return int类型的等间隔历史插值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getIntIntervalProData(int id, int timeSpan, int intervalCount, Date startTime, boolean isBool) throws DataAsException
    {
        List<DataEntity> result = new ArrayList<DataEntity>();
        List<IntData> dataList = new ArrayList<IntData>();
        try
        {
            dataList = his.getIntIntervalValues(id, timeSpan, intervalCount, startTime);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getIntIntervalProData_1", new String[] { "id", "timeSpan", "startTime", "isBool" }, new Object[] { id, timeSpan, startTime, isBool }, e);
        }
        if (dataList.isEmpty())
        {
            return null;
        }
        for (IntData data : dataList)
        {
            DataEntity entity = new DataEntity();
            entity.setId(data.getId());
            entity.setTime(data.getDateTime());
            if (isBool)
            {
                entity.setValue((data.getValue() == 1 ? true : false));
            }
            else
            {
                entity.setValue(data.getValue());
            }
            result.add(entity);
        }
        return result;
    }

    /**
     * 获取浮点类型的等间隔历史插值
     * 
     * @param id
     *            测点id
     * @param timeSpan
     *            等隔时间（毫秒）
     * @param intervalCount
     *            条数
     * @param startTime
     *            开始时间
     * @return 浮点类型的等间隔历史插值
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getFloatIntervalProData(int id, int timeSpan, int intervalCount, Date startTime) throws DataAsException
    {
        List<DataEntity> result = new ArrayList<DataEntity>();
        List<DoubleData> dataList = new ArrayList<DoubleData>();
        try
        {
            dataList = his.getFloatIntervalValues(id, timeSpan, intervalCount, startTime);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getFloatIntervalProData_1", new String[] { "id", "timeSpan", "intervalCount", "startTime" },
                    new Object[] { id, timeSpan, intervalCount, startTime }, e);
        }
        if (dataList.isEmpty())
        {
            return null;
        }

        for (DoubleData data : dataList)
        {
            DataEntity entity = new DataEntity();
            entity.setId(data.getId());
            entity.setTime(data.getDateTime());
            entity.setValue(data.getValue());
            result.add(entity);
        }
        return result;
    }

    /**
     * 根据时间隔读取，读取一段时间内,等隔时间的趋势数据（返回时间段内最大、最小、开始、结束值）
     * 
     * @param pointMap
     *            测点名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 等隔时间的趋势数据
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getTrendData(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return null;
        }
        int intervalCount = (int) ((endTime.getTime() - startTime.getTime()) / timeSpan);
        int count = 5 * intervalCount;
        if (count < 1 || intervalCount < 1)
        {
            return null;
        }
        Map<String, List<DataEntity>> resultMap = getTrendMap(pointMap, startTime, endTime, count, intervalCount);
        return resultMap;
    }

    /**
     * 根据时间隔读取，读取一段时间内,等隔时间的趋势数据（返回时间段内最大、最小、开始、结束值）
     * 
     * @param pointMap
     *            测点名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 等隔时间的趋势数据
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getTrendDataMap(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return null;
        }
        int intervalCount = (int) ((endTime.getTime() - startTime.getTime()) / timeSpan);
        int count = 5 * intervalCount;
        if (count < 1 || intervalCount < 1)
        {
            return null;
        }
        return getTrendMap(pointMap, startTime, endTime, count, intervalCount);
    }

    /**
     * 获取指定测点在一段时间内的趋势数据
     * 
     * @param pointMap
     *            测点名称对应的测点属性的集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param count
     *            需要获取的条数
     * @param intervalCount
     *            在一段时间内的间隔段数
     * @return 测点名称对应的趋势数据集合
     * @throws DataAsException
     *             自定义异常
     */
    private Map<String, List<DataEntity>> getTrendMap(Map<String, RTPoint> pointMap, Date startTime, Date endTime, int count, int intervalCount) throws DataAsException
    {
        Map<String, List<DataEntity>> resultMap = new HashMap<String, List<DataEntity>>();
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            switch (point.getDataType()) {
                case RT_INT8:
                case RT_INT16:
                case RT_INT32:
                case RT_UINT8:
                case RT_UINT16:
                case RT_UINT32:
                case RT_INT64:
                case RT_BOOL:
                    resultMap.put(point.getTagName(), getTrendIntData(point.getId(), intervalCount, count, startTime, endTime));
                    break;
                case RT_FLOAT:
                case RT_DOUBLE:
                    resultMap.put(point.getTagName(), getTrendFloatData(point.getId(), intervalCount, count, startTime, endTime));
                    break;
                default:
                    // System.err.println(tagName + " 该类型不支持趋势数据的获取");
                    break;
            }
        }
        return resultMap;
    }

    /**
     * 获取 浮点 类型趋势数据
     * 
     * @param id
     *            测点id
     * @param intervalCount
     *            在一段时间内的间隔段数
     * @param count
     *            需要获取的条数
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 趋势数据
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getTrendFloatData(int id, int intervalCount, int count, Date startTime, Date endTime) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        List<DoubleData> dataList = new ArrayList<DoubleData>();
        try
        {
            dataList = his.getFloatPlotValues(id, intervalCount, count, startTime, endTime);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }

            for (DoubleData data : dataList)
            {
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getTrendFloatData_1", new String[] { "id", "intervalCount", "count", "startTime", "endTime" },
                    new Object[] { id, intervalCount, count, startTime, endTime }, e);
        }

        return list;
    }

    /**
     * 获取 int 类型趋势数据
     * 
     * @param id
     *            测点id
     * @param intervalCount
     *            在一段时间内的间隔段数
     * @param count
     *            需要获取的条数
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 趋势数据
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getTrendIntData(int id, int intervalCount, int count, Date startTime, Date endTime) throws DataAsException
    {
        List<DataEntity> list = new ArrayList<DataEntity>();
        List<IntData> dataList = new ArrayList<IntData>();
        try
        {
            dataList = his.getIntPlotValues(id, intervalCount, count, startTime, endTime);
            if (dataList == null || dataList.isEmpty())
            {
                return list;
            }

            for (IntData data : dataList)
            {
                DataEntity entity = new DataEntity();
                entity.setId(data.getId());
                entity.setTime(data.getDateTime());
                entity.setValue(data.getValue());
                list.add(entity);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getTrendIntData_1", new String[] { "id", "intervalCount", "count", "startTime", "endTime" },
                    new Object[] { id, intervalCount, count, startTime, endTime }, e);
        }

        return list;
    }

    /**
     * 
     * @param pointMap
     *            测点名称对应的测点属性的集合
     * @param timeList
     *            时间戳集合
     * @return 插值集合
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, List<DataEntity>> getTimeValuesHistoryData(Map<String, RTPoint> pointMap, List<Date> timeList) throws DataAsException
    {
        Map<String, List<DataEntity>> resultMap = new HashMap<String, List<DataEntity>>();
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            switch (point.getDataType()) {
                case RT_INT8:
                case RT_INT16:
                case RT_INT32:
                case RT_UINT8:
                case RT_UINT16:
                case RT_UINT32:
                case RT_INT64:
                case RT_BOOL:
                    resultMap.put(tagName, getIntTimeValues(point.getId(), timeList));
                    break;
                case RT_FLOAT:
                case RT_DOUBLE:
                    resultMap.put(tagName, getFloatTimeValues(point.getId(), timeList));
                    break;
                default:
                    // System.err.println(tagName + " 该类型不支持趋势数据的获取");
                    break;
            }
        }
        return resultMap;
    }

    /**
     * 获取int类型对应时间戳集合的插值数据
     * 
     * @param id
     *            测点id
     * @param timeList
     *            时间戳集合
     * @return 插值集合
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getIntTimeValues(int id, List<Date> timeList) throws DataAsException
    {
        if (id <= 0 || timeList == null || timeList.isEmpty())
        {
            return null;
        }

        List<IntData> datas = new ArrayList<IntData>();
        try
        {
            Date[] timeArray = new Date[timeList.size()];
            datas = his.getIntTimedValues(id, timeList.size(), timeList.toArray(timeArray));
            if (datas == null || datas.isEmpty())
            {
                return null;
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getIntTimeValues_1", new String[] { "id", "timeList" }, new Object[] { id, timeList }, e);
        }

        List<DataEntity> list = new ArrayList<DataEntity>();
        for (IntData data : datas)
        {
            DataEntity entity = new DataEntity();
            entity.setId(data.getId());
            entity.setTime(data.getDateTime());
            entity.setValue(data.getValue());
            list.add(entity);
        }
        return list;
    }

    /**
     * 获取 浮点 类型对应时间戳集合的插值数据
     * 
     * @param id
     *            测点id
     * @param timeList
     *            时间戳集合
     * @return 插值集合
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> getFloatTimeValues(int id, List<Date> timeList) throws DataAsException
    {
        if (id <= 0 || timeList == null || timeList.isEmpty())
        {
            return null;
        }

        List<DoubleData> datas = new ArrayList<DoubleData>();
        try
        {
            Date[] timeArray = new Date[timeList.size()];
            datas = his.getFloatTimedValues(id, timeList.size(), timeList.toArray(timeArray));
            if (datas == null || datas.isEmpty())
            {
                return null;
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataOperation_getFloatTimeValues_1", new String[] { "id", "timeList" }, new Object[] { id, timeList }, e);
        }
        List<DataEntity> list = new ArrayList<DataEntity>();
        for (DoubleData data : datas)
        {
            DataEntity entity = new DataEntity();
            entity.setId(data.getId());
            entity.setTime(data.getDateTime());
            entity.setValue(data.getValue());
            list.add(entity);
        }
        return list;
    }

    /**
     * 统计多个测点在一段时间内的最大值或者最小值
     * 
     * @param pointMap
     *            测点名称——>测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param model
     *            统计模式
     * @return 最大值或者最小值
     * @throws DataAsException
     *             自定义异常
     */
    public DataEntity calculation(Map<String, RTPoint> pointMap, Date startTime, Date endTime, RTCalculationMode model) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return null;
        }
        List<DataEntity> dataList = sumaryTagListData(pointMap, startTime, endTime, model);
        if (dataList == null || dataList.isEmpty())
        {
            return null;
        }
        DataEntity result = sumaryDataList(dataList, model);
        return result;
    }

    /**
     * 根据统计模式获取测点在指定时间段内的统计值的集合
     * 
     * @param pointMap
     *            测点名称——>测点信息集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param model
     *            统计模式
     * @return 统计值集合
     * @throws DataAsException
     *             自定义异常
     */
    private List<DataEntity> sumaryTagListData(Map<String, RTPoint> pointMap, Date startTime, Date endTime, RTCalculationMode model) throws DataAsException
    {
        List<DataEntity> resultList = new ArrayList<DataEntity>();
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            DataEntity entity = sumarySingleTagData(tagName, point, startTime, endTime, model);
            if (entity != null)
            {
                resultList.add(entity);
            }
        }
        return resultList;
    }

    /**
     * 根据统计模式统计 指定集合中的值
     * 
     * @param dataList
     *            统计值集合
     * @param model
     *            统计取值模式
     * @return 统计值
     */
    private DataEntity sumaryDataList(List<DataEntity> dataList, RTCalculationMode model)
    {
        double result = (double) dataList.get(0).getValue();

        DataEntity resultModel = new DataEntity();
        for (DataEntity data : dataList)
        {
            double tempValue = (double) data.getValue();
            if (model == RTCalculationMode.RT_MAX)
            {
                if (result <= tempValue)
                {
                    result = tempValue;
                    resultModel.setId(data.getId());
                    resultModel.setTagName(data.getTagName());
                    resultModel.setValue(result);
                    resultModel.setTime(data.getTime());
                }
            }
            else if (model == RTCalculationMode.RT_MIN)
            {
                if (result >= tempValue)
                {
                    resultModel.setId(data.getId());
                    resultModel.setTagName(data.getTagName());
                    resultModel.setValue(result);
                    resultModel.setTime(data.getTime());
                }
            }
            else
            {
                // System.err.println(data.getTagName() + " 没有对应的统计模式");
            }
        }
        return resultModel;
    }

    /**
     * 获取单个统计数据
     * 
     * @param tagName
     *            测点名称
     * @param point
     *            测点属性
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param model
     *            统计模式
     * @return 单个统计数据
     * @throws DataAsException
     *             自定义异常
     */
    private DataEntity sumarySingleTagData(String tagName, RTPoint point, Date startTime, Date endTime, RTCalculationMode model) throws DataAsException
    {
        DataEntity entity = new DataEntity();
        try
        {
            SummaryEntity summaryEntity = his.getNumberSummary(point.getId(), startTime, endTime);
            if (summaryEntity == null)
            {
                return null;
            }

            entity.setId(point.getId());
            entity.setTagName(tagName);

            if (model == RTCalculationMode.RT_MAX)
            {
                entity.setValue(summaryEntity.getMaxValue());
                entity.setTime(summaryEntity.getMaxValueTime());

            }
            else if (model == RTCalculationMode.RT_MIN)
            {
                entity.setValue(summaryEntity.getMinValue());
                entity.setTime(summaryEntity.getMinValueTime());
            }
            else if (model == RTCalculationMode.RT_SUM)
            {
                entity.setValue(summaryEntity.getTotalValue());
            }
            else
            {
                // System.err.println(tagName + " 没有对应的统计模式");
            }
        }
        catch (Exception e)
        {
            // System.err.println(tagName + " 不支持统计的类型" + e.getMessage());
            DataAsExpSet.throwExpByResCode("DataOperation_sumarySingleTagData_1", new String[] { "tagName", "point", "startTime", "endTime", "model" },
                    new Object[] { tagName, point, startTime, endTime, model }, e);
        }

        return entity;
    }

    /**
     * 返回累计值
     * 
     * @param pointMap
     *            测点（计算点）名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return (测点名称，数值)Map
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, Integer> getSumValue(Map<String, RTPoint> pointMap, Date startTime, Date endTime) throws DataAsException
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return null;
        }

        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            int count;
            try
            {
                count = his.getArchivedValuesCount(point.getId(), startTime, endTime);
                if (count <= 0)
                {
                    continue;
                }
                if (count == 1)
                {
                    count += 1;
                }
                String filter = "'" + GoldenUtils.getTableTagName(tableName, point.getTagName()) + "'==1";
                List<IntData> data = his.getArchivedIntValuesFilt(point.getId(), filter, count, startTime, endTime);
                resultMap.put(tagName, data.size());
            }
            catch (Exception e)
            {
                // System.err.println(tagName + " 不支持统计的类型" + e.getMessage());
                DataAsExpSet.throwExpByResCode("DataOperation_getSumValue_1", new String[] { "pointMap", "startTime", "endTime" }, new Object[] { pointMap, startTime, endTime }, e);
            }
        }
        return resultMap;
    }

    /**
     * 获取计算点质量码
     * 
     * @param pointMap
     *            测点（计算点）名称集合
     * @return 计算点质量码
     * @throws Exception
     */
    public Map<String, String> getCalcPointQuality(Map<String, RTPoint> pointMap) throws Exception
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return null;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            Entity<IntData> entdata = new Entity<IntData>();
            entdata = snap.getIntSnapshots(new int[] { point.getId() });
            if (entdata != null && !entdata.getList().isEmpty())
            {
                resultMap.put(tagName, String.valueOf(entdata.getList().get(0).getQuality()));
            }

        }
        return resultMap;
    }

    /**
     * 获取测点历史数据数量
     * 
     * @param id
     *            测点id
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史数据数量
     * @throws Exception
     */
    public int getDataCount(int id, Date startTime, Date endTime) throws Exception
    {
        int result = 0;
        result = his.getArchivedValuesCount(id, startTime, endTime);
        return result;
    }

    /**
     * 返回指定时间段所有数据点的最大数量
     * 
     * @param pointMap
     *            测点（计算点）名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 最大数量
     * @throws Exception
     */
    public int getDataMaxCount(Map<String, RTPoint> pointMap, Date startTime, Date endTime) throws Exception
    {
        if (pointMap == null || pointMap.isEmpty())
        {
            return 0;
        }
        int result = 0;
        for (String tagName : pointMap.keySet())
        {
            RTPoint point = pointMap.get(tagName);
            int count = 0;
            count = his.getArchivedValuesCount(point.getId(), startTime, endTime);
            if (result < count)
            {
                result = count;
            }
        }
        return result;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
}
