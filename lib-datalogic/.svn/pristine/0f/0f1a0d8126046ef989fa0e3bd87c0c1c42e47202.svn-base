package com.goldwind.datalogic.business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import com.goldwind.dataaccess.rtdb.model.DataEntity;

/**
 * 实时数据实体计算帮助类
 * 
 * @author 冯春源
 *
 */
public class ArrgDataEntity
{
    /**
     * 取得传入的实时数据实体的测点值
     *
     * @param data
     *            实时数据实体
     * @return 实时数据实体的测点值, 如果传入参数异常(eg:参数为空), 返回null
     */
    public static BigDecimal getDataEntityValue(DataEntity data)
    {
        if (data == null)
        {
            return null;
        }

        return new BigDecimal(String.valueOf(data.getValue()));
    }

    /**
     * 取得传入的实时数据实体测点值的平均值
     *
     * @param datas
     *            实时数据实体
     * @return 实时数据实体测点值的平均值, 如果传入参数异常(eg:参数为空), 返回null
     */
    public static BigDecimal getDataEntityAvg(Map<String, DataEntity> datas)
    {
        // iec量总和
        BigDecimal sum = new BigDecimal("0");

        if (datas == null || datas.isEmpty())
        {
            return null;
        }
        for (Map.Entry<String, DataEntity> entry: datas.entrySet())
        {
            BigDecimal newVal = new BigDecimal(String.valueOf(entry.getValue().getValue()));
            sum = sum.add(newVal);
        }

        return sum.divide(new BigDecimal(datas.size()), 4, RoundingMode.HALF_UP); // 设置精度为4位小数;
    }

    /**
     * 取得传入的实时数据实体测点值的合计值
     *
     * @param datas
     *            实时数据实体
     * @return 实时数据实体测点值的合计值, 如果传入参数异常(eg:参数为空), 返回null
     */
    public static BigDecimal getDataEntitySum(Map<String, DataEntity> datas)
    {
        // iec量总和
        BigDecimal sum = new BigDecimal("0");

        if (datas == null || datas.isEmpty())
        {
            return null;
        }
        for (Map.Entry<String, DataEntity> entry: datas.entrySet())
        {
            BigDecimal newVal = new BigDecimal(String.valueOf(entry.getValue().getValue()));
            sum = sum.add(newVal);
        }

        return sum;
    }

    /**
     * 取得传入的实时数据实体测点值的最大值
     *
     * @param datas
     *            实时数据实体
     * @return 实时数据实体测点值的最大值, 如果传入参数异常(eg:参数为空), 返回null
     */
    public static BigDecimal getDataEntityMax(Map<String, DataEntity> datas)
    {
        // iec量最大值
        // BigDecimal max = new BigDecimal(0);
        BigDecimal max = null;

        if (datas == null || datas.isEmpty())
        {
            return null;
        }
        for (Map.Entry<String, DataEntity> entry: datas.entrySet())
        {
            BigDecimal value = new BigDecimal(String.valueOf(entry.getValue().getValue()));
            if (null == max)
            { // 第一个有效的iec量
                max = value;
            }
            else
            {
                if (value.compareTo(max) > 0)
                {
                    max = value;
                }
            }
        }

        return max;
    }

    /**
     * 取得传入的实时数据实体测点值的最小值
     *
     * @param datas
     *            实时数据实体
     * @return 实时数据实体测点值的最小值, 如果传入参数异常(eg:参数为空), 返回null
     */
    public static BigDecimal getDataEntityMin(Map<String, DataEntity> datas)
    {
        // iec量最小值
        BigDecimal min = null;

        if (datas == null || datas.isEmpty())
        {
            return null;
        }
        for (Map.Entry<String, DataEntity> entry: datas.entrySet())
        {
            BigDecimal value = new BigDecimal(String.valueOf(entry.getValue().getValue()));
            if (null == min)
            { // 第一个有效的iec量
                min = value;
            }
            else
            {
                if (value.compareTo(min) < 0)
                {
                    min = value;
                }
            }
        }

        return min;
    }
}
