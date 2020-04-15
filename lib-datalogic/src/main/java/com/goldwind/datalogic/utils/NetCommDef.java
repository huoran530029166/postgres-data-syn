package com.goldwind.datalogic.utils;

import java.util.Hashtable;
import java.util.Locale;

/**
 * 网络操作定义
 *
 * @author 曹阳
 */
public class NetCommDef
{
    /**
     * 通讯缓存区大小
     */
    private static int tcpSendBufferSize = 8192;
    /**
     * tcp发送缓存区大小
     */
    private static int tcpRecBufferSize = 8192;
    /**
     * udp发送缓存区大小
     */
    private static int udpSendBufferSize = 8192;
    /**
     * udp接收缓存区大小
     */
    private static int udpRecBufferSize = 8192;
    /**
     * tcp接收数据最大线程数
     */
    private static int tcpRecMaxThread = 1000;
    /**
     * 通讯发送超时时间
     */
    private static int commSendTimeOut = 10000;
    /**
     * 通讯接收超时时间
     */
    private static int commRecTimeOut = 60000;
    /**
     * 查询历史瞬态数据超时时间
     */
    private static int getRealtimeTimeOut = 600000;
    /**
     * 通讯监听接收超时时间
     */
    private static int commLisRecTimeOut = 0;
    /**
     * 连续发送等待时间
     */
    private static int commCtuwtTimeOut = 3000;
    /**
     * 连接检测开始时间
     */
    private static int keepAliveTime = 30000;
    /**
     * 连接检测周期
     */
    private static int keepAliveInterval = 1000;
    /**
     * ping指令超时时间
     */
    private static int pingTimeOut = 5000;
    /**
     * 通讯等待测试周期
     */
    private static int commWaitTestPer = 10;
    /**
     * 通讯等待发送周期
     */
    private static int commWaiteSendPer = 1000;
    /**
     * 等待超时
     */
    private static int commWaitTimeOut = 1000000;
    /**
     * 通讯监听异常等待时间
     */
    private static int commListeneXpper = 10000;
    /**
     * 通讯异常重启间隔时间
     */
    private static int commExpresetitv = 30000;
    /**
     * 是否发送数据到隔离设备
     */
    private static boolean sendToIsolation = false;
    /**
     * 是否从隔离设备接收数据
     */
    private static boolean recFromIsolation = false;
    /**
     * 接收的不同隔离类型的端口
     */
    private static int[] recDiffIsolatTypt = new int[0];
    /**
     * 通讯使用新压缩,压缩速度快,但是压缩比小
     */
    private static boolean commusenewComp = false;
    /**
     * 成功标志
     */
    public static final String NETSUCCEEDFLAG = "(ok)";
    /**
     * 成功标志开头
     */
    public static final String NETSUCCEEDFLAGSTART = "(ok";
    /**
     * 成功标志字节数组
     */
    public static final byte[] NETSUCCEEDBUFFER = NETSUCCEEDFLAG.getBytes();
    /**
     * 完成标志
     */
    public static final String NETFINISHFLAG = "(finish)";
    /**
     * 完成标志字节数组
     */
    public static final byte[] NETFINISHBUFFER = NETFINISHFLAG.getBytes();
    /**
     * 等待标志
     */
    public static final String NETWAITFLAG = "(wait)";
    /**
     * 等待标志字节数组
     */
    public static final byte[] NETWAITBUFFER = NETWAITFLAG.getBytes();
    /**
     * 忙碌标志
     */
    public static final String NETBUSYFLAG = "(busy)";
    /**
     * 测试标志
     */
    public static final String NETTESTFLAG = "(test)";
    /**
     * 错误标志
     */
    public static final String NETERRORFLAG = "(error)";
    /**
     * 错误信息标志
     */
    public static final String NETERRORINFOFLAG = "(error:@info)";
    /**
     * 成功信息标志
     */
    public static final String NETSUCCINFOFLAG = "(@info)";
    /**
     * 未定义标志
     */
    public static final String NETNODEFINEFLAG = "(error:nodefine)";
    /**
     * 版本改变标志
     */
    public static final String NETCHANGEDFLAG = "(error:changed)";
    /**
     * 普通消息长度
     */
    public static final int NETDATANORMALLEN = 100;
    /**
     * 数组分隔符
     */
    public static final String NETARRAYSPLITCHAR = "|";
    /**
     * 结束符
     */
    public static final String NETDEFFNSSYMBOL = ")";
    /**
     * 组播命令标志
     */
    public static final String BROADCASTORDFLAG = "broadcast";
    /**
     * 未压缩标志
     */
    public static final String NETUNZIPFLAG = "zip0";
    /**
     * 压缩标志
     */
    public static final String NETZIPFLAG = "zip1";
    /**
     * 隔离返回错误字节数组
     */
    public static final byte[] NETISOLATIONERROR = new byte[] { 0x00 };
    /**
     * 隔离返回错误标志
     */
    public static final String NETISOLATERRFLAG = "0";
    /**
     * 隔离返回成功字节数组
     */
    public static final byte[] NETISOLATIONSUC = new byte[] { (byte) 0xFF };
    /**
     * 隔离返回成功标志
     */
    public static final String NETISOLATSUCFLG = "1";
    /**
     * 消息分隔符(大数据拼装使用)
     */
    public static final String NETDATASPLIT = "$";
    /**
     * 数据替换分隔符
     */
    public static final String NETDATARPLSPLIT = "&";
    /**
     * 消息结束符
     */
    public static final String NETDATAFNSSYMBOL = ")";
    /**
     * soe约定符
     */
    public static final String SOESYMBLE = "soe_";
    /**
     * 告警推送数据分隔符（推送数据拼装使用）
     */
    public static final String PUSHDATASYMBOL = ",";

