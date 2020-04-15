package com.goldwind.dataaccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goldwind.dataaccess.DataAsDef.SystemPromptType;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 
 * @author 曹阳
 *
 */
public class DataAsFunc
{
    /**
     * 当前路径
     * 
     */
    private static File LOCALFILE = new File(System.getProperty("user.dir"));

    /**
     * 创建目录
     * 
     * @param dir
     *            目录名称
     * @throws DataAsException
     *             自定义异常
     */
    public static void createDir(String dir) throws DataAsException
    {
        try
        {
            File f = new File(dir);
            if (!f.exists())
            {
                f.mkdir();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_createDir_1", new String[] { "dir" }, new Object[] { dir }, exp);
        }
    }

    /**
     * 时间字符串的标准化,自动支持三种格式，yyyy-MM-dd、yyyy-MM-dd HH:MM:ss、yyyy-MM-dd HH:MM:ss.fff，例如原字符串为2010-1-1，将被转换为2010-01-01
     * 
     * @param dtStr
     *            日期格式字符串
     * @return 格式化后日期字符串
     * @throws DataAsException
     *             自定义异常
     */
    public static String dateTimeStrFormat(String dtStr) throws DataAsException
    {
        String val = "";
        if (dtStr == null || dtStr.isEmpty())
        {
            return val;
        }
        try
        {
            Pattern r = Pattern.compile("[0-9]{1,}");
            Matcher mc = r.matcher(dtStr);
            int i = 0;
            while (mc.find())
            {
                if (i == 0) // year
                {
                    val = mc.group(0);
                    if (val.length() < 4)
                    {
                        val = "20" + val;
                    }
                }
                else if (i == 1)// month
                {
                    val += "-" + DataAsFunc.padLeft(mc.group(0), 2);
                }
                else if (i == 2)// day
                {
                    val += "-" + DataAsFunc.padLeft(mc.group(0), 2);
                }
                else if (i == 3)// hour
                {
                    val += " " + DataAsFunc.padLeft(mc.group(0), 2);
                }
                else if (i == 4)// minute
                {
                    val += ":" + DataAsFunc.padLeft(mc.group(0), 2);
                }
                else if (i == 5)// second
                {
                    val += ":" + DataAsFunc.padLeft(mc.group(0), 2);
                }
                else if (i == 6 && Integer.valueOf(mc.group(0)) != 0)
                {
                    // 此处需要将时间中添加毫秒数，ss.SSS
                    val += "." + DataAsFunc.padLeft(mc.group(0), 3);
                }

                i++;
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_dateTimeStrFormat_1", new String[] { "dtStr" }, new Object[] { dtStr }, exp);
        }
        return val;
    }

    /**
     * 得到系统启动路径
     * 
     * @return 路径名
     */
    public static String getCurrentDir()
    {
        return System.getProperties().getProperty("user.dir");
    }

    /**
     * 获取环境变量
     * 
     * @return 环境变量集合
     * @throws IOException
     *             i/o异常
     */
    public static Map<String, String> getEnv() throws IOException
    {
        Map<String, String> map = new HashMap<String, String>();
        String os = System.getProperty("os.name").toLowerCase();
        Process p = null;
        if (os.indexOf("windows") > -1)
        {
            p = Runtime.getRuntime().exec("cmd /c set");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] str = line.split("=");
                map.put(str[0], str[1]);
            }
        }
        else if (os.indexOf("linux") > 1)
        {
            map = System.getenv();
        }
        return map;
    }

    /**
     * 得到公用资源文件里信息
     * 
     * @param resCode
     *            资源代码
     * @param cultureInfo
     *            本地语言环境
     * @return 资源信息
     */
    public static String getProgramRes(String resCode, Locale cultureInfo)
    {
        return DataAsDef.getMessageResource("com.goldwind.resoucefile/program", cultureInfo).getString(resCode);
    }

    /**
     * 得到简洁的语言环境名称
     * 
     * @return 语言环境名称
     */
    public static String getShortCulture()
    {
        return DataAsDef.getLocalCulture().toString().replace("_", "").toLowerCase();
    }

