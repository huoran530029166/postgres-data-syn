package com.goldwind.dataaccess.database;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.ResourceFileFactory;
import com.goldwind.dataaccess.StringBufferUtil;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.exception.NullException;

/**
 * @author 曹阳
 */
public class DbAssistant
{
    /**
     * 结束符
     */
    private static String[] sqlEndFlag = new String[] { ")", " union ", " intersect ", " except " };

    /**
     * 结束索引
     */
    private static int endIndex;
    /**
     * 行号
     */
    private static String rowNum;
    /**
     * "config."常量
     */
    private static String CONFIGSTR = "config.";

    /**
     * "custom."常量
     */
    private static String CUSTOMSTR = "custom.";

    public enum DatabaseType
    {
        /**
         * 0:sqlserver;1:oracle;2:posrgre;3:sqlite;4:access;5:mysql
         */
        Oracle(1), Postgre(2), SQLite(3), Access(4), MySql(5), SqlServer(6), Highgo(7), Sgcc(8), Golden(9), Bigdata(10);

        /**
         * 枚举的值
         */
        private int value = 0;

        public int getValue()
        {
            return value;
        }

        DatabaseType(int value)
        {
            this.value = value;
        }

    }

    /**
     * 根据数据库类型设置字段分隔符
     *
     * @param dbType 数据库类型
     * @return 开始符, 结束符
     */
    private static HashMap<String, String> getDbNameSplitSymbol(DatabaseType dbType)
    {
        HashMap<String, String> returnData = new HashMap<>();
        String beginSymbol = "";
        String endSymbol = "";

        switch (dbType)
        {
            case SQLite:
            case Oracle:
            case SqlServer:
                break;
            case Postgre:
                beginSymbol = "\"";
                endSymbol = beginSymbol;
                break;
            case MySql:
                beginSymbol = "`";
                endSymbol = beginSymbol;
                break;
            case Access:
                beginSymbol = "[";
                endSymbol = "]";
                break;
            default:
                break;
        }
        returnData.put("beginSymbol", beginSymbol);
        returnData.put("endSymbol", endSymbol);
        return returnData;
    }

    /**
     * 根据数据库类型设置字段分隔符
     *
     * @param objectName 字段
     * @param dbType     数据库类型
     * @return 增加分隔符的字段
     */
    public static String convertObjectByDb(String objectName, DatabaseType dbType)
    {
        HashMap<String, String> symbol = getDbNameSplitSymbol(dbType);
        return symbol.get("beginSymbol") + objectName + symbol.get("endSymbol");
    }

