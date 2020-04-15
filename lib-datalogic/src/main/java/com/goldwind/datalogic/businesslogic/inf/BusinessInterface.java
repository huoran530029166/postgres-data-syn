package com.goldwind.datalogic.businesslogic.inf;

import java.util.Date;
import java.util.List;

import com.goldwind.datalogic.businesslogic.model.EbaExampLoss;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;

/**
 * 业务逻辑基础接口
 * 
 * @author 谭璟
 *
 */
public interface BusinessInterface
{
    /**
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param list
     *            设备list
     * @param listhandle
     *            类型容器
     * @param dbOper
     *            数据库操作对象
     * @param together
     *            聚合类型
     * @return 返回对象容器
     */
    List<Losselec> getDeviceLossElec(Date starttime, Date endtime, List<String> list, List<Integer> listhandle, String type, Together together);

    /**
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param list
     *            设备list
     * @param listhandle
     *            类型容器
     * @param dbOper
     *            数据库操作对象
     * @return 返回对象容器
     */
    List<Losstime> getDeviceLossDel(Date starttime, Date endtime, List<String> list, List<Integer> listhandle, String type);

    /**
     * EBA 接口
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param list
     *            设备类型
     * @param listhandle
     *            运行状态字类型
     * @param type
     *            运行状态类型
     * @return
     */
    List<EbaExampLoss> getDeviceLossEba(Date starttime, Date endtime, List<String> list, List<Integer> listhandle, String type);
}
