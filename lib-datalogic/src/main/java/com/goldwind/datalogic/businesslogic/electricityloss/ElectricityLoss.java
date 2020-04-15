package com.goldwind.datalogic.businesslogic.electricityloss;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DateAsDef;
import com.goldwind.dataaccess.DynamicRun;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.StringUtil;
import com.goldwind.dataaccess.database.DatabaseHelper;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.FormulaClass;
import com.goldwind.datalogic.business.model.ChangeDataBean;
import com.goldwind.datalogic.business.model.DeviceInfo;
import com.goldwind.datalogic.business.model.FactorClass;
import com.goldwind.datalogic.business.model.ProtocolRuleBean;
import com.goldwind.datalogic.businesslogic.model.DeviceInfoObject;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;
import com.goldwind.datalogic.utils.MemoryData;
import com.goldwind.datalogic.utils.NetCommDef.WfDeviceType;

public abstract class ElectricityLoss
{
    /**
     * 带参数构造函数
     * 
     * @param dbOper
     */
    public ElectricityLoss(DbOperBase dbOper, Map<String, List<ExpDataObject>> map)
    {
        this.dbOper = dbOper;
        expHash = map;
    }

    /**
     * 输出日志
     */
    protected Log logger = Log.getLog(this.getClass());

    /**
     * 数据库操作对象
     */
    protected DbOperBase dbOper = null;

    /**
     * 公式容器
     */
    protected Map<String, List<ExpDataObject>> expHash = new HashMap<>();

    /**
     * 得到公式
     * 
     * @param vTypeId
     *            维护类型20风机，30PBA，40逆变器
     * @param vProtocolId
     *            协议
     * @return 返回公式
     */
    public List<ExpDataObject> getExpStr(String vTypeId, String vProtocolId, int vStandardPower)
    {
        List<ExpDataObject> expList = expHash.get(vTypeId);
        if (expList == null || expList.isEmpty())
        {
            return new ArrayList<>();
        }
        List<ExpDataObject> expDataObjectList = new ArrayList<>();
        for (ExpDataObject bean : expList)
        {
            expDataObjectList.add(new ExpDataObject(bean.getSetid(), bean.getFormula(), bean.getSort(),bean.getTypeid()));
        }
        for (ExpDataObject expDataObject : expDataObjectList)
        {
            ExpDataObject bean = getExpDataObjectByProtocolId(expDataObject.getSetid(), vProtocolId);
            if (null != bean)
            {
                // 利用协议配置损失电量公式
                expDataObject.setFormula(bean.getFormula());
            }
            else if (vStandardPower > 0)
            {
                bean = getExpByStandardpower(expDataObject.getSetid(), vStandardPower);
                if (null != bean)
                {
                    // 利用机型配置公式
                    expDataObject.setFormula(bean.getFormula());
                }
            }
        }
        expDataObjectList.sort(Comparator.comparing(ExpDataObject::getSetid));
        expDataObjectList.removeIf(o -> o.getSetid() != 2006 && !StringUtil.hasText(o.getFormula()));
        return expDataObjectList;
    }

    /**
     * 查找自定义公式
     * 
     * @param setId
     * @param protocolid
     * @return
     */
    protected ExpDataObject getExpByStandardpower(int setId, int standardpower)
    {
        List<ExpDataObject> beans = expHash.get("P" + standardpower);
        if (null != beans)
        {
            return beans.stream().filter(o -> o.getSetid() == setId).findAny().orElse(null);
        }
        return null;
    }

    /**
     * 查找自定义公式
     * 
     * @param setId
     * @param protocolid
     * @return
     */
    protected ExpDataObject getExpDataObjectByProtocolId(int setId, String protocolid)
    {
        List<ExpDataObject> beans = expHash.get(protocolid);
        if (null != beans)
        {
            return beans.stream().filter(o -> o.getSetid() == setId).findAny().orElse(null);
        }
        return null;
    }