    /**
     * 拓传异常信息分隔符
     */
    public static final String EXTENERRSPLITER = "@@";

    /**
     * 特殊设备
     */
    private static Hashtable<?, ?> htSpeciaDev = new Hashtable<Object, Object>();

    /**
     * 容器容量上限
     */
    private static int containErMaxSize = 4096;

    /**
     * 数据方向
     *
     * @author 曹阳
     */
    public enum NetDataDirection
    {
        /**
         * 向上
         */
        UP,
        /**
         * 向下
         */
        DOWN,
        /**
         * 无方向
         */
        NODIRECTION,
        /**
         * 未知
         */
        UNKNOWN
    }

    /**
     * 数据类型（数据库生产数据、数据库所有数据、历史瞬态数据）
     *
     * @author wrb
     */
    public enum NetDataClassify
    {
        /**
         * 数据库生产数据
         */
        DBPRODUCT,
        /**
         * 历史瞬态数据
         */
        RT,
        /**
         * 数据库所有数据中除数据库生产数据其余数据（SQL脚本、运行日志、指令日志、五分钟数据）
         */
        UNKNOWN
    }

    /**
     * 向上数据类型
     *
     * @author Administrator
     */
    public enum NetUpDataType
    {
        /**
         * 未知
         */
        Unknown,
        /**
         * 主轮询数据(单包)
         */
        DevMainData,
        /**
         * 主轮询数据(多包)
         */
        DevMainData_Split,
        /**
         * 设备包数据
         */
        DevPackData,
        /**
         * 设备通讯状态
         */
        DevComState,
        /**
         * 历史瞬态数据
         */
        DevRealtimeData,
        /**
         * 一分钟理论数据
         */
        DevOneData,
        /**
         * 一分钟数据
         */
        ONEMINDATA,
        /**
         * 五分钟数据
         */
        FIVEMINDATA,
        /**
         * 十分钟数据
         */
        DevTenData,
        /**
         * 日数据
         */
        DevDayData,
        /**
         * 功率曲线数据
         */
        DevPowerCurve,
        /**
         * 故障数据
         */
        DevFaultData,
        /**
         * 状态数据
         */
        DevStateData,
        /**
         * 警告数据
         */
        DevAlarmData,
        /**
         * 变位数据
         */
        DevChangeSaveData,
        /**
         * 沉积数据
         */
        DevSedimentData,
        /**
         * 指令日志
         */
        OrderLog,
        /**
         * 电量计划
         */
        ElecPlan,
        /**
         * 旧版主轮询数据
         */
        OLDDEVMAINDATA,
        /**
         * 旧版历史瞬态数据
         */
        OLDDEVREALTIMEDATA,
        /**
         * 旧版五分钟数据
         */
        OLDDEVFIVEDATA,
        /**
         * 旧版十分钟数据
         */
        OLDDEVTENDATA,
        /**
         * 旧版日数据
         */
        OLDDEVDAYDATA,
        /**
         * 旧版功率曲线数据
         */
        OLDDEVPOWERCURVE,
        /**
         * 旧版警告数据
         */
        OLDDEVALARMDATA,
        /**
         * 旧版设备通讯状态
         */
        OLDDEVCOMSTATE,
        /**
         * 旧版设备故障数据
         */
        OLDDEVFAULTDATA,
        /**
         * 旧版设备状态数据
         */
        OLDDEVSTATEDATA,
        /**
         * 运行日志
         */
        RunLog,
        /**
         * 系统提示
         */
        SystemPrompt,
        /**
         * 用户数据
         */
        UserData,
        /**
         * 上级用户数据
         */
        UpUserData,
        /**
         * SQL数据
         */
        SqlData,
        /**
         * 上传SQL数据
         */
        UpSqlData,
        /**
         * 20140815 jzy Scada预警状态
         */
        IECscr,
        /**
         * 前置版本
         */
        PreVersion,
        /**
         * 设备版本
         */
        DevVersion,
        /**
         * 新五分钟数据
         */
        NewFiveData,
        /**
         * 状态日志
         */
        MainLog,
        /**
         * 告警日志
         */
        WarnLog,
        /**
         * 告警日志结束
         */
        WarnEnd,
        /**
         * 用户行为数据
         */
        UserbehaviorData,
        /**
         * 事件数据
         */
        EventData,
        /**
         * 上送生产数据
         */
        UploadProductData,
        /**
         * 上送缓存数据文件的数据
         */
        UploadServerData,
        /**
         * 消息订阅的数据
         */
        MsgSubscribeData,
        /**
         * Json数据
         */
        Json,
        /**
         * 告警服务组播的服务器状态包
         */
        ServerState,
        /**
         * 102协议数据
         */
        TimeData,
        /**
         * 报信主站上送数据
         */
        ProtectData,
        /**
         * 保信主站召唤日志
         */
        ProtectCallLog,
        /**
         * 报信主站控制日志
         */
        ProtectSetLog,
        /**
         * 报信主站流类型数据
         */
        ProtectStream,
        /**
         * 报信主站字符串
         */
        ProtectString,
        /**
         * 控制返回结果
         */
        ControlResult,
        /**
         * 温度功率曲线数据
         */
        EnvTemperatureCurve,
        /**
         * gps数据
         */
        GPSData,
        /**
         * 监控数据
         */
        MonData,
        /**
         * go+上送用户数据
         */
        GoUserData,
        /**
         * 新运行日志数据,是用warnlog转换而来的
         */
        NewRunLog,
        /**
         * 虚拟遥信量数据
         */
        VirtualTelemetry,
        /**
         * DevControlState(三版移植)
         */
        DevControlState,
        /**
         * XllglData(三版移植)
         */
        XllglData,

