package com.goldwind.datalogic.dsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.datalogic.business.model.WeatherConfig;

public class BaseConfig
{
    /**
     * "127.0.0.1"常量
     */
    private static String INITIPSTRING = "127.0.0.1";
    /**
     * 本地IP
     */
    private static String LOCALIP = INITIPSTRING;

    /**
     * 数据库类型
     */
    private static DatabaseType DBTYPE = DatabaseType.Postgre;
    /**
     * 数据库url
     */
    private static String DBURL = "jdbc:postgresql://127.0.0.1:5432/scadadb";
    /**
     * 数据库服务器
     */
    private static String DBSERVER = INITIPSTRING;
    /**
     * 数据库端口号
     */
    private static int PORT = 5432;
    /**
     * 数据库用户名
     */
    private static String DBUSER = "postgres";
    /**
     * 数据库密码
     */
    private static String DBPASSWORD = "postgres";
    /**
     * 数据库名
     */
    private static String DBNAME = "scadadb";
    /**
     * 连接池最大连接数
     */
    private static int MAXPOOLSIZE = 100;
    /**
     * 连接池最小连接数
     */
    private static int MINPOOLSIZE = 10;
    /**
     * 数据库连接超时时间
     */
    private static int DBTIMEOUT = 60;
    /**
     * 数据库连接超时时间
     */
    private static int INSERTDBTIMEOUT = 0;

    /**
     * 组播地址
     */
    private static String GROUPIP = "224.1.1.15";
    /**
     * 组播端口
     */
    private static int GROUPPORT = 8769;

    /**
     * 通讯注册告警持续时长
     */
    private static int COMTESTWARNTIME = 300;
    /**
     * 通讯注册告警周期
     */
    private static int COMTESTWARNCONTINUETIME = 3600;

    /**
     * 检查数据库连接状态周期（单位：秒）
     */
    private static int CHECKDBPERIOD = 300;

    /**
     * 语言环境
     */
    private static String LANGUAGE = "zh";

    /**
     * 风场ID
     */
    private static String WFID = "";

    /**
     * 是否获取气象信息
     */
    private static boolean GETWEATHERDATA = false;

    /**
     * 是否产生气象告警
     */
    private static boolean WEATHERWARNING = false;

    /**
     * 是否获取台风信息
     */
    private static boolean GETTYPHOONDATA = false;

    /**
     * 是否产生台风告警
     */
    private static boolean TYPHOONWARNING = false;

    /**
     * 是否开启数据通讯
     */
    private static boolean DATACOMM = true;

    /**
     * 风机范围值
     */
    private static Double WINDVALUENUM = 0.0;

    /**
     * 光伏范围值
     */
    private static Double INVALUE = 0.0;

    /**
     * 气象服务配置
     */
    private static List<WeatherConfig> WEATHERCONFIG = new ArrayList<>();

    /**
     * 台风预警配置(key是风场ID,value是经纬度)
     */
    private static HashMap<String, List<String>> TYPHOONCONFIG = new HashMap<>();

    /**
     * 实时数据库服务器
     */
    private static String RTDBSERVER = INITIPSTRING;

    /**
     * 实时数据库端口号
     */
    private static int RTDBPORT = 6327;

    /**
     * 实时数据库用户名
     */
    private static String RTDBUSER = "sa";

    /**
     * 实时数据库密码
     */
    private static String RTDBPASSWORD = "golden";

    /**
     * 实时数据库名
     */
    private static String RTDBTABLENAME = "goldwind";

    /**
     * 实时数据库连接池最大连接数
     */
    private static int RTDBMAXPOOLSIZE = 100;

    /**
     * 实时数据库连接池连接数
     */
    private static int RTDBPOOLSIZE = 100;

    /**
     * @return the gROUPIP
     */
    public static String getGROUPIP()
    {
        return GROUPIP;
    }

    /**
     * @param gROUPIP
     *            the gROUPIP to set
     */
    public static void setGROUPIP(String gROUPIP)
    {
        GROUPIP = gROUPIP;
    }

    /**
     * @return the gROUPPORT
     */
    public static int getGROUPPORT()
    {
        return GROUPPORT;
    }

    /**
     * @param gROUPPORT
     *            the gROUPPORT to set
     */
    public static void setGROUPPORT(int gROUPPORT)
    {
        GROUPPORT = gROUPPORT;
    }

    /**
     * @return the dBTYPE
     */
    public static DatabaseType getDBTYPE()
    {
        return DBTYPE;
    }

