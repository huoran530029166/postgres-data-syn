package com.goldwind.dataaccess.rtdb.golden.oper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.rtdb.golden.common.GoldenGlobalData;
import com.goldwind.dataaccess.rtdb.golden.common.GoldenUtils;
import com.goldwind.dataaccess.rtdb.golden.map.MappingManagement;
import com.goldwind.dataaccess.rtdb.model.RTDataType;
import com.goldwind.dataaccess.rtdb.model.RTPoint;
import com.rtdb.api.enums.RTDBClass;
import com.rtdb.api.enums.RTDBTimeCopy;
import com.rtdb.api.enums.RTDBTrigger;
import com.rtdb.enums.DataSort;
import com.rtdb.enums.TagChangeReason;
import com.rtdb.model.BasePoint;
import com.rtdb.model.CalcPoint;
import com.rtdb.model.FullPoint;
import com.rtdb.model.MinPoint;
import com.rtdb.model.SearchCondition;
import com.rtdb.model.Table;
import com.rtdb.service.impl.BaseImpl;
import com.rtdb.service.inter.Base;

/**
 * 测点操作类
 * 
 * @author Administrator
 *
 */
public class PointOperation
{
    /**
     * 庚顿基本信息点服务
     */
    private Base base = null;
    /**
     * 内存map
     */
    private MappingManagement map = null;
    /**
     * 测点表名
     */
    private String tableName;

    public PointOperation(String tableName)
    {
        this.tableName = tableName;
        map = new MappingManagement();
    }