        /**
         * 网络设备监测数据
         */
        MONITORJSON
    }

    /**
     * 向下数据类型
     *
     * @author Administrator
     */
    public enum NetDownDataType
    {
        /**
         * 未知
         */
        Unknown,
        /**
         * 向前置要主轮询数据
         */
        GetDevMainData,
        /**
         * 向前置要包数据
         */
        GetDevPackData,
        /**
         * 向前置要数据
         */
        GetDevData,
        /**
         * 向前置要内存数据
         */
        GetDevCacheData,
        /**
         * 得到数据订单编号
         */
        GetPlanOrder,
        /**
         * 得到订单数据(单台风机)
         */
        GetPlanData,
        /**
         * 得到订单数据(多台风机)
         */
        GetPlanDataNoId,
        /**
         * 设备(风机等)控制指令
         */
        DevControlOrder,
        /**
         * 设备(风机等)带参数控制指令
         */
        DevOtherControlOrder,
        /**
         * 更改参数
         */
        SetDevDataValues,
        /**
         * 其它更改参数
         */
        SetDevOtherDataValues,
        /**
         * 设置设备时间
         */
        SetDevTime,
        /**
         * 设置某个前置管理的所有设备的时间
         */
        SetPreAllDevTime,
        /**
         * 更改前置数据库配置
         */
        WirtePreConfig,
        /**
         * 更改前置文件配置
         */
        WritePreIni,
        /**
         * 读取plc中的文件名称
         */
        GetPlcFileName,
        /**
         * 读取plc文件
         */
        GetPlcFileData,
        /**
         * 重启前置
         */
        RestartPre,
        /**
         * 读取前置升级文件路径
         */
        GetPreUpgradeDir,
        /**
         * 读取前置文件配置
         */
        ReadPreIni,
        /**
         * 读取前置文件配置
         */
        GetPreIni,
        /**
         * 要求前置拷贝程序文件
         */
        PreFileCopy,
        /**
         * 要求前置更新配置库文件
         */
        PreAccessUpdate,
        /**
         * 读取前置存储文件
         */
        PreRptDataRead,
        /**
         * 删除前置存储文件
         */
        PreRptDataDel,
        /**
         * 读取前置存储文件行数
         */
        PreRptDataReadCount,
        /**
         * 读取前置实时数据
         */
        PreRealtimeDataRead,
        /**
         * 删除前置实时数据
         */
        PreRealtimeDataDel,
        /**
         * 读取前置实时数据行数
         */
        PreRealtimeDataReadCount,
        /**
         * 读取前置磁盘空间
         */
        GetPreDiskSpace,
        /**
         * 读取前置cpu信息
         */
        GetPreCpu,
        /**
         * 读取前置内存信息
         */
        GetPreMemory,
        /**
         * 读取前置版本号
         */
        GetPreVersion,
        /**
         * 读取前置时间
         */
        GetPreTime,
        /**
         * 向设备发送一般性指令
         */
        DevCommonOrder,
        /**
         * 向前置发送一般性指令
         */
        PreCommonOrder,
        /**
         * 服务管理
         */
        ServiceManage,
        /**
         * 服务通讯测试
         */
        SrvComTest,
        /**
         * 前置通讯测试
         */
        PreComTest,
        /**
         * 组播指令
         */
        BroadcastOrder,
        /**
         * 更改多个参数
         */
        SetDevMultDataValues,
        /**
         * 保信主站召唤指令
         */
        GetProtectData,
        /**
         * 保信主站控制命令
         */
        SetProtectData
    }

