package com.goldwind.datalogic.business.model;

/**
 * 控制系统分类实体类
 *
 * @author 冯春源
 */
public class ControlsysType
{
    /**
     * 系统分类的序号
     */
    private Integer id;
    /**
     * 中文描述
     */
    private String descrcn;
    /**
     * 英文描述
     */
    private String descren;

    public ControlsysType(Integer id, String descrcn, String descren)
    {
        this.id = id;
        this.descrcn = descrcn;
        this.descren = descren;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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
     * 依据传进的参数获取中英文解释
     * 
     * @param language
     *            语言
     * @return 翻译好的控制指令故障码
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

    @Override
    public String toString()
    {
        return "ControlsysType [id=" + id + ", descrcn=" + descrcn + ", descren=" + descren + "]";
    }
}
