package com.goldwind.datalogic.businesslogic.eba;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.businesslogic.model.EbaExampLoss;
import com.goldwind.datalogic.businesslogic.model.ExpDataObject;
import com.goldwind.datalogic.businesslogic.model.OneDataLossElec;
import com.goldwind.datalogic.businesslogic.model.WfinfoData;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;
import com.goldwind.datalogic.utils.ControlProcessDef;
import com.goldwind.datalogic.utils.ControlProcessDef.DeviceChangeType;
import com.goldwind.datalogic.utils.ControlProcessDef.RunState;

/**
 * MS 美国电场EBA 算法实现
 * 
 * @author 谭璟
 *
 */
public class MsEba extends AbEbaLossHandle
{
    /**
     * 日志对象
     */
    private static Log logger = Log.getLog(MsEba.class);

    /**
     * 需要对象
     */
    private EbaObject object = null;

    /**
     * rectime常量
     */
    private String rectimeStr = "rectime";

    /**
     * endelec常量
     */
    private String endelecStr = "endelec";
    /**
     * beginelec常量
     */
    private String beginelecStr = "beginelec";

    /**
     * 实现具体逻辑方法
     * 
     * @throws Exception
     */
    @Override
    protected void getDeivceLossHandle() throws Exception
    {
        // 得到风场下 所有风机的基本信息
        WfinfoData ob = getDeviceWfid();

        // 根据flag 得到对应公式
        List<ExpDataObject> list = getExpData();

        // 如果没有配置公式 直接return
        if (!list.isEmpty())
        {
            List<EbaExampLoss> li = getDeivceHandle(list);

            // 如果都正常发电不需要计算
            if (!li.isEmpty())
            {
                for (EbaExampLoss obj : li)
                {
                    // Eba算法
                    getHandle(obj, list, ob);
                }

                List<String> listsql = new ArrayList<>();
                for (EbaExampLoss objext : li)
                {
                    if (objext.getFlag() != DeviceChangeType.plannocal.getResult())
                    {
                        String delsql = "delete from eba_onedata_losselec where wtid = '" + objext.getWtid() + "' and rectime ='" + objext.getRectime() + "'";
                        String sql = "insert into eba_onedata_losselec values ('" + objext.getWtid() + "','" + objext.getRectime() + "','";
                        sql += objext.getStopcode() + "','" + objext.getRespid() + "','" + objext.getLosselec() + "','" + objext.getFlag() + "')";

                        listsql.add(delsql);
                        listsql.add(sql);
                    }
                }

                // 入数据库
                getObjectToSql(listsql);
            }
        }
    }

    /**
     * 得到风机 所在的风场
     * 
     * @return 返回风场
     * @throws SQLException
     *             异常信息
     */
    private WfinfoData getDeviceWfid() throws SQLException
    {
        WfinfoData obj = null;
        try
        {
            List<String> listwind = new ArrayList<>();
            object.getDbOper().openConn();
            String sql = null;
            obj = new WfinfoData(object.getWfId());
            // 判断风场不为空的情况下
            if (StringUtils.isNotBlank(obj.getWfid()))
            {
                // 得到测风塔
                sql = "select b.wtid from config.wttypeinfo a, config.wtinfo b where b.wfid = ' " + obj.getWfid() + "' and b.protocolid = a.protocolid and a.devicetype = 2";
                ResultSet ds = object.getDbOper().getResultSet(sql, null);
                while (ds.next())
                {
                    listwind.add(ds.getString("wtid"));
                }
                obj.setWindTowerList(listwind);

                List<String> listwtid = new ArrayList<>();
                // key：风机id value：协议
                Map<String, String> result = new HashMap<>();
                Map<String, Double> wtidpower = new HashMap<>();
                // 得到 风场下的所有 风机 和 风机对应的协议
                sql = "select a.wfid,a.wtid,a.protocolid,b.normalstate,b.standardpower from config.wtinfo a,config.wttypeinfo b where a.protocolid=b.protocolid  and a.wfid= '" + obj.getWfid()
                        + "' and devicetype = 0";
                sql += " order by wtid";
                ResultSet dn = object.getDbOper().getResultSet(sql, null);
                while (dn.next())
                {
                    listwtid.add(dn.getString("wtid"));
                    result.put(dn.getString("wtid"), dn.getString("protocolid"));
                    wtidpower.put(dn.getString("wtid"), dn.getDouble("standardpower"));
                }
                obj.setWtidProtocolidHash(result);
                obj.setWindTurbineList(listwtid);
                obj.setWtidpower(wtidpower);
            }
        }
        catch (Exception ex)
        {
            logger.error("RsEba_getDeviceWfid", ex);
        }
        finally
        {
            object.getDbOper().closeConn();
        }
        return obj;
    }

