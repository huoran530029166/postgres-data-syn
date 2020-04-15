/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: BusinessFuncTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.business 
 * @Description: BusinessFuncTest
 * @author: Administrator   
 * @date: 2019年8月25日 下午8:35:02 
 * @version: V1.0   
 */
package com.goldwind.datalogic.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.datalogic.business.BusinessDef.ConditionType;
import com.goldwind.datalogic.business.BusinessDef.IecPathDataType;
import com.goldwind.datalogic.business.BusinessDef.IecPathElecType;

/**
 * @ClassName: BusinessFuncTest
 * @Description: BusinessFuncTest
 * @author: Administrator
 * @date: 2019年8月25日 下午8:35:02
 */
public class BusinessFuncTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeUnknown()
    {
        String iecpath = "WTUR.PwrAt";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Unknown;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeDigital_Control()
    {
        String iecpath = "WTRF.Bool.Wd.b0.0001";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Digital_Control;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeAnalog()
    {
        String iecpath = "WTPS.Temp.Ra.F32.ambient";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Analog;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeTotal()
    {
        String iecpath = "WTUR.PwrAt.Rt.F32";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Total;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeDigital()
    {
        String iecpath = "WTUR.PwrAt.Rd.F32";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Digital;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeDigital_Bilateral()
    {
        String iecpath = "WTUR.PwrAt.Rb.F32";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Digital_Bilateral;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeCalculation()
    {
        String iecpath = "WTUR.PwrAt.Rc.F32";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Calculation;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeString()
    {
        String iecpath = "WTUR.PwrAt.Rg.F32";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.String;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathElecType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathElecTypeTime()
    {
        String iecpath = "WTUR.PwrAt.Rw.Dt";
        IecPathElecType act = BusinessFunc.getIecPathElecType(iecpath);
        IecPathElecType exp = IecPathElecType.Time;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#isTimeElecType(java.lang.String)}.
     */
    @Test
    public void testIsTimeElecTypeByw()
    {
        String iecpath = "WTUR.Tm.Rw.Dt";
        boolean act = BusinessFunc.isTimeElecType(iecpath);
        Assert.assertTrue(act);
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#isTimeElecType(java.lang.String)}.
     */
    @Test
    public void testIsTimeElecTypeByDt()
    {
        String iecpath = "WTUR.Other.Rs.Dt.plc";
        boolean act = BusinessFunc.isTimeElecType(iecpath);
        Assert.assertTrue(act);
    }
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Unknown()
    {
        String col_3="";
        IecPathElecType exp = IecPathElecType.Unknown;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Analog()
    {
        String col_3="YC";
        IecPathElecType exp = IecPathElecType.Analog;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Digital()
    {
        String col_3="YX-DGH";
        IecPathElecType exp = IecPathElecType.Digital;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Digital_Control()
    {
        String col_3="YK";
        IecPathElecType exp = IecPathElecType.Digital_Control;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Digital_Adjust()
    {
        String col_3="YT";
        IecPathElecType exp = IecPathElecType.Digital_Adjust;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecElecTypeByCol3(java.lang.String)}.
     */
    @Test
    public void testGetIecElecTypeByCol3Total()
    {
        String col_3="YM";
        IecPathElecType exp = IecPathElecType.Total;
        IecPathElecType act=BusinessFunc.getIecElecTypeByCol3(col_3);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathDataType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathDataTypeUnknow()
    {
        IecPathDataType exp = IecPathDataType.unknow;
        String iecpath="";
        IecPathDataType act=BusinessFunc.getIecPathDataType(iecpath);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathDataType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathDataTypeInteger()
    {
        IecPathDataType exp = IecPathDataType.Integer;
        String iecpath="WTUR.Other.Ri.I16.bLitPow";
        IecPathDataType act=BusinessFunc.getIecPathDataType(iecpath);
        Assert.assertThat(act, Matchers.equalTo(exp));
        String iecpath1="WTUR.Other.Rn.U16.LubDate";
        IecPathDataType act1=BusinessFunc.getIecPathDataType(iecpath1);
        Assert.assertThat(act1, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathDataType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathDataTypeBool()
    {
        IecPathDataType exp = IecPathDataType.Bool;
        String iecpath="WCNV.Bool.Rd.b0.AcVola.Low";
        IecPathDataType act=BusinessFunc.getIecPathDataType(iecpath);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathDataType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathDataTypeDouble()
    {
        IecPathDataType exp = IecPathDataType.Double;
        String iecpath="WTUR.PwrAt.Ra.F32";
        IecPathDataType act=BusinessFunc.getIecPathDataType(iecpath);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
    
    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecPathDataType(java.lang.String)}.
     */
    @Test
    public void testGetIecPathDataTypeString()
    {
        IecPathDataType exp = IecPathDataType.String;
        String iecpath="WTUR.PwrAt.Ra.S";
        IecPathDataType act=BusinessFunc.getIecPathDataType(iecpath);
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getPowCurveSegment(com.goldwind.dataaccess.database.DbOperBase)}.
//     */
//    @Test
//    public void testGetPowCurveSegment()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getFaultRecorderFileList(int, java.util.Date, java.util.Date, java.lang.String, java.lang.String, int)}.
//     */
//    @Test
//    public void testGetFaultRecorderFileList()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getFaultRecorderDoc(int, java.lang.String, java.lang.String, java.lang.String, int)}.
//     */
//    @Test
//    public void testGetFaultRecorderDoc()
//    {
//        fail("Not yet implemented");
//    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getUpdateConditionSql(com.goldwind.datalogic.business.BusinessDef.ConditionType)}.
     */
    @Test
    public void testGetUpdateConditionSql()
    {
        String act=BusinessFunc.getUpdateConditionSql(ConditionType.dataCondition);
        String exp="name='dataCondition'";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getBetweenDates(java.util.Date, java.util.Date, java.lang.String)}.
     */
    @Test
    public void testGetBetweenDates()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 7, 20);
        Date start=ca.getTime();
        ca.set(2019, 7, 22);
        Date end=ca.getTime();
        String formatter=DataAsDef.DATEFORMATSTR;
        List<String> exp=new ArrayList<>();
        exp.add("2019-08-20");
        exp.add("2019-08-21");
        exp.add("2019-08-22");
        List<String> act=BusinessFunc.getBetweenDates(start, end, formatter);
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getBetweenDatesbyParam(java.util.Date, java.util.Date, int)}.
     */
    @Test
    public void testGetBetweenDatesbyParam()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 7, 20);
        Date start=ca.getTime();
        ca.set(2019, 7, 22);
        Date end=ca.getTime();
        List<String> exp=new ArrayList<>();
        exp.add("2019-08-20");
        exp.add("2019-08-21");
        List<String> act=BusinessFunc.getBetweenDatesbyParam(start, end, 6);
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getInitWfplanelecSql(java.lang.Integer, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testGetInitWfplanelecSql()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 7, 20);
        Date start=ca.getTime();
        ca.set(2019, 8, 22);
        Date end=ca.getTime();
        List<String> exp=new ArrayList<>();
        exp.add("insert into public.wfplanelec(wfid,rectime,datetype) values (300032,'2019-08-01 00:00:00',1);");
        exp.add("insert into public.wfplanelec(wfid,rectime,datetype) values (300032,'2019-09-01 00:00:00',1);");
        List<String> act=BusinessFunc.getInitWfplanelecSql(300032,start, end);
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getInitStageonpriceSql(java.lang.Integer, java.util.Date, java.util.Date)}.
     * @throws ParseException 异常
     */
    @Test
    public void testGetInitStageonpriceSql() throws ParseException
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 7, 20);
        Date start=ca.getTime();
        ca.set(2019, 8, 22);
        Date end=ca.getTime();
        List<String> exp=new ArrayList<>();
        exp.add("insert into config.stageonprice(wfid,onyear,onmonth) values (300032,2019,8);");
        exp.add("insert into config.stageonprice(wfid,onyear,onmonth) values (300032,2019,9);");
        List<String> act=BusinessFunc.getInitStageonpriceSql(300032,start, end);
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getInitWfcostlistSql(java.lang.Integer, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testGetInitWfcostlistSql()
    {
        Calendar ca=Calendar.getInstance();
        ca.set(2019, 7, 20);
        Date start=ca.getTime();
        ca.set(2019, 8, 22);
        Date end=ca.getTime();
        List<String> exp=new ArrayList<>();
        exp.add("insert into config.wfcostlist(wfid,costtypeid,costdate) select 300032 as wfid,id,'2019-08-01 00:00:00' as costdate from config.wfcosttype;");
        exp.add("insert into config.wfcostlist(wfid,costtypeid,costdate) select 300032 as wfid,id,'2019-09-01 00:00:00' as costdate from config.wfcosttype;");
        List<String> act=BusinessFunc.getInitWfcostlistSql(300032,start, end);
        Assert.assertArrayEquals(exp.toArray(), act.toArray());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getDelWfplanelecSql(java.lang.Integer)}.
     */
    @Test
    public void testGetDelWfplanelecSql()
    {
       
        String act=BusinessFunc.getDelWfplanelecSql(300032);
        String exp="delete from public.wfplanelec where wfid=300032";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getDelStageonpriceSql(java.lang.Integer)}.
     */
    @Test
    public void testGetDelStageonpriceSql()
    {
        String act=BusinessFunc.getDelStageonpriceSql(300032);
        String exp="delete from config.stageonprice where wfid=300032";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getDelWfcostlistSql(java.lang.Integer)}.
     */
    @Test
    public void testGetDelWfcostlistSql()
    {
        String act=BusinessFunc.getDelWfcostlistSql(300032);
        String exp="delete from config.wfcostlist where wfid=300032";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#singleIECToControlIEC(java.lang.String, java.util.List)}.
//     */
//    @Test
//    public void testSingleIECToControlIEC()
//    {
//        fail("Not yet implemented");
//    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#soeIECTonormalIEC(java.lang.String)}.
     */
    @Test
    public void testSoeIECTonormalIEC()
    {
        String soeIec="soe_WTUR.Other.Rn.U16.LubDate";
       String act=BusinessFunc.soeIECTonormalIEC(soeIec);
       String exp="WTUR.Other.Rn.U16.LubDate";
       Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#normalIECTosoeIEC(java.lang.String)}.
     */
    @Test
    public void testNormalIECTosoeIEC()
    {
        String soeIec="WTUR.Other.Rn.U16.LubDate";
        String act=BusinessFunc.normalIECTosoeIEC(soeIec);
        String exp="soe_WTUR.Other.Rn.U16.LubDate";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#getIecupdValue(int, java.lang.Double, int, double, double)}.
//     */
//    @Test
//    public void testGetIecupdValue()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for
//     * {@link com.goldwind.datalogic.business.BusinessFunc#isConformFilterRules(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
//     */
//    @Test
//    public void testIsConformFilterRules()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for
//     * {@link com.goldwind.datalogic.business.BusinessFunc#whetherToBlock(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
//     */
//    @Test
//    public void testWhetherToBlock()
//    {
//        fail("Not yet implemented");
//    }
//
//    /**
//     * Test method for {@link com.goldwind.datalogic.business.BusinessFunc#arrgCompute(java.util.ArrayList, com.goldwind.datalogic.utils.FactorDef.ArrgType)}.
//     */
//    @Test
//    public void testArrgCompute()
//    {
//        fail("Not yet implemented");
//    }

}
