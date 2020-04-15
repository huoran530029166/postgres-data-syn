package com.goldwind.dbdatasyn.sqlite2db.service;

import java.sql.ResultSet;
import java.util.*;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dbdatasyn.pojo.DbConInfo;
import com.goldwind.dbdatasyn.sqlite2db.init.Init;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Col;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Pk;
import com.goldwind.dbdatasyn.sqlite2db.pojo.SynResult;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Table;
import com.goldwind.dbdatasyn.sqlite2db.service.serviceinterface.IConvert;
import com.goldwind.dbdatasyn.sqlite2db.utils.Enums;
import com.goldwind.dbdatasyn.sqlite2db.utils.Func;
import com.goldwind.dbdatasyn.utils.RunParams;

/**
 * 本地库同步关系库 PG库初始化参数转换业务类
 *
 * @author huoran
 */
public class PgConvertService implements IConvert
{
    /**
     * PG关系型数据库 外键约束集合(主表-子表集合)
     */
    private Map<String, List<String>> fkMains = new HashMap<>();
    /**
     * PG关系型数据库 外键约束集合(子表-主表集合)
     */
    private Map<String, List<String>> fkChilds = new HashMap<>();

    /**
     * 关系型数据库连接信息转换方法
     *
     * @param dbConInfo 关系型数据库连接信息对象
     */
    @Override public void dbConInfoConvert(DbConInfo dbConInfo)
    {
        RunParams.setDatabase_url(dbConInfo.getDatabase_url());
        RunParams.setUsername(dbConInfo.getUsername());
        RunParams.setPassword(dbConInfo.getPassword());
        RunParams.setMax_pool_size(dbConInfo.getMax_pool_size());
        RunParams.setMin_pool_size(dbConInfo.getMin_pool_size());
        RunParams.setDb_outtime(dbConInfo.getDb_outtime());
        RunParams.setMax_idletime(dbConInfo.getMax_idletime());
    }

    /**
     * 本地库地址转换方法
     *
     * @param sqlitePath 本地库地址
     */
    @Override public void sqlitePathConvert(String sqlitePath)
    {
        RunParams.setSqlitePath(sqlitePath);
    }

    /**
     * 本地库异常文件地址转换方法
     *
     * @param sqliteErrPath 本地库异常日志
     */
    @Override public void sqliteErrPathConvert(String sqliteErrPath)
    {
        com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.setSqliteErrPath(sqliteErrPath);
    }

    /**
     * 删除标志转换方法
     *
     * @param delFlag 删除标志
     */
    @Override public void delFlagConvert(boolean delFlag)
    {
        com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.setDelFalg(delFlag);
    }