    /**
     * 得到flag中公式
     * 
     * @return 返回公式
     * @throws SQLException
     *             异常
     */
    private List<ExpDataObject> getExpData() throws SQLException
    {
        List<ExpDataObject> list = new ArrayList<>();
        try
        {
            object.getDbOper().openConn();

            // 读取完协议公式 读取默认公式 如果修改为 基于 运行状态字的公式 只能走总公式 不能走协议公式 如果是原来的公式 先协议 后走 总公式
            StringBuffer buffer = new StringBuffer();
            buffer.append("select id,expdescr,sort,typeid from config.energyuse_config where isenabled = 1 order by sort ");
            ResultSet ds = object.getDbOper().getResultSet(buffer.toString(), null);
            while (ds.next())
            {
                if (ds.getString("typeid").equals(object.getFlag()))
                {
                    ExpDataObject obj = new ExpDataObject(ds.getInt("id"), ds.getString("expdescr"), ds.getInt("sort"),ds.getInt("typeid"));
                    list.add(obj);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("RsEba_getExpData", ex);
        }
        finally
        {
            object.getDbOper().closeConn();
        }
        return list;
    }

    /**
     * 得到有问题数据(Eba)
     * 
     * @param list
     *            公式
     * @throws Exception
     *             异常
     * 
     * @return 返回集合
     */
    private List<EbaExampLoss> getDeivceHandle(List<ExpDataObject> list) throws Exception
    {
        List<EbaExampLoss> li = new ArrayList<>();
        try
        {
            // "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            String starttime = sdf.format(object.getStart());
            String endtime = sdf.format(object.getEnd());
            object.getDbOper().openConn();

            String sql = "select * from onedata where rectime between '" + starttime + "' and '" + endtime + "' and wtid = '" + object.getDevice() + "'  order by rectime";
            ResultSet dt = object.getDbOper().getResultSet(sql, null);
            while (dt.next())
            {
                int setid = -1;
                String wtstaute = dt.getString("wtstatus");
                String limitstatus = dt.getString("limitstatus");
                String faultcode = dt.getString("faultcode");
                String stopcode = dt.getString("stopcode");
                String limitcode = dt.getString("limitcode");
                double windspeed = dt.getDouble("windspeed");
                int defresipid = dt.getInt("defresipid"); // 默认责任人
                double losselec = dt.getDouble("theorypower") / 60; // 理论功率

                OneDataLossElec bean = new OneDataLossElec(dt.getString("wtid"), dt.getString(rectimeStr));
                bean.setWtstatus(wtstaute);
                bean.setLimitstatus(limitstatus);
                bean.setFaultcode(faultcode);
                bean.setLimitcode(limitcode);
                bean.setStopcode(stopcode);

                setid = DeviceHandleFunction.getCodeRusult(list, bean);

                if (defresipid != 9)
                {
                    if (setid != RunState.IAOGFP.getResult())
                    {
                        if (setid != RunState.IU.getResult())
                        {
                            EbaExampLoss obj = new EbaExampLoss(object.getDevice(), dt.getString(rectimeStr), Integer.valueOf(stopcode), 0.0);
                            obj.setWindfalutcode(faultcode);
                            obj.setWindspeed(windspeed);
                            obj.setRespid(defresipid);
                            obj.setLosselec(losselec);
                            obj.setKind((dt.getDouble(endelecStr) - dt.getDouble(beginelecStr)) > 0 ? (dt.getDouble(endelecStr) - dt.getDouble(beginelecStr)) : 0);
                            li.add(obj);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("RsEba_getDeivceHandle", ex);
        }
        finally
        {
            object.getDbOper().closeConn();
        }
        return li;
    }

    /**
     * 处理对应时间的数据
     * 
     * @param obj
     *            当前数据对象
     * @param list
     *            公式
     * @param re
     *            数量
     * @throws Exception
     *             异常
     */
    private void getHandle(EbaExampLoss obj, List<ExpDataObject> list, WfinfoData re) throws Exception
    {
        try
        {
            // 正常发电量
            double real = 0.0;
            // 正常发电量台数
            int flag = 0;
            // 限功率发电量台数
            // int nflag = 0;

            object.getDbOper().openConn();
            // 查询本风场的其他风机的一分钟数据
            String sql = "select * from onedata where rectime = '" + obj.getRectime() + "' and wtid != '" + object.getDevice() + "' and wfid = '" + re.getWfid() + "' order by rectime";
            ResultSet dt = object.getDbOper().getResultSet(sql, null);
            while (dt.next())
            {
                int setid = -1;
                String wtstaute = dt.getString("wtstatus");
                String limitstatus = dt.getString("limitstatus");
                String faultcode = dt.getString("faultcode");
                String stopcode = dt.getString("stopcode");
                String limitcode = dt.getString("limitcode");
                int defresipid = dt.getInt("defresipid");

                OneDataLossElec bean = new OneDataLossElec(dt.getString("wtid"), dt.getString(rectimeStr));
                bean.setWtstatus(wtstaute);
                bean.setLimitstatus(limitstatus);
                bean.setFaultcode(faultcode);
                bean.setLimitcode(limitcode);
                bean.setStopcode(stopcode);
                setid = DeviceHandleFunction.getCodeRusult(list, bean);

                if (setid == RunState.IAOGFP.getResult() || defresipid == 9)
                {
                    flag += 1;
                    real += (dt.getDouble(endelecStr) - dt.getDouble(beginelecStr)) > 0 ? (dt.getDouble(endelecStr) - dt.getDouble(beginelecStr)) : 0;
                }
            }

            double theorypowerFromPowerCure = 0.0d;
            SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            // 将一分钟数据的时间转成十分钟数据时间
            String timestr = getDataTenMinute(obj.getRectime());
            Calendar ca = Calendar.getInstance();
            ca.setTime(sdf.parse(timestr));
            ca.add(Calendar.YEAR, -1);
            String rebtime = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(ca.getTime());

            // 说明有正常发电量风机 方案一：部分风机异常且不全是限功率状态
            if (flag > 0)
            {
                obj.setLosselec((real / flag - obj.getKind()) > 0 ? (real / flag - obj.getKind()) : 0);
                obj.setFlag(DeviceChangeType.plana.getResult());
            }
            else
            {
                // 查看是否属于测风塔故障（有测风塔故障说明没有机舱风速）
                if (obj.getWindfalutcode() != null && lifalut.contains(obj.getWindfalutcode()))
                {
                    // 查看历史同期是否有功率曲线数据，有的话就走方案三，没有的话就不进行计算
                    boolean result = getObjResult(obj, re.getWfid());
                    if (result)
                    {
                        obj.setFlag(DeviceChangeType.planc.getResult());
                    }
                    else
                    {
                        obj.setFlag(DeviceChangeType.plannocal.getResult());
                    }
                }
                else
                {
                    // 机舱风速有效时走方案二
                    obj.setLosselec((obj.getLosselec() - obj.getKind()) > 0 ? (obj.getLosselec() - obj.getKind()) : 0);
                    obj.setFlag(DeviceChangeType.planb.getResult());
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("MsEba_getHandle", ex);
        }
        finally
        {
            object.getDbOper().closeConn();
        }
    }

    /**
     * 判断当前时间段
     * 
     * @return 返回一个布尔类型
     * @throws DataAsException
     *             自定义异常
     * @throws SQLException
     *             异常
     */
    private boolean getDevicePower() throws DataAsException, SQLException
    {
        boolean result = false;
        try
        {
            String rectime = null;
            String maxtime = null;

            object.getDbOper().openConn();
            String sql = "select max(rectime) as maxtime ,min(rectime) as mintime from powcurve where wtid = '" + object.getDevice() + "'";
            ResultSet dt = object.getDbOper().getResultSet(sql, null);
            while (dt.next())
            {
                rectime = dt.getString("mintime");
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
                // "yyyy-MM-dd HH:mm:ss"
                SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
                Calendar cal = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(sdf.parse(rectime));
                cal.setTime(sdf.parse(maxtime));
                cal1.add(Calendar.YEAR, 1);

                // 最小时间 + 1年 大于 最大时间 说明 EBA 部署小于12个月
                if (cal1.compareTo(cal) <= 0)
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
            logger.error(ex.getMessage());
        }
        finally
        {
            object.getDbOper().closeConn();
        }
        return result;
    }

    /**
     * 最后一种方案处理
     * 
     * @param obj
     *            需要处理对象
     * @param wfid
     *            风场id
     * @throws Exception
     *             异常
     * @return 返回真/假 如果真计算损失电量 如果假直接走下一步
     */
    private boolean getObjResult(EbaExampLoss obj, String wfid) throws Exception
    {
        boolean result = false;
        try
        {
            // "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            double real = 0.0;
            object.getDbOper().openConn();
            String rectime = obj.getRectime();
            // 将一分钟数据的时间转成十分钟数据时间
            String timestr = getDataTenMinute(rectime);

            // 风机当前时间
            Calendar ca = Calendar.getInstance();
            Calendar camax = Calendar.getInstance();
            Calendar camin = Calendar.getInstance();
            ca.setTime(sdf.parse(timestr));

            // powercure的最小时间
            String mintime = null;
            // powercure的最大时间
            String maxtime = null;
            // 同期的风速
            String winSpeed = null;
            // 获取到风速的时间
            String rebtime = null;
            // 循环判断 当前时间 前一年此时数据 如果没有 在往前推一年时间 特殊处理2月 因为存在 28和29号 如果 没有29号 取28号的数据 还有一个最小时间 首先得到最小时间 然后在往前推一年时间
            String bf = "select min(rectime) as mintime ,max(rectime) as maxtime from powcurve where wtid = \'" + obj.getWtid() + "\'";
            ResultSet dn = object.getDbOper().getResultSet(bf.toString(), null);
            while (dn.next())
            {
                mintime = dn.getString("mintime");
                maxtime = dn.getString("maxtime");
            }
            camin.setTime(sdf.parse(mintime));
            camax.setTime(sdf.parse(maxtime));

            // 获取同期的功率曲线风速（取历史同期曲线，上一年没有继续往上推一年，直到时间小于功率曲线第一条时间，如果没有则不计算）
            if (mintime != null && maxtime != null)
            {
                // 查看去年的功率曲线数据
                ca.add(Calendar.YEAR, -1);
                while (ca.compareTo(camin) >= 0)
                {
                    // 获取同期的功率曲线数据
                    String powercureSql = "select * from powcurve where wtid = \'" + obj.getWtid() + "\' and rectime=\'" + new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(ca.getTime())
                            + "\'";
                    ResultSet powercureRs = object.getDbOper().getResultSet(powercureSql, null);
                    while (powercureRs.next())
                    {
                        winSpeed = powercureRs.getString("winspeed");
                    }
                    // 如果获取到风速，则直接跳出
                    if (StringUtils.isNotBlank(winSpeed))
                    {
                        rebtime = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(ca.getTime());
                        result = true;
                        real = Double.parseDouble(winSpeed);
                        break;
                    }
                    ca.add(Calendar.YEAR, -1);
                }
            }

            // 处理完时间上的问题
            if (result)
            {
                // 依据自学习功率曲线获取对应的理论功率
                if (rebtime != null)
                {
                    double deresult = getpowerFromLearning(real, obj.getWtid(), rectime, object.getDbOper(), wfid, rebtime);
                    if (deresult > 0)
                    {
                        obj.setLosselec(deresult / 60 - obj.getKind() > 0 ? deresult / 60 - obj.getKind() : 0);
                        obj.setWindspeed(Double.parseDouble(winSpeed));
                    }
                    else
                    {
                        obj.setLosselec(0);
                    }
                }
                else
                {
                    obj.setLosselec(0);
                }
            }
        }
        catch (Exception ex)
        {
            logger.error("MsEba_getObjResult", ex);
        }
        finally
        {
            object.getDbOper().closeConn();
        }
        return result;
    }

    /**
     * 入数据库
     * 
     * @param li
     *            sql语句
     * @throws Exception
     *             异常
     */
    private void getObjectToSql(List<String> li) throws Exception
    {
        try
        {
            object.getDbOper().openConn();
            object.getDbOper().excuteBatchSql(li.toArray(new String[li.size()]));
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
        finally
        {
            object.getDbOper().closeConn();
        }
    }

    /**
     * 中心风速 对应算法
     * 
     * @param legth
     *            左边
     * @param right
     *            右边
     * @param real
     *            中心
     * @param wtid
     *            风机
     * @param rectime
     *            最后时间
     * @param start
     *            开始时间
     * @param dbOperBase
     *            数据库连接对象
     * @return 返回用
     * @throws Exception
     *             异常
     */
    public static double getWindPower(double legth, double right, double real, String wtid, String start, String rectime, DbOperBase dbOperBase) throws Exception
    {
        double re = 0.0;
        try
        {
            String sql = "select avg(tbpow) as power from powcurve where wtid = '" + wtid + "' and rectime between '" + start + "' and  '" + rectime + "' and winspeed >  '" + legth
                    + "' and winspeed <= '" + right + "'";

            dbOperBase.openConn();
            ResultSet dn = dbOperBase.getResultSet(sql, null);

            while (dn.next())
            {
                re = dn.getDouble("power");
            }
        }
        catch (Exception ex)
        {
            logger.error("MsEba_getWindPower", ex);
        }
        finally
        {
            dbOperBase.closeConn();
        }
        return re;
    }

    /**
     * 从自学习功率曲线获取功率
     * 
     * @param realSpeed
     *            风速
     * @param wtid
     *            风机id
     * @param rectime
     *            结束时间
     * @param dbOperBase
     *            数据库对象
     * @param wfid
     *            风场id
     * @param rebtime
     *            开始时间
     * @return 理论功率
     * @throws SQLException
     *             异常
     */
    public static double getpowerFromLearning(double realSpeed, String wtid, String rectime, DbOperBase dbOperBase, String wfid, String rebtime) throws SQLException
    {
        double deresult = 0.0d;
        // 步长（默认为0.5）
        double speedStep = 0.5d;
        // 担保功率曲线id
        String guarpowid = null;
        try
        {
            dbOperBase.openConn();
            // 获取功率曲线步长
            String guarpowSql = "select * from config.guarpow gu where wfid=" + wfid + "and density=1";
            ResultSet guarpowRs = dbOperBase.getResultSet(guarpowSql, null);
            while (guarpowRs.next())
            {
                guarpowid = guarpowRs.getString("id");
            }
            if (StringUtils.isNotBlank(guarpowid))
            {
                String speedStepSql = "select  (max(windspeed)-min(windspeed))/(count(windspeed)-1) as speedstep from config.guarpowdetail where guarpowid=" + guarpowid;
                ResultSet speedStepRs = dbOperBase.getResultSet(speedStepSql, null);
                while (speedStepRs.next())
                {
                    speedStep = Double.parseDouble(speedStepRs.getString("speedstep"));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("MsEba_getpowerFromLearning", e);
        }
        finally
        {
            dbOperBase.closeConn();
        }
        // 确定风机风速中心风速
        double middle = Math.floor(realSpeed);
        double dtemp = realSpeed - middle;

        double minwindleft = 0.0d;
        double minwindright = 0.0d;
        if (dtemp >= speedStep)
        {
            minwindleft = middle + speedStep;
            minwindright = middle + speedStep * 2;
        }
        else
        {
            minwindleft = middle;
            minwindright = middle + speedStep;
        }
        try
        {
            // 获取风速仓对应的功率
            double re1 = MsEba.getWindPower(minwindleft - speedStep / 2, minwindleft + speedStep / 2, minwindleft, wtid, rebtime, rectime, dbOperBase);
            double re2 = MsEba.getWindPower(minwindright - speedStep / 2, minwindright + speedStep / 2, minwindright, wtid, rebtime, rectime, dbOperBase);
            if ((re2 - re1) >= 0)
            {
                deresult = ((re2 - re1) / (minwindright - minwindleft)) * (realSpeed - minwindleft) + re1;
            }
            else
            {
                deresult = re1;
            }
        }
        catch (Exception e)
        {
            logger.error("MsEba_getpowerFromLearning1", e);
        }
        return deresult;
    }

    /**
     * 设置对象
     * 
     * @param object
     *            对象
     */
    public void setObject(EbaObject object)
    {
        this.object = object;
    }

    /**
     * 得到十分钟数据时间（在数据处理中存在，是否需要下沉）
     * 
     * @param dataTime
     *            数据时间
     * @return val 数据时间
     * @throws DataAsException
     *             自定义异常
     */
    public static String getDataTenMinute(String dataTime) throws DataAsException
    {
        String val = "";

        try
        {
            Calendar calendar = Calendar.getInstance();
            if (!(dataTime.isEmpty() || dataTime == null))
            {
                calendar = DataAsFunc.parseDateTime(dataTime);
            }
            if ((calendar.get(Calendar.MINUTE) % 10) >= ControlProcessDef.TENMINUTEDATAMAXMINUTE)
            {
                calendar.add(Calendar.MINUTE, 10);
            }

            calendar.add(Calendar.MINUTE, -10);
            val = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(calendar.getTime());
            val = val.substring(0, 15) + "0:00.000";
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataTimeManage_getDataTenMinute_1", new String[] { "dataTime" }, new Object[] { dataTime }, e);
        }
        return val;
    }

}
