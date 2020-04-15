package com.goldwind.datalogic.businesslogic;

import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.database.DbOperBase;

/**
 * 测试基础类 里面包含 数据库连接属性和初始化
 * 
 * @author pactera
 *
 */
public class TestCase
{
    /**
     * ip:prot路径
     */
    private final String ip = "10.80.5.68:8101";

    /**
     * 数据库名字
     */
    private final String dbname = "qhsoam1011";

    /**
     * 用户名
     */
    private final String username = "pguser";

    /**
     * 密码
     */
    private final String password = "pguser";

    public TestCase()
    {
    }

    /**
     * 初始化数据库
     * 
     * @throws Exception
     *             异常
     */
    protected void inItDb() throws Exception
    {
        String url = "jdbc:postgresql://" + ip + "/" + dbname;
        DbOperBase.initPool(DatabaseType.Postgre, url, username, password, 100, 10, 60);
    }
}
