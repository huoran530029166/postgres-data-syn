package com.goldwind.dbdatasyn.db2sqlite.service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.DataAsExpSet;

import com.goldwind.dbdatasyn.db2sqlite.init.Init;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Col;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Participation;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Pk;
import com.goldwind.dbdatasyn.db2sqlite.pojo.Table;
import com.goldwind.dbdatasyn.db2sqlite.service.serviceinterface.IConvert;
import com.goldwind.dbdatasyn.db2sqlite.utils.Enums;
import com.goldwind.dbdatasyn.pojo.DbConInfo;
import com.goldwind.dbdatasyn.utils.RunParams;

/**
 * 初始化 转换 业务类
 *
 * @author huoran
 */
public class PgConvertService implements IConvert
{
    /**
     * 模式、表入参转换方法
     *
     * @param participations 模式、表入参对象
     * @throws Exception 异常
     */
    public void participationsConvert(List<Participation> participations) throws Exception
    {
        com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.setParticipations(participations);
        //生成需要同步的表名、模式名
        createTables();
        //根据需要同步的表名生成表主键信息集合
        createPKs();
        //根据表的主键信息集合生成主键是否包含协议字段标识
        createPkProFlags();
        //加载数据库表所有的协议号数值
        createTableProtocolids();
        //数据库表加载列信息
        createCols();
    }

    /**
     * 根据模式、表名入参生成需要同步的数据库表对象(包含模式名、表名)
     *
     * @throws Exception 异常
     */
    public void createTables() throws Exception
    {
        List<Table> tables = new ArrayList<>();
        com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.setTables(tables);

        for (Participation participation : com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getParticipations())
        {
            if (participation.getType() == Enums.ParticipationType.table)
            {
                //如果入参是表对象,直接传入模式名和表名
                String[] names = participation.getName().split("@separator@", -1);
                Table table = new Table();
                table.setSchemaName(names[0]);
                table.setTableName(names[1]);
                com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables().add(table);
            }
            else if (participation.getType() == Enums.ParticipationType.schema)
            {
                //如果入参是模式对象,查询该模式下所有表,依次传入模式名和表名
                try
                {
                    Init.dbOperBase.openConn();
                    StringBuilder builder = new StringBuilder();
                    builder.append("SELECT table_name FROM information_schema.tables WHERE table_schema=?");
                    ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), new Object[] { participation.getName() });
                    while (rs.next())
                    {
                        Table table = new Table();
                        table.setSchemaName(participation.getName());
                        table.setTableName(rs.getString(1));
                        com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables().add(table);
                    }
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("根据入参模式查询数据库表发生异常", new String[] { "schemaName" }, new Object[] { participation.getName() }, e);
                }
                finally
                {
                    try
                    {
                        Init.dbOperBase.closeConn();
                    }
                    catch (Exception e)
                    {
                        DataAsExpSet.throwExpByMsg("根据入参模式查询数据库表发生异常", new String[] { "schemaName" }, new Object[] { participation.getName() }, e);
                    }
                }
            }
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

            for (Table table : com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables())
            {
                String key = table.getSchemaName() + "." + table.getTableName();
                table.setPks(pkMap.get(key));
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
        for (Table table : com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables())
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
        for (Table table : com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables())
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
                        int protocolid = rs.getInt(1);
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
     * 数据库表加载列信息
     *
     * @throws Exception 异常
     */
    public void createCols() throws Exception
    {
        for (Table table : com.goldwind.dbdatasyn.db2sqlite.utils.RunParams.getTables())
        {
            StringBuilder builder = new StringBuilder();
            builder.append("select table_schema,table_name,column_name,udt_name from information_schema.columns ");
            builder.append("where table_schema='" + table.getSchemaName() + "' and table_name='" + table.getTableName() + "' ");
            builder.append("order by table_schema,table_name,ordinal_position ");

            try
            {
                Init.dbOperBase.openConn();
                ResultSet rs = Init.dbOperBase.getResultSet(builder.toString(), null);
                List<Col> cols = new ArrayList<>();
                while (rs.next())
                {
                    Col col = new Col();
                    col.setSchemaName(rs.getString(1));
                    col.setTableName(rs.getString(2));
                    col.setColName(rs.getString(3));
                    col.setColType(rs.getString(4));

                    cols.add(col);
                }
                table.setCols(cols);
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByMsg("数据库表加载列信息发生异常", null, null, e);
            }
            finally
            {
                try
                {
                    Init.dbOperBase.closeConn();
                }
                catch (Exception e)
                {
                    DataAsExpSet.throwExpByMsg("数据库表加载列信息发生异常", null, null, e);
                }
            }
        }
    }

    /**
     * 关系库连接信息入参转换方法
     *
     * @param dbConInfo 关系库连接信息
     */
    public void dbConInfoConvert(DbConInfo dbConInfo)
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
     * 本地库入参转换方法
     *
     * @param sqlitePath 本地库地址
     */
    public void sqlitePathConvert(String sqlitePath)
    {
        RunParams.setSqlitePath(sqlitePath);
    }
}
