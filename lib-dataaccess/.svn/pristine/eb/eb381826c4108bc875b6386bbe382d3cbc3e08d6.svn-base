package com.goldwind.dataaccess.rtdb.golden.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.bigdata.gtsdb.sdk.IRTDataOperImpl;
import com.goldwind.bigdata.gtsdb.vo.ApiReadData;
import com.goldwind.bigdata.gtsdb.vo.ApiResponse;
import com.goldwind.bigdata.gtsdb.vo.ApiTagValueList;
import com.goldwind.bigdata.gtsdb.vo.DataFilterList;
import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.rtdb.IRTDataOper;
import com.goldwind.dataaccess.rtdb.golden.oper.DataOperation;
import com.goldwind.dataaccess.rtdb.golden.oper.GoldenServerPool;
import com.goldwind.dataaccess.rtdb.golden.oper.PointOperation;
import com.goldwind.dataaccess.rtdb.model.DataEntity;
import com.goldwind.dataaccess.rtdb.model.RTCalculationMode;
import com.goldwind.dataaccess.rtdb.model.RTHistorianMode;
import com.goldwind.dataaccess.rtdb.model.RTPoint;

/**
 * 庚顿实时数据操作类
 * 
 * @author shenlf
 *
 */
public class GoldenRTDataOper implements IRTDataOper
{
    /**
     * 标签点操作类
     */
    private PointOperation pointOper = null;
    /**
     * 读取数据操作类(Golden)
     */
    private DataOperation dataOper = null;
    /**
     * 读取数据操作类(Bigdata)
     */
    private com.goldwind.bigdata.gtsdb.sdk.IRTDataOper bigDataOper = null;
    /**
     * 测点表名
     */
    private String tableName;

