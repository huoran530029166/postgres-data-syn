/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DateAsDefTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess 
 * @Description: TODO
 * @author: Administrator   
 * @date: 2019年9月2日 下午8:29:25 
 * @version: V1.0   
 */
package com.goldwind.dataaccess;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @ClassName: DateAsDefTest
 * @Description: DateAsDefTest
 * @author: Administrator
 * @date: 2019年9月2日 下午8:29:25
 */
public class DateAsDefTest
{

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#getDateTimeFormatter(java.lang.String)}.
     */
    @Test
    public void testGetDateTimeFormatter()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATEFORMATSTR);
        LocalDate date = LocalDate.of(2019, 9, 21);
        String act = date.format(dateTimeFormatter);
        String exp = "2019-09-21";
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#string2LocalDateTime(java.lang.String, java.time.format.DateTimeFormatter)}.
     */
    @Test
    public void testString2LocalDateTime()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime act = LocalDateTime.of(2019, 9, 21, 12, 30, 20);
        String str = "2019-09-21 12:30:20";
        LocalDateTime exp = DateAsDef.string2LocalDateTime(str, dateTimeFormatter);
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#getTimeStringNew(java.time.LocalDateTime, java.time.format.DateTimeFormatter)}.
     */
    @Test
    public void testGetTimeStringNewNull()
    {
        LocalDateTime date = LocalDateTime.of(2019, 9, 21, 12, 30, 20);
        String act = DateAsDef.getTimeStringNew(date, null);
        String exp = "2019-09-21 12:30:20";
        Assert.assertEquals(exp, act);

        String timeString2 = DateAsDef.dateToString(LocalDateTime.now());
        System.out.println("dateToString>>" + timeString2);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#getTimeStringNew(java.time.LocalDateTime, java.time.format.DateTimeFormatter)}.
     */
    @Test
    public void testGetTimeStringNew()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATEFORMATSTR);
        LocalDateTime date = LocalDateTime.of(2019, 9, 21, 12, 30, 20);
        String act = DateAsDef.getTimeStringNew(date, dateTimeFormatter);
        String exp = "2019-09-21";
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#date2LocalDateTime(java.util.Date)}.
     */
    @Test
    public void testDate2LocalDateTime()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        Calendar ca = Calendar.getInstance();
        ca.set(2019, 8, 21, 12, 20, 30);
        Date date = ca.getTime();
        LocalDateTime act = DateAsDef.date2LocalDateTime(date);
        LocalDateTime exp = LocalDateTime.of(2019, 9, 21, 12, 20, 30);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
        // Assert.assertThat(act.format(dateTimeFormatter), Matchers.equalTo(exp.format(dateTimeFormatter)));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#localDateTime2Date(java.time.LocalDateTime)}.
     */
    @Test
    public void testLocalDateTime2Date()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 9, 21, 12, 20, 30);
        Calendar ca = Calendar.getInstance();
        ca.set(2019, 8, 21, 12, 20, 30);
        Date exp = ca.getTime();
        Date act = DateAsDef.localDateTime2Date(localDateTime);

        Assert.assertEquals(simpleDateFormat.format(exp), simpleDateFormat.format(act));
        // Assert.assertThat(simpleDateFormat.format(act), Matchers.equalTo(simpleDateFormat.format(exp)));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#stringTimeFormat(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStringTimeFormatEmpty()
    {
        String time = "";
        String act = DateAsDef.stringTimeFormat(time, DataAsDef.DATETIMEFORMATSTR, DataAsDef.DATETIMEFORMATSTR);
        String exp = "";
        Assert.assertEquals(exp, act);

        // Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#stringTimeFormat(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStringTimeFormat()
    {
        String time = "2019-09-21 12:30:20";
        String act = DateAsDef.stringTimeFormat(time, DataAsDef.DATETIMEFORMATSTR, DataAsDef.DATEFORMATSTR);
        String exp = "2019-09-21";
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addSecond(java.time.LocalDateTime, java.lang.Long)}.
     */
    @Test
    public void testAddSecond()
    {
        DateTimeFormatter formatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime act = LocalDateTime.of(1970, 1, 1, 8, 1, 0);
        LocalDateTime exp = DateAsDef.addSecond(60L);

        Assert.assertEquals(exp.format(formatter), act.format(formatter));

        // Assert.assertThat(act.format(formatter), Matchers.equalTo(exp.format(formatter)));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#betweenTime(java.time.temporal.Temporal, java.time.temporal.Temporal)}.
     */
    @Test
    public void testBetweenTime()
    {
        LocalDateTime s = LocalDateTime.of(1970, 1, 1, 8, 1, 0);
        LocalDateTime e = LocalDateTime.of(1970, 1, 1, 8, 4, 0);
        long act = DateAsDef.betweenTime(s, e);
        long exp = 180L;
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#betweenTime(java.time.temporal.Temporal, java.time.temporal.Temporal)}.
     */
    @Test
    public void testBetweenTime2()
    {
        LocalTime s = LocalTime.of(12, 20, 20);
        LocalTime e = LocalTime.of(12, 30, 20);
        long act = DateAsDef.betweenTime(s, e);
        long exp = 600L;
        Assert.assertEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#betweenDay(java.time.LocalDate, java.time.LocalDate)}.
     */
    @Test
    public void testBetweenDay()
    {
        LocalDate dateS = LocalDate.of(2019, 8, 21);
        LocalDate dateE = LocalDate.of(2019, 8, 30);
        System.out.println(DateAsDef.betweenDayInOneMonth(dateS, dateE));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTime()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 0, 5, 2, 0, 0, 0, 0);
        LocalDateTime exp = LocalDateTime.of(2020, 1, 3, 12, 20, 0, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTimeDay()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 0, 0, 40, 0, 0, 0, 0);
        LocalDateTime exp = LocalDateTime.of(2019, 9, 10, 12, 20, 0, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTimeHour()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 0, 0, 0, 20, 0, 0, 0);
        LocalDateTime exp = LocalDateTime.of(2019, 8, 2, 8, 20, 0, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTimeMouse()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 0, 0, 0, 20, 42, 0, 0);
        LocalDateTime exp = LocalDateTime.of(2019, 8, 2, 9, 2, 0, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTimeSecond()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 0, 0, 0, 20, 42, 72, 0);
        LocalDateTime exp = LocalDateTime.of(2019, 8, 2, 9, 3, 12, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addTime(java.time.LocalDateTime, long, long, long, long, long, long, long)}.
     */
    @Test
    public void testAddTimeyear()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.of(2019, 8, 1, 12, 20);
        LocalDateTime act = DateAsDef.addTime(localDateTime, 1, 0, 0, 20, 42, 72, 0);
        LocalDateTime exp = LocalDateTime.of(2020, 8, 2, 9, 3, 12, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#addWeek(java.time.LocalDateTime, long)}.
     */
    @Test
    public void testAddWeek()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime dateTime = LocalDateTime.of(2019, 8, 24, 12, 0);
        LocalDateTime act = DateAsDef.addWeek(dateTime, 2);
        LocalDateTime exp = LocalDateTime.of(2019, 9, 7, 12, 0);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#nextweek(java.time.LocalDate, java.time.DayOfWeek)}.
     */
    @Test
    public void testNextweek()
    {
        DateTimeFormatter dateTimeFormatter = DateAsDef.getDateTimeFormatter(DataAsDef.DATEFORMATSTR);
        LocalDate localDate = LocalDate.of(2019, 9, 1);
        LocalDate act = DateAsDef.nextweek(localDate, DayOfWeek.FRIDAY);
        LocalDate exp = LocalDate.of(2019, 9, 6);
        Assert.assertEquals(exp.format(dateTimeFormatter), act.format(dateTimeFormatter));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#getDiffDays(java.lang.Long, java.lang.Long)}.
     */
    @Test
    public void testGetDiffDays()
    {

        Long start = 0L;
        Long end = 1L * (1000 * 60 * 60 * 24);
        long diffDay = DateAsDef.getDiffDays(start, end);
        Assert.assertTrue(diffDay == 1);

        start = 1L * (1000 * 60 * 60 * 24);
        end = 0L;
        diffDay = DateAsDef.getDiffDays(start, end);
        Assert.assertTrue(diffDay == -1);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.DateAsDef#getDiffDays(java.lang.Long, java.lang.Long)}.
     */
    // @Test
    // public void testGetDiffDays1()
    // {
    // Long start = 1L * (1000 * 60 * 60 * 24);
    // Long end = 0L;
    // long diffDay = DateAsDef.getDiffDays(null, end);
    // Assert.assertTrue(diffDay == 0);
    // diffDay = DateAsDef.getDiffDays(start, null);
    // Assert.assertTrue(diffDay == 0);
    // }
}
