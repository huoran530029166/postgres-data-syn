package com.goldwind.dataaccess.rtdb.golden.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.rtdb.model.RTDataType;
import com.goldwind.dataaccess.rtdb.model.RTHistorianMode;
import com.goldwind.dataaccess.rtdb.model.RTPoint;
import com.rtdb.api.enums.RtdbType;
import com.rtdb.enums.RtdbHisMode;
import com.rtdb.enums.ValueType;
import com.rtdb.model.BasePoint;
import com.rtdb.model.MinPoint;

/**
 * 庚顿工具类
 * 
 * @author shenlf
 *
 */
public class GoldenUtils
{
    /**
     * 根据测点名称获取测点全名称
     * 
     * @param tableName
     *            表名
     * @param tagName
     *            测点名称
     * @return 测点全名称
     */
    public static String getTableTagName(String tableName, String tagName)
    {
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(tagName))
        {
            return null;
        }
        return tableName.concat(".").concat(tagName.trim());
    }

    /**
     * 根据测点名称获取测点全名称
     * 
     * @param tableName
     *            表名
     * @param tagNames
     *            测点名称数组
     * @return 测点全名称集合
     */
    public static List<String> getTableTagNames(String tableName, String[] tagNames)
    {
        if (StringUtils.isBlank(tableName) || tagNames == null || tagNames.length <= 0)
        {
            return null;
        }
        List<String> tagNameList = new ArrayList<String>();
        for (String tagName : tagNames)
        {
            tagName = getTableTagName(tableName, tagName.trim());
            tagNameList.add(tagName);
        }
        return tagNameList;
    }

    /************************************** 转换类型 ***************************************/
    /**
     * 将数据库的类型返回为ValueType
     * 
     * @param type
     *            类型
     * @return ValueType
     */
    public static ValueType translateTypeToValueType(RTDataType type)
    {
        ValueType valueType = null;
        switch (type)
        {
            case RT_BOOL:
                valueType = ValueType.RTDB_BOOL;
                break;
            case RT_INT16:
                valueType = ValueType.RTDB_INT16;
                break;
            case RT_INT32:
                valueType = ValueType.RTDB_INT32;
                break;
            case RT_INT64:
                valueType = ValueType.RTDB_INT64;
                break;
            case RT_INT8:
                valueType = ValueType.RTDB_INT8;
                break;
            case RT_FLOAT:
                valueType = ValueType.RTDB_REAL32;
                break;
            case RT_DOUBLE:
                valueType = ValueType.RTDB_REAL64;
                break;
            case RT_STRING:
                valueType = ValueType.RTDB_STRING;
                break;
            case RT_UINT16:
                valueType = ValueType.RTDB_UINT16;
                break;
            case RT_UINT32:
                valueType = ValueType.RTDB_UINT32;
                break;
            case RT_UINT8:
                valueType = ValueType.RTDB_UINT8;
                break;
            case RT_DATETIME:
                valueType = ValueType.RTDB_DateTime;
                break;
            default:
                break;
        }
        return valueType;
    }

    /**
     * 转化类型为庚顿数据值类型
     * 
     * @param point
     *            标签点信息
     * @param basePoint
     *            基本标签点
     */
    public static void translateRtTypeToBasePoint(RTPoint point, BasePoint basePoint)
    {
        basePoint.setType(translateTypeToValueType(point.getDataType()));
    }

    /**
     * 将minpoint集合 转换为rtpoint集合
     * 
     * @param minPoints
     *            需要转换的最小属性标签集体
     * @return 转换后的RTPoint集合
     */
    public static List<RTPoint> transLateToRtPoint(List<MinPoint> minPoints)
    {
        if (minPoints == null || minPoints.size() <= 0)
        {
            return null;
        }
        List<RTPoint> points = new ArrayList<RTPoint>();
        for (MinPoint minPoint : minPoints)
        {
            String tableTagName = minPoint.getTagName().trim();
            String[] temp = tableTagName.split("\\.");
            RTPoint point = new RTPoint();
            point.setId(minPoint.getId());
            point.setDataType(translateTypeToRtType(minPoint.getValueType()));
            point.setTableTagName(tableTagName);
            point.setTagName(temp[1]);
            point.setTableName(temp[0]);
            point.setDesc(minPoint.getDesc());
            points.add(point);
        }
        return points;
    }

    /**
     * 将minpoint集合 转换为rtpoint集合
     * 
     * @param minPoints
     *            需要转换的最小属性标签集体
     * @return 转换后的RTPoint集合
     */
    public static Map<String, RTPoint> transLateToRtPointMap(List<MinPoint> minPoints)
    {
        if (minPoints == null || minPoints.isEmpty())
        {
            return null;
        }
        Map<String, RTPoint> pointMap = new HashMap<String, RTPoint>();
        for (MinPoint minPoint : minPoints)
        {
            String tableTagName = minPoint.getTagName().trim();
            String[] temp = tableTagName.split("\\.");
            RTPoint point = new RTPoint();
            point.setId(minPoint.getId());
            point.setDataType(translateTypeToRtType(minPoint.getValueType()));
            point.setTableTagName(tableTagName);
            point.setTagName(temp[1]);
            point.setTableName(temp[0]);
            point.setDesc(minPoint.getDesc());
            pointMap.put(tableTagName, point);
        }
        return pointMap;
    }

    /**
     * 将数据库的类型返回为RTDataType
     * 
     * @param type
     *            类型
     * @return 转换后的RTDataType
     */
    public static RTDataType translateTypeToRtType(ValueType type)
    {

        RTDataType rtDataType = null;
        switch (type)
        {
            case RTDB_BOOL:
                rtDataType = RTDataType.RT_BOOL;
                break;
            case RTDB_INT16:
                rtDataType = RTDataType.RT_INT16;
                break;
            case RTDB_INT32:
                rtDataType = RTDataType.RT_INT32;
                break;
            case RTDB_INT8:
                rtDataType = RTDataType.RT_INT8;
                break;
            case RTDB_INT64:
                rtDataType = RTDataType.RT_INT64;
                break;
            case RTDB_REAL32:
                rtDataType = RTDataType.RT_FLOAT;
                break;
            case RTDB_REAL64:
                rtDataType = RTDataType.RT_DOUBLE;
                break;
            case RTDB_STRING:
                rtDataType = RTDataType.RT_STRING;
                break;
            case RTDB_UINT16:
                rtDataType = RTDataType.RT_UINT16;
                break;
            case RTDB_UINT32:
                rtDataType = RTDataType.RT_UINT32;
                break;
            case RTDB_UINT8:
                rtDataType = RTDataType.RT_UINT8;
                break;
            case RTDB_DateTime:
                rtDataType = RTDataType.RT_DATETIME;
                break;
            default:
                break;
        }
        return rtDataType;
    }

    /**
     * 将数据库的类型返回为RTDataType
     * 
     * @param type
     *            类型
     * @return 转换后的RTDataType
     */
    public static RTDataType translateDbTypeToRtType(RtdbType type)
    {

        RTDataType rtDataType = null;
        switch (type)
        {
            case RTDB_BOOL:
                rtDataType = RTDataType.RT_BOOL;
                break;
            case RTDB_INT16:
                rtDataType = RTDataType.RT_INT16;
                break;
            case RTDB_INT32:
                rtDataType = RTDataType.RT_INT32;
                break;
            case RTDB_INT8:
                rtDataType = RTDataType.RT_INT8;
                break;
            case RTDB_INT64:
                rtDataType = RTDataType.RT_INT64;
                break;
            case RTDB_REAL32:
                rtDataType = RTDataType.RT_FLOAT;
                break;
            case RTDB_REAL64:
                rtDataType = RTDataType.RT_DOUBLE;
                break;
            case RTDB_STRING:
                rtDataType = RTDataType.RT_STRING;
                break;
            case RTDB_UINT16:
                rtDataType = RTDataType.RT_UINT16;
                break;
            case RTDB_UINT32:
                rtDataType = RTDataType.RT_UINT32;
                break;
            case RTDB_UINT8:
                rtDataType = RTDataType.RT_UINT8;
                break;
            case RTDB_DateTime:
                rtDataType = RTDataType.RT_DATETIME;
                break;
            default:
                break;
        }
        return rtDataType;
    }

    /**
     * 转换历史取值模型
     * 
     * @param rtmode
     *            源历史取值模型
     * @return 转换后的历史数据模型
     */
    public static RtdbHisMode translateMode(RTHistorianMode rtmode)
    {
        RtdbHisMode mode = null;
        switch (rtmode)
        {
            case RT_EXACT:
                mode = RtdbHisMode.RTDB_EXACT;
                break;
            case RT_INTER:
                mode = RtdbHisMode.RTDB_INTER;
                break;
            case RT_PREVIOUS:
                mode = RtdbHisMode.RTDB_PREVIOUS;
                break;
            case RT_NEXT:
                mode = RtdbHisMode.RTDB_NEXT;
                break;
            default:
                System.err.println("没有对应的历史数据取值模式");
        }
        return mode;
    }
}
