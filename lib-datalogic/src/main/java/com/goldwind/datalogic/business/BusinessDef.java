package com.goldwind.datalogic.business;

/**
 * 
 * @author Administrator
 *
 */
public class BusinessDef
{
    /**
     * 文件路径是否使用电场
     */
    private static boolean filePathUseWf = false;
    /**
     * 二版转换特殊路径,与新版特殊路径位置相同
     */
    private static String[] twoSpecialPath = new String[] { "WTUR.TurSt.actsp", "WTUR.Flt.actsp", "WTUR.Flt.Alarm" };

    /**
     * 新版转换特殊路径
     */
    private static String[] newSpecialPath = new String[] { "WTUR.TurSt.Rs.S", "WTUR.Flt.Rs.S", "WTUR.Alam.Rs.S" };
    /**
     * 同步关系库的表数量
     */
    private static volatile int synchroTableNumber = 0;
    /**
     * 同步关系库的表剩余数量
     */
    private static volatile int synchroTableLessNumber = 0;
    /**
     * 十分钟对应小时数的最小常量（单位：小时）
     */
    public static final double TENMINUTETOHOURMIN = 0.16;
    /**
     * 十分钟对应小时数的常量（单位：小时）
     */
    public static final double TENMINUTETOHOUR = 0.166666;
    /**
     * 同步本地库状态
     */
    private static volatile SynchroState localDBSynchroState = SynchroState.Unknow;
    /**
     * 同步内存数据状态
     */
    private static volatile SynchroState memoryDataSynchroState = SynchroState.Unknow;

    /**
     * 十分钟初始列的长度
     */
    public static final int STATISTICDATACOLUMNNUM = -3;
    /**
     * 日数据初始列的长度
     */
    public static final int DAYDATACOLUMNNUM = -6;

    public static SynchroState getLOCALDBSYNCHROSTATE()
    {
        return localDBSynchroState;
    }

    public static void setLOCALDBSYNCHROSTATE(SynchroState lOCALDBSYNCHROSTATE)
    {
        localDBSynchroState = lOCALDBSYNCHROSTATE;
    }

    public static SynchroState getMEMORYDATASYNCHROSTATE()
    {
        return memoryDataSynchroState;
    }

    public static void setMEMORYDATASYNCHROSTATE(SynchroState mEMORYDATASYNCHROSTATE)
    {
        memoryDataSynchroState = mEMORYDATASYNCHROSTATE;
    }

    public static int getSYNCHROTABLELESSNUMBER()
    {
        return synchroTableLessNumber;
    }

    public static void setSYNCHROTABLELESSNUMBER(int sYNCHROTABLELESSNUMBER)
    {
        synchroTableLessNumber = sYNCHROTABLELESSNUMBER;
    }

    public static int getSYNCHROTABLENUMBER()
    {
        return synchroTableNumber;
    }

    public static void setSYNCHROTABLENUMBER(int sYNCHROTABLENUMBER)
    {
        synchroTableNumber = sYNCHROTABLENUMBER;
    }

    /**
     * 单页显示数量
     * 
     */
    public static final int PAGESIZE = 20;

    public static boolean isFILEPATHUSEWF()
    {
        return filePathUseWf;
    }

    public static void setFILEPATHUSEWF(boolean fILEPATHUSEWF)
    {
        filePathUseWf = fILEPATHUSEWF;
    }

    public static String[] getTWOSPECIALPATH()
    {
        return twoSpecialPath;
    }

    public static void setTWOSPECIALPATH(String[] tWOSPECIALPATH)
    {
        twoSpecialPath = tWOSPECIALPATH;
    }

    public static String[] getNEWSPECIALPATH()
    {
        return newSpecialPath;
    }

    public static void setNEWSPECIALPATH(String[] nEWSPECIALPATH)
    {
        newSpecialPath = nEWSPECIALPATH;
    }

