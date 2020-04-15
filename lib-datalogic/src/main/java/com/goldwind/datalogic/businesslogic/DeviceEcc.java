package com.goldwind.datalogic.businesslogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.businesslogic.inf.BusinessEcc;

/**
 * ECC 业务接口 调用方法
 * 
 * @author 谭璟
 *
 */
public class DeviceEcc extends BusinessEcc
{
    private static Log LOGGER = Log.getLog(BusinessEcc.class);
    /**
     * 数据操作对象
     */
    private DbOperBase dbOper = null;

    /**
     * 大气压
     */
    private double atmosphere = 0.0;

    /**
     * 担保功率曲线空气密度
     */
    private double atomdesty = 0.0;

    /**
     * 时间格式
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 默认构造函数
     */
    public DeviceEcc()
    {

    }

    /**
     * 带参数构造函数
     * 
     * @param dbOper
     *            数据操作对象
     */
    public DeviceEcc(DbOperBase dbOper)
    {
        this.dbOper = dbOper;
    }

    @Override
    public Map<Double, Double> getDeviceEcc(String wfid, String wtid, String rectime, Map<Double, Double> hash, List<String> li)
    {
        Map<Double, Double> result = new LinkedHashMap<>();
        try
        {
            if (hash.size() == 0)
            {
                return null;
            }

            Map<Double, Double> hs = new LinkedHashMap<>();

            // 得到 担保功率曲线对应空气密度
            atomdesty = getWfidDeinsty(wfid, dbOper);

            // "false"字符串
            String falseStr = "false";
            StringStr str = new StringStr(falseStr);
            // 说明 当前风场 有测风塔集合 通过 测风塔得到 大气压
            atmosphere = getWfidTestWind(wfid, li, rectime, str);

            if (str.str.equals(falseStr))
            {
                atmosphere = getHightNum(atmosphere);
            }

            // 对原始数据进行整合
            dbOper.openConn();
            double valavg = 0.0;
            int a = 0;

            String starttime = rectime + " 00:00:00";
            String endtime = rectime + " 23:59:59";
            String sql = "select * from powcurve where wfid = '" + wfid + "' and wtid = '" + wtid + "' and rectime between '" + starttime + "' and '" + endtime + "' order by rectime";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                a += 1;
                valavg += dt.getDouble("envitemp");
                hs.put(dt.getDouble("winspeed"), dt.getDouble("tbpow"));
            }

            // 环境平均温度
            valavg = (valavg / (a == 0 ? 1 : a)) + 273.15;

            // 得到月或空气密度
            double mouthdesty = 0.0;
            if ((falseStr).equals(str))
            {
                mouthdesty = density * (atmosphere * 10 / atmos) * (273.15 / valavg);
            }
            else
            {
                mouthdesty = density * (atmosphere / atmos) * (273.15 / valavg);
            }

            // 担保功率速度择算
            for (double value : hash.keySet())
            {
                double power = 0.0;
                double speed = value * (Math.pow((atomdesty / mouthdesty), (1.0) / 3));
                result.put(speed, power);
            }

            // 担保功率曲线功率的择算
            getWtidValue(result, hash);
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
                LOGGER.error(e);
            }
        }
        return result;
    }

    /**
     * 查询 担保功率曲线的空气密度
     * 
     * @param wfid
     *            风场
     * @param dbOper
     *            数据库操作对象
     * @return 返回对应的 空气密度
     */
    private double getWfidDeinsty(String wfid, DbOperBase dbOper)
    {
        double result = 0.0;
        try
        {
            Map<String, Double> hash = new HashMap<>();
            dbOper.openConn();
            String sql = "select * from ( select configkey,groupid,configval from custom.usercnf union all select key as configkey,";
            sql += "null as groupid,initvalue as configval from config.modelcnf ) a where configkey = 'CustomAirDensity' order by groupid";
            ResultSet dt = dbOper.getResultSet(sql, null);
            while (dt.next())
            {
                if (dt.getString("groupid") != null)
                {
                    String groupid = dt.getString("groupid");
                    double value = dt.getDouble("configval");
                    hash.put(groupid, value);
                }
            }

            // 说明当前 风场是在表中 可以查询到数据
            if (hash.containsKey(wfid))
            {
                return hash.get(wfid);
            }
            else
            {
                sql = "SELECT * from config.groupinfo where id = '" + wfid + "'";
                ResultSet dn = dbOper.getResultSet(sql, null);
                if (dn.next())
                {
                    String parentid = dn.getString("parentid");
                    if (hash.containsKey(parentid))
                    {
                        return hash.get(parentid);
                    }
                    else
                    {
                        sql = "SELECT * from config.groupinfo where id = '" + parentid + "'";
                        ResultSet dm = dbOper.getResultSet(sql, null);
                        if (dm.next())
                        {
                            String nn = dm.getString("parentid");
                            if (hash.containsKey(nn))
                            {
                                return hash.get(nn);
                            }
                            else
                            {
                                result = density;
                            }
                        }
                        else
                        {
                            result = density;
                        }
                    }
                }
                else
                {
                    result = density;
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
                LOGGER.error(e);
            }
        }
        return result;
    }

    /**
     * @param wfid
     *            风场
     * @param li
     *            测试塔集合
     * @param rectime
     *            时间
     * @param st
     *            状态字 true 返回是 大气压 false问海拔高度
     * @return 返回
     */
    private double getWfidTestWind(String wfid, List<String> li, String rectime, StringStr st)
    {
        double result = 0.0;
        try
        {
            dbOper.openConn();

            // "height"字符串
            String heightStr = "height";
            // "select * from config.wfinfo where wfid = '"字符串
            String selectWfinfoSql = "select * from config.wfinfo where wfid = '";

            if (!li.isEmpty())
            {
                double value = 0.0;
                int a = 0; // 测试风他台数 如果 值计算当前 测风塔有数据值的台数
                double value1 = 0.0;
                int b = 0;

                for (String wtid : li)
                {
                    String protocolid = null;
                    StringBuffer str = new StringBuffer();
                    str.append("select * from config.wtinfo where wtid = ").append("\' " + wtid + "\'");
                    ResultSet dt = dbOper.getResultSet(str.toString(), null);

                    while (dt.next())
                    {
                        protocolid = dt.getString("protocolid");
                    }

                    // 得到 测风塔协议之后 得到大气压平均值的IEC量
                    int offset = -1;
                    String iec = null;

                    StringBuffer sql = new StringBuffer();
                    sql.append("select * from config.propaths where protocolid = ").append("\'" + protocolid + "\' and cdc = 'TEN' and  transtype = 2 order by offsets,iecpath");
                    ResultSet dn = dbOper.getResultSet(sql.toString(), null);
                    while (dn.next())
                    {
                        offset++;
                        if (dn.getString("descrcn").equals("气压平均值"))
                        {
                            iec = dn.getString("iecpath");
                        }
                    }

                    if (iec != null && !iec.isEmpty())
                    {
                        // 对时间进行处理 大气压取值为 从rectime到结束时间 往前推1个月时间
                        int i = 0;
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(sdf.parse(rectime));
                        ca.add(Calendar.MONTH, -1);

                        String start = sdf.format(ca.getTime());
                        String id = "id" + offset;
                        String sqk = "select " + id + " from daydata where wfid  = '" + wfid + "' and  wtid = '" + wtid + "' and rectime between '" + start + "' and '" + rectime
                                + "' order by rectime";

                        ResultSet bb = dbOper.getResultSet(sqk, null);
                        while (bb.next())
                        {
                            if (bb.getDouble(id) > 0)
                            {
                                i += 1;
                                value += bb.getDouble(id);
                            }
                        }

                        value = value / (i == 0 ? 1 : i);
                        if (value > 0)
                        {
                            a++;
                            result += value;
                        }
                    }
                    else
                    {
                        String sq = "SELECT altitude from config.assetinfo where wtid = '" + wtid + "'";
                        ResultSet dtt = dbOper.getResultSet(sq, null);
                        if (dtt.next())
                        {
                            b += 1;
                            value1 += dt.getDouble("altitude");
                        }
                        else
                        {
                            sq = selectWfinfoSql + wfid + "'";
                            ResultSet dnn = dbOper.getResultSet(sq, null);
                            while (dnn.next())
                            {
                                value = dnn.getDouble(heightStr);
                            }
                            return value;
                        }
                    }
                }

                // 当前测风塔有数据 如果没有数据
                if (result > 0)
                {
                    st.setStr("true");
                    result = result / a;
                }
                else
                {
                    if (value1 > 0)
                    {
                        result = value1 / b;
                    }
                    else
                    {
                        // 如果在上述 判断中 找不到测风塔IEC量 首先从config.assetinfo 资产信息表查询海拔高度，如果有就取值 ，如果没有就到config.wfinfo找到对应风场的海拔高度
                        String sql = selectWfinfoSql + wfid + "'";
                        ResultSet dnnn = dbOper.getResultSet(sql, null);
                        while (dnnn.next())
                        {
                            result = dnnn.getDouble(heightStr);
                        }
                    }
                }
            }
            else
            {
                // 如果在上述 判断中 找不到测风塔IEC量 首先从config.assetinfo 资产信息表查询海拔高度，如果有就取值 ，如果没有就到config.wfinfo找到对应风场的海拔高度
                String sql = selectWfinfoSql + wfid + "'";
                ResultSet dns = dbOper.getResultSet(sql, null);
                while (dns.next())
                {
                    result = dns.getDouble(heightStr);
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
                LOGGER.error(e);
            }
        }
        return result;
    }

    /**
     * 通过海拔高度 得到大气压
     * 
     * @param hight
     *            海拔高度
     * @return 返回大气压
     */
    private double getHightNum(double hight)
    {
        // double result = 0.0;
        double val = 6357 + (hight / 1000);
        double val1 = (0.0255 * (hight / 1000)) * (6357 / val);
        double val2 = 1 - val1;
        double result = Math.pow(val2, 5.256);
        result = atomtemp * result;
        return result;
    }

    /**
     * 担保功率曲线择算
     * 
     * @param hash
     *            择算后的速度 容器
     * @param hashpower
     *            原始录入功率曲线对象
     */
    private void getWtidValue(Map<Double, Double> hash, Map<Double, Double> hashpower)
    {
        List<Double> li = new ArrayList<>();
        List<Double> l = new ArrayList<>();
        double power;

        for (Double value : hashpower.keySet())
        {
            li.add(value);
        }

        for (Double value : hash.keySet())
        {
            l.add(value);
        }

        for (int i = 0; i < li.size(); i++)
        {
            double speed = li.get(i);
            if (i != li.size() - 1)
            {
                // 择算后的功率
                power = (hashpower.get(li.get(i + 1)) - hashpower.get(li.get(i)) / (l.get(i + 1) - l.get(i))) * (l.get(i) - li.get(i)) + hashpower.get(li.get(i));
                hash.put(l.get(i), power);
            }
            else
            {
                power = hashpower.get(speed);
                hash.put(l.get(i), power);
            }
        }
    }

    /**
     * 临时变量
     * 
     * @author 谭璟
     *
     */
    private static class StringStr
    {
        /**
         * 单独一个变量值
         */
        private String str = null;

        public StringStr(String str)
        {
            this.str = str;
        }

        public void setStr(String str)
        {
            this.str = str;
        }
    }
}