    /**
     * 默认损失类型为空 是所有的 损失电量
     * 
     * @param typeID
     *            损失电量类型
     * @return 返回标识容器
     */
    protected List<Integer> getSetIdList(String typeID)
    {
        List<Integer> list = new ArrayList<>();
        try
        {
            dbOper.openConn();
            String sql = "select id from config.energyuse_config WHERE typeid =" + typeID + " order by sort";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                list.add(dt.getInt("id"));
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public List<String> getDeviceFalutTimeList(Date start, Date end, String wtid)
    {
        List<String> timeList = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM   PUBLIC.wterrortotal  WHERE totalflag = 1 AND wtid=@wtid");
        // 需要剔除电网故障
        sqlBuilder.append(" AND (get_wtprotocolrec_protocolid(wtid, starttime)||'_'||blooeydescr) not in (select protocolid||'_'||iecvalue from config.pathdescr where faulttype='ERROR_GRID')");
        sqlBuilder.append(" AND starttime < '@endtime'   AND ( endtime IS NULL OR endtime > '@begintime' ) ");
        sqlBuilder.append(" AND m_date_intersect ( starttime, endtime, '@begintime', '@endtime', 'second' ) >0 ");
        sqlBuilder.append(" ORDER BY wtid,    starttime");
        try
        {
            dbOper.openConn();
            String sql = sqlBuilder.toString().replace("@wtid", wtid).replace("@begintime", DateAsDef.dateToString(start)).replace("@endtime", DateAsDef.dateToString(end));
            ResultSet dq = dbOper.getResultSet(sql, null);
            while (dq.next())
            {
                // 读取到的故障 开始时间
                String timestart = dq.getString("starttime");
                // 读取故障结束时间
                String timeend = dq.getString("endtime");
                List<String> times = getFaultTimeList(start, end, timestart, timeend);
                timeList.addAll(times);

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
        return timeList;
    }

    /**
     * 多个wtid连接成串，以逗号分割
     * 
     * @param wtidList
     * @return
     */
    protected String getWtids(List<String> wtidList)
    {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < wtidList.size(); i++)
        {
            if (i > 0)
            {
                line.append(",");
            }
            line.append("'" + wtidList.get(i) + "'");
        }
        return line.toString();
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
    protected Map<String, DeviceInfoObject> getDeviceObjectNew(List<String> wtidList)
    {
        Map<String, DeviceInfoObject> hash = new HashMap<>();
        try
        {
            dbOper.openConn();
            StringBuilder str = new StringBuilder();
            str.append("select a.wfid,a.wtid,a.protocolid,b.normalstate,a.wtnarrate from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid ");
            if (null != wtidList && !wtidList.isEmpty())
            {
                str.append(" and a.wtid in (" + getWtids(wtidList) + ")");
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
     * 根据测试风机 得到 对应风机 风机对象和标杆风机对象
     * 
     * @param device
     *            设备id
     * @param type
     *            类型
     * @return 返回 wtid 对应对象
     */
    protected Map<String, DeviceInfoObject> getDeviceMapSameFarm(List<String> wtidList)
    {
        Map<String, DeviceInfoObject> hash = new HashMap<>();
        try
        {
            dbOper.openConn();
            StringBuilder str = new StringBuilder();
            str.append("select a.wfid,a.wtid,a.protocolid,b.normalstate,a.wtnarrate from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid ");
            if (null != wtidList && !wtidList.isEmpty())
            {
                str.append(" and a.wfid in ( select distinct wfid from config.wtinfo where wtid in (" + getWtids(wtidList) + "))");
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
     * 获得标杆机
     * 
     * @param wtidList
     * @return
     */
    public Map<String, String> getBmarkHash(List<String> wtidList)
    {
        HashMap<String, String> hash = new HashMap<>();
        if (null == wtidList || wtidList.isEmpty())
        {
            return hash;
        }
        if (wtidList.size() == 1)
        {
            String wtid = wtidList.get(0);
            String bmarkdevice = DeviceHandleFunction.getBmarkDeiveInfo(wtid, dbOper);
            if (null != bmarkdevice && !bmarkdevice.isEmpty())
            {
                hash.put(wtid, bmarkdevice);
            }
            return hash;
        }
        try
        {
            dbOper.openConn();
            String sql = "select wtid from config.bmarkdevice where bmid = 1 and endtime is null";
            ResultSet dtmrk = dbOper.getResultSet(sql, null);
            while (dtmrk.next())
            {
                String bmarkdevice = dtmrk.getString("wtid");
                if (wtidList.contains(bmarkdevice))
                {
                    hash.put(bmarkdevice, bmarkdevice);
                }
            }
            // 首先去确定风场有没有设定标杆机组 如果设定标杆机组 用当wtid 去得到当前的关联的标杆机组编号 如果wtid没有关联到标杆 就直接返回null 如果当前风场没有设定标杆机组就直接返回1
            sql = "select wtid,bmarkwtid from config.normaldevice where rectime is not null and endtime is null";
            ResultSet dtm = dbOper.getResultSet(sql, null);
            while (dtm.next())
            {
                String wtid = dtm.getString("wtid");
                String bmarkdevice = dtm.getString("bmarkwtid");
                if (wtidList.contains(wtid))
                {
                    hash.put(wtid, bmarkdevice);
                }
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
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
     * 获得同电场下所有的标杆机
     * 
     * @param wtidList
     * @return
     */
    protected Map<String, String> getBmarkHashAllFarm()
    {
        HashMap<String, String> hash = new HashMap<>();
        try
        {
            dbOper.openConn();
            String sql = "select wtid from config.bmarkdevice where bmid = 1 and endtime is null";
            ResultSet dtmrk = dbOper.getResultSet(sql, null);
            while (dtmrk.next())
            {
                String bmarkdevice = dtmrk.getString("wtid");
                hash.put(bmarkdevice, bmarkdevice);
            }
            // 首先去确定风场有没有设定标杆机组 如果设定标杆机组 用当wtid 去得到当前的关联的标杆机组编号 如果wtid没有关联到标杆 就直接返回null 如果当前风场没有设定标杆机组就直接返回1
            sql = "select wtid,bmarkwtid from config.normaldevice where rectime is not null and endtime is null";
            ResultSet dtm = dbOper.getResultSet(sql, null);
            while (dtm.next())
            {
                String wtid = dtm.getString("wtid");
                String bmarkdevice = dtm.getString("bmarkwtid");
                hash.put(wtid, bmarkdevice);
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
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
     * 根据设备id集合获取一分钟数据集合
     * 
     * @param wtidList
     * @return
     */
    protected Map<String, List<OneDataLossElec>> getOneDataLossElecListMap(List<String> wtidList, String start, String end)
    {
        HashMap<String, List<OneDataLossElec>> oneDataLossElecListHash = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select wtid,rectime,windspeed,envitemp,wtstatus,limitcode,faultcode,stopcode,limitstatus,beginelec,endelec,theorypower/60 as theorypower,realpower/60 as realpower, ");
        sqlBuilder.append(" (case when (beginelec > 0 and endelec > 0) then  (case when (((endelec - beginelec)) < 0) ");
        sqlBuilder.append(" then 0  else ((endelec - beginelec))   end)  else (theorypower / 60)  end ) as elec ,");
        sqlBuilder.append(" ((case when (beginelec > 0 and endelec > 0 and (endelec - beginelec) > 0) then  (case when (((theorypower / 60) - (endelec - beginelec)) < 0) ");
        sqlBuilder.append(" then 0  else ((theorypower / 60) -(endelec - beginelec))   end)  else (theorypower / 60)  end )) as losselc ");
        sqlBuilder.append(" from  onedata where (rectime >= '" + start + "' and  rectime < '" + end + "')");
        sqlBuilder.append(" and wtid in(" + getWtids(wtidList) + ")");
        sqlBuilder.append(" order by rectime");
        try
        {
            dbOper.openConn();
            ResultSet dt = dbOper.getResultSet(sqlBuilder.toString(), null);
            while (dt.next())
            {
                String wtid = dt.getString("wtid"); // 风机
                String rectime = dt.getString("rectime");
                OneDataLossElec bean = new OneDataLossElec(wtid, rectime);
                // 设置风速
                bean.setWindspeed(ArrayOper.formatDouble2(dt.getDouble("windspeed")));
                // 设置环境温度
                bean.setEnvitemp(ArrayOper.formatDouble2(dt.getDouble("envitemp")));
                // 理论发电量
                bean.setTheorypower(ArrayOper.formatDouble2(dt.getDouble("theorypower")));
                // 实际发电量
                bean.setRealpower(ArrayOper.formatDouble2(dt.getDouble("realpower")));
                // 实际电量
                bean.setElec(ArrayOper.formatDouble2(dt.getDouble("elec")));
                bean.setRealloselec(ArrayOper.formatDouble2(dt.getDouble("losselc")));
                bean.setBeginelec(ArrayOper.formatDouble2(dt.getDouble("beginelec")));
                bean.setEndelec(ArrayOper.formatDouble2(dt.getDouble("endelec"))); 
                bean.setWtstatus(dt.getString("wtstatus"));
                bean.setLimitstatus(dt.getString("limitstatus"));
                bean.setFaultcode(dt.getString("faultcode"));
                bean.setLimitcode(dt.getString("limitcode"));
                bean.setStopcode(dt.getString("stopcode"));

                if (oneDataLossElecListHash.containsKey(wtid))
                {
                    List<OneDataLossElec> list = oneDataLossElecListHash.get(wtid);
                    list.add(bean);
                }
                else
                {
                    List<OneDataLossElec> list = new ArrayList<OneDataLossElec>();
                    list.add(bean);
                    oneDataLossElecListHash.put(wtid, list);
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
        return oneDataLossElecListHash;
    }

    /**
     * 得到所有风机的故障时间集合
     * 
     * @param dbOper
     * @param wtidList
     * @param start
     * @param end
     * @return
     */
    public Map<String, List<String>> getFaultTimeListHash(List<String> wtidList, Date start, Date end)
    {

        HashMap<String, List<String>> faultTimeListHash = new HashMap<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM   PUBLIC.wterrortotal  WHERE totalflag = 1 and blooeydescr<>'0' AND wtid in (@wtid)");
        // 需要剔除电网故障
        sqlBuilder.append(" AND (get_wtprotocolrec_protocolid(wtid,starttime)||'_'||blooeydescr) not in (select protocolid||'_'||iecvalue from config.pathdescr where faulttype='ERROR_GRID')");
        sqlBuilder.append(" AND starttime < '@endtime'   AND ( endtime IS NULL OR endtime > '@begintime' ) ");
        sqlBuilder.append(" AND m_date_intersect ( starttime, endtime, '@begintime', '@endtime', 'second' ) >0 ");
        sqlBuilder.append(" ORDER BY wtid, starttime");
        try
        {
            dbOper.openConn();
            String sql = sqlBuilder.toString().replace("@wtid", getWtids(wtidList)).replace("@begintime", DateAsDef.dateToString(start)).replace("@endtime", DateAsDef.dateToString(end));
            ResultSet dq = dbOper.getResultSet(sql, null);
            while (dq.next())
            {
                String wtid = dq.getString("wtid");
                String timestart = dq.getString("starttime"); // 读取到的 开始时间
                String timeend = dq.getString("endtime"); // 读取结束时间
                List<String> timeList = getFaultTimeList(start, end, timestart, timeend);
                if (faultTimeListHash.containsKey(wtid))
                {
                    faultTimeListHash.get(wtid).addAll(timeList);
                }
                else
                {
                    faultTimeListHash.put(wtid, timeList);
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
        return faultTimeListHash;
    }

    /**
     * 获取风机故障时长
     * 
     * @param dbOper
     * @param wtidList
     * @param start
     * @param end
     * @return
     */
    protected double getFaultTimeLength(String wtid, Date start, Date end)
    {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(
                "SELECT m_date_intersect ( starttime, endtime, '@begintime', '@endtime', 'second' ) as timespan,* FROM   PUBLIC.wterrortotal  WHERE totalflag = 1 and blooeydescr<>'0' AND wtid =@wtid");
        // 需要剔除电网故障
        sqlBuilder.append(" AND (get_wtprotocolrec_protocolid(wtid,starttime)||'_'||blooeydescr) not in (select protocolid||'_'||iecvalue from config.pathdescr where faulttype='ERROR_GRID')");
        sqlBuilder.append(" AND starttime < '@endtime'   AND ( endtime IS NULL OR endtime > '@begintime' ) ");
        sqlBuilder.append(" AND m_date_intersect ( starttime, endtime, '@begintime', '@endtime', 'second' ) >0 ");
        sqlBuilder.append(" ORDER BY wtid, starttime");
        try
        {
            dbOper.openConn();
            String sql = sqlBuilder.toString().replace("@wtid", wtid).replace("@begintime", DateAsDef.dateToString(start)).replace("@endtime", DateAsDef.dateToString(end));
            ResultSet dq = dbOper.getResultSet(sql, null);
            double sumTimeSpan = 0.0D;
            while (dq.next())
            {
                sumTimeSpan += dq.getDouble("timespan");
            }
            return sumTimeSpan / 60.0;
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
        return 0.0D;
    }

    /**
     * 根据统计时间，故障时间，获得故障时间落在统计时间内的一分钟集合 xjs 2020-1-2 22:39:25 完成重构
     * 
     * @param vStartDate
     * @param vEndDate
     * @param timestart
     * @param timeend
     * @return
     * @throws ParseException
     * @throws DataAsException
     */
    public List<String> getFaultTimeList(Date vStartDate, Date vEndDate, String timestart, String timeend) throws ParseException, DataAsException
    {
        // 处理故障开始时间，如果小于统计开始时间，则以统计开始时间为准
        Calendar faultStartCalendar = Calendar.getInstance();
        faultStartCalendar.setTime(DateAsDef.parseDate(timestart));
        if (faultStartCalendar.getTimeInMillis() < vStartDate.getTime())
        {
            faultStartCalendar.setTime(vStartDate);
        }
        // 处理故障结束时间，如果为null或者大于统计结束时间，则以统计结束时间为准
        Calendar faultEndCalendar = Calendar.getInstance();
        if (timeend == null || DateAsDef.parseDate(timeend).compareTo(vEndDate) > 0)
        {
            faultEndCalendar.setTime(vEndDate);
        }
        else
        {
            // 读取结束时间
            faultEndCalendar.setTime(DateAsDef.parseDate(timeend));
        }
        TimeCalendar obj = new TimeCalendar(faultStartCalendar, faultEndCalendar);
        List<String> oneMiniteList = new ArrayList<>();
        while (obj.getEndtime().compareTo(obj.getStartime()) >= 0)
        {
            String rectime = DateAsDef.dateToString(obj.getStartime().getTime(), DataAsDef.DATETIMEFORMATSEDSTR);
            oneMiniteList.add(rectime);
            obj.getStartime().add(Calendar.MINUTE, 1);
        }
        return oneMiniteList;
    }

    /**
     * 抽取所有公式中用到的变位数据iecpath
     * 
     * @return
     */
    public List<String> getChangeDataIecPathList()
    {
        List<String> listdev = new ArrayList<>(Arrays.asList("Program.StopStatusType", "WTUR.TurSt.Rs.S", "WTUR.Other.Rs.S.LitPowByPLC", "WTUR.Flt.Rs.S", "WTUR.Other.Rs.S.LitPowByState",
                "WTUR.Bool.Rd.b1.LimPowStopState", "WTUR.Other.Wn.I16.StopModeWord", "WTUR.Temp.Ra.F32"));
        return expHash.values().stream().flatMap(List::stream).filter(o -> o.getSetid() < 3000).flatMap(o -> DynamicRun.getAllExprIecpaths(o.getFormula()).stream()).filter(o -> !listdev.contains(o))
                .distinct().collect(Collectors.toList());
    }
    

    /**
     * 获得单个风机的changedata数据
     * 
     * @param wtid
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<ChangeDataBean> getChangeDataBeans(List<String> changeDataIecpathList, String wtid, Date beginDate, Date endDate)
    {
        List<ChangeDataBean> beans = new ArrayList<>();
        try
        {
            // 变位表不存在，不查询
            if (!DatabaseHelper.checkTableExsit(dbOper, "changedata_" + wtid))
            {
                return beans;
            }
            String sql = "select * from change.changedata_@wtid  where rectime>'2019-01-01' and  qualityflag=1"
                    + " AND rectime < '@endtime'   AND ( endtime IS NULL OR endtime > '@begintime' ) and m_date_intersect ( rectime, endtime, '@begintime', '@endtime', 'second' ) >0";
            if (!changeDataIecpathList.isEmpty())
            {
                sql += " and iecpath in (" + getWtids(changeDataIecpathList) + ")";
            }
            sql += " order by rectime desc";
            sql = sql.replace("@wtid", wtid).replace("@begintime", DateAsDef.dateToString(beginDate)).replace("@endtime", DateAsDef.dateToString(endDate));
            dbOper.openConn();
            ResultSet dq = dbOper.getResultSet(sql, null);
            while (dq.next())
            {
                String timestart = dq.getString("rectime"); // 读取到的 开始时间
                String timeend = dq.getString("endtime"); // 读取结束时间
                ChangeDataBean bean = new ChangeDataBean();
                bean.setWtid(dq.getInt("wtid"));
                bean.setIecpath(dq.getString("iecpath"));
                bean.setRectime(timestart);
                bean.setEndtime(timeend);
                bean.setChangedata(dq.getString("changedata"));
                List<String> timeList = getFaultTimeList(beginDate, endDate, timestart, timeend);
                bean.getMineteList().addAll(timeList);
                beans.add(bean);
            }
        }
        catch (Exception ex)
        {
            logger.error("getChangeDataBeans" + ex.getMessage(), ex);
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
        return beans;
    }

    /**
     * 读取所有的协议的叶片结冰模式字判断类型 1 停机模式字 2 运行状态字
     * 
     * @return
     */
    protected HashMap<String, ProtocolRuleBean> getProtocolRuleHash()
    {
        HashMap<String, ProtocolRuleBean> hash = new HashMap<>();
        try
        {
            String sql = "select protocolid,stopstatustype,standardpower from config.protocolrule order by protocolid";
            List<Map<String, Object>> maps = DatabaseHelper.resultSetToList(sql, null);
            for (Map<String, Object> map : maps)
            {
                String protocolid = DatabaseHelper.getString(map, "protocolid");
                ProtocolRuleBean bean = new ProtocolRuleBean();
                bean.setProtocolId(DatabaseHelper.getInt(map, "protocolid"));
                bean.setStandardPower(DatabaseHelper.getInt(map, "standardpower"));
                bean.setStopStatusTtype(DatabaseHelper.getInt(map, "stopstatustype"));
                hash.put(protocolid, bean);
            }
        }
        catch (Exception e)
        {
            logger.error("getProtocolRuleHash" + e.toString());
        }
        return hash;
    }  
    
    /**
     * 获得同电场下指定类型的设备id集合
     * xjs 2020-4-8 11:39:35
     * @param wfid
     * @param wtid
     * @param deviceType
     * @return
     */
    public  List<DeviceInfo>getDeviceIdListByWtid(String wtid,int deviceType)
    {
       DeviceInfo info=MemoryData.getDeviceInfoList().get(wtid);       
       return MemoryData.getDeviceInfoList(WfDeviceType.valueOf(deviceType))
               .stream()
               .filter(o ->o.getWfId().equals(info.getWfId()))
               .sorted((x,y)->y.getId().compareTo(x.getId()))
               .collect(Collectors.toList());       
    }
    
    /**
     * 获得同电场下指定类型的设备id集合
     * xjs 2020-4-8 11:39:35
     * @param wfid
     * @param wtid
     * @param deviceType
     * @return
     */
    public  List<DeviceInfo>getDeviceIdListByWfid(String wfid,int deviceType)
    {      
       return MemoryData.getDeviceInfoList(WfDeviceType.valueOf(deviceType))
               .stream()
               .filter(o ->o.getWfId().equals(wfid))
               .sorted((x,y)->y.getId().compareTo(x.getId()))
               .collect(Collectors.toList());       
    }
    
    
    
    /**
     * 获得同一电场下指定设备类型指定Iecpath的变位数据
     * @param wfid
     * @param iecPath
     * @param deviceType
     * @param start
     * @param end
     * @return
     */
    public Map<String, List<ChangeDataBean>> getChangeDataHash(String wfid,String iecPath,int deviceType, Date start, Date end)
    {
        Map<String, List<ChangeDataBean>> hashMap = new HashMap<>();
        List<DeviceInfo>deviceInfos=getDeviceIdListByWfid(wfid, deviceType);
        for (DeviceInfo oneDevice : deviceInfos)
        {
            String key=oneDevice.getId()+"_"+iecPath;  
            List<ChangeDataBean> changeDataBeans=getChangeDataBeans(dbOper, iecPath, oneDevice.getId(), start, end);                            
            if (!hashMap.containsKey(key) && !changeDataBeans.isEmpty() )
            {
                hashMap.put(key, changeDataBeans);    
            } 
        }        
        return hashMap;
    }
    
    
    /**
     * 获得一组changedata数据
     * 
     * @param wtidList
     * @param start
     * @param end
     * @return
     */
    public Map<String, List<ChangeDataBean>> getChangeDataHash(int vTypeId,List<String> wtidList, Date start, Date end)
    {
        List<FactorClass> factorClasses=getFactorClassList(vTypeId);
        HashMap<String, List<ChangeDataBean>> hashMap = new HashMap<>();
        for (String wtid : wtidList)
        {
            for (FactorClass factorClass : factorClasses)
            {                              
                switch (factorClass.getFactorType())
                {
                    //0_设备ID:IEC路径
                    case SingleDevice:       
                    {
                        String key=wtid+"_"+factorClass.getIecPath();  
                        List<ChangeDataBean> changeDataBeans=getChangeDataBeans(dbOper, factorClass.getIecPath(), wtid, start, end);
                        if (!hashMap.containsKey(key) && !changeDataBeans.isEmpty() )
                        {
                            hashMap.put(key, changeDataBeans);    
                        } 
                    }
                        break;
                    case MultipleDevices:   
                    {
                        List<DeviceInfo>deviceList=getDeviceIdListByWtid( wtid, factorClass.getDeviceType());
                        for (DeviceInfo oneDevice : deviceList)
                        {
                            String key=oneDevice.getId()+"_"+factorClass.getIecPath();  
                            List<ChangeDataBean> changeDataBeans=getChangeDataBeans(dbOper, factorClass.getIecPath(), oneDevice.getId(), start, end);                            
                            if (!hashMap.containsKey(key) && !changeDataBeans.isEmpty() )
                            {
                                hashMap.put(key, changeDataBeans);    
                            } 
                        }
                    }
                    break;
                    case IECCOMPUTE:  
                    {
                        String key=wtid+"_"+factorClass.getIecPath();  
                        List<ChangeDataBean> changeDataBeans=getChangeDataBeans(dbOper, factorClass.getIecPath(), wtid, start, end);
                        if (!hashMap.containsKey(key) && !changeDataBeans.isEmpty() )
                        {
                            hashMap.put(key, changeDataBeans);    
                        }                        
                    }                      
                        break;
                    default:
                        break;
                }               
            }                       
        }
        return hashMap;
    }  
    
    /**
     * 获得单个风机的changedata数据
     * 
     * @param wtid
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<ChangeDataBean> getChangeDataBeans(DbOperBase dbOper,String vIecPath, String wtid, Date beginDate, Date endDate)
    {
        List<ChangeDataBean> beans = new ArrayList<>();
        try
        {
            // 变位表不存在，不查询
            if (!DatabaseHelper.checkTableExsit(dbOper, "changedata_" + wtid))
            {
                return beans;
            }
            String sql = "select * from change.changedata_@wtid  where rectime>'2019-01-01' and  qualityflag=1"
                    + " AND rectime < '@endtime'   AND ( endtime IS NULL OR endtime > '@begintime' ) and m_date_intersect ( rectime, endtime, '@begintime', '@endtime', 'second' ) >0";
            sql += " and iecpath ='" + vIecPath + "'";            
            sql += " order by rectime desc";
            sql = sql.replace("@wtid", wtid).replace("@begintime", DateAsDef.dateToString(beginDate)).replace("@endtime", DateAsDef.dateToString(endDate));
            dbOper.openConn();
            ResultSet dq = dbOper.getResultSet(sql, null);
            while (dq.next())
            {
                String timestart = dq.getString("rectime"); // 读取到的 开始时间
                String timeend = dq.getString("endtime"); // 读取结束时间
                ChangeDataBean bean = new ChangeDataBean();
                bean.setWtid(dq.getInt("wtid"));
                bean.setIecpath(dq.getString("iecpath"));
                bean.setRectime(timestart);
                bean.setEndtime(timeend);
                bean.setChangedata(dq.getString("changedata"));
                List<String> timeList = getFaultTimeList(beginDate, endDate, timestart, timeend);
                bean.getMineteList().addAll(timeList);
                beans.add(bean);
            }
        }
        catch (Exception ex)
        {
            logger.error("getChangeDataBeans" + ex.getMessage(), ex);
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
        return beans;
    }
    
    /**
     * 抽取所有公式中用到的变位数据
     * 
     * @return
     */
    public List<FactorClass> getFactorClassList(int vTypeID)
    {
       List<ExpDataObject>formulaDataExpObjects= expHash.values().stream().flatMap(List::stream).filter(o -> o.getTypeid()==vTypeID).collect(Collectors.toList());
       List<String> listdev = new ArrayList<>(Arrays.asList("Program.StopStatusType", "WTUR.TurSt.Rs.S", "WTUR.Other.Rs.S.LitPowByPLC", "WTUR.Flt.Rs.S", "WTUR.Other.Rs.S.LitPowByState",
                "WTUR.Bool.Rd.b1.LimPowStopState", "WTUR.Other.Wn.I16.StopModeWord", "WTUR.Temp.Ra.F32"));        
       List<FactorClass> factorClasses= DeviceHandleFunction.getFactorClassList(formulaDataExpObjects);
       return factorClasses.stream().filter(o -> !listdev.contains(o.getIecPath())).distinct().collect(Collectors.toList());
    }
    
    /**
     * 公式结果处理
     * @param expList
     * @param oneDataBean
     * @param stopStatusType
     * @return
     */
    public  int getCodeRusult(List<ExpDataObject> expList, OneDataLossElec oneDataBean,Map<String, List<ChangeDataBean>>changeDataMap, int stopStatusType)
    {
        // 判断公式集合中 没有公式 没有公式 统一 返回 -1
        if (null == expList || expList.isEmpty())
        {
            return -1;
        }
        for (ExpDataObject obj : expList)
        {
            // 根据公式 看对象数据 是否 满足 公式 满足 返回 对应标示
            String resulttemp = DeviceHandleFunction.getExpString(obj.getFormula(),oneDataBean); 
            resulttemp = resulttemp.replace("@Program.StopStatusType", stopStatusType+"");
            resulttemp=resulttemp.replace("，", ",");    
            if (resulttemp.indexOf("DeviceCommState") < 0 && resulttemp.indexOf("alam") < 0)
            {                
                try
                {
                    //这里要将多个变位表数据进行替换       
                    FormulaClass formula = new FormulaClass(resulttemp);
                    FactorClass[] factors = formula.getFactorArray();
                    for (FactorClass factorClass : factors)
                    {
                        String value=  getFactorClassValue(oneDataBean, changeDataMap, factorClass);
                        if (StringUtil.hasText(value))
                        {
                            resulttemp = resulttemp.replace(factorClass.getFormula(), value); 
                        } 
                    }                                    
                    
                    boolean bresult = DynamicRun.logicExpression(resulttemp);
                    if (bresult)
                    {
                        return obj.getSetid();
                    }
                }
                catch (DataAsException e)
                {
                   logger.error("ElectricityLoss_getCodeRusult_"+resulttemp,e);
                }
            }         
        }
        return -1;
    }

    /**
     * 子公式用变位值替换
     * @param oneDataBean
     * @param changeDataMap
     * @param factorClass
     * @return
     */
    public String getFactorClassValue(OneDataLossElec oneDataBean, Map<String, List<ChangeDataBean>> changeDataMap,FactorClass factorClass) 
    {
        switch (factorClass.getFactorType()) 
        {
            case SingleDevice:
            {
                return  getSingleDevice(oneDataBean, changeDataMap, factorClass);                  
            }                                                       
            case MultipleDevices:
            {     
                return getMultipleDevices(factorClass, oneDataBean, changeDataMap);                                                      
            }                                            
            case IECCOMPUTE:
            {
                return  getIECCOMPUTE(oneDataBean, changeDataMap, factorClass);                
            }             
        }
        return "";
    }
    
    
    /**
     * 旧版公式配置
     * @param oneDataBean
     * @param changeDataMap
     * @param resulttemp
     * @param factorClass
     * @return
     */
    public String getIECCOMPUTE(OneDataLossElec oneDataBean, Map<String, List<ChangeDataBean>> changeDataMap,FactorClass factorClass)
    {
        String key=oneDataBean.getWtid()+"_"+factorClass.getIecPath();
        if (changeDataMap.containsKey(key))
        {
            List<ChangeDataBean>changeDataBeans=changeDataMap.get(key);
            ChangeDataBean bean=changeDataBeans.stream().filter(o->key.equals(o.getIecpath())&&o.getMineteList().contains(oneDataBean.getRectime())).findFirst().orElse(null);
            if (null!=bean)
            {
                return bean.getChangedata();  
            }
        }
        return "";
    }
    
    /**
     * 单设备公式配置，规则如下{0_14080201:FHX.Bool.Rd.b0.0502}>0
     * @param oneDataBean
     * @param changeDataMap
     * @param resulttemp
     * @param factorClass
     * @return
     */
    public String getSingleDevice(OneDataLossElec oneDataBean, Map<String, List<ChangeDataBean>> changeDataMap,FactorClass factorClass)
    {
        String key=oneDataBean.getWtid()+"_"+factorClass.getIecPath();
        if (changeDataMap.containsKey(key))
        {
            List<ChangeDataBean>changeDataBeans=changeDataMap.get(key);
            ChangeDataBean bean=changeDataBeans.stream().filter(o->key.equals(o.getIecpath())&&o.getMineteList().contains(oneDataBean.getRectime())).findFirst().orElse(null);
            if (null!=bean)
            {
                return bean.getChangedata();  
            }
        }
        return "";
    }
    
    /**
     *设备计算：0_设备ID1,设备ID2:设备类型:IEC路径:聚合方式，需要兼容目前的设备ID:IEC路径，如果是单设备，聚合方式可以为空。
     *组织计算：1_组织编号:设备类型:IEC路径:聚合方式，其中组织编号为0，表示系统，设备类型为-1，表示所有设备类型，聚合方式有：SUM、AVG等
     *损失电量公式，暂时不做聚合解析。只取同设备类型中最大的设备的值
     *xjs 2020-4-9 09:16:52
     * @param factorClass
     * @param oneDataBean
     * @param changeDataMap
     * @return
     */
    public String getMultipleDevices(FactorClass factorClass,OneDataLossElec oneDataBean,Map<String, List<ChangeDataBean>>changeDataMap) 
    {
        List<DeviceInfo>deviceInfos=getDeviceIdListByWtid(oneDataBean.getWtid(), factorClass.getDeviceType());        
        for (DeviceInfo deviceInfo : deviceInfos)
        {
            String key=deviceInfo.getId()+"_"+factorClass.getIecPath();
            if (changeDataMap.containsKey(key))
            {
                List<ChangeDataBean>changeDataBeans=changeDataMap.get(key);
                ChangeDataBean bean=changeDataBeans.stream().filter(o->key.equals(o.getIecpath())&&o.getMineteList().contains(oneDataBean.getRectime())).findFirst().orElse(null);
                if (null!=bean)
                {
                    return bean.getChangedata();
                }
            }
        }      
        return "";
    }
}
