package com.goldwind.dataaccess;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.dataaccess.security.EncryptClass;

/**
 * 
 * @author Administrator
 *
 */
public class DataAsDef
{

    public enum OsType
    {
        /**
         * 操作系统类型
         */
        Windows, Linux
    }

    public enum SystemPromptType
    {

        /**
         * 系统提示类型
         */
        DataSpaceNotEnough(0), PowerManageLog(100), PowerManageInfo(101);

        /**
         * 
         * @param index
         *            根据值获取系统提示类型
         * @return 系统提示类型
         */
        public static SystemPromptType getValue(int index)
        {

            switch (index) {
                case 0:
                    return SystemPromptType.DataSpaceNotEnough;
                case 100:
                    return SystemPromptType.PowerManageLog;
                case 101:
                    return SystemPromptType.PowerManageInfo;
                default:
                    return null;
            }
        }

        /**
         * 系统提示类型
         */
        private int value = 0;

        SystemPromptType(int value)
        {

            this.value = value;

        }

        public int getValue()
        {
            return value;
        }
    }

    /**
     * 调试状态
     */
    private boolean debugState = false;

    /**
     * 本地语言
     */
    private static Locale localCulture;

    /**
     * 使用码
     */
    private static String useCode;

    /**
     * 日期时间格式化字符串，精确到秒，yyyy-MM-dd HH:mm:ss
     */
    public static final String DATETIMEFORMATSTR = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期时间格式化字符串，精确到分钟yyyy-MM-dd HH:mm:00
     */
    public static final String DATETIMEFORMATSEDSTR = "yyyy-MM-dd HH:mm:00";

    /**
     * 日期时间格式化字符串 精确到小时yyyy-MM-dd HH:00:00
     */
    public static final String DATETIMEFORMATHOURSTR = "yyyy-MM-dd HH:00:00";

    /**
     * 数据库日期时间格式化字符串yyyy-mm-dd hh24:mi:ss
     */
    public static final String DBDATATIMEFORMATSTR = "yyyy-mm-dd hh24:mi:ss";

    /**
     * 日期格式化字符串,精确到天yyyy-MM-dd
     */
    public static final String DATEFORMATSTR = "yyyy-MM-dd";
    /**
     * 月格式化字符串精确到月
     */
    public static final String MOTHFORMATSTR = "yyyy-MM";
    /**
     * 数据库日期格式化字符串，精确到日期yyyy-mm-dd
     */
    public static final String DBDATEFORMATSTR = "yyyy-mm-dd";
    /**
     * 毫秒时间格式化字符串
     */
    public static final String DATETIMEMSFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 数据库毫秒时间格式化字符串
     */
    public static final String DBDATETIMEMSFORMAT = "yyyy-mm-dd hh24:mi:ss.ms";

    /**
     * 日期时间格式化字符串(年/月/日)
     */
    public static final String DATETIMEFORMATYMDSTR = "yyyy/MM/dd HH:mm:ss";

    /**
     * 数据库日期时间格式化字符串(年/月/日)
     */
    public static final String DBDATATIMEFORMATYMDSTR = "yyyy/mm/dd hh24:mi:ss";

    /**
     * 日期时间格式化字符串(月/日/年)
     */
    public static final String DATETIMEFORMATMDYSTR = "MM/dd/yyyy HH:mm:ss";

    /**
     * 数据库日期时间格式化字符串(月/日/年)
     */
    public static final String DBDATATIMEFORMATMDYSTR = "mm/dd/yyyy hh24:mi:ss";

    /**
     * 日期时间格式化字符串(日/月/年)
     */
    public static final String DATETIMEFORMATDMYSTR = "dd/MM/yyyy HH:mm:ss";

    /**
     * 数据库日期时间格式化字符串(日/月/年)
     */
    public static final String DBDATATIMEFORMATDMYSTR = "dd/mm/yyyy hh24:mi:ss";
    /**
     * 时间格式化字符串
     */
    public static final String TIMEFORMATSTR = "HH:MM:ss";
    /**
     * 根据毫秒格式生成临时文件名称yyyyMMddHHmmssSSS
     */
    public static final String TEMPFILENAMEBYMSEL = "yyyyMMddHHmmssSSS";
    /**
     * 根据时间生成临时文件名称yyyyMMddHHmmss
     */
    public static final String TEMPFILENAMEBYTIME = "yyyyMMddHHmmss";
    /**
     * 根据日期生成临时文件名称yyyyMMdd
     */
    public static final String TEMPFILENAMEBYDATE = "yyyyMMdd";
    /**
     * 根据月份生成临时文件名称yyyyMM
     */
    public static final String TEMPFILENAMEBYMOTH = "yyyyMM";
    /**
     * 根据年份生成临时文件名称yyyy
     */
    public static final String TEMPFILENAMEBYYEAR = "yyyy";
    /**
     * 本地库文件扩展名
     */
    public static final String LOCALDBFILEEXTNAME = ".data";
    /**
     * 本地库压缩文件扩展名
     */
    public static final String LOCARCDBFILEEXTNAME = ".arc";
    /**
     * 临时文件扩展名
     */
    public static final String TEMPFILEEXTNAME = ".ftmp";
    /**
     * 缓存文件扩展名
     */
    public static final String BUFFTEMPEXTNAME = ".buftmp"; // 缓存文件扩展名
    /**
     * 文本文件扩展名
     */
    public static final String BUFFTXTEXTNAME = ".txt";