    public GoldenRTDataOper(String tableName) throws DataAsException
    {
        this.tableName = tableName;
        // 判断数据库类型
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                pointOper = new PointOperation(tableName);
                dataOper = new DataOperation(tableName);
                break;
            case Bigdata:
                bigDataOper = new IRTDataOperImpl();
                break;
            default:
                break;
        }
    }

    public String getTableName()
    {
        return tableName;
    }

    @Override
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
        pointOper.setTableName(tableName);
        dataOper.setTableName(tableName);
    }

    @Override
    public String getDataSource()
    {
        return null;
    }

    @Override
    public boolean test()
    {
        return true;
    }

    @Override
    public void open() throws DataAsException
    {
        // 判断数据库类型
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                pointOper.open();
                dataOper.open();
                break;
            case Bigdata:

                // 判断是否开启Socks5代理的连接方式，避免HTTP检查
                if (!GoldenServerPool.getSOCKSKEY().isEmpty() && !GoldenServerPool.getTARGETUSER().isEmpty())
                {
                    bigDataOper.connect(GoldenServerPool.getIp(), GoldenServerPool.getPort(), tableName, GoldenServerPool.getSOCKSKEY(), GoldenServerPool.getTARGETUSER(),
                            GoldenServerPool.getLOCALPROXYPORT());
                }
                else
                {
                    bigDataOper.connect(GoldenServerPool.getIp(), GoldenServerPool.getPort(), tableName);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void close() throws DataAsException
    {
        // 判断数据库类型
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                pointOper.disConn();
                dataOper.disConn();
                break;
            default:
                break;
        }
    }

    @Override
    public int appendTable(String tableName, String tableDesc) throws DataAsException
    {
        return pointOper.appendTable(tableName, tableDesc);
    }

    @Override
    public int getTableId(String tableName) throws DataAsException
    {
        return pointOper.getTableId(tableName);
    }

    @Override
    public void createPoint(RTPoint point) throws DataAsException
    {
        pointOper.createPoint(point);
    }

    @Override
    public int createPoints(List<RTPoint> points) throws DataAsException
    {
        return pointOper.createPoints(points);
    }

    @Override
    public void createCalcPoint(RTPoint point) throws DataAsException
    {
        pointOper.createCalcPoint(point);

    }

    @Override
    public int createCalcPoints(List<RTPoint> points) throws DataAsException
    {
        return pointOper.createCalcPoints(points);
    }

    @Override
    public boolean updateBasePointTagName(String oldName, String newName) throws DataAsException
    {
        return pointOper.updateBasePointTagName(oldName, newName);
    }

    @Override
    public boolean updateCalcPoint(RTPoint point) throws DataAsException
    {
        return pointOper.updateCalcPoint(point);
    }

    @Override
    public List<RTPoint> getPropertyPoints(String[] tagNames) throws DataAsException
    {
        return pointOper.getPointsByTagNames(tagNames);
    }

    @Override
    public List<RTPoint> search(String tagName) throws DataAsException
    {
        return pointOper.search(tagName);
    }

    @Override
    public int removePoints(String[] tableTagNames) throws DataAsException
    {
        return pointOper.removePoints(tableTagNames);
    }

    @Override
    public int putData(List<DataEntity> list) throws DataAsException
    {
        return dataOper.putData(list);
    }

    @Override
    public Map<String, List<DataEntity>> getData(String[] tagNames, Date startTime, Date endTime) throws DataAsException
    {
        // 判断数据库类型,返回指定时间段所有数据
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                List<RTPoint> points = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    points = pointOper.getPointsByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    points = pointOper.getPointsByTagNames(tagNamesforrealtimedata);
                }
                return convertDatas(tagNames, dataOper.getData(points, startTime, endTime), ".", "_");
            case Bigdata:
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getData(tagNamesforbiddata, startTime, endTime);
                return convertDatas(tagNames, standardizeData(bigdatas), "_", ".");// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;
    }

    // /**
    // * 将大数据接口数据转成标准格式(原大数据返回类型，401版本后废弃)
    // *
    // * @param bigdatas
    // * 大数据返回数据
    // * @return 标准格式数据
    // * @throws DataAsException
    // * 自定义异常
    // */
    // private Map<String, List<DataEntity>> standardizeDataOld(Map<String, List<com.goldwind.bigdata.query.client.entity.DataEntity>> bigdatas) throws DataAsException
    // {
    // // 标准格式数据
    // Map<String, List<DataEntity>> data = new HashMap<>();
    // if (null != bigdatas && !bigdatas.isEmpty())
    // {
    // for (String tagname : bigdatas.keySet())
    // {
    // List<DataEntity> dataEntitys = new ArrayList<>();
    // List<com.goldwind.bigdata.query.client.entity.DataEntity> bigtDataEntity = bigdatas.get(tagname);
    // for (int i = 0; i < bigtDataEntity.size(); i++)
    // {
    // DataEntity dataEntity = new DataEntity();
    // dataEntity.setTagName(tagname.substring(tagname.indexOf(".") + 1));
    // dataEntity.setValue(bigtDataEntity.get(i).getValue());
    // dataEntity.setTime(new Date(bigtDataEntity.get(i).getTime()));
    // dataEntitys.add(dataEntity);
    // }
    // data.put(tagname, dataEntitys);
    // }
    // }
    // return data;
    // }

    /**
     * 将大数据接口数据转成标准格式
     * 
     * @param apiResponse
     *            大数据返回数据
     * @return 标准格式数据
     * @throws DataAsException
     *             自定义异常
     */
    private Map<String, List<DataEntity>> standardizeData(ApiResponse<ApiReadData> apiResponse) throws DataAsException
    {
        // 标准格式数据
        Map<String, List<DataEntity>> data = new HashMap<>();
        // 收到大数据的数据
        Map<String, ApiTagValueList> bigdatas = apiResponse.getData().getValue();
        if (null != bigdatas && !bigdatas.isEmpty())
        {
            for (String tagname : bigdatas.keySet())
            {
                List<DataEntity> dataEntitys = new ArrayList<>();
                ApiTagValueList bigDataEntity = bigdatas.get(tagname);
                // List<Long> times = apiResponse.getData().getTimeCollect().get(0).getTimeList();
                List<Long> times = bigDataEntity.getLazyTimeList();
                for (int i = 0; i < times.size(); i++)
                {
                    DataEntity dataEntity = new DataEntity();
                    dataEntity.setTagName(tagname.substring(tagname.indexOf(".") + 1));
                    dataEntity.setValue(bigDataEntity.getValueList().get(i));
                    dataEntity.setTime(new Date(times.get(i)));
                    dataEntitys.add(dataEntity);
                }
                data.put(tagname, dataEntitys);
            }
        }
        return data;
    }

    @Override
    public Map<String, List<DataEntity>> getDataByTimeSpan(String[] tagNames, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        // 判断数据库类型,根据时间隔读取，读取一段时间内,等隔时间的历史数据
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                Map<String, RTPoint> pointMap = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    pointMap = pointOper.getPointMapByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    pointMap = pointOper.getPointMapByTagNames(tagNamesforrealtimedata);
                }
                return convertDatas(tagNames, dataOper.getDataByTimeSpan(pointMap, startTime, endTime, timeSpan), ".", "_");
            case Bigdata:
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getDataByTimeSpanHighLevel(tagNamesforbiddata, startTime, endTime, timeSpan);
                return convertDatas(tagNames, standardizeData(bigdatas), "_", ".");// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;
    }

    @Override
    public Map<String, List<DataEntity>> getDataByCount(String[] tagNames, Date startTime, Date endTime, int count) throws DataAsException
    {
        // 判断数据库类型,根据需要的行数，读取一段时间内,等隔时间的历史数据
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                Map<String, RTPoint> pointMap = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    pointMap = pointOper.getPointMapByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    pointMap = pointOper.getPointMapByTagNames(tagNamesforrealtimedata);
                }
                return convertDatas(tagNames, dataOper.getDataByCount(pointMap, startTime, endTime, count), ".", "_");
            case Bigdata:
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getDataByCount(tagNamesforbiddata, startTime, endTime, count);
                return convertDatas(tagNames, standardizeData(bigdatas), "_", ".");// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;
    }

    @Override
    public Map<String, DataEntity> getCrossSectionData(String[] tagNames, RTHistorianMode historianMode, Date time) throws DataAsException
    {
        // 获取批量测点在某一时间的历史断面数据
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:// 获取断面数据
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                List<RTPoint> points = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    points = pointOper.getPointsByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    points = pointOper.getPointsByTagNames(tagNamesforrealtimedata);
                }
                return convertData(tagNames, dataOper.getCrossSectionData(points, historianMode, time), ".", "_");
            case Bigdata:// 取某时刻点后面最近一条数据
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getSingleData(tagNamesforbiddata, time);
                Map<String, List<DataEntity>> datas = standardizeData(bigdatas);
                Map<String, DataEntity> result = new HashMap<>();
                for (String tagname : datas.keySet())
                {
                    List<DataEntity> dataEntities = datas.get(tagname);
                    if (null != dataEntities && !dataEntities.isEmpty())
                    {
                        result.put(tagname, dataEntities.get(0));
                    }
                }
                return convertData(tagNames, result, "_", ".");// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;

    }

    @Override
    public Map<String, List<DataEntity>> getTrendData(String[] tagNames, Date startTime, Date endTime, int timeSpan) throws DataAsException
    {
        // 判断数据库类型,根据时间隔读取，读取一段时间内,等隔时间的趋势数据（返回时间段内最大、最小、开始、结束值）
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                Map<String, RTPoint> pointMap = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    pointMap = pointOper.getPointMapByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    pointMap = pointOper.getPointMapByTagNames(tagNamesforrealtimedata);
                }
                return convertDatas(tagNames, dataOper.getTrendData(pointMap, startTime, endTime, timeSpan), ".", "_");
            case Bigdata:
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getTrendData(tagNamesforbiddata, startTime, endTime, timeSpan);
                return convertDatas(tagNames, standardizeData(bigdatas), "_", ".");// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;
    }

    @Override
    public int getDataCount(String tagName, Date startTime, Date endTime) throws Exception
    {
        // 返回指定时间段数据点的数量
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                List<RTPoint> points = pointOper.getPointsByTagNames(new String[] { tagName.replace(".", "_") });
                if (points == null || points.isEmpty())
                {
                    return 0;
                }
                int id = points.get(0).getId();
                return dataOper.getDataCount(id, startTime, endTime);
            // case Bigdata:
            // String tagNamesforbiddata = tagName.replace("_", ".");
            // return bigDataOper.getDataCount(tagNamesforbiddata, startTime, endTime);
            default:
                break;
        }
        return 0;
    }

    @Override
    public int getDataCount(String[] tagNames, Date startTime, Date endTime) throws Exception
    {
        // 返回指定时间段所有数据点的最大数量
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                String[] tagNamesforrealtimedata = ArrayOper.arraysReplace(tagNames, ".", "_");
                Map<String, RTPoint> pointMap = null;
                if (GoldenServerPool.getLOWERCASEFLG())
                {
                    pointMap = pointOper.getPointMapByTagNames(ArrayOper.lowerTrimArray(tagNamesforrealtimedata));
                }
                else
                {
                    pointMap = pointOper.getPointMapByTagNames(tagNamesforrealtimedata);
                }

                return dataOper.getDataMaxCount(pointMap, startTime, endTime);
            // case Bigdata:
            // String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
            // return bigDataOper.getDataCount(tagNamesforbiddata, startTime, endTime);
            default:
                break;
        }
        return 0;
    }

    @Override
    public InputStream downloadData(String deviceId, LinkedHashMap<String, String> tagMap, Date startTime, Date endTime) throws DataAsException
    {
        // 判断数据库类型,如果是大数据,下载设备指定范围的数据
        switch (GoldenServerPool.getPoolDbType()) {
            case Bigdata:
                return bigDataOper.download(deviceId, tagMap, startTime, endTime);
            default:
                break;
        }
        return null;
    }

    /**
     * 返回指定时间段所有数据点的最大数量
     * 
     * @param tagNames
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param filterList
     *            查询条件
     * @return 标准格式数据
     * @throws Exception
     *             异常
     */
    public Map<String, List<DataEntity>> getDataByFilter(String[] tagNames, Date startTime, Date endTime, DataFilterList filterList) throws Exception
    {
        // 返回指定时间段所有数据点的最大数量
        switch (GoldenServerPool.getPoolDbType()) {
            case Bigdata:
                ApiResponse<ApiReadData> bigdatas = null;
                String[] tagNamesforbiddata = ArrayOper.arraysReplace(tagNames, "_", ".");
                bigdatas = bigDataOper.getDataByFilter(tagNamesforbiddata, startTime, endTime, filterList);
                return standardizeData(bigdatas);// 将大数据接口数据转成标准格式
            default:
                break;
        }
        return null;
    }

    /**
     * 将返回Map中的Key替换为原先传入的测点名称
     * 
     * @param tagNames
     *            测点名称
     * @param datas
     *            以实时库的测点名称作为key的Map
     * @param oldChar
     *            旧字符(传入测点名称的字符)
     * @param newChar
     *            新字符(库中测点名称的字符)
     * @return 以传入的测点名称作为key的Map
     */
    private Map<String, List<DataEntity>> convertDatas(String[] tagNames, Map<String, List<DataEntity>> datas, String oldChar, String newChar)
    {
        // 以传入的测点名称作为key的Map
        Map<String, List<DataEntity>> result = new HashMap<String, List<DataEntity>>();
        // 测点名称 信息，key:实时库中的测点名称 value:原先传入的测点名称
        Map<String, String> tagnameInfos = new HashMap<String, String>();

        for (int i = 0; i < tagNames.length; i++)
        {
            String rtdbTageName = tagNames[i].replace(oldChar, newChar);
            if (GoldenServerPool.getPoolDbType() == DatabaseType.Golden && GoldenServerPool.getLOWERCASEFLG())
            {
                rtdbTageName = rtdbTageName.toLowerCase();
            }
            tagnameInfos.put(rtdbTageName, tagNames[i]);
        }

        for (String rtdbTageName : datas.keySet())
        {
            result.put(tagnameInfos.get(rtdbTageName), datas.get(rtdbTageName));
        }

        return result;
    }

    /**
     * 将返回Map中的Key替换为原先传入的测点名称
     * 
     * @param tagNames
     *            测点名称
     * @param data
     *            以实时库的测点名称作为key的Map
     * @param oldChar
     *            旧字符(传入测点名称的字符)
     * @param newChar
     *            新字符(库中测点名称的字符)
     * @return 以传入的测点名称作为key的Map
     */
    private Map<String, DataEntity> convertData(String[] tagNames, Map<String, DataEntity> data, String oldChar, String newChar)
    {
        // 以传入的测点名称作为key的Map
        Map<String, DataEntity> result = new HashMap<String, DataEntity>();
        // 测点名称 信息，key:实时库中的测点名称 value:原先传入的测点名称
        Map<String, String> tagnameInfos = new HashMap<String, String>();

        for (int i = 0; i < tagNames.length; i++)
        {
            String rtdbTageName = tagNames[i].replace(oldChar, newChar);
            if (GoldenServerPool.getPoolDbType() == DatabaseType.Golden && GoldenServerPool.getLOWERCASEFLG())
            {
                rtdbTageName = rtdbTageName.toLowerCase();
            }
            tagnameInfos.put(rtdbTageName, tagNames[i]);
        }

        for (String rtdbTageName : data.keySet())
        {
            result.put(tagnameInfos.get(rtdbTageName), data.get(rtdbTageName));
        }

        return result;
    }

    @Override
    public DataEntity calculation(String[] tagNames, Date startTime, Date endTime, RTCalculationMode calculationMode) throws DataAsException
    {
        Map<String, RTPoint> pointMap = pointOper.getPointMapByTagNames(tagNames);
        return dataOper.calculation(pointMap, startTime, endTime, calculationMode);
    }

    @Override
    public Map<String, Integer> getSumValue(String[] tagNames, Date startTime, Date endTime) throws DataAsException
    {
        Map<String, RTPoint> pointMap = pointOper.getPointMapByTagNames(tagNames);
        return dataOper.getSumValue(pointMap, startTime, endTime);
    }

    @Override
    public Map<String, String> getCalcPointQuality(String[] tagNames) throws Exception
    {
        Map<String, RTPoint> pointMap = pointOper.getPointMapByTagNames(tagNames);
        return dataOper.getCalcPointQuality(pointMap);
    }

}
