package com.goldwind.dbdatasyn.sqlite2db.pojo;

import com.goldwind.dbdatasyn.sqlite2db.utils.Enums;

/**
 * 本地库同步到关系库的同步结果对象
 *
 * @author huoran
 */
public class SynResult
{
    /**
     * 同步结果
     */
    private Enums.SynResultEnum result;
    /**
     * 结果描述
     */
    private String msg;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Enums.SynResultEnum getResult()
    {
        return result;
    }

    public void setResult(Enums.SynResultEnum result)
    {
        this.result = result;
    }

    @Override public String toString()
    {
        return "SynResult{" + "result=" + result + ", msg='" + msg + '\'' + '}';
    }
}
