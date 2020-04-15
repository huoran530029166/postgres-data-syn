package com.goldwind.dbdatasyn.sqlite2db.service;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.database.DbAssistant;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dbdatasyn.sqlite2db.init.Init;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Col;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Pk;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Table;
import com.goldwind.dbdatasyn.sqlite2db.pojo.TableData;
import com.goldwind.dbdatasyn.sqlite2db.service.serviceinterface.ISynData;
import com.goldwind.dbdatasyn.sqlite2db.utils.Enums;
import com.goldwind.dbdatasyn.sqlite2db.utils.Func;
import com.goldwind.dbdatasyn.sqlite2db.utils.RunParams;

import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * 本地库同步关系库 PG库同步数据业务类
 *
 * @author huoran
 */
public class PgSynDataService implements ISynData
{
    /**
     * 同步数据主方法
     */
    @Override public void synData() throws Exception
    {
        //删除SQL集合
        List<String> delSqls = new ArrayList<>();
        for (Table table : RunParams.getTables())
        {
            System.out.println("开始同步表:" + table.getSchemaName() + "." + table.getTableName());
            synTableData(table, delSqls);
        }

        try
        {
            if (RunParams.isDelFalg())
            {
                System.out.println("删除标记为true,开始批量删除多余数据");
                //如果删除标记为true,在全表执行完新增、修改后,执行批量删除动作；删除动作如果失败不需记录错误异常
                Init.dbOperBase.openConn();
                Init.dbOperBase.excuteBatchSqlIgnorErr(delSqls.toArray(new String[delSqls.size()]));
                System.out.println("删除数据完成");
            }
        }
        catch (Exception e)
        {
            System.out.println("del data fail");
        }
        finally
        {
            try
            {
                Init.dbOperBase.closeConn();
            }
            catch (Exception e)
            {

            }
        }
    }

    /**
     * 同步单表数据
     *
     * @param table   表对象
     * @param delSqls 删除SQL集合
     */
    @Override public void synTableData(Table table, List<String> delSqls) throws Exception
    {
        //定义异常记录对象
        Map<String, HashMap<Integer, Exception>> errInfos = new HashMap<>();
        try
        {
            if (table.isPkProFlag())
            {
                System.out.println("同步表:" + table.getSchemaName() + "." + table.getTableName() + "主键包含协议号,进行多协议分组同步");
                //表主键包含协议号,根据协议号分组查询比对数据
                //PG库该表协议号集合
                List<Integer> dbProtocolids = table.getProtocolids();
                //本地库该表协议号集合
                List<Integer> sqliteProtocolids = table.getProtocolidsSqlite();
                //该表本地库不存在、关系库存在的协议号集合
                List<Integer> sqliteNoExistProIds = new ArrayList<>(dbProtocolids);
                sqliteNoExistProIds.removeAll(sqliteProtocolids);
                if (RunParams.isDelFalg())
                {
                    //如果删除标记为true,删除PG库中多余的协议号对应的数据行
                    deleteNoExistProData(table, sqliteNoExistProIds, errInfos, delSqls);
                }
                int count = 0;
                for (Integer protocolid : table.getProtocolidsSqlite())
                {
                    count++;
                    System.out.println("同步表:" + table.getSchemaName() + "." + table.getTableName() + ",开始同步协议:" + protocolid + ",进度:" + count + "/" + table.getProtocolidsSqlite().size());
                    //表主键包含协议号,按本地库协议号集合依次查询比对数据
                    TableData sqliteData = querySqliteData(table, protocolid, errInfos);
                    TableData dbData = queryDbData(table, protocolid, errInfos);
                    if (sqliteData.isEffectFlag() && dbData.isEffectFlag())
                    {
                        //当本地库和关系库查询出的数据都为有效数据时,比对数据
                        compareTableData(protocolid, sqliteData, dbData, table, errInfos, delSqls);
                    }
                    System.out.println("同步表:" + table.getSchemaName() + "." + table.getTableName() + ",完成同步协议:" + protocolid + ",进度:" + count + "/" + table.getProtocolidsSqlite().size());
                }
            }
            else
            {
                System.out.println("同步表:" + table.getSchemaName() + "." + table.getTableName() + "主键不包含协议号,开始全表分组同步");
                //表主键不包含协议号,全表查询比对数据
                TableData sqliteData = querySqliteData(table, -1, errInfos);
                TableData dbData = queryDbData(table, -1, errInfos);
                if (sqliteData.isEffectFlag() && dbData.isEffectFlag())
                {
                    //当本地库和关系库查询出的数据都为有效数据时,比对数据
                    compareTableData(-1, sqliteData, dbData, table, errInfos, delSqls);
                }
                System.out.println("同步表:" + table.getSchemaName() + "." + table.getTableName() + "主键不包含协议号,完成全表分组同步");

            }
        }
        catch (Exception e)
        {
            //主线程发生异常时,记录一条group = main的异常记录
            HashMap<Integer, Exception> flowErr = new HashMap<>();
            flowErr.put(null, e);
            errInfos.put("main", flowErr);
            DataAsExpSet.throwExpByMsg("同步表:" + table.getSchemaName() + "." + table.getTableName() + ",单表主线程发生异常", null, null, e);
        }
        finally
        {
            //将异常信息写入本地库异常文件
            Func.LogSqliteErrs(errInfos, table);
        }
    }

