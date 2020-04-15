package com.goldwind.dataaccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 
 * @author 曹阳
 *
 */
public class DataAsExpSet
{
    /**
     * 资源文件路径，例如com.goldwind.dataaccess.
     */
    private static String RESFILEPATH;

    public static String getResFilePath()
    {
        return RESFILEPATH;
    }

    public static void setResFilePath(String resFilePath)
    {
        DataAsExpSet.RESFILEPATH = resFilePath;
    }

    /**
     * ͨ通过资源代码得到描述信息
     * 
     * @param resCode
     *            资源代码
     * @return 描述信息
     */
    public static String getMsgByResCode(String resCode)
    {
        return getMsgByCodeFromMemory(resCode);
    }

    /**
     * @Title: getMsgByResCode
     * @Description: 增加Locale参数
     * @param resCode
     *            code
     * @param local
     *            本地语言
     * @return 返回结果
     * @return: String 结果类型
     */
    public static String getMsgByResCode(String resCode, Locale local)
    {
        String val = resCode;
        // 获取异常内存
        HashMap<String, Map<String, String>> dataMemory = DataAsMemory.getDataExpMemory();
        
        String language=local.getLanguage();
      //对语言进行处理-去除-后面的修饰
        language=language.split("-")[0];
        // 设置key值
        String key = language + "_" + local.getCountry();
        Map<String, String> dMap = dataMemory.get(key);
        if (dMap.containsKey(resCode))
        {
            val = dMap.get(resCode);
        }
        return val;
    }

    /**
     * 通过资源代码得到描述信息
     * 
     * @Title: getMsgByCodeFromMemory
     * @Description: 通过资源代码得到描述信息
     * @param resCode
     *            code
     * @return 资源value
     * @return: String
     */
    private static String getMsgByCodeFromMemory(String resCode)
    {
        return getMsgByMemory(resCode);
    }

    /**
     * logger定义
     * 
     */
    private static Logger LOGGER = LoggerFactory.getLogger(DataAsExpSet.class);

    /**
     * @Title: getMsgByMemory
     * @Description: 依据异常内存获取信息
     * @param resCode
     *            code
     * @return value
     * @return: String
     */
    private static String getMsgByMemory(String resCode)
    {
        String val = resCode;
        // 获取异常内存
        HashMap<String, Map<String, String>> dataMemory = DataAsMemory.getDataExpMemory();

        Locale locale = LocaleContextHolder.getLocale();
        
        // 如果本地语言没有，则使用默认语言
        if(locale == null || locale.getLanguage() == null || locale.getCountry() == null){
            locale = DataAsDef.getLocalCulture();
        }
        
        String language=locale.getLanguage();
        //对语言进行处理-去除-后面的修饰
        language=language.split("-")[0];
        
        // 如果本地语言没有，则使用默认语言
        if (!dataMemory.containsKey(language + "_" + locale.getCountry()))
        {
            locale = DataAsDef.getLocalCulture();
        }

        Map<String, String> dMap = dataMemory.get(language + "_" + locale.getCountry());
        if (dMap != null && dMap.containsKey(resCode))
        {
            val = dMap.get(resCode);
        }
        return val;

    }

    // /**
    // * ͨ通过资源代码得到描述信息
    // *
    // * @param resCode
    // * 资源代码
    // * @param funcName
    // * 方法名称
    // * @return 描述信息
    // */
    // private static String getMsgByResCode(String resCode, String funcName)
    // {
    // String val = "";
    // String[] arrFunName = funcName.split("\\.");
    // ArrayList<String> arr = new ArrayList<String>();
    // for (int i = 0; i < arrFunName.length; i++)
    // {
    // arr.add(arrFunName[i]);
    // }
    //
    // if ("ctor".equals(arr.get(arr.size() - 1)))
    // {
    // arr.set(arr.size() - 2, arr.get(arr.size() - 3));
    // arr.remove(arr.size() - 1);
    // }
    //
    // String space = "";
    // for (int i = 0; i < 3; i++)
    // {
    // space += arr.get(i);
    // if (i < 3)
    // {
    // space += ".";
    // }
    // }
    // if (RESFILEPATH != null)
    // {
    // space = RESFILEPATH;
    // }
    // // String className = arr.get(arr.size() - 2);
    // // String func = arr.get(arr.size() - 1);
    // String resFileName = space + "resourcefile/Language";
    // // 将原有分散的错误信息资源文件汇总，只需要到一个固定的路径读取
    // val = DataAsDef.getMessageResource(resFileName, DataAsDef.getLocalCulture()).getString(resCode);
    //
    // if (val == null || val.isEmpty())
    // {
    // val = resCode;
    // }
    // return val;
    // }

