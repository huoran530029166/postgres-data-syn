package com.goldwind.dbdatasyn.sqlite2db.service.serviceinterface;

import com.goldwind.dbdatasyn.pojo.DbConInfo;
import com.goldwind.dbdatasyn.sqlite2db.pojo.SynResult;

/**
 * 初始化 参数转换接口
 *
 * @author huoran
 */
public interface IConvert
{
    /**
     * 关系型数据库连接信息转换方法
     *
     * @param dbConInfo 关系型数据库连接信息对象
     */
    void dbConInfoConvert(DbConInfo dbConInfo);

    /**
     * 本地库地址转换方法
     *
     * @param sqlitePath 本地库地址
     */
    void sqlitePathConvert(String sqlitePath);

    /**
     * 本地库异常文件地址转换方法
     *
     * @param sqliteErrPath 本地库异常日志
     */
    void sqliteErrPathConvert(String sqliteErrPath);

    /**
     * 删除标志转换方法
     *
     * @param delFlag 删除标志
     */
    void delFlagConvert(boolean delFlag);

    /**
     * 根据本地库需同步表名集合创建关系型数据库中可以同步的表名对象集合
     *
     * @throws Exception 异常
     */
    void createAliveTables() throws Exception;

    /**
     * 校验表对象外键关系
     *
     * @throws Exception 异常
     */
    void checkFkTableInfo() throws Exception;

    /**
     * 校验表对象是否缺失外键主表
     *
     * @return 校验结果
     */
    SynResult checkFkMainMiss();

    /**
     * 校验表对象是否缺失外键子表标致
     */
    SynResult checkFkChildMissFlag();

    /**
     * 对符合条件的表对象进行外键关系排序
     */
    void sortTables();

    /**
     * 创建表对象的关系型数据库列信息
     *
     * @throws Exception 异常
     */
    void createDbCols() throws Exception;

    /**
     * 创建表对象的本地库列信息
     *
     * @throws Exception 异常
     */
    void createSqliteCols() throws Exception;

    /**
     * 比对关系型数据库和本地库列信息,生成列执行类型属性
     */
    void compareCols();

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
     * 加载关系库主键协议号集合
     *
     * @throws Exception 异常
     */
    void createTableProtocolids() throws Exception;

    /**
     * 加载本地库主键协议号集合
     *
     * @throws Exception 异常
     */
    void createTableProtocolidsSqlite() throws Exception;
}
