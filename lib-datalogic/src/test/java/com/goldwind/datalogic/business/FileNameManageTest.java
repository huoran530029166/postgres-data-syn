/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: FileNameManageTest.java 
 * @Prject: SOAM-lib-datalogic
 * @Package: com.goldwind.datalogic.business 
 * @Description: FileNameManageTest
 * @author: Administrator   
 * @date: 2019年8月26日 下午6:05:56 
 * @version: V1.0   
 */
package com.goldwind.datalogic.business;

import java.util.HashMap;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.NetCommDef.PlcFileType;

/**
 * @ClassName: FileNameManageTest
 * @Description: FileNameManageTest
 * @author: Administrator
 * @date: 2019年8月26日 下午6:05:56
 */
public class FileNameManageTest
{

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFileDevId(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFileDevId()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getFileDevId("600", "600091");
        String exp = "600.600091";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFileDevId(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFileDevIdFalse()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getFileDevId("600", "600091");
        String exp = "600091";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#parseFileName(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseFileName() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String fileName = "file_wfId.deviceId_dataDate.file";
        HashMap<String, String> map = FileNameManage.parseFileName(fileName);
        Assert.assertThat(map, Matchers.hasEntry("wfId", "wfId"));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#parseFileName(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testParseFileNameFalse() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String fileName = "file_wfId.deviceId_dataDate.file";
        HashMap<String, String> map = FileNameManage.parseFileName(fileName);
        Assert.assertThat(map, Matchers.hasEntry("wfId", ""));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByName()
    {
        String fileName = "";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Unknown;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameB()
    {
        String fileName = "plcb_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.B;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameFhtml()
    {
        String fileName = "plcfh_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Fhtml;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameFtxt()
    {
        String fileName = "plcft_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Ftxt;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameM()
    {
        String fileName = "plcm_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.M;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameAction()
    {
        String fileName = "plca_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Action;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameOperation()
    {
        String fileName = "plco_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Operation;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameHfb()
    {
        String fileName = "plcHfb_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Hfb;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameW()
    {
        String fileName = "plcW_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.W;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameE()
    {
        String fileName = "plcE_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.E;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameC()
    {
        String fileName = "plcC_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.C;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameFc()
    {
        String fileName = "plcFc_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.Fc;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameT()
    {
        String fileName = "plcT_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.T;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPlcFileTypeByName(java.lang.String)}.
     */
    @Test
    public void testGetPlcFileTypeByNameS()
    {
        String fileName = "plcS_wtid.deviceId_dataDate.file";
        PlcFileType act = FileNameManage.getPlcFileTypeByName(fileName);
        PlcFileType exp = PlcFileType.S;
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTenMinFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetTenMinFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getTenMinFilePath("/path/iec", "600032", "data123456");
        String exp = "/path/iec/UnArchive/TenMinutes/data12/600032";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTenMinFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetTenMinFileNameStringStringStringString()
    {
        String act = FileNameManage.getTenMinFileName("/path/iec", "600", "600032", "");
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTenMinFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetTenMinFileNameStringStringStringStringNotNull()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getTenMinFileName("/path/iec", "600", "600032", "data123456");
        String exp = "/path/iec/UnArchive/TenMinutes/data12/ten_600032_data12.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTenMinFileName(java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     */
    @Test
    public void testGetTenMinFileNameStringString() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String fileName = "file_wfId.deviceId_dataDate.file";
        String rootDir = "/path/iec";
        String act = FileNameManage.getTenMinFileName(rootDir, fileName);
        String exp = "/path/iec/UnArchive/TenMinutes/dataDa/ten_wfId.deviceId_dataDa.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcTenMinFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcTenMinFileName() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getArcTenMinFileName("/path/iec", "600", "600032", "20190801");
        String exp = "/path/iec/HistoryFile/2019/TenMinutes/201908/ten_600032_201908.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPowCurveFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetPowCurveFileNameNull()
    {
        String act = FileNameManage.getPowCurveFileName("/path/iec", "600012", "6000", "");
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPowCurveFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetPowCurveFileName()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getPowCurveFileName("/path/iec", "600012", "6000", "20190827");
        String exp = "/path/iec/UnArchive/PowerCurve/201908/pow_6000_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPowCurveFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetPowCurveFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getPowCurveFilePath("/path/iec", "600012", "20190827");
        String exp = "/path/iec/UnArchive/PowerCurve/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getPowCurveFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetPowCurveFilePathNull()
    {
        String act = FileNameManage.getPowCurveFilePath("/path/iec", "600012", "");
        String exp = "/path/iec/UnArchive/PowerCurve";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcPowCurFName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcPowCurFName() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getArcPowCurFName("/path/iecpath", "6000", "600012", "20190807");
        String exp = "/path/iecpath/HistoryFile/2019/PowerCurve/201908/pow_600012_201908.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevFaultFileNameStringStringStringStringNull()
    {
        String act = FileNameManage.getDevFaultFileName("/path/iec", "6000", "600012", "");
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevFaultFileNameStringStringStringString()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getDevFaultFileName("/path/iec", "6000", "600012", "20190807");

        String exp = "/path/iec/UnArchive/DeviceFault/201908/devflt_600012_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFileName(java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetDevFaultFileNameStringString() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String fileName = "file_wfId.deviceId_dataDate.file";
        String act = FileNameManage.getDevFaultFileName("/path/iec", fileName);
        String exp = "/path/iec/UnArchive/DeviceFault/dataDa/wfId/devflt_wfId.deviceId_dataDa.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevFaultFilePathNull()
    {
        String act = FileNameManage.getDevFaultFilePath("/path/iec", "6000", "");
        String exp = "/path/iec/UnArchive/DeviceFault";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevFaultFilePathFalse()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getDevFaultFilePath("/path/iec", "6000", "20190807");
        String exp = "/path/iec/UnArchive/DeviceFault/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevFaultFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevFaultFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getDevFaultFilePath("/path/iec", "6000", "20190807");
        String exp = "/path/iec/UnArchive/DeviceFault/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcDevFltFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcDevFltFileName() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getArcDevFltFileName("/apth/iec", "6000", "6000032", "20190807");
        String exp = "/apth/iec/HistoryFile/2019/DeviceFault/201908/devflt_6000032_201908.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevStatusFileNameStringStringStringStringNull()
    {
        String act = FileNameManage.getDevStatusFileName("/apth/iec", "6000", "6000032", "");
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevStatusFileNameStringStringStringString()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getDevStatusFileName("/apth/iec", "6000", "6000032", "201908071203");
        String exp = "/apth/iec/UnArchive/DeviceStatus/201908/devstatus_6000032_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFileName(java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetDevStatusFileNameStringString() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String fileName = "file_wfId.deviceId_20190807.file";
        String act = FileNameManage.getDevStatusFileName("/path/iec", fileName);
        String exp = "/path/iec/UnArchive/DeviceStatus/201908/wfId/devstatus_wfId.deviceId_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevStatusFilePathNull()
    {
        // BusinessDef.isFILEPATHUSEWF()
        String act = FileNameManage.getDevStatusFilePath("/path/iec", "6000", "");
        String exp = "/path/iec/UnArchive/DeviceStatus";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevStatusFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getDevStatusFilePath("/path/iec", "6000", "20190807");
        String exp = "/path/iec/UnArchive/DeviceStatus/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDevStatusFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetDevStatusFilePathFalse()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getDevStatusFilePath("/path/iec", "6000", "20190807");
        String exp = "/path/iec/UnArchive/DeviceStatus/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcDevStaFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcDevStaFileName() throws DataAsException
    {
        String act = FileNameManage.getArcDevStaFileName("/path/iec", "6000", "600012", "20190807");
        String exp = "/path/iec/HistoryFile/2019/DeviceStatus/201908/devstatus_600012_201908.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getRealDataFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetRealDataFileNameStringStringStringString()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getRealDataFileName("/path/iec", "6000", "6000012", "201908071230");
        String exp = "/path/iec/UnArchive/RealtimeData/20190807/real_6000012_20190807.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getRealDataFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetRealDataFileNameStringStringStringStringNull()
    {
        String act = FileNameManage.getRealDataFileName("/path/iec", "6000", "6000012", "");
        Assert.assertNull(act);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getRealDataFileName(java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetRealDataFileNameStringString() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String fileName = "file_wfId.deviceId_dataDate.file";
        String act = FileNameManage.getRealDataFileName("/path/iec", fileName);
        String exp = "/path/iec/UnArchive/RealtimeData/dataDate/wfId/real_wfId.deviceId_dataDate.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getRealDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetRealDataFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getRealDataFilePath("/path/iec", "6000", "201908071230");
        String exp = "/path/iec/UnArchive/RealtimeData/20190807";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getUserBehaviorFileName(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetUserBehaviorFileName()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getUserBehaviorFileName("/path/iec", "6000", "201908071230");
        String exp = "/path/iec/UnArchive/UserBehaviorData/201908/user_6000_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getUserBehaviorDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetUserBehaviorDataFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getUserBehaviorDataFilePath("/path/iec", "6000", "201908071230");
        String exp = "/path/iec/UnArchive/UserBehaviorData/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getUserBehaviorDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetUserBehaviorDataFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getUserBehaviorDataFilePath("/path/iec", "6000", "201908071230");
        String exp = "/path/iec/UnArchive/UserBehaviorData/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnLogFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnLogFileName()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getWarnLogFileName("/path/iec", "6000", "6000012", "2019080711230");

        String exp = "/path/iec/UnArchive/WarnLog/201908/warnlog_6000012_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnLogDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnLogDataFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getWarnLogDataFilePath("/path/iec", "6000012", "2019080711230");
        String exp = "/path/iec/UnArchive/WarnLog/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnLogDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnLogDataFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getWarnLogDataFilePath("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/WarnLog/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getChangeDataFileName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetChangeDataFileName()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getChangeDataFileName("/path/iec", "6000", "6000012", "2019080711230");
        String exp = "/path/iec/UnArchive/ChangeData/201908/changedata_6000012_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getChangeDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetChangeDataFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getChangeDataFilePath("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/ChangeData/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getChangeDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetChangeDataFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getChangeDataFilePath("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/ChangeData/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFaultDataFileName(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFaultDataFileName()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getFaultDataFileName("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/FaultDataFile/201908/fault_6000_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFaultDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFaultDataFilePathStringStringString()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getFaultDataFilePath("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/FaultDataFile/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFaultDataFilePath(java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetFaultDataFilePathStringStringStringTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getFaultDataFilePath("/path/iec", "6000", "2019080711230");
        String exp = "/path/iec/UnArchive/FaultDataFile/201908/6000";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnEndDataFileName(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnEndDataFileName()
    {
        String act = FileNameManage.getWarnEndDataFileName("/path/iec", "2019080711230");
        String exp = "/path/iec/UnArchive/WarnEndData/201908/warnend_201908.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnEndDataFilePath(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnEndDataFilePath()
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getWarnEndDataFilePath("/path/iec", "2019080711230");
        String exp = "/path/iec/UnArchive/WarnEndData/201908";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnEndDataFilePath(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetWarnEndDataFilePathTrue()
    {
        BusinessDef.setFILEPATHUSEWF(true);
        String act = FileNameManage.getWarnEndDataFilePath("/path/iec", "2019080711230");
        String exp = "/path/iec/UnArchive/WarnEndData/201908/";
        Assert.assertThat(act, Matchers.equalTo(exp));
        BusinessDef.setFILEPATHUSEWF(false);
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDbFltDataFileName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetDbFltDataFileName() throws Exception
    {
        String act = FileNameManage.getDbFltDataFileName("/path/iec");
        String exp = "/path/iec/FaultData/DatabaseFault/dbflt";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFileFltDataFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetFileFltDataFName() throws Exception
    {
        String act = FileNameManage.getFileFltDataFName("/path/iec");
        String exp = "/path/iec/FaultData/FileFault/fileflt";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getCommFltDataFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetCommFltDataFName() throws Exception
    {
        String act = FileNameManage.getCommFltDataFName("/path/iec");
        String exp = "/path/iec/FaultData/CommFault/commflt";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getJumpTimeFileName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetJumpTimeFileName() throws Exception
    {
        String act = FileNameManage.getJumpTimeFileName("/path/iec");
        String exp = "/path/iec/FaultData/JumpTime/jumptime";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getBadFilePath(java.lang.String)}.
     */
    @Test
    public void testGetBadFilePath()
    {
        String act = FileNameManage.getBadFilePath("/path/iec");
        String exp = "/path/iec/FaultData/BadFile";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTransBuffFileName(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetTransBuffFileName() throws Exception
    {
        String act = FileNameManage.getTransBuffFileName("/path/iec", "2019080711230");
        String exp = "/path/iec/BufferData/TransBuffer/2019080711230/trsbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getHisSynchFileName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetHisSynchFileName() throws Exception
    {
        String act = FileNameManage.getHisSynchFileName("/path/iec");
        String exp = "/path/iec/BufferData/RemoteSynch/hissyn";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getRtDbBuffFileName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetRtDbBuffFileName() throws Exception
    {
        String act = FileNameManage.getRtDbBuffFileName("/path/iec");
        String exp = "/path/iec/BufferData/RtDbBuffer/rtdbbuf";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getSaveDbBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetSaveDbBuffFName() throws Exception
    {
        String act = FileNameManage.getSaveDbBuffFName("/path/iec");
        String exp = "/path/iec/BufferData/DatabaseBuffer/dbbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getSaveRtDbBuffFName(java.lang.String)}.
     * 
     * @throws InterruptedException
     *             异常
     */
    @Test
    public void testGetSaveRtDbBuffFName() throws InterruptedException
    {
        String act = FileNameManage.getSaveRtDbBuffFName("/path/iec");
        String exp = "/path/iec/BufferData/RtDbBuffer/rtdbbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWarnEndBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetWarnEndBuffFName() throws Exception
    {
        String act = FileNameManage.getWarnEndBuffFName("/path/iec");
        String exp = "/path/iec/BufferData/WarnEndBuffer/warnendbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getUploadBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetUploadBuffFName() throws Exception
    {
        String act = FileNameManage.getUploadBuffFName("/path/iec");
        String exp = "/path/iec/BufferData/UploadBuffer/Uploadbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getWebTransBufffileName(java.lang.String, java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetWebTransBufffileName() throws Exception
    {
        String act = FileNameManage.getWebTransBufffileName("/path/iec", "20190807");
        String exp = "/path/iec/BufferData/WebTransBuffer/20190807/trsbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getSubscribeBufffileName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetSubscribeBufffileName() throws Exception
    {
        String act = FileNameManage.getSubscribeBufffileName("/path/iec");
        String exp = "/path/iec/BufferData/SubscribeBuffer/SubTrsbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getStrongBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetStrongBuffFName() throws Exception
    {
        String act = FileNameManage.getStrongBuffFName("/path/iec");
        String exp = "/path/iec/BufferData/MySqlBuffer/MySqlbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDbDdlBuffFileName(java.lang.String)}.
     */
    @Test
    public void testGetDbDdlBuffFileName()
    {
        String act = FileNameManage.getDbDdlBuffFileName("/path/iec");
        String exp = "/path/iec/BufferData/DatabaseBuffer/ddlbuf.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTaskBuffFileName(java.lang.String)}.
     */
    @Test
    public void testGetTaskBuffFileName()
    {
        String act = FileNameManage.getTaskBuffFileName("/path/iec");
        String exp = "/path/iec/BufferData/TaskBuffer/taskbuf.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getDataPrvBuffFName(java.lang.String)}.
     */
    @Test
    public void testGetDataPrvBuffFName()
    {
        String act = FileNameManage.getDataPrvBuffFName("/path/iec");
        String exp = "/path/iec/DataPrvBuffer/dataprvbuf.data";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getFaultDataFilePath(java.lang.String)}.
     */
    @Test
    public void testGetFaultDataFilePathString()
    {
        String act = FileNameManage.getFaultDataFilePath("/path/iec");
        String exp = "/path/iec/FaultData";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcRealDataFName(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcRealDataFName() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getArcRealDataFName("/path/iec", "6000", "6000012", "201908071203");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/20190807/real_6000012_20190807.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcFileName(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcFileName() throws DataAsException
    {
        String fileName = "/getArcFileName/UnArchive/gshdjddff/shdjj_123_20180901.data";
        String act = FileNameManage.getArcFileName(fileName);
        String exp = "/getArcFileName/HistoryFile/2018/gshdjddff/shdjj_123_20180901.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcFileDir(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetArcFileDir() throws DataAsException
    {
        BusinessDef.setFILEPATHUSEWF(false);
        String act = FileNameManage.getArcFileDir("/path/iec/HistoryFile/2019/RealtimeData/UnArchive/real_6000012_20190807.arc");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/HistoryFile/real/real_6000012_20190807.arc";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getArcFileRootDir(java.lang.String)}.
     */
    @Test
    public void testGetArcFileRootDir()
    {
        String act = FileNameManage.getArcFileRootDir("/path/iec/HistoryFile/2019/RealtimeData");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/HistoryFile";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTempFileName()}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetTempFileName() throws Exception
    {
        String act = FileNameManage.getTempFileName();
        String exp = ".tmp";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getSaveReTcpBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetSaveReTcpBuffFName() throws Exception
    {
        String act = FileNameManage.getSaveReTcpBuffFName("/path/iec/HistoryFile/2019/RealtimeData");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/temp/tcp/tcpbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getSaveReUdpBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetSaveReUdpBuffFName() throws Exception
    {
        String act = FileNameManage.getSaveReUdpBuffFName("/path/iec/HistoryFile/2019/RealtimeData");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/temp/udp/udpbuf_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

    /**
     * Test method for {@link com.goldwind.datalogic.business.FileNameManage#getTicketBuffFName(java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testGetTicketBuffFName() throws Exception
    {
        String act = FileNameManage.getTicketBuffFName("/path/iec/HistoryFile/2019/RealtimeData");
        String exp = "/path/iec/HistoryFile/2019/RealtimeData/temp/ticket/TicketData_";
        Assert.assertThat(act, Matchers.containsString(exp));
    }

}
