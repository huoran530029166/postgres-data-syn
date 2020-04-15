package com.goldwind.dataaccess.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author 曹阳
 *
 */
public class DataAsException extends Exception implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 
     */
    private List<String> data;
    /**
     * 系统级异常
     */
    private Exception exception;

    /**
     * 异常code码
     */
    private String code;

    /**
     * 获取参数和参数值
     * 
     * @return map 参数和参数值的映射
     */
    public List<String> getData()
    {
        if (data == null)
        {
            data = new ArrayList<String>();
        }

        return data;
    }

    public void setData(List<String> data)
    {
        this.data = data;
    }

    public DataAsException(String message)
    {
        super(message);
    }

    public DataAsException(String message, Exception innerException)
    {
        super(message, innerException);
    }

    /**
     * @return the exception
     */
    public Exception getException()
    {
        return exception;
    }

    /**
     * @param exception
     *            the exception to set
     */
    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    @Override
    public void printStackTrace()
    {
        System.out.print(getLocalizedMessage());
    }

    @Override
    public String toString()
    {
        return getLocalizedMessage();
    }

    /**
     * 获取所有异常描述
     * 
     * @return 异常描述字符串
     */
    @Override
    public String getLocalizedMessage()
    {
        StringBuffer message = new StringBuffer();
        for (int i = getData().size() - 1; i >= 0; i--)
        {
            message.append(getData().get(i)).append("\n");
        }
        if (exception != null)
        {
            message.append(exception.toString()).append("\n");
            StackTraceElement[] st = exception.getStackTrace();
            for (StackTraceElement ste : st)
            {
                message.append(ste.toString()).append("\n");
            }
        }
        return message.toString();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
