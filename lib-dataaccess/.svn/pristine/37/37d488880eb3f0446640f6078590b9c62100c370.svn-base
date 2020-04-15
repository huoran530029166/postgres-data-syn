package com.goldwind.dataaccess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 数组操作类
 * 
 * @author 曹阳
 *
 */
public class ArrayOper
{
    /**
     * 将ArrayList转换为字符串数组
     * 
     * @param al
     *            arrayList
     * @return 字符串数组
     */
    public static String[] arrayListToArray(ArrayList<Object> al)
    {
        String[] arr = new String[al.size()];
        for (int i = 0; i < al.size(); i++)
        {
            arr[i] = String.valueOf(al.get(i));
        }
        return arr;
    }

    /**
     * 将数组转换为字符串,使用逗号分隔数组元素
     * 
     * @param srcArray
     *            数组列表
     * @return 字符串
     */
    public static String arrayToString(ArrayList<Object> srcArray)
    {
        return arrayListToString(srcArray, ",");
    }

    /**
     * 将数组列表转换为字符串,使用指定符号分隔数组元素
     * 
     * @param srcArray
     *            数组列表
     * @param splitSymbol
     *            分隔符
     * @return 字符串
     */
    public static String arrayListToString(ArrayList<Object> srcArray, String splitSymbol)
    {
        if (srcArray == null || srcArray.isEmpty())
        {
            return "";
        }
        String val = "";
        for (int i = 0; i < srcArray.size(); i++)
        {
            val += String.valueOf(srcArray.get(i));
            if (i < srcArray.size() - 1)
            {
                val += splitSymbol;
            }
        }
        return val;
    }

    /**
     * 将数组转换为字符串,使用指定符号分隔数组元素
     * 
     * @param srcArray
     *            数组
     * @param splitSymbol
     *            分隔符
     * @return 字符串
     */
    public static String arrayToString(Object[] srcArray, String splitSymbol)
    {
        if (srcArray == null || srcArray.length == 0)
        {
            return "";
        }
        String val = "";
        for (int i = 0; i < srcArray.length; i++)
        {
            if (srcArray[i] != null)// 判断元素知否为空，若为空则用null表示
            {
                if ("".equals(srcArray[i]))
                {
                    val += "null";
                }
                val += String.valueOf(srcArray[i]);
            }
            if (i < srcArray.length - 1)
            {
                val += splitSymbol;
            }
        }
        return val;
    }

    /**
     * 将数组转换为字符串,使用指定符号分隔数组元素
     * 
     * @param srcArray
     *            数组
     * @param splitSymbol
     *            分隔符
     * @return 字符串
     */
    public static String newAarrayToString(Object[] srcArray, String splitSymbol)
    {
        if (srcArray == null || srcArray.length == 0)
        {
            return "";
        }
        StringBuilder val = new StringBuilder();
        for (int i = 0; i < srcArray.length; i++)
        {
            if (srcArray[i] != null)// 判断元素知否为空，若为空则用null表示
            {
                if ("".equals(srcArray[i]))
                {
                    val.append("null");// += "null";
                }
                val.append(String.valueOf(srcArray[i]));
            }
            if (i < srcArray.length - 1)
            {
                val.append(splitSymbol);
            }
        }
        return val.toString();
    }

    /**
     * 将数据分解为全部带单引号的字符串
     */
    public static String arrayToStrWithSymbol(Object[] srcArray, String splitSymbol)
    {
        if (srcArray == null || srcArray.length == 0)
        {
            return "";
        }
        String val = "";
        for (int i = 0; i < srcArray.length; i++)
        {
            if (srcArray[i] != null)// 判断元素知否为空，若为空则用null表示
            {
                if ("".equals(srcArray[i]))
                {
                    val += "'null'";
                }
                val += "'" + String.valueOf(srcArray[i]).trim() + "'";
            }
            if (i < srcArray.length - 1)
            {
                val += splitSymbol;
            }
        }
        return val;
    }

    /**
     * 将数据分解为全部带单引号的字符串
     */
    public static String newArrayToStrWithSymbol(String[] srcArray, String splitSymbol)
    {
        if (srcArray == null || srcArray.length == 0)
        {
            return "";
        }
        StringBuilder val = new StringBuilder();
        for (int i = 0; i < srcArray.length; i++)
        {
            if (srcArray[i] != null && srcArray[i].length() > 0)// 判断元素知否为空，若为空则用null表示
            {
                // if ("".equals(srcArray[i]))
                // {
                //
                // }
                // else
                // {
                val.append("'" + srcArray[i] + "'");
                // }
            }
            else
            {
                val.append("''");
            }
            // val.append("'" + srcArray[i].trim() + "'");
            if (i < srcArray.length - 1)
            {
                val.append(splitSymbol);
            }
        }
        return val.toString();
    }

