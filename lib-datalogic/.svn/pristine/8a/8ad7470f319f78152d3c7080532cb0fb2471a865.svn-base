package com.goldwind.datalogic.utils;

import java.io.Serializable;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 用户数据单元
 * 
 * @author 曹阳
 *
 */
public class UserDataUnit implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 列名
     */
    private String col;
    /**
     * 值
     */
    private String val;
    /**
     * 时间标志
     */
    private boolean timeFlag;

    public UserDataUnit(String col, String val, boolean timeFlag)
    {
        this.col = (col == null) ? "" : col;
        this.val = (val == null) ? "" : val;
        this.timeFlag = timeFlag;
    }


    /**
     * 创建数据单元
     * 
     * @param descr
     *            数据单元字符串
     * @return 用户数据单元对象
     * @throws DataAsException
     *             自定义异常
     */
    public static UserDataUnit createUnit(String descr) throws DataAsException
    {
        UserDataUnit unit = null;
        try
        {
            String[] arr = ArrayOper.trimArray(descr.split("^"));
            String name = arr[0];
            String val = arr[1];
            boolean timeFlag = ("1".equals(arr[2])) ? true : false;
            unit = new UserDataUnit(name, val, timeFlag);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("UserDataUnit_createUnit_1", new String[] { "descr" }, new Object[] { descr }, exp);
        }
        return unit;
    }

    @Override
    public String toString()
    {
        return col + "^" + val + "^" + ((timeFlag) ? "1" : "0");
    }

    public String getCol()
    {
        return col;
    }

    public void setCol(String col)
    {
        this.col = col;
    }

    public String getVal()
    {
        return val;
    }

    public void setVal(String val)
    {
        this.val = val;
    }

    public boolean isTimeFlag()
    {
        return timeFlag;
    }

    public void setTimeFlag(boolean timeFlag)
    {
        this.timeFlag = timeFlag;
    }

}