    /**
     * 建立连接
     * 
     * @throws DataAsException
     *             自定义异常
     */
    public void open() throws DataAsException
    {
        if (base == null)
        {
            try
            {
                base = new BaseImpl(GoldenServerPool.getPool().getServerImpl());
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("PointOperation_PointOperation_2", new String[] { "tableName" }, new Object[] { tableName }, e);
            }
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
        if (base != null)
        {
            try
            {
                base.close();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("PointOperation_disConn_1", new String[] {}, new Object[] {}, e);
            }
        }
    }

    /************************************** 表管理 ****************************************/
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
    public int appendTable(String tableName, String tableDesc) throws DataAsException
    {
        int tableId = 0;
        if (StringUtils.isBlank(tableName))
        {
            return tableId;
        }
        try
        {
            tableId = base.appendTable(tableName, tableDesc);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_appendTable_1", new String[] { "tableName", "tableDesc" }, new Object[] { tableName, tableDesc }, e);
        }
        return tableId;
    }

    /**
     * 获取测点表id，表名称为初始化数据
     * 
     * @param tableName
     *            表名
     * @return 测点表id
     * @throws DataAsException
     *             自定义异常
     */
    public int getTableId(String tableName) throws DataAsException
    {
        int tableId = 0;
        try
        {
            Table table = base.getTablePropertiesByName(tableName);
            if (table == null || table.getId() <= 0)
            {
                return tableId;
            }
            tableId = table.getId();
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_getTableId_1", new String[] { "tableName" }, new Object[] { tableName }, e);
        }

        return tableId;
    }

    /************************************** 创建测点 **************************************/
    /**
     * 创建单个测点
     * 
     * @param point
     *            需创建测点信息
     * @return 创建结果
     * @return
     * @throws DataAsException
     *             自定义异常
     */
    public boolean createPoint(RTPoint point) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(point.getTagName()))
        {
            return result;
        }
        if (MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim())) != null)
        {
            // System.err.println(GoldenUtils.getTableTagName(tableName, point.getTagName()) + "测点已存在！");
            return result;
        }
        try
        {
            result = createPointCommon(point, MappingManagement.getTableMap().get(tableName));
            if (!result)
            {
                // System.err.println(point.getTagName() + " 创建测点失败！");
            }
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 创建测点失败");
            DataAsExpSet.throwExpByResCode("PointOperation_createPoint_1", new String[] { "point" }, new Object[] { point }, e);
        }
        return result;
    }

    /**
     * 批量创建测点
     * 
     * @param points
     *            需创建测点信息
     * @return 创建成功个数
     * @throws DataAsException
     *             自定义异常
     */
    public int createPoints(List<RTPoint> points) throws DataAsException
    {
        int successCount = 0;
        for (RTPoint point : points)
        {

            if (MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim())) != null)
            {
                // System.err.println(GoldenUtils.getTableTagName(tableName, point.getTagName()) + "测点已存在！");
                continue;
            }
            try
            {
                boolean result = createPointCommon(point, MappingManagement.getTableMap().get(tableName));
                if (!result)
                {
                    // System.err.println(point.getTagName() + " 创建测点失败！");
                }
                else
                {
                    successCount++;
                }
            }
            catch (Exception e)
            {
                // System.err.println(point.getTagName() + " 创建测点失败");
                DataAsExpSet.throwExpByResCode("PointOperation_createPoints_1", new String[] { "points" }, new Object[] { points }, e);
            }
        }
        return successCount;
    }

    /**
     * 创建计算点
     * 
     * @param point
     *            需创建测点信息
     * @return 创建结果
     * @throws DataAsException
     *             自定义异常
     */
    public boolean createCalcPoint(RTPoint point) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(point.getTagName()))
        {
            return result;
        }
        if (MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim())) != null)
        {
            // System.err.println(GoldenUtils.getTableTagName(tableName, point.getTagName()) + "测点已创建！");
            return result;
        }
        try
        {
            result = createClacPointCommon(point, MappingManagement.getTableMap().get(tableName));
            if (!result)
            {
                // System.err.println(point.getTagName() + " 创建计算测点失败！其方程式为：" + point.getEquation());
            }
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 创建计算测点失败！其方程式为：" + point.getEquation());
            DataAsExpSet.throwExpByResCode("PointOperation_createCalcPoint_1", new String[] { "point" }, new Object[] { point }, e);
        }
        return result;
    }

    /**
     * 批量创建计算点
     * 
     * @param points
     *            需创建测点信息
     * @return 创建成功个数
     * @throws DataAsException
     *             自定义异常
     */
    public int createCalcPoints(List<RTPoint> points) throws DataAsException
    {
        int successCount = 0;
        for (RTPoint point : points)
        {
            try
            {
                if (MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim())) != null)
                {
                    continue;
                }
                boolean result = createClacPointCommon(point, MappingManagement.getTableMap().get(tableName));
                if (!result)
                {
                    // System.err.println(point.getTagName() + " 创建计算测点失败！其方程式为：" + point.getEquation());
                }
                else
                {
                    successCount++;
                }
            }
            catch (Exception e)
            {
                // System.err.println(point.getTagName() + " 创建计算测点失败！其方程式为：" + point.getEquation());
                DataAsExpSet.throwExpByResCode("PointOperation_createCalcPoints_1", new String[] { "points" }, new Object[] { points }, e);
            }
        }

        return successCount;
    }

    /**
     * 测点创建通用
     * 
     * @param point
     *            需创建测点信息
     * @param tableId
     *            表id
     * @return 是否创建成功
     * @throws DataAsException
     *             自定义异常
     */
    private boolean createPointCommon(RTPoint point, int tableId) throws DataAsException
    {

        FullPoint fullPoint = new FullPoint();
        BasePoint basePoint = new BasePoint();
        basePoint.setTable(tableId);
        basePoint.setTag(point.getTagName().trim());
        if (!StringUtils.isBlank(point.getDesc()))
        {
            basePoint.setDesc(point.getDesc());
        }
        basePoint.setCompress(true);
        // 0: RTDB_BASE 1: RTDB_SCAN 2: RTDB_CALC 3: GOLDEN_ALARM
        basePoint.setClassof(RTDBClass.RTDB_BASE);
        basePoint.setArchive(true);
        basePoint.setMicrosecond(true);
        // 转换数值类型
        basePoint.setType(GoldenUtils.translateTypeToValueType(point.getDataType()));
        // 小数需要设置数值位数
        if (RTDataType.RT_DOUBLE == point.getDataType() || RTDataType.RT_FLOAT == point.getDataType())
        {
            basePoint.setDigits((short) 3);
        }

        fullPoint.setBasePoint(basePoint);

        boolean result = false;
        try
        {
            // 创建标签点并获得标签点id
            int pointId = base.insertPointReturnId(fullPoint);
            if (pointId != 0)
            {
                // 将标签点添加至内存
                point.setId(pointId);
                map.add(GoldenUtils.getTableTagName(tableName, point.getTagName().trim()), point);
                result = true;
            }
            // result = base.insertPoint(fullPoint);
            // if (result)
            // {
            // map.add(GoldenUtils.getTableTagName(tableName, point.getTagName().trim()), point);
            // }
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 创建测点失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_createPointCommon_1", new String[] { "point", "tableId" }, new Object[] { point, tableId }, e);
        }
        return result;
    }

    /**
     * 计算点创建通用
     * 
     * @param point
     *            需创建测点信息
     * @param tableId
     *            表id
     * @return 创建是否成功
     * @throws DataAsException
     *             自定义异常
     */
    private boolean createClacPointCommon(RTPoint point, int tableId) throws DataAsException
    {
        FullPoint fullPoint = new FullPoint();
        BasePoint basePoint = new BasePoint();
        basePoint.setTable(tableId);
        basePoint.setTag(point.getTagName().trim());
        if (!StringUtils.isBlank(point.getDesc()))
        {
            basePoint.setDesc(point.getDesc());
        }
        basePoint.setCompress(true);
        // 0: RTDB_BASE 1: RTDB_SCAN 2: RTDB_CALC 3: GOLDEN_ALARM
        basePoint.setClassof(RTDBClass.RTDB_CALC);
        basePoint.setArchive(true);
        // 表示毫秒点
        basePoint.setMicrosecond(true);
        basePoint.setType(GoldenUtils.translateTypeToValueType(point.getDataType()));
        // 小数需要设置数值位数
        if (RTDataType.RT_DOUBLE == point.getDataType() || RTDataType.RT_FLOAT == point.getDataType())
        {
            basePoint.setDigits((short) 3);
        }
        fullPoint.setBasePoint(basePoint);

        CalcPoint calcPoint = new CalcPoint();
        calcPoint.setEquation(point.getEquation());
        // Trigger=1表示事件触发，Trigger=2表示周期触发，Trigger=0表示无触发
        if (RTDBTrigger.parse(point.getTrigger()) != null)
        {
            calcPoint.setTrigger(RTDBTrigger.parse(point.getTrigger()));
        }
        // 0:RTDB_CALC_TIME 1:RTDB_LATEST_TIME 2:RTDB_EARLIEST_TIME
        if (RTDBTimeCopy.parse(Integer.valueOf(point.getTimecopy())) != null)
        {
            calcPoint.setTimecopy(RTDBTimeCopy.parse(point.getTimecopy()));
        }
        // 当触发机制为定时触发时设置其计算周期
        if (point.getTrigger() == 3)
        {
            calcPoint.setPeriod(point.getPeriod());
        }
        fullPoint.setCalcPoint(calcPoint);

        boolean result = false;
        try
        {
            result = base.insertPoint(fullPoint);

            // 将点增加至内存
            map.add(GoldenUtils.getTableTagName(tableName, point.getTagName().trim()), point);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_createClacPointCommon_1", new String[] { "point", "tableId" }, new Object[] { point, tableId }, e);
        }
        return result;
    }

    /************************************** 更新测点 **************************************/
    /**
     * 设置计算点是否触发
     * 
     * @param point
     *            计算点实体类
     * @return 更新单个测点属性是否成功
     * @throws DataAsException
     *             自定义异常
     */
    public boolean setCalcPointTrigger(RTPoint point) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(point.getTagName()))
        {
            return result;
        }
        RTPoint oldPoint = MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim()));
        if (oldPoint == null || oldPoint.getId() <= 0)
        {
            // System.err.println(point.getTagName() + " 内存中没有此测点信息！");
            // DataAsExpSet.throwExpByResCode("PointOperation_setCalcPointTrigger_1", new String[] { "point" }, new Object[] { point }, null);
            return result;
        }
        // 获取完整测点信息
        FullPoint fullPoint = new FullPoint();
        try
        {
            fullPoint = base.getPointsProperties(new int[] { oldPoint.getId() }).getList().get(0);
            if (fullPoint == null)
            {
                return result;
            }
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 查询测点信息失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_setCalcPointTrigger_2", new String[] { "point" }, new Object[] { point }, e);
        }

        BasePoint basePoint = fullPoint.getBasePoint();
        basePoint.setClassof(RTDBClass.parse(2));
        CalcPoint calcPoint = fullPoint.getCalcPoint();
        if (calcPoint != null)
        {
            // Trigger=1表示事件触发，Trigger=2表示周期触发，Trigger=0表示无触发
            if (RTDBTrigger.parse(point.getTrigger()) != null)
            {
                calcPoint.setTrigger(RTDBTrigger.parse(point.getTrigger()));
            }
            // 0:RTDB_CALC_TIME 1:RTDB_LATEST_TIME 2:RTDB_EARLIEST_TIME
            if (RTDBTimeCopy.parse(Integer.valueOf(point.getTimecopy())) != null)
            {
                calcPoint.setTimecopy(RTDBTimeCopy.parse(point.getTimecopy()));
            }
            // 当触发机制为定时触发时设置其计算周期
            if (point.getTrigger() == 3)
            {
                calcPoint.setPeriod(point.getPeriod());
            }
        }
        else
        {
            return result;
        }
        // 更新单个测点属性
        try
        {
            result = base.updatePointProperty(fullPoint);
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 设置计算点是否触发失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_setCalcPointTrigger_3", new String[] { "point" }, new Object[] { point }, e);
        }
        return result;
    }

    /**
     * 更新基本测点名称
     * 
     * @param oldName
     *            原测点名称
     * @param newName
     *            新测点名称
     * @return 更新结果
     * @throws DataAsException
     *             自定义异常
     */
    public boolean updateBasePointTagName(String oldName, String newName) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(oldName) || StringUtils.isBlank(newName))
        {
            return result;
        }

        // 判断内存中是否有此测点
        RTPoint oldPoint = MappingManagement.getValue(tableName + "." + oldName);
        if (oldPoint == null)
        {
            // System.err.println(oldName + " 内存中没有此测点信息！");
            // DataAsExpSet.throwExpByResCode("PointOperation_updatePointTagName_1", new String[] { "oldName", "newName" }, new Object[] { oldName, newName }, null);
            return result;
        }
        MinPoint minPoint = new MinPoint();
        try
        {
            minPoint = base.getPointsProperties(new String[] { GoldenUtils.getTableTagName(tableName, oldName) }).getList().get(0);
            if (minPoint == null)
            {
                return result;
            }
        }
        catch (Exception e)
        {
            // System.err.println(oldName + " 查询测点信息失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_updatePointTagName_2", new String[] { "oldName", "newName" }, new Object[] { oldName, newName }, e);
        }

        try
        {
            FullPoint fullPoint = base.getPointsProperties(new int[] { minPoint.getId() }).getList().get(0);
            BasePoint basePoint = fullPoint.getBasePoint();
            basePoint.setTag(newName);
            result = base.updatePointProperty(fullPoint);

            map.subscribeforPoint(new int[] { basePoint.getId() }, TagChangeReason.TAG_UPDATED);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_updatePointTagName_3", new String[] { "oldName", "newName" }, new Object[] { oldName, newName }, e);
        }
        return result;
    }

    /**
     * 修改计算点属性
     * 
     * @param point
     *            需修改的测点信息
     * @return 修改是否成功
     * @throws DataAsException
     *             自定义异常
     */
    public boolean updateCalcPoint(RTPoint point) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(point.getTagName()))
        {
            return result;
        }
        if (point.getId() == 0)
        {

        }
        // 判断内存中是否有此测点
        RTPoint oldPoint = MappingManagement.getValue(GoldenUtils.getTableTagName(tableName, point.getTagName().trim()));
        if (oldPoint == null)
        {
            // System.err.println(point.getTagName() + " 内存中没有此测点信息！");
            // DataAsExpSet.throwExpByResCode("PointOperation_updateCalcPoint_1", new String[] { "point" }, new Object[] { point }, null);
            return result;
        }
        FullPoint fullPoint = new FullPoint();
        try
        {
            MinPoint minPoint = base.getPointsProperties(new String[] { GoldenUtils.getTableTagName(tableName, point.getTagName().trim()) }).getList().get(0);
            if (minPoint == null)
            {
                return result;
            }
            fullPoint = base.getPointsProperties(new int[] { minPoint.getId() }).getList().get(0);
        }
        catch (Exception e)
        {
            // System.err.println(point.getTagName() + " 查询测点信息失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_updateCalcPoint_2", new String[] { "point" }, new Object[] { point }, e);
        }

        if (fullPoint == null)
        {
            return result;
        }

        try
        {
            BasePoint basePoint = fullPoint.getBasePoint();
            basePoint.setTag(point.getTagName().trim());
            if (!StringUtils.isBlank(point.getDesc()))
            {
                basePoint.setDesc(point.getDesc());
            }
            basePoint.setClassof(RTDBClass.RTDB_CALC);
            if (GoldenUtils.translateTypeToValueType(point.getDataType()) != null)
            {
                basePoint.setType(GoldenUtils.translateTypeToValueType(point.getDataType()));
            }
            CalcPoint calcPoint = fullPoint.getCalcPoint();
            calcPoint.setEquation(point.getEquation());
            if (RTDBTrigger.parse(point.getTrigger()) != null)
            {
                calcPoint.setTrigger(RTDBTrigger.parse(point.getTrigger()));
            }
            result = base.updatePointProperty(fullPoint);
            map.subscribeforPoint(new int[] { basePoint.getId() }, TagChangeReason.TAG_UPDATED);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_updateCalcPoint_3", new String[] { "point" }, new Object[] { point }, e);
        }
        return result;
    }

    /************************************** 获取信息 ***************************************/
    /**
     * 根据测点名称获取测点属性集合
     * 
     * @param tagNames
     *            测点名称数组
     * @return 测点信息集合
     * @throws DataAsException
     *             自定义异常
     */
    public List<RTPoint> getPointsByTagNames(String[] tagNames) throws DataAsException
    {
        List<String> tableTagNames = GoldenUtils.getTableTagNames(tableName, tagNames);
        // List<MinPoint> minPoints = getMinPointsByNames(tableTagNames);
        // return GoldenUtils.transLateToRtPoint(minPoints);
        List<RTPoint> points = new ArrayList<RTPoint>();
        for (String tableTagName : tableTagNames)
        {
            if (MappingManagement.getContainter().containsKey(tableTagName))
            {
                points.add(MappingManagement.getContainter().get(tableTagName));
            }
            else
            {
                DataAsExpSet.throwExpByResCode("PointOperation_getPointsByTagNames_1", new String[] { "tableTagName" }, new Object[] { tableTagName }, new Exception("TagName does not exist."));
            }
        }
        return points;
    }

    /**
     * 根据测点名称获取测点属性Map
     * 
     * @param tagNames
     *            测点名称数组
     * @return 测点信息Map
     * @throws DataAsException
     *             自定义异常
     */
    public Map<String, RTPoint> getPointMapByTagNames(String[] tagNames) throws DataAsException
    {
        List<String> tableTagNames = GoldenUtils.getTableTagNames(tableName, tagNames);
        // List<MinPoint> minPoints = getMinPointsByNames(tableTagNames);
        // return GoldenUtils.transLateToRtPointMap(minPoints);
        Map<String, RTPoint> pointsMap = new HashMap<String, RTPoint>();
        for (String tableTagName : tableTagNames)
        {
            pointsMap.put(tableTagName, MappingManagement.getContainter().get(tableTagName));
        }
        return pointsMap;
    }

    /**
     * 根据测点全名称获取最小属性
     * 
     * @param tableTagNames
     *            测点全名称集合
     * @return 最小属性测点集合
     * @throws DataAsException
     *             自定义异常
     */
    public List<MinPoint> getMinPointsByNames(List<String> tableTagNames) throws DataAsException
    {
        if (tableTagNames == null || tableTagNames.size() <= 0)
        {
            return null;
        }
        List<MinPoint> minPoints = null;
        try
        {
            String[] tableTagNameArray = new String[tableTagNames.size()];
            minPoints = base.findPoints(tableTagNames.toArray(tableTagNameArray));
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_getMinPointsByNames_1", new String[] { "tableTagNames" }, new Object[] { tableTagNames }, e);
        }
        return minPoints;
    }

    /**
     * 根据测点名称进行模糊搜索
     * 
     * @param tag
     *            测点名称
     * @return 测点信息集合
     * @throws DataAsException
     *             自定义异常
     */
    public List<RTPoint> search(String tag) throws DataAsException
    {
        List<Integer> ids = searchByTag(tag);

        return searchByIds(ids);
    }

    /**
     * 根据测点名称获取测点id
     * 
     * @param tagName
     *            模糊匹配测点名称
     * @return 测点id集合
     * @throws DataAsException
     *             自定义异常
     */
    private List<Integer> searchByTag(String tagName) throws DataAsException
    {
        if (StringUtils.isBlank(tagName))
        {
            return null;
        }
        // 初始化搜索条件
        SearchCondition condition = new SearchCondition();
        condition.setTagmask(tagName);
        List<Integer> idsAll = new ArrayList<Integer>();
        int i = 0;
        while (true)
        {
            try
            {
                List<Integer> idArrayTemp = base.searchInBatches(i, condition, GoldenGlobalData.getEverycount(), DataSort.SORT_BY_TAG).getList();
                List<Integer> ids = new ArrayList<Integer>();
                if (idArrayTemp.isEmpty() && i == 0)
                {
                    return idsAll;
                }
                // 过滤空项
                for (int id : idArrayTemp)
                {
                    if (id != 0)
                    {
                        ids.add(id);
                    }
                }
                if (ids.size() < GoldenGlobalData.getEverycount())
                {
                    idsAll.addAll(ids);
                    break;
                }
                else
                {
                    i = i + ids.size();
                    idsAll.addAll(ids);
                }
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("PointOperation_searchByTag_1", new String[] { "tagName" }, new Object[] { tagName }, e);
            }
        }

        return idsAll;

    }

    /**
     * 根据测点id集合获取测点属性rtpoint
     * 
     * @param ids
     *            id集合
     * @return 测点属性rtpoint集合
     * @throws DataAsException
     *             自定义异常
     */
    private List<RTPoint> searchByIds(List<Integer> ids) throws DataAsException
    {
        if (ids == null || ids.size() <= 0)
        {
            return null;
        }
        int[] idArray = new int[ids.size()];

        for (int i = 0; i < ids.size(); i++)
        {
            idArray[i] = ids.get(i);
        }
        List<RTPoint> pointList = new ArrayList<RTPoint>();
        try
        {
            List<FullPoint> fullPoints = base.getPointsProperties(idArray).getList();
            for (FullPoint fullPoint : fullPoints)
            {
                BasePoint basePoint = fullPoint.getBasePoint();
                String[] temp = basePoint.getTable_dot_tag().split("\\.");
                RTPoint point = new RTPoint();
                point.setId(basePoint.getId());
                point.setTableName(temp[0]);
                point.setTagName(basePoint.getTag());
                point.setTableTagName(basePoint.getTable_dot_tag());
                point.setDataType(GoldenUtils.translateTypeToRtType(basePoint.getType()));
                pointList.add(point);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("PointOperation_searchByIds_1", new String[] { "ids" }, new Object[] { ids }, e);
        }
        return pointList;
    }

    /************************************** 删除测点 **************************************/
    /**
     * 根据测点全名称，移除单个测点
     * 
     * @param tableTagName
     *            测点名称
     * @return 删除是否成功
     * @throws DataAsException
     *             自定义异常
     */
    public boolean removePoint(String tableTagName) throws DataAsException
    {
        boolean result = false;
        if (StringUtils.isBlank(tableTagName))
        {
            // DataAsExpSet.throwExpByResCode("PointOperation_removePoint_1", new String[] { "tableTagName" }, new Object[] { tableTagName }, null);
            return result;
        }
        RTPoint point = MappingManagement.getValue(tableTagName.trim());
        if (point == null)
        {
            return result;
        }
        try
        {
            result = base.removePointByName(tableTagName.trim());
            if (!result)
            {
                // System.err.println(tableTagName + " 删除测点失败！");
            }
            else
            {
                map.subscribeforPoint(new int[] { point.getId() }, TagChangeReason.TAG_REMOVED);
            }
        }
        catch (Exception e)
        {
            // System.err.println(tableTagName + " 删除测点失败！");
            DataAsExpSet.throwExpByResCode("PointOperation_removePoint_2", new String[] { "tableTagName" }, new Object[] { tableTagName }, e);
        }
        return result;
    }

    /**
     * 根据测点名称批量移除测点
     * 
     * @param tableTagNames
     *            测点全名称数组
     * @return 删除成功个数
     * @throws DataAsException
     *             自定义异常
     */
    public int removePoints(String[] tableTagNames) throws DataAsException
    {
        int successCount = 0;
        for (String tableTagName : tableTagNames)
        {
            boolean result = removePoint(tableTagName.trim());
            if (result)
            {
                successCount++;
            }
        }
        return successCount;
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
