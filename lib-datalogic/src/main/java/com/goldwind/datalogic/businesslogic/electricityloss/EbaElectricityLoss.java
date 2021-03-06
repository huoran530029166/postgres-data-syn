package com.goldwind.datalogic.businesslogic.electricityloss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DynamicRun;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.business.BusinessFunc;
import com.goldwind.datalogic.business.model.DataPowerObj;
import com.goldwind.datalogic.businesslogic.model.DeviceEbaWtid;
import com.goldwind.datalogic.businesslogic.model.EbaExampLoss;
import com.goldwind.datalogic.businesslogic.model.ExampleElec;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.model.WfinfoData;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.DeviceChangeType;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.LossType;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.RunState;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;
import com.goldwind.datalogic.utils.ControlProcessDef.TypeId;

public class EbaElectricityLoss 
{

    /**
     * 输出日志
     */
    protected static Log LOGGER = Log.getLog(EbaElectricityLoss.class);

    /**
     * 数据库操作对象
     */
    protected DbOperBase dbOper = null;
    
    /**
     * 公式容器
     */
    protected Map<String, List<ExpDataObject>> expHash = new HashMap<>();
    
    
    public EbaElectricityLoss(DbOperBase dbOper,Map<String, List<ExpDataObject>> map)
    {
        this.dbOper = dbOper;
        expHash = map;       
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

        // 增加风机风速仪 故障码
        List<String> listfalut = new ArrayList<>();
        listfalut.add("428");
        listfalut.add("429");
        listfalut.add("641");
        listfalut.add("642");

        List<EbaExampLoss> listloss = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        // 时间转换 String 类型
        String start = sdf.format(starttime);
        String end = sdf.format(endtime);

        // 如果运行状态字 类型为null 或者 没有 重新到数据库 直接读取运行状态字
        if (listhandle == null || listhandle.size() == 0)
        {
            listhandle = getHandle(type);
        }

        for (String deviceId : deviceIdList)
        {
            // 读取 全场的 协议
            WfinfoData windfarmEntity = getDeviceWfid(deviceId);

            if (null == windfarmEntity)
            {
                continue;
            }

            // 读取样板风机
            DeviceEbaWtid templateDevice = getDeciveWtid(deviceId);

            // 处理 device 非正常发电时间
            List<EbaExampLoss> ebaExampLossList = getDeviceTimeObject(deviceId, start, end, windfarmEntity, type);

            // 处理故障时间
            for (EbaExampLoss ebaExampLossModel : ebaExampLossList)
            {
                boolean result = getDeviceLossData(ebaExampLossModel, windfarmEntity, templateDevice, type);

                // 说明不满足 方案一 或者 方案二 直接进入 方案 三 四 五 六
                if (!result)
                {
                    boolean nresult = getDeviceRunTime(ebaExampLossModel);
                    // 说明 当前 这一分钟 对应风速是正常的 走方案三 或者 方案五
                    if (listfalut.indexOf(ebaExampLossModel.getWindfalutcode()) < 0)
                    {
                        // 为真 说明为部署时间小于12个月
                        if (nresult)
                        {
                            // 方案三
                            getPowerCurve(ebaExampLossModel, windfarmEntity);
                            ebaExampLossModel.setFlag(DeviceChangeType.PLANC.getResult());
                        }
                        else
                        {
                            // 方案五
                            boolean re = getOneYearPower(ebaExampLossModel, windfarmEntity);
                            if (!re)
                            {
                                getPowerCurve(ebaExampLossModel, windfarmEntity);
                            }
                            ebaExampLossModel.setFlag(DeviceChangeType.PLANE.getResult());
                        }
                    }
                    else // 进入方案四和方案六
                    {
                        // 首先拿测风塔的风速和风向
                        List<String> listwtid = windfarmEntity.getWindTowerList();
                        if (listwtid != null && listwtid.size() != 0)
                        {
                            Double[] re = getWindAndSpeed(listwtid, ebaExampLossModel.getRectime());
                            // 说明部署时间 小于12个月
                            if (nresult)
                            {
                                // 因为风速 不正常 使用测风塔的数据
                                ebaExampLossModel.setWindspeed(re[0]);
                                getPowerCurve(ebaExampLossModel, windfarmEntity);
                                int ntemp = (re[1].intValue());
                                int n = ntemp / 30;
                                double num = templateDevice.getFunctranskList().get(n);
                                ebaExampLossModel.setLosselec(ebaExampLossModel.getLosselec() * num);
                                ebaExampLossModel.setFlag(DeviceChangeType.PLAND.getResult());
                            }
                            else
                            {
                                ebaExampLossModel.setWindspeed(re[0]);
                                boolean rs = getOneYearPower(ebaExampLossModel, windfarmEntity);
                                if (!rs)
                                {
                                    getPowerCurve(ebaExampLossModel, windfarmEntity);
                                }

                                int ntemp = (re[1].intValue());
                                int n = ntemp / 30;
                                double num = templateDevice.getFunctranskList().get(n);
                                ebaExampLossModel.setLosselec(ebaExampLossModel.getLosselec() * num);
                                ebaExampLossModel.setFlag(DeviceChangeType.PLANF.getResult());
                            }
                        }
                    }
                }
                listloss.add(ebaExampLossModel);
            }
        }
        return listloss;
    }
    
