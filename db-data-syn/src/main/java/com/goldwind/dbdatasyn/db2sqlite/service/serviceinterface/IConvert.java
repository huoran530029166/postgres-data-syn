package com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface;

import java.util.List;

import com.goldwind.dbdatasyn.db2sqlite.pojo.Participation;
import com.goldwind.dbdatasyn.pojo.DbConInfo;

public interface IConvert
{
    /**
     * 入参转换方法
     *
     * @param participations 入参
     * @throws Exception 异常
     */
    void participationsConvert(List<Participation> participations) throws Exception;

    /**
     * 数据库连接信息转换方法
     *
     * @param dbConInfo 数据库连接信息模型
     */
    void dbConInfoConvert(DbConInfo dbConInfo);

    /**
     * 本地库地址转换方法
     *
     * @param sqlitePath 本地库地址
     */
    void sqlitePathConvert(String sqlitePath);

    /**
     * 加载同步表对象
     *
     * @throws Exception 异常
     */
    void createTables() throws Exception;

    /**
     * 加载主键对象
     *
     * @throws Exception 异常
     */
    void createPKs() throws Exception;

    /**
     * 加载主键协议标识
     */
    void createPkProFlags();

    /**
     * 加载主键协议号集合
     *
     * @throws Exception 异常
     */
    void createTableProtocolids() throws Exception;

    /**
     * 加载表的列信息
     *
     * @throws Exception 异常
     */
    void createCols() throws Exception;

}