    /**
     * 最小时间
     */
    private Calendar minTime;
    /**
     * utf-8字符集
     */
    private static String cnCharset = "utf-8";

    /**
     * 获取本地语言环境
     * 
     * @return 本地语言环境
     */
    public static Locale getLocalCulture()
    {
        if (DataAsDef.localCulture == null)
        {
            DataAsDef.localCulture = new Locale("zh", "CN");
        }
        return DataAsDef.localCulture;

    }

    /**
     * 获取指定路径资源文件
     * 
     * @param path
     *            资源文件路径
     * @param culture
     *            本地语言环境
     * @return 资源文件
     */
    public static ResourceBundle getMessageResource(String path, Locale culture)
    {
        return ResourceFileFactory.getResourceFile(path, culture);
    }

    public static String getUseCode()
    {
        return useCode;
    }

    public static void setLocalCulture(Locale localCulture)
    {
        DataAsDef.localCulture = localCulture;
    }

    /**
     * 设置使用码
     * 
     * @param useCode
     *            使用码
     * @throws Exception
     *             异常
     */
    public static void setUseCode(String useCode) throws Exception
    {
        String code = EncryptClass.reEncrypt("DataAccess.Program", "189");
        if (code.equals(useCode))
        {
            DataAsDef.useCode = useCode;
        }
        else
        {
            DataAsDef.useCode = "";
        }
    }

    /**
     * @return the debugState
     */
    public boolean isDebugState()
    {
        return debugState;
    }

    /**
     * @param debugState
     *            the debugState to set
     */
    public void setDebugState(boolean debugState)
    {
        this.debugState = debugState;
    }

    /**
     * 获取最小时间
     * 
     * @return 最小时间
     * @throws DataAsException
     *             自定义异常
     */
    public Calendar getMINTIME() throws DataAsException
    {
        if (minTime == null)
        {
            minTime = DataAsFunc.parseDateTime("1900-01-01 00:00:00");
        }
        return minTime;
    }

    public void setMINTIME(Calendar mINTIME)
    {
        minTime = mINTIME;
    }

    public static String getCNCHARSET()
    {
        return cnCharset;
    }

    public static void setCNCHARSET(String cNCHARSET)
    {
        cnCharset = cNCHARSET;
    }

    /**
     * 子包名称
     * 
     * @ClassName: PACKAGE_NAME
     * @Description: 子包名称
     * @author: Administrator
     * @date: 2018年7月24日 上午9:49:48
     */
    public enum PACKAGE_NAME
    {
        /**
         * lib-dataaccess
         */
        DATAACCESS,
        /**
         * lib-datalogic
         */
        DATALOGIC,
        /**
         * lib-filetrans
         */
        FILETRANS,
        /**
         * lib-warning
         */
        WARNING,
        /**
         * DataDisposalService
         */
        DATADISPOSAL,
        /**
         * SoftAdapter
         */
        SOFTADAPTER,
        /**
         * lib-computing
         */
        COMPUTING,
        /**
         * lib-monitor
         */
        MONITOR
    }

    /**
     * 
     * @ClassName: LANGUAGE
     * @Description: 语言枚举
     * @author: Administrator
     * @date: 2018年7月24日 下午7:09:50
     */
    public enum LANGUAGE
    {
        US("en", "US", StandardCharsets.UTF_8), CN("zh", "CN", StandardCharsets.UTF_8), BR("pt", "BR", StandardCharsets.ISO_8859_1), PE("es", "PE", StandardCharsets.ISO_8859_1), RU("ru", "RU",
                StandardCharsets.UTF_8);
        /**
         * 语言
         */
        private String language;
        /**
         * 国家
         */
        private String country;
        /**
         * 编码方式
         */
        private Charset charset;

        LANGUAGE(String language, String country, Charset charset)
        {
            this.language = language;
            this.country = country;
            this.charset = charset;
        }

        public String getLanguage()
        {
            return language;
        }

        public String getCountry()
        {
            return country;
        }

        public Charset getCharset()
        {
            return charset;
        }

    }

    /**
     * 上传下载秘钥
     * 
     * @date: 2019年8月8日 冯春源
     */
    public enum ApplicationType
    {
        /**
         * 其他
         */
        OTHERS(0, "其他"),
        /**
         * 可服务工具的密钥
         */
        ServiceableTool(1, "可服务工具的密钥"),
        /**
         * 指标工具的密钥
         */
        QuotaTools(2, "指标工具的密钥");

        /**
         * 枚举ID
         */
        private int code;
        /**
         * 枚举名称
         */
        private String name;

        ApplicationType(int code, String name)
        {
            this.code = code;
            this.name = name;
        }

        public int getCode()
        {
            return code;
        }

        public String getName()
        {
            return name;
        }
    }

}
