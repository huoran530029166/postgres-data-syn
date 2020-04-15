package com.goldwind.datalogic.businesslogic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.businesslogic.model.Losstime;
import com.goldwind.datalogic.businesslogic.model.TimeCalendar;
import com.goldwind.datalogic.businesslogic.util.DataEnumObject.LossType;

/**
 * 数据聚合函数类
 * 
 * @author 谭璟
 *
 */
public class DataTogetherFunction
{

    /**
     * 按照日聚合
     * 
     * @param listhandle
     *            损失类型集合
     * @param hash
     *            数据集合
     * @param energyUseOneDataList
     *            存放数据容器
     * @return 返回对应数据集合
     */
    public static Map<Integer, Losstime> getDayDataAgg(List<Integer> setidList, String deviceID, Map<String, Losstime> hash, List<Losstime> energyUseOneDataList)
    {
        Map<Integer, Losstime> result = new HashMap<>();
        // 首先去过滤hash把故障停机的数据检索出来
        for (Losstime bean : hash.values())
        {
            if (bean.getSetid() == -1 || !setidList.contains(bean.getSetid()))
            {
                continue;
            }
            if (result.containsKey(bean.getSetid()))
            {
                Losstime object = result.get(bean.getSetid());
                object.setTheory(object.getTheory() + bean.getTheory());
                object.setReal(object.getReal() + bean.getReal());
                object.setCount(object.getCount() + bean.getCount());
                object.setBmrkloss(object.getBmrkloss() + bean.getBmrkloss());
                object.setLoss(object.getLoss() + bean.getLoss());
                object.setElec(object.getElec() + bean.getElec());
            }
            else
            {
                Losstime object = new Losstime(deviceID, bean.getSetid(), 0, 0, 0, 0, 0);
                object.setTheory(bean.getTheory());
                object.setReal(bean.getReal());
                object.setCount(bean.getCount());
                object.setBmrkloss(bean.getBmrkloss());
                object.setLoss(bean.getLoss());
                object.setElec(bean.getElec());
                object.setDeviceId(bean.getDeviceId());
                result.put(bean.getSetid(), object);
            }
            // 返回所有明细
            energyUseOneDataList.add(bean);

        }
        return result;
    }

    /**
     * 按照小时聚合
     * 
     * @param listhandle
     *            集合类型
     * @param hash
     *            除故障停机的数据集合
     * @param listime
     *            时间集合
     * @return list
     * @throws ParseException
     *             异常
     */
    public static List<Losstime> getHourDataPart(List<Integer> listhandle, Map<String, Losstime> hash, List<TimeCalendar> listime) throws ParseException
    {
        List<Losstime> list = new ArrayList<>();
        for (Integer data : listhandle)
        {
            for (TimeCalendar obj : listime)
            {
                Losstime object = getDataObject(obj, hash, data);
                list.add(object);
            }
        }
        return list;
    }

    /***
     * 得到时间段 聚合
     * 
     * @param setidList
     *            损失电量集合
     * @param hash
     *            除了故障停机集合
     * @return list 集合
     * @throws ParseException
     *             异常
     */
    public static List<Losstime> getTimeDataPart(List<Integer> setidList, Map<String, Losstime> hash) throws DataAsException, ParseException
    {
        Map<Integer, List<String>> rectimeListHash = new HashMap<>();
        for (Losstime bean : hash.values())
        {
            // 在时间段统计中 2001和2008合并成1个
            if (bean.getSetid() == 2008)
            {
                bean.setSetid(2001);
            }
            if (rectimeListHash.containsKey(bean.getSetid()))
            {
                List<String> rectimeList = rectimeListHash.get(bean.getSetid());
                rectimeList.add(bean.getTime());
            }
            else
            {
                List<String> rectimeList = new ArrayList<>();
                rectimeList.add(bean.getTime());
                rectimeListHash.put(bean.getSetid(), rectimeList);
            }
        }

        List<Losstime> losstimeList = new ArrayList<>();
        for (Integer setID : setidList)
        {
            List<String> rectimeList = rectimeListHash.get(setID);
            if (rectimeList != null)
            {
                List<Losstime> listresult = getDeviceTimeLoss(setID, rectimeList, hash);
                for (Losstime da : listresult)
                {
                    losstimeList.add(da);
                }
            }
        }
        return losstimeList;
    }

    /**
     * 获取setid 对应数据集合
     * 
     * @param obj
     *            时间对象
     * @param hash
     *            对象容器
     * @param setid
     *            损失电量类型
     * @return 返回setid对应的数据集合
     * @throws ParseException
     *             异常
     */
    private static Losstime getDataObject(TimeCalendar obj, Map<String, Losstime> hash, int setid) throws ParseException
    {
        Losstime object = new Losstime(null, setid, 0, 0, 0, 0, 0);
        object.setStartttime(obj.getStart());
        object.setEndtime(obj.getEnd());
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar ca = Calendar.getInstance();
        ca.setTime(SDF.parse(obj.getStart()));

        Calendar ca1 = Calendar.getInstance();
        ca1.setTime(SDF.parse(obj.getEnd()));

        while (ca1.compareTo(ca) >= 0)
        {
            String time = SDF.format(ca.getTime());
            if (hash.containsKey(time))
            {
                object.setBmrkloss(object.getBmrkloss() + hash.get(time).getBmrkloss());
                object.setCount(object.getCount() + hash.get(time).getCount());
                object.setReal(object.getReal() + hash.get(time).getReal());
                object.setTheory(object.getTheory() + hash.get(time).getTheory());
                object.setLoss(object.getLoss() + hash.get(time).getLoss());
                object.setSetid(hash.get(time).getSetid());
            }
            ca.add(Calendar.MINUTE, 1);
        }
        return object;
    }

