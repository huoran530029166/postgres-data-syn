package com.goldwind.datalogic.businesslogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.businesslogic.electricityloss.EbaElectricityLoss;
import com.goldwind.datalogic.businesslogic.electricityloss.InverterElectricityLoss;
import com.goldwind.datalogic.businesslogic.electricityloss.RunStateElectricityLoss;
import com.goldwind.datalogic.businesslogic.electricityloss.WindTurbineElectricityLoss;
import com.goldwind.datalogic.businesslogic.model.EbaExampLoss;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;
import com.goldwind.datalogic.utils.ControlProcessDef.TypeId;

/**
 * 业务逻辑函数入口
 * 
 * @author 谭璟
 *
 */
public class DeviceHandle 
{
    /**
     * 输出日志
     */
    protected static Log logger = Log.getLog(DeviceHandle.class);

    /**
     * 损失电量操作类
     * **/
    private WindTurbineElectricityLoss handleLosstype;
    
    /**
     * 运行状态字损失电量操作类 
     * **/
    private RunStateElectricityLoss handleRunstate;

    /**
     * 光伏损失电量操作类
     * **/
    private InverterElectricityLoss handleInverter;

    
    /**
     * eba损失电量操作类
     * **/
    private EbaElectricityLoss handleEba;

    /**
     * 带参数构造函数
     * 
     * @param dbOper
     */
    public DeviceHandle(DbOperBase dbOper)
    {
        Map<String, List<ExpDataObject>> map = DeviceHandleFunction.getDeviceEnergy(dbOper);
        handleLosstype = new WindTurbineElectricityLoss(dbOper, map);
        handleRunstate = new RunStateElectricityLoss(dbOper, map);
        handleInverter = new InverterElectricityLoss(dbOper, map);
        handleEba = new EbaElectricityLoss(dbOper, map);
    }   
    
    /**
     * 损失电量查询接口
     * @param starttime
     * @param endtime
     * @param wtidlist 风机id list
     * @param setIdList setid list
     * @param type  typeid大类
     * @param together 聚合类型：日聚合，小时聚合，明细
     * @return
     */
    public List<Losselec> getDeviceLossElec(Date starttime, Date endtime, List<String> wtidlist, List<Integer> setIdList, String type, Together together)
    {
        endtime=new Date(endtime.getTime()-1);
        return getOneDataLossElec(starttime, endtime, wtidlist, setIdList, type, together);
    }
    
    /**
     * 损失电量查询接口
     * @param starttime
     * @param endtime
     * @param wtidlist 风机id list
     * @param setIdList setid list
     * @param type  typeid大类
     * @param together 聚合类型：日聚合，小时聚合，明细
     * @return
     */
    public List<Losselec> getOneDataLossElec(Date starttime, Date endtime, List<String> wtidlist, List<Integer> setIdList, String type, Together together)
    {
        //风机损失电量
        if (type.equals(TypeId.losstype.getResult()))
        {
            return handleLosstype.getDeviceLossElecNew(starttime, endtime, wtidlist, setIdList, type, together);
        }
        //运行状态字损失电量 PBA
        if (type.equals(TypeId.runstate.getResult()))
        {
            return handleRunstate.getDeviceLossElecNew(starttime, endtime, wtidlist, setIdList, type, together);
        }
        //逆变器损失电量
        if (type.equals(TypeId.inverter.getResult()))
        {
            return handleInverter.getDeviceLossElecNew(starttime, endtime, wtidlist, setIdList, type, together);
        }
        return new ArrayList<>();
    }      


    /**
     * EBA 查询接口
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param deviceIdList
     *            设备集合
     * @param listhandle
     *            维护类型
     * @param type
     *            处理类型
     * @return 返回对象集合
     */
    public List<EbaExampLoss> getDeviceLossEba(Date starttime, Date endtime, List<String> deviceIdList, List<Integer> listhandle, String type)
    {
        return handleEba.getDeviceLossEba(starttime, endtime, deviceIdList, listhandle, type);
    }
    
}
