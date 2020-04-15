package com.goldwind.datalogic.business;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.DynamicRun;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.database.DbAssistant;
import com.goldwind.dataaccess.database.DbOperBase;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.BusinessDef.ConditionType;
import com.goldwind.datalogic.business.BusinessDef.Filtertype;
import com.goldwind.datalogic.business.BusinessDef.IecPathDataType;
import com.goldwind.datalogic.business.BusinessDef.IecPathElecType;
import com.goldwind.datalogic.business.model.NoticeSortInfo;
import com.goldwind.datalogic.business.model.TableName;
import com.goldwind.datalogic.business.model.WtHisProtocolInfo;
import com.goldwind.datalogic.utils.DataAssemble;
import com.goldwind.datalogic.utils.DataParse;
import com.goldwind.datalogic.utils.FactorDef.ArrgType;
import com.goldwind.datalogic.utils.MemoryData;
import com.goldwind.datalogic.utils.NetCommDef;

/**
 * @author 曹阳
 */
public class BusinessFunc
{
    /**
     * 输出日志
     */
    private static Log logger = Log.getLog(BusinessFunc.class);
    private static String sep = "@sep@";

    /**
     * 得到iec路径表示的数据量类型
     *
     * @param path iec路径
     * @return 数据量类型
     */
    public static IecPathElecType getIecPathElecType(String path)
    {
        IecPathElecType elecType = IecPathElecType.Unknown;
        if (path != null && !path.isEmpty())
        {
            String[] arr = path.split("\\.");
            if (arr.length >= 3 && arr[2].length() >= 2)
            {
                String var = arr[2].substring(1, 2).toLowerCase();
                if ("wd".equalsIgnoreCase(arr[2]))
                {
                    elecType = IecPathElecType.Digital_Control;
                }
                else if ("a".equals(var))
                {
                    elecType = IecPathElecType.Analog;
                }
                else if ("t".equals(var))
                {
                    elecType = IecPathElecType.Total;
                }
                else if ("d".equals(var))
                {
                    elecType = IecPathElecType.Digital;
                }
                else if ("b".equals(var))
                {
                    elecType = IecPathElecType.Digital_Bilateral;
                }
                else if ("c".equals(var))
                {
                    elecType = IecPathElecType.Calculation;
                }
                else if ("g".equals(var))
                {
                    elecType = IecPathElecType.String;
                }
                else if ("w".equalsIgnoreCase(var) || (arr.length >= 4 && arr[3].length() >= 2 && "Dt".equalsIgnoreCase(arr[3])))
                {
                    elecType = IecPathElecType.Time;
                }
            }
        }
        return elecType;
    }

    /**
     * 判断iec路径的数据量类型是否为时间量
     *
     * @param path iec路径
     * @return 是否为时间量
     */
    public static boolean isTimeElecType(String path)
    {
        boolean flg = false;

        if (!path.isEmpty())
        {
            String[] arr = path.split("\\.");
            if ((arr.length >= 3 && arr[2].length() >= 2 && "w".equalsIgnoreCase(arr[2].substring(1, 2).toLowerCase())) || (arr.length >= 4 && "Dt".equalsIgnoreCase(arr[3])))
            {
                flg = true;
            }
        }

        return flg;
    }

    /**
     * 根据clo_3取得iec量的数据量类型
     *
     * @param col_3 col_3
     * @return 数据量类型
     */
    public static IecPathElecType getIecElecTypeByCol3(String col_3)
    {
        IecPathElecType elecType = IecPathElecType.Unknown;

        if (col_3 != null && !col_3.isEmpty())
        {
            String iecType = col_3.split("-")[0];
            switch (iecType)
            {
                case "YC":
                    elecType = IecPathElecType.Analog;// 遥测量(YC)
                    break;
                case "YX":
                    elecType = IecPathElecType.Digital;// 遥信量(YX)
                    break;
                case "YK":
                    elecType = IecPathElecType.Digital_Control;// 遥控量(YK)
                    break;
                case "YT":
                    elecType = IecPathElecType.Digital_Adjust;// 遥调量(YT)
                    break;
                case "YM":
                    elecType = IecPathElecType.Total;// 遥脉量(YM)
                    break;
                default:
                    break;
            }
        }

        return elecType;

    }

    /**
     * 得到iec路径表示的数据类型
     *
     * @param path iec路径
     * @return 数据类型
     */
    public static IecPathDataType getIecPathDataType(String path)
    {
        IecPathDataType dataType = IecPathDataType.unknow;
        if (!path.isEmpty())
        {
            String[] arr = path.split("\\.");
            if (arr.length >= 4 && arr[2].length() >= 2)
            {
                String var = arr[3].substring(0, 1).toLowerCase();
                if ("i".equals(var) || "u".equals(var))
                {
                    dataType = IecPathDataType.Integer;
                }
                else if ("b".equals(var))
                {
                    dataType = IecPathDataType.Bool;
                }
                else if ("f".equals(var))
                {
                    dataType = IecPathDataType.Double;
                }
                else if ("s".equals(var))
                {
                    dataType = IecPathDataType.String;
                }

            }
        }
        return dataType;
    }

