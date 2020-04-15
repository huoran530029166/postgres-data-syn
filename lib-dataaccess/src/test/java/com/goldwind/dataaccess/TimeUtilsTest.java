package com.goldwind.dataaccess;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.Test;

public class TimeUtilsTest
{

    @Test
    public void test() throws ParseException
    {
        String pattern="yyyy-MM-dd HH:mm:ss";
        String time="2019-08-16 15:10:54";
        Date date=DateAsDef.parseDate(time, pattern);
        System.out.println(date);
        
        
        String result=DateAsDef.dateToString(date, pattern);
        System.out.println(result);        
        
        System.out.println("TimeUtils测试完成");
        
    }
    
    @Test
    public void parseDate()
    {
        // 定义一个任意格式的日期时间字符串
        String str1 = "2019-08-16 15:10:54";
        // 根据需要解析的日期、时间字符串定义解析所用的格式器
        DateTimeFormatter fomatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 执行解析
        LocalDateTime dt1 = LocalDateTime.parse(str1, fomatter1);
        System.out.println(dt1); // 输出 2014-04-12T01:06:09
        
    }

}