    /**
     * 查询本地库分组数据,如果主键包含协议号,传入协议号作为条件;如果主键不包含协议号,入参传-1;
     *
     * @param table      表对象
     * @param protocolid 协议号,为-1时,查询全表
     * @param errInfos   异常信息收集对象
     */
    @Override public TableData querySqliteData(Table table, Integer protocolid, Map<String, HashMap<Integer, Exception>> errInfos)
    {
        //定义数据库分组数据对象
        TableData result = new TableData();
        try
        {
            Init.sqLiteOper.openConn();
            String sql = assembleSqliteDataSql(protocolid, table);
            ResultSet rs = Init.sqLiteOper.getResultSet(sql, null);
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            while (rs.next())
            {
                Map<String, String> lineData = new TreeMap<>();
                //动态获取数据库结果集中的数据组装行数据内存
                for (int i = 1; i <= count; i++)
                {
                    String colTypeName = rsmd.getColumnTypeName(i);
                    String colName = rsmd.getColumnName(i);
                    String data = rs.getString(colName);

                    if (data != null && ("float".equalsIgnoreCase(colTypeName) || "double".equalsIgnoreCase(colTypeName)))
                    {
                        //如果本地库列数据为小数,对该值做整数/小数判断,去除小数部分全为0的部分
                        double d = Double.valueOf(data);
                        if (judgeDoubleIsInt(d))
                        {
                            //如果double是int
                            data = String.valueOf((int) Math.floor(d));
                        }
                        else
                        {
                            //如果double不是int,去除末尾多余的0
                            data = delTailZero(data);
                        }
                    }
                    lineData.put(colName, data);
                }
                StringBuilder pkKey = new StringBuilder();
                //组装主键key值
                if (table.getPks().isEmpty())
                {
                    //如果没有主键,按所有字段都是主键计算
                    Iterator<Map.Entry<String, String>> iterator = lineData.entrySet().iterator();
                    while (iterator.hasNext())
                    {
                        Map.Entry<String, String> entry = iterator.next();
                        pkKey.append(entry.getValue() + "@sep@");
                    }
                }
                else
                {
                    //如果有主键,按主键计算
                    for (Pk pk : table.getPks())
                    {
                        String pkName = pk.getColName();
                        pkKey.append(lineData.get(pkName) + "@sep@");
                    }

                }
                for (int i = 0; i < 5; i++)
                {
                    //删除最后的一个@sep@
                    pkKey.deleteCharAt(pkKey.length() - 1);
                }
                result.getDatas().put(pkKey.toString(), lineData);
            }
        }
        catch (Exception e)
        {
            //主线程发生异常时,记录一条group = querySqliteData的异常记录
            HashMap<Integer, Exception> flowErr = new HashMap<>();
            flowErr.put(null, e);
            errInfos.put("querySqliteData", flowErr);
            result.setEffectFlag(false);
        }
        finally
        {
            try
            {
                Init.sqLiteOper.closeConn();
            }
            catch (Exception e)
            {

            }
        }
        return result;
    }

