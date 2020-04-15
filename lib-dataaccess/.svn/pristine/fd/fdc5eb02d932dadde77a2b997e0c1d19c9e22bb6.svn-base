package com.goldwind.dataaccess.rtdb.model;

/**
 * 历史数据取值模式
 * 
 * @author shenlf
 *
 */
public enum RTHistorianMode
{
    /**
     * 寻找下一个最近的数据
     */
    RT_NEXT,
    /**
     * 寻找上一个最近的数据
     */
    RT_PREVIOUS,

    /**
     * 取指定时间的数据，如果没有则返回错误 GoE_DATA_NOT_FOUND
     */
    RT_EXACT,

    /**
     * 取指定时间的内插值数据
     */
    RT_INTER,

    /**
     * 趋势采样(指定时间区间、采样点数或采样周期返回每个采样间隔的最大和最小原始值，用于画趋势曲线)
     */
    RT_TREND
}
