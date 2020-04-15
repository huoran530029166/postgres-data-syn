package com.goldwind.datalogic.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.CRC32;

import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 
 * @author Administrator
 *
 */
public final class CRCUtils
{
    private CRCUtils()
    {

    }

    /**
     * 获取指定协议的crc字符串
     * 
     * @param protocolid
     *            协议号
     * @param dbOper
     *            数据库操作对象
     * @return crc字符串
     * @throws DataAsException
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws SQLException,DataAsException,
     *             UnsupportedEncodingException 异常
     */
    public static String getProtocolCRC(int protocolid, DbOperBase dbOper) throws SQLException, DataAsException, UnsupportedEncodingException
    {
        String protocolCRC = "";
        String iecString = getProtocolIECString(protocolid, dbOper);
        CRC32 crc32 = new CRC32();
        crc32.update(iecString.getBytes(StandardCharsets.US_ASCII));
        protocolCRC = String.valueOf(crc32.getValue());
        return protocolCRC;
    }

    /**
     * 获取指定协议号的iec路径字符串
     * 
     * @param protocolid
     *            协议号
     * @param dbOper
     *            数据库操作对象
     * @return iec路径字符串
     * @throws SQLException
     *             数据库异常
     * @throws DataAsException
     *             自定义异常
     */
    private static String getProtocolIECString(int protocolid, DbOperBase dbOper) throws SQLException, DataAsException
    {
        StringBuilder result = new StringBuilder();
        String sql = "select iecpath from propaths where protocolid = " + protocolid + " order by offsets, iecpath";
        ResultSet rs = null;
        rs = dbOper.getResultSet(sql, null);
        while (rs.next())
        {
            result.append(rs.getString("iecpath"));
        }

        sql = "select iecpath from propaths where protocolid = " + protocolid + " and transtype = 1 and bsave = 1 order by offsets, iecpath";
        rs = dbOper.getResultSet(sql, null);
        while (rs.next())
        {
            result.append(rs.getString("iecpath"));
        }

        return result.toString();
    }
}