    /**
     * @param dBTYPE
     *            the dBTYPE to set
     */
    public static void setDBTYPE(DatabaseType dBTYPE)
    {
        DBTYPE = dBTYPE;
    }

    /**
     * @return the dBURL
     */
    public static String getDBURL()
    {
        return DBURL;
    }

    /**
     * @param dBURL
     *            the dBURL to set
     */
    public static void setDBURL(String dBURL)
    {
        DBURL = dBURL;
    }

    /**
     * @return the dBSERVER
     */
    public static String getDBSERVER()
    {
        return DBSERVER;
    }

    /**
     * @param dBSERVER
     *            the dBSERVER to set
     */
    public static void setDBSERVER(String dBSERVER)
    {
        DBSERVER = dBSERVER;
    }

    /**
     * @return the pORT
     */
    public static int getPORT()
    {
        return PORT;
    }

    /**
     * @param pORT
     *            the pORT to set
     */
    public static void setPORT(int pORT)
    {
        PORT = pORT;
    }

    /**
     * @return the dBUSER
     */
    public static String getDBUSER()
    {
        return DBUSER;
    }

    /**
     * @param dBUSER
     *            the dBUSER to set
     */
    public static void setDBUSER(String dBUSER)
    {
        DBUSER = dBUSER;
    }

    /**
     * @return the dBPASSWORD
     */
    public static String getDBPASSWORD()
    {
        return DBPASSWORD;
    }

    /**
     * @param dBPASSWORD
     *            the dBPASSWORD to set
     */
    public static void setDBPASSWORD(String dBPASSWORD)
    {
        DBPASSWORD = dBPASSWORD;
    }

    /**
     * @return the dBNAME
     */
    public static String getDBNAME()
    {
        return DBNAME;
    }

    /**
     * @param dBNAME
     *            the dBNAME to set
     */
    public static void setDBNAME(String dBNAME)
    {
        DBNAME = dBNAME;
    }

    /**
     * @return the mAXPOOLSIZE
     */
    public static int getMAXPOOLSIZE()
    {
        return MAXPOOLSIZE;
    }

    /**
     * @param mAXPOOLSIZE
     *            the mAXPOOLSIZE to set
     */
    public static void setMAXPOOLSIZE(int mAXPOOLSIZE)
    {
        MAXPOOLSIZE = mAXPOOLSIZE;
    }

    /**
     * @return the mINPOOLSIZE
     */
    public static int getMINPOOLSIZE()
    {
        return MINPOOLSIZE;
    }

    /**
     * @param mINPOOLSIZE
     *            the mINPOOLSIZE to set
     */
    public static void setMINPOOLSIZE(int mINPOOLSIZE)
    {
        MINPOOLSIZE = mINPOOLSIZE;
    }

    /**
     * @return the dBTIMEOUT
     */
    public static int getDBTIMEOUT()
    {
        return DBTIMEOUT;
    }

    /**
     * @param dBTIMEOUT
     *            the dBTIMEOUT to set
     */
    public static void setDBTIMEOUT(int dBTIMEOUT)
    {
        DBTIMEOUT = dBTIMEOUT;
    }

    /**
     * @return the iNSERTDBTIMEOUT
     */
    public static int getINSERTDBTIMEOUT()
    {
        return INSERTDBTIMEOUT;
    }

    /**
     * @param iNSERTDBTIMEOUT
     *            the iNSERTDBTIMEOUT to set
     */
    public static void setINSERTDBTIMEOUT(int iNSERTDBTIMEOUT)
    {
        INSERTDBTIMEOUT = iNSERTDBTIMEOUT;
    }

    /**
     * @return the lOCALIP
     */
    public static String getLOCALIP()
    {
        return LOCALIP;
    }

    /**
     * @param lOCALIP
     *            the lOCALIP to set
     */
    public static void setLOCALIP(String lOCALIP)
    {
        LOCALIP = lOCALIP;
    }

    /**
     * @return the cOMTESTWARNTIME
     */
    public static int getCOMTESTWARNTIME()
    {
        return COMTESTWARNTIME;
    }

    /**
     * @param cOMTESTWARNTIME
     *            the cOMTESTWARNTIME to set
     */
    public static void setCOMTESTWARNTIME(int cOMTESTWARNTIME)
    {
        COMTESTWARNTIME = cOMTESTWARNTIME;
    }

    /**
     * @return the cOMTESTWARNCONTINUETIME
     */
    public static int getCOMTESTWARNCONTINUETIME()
    {
        return COMTESTWARNCONTINUETIME;
    }

