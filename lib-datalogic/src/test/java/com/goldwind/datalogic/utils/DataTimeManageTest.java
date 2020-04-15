package com.goldwind.datalogic.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

import junit.framework.Assert;

public class DataTimeManageTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testGetDataOneMinute()
    {
        try
        {
            String onetime = DataTimeManage.getDataOneMinute("2019-09-23 21:05:00.000");
            Assert.assertEquals("2019-09-23 21:04:00.000", onetime);
        }
        catch (DataAsException e)
        {
        }
    }

    @Test
    public void testGetDataTenMinute()
    {
        try
        {
            String onetime = DataTimeManage.getDataTenMinute("2019-09-23 21:05:00.000");
            Assert.assertEquals("2019-09-23 20:50:00.000", onetime);
        }
        catch (DataAsException e)
        {
        }
    }

    @Test
    public void testGetDataDate()
    {
        try
        {
            String onetime = DataTimeManage.getDataDate("2019-09-23 21:05:00.000");
            System.out.println(onetime);
            Assert.assertEquals("2019-09-22 21:05:00", onetime);
        }
        catch (DataAsException e)
        {
        }
    }
}
