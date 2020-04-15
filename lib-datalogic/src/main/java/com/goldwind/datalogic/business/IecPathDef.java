package com.goldwind.datalogic.business;

/**
 * 常用IEC路径定义
 *
 * @author Administrator
 */
public class IecPathDef
{
    /**
     * 风机总发电量路径
     */
    public static final String ELECNUM = "WTUR.TotEgyAt.Wt.F32";
    /**
     * 风机日发电量路径——冯春源（风机发电量修正用）
     */
    public static final String DAYELECNUM = "WTUR.Other.Ra.F32.day";
    /**
     * 光伏逆变器发电量路径
     */
    public static final String PVELECNUM = "SPIN.TotEgyAt.Wt.F32";
    /**
     * 光伏逆变器日发电量路径——冯春源（逆变器发电量修正用）
     */
    public static final String NBQELECNUM = "SPIN.Other.Ra.F32.day";
    /**
     * 发电时间路径
     */
    public static final String ELECTIME = "WTUR.ElecTm.Wt.F32";
    /**
     * 历史瞬态数据时间iec路径
     */
    public static final String REALDATATIME = "WTUR.Tm.Rw.Dt";
    /**
     * 旧版历史瞬态数据时间iec路径
     */
    public static final String OLDREALDATATIME = "WMAN.Tm";
    /**
     * 警告路径
     */
    public static final String DEVALARM = "WTUR.Alam.Rs.S";
    /**
     * 故障路径
     */
    public static final String DEVERROR = "WTUR.Flt.Rs.S";
    /**
     * 状态路径
     */
    public static final String DEVSTATUS = "WTUR.TurSt.Rs.S";
    /**
     * 主故障路径
     */
    public static final String DEVMAINERROR = "WTUR.Flt.Ri.I32.main";
    /**
     * 定期维护时间
     */
    public static final String DEVMAINTAINTIME = "WTUR.MtnTm.Wt.F32";
    /**
     * 定期维护时间开始值
     */
    public static final String DEVMAINTAINBEGIN = "WTUR.MtnTm.Wt.F32[min]";
    /**
     * 定期维护时间结束值
     */
    public static final String DEVMAINTAINEND = "WTUR.MtnTm.Wt.F32[max]";
    /**
     * 故障时间
     */
    public static final String DEVFAULTTIME = "WTUR.FaltTm.Wt.F32";
    /**
     * 故障时间开始值
     */
    public static final String DEVFAULTBEGIN = "WTUR.FaltTm.Wt.F32[min]";
    /**
     * 故障时间结束值
     */
    public static final String DEVFAULTEND = "WTUR.FaltTm.Wt.F32[max]";
    /**
     * 系统OK时间
     */
    public static final String DEVSYSOKTIME = "WTUR.OkTm.Wt.F32";
    /**
     * 系统OK时间开始值
     */
    public static final String DEVSYSOKBEGIN = "WTUR.OkTm.Wt.F32[min]";
    /**
     * 系统OK时间结束值
     */
    public static final String DEVSYSOKEND = "WTUR.OkTm.Wt.F32[max]";
    /**
     * 外部OK时间
     */
    public static final String DEVEXTOKTIME = "WTUR.OutOkTm.Wt.F32";
    /**
     * 外部OK时间开始值
     */
    public static final String DEVEXTOKBEGIN = "WTUR.OutOkTm.Wt.F32[min]";
    /**
     * 外部OK时间结束值
     */
    public static final String DEVEXTOKEND = "WTUR.OutOkTm.Wt.F32[max]";
    /**
     * 手动停机时间
     */
    public static final String MANUALSTOP = "WTUR.FaltTm.Wt.F32.Manualstop";
    /**
     * 手动停机时间开始值
     */
    public static final String MANUALSTOPTIMEBEGIN = "WTUR.FaltTm.Wt.F32.Manualstop[min]";
    /**
     * 手动停机时间结束值
     */
    public static final String MANUALSTOPTIMEEND = "WTUR.FaltTm.Wt.F32.Manualstop[max]";
    /**
     * 外部故障时间-周中杰
     */
    public static final String ENVIRONMENTNOTOK = "WTUR.CTim.Rt.F32.environmentnotok";
    /**
     * 外部故障时间开始值
     */
    public static final String ENVIRONMENTNOTOKBEGIN = "WTUR.CTim.Rt.F32.environmentnotok[min]";
    /**
     * 外部故障时间结束值
     */
    public static final String ENVIRONMENTNOTOKEND = "WTUR.CTim.Rt.F32.environmentnotok[max]";

