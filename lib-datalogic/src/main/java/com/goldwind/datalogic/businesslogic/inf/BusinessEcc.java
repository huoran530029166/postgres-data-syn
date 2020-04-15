package com.goldwind.datalogic.businesslogic.inf;

import java.util.List;
import java.util.Map;

/**
 * ECC相关业务流程
 * 
 * @author 谭璟
 *
 */
public abstract class BusinessEcc implements BusinessEccInterface
{

    /**
     * 标准空气密度
     */
    protected final double density = 1.293;

    /**
     * 标准物理大气压
     */
    protected final double atmos = 1013.25;

    /**
     * 大气压计算数值
     */
    protected final double atomtemp = 101.3;

    /**
     * 得到风场下对应风机的择算后风速和功率
     * 
     * @param wfid
     *            风场
     * @param wtid
     *            风机
     * @param rectime
     *            时间
     * @param hash
     *            录入功率曲线
     * @param li
     *            测风塔集合
     * @return 返回择算好的 风速和录入功率先去值
     */
    public abstract Map<Double, Double> getDeviceEcc(String wfid, String wtid, String rectime, Map<Double, Double> hash, List<String> li);// NOSONAR
}
