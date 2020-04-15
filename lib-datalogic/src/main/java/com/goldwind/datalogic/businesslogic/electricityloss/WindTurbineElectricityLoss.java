package com.goldwind.datalogic.businesslogic.electricityloss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.goldwind.dataaccess.DateAsDef;
import com.goldwind.dataaccess.DynamicRun;
import com.goldwind.dataaccess.database.DatabaseHelper;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.business.BusinessFunc;
import com.goldwind.datalogic.business.model.ChangeDataBean;
import com.goldwind.datalogic.business.model.ProtocolRuleBean;
import com.goldwind.datalogic.businesslogic.model.DeviceInfoObject;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.model.WfidElec;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.LossType;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;
import com.goldwind.datalogic.businesslogic.util.DataTogetherFunction;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;

public class WindTurbineElectricityLoss extends ElectricityLoss
{
    /**
     * 所有协议叶片结冰损失电量类型
     */
    private HashMap<String, ProtocolRuleBean> protocolRuleHash;

    private boolean bladeIceLossPower = false;

    public WindTurbineElectricityLoss(DbOperBase dbOper, Map<String, List<ExpDataObject>> map)
    {
        super(dbOper, map);
        bladeIceLossPower = getBladeIceLossPower();
        protocolRuleHash = getProtocolRuleHash();
    }