    /**
     * 将标准sql语句转换为特定数据库类型的sql语句
     *
     * @param srcSql     sql语句
     * @param destDbType 数据库类型
     * @return 格式化后的sql语句
     * @throws DataAsException 自定义异常
     */
    public static String convertSqlByDb(String srcSql, DatabaseType destDbType) throws DataAsException
    {
        String val = "";
        if (srcSql != null && !srcSql.isEmpty())
        {
            StringBuffer srcSqlBuff = new StringBuffer(srcSql);

            try
            {
                switch (destDbType)
                {
                    case Oracle:
                        break;
                    case Postgre:
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "isnull(", "coalesce(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "dbo.", "public.");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "getdate()", "current_timestamp");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "[offsets]", "offsets");
                        // srcSql = srcSql.replace("isnull(", "coalesce(").replace("dbo.", "public.").replace("getdate()", "current_timestamp").replace("[offsets]", "offsets");
                        val = converSqlTopToLimit(srcSqlBuff.toString());
                        break;
                    case SqlServer:
                        val = srcSql;
                        break;
                    case SQLite:
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "isnull(", "ifnull(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "dbo.", "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "getdate()", "datetime('now','+8 hour')");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "[offsets]", "offsets");
                        // srcSql = srcSql.replace("isnull(", "ifnull(").replace("dbo.", "").replace("getdate()", "datetime('now','+8 hour')").replace("[offsets]", "offsets");
                        val = converSqlTopToLimit(srcSqlBuff.toString());
                        srcSqlBuff = new StringBuffer(val);
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "str_to_date(", "datetime(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CONFIGSTR, "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CUSTOMSTR, "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "platform.", "");
                        val = srcSqlBuff.toString();
                        // val = val.replace("str_to_date(", "datetime(");
                        // val = val.replace("config.", "").replace("custom.", "");
                        break;
                    case Access:
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "isnull(", "iif(isnull(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "dbo.", "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "getdate()", "now()");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "[offsets]", "offsets");
                        val = converSqlTopToLimit(srcSqlBuff.toString());
                        srcSqlBuff = new StringBuffer(val);
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "str_to_date(", "cdate(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CONFIGSTR, "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CUSTOMSTR, "");
                        val = srcSqlBuff.toString();
                        break;
                    case MySql:
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "isnull(", "ifnull(");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "dbo.", "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "getdate()", "now()");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "[offsets]", "offsets");
                        val = converSqlTopToLimit(srcSqlBuff.toString());
                        srcSqlBuff = new StringBuffer(val);
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CONFIGSTR, "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, CUSTOMSTR, "");
                        StringBufferUtil.sbReplaceAll(srcSqlBuff, "platform.", "");
                        val = srcSqlBuff.toString();
                        break;
                    default:
                        val = srcSql;
                        break;
                }
            }
            catch (Exception exp)
            {
                DataAsExpSet.throwExpByResCode("DbAssistant_convertSqlByDb_1", new String[] { "sql" }, new Object[] { srcSql }, exp);
            }
        }
        return val;
    }

    /**
     * 得到指定数据库模式
     *
     * @param dbType 数据库类型
     * @return 模式名, 默认返回public
     */
    public static String getRealSchema(DatabaseType dbType)
    {
        switch (dbType)
        {
            case Oracle:
                return "scada";
            case Postgre:
                return "public";
            case SqlServer:
                return "dbo";
            default:
                return "public";
        }
    }

    /**
     * 将数据表转换为插入的sql语句
     *
     * @param tableName 插入的数据表名
     * @param dt        插入的数据
     * @param params    数据库连接信息（url、username、password）
     * @return 插入的sql语句
     * @throws DataAsException 自定义异常
     * @throws SQLException    数据库异常
     */
    public static String[] tableToInsSql(String tableName, ResultSet dt, String... params) throws DataAsException, SQLException
    {
        if (dt == null)
        {
            return new String[0];
        }

        List<String> val = new ArrayList<>();
        try
        {
            ResultSetMetaData rmeta = dt.getMetaData();

            StringBuilder sql = new StringBuilder("insert into " + tableName + "(");
            for (int i = 0; i < rmeta.getColumnCount(); i++)
            {
                if (rmeta.getColumnName(i + 1).indexOf('.', 0) > -1)
                {
                    sql.append("'");
                    sql.append(rmeta.getColumnName(i + 1));
                    sql.append("'");

                    if (i != rmeta.getColumnCount() - 1)
                    {
                        sql.append(",");
                    }
                }
                else
                {
                    sql.append(rmeta.getColumnName(i + 1));
                    if (i != rmeta.getColumnCount() - 1)
                    {
                        sql.append(",");
                    }
                }
            }
            sql.append(") values (");

            while (dt.next())
            {
                StringBuilder tmp = new StringBuilder(sql);
                for (int j = 1; j <= rmeta.getColumnCount(); j++)
                {
                    if (params != null && params.length >= 3)
                    {
                        tmp.append(dataToDbFormatStr(dt.getObject(j), rmeta.getColumnType(j), rmeta.getColumnTypeName(j), params[0], params[1], params[2]));
                    }
                    else
                    {
                        tmp.append(dataToDbFormatStr(dt.getObject(j), rmeta.getColumnType(j)));
                    }

                    if (j != rmeta.getColumnCount())
                    {
                        tmp.append(",");
                    }
                }
                tmp.append(")");
                val.add(tmp.toString());
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_tableToInsSql_1", new String[] { "tableName" }, new Object[] { tableName }, exp);

        }
        return val.toArray(new String[val.size()]);
    }

    /**
     * 将数据表转换为插入的sql语句
     *
     * @param tableName 插入的数据表名
     * @param dt        插入的数据
     * @return 插入的sql语句
     * @throws DataAsException 自定义异常
     * @throws SQLException    数据库异常
     */
    public static String tableToInsSqlbyParam(String tableName, ResultSet dt) throws DataAsException, SQLException
    {
        StringBuilder sql = new StringBuilder("insert into " + tableName + "(");
        try
        {
            ResultSetMetaData rmeta = dt.getMetaData();
            StringBuilder values = new StringBuilder();
            for (int i = 0; i < rmeta.getColumnCount(); i++)
            {
                if (rmeta.getColumnName(i + 1).indexOf('.', 0) > -1)
                {
                    sql.append("'");
                    sql.append(rmeta.getColumnName(i + 1));
                    sql.append("'");
                    values.append("?");
                    if (i != rmeta.getColumnCount() - 1)
                    {
                        sql.append(",");
                        values.append(",");
                    }
                }
                else
                {
                    sql.append(rmeta.getColumnName(i + 1));
                    values.append("?");
                    if (i != rmeta.getColumnCount() - 1)
                    {
                        sql.append(",");
                        values.append(",");
                    }
                }
            }
            sql.append(") values (").append(values).append(")");

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_tableToInsSql_1", new String[] { "tableName" }, new Object[] { tableName }, exp);

        }
        return sql.toString();
    }

    /**
     * 数据表转更新语句
     *
     * @param tableName   更新的数据表名
     * @param primaryKeys 主键数组
     * @param dt          更新的数据
     * @param sqlList     升级数据list
     * @return 升级语句和主键列名组成的map, key∈{sql,主键列名},value为对应的ArrayList
     * @throws DataAsException 自定义异常
     * @throws SQLException    数据库异常
     */
    public static Map<String, ArrayList<Object>> tableToUpdSql(String tableName, String[] primaryKeys, ResultSet dt, ArrayList<Object> sqlList) throws DataAsException, SQLException
    {
        HashMap<String, ArrayList<Object>> hp = new HashMap<>();
        for (String key : primaryKeys)
        {
            hp.put(key, new ArrayList<>());
        }
        if (dt == null)
        {
            return hp;
        }

        try
        {

            while (dt.next())
            {
                ResultSetMetaData rmeta = dt.getMetaData();
                String sql = "update " + tableName + " set @col where @key";
                for (int j = 1; j < rmeta.getColumnCount() + 1; j++)
                {
                    String colName = rmeta.getColumnName(j);
                    String data = dataToDbFormatStr(dt.getObject(j), rmeta.getColumnType(j));
                    if (ArrayOper.findDataInArray(colName, primaryKeys) < 0)
                    {
                        sql = sql.replace("@col", colName + " = " + data + ", @col");
                    }
                    else
                    {
                        sql = sql.replace("@key", colName + " = " + data + " and @key");
                        // 将主键值放入map中
                        hp.get(colName).add(data);
                    }
                }
                sqlList.add(sql.replace(", @col", "").replace(" and @key", ""));
            }

            // 将upsql放入map中
            hp.put("upsql", sqlList);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_tableToUpdSql_1", new String[] { "tableName", "primaryKeys" }, new Object[] { tableName, primaryKeys }, exp);
        }
        return hp;
    }

    /**
     * 数据表转删除语句
     *
     * @param tableName   删除的数据表名
     * @param primaryKeys 主键数组
     * @param dt          删除的数据
     * @return 删除的sql语句
     * @throws DataAsException 自定义异常
     * @throws SQLException    数据库异常
     */
    public static String[] tableToDelSql(String tableName, String[] primaryKeys, ResultSet dt) throws DataAsException, SQLException
    {
        if (dt == null)
        {
            return new String[0];
        }

        String[] val = new String[dt.getRow()];
        try
        {
            ResultSetMetaData rmeta = dt.getMetaData();

            for (int i = 0; i < dt.getRow(); i++)
            {
                String sql = "delete from " + tableName + " where @key";
                for (int j = 1; j < rmeta.getColumnCount(); j++)
                {
                    String colName = rmeta.getColumnName(j);
                    if (ArrayOper.findDataInArray(colName, primaryKeys) >= 0)
                    {
                        String data = dataToDbFormatStr(dt.getObject(j), rmeta.getColumnType(j));
                        sql = sql.replace("@key", colName + " = " + data + " and @key");
                    }
                }
                val[i] = sql.replace(" and @key", "");
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_tableToDelSql_1", new String[] { "tableName", "primaryKeys" }, new Object[] { tableName, primaryKeys }, exp);
        }
        return val;
    }

    /**
     * 根据数据类型,将数据转换为能在sql语句中使用的字符串
     *
     * @param data     数据
     * @param dataType 数据类型
     * @param params   属性类型名称和数据库连接信息（colTypeName、url、username、password）
     * @return sql语句中使用的字符串
     * @throws DataAsException 自定义异常
     */
    public static String dataToDbFormatStr(Object data, int dataType, String... params) throws DataAsException
    {
        String val = "";
        try
        {
            if (data == null)
            {
                return "null";
            }
            switch (dataType)
            {
                case -5: // BIGINT/INT8
                    // 根据oid获取大数据对象数据（格式：@oidsep@xxx）
                    val = getLargeObject(data, params);
                    if (val != null)
                    {
                        val = "'" + getLargeObject(data, params) + "'";
                    }
                    break;
                case 2004: // BLOB
                case 4: // INTEGER
                case -6: // TINYINT
                case 5: // SMALLINT
                case 6: // FLOAT
                case 7: // REAL
                case 8: // DOUBLE
                case 2: // NUMERIC
                case 3: // DECIMAL
                    val = String.valueOf(data);
                    break;
                case 1: // CHAR
                case 12: // VARCHAR
                case -1: // LONGVARCHAR
                case -15: // NCHAR
                case -9: // NVARCHAR
                case -16: // LONGNVARCHAR
                case 2005: // CLOB
                    val = "'" + repDataNonSymbol(data.toString()) + "'";
                    break;
                case -7: // BIT
                case 16: // BOOLEAN
                    if ("0".equalsIgnoreCase(data.toString()) || "false".equalsIgnoreCase(data.toString()))
                    {
                        val = "0";
                    }
                    else
                    {
                        val = "1";
                    }

                    break;
                case 91: // DATE
                    break;
                case 93: // TIMESTAMP

                    SimpleDateFormat sdf = null;
                    Date dt = null;
                    try
                    {
                        sdf = new SimpleDateFormat(DataAsDef.DATETIMEMSFORMAT);
                        dt = sdf.parse(String.valueOf(data));// (Date) data;
                    }
                    catch (Exception exp)
                    {
                        sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
                        dt = sdf.parse(String.valueOf(data));// (Date) data;
                    }

                    String str = sdf.format(dt);
                    val = "'" + DataAsFunc.dateTimeStrFormat(str) + "'";
                    break;
                default:
                    DataAsExpSet.throwExpByResCode("DbAssistant_dataToDbFormatStr_2", new String[] { "data", "dataType" }, new Object[] { data, dataType }, null);
                    break;
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_dataToDbFormatStr_1", new String[] { "data", "dataType" }, new Object[] { data.toString(), dataType }, exp);
        }
        return val;
    }

    /**
     * 根据oid获取大数据对象数据
     *
     * @param data   原数据字符串
     * @param params 属性类型名称和数据库连接信息（colTypeName、url、username、password）
     * @return 大数据对象数据（格式：@oidsep@xxx）
     * @throws DataAsException 自定义异常
     */
    private static String getLargeObject(Object data, String... params)
    {
        // 返回结果(默认是int8类型)
        String val = String.valueOf(data);
        // 属性类型名称
        String colTypeName = "";
        // 数据库连接url
        String url = "";
        // 数据库用户名
        String username = "";
        // 数据库密码
        String password = "";

        if (params == null || params.length < 4)
        {
            return val;
        }

        for (int i = 0; i < params.length; i++)
        {
            if (i == 0)
            {
                colTypeName = params[i];
            }
            else if (i == 1)
            {
                url = params[i];
            }
            else if (i == 2)
            {
                username = params[i];
            }
            else if (i == 3)
            {
                password = params[i];
            }
        }

        // oid类型(在pg中int8类型和oid类型 coltypa都等于-5,所以不能单纯根据colType来判断,需要结合colTypeName(oid/int8)来判断是否为oid类型)
        if ("oid".equals(colTypeName))
        {
            try (DbOperBase dbOper = new DbOperBase(DatabaseType.Postgre, url, username, password);)
            {
                dbOper.openConn();
                Connection connection = dbOper.getConn();
                connection.setAutoCommit(false);
                LargeObjectManager lobj = ((org.postgresql.PGConnection) connection).getLargeObjectAPI();
                LargeObject obj = lobj.open((long) data, LargeObjectManager.READ);
                byte[] buf = new byte[1024];
                ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
                int n;
                while ((n = obj.read(buf, 0, 1024)) > 0)
                {
                    bOutput.write(buf, 0, n);
                }
                obj.close();
                String s = new String(bOutput.toByteArray(), "utf-8");
                //对oid字符串内的单引号处理,避免insert时报错
                s = s.replace("\'", "\'\'");
                val = "@oidsep@" + s;
                bOutput.close();
                connection.setAutoCommit(true);
            }
            // 此处不抛出异常，直接返回null，软适配层已做处理
            catch (Exception e)
            {
                val = null;
            }
        }

        return val;
    }

    /**
     * 根据数据类型,将数据转换为能在sql语句中使用的字符串
     *
     * @param data     原数据字符串
     * @param dataType 数据库类型
     * @return 能在sql语句中使用的字符串
     * @throws DataAsException 自定义异常
     */
    public static String dataToDbFormatStr(String data, String dataType) throws DataAsException
    {
        String val = "";
        try
        {
            dataType = dataType.toLowerCase();
            if (data == null || "".equals(data))
            {
                return "null";
            }

            if ("byte".equals(dataType) || dataType.indexOf("int") == 0 || dataType.indexOf("uint") == 0 || "float".equals(dataType) || "double".equals(dataType) || "decimal".equals(dataType)
                    || "real".equals(dataType))
            {
                val = data.toString();
            }
            else if ("bool".equals(dataType))
            {
                if ("0".equals(data) || "false".equalsIgnoreCase(data))
                {
                    val = "0";
                }
                else
                {
                    val = "1";
                }
            }
            else if ("string".equals(dataType))
            {
                val = "'" + DbAssistant.repDataNonSymbol(data) + "'";
            }
            else if ("datetime".equals(dataType))
            {
                val = "'" + DataAsFunc.dateTimeStrFormat(data.toString()) + "'";
            }
            else if ("byte[]".equals(dataType))
            {
                val = null;
            }
            else
            {
                DataAsExpSet.throwExpByResCode("DbAssistant_dataToDbFormatStr_2", new String[] { "data", "dataType" }, new Object[] { data, dataType }, null);
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_dataToDbFormatStr_1", new String[] { "data", "dataType" }, new Object[] { data.toString(), dataType }, exp);
        }
        return val;
    }

    /**
     * 得到异常中包含数据库异常的代码
     *
     * @param exp 异常
     * @return 异常代码
     */
    public static String getDbExceptionCode(Exception exp)
    {
        if (exp == null)
        {
            return "";
        }
        if (exp instanceof java.sql.SQLException)
        {
            return String.valueOf(((java.sql.SQLException) exp).getSQLState());
        }
        else if (exp instanceof org.postgresql.util.PSQLException)
        {
            return String.valueOf(((org.postgresql.util.PSQLException) exp).getSQLState());
        }
        else if (exp.getCause() == null || exp.getCause() instanceof NullException)
        {
            return "";
        }
        else
        {
            return getDbExceptionCode((Exception) exp.getCause());
        }
    }

    /**
     * 得到异常中包含数据库异常的描述信息
     *
     * @param exp 异常
     * @return 异常的描述信息
     */
    public static String getDbExceptionMsg(Exception exp)
    {
        Exception dbExp = getDbException(exp);
        if (dbExp == null)
        {
            if (exp != null)
            {
                return exp.getMessage();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return dbExp.getMessage();
        }
    }

    /**
     * 检测异常中是否包含数据库数据重复的异常
     *
     * @param dbType 数据库类型
     * @param exp    异常
     * @return true包含数据重复异常，false不包含
     */
    public static boolean checkDbRepeatKeyExp(DatabaseType dbType, Exception exp)
    {
        String code = getDbExceptionCode(exp);
        boolean val = false;

        switch (dbType)
        {
            // DatabaseType.Postgre
            case Postgre:
                if ("23505".equals(code))
                {
                    val = true;
                }
                break;
            case SQLite:
                String msg = getDbExceptionMsg(exp);
                if ("constraint".equalsIgnoreCase(code) || "19".equals(code) || (null != msg && msg.indexOf("unique") > -1))
                {
                    val = true;
                }
                break;
            case MySql:
                if ("1062".equals(code) || "1063".equals(code))
                {
                    val = true;
                }
                break;
            default:
                if ("1".equals(code))
                {
                    val = true;
                }
                break;
        }
        return val;
    }

    /**
     * 得到异常中包含的数据库异常
     *
     * @param exp 异常
     * @return 数据库异常
     */
    public static Exception getDbException(Exception exp)
    {
        if (exp == null || NullException.class.isInstance(exp))
        {
            return null;
        }
        else
        {
            return getDbException((Exception) exp.getCause());
        }
    }

    /**
     * 根据数据类型,得到数据库字段类型,只支持sqlite和access两种数据库
     *
     * @param dataType 数据类型
     * @param dbType   数据库类型
     * @return 返回数据库字段类型
     * @throws DataAsException 自定义异常
     */
    public static String getDbTypeByDataType(String dataType, DatabaseType dbType) throws DataAsException
    {
        String val = "";
        try
        {
            String resFileName = "com.goldwind.dataaccess.resourcefile/" + dbType.toString();

            ResourceBundle res = ResourceFileFactory.getResourceFile(resFileName, null);

            val = res.getString(dataType.toLowerCase().replace("system.", ""));
            if (val == null || val.isEmpty())
            {
                throw new NullException();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_getDbTypeByDataType_1", new String[] { "dataType", "dbType" }, new Object[] { dataType, dbType }, exp);
        }
        return val;
    }

    /**
     * 替换数据中的不合理符号
     *
     * @param data sql语句
     * @return 替换不合理符号后的sql语句
     */
    public static String repDataNonSymbol(String data)
    {
        if (data == null || data.isEmpty())
        {
            return data;
        }
        else
        {
            return data.replace("'", "''");
        }
    }

    /**
     * 设置结束时间（主要过滤sqlserver数据库）
     *
     * @param dt     时间
     * @param dbType 数据库类型
     * @return 调整精度后的时间
     */
    public static Date setEndTime(Date dt, DatabaseType dbType)
    {
        Date val = dt;
        int millisecond = 0;
        long l = val.getTime();
        String sMillisecond = Long.toString(l);
        sMillisecond = sMillisecond.substring(sMillisecond.length() - 3);
        millisecond = Integer.parseInt(sMillisecond);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(val);
        if (millisecond == 0)
        {
            if (dbType == DatabaseType.SqlServer)
            {
                gc.add(14, 998); //
                val = gc.getTime();
            }
            else
            {
                gc.add(14, 999);
                val = gc.getTime();
            }
        }
        else if (millisecond == 999)
        {
            if (dbType == DatabaseType.SqlServer)
            {
                gc.add(14, -1);
                val = gc.getTime();
            }
        }
        return val;
    }

    /**
     * 将数据表转换为建表语句
     *
     * @param tableName 表名
     * @param dt        数据
     * @param dbType    转换的数据库类型
     * @param conn      数据库连接
     * @return 建表sql语句
     * @throws SQLException    数据库异常
     * @throws DataAsException 自定义异常
     */
    public static String tableToDefSql(String tableName, ResultSet dt, DatabaseType dbType, Connection conn) throws SQLException, DataAsException
    {
        String schem = null;
        String tName = tableName;
        if (tableName.indexOf('.') >= 0)
        {
            String[] tables = tableName.split("\\.");
            schem = tables[0];
            tName = tables[1];
        }
        String val = "create table " + tableName + "(";
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rs = dbmd.getPrimaryKeys(conn.getCatalog(), schem, tName);// 获取主键
        // ResultSet rs1 = dbmd.getIndexInfo(null, "config", tableName, true, true);// 获取索引
        ResultSetMetaData rsmd = dt.getMetaData();
        List<String> primaryKeys = new ArrayList<String>();
        // List<String> indexKeys = new ArrayList<String>();
        while (rs.next())
        {
            primaryKeys.add(rs.getString("COLUMN_NAME"));
        }
        // while (rs1.next())
        // {
        // indexKeys.add(rs1.getString("COLUMN_NAME"));
        // }
        try
        {
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                String fieldName = rsmd.getColumnName(i);
                String dataType = rsmd.getColumnTypeName(i);
                dataType = getDbTypeByDataType(dataType, dbType); // 将字段类型转换为数据库类型
                val += " " + fieldName + " " + dataType; // 加入字段定义
                if (rsmd.isNullable(i) == ResultSetMetaData.columnNoNulls)
                {
                    val += " not null";
                }
                // if (indexKeys.contains(fieldName))
                // {
                // val += " unique";
                // }
                if (i < rsmd.getColumnCount())
                {
                    val += ",";
                }
            }

            // 加入主键描述
            if (!primaryKeys.isEmpty())
            {
                val += ", primary key(";
                for (int i = 0; i < primaryKeys.size(); i++)
                {
                    val += primaryKeys.get(i);
                    if (i < primaryKeys.size() - 1)
                    {
                        val += ",";
                    }
                }
                val += ")";
            }
            val += ")";
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DbAssistant_tableToDefSql_1", new String[] { "tableName", "dbType" }, new Object[] { tableName, dbType }, exp);
        }
        return val;
    }

    /**
     * 替换sql语句中的top为limit
     *
     * @param sql 语句
     * @return 替换后的语句
     * @throws DataAsException 自定义异常
     */
    public static String converSqlTopToLimit(String sql) throws DataAsException
    {
        // 定义top字符串，修改sonar
        String topStr = " top ";

        // 去除变量中包含的top
        String tempSql = sql.toLowerCase().replaceAll("\'(.*?)\'", "").replaceAll("\"(.*?)\"", "");
        // 去除变量后在判断sql中是否存在top
        int tempTopIndex = tempSql.indexOf(topStr);
        if (tempTopIndex >= 0)
        {
            int startIndex = sql.toLowerCase().indexOf(topStr);
            while (startIndex >= 0)
            {
                String rowNum = "";
                int endIndex = 0;
                getSqlTopPart(sql, startIndex, endIndex, rowNum);
                endIndex = DbAssistant.endIndex;
                rowNum = DbAssistant.rowNum;

                int sqlLastIndex = getSqlEndPos(sql, startIndex);

                char[] c = sql.toCharArray();

                if (sqlLastIndex >= c.length)
                {
                    sql = sql.substring(0, startIndex) + sql.substring(endIndex, endIndex + sqlLastIndex - endIndex) + " limit " + rowNum + " ";
                }
                else
                {
                    sql = sql.substring(0, startIndex) + sql.substring(endIndex, endIndex + sqlLastIndex - endIndex) + " limit " + rowNum + " " + sql.substring(sqlLastIndex);
                }

                startIndex = sql.indexOf(topStr);
            }
        }
        return sql;
    }

    /**
     * 寻找sql语句中的top部分
     *
     * @param sql        语句
     * @param startIndex 开始位置
     * @param endIndex   结束位置
     * @param rowNum     行号
     */
    private static void getSqlTopPart(String sql, int startIndex, int endIndex, String rowNum)
    {
        endIndex = startIndex + 5;
        int rowNumStartIndex = 0;
        int flg = 0;
        char[] c = sql.toCharArray();
        while (true)
        {
            if (c[endIndex] == ' ')
            {
                if (flg != 0)
                {
                    break;
                }
            }
            else
            {
                if (flg == 0)
                {
                    rowNumStartIndex = endIndex;
                    flg = 1;
                }
            }
            endIndex++;
        }
        rowNum = sql.substring(rowNumStartIndex, rowNumStartIndex + endIndex - rowNumStartIndex);
        DbAssistant.endIndex = endIndex;
        DbAssistant.rowNum = rowNum;
    }

    /**
     * 查找sql语句中的结束位置
     *
     * @param sql        sql语句
     * @param startIndex 开始位置
     * @return 结束位置
     */
    private static int getSqlEndPos(String sql, int startIndex)
    {
        int parenthFlg = 0;
        char[] c = sql.toCharArray();
        for (int i = startIndex; i < c.length; i++)
        {
            if (c[i] == '(')
            {
                parenthFlg++;
            }

            if (c[i] == ')')
            {
                parenthFlg--;
            }

            if (parenthFlg < 0)
            {
                int index = checkSqlFlg(sql.substring(0, i + 1), sqlEndFlag);
                if (index >= 0)
                {
                    return index;
                }
            }
        }
        return c.length;
    }

    /**
     * 检查sql语句末尾是否有关键字标志
     *
     * @param sql     语句
     * @param flgList 关键字数组
     * @return 关键字所在位置
     */
    private static int checkSqlFlg(String sql, String[] flgList)
    {
        for (int i = 0; i < flgList.length; i++)
        {
            int index = checkSqlFlg(sql, flgList[i]);
            if (index >= 0)
            {
                return index;
            }
        }
        return -1;
    }

    /**
     * 检查sql语句末尾是否有关键字标志
     *
     * @param sql 语句
     * @param flg 关键字
     * @return 关键字所在位置
     */
    private static int checkSqlFlg(String sql, String flg)
    {
        if (sql == null || sql.isEmpty())
        {
            return -1;
        }
        if (sql.lastIndexOf(flg) == sql.length() - flg.length())
        {
            return sql.length() - flg.length();
        }
        else
        {
            return -1;
        }
    }

    /**
     * 将数组组装为SQL语句的或条件
     *
     * @param paramArray    条件值数组
     * @param conditionName 条件变量名
     * @param paramIsString 条件变量是否是字符串
     * @param nullAsAll     true获取所有数据，false为null
     * @return 或条件语句块
     */
    public static String arrayOrConditionAsm(String[] paramArray, String conditionName, boolean paramIsString, boolean nullAsAll)
    {
        if (paramArray == null || paramArray.length == 0)
        {
            if (nullAsAll)
            {
                return "(1 = 1)";
            }
            else
            {
                return "(1 <> 1)";
            }
        }
        String val = "(";
        for (int i = 0; i < paramArray.length; i++)
        {
            String param = paramArray[i];
            if (paramIsString)
            {
                param = "'" + param + "'";
            }
            val += conditionName + " = " + param;
            if (i < paramArray.length - 1)
            {
                val += " or ";
            }
        }
        val += ")";
        return val;
    }

    /**
     * 将数组组装为SQL语句的与条件
     *
     * @param paramArray    条件值数组
     * @param conditionName 条件变量名
     * @param paramIsString 条件变量是否是字符串
     * @param nullAsAll     true获取所有数据，false为null
     * @return 与条件语句块
     */
    public static String arrayAndConditionAsm(String[] paramArray, String conditionName, boolean paramIsString, boolean nullAsAll)
    {
        if (paramArray == null || paramArray.length == 0)
        {
            if (nullAsAll)
            {
                return "(1 = 1)";
            }
            else
            {
                return "(1 <> 1)";
            }
        }
        String val = "(";
        for (int i = 0; i < paramArray.length; i++)
        {
            String param = paramArray[i];
            if (paramIsString)
            {
                param = "'" + param + "'";
            }

            val += conditionName + " = " + param;
            if (i < paramArray.length - 1)
            {
                val += " and ";
            }
        }
        val += ")";
        return val;
    }

    /**
     * 将数组组装为SQL语句的或模糊查询条件
     *
     * @param paramArray    条件值数组
     * @param conditionName 条件变量名
     * @param paramIsString 条件变量是否是字符串
     * @param nullAsAll     true获取所有数据，false为null
     * @return 与条件语句块
     */
    public static String arrayOrLikeCdtAsm(String[] paramArray, String conditionName, boolean paramIsString, boolean nullAsAll)
    {
        if (paramArray == null || paramArray.length == 0)
        {
            if (nullAsAll)
            {
                return "(1 = 1)";
            }
            else
            {
                return "(1 <> 1)";
            }
        }

        String val = "(";
        for (int i = 0; i < paramArray.length; i++)
        {
            String param = paramArray[i];
            if (paramIsString)
            {
                param = "'%" + param + "%'";
            }
            val += conditionName + " like " + param;
            if (i < paramArray.length - 1)
            {
                val += " or ";
            }
        }
        val += ")";
        return val;
    }

    /**
     * 将数组组装为SQL语句的选择列
     *
     * @param columnArray 选择列数组
     * @param dbType      数据库类型
     * @return 选择列语句块
     * @throws DataAsException 自定义异常
     */
    public static String arraySelectAsm(String[] columnArray, DatabaseType dbType) throws DataAsException
    {
        String val = "";
        if (columnArray == null || columnArray.length == 0)
        {
            return val;
        }
        else
        {
            for (int i = 0; i < columnArray.length; i++)
            {
                val += DbAssistant.convertObjectByDb(columnArray[i], dbType);
                if (i < columnArray.length - 1)
                {
                    val += ", ";
                }
            }
        }

        return val;
    }

    /**
     * 将数组组装为SQL语句的选择列，可指定别名或表名
     *
     * @param tableName   别名或表名
     * @param columnArray 选择列数组
     * @param dbType      数据类型
     * @return 选择列语句块
     * @throws DataAsException 自定义异常
     */
    public static String arraySelectAsm(String tableName, String[] columnArray, DatabaseType dbType) throws DataAsException
    {
        String val = "";
        if (columnArray == null || columnArray.length == 0)
        {
            return val;
        }
        else
        {
            val = ", ";
        }
        for (int i = 0; i < columnArray.length; i++)
        {
            String colName = DbAssistant.convertObjectByDb(columnArray[i], dbType);
            val += tableName + "." + colName + " as " + colName;
            if (i < columnArray.length - 1)
            {
                val += ", ";
            }
        }
        return val;
    }

    /**
     * 将数组组装为SQL语句的选择列
     *
     * @param columnArray 选择列数组
     * @return 选择列语句块
     */
    public static String arraySelectAsm(String[] columnArray)
    {
        String val = "";
        if (columnArray == null || columnArray.length == 0)
        {
            return val;
        }
        else
        {
            val = ", ";
        }
        for (int i = 0; i < columnArray.length; i++)
        {
            val += columnArray[i];
            if (i < columnArray.length - 1)
            {
                val += ", ";
            }
        }
        return val;
    }

    /**
     * sqlLite值处理方法
     *
     * @param keyWord 转换前的SQL
     * @return 转换后的SQL
     */
    public static String sqliteEscape(String keyWord)
    {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static String[] getSqlEndFlag()
    {
        return sqlEndFlag;
    }

    public static int getEndIndex()
    {
        return endIndex;
    }

    public static String getRowNum()
    {
        return rowNum;
    }

    public static void setSqlEndFlag(String[] sqlEndFlag)
    {
        DbAssistant.sqlEndFlag = sqlEndFlag;
    }

    public static void setEndIndex(int endIndex)
    {
        DbAssistant.endIndex = endIndex;
    }

    public static void setRowNum(String rowNum)
    {
        DbAssistant.rowNum = rowNum;
    }
}