    /**
     * 清除数组中的空字符串
     * 
     * @param srcArray
     *            字符串数组
     * @return 清除空字符串后的字符串数组
     */
    public static String[] clearEmptyItem(String[] srcArray)
    {
        if (srcArray == null)
        {
            return null;
        }
        ArrayList<Object> al = new ArrayList<>();
        for (int i = 0; i < srcArray.length; i++)
        {
            if (srcArray[i] != null && !srcArray[i].isEmpty())
            {
                al.add(srcArray[i]);
            }
        }
        return arrayListToArray(al);
    }

    /**
     * 在数组中查找整数
     * 
     * @param data
     *            整数
     * @param datas
     *            整数数组
     * @return 该整数在数组中的索引
     */
    public static int findDataInArray(int data, int[] datas)
    {
        if (datas == null)
        {
            return -1;
        }
        for (int i = 0; i < datas.length; i++)
        {
            if (datas[i] == data)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在数组中查找字符串
     * 
     * @param data
     *            字符串
     * @param datas
     *            字符串数组
     * @return 字符串在数组中的索引
     */
    public static int findDataInArray(String data, String[] datas)
    {
        if (datas == null)
        {
            return -1;
        }
        for (int i = 0; i < datas.length; i++)
        {
            if (datas[i].equals(data))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在数组中查找数据的索引,未找到,返回-1
     * 
     * @param data
     *            字符串
     * @param datas
     *            字符串数组
     * @param ignoreCase
     *            是否忽略大小写
     * @return 字符串在改数组中的索引
     */
    public static int findDataInArray(String data, String[] datas, boolean ignoreCase)
    {
        if (datas == null || datas.length == 0)
        {
            return -1;
        }
        for (int i = 0; i < datas.length; i++)
        {
            if (datas[i] == null || datas[i].isEmpty())
            {
                continue;
            }
            if (ignoreCase)
            {
                if (datas[i].equalsIgnoreCase(data))
                {
                    return i;
                }
            }
            else
            {
                if (datas[i].equals(data))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 在数组中插入项
     * 
     * @param srcArray
     *            数组
     * @param item
     *            插入项
     * @param index
     *            插入位置
     * @return 更新后的数组
     */
    public static String[] insItemInArray(String[] srcArray, String item, int index)
    {
        if (srcArray == null)
        {
            return new String[0];
        }
        String[] destArray = new String[srcArray.length + 1];
        for (int i = 0; i < srcArray.length + 1; i++)
        {
            if (i < index)
            {
                destArray[i] = srcArray[i];
            }
            else if (i == index)
            {
                destArray[i] = item;
            }
            else
            {
                destArray[i] = srcArray[i - 1];
            }
        }
        return destArray;
    }

    /**
     * 将整型数组转换为字符串数组
     * 
     * @param srcArray
     *            整型数组
     * @return 字符串数组
     */
    public static String[] intArrayToStrArray(int[] srcArray)
    {
        if (srcArray == null)
        {
            return new String[0];
        }
        String[] destArray = new String[srcArray.length];
        for (int i = 0; i < srcArray.length; i++)
        {
            destArray[i] = String.valueOf(srcArray[i]);
        }
        return destArray;
    }

    /**
     * 将数组中的字符串小写并去掉空格
     * 
     * @param array
     *            字符串数组
     * @return newLowerTrimArray 小写并去掉空格的字符串数组
     */
    public static String[] lowerTrimArray(String[] array)
    {
        if (array == null)
        {
            return null;
        }
        String[] newArr = new String[array.length];
        for (int i = 0; i < array.length; i++)
        {
            newArr[i] = array[i].trim().toLowerCase();
        }
        return newArr;
    }

    /**
     * 在数组中移除项
     * 
     * @param srcArray
     *            数组
     * @param index
     *            移除项的索引
     * @return 修改后的数组
     */
    public static String[] removeItemInArray(String[] srcArray, int index)
    {

        String[] srcArray1 = null;

        if (srcArray == null || index >= srcArray.length)
        {
            return srcArray;
        }

        srcArray1 = new String[srcArray.length - 1];
        int j = 0;
        for (int i = 0; i < srcArray.length; i++)
        {
            if (i == index)
            {
                continue;
            }

            srcArray1[j] = srcArray[i];
            j++;
        }

        return srcArray1;
    }

    /**
     * 将字符串数组转换为整型数组
     * 
     * @param srcArray
     *            字符串数组
     * @return 整型数组
     * @throws DataAsException
     *             异常
     */
    public static int[] strArrayToIntArray(String[] srcArray) throws DataAsException
    {
        if (srcArray == null)
        {
            return new int[0];
        }
        int[] destArray = new int[srcArray.length];
        for (int i = 0; i < srcArray.length; i++)
        {
            destArray[i] = Integer.parseInt(srcArray[i].trim());
        }
        return destArray;
    }

    /**
     * 将字符串 装换为double类型
     * 
     * @param srcArray
     *            string数组
     * @return double 数组
     * @throws DataAsException
     *             异常
     */
    public static double[] strArrayToDouble(String[] srcArray) throws DataAsException
    {
        if (srcArray == null)
        {
            return new double[0];
        }
        double[] destArray = new double[srcArray.length];
        for (int i = 0; i < srcArray.length; i++)
        {
            destArray[i] = Double.parseDouble(srcArray[i].trim());
        }
        return destArray;
    }

    /**
     * 字符串转double
     * 
     * @param srcArray
     *            字符串数组
     * @param dval
     *            double数组
     * @return double数组
     * @throws DataAsException
     *             自定义异常
     */
    public static double[] strArrayToDouble(String[] srcArray, double[] dval) throws DataAsException
    {
        if (srcArray == null)
        {
            return new double[0];
        }

        for (int i = 0; i < srcArray.length; i++)
        {
            dval[i] = Double.parseDouble(srcArray[i].trim());
        }
        return dval;
    }

    /**
     * 清理数组元素中的空格
     * 
     * @param array
     *            字符串数组
     * @return 清理元素空格后的字符串数组
     */
    public static String[] trimArray(String[] array)
    {
        if (array == null)
        {
            return null;
        }
        String[] newArr = new String[array.length];
        for (int i = 0; i < array.length; i++)
        {
            newArr[i] = array[i].trim();
        }
        return newArr;
    }

    /**
     * 合并数组
     * 
     * @param srcArray
     *            字符串数组
     * @param destArray
     *            字符串数组
     * @param allowRepeatItem
     *            是否允许重复
     * @param sort
     *            是否排序
     * @return 合并后数组
     */
    @Deprecated
    public static String[] unionArray(String[] srcArray, String[] destArray, boolean allowRepeatItem, boolean sort)
    {
        String[] val;
        int iMax = 0;
        if (srcArray != null)
        {
            iMax = srcArray.length;
        }

        if (destArray == null)
        {
            return srcArray;
        }
        else
        {
            iMax += destArray.length;
        }

        // 合并
        val = new String[iMax];
        if (srcArray != null)
        {
            for (int i = 0; i < srcArray.length; i++)
            {
                val[i] = srcArray[i];
            }

            for (int j = srcArray.length; j < iMax; j++)
            {
                if (!allowRepeatItem && findDataInArray(destArray[j - srcArray.length], srcArray) >= 0)
                {
                    continue;
                }

                val[j] = destArray[j - srcArray.length];
            }
        }

        // 排序
        if (sort)
        {
            Arrays.sort(val);
        }

        return val;
    }

    /**
     * 合并数组
     * 
     * @param srcArray
     *            字符串数组
     * @param destArray
     *            字符串数组
     * @param allowRepeatItem
     *            是否允许重复
     * @param sort
     *            是否排序
     * @return 合并后数组
     */
    public static String[] unionArrayNew(String[] srcArray, String[] destArray, boolean allowRepeatItem, boolean sort)
    {
        // 判断如果合并的两个数组有其中一个为null，直接返回另一个
        if (srcArray == null)
        {
            return destArray;
        }
        if (destArray == null)
        {
            return srcArray;
        }
        // 将两个数组合并为一个
        String[] allArray = new String[srcArray.length + destArray.length];
        System.arraycopy(srcArray, 0, allArray, 0, srcArray.length);
        System.arraycopy(destArray, 0, allArray, srcArray.length, destArray.length);
        // 是否需要去重
        if (!allowRepeatItem)
        {
            List<String> list = new ArrayList<String>(30);
            for (String item : allArray)
            {
                if (!list.contains(item))
                {
                    list.add(item);
                }
            }
            return sortArray((String[]) list.toArray(new String[list.size()]), sort);
        }
        return sortArray(allArray, sort);
    }

    /**
     * @Title: sortArray
     * @Description: 对数组排序
     * @param destArray
     *            目标数组
     * @param sort
     *            是否排序
     * @return 结果数组
     * @return: String[] 结果数组
     */
    public static String[] sortArray(String[] destArray, boolean sort)
    {
        if (sort)
        {
            Arrays.sort(destArray);
        }
        return destArray;
    }

    /**
     * 截断数组
     * 
     * @param array
     *            原始数组
     * @param startNum
     *            起始数组
     * @param endNum
     *            结束数组
     * @return 截断的数组
     */
    public static String[] cutArray(String[] array, int startNum, int endNum)
    {
        // 判断起始位置是否小于结束位置
        if (endNum > startNum && startNum < array.length)
        {
            // 判断结束位置是否大于array的长度
            if (endNum > array.length)
            {
                endNum = array.length;
            }

            if (endNum - startNum > array.length)
            {
                return array;
            }
            else
            {
                String[] arr = new String[endNum - startNum];
                for (int i = startNum; i < endNum; i++)
                {
                    arr[i - startNum] = array[i];
                }
                return arr;
            }
        }
        return new String[] {};
    }

    /**
     * 平均值
     * 
     * @param array
     *            字符串数组
     * @return 返回平均值
     * @throws DataAsException
     *             异常
     */
    public static int avgArray(String[] array) throws DataAsException
    {
        int[] temp = strArrayToIntArray(array);
        int result = 0;

        for (int i = 0; i < temp.length; i++)
        {
            result += temp[i];
        }

        result = result / temp.length;
        return result;
    }

    /**
     * double类型结果
     * 
     * @param array
     *            字符串
     * @return 返回平均值
     * @throws DataAsException
     *             异常
     */
    public static double avgArrayDouble(String[] array) throws DataAsException
    {
        double[] temp = strArrayToDouble(array);
        double result = 0;

        if (temp.length != 0)
        {
            for (int i = 0; i < temp.length; i++)
            {
                result += temp[i];
            }

            result = result / temp.length;
        }
        return result;
    }

    /**
     * resize方法
     * 
     * @param oldArray
     *            数组
     * @param newSize
     *            新数组长度
     * @return 重塑后的数组
     */
    public static Object resizeArray(Object oldArray, int newSize)
    {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class<?> elementType = oldArray.getClass().getComponentType();
        Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0)
        {
            System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
    }

    /**
     * 四舍五入
     * 
     * @param d
     *            双精度浮点数
     * @return 四舍五入后的双精度浮点数
     */
    public static double formatDouble2(double d)
    {
        BigDecimal bg = BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }

    /**
     * 字符串map转数组
     * 
     * @param hm
     *            字符串map
     * @return 字符串数组
     */
    public static String[] map2Array(Map<String, String> hm)
    {
        String[] ss = null;
        if (hm != null)
        {
            ss = new String[hm.size()];
            int i = 0;
            for (String s : hm.keySet())
            {
                ss[i++] = s + "=" + hm.get(s);
            }
        }
        return ss;
    }

    /**
     * 移除数组中第i个数据
     * 
     * @param oldSA
     *            数组
     * @param i
     *            被删除的数组
     * @return 删除数组后的数据
     */
    public static String[] removeDataInSArray(String[] oldSA, int i)
    {
        String[] nString = new String[] {};
        if ((oldSA.length - 1) < i || i < 0)// 判断输入的i是否过于
        {
            return oldSA;
        }
        else
        {
            ArrayList<Object> al = new ArrayList<>();
            for (int j = 0; j != i && j < oldSA.length; j++)
            {
                al.add(oldSA[j]);
            }
            nString = ArrayOper.arrayListToArray(al);
        }

        return nString;
    }

    /**
     * 新切分数据(性能中等)
     * 
     * @param a
     *            所需要切分的数据
     * @param symble
     *            分隔符
     * @return 切分后的list
     */
    @Deprecated
    public static String[] splitToArrrayList(String a, String symble)
    {
        StringTokenizer st = new StringTokenizer(a, symble);
        String[] al = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens())
        {
            al[i] = st.nextToken();
            i++;
        }
        return al;
    }

    /**
     * 替换数组中的指定字符
     * 
     * @param oldArrays
     *            旧数组
     * @param oldChar
     *            旧字符
     * @param newChar
     *            新字符
     * @return 替换字符后的数组
     */
    public static String[] arraysReplace(String[] oldArrays, String oldChar, String newChar)
    {
        String[] newArrays = new String[oldArrays.length];

        for (int i = 0; i < oldArrays.length; i++)
        {
            newArrays[i] = oldArrays[i].replace(oldChar, newChar);
        }

        return newArrays;
    }
}
