/**
 * 
 */
package com.goldwind.datalogic.business.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 33359
 * 变位数据表实体映射
 */
public class ChangeDataBean
{
    /**
     * 
     */
    private int wtid;

    private String iecpath;

    private String rectime;

    private String endtime;

    private String changedata;
    
    /**
     * 由开始时间，结束时间，抽离成一分钟数据结合
     */
    private List<String>mineteList=new ArrayList<String>();

    public int getWtid()
    {
        return wtid;
    }

    public void setWtid(int wtid)
    {
        this.wtid = wtid;
    }

    public String getIecpath()
    {
        return iecpath;
    }

    public void setIecpath(String iecpath)
    {
        this.iecpath = iecpath;
    }

    public String getRectime()
    {
        return rectime;
    }

    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    public String getChangedata()
    {
        return changedata;
    }

    public void setChangedata(String changedata)
    {
        if ("true".equalsIgnoreCase(changedata))
        {
            this.changedata="1";
        }
        else if ("false".equalsIgnoreCase(changedata))
        {
            this.changedata="0";
        }
        else 
        {
            this.changedata = changedata;    
        }        
    }

    /**
     * @return the mineteList
     */
    public List<String> getMineteList()
    {
        return mineteList;
    }    
    
    /**
     * 变位值解析成bool值
     * @return
     */
    public boolean getIecValue()
    {
        String value = this.changedata;
        if ("true".equalsIgnoreCase(value))
        {
            value = "1";
        }
        else if ("false".equalsIgnoreCase(value))
        {
            value = "0";
        }
        return "1".equals(value);
    }
}
