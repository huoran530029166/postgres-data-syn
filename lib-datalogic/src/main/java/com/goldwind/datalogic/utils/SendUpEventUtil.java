package com.goldwind.datalogic.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 报信主站上送数据解析方法
 * 
 * @author jiangtao
 *
 */
public class SendUpEventUtil
{

    /**
     * 日志记录器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SendUpEventUtil.class);
    /**
     * 正常值
     */
    private final static String originFields = "seDataType,statId,deviceNo," + "cpuNo,regionId,groupId,itemNo,fun,inf,prepositionTime,originData,identityId," + "orderNum,isCache,protocolId";

    /**
     * 分解后所需的所有字段
     */
    private final static String fields = "beginTime,subTime,valueType," + "eventValue,ret,fan,fpt,cot,jpt," + "scl,dpi,dpiTime,result,waveFileName,lineName,"
            + "setId,userId,setType,setModel,groupType,info";
    /**
     * 异常告警信息/自检信息/开关量变位/压板状态变位/通讯状态变位信息上送(ASDU1上送) dataType 为0, data为：(value，begintime，subtime)
     * 
     */
    private final static String sendUpEvent0 = "eventValue,beginTime,subTime";

    /**
     * 保护动作信息上送 dataType为1, data为：(value|RET|FAN|FPT|begintime|subtime)
     * 
     */
    private final static String sendUpEvent1 = "eventValue,ret,fan,fpt,beginTime,subTime";

    /**
     * 异常告警信息/自检信息/开关量变位/压板状态变位/通讯状态变位/保护动作/故障量信息上送（ASDU10上送） dataType为2, data为：(cot，valuetype，value，time)
     * 
     */
    private final static String sendUpEvent2 = "cot,valueType,eventValue,beginTime";

    /**
     * 简要录波上送,faultTime这边被并入beginTime字段 dataType为3, data为：(FPT，JPT，SCL，DPI，DPItime，（result） ，faulttime，wavefilename，linename，subtime)
     */
    private final static String sendUpEvent3 = "fpt,jpt,scl,dpi,dpiTime,result,beginTime,waveFileName,lineName,subTime";

    /**
     * 前置与保信子站通讯状态 dataType为4,连接data为1，断开data为0
     */
    private final static String sendUpEvent4 = "eventValue";

    /**
     * 故障测距 dataType为5, SCL（短路位置），RET（相对时间）,FAN（故障序号），故障时间
     */
    private final static String sendUpEvent5 = "scl,ret,fan,beginTime";

    /**
     * 当下发控制指令控制成功后,由前置给数据处理发送了一条控制日志,数据处理将这条控制日志转化为上送告警 dataType为10,data为：{setid,userid,msg,settype,setmodel,grouptype,valuetype,value,{info}},msg的格式如(xx:xx)
     */
    private final static String sendUpEvent10 = "setId,userId,result,setType,setModel,groupType,valueType,eventValue,info";

    /**
     * 解析报信主站上送数据
     * 
     * @param netData
     *            报文(protectdata|datatype|statid|devid|cpuid|groupid|itemid|fun|inf|time|data|identityid|ordernum|iscache|protocolid)
     * 
     * @return "beginTime,subTime,valueType,eventValue,ret,fan,fpt,cot,jpt,scl,dpi,dpiTime,result,lineName"
     */
    public static Map<String, Object> parseSendUpInfo(String netData)
    {
        String[] arr = DataParse.removeParenth(netData).split("\\|", -1);

        return parseSendUpInfo(arr);
    }

    /**
     * 解析报信主站上送数据
     * 
     * @param infos
     *            去除左右括号并且被分割过的data
     * 
     * @return "beginTime,subTime,valueType,eventValue,ret,fan,fpt,cot,jpt,scl,dpi,dpiTime,result,lineName"
     */
    public static Map<String, Object> parseSendUpInfo(String[] infos)
    {
        if (infos == null || infos.length != 16)
        {
            return null;
        }
        Map<String, Object> fieldValues = new HashMap<>();
        String data = infos[11];
        data = data.substring(1, data.length() - 1);
        int dataType = Integer.parseInt(infos[1]);
        String result = null;
        // 简要录波比较特殊，有个result字段需要特殊处理
        if (3 == dataType || 10 == dataType)
        {
            Pattern p = Pattern.compile("\\{([^}]+)}");
            Matcher m = p.matcher(data);
            if (m.groupCount() == 0)
            {
                throw new IllegalArgumentException("解析录波数据失败");
            }
            m.find();
            result = m.group(1);
            data = m.replaceFirst("");
        }
        String[] dataInfos = data.split(",", -1), originArray = originFields.split(",");
        if (3 == dataType)
        {
            dataInfos[5] = result;
        }
        else if (10 == dataType)
        {
            dataInfos[8] = result;
        }
        List<String> resolveFieldName = Arrays.asList(fields.split(",")), particularFieldName = getParticularFieldName(dataType);
        // 先组装协议的原始数据
        for (int i = 1; i < infos.length; i++)
        {
            fieldValues.put(originArray[i - 1], StringUtils.isEmpty(infos[i]) ? "null" : infos[i]);
        }
        if (particularFieldName != null && particularFieldName.size() > 0)
        {
            for (int i = 0; i < particularFieldName.size(); i++)
            {
                if (i < dataInfos.length)
                {
                    fieldValues.put(particularFieldName.get(i), StringUtils.isEmpty(dataInfos[i]) ? "null" : dataInfos[i]);
                }
                else
                {
                    fieldValues.put(particularFieldName.get(i), "null");
                }
            }
            for (String fieldName : resolveFieldName)
            {
                if (!particularFieldName.contains(fieldName))
                {
                    fieldValues.put(fieldName, "null");
                }
            }
        }
        return fieldValues;
    }

