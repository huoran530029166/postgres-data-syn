package com.goldwind.dataaccess.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;

public class DatabaseHelper
{
    private DatabaseHelper()
    {

    }

    private static Log logger = Log.getLog(DatabaseHelper.class);

    private static String errorStr = "DatabaseHelper执行脚本失败";

    /**
     * 执行数据库查询将结果封装到List<Map<String,Object>>中
     * 
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String, Object>> resultSetToList(String sql, Object[] params)
    {
        try (DbOperBase oper = new DbOperBase())
        {
            Long startTime = System.currentTimeMillis();
            oper.openConn();
            ResultSet dn = oper.getResultSet(sql, params);
            List<Map<String, Object>> resultList = resultSetToList(dn);
            Long endTime = System.currentTimeMillis();
            Long elapsedTime = (endTime - startTime) / 1000;
            if (elapsedTime > 10)
            {
                logger.warn("超时sql耗时超过" + elapsedTime + "秒>>" + sql);
            }
            return resultList;
        }
        catch (Exception e)
        {
            logger.error(DataAsExpSet.getExpMsgByResCode("DatabaseHelper_resultSetToList", new String[] { "sql", "params" }, new Object[] { sql, params }, e));
        }
        return new ArrayList<>();
    }

    /**
     * 执行数据库查询将结果封装到List<Map<String,Object>>中
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> resultSetToList(DbOperBase oper, String sql, Object[] params)
    {
        try
        {
            oper.openConn();
            Long startTime = System.currentTimeMillis();
            ResultSet dn = oper.getResultSet(sql, params);
            List<Map<String, Object>> resultList = resultSetToList(dn);
            Long endTime = System.currentTimeMillis();
            Long elapsedTime = (endTime - startTime) / 1000;
            if (elapsedTime > 10)
            {
                logger.warn("超时sql耗时超过" + elapsedTime + "秒>>" + sql);
            }
            return resultList;
        }
        catch (Exception e)
        {
            logger.error(DataAsExpSet.getExpMsgByResCode("DatabaseHelper_resultSetToList_1", new String[] { "sql", "params" }, new Object[] { sql, params }, e));
        }
        return new ArrayList<>();
    }

    public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException
    {
        List<Map<String, Object>> results = new ArrayList<>();
        List<String> colNameList = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for (int i = 0; i < colCount; i++)
        {
            colNameList.add(rsmd.getColumnName(i + 1));
        }
        while (rs.next())
        {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < colCount; i++)
            {
                String key = colNameList.get(i);
                Object value = rs.getObject(colNameList.get(i));
                map.put(key, value);
            }
            results.add(map);
        }
        return results;
    }

    /**
     * 执行sql脚本方法封装,返回-1表示执行异常
     * 
     * @throws DataAsException
     **/
    public static int excute(String sql) throws DataAsException
    {
        try (DbOperBase oper = new DbOperBase())
        {
            oper.openConn();
            return oper.excute(sql, null);
        }
        catch (Exception e)
        {
            logger.error(errorStr + sql + System.lineSeparator() + e);
            DataAsExpSet.throwExpByMsg("DatabaseHelper_resultSetToList", new String[] { "sql" }, new Object[] { sql }, e);
        }
        return -1;
    }

    /**
     * 检查表名是否存在
     * 
     * @param oper
     * @param tableName
     * @return
     */
    public static boolean checkTableExsit(DbOperBase oper, String tableName)
    {
        try
        {
            oper.openConn();
            ResultSet dn = oper.getResultSet("select to_regclass('" + tableName + "')  is not null as temp ;", null);
            if (dn.next())
            {
                return dn.getBoolean("temp");
            }
        }
        catch (Exception e)
        {
            logger.error(DataAsExpSet.getExpMsgByResCode("DatabaseHelper_checkTableExsit_1", new String[] { "tableName" }, new Object[] { tableName }, e));
        }
        return false;
    }

    /**
     * 不使用事务批量执行SQL语句
     **/
    public static int runBatchSql(String[] sqls) throws Exception
    {

        try (DbOperBase oper = new DbOperBase();)
        {
            oper.openConn();
            oper.runBatchSql(sqls);
            return sqls.length;
        }
        catch (Exception e)
        {
            logger.error(errorStr + StringUtils.join(sqls, "\r\n"), e);
            throw e;
        }
    }

    /**
     * 不忽略重复错误使用事务批量执行SQL语句
     **/
    public static int excuteBatchSqls(String[] sqls) throws Exception
    {

        try (DbOperBase oper = new DbOperBase();)
        {
            oper.openConn();
            oper.excuteBatchSqls(sqls);
            return sqls.length;
        }
        catch (Exception e)
        {
            logger.error(errorStr + StringUtils.join(sqls, "\r\n"), e);
            throw e;
        }
    }

    /**
     * 执行sql脚本方法封装
     * 
     * @throws Exception
     **/
    public static int excute(DbOperBase oper, String[] sqls) throws Exception
    {
        try
        {
            oper.openConn();
            oper.runBatchSql(sqls);
            return sqls.length;
        }
        catch (Exception e)
        {
            logger.error(errorStr + StringUtils.join(sqls, "\r\n"), e);
            throw e;
        }
    }

    public static int getInt(Map<String, Object> map, String key)
    {

        if (null != map.get(key))
        {
            String value = map.get(key) + "";
            return Integer.parseInt(value);
        }
        return 0;

    }

    public static long getLong(Map<String, Object> map, String key)
    {
        if (null != map.get(key))
        {
            String value = map.get(key) + "";
            return Long.parseLong(value);
        }
        return 0;
    }

    public static double getDouble(Map<String, Object> map, String key)
    {
        if (null != map.get(key))
        {
            String value = map.get(key) + "";
            return Double.parseDouble(value);
        }
        return 0.0;
    }

    public static String getString(Map<String, Object> map, String key)
    {
        if (null != map.get(key))
        {
            return map.get(key) + "";
        }
        return "";
    }

}
