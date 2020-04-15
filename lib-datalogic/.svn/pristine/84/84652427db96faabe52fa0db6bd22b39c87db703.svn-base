/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: DataParseTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.utils 
 * @Description: DataParseTest
 * @author: Administrator   
 * @date: 2019年8月20日 下午4:13:11 
 * @version: V1.0   
 */
package com.goldwind.datalogic.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.model.ExtensionData;
import com.goldwind.datalogic.utils.NetCommDef.CommState;
import com.goldwind.datalogic.utils.NetCommDef.NetDataDirection;
import com.goldwind.datalogic.utils.NetCommDef.NetDownDataType;
import com.goldwind.datalogic.utils.NetCommDef.NetNoDirDataType;
import com.goldwind.datalogic.utils.NetCommDef.NetUpDataType;
import com.goldwind.datalogic.utils.NetCommDef.TableDataOperType;

/**
 * @ClassName: DataParseTest
 * @Description: DataParseTest
 * @author: Administrator
 * @date: 2019年8月20日 下午4:13:11
 */
public class DataParseTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getDataDirection(java.lang.String)}.
     */
    @Test
    public void testGetDataDirection()
    {
        String netData = "";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.UNKNOWN));
    }

    @Test
    public void testGetDataDirection1()
    {
        String netData = "(tendata|wtid|数据|ordernum|iscache|protocolid)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.UP));
    }

    @Test
    public void testGetDataDirection2()
    {
        String netData = "(getdspsrvdata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection3()
    {
        String netData = "(largedata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection4()
    {
        String netData = "(nettest)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection5()
    {
        String netData = "(hisdatacomstate)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection6()
    {
        String netData = "(start)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection7()
    {
        String netData = "(serverinfo)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection8()
    {
        String netData = "(sendfile)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection9()
    {
        String netData = "(getdspsrvdata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection10()
    {
        String netData = "(getdspsrvdatasingle)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection11()
    {
        String netData = "(getdspsrvvirtualdata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection12()
    {
        String netData = "(getweatherdata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection13()
    {
        String netData = "(gettyphoondata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection14()
    {
        String netData = "(settyphoonlevel)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection15()
    {
        String netData = "(getbffiledata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection16()
    {
        String netData = "(downloadwavefile)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection17()
    {
        String netData = "(excludcnf)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection18()
    {
        String netData = "(uploadconfigdata)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection19()
    {
        String netData = "(savefile)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection20()
    {
        String netData = "(downloadfile)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection21()
    {
        String netData = "(protectstream)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection22()
    {
        String netData = "(wait)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection23()
    {
        String netData = "(finish)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection24()
    {
        String netData = "(uploadfile)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection25()
    {
        String netData = "(comtestquery)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection26()
    {
        String netData = "(modifydbinfo)";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.NODIRECTION));
    }

    @Test
    public void testGetDataDirection27()
    {
        String netData = "wtcontrolorder()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    @Test
    public void testGetDataDirection28()
    {
        String netData = "wtothercontrolorder()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    @Test
    public void testGetDataDirection29()
    {
        String netData = "setdatavalues()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    @Test
    public void testGetDataDirection30()
    {
        String netData = "getpackdata()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    @Test
    public void testGetDataDirection31()
    {
        String netData = "getprotectdata()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    @Test
    public void testGetDataDirection32()
    {
        String netData = "setprotectdata()";
        NetDataDirection act = DataParse.getDataDirection(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDataDirection.DOWN));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getUpDataType(java.lang.String)}.
     */
    @Test
    public void testGetUpDataType()
    {
        String netData = "(wman|650101001|数据|q1,q2,q3…|ordernum|iscache|protocolid)";
        NetUpDataType act = DataParse.getUpDataType(netData);
        Assert.assertThat(act, Matchers.equalTo(NetUpDataType.DevMainData));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getNoDirectDataType(java.lang.String)}.
     */
    @Test
    public void testGetNoDirectDataType()
    {
        String netData = "(comtest)";
        NetNoDirDataType act = DataParse.getNoDirectDataType(netData);
        Assert.assertThat(act, Matchers.equalTo(NetNoDirDataType.Unknown));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getDownDataType(java.lang.String)}.
     */
    @Test
    public void testGetDownDataType()
    {
        String netData = "setdatavalues(wtid|path|value|系统编号.用户编号|controltype|info1,info2…)";
        NetDownDataType act = DataParse.getDownDataType(netData);
        Assert.assertThat(act, Matchers.equalTo(NetDownDataType.SetDevDataValues));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseMainData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseMainData() throws DataAsException
    {
        String netData = "(wman|650101001|数据|q1,q2,q3…|ordernum|iscache|protocolid)";
        HashMap<String, Object> act = DataParse.parseMainData(netData);
        Assert.assertThat(act, Matchers.hasValue("650101001"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parsePackData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParsePackData() throws DataAsException
    {
        String netData = "parse|650101001|2|2,3,4,5,12";
        HashMap<String, Object> map = DataParse.parsePackData(netData);
        Assert.assertThat(map, Matchers.hasValue("650101001"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseComState(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseComState() throws DataAsException
    {
        String netData = "(test|30023|2)";
        HashMap<String, Object> map = DataParse.parseComState(netData);
        Assert.assertThat(map, Matchers.hasEntry("deviceId", "30023"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseRealtimeData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseRealtimeData() throws DataAsException
    {
        String netData = "(realtimedata|wtid|数据|q1,q2,q3…|ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseRealtimeData(netData);
        Assert.assertThat(map, Matchers.hasValue("wtid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseOneData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseOneData() throws DataAsException
    {
        String netData = "(onedata|650101001|10.1,1202.13,1390.12,2,2,故障码,停机模式字,限功率模式字|0|0|200000)";
        HashMap<String, Object> map = DataParse.parseOneData(netData);
        Assert.assertThat(map, Matchers.hasEntry("enviTemp", "限功率模式字"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseTenData(java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testParseTenData() throws DataAsException
    {
        String[] tenDatas = { "数据", "2" };
        String netData = "(tendata|wtid|数据,2|ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseTenData(netData);
        Assert.assertThat(map, Matchers.hasEntry("tenDatas", tenDatas));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseTimeData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseTimeData() throws DataAsException
    {
        String[] timeDatas = { "d1", "d2", "d3" };
        String netData = "(timedata|wtid|rectime|d1,d2,d3|ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseTimeData(netData);
        Assert.assertThat(map, Matchers.hasEntry("timeDatas", timeDatas));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parsePowerCurve(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParsePowerCurve() throws DataAsException
    {
        String netData = "(powercurve|650101001|11.5,1324.21,23.8|200,198|0|0|20000)";
        HashMap<String, Object> map = DataParse.parsePowerCurve(netData);
        Assert.assertThat(map, Matchers.hasEntry("power", "1324.21"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseFaultData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseFaultData() throws DataAsException
    {
        String netData = "(falutdata|wtid|data|(iecpath1)=value;(iecpath2)=value|statedata|errored| sourcecode |ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseFaultData(netData);
        Assert.assertThat(map, Matchers.hasEntry("faultData", "data"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseStateData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseStateData() throws DataAsException
    {
        String netData = "(statedata|650101001|2|0|0|200000)";
        HashMap<String, Object> map = DataParse.parseStateData(netData);
        Assert.assertThat(map, Matchers.hasEntry("stateData", "2"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseAlarmData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseAlarmData() throws DataAsException
    {
        String netData = "(alarmdata|wtid|data|(iecpath1)=value;(iecpath2)=value|statedata|errored| sourcecode |ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseAlarmData(netData);
        Assert.assertThat(map, Matchers.hasEntry("relationInfo", "(iecpath1)=value;(iecpath2)=value"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseEventData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseEventData() throws DataAsException
    {
        String netData = "(eventdata|wtid|data|(iecpath1)=value;(iecpath2)=value|statedata|errored|sourcecode |ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseEventData(netData);
        Assert.assertThat(map, Matchers.hasEntry("statedata", "statedata"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetConfig(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGetConfig() throws DataAsException
    {
        String netData = "(tableName|ip)";
        HashMap<String, Object> map = DataParse.parseGetConfig(netData);
        Assert.assertThat(map, Matchers.hasEntry("tableName", "tablename"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetProCondition(java.lang.String)}.
     */
    @Test
    public void testParseGetProCondition()
    {
        String netData = "(frontversion|110115|192.168.201.2|2015030101|0|0|)";
        String version = DataParse.parseGetProCondition(netData);
        Assert.assertThat(version, Matchers.equalTo("frontversion|110115|192.168.201.2|2015030101|0|0|"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetProConfig(java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testParseGetProConfig() throws DataAsException
    {
        String netData = "(tableName|protocolId)";
        HashMap<String, Object> map = DataParse.parseGetProConfig(netData);
        Assert.assertThat(map, Matchers.hasEntry("tableName", "tablename"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetIni(java.lang.String)}.
     */
    @Test
    public void testParseGetIni()
    {
        String netData = "(tableName|protocolId)";
        String act = DataParse.parseGetIni(netData);
        Assert.assertThat(act, Matchers.equalTo("tableName|protocolId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseChangeSaveData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseChangeSaveData() throws DataAsException
    {
        String netData = "(changesave|110115001|WTUR.Other.Wn.I16.StopModeWord|15)";
        HashMap<String, Object> map = DataParse.parseChangeSaveData(netData);
        Assert.assertThat(map, Matchers.hasEntry("iecPath", "WTUR.Other.Wn.I16.StopModeWord"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#removeSedimentFlg(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testRemoveSedimentFlg() throws DataAsException
    {
        String netData = "(sediment|2019-08-01(changesave|110115001|WTUR.Other.Wn.I16.StopModeWord|15))";
        HashMap<String, Object> map = DataParse.removeSedimentFlg(netData);
        Assert.assertThat(map, Matchers.hasEntry("srcData", "(changesave|110115001|WTUR.Other.Wn.I16.StopModeWord|15)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseSocketOrderLog(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseSocketOrderLog() throws DataAsException
    {
        String netData = "(deviceId|orderFlg|systemId|userId|descr|parms|info|controltype|controlid|returntype|ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseSocketOrderLog(netData);
        Assert.assertThat(map, Matchers.hasEntry("descr", "descr"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseElecPlan(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseElecPlan() throws DataAsException
    {
        String netData = "(elecplan|650132701|2018-05-04 00:00:00.000|2018-05-04 00:00:00.000|32000|1)";
        HashMap<String, Object> map = DataParse.parseElecPlan(netData);
        Assert.assertThat(map, Matchers.hasEntry("planValue", "32000"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseRunLog(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseRunLog() throws DataAsException
    {
        String netData = "(runlog|systemId|typeId|recTime|wfId|objectId|logCode|guid|relationDatas)";
        HashMap<String, Object> map = DataParse.parseRunLog(netData);
        Assert.assertThat(map, Matchers.hasEntry("objectId", "objectId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseSystemPrompt(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseSystemPrompt() throws DataAsException
    {
        String netData = "(SystemPrompt|12|descr)";
        HashMap<String, Object> map = DataParse.parseSystemPrompt(netData);
        Assert.assertThat(map, Matchers.hasEntry("descr", "descr"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseDeviceInfo(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseDeviceInfo() throws DataAsException
    {
        String netData = "(olddev|deviceId|wfId)";
        HashMap<String, Object> map = DataParse.parseDeviceInfo(netData);
        Assert.assertThat(map, Matchers.hasEntry("wfId", "wfId"));
    }

    @Test
    public void testParseDeviceInfo1() throws DataAsException
    {
        String netData = "(dev|deviceId|wfId)";
        HashMap<String, Object> map = DataParse.parseDeviceInfo(netData);
        Assert.assertThat(map, Matchers.hasEntry("deviceId", "deviceId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#tryParseDeviceInfo(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testTryParseDeviceInfo() throws DataAsException
    {
        String netData = "(sediment(dev|deviceId|wfId))";
        HashMap<String, Object> map = DataParse.tryParseDeviceInfo(netData);
        Assert.assertThat(map, Matchers.hasEntry("deviceId", "deviceId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseUserData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseUserData() throws DataAsException
    {
        String netData = "(userDate|ins|tableName|wfId||)";
        HashMap<String, Object> map = DataParse.parseUserData(netData);
        Assert.assertThat(map, Matchers.hasEntry("tableName", "tableName"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#convertUserDataToSql(com.goldwind.datalogic.utils.NetCommDef.TableDataOperType, java.lang.String, java.util.List, java.util.List)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testConvertUserDataToSql() throws DataAsException
    {
        List<UserDataUnit> unitList1 = new ArrayList<UserDataUnit>();
        UserDataUnit u = new UserDataUnit("col", "v", false);
        unitList1.add(u);
        List<UserDataUnit> unitList2 = new ArrayList<UserDataUnit>();
        UserDataUnit u1 = new UserDataUnit("col1", "v1", false);
        UserDataUnit u2 = new UserDataUnit("col2", "v2", true);
        UserDataUnit u3 = new UserDataUnit("col3", "v3", false);
        unitList2.add(u1);
        unitList2.add(u2);
        unitList2.add(u3);
        String sql = DataParse.convertUserDataToSql(TableDataOperType.Ins, "tableName", unitList1, unitList2);
        Assert.assertThat(sql, Matchers.equalTo("insert into tableName(col) values (v)"));
    }

    @Test
    public void testConvertUserDataToSql1() throws DataAsException
    {
        List<UserDataUnit> unitList1 = new ArrayList<UserDataUnit>();
        UserDataUnit u = new UserDataUnit("col", "v", false);
        unitList1.add(u);
        List<UserDataUnit> unitList2 = new ArrayList<UserDataUnit>();
        UserDataUnit u1 = new UserDataUnit("col1", "v1", false);
        UserDataUnit u2 = new UserDataUnit("col2", "v2", true);
        UserDataUnit u3 = new UserDataUnit("col3", "v3", false);
        unitList2.add(u1);
        unitList2.add(u2);
        unitList2.add(u3);
        String sql = DataParse.convertUserDataToSql(TableDataOperType.Upd, "tableName", unitList1, unitList2);
        Assert.assertThat(sql, Matchers.equalTo("update tableName set col = v where col1 = v1 and col2 = v2 and col3 = v3"));
    }

    @Test
    public void testConvertUserDataToSql2() throws DataAsException
    {
        List<UserDataUnit> unitList1 = new ArrayList<UserDataUnit>();
        UserDataUnit u = new UserDataUnit("col", "v", false);
        unitList1.add(u);
        List<UserDataUnit> unitList2 = new ArrayList<UserDataUnit>();
        UserDataUnit u1 = new UserDataUnit("col1", "v1", false);
        UserDataUnit u2 = new UserDataUnit("col2", "v2", true);
        UserDataUnit u3 = new UserDataUnit("col3", "v3", false);
        unitList2.add(u1);
        unitList2.add(u2);
        unitList2.add(u3);
        String sql = DataParse.convertUserDataToSql(TableDataOperType.Del, "tableName", unitList1, unitList2);
        Assert.assertThat(sql, Matchers.equalTo("delete from tableName where col = v"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseUpSqlData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseUpSqlData() throws DataAsException
    {
        String netData = "(upsqldata|tableName|wfId|sql)";
        HashMap<String, Object> map = DataParse.parseUpSqlData(netData);
        Assert.assertThat(map, Matchers.hasEntry("sql", "sql"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseUpSqlDataToSql(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseUpSqlDataToSql() throws DataAsException
    {
        String netData = "(upsqldata|tableName|wfId|sql)";
        String sql = DataParse.parseUpSqlDataToSql(netData);
        Assert.assertThat(sql, Matchers.equalTo("(sqldata|tableName|wfId|sql)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseIECscrData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseIECscrData() throws DataAsException
    {
        String netData = "(iec|deviceId|mainData,2,3)";
        HashMap<String, Object> map = DataParse.parseIECscrData(netData);
        String[] exp = { "mainData", "2", "3" };
        Assert.assertThat(map, Matchers.hasEntry("mainDatas", exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parsePreVersion(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParsePreVersion() throws DataAsException
    {
        String netData = "(iec|deviceId|preIp|preVersion)";
        HashMap<String, Object> map = DataParse.parsePreVersion(netData);
        Assert.assertThat(map, Matchers.hasEntry("preVersion", "preVersion"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseDevVersion(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseDevVersion() throws DataAsException
    {
        String netData = "(iec|deviceId|proId|proVersion|preIp)";
        HashMap<String, Object> map = DataParse.parseDevVersion(netData);
        Assert.assertThat(map, Matchers.hasEntry("preIp", "preIp"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseMainLog(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseMainLog() throws DataAsException
    {
        String netData = "(mainLog|machineid|warnid)";
        HashMap<String, Object> map = DataParse.parseMainLog(netData);
        Assert.assertThat(map, Matchers.hasEntry("warnid", "warnid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseWarnLog(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseWarnLog() throws DataAsException
    {
        String netData = "(warnlog|systemid|levelid|rectime|wfid|objectid|logcode|warnid|flag|info|typeid|iecval|objecttype|stateval| opertype)";
        HashMap<String, Object> map = DataParse.parseWarnLog(netData);
        Assert.assertThat(map, Matchers.hasEntry("iecval", "iecval"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseWarnEnd(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseWarnEnd() throws DataAsException
    {
        String netData = "(warnend|rectime|warnid)";
        HashMap<String, Object> map = DataParse.parseWarnEnd(netData);
        Assert.assertThat(map, Matchers.hasEntry("warnid", "warnid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetPlcFileName(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGetPlcFileName() throws DataAsException
    {
        String netData = "(deviceId|2019-08-01|2019-08-10|0|plcIp|plcUser|plcPassword)";
        HashMap<String, Object> map = DataParse.parseGetPlcFileName(netData);
        Assert.assertThat(map, Matchers.hasEntry("plcPassword", "plcPassword"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetPlcFileData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGetPlcFileData() throws DataAsException
    {
        String netData = "(deviceId|fileName|plcIp|plcUser|plcPassword)";
        HashMap<String, Object> map = DataParse.parseGetPlcFileData(netData);
        Assert.assertThat(map, Matchers.hasEntry("plcPassword", "plcPassword"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseServiceManage(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseServiceManage() throws DataAsException
    {
        String netData = "(1|paramList,1,2)";
        String[] paramList = { "paramList", "1", "2" };
        HashMap<String, Object> map = DataParse.parseServiceManage(netData);
        Assert.assertThat(map, Matchers.hasEntry("paramList", paramList));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parsePreAdapterStart(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParsePreAdapterStart() throws DataAsException
    {
        String netData = "(start|127.0.0.1)";
        String act = DataParse.parsePreAdapterStart(netData);
        String exp = "127.0.0.1";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseHisDataComState(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseHisDataComState() throws DataAsException
    {
        String netData = "(ComState|30023|0)";
        HashMap<String, Object> map = DataParse.parseHisDataComState(netData);
        Assert.assertThat(map, Matchers.hasEntry("state", CommState.Connect));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseAddTask(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseAddTask() throws DataAsException
    {
        String netData = "(userId|monitorId|deviceIds|proId|orderFlg|paramList|0|beginTime)";
        HashMap<String, Object> map = DataParse.parseAddTask(netData);
        Assert.assertThat(map, Matchers.hasEntry("monitorId", "monitorId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseDelTask(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseDelTask() throws DataAsException
    {
        String netData = "(taskId|userid)";
        HashMap<String, Object> map = DataParse.parseDelTask(netData);
        Assert.assertThat(map, Matchers.hasEntry("taskId", "taskId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseStopTask(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseStopTask() throws DataAsException
    {
        String netData = "(taskId|userid)";
        HashMap<String, Object> map = DataParse.parseStopTask(netData);
        Assert.assertThat(map, Matchers.hasEntry("taskId", "taskId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseLargeData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseLargeData() throws DataAsException
    {
        String netData = "(largedata|dataName|2|1)";
        HashMap<String, Object> map = DataParse.parseLargeData(netData);
        Assert.assertThat(map, Matchers.hasEntry("zipData", true));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseSendFileOrder(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseSendFileOrder() throws DataAsException
    {
        String netData = "(sendfile|fileName|2)";
        HashMap<String, Object> map = DataParse.parseSendFileOrder(netData);
        Assert.assertThat(map, Matchers.hasEntry("packCount", 2));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseSaveFileOrder(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseSaveFileOrder() throws DataAsException
    {
        String netData = "(savefile|fileType|fileName|dataTime|2)";
        HashMap<String, Object> map = DataParse.parseSaveFileOrder(netData);
        Assert.assertThat(map, Matchers.hasEntry("packCount", 2));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGroupOrderLog(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGroupOrderLog() throws DataAsException
    {
        String netData = "(grouporderlog|deviceId|orderType.dhdj.id.23|orderFlg|systemId.userId.descr|true)";
        HashMap<String, Object> map = DataParse.parseGroupOrderLog(netData);
        Assert.assertThat(map, Matchers.hasEntry("systemId", "systemId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetSoftAdapInfo(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGetSoftAdapInfo() throws DataAsException
    {
        String netData = "(objId|dataType|paramList,12,23,33)";
        String[] list = { "paramList", "12", "23", "33" };
        HashMap<String, Object> map = DataParse.parseGetSoftAdapInfo(netData);
        Assert.assertThat(map, Matchers.hasEntry("paramList", list));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getParenthRightData(java.lang.String)}.
     */
    @Test
    public void testGetParenthRightData()
    {
        String netData = "(sedtime|objId|dataType|paramList,12,23,33)";
        String act = DataParse.getParenthRightData(netData);
        Assert.assertThat(act, Matchers.equalTo("sedtime"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#getParenthLeftData(java.lang.String)}.
     */
    @Test
    public void testGetParenthLeftData()
    {
        String netData = "sedtime(objId|dataType|paramList,12,23,33)";
        String act = DataParse.getParenthLeftData(netData);
        Assert.assertThat(act, Matchers.equalTo("sedtime"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#removeParenth(java.lang.String)}.
     */
    @Test
    public void testRemoveParenth()
    {
        String netData = "sedtime(objId|dataType|paramList,12,23,33)";
        String act = DataParse.removeParenth(netData);
        Assert.assertThat(act, Matchers.equalTo("objId|dataType|paramList,12,23,33"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#removePairSymbol(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testRemovePairSymbol()
    {
        // 有问题
        String netData = "(objId|dataType|paramList,12,23,33}";
        String act = DataParse.removePairSymbol(netData, "(", "}");
        Assert.assertThat(act, Matchers.equalTo("objId|dataType|paramList,12,23,3"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#intToByteArray(int)}.
     */
    @Test
    public void testIntToByteArray()
    {
        byte[] btyes = DataParse.intToByteArray(6);
        byte[] exp = { 6, 0, 0, 0 };
        Assert.assertArrayEquals(exp, btyes);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#byteArrayToInt(byte[])}.
     */
    @Test
    public void testByteArrayToInt()
    {
        byte[] exp = { 6, 0, 0, 0 };
        int act = DataParse.byteArrayToInt(exp);
        Assert.assertThat(act, Matchers.equalTo(6));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#byteMerger(byte[], byte[])}.
     */
    @Test
    public void testByteMerger()
    {
        byte[] byte1 = { 6, 1, 0, 0 };
        byte[] byte2 = { 6, 0, 0, 0 };
        byte[] exp = { 6, 1, 0, 0, 6, 0, 0, 0 };
        byte[] act = DataParse.byteMerger(byte1, byte2);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#byteCut(byte[], int, int)}.
     */
    @Test
    public void testByteCut()
    {
        byte[] byte1 = { 6, 1, 0, 0 };
        byte[] exp = { 1, 0 };
        byte[] act = DataParse.byteCut(byte1, 1, 3);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#packbyte(int, byte[])}.
     */
    @Test
    public void testPackbyte()
    {
        byte[] byte1 = { 6, 1 };
        byte[] exp = { 1, 0, 0, 0, 2, 0, 0, 0, 6, 1 };
        byte[] act = DataParse.packbyte(1, byte1);
        Assert.assertArrayEquals(exp, act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseBigData(byte[])}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseBigData() throws DataAsException
    {
        byte[] byte1 = { 6, 0, 0, 0, 6, 0, 0, 0, 2, 2, 2, 4, 3, 1 };
        HashMap<String, Object> map = DataParse.parseBigData(byte1);
        Assert.assertThat(map, Matchers.hasEntry("packLen", 6));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseUserBehaviorData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseUserBehaviorData() throws DataAsException
    {
        String netData = "(userbehavior|opertype|operfunc|rectime|endtime|remark|userid|wfid)";
        HashMap<String, Object> map = DataParse.parseUserBehaviorData(netData);
        Assert.assertThat(map, Matchers.hasEntry("opertype", "opertype"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseServiceInfoData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseServiceInfoData() throws DataAsException
    {
        String netData = "(serviceinfo|ip|system|code|disc|cpu|memory|agent)";
        HashMap<String, Object> map = DataParse.parseServiceInfoData(netData);
        Assert.assertThat(map, Matchers.hasEntry("ip", "ip"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseWtControlData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseWtControlData() throws DataAsException
    {
        String netData = "(wtid|nflg|用户编号|info1)";
        HashMap<String, Object> map = DataParse.parseWtControlData(netData);
        Assert.assertThat(map, Matchers.hasEntry("userid", "用户编号"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseWtOtherControlData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseWtOtherControlData() throws DataAsException
    {
        String netData = "(wtid|nflg|param1,param2|系统编号.用户编号|info1,info2)";
        HashMap<String, Object> map = DataParse.parseWtOtherControlData(netData);
        Assert.assertThat(map, Matchers.hasEntry("parm", "param1,param2"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseSetDataValueData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseSetDataValueData() throws DataAsException
    {
        String netData = "(wtid|path|value|系统编号.用户编号|controltype|info1,info2)";
        HashMap<String, Object> map = DataParse.parseSetDataValueData(netData);
        Assert.assertThat(map, Matchers.hasEntry("controltype", "controltype"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extOldData(java.lang.String, com.goldwind.datalogic.utils.NetCommDef.NetDownDataType, java.lang.String)}.
     */
    @Test
    public void testExtOldDataStringNetDownDataTypeString()
    {
        String data = "(data|127.0.0.1,172.0.0.1)";
        ExtensionData date = DataParse.extOldData(data, NetDownDataType.SetProtectData, "30012");
        Assert.assertThat(date.getExtData(), Matchers.equalTo("(data|127.0.0.1,172.0.0.1)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extOldData(java.lang.String, com.goldwind.datalogic.utils.NetCommDef.NetDownDataType, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testExtOldDataStringNetDownDataTypeStringString()
    {
        String data = "(data|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extOldData(data, NetDownDataType.SetProtectData, "32", "30012");
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extWtOtherControlData(java.lang.String)}.
     */
    @Test
    public void testExtWtOtherControlData()
    {
        String data = "(WtOtherControl|....|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extWtOtherControlData(data);
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extSetDataValueData(java.lang.String)}.
     */
    @Test
    public void testExtSetDataValueData()
    {
        String data = "(SetDataValueData|....|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extSetDataValueData(data);
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extGetPackData(java.lang.String)}.
     */
    @Test
    public void testExtGetPackData()
    {
        String data = "(GetPackData|....|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extGetPackData(data);
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseProtectStreamData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseProtectStreamData() throws DataAsException
    {
        String netData = "(protectstream|callid|calltype|packcount|iszip|savefile|1:file1size,2:file2size，3：file2size)";
        HashMap<String, Object> map = DataParse.parseProtectStreamData(netData);
        Assert.assertThat(map, Matchers.hasEntry("callid", "callid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseProtectCallLogData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseProtectCallLogData() throws DataAsException
    {
        String netData = "(protectcalllog|calltype|callid|userid|rectime|msg|logtype|statid|devid|cupid|regionid|groupid|grouptype|info1,info2…|returntype|dataformat|savefile|ordernum|iscache|protocolid)";
        HashMap<String, Object> map = DataParse.parseProtectCallLogData(netData);
        Assert.assertThat(map, Matchers.hasEntry("devid", "devid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseProtectSetLogData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseProtectSetLogData() throws DataAsException
    {
        String netData = "(protectsetlog|setid|userid|rectime|msg|logtype|settype|setmodel|statid|devid|cpuid|regionid|groupid|grouptype|itemid|valuetype|value|info1,info2…|ordernum|iscache|protocolid|value)";
        HashMap<String, Object> map = DataParse.parseProtectSetLogData(netData);
        Assert.assertThat(map, Matchers.hasEntry("msg", "msg"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGetProtectData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGetProtectData() throws DataAsException
    {
        String netData = "getprotectdata(calltype|callid|userid|statid|devid|cpuid|regionid|groupid|grouptype|info1,info2…|returntype|dataformat|savefile)";
        HashMap<String, Object> map = DataParse.parseGetProtectData(netData);
        Assert.assertThat(map, Matchers.hasEntry("grouptype", "grouptype"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseProtectSetData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseProtectSetData() throws DataAsException
    {
        String netData = "(setid|userid|settype|setmodel|statid|devid|cpuid|regionid|groupid|grouptype|itemid|valuetype|value|info)";
        HashMap<String, Object> map = DataParse.parseProtectSetData(netData);
        Assert.assertThat(map, Matchers.hasEntry("setmodel", "setmodel"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extGetProtectData(java.lang.String)}.
     */
    @Test
    public void testExtGetProtectData()
    {
        String netData = "(extgetprotectdata|....|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extGetProtectData(netData);
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#extSetProtectData(java.lang.String)}.
     */
    @Test
    public void testExtSetProtectData()
    {
        String netData = "(extgetprotectdata|....|127.0.0.1:8805:1,172.0.0.1:8801:1)";
        ExtensionData date = DataParse.extSetProtectData(netData);
        Assert.assertThat(date.getExtPort(), Matchers.equalTo(8805));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseServerState(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseServerState() throws DataAsException
    {
        String netData = "(extgetprotectdata|srvid|state)";
        HashMap<String, String> map = DataParse.parseServerState(netData);
        Assert.assertThat(map, Matchers.hasEntry("srvid", "srvid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseControlresultData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseControlresultData() throws DataAsException
    {
        String netData = "(controlresult|srvid|state)";
        HashMap<String, Object> map = DataParse.parseControlresultData(netData);
        Assert.assertThat(map, Matchers.hasEntry("result", "(srvid)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseResultData(java.lang.String)}.
     */
    @Test
    public void testParseResultData()
    {
        String netData = "(controlresult|srvid|state)";
        HashMap<String, String> map = DataParse.parseResultData(netData);
        Assert.assertThat(map, Matchers.hasEntry("result", "(controlresult|srvid)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseEnvTemperatureCurve(java.lang.String)}.
     */
    @Test
    public void testParseEnvTemperatureCurve()
    {
        String netData = "(controlresult|srvid|state)";
        HashMap<String, String> map = DataParse.parseResultData(netData);
        Assert.assertThat(map, Matchers.hasEntry("result", "(controlresult|srvid)"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGPSData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGPSData() throws DataAsException
    {
        String netData = "(gpsdata|time|company|userid|username|wfid|lat|lng)";
        HashMap<String, Object> map = DataParse.parseGPSData(netData);
        Assert.assertThat(map, Matchers.hasEntry("company", "company"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseMonData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseMonData() throws DataAsException
    {
        String netData = "(mondata|objectid|rectime|d1,d2,d3…)";
        HashMap<String, Object> map = DataParse.parseMonData(netData);
        Assert.assertThat(map, Matchers.hasEntry("objectid", "objectid"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.utils.DataParse#parseGoUserData(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseGoUserData() throws DataAsException
    {
        String netData = "(gouserdata|userid|realname|mobile|dept|position|company|wfid|delflag)";
        HashMap<String, Object> map = DataParse.parseGoUserData(netData);
        Assert.assertThat(map, Matchers.hasEntry("mobile", "mobile"));
    }

}