    /**
     * 得到不包括命名空间的简单类型名称
     * 
     * @param type
     *            类
     * @return 类名称
     */
    public static String getShortTypeName(Class<?> type)
    {
        String val = type.toString();
        if (val.indexOf('.') > -1)
        {
            val = val.substring(val.lastIndexOf('.') + 1);
        }
        return val;
    }

    /**
     * 得到系统提示信息
     * 
     * @param promptType
     *            提示信息类型
     * @param descr
     *            描述信息
     * @param cultureInfo
     *            本地语言环境
     * @return 系统提示信息
     */
    public static String getSystemPrompt(SystemPromptType promptType, String descr, Locale cultureInfo)
    {
        String val = "";

        try
        {
            ResourceBundle res = DataAsDef.getMessageResource("com.goldwind.resourcefile/Prompt", cultureInfo);
            val = res.getString(String.valueOf(promptType.getValue()));
            val = val.replace("@descr", descr);
        }
        catch (Exception e)
        {
            val = "prompt:" + String.valueOf(promptType.getValue()) + " descr:" + descr + " cultureInfo:" + cultureInfo.toString();
        }

        return val;
    }

    /**
     * 得到TcpClient的描述信息
     * 
     * @param socket
     *            客户端连接
     * @return 描述信息
     */
    public static String getTcpClientInfo(Socket socket)
    {
        String val = "";
        if (socket.isConnected())
        {
            val = "LocalIp:";
            val += socket.getLocalAddress().getHostAddress() + " " + String.valueOf(socket.getLocalPort());
            val += " RemoteIp:";
            InetSocketAddress inetSAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            val += inetSAddress.getAddress().getHostAddress() + " " + String.valueOf(inetSAddress.getPort());
        }
        return val;
    }

    /**
     * 要使用数据访问模块的任何功能，必须先使用此函数进行初始化
     * 
     * @param localCulture
     *            本地语言环境
     * @param useCode
     *            使用码
     * @throws Exception
     *             异常
     */
    public static void initDataAccess(Locale localCulture, String useCode) throws Exception
    {
        DataAsDef.setLocalCulture(localCulture);
        DataAsDef.setUseCode(useCode);
    }

    /**
     * 将字符串向左填充0达到length长度
     * 
     * @param s
     *            字符串
     * @param length
     *            长度
     * @return 向填充0后的字符串
     */
    public static String padLeft(String s, int length)
    {
        byte[] bs = new byte[length];
        byte[] ss = s.getBytes();
        Arrays.fill(bs, (byte) (48 & 0xff));
        System.arraycopy(ss, 0, bs, length - ss.length, ss.length);
        return new String(bs);
    }