    /**
     * 得到调用此方法的前一方法的名称
     * 
     * @return 方法名称
     */
    private static String getPrevFunc()
    {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[3].getClassName() + "." + ste[3].getMethodName();
    }

    /**
     * 通过描述和方法名抛出异常
     * 
     * @param msg
     *            描述信息
     * @param funcName
     *            方法名称
     * @param paramNames
     *            参数名
     * @param paramValues
     *            参数值
     * @param innerException
     *            系统异常
     * @param codes
     *            异常码
     * @throws DataAsException
     *             自定义异常
     */
    private static void throwExpByMsg(String msg, String funcName, String[] paramNames, Object[] paramValues, Exception innerException, String... codes) throws DataAsException
    {
        DataAsException exp = new DataAsException(msg);
        // 判断参数是否大于0，大于0的话第一个表示code
        if (codes.length > 0)
        {
            exp.setCode(codes[0]);
        }
        if (innerException == null)
        {
            exp.setException(new DataAsException(msg));
        }
        else
        {
            if (innerException instanceof DataAsException)
            {
                exp = new DataAsException(msg, innerException);
            }
            else
            {
                exp.setException(innerException);
            }
        }
        if (paramNames != null && paramValues != null)
        {
            String message = funcName + ":params[";
            for (int i = 0; i < paramNames.length; i++)
            {
                // 判断是集合或数组时不打印值
                if (paramValues[i] != null)
                {
                    if (paramValues[i] instanceof Collection || paramValues[i].getClass().isArray())
                    {
                        paramValues[i] = null;
                    }
                }
                if (i == paramNames.length - 1)
                {
                    message += paramNames[i] + "=" + paramValues[i] + "]";
                }
                else
                {
                    message += paramNames[i] + "=" + paramValues[i] + ",";
                }
            }
            message += "->" + msg;
            exp.getData().add(message);
        }
        else
        {
            exp.getData().add(funcName + "->" + msg);
        }
        throw exp;
    }

    /**
     * 通过描述抛出异常
     * 
     * @param msg
     *            描述信息
     * @param paramNames
     *            参数名
     * @param paramValues
     *            参数值
     * @param innerException
     *            系统异常
     * @param codes
     *            异常码
     * @throws DataAsException
     *             自定义异常
     */
    public static void throwExpByMsg(String msg, String[] paramNames, Object[] paramValues, Exception innerException, String... codes) throws DataAsException
    {
        throwExpByMsg(msg, getPrevFunc(), paramNames, paramValues, innerException, codes);
    }

    /**
     * 通过资源代码和方法名抛出异常
     * 
     * @param resCode
     *            资源代码
     * @param funcName
     *            方法名称
     * @param paramNames
     *            参数名
     * @param paramValues
     *            参数值
     * @param innerException
     *            系统异常
     * @param codes
     *            异常码
     * @throws DataAsException
     *             自定义异常
     */
    private static void throwExpByResCode(String resCode, String funcName, String[] paramNames, Object[] paramValues, Exception innerException, String... codes) throws DataAsException
    {
        throwExpByMsg(getMsgByCodeFromMemory(resCode), funcName, paramNames, paramValues, innerException, codes);
    }

    /**
     * 通过资源代码抛出异常
     * 
     * @param resCode
     *            资源代码
     * @param paramNames
     *            参数名
     * @param paramValues
     *            参数值
     * @param innerException
     *            系统异常
     * @param codes
     *            异常码
     * @throws DataAsException
     *             自定义异常
     */
    public static void throwExpByResCode(String resCode, String[] paramNames, Object[] paramValues, Exception innerException, String... codes) throws DataAsException
    {
        throwExpByResCode(resCode, getPrevFunc(), paramNames, paramValues, innerException, codes);
    }

    /**
     * 通过资源代码获取异常描述信息
     * 
     * @param resCode
     *            资源代码
     * @param paramNames
     *            参数名
     * @param paramValues
     *            参数值
     * @param innerException
     *            系统异常
     * @param codes
     *            异常码
     * @return 异常描述信息
     */
    public static String getExpMsgByResCode(String resCode, String[] paramNames, Object[] paramValues, Exception innerException, String... codes)
    {
        String msg = "";
        try
        {
            throwExpByResCode(resCode, getPrevFunc(), paramNames, paramValues, innerException, codes);
        }
        catch (DataAsException e)
        {
            msg = e.getLocalizedMessage();
        }
        return msg;
    }

    /**
     * Exception转详情 xjs 2019-11-7 15:32:12
     * 
     * @param e
     * @return
     */
    public static String getExcetionDetail(Exception e)
    {
        StringBuffer stringBuffer = new StringBuffer(e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        int length = messages.length;
        for (int i = 0; i < length; i++)
        {
            stringBuffer.append("\t" + messages[i].toString() + "\n");

        }
        return stringBuffer.toString();
    }
}
