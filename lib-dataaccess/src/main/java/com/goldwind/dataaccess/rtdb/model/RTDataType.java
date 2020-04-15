package com.goldwind.dataaccess.rtdb.model;

/**
 * 数据类型枚举
 * 
 * @author shenlf
 */
public enum RTDataType
{
    /**
     * 布尔类型, 0或1
     */
    RT_BOOL(0),

    /**
     * 无符号8位整数，占用1字节
     */
    RT_UINT8(1),

    /**
     * 有符号8位整数，占用1字节
     */
    RT_INT8(2),

    /**
     * 无符号16位整数，占用2字节
     */
    RT_UINT16(4),

    /**
     * 有符号16位整数，占用2字节
     */
    RT_INT16(5),

    /**
     * 无符号32位整数，占用4字节
     */
    RT_UINT32(6),

    /**
     * 有符号32位整数，占用4字节
     */
    RT_INT32(7),

    /**
     * 有符号64位整数，占用8字节
     */
    RT_INT64(8),

    /**
     * 32位单精度浮点数，占用4字节
     */
    RT_FLOAT(10),

    /**
     * Double 64位双精度浮点数，占用8字节
     */
    RT_DOUBLE(11),

    /**
     * 变长字符串，长度不超过存储页面大小
     */
    RT_STRING(13),

    /**
     * 时间类型
     */
    RT_DATETIME(18);
    /**
     * 枚举对应的值
     */
    private int value;

    RTDataType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    /**
     * 获取对应枚举值
     * 
     * @param value
     *            值
     * @return 枚举值
     */
    public RTDataType parse(int value)
    {
        RTDataType type;
        switch (value)
        {
            case 0:
                type = RTDataType.RT_BOOL;
                break;
            case 1:
                type = RTDataType.RT_UINT8;
                break;
            case 2:
                type = RTDataType.RT_INT8;
                break;
            case 4:
                type = RTDataType.RT_UINT16;
                break;
            case 5:
                type = RTDataType.RT_INT16;
                break;
            case 6:
                type = RTDataType.RT_UINT32;
                break;
            case 7:
                type = RTDataType.RT_INT32;
                break;
            case 8:
                type = RTDataType.RT_INT64;
                break;
            case 10:
                type = RTDataType.RT_FLOAT;
                break;
            case 11:
                type = RTDataType.RT_DOUBLE;
                break;
            case 13:
                type = RTDataType.RT_STRING;
                break;
            case 18:
                type = RTDataType.RT_DATETIME;
                break;
            default:
                type = null;
                break;
        }
        return type;
    }
}
