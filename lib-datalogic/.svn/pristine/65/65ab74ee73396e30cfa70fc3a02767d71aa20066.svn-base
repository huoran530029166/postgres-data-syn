package com.goldwind.datalogic;

import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.datalogic.utils.MemoryData;

/**
 * 系统初始化类
 * 
 * @author 冯春源
 *
 */
public class DatalogicInit
{
    /**
     * 系统初始化
     * 
     * @param dbOper
     *            数据库连接对象
     */
    public static void init(DbOperBase dbOper)
    {
        // 同步内存和内存切换
        MemoryData.getDBData(dbOper);
    }
}
