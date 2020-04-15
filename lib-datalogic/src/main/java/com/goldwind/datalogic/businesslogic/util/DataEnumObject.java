package com.goldwind.datalogic.businesslogic.util;

/**
 * 枚举 方法
 * 
 * @author 谭璟
 *
 */
public class DataEnumObject
{


    /**
     * 损失电量 类型
     * 
     * @author 谭璟
     *
     */
    public enum LossType
    {
        /**
         * 正常
         */
        Normal(0),

        /**
         * 调度限功率2001
         */
        Dispatchelc(2001),

        /**
         * 风机维护2002
         */
        Windcontrol(2002),

        /**
         * 技术待命2003
         */
        Technicalstandby(2003),

        /**
         * 远程停机2004
         */
        Romatestop(2004),

        /**
         * 电网故障2005
         */
        PowerFault(2005),

        /**
         * 故障停机2006
         */
        FaultStop(2006),

        /**
         * 就地停机2007
         */
        Otherdispatchelc(2007),
        
        /**
         * 叶片结冰损失电量
         */
        BladeIceLossPower(2031),

        /**
         * 其他限功率2008
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
     * 聚合标识
     * 
     * @author 谭璟
     *
     */
    public enum Together
    {
        /**
         * 日聚合
         */
        DayTogether(1),

        /**
         * 时间段聚合
         */
        TimeTogether(2),

        /**
         * 小时聚合
         */
        HourTogether(3);

        /**
         * 类型值
         */
        private int result;

        Together(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }

    /**
     * 样板风机 自身理论功率 传递函数
     * 
     * @author 谭璟
     *
     */
    public enum DeviceChangeType
    {
        /**
         * 样板风机类型 方案一
         */
        PLANA(1),

        /**
         * 方案二
         */
        PLANB(2),

        /**
         * 方案三
         */
        PLANC(3),

        /**
         * 方案四
         */
        PLAND(4),

        /**
         * 方案五
         */
        PLANE(5),

        /**
         * 方案六
         */
        PLANF(6);

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
     * 运行状态字 标识
     * 
     * @author 谭璟
     *
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

    public enum InverterType
    {
        /**
         * 集中式
         */
        NBQJZ("NBQJZ"),

        /**
         * 组串式逆变器
         */
        NBQZC("NBQZC");

        /**
         * 数值
         */
        private String result;

        InverterType(String result)
        {
            this.result = result;
        }

        public String getResult()
        {
            return this.result;
        }
    }

    /**
     * 运行状态字 标识
     * 
     * @author 谭璟
     *
     */
    public enum IvenState
    {
        /**
         * 故障停机
         */
        IVFAULT(4001),

        /**
         * 降额
         */
        IVDOWN(4002),

        /**
         * 限额
         */
        IVNUM(4003),

        /**
         * 远程停机
         */
        IVROMT(4004);

        /**
         * 类型值
         */
        private int result;

        IvenState(int result)
        {
            this.result = result;
        }

        public int getResult()
        {
            return this.result;
        }
    }
}