    /**
     * 
     * @param setID
     *            损失电量类型
     * @param rectimeList
     *            对应list 容器
     * @param hj
     *            时间对应对象
     * @return 返回时间段
     * @throws ParseException
     *             异常
     */
    private static List<Losstime> getDeviceTimeLoss(int setID, List<String> rectimeList, Map<String, Losstime> hj) throws ParseException
    {
        List<Losstime> listresult = new ArrayList<>();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 得到时间段
        List<TimeCalendar> listcal = DataTogetherFunction.getTimeCalendar(rectimeList);
        for (TimeCalendar time : listcal)
        {
            Losstime obj = getDeviceOject(time.getStartime(), time.getEndtime(), hj);
            listresult.add(obj);
        }
        if (setID == LossType.Dispatchelc.getResult())
        {
            for (Losstime obj : listresult)
            {
                Calendar ca = Calendar.getInstance();
                ca.setTime(SDF.parse(obj.getEndtime()));
                ca.add(Calendar.MINUTE, 1);
                obj.setEndtime(SDF.format(ca.getTime()));
            }
        }
        return listresult;
    }

    /**
     * 处理单个 时间段
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param hj
     *            集合
     * @return 返回时间段 数据统计
     */
    private static Losstime getDeviceOject(Calendar starttime, Calendar endtime, Map<String, Losstime> hj)
    {
        Losstime obj = new Losstime(null, 0, 0, 0, 0, 0, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        obj.setStartttime(sdf.format(starttime.getTime()));
        obj.setEndtime(sdf.format(endtime.getTime()));

        // 处理开始时间和结束时间相同的情况
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endtime.getTime());

        while (endCal.compareTo(starttime) >= 0)
        {
            String time = sdf.format(starttime.getTime());
            if (hj.containsKey(time))
            {
                obj.setBmrkloss(obj.getBmrkloss() + hj.get(time).getBmrkloss());
                obj.setCount(obj.getCount() + hj.get(time).getCount());
                obj.setReal(obj.getReal() + hj.get(time).getReal());
                obj.setTheory(obj.getTheory() + hj.get(time).getTheory());
                obj.setLoss(obj.getLoss() + hj.get(time).getLoss());
                obj.setSetid(hj.get(time).getSetid());
                obj.setDeviceId(hj.get(time).getDeviceId());
            }
            starttime.add(Calendar.MINUTE, 1);
        }
        return obj;
    }

    /**
     * 返回时间段容器
     * 
     * @param listdate
     *            时间容器
     * @return 返回时间段容器
     */
    public static List<TimeCalendar> getTimeCalendar(List<String> listdate)
    {
        List<TimeCalendar> list = new ArrayList<>();
        List<Calendar> listtime = new ArrayList<>();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try
        {

            for (String da : listdate)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(SDF.parse(da));
                listtime.add(cal);
            }

            // 排序
            Collections.sort(listtime, new Comparator<Calendar>()
            {
                @Override
                public int compare(Calendar o1, Calendar o2)
                {
                    return o1.compareTo(o2);
                }
            });

            if (listtime.size() == 1)
            {
                TimeCalendar obj = new TimeCalendar(listtime.get(0), listtime.get(0));
                list.add(obj);
            }
            else
            {
                int j = 1;
                for (int i = 0; i < listtime.size(); i++)
                {
                    boolean flag = true;
                    Calendar compare = listtime.get(i);

                    if (i == listtime.size() - 2)
                    {
                        Calendar compare_next = listtime.get(j);
                        // 说明连续
                        if (compare_next.getTimeInMillis() - compare.getTimeInMillis() == 60000)
                        {
                            TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(j));
                            list.add(obj);
                        }
                        else
                        {
                            TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(i));
                            list.add(obj);

                            TimeCalendar object = new TimeCalendar(listtime.get(j), listtime.get(j));
                            list.add(object);
                        }
                        break;
                    }
                    else
                    {
                        for (; j <= listtime.size(); j++)
                        {
                            Calendar compare_next = listtime.get(j);
                            if (flag)
                            {
                                // 说明是连续的
                                if (compare_next.getTimeInMillis() - compare.getTimeInMillis() == 60000)
                                {
                                    if (j == listtime.size() - 1)
                                    {
                                        TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(j));
                                        list.add(obj);
                                    }
                                    else
                                    {
                                        flag = false;
                                    }
                                }
                                else
                                {
                                    if (j != listtime.size() - 1)
                                    {
                                        TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(i));
                                        list.add(obj);
                                        j += 1;
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                int n = j - 1;
                                Calendar compare_last = listtime.get(n);
                                // 说明 不连续 然后 得到时间段
                                if (compare_next.getTimeInMillis() - compare_last.getTimeInMillis() != 60000)
                                {
                                    TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(n));
                                    list.add(obj);
                                    i = n;
                                    j += 1;
                                    break;
                                }
                                else
                                {
                                    if (j == listtime.size() - 1)
                                    {
                                        TimeCalendar obj = new TimeCalendar(listtime.get(i), listtime.get(j));
                                        list.add(obj);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
        return list;
    }
}