    /**
     * 查询功率曲线区间
     *
     * @param dbOper 数据库操作对象
     * @return return list，顺序为low,top,sub
     * @throws DataAsException 自定义异常
     * @throws SQLException    数据库异常
     */
    public static List<String> getPowCurveSegment(DbOperBase dbOper) throws SQLException, DataAsException
    {
        List<String> al = new ArrayList<>();

        // 读取配置
        String low = "1.75";
        String top = "31.25";

        try
        {
            String selSql = "select * from config.dataconfig";
            ResultSet configTable = dbOper.getResultSet(selSql, null);
            while (configTable.next())
            {
                if ("PowCurveSegmentLow".equalsIgnoreCase(configTable.getString("name")))
                {
                    low = configTable.getString("val");
                }

                if ("PowCurveSegmentTop".equalsIgnoreCase(configTable.getString("name")))
                {
                    top = configTable.getString("val");
                }
            }

            String sub = String.valueOf(Math.floor(Double.valueOf(low) / 0.5));
            al.add(low);
            al.add(top);
            al.add(sub);

            // 验证参数
            double l = Double.valueOf(low);
            double t = Double.valueOf(top);

            if ((t - l) < 10)
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_getPowCurveSegment_1", new String[] { "low", "top", "sub", "l", "t" }, new Object[] { low, top, sub, l, t }, null);
            }
            if (l < 0 || t > 100)
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_getPowCurveSegment_1", new String[] { "low", "top", "sub", "l", "t" }, new Object[] { low, top, sub, l, t }, null);
            }
            if ((!(DynamicRun.isDoubleEqual(l - (Math.floor(l)), 0.25)) && (!(DynamicRun.isDoubleEqual(l - (Math.floor(l)), 0.75)))) || (!(DynamicRun.isDoubleEqual(t - (Math.floor(t)), 0.25))
                    && (!(DynamicRun.isDoubleEqual(t - (Math.floor(t)), 0.75)))))
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_getPowCurveSegment_1", new String[] { "low", "top", "sub", "l", "t" }, new Object[] { low, top, sub, l, t }, null);
            }
        }
        catch (Exception ex)
        {
            DataAsExpSet.throwExpByResCode("BusinessFunc_getPowCurveSegment_2", new String[] { "low", "top", }, new Object[] { low, top }, ex);
        }
        return al;
    }

    /**
     * 获取故障录波文件列表
     *
     * @param wtid       设备ID
     * @param bTime      开始时间
     * @param eTime      结束时间
     * @param dateFormat 日期格式
     * @param ip         前置IP
     * @param port       前置端口
     * @return 文件路径列表
     * @throws IOException io异常
     */
    public static List<String> getFaultRecorderFileList(int wtid, Date bTime, Date eTime, String dateFormat, String ip, int port) throws IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        List<String> fileList = new ArrayList<>();
        Socket client = new Socket(ip, port);
        try
        {
            client.setSoTimeout(10000);
            OutputStream os = client.getOutputStream();
            DataInputStream dis = new DataInputStream(client.getInputStream());
            os.write(DataAssemble.getWavefileList(String.valueOf(wtid), sdf.format(bTime), sdf.format(eTime)).getBytes("UTF-8"));
            os.flush();
            byte[] all = new byte[0];
            byte[] buf = new byte[8192];
            int len;
            while ((len = dis.read(buf)) != -1)
            {
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                swapStream.write(buf, 0, len);
                buf = swapStream.toByteArray();
                String back = new String(buf, "UTF-8");
                if ("(wait)".equals(back))
                {
                    os.write("(ok)".getBytes());
                    os.flush();
                    buf = new byte[8192];
                }
                else if ("(finish)".equals(back))
                {
                    String fileNames = new String(all, "UTF-8");
                    String[] files = fileNames.substring(1, fileNames.length() - 1).split(",");
                    for (String s : files)
                    {
                        fileList.add(s);
                    }
                    client.close();
                    break;
                }
                else
                {
                    all = byteMerger(all, buf);
                    os.write("(ok)".getBytes());
                    os.flush();
                }

            }
        }
        finally
        {
            if (client != null)
            {
                client.close();
            }
        }
        return fileList;
    }

    /**
     * 获取故障录波文件
     *
     * @param wtid         设备ID
     * @param sourceFile   源文件名，如果有多个文件则以逗号分隔，例如file1,file2
     * @param destFilePath 文件存放目标路径
     * @param ip           前置IP
     * @param port         前置端口
     * @return 是否成功
     * @throws IOException
     * @throws UnknownHostException
     */
    public static boolean getFaultRecorderDoc(int wtid, String sourceFile, String destFilePath, String ip, int port)
    {
        String[] files = sourceFile.split(",");
        OutputStream os = null;
        DataInputStream dis = null;
        Socket client = null;
        boolean re = true;
        for (String f : files)
        {

            RandomAccessFile raf = null;
            try
            {
                client = new Socket(ip, port);
                client.setSoTimeout(10000);
                InputStream is = client.getInputStream();
                os = client.getOutputStream();
                dis = new DataInputStream(is);
                raf = new RandomAccessFile(destFilePath + "/" + f, "rw");
                os.write(DataAssemble.getWavefileData(String.valueOf(wtid), f).getBytes("UTF-8"));
                byte[] buf = new byte[8192];
                int len;
                while ((len = dis.read(buf, 0, buf.length)) != -1)
                {
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    swapStream.write(buf, 0, len);
                    buf = swapStream.toByteArray();
                    String back = new String(buf, "UTF-8");
                    if ("(wait)".equals(back))
                    {
                        os.write("(ok)".getBytes());
                        os.flush();
                        buf = new byte[8192];
                    }
                    else if ("(finish)".equals(back))
                    {
                        break;
                    }
                    else
                    {
                        HashMap<String, Object> hm = parseBigData(buf);
                        int packLen = (int) hm.get("packLen");
                        byte[] data = (byte[]) hm.get("packData");
                        int t = 0;
                        byte[] tmp = new byte[8192];
                        while (t < (packLen - data.length))
                        {
                            t += dis.read(tmp, t, packLen - data.length - t);
                        }
                        raf.write(data);
                        raf.write(tmp, 0, t);
                        os.write("(ok)".getBytes());
                        os.flush();
                    }

                }
            }
            catch (Exception e)
            {
                logger.error(e);
                re = false;
                break;
            }
            finally
            {
                try
                {
                    if (raf != null)
                    {
                        raf.close();
                    }

                }
                catch (IOException e)
                {
                    logger.error(e);
                }
                if (client != null)
                {
                    try
                    {
                        client.close();
                    }
                    catch (IOException e)
                    {
                        logger.error(e);
                    }
                }
            }
        }

        return re;
    }

    /**
     * 获取更新版本号语句
     *
     * @param st 版本号类型
     * @return 更新语句
     */
    public static String getUpdateConditionSql(ConditionType st)
    {
        String timestamp = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYMSEL).format(new Date());
        return "update config.dataconfig set val=" + timestamp + " where name='" + st + "'";
    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param start  开始时间
     * @param end    结束时间
     * @param format 日期格式字符串
     * @return 两个日期之间所有日期字符串，包括开始和结束日期
     */
    public static List<String> getBetweenDates(Date start, Date end, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DAY_OF_YEAR, 1);
        while (tempStart.before(tempEnd))
        {
            result.add(sdf.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;

    }

    /**
     * 合并字节数组
     *
     * @param byte1 字节数组1
     * @param byte2 字节数组2
     * @return 合并后的字节数组
     */
    private static byte[] byteMerger(byte[] byte1, byte[] byte2)
    {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    /**
     * 解析大数据包
     *
     * @param msg 消息
     * @return hashmap
     * @throws DataAsException 自定义异常
     */
    private static HashMap<String, Object> parseBigData(byte[] msg) throws DataAsException
    {
        HashMap<String, Object> hp = new HashMap<String, Object>();
        try
        {

            // 得到包号
            byte[] num = new byte[4];
            System.arraycopy(msg, 0, num, 0, 4);

            int packNum = DataParse.byteArrayToInt(num);
            // int packNum = Integer.parseInt(new String(DataParse.intToByteArray(4), "UTF-8"));

            // 得到数据长度
            byte[] len = new byte[4];
            System.arraycopy(msg, 4, len, 0, 4);
            int packLen = DataParse.byteArrayToInt(len);

            // 得到数据
            byte[] packData = new byte[msg.length - 8];
            System.arraycopy(msg, 8, packData, 0, msg.length - 8);

            hp.put("packNum", packNum);
            hp.put("packLen", packLen);
            hp.put("packData", packData);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("DataParse_parseBigData_1", null, null, exp);
        }
        return hp;
    }

    /**
     * 根据统计类型，返回时间段内的时间
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param type  时间类型,年:Calendar.YEAR,月:Calendar.MONTH,日:Calendar.DAY_OF_YEAR,小时:Calendar.HOUR
     * @return 两个日期之间所有指定统计类型的时间，包括开始时间和不包括结束时间
     */
    public static List<String> getBetweenDatesbyParam(Date start, Date end, int type)
    {
        String format = "yyyy-MM-dd";
        switch (type)
        {
            case Calendar.YEAR:
                format = "yyyy";
                break;
            case Calendar.MONTH:
                format = "yyyy-MM";
                break;
            case Calendar.DAY_OF_YEAR:
                format = "yyyy-MM-dd";
                break;
            case Calendar.HOUR:
                format = "yyyy-MM-dd HH";
                break;
            default:
                break;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd))
        {
            result.add(sdf.format(tempStart.getTime()));
            tempStart.add(type, 1);
        }
        return result;

    }

    /**
     * 根据开始日期与结束日期区间月份（包括开始时间和不包括结束时间），获取该日期段内指定电场id的月初始化计划发电量sql集合
     *
     * @param wfid      电场id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 该日期段内指定电场id的月初始化计划发电量sql集合
     */
    public static List<String> getInitWfplanelecSql(Integer wfid, Date startDate, Date endDate)
    {
        List<String> dateList = getBetweenDatesbyParam(startDate, endDate, Calendar.MONTH);
        List<String> result = new ArrayList<>();
        for (String dateStr : dateList)
        {
            String sql = "insert into public.wfplanelec(wfid,rectime,datetype) values (" + wfid.toString() + ",'" + dateStr + "-01 00:00:00',1);";
            result.add(sql);
        }
        return result;
    }

    /**
     * 根据开始日期与结束日期区间月份（包括开始时间和不包括结束时间），获取该日期段内指定电场id的月初始化阶段上网电价sql集合
     *
     * @param wfid      电场id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 该日期段内指定电场id的月初始化阶段上网电价sql集合
     * @throws ParseException 字符串转日期异常
     */
    public static List<String> getInitStageonpriceSql(Integer wfid, Date startDate, Date endDate)
    {
        List<String> dateList = getBetweenDatesbyParam(startDate, endDate, Calendar.MONTH);
        List<String> result = new ArrayList<>();
        for (String dateStr : dateList)
        {
            LocalDate date = LocalDate.parse(dateStr + "-01");
            String sql = "insert into config.stageonprice(wfid,onyear,onmonth) values (" + wfid + "," + date.getYear() + "," + date.getMonth().getValue() + ");";
            result.add(sql);
        }
        return result;
    }

    /**
     * 根据开始日期与结束日期区间月份（包括开始时间和不包括结束时间），获取该日期段内指定电场id的月初始化成本sql集合
     *
     * @param wfid      电场id
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 该日期段内指定电场id的月初始化成本列表sql集合
     */
    public static List<String> getInitWfcostlistSql(Integer wfid, Date startDate, Date endDate)
    {
        List<String> dateList = getBetweenDatesbyParam(startDate, endDate, Calendar.MONTH);
        List<String> result = new ArrayList<>();
        for (String dateStr : dateList)
        {
            String sql = "insert into config.wfcostlist(wfid,costtypeid,costdate) select " + wfid + " as wfid,id,'" + dateStr + "-01 00:00:00' as costdate from config.wfcosttype;";
            result.add(sql);
        }
        return result;
    }

    /**
     * 获取删除指定电场id的月初始化计划发电量sql
     *
     * @param wfid 电场id
     * @return 删除指定电场id的月初始化计划发电量sql
     */
    public static String getDelWfplanelecSql(Integer wfid)
    {
        return "delete from public.wfplanelec where wfid=" + wfid.toString();
    }

    /**
     * 获取删除指定电场id的月初始化阶段上网电价sql
     *
     * @param wfid 电场id
     * @return 删除指定电场id的月初始化阶段上网电价sql
     */
    public static String getDelStageonpriceSql(Integer wfid)
    {
        return "delete from config.stageonprice where wfid=" + wfid.toString();
    }

    /**
     * 获取删除指定电场id的月初始化成本sql
     *
     * @param wfid 电场id
     * @return 删除指定电场id的月初始化成本sql
     */
    public static String getDelWfcostlistSql(Integer wfid)
    {
        return "delete from config.wfcostlist where wfid=" + wfid.toString();
    }

    /**
     * 遥信量替换为遥控量方法(老版)
     *
     * @param singleIEC 遥信量
     * @param realPaths 瞬态IEC集合
     * @return 遥控量 null:没找到对应遥控量 异常 传入IEC非标准升压站遥信量
     */
    public static String singleIECToControlIECOld(String singleIEC, List<String> realPaths)
    {
        // 定义返回遥控量
        String ctrIec = null;
        String[] iecparts = singleIEC.split("\\.");

        // soam2.1判断逻辑标识
        boolean flag21 = true;

        // soam2.1判断逻辑
        // 替换第三小节的Rd为Wd 单点遥信
        if ("Rd".equals(iecparts[2]))
        {
            iecparts[2] = "Wd";
            ctrIec = BusinessFunc.soam21JoinCtrPath(iecparts);
        }
        // 替换第三、四小节 双点遥信
        else if (iecparts[2].equals("Rb"))
        {
            iecparts[2] = "Wd";
            iecparts[3] = "b0";
            ctrIec = BusinessFunc.soam21JoinCtrPath(iecparts);
        }
        else
        {
            // soam2.1判断逻辑 无匹配项
            flag21 = false;
        }

        if (!(flag21 && realPaths.contains(ctrIec)))
        {
            // 如果2.1逻辑无匹配 或者 瞬态量中不包含2.1翻译后的遥控量 进入2.0翻译逻辑

            // 初始化2.0翻译控制IEC值
            ctrIec = null;
            for (String iec : realPaths)
            {
                // 如果瞬态量中的某IEC与传入遥信量不同且第5小节相同 即为遥控量
                if (iecparts.length >= 5 && iec.split("\\.").length == 5 && iecparts[4].equals(iec.split("\\.")[4]) && (!iec.equals(singleIEC)))
                {
                    ctrIec = iec;
                }
            }
        }
        return ctrIec;
    }

    /**
     * 遥信量替换为遥控量方法(新版)
     *
     * @param singleIEC 遥信量
     * @param allPaths  协议下所有IEC集合
     * @param flg       是否使用第二、第三规则
     * @return 遥控量 null:没找到对应遥控量
     * @throws DataAsException 异常 传入IEC校验和转化后IEC校验
     */
    public static String singleIECToControlIEC(String singleIEC, List<String> allPaths, boolean flg) throws DataAsException
    {
        // soam2.1判断逻辑标识
        boolean flag21 = true;

        // 校验传入遥信量为协议下存在的IEC,否则 抛出异常
        if (!allPaths.contains(singleIEC))
        {
            DataAsExpSet.throwExpByResCode("BusinessFunc_singleIECToControlIEC_1", null, null, null);
        }
        // 定义返回遥控量
        String ctrIec = null;
        String[] iecparts = singleIEC.split("\\.");

        // 新版判断逻辑
        // 替换第三小节的Rd为Wd
        if ("Rd".equals(iecparts[2]))
        {
            iecparts[2] = "Wd";
            ctrIec = BusinessFunc.soam21JoinCtrPath(iecparts);
        }
        else if (flg)
        {
            // 第二规则
            if (iecparts[2].equals("Rb"))
            {
                iecparts[2] = "Wd";
                iecparts[3] = "b0";
                ctrIec = BusinessFunc.soam21JoinCtrPath(iecparts);
            }
            else
            {
                // soam2.1判断逻辑 无匹配项
                flag21 = false;
            }
        }

        // [使用第二、第三规则打开的情况] 且 [2.1逻辑无匹配 或者 【协议下所有IEC集合】中不包含【第一/第二规则】翻译后的遥控量],进入第三规则
        if (flg && (!flag21 || !allPaths.contains(ctrIec)))
        {
            // 初始化2.0翻译控制IEC值
            ctrIec = null;
            for (String iec : allPaths)
            {
                // 如果瞬态量中的某IEC与传入遥信量不同且第5小节相同 即为遥控量
                if (iecparts.length >= 5 && iec.split("\\.").length == 5 && iecparts[4].equals(iec.split("\\.")[4]) && (!iec.equals(singleIEC)))
                {
                    ctrIec = iec;
                }
            }
        }

        // 校验转换后的遥控量为协议下存在的IEC,否则 抛出异常
        if (ctrIec != null && !allPaths.contains(ctrIec))
        {
            DataAsExpSet.throwExpByResCode("BusinessFunc_singleIECToControlIEC_2", null, null, null);
        }
        return ctrIec;
    }

    /**
     * soam2.1逻辑 拼接IEC量
     *
     * @param iecparts IEC part数组
     * @return IEC量
     */
    private static String soam21JoinCtrPath(String[] iecparts)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < iecparts.length; i++)
        {
            builder.append(iecparts[i] + ".");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    /**
     * soeIEC量转换为原始IEC量
     *
     * @param soeIec soeIEC量
     * @return 原始IEC量
     */
    public static String soeIECTonormalIEC(String soeIec)
    {
        String iecPath = soeIec;
        if (null != soeIec && !soeIec.isEmpty() && soeIec.contains(NetCommDef.SOESYMBLE))
        {
            iecPath = soeIec.split(NetCommDef.SOESYMBLE)[1];
        }
        return iecPath;
    }

    /**
     * 原始IEC量转换为soeIEC量
     *
     * @param normalIec soeIEC量
     * @return 原始IEC量
     */
    public static String normalIECTosoeIEC(String normalIec)
    {
        return NetCommDef.SOESYMBLE + normalIec;
    }

    /**
     * 取得修正后的iec累积量的值
     *
     * @param iectype       iec类型
     * @param iecupdvalue   iec累积量的值（修正前）
     * @param timediff      时间差（分钟）
     * @param standardpower 额定功率
     * @param percent       系数
     * @return 修正后的iec累积量的值
     */
    public static Double getIecupdValue(int iectype, Double iecupdvalue, double timediff, double standardpower, double percent)
    {
        double updateValue = iecupdvalue;
        // 取得累积量修正值（如果是1:发电量，超出范围，结果设为：0kWh；如果是2:时间，超出范围，结果设为：10分钟）
        if (iectype == 1)
        {
            // 如果额定功率为0，则显示原始差值（原始差值>=0）
            // 发电量使用末值减初值，结果应该大于等于0，并且不应超过设备该时间段满发电量的1.5倍（可配），如果超出此范围，结果设为0kwh。
            if (iecupdvalue < 0 || (standardpower > 0 && iecupdvalue > standardpower * (percent / 100) * (timediff / 60.0)))
            {
                updateValue = 0;
            }
        }
        else if (iectype == 2)
        {
            // 时间相关的量也使用末值减去初值，结果应该大于等于0，并且不应超过该时间段时长；否则认为跳变，修正为0。
            if (iecupdvalue < 0 || iecupdvalue * 60 > timediff * (percent / 100))
            {
                updateValue = 0;
            }
            // 如果超出允许的比例范围，结果设为10分钟的相应整数倍。
            else if (iecupdvalue * 60 > timediff && iecupdvalue * 60 <= timediff * (percent / 100))
            {
                updateValue = BusinessDef.TENMINUTETOHOUR * timediff / 10;
            }
            // 2019.12.26（十分钟修正精度问题）：将时间量值为0.16的改为0.166666
            else if (iecupdvalue == BusinessDef.TENMINUTETOHOURMIN && timediff == 10)
            {
                updateValue = BusinessDef.TENMINUTETOHOUR;
            }
        }
        return updateValue;
    }

    /**
     * 判断是否符合过滤条件(用户级屏蔽),调用方在程序启动时需调用DatalogicInit.init(dbOper)
     *
     * @param systemid   告警系统
     * @param objectid   告警对象
     * @param levelid    告警级别
     * @param typeid     告警分类（二级分类）
     * @param info       告警事件（关联信息）
     * @param objecttype 告警对象类型，0 场站告警 1设备告警 2 其它告警
     * @param iecVal     关联值
     * @param filtertype 过滤类型，0-屏蔽，1-订阅
     * @param userid     用户id
     * @return 判断结果（true:【屏蔽】，false:【不屏蔽】）
     */
    public static boolean isConformFilterRules(String systemid, String objectid, String levelid, String typeid, String info, String objecttype, String iecVal, Filtertype filtertype, String userid)
    {
        // 判断结果
        boolean result = false;
        HashMap<String, HashMap<String, NoticeSortInfo>> noticeSortList = MemoryData.getNoticeSortInfo();

        // 如果内存中没有该用户的消息过滤规则或者传入的告警系统为null
        if (null == noticeSortList || null == systemid || !noticeSortList.containsKey(userid))
        {
            return false;
        }

        // 循环该用户的所有消息过滤规则
        for (Entry<String, NoticeSortInfo> entry : noticeSortList.get(userid).entrySet())
        {
            NoticeSortInfo noticeSortInfo = entry.getValue();

            // 判断告警报文与某条规则是否匹配
            if (isConformFilterRule(systemid, objectid, levelid, typeid, info, objecttype, iecVal, filtertype, noticeSortInfo))
            {
                result = true;
                break;
            }
        }

        return result;
    }

    /**
     * 判断是否符合过滤规则-告警对象
     *
     * @param objecttype     告警对象类型，0 场站告警 1设备告警 2 其它告警（告警包里的）
     * @param objectid       告警对象（告警包里的）
     * @param noticeSortInfo 用户的消息过滤规则
     * @return 判断结果
     */
    private static boolean isConformObjectid(String objecttype, String objectid, NoticeSortInfo noticeSortInfo)
    {
        // 判断结果
        boolean result = false;

        // 兼容旧的告警报文格式，对旧的报文需要过滤/屏蔽时，返回false-不过滤/屏蔽--fcy 20180913
        if (null == objecttype || objecttype.isEmpty() || null == objectid || objectid.isEmpty())
        {
            return false;
        }

        // 根据不同告警对象类型判断
        switch (objecttype)
        {
            case "0":// 0-场站告警
            case "1":// 1-设备告警
            case "101":// 101-地理监测
                // 判断过滤规则是否包含告警包中的objectid或为其上层组织(objectid是风场Id/设备Id)
                if (isUpGroup(objecttype, objectid, noticeSortInfo))
                {
                    result = true;
                }
                break;
            case "2":// 2-系统运行（原其它告警）
                // 用户过滤条件-告警对象
                String dbObjectId = noticeSortInfo.getObjectId();
                List<String> dbObjectIds = Arrays.asList(dbObjectId.split(","));
                if (dbObjectIds.contains(objectid))
                {
                    result = true;
                }
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 判断过滤规则是否包含告警包中的objectid或为其上层组织
     *
     * @param objecttype     告警对象类型0-场站告警，1-设备告警，101-地理监测
     * @param objectId       告警对象(wtid/wfid/组织ID)
     * @param noticeSortInfo 用户的消息过滤规则
     * @return 判断结果
     */
    private static boolean isUpGroup(String objecttype, String objectId, NoticeSortInfo noticeSortInfo)
    {
        // 判断结果
        boolean result = false;

        try
        {
            if ("-1".equals(noticeSortInfo.getDevicetype()))
            {
                return true;
            }

            // 兼容objectId有可能是0开头的组织ID(内存中组织id全部转为int再转为string以去掉前面的0) 2020.02.28 fcy
            String objectid = objectId;
            if (objectid.startsWith("0"))
            {
                objectid = String.valueOf(Integer.parseInt(objectId));
            }

            // 用户过滤条件-告警对象
            String dbObjectId = noticeSortInfo.getObjectId();

            // 0-场站告警或101-地理监测(objectid是风场Id/组织ID，属于过滤规则中配置的风场或其上层组织)
            if ("0".equals(objecttype) || "101".equals(objecttype))
            {
                // 根据不同设备类型判断
                switch (noticeSortInfo.getDevicetype())
                {
                    case "1":// 1-电场
                    case "2":// 2-区域
                    case "3":// 3-集团
                        result = isConformGroupId(objectid, dbObjectId);
                        break;
                    default:
                        break;
                }
            }
            // 1-设备告警(objectid是设备Id，属于过滤规则中配置的设备或其上层组织)
            else if ("1".equals(objecttype))
            {
                // 风场Id
                String wfid = MemoryData.getDeviceInfoList().get(objectid).getWfId();

                // 根据不同设备类型判断
                switch (noticeSortInfo.getDevicetype())
                {
                    case "0":// 0-设备
                        List<String> dbObjectIds = Arrays.asList(dbObjectId.split(","));
                        if (dbObjectIds.contains(objectid))
                        {
                            result = true;
                        }
                        break;
                    case "1":// 1-电场
                    case "2":// 2-区域
                    case "3":// 3-集团
                        result = isConformGroupId(wfid, dbObjectId);
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception e)
        {
            logger.error("objecttype:" + objecttype + "objectId:" + objectId);
            logger.error(e);
        }

        return result;
    }

    /**
     * 判断是否符合过滤规则-告警事件(基础告警)
     *
     * @param info           告警关联信息
     * @param noticeSortInfo 用户的消息过滤规则
     * @return 判断结果
     */
    private static boolean isConformInfo(String info, NoticeSortInfo noticeSortInfo)
    {
        // 判断结果
        boolean result = false;
        // 取得过滤规则表中的告警关联信息
        String sortInfo = noticeSortInfo.getInfo();

        // 判断过滤规则是否包含告警包中的info
        if (null != info && !info.isEmpty())
        {
            if (sortInfo.contains(info) || sortInfo.contains(BusinessFunc.soeIECTonormalIEC(info)))
            {
                return true;
            }

            switch (info)
            {
                case IecPathDef.DEVEVT:// 设备事件IEC量
                    if (sortInfo.contains("@Eventdata") || sortInfo.contains("@Eventdataend"))
                    {
                        result = true;
                    }
                    break;
                case IecPathDef.DEVALARM:// 设备警告IEC量
                    if (sortInfo.contains("@Alarmdata") || sortInfo.contains("@Alarmdataend"))
                    {
                        result = true;
                    }
                    break;
                case IecPathDef.DEVERROR:// 设备故障IEC量
                    if (sortInfo.contains("@Faultdata") || sortInfo.contains("@Faultdataend"))
                    {
                        result = true;
                    }
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    /**
     * 判断是否需要屏蔽(集团级屏蔽用，某组织的用户设置屏蔽规则后，对该组织下的所有符合规则的告警进行屏蔽),调用方在【程序启动】/【修改配置】时需调用DatalogicInit.init(dbOper)
     *
     * @param wfid       风场ID
     * @param systemid   告警系统
     * @param objectid   告警对象
     * @param levelid    告警级别
     * @param typeid     告警分类（二级分类）
     * @param info       告警事件（关联信息）
     * @param objecttype 告警对象类型，0 场站告警 1设备告警 2 其它告警
     * @param iecVal     关联值
     * @param filtertype 过滤类型，0-屏蔽，2-风格
     * @return 判断结果（true:【屏蔽】，false:【不屏蔽】）,用户id
     */
    public static HashMap<String, Object> whetherToBlock(String wfid, String systemid, String objectid, String levelid, String typeid, String info, String objecttype, String iecVal,
            Filtertype filtertype)
    {

        // 判断结果
        HashMap<String, Object> returnData = new HashMap<String, Object>();
        boolean flg = false;
        String userid = "";

        HashMap<String, HashMap<String, NoticeSortInfo>> noticeSortList = MemoryData.getNoticeSortInfo();
        try
        {
            // 内存中消息过滤规则不为null 且 传入的告警系统不为null
            if (null != noticeSortList && null != systemid)
            {
                outterLoop:
                for (Entry<String, HashMap<String, NoticeSortInfo>> entryList : noticeSortList.entrySet())
                {
                    // 取得userid
                    userid = entryList.getKey();

                    HashMap<String, NoticeSortInfo> noticeSortInfoList = entryList.getValue();
                    // 循环该用户的所有消息过滤规则
                    for (NoticeSortInfo noticeSortInfo : noticeSortInfoList.values())
                    {
                        // 判断过滤类型是否符合
                        if (!String.valueOf(filtertype.getResult()).equals(noticeSortInfo.getFiltertype()))
                        {
                            continue;
                        }

                        // 判断是否符合过滤规则-组织机构id
                        if (isConformGroupId(wfid, noticeSortInfo.getGroupId()))
                        {
                            // 判断告警报文与某条规则是否匹配
                            if (isConformFilterRule(systemid, objectid, levelid, typeid, info, objecttype, iecVal, filtertype, noticeSortInfo))
                            {
                                flg = true;
                                break outterLoop;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            flg = false;
        }

        returnData.put("flg", flg);
        returnData.put("usenum", userid);
        return returnData;
    }

    /**
     * 判断告警报文与某条指定的过滤规则是否匹配(集团级风格用，某组织的用户设置规则后，对该组织下的所有符合规则的告警生效蔽),调用方在【程序启动】/【修改配置】时需调用DatalogicInit.init(dbOper)
     *
     * @param wfid       告警报文-风场ID
     * @param systemid   告警报文-告警系统
     * @param objectid   告警对象
     * @param levelid    告警级别
     * @param typeid     告警分类（二级分类）
     * @param info       告警事件（关联信息）
     * @param objecttype 告警对象类型，0 场站告警 1设备告警 2 其它告警
     * @param iecVal     关联值
     * @param filtertype 过滤类型，0-屏蔽，1-订阅，2-风格
     * @param noid       noticesort的主键（自增id）
     * @return 判断结果（true:【匹配】，false:【不匹配】）
     */
    public static boolean isConformFilterRules(String wfid, String systemid, String objectid, String levelid, String typeid, String info, String objecttype, String iecVal, Filtertype filtertype,
            String noid)
    {
        // 是否匹配
        boolean flg = false;

        // noticesort的主键（自增id）是否为null或空
        if (StringUtils.isEmpty(noid))
        {
            return false;
        }

        // 内存中的所有过滤规则
        HashMap<String, HashMap<String, NoticeSortInfo>> noticeSortList = MemoryData.getNoticeSortInfo();

        // 在所有过滤规则中找到这条规则，并判断是否匹配
        outterLoop:
        for (HashMap<String, NoticeSortInfo> noticeSortInfos : noticeSortList.values())
        {
            for (Map.Entry<String, NoticeSortInfo> entry : noticeSortInfos.entrySet())
            {
                if (noid.equals(entry.getKey()))
                {
                    // 过滤规则（告警风格）
                    NoticeSortInfo noticeSortInfo = entry.getValue();

                    // 判断是否符合过滤规则-组织机构id
                    if (isConformGroupId(wfid, noticeSortInfo.getGroupId()))
                    {
                        // 判断告警报文与某条规则是否匹配
                        if (isConformFilterRule(systemid, objectid, levelid, typeid, info, objecttype, iecVal, filtertype, noticeSortInfo))
                        {
                            flg = true;
                            break outterLoop;
                        }
                    }
                }
            }
        }

        return flg;
    }

    /**
     * 判断告警报文与某条规则是否匹配
     *
     * @param systemid       告警报文-告警系统
     * @param objectid       告警对象
     * @param levelid        告警级别
     * @param typeid         告警分类（二级分类）
     * @param info           告警事件（关联信息）
     * @param objecttype     告警对象类型，0 场站告警 1设备告警 2 其它告警
     * @param iecVal         关联值
     * @param filtertype     过滤类型，0-屏蔽，2-风格
     * @param noticeSortInfo 消息过滤规则
     * @return 判断结果（true:【匹配】，false:【不匹配】）
     */
    public static boolean isConformFilterRule(String systemid, String objectid, String levelid, String typeid, String info, String objecttype, String iecVal, Filtertype filtertype,
            NoticeSortInfo noticeSortInfo)
    {
        // 是否匹配
        boolean flg = false;

        // 对每一条屏蔽规则添加异常处理，防止异常数据在某条规则判断时抛异常不判断剩下的规则——2019.08.23
        try
        {
            // 消息过滤规则为null或者传入的告警系统为null
            if (null == noticeSortInfo || null == systemid)
            {
                return false;
            }

            // 判断过滤类型是否符合
            if (!String.valueOf(filtertype.getResult()).equals(noticeSortInfo.getFiltertype()))
            {
                return false;
            }

            // 判断是否符合过滤规则-告警系统
            if ("-1".equals(noticeSortInfo.getSystemId()) || systemid.equals(noticeSortInfo.getSystemId()))
            {
                // 判断是否符合过滤规则-告警对象
                if (("-1".equals(noticeSortInfo.getObjecttype()) || objecttype.equals(noticeSortInfo.getObjecttype())) && isConformObjectid(objecttype, objectid, noticeSortInfo))
                {
                    String[] levelids = noticeSortInfo.getLevelId().split(",");
                    outterLoop:
                    for (int i = 0; i < levelids.length; i++)
                    {
                        // 判断是否符合过滤规则-告警级别
                        if ("-1".equals(levelids[i].trim()) || levelid.equals(levelids[i].trim()))
                        {
                            // 判断是否符合过滤规则-二级分类
                            if ("-1".equals(noticeSortInfo.getTypeId()))
                            {
                                flg = true;
                                break;
                            }
                            else
                            {
                                String[] typeids = noticeSortInfo.getTypeId().split(",");
                                for (int j = 0; j < typeids.length; j++)
                                {
                                    // 在符合过滤规则-二级分类的前提下，判断是否符合过滤规则-告警事件
                                    if (typeid.equals(typeids[j].trim()) && ("-1".equals(noticeSortInfo.getInfo()) || noticeSortInfo.getInfo().isEmpty() || isConformInfoAndIecVal(info, noticeSortInfo,
                                            iecVal, typeid)))
                                    {
                                        flg = true;
                                        break outterLoop;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }

        return flg;
    }

    /**
     * 判断传入的【风场ID/组织ID】是否属于传入【数据库组织机构id】
     *
     * @param wfId      风场ID/组织ID
     * @param dbGroupId 用户过滤条件-数据库组织机构id（支持多组织id，中间用英文逗号隔开）
     * @return 判断结果 （true-属于 false-不属于）
     */
    public static boolean isConformGroupId(String wfId, String dbGroupId)
    {
        // 兼容wfId有可能是0开头的组织ID(内存中组织id全部转为int再转为string以去掉前面的0)2020.02.28 fcy
        String wfid = wfId;
        if (wfid.startsWith("0"))
        {
            wfid = String.valueOf(Integer.parseInt(wfId));
        }

        // 判断结果
        boolean result = false;

        // 兼容以前的旧配置，升级后，旧配置会通过脚本升级（填上用户对应的组织ID），未升级的则不屏蔽了
        if (dbGroupId == null || dbGroupId.isEmpty())
        {
            return false;
        }

        // 兼容wfId有可能是0开头的组织ID（内存中组织id全部转为int再转为string以去掉前面的0）2020.02.28 fcy
        List<String> dbTempgroupIds = Arrays.asList(dbGroupId.split(","));
        List<String> dbGroupIds = new ArrayList<>();
        for (int i = 0; i < dbTempgroupIds.size(); i++)
        {
            String tempGroupId = dbTempgroupIds.get(i);
            if (tempGroupId.startsWith("0"))
            {
                dbGroupIds.add(String.valueOf(Integer.parseInt(tempGroupId)));
            }
            else
            {
                dbGroupIds.add(tempGroupId);
            }
        }

        // 区域Id
        String areaid = "";
        // 集团Id
        String groupid = "";
        // 顶层集团Id
        String topgroupid = "";
        if (MemoryData.getGroupInfo().containsKey(wfid))
        {
            areaid = MemoryData.getGroupInfo().get(wfid).getParentId();
            if (MemoryData.getGroupInfo().containsKey(areaid))
            {
                groupid = MemoryData.getGroupInfo().get(areaid).getParentId();
                if (MemoryData.getGroupInfo().containsKey(groupid))
                {
                    topgroupid = MemoryData.getGroupInfo().get(groupid).getParentId();
                }
            }
        }

        // 判断过滤规则的组织机构id是否包含告警包中的wfid或为其上层组织
        if (dbGroupIds.contains(wfid) || dbGroupIds.contains(areaid) || dbGroupIds.contains(groupid) || dbGroupIds.contains(topgroupid))
        {
            result = true;
        }

        return result;
    }

    /**
     * 根据不同聚合方式计算传入的数值集合的聚合值
     *
     * @param values   数值集合
     * @param arrgType 聚合方式
     * @return 数值集合的聚合值
     */
    public static BigDecimal arrgCompute(List<Object> values, ArrgType arrgType)
    {
        BigDecimal result = null;

        if (!values.isEmpty())
        {
            // 根据不同聚合方式计算
            if (arrgType.equals(ArrgType.SUM))
            {
                for (Object val : values)
                {
                    BigDecimal newVal = new BigDecimal(String.valueOf(val));
                    if (null == result)
                    {
                        // 第一个有效的值
                        result = newVal;
                    }
                    else
                    {
                        result = result.add(newVal);
                    }
                }
            }
            else if (arrgType.equals(ArrgType.AVG))
            {
                for (Object val : values)
                {
                    BigDecimal newVal = new BigDecimal(String.valueOf(val));
                    if (null == result)
                    {
                        // 第一个有效的值
                        result = newVal;
                    }
                    else
                    {
                        result = result.add(newVal);
                    }
                }
                result = result.divide(new BigDecimal(values.size()), 4, RoundingMode.HALF_UP); // 设置精度为4位小数;;
            }
            else if (arrgType.equals(ArrgType.max))
            {
                for (Object val : values)
                {
                    BigDecimal newVal = new BigDecimal(String.valueOf(val));
                    if (null == result)
                    {
                        // 第一个有效的值
                        result = newVal;
                    }
                    else
                    {
                        if (newVal.compareTo(result) > 0)
                        {
                            result = newVal;
                        }
                    }
                }
            }
            else if (arrgType.equals(ArrgType.min))
            {
                for (Object val : values)
                {
                    BigDecimal newVal = new BigDecimal(String.valueOf(val));
                    if (null == result)
                    {
                        // 第一个有效的值
                        result = newVal;
                    }
                    else
                    {
                        if (newVal.compareTo(result) < 0)
                        {
                            result = newVal;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 判断是否符合过滤规则-告警事件(基础告警)
     *
     * @param info           告警关联信息
     * @param noticeSortInfo 用户的消息过滤规则
     * @param iecVal         关联值
     * @param typeId         告警分类（二级分类）
     * @return 判断结果
     */
    private static boolean isConformInfoAndIecVal(String info, NoticeSortInfo noticeSortInfo, String iecVal, String typeId)
    {
        // 判断结果
        boolean result = false;
        // 取得过滤规则表中的告警关联信息
        String sortInfo = noticeSortInfo.getInfo();

        // 判断过滤规则是否包含告警包中的info
        if (null != info && !info.isEmpty())
        {
            String[] dbInfos = sortInfo.split(",");
            for (int i = 0; i < dbInfos.length; i++)
            {
                String dbInfo = dbInfos[i].trim();

                // 判断是否要屏蔽到告警关联信息下的iecVal
                if (!dbInfo.contains(":"))
                {
                    // 按照逗号分割后的dbInfo可能是存的typeid，可能是告警关联信息（告警码或者iec路径）
                    if (dbInfo.equals(typeId) || dbInfo.equals(info) || dbInfo.equals(BusinessFunc.soeIECTonormalIEC(info)))
                    {
                        return true;
                    }

                    switch (info)
                    {
                        case IecPathDef.DEVEVT:// 设备事件IEC量
                            if (dbInfo.contains("@Eventdata") || dbInfo.contains("@Eventdataend"))
                            {
                                result = true;
                            }
                            break;
                        case IecPathDef.DEVALARM:// 设备警告IEC量
                            if (dbInfo.contains("@Alarmdata") || dbInfo.contains("@Alarmdataend"))
                            {
                                result = true;
                            }
                            break;
                        case IecPathDef.DEVERROR:// 设备故障IEC量
                            if (dbInfo.contains("@Faultdata") || dbInfo.contains("@Faultdataend"))
                            {
                                result = true;
                            }
                            break;
                        default:
                            break;
                    }

                    if (result)
                    {
                        return true;
                    }
                }
                // 判断是否符合过滤规则-iecVal(所有的值均在屏蔽规则中，才将该告警屏蔽。)
                else if ((dbInfo.split(":")[0].equals(info) || dbInfo.split(":")[0].equals(BusinessFunc.soeIECTonormalIEC(info))) && isConformAllIecVal(dbInfo, iecVal))
                {
                    return true;
                }
            }
        }

        return result;
    }

    /**
     * 判断是否符合过滤规则-iecVal(所有的值均在屏蔽规则中，才将该告警屏蔽。)
     *
     * @param dbInfo 过滤规则表中的告警关联信息(按逗号切割后的单个iec:iecVal)
     * @param iecVal 关联值
     * @return 判断结果
     */
    private static boolean isConformAllIecVal(String dbInfo, String iecVal)
    {
        // 判断结果
        boolean result = false;

        if (iecVal != null && !iecVal.isEmpty())
        {
            // 当且仅当所有的值均在屏蔽规则中，才将该告警屏蔽。
            String[] dbVals = dbInfo.split(":")[1].split(";");
            String[] iecVals = iecVal.split(";");
            boolean flg = true;
            for (int j = 0; j < iecVals.length; j++)
            {
                // 调用binarySearch()方法前要先调用sort方法对数组进行排序，否则得出的返回值不定，这是二分搜索算法决定的。
                Arrays.sort(dbVals);
                if (Arrays.binarySearch(dbVals, iecVals[j].trim()) < 0)
                {
                    flg = false;
                    break;
                }
            }
            if (flg)
            {
                return true;
            }
        }

        return result;
    }

    /**
     * 根据设备编号和数据时间获取该条数据对应的协议编号
     *
     * @param wtid    设备编号
     * @param rectime 时间(传入参数为null或空，则返回wtinfo表中对应协议id)
     * @return 协议编号
     */
    public static String getWtProtocolidByRectime(String wtid, String rectime)
    {
        String protocolid = "";

        // 首先获取内存中该设备最新对应的协议Id
        if (MemoryData.getDeviceInfoList().containsKey(wtid))
        {
            protocolid = MemoryData.getDeviceInfoList().get(wtid).getWtInfoProtocolId();
        }

        try
        {
            // 获取内存中该设备该时刻对应的协议Id
            if (MemoryData.getWTHISPROTOCOLLIST().containsKey(wtid) && !StringUtils.isEmpty(rectime))
            {
                Calendar rectimeCalendar = DataAsFunc.parseDateTime(rectime);

                List<WtHisProtocolInfo> wtHisProtocolInfos = MemoryData.getWTHISPROTOCOLLIST().get(wtid);
                for (int i = 0; i < wtHisProtocolInfos.size(); i++)
                {
                    Calendar begintime = wtHisProtocolInfos.get(i).getBegintime();
                    Calendar endtime = wtHisProtocolInfos.get(i).getEndtime();
                    if (rectimeCalendar.compareTo(begintime) >= 0 && (null == endtime || rectimeCalendar.compareTo(endtime) < 0))
                    {
                        protocolid = wtHisProtocolInfos.get(i).getProtocolId();
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e);
        }

        return protocolid;
    }

    /**
     * 根据设备编号和数据时间 判断该时刻该设备使用的是否时最新协议
     *
     * @param wtid    设备编号
     * @param rectime 时间(传入参数为null或空，则返回false)
     * @return 协议编号
     */
    public static boolean isNewProtocolidByRectime(String wtid, String rectime)
    {
        boolean[] isNewProtocolid = { false };
        try
        {
            // 获取内存中该设备该时刻对应的协议Id
            if (MemoryData.getWTHISPROTOCOLLIST().containsKey(wtid) && !StringUtils.isBlank(rectime))
            {
                Calendar rectimeCalendar = DataAsFunc.parseDateTime(rectime);

                List<WtHisProtocolInfo> wtHisProtocolInfos = MemoryData.getWTHISPROTOCOLLIST().get(wtid);

                wtHisProtocolInfos.stream().map(info -> {
                    if (rectimeCalendar.compareTo(info.getBegintime()) > 0 && (null == info.getEndtime()))
                    {
                        isNewProtocolid[0] = true;
                    }
                    return info;
                }).collect(Collectors.toList());
            }
        }
        catch (Exception e)
        {
            logger.error("BusinessFunc_isNewProtocolidByRectime" + e.getClass().getName(), e);
        }

        return isNewProtocolid[0];
    }

    /**
     * 过滤外键关系方法
     *
     * @param tableNames 传入选择的表名集合
     * @return 在传入表集合中不存在的外键引用表集合
     * @throws Exception 异常
     */
    public static List<TableName> filterFkTables(List<TableName> tableNames, String dbUrl, String dbUserName, String dbPassWord) throws Exception
    {
        List<TableName> result = new ArrayList<>();
        DbOperBase operBase = new DbOperBase(DbAssistant.DatabaseType.Postgre, dbUrl, dbUserName, dbPassWord);
        try
        {
            operBase.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("select ");
            builder.append("a.conrelid as tableId,c.nspname as tableNsp,b.relname as tableName, ");
            builder.append("a.confrelid as pTableId,e.nspname as pTableNsp,d.relname as pTableName ");
            builder.append("from pg_constraint a ");
            builder.append("left join pg_class b on a.conrelid = b.oid ");
            builder.append("left join pg_namespace c on a.connamespace = c.oid ");
            builder.append("left join pg_class d on a.confrelid = d.oid ");
            builder.append("left join pg_namespace e on d.relnamespace = e.oid ");
            builder.append("where a.contype = \'f\' ");
            ResultSet rs = operBase.getResultSet(builder.toString(), null);
            Map<String, List<String>> fks = new HashMap<>();
            while (rs.next())
            {
                String tableNsp = rs.getString(2);
                String tableName = rs.getString(3);
                String pTableNsp = rs.getString(5);
                String pTableName = rs.getString(6);
                String key = tableNsp + sep + tableName;

                fks.computeIfAbsent(key, k -> new ArrayList<>()).add(pTableNsp + sep + pTableName);
            }
            operBase.closeConn();

            // 传入表集合
            Set<String> fkMainTables = new HashSet<>();
            // 外键表集合
            Set<String> fkTables = new HashSet<>();
            for (TableName tableName : tableNames)
            {
                fkMainTables.add(tableName.getSchemaName() + sep + tableName.getTableName());
                List<String> pTableStr = fks.get(tableName.getSchemaName() + sep + tableName.getTableName());
                if (pTableStr != null)
                {
                    // 传入表存在外键表
                    fkTables.addAll(pTableStr);
                }
            }
            // 删除引用外键表中已经在传入表中存在的表
            fkTables.removeAll(fkMainTables);
            for (String fkTableStr : fkTables)
            {
                TableName tableName = new TableName();
                String[] names = fkTableStr.split(sep, -1);
                tableName.setSchemaName(names[0]);
                tableName.setTableName(names[1]);
                result.add(tableName);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet
                    .throwExpByResCode("BusinessFunc_filterFkTables_1", new String[] { "tableNames", "dbUrl", "dbUserName", "dbPassWord" }, new Object[] { tableNames, dbUrl, dbUserName, dbPassWord },
                            e);
        }
        finally
        {
            try
            {
                operBase.closeConn();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_filterFkTables_1", new String[] { "tableNames", "dbUrl", "dbUserName", "dbPassWord" },
                        new Object[] { tableNames, dbUrl, dbUserName, dbPassWord }, e);
            }
        }
        return result;
    }

    /**
     * 查询所有的非系统自定义模式名
     *
     * @param dbUrl      数据库连接URL
     * @param dbUserName 用户名
     * @param dbPassWord 密码
     * @return 模式名集合
     * @throws Exception 异常
     */
    public static List<String> queryAllNotSystemSchemas(String dbUrl, String dbUserName, String dbPassWord) throws Exception
    {
        List<String> schemaList = new ArrayList<>();
        DbOperBase operBase = new DbOperBase(DbAssistant.DatabaseType.Postgre, dbUrl, dbUserName, dbPassWord);
        try
        {
            operBase.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("select DISTINCT table_schema from information_schema.tables ");
            builder.append("where table_schema not in ('pg_catalog','information_schema') ");
            ResultSet rs = operBase.getResultSet(builder.toString(), null);
            while (rs.next())
            {
                String table_schema = rs.getString(1);
                schemaList.add(table_schema);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("BusinessFunc_queryAllNotSystemSchemas_1", new String[] { "dbUrl", "dbUserName", "dbPassWord" }, new Object[] { dbUrl, dbUserName, dbPassWord }, e);
        }
        finally
        {
            try
            {
                operBase.closeConn();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_queryAllNotSystemSchemas_1", new String[] { "dbUrl", "dbUserName", "dbPassWord" }, new Object[] { dbUrl, dbUserName, dbPassWord }, e);
            }
        }
        return schemaList;
    }

    /**
     * 根据模式名查询表名
     *
     * @param schemaName 模式名
     * @param dbUrl      数据库连接信息
     * @param dbUserName 用户名
     * @param dbPassWord 密码
     * @return 表名集合
     * @throws Exception 异常
     */
    public static List<TableName> queryAllTableNameBySchema(String schemaName, String dbUrl, String dbUserName, String dbPassWord) throws Exception
    {
        List<TableName> tableNameList = new ArrayList<>();
        DbOperBase operBase = new DbOperBase(DbAssistant.DatabaseType.Postgre, dbUrl, dbUserName, dbPassWord);
        try
        {
            operBase.openConn();
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT table_schema,table_name FROM information_schema.tables ");
            builder.append("where table_schema not in ('pg_catalog','information_schema') ");
            builder.append("and table_schema=?");
            ResultSet rs = operBase.getResultSet(builder.toString(), new Object[] { schemaName });
            while (rs.next())
            {
                String schema = rs.getString(1);
                String table = rs.getString(2);
                TableName tableName = new TableName();
                tableName.setSchemaName(schema);
                tableName.setTableName(table);

                tableNameList.add(tableName);
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("BusinessFunc_queryAllTableNameBySchema_1", new String[] { "schemaName", "dbUrl", "dbUserName", "dbPassWord" },
                    new Object[] { schemaName, dbUrl, dbUserName, dbPassWord }, e);
        }
        finally
        {
            try
            {
                operBase.closeConn();
            }
            catch (Exception e)
            {
                DataAsExpSet.throwExpByResCode("BusinessFunc_queryAllTableNameBySchema_1", new String[] { "schemaName", "dbUrl", "dbUserName", "dbPassWord" },
                        new Object[] { schemaName, dbUrl, dbUserName, dbPassWord }, e);
            }
        }
        return tableNameList;
    }
}
