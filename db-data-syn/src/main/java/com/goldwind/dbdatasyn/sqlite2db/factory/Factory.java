package com.goldwind.dbdatasyn.sqlite2db.factory;

import com.goldwind.dbdatasyn.sqlite2db.service.PgConvertService;
import com.goldwind.dbdatasyn.sqlite2db.service.PgSynDataService;
import com.goldwind.dbdatasyn.sqlite2db.service.serviceinterface.IConvert;
import com.goldwind.dbdatasyn.sqlite2db.service.serviceinterface.ISynData;
import com.goldwind.dbdatasyn.utils.Enum;

/**
 * 本地库同步到关系库 业务工厂类
 *
 * @author huoran
 */
public class Factory
{
    private Factory()
    {
    }

    /**
     * 转换业务类对象 工厂方法
     *
     * @param dbOperType 数据库类型
     * @return 转换业务类对象
     */
    public static IConvert makeIConvertService(Enum.DbOperType dbOperType)
    {
        IConvert result = null;
        switch (dbOperType)
        {
            case POSTGRES:
                result = new PgConvertService();
                break;
            case SQLSERVER:
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 数据同步业务类对象 工厂方法
     *
     * @param dbOperType 数据库类型
     * @return 数据同步业务类对象
     */
    public static ISynData makeISynDataService(Enum.DbOperType dbOperType)
    {
        ISynData result = null;
        switch (dbOperType)
        {
            case POSTGRES:
                result = new PgSynDataService();
                break;
            case SQLSERVER:
                break;
            default:
                break;
        }
        return result;
    }
}
