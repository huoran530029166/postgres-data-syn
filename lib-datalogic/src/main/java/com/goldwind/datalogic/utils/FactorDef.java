package com.goldwind.datalogic.utils;

/**
 * 公式基础定义
 * 
 * @author 冯春源
 *
 */
public class FactorDef
{
    /**
     * 计算因子类型, 0 单设备, 1 多设备
     * 
     * @author 冯春源
     *
     */
    public enum FactorType
    {
        /**
         * 设备计算（格式说明：{0_设备ID1,设备ID2:设备类型:IEC路径:聚合方式}，需要兼容目前的设备ID:IEC路径，如果是单设备，聚合方式可以为空。）
         */
        SingleDevice(0),

        /**
         * 组织计算（格式说明：{1_组织编号:设备类型:IEC路径:聚合方式}，其中组织编号为0，表示系统，设备类型为-1，表示所有设备类型，聚合方式有：SUM、AVG等。）
         * 
         */
        MultipleDevices(1),
        /**
         * iec量计算 （格式说明：@WTUR.Other.Wn.I16.StopModeWord）
         */
        IECCOMPUTE(2);

        /**
         * 参数
         */
        private int result;

        FactorType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 聚合方式
     * 
     * @author 冯春源
     *
     */
    public enum ArrgType
    {
        /**
         * 求和
         */
        SUM("SUM"),

        /**
         * 求平均
         */
        AVG("AVG"),

        /**
         * 最大值
         */
        max("max"),

        /**
         * 最小值
         */
        min("min");

        /**
         * 参数
         */
        private String result;

        ArrgType(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }
}