    /**
     * iec路径电量类型
     * 
     * @author Administrator
     *
     */
    public enum IecPathElecType
    {
        /**
         * 模拟量(遥测量)
         */
        Analog, //
        /**
         * 累计量(遥脉量)
         */
        Total, //
        /**
         * 数字量(遥信量)
         */
        Digital, //
        /**
         * 数字量(遥信量_双点)
         */
        Digital_Bilateral,
        /**
         * 计算量
         */
        Calculation, //
        /**
         * 字符串
         */
        String, //
        /**
         * 数字量(遥控量)
         */
        Digital_Control,
        /**
         * 遥调量
         */
        Digital_Adjust,
        /**
         * 时间量
         */
        Time,
        /**
         * 未知
         */
        Unknown //
    }

    /**
     * 操作日志类型
     * 
     * @author Administrator
     *
     */
    public enum OperLogType
    {
        /**
         * 设备指令
         */
        Order(1), //
        /**
         * 任务
         */
        Task(2), //
        /**
         * 用户
         */
        User(3), //
        /**
         * 所有
         */
        All(100); //

        /**
         * 
         */
        private int value;

        OperLogType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * 数据转换类型
     * 
     * @author Administrator
     *
     */
    public enum DataConvertType
    {
        /**
         * 
         */
        TwoToTwo, TwoToNew, NewToNew, NewToTwo
    }

    /**
     * 中控系统类型
     * 
     * @author Administrator
     *
     */
    public enum CenterMonitorType
    {
        /**
         * 
         */
        Two, New
    }

    /**
     * 数据传输类型
     * 
     * @author Administrator
     *
     */
    public enum DataTransType
    {
        /**
         * 瞬态数据
         */
        Main, //
        /**
         * 历史瞬态数据
         */
        Realtime, //
        /**
         * 历史数据
         */
        His //
    }

    /**
     * 数据类别
     * 
     * @author Administrator
     *
     */
    public enum MonitorDataClass
    {
        /**
         * 数据库数据
         */
        db, //
        /**
         * 文件数据
         */
        file, //
        /**
         * 数据库汇总数据
         */
        dbstat //
    }

    /**
     * 数据类型
     * 
     * @author Administrator
     *
     */
    public enum MonitorDataType
    {
        /**
         * 无
         */
        Non(0),
        /**
         * 一分钟
         */
        OneData(1),
        /**
         * 十分钟
         */
        StatisticData(2),
        /**
         * 功率曲线
         */
        PowCurve(3),
        /**
         * 告警信息
         */
        WtAlarmInfo(4),
        /**
         * 错误信息
         */
        WtErrorInfo(5),
        /**
         * 通讯状态信息
         */
        WtComStateInfo(6),
        /**
         * 状态信息
         */
        WtStatusInfo(7),
        /**
         * 变位数据
         */
        ChangeData(8),
        /**
         * 五分钟数据
         */
        FIVEMINDATA(9),
        /**
         * 运行日志
         */
        RunLog(10),
        /**
         * 操作日志
         */
        OperLog(11),
        /**
         * 计划电量
         */
        ElecPlan(12),
        /**
         * plc文件
         */
        PlcFileRec(13),
        /**
         * 错误汇总
         */
        WtErrorTotal(14),
        /**
         * 其他设备警告数据
         */
        WarnLog(15),
        /**
         * 事件数据
         */
        WtEventLog(16),
        /**
         * 实时数据
         */
        RealtimeData(101),
        /**
         * 十分钟文件
         */
        TenFile(102),
        /**
         * 功率曲线文件
         */
        PowerCurveFile(103),
        /**
         * 设备故障文件
         */
        DeviceFaultFile(104),
        /**
         * 电厂一分钟数据
         */
        FarmOneData(201),
        /**
         * 损失电量汇总
         */
        LossElecTotal(202),
        /**
         * 日数据
         */
        DayData(203),
        /**
         * 风场十分钟数据
         */
        FarmTenData(204),
        /**
         * 风场日数据
         */
        FarmDayData(205),
        /**
         * 历史风玫瑰数据
         */
        HisWindRose(206),
        /**
         * 历史风频数据
         */
        HisWindFrq(207),
        /**
         * 历史功率曲线
         */
        HisPowCurve(208),
        /**
         * 风场功率曲线
         */
        FarmPowCurve(209),
        /**
         * 风场历史功率曲线
         */
        FarmHisPowCurve(210),
        /**
         * 风场限功率
         */
        FarmLimitPower(219),
        /**
         * 数据状态
         */
        DataStat(220),
        /**
         * 能量平台用数据
         */
        EnergyUse_Data(221),
        /**
         * 未知
         */
        UsePercent_Stat(222),

