package com.goldwind.datalogic.businesslogic;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.businesslogic.model.DeviceInfoObject;
import com.goldwind.datalogic.businesslogic.util.DeviceHandleFunction;
import com.goldwind.datalogic.utils.ControlProcessDef.TypeId;

public class DeviceHandleTest
{
    @Test
    public void test()
    {
        try
        {
            Class cls = Class.forName("DeviceHandle");

            dbLink link = new dbLink();
            link.cheak("", "140802001", "20");
        }
        catch (Exception ex)
        {

        }
        finally
        {

        }
    }

    final class dbLink
    {
        public dbLink() throws Exception
        {
            init();
        }

        private void init() throws Exception
        {
            DbOperBase.initPool(DatabaseType.Postgre, "jdbc:postgresql://10.80.5.68:8101/qhsoam1011", "pguser", "pguser", 100, 10, 60);
        }

        public Object cheak(String classname, String device, String type)
        {
            try
            {
                Class cls = Class.forName(classname);
                Method[] m = cls.getMethods();
                for (Method o : m)
                {
                    o.getName().equals("");
                }
                cls.getMethod("getDeviceObject", String.class);
            }
            catch (Exception ex)
            {

            }

            DbOperBase dbOper = new DbOperBase();
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
                    if (type.equals(TypeId.inverter.getResult()))
                    {
                        if (bmarkdevice != null && bmarkdevice.equals(dt.getString("wtid")))
                        {
                            obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"),  "bmark");
                        }
                        else
                        {
                            obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"),  "normal");
                        }
                    }
                    else
                    {
                        obj = new DeviceInfoObject(dt.getString("wfid"), dt.getString("wtid"),  dt.getString("normalstate"));
                    }

                    if (obj.getNomalstate().equals("bmark"))
                    {
                        strtemp = dt.getString("wtnarrate");
                        obj.setInverType(dt.getString("wtnarrate"));
                    }
                    else
                    {
                        obj.setInverType(dt.getString("wtnarrate"));
                    }
                    hash.put(dt.getString("wtid"), obj);
                }

                if (bmarkdevice != null)
                {
                    // 说明是光伏（逆变器）
                    if (type.equals(TypeId.inverter.getResult()))
                    {
                        String wfidtemp = device.substring(0, device.length() - 3);
                        String sql = "select * from config.wtinfo where wfid = '" + wfidtemp + "' order by wtid";
                        ResultSet dn = dbOper.getResultSet(sql, null);
                        while (dn.next())
                        {
                            if (!dn.getString("wtid").equals(device))
                            {
                                if (dn.getString("wtnarrate") != null)
                                {
                                    if (dn.getString("wtnarrate").equals(strtemp))
                                    {
                                        DeviceInfoObject obj = new DeviceInfoObject(dn.getString("wfid"), dn.getString("wtid"),  "normal");
                                        obj.setInverType(dn.getString("wtnarrate"));
                                        hash.put(dn.getString("wtid"), obj);
                                    }
                                }
                            }
                        }
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
                    e.getMessage();
                }
            }
            return hash;
        }
    }

    @Test
    public void testIsGWWindTurbine()
    {
        boolean result = DeviceHandleFunction.isGWWindTurbine("GW-750");
        assertTrue(result == true);
    }

    @Test
    public void testIsGWWindTurbine1()
    {
        boolean result = DeviceHandleFunction.isGWWindTurbine("WG-750");
        assertTrue(result == false);
    }

    @Test
    public void testIsGWWindTurbine2()
    {
        boolean result = DeviceHandleFunction.isGWWindTurbine(null);
        assertTrue(result == false);
    }
}
