package com.goldwind.datalogic.business.model;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.ControlProcessDef.NetDataTransType;

/**
 * 协议路径信息
 * 
 * @author 谭璟
 *
 */
public class ProPath
{
    private String pathunit;

    /**
     * iec路径
     */
    private String iecPath;

    /**
     * 指令数据包
     */
    private String comPath;

    /**
     * 数据标识
     */
    private int dataFlg;

    /**
     * 对应字段的偏移量
     */
    private String colOffsetVal;

    /**
     * 最小值
     */
    private String minValue;

    /**
     * 最大值
     */
    private String maxValue;

    /**
     * 是否检查
     */
    private boolean ncheck;

    /**
     * 是否保存
     */
    private boolean bsave;

    /**
     * 传输类型
     */
    private NetDataTransType transType;

    /**
     * 当前路径对应最小值路径的位置,当前路径如为path[max]、path[MAX]、path,此属性为path[min]、path[MIN]、path[min]在数组中的位置
     */
    private int minPos;

    /**
     * 当前路径对应最大值路径的位置,当前路径如为path[min]、path[MIN]、path,此属性为path[max]、path[MAX]、path[max]在数组中的位置
     */
    private int maxPos;

    /**
     * 中文描述
     */
    private String descrcn;

    /**
     * 英文描述
     */
    private String descren;

    /**
     * col_3
     */
    private String col3;

    public ProPath(String iecPath, String comPath, int dataFlg, String colOffsetVal, String minValue, String maxValue, boolean ncheck, boolean bsave, NetDataTransType transType, String descrcn,
            String descren, String col3) throws DataAsException
    {
        this.iecPath = iecPath;
        this.comPath = comPath;
        this.dataFlg = dataFlg;
        this.colOffsetVal = colOffsetVal;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.ncheck = ncheck;
        this.bsave = bsave;
        this.transType = transType;
        this.minPos = -1;
        this.maxPos = -1;
        this.descrcn = descrcn;
        this.descren = descren;
        this.col3 = col3;
    }

    public String getIecPath()
    {
        return iecPath;
    }

    public void setIecPath(String iecPath)
    {
        this.iecPath = iecPath;
    }

    public String getComPath()
    {
        return comPath;
    }

    public void setComPath(String comPath)
    {
        this.comPath = comPath;
    }

    public int getDataFlg()
    {
        return dataFlg;
    }

    public void setDataFlg(int dataFlg)
    {
        this.dataFlg = dataFlg;
    }

    public String getColOffsetVal()
    {
        return colOffsetVal;
    }

    public void setColOffsetVal(String colOffsetVal)
    {
        this.colOffsetVal = colOffsetVal;
    }

    public String getMinValue()
    {
        return minValue;
    }

    public void setMinValue(String minValue)
    {
        this.minValue = minValue;
    }

    public String getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
    }

    public boolean isNcheck()
    {
        return ncheck;
    }

    public void setNcheck(boolean ncheck)
    {
        this.ncheck = ncheck;
    }

    public boolean isBsave()
    {
        return bsave;
    }

    public void setBsave(boolean bsave)
    {
        this.bsave = bsave;
    }

    public NetDataTransType getTransType()
    {
        return transType;
    }

    public void setTransType(NetDataTransType transType)
    {
        this.transType = transType;
    }

    public int getMinPos()
    {
        return minPos;
    }

    public void setMinPos(int minPos)
    {
        this.minPos = minPos;
    }

    public int getMaxPos()
    {
        return maxPos;
    }

    public void setMaxPos(int maxPos)
    {
        this.maxPos = maxPos;
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
     * 依据语言翻译
     * 
     * @param language
     * @return
     */
    public String getDescr(String language)
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

    public String getCol3()
    {
        return col3;
    }

    public void setCol3(String col3)
    {
        this.col3 = col3;
    }

    /**
     * @return the pathunit
     */
    public String getPathunit()
    {
        return pathunit;
    }

    /**
     * @param pathunit
     *            the pathunit to set
     */
    public void setPathunit(String pathunit)
    {
        this.pathunit = pathunit;
    }
}
