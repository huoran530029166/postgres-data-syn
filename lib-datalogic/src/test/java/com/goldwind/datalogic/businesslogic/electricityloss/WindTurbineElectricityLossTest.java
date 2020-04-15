package com.goldwind.datalogic.businesslogic.electricityloss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.businesslogic.DeviceHandle;
import com.goldwind.datalogic.businesslogic.model.Losselec;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.Together;

public class WindTurbineElectricityLossTest
{

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        DbOperBase.initPool(DatabaseType.Postgre, "jdbc:postgresql://10.80.5.109:8101/qhsoam_test", "pguser", "pguser", 100, 10, 0);
    }

    @Test
    public void test()
    {
        try (DbOperBase dbOper = new DbOperBase())
        {
            DeviceHandle inter = new DeviceHandle(dbOper);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startime = sdf.parse("2019-03-26 00:00:00");
            Date endtime = sdf.parse("2019-03-27 00:00:00");
            List<String> listdev = new ArrayList<String>(Arrays.asList("632809001"));
            long start1 = System.currentTimeMillis();
            List<Losselec> newList = inter.getDeviceLossElec(startime, endtime, listdev, null, "20", Together.DayTogether);
            long start2 = System.currentTimeMillis();
            System.out.println("LossElecNew>>" + newList.size() + "耗时" + (start2 - start1) / 1000.0 + "s");

            Assert.assertTrue(true);
        }
        catch (Exception e)
        {

        }
    }

}
