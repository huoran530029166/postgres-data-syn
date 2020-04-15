package com.goldwind.dataaccess.rtdb.golden.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.rtdb.golden.common.GoldenUtils;
import com.goldwind.dataaccess.rtdb.golden.oper.GoldenServerPool;
import com.goldwind.dataaccess.rtdb.model.RTPoint;
import com.rtdb.api.enums.RtdbType;
import com.rtdb.enums.DataSort;
import com.rtdb.enums.TagChangeReason;
import com.rtdb.model.MinPoint;
import com.rtdb.model.SearchCondition;
import com.rtdb.model.Table;
import com.rtdb.service.impl.BaseImpl;
import com.rtdb.service.impl.ServerImpl;
import com.rtdb.service.inter.Base;

/**
 * 测点映射管理 key:测点名称(带表名)，value：Point类型（id,测点名称,测点类型）
 * 
 * @author shenlf
 *
 */
public class MappingManagement
{
    /**
     * 测点信息map
     */
    private static Map<String, RTPoint> CONTAINTER = new ConcurrentHashMap<String, RTPoint>();
    /**
     * 表信息map
     */
    private static Map<String, Integer> TABLEMAP = new ConcurrentHashMap<String, Integer>();
    /**
     * 定义锁对象
     */
    private static Lock LOCK = new ReentrantLock();

