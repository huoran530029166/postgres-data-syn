package com.goldwind.dbdatasyn.sqlite2db.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.util.*;

import com.goldwind.dbdatasyn.sqlite2db.init.Init;
import com.goldwind.dbdatasyn.sqlite2db.pojo.Table;

public class Func
{
    /**
     * 对table对象赋值排序数值 未排序数值为0,排序数值从1开始
     *
     * @param i             排序数值最小值
     * @param tables        待排序的表对象集合(全集)
     * @param tableSepNames 所有尚未且需要排序的表Sep名集合
     */
    public static void sortTables(int i, List<Table> tables, List<String> tableSepNames)
    {
        //本次排序完成的表名集合,在本次排序完成后从tableSepNames中删除
        List<String> delNamesList = new ArrayList<>();
        for (Table table : tables)
        {
            //表对象所关联的外键主表集合
            List<Map<String, String>> fkMains = table.getSortFkMains();
            //需要删除的在tableSepNames中不存在的本表对象关联的外键主表集合
            List<Map<String, String>> delList = new ArrayList<>();
            if (fkMains != null)
            {
                for (Map<String, String> fkMain : fkMains)
                {
                    String schemaName = fkMain.get("schemaName");
                    String tableName = fkMain.get("tableName");
                    if (!tableSepNames.contains(schemaName + "@sep@" + tableName))
                    {
                        delList.add(fkMain);
                    }
                }
                //将表对象关联的外键主表中删除在tableSepNames中不存在的项
                fkMains.removeAll(delList);
            }

            //如果该表对象未经历排序赋值(排序数值)且当前排序过程中不存在未排序的外键主表,则本次进行排序赋值
            if (table.getSortNum() == 0 && (fkMains == null || fkMains.isEmpty()))
            {
                table.setSortNum(i);
                //排序赋值完成后,加入tableSepNames待删除集合
                delNamesList.add(table.getSchemaName() + "@sep@" + table.getTableName());
            }
        }
        //删除已完成排序的表名信息
        tableSepNames.removeAll(delNamesList);
        //如果剩余未排序表,则进行下一次排序(下一次排序数值+1)
        if (!tableSepNames.isEmpty())
        {
            i++;
            sortTables(i, tables, tableSepNames);
        }
    }

    /**
     * 将错误信息记录到本地库异常文件
     *
     * @param errs 错误信息
     */
    public static void LogSqliteErrs(Map<String, HashMap<Integer, Exception>> errs, Table table)
    {
        try
        {
            boolean tableExistFlag = false;
            Init.sqLiteErrOper.openConn();
            StringBuilder judgeBuilder = new StringBuilder();
            judgeBuilder.append("select * from sqlite_master where type = 'table' and name = \'" + table.getTableName() + "\'");
            ResultSet judgeRs = Init.sqLiteErrOper.getResultSet(judgeBuilder.toString(), null);
            if (judgeRs.next())
            {
                tableExistFlag = true;
            }

            if (!tableExistFlag && !errs.isEmpty())
            {
                //如果不存在该表,在本地库中创建该表
                StringBuilder tableBuilder = new StringBuilder();
                tableBuilder.append("create table " + table.getTableName() + " ( ");
                tableBuilder.append("\"group\" varchar , \"lineid\" int , \"exception\" varchar );");
                Init.sqLiteErrOper.excute(tableBuilder.toString(), null);
            }

            List<String> sqls = new ArrayList<>();
            for (Map.Entry<String, HashMap<Integer, Exception>> entry : errs.entrySet())
            {
                //组
                String group = entry.getKey();
                for (Map.Entry<Integer, Exception> errEntry : entry.getValue().entrySet())
                {
                    //行
                    Integer lineid = errEntry.getKey();
                    Exception e = errEntry.getValue();
                    Writer result = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(result);
                    e.fillInStackTrace().printStackTrace(printWriter);
                    //异常
                    String exception = result.toString();

                    StringBuilder builder = new StringBuilder();
                    builder.append("insert into " + table.getTableName() + " values(");
                    builder.append(group == null ? "null" : "\'" + group + "\',");
                    builder.append(lineid == null ? "null" : lineid + ",");
                    builder.append("\'" + exception + "\')");
                    sqls.add(builder.toString());
                }
            }
            Init.sqLiteErrOper.excuteBatchSqlIgnorErr(sqls.toArray(new String[sqls.size()]));
        }
        catch (Exception e)
        {
            System.out.println("LogSqliteErrs fail");
        }
        finally
        {
            try
            {
                Init.sqLiteErrOper.closeConn();
            }
            catch (Exception e)
            {

            }
        }
    }
}