    /**
     * 根据本地库需同步表名集合创建关系型数据库中可以同步的表名对象集合
     *
     * @throws Exception 异常
     */
    @Override public void createAliveTables() throws Exception
    {
        try
        {
            //查询本地库中tablenames表中的所有数据库表名集合
            Init.sqLiteOper.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("select schemaname,tablename from tablenames");
            ResultSet rs = Init.sqLiteOper.getResultSet(builder.toString(), null);
            List<String> sqliteTableNames = new ArrayList<>();
            while (rs.next())
            {
                String schemaName = rs.getString(1);
                String tableName = rs.getString(2);
                sqliteTableNames.add(schemaName + "@sep@" + tableName);
            }
            Init.sqLiteOper.closeConn();

            //查询关系型数据库中所有的自定义表名集合(不包含系统表)
            Init.dbOperBase.openConn();
            StringBuilder pgbuilder = new StringBuilder();
            pgbuilder.append("SELECT table_schema,table_name FROM information_schema.tables ");
            pgbuilder.append("where table_schema not in ('pg_catalog','information_schema') ");
            ResultSet pgrs = Init.dbOperBase.getResultSet(pgbuilder.toString(), null);
            List<String> pgTableNames = new ArrayList<>();
            while (pgrs.next())
            {
                String schemaName = pgrs.getString(1);
                String tableName = pgrs.getString(2);
                pgTableNames.add(schemaName + "@sep@" + tableName);
            }
            Init.dbOperBase.closeConn();

            //取两个表名集合的交集作为需要同步数据的表名集合
            sqliteTableNames.retainAll(pgTableNames);
            com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.setTableSepNames(sqliteTableNames);

            //赋值到本地库同步关系库数据项目模块的运行参数类中
            for (String tableName : sqliteTableNames)
            {
                String[] tableObjs = tableName.split("@sep@", -1);
                Table table = new Table();
                table.setSchemaName(tableObjs[0]);
                table.setTableName(tableObjs[1]);
                com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables().add(table);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByMsg("根据本地库tablenames加载Table对象集合发生异常", null, null, e);
        }
        finally
        {
            Init.sqLiteOper.closeConn();
            Init.dbOperBase.closeConn();
        }
    }

    /**
     * 校验表对象外键关系
     *
     * @throws Exception 异常
     */
    @Override public void checkFkTableInfo() throws Exception
    {
        //外键约束集合(主表-子表集合)
        Map<String, List<String>> fkMains = new HashMap<>();
        //外键约束集合(子表-主表集合)
        Map<String, List<String>> fkChilds = new HashMap<>();
        try
        {
            Init.dbOperBase.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("select ");
            builder.append("c.nspname as tableNsp,b.relname as tableName, ");
            builder.append("e.nspname as pTableNsp,d.relname as pTableName ");
            builder.append("from pg_constraint a ");
            builder.append("left join pg_class b on a.conrelid = b.oid ");
            builder.append("left join pg_namespace c on a.connamespace = c.oid ");
            builder.append("left join pg_class d on a.confrelid = d.oid ");
            builder.append("left join pg_namespace e on d.relnamespace = e.oid ");
            builder.append("where a.contype = 'f' ");

            ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), null);
            while (rs.next())
            {
                String tablensp = rs.getString(1);
                String tablename = rs.getString(2);
                String ptablensp = rs.getString(3);
                String ptablename = rs.getString(4);

                String ptable = ptablensp + "@sep@" + ptablename;
                String ftable = tablensp + "@sep@" + tablename;
                //赋值外键约束集合(主表-子表集合)
                if (fkMains.containsKey(ptable))
                {
                    fkMains.get(ptable).add(ftable);
                }
                else
                {
                    List<String> fTableList = new ArrayList<>();
                    fTableList.add(ftable);
                    fkMains.put(ptable, fTableList);
                }

                //赋值外键约束集合(子表-主表集合)
                if (fkChilds.containsKey(ftable))
                {
                    fkChilds.get(ftable).add(ptable);
                }
                else
                {
                    List<String> pTableList = new ArrayList<>();
                    pTableList.add(ptable);
                    fkChilds.put(ftable, pTableList);
                }
            }
            Init.dbOperBase.closeConn();
            //赋值业务类全局外键约束变量(主表-子表集合)
            this.fkMains = fkMains;
            //赋值业务类全局外键约束变量(子表-主表集合)
            this.fkChilds = fkChilds;

            for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
            {
                String key = table.getSchemaName() + "@sep@" + table.getTableName();
                if (fkMains.containsKey(key))
                {
                    //判断该表是否为外键主表
                    table.setFkMainTable(true);
                    //如果为外键主表,添加该表对于的外键子表信息
                    for (String fkChild : fkMains.get(key))
                    {
                        String[] tableObjs = fkChild.split("@sep@", -1);
                        Map<String, String> fkMap = new HashMap<>();
                        fkMap.put("schemaName", tableObjs[0]);
                        fkMap.put("tableName", tableObjs[1]);
                        table.getFkChildTables().add(fkMap);
                    }
                }
            }

            for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
            {
                String key = table.getSchemaName() + "@sep@" + table.getTableName();
                if (fkChilds.containsKey(key))
                {
                    //判断该表是否为外键子表
                    table.setFkChildTable(true);
                    //如果为外键子表,添加该表对于的外键主表信息
                    for (String fkMain : fkChilds.get(key))
                    {
                        String[] tableObjs = fkMain.split("@sep@", -1);
                        Map<String, String> fkMap = new HashMap<>();
                        fkMap.put("schemaName", tableObjs[0]);
                        fkMap.put("tableName", tableObjs[1]);
                        table.getFkMainTables().add(fkMap);
                        table.getSortFkMains().add(fkMap);
                    }
                }
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByMsg("校验表对象外键关系发生异常", null, null, e);
        }
        finally
        {
            Init.dbOperBase.closeConn();
        }
    }

