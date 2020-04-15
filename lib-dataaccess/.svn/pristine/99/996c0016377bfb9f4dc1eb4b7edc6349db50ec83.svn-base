package com.goldwind.dataaccess;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * @ClassName: DateAsDef
 * @Description: 日期帮助类
 * @author Administrator
 * @date: 2019年8月6日 下午8:28:07
 */
public class DateAsDef
{
    private DateAsDef()
    {
        
    }

    /**
     * @Title: getDateTimeFormatter
     * @Description: 时间格式
     * @param dateFormatter
     *            格式
     * @return 时间格式
     * @return: DateTimeFormatter 时间格式
     */
    public static DateTimeFormatter getDateTimeFormatter(String dateFormatter)
    {
        return DateTimeFormatter.ofPattern(dateFormatter);
    }

    /**
     * @Title: string2LocalDateTime
     * @Description: 时间String转化成LocalDateTime
     * @param dateTime
     *            时间
     * @param dateTimeFormatter
     *            格式
     * @return LocalDateTime
     * @return: LocalDateTime 时间格式
     */
    public static LocalDateTime string2LocalDateTime(String dateTime, DateTimeFormatter dateTimeFormatter)
    {
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

    /**
     * @Title: getTimeStringNew
     * @Description: 新的日期转标准字符串，返回格式yyyy-MM-dd HH:mm:ss
     * @param date
     *            时间
     * @param dateTimeFormatter
     *            时间格式
     * @return 返回值
     * @return: String 返回值
     */
    public static String getTimeStringNew(LocalDateTime date, DateTimeFormatter dateTimeFormatter)
    {
        if (dateTimeFormatter == null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEFORMATSTR);
            return date.format(formatter);
        }
        else
        {
            return date.format(dateTimeFormatter);
        }
    }

    /**
     * @Title: date2LocalDate
     * @Description: Date格式转换成LocalDateTime(比较消耗性能 慎用)
     * @param date
     *            Date
     * @return 返回值
     * @return: LocalDateTime LocalDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date)
    {
        // 获取instant信息（为世界标准时间）
        Instant instant = date.toInstant();
        // 获取当地经纬度id
        ZoneId zoneId = ZoneId.systemDefault();
        // 获取LocalDateTime数据
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * @Title: localDateTime2Date
     * @Description: localDateTime转成Date
     * @param localDateTime
     *            localDateTime
     * @return 返回值
     * @return: Date Date
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime)
    {
        // 获取当地经纬度id
        ZoneId zoneId = ZoneId.systemDefault();
        // 将Date转成世界标准时间
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        // 转成Date
        return Date.from(instant);
    }

    /**
     * 字符串时间格式化
     *
     * @param timeString
     *            时间
     * @param oldFormat
     *            旧的时间格式
     * @param format
     *            新的标准格式
     * @return 格式化时间
     */
    public static String stringTimeFormat(String timeString, String oldFormat, String format)
    {
        String formatTime = timeString;

        if (StringUtils.isEmpty(timeString))
        {
            return formatTime;
        }

        // 数据库标准时间格式
        DateTimeFormatter oldTimeFormatter = DateTimeFormatter.ofPattern(oldFormat);
        // 系统选项时间格式
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime timeLocalDate = LocalDateTime.parse(timeString, oldTimeFormatter);
        formatTime = timeLocalDate.format(timeFormatter);

        return formatTime;
    }

    /**
     * @Title: addSecond
     * @Description: 1970-01-01T00:00加秒数
     * @param second
     *            秒数
     * @return 返回
     * @return: LocalDateTime LocalDateTime
     */
    public static LocalDateTime addSecond(Long second)
    {
        // 获取当地经纬度id
        ZoneId zoneId = ZoneId.systemDefault();
        // 加上秒 1970-01-01T00:00
        Instant instant1 = Instant.ofEpochSecond(second);
        // 转换为LocalDateTime
        return instant1.atZone(zoneId).toLocalDateTime();
    }

    /**
     * @Title: betweenTime Time计算
     * @Description: 计算两个时间的时间差 秒数
     * @param timeS
     *            开始时间 必须带Time LocalDateTime或者LocalTime
     * @param timeE
     *            结束时间 必须带Time LocalDateTime或者LocalTime
     * @return 秒数
     * @return: Long 秒数
     */
    public static Long betweenTime(Temporal timeS, Temporal timeE)
    {
        Duration between = Duration.between(timeS, timeE);
        // 转化为秒数
        return between.getSeconds();
    }