    /**
     * 添加测点信息至内存map中
     * 
     * @param tableTagName
     *            测点全名称
     * @param point
     *            测点信息
     * @throws DataAsException
     *             异常
     */
    public void add(String tableTagName, RTPoint point) throws DataAsException
    {
        LOCK.lock();
        try
        {
            if (StringUtils.isBlank(tableTagName))
            {
                System.err.println("测点名称为空");
            }
            if (point == null)
            {
                System.err.println("测点信息为空");
            }

            // RTPoint minPoint = getSingleMinPoint(point.getTableTagName());
            // point.setId(minPoint.getId());
            CONTAINTER.put(tableTagName, point);
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * 根据测点名称从内存中取得point
     * 
     * @param tableTagName
     *            测点全名称
     * @return 测点信息
     * @throws DataAsException
     *             自定义异常
     */
    public static RTPoint getValue(String tableTagName) throws DataAsException
    {
        if (StringUtils.isBlank(tableTagName))
        {
            return null;
        }
        if (CONTAINTER.get(tableTagName.trim()) != null)
        {
            return CONTAINTER.get(tableTagName.trim());
        }
        else
        {
            return resumeGetValueAndAddDicionary(tableTagName.trim());
        }
    }

    /**
     * 将新测点更新至内存中
     * 
     * @param tableTagName
     *            测点全名称
     * @return 测点信息
     * @throws DataAsException
     *             自定义异常
     */
    private static RTPoint resumeGetValueAndAddDicionary(String tableTagName) throws DataAsException
    {
        RTPoint point = getSingleMinPoint(tableTagName);
        if (point != null)
        {
            CONTAINTER.put(tableTagName, point);
        }
        return point;
    }

    /**
     * 批量加载测点至list
     * 
     * @param tableTagNames
     *            测点全名称集合
     * @return 测点信息集合
     * 
     */
    public List<RTPoint> getValueCollection(List<String> tableTagNames)
    {
        List<RTPoint> pointCollection = new ArrayList<RTPoint>();

        for (String tableTagName : tableTagNames)
        {
            if (CONTAINTER.containsKey(tableTagName.trim()))
            {
                pointCollection.add(CONTAINTER.get(tableTagName.trim()));
            }
        }
        return pointCollection;
    }

    /**
     * 建立连接，将库里的测点信息载入内存中
     * 
     * @param tableName
     *            表名
     * @throws DataAsException
     *             自定义异常
     */
    public static void loadMap(String tableName) throws DataAsException
    {
        // 判断数据库类型
        switch (GoldenServerPool.getPoolDbType()) {
            case Golden:
                // 若是Goden实时库，则建立连接，并将库里的测点信息载入内存中
                goldenLoadMap(tableName);
                break;
            case Bigdata:// 若是大数据（只查询），无须加载测点信息
            default:
                break;
        }
    }

    /**
     * 若是Goden实时库，则建立连接，并将库里的测点信息载入内存中
     * 
     * @param tbName
     *            指定的表名
     * @throws DataAsException
     *             自定义异常
     */
    private static void goldenLoadMap(String tbName) throws DataAsException
    {
        LOCK.lock();
        Base base = null;
        try
        {
            try
            {
                ServerImpl serverImpl = GoldenServerPool.getPool().getServerImpl();
                base = new BaseImpl(serverImpl);
                System.out.println("建立连接！");
            }
            catch (Exception e)
            {
                System.err.println("连接失败！");
                DataAsExpSet.throwExpByResCode("MappingManagement_loadMap_1", null, null, e);
            }
            int[] tableIds = base.getTableIds();
            for (int tableId : tableIds)
            {
                Table table = base.getTablePropertiesById(tableId);
                String tableName = table.getName();

                // 只加载指定表名的测点
                if (!tbName.equals(tableName))
                {
                    continue;
                }
                // 加载表名称——id键值对至内存map中
                TABLEMAP.put(tableName, tableId);

                // **************** 查找该表下的测点并封装至内存map中 ****************
                int tagSize = base.getTableSizeById(tableId);
                if (tagSize <= 0)
                {
                    continue;
                }
                SearchCondition condition = new SearchCondition();
                condition.setTablemask(tableName);
                int[] ids = base.search(condition, tagSize, DataSort.SORT_BY_TAG);
                if (ids == null || ids.length <= 0)
                {
                    continue;
                }
                String[] tableTagNames = base.getTableTagNames(ids);
                if (tableTagNames == null || tableTagNames.length <= 0)
                {
                    continue;
                }
                RtdbType[] types = base.getTypes(ids);
                for (int i = 0; i < ids.length; i++)
                {
                    // 过滤内存中已有的测点
                    if (CONTAINTER.containsKey(tableTagNames[i].trim()))
                    {
                        continue;
                    }

                    String[] temp = tableTagNames[i].trim().split("\\.");
                    RtdbType dbType = types[i];
                    RTPoint point = new RTPoint();
                    point.setId(ids[i]);
                    point.setTableName(tableName);
                    point.setTagName(temp[1]);
                    point.setTableTagName(tableTagNames[i]);
                    point.setDataType(GoldenUtils.translateDbTypeToRtType(dbType));
                    CONTAINTER.put(tableTagNames[i].trim(), point);
                }
                // List<MinPoint> minPoints = base.getPointsProperties(tagNames).getList();
                // for (MinPoint minPoint : minPoints)
                // {
                // String tableTagName = minPoint.getTagName();
                // RTPoint point = transMinPointToRTPoint(minPoint);
                // CONTAINTER.put(tableTagName, point);
                // }
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("MappingManagement_loadMap_2", null, null, e);
        }
        finally
        {
            try
            {
                // GoldenServerPool.getPool().releaseServerImpl(server);
                if (base != null)
                {
                    base.close();
                }
                System.out.println("释放连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_loadMap_3", null, null, e);
            }
            LOCK.unlock();
        }
    }

    /**
     * 订阅测点
     * 
     * @param ids
     *            测点id数组
     * @param reason
     *            数据库执行的操作
     * @throws DataAsException
     *             自定义异常
     */
    public void subscribeforPoint(int[] ids, TagChangeReason reason) throws DataAsException
    {
        LOCK.lock();
        try
        {
            if (isUpdate(ids, reason))
            {
                updateMapDic(ids[0], reason);
            }
        }
        finally
        {
            LOCK.unlock();
        }
    }

    /**
     * 检查测点是否具备更新条件
     * 
     * @param ids
     *            测点id数组
     * @param reason
     *            数据库执行的操作
     * @return 是否可更改
     */
    private boolean isUpdate(int[] ids, TagChangeReason reason)
    {
        // 测点被清空不用考虑
        if (TagChangeReason.TAG_PURGED == reason || TagChangeReason.TAB_UPDATED == reason || TagChangeReason.TAG_CREATED == reason || ids == null || ids.length <= 0 || CONTAINTER.isEmpty())
        {
            return false;
        }

        return true;
    }

    /**
     * 根据数据库操作来更新缓存映射
     * 
     * @param id
     *            测点id
     * @param reason
     *            数据库执行的操作
     * @throws DataAsException
     *             自定义异常
     */
    private void updateMapDic(int id, TagChangeReason reason) throws DataAsException
    {
        // 根据 reason 维护缓存
        switch (reason) {
            case TAG_REMOVED:
                // 测点被删除
                updateContainerDel(id);
                break;
            case TAG_UPDATED:
                // 测点属性修改
                updateContainerModify(id);
                break;
            default:
                break;
        }
    }

    /**
     * 数据库添加测点时 删除映射
     * 
     * @param id
     *            测点id
     */
    private void updateContainerDel(int id)
    {
        String key = null;
        for (String tableTagName : CONTAINTER.keySet())
        {
            RTPoint point = CONTAINTER.get(tableTagName);
            if (point.getId() == id)
            {
                key = tableTagName;
                break;
            }
        }
        if (StringUtils.isBlank(key))
        {
            return;
        }
        if (CONTAINTER.containsKey(key))
        {
            CONTAINTER.remove(key);
            System.out.println(key + "测点被移除");
        }

    }

    /**
     * 添加测点时 修改映射
     * 
     * @param id
     *            测点id
     * @throws DataAsException
     *             自定义异常
     */
    private void updateContainerModify(int id) throws DataAsException
    {
        MinPoint minPoint = getSingleMinPoint(id);
        if (minPoint == null)
        {
            return;
        }
        Base base = null;
        try
        {
            base = new BaseImpl(GoldenServerPool.getPool().getServerImpl());

            System.out.println("建立连接！");

            String tableTagName = base.getTableTagNames(new int[] { id })[0];
            RTPoint point = transMinPointToRTPoint(minPoint);
            try
            {
                CONTAINTER.remove(tableTagName.trim());
                CONTAINTER.put(tableTagName.trim(), point);
                System.out.println(" 测点id为： " + id + " 的测点被修改");
            }
            catch (Exception e)
            {
                System.err.println("修改映射出现异常" + e.getMessage());
                DataAsExpSet.throwExpByResCode("MappingManagement_updateContainerModify_1", new String[] { "id" }, new Object[] { id }, e);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("MappingManagement_updateContainerModify_2", new String[] { "id" }, new Object[] { id }, e);
        }
        finally
        {
            try
            {
                if (base != null)
                {
                    base.close();
                }
                System.out.println("释放连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_updateContainerModify_3", new String[] { "id" }, new Object[] { id }, e);
            }
        }
    }

    /**
     * 根据id获取测点的基本属性
     * 
     * @param id
     *            测点id
     * @return 测点的基本属性
     * @throws DataAsException
     *             自定义异常
     */
    private MinPoint getSingleMinPoint(int id) throws DataAsException
    {
        Base base = null;
        MinPoint point = null;
        try
        {
            try
            {
                base = new BaseImpl(GoldenServerPool.getPool().getServerImpl());
                System.out.println("建立连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_1", new String[] { "id" }, new Object[] { id }, e);
            }

            if (base != null)
            {
                String[] names = base.getTableTagNames(new int[] { id });
                if (names != null && names.length > 0)
                {
                    List<MinPoint> minPoints = base.findPoints(names);
                    if (minPoints == null || minPoints.isEmpty() || minPoints.get(0) == null || minPoints.get(0).getValueType() == null)
                    {
                        return point;
                    }
                    point = minPoints.get(0);
                }
            }
        }
        catch (Exception exception)
        {
            DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_2", new String[] { "id" }, new Object[] { id }, exception);
        }
        finally
        {
            try
            {
                if (base != null)
                {
                    base.close();
                }
                System.out.println("释放连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_3", new String[] { "id" }, new Object[] { id }, e);
            }
        }
        return point;
    }

    /**
     * 根据tagName获取测点的基本属性
     * 
     * @param tableTagName
     *            测点全名称
     * @return 测点的基本属性
     * @throws DataAsException
     *             自定义异常
     */
    private static RTPoint getSingleMinPoint(String tableTagName) throws DataAsException
    {
        if (StringUtils.isBlank(tableTagName))
        {
            return null;
        }

        RTPoint point = null;
        Base base = null;
        try
        {
            try
            {
                base = new BaseImpl(GoldenServerPool.getPool().getServerImpl());
                System.out.println("建立连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_1", new String[] { "tableTagName" }, new Object[] { tableTagName }, e);
            }
            List<MinPoint> minPoints = null;
            try
            {
                minPoints = base.findPoints(new String[] { tableTagName });
            }
            catch (Exception e1)
            {
                // DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_2", new String[] { "tableTagName" }, new Object[] { tableTagName }, e1);
                System.err.println("未找到名称为" + tableTagName + "的测点！");
            }
            if (minPoints == null || minPoints.isEmpty() || minPoints.get(0) == null || minPoints.get(0).getValueType() == null)
            {
                return null;
            }
            point = transMinPointToRTPoint(minPoints.get(0));
        }
        finally
        {
            try
            {
                // GoldenServerPool.getPool().releaseServerImpl(server);
                if (base != null)
                {
                    base.close();
                }
                System.out.println("释放连接！");
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("MappingManagement_getSingleMinPoint_3", new String[] { "tableTagName" }, new Object[] { tableTagName }, e);
            }
        }
        return point;
    }

    /**
     * 将minPoint对象转换为RTPoint对象
     * 
     * @param minPoint
     *            需要转换的对象
     * @return point
     */
    private static RTPoint transMinPointToRTPoint(MinPoint minPoint)
    {
        String[] temp = minPoint.getTagName().trim().split("\\.");
        RTPoint point = new RTPoint();
        point.setId(minPoint.getId());
        point.setTableName(temp[0]);
        point.setTagName(temp[1]);
        point.setTableTagName(minPoint.getTagName().trim());
        point.setDesc(minPoint.getDesc());
        point.setDataType(GoldenUtils.translateTypeToRtType(minPoint.getValueType()));
        return point;
    }

    public static Map<String, RTPoint> getContainter()
    {
        return CONTAINTER;
    }

    public static void setContainter(Map<String, RTPoint> containter)
    {
        MappingManagement.CONTAINTER = containter;
    }

    public static Map<String, Integer> getTableMap()
    {
        return TABLEMAP;
    }

    public static void setTableMap(Map<String, Integer> tableMap)
    {
        MappingManagement.TABLEMAP = tableMap;
    }
}
