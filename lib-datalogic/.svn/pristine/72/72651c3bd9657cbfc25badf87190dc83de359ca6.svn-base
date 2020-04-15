package com.goldwind.datalogic.businesslogic.electricityloss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.business.BusinessFunc;
import com.goldwind.datalogic.businesslogic.model.DeviceInfoObject;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;
import com.goldwind.datalogic.businesslogic.util.DataTogetherFunction;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;

public class InverterElectricityLoss extends ElectricityLoss
{
    public InverterElectricityLoss(DbOperBase dbOper, Map<String, List<ExpDataObject>> map)
    {
        super(dbOper, map);
    }

    /**
     * 新版逆变器损失电量查询接口
     * 
     * @param starttime
     * @param endtime
     * @param wtidlist
     * @param setidList
     * @param type
     * @param together
     * @return
     */
    public List<Losselec> getDeviceLossElecNew(Date starttime, Date endtime, List<String> wtidlist, List<Integer> setidList, String type, Together together)
    {
        // "yyyy-MM-dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        // 时间转换 String 类型
        String start = sdf.format(starttime);
        String end = sdf.format(endtime);
        List<Losselec> result = new ArrayList<>();
        try
        {
            // 损失电量类型判断
            if (setidList == null || setidList.isEmpty())
            {
                setidList = getSetIdList(type);
            }

            // 设备列表与标杆机聚合
            List<String> deviceList = new ArrayList<>();
            deviceList.addAll(wtidlist);

            // 获取标杆机map
            Map<String, String> bmarkHashMap = getBmarkHashAllFarm();

            // 获得所有设备协议对照
            Map<String, DeviceInfoObject> deviceInfoMap = getDeviceMapSameFarm(wtidlist);

            // 风机-标杆机s Map
            Map<String, List<String>> deviceBmarksMap = new HashMap<>();

            // 获取所有风机标杆机
            for (String deviceId : wtidlist)
            {
                List<String> bmarkList = getBmarkList(deviceId, bmarkHashMap, deviceInfoMap);
                deviceBmarksMap.put(deviceId, bmarkList);
                for (String bmark : bmarkList)
                {
                    if (!deviceList.contains(bmark))
                    {
                        deviceList.add(bmark);
                    }
                }
            }

            // 一分钟数据集合
            Map<String, List<OneDataLossElec>> onedataLossElecListMap = getOneDataLossElecListMap(deviceList, start, end);

            for (String deviceID : wtidlist)
            {
                List<String> bmarkList = deviceBmarksMap.get(deviceID);

                // 所有的损失电量的 hashmap
                Map<String, Losstime> hash = getDeviceTimeInfoNew(deviceID, bmarkList, onedataLossElecListMap, type, setidList, deviceInfoMap);

                if (hash.size() != 0)
                {
                    Map<Integer, Losstime> mapdataobj = new HashMap<>(); // 损失电量对应对象聚合
                    List<Losstime> listdataobj = new ArrayList<>(); // 时间段对象集合
                    List<Losstime> listfalut = new ArrayList<>(); // 一分钟故障停机数据集合
                    if (together.equals(Together.DayTogether))
                    {
                        mapdataobj = DataTogetherFunction.getDayDataAgg(setidList, deviceID, hash, listfalut);
                    }
                    else if (together.equals(Together.HourTogether))
                    {
                        // 时间段
                        List<TimeCalendar> listfaluttime = getTimeHandle(starttime, endtime);
                        if (!listfaluttime.isEmpty())
                        {
                            listdataobj = DataTogetherFunction.getHourDataPart(setidList, hash, listfaluttime);
                        }
                    }
                    else if (together.equals(Together.TimeTogether))
                    {
                        listdataobj = DataTogetherFunction.getTimeDataPart(setidList, hash);
                    }

                    // 整合 obejct.getWfid()
                    if (!mapdataobj.isEmpty() || !listfalut.isEmpty() || !listdataobj.isEmpty())
                    {
                        Losselec dataobj = new Losselec(deviceInfoMap.get(deviceID).getWfid(), deviceID, start, end);
                        dataobj.setEnergyUseDataHash(mapdataobj);
                        dataobj.setEnergyUseOneDataList(listfalut);
                        dataobj.setLosstimeList(listdataobj);
                        result.add(dataobj);
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
        return result;
    }

    /**
     * 获得风机的指定标杆机组 获取该电场下面所有与其标杆机类型相同的标杆机集合
     * 
     * @param wtid
     * @param bmarkHashMap
     *            所有风机-标杆机 keymap
     * @param deviceInfoMap
     *            所有风机-协议 keymap
     * @return
     */
    private List<String> getBmarkList(String wtid, Map<String, String> bmarkHashMap, Map<String, DeviceInfoObject> deviceInfoMap)
    {
        List<String> list = new ArrayList<>();
        String bmarkID = bmarkHashMap.get(wtid);
        if (null == bmarkID)
        {
            return list;
        }
        DeviceInfoObject bean = deviceInfoMap.get(bmarkID);
        if (null == bean || null == bean.getWfid() || null == bean.getInverType())
        {
            return list;
        }
        for (DeviceInfoObject info : deviceInfoMap.values())
        {
            // 找到同电场下，同类型的，所有标杆机逆变器
            if (bean.getWfid().equals(info.getWfid()) && bean.getInverType().equals(info.getInverType()) && bmarkHashMap.values().contains(info.getWtid()))
            {
                list.add(info.getWtid());
            }
        }
        return list;
    }

    /**
     * 设备时间对应类型
     * 
     * @param device
     *            设备ID
     * @param start
     *            时间时间
     * @param end
     *            结束时间
     * @param type
     *            公式类型
     * @param setidList
     *            维护类型集合
     * @param hashobj
     *            对象设备
     * @return 返回时间对应duixiang
     */
    protected Map<String, Losstime> getDeviceTimeInfoNew(String device, List<String> bmarkList, Map<String, List<OneDataLossElec>> onedataLossElecListMap, String type, List<Integer> setidList,
            Map<String, DeviceInfoObject> hashobj)
    {
        Map<String, Losstime> hash = new HashMap<>();
        // 如果光伏损失电量 并且 没有设定标杆机组 则没有损失电量
        if (hashobj.isEmpty())
        {
            return hash;
        }

        try
        {
            // 设置普通风机
            List<OneDataLossElec> onedataList = onedataLossElecListMap.get(device);
            if (onedataList != null)
            {
                for (OneDataLossElec bean : onedataList)
                {
                    List<ExpDataObject> listexp = getExpStr(type, bean.getProtocolId(),0);

                    int setid = DeviceHandleFunction.getCodeRusult(listexp, bean);
                    // 解决故障停机特殊情况 在wterrortotal情况下 只要在故障时间中都属于 故障 对应在onedata时间段中都属于故障 不管故障经过公式对应是什么 都属于故障
                    if (setidList.contains(setid))
                    {
                        Losstime obj = new Losstime(bean.getWtid(), setid, bean.getTheorypower(), bean.getRealpower(), 1, bean.getRealloselec(), 0);
                        obj.setElec(bean.getElec());
                        obj.setTime(bean.getRectime());
                        obj.setInveType(hashobj.get(bean.getWtid()).getInverType());
                        hash.put(bean.getRectime(), obj);
                    }
                }
            }

            // 设置标杆机
            Map<String, Losstime> bmark = new HashMap<>();
            if (bmarkList != null)
            {
                for (String bmarkID : bmarkList)
                {
                    if (bmarkID.equals(device))
                    {
                        continue;
                    }
                    List<OneDataLossElec> bmarkOnedataList = onedataLossElecListMap.get(bmarkID);
                    if (bmarkOnedataList != null)
                    {
                        for (OneDataLossElec bean : bmarkOnedataList)
                        {
                            String protocolid = BusinessFunc.getWtProtocolidByRectime(bean.getWtid(), bean.getRectime());
                            List<ExpDataObject> listexp = getExpStr(type, protocolid,0);
                            int setid = DeviceHandleFunction.getCodeRusult(listexp, bean);
                            // 说明是 标杆机组是正常发电量的
                            if (setid == -1)
                            {
                                if (bmark.get(bean.getRectime()) != null)
                                {
                                    Losstime obj = bmark.get(bean.getRectime());
                                    obj.setCount(obj.getCount() + 1);
                                    obj.setElec(obj.getElec() + bean.getElec());
                                }
                                else
                                {
                                    Losstime obj = new Losstime(device, setid, bean.getTheorypower(), bean.getRealpower(), 1, 0, 0);
                                    obj.setElec(bean.getElec());
                                    obj.setTime(bean.getRectime());
                                    obj.setWfidbmark(bean.getRealloselec());
                                    bmark.put(bean.getRectime(), obj);
                                }
                            }
                        }
                    }
                }
            }

            // 在判断一次
            // 处理 标杆机组 对象 标杆机组有的 可以 计算损失电量 如果没有的 不计算 直接 remove 分为主串式 集中
            for (Entry<String, Losstime> bmarkEntry : bmark.entrySet())
            {
                Losstime bmarkBean = bmarkEntry.getValue();
                if (hash.containsKey(bmarkEntry.getKey()))
                {
                    Losstime bean = hash.get(bmarkEntry.getKey());
                    bean.setBmrkloss((bmarkBean.getElec() / bmarkBean.getCount() - bean.getElec()));
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("DeviceHandle_" + ex.getMessage(), ex);
        }
        return hash;
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
            String strtemp = null;
            dbOper.openConn();
            StringBuffer str = new StringBuffer();
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
                DeviceInfoObject obj = null;
                if (bmarkdevice != null && bmarkdevice.equals(dt.getString("wtid")))
                {
                    obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"), "bmark");
                    strtemp = dt.getString("wtnarrate");
                }
                else
                {
                    obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"), "normal");
                }
                obj.setInverType(dt.getString("wtnarrate"));
                hash.put(dt.getString("wtid"), obj);
            }

            if (bmarkdevice != null)
            {
                String sql = "select * from config.wtinfo where wfid =(select wfid from config.wtinfo where wtid='" + device + "')  order by wtid";
                ResultSet dn = dbOper.getResultSet(sql, null);
                while (dn.next())
                {
                    String wfid = dn.getString("wfid");
                    String wtid = dn.getString("wtid");
                    String wtnarrate = dn.getString("wtnarrate");
                    if (!wtid.equals(device) && wtnarrate != null && wtnarrate.equals(strtemp))
                    {
                        DeviceInfoObject obj = new DeviceInfoObject(wfid, wtid, "normal");
                        obj.setInverType(dn.getString("wtnarrate"));
                        hash.put(wtid, obj);
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
                logger.error(e);
            }
        }
        return hash;
    }
}