    /**
     * 组装本地库数据查询SQL
     *
     * @param protocolid 协议号,为-1时,查询全表
     * @param table      表对象
     * @return SQL语句
     */
    @Override public String assembleSqliteDataSql(Integer protocolid, Table table)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        for (Col col : table.getColSqliteMap().values())
        {
            // 根据本地库列信息拼接查询语句,
            // 如果是保留列：直接拼接
            // 如果是删除列：不拼接
            // 如果是新增列：按null值拼接
            Enums.ColExcuteType type = col.getColExcuteType();
            switch (type)
            {
                case RETAIN:
                    builder.append(col.getColName() + " as " + col.getColName() + " ,");
                    break;
                case ADDED:
                    builder.append("null as " + col.getColName() + " ,");
                    break;
                case DELETE:
                    break;
                default:
                    break;
            }
        }
        //如果sql以,结尾删除结尾的,
        if (builder.charAt(builder.length() - 1) == ',')
        {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append(" from " + table.getTableName() + " ");
        //拼接protocolid查询条件
        if (protocolid == null)
        {
            builder.append("where protocolid is null ");
        }
        else if (protocolid != -1)
        {
            builder.append("where protocolid = " + protocolid + " ");
        }
        return builder.toString();
    }

    /**
     * 查询关系库分组数据,如果主键包含协议号,传入协议号作为条件;如果主键不包含协议号,入参传-1;
     *
     * @param table      表对象
     * @param protocolid 协议号,为-1时,查询全表
     * @param errInfos   异常收集对象
     */
    @Override public TableData queryDbData(Table table, Integer protocolid, Map<String, HashMap<Integer, Exception>> errInfos)
    {
        TableData result = new TableData();
        try
        {
            Init.dbOperBase.openConn();
            String sql = assembleDbDataSql(protocolid, table);
            ResultSet rs = Init.dbOperBase.getResultSet(sql, null);
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            while (rs.next())
            {
                Map<String, String> lineData = new TreeMap<>();
                //动态获取数据库结果集中的数据组装行数据内存
                for (int i = 1; i <= count; i++)
                {
                    String colTypeName = rsmd.getColumnTypeName(i);
                    String colName = rsmd.getColumnName(i);
                    String data = rs.getString(colName);

                    if (data != null && ("numeric".equalsIgnoreCase(colTypeName) || "single".equalsIgnoreCase(colTypeName) || "decimal".equalsIgnoreCase(colTypeName) || "float"
                            .equalsIgnoreCase(colTypeName) || "double".equalsIgnoreCase(colTypeName)))
                    {
                        //如果本地库列数据为小数,对该值做整数/小数判断,去除小数部分全为0的部分
                        double d = Double.valueOf(data);
                        if (judgeDoubleIsInt(d))
                        {
                            //如果double是int
                            data = String.valueOf((int) Math.floor(d));
                        }
                        else
                        {
                            //如果double不是int,去除末尾多余的0
                            data = delTailZero(data);
                        }
                    }
                    lineData.put(colName, data);
                }
                StringBuilder pkKey = new StringBuilder();
                //组装主键key值
                if (table.getPks().isEmpty())
                {
                    //如果没有主键,按所有字段都是主键计算
                    Iterator<Map.Entry<String, String>> iterator = lineData.entrySet().iterator();
                    while (iterator.hasNext())
                    {
                        Map.Entry<String, String> entry = iterator.next();
                        pkKey.append(entry.getValue() + "@sep@");
                    }
                }
                else
                {
                    //如果有主键,按主键计算
                    for (Pk pk : table.getPks())
                    {
                        String pkName = pk.getColName();
                        pkKey.append(lineData.get(pkName) + "@sep@");
                    }

                }
                for (int i = 0; i < 5; i++)
                {
                    //删除最后的一个@sep@
                    pkKey.deleteCharAt(pkKey.length() - 1);
                }
                result.getDatas().put(pkKey.toString(), lineData);
            }
        }
        catch (Exception e)
        {
            //主线程发生异常时,记录一条group = queryDbData的异常记录
            HashMap<Integer, Exception> flowErr = new HashMap<>();
            flowErr.put(null, e);
            errInfos.put("queryDbData", flowErr);
            result.setEffectFlag(false);
        }
        finally
        {
            try
            {
                Init.dbOperBase.closeConn();
            }
            catch (Exception e)
            {

            }

        }
        return result;
    }

