package com.goldwind.dataaccess.database;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sqlite.SqliteConn;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.database.DbAssistant.DatabaseType;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * SQLite数据库操作类
 * 
 * @author Administrator
 *
 */
public class SQLiteOper extends DbOperBase
{
    /**
     * 是否使用内存模式
     */
    private boolean momoryMode = false;
    /**
     * sqlite连接
     */
    private SqliteConn con;
    /**
     * sqlite库文件路径+名称
     */
    private String fileName;

    public SQLiteOper(String connUrl)
    {
        super(DatabaseType.SQLite, connUrl, null, null);
        this.fileName = connUrl;

    }

    /**
     * 使用内存模式
     */
    public void useMomoryMode()
    {
        this.momoryMode = true;
    }

    @Override
    public void openConn() throws Exception
    {
        // Class.forName("org.sqlite.JDBC");
        if (momoryMode)
        {
            setConnUrl("jdbc:sqlite::memory:");
        }
        else
        {
            if (getConnUrl().indexOf("jdbc:sqlite:") < 0)
            {
                setConnUrl("jdbc:sqlite:/" + getConnUrl());
            }
        }
        this.con = new SqliteConn(getConnUrl());
        // setConn(DriverManager.getConnection(getConnUrl()));
        setConn(con.getCon());
    }

    /**
     * 创建文件库
     * 
     * @param filePath
     *            文件库路径名
     * @throws DataAsException
     *             自定义异常
     */
    public static void createDbFile(String filePath) throws DataAsException
    {
        try
        {
            File file = new File(filePath);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists())
            {
                dir.mkdirs();
            }

            if (!file.exists())
            {
                file.createNewFile();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("SQLiteOper_createDbFile_1", new String[] { "fileName" }, new Object[] { filePath }, exp);
        }
    }

    /**
     * 将内存库存到本地
     * 
     * @throws SQLException
     *             sql异常
     */
    public void memory2db() throws SQLException
    {
        con.backupDb(fileName);
    }

    public boolean isMemoryMode()
    {
        return this.momoryMode;
    }

    /**
     * 检查表中是否有指定列 如果没有则添加
     * 
     * @param sqliteOper
     *            sqlite连接
     * @param tableName
     *            所检查表名
     * @param checkColumnNames
     *            所检查列-列属性
     * @return
     * @throws DataAsException
     */
    public static void checkColumns(SQLiteOper sqliteOper, String tableName, HashMap<String, String> checkColumnNames) throws DataAsException
    {
        // 需要添加的列名
        List<String> alterColumn = new ArrayList<>(16);
        try
        {
            // 查询所检查表的所有列名
            ResultSet fileSendColumns = sqliteOper.getResultSet("select * from " + tableName + " limit 0;", null);
            List<String> columnList = new ArrayList<>(16);
            ResultSetMetaData fileSendMetaData = fileSendColumns.getMetaData();
            int columnCount = fileSendMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++)
            {
                columnList.add(fileSendMetaData.getColumnName(i));
            }
            
            // //查询所检查表的所有列名和类型
            // ResultSet rs = sqliteOper.getResultSet("PRAGMA table_info(["+tableName+"]);", null);
            // while(rs.next()) {
            // rs.getString("")
            // }

            // 根据传进来的需要检查的列名 进行检查
            checkColumnNames.forEach((columnName, columnType) -> {
                if (!columnList.contains(columnName))
                {
                    alterColumn.add(columnName);
                }
            });

            // 不存在的列 进行添加列操作
            if (!alterColumn.isEmpty())
            {
                for (String columnName : alterColumn)
                {
                    sqliteOper.excute("alter table " + tableName + " add column " + columnName + " " + checkColumnNames.get(columnName), null);
                }
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("SQLiteOper_checkColumns_1", new String[] { "sqliteOper", "tableName", "checkColumnNames" }, new Object[] { sqliteOper, tableName, checkColumnNames }, e);
        }
    }

    /**
     * 检查数据库是否具有指定表 如果没有则创建
     * 
     * @param sqliteOper
     *            sqlite连接
     * @param checkTableName
     *            检查表名
     * @param createTableSql
     *            创建表sql
     * @throws DataAsException
     */
    public static void checkTable(SQLiteOper sqliteOper, String checkTableName, String createTableSql) throws DataAsException
    {
        // SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;
        // 数据库所有的表名
        List<String> tableNameList = new ArrayList<>(16);
        try
        {
            ResultSet rs = sqliteOper.getResultSet("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;", null);
            while (rs.next())
            {
                String tableName = rs.getString("name");
                tableNameList.add(tableName);
            }

            // 查询数据库中有没有所指定的表
            if (!tableNameList.contains(checkTableName))
            {
                sqliteOper.excute(createTableSql, null);
            }

        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("SQLiteOper_checkTable_1", new String[] { "sqliteOper", "checkTableName", "createTableSql" }, new Object[] { sqliteOper, checkTableName, createTableSql },
                    e);
        }
    }
}