    public static Map<String, Object> parseSendUpInfoForJava(String netData)
    {
        String[] arr = DataParse.removeParenth(netData).split("\\|", -1);

        return parseSendUpInfoForJava(arr);
    }

    public static Map<String, Object> parseSendUpInfoForJava(String[] infos)
    {
        if (infos == null || infos.length != 16)
        {
            return null;
        }
        Map<String, Object> fieldValues = new HashMap<>();
        String data = infos[11];
        data = data.substring(1, data.length() - 1);
        int dataType = Integer.parseInt(infos[1]);
        String result = null;
        // 简要录波比较特殊，有个result字段需要特殊处理
        if (3 == dataType || 10 == dataType)
        {
            Pattern p = Pattern.compile("\\{([^}]+)}");
            Matcher m = p.matcher(data);
            if (m.groupCount() == 0)
            {
                throw new IllegalArgumentException("解析录波数据失败");
            }
            m.find();
            result = m.group(1);
            data = m.replaceFirst("");
        }
        String[] dataInfos = data.split(",", -1), originArray = originFields.split(",");
        if (3 == dataType)
        {
            dataInfos[5] = result;
        }
        else if (10 == dataType)
        {
            dataInfos[8] = result;
        }
        List<String> resolveFieldName = Arrays.asList(fields.split(",")), particularFieldName = getParticularFieldName(dataType);
        // 先组装协议的原始数据
        for (int i = 1; i < infos.length; i++)
        {
            fieldValues.put(originArray[i - 1], infos[i]);
        }
        if (particularFieldName != null && particularFieldName.size() > 0)
        {
            for (int i = 0; i < particularFieldName.size(); i++)
            {
                if (i < dataInfos.length)
                {
                    fieldValues.put(particularFieldName.get(i), dataInfos[i]);
                }
                else
                {
                    fieldValues.put(particularFieldName.get(i), null);
                }
            }
            for (String fieldName : resolveFieldName)
            {
                if (!particularFieldName.contains(fieldName))
                {
                    fieldValues.put(fieldName, null);
                }
            }
        }
        return fieldValues;
    }

    /**
     * 获取类型
     * 
     * @param dataType
     *            数据类型
     * @return 数据
     */
    private static List<String> getParticularFieldName(int dataType)
    {
        List<String> particularFieldName;
        switch (dataType) {
            case 0:
                particularFieldName = Arrays.asList(sendUpEvent0.split(","));
                break;
            case 1:
                particularFieldName = Arrays.asList(sendUpEvent1.split(","));
                break;
            case 2:
                particularFieldName = Arrays.asList(sendUpEvent2.split(","));
                break;
            case 3:
                particularFieldName = Arrays.asList(sendUpEvent3.split(","));
                break;
            case 4:
                particularFieldName = Arrays.asList(sendUpEvent4.split(","));
                break;
            case 5:
                particularFieldName = Arrays.asList(sendUpEvent5.split(","));
                break;
            case 10:
                particularFieldName = Arrays.asList(sendUpEvent10.split(","));
                break;
            default:
                return null;
        }
        return particularFieldName;
    }

    // public static void main(String[] args)
    // {
    // // String ss = "0|aa|bb|cc|dd|ee|fun|inf|time|1,2018-09-11 14:40:00.000,2018-09-11 14:40:00.000|1|0|protocolid";
    // String[] ss = "protectdata|3|632523920|1|0|||14|1|2018-09-26
    // 13:42:38.000|{255,255,100,2,16,{220.100006;90.099998,220.199997;90.199997,220.300003;90.300003,220.399994;90.400002,220.500000;90.500000,220.600006;90.599998,220.699997;90.699997,220.800003;90.800003,220.899994;90.900002,221.100006;91.099998,221.199997;91.199997,221.300003;91.300003},2018-08-10
    // 03:29:26.214,2018-9-13$16:01:35warefile录波,中国江苏省无锡市新吴区新安街道一号线路,2018-08-11 10:00:31.431}|188|0|0"
    // .split("\\|", -1);
    // // String[] ss = { "protectdata", "1", "632523901", "1", "0", "", "", "11", "10", "2018-09-19 11:07:03.000", "{2,17731,512,1,2018-08-10 10:00:31.431,2018-08-10 10:00:31.431}", "12662", "0",
    // // "0" };
    // Map<String, Object> test = parseSendUpInfo(ss);
    // for (String key : test.keySet())
    // {
    // System.out.println(key + "----" + test.get(key));
    // }
    //
    // }

