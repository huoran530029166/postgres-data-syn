package com.goldwind.dataaccess.rtdb;

import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.rtdb.model.DataEntity;
import com.goldwind.dataaccess.rtdb.model.RTCalculationMode;
import com.goldwind.dataaccess.rtdb.model.RTHistorianMode;
import com.goldwind.dataaccess.rtdb.model.RTPoint;

/**
 * 实时库操作类接口
 * 
 * @author shenlf
 *
 */
public interface IRTDataOper
{
    /**
     * 获取数据源ip
     * 
     * @return 数据源
     */
    String getDataSource();

    /********************** 其他方法 *********************************/
    /**
     * 测试数据库连接
     * 
     * @return 是否成功
     */
    boolean test();

    /**
     * 开启数据库会话
     * 
     * @throws DataAsException
     *             自定义异常
     */
    void open() throws DataAsException;

    /**
     * 关闭连接
     * 
     * @throws DataAsException
     *             自定义异常
     */
    void close() throws DataAsException;

    /**
     * 获取测点表id，表名称为初始化数据
     * 
     * @param tableName
     *            表名
     * @return 测点表id
     * @throws DataAsException
     *             自定义异常
     */
    int getTableId(String tableName) throws DataAsException;

    /****************** 对数据点的操作(创建，获取，更新，查询，删除，修改) **********/
    /**
     * 创建测点
     * 
     * @param point
     *            数据测点
     * @throws DataAsException
     *             自定义异常
     */
    void createPoint(RTPoint point) throws DataAsException;

    /**
     * 批量创建测点
     * 
     * @param points
     *            数据测点
     * @return 创建成功的个数
     * @throws DataAsException
     *             自定义异常
     */
    int createPoints(List<RTPoint> points) throws DataAsException;

    /**
     * 创建计算点
     * 
     * @param point
     *            数据测点
     * @throws DataAsException
     *             自定义异常
     */
    void createCalcPoint(RTPoint point) throws DataAsException;

    /**
     * 批量创建计算点
     * 
     * @param points
     *            数据测点
     * @return 创建成功的个数
     * @throws DataAsException
     *             自定义异常
     */
    int createCalcPoints(List<RTPoint> points) throws DataAsException;

    /**
     * 更新基本测点名称
     * 
     * @param oldName
     *            原测点名称
     * @param newName
     *            新测点名称
     * @return 是否更新成功
     * @throws DataAsException
     *             自定义异常
     */
    boolean updateBasePointTagName(String oldName, String newName) throws DataAsException;

    /**
     * 更新单个计算点
     * 
     * @param point
     *            数据测点
     * @return 是否更新成功
     * @throws DataAsException
     *             自定义异常
     */
    boolean updateCalcPoint(RTPoint point) throws DataAsException;

    /**
     * 批量获取测点属性
     * 
     * @param tagNames
     *            测点名称
     * @return 测点集合
     * @throws DataAsException
     *             自定义异常
     */
    List<RTPoint> getPropertyPoints(String[] tagNames) throws DataAsException;

    /**
     * 在数据表中搜索符合条件的测点，支持"*"和"?"通配符，缺省设置为"*"，长度不得超过测点名称占用字节数
     * 
     * @param tagName
     *            测点名
     * @return 符合条件的测点
     * @throws DataAsException
     *             自定义异常
     */
    List<RTPoint> search(String tagName) throws DataAsException;

    /**
     * 批量删除数据测点
     * 
     * @param tableTagNames
     *            测点全名称
     * @return 删除是否成功
     * @throws DataAsException
     *             自定义异常
     */
    int removePoints(String[] tableTagNames) throws DataAsException;

    /****************** 写入数据 **************************************/
    /**
     * 批量写入标签数据
     * 
     * @param list
     *            数据实体集合
     * @return 写入成功数量
     * @throws DataAsException
     *             自定义异常
     */
    int putData(List<DataEntity> list) throws DataAsException;