    /**
     * 无方向数据类型
     *
     * @author Administrator
     */
    public enum NetNoDirDataType
    {
        /**
         * 位置
         */
        Unknown,
        /**
         * 前置初始启动
         */
        PreStart,
        /**
         * 服务历史数据通讯状态
         */
        SrvHisDataComState,
        /**
         * 向数据服务要系统时间
         */
        GetSysDateTime,
        /**
         * 向数据服务要前置数据库配置
         */
        GetPreConfig,
        /**
         * 增加任务
         */
        AddSrvTask,
        /**
         * 删除任务
         */
        DelSrvTask,
        /**
         * 停止任务
         */
        StopSrvTask,
        /**
         * 大数据
         */
        LargeData,
        /**
         * 服务器信息
         */
        ServerInfo,
        /**
         * 服务器内存数据数量
         */
        GetSrvMemDataNum,
        /**
         * 旧版通讯测试
         */
        OldSrvComTest,
        /**
         * 发送文件
         */
        SendFile,
        /**
         * 网络测试
         */
        NetTest,
        /**
         * 向数据服务要协议版本号
         */
        GetProCondition,
        /**
         * 向数据服务要协议配置
         */
        GetProConfig,
        /**
         * 服务器通讯数据数量
         */
        GetSrvCommDataNum,
        /**
         * 服务器维护信息
         */
        GetSrvMaintainInfo,
        /**
         * 软适配信息
         */
        GetSoftAdapterInfo,
        /**
         * 获取数据处理文件服务信息
         */
        GetDspsrvData,
        /**
         * 获取数据处理文件服务信息（单台）
         */
        GetDspsrvDataSingle,
        /**
         * 获取数据处理文件服务虚拟测点信息
         */
        GetDspsrvVirtualData,
        /**
         * 获取气象服务信息
         */
        GetWeatherData,
        /**
         * 获取台风信息
         */
        GetTyphoonData,
        /**
         * 手动设置台风级别
         */
        SetTyphoonLevel,
        /**
         * 获取BF文件信息
         */
        GetBfFileData,
        /**
         * 获取内存数据版本
         */
        GetMemoryVersion,
        /**
         * 获取队列大小
         */
        GetQueueSize,
        /**
         * 停止服务
         */
        Stop,
        /**
         * EBA 可排除项
         */
        Excludcnf,
        /**
         * 获取配置数据版本
         */
        GetConfigVersion,
        /**
         * 获取配置数据
         */
        GetConfigData,
        /**
         * 上传配置文件
         */
        UploadConfigData,
        /**
         * 下载波形文件
         */
        DownloadWaveFile,
        /**
         * 需要下载的文件
         */
        DownloadFile,
        /**
         * 通用需要保存的文件
         */
        FileSave,
        /**
         * 保信主站文件保存
         */
        ProtectStream,

        /**
         * 保信主站字符串召唤
         */

        ProtectString,
        /**
         * 等待数据
         */
        Wait,
        /**
         * 结束数据
         */
        Finish,
        /**
         * 通讯成功标志
         */
        OK,
        /**
         * 通讯测试标识
         */
        Test,
        /**
         * 通讯异常标志
         */
        Error,
        /**
         * 通讯繁忙标志
         */
        Busy,
        /**
         * 上送文件
         */
        UploadFile,
        /**
         * 测试端口是否通讯正常
         */
        ComtestQuery,
        /**
         * 修改Db信息
         */
        ModifyDbInfo,
        /**
         * 获取录入功率曲线数据
         */
        GETGUARPOW,
        /**
         * 获取故障录波文件列表
         */
        GETGZLBLIST,
        /**
         * 获取故障录波文件
         */
        GETGZLB

    }

    /**
     * POST 请求类型
     *
     * @author 谭璟
     */
    public enum MethodType
    {
        /**
         * POST 请求
         */
        POST("POST"),

        /**
         * GET 请求
         */
        GET("GET");

        /**
         * 变量
         */
        private String result;

