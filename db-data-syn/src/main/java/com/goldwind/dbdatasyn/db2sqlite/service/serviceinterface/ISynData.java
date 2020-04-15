package com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface;

import java.util.HashMap;
import java.util.Map;

public interface ISynData
{
    /**
     * 创建Sqlite副本文件
     *
     * @throws Exception 异常
     */
    void createSqliteFileCopy() throws Exception;

    /**
     * 创建tablenames表
     *
     * @throws Exception 异常
     */
    void createTableNamesTable() throws Exception;

    /**
     * 同步所有的表数据
     *
     * @throws Exception 异常
     */
    Map<String, HashMap<Integer, Exception>> synAllData() throws Exception;

    /**
     * 副本文件和主文件的切换
     *
     * @throws Exception 异常
     */
    void copyToResultFile() throws Exception;
}
