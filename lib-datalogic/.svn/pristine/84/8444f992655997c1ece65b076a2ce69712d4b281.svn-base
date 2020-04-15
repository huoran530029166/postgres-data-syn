package com.goldwind.datalogic.businesslogic.electricityloss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.business.model.ProtocolRuleBean;
import com.goldwind.datalogic.businesslogic.model.DeviceInfoObject;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;
import com.goldwind.datalogic.dsp.BaseConfig;
import com.goldwind.datalogic.utils.ControlProcessDef.RunState;
import com.goldwind.datalogic.businesslogic.util.DataTogetherFunction;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;

public class RunStateElectricityLoss extends ElectricityLoss
{

    /**
     * 所有协议叶片结冰损失电量类型
     */
    private HashMap<String, ProtocolRuleBean> protocolRuleHash;
    

    public RunStateElectricityLoss(DbOperBase dbOper, Map<String, List<ExpDataObject>> map)
    {
        super(dbOper, map);
        protocolRuleHash = getProtocolRuleHash();
    }

    /**
     * 新版运行状态字损失电量查询接口
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间转换 String 类型
        String start = sdf.format(starttime);
        String end = sdf.format(endtime);
        List<Losselec> losselecList = new ArrayList<>();
        try
        {
            // 损失电量类型判断
            if (setidList == null || setidList.isEmpty())
            {
                setidList = getSetIdList(type);
            }
            // 一分钟数据集合
            Map<String, List<OneDataLossElec>> onedataLossElecListMap = getOneDataLossElecListMap(wtidlist, start, end);
            for (String device : wtidlist)
            {
                Map<String, Losstime> hash = getDeviceTimeInfoNew(device, onedataLossElecListMap, type, setidList);// 所有的损失电量的 hashmap
                if (hash.size() != 0)
                {
                    Map<Integer, Losstime> mapdataobj = new HashMap<>(); // 损失电量对应对象聚合
                    List<Losstime> listdataobj = new ArrayList<>(); // 时间段对象集合
                    List<Losstime> listfalut = new ArrayList<>(); // 一分钟故障停机数据集合
                    if (together.equals(Together.DayTogether))
                    {
                        mapdataobj = DataTogetherFunction.getDayDataAgg(setidList, device, hash, listfalut);
                    }
                    else if (together.equals(Together.HourTogether))
                    {
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
                    if (mapdataobj.size() != 0 || !listfalut.isEmpty() || !listdataobj.isEmpty())
                    {
                        Losselec dataobj = new Losselec("", device, start, end);
                        dataobj.setEnergyUseDataHash(mapdataobj);
                        dataobj.setEnergyUseOneDataList(listfalut);
                        dataobj.setLosstimeList(listdataobj);
                        losselecList.add(dataobj);
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
        return losselecList;
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
     * @param typeID
     *            公式类型
     * @param setidList
     *            维护类型集合
     * @param hashobj
     *            对象设备
     * @return 返回时间对应duixiang
     */
    protected Map<String, Losstime> getDeviceTimeInfoNew(String device, Map<String, List<OneDataLossElec>> onedataLossElecListMap, String typeID, List<Integer> setidList)
    {
        Map<String, Losstime> hash = new HashMap<>();
        try
        {
            List<OneDataLossElec> onedataList = onedataLossElecListMap.get(device);
            if (null == onedataList || onedataList.isEmpty())
            {
                return hash;
            }
            for (OneDataLossElec bean : onedataList)
            {
                String protocolId = bean.getProtocolId();
                int standardpower = 0;
                // 叶片结冰模式字类型 1 停机模式字 2 运行状态字
                ProtocolRuleBean protocolExtendBean = getProtocolRuleBean(protocolId);
                if (null != protocolExtendBean)
                {
                    standardpower = protocolExtendBean.getStandardPower();
                }                                             
                List<ExpDataObject> listexp = getExpStr(typeID, bean.getProtocolId(), standardpower);
                int setid = DeviceHandleFunction.getCodeRusult(listexp, bean);
                if((bean.getEndelec()<bean.getBeginelec())
                        ||(standardpower>0 && BaseConfig.getWINDVALUENUM()>1 && bean.getElec() > standardpower * 1.0 / 60 * BaseConfig.getWINDVALUENUM())) 
                {
                    //发电量小于0或者发电量是否大于额定功率的1.5倍.该条数据的运行状态字置为0，状态等级置为12                
                    bean.setStopcode("0");
                    setid=RunState.IU.getResult();
                }                                
                if (setidList.contains(setid))
                {
                    Losstime obj = new Losstime(bean.getWtid(), setid, bean.getTheorypower(), bean.getRealpower(), 1, bean.getRealloselec(), 0);
                    obj.setElec(bean.getElec());
                    obj.setTime(bean.getRectime());
                    obj.setDeviceId(bean.getWtid());
                    hash.put(bean.getRectime(), obj);
                }              
            }
            return hash;
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
    protected Map<String, DeviceInfoObject> getDeviceObject(String device, String type)
    {
        Map<String, DeviceInfoObject> hash = new HashMap<>();
        try
        {
            // 得打 标杆机组
            String bmarkdevice = DeviceHandleFunction.getBmarkDeiveInfo(device, dbOper);

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
                DeviceInfoObject obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"), dt.getString("normalstate"));
                if (obj.getNomalstate().equals("bmark"))
                {
                    obj.setInverType(dt.getString("wtnarrate"));
                }
                else
                {
                    obj.setInverType(dt.getString("wtnarrate"));
                }
                hash.put(dt.getString("wtid"), obj);
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

    /**
     * 根据协议id判断叶片结冰模式字类型
     * 
     * @param protocolid
     * @return
     */
    private ProtocolRuleBean getProtocolRuleBean(String protocolid)
    {
        if (null == protocolRuleHash || !protocolRuleHash.containsKey(protocolid))
        {
            return null;
        }
        return protocolRuleHash.get(protocolid);
    }     
}