    /****************** 读取数据 **************************************/
    /**
     * 返回指定时间段所有数据
     * 
     * @param tagNames
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, List<DataEntity>> getData(String[] tagNames, Date startTime, Date endTime) throws DataAsException;

    /**
     * 根据时间隔读取，读取一段时间内,等隔时间的历史数据
     * 
     * @param tagNames
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, List<DataEntity>> getDataByTimeSpan(String[] tagNames, Date startTime, Date endTime, int timeSpan) throws DataAsException;

    /**
     * 根据需要的行数，读取一段时间内,等隔时间的历史数据
     * 
     * @param tagNames
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param count
     *            需要数据的个数
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, List<DataEntity>> getDataByCount(String[] tagNames, Date startTime, Date endTime, int count) throws DataAsException;

    /**
     * 获取批量测点在某一时间的历史断面数据
     * 
     * @param tagNames
     *            测点名称
     * @param historianMode
     *            历史数据取值模式
     * @param time
     *            时间
     * @return 断面数据
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, DataEntity> getCrossSectionData(String[] tagNames, RTHistorianMode historianMode, Date time) throws DataAsException;

    /**
     * 根据时间隔读取，读取一段时间内,等隔时间的趋势数据（返回时间段内最大、最小、开始、结束值）
     * 
     * @param tagNames
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param timeSpan
     *            等隔时间（毫秒）
     * @return 历史数据
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, List<DataEntity>> getTrendData(String[] tagNames, Date startTime, Date endTime, int timeSpan) throws DataAsException;

    /**
     * 返回指定时间段数据点的数量
     * 
     * @param tagName
     *            测点名称
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 测点名称对应的数据点的数量
     * @throws DataAsException
     *             自定义异常
     */
    int getDataCount(String tagName, Date startTime, Date endTime) throws Exception;

    /**
     * 返回指定时间段所有数据点的最大数量
     * 
     * @param tagNames
     *            测点（计算点）名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 测点名称对应的数据点的最大数量
     * @throws DataAsException
     *             自定义异常
     */
    int getDataCount(String[] tagNames, Date startTime, Date endTime) throws Exception;

    /****************** 统计操作 **************************************/
    /**
     * 返回统计结果
     * 
     * @param tagNames
     *            测点（计算点）名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param calculationMode
     *            统计模式
     * @return 统计结果
     * @throws DataAsException
     *             自定义异常
     */
    DataEntity calculation(String[] tagNames, Date startTime, Date endTime, RTCalculationMode calculationMode) throws DataAsException;

    /**
     * 返回累计值
     * 
     * @param tagNames
     *            测点（计算点）名称集合
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return sum值
     * @throws DataAsException
     *             自定义异常
     */
    Map<String, Integer> getSumValue(String[] tagNames, Date startTime, Date endTime) throws DataAsException;

    /**
     * 获取计算点质量码
     * 
     * @param tagNames
     *            测点（计算点）名称集合
     * @return 质量码
     * @throws DataAsException
     *             自定义异常
     * @throws Exception
     */
    Map<String, String> getCalcPointQuality(String[] tagNames) throws Exception;

    /**
     * 添加新表
     * 
     * @param tableName
     *            添加的表名
     * @param tableDesc
     *            添加的表描述
     * @return 返回新建表的ID
     * @throws DataAsException
     *             自定义异常
     * 
     */
    int appendTable(String tableName, String tableDesc) throws DataAsException;

    /**
     * 设置表名，用于该类中其他操作使用
     * 
     * @param tableName
     *            表名
     */
    void setTableName(String tableName);

    /**
     * 下载设备指定范围的数据
     * 
     * @param deviceId
     *            设备ID
     * @param tagMap
     *            测点-描述
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return 数据流InputStream
     * @throws DataAsException
     *             自定义异常
     */
    InputStream downloadData(String deviceId, LinkedHashMap<String, String> tagMap, Date startTime, Date endTime) throws DataAsException;
}
