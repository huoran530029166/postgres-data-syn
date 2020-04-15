/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataAssembleTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: DataAssemble单元测试
 * @author: Administrator   
 * @date: 2019年8月9日 下午1:09:14 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.DataAsDef.SystemPromptType;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.ControlProcessDef.ReturnType;
import com.goldwind.datalogic.utils.NetCommDef.CommState;
import com.goldwind.datalogic.utils.NetCommDef.PlcFileType;
import com.goldwind.datalogic.utils.NetCommDef.ServiceManageType;
import com.goldwind.datalogic.utils.NetCommDef.TableDataOperType;
import com.goldwind.datalogic.utils.NetCommDef.TaskCycType;

/**
 * @ClassName: DataAssembleTest
 * @Description: DataAssemble单元测试
 * @author Administrator
 * @date: 2019年8月9日 下午1:09:14
 */
public class DataAssembleTest
{

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#addSrvTaskAsm(java.lang.String, java.lang.String, java.lang.String[], int, int, java.lang.String, java.lang.String[], com.goldwind.datalogic.utils.NetCommDef.TaskCycType, java.util.Date)}.
     */
    @Test
    public void testAddSrvTaskAsm()
    {
        String userId = "userId";
        String monitorId = "monitorId";
        String[] deviceList = { "3000001", "3000002", "3000003" };
        int proId = 8801;
        int orderType = 1;
        String orderFlg = "orderFlg";
        String[] paramList = { "1", "2" };
        TaskCycType cycType = TaskCycType.OnlyOnce;
        Calendar ca = Calendar.getInstance();
        ca.set(2019, 0, 12, 12, 0, 0);
        Date beginTime = ca.getTime();
        String act = DataAssemble.addSrvTaskAsm(userId, monitorId, deviceList, proId, orderType, orderFlg, paramList, cycType, beginTime);
        String exp = "addtask(userId|monitorId|3000001,3000002,3000003|8801|1.orderFlg|1,2|0|2019-01-12 12:00:00)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#delSrvTaskAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testDelSrvTaskAsm()
    {
        String taskId = "taskId";
        String userId = "userId";
        String act = DataAssemble.delSrvTaskAsm(taskId, userId);
        String exp = "deltask(taskId|userId)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#stopSrvTaskAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testStopSrvTaskAsm()
    {
        String taskId = "taskId";
        String userId = "userId";
        String act = DataAssemble.delSrvTaskAsm(taskId, userId);
        String exp = "deltask(taskId|userId)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#srvHisDataComStaAsm(int, com.goldwind.datalogic.utils.NetCommDef.CommState)}.
     */
    @Test
    public void testSrvHisDataComStaAsm()
    {
        int deviceId = 600012;
        CommState state = CommState.ComError;
        String act = DataAssemble.srvHisDataComStaAsm(deviceId, state);
        String exp = "(hisdatacomstate|600012|7)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#largeDataAsm(java.lang.String, int, boolean)}.
     */
    @Test
    public void testLargeDataAsm()
    {
        String dataName = "dataName";
        int packCount = 10;
        // 测试为false的情况
        boolean zipData = false;
        String act = DataAssemble.largeDataAsm(dataName, packCount, zipData);
        String exp = "(largedata|dataName|10|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#largeDataAsm(java.lang.String, int, boolean)}.
     */
    @Test
    public void testLargeDataAsm1()
    {
        String dataName = "dataName";
        int packCount = 10;
        // 测试为true的情况
        boolean zipData = true;
        String act = DataAssemble.largeDataAsm(dataName, packCount, zipData);
        String exp = "(largedata|dataName|10|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#systemPromptAsm(com.goldwind.dataaccess.DataAsDef.SystemPromptType, java.lang.String)}.
     */
    @Test
    public void testSystemPromptAsm()
    {
        SystemPromptType netPromptType = SystemPromptType.DataSpaceNotEnough;
        String descr = "descr";
        String act = DataAssemble.systemPromptAsm(netPromptType, descr);
        String exp = "(prompt|0|descr)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#serverInfoAsm(java.lang.String)}.
     */
    @Test
    public void testServerInfoAsm()
    {
        String ip = "127.0.0.1";
        String act = DataAssemble.serverInfoAsm(ip);
        String exp = "(serverinfo|127.0.0.1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#netTestDataAsm(int)}.
     */
    @Test
    public void testNetTestDataAsm()
    {
//        String act = DataAssemble.netTestDataAsm(12);
//        Assert.assertThat(act, Matchers.anything());
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#sedimentDataAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSedimentDataAsm()
    {
        String data = "2019-08-09";
        String dataTime = "12:23";
        String act = DataAssemble.sedimentDataAsm(data, dataTime);
        String exp = "(sediment|12:23|2019-08-09)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#deviceDayDataAsm(java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testDeviceDayDataAsm()
    {
        String deviceId = "300016";
        String[] datas = { "12", "13" };
        String act = DataAssemble.deviceDayDataAsm(deviceId, datas);
        String exp = "(daydata|300016|12,13)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#userDataAsm(com.goldwind.datalogic.utils.NetCommDef.TableDataOperType, java.lang.String, java.lang.String, java.util.List, java.util.List)}.
     */
    @Test
    public void testUserDataAsm()
    {
        TableDataOperType operType = TableDataOperType.Del;
        String tableName = "tableName";
        String wfId = "wfId";
        List<UserDataUnit> unitList1 = null;
        List<UserDataUnit> unitList2 = null;
        String act = DataAssemble.userDataAsm(operType, tableName, wfId, unitList1, unitList2);
        String exp = "(userdata|del|tableName|wfId||)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#userDataToUpUserData(java.lang.String)}.
     */
    @Test
    public void testUserDataToUpUserData()
    {
        String data = "(userdata|783)";
        String act = DataAssemble.userDataToUpUserData(data);
        String exp = "(upuserdata|783)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#upUserDataToUserData(java.lang.String)}.
     */
    @Test
    public void testUpUserDataToUserData()
    {
        String data = "(upuserdata|783)";
        String act = DataAssemble.upUserDataToUserData(data);
        String exp = "(userdata|783)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#upSqlDataToSqlData(java.lang.String)}.
     */
    @Test
    public void testUpSqlDataToSqlData()
    {
        String data = "(upsqldata|783)";
        String act = DataAssemble.upSqlDataToSqlData(data);
        String exp = "(sqldata|783)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#upSqlDataAsm(java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testUpSqlDataAsm() throws DataAsException
    {
        String tableName = "tenData";
        String wfId = "600001";
        String sql = "select * from tenData";
        String act = DataAssemble.upSqlDataAsm(tableName, wfId, sql);
        String exp = "(upsqldata|tenData|600001|select * from tenData)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getDevMainDataAsm(int)}.
     */
    @Test
    public void testGetDevMainDataAsm()
    {
        String act = DataAssemble.getDevMainDataAsm(30000123);
        String exp = "getwman(30000123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getDevPackDataAsm(int, java.lang.String, int)}.
     */
    @Test
    public void testGetDevPackDataAsmIntStringInt()
    {
        String act = DataAssemble.getDevPackDataAsm(30000123, "packName", 1);
        String exp = "getpackdata(packName|30000123|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getDevPackDataAsm(int, java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testGetDevPackDataAsmIntStringIntString()
    {
        String act = DataAssemble.getDevPackDataAsm(30000123, "packName", 1, "controlpath");
        String exp = "getpackdata(packName|30000123|1|controlpath)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getDevDataAsm(int, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testGetDevDataAsm()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.getDevDataAsm(30000123, "packName", arrayStr);
        String exp = "getdevicedata(packName|30000123|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getDevCacheDataAsm(int, java.lang.String[])}.
     */
    @Test
    public void testGetDevCacheDataAsm()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.getDevCacheDataAsm(30000123, arrayStr);
        String exp = "getcachedata(30000123|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPlanOrderAsm(int[], java.lang.String[])}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testGetPlanOrderAsm() throws DataAsException
    {
        String[] arrayStr = { "1", "2" };
        int[] arrayInt = { 1, 2 };
        String act = DataAssemble.getPlanOrderAsm(arrayInt, arrayStr);
        String exp = "getplanorder(1.1,2.2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPlanDataAsm(int, java.lang.String)}.
     */
    @Test
    public void testGetPlanDataAsmIntString()
    {
        String act = DataAssemble.getPlanDataAsm(1, "arrayStr");
        String exp = "getplandata(1|arrayStr)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPlanDataAsm(java.lang.String)}.
     */
    @Test
    public void testGetPlanDataAsmString()
    {
        String act = DataAssemble.getPlanDataAsm("arrayStr");
        String exp = "getplandatanoid(arrayStr)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testDevControlOrderAsmStringStringStringStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderAsm("deviceIds", "orderFlg", "123", arrayStr, "234", ReturnType.Syn);
        String exp = "wtcontrolorder(deviceIds|orderFlg|123|1,2|234|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testDevControlOrderAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderAsmNS("123", "orderFlg", "234", arrayStr);
        String exp = "wtcontrolorder(123|orderFlg|234|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testDevControlOrderUUIDAsmStringStringStringStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderUUIDAsm("123", "orderFlg", "234", arrayStr, "uuid", ReturnType.Asyn);
        String exp = "wtcontrolorder(123|orderFlg|234|1,2|uuid|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderUUIDAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testDevControlOrderUUIDAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderUUIDAsmNS("123", "orderFlg", "234", arrayStr);
        String exp = "wtcontrolorder(123|orderFlg|234|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testDevControlOrderAsmStringStringStringStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderAsm("123", "orderFlg", "234", arrayStr, "1211", ReturnType.Syn);
        String exp = "wtcontrolorder(123|orderFlg|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devControlOrderUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testDevControlOrderUUIDAsmStringStringStringStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devControlOrderUUIDAsm("123", "orderFlg", "234", arrayStr, "1211", ReturnType.Syn);
        String exp = "wtcontrolorder(123|orderFlg|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testDevOtherCtrlOrderAsmStringStringStringStringArrayStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderAsm("123", "orderFlg", "234", arrayStr, arrayStr, "1211", ReturnType.Syn);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[])}.
     */
    @Test
    public void testDevOtherCtrlOrderAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderAsmNS("123", "orderFlg", "234", arrayStr, arrayStr);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testDevOtherCtrlOrderUUIDAsmStringStringStringStringArrayStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderUUIDAsm("123", "orderFlg", "234", arrayStr, arrayStr, "1211", ReturnType.Syn);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderUUIDAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[])}.
     */
    @Test
    public void testDevOtherCtrlOrderUUIDAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderUUIDAsmNS("123", "orderFlg", "234", arrayStr, arrayStr);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testDevOtherCtrlOrderAsmStringStringStringStringArrayStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderAsm("123", "orderFlg", "234", arrayStr, arrayStr, "1211", ReturnType.Syn);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#devOtherCtrlOrderUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testDevOtherCtrlOrderUUIDAsmStringStringStringStringArrayStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.devOtherCtrlOrderUUIDAsm("123", "orderFlg", "234", arrayStr, arrayStr, "1211", ReturnType.Syn);
        String exp = "wtothercontrolorder(123|orderFlg|1,2|234|1,2|1211|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#tomessageByCtr(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testTomessageByCtrIntStringStringStringStringArrayStringArrayStringStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.tomessageByCtr(123, "orderFlg", "234", "444", arrayStr, arrayStr, "1211", "11", ReturnType.Syn);
        String exp = "(wtcontrol|123|orderFlg|234|444|1,2|1,2|1211|11|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#tomessageByCtr(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String)}.
     */
    @Test
    public void testTomessageByCtrIntStringStringStringStringArrayStringArrayString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.tomessageByCtr(123, "orderFlg", "234", "444", arrayStr, arrayStr, "1211", "11", ReturnType.Syn);
        String exp = "(wtcontrol|123|orderFlg|234|444|1,2|1,2|1211|11|0)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#tomessageByCtrNS(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String[], java.lang.String)}.
     */
    @Test
    public void testTomessageByCtrNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.tomessageByCtrNS(123, "orderFlg", "234", "444", arrayStr, arrayStr, "1211");
        String exp = "(wtcontrol|123|orderFlg|234|444|1,2|1,2|1211)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testSetDevDataValuesAsmStringStringStringStringStringStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesAsm("123", "orderFlg", "234", "444", "445", arrayStr, "1", ReturnType.Asyn);
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2|1|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSetDevDataValuesAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesAsmNS("123", "orderFlg", "234", "444", "445", arrayStr);
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testSetDevDataValuesUUIDAsmStringStringStringStringStringStringArrayStringReturnType()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesUUIDAsm("123", "orderFlg", "234", "444", "445", arrayStr, "666", ReturnType.Asyn);
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2|666|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesUUIDAsmNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSetDevDataValuesUUIDAsmNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesUUIDAsmNS("123", "orderFlg", "234", "444", "445", arrayStr);
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testSetDevDataValuesAsmStringStringStringStringStringStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesAsm("123", "orderFlg", "234", "444", "445", arrayStr, "555", ReturnType.Asyn, "666");
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2|555|1|666)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setDevDataValuesUUIDAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType, java.lang.String)}.
     */
    @Test
    public void testSetDevDataValuesUUIDAsmStringStringStringStringStringStringArrayStringReturnTypeString()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevDataValuesUUIDAsm("123", "orderFlg", "234", "444", "445", arrayStr, "555", ReturnType.Asyn, "666");
        String exp = "setdatavalues(123|orderFlg|234|444|445|1,2|555|1|666)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#setDevMultDataValAsm(java.lang.String, java.lang.String[], java.lang.String[])}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testSetDevMultDataValAsm() throws DataAsException
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevMultDataValAsm("123", arrayStr, arrayStr);
        String exp = "setmultdatavalues(123|1=1,2=2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#setDevOthDataValAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSetDevOthDataValAsm()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setDevOthDataValAsm("123", "222", "333", arrayStr);
        String exp = "setotherdatavalues(123|222|333|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#writePreConfigAsm(java.lang.String)}.
     */
    @Test
    public void testWritePreConfigAsm()
    {
        String act = DataAssemble.writePreConfigAsm("123");
        String exp = "wirteconfig(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#writePreIniAsm(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testWritePreIniAsm()
    {
        String act = DataAssemble.writePreIniAsm("123", "222", "333");
        String exp = "writeini(123|222|333)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getPlcFileNameAsm(java.lang.String, java.util.Date, java.util.Date, com.goldwind.datalogic.utils.NetCommDef.PlcFileType, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testGetPlcFileNameAsm() throws DataAsException
    {
        Calendar ca = Calendar.getInstance();
        ca.set(2019, 7, 16);
        Date date = ca.getTime();
        String act = DataAssemble.getPlcFileNameAsm("123", date, date, PlcFileType.Action, "222", "333", "444", "555", 55);
        String exp = "getplcfilename(123|2019-08-16|2019-08-16|5|222|333|444)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getPlcFileDataAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testGetPlcFileDataAsm() throws DataAsException
    {
        String act = DataAssemble.getPlcFileDataAsm("123", "222", "333", "444", "555", "666", 55);
        String exp = "getplcfiledata(123|222|333|444|555)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#restartPreAsm(java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testRestartPreAsm()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.restartPreAsm("123", arrayStr);
        String exp = "resetservice(123|1|2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreUpgradeDirAsm()}.
     */
    @Test
    public void testGetPreUpgradeDirAsm()
    {
        String act = DataAssemble.getPreUpgradeDirAsm();
        String exp = "getsharedir()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#readPreIniAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testReadPreIniAsm()
    {
        String act = DataAssemble.readPreIniAsm("111", "222");
        String exp = "readini(111|222)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreIniAsm(java.lang.String)}.
     */
    @Test
    public void testGetPreIniAsm()
    {
        String act = DataAssemble.getPreIniAsm("111");
        String exp = "getini(111)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#preFileCopyAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testPreFileCopyAsm()
    {
        String act = DataAssemble.preFileCopyAsm("111", "222");
        String exp = "filecopy(111|222)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#preAccessUpdateAsm(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testPreAccessUpdateAsm()
    {
        String act = DataAssemble.preAccessUpdateAsm("111", "222");
        String exp = "access(111|222)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#readPreFileDataAsm(int)}.
     */
    @Test
    public void testReadPreFileDataAsm()
    {
        String act = DataAssemble.readPreFileDataAsm(123);
        String exp = "rptdataread(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#delPreFileDataAsm(int)}.
     */
    @Test
    public void testDelPreFileDataAsm()
    {
        String act = DataAssemble.delPreFileDataAsm(123);
        String exp = "rptdatadel(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreFileRowCount()}.
     */
    @Test
    public void testGetPreFileRowCount()
    {
        String act = DataAssemble.getPreFileRowCount();
        String exp = "rptdatareadcount()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#readPreRealtimeData(int)}.
     */
    @Test
    public void testReadPreRealtimeData()
    {
        String act = DataAssemble.readPreRealtimeData(123);
        String exp = "realtimedataread(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#delPreRealtimeData(int)}.
     */
    @Test
    public void testDelPreRealtimeData()
    {
        String act = DataAssemble.delPreRealtimeData(123);
        String exp = "realtimedatadel(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreRTRowCount()}.
     */
    @Test
    public void testGetPreRTRowCount()
    {
        String act = DataAssemble.getPreRTRowCount();
        String exp = "realtimedatareadcount()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreDiskSpaceAsm(java.lang.String)}.
     */
    @Test
    public void testGetPreDiskSpaceAsm()
    {
        String act = DataAssemble.getPreDiskSpaceAsm("123");
        String exp = "getdiskspace(123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreCpuAsm()}.
     */
    @Test
    public void testGetPreCpuAsm()
    {
        String act = DataAssemble.getPreCpuAsm();
        String exp = "getcpu()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreMemoryAsm()}.
     */
    @Test
    public void testGetPreMemoryAsm()
    {
        String act = DataAssemble.getPreMemoryAsm();
        String exp = "getmemory()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreVersionAsm()}.
     */
    @Test
    public void testGetPreVersionAsm()
    {
        String act = DataAssemble.getPreVersionAsm();
        String exp = "getversion()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getPreTimeAsm()}.
     */
    @Test
    public void testGetPreTimeAsm()
    {
        String act = DataAssemble.getPreTimeAsm();
        String exp = "getdatetime()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#preTestAsm()}.
     */
    @Test
    public void testPreTestAsm()
    {
        String act = DataAssemble.preTestAsm();
        String exp = "comtest()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#serviceTestAsm(java.lang.String, int)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testServiceTestAsm() throws DataAsException
    {
        String act = DataAssemble.serviceTestAsm("111", 123);
        String exp = "servicetest()";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#serviceManageAsm(java.lang.String, int, com.goldwind.datalogic.utils.NetCommDef.ServiceManageType, java.lang.String[])}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testServiceManageAsm() throws DataAsException
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.serviceManageAsm("111", 123, ServiceManageType.RESTARTSERVICE, arrayStr);
        String exp = "servicemanage(1|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#sendDevCommonOrder(int, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSendDevCommonOrder()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.sendDevCommonOrder(111, "123", "222", arrayStr);
        String exp = "222(111|123|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#sendPreCommonOrder(java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSendPreCommonOrder()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.sendPreCommonOrder("111", "123", arrayStr);
        String exp = "123(111|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#sendFileOrderAsm(java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testSendFileOrderAsm()
    {
        String act = DataAssemble.sendFileOrderAsm("111", 123, "123");
        String exp = "(sendfile|111|123|123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#saveFileOrderAsm(java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)}.
     */
    @Test
    public void testSaveFileOrderAsm()
    {
        String act = DataAssemble.saveFileOrderAsm("111", "123", "123", 2, "2222");
        String exp = "(savefile|111|123|123|2|2222)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#downloadFile(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testDownloadFile()
    {
        String act = DataAssemble.downloadFile("111", "123", "123");
        String exp = "(downloadfile|111|123|123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#groupOrdToSocketOrd(java.lang.String)}.
     */
    @Test
    public void testGroupOrdToSocketOrd()
    {
        String act = DataAssemble.groupOrdToSocketOrd("111222223333");
        String exp = "333";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#socketOrdToGroupOrd(java.lang.String)}.
     */
    @Test
    public void testSocketOrdToGroupOrd()
    {
        String act = DataAssemble.socketOrdToGroupOrd("123");
        String exp = "broadcast123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#dataAsmArray(java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testDataAsmArray()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.dataAsmArray("123", arrayStr);
        String exp = "1231,2";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#downDataAsmIp(java.lang.String, java.lang.String, int)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testDownDataAsmIp() throws DataAsException
    {
        String act = DataAssemble.downDataAsmIp("123", "127.0.0.1:8804,123.23.9.12", 6);
        String exp = "123(123.23.9.12:6)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#analysisRealTimeOrder(java.util.Date, java.util.Date, java.util.List)}.
     */
    @Test
    public void testAnalysisRealTimeOrder()
    {
        Calendar ca = Calendar.getInstance();
        ca.set(2019, 7, 16, 12, 8, 1);
        Date date = ca.getTime();
        ca.set(2019, 8, 16, 12, 8, 1);
        Date dateE = ca.getTime();
        List<String> l = new ArrayList<String>();
        String act = DataAssemble.analysisRealTimeOrder(date, dateE, l);
        String exp = "(getdspsrvdata|realtimedata|2019-08-16 12:08:01|2019-09-16 12:08:01)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#ebaExcludOrder(int, java.util.List)}.
     */
    @Test
    public void testEbaExcludOrder()
    {
        List<String> l = new ArrayList<String>();
        l.add("aa");
        String act = DataAssemble.ebaExcludOrder(4, l);
        String exp = "(excludcnf|null||{aa})";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getClassDataAsm(int, java.lang.String, int)}.
     */
    @Test
    public void testGetClassDataAsm()
    {
        String act = DataAssemble.getClassDataAsm(4, "ll", 5);
        String exp = "getclassdata(4|ll|5)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getWavefileList(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWavefileList()
    {
        String act = DataAssemble.getWavefileList("4", "ll", "5");
        String exp = "getwavefilelist(4|ll|5)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getWavefileData(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWavefileData()
    {
        String act = DataAssemble.getWavefileData("ll", "5");
        String exp = "getwavefiledata(ll|5)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#runlog2warnlog(java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testRunlog2warnlog() throws DataAsException
    {
        String act = DataAssemble.runlog2warnlog("5|3|23|31|30|2|32");
        String exp = "(warnlog|3|0|31|30|2|32|null|0||23)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#warnlog2runlog(java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testWarnlog2runlog() throws DataAsException
    {
        String act = DataAssemble.warnlog2runlog("5|3|23|31|30|2|32|34|23");
        String exp = "(runlog|3|null|31|30|2|32|34|)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setProtectDataAsm(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSetProtectDataAsm()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setProtectDataAsm("1", "2", "3", "4", "5", arrayStr);
        String exp = "setprotectdata(1|2|3|4|5|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#getprotectdata(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetprotectdataStringStringStringString()
    {
        String act = DataAssemble.getprotectdata("1", "2", "3", "4");
        String exp = "getprotectdata(1|2|3|4)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setprotectdata(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSetprotectdata()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setprotectdata("setid", "userid", "settype", "setmodel", "statid", "devid", "cpuid", "regionid", "1", "grouptype", "2", "valuetype", "value", arrayStr, "uuid",
                "controlpath");
        String exp = "setprotectdata(setid|userid|settype|setmodel|statid|devid|cpuid|regionid|1|grouptype|2|valuetype|value|1,2|uuid|controlpath)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#setprotectdataNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])}.
     */
    @Test
    public void testSetprotectdataNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.setprotectdataNS("setid", "userid", "settype", "setmodel", "statid", "devid", "cpuid", "regionid", "1", "grouptype", "2", "valuetype", "value", arrayStr);
        String exp = "setprotectdata(setid|userid|settype|setmodel|statid|devid|cpuid|regionid|1|grouptype|2|valuetype|value|1,2)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#protectsetlog(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testProtectsetlog()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.protectsetlog("setid", "settype", "setmodel", "setid", "userid", "settype", "setmodel", "statid", "devid", "cpuid", "regionid", "1", "cpuid", "regionid", "1",
                "grouptype", arrayStr, "1", "2", "3");
        String exp = "(protectsetlog|setid|settype|setmodel|setid|userid|settype|setmodel|statid|devid|cpuid|regionid|1|cpuid|regionid|1|grouptype|1,2|1|2|3)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#protectsetlogNS(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testProtectsetlogNS()
    {
        String[] arrayStr = { "1", "2" };
        String act = DataAssemble.protectsetlogNS("setid", "settype", "setmodel", "setid", "userid", "settype", "setmodel", "statid", "devid", "cpuid", "regionid", "1", "cpuid", "regionid", "1",
                "grouptype", arrayStr, "1", "2", "3", "4");
        String exp = "(protectsetlog|setid|settype|setmodel|setid|userid|settype|setmodel|statid|devid|cpuid|regionid|1|cpuid|regionid|1|grouptype|1,2|1|2|3|4)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#falutdata2warnlog(java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testFalutdata2warnlog() throws DataAsException
    {
        String act = DataAssemble.falutdata2warnlog("falutdata|37|84|23", "settype", "setmodel");
        String exp = "(warnlog|1|2|settype|setmodel|null|84|null|1||)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#alarmdata2warnlog(java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testAlarmdata2warnlog() throws DataAsException
    {
        String act = DataAssemble.alarmdata2warnlog("alarmdata|37|84|23|123", "2019-08-17", "320029");
        String exp = "(warnlog|1|2|2019-08-17|320029|null|84|null|1||)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#eventdata2warnlog(java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testEventdata2warnlog() throws DataAsException
    {
        String act = DataAssemble.eventdata2warnlog("eventdata|37|84|23|123", "2019-08-17", "320029");
        String exp = "(warnlog|1|0|2019-08-17|320029|null|84|null|0||)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getprotectdata(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String[], int, int, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetprotectdataIntStringStringStringStringStringStringStringIntStringArrayIntIntStringString()
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.getprotectdata(123, "callid", "123", "12", "3000232", "123", "123", "12", 123, infos, ReturnType.Asyn.getResult(), 123, "save");
        String exp = "getprotectdata(123|callid|123|12|3000232|123|123|12|123|12,23,34|1|123|save)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getprotectdata(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], int, int, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetprotectdataIntStringStringStringStringStringStringStringStringStringArrayIntIntStringStringString() throws Exception
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.getprotectdata(123, "callid", "123", "12", "3000232", "123", "123", "12", "123", infos, ReturnType.Asyn.getResult(), 123, "save", "1", "23");
        String exp = "getprotectdata(123|callid|123|12|3000232|123|123|12|123|12,23,34|1|123|save|1|23)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getprotectdata(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String[], int, int, java.lang.String)}.
     * 
     * @throws Exception
     */
    @Test
    public void testGetprotectdataIntStringStringStringStringStringStringStringIntStringArrayIntIntString() throws Exception
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.getprotectdata(123, "callid", "123", "12", "3000232", "123", "123", "12", "123", infos, ReturnType.Asyn.getResult(), 123, "save", "1", "23");
        String exp = "getprotectdata(123|callid|123|12|3000232|123|123|12|123|12,23,34|1|123|save|1|23)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#getprotectdataNS(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], int, int, java.lang.String)}.
     */
    @Test
    public void testGetprotectdataNS()
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.getprotectdataNS(123, "callid", "123", "12", "3000232", "123", "123", "12", "123", infos, ReturnType.Asyn.getResult(), 123, "save");
        String exp = "getprotectdata(123|callid|123|12|3000232|123|123|12|123|12,23,34|1|123|save)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#protectcalllog(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testProtectcalllog()
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.protectcalllog(123, "callid", "123", "12", "3000232", 123, "1", "2", "123", "123", "12", "123", infos, ReturnType.Asyn.getResult(), 123, "save", "1", "2", "3");
        String exp = "(protectcalllog|123|callid|123|12|3000232|123|1|2|123|123|12|123|12,23,34|1|123|save|1|2|3)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for
     * {@link com.goldwind.datalogic.utils.DataAssemble#protectcalllogNS(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String[], int, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testProtectcalllogNS()
    {
        String[] infos = { "12", "23", "34" };
        String act = DataAssemble.protectcalllogNS(123, "callid", "123", "12", "3000232", 123, "1", "2", "123", "123", "12", "123", infos, ReturnType.Asyn.getResult(), 123, "save", "1", "2", "3");
        String exp = "(protectcalllog|123|callid|123|12|3000232|123|1|2|123|123|12|123|12,23,34|1|123|save|1|2|3)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#controlResultAsm(java.lang.String, java.lang.String, com.goldwind.datalogic.utils.ControlProcessDef.ReturnType)}.
     */
    @Test
    public void testControlResultAsm()
    {
        String act = DataAssemble.controlResultAsm("callid", "123", ReturnType.Asyn);
        String exp = "(controlresult|callid|123|1)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#controlResultAsmNS(java.lang.String)}.
     */
    @Test
    public void testControlResultAsmNS()
    {
        String act = DataAssemble.controlResultAsmNS("callid");
        String exp = "(controlresult|callid)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataAssemble#extensionOrderAsm(java.lang.String, java.util.UUID, java.lang.String)}.
     */
    @Test
    public void testExtensionOrderAsm()
    {
        String act = DataAssemble.extensionOrderAsm("callid", UUID.fromString("73afcb45-7106-4c10-b610-1ac466b303dc"), "123");
        String exp = "calli|73afcb45-7106-4c10-b610-1ac466b303dc|123)";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

}