    /**
     * @Title: betweenTime Time计算
     * @Description: 计算两个时间的时间差 秒数
     * @param timeS
     *            开始时间
     * @param timeE
     *            结束时间
     * @return 秒数
     * @return: Long 秒数
     */
    public static Long betweenTime(Date timeS, Date timeE)
    {
        return (timeE.getTime() - timeS.getTime()) / 1000;
    }

    /**
     * @Title: betweenTime Time计算
     * @Description: 计算两个时间的时间差 秒数
     * @param timeS
     *            开始时间
     * @param timeE
     *            结束时间
     * @return 秒数
     * @return: Long 秒数
     */
    public static Long betweenTime(Calendar timeS, Calendar timeE)
    {
        return (timeE.getTimeInMillis() - timeS.getTimeInMillis()) / 1000;
    }

    /**
     * @Title: betweenDayInOneMonth
     * @Description: 计算两个时间的时间差 天数 注意：一个月之内的
     * @param dateS
     *            开始时间
     * @param dateE
     *            结束时间
     * @return 天数
     * @return: int 天数
     */
    public static int betweenDayInOneMonth(LocalDate dateS, LocalDate dateE)
    {
        Period between = Period.between(dateS, dateE);
        // 转化为天数
        return between.getDays();
    }

    /**
     * @Title: addTime
     * @Description: 增加时间
     * @param dateTime
     *            原来的时间
     * @param years
     *            增加的年数
     * @param months
     *            增加的月数
     * @param days
     *            增加的天数
     * @param hours
     *            增加小时
     * @param minutes
     *            增加分钟
     * @param seconds
     *            增加秒数
     * @param nanos
     *            增加纳秒
     * @return 增加后的时间
     * @return: LocalDateTime 增加后的时间
     */
    public static LocalDateTime addTime(LocalDateTime dateTime, long years, long months, long days, long hours, long minutes, long seconds, long nanos)
    {
        return dateTime.plusYears(years).plusMonths(months).plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusNanos(nanos);
    }

    /**
     * @Title: addWeek 增加星期
     * @Description: 增加星期
     * @param dateTime
     *            增加星期
     * @param weeks
     *            增加星期数
     * @return 日期
     * @return: LocalDateTime 日期
     */
    public static LocalDateTime addWeek(LocalDateTime dateTime, long weeks)
    {
        return dateTime.plusWeeks(weeks);
    }

    /**
     * @Title: nextweek 下个周几
     * @Description: 下个周几
     * @param date
     *            LocalDate
     * @param dayOfWeek
     *            星期几
     * @return LocalDate
     * @return: LocalDate LocalDate
     */
    public static LocalDate nextweek(LocalDate date, DayOfWeek dayOfWeek)
    {
        return date.with(TemporalAdjusters.nextOrSame(dayOfWeek));
    }

    /**
     * @Title: getMonthFirstDate
     * @Description: 获取本月的第一天
     * @return 返回本月第一天日期
     * @return: Date 第一天日期
     */
    public static Date getMonthFirstDate()
    {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return cale.getTime();
    }

