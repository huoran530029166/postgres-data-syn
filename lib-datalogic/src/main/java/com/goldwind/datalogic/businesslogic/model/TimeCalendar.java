package com.goldwind.datalogic.businesslogic.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.goldwind.dataaccess.DateAsDef;

/**
 * 时间格式
 * 
 * @author 谭璟
 *
 */
public class TimeCalendar
{

    /**
     * 时间格式
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 开始时间
     */
    private Calendar startime = Calendar.getInstance();

    /**
     * 结束时间
     */
    private Calendar endtime = Calendar.getInstance();

    /**
     * 开始时间
     */
    private String start;

    /**
     * 结束时间
     */
    private String end;

    /**
     * 默认构造函数
     */
    public TimeCalendar()
    {

    }

    /**
     * 带参数的构造函数
     */
    public TimeCalendar(Calendar startime, Calendar endtime)
    {
        this.startime = startime;
        this.endtime = endtime;
        this.start=DateAsDef.dateToString(startime.getTime());
        this.end=DateAsDef.dateToString(endtime.getTime());
    }

    /**
     * String 类型时间
     * 
     * @param start
     *            开始
     * @param end
     *            结束
     */
    public TimeCalendar(String start, String end)
    {
        this.start = start;
        this.end = end;
        changTimeToCa();
    }

    public Calendar getStartime()
    {
        return startime;
    }

    public void setStartime(Calendar startime)
    {
        this.startime = startime;
    }

    public Calendar getEndtime()
    {
        return endtime;
    }

    public void setEndtime(Calendar endtime)
    {
        this.endtime = endtime;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getEnd()
    {
        return end;
    }

    public void setEnd(String end)
    {
        this.end = end;
    }

    /**
     * String to Calendar
     * 
     */
    private void changTimeToCa()
    {
        try
        {
            this.startime.setTime(sdf.parse(start));
            this.endtime.setTime(sdf.parse(end));
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }

    }
    
    @Override
    public String toString() 
    {
        return "{StartTime:"+DateAsDef.dateToString(this.startime.getTime())+",EndTime:"+DateAsDef.dateToString(this.endtime.getTime())+"}";
    }
}