    /**
     * 新版一分钟损失电量接口
     * 
     * @param vStartDateTime
     *            开始时间
     * @param vEndDateTime
     *            结束时间
     * @param wtidList
     *            设备id列表
     * @param setidList
     *            setid列表
     * @param typeID
     * @param together
     *            聚合类型枚举
     * @return
     */
    public List<Losselec> getDeviceLossElecNew(Date vStartDateTime, Date vEndDateTime, List<String> wtidList, List<Integer> setidList, String typeID, Together together)
    {

        List<Losselec> losselecResultList = new ArrayList<>();
        try
        {
            // 损失电量类型判断
            if (setidList == null || setidList.isEmpty())
            {
                setidList = getSetIdList(typeID);
            }

            // 设备列表与标杆机聚合
            List<String> deviceList = new ArrayList<>();
            deviceList.addAll(wtidList);
            Map<String, String> bmarkHashMap = getBmarkHash(wtidList);
            for (String bmarkDeviceId : bmarkHashMap.values())
            {
                if (!deviceList.contains(bmarkDeviceId))
                {
                    deviceList.add(bmarkDeviceId);
                }
            }

            // 一分钟数据集合
            Map<String, List<OneDataLossElec>> onedataLossElecListMap = getOneDataLossElecListMap(deviceList, DateAsDef.dateToString(vStartDateTime), DateAsDef.dateToString(vEndDateTime));

            if (onedataLossElecListMap.isEmpty())
            {
                return losselecResultList;
            }
            
            // 变位数据集合
            Map<String, List<ChangeDataBean>> changeDataHash = getChangeDataHash(Integer.parseInt(typeID),deviceList, vStartDateTime, vEndDateTime);
            
            // 故障集合
            Map<String, List<String>> faultTimeListHash = getFaultTimeListHash(deviceList, vStartDateTime, vEndDateTime);

            // 获得所有设备协议对照
            Map<String, DeviceInfoObject> mapdevice = getDeviceObjectNew(deviceList);

            for (String deviceID : wtidList)
            {
                // 所有的损失电量的 hashmap
                Map<String, Losstime> losstimeHash = getDeviceTimeInfoNew(deviceID, bmarkHashMap.get(deviceID), onedataLossElecListMap, faultTimeListHash, changeDataHash, typeID, mapdevice);
                if (!losstimeHash.isEmpty())
                {
                    // 损失电量对应对象聚合
                    Map<Integer, Losstime> energyUseDataMap = new HashMap<>();
                    // 时间段对象集合
                    List<Losstime> losstimeDetailList = new ArrayList<>();
                    // 一分钟故障停机数据集合
                    List<Losstime> energyUseOneDataList = new ArrayList<>();
                    if (together.equals(Together.DayTogether))
                    {
                        energyUseDataMap = DataTogetherFunction.getDayDataAgg(setidList, deviceID, losstimeHash, energyUseOneDataList);
                        Losstime beanLosstime = energyUseDataMap.values().stream().filter(o -> o.getSetid() == LossType.FaultStop.getResult()).findAny().orElse(null);
                        if (beanLosstime != null)
                        {
                            double timeSpan = getFaultTimeLength(deviceID, vStartDateTime, vEndDateTime);
                            timeSpan = DynamicRun.keepPoint(timeSpan, 4);
                            if (timeSpan > 0.1)
                            {
                                logger.info(deviceID + "故障时长" + beanLosstime.getCount() + "min" + "修改为" + timeSpan);
                                beanLosstime.setCount(timeSpan);
                            }
                        }
                    }
                    else if (together.equals(Together.HourTogether))
                    {
                        List<TimeCalendar> falutTimeCalendarList = getTimeHandle(vStartDateTime, vEndDateTime);
                        if (!falutTimeCalendarList.isEmpty())
                        {
                            losstimeDetailList = DataTogetherFunction.getHourDataPart(setidList, losstimeHash, falutTimeCalendarList);
                        }
                    }
                    else if (together.equals(Together.TimeTogether))
                    {
                        losstimeDetailList = DataTogetherFunction.getTimeDataPart(setidList, losstimeHash);
                    }
                    // 整合 obejct.getWfid()
                    if (energyUseDataMap.size() != 0 || !energyUseOneDataList.isEmpty() || !losstimeDetailList.isEmpty())
                    {
                        String wfid = mapdevice.get(deviceID).getWfid();
                        Losselec losselecBean = new Losselec(wfid, deviceID, DateAsDef.dateToString(vStartDateTime), DateAsDef.dateToString(vEndDateTime));
                        losselecBean.setEnergyUseDataHash(energyUseDataMap);
                        losselecBean.setEnergyUseOneDataList(energyUseOneDataList);
                        losselecBean.setLosstimeList(losstimeDetailList);
                        losselecResultList.add(losselecBean);
                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error("DeviceHandle_" + e.getMessage(), e);
            }
        }
        return losselecResultList;
    }

    /**
     * 获取损失电量明细
     * 
     * @param deviceID
     *            风机
     * @param bmarkdevice
     *            标杆风机
     * @param onedataLossElecListMap
     * @param faultTimeListHash
     * @param typeID
     * @param setIdList
     * @param deviceInfoMap
     * @return
     */
    protected Map<String, Losstime> getDeviceTimeInfoNew(String deviceID, String bmarkdevice, Map<String, List<OneDataLossElec>> onedataLossElecListMap,
            Map<String, List<String>> faultTimeListHash, Map<String, List<ChangeDataBean>> changeDataHash, String typeID, Map<String, DeviceInfoObject> deviceInfoMap)
    {
        Map<String, Losstime> losstimehash = new HashMap<>();
        Map<String, Losstime> bmarkLossTimeHash = new HashMap<>();
        try
        {
            // 测试风机本身的故障时间
            List<String> faultTimeList = new ArrayList<>();
            if (faultTimeListHash.containsKey(deviceID))
            {
                faultTimeList = faultTimeListHash.get(deviceID);
            }

            List<OneDataLossElec> onedataList = onedataLossElecListMap.get(deviceID);
            if (onedataList != null && !onedataList.isEmpty())
            {
                for (OneDataLossElec bean : onedataList)
                {
                    String protocolId = bean.getProtocolId();
                    int stopType = -1;
                    int standardpower = 0;
                    // 叶片结冰模式字类型 1 停机模式字 2 运行状态字
                    ProtocolRuleBean protocolExtendBean = getProtocolRuleBean(protocolId);
                    if (null != protocolExtendBean)
                    {
                        stopType = protocolExtendBean.getStopStatusTtype();
                        standardpower = protocolExtendBean.getStandardPower();
                    }
                    List<ExpDataObject> expList = getExpStr(typeID, protocolId, standardpower);
                    int setid = getCodeRusult(expList, bean, changeDataHash, stopType);
                    Losstime obj = new Losstime(bean.getWtid(), setid, bean.getTheorypower(), bean.getRealpower(), 1, bean.getRealloselec(), 0);
                    obj.setDeviceId(bean.getWtid());
                    obj.setElec(bean.getElec());
                    obj.setTime(bean.getRectime());
                    obj.setInveType(deviceInfoMap.get(bean.getWtid()).getInverType());
                    obj.setBmrkloss(obj.getLoss());
                    obj.setWfidbmark(obj.getTheory());
                    if (setid != LossType.PowerFault.getResult())
                    {
                        if (faultTimeList.contains(bean.getRectime()))
                        {
                            // 说明在故障时间段中
                            obj.setSetid(LossType.FaultStop.getResult());
                        }
                        else if (setid == LossType.FaultStop.getResult())
                        {
                            // 当前标识为2006 但是又不在故障时间段中 这个时候 需要再一次判断 是否满足其他类型
                            List<ExpDataObject> listexp1 = expList.stream().filter(o -> LossType.FaultStop.getResult() != o.getSetid()).collect(Collectors.toList());// 剔除2006
                            int newsetid = DeviceHandleFunction.getCodeRusult(listexp1, bean);
                            obj.setSetid(newsetid);
                        }
                    }
                    losstimehash.put(bean.getRectime(), obj);
                }
            }
            else
            {
                // 如果当天不存在一分钟数据，则当天跳过不维护
                return losstimehash;
            }

            // 处理标杆风机
            // 判断当前标杆机组 是否在当前时间是否有故障 得到故障时间
            List<String> bmarkFaultTimeList = new ArrayList<>();
            if (bmarkdevice != null && faultTimeListHash.containsKey(bmarkdevice))
            {
                bmarkFaultTimeList = faultTimeListHash.get(bmarkdevice);
            }
            if (null != bmarkdevice && !bmarkdevice.equals(deviceID))
            {
                List<OneDataLossElec> bmarkOnedataList = onedataLossElecListMap.get(bmarkdevice);
                if (null != bmarkOnedataList && !bmarkOnedataList.isEmpty())
                {
                    for (OneDataLossElec bean : bmarkOnedataList)
                    {
                        String protocolId = BusinessFunc.getWtProtocolidByRectime(bean.getWtid(), bean.getRectime());
                        int stopType = -1;
                        int standardpower = 0;
                        // 叶片结冰模式字类型
                        ProtocolRuleBean protocolExtendBean = getProtocolRuleBean(protocolId);
                        if (null != protocolExtendBean)
                        {
                            stopType = protocolExtendBean.getStopStatusTtype();
                            standardpower = protocolExtendBean.getStandardPower();
                        }
                        List<ExpDataObject> expList = getExpStr(typeID, protocolId, standardpower);
                        if (!bladeIceLossPower)
                        {
                            expList.removeIf(o -> o.getSetid() == 2031);
                        }
                        int setid = DeviceHandleFunction.getCodeRusult(expList, bean, changeDataHash.get(bmarkdevice), stopType);
                        if (setid != LossType.PowerFault.getResult())
                        {
                            // 说明是 标杆机组
                            if (bmarkFaultTimeList.contains(bean.getRectime()))
                            {
                                setid = LossType.FaultStop.getResult();
                            }
                            else if (setid == LossType.FaultStop.getResult())
                            {
                                // 是正常发电
                                DeviceInfoObject deviceInfoObject = deviceInfoMap.get(bean.getWtid());
                                if (deviceInfoObject != null && deviceInfoObject.getNomalstate() != null && deviceInfoObject.getNomalstate().equals(bean.getStopcode()))
                                {
                                    setid = -1;
                                }
                                else
                                {
                                    // 当前标识为2006 但是又不在故障时间段中 这个时候 需要再一次判断 是否满足其他类型
                                    List<ExpDataObject> listexp1 = expList.stream().filter(o -> LossType.FaultStop.getResult() != o.getSetid()).collect(Collectors.toList());// 剔除2006
                                    setid = DeviceHandleFunction.getCodeRusult(listexp1, bean);
                                }
                            }
                        }
                        Losstime losstimeBean = new Losstime(bean.getWtid(), setid, bean.getTheorypower(), bean.getRealpower(), 1, 0, 0);
                        losstimeBean.setElec(bean.getElec());
                        losstimeBean.setTime(bean.getRectime());
                        losstimeBean.setWfidbmark(bean.getElec());
                        losstimeBean.setDeviceId(bean.getWtid());
                        bmarkLossTimeHash.put(bean.getRectime(), losstimeBean);
                    }
                }
            }

            // 通过标杆机发电量设置损失电量
            for (String rectime : losstimehash.keySet())
            {
                // 说明 时间 对应 标杆机组对象是存在的
                if (bmarkLossTimeHash.containsKey(rectime))
                {
                    Losstime bmarkLosstime = bmarkLossTimeHash.get(rectime);
                    if (bmarkLosstime.getSetid() == -1)
                    {
                        Losstime obj = losstimehash.get(rectime);
                        obj.setBmrkloss(bmarkLosstime.getElec() - obj.getElec());
                        obj.setWfidbmark(bmarkLosstime.getWfidbmark());
                    }
                }
            }

            // 通过故障时间列表补漏
            for (String time : faultTimeList)
            {
                if (!losstimehash.containsKey(time))
                {
                    Losstime obj = new Losstime(deviceID, LossType.FaultStop.getResult(), 0, 0, 1, 0, 0);
                    obj.setTime(time);
                    losstimehash.put(time, obj);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        return losstimehash;
    }

   
    /**
     * 获取损失电量明细
     * @param vStartDateTime 开始时间
     * @param vEndDateTime 结束时间
     * @param wtidList  设备id集合
     * @param changeDataHash 变位数据集合
     * @param typeID 损失电量大类typeid
     * @return
     */
    public List<Losstime> getDeviceLossDel(Date vStartDateTime, Date vEndDateTime, List<String> wtidList,  Map<String, List<ChangeDataBean>> changeDataHash , String typeID)
    {
        List<String> listtime = new ArrayList<>();
        List<Losstime> listresult = new ArrayList<>();
        Map<String, List<Losstime>> lit = new HashMap<>();
        try
        {
            // 设备列表与标杆机聚合
            List<String> deviceList = new ArrayList<>();
            deviceList.addAll(wtidList);
            Map<String, String> bmarkHashMap = getBmarkHash(wtidList);
            for (String bmarkDeviceId : bmarkHashMap.values())
            {
                if (!deviceList.contains(bmarkDeviceId))
                {
                    deviceList.add(bmarkDeviceId);
                }
            }

            // 一分钟数据集合
            Map<String, List<OneDataLossElec>> onedataLossElecListMap = getOneDataLossElecListMap(deviceList, DateAsDef.dateToString(vStartDateTime), DateAsDef.dateToString(vEndDateTime));

            if (onedataLossElecListMap.isEmpty())
            {
                return listresult;
            }            
                        
            
            // 故障集合
            Map<String, List<String>> faultTimeListHash = getFaultTimeListHash(deviceList, vStartDateTime, vEndDateTime);

            // 获得所有设备协议对照
            Map<String, DeviceInfoObject> mapdevice = getDeviceObjectNew(deviceList);
            
            for (String deviceID : wtidList)
            {                
                Map<String, Losstime> losstimeHash = getDeviceTimeInfoNew(deviceID, bmarkHashMap.get(deviceID), onedataLossElecListMap, faultTimeListHash, changeDataHash, typeID, mapdevice);
                if (losstimeHash.isEmpty())
                {
                    continue;
                }
                for (String str : losstimeHash.keySet())
                {
                    if (listtime.indexOf(str) < 0)
                    {
                        listtime.add(str);
                    }

                    if (!lit.containsKey(str))
                    {
                        List<Losstime> li = new ArrayList<>();
                        li.add(losstimeHash.get(str));

                        lit.put(str, li);
                    }
                    else
                    {
                        List<Losstime> li = lit.get(str);
                        li.add(losstimeHash.get(str));
                    }
                }
            }

            // 不为0 说明 有限电时间的
            if (listtime.size() != 0)
            {
                // 得到时间段
                List<TimeCalendar> listcal = DataTogetherFunction.getTimeCalendar(listtime);

                for (TimeCalendar time : listcal)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Losstime objdata = new Losstime(null, LossType.Dispatchelc.getResult(), 0, 0, 0, 0, 0);
                    String starttemp = sdf.format(time.getStartime().getTime());
                    String endttemp = sdf.format(time.getEndtime().getTime());

                    objdata.setStartttime(starttemp);
                    objdata.setEndtime(endttemp);

                    WfidElec obj = new WfidElec(starttemp.substring(0, starttemp.length() - 9), 0, 0);
                    double alltheory = 0.0;
                    double allelec = 0.0;

                    while (time.getEndtime().compareTo(time.getStartime()) >= 0)
                    {
                        String timestr = sdf.format(time.getStartime().getTime());
                        List<Losstime> li = lit.get(timestr);

                        for (Losstime data : li)
                        {
                            objdata.setTheory(objdata.getTheory() + data.getTheory());
                            objdata.setReal(objdata.getReal() + data.getReal());
                            objdata.setCount(objdata.getCount() + data.getCount());
                            objdata.setBmrkloss(objdata.getBmrkloss() + data.getBmrkloss());
                            objdata.setLoss(objdata.getLoss() + data.getLoss());
                            objdata.setWfidbmark(objdata.getWfidbmark() + data.getWfidbmark());

                            alltheory += data.getTheory();
                            allelec += data.getElec();
                        }
                        time.getStartime().add(Calendar.MINUTE, 1);
                    }

                    obj.setWfidelec(allelec);
                    obj.setWfidtheroy(alltheory);
                    objdata.setWfidelec(obj);

                    // 处理末尾时间
                    String tempend = endttemp.substring(11, endttemp.length());
                    if (tempend.equals("23:59:00"))
                    {
                        objdata.setEndtime(endttemp.substring(0, 10) + " 24:00:00");
                    }
                    else
                    {
                        time.getEndtime().add(Calendar.MINUTE, 1);
                        objdata.setEndtime(sdf.format(time.getEndtime().getTime()));
                    }
                    listresult.add(objdata);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error(e);
            }
        }
        return listresult;
    }

    /**
     * 根据测试风机 得到 对应风机 风机对象和标杆风机对象
     * 
     * @param device
     *            设备id
     * @param type
     *            类型
     * @return 返回 wtid 对应对象
     */
    protected Map<String, DeviceInfoObject> getDeviceObject(String device)
    {
        Map<String, DeviceInfoObject> hash = new HashMap<>();
        try
        {
            // 得打 标杆机组
            String bmarkdevice = DeviceHandleFunction.getBmarkDeiveInfo(device, dbOper);
            dbOper.openConn();
            StringBuilder str = new StringBuilder();
            // 如果不为null 说明 标杆机组 存在
            if (bmarkdevice != null)
            {
                str.append("select a.wfid,a.wtid,a.protocolid,b.normalstate,a.wtnarrate from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid  and a.wtid in (")
                        .append("\'" + device + "\',").append("\' " + bmarkdevice + "\')");
            }
            else
            {
                str.append("select a.wfid,a.wtid,a.protocolid,b.normalstate,a.wtnarrate from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid  and a.wtid=")
                        .append("\'" + device + "\'");
            }

            ResultSet dt = dbOper.getResultSet(str.toString(), null);
            while (dt.next())
            {
                DeviceInfoObject obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"), dt.getString("normalstate"));
                obj.setInverType(dt.getString("wtnarrate"));
                hash.put(dt.getString("wtid"), obj);
            }
        }
        catch (Exception ex)
        {
            logger.error("DeviceHandle_" + ex.toString());
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                logger.error(e);
            }
        }
        return hash;
    }
    

    /**
     * 根据协议id判断叶片结冰模式字类型
     * 
     * @param protocolid
     * @return
     */
    public ProtocolRuleBean getProtocolRuleBean(String protocolid)
    {
        if (null == protocolRuleHash || !protocolRuleHash.containsKey(protocolid))
        {
            return null;
        }
        return protocolRuleHash.get(protocolid);
    }

    /**
     * 叶片结冰损失电量开关
     * 
     * @return
     */
    private boolean getBladeIceLossPower()
    {
        try
        {
            String sql = "select * from config.modelcnf where key='BladeIceLossPower'";
            List<Map<String, Object>> maps = DatabaseHelper.resultSetToList(sql, null);
            if (!maps.isEmpty())
            {
                return DatabaseHelper.getInt(maps.get(0), "initvalue") == 1;
            }
        }
        catch (Exception e)
        {
            logger.error("getBladeIceLossPower" + e.toString());
        }
        return false;
    }

}