    /**
     * 风机历史风速iec路径
     */
    public static final String TURBHISWINDSPEED = "WTUR.WSpd.Ra.F32[AVG]";
    /**
     * 风机历史风向iec路径
     */
    public static final String TRUBHISWINDVANE = "WTUR.Wdir.Ra.F32[AVG]";
    /**
     * 风机历史风向iec路径(平均绝对风向)
     */
    public static final String TRUBHISWINDVANEABS = "WTUR.Wdir.Ra.F32.abs[AVG]";
    /**
     * 风机历史风功率iec路径
     */
    public static final String TRUBHISWINDPOWER = "WTUR.PwrAt.Ra.F32[AVG]";
    /**
     * 测风塔历史风速iec路径
     */
    public static final String TOWERHISWINDSPEED = "METT.WSpd.Ra.F32[AVG]";
    /**
     * 测风塔历史风速iec路径(轮毂高度)
     */
    public static final String TOWERHISWINDSPEEDHUB = "METT.WSpd.Ra.F32.Hub[AVG]";
    /**
     * 测风塔历史风向iec路径
     */
    public static final String TOWERHISWINDVANE = "METT.Wdir.Ra.F32[AVG]";
    /**
     * 测风塔历史风向iec路径(轮毂高度)
     */
    public static final String TOWERHISWINDVANEHUB = "METT.Wdir.Ra.F32.Hub[AVG]";
    /**
     * 测风塔历史风功率iec路径
     */
    public static final String TOWERHISWINDPOWER = "METT.PwrAt.Ra.F32[AVG]";
    /**
     * agc投切状态
     */
    public static final String AGCSTATE = "EMSD.Bool.Rd.b0.AGCState";
    /**
     * 有功上网功率（升压站）
     */
    public static final String MHACTIVEPOWER = "MH.PwrAt.Ra.F32";
    /**
     * 无功上网功率（升压站）
     */
    public static final String MHREACTIVEPOWER = "MH.PwrReact.Ra.F32";
    /**
     * 有功计划值（功率控制）
     */
    public static final String PLANNEDVALUE = "EMSD.PwrAt.Ra.F32.PlannedValue";

    /**
     * 停机模式字
     */
    public static final String STOPMODEWORD = "WTUR.Other.Wn.I16.StopModeWord";
    /**
     * 限功率模式字
     */
    public static final String LITPOWBYPLC = "WTUR.Other.Rs.S.LitPowByPLC";

    /**
     * 风机状态说明
     */
    public static final String WINDTURBINESTATE = "WTUR.TurSt.Rs.S";

    /**
     * 上网电量
     */
    public static final String EXPORTELEC = "MH.TotEgyAt.Wt.F32.OZ";

    /**
     * 购网电量
     */
    public static final String PURCHASEELEC = "MH.TotEgyAt.Wt.F32.IZ";

    /**
     * 24小时内允许远程复位次数
     */
    public static final String SCARECONALL24 = "WTUR.Flt.Rs.S.scareconall24";

    /**
     * 24小时内远程复位次数
     */
    public static final String SCARECON24 = "WTUR.Flt.Rs.S.scarecon24";
    /**
     * 协议检查
     */
    public static final String IECPATHCRC = "WTUR.Other.Rs.S.iecpathcrc";

    /**
     * 叶片1角度
     */
    public static final String BLADE1 = "WTPS.Ang.Ra.F32.blade1";

    /**
     * 叶片2角度
     */
    public static final String BLADE2 = "WTPS.Ang.Ra.F32.blade2";

    /**
     * 叶片3角度
     */
    public static final String BLADE3 = "WTPS.Ang.Ra.F32.blade3";

    /**
     * 变桨电容1高电压
     */
    public static final String HPITCH1 = "WTPS.CV.Ra.F32.Hpitch1";

    /**
     * 变桨电容2高电压
     */
    public static final String HPITCH2 = "WTPS.CV.Ra.F32.Hpitch2";

    /**
     * 变桨电容3高电压
     */
    public static final String HPITCH3 = "WTPS.CV.Ra.F32.Hpitch3";

    /**
     * 变桨1电容电压
     */
    public static final String PITCH1 = "WTPS.CV.Ra.F32.pitch1";

    /**
     * 变桨2电容电压
     */
    public static final String PITCH2 = "WTPS.CV.Ra.F32.pitch2";

    /**
     * 变桨3电容电压
     */
    public static final String PITCH3 = "WTPS.CV.Ra.F32.pitch3";

    /**
     * 发电机转速
     */
    public static final String WGENSPD = "WGEN.Spd.Ra.F32";

    /**
     * 设备事件IEC量
     */
    public static final String DEVEVT = "WTUR.Evt.Rs.S";

    /**
     * 机组停机状态字类型
     */
    public static final String AVAILABLEFACTYPE = "WTUR.Other.Rn.U16.AvailablefacType";

    /**
     * 测风塔标准IEC量
     */
    public static final String ANTOWER = "METT.Pres.Ra.F32";