    /**
     * 移除括号
     * 
     * @param str
     *            原始数据
     * @return 移除括号后的数据
     */
    private static String[] breakDownInfo(String str)
    {
        Pattern p = Pattern.compile("([^,]*),");
        Matcher m = p.matcher(str);
        if (m.groupCount() == 0)
        {
            return null;
        }
        List<String> infos = new ArrayList<>();
        while (m.find())
        {
            infos.add(m.group(1));
            str = m.replaceFirst("");
            m = p.matcher(str);
        }
        infos.add(str);
        String[] array = new String[infos.size()];
        return infos.toArray(array);
    }

    /**
     * 解析报信主站上送数据
     * 
     * @param info
     *            去除左右括号并且被分割过的data
     * 
     * @return 解析为JSON格式的数据
     */
    public static JSONObject parseSendUpInfo(ProtectSendUpEvent info)
    {
        if (info == null)
        {
            return null;
        }
        JSONObject fieldValues = new JSONObject();
        String data = info.getData();
        data = data.substring(1, data.length() - 1);
        String result = null;
        int dataType = info.getDataType();
        // 简要录波比较特殊，有个result字段需要特殊处理
        if (3 == dataType || 10 == dataType)
        {
            Pattern p = Pattern.compile("\\{([^}]+)}");
            Matcher m = p.matcher(data);
            if (m.groupCount() == 0)
            {
                throw new IllegalArgumentException("解析录波数据失败");
            }
            m.find();
            result = m.group(1);
            data = m.replaceFirst("");
        }
        String[] dataInfos = data.split(",", -1);
        if (3 == dataType)
        {
            dataInfos[5] = result;
        }
        else if (10 == dataType)
        {
            dataInfos[8] = result;
        }
        List<String> resolveFieldName = Arrays.asList(fields.split(",")), particularFieldName = getParticularFieldName(dataType);
        // 先组装协议的原始数据
        fieldValues.put("dataType", info.getDataType());
        fieldValues.put("statId", info.getStatId());
        fieldValues.put("devId", info.getDevNo());
        fieldValues.put("cpuId", info.getCpuNo());
        fieldValues.put("regionId", info.getRegionId());
        fieldValues.put("groupId", info.getGroupId());
        fieldValues.put("itemId", info.getItemNo());
        fieldValues.put("fun", info.getFun());
        fieldValues.put("inf", info.getInf());
        fieldValues.put("time", info.getTime());
        fieldValues.put("data", info.getData());
        fieldValues.put("identityId", info.getIdentityId());
        fieldValues.put("orderNum", info.getOrderNum());
        fieldValues.put("isCache", info.getIsCache());
        fieldValues.put("protocolId", info.getProtocolId());
        fieldValues.put("rowModifier", "0000000000000000");
        if (particularFieldName != null && particularFieldName.size() > 0)
        {
            for (int i = 0; i < particularFieldName.size(); i++)
            {
                if (particularFieldName.get(i).equals("result"))
                {
                    particularFieldName.set(i, "incidentalResult");
                }
                try
                {
                    if (i < dataInfos.length)
                    {
                        fieldValues.put(particularFieldName.get(i), dataInfos[i]);
                        setProperty(info, particularFieldName.get(i), dataInfos[i]);
                    }
                    else
                    {
                        fieldValues.put(particularFieldName.get(i), null);
                        setProperty(info, particularFieldName.get(i), null);
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("protect substation translate alarm info occurred error\n" + e.getMessage());
                }
            }
            for (String fieldName : resolveFieldName)
            {
                if (!particularFieldName.contains(fieldName))
                {
                    if ("result".equals(fieldName))
                    {
                        fieldName = "incidentalResult";
                    }
                    try
                    {
                        fieldValues.put(fieldName, null);
                        setProperty(info, fieldName, null);
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("protect substation translate alarm info occurred error\n" + e.getMessage());
                    }
                }
            }
        }
        return fieldValues;
    }

    // 该方法的参数列表是一个类的 类名、成员变量、给变量的赋值
    private static void setProperty(Object obj, String PropertyName, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        // 获取obj类的字节文件对象
        Class c = obj.getClass();
        // 获取该类的成员变量
        Field f = c.getDeclaredField(PropertyName);
        // 取消语言访问检查
        f.setAccessible(true);
        // 给变量赋值
        if (f.getType() == String.class)
        {
            f.set(obj, value);
        }
        else if (f.getType() == Integer.class)
        {
            f.set(obj, Integer.parseInt(value));
        }
        else if (f.getType() == Double.class)
        {
            f.set(obj, Double.parseDouble(value));
        }
        else if (f.getType() == Long.class)
        {
            f.set(obj, Long.parseLong(value));
        }
    }
}
