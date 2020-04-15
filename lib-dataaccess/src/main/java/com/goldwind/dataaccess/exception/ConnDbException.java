package com.goldwind.dataaccess.exception;

import java.io.Serializable;

/**
 * 
 * @author 曹阳
 *
 */
public class ConnDbException extends DataAsException implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ConnDbException(String message)
    {
        super(message);
    }

    public ConnDbException(String message, Exception innerException)
    {
        super(message, innerException);
    }
}
