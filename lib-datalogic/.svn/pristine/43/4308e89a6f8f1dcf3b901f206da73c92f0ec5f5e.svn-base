package com.goldwind.datalogic.businesslogic.eba;

import java.util.ArrayList;
import java.util.List;

/**
 * 损失电量 抽象类
 * 
 * @author 谭璟
 *
 */
public abstract class AbEbaLossHandle
{
    /**
     * 风速仪集合
     */
    protected List<String> lifalut = new ArrayList<>();

    public List<String> getLifalut()
    {
        return lifalut;
    }

    public AbEbaLossHandle()
    {
        lifalut.add("130");
        lifalut.add("131");
    }

    /**
     * 具体损失电量算法实现
     * 
     * @throws Exception
     *             异常
     */
    protected abstract void getDeivceLossHandle() throws Exception;

    public void ebaLossHandle() throws Exception
    {
        getDeivceLossHandle();
    }
}