    /**
     * 根据 合同 功率曲线 得到 损失电量
     * 
     * @param obj
     *            损失电量 对象
     * @param wfid
     *            风场对象
     */
    protected void getPowerCurve(EbaExampLoss obj, WfinfoData wfid)
    {
        List<Double> list = new ArrayList<Double>(wfid.getHashpower().keySet());
        if (list.size() == 0)
        {
            obj.setLosselec(0);
            return;
        }
        // 得到所有的风速 集合 从小到大 排序
        Collections.sort(list);
        // 在合同功率曲线之类

        if (obj.getWindspeed() > list.get(0))
        {
            if (obj.getWindspeed() < list.get(list.size() - 1))
            {
                double windSpeed = obj.getWindspeed();
                Set<Double> speedList = wfid.getHashpower().keySet();
                double minWindSpeed = speedList.stream().filter(s -> s < windSpeed).max((a, b) -> a.compareTo(b)).orElse(null);
                double minPower = wfid.getHashpower().get(minWindSpeed);
                double maxWindSpeed = speedList.stream().filter(s -> s >= windSpeed).min((a, b) -> a.compareTo(b)).orElse(null);
                double maxPower = wfid.getHashpower().get(maxWindSpeed);
                double reuslt = getDataResult(maxWindSpeed, maxPower, minWindSpeed, minPower, obj.getWindspeed());
                double lossElec = Math.max(0, reuslt / 60 - obj.getKind());
                obj.setLosselec(lossElec);
            }
            else // 超过 合同功率曲线的最小值 用 满载来计算损失电量
            {
                double lossElec = Math.max(0, wfid.getWtidpower().get(obj.getWtid()) / 60 - obj.getKind());
                obj.setLosselec(lossElec);
            }
        }
        else
        {
            double lossElec = Math.max(0, wfid.getHashpower().get(list.get(1)) / 60 - obj.getKind());
            obj.setLosselec(lossElec);
        }
    }

    
    /**
     * 通过自学习理论功率曲线
     * 
     * @param obj
     *            风机对象
     * @param wfid
     *            风场对象
     * @return boolean 返回true/false
     */
    protected boolean getOneYearPower(EbaExampLoss ebaEntity, WfinfoData wfinfoEntity)
    {
        boolean flag = false;
        try
        {

            String endTime = ebaEntity.getRectime();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            calendar.setTime(sdf.parse(endTime));
            calendar.add(Calendar.YEAR, -1);
            String starttime = sdf.format(calendar.getTime());

            // 存放风速对应功率表
            List<DataPowerObj> windSpeedTbpowList = getWindspeedTbpowList(ebaEntity.getWtid(), starttime, endTime);

            // 划分区间 按照0.5m/s 风速区间 划分
            // 如果大于 这个 值 直接 使用合同 功率曲线
            if (ebaEntity.getWindspeed() > 31.25 || windSpeedTbpowList.size() == 0)
            {
                flag = false;
            }
            else
            {
                // 以0.5m/s 为中心 取区间
                if (ebaEntity.getWindspeed() > 0.75 && ebaEntity.getWindspeed() < 31.25)
                {
                    double windSpeed = ebaEntity.getWindspeed();
                    Set<Double> speedList = wfinfoEntity.getHashpower().keySet();
                    double minWindSpeed = speedList.stream().filter(s -> s < windSpeed).max((a, b) -> a.compareTo(b)).orElse(null);
                    double maxWindSpeed = speedList.stream().filter(s -> s >= windSpeed).min((a, b) -> a.compareTo(b)).orElse(null);
                    double halfStep = (maxWindSpeed - minWindSpeed) * 0.5;
                    double minPower = windSpeedTbpowList.stream().filter(s -> s.getWindspeed() > minWindSpeed - halfStep && s.getWindspeed() <= minWindSpeed + halfStep)
                            .mapToDouble(DataPowerObj::getPower).average().getAsDouble();
                    double maxPower = windSpeedTbpowList.stream().filter(s -> s.getWindspeed() > maxWindSpeed - halfStep && s.getWindspeed() <= maxWindSpeed + halfStep)
                            .mapToDouble(DataPowerObj::getPower).average().getAsDouble();
                    if (minPower <= 0)
                    {
                        minPower = wfinfoEntity.getHashpower().get(minWindSpeed);
                    }
                    if (maxPower <= 0)
                    {
                        maxPower = wfinfoEntity.getHashpower().get(maxWindSpeed);
                    }
                    double re = getDataResult(maxWindSpeed, maxPower, minWindSpeed, minPower, ebaEntity.getWindspeed());
                    double losselec = Math.max(0.0, re / 60 - ebaEntity.getKind());
                    flag = true;
                    ebaEntity.setLosselec(losselec);
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        return flag;
    }
    
    /**
     * 得到非正常时间 损失电量类型
     * 
     * @param obj
     *            风机对象
     * @param wfid
     *            风机对应协议
     * @param object
     *            风机对应样板对象
     * @param type
     *            类型
     * 
     * @return boolean 满足 方案一 或者 方案二
     */
    protected boolean getDeviceLossData(EbaExampLoss obj, WfinfoData wfid, DeviceEbaWtid object, String type)
    {
        boolean result = false;
        try
        {
            Map<String, ExampleElec> hs = new HashMap<>();
            List<ExampleElec> listobj = new ArrayList<>();

            dbOper.openConn();
            String sql = "SELECT wtid,wtstatus,limitcode,faultcode,stopcode,limitstatus,rectime,defresipid,endelec,beginelec FROM onedata ";
            sql += " where rectime = '" + obj.getRectime() + "' and ";
            sql += " wtid in (";

            for (int i = 0; i < wfid.getWindTurbineList().size(); i++)
            {
                if (i == wfid.getWindTurbineList().size() - 1)
                {
                    sql += "'" + wfid.getWindTurbineList().get(i) + "') ";
                }
                else
                {
                    sql += "' " + wfid.getWindTurbineList().get(i) + "',";
                }
            }

            sql += " order by wtid";
            ResultSet ds = dbOper.getResultSet(sql, null);
            while (ds.next())
            {
                String device = ds.getString("wtid");
                String rectime = ds.getString("rectime");
                String wtstatus = ds.getString("wtstatus");
                String limitcode = ds.getString("limitcode");
                String faultcode = ds.getString("faultcode");
                String stopcode = ds.getString("stopcode");
                String limitstatus = ds.getString("limitstatus");
                int defresipid = ds.getInt("defresipid"); // 默认责任人
                double endelec = ds.getDouble("endelec"); // 末发电量
                double beginelec = ds.getDouble("beginelec"); // 初始发电量

                OneDataLossElec bean = new OneDataLossElec(device, rectime);                                                                                                                       
                bean.setWtstatus(wtstatus);
                bean.setLimitstatus(limitstatus);
                bean.setFaultcode(faultcode);
                bean.setLimitcode(limitcode);
                bean.setStopcode(stopcode);
                
                
                List<ExpDataObject> liststr = getExpStr(type, wfid.getWtidProtocolidHash().get(device));
                int setid = DeviceHandleFunction.getCodeRusult(liststr, bean);

                if (setid == RunState.IAOGFP.getResult() || defresipid == 9 || setid < LossType.Normal.getResult())
                {
                    // 这种情况 包括 beginelec为0 或者 endelec - beginelec 异常大的情况 做一次修正
                    if (DynamicRun.isDoubleEqual(beginelec, 0) || DynamicRun.isDoubleEqual(endelec, 0))
                    {
                        ExampleElec data = new ExampleElec(rectime, 0, 1);
                        hs.put(device, data);
                    }
                    else
                    {
                        if (wfid.getWtidpower().get(device) != null)
                        {
                            double real = BusinessFunc.getIecupdValue(1, (endelec - beginelec), 1, wfid.getWtidpower().get(device), 150);
                            ExampleElec data = new ExampleElec(rectime, real, 1);
                            hs.put(device, data);
                        }
                        else
                        {
                            ExampleElec data = new ExampleElec(rectime, 0, 1);
                            hs.put(device, data);
                        }
                    }
                }
            }

            if (hs.size() != 0)
            {
                // 在正常发电的风机中 有多少是样板风机的
                if (object.getWtidList().size() > 0)
                {
                    for (String wtid : object.getWtidList())
                    {
                        if (hs.get(wtid) != null)
                        {
                            ExampleElec data = hs.get(wtid);
                            listobj.add(data);
                        }
                    }
                }

                // 进入方案一的条件 样板机组在测试风机非正常发电的一分钟内 正常发电的情况 取样板风机 发电量的合计值 求平均
                // 大于等于7说明 样本风机 有7台
                if (listobj.size() >= 7)
                {
                    double avgElec = listobj.stream().collect(Collectors.averagingDouble(ExampleElec::getElec));
                    double lossElec = Math.max(0, avgElec - obj.getKind());
                    obj.setLosselec(lossElec);
                    obj.setFlag(DeviceChangeType.PLANA.getResult());
                    result = true;
                }
                else
                {
                    double avgElec = hs.values().stream().collect(Collectors.averagingDouble(ExampleElec::getElec));
                    double lossElec = Math.max(0, avgElec - obj.getKind());
                    obj.setLosselec(lossElec);
                    obj.setFlag(DeviceChangeType.PLANB.getResult());
                    result = true;
                }

                if (result)
                {
                    if (obj.getLosselec() < 0)
                    {
                        obj.setLosselec(0);
                    }
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return result;
    }
    
    /**
     * 得到EBA的部署时间
     * 
     * @param obj
     *            风机对象
     * @return 返回true/false
     */
    protected boolean getDeviceRunTime(EbaExampLoss obj)
    {
        boolean result = false;
        try
        {
            dbOper.openConn();
            String sql = "select min(rectime) as rectime ,max(rectime) as maxtime from powcurve where wtid = '" + obj.getWtid() + "'";
            String rectime = null;
            String maxtime = null;

            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                rectime = dt.getString("rectime");
                maxtime = dt.getString("maxtime");
            }

            // 说明 在数据库 没有 对应的数据 最小时间个最大时间相同 说明 部署小于12个月
            if (rectime == null || maxtime == null)
            {
                return true;
            }

            if (rectime.equals(maxtime))
            {
                result = true;
            }
            else
            {
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
                cal1.setTime(sdf.parse(rectime));
                cal.setTime(sdf.parse(maxtime));
                cal1.add(Calendar.YEAR, 1);

                // 最小时间 + 1年 大于 最大时间 说明 EBA 部署小于12个月
                if (cal1.compareTo(cal) >= 0)
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return result;
    }

    /**
     * 得到风机样板风机对相关
     * 
     * @param wtid
     *            风机
     * @return 返回对象
     */
    protected DeviceEbaWtid getDeciveWtid(String wtid)
    {
        DeviceEbaWtid obj = null;
        try
        {
            List<String> listwtid = new ArrayList<>();
            List<Double> list = new ArrayList<>();
            double windspeed = 0.0;

            dbOper.openConn();
            String sql = "select * from config.examplecnf where deviceid = '" + wtid + "'";
            ResultSet dt = dbOper.getResultSet(sql, null);

            while (dt.next())
            {
                windspeed = dt.getDouble("windspeed");
                if (dt.getString("examplewt") != null && !dt.getString("examplewt").equals(""))
                {
                    String[] str = dt.getString("examplewt").split(";");
                    for (String device : str)
                    {
                        listwtid.add(device);
                    }
                }

                // 传递函数
                list.add(dt.getDouble("functransk1"));
                list.add(dt.getDouble("functransk2"));
                list.add(dt.getDouble("functransk3"));
                list.add(dt.getDouble("functransk4"));
                list.add(dt.getDouble("functransk5"));
                list.add(dt.getDouble("functransk6"));
                list.add(dt.getDouble("functransk7"));
                list.add(dt.getDouble("functransk8"));
                list.add(dt.getDouble("functransk9"));
                list.add(dt.getDouble("functransk10"));
                list.add(dt.getDouble("functransk11"));
                list.add(dt.getDouble("functransk12"));
            }
            obj = new DeviceEbaWtid(windspeed, listwtid, list);
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return obj;
    }
    
    /**
     * 得到除 正常发电状态之后的时间
     * 
     * @param wtid
     *            风机
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param wfid
     *            风机对应对协议
     * @param type
     *            类型
     * @return 返回正常时间
     */
    protected List<EbaExampLoss> getDeviceTimeObject(String wtid, String starttime, String endtime, WfinfoData wfid, String type)
    {
        List<EbaExampLoss> listtime = new ArrayList<>();
        try
        {
            List<String> lirectime = new ArrayList<>();
            if ("20".equals(type))
            {
                lirectime = getDeviceFalutTimeList(starttime, endtime, wtid);
            }

            dbOper.openConn();
            String sql = "select windspeed,rectime,wtstatus,stopcode,faultcode,limitcode,limitstatus,endelec,beginelec ,realpower,";
            sql += " defresipid,upresipid,upstopcode from onedata  where wtid = '" + wtid + "' and ";
            sql += " rectime between '" + starttime + "' and '" + endtime + "' order by rectime";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                String wtstaute = dt.getString("wtstatus");
                String stopcode = dt.getString("stopcode");
                String faultcode = dt.getString("faultcode");
                String limitcode = dt.getString("limitcode");
                String limitstatus = dt.getString("limitstatus");
                String time = dt.getString("rectime");
                double windspeed = dt.getBigDecimal("windspeed") != null ? dt.getBigDecimal("windspeed").doubleValue() : 0;
                double endelec = dt.getDouble("endelec"); // 末发电量
                double beginelec = dt.getDouble("beginelec");// 初始发电量
                int defresipid = dt.getInt("defresipid"); // 默认责任人
                int upresipid = dt.getInt("upresipid"); // 更新之后 责任人
                String stopcode1 = dt.getString("upstopcode"); // 修改之后的停机模式字

                //定义最终停机模式字
                String stopCodeString=stopcode;
                if (stopcode1!=null&&!stopcode1.isEmpty())
                {
                    stopCodeString=stopcode1;
                }
                
                OneDataLossElec bean = new OneDataLossElec(wtid, dt.getString("rectime"));                                                                                                                       
                bean.setWtstatus(wtstaute);
                bean.setLimitstatus(limitstatus);
                bean.setFaultcode(faultcode);
                bean.setLimitcode(limitcode);
                bean.setStopcode(stopcode);
                
                List<ExpDataObject> liststr = getExpStr(type, wfid.getWtidProtocolidHash().get(wtid));
                int setid = -1;
                // 说明当前有 状态修改
                if (stopcode1 != null)
                {
                    setid = DeviceHandleFunction.getCodeRusult(liststr, bean);
                }
                else // 说明状态没有修改 读取 原来的stopcode
                {
                    setid = DeviceHandleFunction.getCodeRusult(liststr, bean);
                }

                // 增加损失电量type为20 走eba算法
                EbaExampLoss obj = null;
                if ("20".equals(type))
                {
                    if (lirectime.indexOf(time) > -1)
                    {
                        setid = 2006;
                        obj = new EbaExampLoss(wtid, time, Integer.valueOf(stopCodeString), 0.0);
                        if (upresipid == 0)
                        {
                            obj.setRespid(8); // 8为 其他
                        }
                        else
                        {
                            obj.setRespid(upresipid);
                        }
                    }
                    else
                    {
                        if (setid > LossType.Normal.getResult() && setid != 2006)
                        {
                            obj = new EbaExampLoss(wtid, time, Integer.valueOf(stopCodeString), 0.0);
                            if (upresipid == 0)
                            {
                                obj.setRespid(8); // 8为 其他
                            }
                            else
                            {
                                obj.setRespid(upresipid);
                            }
                        }
                    }
                }
                else
                {
                    if (setid != RunState.IAOGFP.getResult() && defresipid != 9)
                    {
                        if (stopcode1 != null)
                        {
                            obj = new EbaExampLoss(wtid, time, Integer.valueOf(stopCodeString), 0.0);
                            if (upresipid == 0)
                            {
                                obj.setRespid(8); // 8为 其他
                            }
                            else
                            {
                                obj.setRespid(upresipid);
                            }
                        }
                        else
                        {
                            obj = new EbaExampLoss(wtid, time, Integer.valueOf(stopCodeString), 0.0);
                            if (defresipid == 0)
                            {
                                obj.setRespid(8); // 8为 其他
                            }
                            else
                            {
                                obj.setRespid(defresipid);
                            }
                        }
                    }
                }

                if (obj != null)
                {
                    obj.setWindspeed(windspeed);
                    obj.setWindfalutcode(faultcode);
                    double real = BusinessFunc.getIecupdValue(1, (endelec - beginelec), 1, wfid.getWtidpower().get(wtid), 150);
                    if (DynamicRun.isDoubleEqual(beginelec, 0) || DynamicRun.isDoubleEqual(endelec, 0))
                    {
                        obj.setKind(0);
                    }
                    else
                    {
                        obj.setKind(real);
                    }
                    listtime.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return listtime;
    }
    
    /**
     * 得到风机 所在的风场
     * 
     * @param wtid
     *            风机
     * @return 返回风场
     */
    protected WfinfoData getDeviceWfid(String wtid)
    {
        WfinfoData obj = null;
        try
        {
            List<String> listwind = new ArrayList<>();
            dbOper.openConn();
            String sql = "select * from config.wtinfo where wtid ='" + wtid + "'";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                obj = new WfinfoData(dt.getString("wfid"));
            }

            // 得到测风塔
            sql = "select b.wtid from config.wttypeinfo a, config.wtinfo b where b.wfid = ' " + obj.getWfid() + "' and b.protocolid = a.protocolid and a.devicetype = 2";
            ResultSet ds = dbOper.getResultSet(sql, null);
            while (ds.next())
            {
                listwind.add(ds.getString("wtid"));
            }
            obj.setWindTowerList(listwind);

            List<String> listwtid = new ArrayList<>();
            Map<String, String> result = new HashMap<String, String>();
            Map<String, Double> wtidpower = new HashMap<>();
            // 得到 风场下的所有 风机 和 风机对应的协议
            sql = "select a.wfid,a.wtid,a.protocolid,b.normalstate,b.standardpower from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid  and a.wfid= '" + obj.getWfid()
                    + "' and devicetype = 0";
            sql += " order by wtid";
            ResultSet dn = dbOper.getResultSet(sql, null);
            while (dn.next())
            {
                listwtid.add(dn.getString("wtid"));
                result.put(dn.getString("wtid"), dn.getString("protocolid"));
                wtidpower.put(dn.getString("wtid"), dn.getDouble("standardpower"));
            }
            obj.setWtidProtocolidHash(result);
            obj.setWindTurbineList(listwtid);
            obj.setWtidpower(wtidpower);

            // 获取 录入功率曲线
            String id = null;
            Map<Double, Double> hash = new HashMap<Double, Double>();
            // 先获取风机guarpowid
            String getGuarpowidSql = "select guarpowid from config.wtpowcurve where  wtid = '" + wtid + "' ";
            ResultSet rsGuarpowid = dbOper.getResultSet(getGuarpowidSql, null);
            while (rsGuarpowid.next())
            {
                id = rsGuarpowid.getString("guarpowid");
            }
            if (id == null)
            {
                // 风机没有获取风场guarpowid
                sql = "select * from config.guarpow where wfid = '" + obj.getWfid() + "' and density = 1";
                ResultSet nn = dbOper.getResultSet(sql, null);
                while (nn.next())
                {
                    id = nn.getString("id");
                }
            }

            // 根据id得到功率
            if (id != null)
            {
                sql = "select * from config.guarpowdetail where guarpowid =  " + id + " order by windspeed";
                ResultSet mm = dbOper.getResultSet(sql, null);
                while (mm.next())
                {
                    hash.put(mm.getDouble("windspeed"), mm.getDouble("tbpow"));
                }
            }
            obj.setHashpower(hash);
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {

            }
        }
        return obj;
    }

    protected List<DataPowerObj> getWindspeedTbpowList(String wtid, String startTime, String endTime)
    {
        List<DataPowerObj> windSpeedTbpowList = new ArrayList<DataPowerObj>();
        try
        {
            dbOper.openConn();
            // 存放风速对应功率表
            String sql = "select * from powcurve where wtid ='" + wtid + "' and rectime between '" + startTime + "' and '" + endTime + "'";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                windSpeedTbpowList.add(new DataPowerObj(dt.getDouble("winspeed"), dt.getDouble("tbpow")));
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return windSpeedTbpowList;
    }

    /**
     * 得到这一分钟 测风塔的 风速和风向 平均值
     * 
     * @param list
     *            测风塔集合
     * @param rectime
     *            时间
     * @return 返回 风速 对应值
     */
    protected Double[] getWindAndSpeed(List<String> list, String rectime)
    {
        Double[] str = new Double[2];
        try
        {
            double hh = 0.0;
            double ee = 0.0;

            dbOper.openConn();
            String sql = "select * from onedata where rectime = '" + rectime + "' and wtid in (";
            for (int i = 0; i < list.size(); i++)
            {
                if (i == list.size() - 1)
                {
                    sql += "'" + list.get(i) + "') ";
                }
                else
                {
                    sql += "' " + list.get(i) + "',";
                }
            }

            sql += " order by wtid";
            ResultSet dt = dbOper.getResultSet(sql, null);
            int len = 0;
            while (dt.next())
            {
                if (dt.getDouble("windspeed") > 0 && dt.getDouble("realpower") > 0)
                {
                    hh += dt.getDouble("windspeed");
                    ee += dt.getDouble("realpower");
                    len++;
                }
                else if (dt.getDouble("theorypower") > 0 && dt.getDouble("wtstatus") > 0)
                {
                    hh += dt.getDouble("theorypower");
                    ee += dt.getDouble("wtstatus");
                    len++;
                }
                else if (dt.getDouble("limitstatus") > 0 && dt.getDouble("faultcode") > 0)
                {
                    hh += dt.getDouble("limitstatus");
                    ee += dt.getDouble("faultcode");
                    len++;
                }
            }
            if (len > 0)
            {
                hh = hh / len;
                ee = ee / len;
            }
            str[0] = hh;
            str[1] = ee;
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return str;
    }
    
    /**
     * 线性算法 计算功率
     * 
     * @param maxwindspeed
     *            大风速
     * @param maxpower
     *            大功率
     * @param minwindspeed
     *            小风速
     * @param minpower
     *            小功率
     * @param windspeed
     *            本省风速
     * @return 返回真实值
     */
    protected double getDataResult(double maxwindspeed, double maxpower, double minwindspeed, double minpower, double windspeed)
    {
        double result = 0.0;

        if ((maxwindspeed - minwindspeed) > 0 && (maxpower - minpower) > 0)
        {
            result = (maxpower - minpower) / (maxwindspeed - minwindspeed) * (windspeed - minwindspeed) + minpower;
        }
        else
        {
            result = minpower;
        }
        return result;
    }
    


    /**
     * 得到公式
     * 
     * @param flag
     *            维护类型
     * @param protocalid
     *            协议
     * @return 返回公式
     */
    protected List<ExpDataObject> getExpStr(String flag, String protocalid)
    {
        List<ExpDataObject> listexp = new ArrayList<>();

        // 根据协议得到公式集合
        if (flag.equals(TypeId.losstype.getResult()))
        {
            if (expHash.containsKey(protocalid))
            {
                listexp = expHash.get(protocalid);
            }
            else
            {
                listexp = expHash.get(flag);
            }
        }
        else
        {
            listexp = expHash.get(flag);
        }
        return listexp;
    }

    /**
     * 默认损失类型为空 是所有的 损失电量
     * 
     * @param flag
     *            损失电量类型
     * @return 返回标识容器
     */
    protected List<Integer> getHandle(String flag)
    {
        List<Integer> list = new ArrayList<>();
        try
        {
            dbOper.openConn();
            StringBuffer str = new StringBuffer();
            str.append("select id from config.energyuse_config WHERE typeid = ").append("\'" + flag + "\' order by sort");
            ResultSet dt = dbOper.getResultSet(str.toString(), null);
            while (dt.next())
            {
                list.add(dt.getInt("id"));
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return list;
    }

    /**
     * 划分时间段 00:00:00~00:59:00 为1小时
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @return 返回时间段对象
     */
    protected List<TimeCalendar> getTimeHandle(Date starttime, Date endtime)
    {
        List<TimeCalendar> list = new ArrayList<>();
        Calendar ca = Calendar.getInstance(); // 记录开始时间
        Calendar ca1 = Calendar.getInstance(); // 记录结束时间
        boolean falg = true; // 排除第一次 不是 00分情况

        Calendar castart = Calendar.getInstance();
        castart.setTime(starttime);

        Calendar caend = Calendar.getInstance();
        caend.setTime(endtime);

        String timestart = "";
        String timeend = "";

        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        while (caend.compareTo(castart) >= 0)
        {
            // 说明是00分开始
            if (castart.get(Calendar.MINUTE) == 0 || falg)
            {
                timestart = sdf.format(castart.getTime());
                ca.setTime(castart.getTime());
                falg = false;
            }

            // 说明是59分结尾
            if (castart.get(Calendar.MINUTE) == 59 && (caend.compareTo(castart) > 0))
            {
                timeend = sdf.format(castart.getTime());
                TimeCalendar obj = new TimeCalendar(timestart, timeend);
                ca1.setTime(castart.getTime());
                list.add(obj);
            }
            else if (caend.compareTo(castart) == 0)
            {
                timeend = sdf.format(caend.getTime());
                TimeCalendar obj = new TimeCalendar(timestart, timeend);
                ca1.setTime(caend.getTime());
                list.add(obj);
            }
            castart.add(Calendar.MINUTE, 1);
        }
        return list;
    }
     
    /**
     * 
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @param wtid
     *            设备对象
     * @param protocolid
     *            协议
     * @return 返回时间段对象
     */
    protected List<String> getDeviceFalutTimeList(String start, String end, String wtid)
    {
        List<String> listresult = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        try
        {
            List<TimeCalendar> list = new ArrayList<>();
            dbOper.openConn();            
            // 处理有交集 或者 endtime 为null 的情况 首先去 处理 endtime 不为 null 后处理 endtime 不为null时
            StringBuilder sq = new StringBuilder();
            sq.append("select a.* from wterrortotal a,config.pathdescr b where a.wtid = ").append("\'" + wtid + "\' and ");
            sq.append(" b.protocolid = get_wtprotocolrec_protocolid(a.wtid,a.starttime) and b.iecpath = 'WTUR.Flt.Rs.S' and a.blooeydescr = b.iecvalue and b.faulttype != 'ERROR_GRID' and a.totalflag = '1' ");
            sq.append(" and  (endtime is null or endtime>=\'"+start+"\') and ( starttime  <= \'" + end + "\') order by starttime");

            ResultSet dq = dbOper.getResultSet(sq.toString(), null);
            while (dq.next())
            {
                String timestart = dq.getString("starttime"); // 读取到的 开始时间
                String timeend = dq.getString("endtime"); // 读取结束时间

                if (timeend == null)
                {
                    Calendar faultStartCalendar = Calendar.getInstance();
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();

                    faultStartCalendar.setTime(sdf.parse(timestart)); // 读取到的开始时间
                    String strtemp = sdf.format(faultStartCalendar.getTime()).substring(0, sdf.format(faultStartCalendar.getTime()).length() - 2) + "00";
                    faultStartCalendar.setTime(sdf.parse(strtemp));

                    startCalendar.setTime(sdf.parse(start)); // 开始时间
                    endCalendar.setTime(sdf.parse(end)); // 结束时间

                    if (faultStartCalendar.compareTo(startCalendar) > 0)
                    {
                        TimeCalendar obj = new TimeCalendar(faultStartCalendar, endCalendar);
                        list.add(obj);
                    }
                    else
                    {
                        TimeCalendar obj = new TimeCalendar(startCalendar, endCalendar);
                        list.add(obj);
                    }
                }
                else
                {
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    Calendar faultEndCalendar = Calendar.getInstance();                    
                    Calendar faultStartCalendar = Calendar.getInstance();

                    startCalendar.setTime(sdf.parse(start)); // 查询开始时间
                    endCalendar.setTime(sdf.parse(end)); // 查询结束时间
                    faultEndCalendar.setTime(sdf.parse(timeend)); // 读取结束时间

                    faultStartCalendar.setTime(sdf.parse(timestart)); // 读取开始时间
                    String strtemp = sdf.format(faultStartCalendar.getTime()).substring(0, sdf.format(faultStartCalendar.getTime()).length() - 2) + "00";
                    faultStartCalendar.setTime(sdf.parse(strtemp));
                    
                    // 每一条 故障的 结束时间 如果比查询的开始时间 要小 说明当前 故障是结束掉的
                    if (startCalendar.compareTo(faultEndCalendar) >= 0)
                    {
                        continue;
                    }                    

                    // 查询开始时间要比读取开始时间要大 && 读取的结束时间大于查询结束时间 增加完善 跨天的判断
                    if (faultStartCalendar.compareTo(startCalendar) >= 0 && faultEndCalendar.compareTo(endCalendar) > 0)
                    {
                        TimeCalendar obj = new TimeCalendar(faultStartCalendar, endCalendar);
                        list.add(obj);
                    }
                    else if (faultStartCalendar.compareTo(startCalendar) >= 0 && faultEndCalendar.compareTo(endCalendar) <= 0)
                    {
                        TimeCalendar obj = new TimeCalendar(faultStartCalendar, faultEndCalendar);
                        list.add(obj);
                    }
                    else if (startCalendar.compareTo(faultStartCalendar) > 0 && endCalendar.compareTo(faultEndCalendar) >= 0)
                    {
                        TimeCalendar obj = new TimeCalendar(startCalendar, faultEndCalendar);
                        list.add(obj);
                    }
                    else if (startCalendar.compareTo(faultStartCalendar) > 0 && endCalendar.compareTo(faultEndCalendar) < 0)
                    {
                        TimeCalendar obj = new TimeCalendar(startCalendar, endCalendar);
                        list.add(obj);
                    }
                }
            }

            // 得到故障时间段中 每一天
            if (!list.isEmpty())
            {
                listresult= getFaultTime(list);
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                dbOper.closeConn();
            }
            catch (SQLException e)
            {
                LOGGER.error(e);
            }
        }
        return listresult;
    }

    /**
     * 得到故障时间
     * 
     * @param list
     *            故障时间集合
     * @return 返回String 故障时间
     */
    protected List<String> getFaultTime(List<TimeCalendar> list)
    {
        List<String> li = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        try
        {
            for (TimeCalendar obj : list)
            {
                while (obj.getEndtime().compareTo(obj.getStartime()) >= 0)
                {
                    String rectime = sdf.format(obj.getStartime().getTime());
                    li.add(rectime);
                    obj.getStartime().add(Calendar.MINUTE, 1);
                }
            }
        }
        catch (Exception ex)
        {
            LOGGER.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        return li;
    }
}
