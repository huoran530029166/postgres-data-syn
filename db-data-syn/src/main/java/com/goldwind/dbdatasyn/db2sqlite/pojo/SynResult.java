package com.goldwind.dbdatasyn.db2sqlite.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 同步结果对象
 *
 * @author huoran
 */
public class SynResult
{
    /**
     * 成功标志
     */
    private boolean reuslt;
    /**
     * 同步数据错误信息 key:表名;key:行数,value:异常
     */
    private Map<String, HashMap<Integer, Exception>> errs;

    public boolean isReuslt()
    {
        return reuslt;
    }

    public void setReuslt(boolean reuslt)
    {
        this.reuslt = reuslt;
    }

    public Map<String, HashMap<Integer, Exception>> getErrs()
    {
        return errs;
    }

    public void setErrs(Map<String, HashMap<Integer, Exception>> errs)
    {
        this.errs = errs;
    }

    @Override public String toString()
    {
        return "SynResult{" + "reuslt=" + reuslt + ", errs=" + errs + '}';
    }
}