        /**
         * 损失电量维护一分钟
         */
        EnergyUse_OneData(223),

        /**
         * 升压站
         */
        Daydata_1(224),

        /**
         * 十分钟修正
         */
        StatisticDataUpd(225),

        /**
         * 日数据修正
         */
        DayDataUpd(226),
        /**
         * 102协议电能表数据
         */
        TimeData(227),
        /**
         * warnlog中告警udp的缓存备注数据
         */
        UdpBuffer(228),
        /**
         * 温度功率曲线数数据
         */
        EnvTemperatureCurve(229),
        /**
         * 功率控制一分钟数据
         */
        EMSDONEDATA(230),
        /**
         * 新增 一分钟数据
         */
        ONEMINDATA(231),
        /**
         * Gis地图和监控对象)监控对象的结果数据
         */
        MONITORDATA(232),
        /**
         * 十五分钟数据
         */
        FIFTEENMINDATA(233),
        /**
         * 历史风时数数据
         */
        HisWindDur(234);

        /**
         * 枚举值
         */
        private int value;

        MonitorDataType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    public enum UsePercentStatType
    {
        /**
         * 年
         */
        Year(1), //
        /**
         * 当年
         */
        CurrYear(2), //
        /**
         * 月
         */
        Month(3), //
        /**
         * 当月
         */
        CurrMonth(4), //
        /**
         * 周
         */
        Week(5), //
        /**
         * 当周
         */
        CurrWeek(6), //
        /**
         * 双周
         */
        DoubWeek(7), //
        /**
         * 当双周
         */
        CurrDoubWeek(8); //

        /**
         * 枚举值
         */
        private int value;

        UsePercentStatType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * 服务器服务类型
     * 
     * @author Administrator
     *
     */
    public enum ServerServiceType
    {
        /**
         * 关系库
         */
        RelationalDb, //
        /**
         * 实时库
         */
        RealtimeDb, //
        /**
         * 内存库
         */
        MemoryDb //
    }

    /**
     * IEC量的数据类型
     */
    public enum IecPathDataType
    {
        /**
         * 布尔型
         */
        Bool,
        /**
         * 字符型
         */
        String,
        /**
         * 整型
         */
        Integer,
        /**
         * 双精度实数
         */
        Double,
        /**
         * 日期类型
         */
        dateTime,
        /**
         * 未知
         */
        unknow
    }

    public enum SynchroState
    {
        /**
         * 开始同步
         */
        Start,
        /**
         * 同步结束
         */
        Stop,
        /**
         * 未知
         */
        Unknow
    }

    public enum ConditionType
    {
        /**
         * 标杆机组版本号
         */
        BenchmarkCondition,
        /**
         * 扇区管理版本号
         */
        SectorCondition,
        /**
         * 风场设备版本号
         */
        WfDeviceCondition,
        /**
         * 权限管理版本号
         */
        RightsCondition,
        /**
         * 报警配置信息同步
         */
        WarnCondition,
        /**
         * 消息推送版本号
         */
        NoticeCondition,
        /**
         * 其他版本号（频繁更新的表）
         */
        OtherCondition,
        /**
         * 保信主站版本号
         */
        SubstationProtectCondition,
        /**
         * 全同步
         */
        dataCondition
    }

    /**
     * 定值区
     */
    public static final String FIXEDVAL_REGION = "FIXEDVAL_REGION";
    /**
     * 定值区区号
     */
    public static final String FIXEDVALNO = "FIXEDVAL-";
    /**
     * 定值区切换
     */
    public static final String CHANGE_FIXED_REGION = "CHANGE_FIXED_REGION";
    /**
     * 装置复归
     */
    public static final String PROTECT_RESET = "PROTECT_RESET";

    /**
     * 遥信量
     */
    public static final String YX = "YX";
    /**
     * 遥测量
     */
    public static final String YC = "YC";
    /**
     * 遥控量
     */
    public static final String YK = "YK";
    /**
     * 遥调量
     */
    public static final String YT = "YT";
    /**
     * 遥脉量
     */
    public static final String YM = "YM";
    /**
     * 软压板遥信量
     */
    public static final String YX_RYB = "YX-RYB";
    /**
     * 软压板遥控量
     */
    public static final String YK_RYB = "YK-RYB";

    public enum ControlType
    {
        /**
         * 预置
         */
        Standby(0),
        /**
         * 取消预置
         */
        Cancel_standby(1),
        /**
         * 执行
         */
        Execute(2),
        /**
         * 摘牌104
         */
        Delisting(104),
        /**
         * 挂牌103
         */
        Listing(103),
        /**
         * 取消人工置数102
         */
        CancelManualPlacement(102),
        /**
         * 人工置数101
         */
        ManualPlacement(101);

        /**
         * 枚举值
         */
        private int value;

        ControlType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * 操作的 数据表
     * 
     * @author pactera
     *
     */
    public enum DataBaseType
    {
        /**
         * 一分钟
         */
        ONEDATA("OneData"),
        /**
         * 十分钟
         */
        STATISTICDATA("StatisticData"),
        /**
         * 十分钟修正
         */
        STATISTICDATAUPD("StatisticDataUpd"),
        /**
         * 日数据修正
         */
        DAYDATAUPD("DayDataUpd"),
        /**
         * 功率曲线
         */
        POWCURVE("PowCurve"),
        /**
         * 告警信息
         */
        WTALARMINFO("WtAlarmInfo"),
        /**
         * 错误信息
         */
        WTERRORINFO("WtErrorInfo"),
        /**
         * 通讯状态信息
         */
        WTCOMSTATEINFO("WtComStateInfo"),
        /**
         * 状态信息
         */
        WTSTATUSINFO("WtStatusInfo"),
        /**
         * 变位数据
         */
        CHANGEDATA("ChangeData"),
        /**
         * 五分钟数据
         */
        FIVEDATA("FiveData"),
        /**
         * 运行日志
         */
        RUNLOG("RunLog"),
        /**
         * 操作日志
         */
        OPERLOG("OperLog"),
        /**
         * 计划电量
         */
        ELECPLAN("ElecPlan"),
        /**
         * plc文件
         */
        PLCFILEREC("PlcFileRec"),
        /**
         * 错误汇总
         */
        WTERRORTOTAL("WtErrorTotal"),
        /**
         * 其他设备警告数据
         */
        WARNLOG("WarnLog"),
        /**
         * 事件数据
         */
        WTEVENTLOG("WtEventLog"),
        /**
         * 实时数据
         */
        REALTIMEDATA("RealtimeData"),
        /**
         * 十分钟文件
         */
        TENFILE("TenFile"),
        /**
         * 功率曲线文件
         */
        POWERCURVEFILE("PowerCurveFile"),
        /**
         * 设备故障文件
         */
        DEVICEFAULTFILE("DeviceFaultFile"),
        /**
         * 电厂一分钟数据
         */
        FARMONEDATA("FarmOneData"),
        /**
         * 损失电量汇总
         */
        LOSSELECTOTAL("LossElecTotal"),
        /**
         * 日数据
         */
        DAYDATA("DayData"),
        /**
         * 风场十分钟数据
         */
        FARMTENDATA("FarmTenData"),
        /**
         * 风场日数据
         */
        FARMDAYDATA("FarmDayData"),
        /**
         * 历史风玫瑰数据
         */
        HISWINDROSE("HisWindRose"),
        /**
         * 历史风频数据
         */
        HISWINDFRQ("HisWindFrq"),
        /**
         * 历史功率曲线
         */
        HISPOWCURVE("HisPowCurve"),
        /**
         * 风场功率曲线
         */
        FARMPOWCURVE("FarmPowCurve"),
        /**
         * 风场历史功率曲线
         */
        FARMHISPOWCURVE("FarmHisPowCurve"),
        /**
         * 风场限功率
         */
        FARMLIMITPOWER("FarmLimitPower"),

        /**
         * 数据状态
         */
        DATASTAT("DataStat"),

        /**
         * 能量平台用数据
         */
        ENERGYUSE_DATA("EnergyUse_Data"),
        /**
         * 未知
         */
        USEPERCENT_STAT("UsePercent_Stat"),

        /**
         * 损失电量维护一分钟
         */
        ENERGYUSE_ONEDATA("EnergyUse_OneData"),

        /**
         * 升压站
         */
        DAYDATA_1("Daydata_1"),

        /**
         * 线损维护表
         */
        POWERCUTCNF("Powercutcnf"),
        /**
         * 十五分钟（蒙东）
         */
        FIFTEENDATA("Fifteendata");

        /**
         * 枚举值
         */
        private String value;

        DataBaseType(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return this.value;
        }
    }

    /**
     * 电场类型（0是风电场，1是光伏电场）
     * 
     * @author 冯春源
     *
     */
    public enum WfType
    {
        /**
         * 风电场
         */
        WindPower(0),

        /**
         * 光伏电场
         */
        Photovoltaic(1),

        /**
         * 微网电场
         */
        Microgrid(2),

        /**
         * 储能电场
         */
        EnergyStorage(3),

        /**
         * 光热电场
         */
        LightHeat(4),

        /**
         * 水电场
         */
        Hydropower(5),

        /**
         * 离网电场
         */
        OffGrid(6),

        /**
         * 其他电场
         */
        other(1000);

        /**
         * 参数
         */
        private int result;

        WfType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        public static WfType getWfType(int result)
        {
            for (WfType wfType : WfType.values())
            {
                if (result == wfType.getResult())
                {
                    return wfType;
                }
            }
            return null;
        }
    }

    /**
     * 告警系统(一级分类)
     * 
     * @author 冯春源
     *
     */
    public enum WarnSystem
    {
        /**
         * 公共平台
         */
        PUBLICPLATFORM(0),

        /**
         * 风电
         */
        WINDPOWER(1),

        /**
         * 功率控制
         */
        POWERCONTROL(3),

        /**
         * 光伏
         */
        PHOTOVOLTAIC(101),

        /**
         * 升压站
         */
        BOOSTERSTATIONS(102),

        /**
         * 水电
         */
        HYDROELECTRICPOWER(103),
        /**
         * 储能
         */
        ENERGYSTORAGEPOWER(104),
        /**
         * 通用告警系统
         */
        GENERALALARMSYSTEM(10000);

        /**
         * 参数
         */
        private int result;

        WarnSystem(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 告警分类(二级分类/五大分类)
     * 
     * @author 冯春源
     *
     */
    public enum WarnType
    {
        /**
         * 越限
         */
        Overlimit(1),

        /**
         * 异常
         */
        Error(2),

        /**
         * 事故
         */
        Fault(3),

        /**
         * 变位
         */
        Change(4),

        /**
         * 告知
         */
        Inform(5),

        /**
         * 保护动作
         */
        ProtecAction(6),

        /**
         * 保护告警
         */
        protectAlarm(7);

        /**
         * 参数
         */
        private int result;

        WarnType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 告警级别（0提示，1警告，2故障）
     * 
     * @author 冯春源
     *
     */
    public enum WarnLevel
    {
        /**
         * 提示
         */
        Notice(0),

        /**
         * 警告
         */
        Warning(1),

        /**
         * 故障
         */
        Fault(2);

        /**
         * 参数
         */
        private int result;

        WarnLevel(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 状态告警标志（0 事件告警，1 状态告警开始，2 状态告警结束）
     * 
     * @author 冯春源
     *
     */
    public enum WarnFlag
    {
        /**
         * 事件告警
         */
        EventAlarm(0),

        /**
         * 状态告警开始
         */
        StatusAlarmStart(1),

        /**
         * 状态告警结束
         */
        StatusAlarmEnd(2);

        /**
         * 参数
         */
        private int result;

        WarnFlag(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 告警对象类型（0-场站告警，1-设备告警，2-系统运行（原其它告警），101-地理监测 大于1的告警类型统称为其它告警）
     * 
     * @author 冯春源
     *
     */
    public enum WarnObjectType
    {
        /**
         * 场站告警
         */
        StationAlarm(0),

        /**
         * 设备告警
         */
        DeviceAlarm(1),

        /**
         * 系统运行（原其它告警）
         */
        OtherAlarm(2),

        /**
         * 地理监测
         */
        GEOGRAPHYMONITORALARM(101);

        /**
         * 参数
         */
        private int result;

        WarnObjectType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 是否产生告警（0-否，1-是，runlogcode表中配置）
     * 
     * @author 谭璟
     *
     */
    public enum IsSend
    {
        /**
         * 是
         */
        IDO("1"),

        /**
         * 否
         */
        UNDO("0");

        /**
         * 数值
         */
        private String result;

        IsSend(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }

        /**
         * 得到enum对象
         * 
         * @param name
         *            名字
         * @return 返回对象
         */
        public static IsSend getResultToType(String name)
        {
            for (IsSend type : IsSend.values())
            {
                if (type.getResult().equals(name))
                {
                    return type;
                }
            }
            return null;
        }
    }

    public enum IecType
    {
        /**
         * 其他
         */
        OTHER(0),
        /**
         * 是
         */
        POWER(1),

        /**
         * 否
         */
        TIME(2);

        /**
         * 数值
         */
        private int result;

        IecType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        /**
         * 得到enum对象
         * 
         * @param name
         *            名字
         * @return 返回对象
         */
        public static IecType getResultToType(int name)
        {
            for (IecType type : IecType.values())
            {
                if (type.getResult() == name)
                {
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 故障维护类型（0-故障串首位维护total,1-主故障变位数据维护total）
     * 
     * @author 冯春源
     *
     */
    public enum FaultMtType
    {
        /**
         * 故障串首位维护total
         */
        WterrotInfo(0),

        /**
         * 主故障变位数据维护total
         */
        ChangeData(1);

        /**
         * 参数
         */
        private int result;

        FaultMtType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 设备类型
     * 
     * @author limin1
     *
     */
    public enum DeviceType
    {
        /**
         * 风机
         */
        WINDTURBINE(0),
        /**
         * 升压站
         */
        TRANSSUBSTATION(1),
        /**
         * 箱变变压器
         */
        BOXTRANSFORMER(3),

        /**
         * 消防
         */
        FIREFIGHTINGEQUIPMENT(19),

        /**
         * AVC无功功率控制
         */
        AVCEQUIPMENT(103),

        /**
         * AGC有功功率控制
         */
        AGCEQUIPMENT(104);

        /**
         * 参数
         */
        private int result;

        DeviceType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        /**
         * devicetype由int类型转换为枚举类型
         * 
         * @param deviceTypeValue
         * @return
         */
        public static BusinessDef.DeviceType getDeviceType(int deviceTypeValue)
        {
            BusinessDef.DeviceType deviceType = DeviceType.WINDTURBINE;
            for (BusinessDef.DeviceType type : BusinessDef.DeviceType.values())
            {
                if (type.getResult() == deviceTypeValue)
                {
                    deviceType = type;
                    break;
                }
            }
            return deviceType;
        }
    }

    /**
     * 控制日志中指令类型
     * 
     * @author 李敏
     *
     */
    public enum OrderType
    {
        /**
         * 无参控制指令
         */
        NoParam(0),
        /**
         * 有参控制指令
         */
        HasParam(1),
        /**
         * 参数修改
         */
        ParamModify(2);

        /**
         * 参数
         */
        private int result;

        OrderType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 属性类别，0：静态属性，1：动态属性
     * 
     * @author 冯春源
     *
     */
    public enum PropType
    {
        /**
         * 0：静态属性
         */
        STATIC(0),
        /**
         * 1：动态属性
         */
        DYNAMIC(1);

        /**
         * 参数
         */
        private int result;

        PropType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 计算类型，1-四则运算，2-关系运算，3-逻辑运算
     * 
     * @author 冯春源
     *
     */
    public enum ExpressType
    {
        /**
         * 1-四则运算
         */
        ARITHMETIC(1),
        /**
         * 2-关系运算
         */
        RELATIONAL_OPERATION(2),
        /**
         * 3-逻辑运算
         */
        LOGIC_OPERATION(3);

        /**
         * 参数
         */
        private int result;

        ExpressType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 过滤类型，0-屏蔽，1-订阅，2-风格
     * 
     * @author 冯春源
     *
     */
    public enum Filtertype
    {
        /**
         * 0-屏蔽
         */
        SHIELD(0),
        /**
         * 1-订阅
         */
        Subscription(1),
        /**
         * 2-风格
         */
        Style(2);

        /**
         * 参数
         */
        private int result;

        Filtertype(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 电场日数据维护类型（1-发电量，2-损失电量，3-故障，4-功率曲线，5-维护）
     * 
     * @author 冯春源
     *
     */
    public enum WfMtType
    {
        /**
         * 发电量
         */
        POWERGENERATION(1),
        /**
         * 损失电量
         */
        ELECLOSS(2),
        /**
         * 故障&维护
         */
        WFRECORD(3),

        /**
         * 功率曲线
         */
        POWCURVE(4);

        /**
         * 参数
         */
        private int result;

        WfMtType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 通过告警等级编号取得告警等级对应描述
     * 
     * @param levelid
     *            告警等级编号
     * @param language
     *            语言环境
     * @return 告警等级对应描述
     */
    public static String getLevelDesc(String levelid, String language)
    {
        // 告警等级对应描述
        String desc = levelid;

        switch (levelid) {
            case "0":
                if ("zh".equalsIgnoreCase(language))
                {
                    desc = "轻微";
                }
                else
                {
                    desc = "Light";
                }
                break;
            case "1":
                if ("zh".equalsIgnoreCase(language))
                {
                    desc = "一般";
                }
                else
                {
                    desc = "Common";
                }
                break;
            case "2":
                if ("zh".equalsIgnoreCase(language))
                {
                    desc = "严重";
                }
                else
                {
                    desc = "Serious";
                }
                break;
            default:
                break;
        }

        return desc;
    }

    /**
     * 电场日数据维护类型（1-发电量，2-损失电量，3-故障，4-功率曲线，5-维护）
     * 
     * @author 冯春源
     *
     */
    public enum PartType
    {
        /**
         * list分区
         */
        LISTPARTITION(0),
        /**
         * 复合分区
         */
        COMPOUNDPARTITION(1);

        /**
         * 参数
         */
        private int result;

        PartType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        /**
         * 得到PartType对象
         * 
         * @param partType
         *            名字
         * @return 返回对象
         */
        public static PartType getResultToType(int partType)
        {
            for (PartType type : PartType.values())
            {
                if (type.getResult() == partType)
                {
                    return type;
                }
            }
            return null;
        }
    }
}