    /**
     * @Title: date2millisecond
     * @Description: 时间转成毫秒
     * @param date
     *            时间
     * @param pattern
     *            格式化
     * @return 毫秒数
     * @return: long 长整型
     * @throws ParseException
     *             异常
     */
    public static Long date2millisecond(String date, String pattern) throws ParseException
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
        // 将参数按照给定的格式解析参数
        Date date2 = simpledateformat.parse(date);
        return date2 == null ? null : date2.getTime();
    }

    /**
     * 设置指定小时、分钟和秒的时间戳
     * 
     * @param date
     *            日期
     * @param pattern
     *            格式化
     * @param hour
     *            设置的小时数
     * @param minute
     *            设置的分钟数
     * @param second
     *            设置的秒数
     * @return 时间戳
     * @throws ParseException
     *             异常
     */
    public static long setTime2millisecond(String date, String pattern, int hour, int minute, int second) throws ParseException
    {
        SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
        // 将参数按照给定的格式解析参数
        Date date2 = simpledateformat.parse(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date2);
        ca.set(Calendar.HOUR_OF_DAY, hour);
        ca.set(Calendar.MINUTE, minute);
        ca.set(Calendar.SECOND, second);
        return ca.getTimeInMillis();
    }

    /**
     * 任意时间字符串转成date对象 xjs 2019-8-16 14:41:33
     * 
     * @param text
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String text, String pattern) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(text);
    }

    /**
     * 解析时间返回Date
     * 
     * @param datetime
     *            需解析日期字符串
     * @return 解析后的时间
     * @throws DataAsException
     *             自定义异常
     */
    public static Date parseDate(String datetime) throws DataAsException
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (datetime.length() == DataAsDef.DATEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATEFORMATSTR);
            }
            else if (datetime.length() == DataAsDef.DATETIMEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            }
            else if (datetime.length() == DataAsDef.DATETIMEMSFORMAT.length())
            {
                // 兼容软适配遗留的旧时间格式："yyyy-MM-dd HH:mm:ss:SSS"(2019.03.18 fcy)
                if (":".equals(datetime.substring(19, 20)))
                {
                    datetime = datetime.substring(0, 19) + "." + datetime.substring(20, 23);
                }

                sdf = new SimpleDateFormat(DataAsDef.DATETIMEMSFORMAT);
            }
            else if (datetime.length() == DataAsDef.TEMPFILENAMEBYDATE.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYDATE);
            }
            Date dt = sdf.parse(datetime);
            return dt;
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_parseDateTime_1", new String[] { "time" }, new Object[] { datetime }, e);
        }
        return new Date();
    }

    /**
     * 解析时间 成LocalDateTime
     * 
     * @param datetime
     *            需解析日期字符串
     * @return 解析后的时间
     * @throws DataAsException
     *             自定义异常
     */
    public static LocalDateTime parseLocalDateTime(String datetime) throws DataAsException
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (datetime.length() == DataAsDef.DATEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATEFORMATSTR);
            }
            else if (datetime.length() == DataAsDef.DATETIMEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            }
            else if (datetime.length() == DataAsDef.DATETIMEMSFORMAT.length())
            {
                // 兼容软适配遗留的旧时间格式："yyyy-MM-dd HH:mm:ss:SSS"(2019.03.18 fcy)
                if (":".equals(datetime.substring(19, 20)))
                {
                    datetime = datetime.substring(0, 19) + "." + datetime.substring(20, 23);
                }

                sdf = new SimpleDateFormat(DataAsDef.DATETIMEMSFORMAT);
            }
            else if (datetime.length() == DataAsDef.TEMPFILENAMEBYDATE.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYDATE);
            }
            Date dt = sdf.parse(datetime);
            return date2LocalDateTime(dt);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_parseDateTime_1", new String[] { "time" }, new Object[] { datetime }, e);
        }
        return LocalDateTime.now();
    }

    /**
     * long→LocalDateTime xjs 2019-8-16 14:41:33
     * 
     * @param milli
     * @return
     */
    public static LocalDateTime longToLocalDateTime(Long milli)
    {
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milli), zone);
    }

    /**
     * 获取当前时间字符串yyyy-MM-dd HH:mm:ss xjs 2019-8-16 14:41:33
     * 
     * @return
     */
    public static String getNow()
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEFORMATSTR);
        return formatter.format(localDateTime);
    }

    /**
     * 获取当前时间字符串yyyy-MM-dd HH:mm:ss.SSS xjs 2019-8-16 14:41:33
     * 
     * @return
     */
    public static String getCurrentTime()
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEMSFORMAT);
        return formatter.format(localDateTime);
    }

    /**
     * 日期对象转字符串yyyy-MM-dd HH:mm:ss
     * 
     * @param localDateTime
     * @return
     */
    public static String dateToString(LocalDateTime localDateTime)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEMSFORMAT);
        return formatter.format(localDateTime);
    }

    /**
     * 日期对象转字符串yyyy-MM-dd HH:mm:ss.SSS xjs 2019-8-16 14:41:33
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date)
    {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEMSFORMAT);
        return formatter.format(localDateTime);
    }

    /**
     * 日期对象转字符串yyyy-MM-dd HH:mm:ss xjs 2019-8-16 14:41:33
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern)
    {
        LocalDateTime localDateTime = date2LocalDateTime(date);
        return dateToString(localDateTime, pattern);
    }

    /**
     * 日期对象转字符串yyyy-MM-dd HH:mm:ss xjs 2019-8-16 14:41:33
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(LocalDateTime localDateTime, String pattern)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(localDateTime);
    }

    /**
     * 获取两个日期之间的日期 start 1.1;end 1.3 结果1.2
     * 
     * @param start
     *            开始日期
     * @param end
     *            结束日期
     * @return 日期集合
     */
    public static List<Date> getBetweenDates(Date start, Date end)
    {
        List<Date> result = new ArrayList<>(30);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.set(Calendar.HOUR_OF_DAY, 0);
        tempEnd.set(Calendar.MINUTE, 0);
        tempEnd.set(Calendar.SECOND, 0);
        tempEnd.set(Calendar.MILLISECOND, 0);
        while (tempStart.before(tempEnd))
        {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 获得两个时间之间的天数 单位：毫秒 天数：long
     * 
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public static long getDiffDays(Long start, Long end)
    {
        if (start == null || end == null)
        {
            return 0;
        }
        // 相差天数
        return (end - start) / (1000 * 60 * 60 * 24);
    }
}