        MethodType(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 服务运行状态
     *
     * @author Administrator
     */
    public enum ServiceRunState
    {
        /**
         * 未知,服务中不能设定为此状态
         */
        UNKNOWN(0),
        /**
         * 运行
         */
        RUNNING(1),
        /**
         * 正在停止
         */
        STOPPING(2),
        /**
         * 停止
         */
        STOPPED(3),
        /**
         * 正在启动
         */
        STARTING(4),
        /**
         * 繁忙
         */
        Busy(5);
        /**
         * 枚举值
         */
        private int value;

        ServiceRunState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * 系统繁忙类型
     *
     * @author Administrator
     */
    public enum SystemBusyType
    {
        /**
         * 通讯
         */
        COMM
    }

    /**
     * 服务管理类型
     *
     * @author Administrator
     */
    public enum ServiceManageType
    {
        /**
         * 重启服务
         */
        RESTARTSERVICE(1),
        /**
         * 下载配置并重启服务
         */
        DOWNCONFIGANDRESTARTSERVICE(3);

        /**
         * 枚举值
         */
        private int value;

        ServiceManageType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * 任务周期类型
     *
     * @author Administrator
     */
    public enum TaskCycType
    {
        /**
         * 一次
         */
        OnlyOnce,
        /**
         * 每日
         */
        PerDay,
        /**
         * 每工作日
         */
        PerWorkDay,
        /**
         * 每周
         */
        PerWeek,
        /**
         * 每月
         */
        PerMonth,
        /**
         * 每年
         */
        PerYear
    }

    /**
     * 通讯状态
     *
     * @author Administrator
     */
    public enum CommState
    {
        /**
         * 通
         */
        Connect(0),
        /**
         * PLC中断
         */
        Disconnect(1),
        /**
         * ping不通
         */
        PingError(2),
        /**
         * 端口没有服务
         */
        NoService(3),
        /**
         * 读取数据错误
         */
        ReadError(4),
        /**
         * 返回数据错误
         */
        ReturnError(5),

        /**
         * ADS错误
         */
        AdsError(6),

        /**
         * Comm通讯错误
         */
        ComError(7),

        /**
         * 远动中断
         */
        RemoteDisconnect(8),

        /**
         * 未知
         */
        Unknown(100);

        /**
         *
         */
        private final int value;

        CommState(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    /**
     * plc文件类型
     *
     * @author Administrator
     */
    public enum PlcFileType
    {

        /**
         * 所有文件
         */
        All(0),
        /**
         * B文件
         */
        B(1),
        /**
         * txt格式的F文件
         */
        Ftxt(2),
        /**
         * html格式的F文件
         */
        Fhtml(3),
        /**
         * M文件
         */
        M(4),
        /**
         * A文件
         */
        Action(5),
        /**
         * O文件
         */
        Operation(6),
        /**
         * Hfb文件
         */
        Hfb(7),
        /**
         * W文件
         */
        W(8),
        /**
         * E文件
         */
        E(9),
        /**
         * C文件
         */
        C(10),
        /**
         * Fc文件
         */
        Fc(11),
        /**
         * T文件
         */
        T(12),
        /**
         * S文件
         */
        S(13),
        /**
         * 未知文件
         */
        Unknown(1000);

        /**
         * 枚举值
         */
        private int value = 0;

        PlcFileType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * 设备类型
     */
    public enum WfDeviceType
    {
        /**
         * 风机
         */
        WindTurbine(0),
        /**
         * 变电站
         */
        TransSubstation(1),
        /**
         * 测风塔
         */
        WindTower(2),
        /**
         * 箱变
         */
        BoxTransformer(3),
        /**
         * 逆变器
         */
        Inverter(4),
        /**
         * 测光塔
         */
        LightTower(5),
        /**
         * 汇流箱
         */
        ConfluxBox(6),
        /**
         * 电池组
         */
        BatteryPack(7),
        /**
         * 电缆分支箱
         */
        CableBranchBox(8),
        /**
         * 能量管理器
         */
        EnergyManager(9),
        /**
         * 能量分配器
         */
        EnergyDistributor(10),
        /**
         * 电能表
         */
        EnergyMeter(11),
        /**
         * UPS
         */
        UPS(12),
        /**
         * 第三方功率预测
         */
        PowerForecast(13),
        /**
         * 方阵
         */
        Cells(15),
        /**
         * 能效
         */
        EnergyEfficiency(16),
        /**
         * EMS风机
         */
        EMSWindTurbine(17),
        /**
         * 消防设备
         */
        Fire_fightingEquipment(19),
        /**
         * 录波设备
         */
        WaveRecording(20),
        /**
         * 保护设备
         */
        Protective(21),
        /**
         * 数采设备
         */
        DataAcquisition(22),
        /**
         * 保信子站
         */
        BaoxinSubstation(23),

        /**
         * 能量分配子设备
         */
        EnergyDistributionSubequipment(24),

        /**
         * 全站设备
         */
        TotalStationEquipment(26),

        /**
         * 双向变流器
         */
        PCS(27),

        /**
         * 电池管理系统
         */
        BMS(28),

        /**
         * 电池
         */
        Battery(29),

        /**
         * 空调
         */
        AirConditioning(30),
        /**
         * PFR--一次快速调频设备
         */
        PrimaryFrequencyRegulat(32),
        /**
         * 电容
         */
        Capacitor(101),
        /**
         * 燃气机
         */
        Turbine(102),
        /**
         * AVC
         */
        AVC(103),
        /**
         * AGC
         */
        AGC(104),
        /**
         * 电表
         */
        ElectricMeter(901),
        /**
         * 水表
         */
        WaterMeter(902),
        /**
         * 蒸汽表
         */
        SteamMeter(903),
        /**
         * 天然气表
         */
        NaturalgasMeter(904),
        /**
         * 热量表
         */
        HeatMeter(905),
        /**
         * 其它设备
         */
        OtherDevice(1000),
        /**
         * 所有
         */
        All(100000);

        /**
         * 通过值获取枚举类型
         *
         * @param value
         *            值
         * @return 枚举类型
         */
        public static WfDeviceType valueOf(int value)
        {
            WfDeviceType tmp = WfDeviceType.OtherDevice;
            for (WfDeviceType wfd : WfDeviceType.values())
            {
                if (wfd.getResult() == value)
                {
                    tmp = wfd;
                }
            }
            return tmp;
        }

        /**
         * 类型值
         */
        private int result;

        WfDeviceType(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }

        /**
         * 返回对象
         * 
         * @param temp
         * @return
         */
        public static WfDeviceType findType(Integer temp)
        {
            for (WfDeviceType wfd : WfDeviceType.values())
            {
                if (wfd.getResult() == temp)
                {
                    return wfd;
                }
            }
            return null;
        }
    }

    /**
     * 操作类型
     *
     * @author Administrator
     */
    public enum TableDataOperType
    {
        /**
         *
         */
        Ins, // 插入
        /**
         *
         */
        Upd, // 修改
        /**
         *
         */
        Del // 删除
    }

    /**
     * 数据类型
     *
     * @author Administrator
     */
    public enum UserDataType
    {
        /**
         * 整型
         */
        DbInt,
        /**
         * 实型
         */
        DbDecimal,
        /**
         * 日期
         */
        DbDate,
        /**
         * 时间
         */
        DbTime,
        /**
         * 日期时间
         */
        DbDateTime,
        /**
         * 字符串
         */
        DbString
    }

    /**
     * 时间类型分类
     *
     * @author 谭璟
     */
    public enum TimeType
    {
        /**
         * 月初
         */
        monthin("0"),

        /**
         * 月末
         */
        monthend("1");

        /**
         * 变量
         */
        private String result;

        TimeType(String result)
        {
            this.result = result;
        }

        public String getResut()
        {
            return this.result;
        }
    }

    /**
     * 时间类型枚举
     *
     * @author 谭璟
     */
    public enum DayTimeFlag
    {
        /**
         * 日
         */
        day(0),

        /**
         * 月
         */
        month(1),

        /**
         * 季度
         */
        quarter(2),

        /**
         * 年
         */
        year(3);

        /**
         * 结果
         */
        private int result;

        DayTimeFlag(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 程序类型
     */
    public enum ProgramType
    {
        /**
         * 未知类型
         */
        UNKNOW(0, "未知类型", "UnKnow"),
        /**
         * 前置
         */
        DAFROUNT(1, "前置", "DaFrount"),
        /**
         * 数据处理
         */
        DATAPROCESSSERVICE(2, "数据处理", "DataProcessService"),
        /**
         * 能量管理
         */
        EMS(3, "能量管理", "EMS");
        /**
         * 参数
         */
        private int result;
        /**
         * 中文名称
         */
        private String cnName;
        /**
         * 英文名称
         */
        private String enName;

        ProgramType(int result, String cnName, String enName)
        {
            this.result = result;
            this.cnName = cnName;
            this.enName = enName;
        }

        public int getResult()
        {
            return this.result;
        }

        public String getCnName()
        {
            return this.cnName;
        }

        public String getEnName()
        {
            return this.enName;
        }

        /**
         * 根据语言环境获取到
         *
         * @return
         */
        public String getName()
        {
            Locale local = Locale.getDefault();
            if ("en".equalsIgnoreCase(local.getLanguage()))
            {
                return this.enName;
            }
            else
            {
                return this.cnName;
            }
        }
    }

    /**
     * 数据类型
     */
    public enum DataType
    {
        /**
         * 数据
         */
        DATA(0, "所有", "All"),
        /**
         * 生产数据
         */
        PRODUCTDATA(1, "生产", "ProDuct"),
        /**
         * 告警数据
         */
        WARNLOG(2, "告警", "Warnlog");

        /**
         * 参数
         */
        private int result;
        /**
         * 中文名称
         */
        private String cnName;
        /**
         * 英文名称
         */
        private String enName;

        DataType(int result, String cnName, String enName)
        {
            this.result = result;
            this.cnName = cnName;
            this.enName = enName;
        }

        public int getResult()
        {
            return this.result;
        }

        public String getCnName()
        {
            return this.cnName;
        }

        public String getEnName()
        {
            return this.enName;
        }

        /**
         * 根据语言环境获取到
         *
         * @return
         */
        public String getName()
        {
            Locale local = Locale.getDefault();
            if ("en".equalsIgnoreCase(local.getLanguage()))
            {
                return this.enName;
            }
            else
            {
                return this.cnName;
            }
        }
    }

    /**
     * 数据队列类型
     */
    public enum QueueType
    {
        /**
         * 数据转发队列
         */
        TRANSQUEUE(0, "数据转发队列", "Trans Queue"),
        /**
         * 数据库保存队列
         */
        HISDATAQUEUE(1, "数据库保存队列", "History Data Queue"),
        /**
         * 历史瞬态数据队列
         */
        REALDATATOFILEQUEUE(2, "历史瞬态数据保存队列", "Realtime Data To File Queue"),
        /**
         * 实时库保存队列
         */
        RTDATAQUEUE(3, "实时库保存队列", "RT Data Queue"),
        /**
         * udp队列
         */
        UDPDATAQUEUE(4, "udp处理队列", "UDP Data Queue"),
        /**
         * 告警队列
         */
        WARNQUEUE(5, "告警队列", "Warn Data Queue"),
        /**
         * kafka队列
         */
        TRANSTOKAFKAQUEUE(6, "转发至kafka队列", "Trans Data To Kafka Queue"),
        /**
         * gis服务队列
         */
        GISDATAQUEUE(7, "GIS服务数据队列", "GIS Data Queue"),
        /**
         * 缓冲队列
         */
        BUFFERDATAQUEUE(8, "缓冲数据队列", "Buffer Data Queue"),
        /**
         * 消息订阅队列
         */
        MSGSUBSCRIBEQUEUE(9, "消息订阅数据队列", "Message Subscribe Data Queue");

        /**
         * 参数
         */
        private int result;
        /**
         * 中文名称
         */
        private String cnName;
        /**
         * 英文名称
         */
        private String enName;

        QueueType(int result, String cnName, String enName)
        {
            this.result = result;
            this.cnName = cnName;
            this.enName = enName;
        }

        public int getResult()
        {
            return this.result;
        }

        public String getCnName()
        {
            return this.cnName;
        }

        public String getEnName()
        {
            return this.enName;
        }

        /**
         * 根据语言环境获取到
         *
         * @return
         */
        public String getName()
        {
            Locale local = Locale.getDefault();
            if ("en".equalsIgnoreCase(local.getLanguage()))
            {
                return this.enName;
            }
            else
            {
                return this.cnName;
            }
        }
    }

    public static int getTCPSENDBUFFERSIZE()
    {
        return tcpSendBufferSize;
    }

    public static void setTCPSENDBUFFERSIZE(int tCPSENDBUFFERSIZE)
    {
        tcpSendBufferSize = tCPSENDBUFFERSIZE;
    }

    public static int getTCPRECBUFFERSIZE()
    {
        return tcpRecBufferSize;
    }

    public static void setTCPRECBUFFERSIZE(int tCPRECBUFFERSIZE)
    {
        tcpRecBufferSize = tCPRECBUFFERSIZE;
    }

    public static int getUDPSENDBUFFERSIZE()
    {
        return udpSendBufferSize;
    }

    public static void setUDPSENDBUFFERSIZE(int uDPSENDBUFFERSIZE)
    {
        udpSendBufferSize = uDPSENDBUFFERSIZE;
    }

    public static int getUDPRECBUFFERSIZE()
    {
        return udpRecBufferSize;
    }

    public static void setUDPRECBUFFERSIZE(int uDPRECBUFFERSIZE)
    {
        udpRecBufferSize = uDPRECBUFFERSIZE;
    }

    public static int getTCPRECMAXTHREAD()
    {
        return tcpRecMaxThread;
    }

    public static void setTCPRECMAXTHREAD(int tCPRECMAXTHREADNUM)
    {
        tcpRecMaxThread = tCPRECMAXTHREADNUM;
    }

    public static int getCOMMSENDTIMEOUT()
    {
        return commSendTimeOut;
    }

    public static void setCOMMSENDTIMEOUT(int cOMMSENDTIMEOUT)
    {
        commSendTimeOut = cOMMSENDTIMEOUT;
    }

    public static int getCOMMRECTIMEOUT()
    {
        return commRecTimeOut;
    }

    public static void setCOMMRECTIMEOUT(int cOMMRECTIMEOUT)
    {
        commRecTimeOut = cOMMRECTIMEOUT;
    }

    public static int getCOMMLISRECTIMEOUT()
    {
        return commLisRecTimeOut;
    }

    public static void setCOMMLISRECTIMEOUT(int cOMMLISTENRECTIMEOUT)
    {
        commLisRecTimeOut = cOMMLISTENRECTIMEOUT;
    }

    public static int getCOMMCTUWTTIMEOUT()
    {
        return commCtuwtTimeOut;
    }

    public static void setCOMMCTUWTTIMEOUT(int cOMMCTUWTTIMEOUT)
    {
        commCtuwtTimeOut = cOMMCTUWTTIMEOUT;
    }

    public static int getKEEPALIVETIME()
    {
        return keepAliveTime;
    }

    public static void setKEEPALIVETIME(int kEEPALIVETIME)
    {
        keepAliveTime = kEEPALIVETIME;
    }

    public static int getKEEPALIVEINTERVAL()
    {
        return keepAliveInterval;
    }

    public static void setKEEPALIVEINTERVAL(int kEEPALIVEINTERVAL)
    {
        keepAliveInterval = kEEPALIVEINTERVAL;
    }

    public static int getPINGTIMEOUT()
    {
        return pingTimeOut;
    }

    public static void setPINGTIMEOUT(int pINGTIMEOUT)
    {
        pingTimeOut = pINGTIMEOUT;
    }

    public static int getCOMMWAITTESTPER()
    {
        return commWaitTestPer;
    }

    public static void setCOMMWAITTESTPER(int cOMMWAITTESTPERIOD)
    {
        commWaitTestPer = cOMMWAITTESTPERIOD;
    }

    public static int getCOMMWAITESENDPER()
    {
        return commWaiteSendPer;
    }

    public static void setCOMMWAITESENDPER(int cOMMWAITESENDPERIOD)
    {
        commWaiteSendPer = cOMMWAITESENDPERIOD;
    }

    public static int getCOMMWAITTIMEOUT()
    {
        return commWaitTimeOut;
    }

    public static void setCOMMWAITTIMEOUT(int cOMMWAITTIMEOUT)
    {
        commWaitTimeOut = cOMMWAITTIMEOUT;
    }

    public static int getCOMMLISTENEXPPER()
    {
        return commListeneXpper;
    }

    public static void setCOMMLISTENEXPPER(int cOMMLISTENEXPPERIOD)
    {
        commListeneXpper = cOMMLISTENEXPPERIOD;
    }

    public static int getCOMMEXPRESETITV()
    {
        return commExpresetitv;
    }

    public static void setCOMMEXPRESETITV(int cOMMEXPRESETINTERVAL)
    {
        commExpresetitv = cOMMEXPRESETINTERVAL;
    }

    public static boolean isSENDTOISOLATION()
    {
        return sendToIsolation;
    }

    public static void setSENDTOISOLATION(boolean sENDTOISOLATION)
    {
        sendToIsolation = sENDTOISOLATION;
    }

    public static boolean isRECFROMISOLATION()
    {
        return recFromIsolation;
    }

    public static void setRECFROMISOLATION(boolean rECFROMISOLATION)
    {
        recFromIsolation = rECFROMISOLATION;
    }

    public static int[] getRECDIFFISOLATTYPT()
    {
        return recDiffIsolatTypt;
    }

    public static void setRECDIFFISOLATTYPT(int[] rECDIFFISOLATTYPT)
    {
        recDiffIsolatTypt = rECDIFFISOLATTYPT;
    }

    public static boolean isCOMMUSENEWCOMP()
    {
        return commusenewComp;
    }

    public static void setCOMMUSENEWCOMP(boolean cOMMUSENEWCOMPRESS)
    {
        commusenewComp = cOMMUSENEWCOMPRESS;
    }

    public static Hashtable<?, ?> getHTSPECIADEV()
    {
        return htSpeciaDev;
    }

    public static void setHTSPECIADEV(Hashtable<?, ?> hTSPECIADEV)
    {
        htSpeciaDev = hTSPECIADEV;
    }

    public static int getGETREALTIMETIMEOUT()
    {
        return getRealtimeTimeOut;
    }

    /**
     * @return the cONTAINERMAXSIZE
     */
    public static int getCONTAINERMAXSIZE()
    {
        return containErMaxSize;
    }

    /**
     * @param cONTAINERMAXSIZE
     *            the cONTAINERMAXSIZE to set
     */
    public static void setCONTAINERMAXSIZE(int cONTAINERMAXSIZE)
    {
        containErMaxSize = cONTAINERMAXSIZE;
    }

    public static String getExtenerrspliter()
    {
        return EXTENERRSPLITER;
    }
}
