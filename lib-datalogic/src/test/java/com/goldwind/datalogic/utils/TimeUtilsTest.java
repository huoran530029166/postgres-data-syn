/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: TimeUtilsTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: TimeUtilsTest
 * @author: Administrator   
 * @date: 2019年9月9日 下午7:31:36 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DateAsDef;

/** 
 * @ClassName: TimeUtilsTest 
 * @Description: TimeUtilsTest
 * @author: Administrator
 * @date: 2019年9月9日 下午7:31:36  
 */
public class TimeUtilsTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#getBetweenDates(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testGetBetweenDates()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 9, 20);
        Date start=ca.getTime(); 
        ca.set(2019, 9, 22);
        Date end=ca.getTime();
        //实际值
        List<Date> act=DateAsDef.getBetweenDates(start, end);
        //期望值
        List<Date> exp=new ArrayList<Date>();
        ca.set(2019, 9, 21);
        exp.add(ca.getTime());
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#getBetweenDates(java.util.Date, java.util.Date)}.
     */
    @Test
    public void testGetBetweenDatesException()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 9, 22);
        Date start=ca.getTime(); 
        ca.set(2019, 9, 20);
        Date end=ca.getTime();
        //实际值  开始比结束小的话返回一个空的list
        List<Date> act=DateAsDef.getBetweenDates(start, end);
        //期望值
        List<Date> exp=new ArrayList<Date>();
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }
    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#getMonthFirstDate()}.
     */
    @Test
    public void testGetMonthFirstDate()
    {
        //获取月份第一天
        Date date=DateAsDef.getMonthFirstDate();
        //格式化
        SimpleDateFormat s=new SimpleDateFormat(DataAsDef.DATEFORMATSTR);
        String dateStr=s.format(date);
        // 判断最后三位为有01
        Assert.assertThat(dateStr.substring(dateStr.length()-3), Matchers.equalTo("-01"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#date2millisecond(java.lang.String, java.lang.String)}.
     * @throws ParseException 异常
     */
    @Test
    public void testDate2millisecond() throws ParseException
    {
        String date="2019-07-07";
        long act=DateAsDef.date2millisecond(date, DataAsDef.DATEFORMATSTR);
        Assert.assertEquals(1562428800000L, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#date2millisecond(java.lang.String, java.lang.String)}.
     * @throws ParseException 异常
     */
    @Test(expected=ParseException.class)
    public void testDate2millisecondException() throws ParseException
    {
        String date="2019-07-07";
        //转化异常
        long act=DateAsDef.date2millisecond(date, DataAsDef.DATETIMEFORMATSTR);
        Assert.assertEquals(1562428800000L, act);
    }
    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#setTime2millisecond(java.lang.String, java.lang.String, int, int, int)}.
     * @throws ParseException 异常
     */
    @Test
    public void testSetTime2millisecond() throws ParseException
    {
        //参数
        String date="2019-07-07";
        int hour=23;
        int minute=59;
        int second=40;
        long act=DateAsDef.setTime2millisecond(date, DataAsDef.DATEFORMATSTR, hour, minute, second);
        Assert.assertEquals(1562515180000L, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.TimeUtils#setTime2millisecond(java.lang.String, java.lang.String, int, int, int)}.
     * @throws ParseException 异常
     */
    @Test
    public void testSetTime2millisecond2() throws ParseException
    {
        //参数
        String date="2019-07-07 13:20:23";
        int hour=23;
        int minute=59;
        int second=40;
        //小时、分钟、秒重新赋值
        long act=DateAsDef.setTime2millisecond(date, DataAsDef.DATETIMEFORMATSTR, hour, minute, second);
        Assert.assertEquals(1562515180000L, act);
    }

}
