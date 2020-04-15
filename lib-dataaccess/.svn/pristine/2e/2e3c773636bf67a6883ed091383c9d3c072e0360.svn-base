package com.goldwind.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class JsonUtilTest
{

    @Test
    public void object2JsonString()
    {
        HashMap<Double, Double> hp = new HashMap<>();
        hp.put(1.0, 1.0);

        // MonthData monthdata = new MonthData();
        // monthdata.setWfid(632809);
        // monthdata.setYear(2);
        // monthdata.setMonth(12);
        // monthdata.setEnergy(BigDecimal.valueOf(3.2));
        String jsonoString = JsonUtil.object2JsonString(hp);
        System.out.println(jsonoString);
        Assert.assertEquals("{1.0:1.0}", jsonoString);
    }

    @Test
    public void jsonString2Object()
    {
        String jsonString = "{1.0, 1.0}";
        // HashMap<Double, Double> monthData = JsonUtil.jsonString2Object(jsonString, HashMap.class);
    }

    @Test
    public void jsonString2List()
    {
        String jsonString = "[{\"data\":3.2},{\"data1\":3.3}]";
        List<String> a = JsonUtil.jsonString2List(jsonString, String.class);
        List<String> jsonlist = new ArrayList<>();
        jsonlist.add("{\"data\":3.2}");
        jsonlist.add("{\"data1\":3.3}");
        Assert.assertEquals(jsonlist, a);

    }

    @Test
    public void jsonString2Map()
    {
        String jsonString = "{\"energy\":3,\"month\":12,\"wfid\":632809}";
        Map<String, Object> a = JsonUtil.jsonString2Map(jsonString);
        Map<String, Object> b = new HashMap<>();
        b.put("energy", 3);
        b.put("month", 12);
        b.put("wfid", 632809);
        Assert.assertEquals(a, b);
    }
}