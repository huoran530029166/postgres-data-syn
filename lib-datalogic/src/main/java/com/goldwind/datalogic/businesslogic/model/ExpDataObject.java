package com.goldwind.datalogic.businesslogic.model;

/**
 * 公式对象
 * 
 * @author 谭璟
 *
 */
public class ExpDataObject
{
    /**
     * 公式标识
     */
    private int setid;

    /**
     * 公式内容
     */
    private String formula;

    /**
     * 公式优先级
     */
    private int sort;
    
    private int typeid;

    
    /**
     * 公式对象
     * 
     * @param vSetID
     *            标识
     * @param vExpstr
     *            公式
     * @param vSort
     *            优先级
     */
    public ExpDataObject(int vSetID, String vExpstr, int vSort,int vTypeId)
    {
        this.setid = vSetID;
        this.formula = vExpstr;
        this.sort = vSort;
        this.typeid=vTypeId;
    }

    public int getSetid()
    {
        return setid;
    }

    public void setSetid(int index)
    {
        this.setid = index;
    }

    public String getFormula()
    {
        if (null == formula)
        {
            return "";
        }
        return formula;
    }

    public void setFormula(String expstr)
    {
        this.formula = expstr;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int expsort)
    {
        this.sort = expsort;
    }

    @Override
    public String toString()
    {
        return this.setid + "," + this.formula + "," + this.sort;
    }

    /**
     * @return the typeid
     */
    public int getTypeid()
    {
        return typeid;
    }

    /**
     * @param typeid the typeid to set
     */
    public void setTypeid(int typeid)
    {
        this.typeid = typeid;
    }
}
