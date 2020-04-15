package com.goldwind.datalogic.business;

import java.io.IOException;
import java.util.HashMap;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.database.SQLiteOper;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 文件同步业务
 * 
 * @author Administrator
 *
 */
public class FileSynchHelp
{
    // filesend filereceive 新添字段 0-文件 1-数据库
    private static final String DATATYPECOLUMN = "datatype";

    // filesend filereceive 新添字段 0-关闭，1-开启
    private static final String SYNCHFLAGCOLUMN = "synchflag";

    // filesend 新添字段 0-间隔同步 1-指定时间点同步
    private static final String PERIODTYPECOLUMN = "periodtype";

    // filesend 新添字段 3600-单位：秒 12:00;14:00-多个时间点用分号隔开
    private static final String PERIODCOLUMN = "newperiod";

    // filereceive 新添字段 delflg 0-否 1-是
    private static final String DELFLG = "delflg";

    // filereceive 新添字段 url
    private static final String URL = "url";

    // filetransrecord filereceive 新添字段 0-否，1-是
    private static final String WARNFLGCOLUMN = "warnflg";

    // 字段类型integer
    private static final String INTEGER = "integer";

    // 字段类型varchar
    private static final String VARCHAR = "varchar";

    /**
     * 检查sqlite本地库是否是新的本地库文件(具有新规则的字段属性)
     * 
     * @param sqlitePath
     *            本地库文件路径
     * @throws DataAsException
     */
    public static void checkConfig(SQLiteOper sqliteOper) throws DataAsException
    {
        // 检查filesend filereceive 是否需要添加列 lixiang:
        HashMap<String, String> checkColumnNames = new HashMap<>(16);
        checkColumnNames.put(DATATYPECOLUMN, INTEGER);
        checkColumnNames.put(SYNCHFLAGCOLUMN, INTEGER);
        checkColumnNames.put(PERIODTYPECOLUMN, INTEGER);

        try
        {
            // 检查filesend表
            SQLiteOper.checkColumns(sqliteOper, "filesend", checkColumnNames);

            // 检查filereceive
            checkColumnNames = new HashMap<>(16);
            checkColumnNames.put(WARNFLGCOLUMN, INTEGER);
            checkColumnNames.put(DATATYPECOLUMN, INTEGER);
            checkColumnNames.put(SYNCHFLAGCOLUMN, INTEGER);
            checkColumnNames.put(DELFLG, INTEGER);
            checkColumnNames.put(URL, VARCHAR);
            SQLiteOper.checkColumns(sqliteOper, "filereceive", checkColumnNames);

            // 检查filetransrecord 是否需要添加列warnflg lixiang:
            checkColumnNames = new HashMap<>(16);
            checkColumnNames.put(WARNFLGCOLUMN, INTEGER);
            SQLiteOper.checkColumns(sqliteOper, "filetransrecord", checkColumnNames);

            // 检查本地库是否有tablenames表 lixiang:
            String createTableSql = "CREATE TABLE tablenames (id integer primary key not null ,synchtype integer not null ,tablename varchar not null ,dburl varchar not null ,username varchar not null ,password varchar not null )";
            SQLiteOper.checkTable(sqliteOper, "tablenames", createTableSql);
        }
        catch (DataAsException e)
        {
            DataAsExpSet.throwExpByResCode("FileTransManage_checkConfig_1", new String[] {}, new Object[] {}, e);
        }
    }
}
