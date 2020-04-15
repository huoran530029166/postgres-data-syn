package org.sqlite;

import java.sql.SQLException;

/**
 * 
 * @author Administrator
 *
 */
public class SqliteConn
{
    /**
     * 
     */
    private Conn con;
    /**
    * 
    */
    private static final String RESOURCE_NAME_PREFIX = "jdbc:sqlite:";

    public SqliteConn(String url) throws SQLException
    {
        if (url.startsWith(RESOURCE_NAME_PREFIX))
        {
            this.con = new Conn(RESOURCE_NAME_PREFIX, url.substring(RESOURCE_NAME_PREFIX.length()));
        }
    }

    public Conn getCon()
    {
        return this.con;
    }

    /**
     * @param fileName
     *            文件名
     * @throws SQLException
     *             sql异常
     */
    public void backupDb(String fileName) throws SQLException
    {
        ExtendedCommand.parse("backup '' to '" + fileName + "'").execute(con.db());
    }

}
