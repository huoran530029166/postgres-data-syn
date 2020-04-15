package com.goldwind.datalogic.businesslogic.inf;

import java.util.List;
import java.util.Map;

/**
 * 天润业务接口
 * 
 * @author 谭璟
 */
public interface BusinessEccInterface
{
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
    Map<Double, Double> getDeviceEcc(String wfid, String wtid, String rectime, Map<Double, Double> hash, List<String> li);
}