    /**
     * 校验表对象是否缺失外键主表
     *
     * @return 校验结果
     */
    @Override public SynResult checkFkMainMiss()
    {
        //默认同步结果为完全同步
        SynResult result = new SynResult();
        result.setResult(Enums.SynResultEnum.COMPLETELY);

        Set<String> fkMainTableNames = new HashSet<>();
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            String key = table.getSchemaName() + "@sep@" + table.getTableName();
            fkMainTableNames.addAll(this.fkChilds.containsKey(key) ? this.fkChilds.get(key) : new HashSet<>());
        }
        fkMainTableNames.removeAll(com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTableSepNames());
        if (!fkMainTableNames.isEmpty())
        {
            //如果缺失外键主表,同步结果为同步失败
            result.setResult(Enums.SynResultEnum.FAIL);
            result.setMsg("校验表对象失败,同步的表信息中缺失外键主表。");
        }
        return result;
    }

    /**
     * 校验表对象是否缺失外键子表标致
     */
    @Override public SynResult checkFkChildMissFlag()
    {
        SynResult result = new SynResult();
        result.setResult(Enums.SynResultEnum.COMPLETELY);

        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            Set<String> fkChildTableNames = new HashSet<>();
            String tableSepName = table.getTableSepName();
            fkChildTableNames.addAll(this.fkMains.containsKey(tableSepName) ? fkMains.get(tableSepName) : new HashSet<>());
            fkChildTableNames.removeAll(com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTableSepNames());
            if (!fkChildTableNames.isEmpty())
            {
                //如果表缺失外键子表,同步结果为部分同步,该表对象缺失外键子表标志设为true
                result.setResult(Enums.SynResultEnum.PART);
                table.setFkChildMissFlag(true);
            }
        }
        return result;
    }

    /**
     * 根据外键关系顺序对需要同步的表对象集合进行排序
     */
    @Override public void sortTables()
    {
        List<String> tableSepNamesCopy = new ArrayList<>(com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTableSepNames());
        //对table对象赋值排序数值 未排序数值为0,排序数值从1开始
        Func.sortTables(1, com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables(), tableSepNamesCopy);
        //根据排序数值大小进行排序
        Collections.sort(com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables());
    }

    /**
     * 创建表对象的关系型数据库列信息
     */
    @Override public void createDbCols() throws Exception
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("select table_schema,table_name,column_name,udt_name from information_schema.columns ");
            builder.append("where table_schema='" + table.getSchemaName() + "' and table_name='" + table.getTableName() + "' ");
            builder.append("order by table_schema,table_name,ordinal_position ");

            try
            {
                Init.dbOperBase.openConn();
                ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), null);
                LinkedHashMap<String, Col> cols = new LinkedHashMap<>();
                while (rs.next())
                {
                    Col col = new Col();
                    col.setSchemaName(rs.getString(1));
                    col.setTableName(rs.getString(2));
                    col.setColName(rs.getString(3));
                    col.setColType(rs.getString(4));

                    cols.put(col.getColName(), col);
                }
                table.setColDbMap(cols);
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByMsg("关系型数据库表加载列信息发生异常", null, null, e);
            }
            finally
            {
                try
                {
                    Init.dbOperBase.closeConn();
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("关系型数据库表加载列信息发生异常", null, null, e);
                }
            }
        }
    }

    /**
     * 创建表对象的本地库列信息
     *
     * @throws Exception 异常
     */
    @Override public void createSqliteCols() throws Exception
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("PRAGMA table_info(" + table.getTableName() + ")");
            try
            {
                Init.sqLiteOper.openConn();
                ResultSet rs = Init.sqLiteOper.getResultSet(builder.toString(), null);
                LinkedHashMap<String, Col> cols = new LinkedHashMap<>();
                while (rs.next())
                {
                    Col col = new Col();
                    col.setSchemaName(table.getSchemaName());
                    col.setTableName(table.getTableName());
                    col.setColName(rs.getString("name"));
                    col.setColType(rs.getString("type"));
                    cols.put(col.getColName(), col);
                }
                table.setColSqliteMap(cols);
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByMsg("本地库表加载列信息发生异常", null, null, e);
            }
            finally
            {
                try
                {
                    Init.sqLiteOper.closeConn();
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("本地库表加载列信息发生异常", null, null, e);
                }
            }
        }
    }

    /**
     * 比对关系型数据库和本地库列信息,生成列执行类型属性
     */
    @Override public void compareCols()
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            //关系型数据库单表存在的列名
            List<String> dbColNames = new ArrayList<>();
            //本地库单表存在的列名
            List<String> sqliteColNames = new ArrayList<>();

            table.getColDbMap().entrySet().forEach(stringColEntry -> dbColNames.add(stringColEntry.getKey()));
            table.getColSqliteMap().entrySet().forEach(stringColEntry -> sqliteColNames.add(stringColEntry.getKey()));

            //关系库与本地库都存在的列,保留
            List<String> retainNames = new ArrayList<>(dbColNames);
            retainNames.retainAll(sqliteColNames);
            retainNames.forEach(tableName -> table.getColSqliteMap().get(tableName).setColExcuteType(Enums.ColExcuteType.RETAIN));

            //关系库存在本地库不存在得列,补足
            List<String> addedNames = new ArrayList<>(dbColNames);
            addedNames.removeAll(retainNames);
            addedNames.forEach(tableName -> {
                Col col = new Col();
                col.setSchemaName(table.getSchemaName());
                col.setTableName(table.getTableName());
                col.setColName(tableName);
                col.setColType(null);
                col.setColExcuteType(Enums.ColExcuteType.ADDED);
                table.getColSqliteMap().put(tableName, col);
            });

            //关系库不存在本地库存在的列,删除
            List<String> deleteNames = new ArrayList<>(sqliteColNames);
            deleteNames.removeAll(retainNames);
            deleteNames.forEach(tableName -> table.getColSqliteMap().get(tableName).setColExcuteType(Enums.ColExcuteType.DELETE));

        }
    }

    /**
     * 根据需要同步的数据库表名加载表主键信息集合
     *
     * @throws Exception 异常
     */
    public void createPKs() throws Exception
    {
        Map<String, List<Pk>> pkMap = new HashMap<>();

        try
        {
            Init.dbOperBase.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("select pg_namespace.nspname,pg_class.relname,pg_constraint.conname as pk_name,pg_attribute.attname as colname,pg_type.typname as typename from ");
            builder.append("pg_constraint  inner join pg_class on pg_constraint.conrelid = pg_class.oid ");
            builder.append("inner join pg_attribute on pg_attribute.attrelid = pg_class.oid ");
            builder.append("and  pg_attribute.attnum = any(pg_constraint.conkey) ");
            builder.append("inner join pg_type on pg_type.oid = pg_attribute.atttypid ");
            builder.append("inner join pg_namespace on pg_namespace.oid = pg_class.relnamespace ");
            builder.append("where pg_constraint.contype='p' ");
            builder.append("order by pg_namespace.nspname,pg_class.relname,pg_constraint.conname,pg_attribute.attnum ");

            ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), null);
            while (rs.next())
            {
                Pk pk = new Pk();
                pk.setSchemaName(rs.getString(1));
                pk.setTableName(rs.getString(2));
                pk.setPkName(rs.getString(3));
                pk.setColName(rs.getString(4));
                pk.setColType(rs.getString(5));

                String tableKey = pk.getSchemaName() + "." + pk.getTableName();
                if (pkMap.get(tableKey) == null)
                {
                    pkMap.put(tableKey, new ArrayList<>());
                }
                pkMap.get(tableKey).add(pk);
            }

            for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
            {
                String key = table.getSchemaName() + "." + table.getTableName();
                table.setPks(pkMap.get(key) != null ? pkMap.get(key) : new ArrayList<>());
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByMsg("根据数据库表名加载表主键发生异常", null, null, e);
        }
        finally
        {
            try
            {
                Init.dbOperBase.closeConn();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByMsg("根据数据库表名加载表主键发生异常", null, null, e);
            }
        }
    }

    /**
     * 创建表主键是否包含协议的标识字段
     */
    public void createPkProFlags()
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            table.setPkProFlag(false);
            if (table.getPks() != null)
            {
                for (Pk pk : table.getPks())
                {
                    if ("protocolid".equalsIgnoreCase(pk.getColName()))
                    {
                        table.setPkProFlag(true);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 加载数据库表所有的协议号数值
     *
     * @throws Exception 异常
     */
    public void createTableProtocolids() throws Exception
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            if (table.isPkProFlag())
            {
                StringBuilder builder = new StringBuilder();
                builder.append("select DISTINCT protocolid from ");
                builder.append(table.getSchemaName() + "." + table.getTableName() + " ");
                builder.append(" order by protocolid ");

                try
                {
                    Init.dbOperBase.openConn();
                    ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), null);
                    List<Integer> proIds = new ArrayList<>();
                    while (rs.next())
                    {
                        String protocolidstr = rs.getString(1);
                        Integer protocolid;
                        if (protocolidstr == null)
                        {
                            protocolid = null;
                        }
                        else
                        {
                            protocolid = Integer.valueOf(protocolidstr);
                        }
                        proIds.add(protocolid);
                    }
                    table.setProtocolids(proIds);
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("根据数据库是否包含协议号标识加载表protocolid数值发生异常", null, null, e);
                }
                finally
                {
                    try
                    {
                        Init.dbOperBase.closeConn();
                    }
                    catch (Exception e)
                    {
                        DataAsExpSet.throwExpByMsg("根据数据库是否包含协议号标识加载表protocolid数值发生异常", null, null, e);
                    }
                }
            }
        }
    }

    /**
     * 加载本地库表所有的协议号数值
     *
     * @throws Exception 异常
     */
    public void createTableProtocolidsSqlite() throws Exception
    {
        for (Table table : com.goldwind.dbdatasyn.sqlite2db.utils.RunParams.getTables())
        {
            if (table.isPkProFlag())
            {
                StringBuilder builder = new StringBuilder();
                builder.append("select DISTINCT protocolid from ");
                builder.append(table.getTableName() + " ");
                builder.append(" order by protocolid ");

                try
                {
                    Init.sqLiteOper.openConn();
                    ResultSet rs = Init.sqLiteOper.getResultSet(builder.toString(), null);
                    List<Integer> proIds = new ArrayList<>();
                    while (rs.next())
                    {
                        String protocolidstr = rs.getString(1);
                        Integer protocolid;
                        if (protocolidstr == null)
                        {
                            protocolid = null;
                        }
                        else
                        {
                            protocolid = Integer.valueOf(protocolidstr);
                        }
                        proIds.add(protocolid);
                    }
                    table.setProtocolidsSqlite(proIds);
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("根据本地库是否包含协议号标识加载表protocolid数值发生异常", null, null, e);
                }
                finally
                {
                    try
                    {
                        Init.sqLiteOper.closeConn();
                    }
                    catch (Exception e)
                    {
                        DataAsExpSet.throwExpByMsg("根据本地库是否包含协议号标识加载表protocolid数值发生异常", null, null, e);
                    }
                }
            }
        }
    }
}
