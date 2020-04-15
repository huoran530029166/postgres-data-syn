package com.goldwind.dbdatasyn.sqlite2db.utils;

/**
 * 枚举类
 *
 * @author huoran
 */
public class Enums
{
    /**
     * 同步结果类型枚举
     */
    public enum SynResultEnum
    {
        /**
         * 完全同步
         */
        COMPLETELY(0),
        /**
        * 部分同步
        */
        PART(1),
        /**
        * 同步失败
        */
        FAIL(2);
        private int value;

        SynResultEnum(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * 表对象列属性执行类型枚举
     */
    public enum ColExcuteType{
        /**
         * 保留
         */
        RETAIN(0),
        /**
         * 新增(用null补足)
         */
        ADDED(1),
        /**
         * 删除(不同步数据)
         */
        DELETE(2);
        private int value;

        ColExcuteType(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }
}
