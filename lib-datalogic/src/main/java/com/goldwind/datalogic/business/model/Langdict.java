package com.goldwind.datalogic.business.model;

/**
 * IEC语言描述对象
 * 
 * @author 霍然
 */
public class Langdict
{
    /**
     * 编号
     */
    private int id;
    /**
     * 表名称
     */
    private String tablename;
    /**
     * 字段名称
     */
    private String fieldname;
    /**
     * 字段值
     */
    private String fieldvalue;
    /**
     * 中文描述
     */
    private String descrcn;
    /**
     * 英文描述
     */
    private String descren;

    /**
     * 依据传进的参数获取中英文解释
     * 
     * @param language
     *            语言
     * @return 翻译好的IEC语言描述对象
     */
    public String getDescription(String language)
    {
        if ("en".equals(language))
        {
            return this.descren;
        }
        else
        {
            return this.descrcn;
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTablename()
    {
        return tablename;
    }

    public void setTablename(String tablename)
    {
        this.tablename = tablename;
    }

    public String getFieldname()
    {
        return fieldname;
    }

    public void setFieldname(String fieldname)
    {
        this.fieldname = fieldname;
    }

    public String getFieldvalue()
    {
        return fieldvalue;
    }

    public void setFieldvalue(String fieldvalue)
    {
        this.fieldvalue = fieldvalue;
    }

    public String getDescrcn()
    {
        return descrcn;
    }

    public void setDescrcn(String descrcn)
    {
        this.descrcn = descrcn;
    }

    public String getDescren()
    {
        return descren;
    }

    public void setDescren(String descren)
    {
        this.descren = descren;
    }

    /**
     * 覆写tostring
     * 
     * @return tostring
     */
    public String toString()
    {
        return "id:" + this.id + ";tablename:" + this.tablename + ";fieldname:" + this.fieldname + ";fieldvalue:" + this.fieldvalue + ";descrcn:" + this.descrcn + ";descren:" + this.descren;
    }
}
