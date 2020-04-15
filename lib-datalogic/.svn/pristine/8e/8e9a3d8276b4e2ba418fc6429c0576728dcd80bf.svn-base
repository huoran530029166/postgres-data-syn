package com.goldwind.datalogic.utils;

/**
 * 基础定义 变量
 *
 * @author 谭璟
 */
public class ControlProcessDef
{
    /**
     * 一分钟数据最大秒数
     */
    public static final int ONEMINUTEDATAMAXSECOND = 50;
    /**
     * 五分钟数据最大分钟数
     */
    public static final int FIVEMINUTEDATAMAXMINUTE = 3;
    /**
     * 十分钟数据最大分钟数
     */
    public static final int TENMINUTEDATAMAXMINUTE = 9;
    /**
     * 日数据最大小时数
     */
    public static final int DAYDATAMAXHOUR = 23;
    /**
     * 修改文件key最大数量
     */
    public static final int MODIFYFILEKEYMAXNUM = 10000;

    public enum NetDataTimeType
    {
        /**
         * 时间类型
         */
        Msel(0), Second(1), OneMinute(2), TenMinutes(3);

        /**
         * 类型值
         */
        private int result;

        NetDataTimeType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    public enum MaintainDataType
    {
        /**
         * 没有维护
         */
        NoMaintain(-1),
        /**
         * 十分钟数据
         */
        TENMINUTES(0),
        /**
         * 功率曲线
         */
        POWERCURVE(1),
        /**
         * 设备故障数据
         */
        DEVICEFAULT(2),
        /**
         * 电场数据
         */
        FARMDATA(3),
        /**
         * 电场功率曲线
         */
        FARMPOWCURVE(4),
        /**
         * 一分钟数据
         */
        ONEDATA(5),
        
        /**
         * 巴盟维护损失电量维护统计
         */
        LOSSELECTIME(2031),
        
        /**
         * 【风机能校维护】风机能效数据维护 
         */
        ENERGYEFFICIENCY(2032),
        
        /**
         * 电场一分钟数据
         */
        FARMONEDATA(6),
        /**
         * 测风塔五分钟数据
         */
        TOWERFIVEDATA(7),
        /**
         * 区域五分钟数据
         */
        REGIONFIVEDATA(8),
        /**
         * 五分钟数据生成
         */
        FIVEDATAGEN(9),
        /**
         * 日数据统计
         */
        DAYDATASTAT(10),
        /**
         * 变位数据
         */
        CHANGEDATA(11),
        /**
         * 设备状态
         */
        DEVICESTATUS(12),
        /**
         * PLC文件
         */
        PLCFILE(13),
        /**
         * 实时数据
         */
        REALTIMEDATA(14),
        /**
         * 通讯状态
         */
        COMSTATE(15),
        /**
         * 微网统计
         */
        MICROGRID(16),
        /**
         * 十分钟数据修正
         */
        TENMINUTESUPD(17),
        /**
         * 日数据修正
         */
        DAYDATEUPD(18),
        /**
         * 102协议数据表
         */
        TIMEDATA(19),
        /**
         * 告警数据
         */
        WARNLOG(20),

        /**
         * 十五分钟数据维护（蒙东项目）
         */
        FIFTEENDATA(21),
        /**
         * 电流离散率
         */
        CURRENTDRE(22),

        /**
         * 按环境条件区间统计发电量(上海申能)
         */
        SPEEDAREAPOWER(23),

        /**
         * 设备月时间可利用率
         */
        WTAVAILBASEDATA(24),

        /**
         * 十五分钟数据
         */
        FIFTEENMINDATA(25),

        /**
         * 数据库备份
         */
        DBBACKUP(1000),
        /**
         * 过期数据清理
         */
        OVERDUEDATA(1001),
        /**
         * 过期文件清理
         */
        OVERDUEFILE(1002),
        /**
         * 文件归档
         */
        FILEARCHIVE(1003),

        /**
         * 限功率信息
         */
        FARMLIMITINFO(1004),

        /**
         * 限电时间功率
         */
        FARMLIMITPOWER(1005),

        /**
         * 跳变数据
         */
        JUMPDATA(1006),

        /**
         * 产能可以利用率
         */
        EXAMPLEDATA(1007),

        /**
         * 一分钟设备故障损失电量
         */
        ONELOSSDATA(1008),

        /**
         * 发电量/上网电量
         */
        DAYELEC(1009),

        /**
         * 逆变器
         */
        INVERTER(1100),

        /**
         * 运行状态字
         */
        RUNSTATE(2000),

        /**
         * 变位数据
         */
        Changedata(2001),

        /**
         * 场内外 受阻
         */
        POWERGENERATION(2002),

        /**
         * 输变电损失电量
         */
        POWERTRANS(2030);

        /**
         * 类型值
         */
        private int result;

        MaintainDataType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    public enum StatDataType
    {
        /**
         * 数据类型
         */
        TenMinutes, PowCurve, DayData, RealtimeData, DeviceFault, BFile, FFile, ComIntTime
    }

    public enum MaintainDateType
    {
        /**
         * 维护数据类型
         */
        FifteenMinutes(0), Hour(1), Day(2), Month(3);

        /**
         * 类型值
         */
        private int result;

        MaintainDateType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    public enum AMDateType
    {
        /**
         * AM系统数据模块类型（1：安全管理，2：运维管理，3：运行交接班管理，4：物资管理，5：设备管理）
         */
        Safedata(1), Operdata(2), Handoverdata(3), Materialdata(4), Equipdata(5);

        /**
         * 类型值
         */
        private int result;

        AMDateType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        /**
         * 数值转枚举
         *
         * @param value
         *            数值
         * @return 枚举类型
         */
        public static AMDateType valueOf(int value)
        {
            switch (value) {
                case 1:
                    return Safedata;
                case 2:
                    return Operdata;
                case 3:
                    return Handoverdata;
                case 4:
                    return Materialdata;
                case 5:
                    return Equipdata;
                default:
                    return null;
            }
        }
    }

    public enum MaintainStatType
    {
        /**
         * 维护设备类型
         */
        Device, Region, Line, Farm, Other
    }

    public enum PredictStatType
    {
        /**
         * 数据统计类型
         */
        AVG, MAX, MIN, max, min, Difference, AVGSUM
    }

    public enum FileDataType
    {
        /**
         * 十分钟数据
         */
        TenMin(0),

        /**
         * 功率曲线数据
         */
        PowCurve(1),

        /**
         * 设备故障数据
         */
        DevFault(2),

        /**
         * 历史瞬态数据
         */
        RealData(3),

        /**
         * 旧版历史瞬态数据
         */
        OldRealData(4),

        /**
         * 数据库错误数据
         */
        DbFaultData(5),

        /**
         * 跳变数据
         */
        JumpTime(6),

        /**
         * 文件错误数据
         */
        FileFaultData(7),

        /**
         * 转发缓存数据
         */
        TransBuff(8),

        /**
         * 数据库缓存数据
         */
        DbBuff(9),

        /**
         * 数据库ddl缓存数据
         */
        DbDdlBuff(10),

        /**
         * 任务缓存数据
         */
        TaskBuff(11),

        /**
         * 历史瞬态同步数据
         */
        RealSynchData(12),

        /**
         * 历史同步数据
         */
        HisSynchData(13),

        /**
         * plc文件归档日志
         */
        PlcFileArchiveLog(14),

        /**
         * 实时数据库缓存数据
         */
        RtDbBuff(15),

        /**
         * 设备状态数据
         */
        DevStatus(16),

        /**
         * 通讯错误数据
         */
        CommFaultData(17),
        /**
         * 用户行为数据
         */
        UserBehavierData(18),
        /**
         * warnend缓存
         */
        WarnEndBuff(19),
        /**
         * 故障原数据
         */
        FaultDataFile(20),
        /**
         * 上传数据缓存
         */
        UploadBuff(21),
        /**
        *
        */
        StrongTcpBuffer(22),
        /**
         * 消息订阅数据缓存
         */
        SubscribeBuff(24),
        /**
         * 操作票数据
         */
        TicketDataBuffer(25),
        /**
         * 过反向隔离组播数据
         */
        ReGroupDataBuffer(26),
        /**
         * 过反向隔离tcp数据
         */
        ReTcpDataBuffer(27),
        /**
         * 过反向隔离拓传指令数据
         */
        ReExtDataBuffer(28),
        /**
         * 过反向隔离拓传结果数据
         */
        ReExtResultDataBuffer(29),
        /**
         * 告警数据
         */
        WarnLog(30),
        /**
         * 变位数据
         */
        ChangeData(31),
        /**
         * 虚拟遥测点数据
         */
        VirtualTelemetry(32),
        /**
         * 控制日志数据
         */
        OperLog(33),
        /**
         * 新增一分钟数据
         */
        ONEMINDATA(34),
        /**
         * 五分钟数据
         */
        FIVEMINDATA(35),
        /**
         * 未知类型
         */
        UNKNOW(10000);

        /**
         * 类型值
         */
        private int result;

        FileDataType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    public enum NetDataTransType
    {
        /**
         * 转发数据类型
         */
        NoMainRealtime, MainRealtime, TenMinutes;

        /**
         * 数值转枚举
         *
         * @param value
         *            数值
         * @return 枚举类型
         */
        public static NetDataTransType valueOf(int value)
        {
            switch (value) {
                case 1:
                    return NoMainRealtime;
                case 2:
                    return MainRealtime;
                case 3:
                    return TenMinutes;
                default:
                    return null;
            }
        }
    }

    public enum ComputeFormulaType
    {
        /**
         * 公式类型
         */
        First, Last, Diff
    }

    /**
     * 历史瞬态数据保存模式
     *
     * @author 王瑞博
     */
    public enum RealtimeDataSaveMode
    {
        /**
         * 文件
         */
        File(0),
        /**
         * 实时库
         */
        RtDb(1),
        /**
         * 文件和实时库
         */
        FileAndRtDb(2),
        /**
         * 不保存
         */
        NoStorage(30);

        /**
         * 类型值
         */
        private int result;

        RealtimeDataSaveMode(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 历史瞬态数据统计模式
     */
    public enum RealtimeDataStatMode
    {
        /**
         * 文件
         */
        File(0),
        /**
         * 实时库
         */
        RtDb(1),
        /**
         * 不统计
         */
        NoStat(2);

        /**
         * 类型值
         */
        private int result;

        RealtimeDataStatMode(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 数据库连通状态
     */
    public enum DbState
    {
        /**
         * 中断
         */
        Disconnect,
        /**
         * 繁忙
         */
        Busy,
        /**
         * 正常
         */
        Normal
    }

    /**
     * 损失电量类型
     */
    public enum LossType
    {

        /**
         * 正常
         */
        Normal(0),

        /**
         * 调度限功率
         */
        Dispatchelc(2001),

        /**
         * 风机维护
         */
        Windcontrol(2002),

        /**
         * 技术待命
         */
        Technicalstandby(2003),

        /**
         * 远程停机
         */
        Romatestop(2004),

        /**
         * 电网故障
         */
        PowerFault(2005),

        /**
         * 故障停机
         */
        FaultStop(2006),

        /**
         * 就地停机
         */
        Otherdispatchelc(2007),

        /**
         * 其他限功率
         */
        OtherLimitPower(2008);

        /**
         * 类型值
         */
        private int result;

        LossType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 运行状态字 标识
     *
     * @author 谭璟
     */
    public enum RunState
    {
        /**
         * 正常发电
         */
        IAOGFP(3001),

        /**
         * 欠发并网发电
         */
        IAOGPP(3002),

        /**
         * 技术待机
         */
        IAONGTS(3003),

        /**
         * 超出环境条件
         */
        IAONGEN(3004),

        /**
         * 请求停机
         */
        IAONGRS(3005),

        /**
         * 超出电气规范
         */
        IAONGEL(3006),

        /**
         * 定期维护
         */
        IANOSM(3007),

        /**
         * 计划性改进：技改、软件升级、风机缺陷停机等
         */
        PLANNEDCORRECTIVEACTIONS(3008),

        /**
         * 故障停机：包括故障停机后的 响应 诊断 物流 故障检修
         */
        FORCEDOUTAGE(3009),

        /**
         * 暂停作业
         */
        SUSPENDED(3010),

        /**
         * 不可抗力
         */
        FORCEMAJEURE(3011),

        /**
         * 数据不可用
         */
        IU(3012),

        /**
         * 无效
         */
        NOTHING(-1);

        /**
         * 类型值
         */
        private int result;

        RunState(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 公式集合
     *
     * @author 谭璟
     */
    public enum TypeId
    {
        /**
         * 损失电量
         */
        losstype("20"),

        /**
         * 运行状态字30
         */
        runstate("30"),

        /**
         * 逆变器 40
         */
        inverter("40");

        /**
         * 类型值
         */
        private String result;

        TypeId(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 风机类型
     */
    public enum WtifType
    {
        /**
         * 3M风机
         */
        GW3000("GW3000"),

        /**
         * 2.5M风机
         */
        GW2500("GW2500"),

        /**
         * 2M风机
         */
        GW2000("GW2000"),

        /**
         * 1500风机
         */
        GW1500("GW1500"),

        /**
         * 750风机
         */
        GW750("GW750"),

        /**
         * 600风机
         */
        GW600("GW600");

        /**
         * 标识
         */
        private String result;

        WtifType(String str)
        {
            this.result = str;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 样板风机 自身理论功率 传递函数
     *
     * @author 谭璟
     */
    public enum DeviceChangeType
    {
        /**
         * 不用计算的方案
         */
        plannocal(-1),

        /**
         * 样板风机类型 方案一
         */
        plana(1),

        /**
         * 方案二
         */
        planb(2),

        /**
         * 方案三
         */
        planc(3),

        /**
         * 方案四
         */
        pland(4),

        /**
         * 方案五
         */
        plane(5),

        /**
         * 方案六
         */
        planf(6);

        /**
         * 数值
         */
        private int result;

        DeviceChangeType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * EBA 操作标识
     *
     * @author 谭璟
     */
    public enum DataBaseExcute
    {
        /**
         * 增加
         */
        Add("add"),

        /**
         * 删除
         */
        Delete("delete"),

        /**
         * 更新
         */
        Updata("updata");

        /**
         * 标识内容
         */
        private String result;

        DataBaseExcute(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 周时区
     *
     * @author 谭璟
     */
    public enum WeekType
    {

        /**
         * 中国周时区
         */
        China("china"),

        /**
         * 国外时区
         */
        English("english");

        /**
         * 标识内容
         */
        private String result;

        WeekType(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 逆变器
     *
     * @author 谭璟
     */
    public enum Inverter
    {
        /**
         * 故障停机
         */
        FalutStop(4001),

        /**
         * 降额
         */
        Derate(4002),

        /**
         * 限额
         */
        Limit(4003),

        /**
         * 远程停机
         */
        RemoteStop(4004);

        /**
         * 参数
         */
        private int result;

        Inverter(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 发送类型
     *
     * @author 谭璟
     */
    public enum SendType
    {
        /**
         * go
         */
        Go(1),

        /**
         * 邮箱
         */
        Mail(2),

        /**
         * 信息
         */
        Msg(3);

        /**
         * 参数
         */
        private int result;

        SendType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

    }

    /**
     * 结果返回类型
     *
     * @author 冯春源
     */
    public enum ReturnType
    {
        /**
         * 同步返回
         */
        Syn(0),

        /**
         * 异步返回
         */
        Asyn(1);

        /**
         * 参数
         */
        private int result;

        ReturnType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        public static ReturnType getType(int value)
        {
            switch (value) {
                case 0:
                    return Syn;
                case 1:
                    return Asyn;
                default:
                    return null;
            }
        }
    }

    /**
     * 电场级电量枚举
     * 
     * @author 谭璟
     *
     */
    public enum ExpSet
    {
        /**
         * 上网电量
         */
        LINE(0),

        /**
         * 升压站进线电量/电场出线电量
         */
        ONLINE(1),

        /**
         * 下网电量
         */
        OFFGRID(2);

        /**
         * 参数
         */
        private int result;

        ExpSet(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }
}
