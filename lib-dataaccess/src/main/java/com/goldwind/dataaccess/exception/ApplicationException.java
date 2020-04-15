package com.goldwind.dataaccess.exception;

import java.io.Serializable;

/**
 * 
 * @author 曹阳
 *
 */
public class ApplicationException extends Exception implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 4449402201141684485L;

    public ApplicationException()
    {
    }

    /** * @param arg0 */
    public ApplicationException(String mesg)
    {
        super(mesg);
    }

    /** * @param arg0 */
    public ApplicationException(Throwable thrab)
    {
        super(thrab);
    }

    /** * @param arg0 * @param arg1 */
    public ApplicationException(String mesg, Throwable thrab)
    {
        super(mesg, thrab);
    }
}