    /**
     * @param cOMTESTWARNCONTINUETIME
     *            the cOMTESTWARNCONTINUETIME to set
     */
    public static void setCOMTESTWARNCONTINUETIME(int cOMTESTWARNCONTINUETIME)
    {
        COMTESTWARNCONTINUETIME = cOMTESTWARNCONTINUETIME;
    }

    /**
     * @return the cHECKDBPERIOD
     */
    public static int getCHECKDBPERIOD()
    {
        return CHECKDBPERIOD;
    }

    /**
     * @param cHECKDBPERIOD
     *            the cHECKDBPERIOD to set
     */
    public static void setCHECKDBPERIOD(int cHECKDBPERIOD)
    {
        CHECKDBPERIOD = cHECKDBPERIOD;
    }

    /**
     * @return the lANGUAGE
     */
    public static String getLANGUAGE()
    {
        return LANGUAGE;
    }

    /**
     * @param lANGUAGE
     *            the lANGUAGE to set
     */
    public static void setLANGUAGE(String lANGUAGE)
    {
        LANGUAGE = lANGUAGE;
    }

    /**
     * @return the wFID
     */
    public static String getWFID()
    {
        return WFID;
    }

    /**
     * @param wFID
     *            the wFID to set
     */
    public static void setWFID(String wFID)
    {
        WFID = wFID;
    }

    public static List<WeatherConfig> getWeatherconfig()
    {
        return WEATHERCONFIG;
    }

    public static HashMap<String, List<String>> getTyphoonConfig()
    {
        return TYPHOONCONFIG;
    }

    public static boolean getWeatherData()
    {
        return GETWEATHERDATA;
    }

    public static void setGetWeatherData(boolean value)
    {
        GETWEATHERDATA = value;
    }

    public static boolean isWeatherWarning()
    {
        return WEATHERWARNING;
    }

    public static void setWeatherWarning(boolean value)
    {
        WEATHERWARNING = value;
    }

    public static boolean getTyphoonData()
    {
        return GETTYPHOONDATA;
    }

    public static void setGetTyphoonData(boolean value)
    {
        GETTYPHOONDATA = value;
    }

    public static boolean isTyphoonWarning()
    {
        return TYPHOONWARNING;
    }

    public static void setTyphoonWarning(boolean value)
    {
        TYPHOONWARNING = value;
    }

    public static boolean isDataComm()
    {
        return DATACOMM;
    }

    public static void setDataComm(boolean value)
    {
        DATACOMM = value;
    }

    public static String getRTDBSERVER()
    {
        return RTDBSERVER;
    }

    public static void setRTDBSERVER(String rTDBSERVER)
    {
        RTDBSERVER = rTDBSERVER;
    }

    public static int getRTDBPORT()
    {
        return RTDBPORT;
    }

    public static void setRTDBPORT(int rTDBPORT)
    {
        RTDBPORT = rTDBPORT;
    }

    public static String getRTDBUSER()
    {
        return RTDBUSER;
    }

    public static void setRTDBUSER(String rTDBUSER)
    {
        RTDBUSER = rTDBUSER;
    }

    public static String getRTDBPASSWORD()
    {
        return RTDBPASSWORD;
    }

    public static void setRTDBPASSWORD(String rTDBPASSWORD)
    {
        RTDBPASSWORD = rTDBPASSWORD;
    }

    public static String getRTDBTABLENAME()
    {
        return RTDBTABLENAME;
    }

    public static void setRTDBTABLENAME(String rTDBTABLENAME)
    {
        RTDBTABLENAME = rTDBTABLENAME;
    }

    public static int getRTDBMAXPOOLSIZE()
    {
        return RTDBMAXPOOLSIZE;
    }

    public static void setRTDBMAXPOOLSIZE(int rTDBMAXPOOLSIZE)
    {
        RTDBMAXPOOLSIZE = rTDBMAXPOOLSIZE;
    }

    public static int getRTDBPOOLSIZE()
    {
        return RTDBPOOLSIZE;
    }

    public static void setRTDBPOOLSIZE(int rTDBPOOLSIZE)
    {
        RTDBPOOLSIZE = rTDBPOOLSIZE;
    }

    public static Double getWINDVALUENUM()
    {
        return WINDVALUENUM;
    }

    public static void setWINDVALUENUM(Double wINDVALUENUM)
    {
        WINDVALUENUM = wINDVALUENUM;
    }

    public static Double getINVALUE()
    {
        return INVALUE;
    }

    public static void setINVALUE(Double iNVALUE)
    {
        INVALUE = iNVALUE;
    }
}
