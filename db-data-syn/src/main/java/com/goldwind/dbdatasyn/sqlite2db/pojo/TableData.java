package com.goldwind.dbdatasyn.sqlite2db.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 表数据 类
 *
 * @author huoran
 */
public class TableData
{
    /**
     * 数据集
     * key:主键数据字符串 多主键之间使用@sep@分割 value:单行数据
     * value key:列名 value value:该列的值
     */
    private Map<String, Map<String, String>> datas = new HashMap<>();

    /**
     * 数据有效性标志,如果查询数据异常,则为false,记录日志,但是不阻塞同步流程
     * 默认为true
     */
    private boolean effectFlag = true;

    public boolean isEffectFlag()
    {
        return effectFlag;
    }

    public void setEffectFlag(boolean effectFlag)
    {
        this.effectFlag = effectFlag;
    }

    public Map<String, Map<String, String>> getDatas()
    {
        return datas;
    }

    public void setDatas(Map<String, Map<String, String>> datas)
    {
        this.datas = datas;
    }

    @Override public String toString()
    {
        return "TableData{" + "datas=" + datas + ", effectFlag=" + effectFlag + '}';
    }
}