    /**
     * 解析时间
     * 
     * @param time
     *            需解析日期字符串
     * @return 解析后的时间
     * @throws DataAsException
     *             自定义异常
     */
    public static Calendar parseDateTime(String time) throws DataAsException
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date();
        try
        {
            dt = sdf.parse("1900-01-01 00:00:00");
            if (time.length() == DataAsDef.DATEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATEFORMATSTR);
            }
            else if (time.length() == DataAsDef.DATETIMEFORMATSTR.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR);
            }
            else if (time.length() == DataAsDef.DATETIMEMSFORMAT.length())
            {
                // 兼容软适配遗留的旧时间格式："yyyy-MM-dd HH:mm:ss:SSS"(2019.03.18 fcy)
                if (":".equals(time.substring(19, 20)))
                {
                    time = time.substring(0, 19) + "." + time.substring(20, 23);
                }

                sdf = new SimpleDateFormat(DataAsDef.DATETIMEMSFORMAT);
            }
            else if (time.length() == DataAsDef.TEMPFILENAMEBYDATE.length())
            {
                sdf = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYDATE);
            }
            dt = sdf.parse(time);
            calendar.setTime(dt);
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_parseDateTime_1", new String[] { "time" }, new Object[] { time }, e);
        }

        return calendar;
    }

    /**
     * 计算分钟差
     * 
     * @param time1
     *            时间1
     * @param time2
     *            时间2
     * @return time2-time1的分钟差
     * @throws Exception
     *             自定义异常
     */
    public static int getMinDiff(String time1, String time2) throws Exception
    {
        int minutes = 0;
        // long from = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSEDSTR).parse(time1).getTime();
        // long to = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSEDSTR).parse(time2).getTime();
        long from = DataAsFunc.parseDateTime(time1).getTimeInMillis();
        long to = DataAsFunc.parseDateTime(time2).getTimeInMillis();
        minutes = (int) ((to - from) / (1000 * 60));
        return minutes;
    }

    /**
     * 计算小时差
     * 
     * @param time1
     *            时间1
     * @param time2
     *            时间2
     * @return time2-time1的小时差
     * @throws Exception
     *             自定义异常
     */
    public static double getHourDiff(Calendar time1, Calendar time2) throws Exception
    {
        double minutes = 0;
        long from = time1.getTimeInMillis();
        long to = time2.getTimeInMillis();
        minutes = ((to - from) / (1000.0 * 60 * 60));
        return minutes;
    }

    /**
     * 移除毫秒
     * 
     * @param time
     *            时间字符串
     * @return 移除毫秒后的时间字符串
     */
    public static String remoteMsel(String time)
    {
        if (time == null || time.isEmpty())
        {
            return "";
        }

        int index = time.lastIndexOf(" ");
        if (index >= 0)
        {
            return time.substring(0, index);
        }
        else
        {
            return time;
        }
    }

    /**
     * 设置操作系统的时间、长日期、短日期的格式为标准格式。
     * 
     * @throws IOException
     *             i/o异常
     */
    public static void setSysDateTimeFormat() throws IOException
    {

        // Runtime.getRuntime().exec("cmd /c date yyyy-MM-dd");
        // Runtime.getRuntime().exec("cmd /c time HH:mm:ss");
    }

    /**
     * 截断毫秒
     * 
     * @param dt
     *            时间
     * @return 截断后时间
     * @throws ParseException
     *             解析异常
     */
    public static Date truncMillisecond(Date dt) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = sdf.format(dt);
        return sdf.parse(str);
    }

    /**
     * 验证使用码
     * 
     * @throws DataAsException
     *             自定义异常
     */
    public static void valUseCode() throws DataAsException
    {
        if (DataAsDef.getUseCode() == null || DataAsDef.getUseCode().isEmpty())
        {
            DataAsExpSet.throwExpByResCode("DataAsFunc_valUseCode_1", null, null, null);
        }
    }

    /**
     * 获取磁盘空间
     * 
     * @return 当前磁盘空间信息:freeSpace-当前磁盘可用空间(单位：G),totalSpace-当前磁盘的总空间(单位：G)
     */
    public static HashMap<String, Double> getDiskSpace()
    {
        // File localFile = new File(System.getProperty("user.dir"));

        HashMap<String, Double> returnData = new HashMap<>();
        returnData.put("freeSpace", LOCALFILE.getUsableSpace() / 1024.0 / 1024.0 / 1024.0);
        returnData.put("totalSpace", LOCALFILE.getTotalSpace() / 1024.0 / 1024.0 / 1024.0);
        return returnData;
    }

    /**
     * 加法运算
     * 
     * @param m1
     *            被加数
     * @param m2
     *            加数
     * @return 计算结果
     */
    public static double addDouble(double m1, double m2)
    {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.add(p2).doubleValue();
    }

    /**
     * 减法运算
     * 
     * @param m1
     *            被减数
     * @param m2
     *            减数
     * @return 计算结果
     */
    public static double subDouble(double m1, double m2)
    {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.subtract(p2).doubleValue();
    }

    /**
     * 乘法运算
     * 
     * @param m1
     *            被乘数
     * @param m2
     *            乘数
     * @return 计算结果
     */
    public static double mulDouble(double m1, double m2)
    {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.multiply(p2).doubleValue();
    }

    /**
     * 除法运算
     * 
     * @param m1
     *            被除数
     * @param m2
     *            除数
     * @param scale
     *            精度（传入小于0的值默认为0）
     * @return 商
     */
    public static double divDouble(double m1, double m2, int scale)
    {
        if (scale < 0)
        {
            scale = 0;
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