    /**
     * 组装关系库数据查询SQL
     *
     * @param protocolid 协议号,为-1时,查询全表
     * @param table      表对象
     * @return SQL语句
     */
    @Override public String assembleDbDataSql(Integer protocolid, Table table)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("select * from " + table.getSchemaName() + "." + table.getTableName() + " ");
        //拼接protocolid查询条件
        if (protocolid == null)
        {
            builder.append("where protocolid is null ");
        }
        else if (protocolid != -1)
        {
            builder.append("where protocolid = " + protocolid + " ");
        }
        return builder.toString();
    }

    /**
     * 比较本地库、关系库 分组数据 生成数据库脚本
     *
     * @param sqliteData 本地库数据集
     * @param protocolid 协议号
     * @param dbData     关系库数据集
     * @param table      表对象
     * @param errInfos   错误异常信息
     * @param delSqls    记录删除SQL集合
     * @return 数据库脚本集合
     */
    @Override public void compareTableData(Integer protocolid, TableData sqliteData, TableData dbData, Table table, Map<String, HashMap<Integer, Exception>> errInfos, List<String> delSqls)
    {
        try
        {
            //定义该表数据集合比对需要批量执行的SQL
            List<String> tableSqls = new ArrayList<>();
            //遍历sqlite行数据
            for (Map.Entry<String, Map<String, String>> sqliteLineData : sqliteData.getDatas().entrySet())
            {
                String pKeyData = sqliteLineData.getKey();
                Map<String, String> sqliteValData = sqliteLineData.getValue();

                if (dbData.getDatas().containsKey(pKeyData))
                {
                    //如果关系库数据集包含与本地库行数据主键一致的数据时,比对数据的主体是否一致
                    Map<String, String> updateMap = compareLineData(sqliteValData, dbData.getDatas().get(pKeyData));
                    if (updateMap.isEmpty())
                    {
                        //如果匹配到了本地库关系库主键相同的数据,操作完之后,在关系库数据集中删除该数据
                        dbData.getDatas().remove(pKeyData);
                        //如果一致,直接跳过比对
                        continue;
                    }
                    else
                    {
                        //如果不一致,生成update语句
                        StringBuilder builder = new StringBuilder();
                        builder.append("update \"" + table.getSchemaName() + "\".\"" + table.getTableName() + "\" set ");
                        for (Map.Entry<String, String> updateEntry : updateMap.entrySet())
                        {
                            builder.append(updateEntry.getKey() + " = " + (updateEntry.getValue() == null ?
                                    "null," :
                                    ("\'" + (updateEntry.getValue().startsWith("@oidsep@") ? oidHandle(updateEntry.getValue()) : updateEntry.getValue())) + "\',"));
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        builder.append(" where ");

                        Map<String, String> delMap = createDelDataMap(sqliteLineData, table);
                        for (Map.Entry<String, String> delEntry : delMap.entrySet())
                        {
                            builder.append(delEntry.getKey() + (delEntry.getValue() == null ? " is null and " : " = \'" + delEntry.getValue() + "\' and "));
                        }
                        for (int i = 0; i < 4; i++)
                        {
                            builder.deleteCharAt(builder.length() - 1);
                        }
                        tableSqls.add(builder.toString());
                    }
                    //如果匹配到了本地库关系库主键相同的数据,操作完之后,在关系库数据集中删除该数据
                    dbData.getDatas().remove(pKeyData);
                }
                else
                {
                    //如果关系库数据集合不包含本地库行数据主键一致的数据,执行新增操作
                    StringBuilder builder = new StringBuilder();
                    builder.append("insert into \"" + table.getSchemaName() + "\".\"" + table.getTableName() + "\" ( ");
                    StringBuilder insertValueBuilder = new StringBuilder();
                    insertValueBuilder.append(" values (");
                    for (Map.Entry<String, String> insertEntry : sqliteValData.entrySet())
                    {
                        builder.append(insertEntry.getKey() + ",");
                        insertValueBuilder.append(insertEntry.getValue() == null ?
                                "null," :
                                ("\'" + (insertEntry.getValue().startsWith("@oidsep@") ? oidHandle(insertEntry.getValue()) : insertEntry.getValue()) + "\',"));
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    insertValueBuilder.deleteCharAt(insertValueBuilder.length() - 1);
                    builder.append(" ) ").append(insertValueBuilder).append(" ) ");

                    tableSqls.add(builder.toString());
                }
            }
            Init.dbOperBase.openConn();
            HashMap<Integer, Exception> errs = Init.dbOperBase.excuteBatchSqlIgnorErr(tableSqls.toArray(new String[tableSqls.size()]));
            if (!errs.isEmpty())
            {
                errInfos.put(protocolid == null ? null : String.valueOf(protocolid), errs);
            }
        }
        catch (Exception e)
        {
            //主线程发生异常时,记录一条group = queryDbData的异常记录
            HashMap<Integer, Exception> flowErr = new HashMap<>();
            flowErr.put(null, e);
            errInfos.put("compareTableData,protocolid:" + protocolid, flowErr);
        }
        finally
        {
            try
            {
                Init.dbOperBase.closeConn();
            }
            catch (Exception e)
            {

            }
        }
        //根据关系库数据集合中剩余的数据(即关系库存在本地库不存在的数据,添加到删除SQL集合中)
        for (Map.Entry<String, Map<String, String>> entry : dbData.getDatas().entrySet())
        {
            Map<String, String> delMap = createDelDataMap(entry, table);
            StringBuilder builder = new StringBuilder();
            builder.append("delete from \"" + table.getSchemaName() + "\".\"" + table.getTableName() + "\" where ");
            for (Map.Entry<String, String> delEntry : delMap.entrySet())
            {
                builder.append(delEntry.getKey() + (delEntry.getValue() == null ? " is null and " : " = \'" + delEntry.getValue() + "\' and "));
            }
            for (int i = 0; i < 4; i++)
            {
                builder.deleteCharAt(builder.length() - 1);
            }
            delSqls.add(builder.toString());
        }
    }

    /**
     * 删除PG库 单表数据 本地库中不存在关系库中存在的协议号数据
     *
     * @param table       表对象
     * @param protocolids 协议号集合
     * @param errInfos    异常记录集合
     * @param delSqls     删除SQL集合
     */
    @Override public void deleteNoExistProData(Table table, List<Integer> protocolids, Map<String, HashMap<Integer, Exception>> errInfos, List<String> delSqls) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        builder.append("delete from " + table.getSchemaName() + "." + table.getTableName() + " where protocolid in ( ");
        protocolids.forEach(protocolid -> builder.append(protocolid + ","));
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        //将删除语句添加到 删除记录对象中
        delSqls.add(builder.toString());
    }

    /**
     * 比对两个行数据MAP
     *
     * @param aMap sqlitemap
     * @param bMap dbmap
     * @return 不同项, 需要update的列信息
     */
    private Map<String, String> compareLineData(Map<String, String> aMap, Map<String, String> bMap)
    {
        Map<String, String> updateMap = new HashMap<>();
        for (Map.Entry<String, String> entry : aMap.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            String bMapValue = bMap.get(key);
            if ((value != null && bMapValue != null && !value.equals(bMapValue)) || ((value == null && bMapValue != null) || (value != null && bMapValue == null)))
            {
                updateMap.put(key, value);
            }
        }
        return updateMap;
    }

    /**
     * 根据主键生成删除MAP
     *
     * @param linData 行数据
     * @param table   表对象
     * @return 删除MAP
     */
    private Map<String, String> createDelDataMap(Map.Entry<String, Map<String, String>> linData, Table table)
    {
        Map<String, String> delMap = new HashMap<>();
        String pkey = linData.getKey();
        Map<String, String> lineValue = linData.getValue();
        String[] pkeys = pkey.split("@sep@", -1);
        if (table.getPks().isEmpty())
        {
            //如果不存在主键,按全字段拼接map
            Iterator<Map.Entry<String, String>> entryIterator = lineValue.entrySet().iterator();
            while (entryIterator.hasNext())
            {
                Map.Entry<String, String> entry = entryIterator.next();
                delMap.put(entry.getKey(), entry.getValue());
            }
        }
        else
        {
            //如果存在主键,按主键拼接map
            for (int i = 0; i < table.getPks().size(); i++)
            {
                Pk pk = table.getPks().get(i);
                delMap.put(pk.getColName(), pkeys[i]);
            }
        }
        return delMap;
    }

    /**
     * 判断一个double是否为int
     *
     * @param d 传入double值
     * @return true:是int;false:不是int
     */
    private boolean judgeDoubleIsInt(double d)
    {
        double eps = 1e-10;
        return d - Math.floor(d) < eps;
    }

    /**
     * 字符串去除末尾的0
     *
     * @param data double数(数值不为int)
     * @return 处理过的double String
     */
    private String delTailZero(String data)
    {
        while (data.contains(".") && data.endsWith("0"))
        {
            data = data.substring(0, data.length() - 1);
        }
        return data;
    }

    /**
     * oid数值处理
     *
     * @param oidStrData oid字符串
     * @return oid的应用id
     */
    private String oidHandle(String oidStrData)
    {
        DbOperBase dbOperBase = new DbOperBase(DbAssistant.DatabaseType.Postgre, com.goldwind.dbdatasyn.utils.RunParams.getDatabase_url(), com.goldwind.dbdatasyn.utils.RunParams.getUsername(),
                com.goldwind.dbdatasyn.utils.RunParams.getPassword());
        LargeObject obj;
        ByteArrayInputStream bInput;
        Connection connection;
        Long oid;
        if (oidStrData == null)
        {
            //如果传入字符串为null,直接返回null
            return null;
        }
        else if (oidStrData.startsWith("@oidsep@"))
        {
            //如果oid数据包含oid字符串占位符,去除占位符
            oidStrData = oidStrData.substring(8);
        }
        try
        {
            dbOperBase.openConn();
            connection = dbOperBase.getConn();
            //将oid数据插入pg_largeobject表
            byte[] bytes = oidStrData.getBytes("utf-8");
            connection.setAutoCommit(false);
            LargeObjectManager lobj = ((org.postgresql.PGConnection) connection).getLargeObjectAPI();
            //自动生成oid数值
            oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            obj = lobj.open(oid, LargeObjectManager.WRITE);
            byte[] buf = new byte[2048];
            bInput = new ByteArrayInputStream(bytes);
            int n;
            while ((n = bInput.read(buf, 0, 2048)) > 0)
            {
                obj.write(buf, 0, n);
            }
            obj.close();
            bInput.close();
            connection.setAutoCommit(true);
        }
        catch (Exception e)
        {
            oid = null;
        }
        finally
        {
            try
            {
                dbOperBase.closeConn();
            }
            catch (Exception e)
            {

            }
        }
        return oid != null ? String.valueOf(oid) : null;
    }
}
