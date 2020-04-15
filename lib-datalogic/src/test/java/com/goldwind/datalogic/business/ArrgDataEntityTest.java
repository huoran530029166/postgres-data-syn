/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: ArrgDataEntityTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.business 
 * @Description: ArrgDataEntityTest
 * @author: Administrator   
 * @date: 2019年8月24日 下午2:30:06 
 * @version: V1.0   
 */
package com.goldwind.datalogic.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.rtdb.model.DataEntity;

/** 
 * @ClassName: ArrgDataEntityTest 
 * @Description: ArrgDataEntityTest
 * @author: Administrator
 * @date: 2019年8月24日 下午2:30:06  
 */
public class ArrgDataEntityTest 
{

    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityValue(com.goldwind.dataaccess.rtdb.model.DataEntity)}.
     */
    @Test
    public void testGetDataEntityValue()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23");
        BigDecimal exp=new BigDecimal("12.23");
        BigDecimal act=ArrgDataEntity.getDataEntityValue(data);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityValue(com.goldwind.dataaccess.rtdb.model.DataEntity)}.
     */
    @Test(expected=NumberFormatException.class)
    public void testGetDataEntityValueException()
    {
        DataEntity data=new DataEntity();
        ArrgDataEntity.getDataEntityValue(data);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityValue(com.goldwind.dataaccess.rtdb.model.DataEntity)}.
     */
    @Test
    public void testGetDataEntityValueNull()
    {
        DataEntity data=null;
        BigDecimal act=ArrgDataEntity.getDataEntityValue(data);
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityAvg(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityAvgNull()
    {
        Map<String, DataEntity> datas=null;
        BigDecimal act=ArrgDataEntity.getDataEntityAvg(datas);
        Assert.assertNull(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityAvg(java.util.Map)}.
     */
    @Test(expected=NumberFormatException.class)
    public void testGetDataEntityAvgException()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23");
        DataEntity data2=new DataEntity();
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        ArrgDataEntity.getDataEntityAvg(datas);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityAvg(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityAvg()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23239");
        DataEntity data2=new DataEntity();
        data2.setValue("2.54239");
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        BigDecimal act=ArrgDataEntity.getDataEntityAvg(datas);
        BigDecimal exp=new BigDecimal("7.3874");
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntitySum(java.util.Map)}.
     */
    @Test
    public void testGetDataEntitySumNull()
    {
        Map<String, DataEntity> datas=null;
        BigDecimal act=ArrgDataEntity.getDataEntitySum(datas);
        Assert.assertNull(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntitySum(java.util.Map)}.
     */
    @Test(expected=NumberFormatException.class)
    public void testGetDataEntitySumException()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23");
        DataEntity data2=new DataEntity();
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        ArrgDataEntity.getDataEntitySum(datas);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntitySum(java.util.Map)}.
     */
    @Test
    public void testGetDataEntitySum()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23239");
        DataEntity data2=new DataEntity();
        data2.setValue("2.54239");
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        BigDecimal act=ArrgDataEntity.getDataEntitySum(datas);
        BigDecimal exp=new BigDecimal("14.77478");
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMax(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityMaxNull()
    {
        Map<String, DataEntity> datas=new HashMap<>();
        BigDecimal act=ArrgDataEntity.getDataEntityMax(datas);
        Assert.assertNull(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMax(java.util.Map)}.
     */
    @Test(expected=NumberFormatException.class)
    public void testGetDataEntityMaxException()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23");
        DataEntity data2=new DataEntity();
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        ArrgDataEntity.getDataEntityMax(datas);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMax(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityMax()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23239");
        DataEntity data2=new DataEntity();
        data2.setValue("2.54239");
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        BigDecimal act=ArrgDataEntity.getDataEntityMax(datas);
        BigDecimal exp=new BigDecimal("12.23239");
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMin(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityMinNull()
    {
        Map<String, DataEntity> datas=new HashMap<>();
        BigDecimal act=ArrgDataEntity.getDataEntityMin(datas);
        Assert.assertNull(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMin(java.util.Map)}.
     */
    @Test(expected=NumberFormatException.class)
    public void testGetDataEntityMinException()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23");
        DataEntity data2=new DataEntity();
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        ArrgDataEntity.getDataEntityMin(datas);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.ArrgDataEntity#getDataEntityMin(java.util.Map)}.
     */
    @Test
    public void testGetDataEntityMin()
    {
        DataEntity data=new DataEntity();
        data.setValue("12.23239");
        DataEntity data2=new DataEntity();
        data2.setValue("2.54239");
        Map<String, DataEntity> datas=new HashMap<String, DataEntity>();
        datas.put("1", data);
        datas.put("2", data2);
        BigDecimal act=ArrgDataEntity.getDataEntityMin(datas);
        BigDecimal exp=new BigDecimal("2.54239");
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

}