    /**
     * 初始化文件版本号
     */
    public static final String INITFILEINFO = "WTUR.Other.Rg.S.InitFileInfo";
    /**
     * 变流程序版本号（2019.10.29wrb修改--魏侯燕确认，之前为WTUR.Other.Rg.S.ConverVer）
     */
    public static final String CONVERVER = "WCNV.Other.Rg.S.ConverVer";
    /**
     * 风机程序版本号
     */
    public static final String PLCVERSION = "WTUR.Other.Rg.S.Plcversion";
    /**
     * 1#变桨软件版本号
     */
    public static final String PITCHVERSION1 = "WTPS.Other.Rg.F32.PitchVersion1";

    /************************************* 能量控制iec量--czr *********************************************/
    /**
     * 控制点有功功率
     */
    public static final String PWRATCONTROL = "EMSD.PwrAt.Ra.F32.Control";

    /**
     * 电网有功计划值
     */
    public static final String PWRATPLANVALTIMEGIRD = "EMSD.PwrAt.Ra.F32.PlanValTimegird";

    /**
     * 全场装机容量
     */
    public static final String EMSDMW = "EMSD.Other.Ra.F32.MW";

    /**
     * 与调度通信状态
     */
    public static final String EMSDGIRDCOMMSTATE = "EMSD.Bool.Rd.b0.GirdcommState";

    /**************************************************************************************************/

    /**
     * 功率预测IEC量 （长趋势）
     */
    public static final String POWERFALSELONG = "Program.PowerForeLong.Rt.F32";

    /**
     * 风速预测IEC量 （长趋势）
     */
    public static final String WINDFALSE = "Program.WindSpeedFore.Rt.F32";

    /**
     * 功率预测IEC量 （短趋势）
     */
    public static final String POWERFALSESHORT = "Program.PowerForeShort.Rt.F32";

    /**
     * 环境温度
     */
    public static final String ENVIRTEMP = "WTUR.Temp.Ra.F32";

    /**
     * 风速
     */
    public static final String WINDSPEED = "WTUR.WSpd.Ra.F32";

    /**
     * 变流器有功功率
     */
    public static final String WINDPOWER = "WTUR.PwrAt.Ra.F32";

    /**
     * 理论有功功率
     */
    public static final String WINDPOWERTHEORY = "WTUR.PwrAt.Ra.F32.Theory";

    /**
     * 理论有功功率(平均理论有功功率)
     */
    public static final String WINDPOWERTHEORYAVG = "WTUR.PwrAt.Ra.F32.Theory[AVG]";

    /************************************* 光/风资源评估--czr *********************************************/
    /**
     * 测风塔第三层风速（主风速）
     */
    public static final String WINDTOWERMSPD = "METT.WSpd.Ra.F32.layer3";

    /**
     * 测风塔第三层风速（平均主风速）
     */
    public static final String WINDTOWERMSPDAVG = "METT.WSpd.Ra.F32.layer3[AVG]";

    /**
     * 测风塔第三层风向（主风向）
     */
    public static final String WINDTOWERMWDIR = "METT.Wdir.Ra.F32.layer3";

    /**
     * 测风塔第三层风向（平均主风向）
     */
    public static final String WINDTOWERMWDIRAVG = "METT.Wdir.Ra.F32.layer3[AVG]";

    /**
     * 测风塔第三层温度传感器（主温度）
     */
    public static final String WINDTOWERMTEMP = "METT.Temp.Ra.F32.layer3";

    /**
     * 测风塔第三层温度传感器（平均主温度）
     */
    public static final String WINDTOWERMTEMPAVG = "METT.Temp.Ra.F32.layer3[AVG]";

    /**
     * 测风塔第三层大气压力传感器（主压力）单位：HPa
     */
    public static final String WINDTOWERMPRES = "METT.Pres.Ra.F32.layer3";

    /**
     * 测风塔第三层大气压力传感器（平均主压力）单位：HPa
     */
    public static final String WINDTOWERMPRESAVG = "METT.Pres.Ra.F32.layer3[AVG]";

    /**
     * 总辐照度瞬时值 单位：W/㎡
     */
    public static final String TOTALIRRADIANCE = "MELT.TRadio.Ra.F32";

    /**
     * 总辐照度平均值 单位：W/㎡
     */
    public static final String TOTALIRRADIANCEAVG = "MELT.TRadio.Ra.F32[AVG]";

    /**
     * 环境温度
     */
    public static final String PHOTOMETRICTOWERTEMP = "MELT.Temp.Ra.F32";

    /**
     * 环境温度平均值
     */
    public static final String PHOTOMETRICTOWERTEMPAVG = "MELT.Temp.Ra.F32[AVG]";

    /**
     * 环境湿度
     */
    public static final String PHOTOMETRICTOWERHUM = "MELT.Hum.Ra.F32";

    /**
     * 环境湿度平均值
     */
    public static final String PHOTOMETRICTOWERHUMAVG = "MELT.Hum.Ra.F32[AVG]";

    /**************************************************************************************************/

}
