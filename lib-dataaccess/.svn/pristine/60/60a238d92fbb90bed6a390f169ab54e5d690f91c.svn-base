package com.goldwind.dataaccess;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 动态运算表达式
 * 
 * @author 曹阳
 *
 */
public class DynamicRun
{
    /**
     * 输出日志
     */
    private static Log logger = Log.getLog(DynamicRun.class);

    /**
     * 计算动态运算表达式
     * 
     * @param computeExpr
     *            动态运算表达式
     * @return 计算结果
     */
    public static double dynamicExpression(String computeExpr)
    {
        if (computeExpr == null || computeExpr.isEmpty())
        {
            return 0;
        }
        String operatChar = "+-*/%()";
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
        Vector<Double> nums = new Vector<Double>();
        Vector<BaseOperator<Double>> operators = new Vector<BaseOperator<Double>>();
        Map<String, BaseOperator<Double>> computeOper = getComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();// 元素项
            if (!currentEle.isEmpty())
            {
                if (operatChar.indexOf(currentEle) == -1)
                {
                    if (isNum(currentEle))
                    {
                        nums.add(Double.valueOf(currentEle));
                    }
                }
                else
                {
                    startCompute(currentEle, nums, operators, computeOper);
                }
            }

        }
        while (!operators.isEmpty())
        {
            computeExpression(nums, operators);
        }
        if (nums.size() > 0)
        {
            return nums.firstElement();
        }
        else
        {
            return 0;
        }

    }

    /**
     * 计算逻辑动态运算表达式
     * 
     * @param computeExpr
     *            逻辑动态运算表达式
     * @return 计算结果
     */
    public static boolean logicExpression(String computeExpr)
    {
        if (computeExpr == null || computeExpr.isEmpty())
        {
            return false;
        }
        try
        {
            computeExpr = invertHandler(containsHandler(replaceHandler(splitHandler(computeExpr))));
            String operatChar = "&|()";
            StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
            Vector<Boolean> nums = new Vector<Boolean>();
            Vector<BaseOperator<Boolean>> operators = new Vector<BaseOperator<Boolean>>();
            Map<String, BaseOperator<Boolean>> computeOper = getLogicComputeOper();
            while (tokenizer.hasMoreTokens())
            {
                String currentEle = tokenizer.nextToken().trim();

                if (!currentEle.isEmpty())
                {
                    if (isLogic(currentEle))
                    {
                        nums.add(Boolean.parseBoolean(currentEle));
                    }
                    else
                    {
                        if (operatChar.indexOf(currentEle) == -1)
                        {
                            nums.add(Boolean.parseBoolean(otherExpression(currentEle)));
                            continue;
                        }
                        startCompute(currentEle, nums, operators, computeOper);
                    }

                }
            }
            while (!operators.isEmpty())
            {
                computeExpression(nums, operators);
            }
            return nums.firstElement();
        }
        catch (Exception e)
        {
            logger.error("Express parse failed:" + computeExpr);
            return false;
        }
    }

    /**
     * 计算其他动态运算表达式
     * 
     * @param computeExpr
     *            其他动态运算表达式
     * @return 计算结果
     */
    private static String otherExpression(String computeExpr)
    {
        if (computeExpr == null || computeExpr.isEmpty())
        {
            return "false";
        }
        String operatChar = "><=∈≠()";
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
        Vector<String> nums = new Vector<String>();
        Vector<BaseOperator<String>> operators = new Vector<BaseOperator<String>>();
        Map<String, BaseOperator<String>> computeOper = getOtherComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();

            if (!currentEle.isEmpty())
            {
                if (operatChar.indexOf(currentEle) == -1)
                {
                    if (currentEle.indexOf("[") == -1)
                    {
                        if (isFourExpress(currentEle))
                        {
                            currentEle = String.valueOf(dynamicExpression(currentEle));
                        }
                    }
                    nums.add(currentEle);
                }
                else
                {
                    startCompute(currentEle, nums, operators, computeOper);
                }

            }
        }
        while (!operators.isEmpty())
        {
            computeExpression(nums, operators);
        }
        return nums.firstElement();

    }

    /**
     * 计算其他动态运算表达式(即关系运算，但关系运算符号之间不支持括号内的计算(例如：2≥(2>1),(2>1)∈[1,3,4,5])，2019.03.19 fcy)
     * 
     * @param computeExpr
     *            其他动态运算表达式
     * @return 计算结果
     */
    public static boolean otherCompareExpression(String computeExpr)
    {
        if (computeExpr == null || computeExpr.isEmpty())
        {
            return false;
        }
        String operatChar = "><=∈⊂≠≤≥";
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
        Vector<String> nums = new Vector<String>();
        Vector<BaseOperator<String>> operators = new Vector<BaseOperator<String>>();
        Map<String, BaseOperator<String>> computeOper = getOtherComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();

            if (!currentEle.isEmpty())
            {
                if (operatChar.indexOf(currentEle) == -1)
                {
                    if (currentEle.indexOf("[") == -1)
                    {
                        if (isFourExpress(currentEle))
                        {
                            currentEle = String.valueOf(dynamicExpression(currentEle));
                        }
                    }
                    nums.add(currentEle);
                }
                else
                {
                    startCompute(currentEle, nums, operators, computeOper);
                }

            }
        }
        while (!operators.isEmpty())
        {
            computeExpression(nums, operators);
        }

        if ("true".equalsIgnoreCase(nums.firstElement()))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * 取得公式中的所有iec路径集合(2019.12.04 fcy)
     * 
     * @param express
     *            计算表达式
     * @return 所有iec路径集合
     */
    public static List<String> getAllExprIecpaths(String express)
    {
        List<String> iecpaths = new ArrayList<>();
        if (express == null || express.isEmpty())
        {
            return iecpaths;
        }
        String operatChar = "+-*/%&|><=∈⊂≠≤≥()[]{},:;";
        StringTokenizer tokenizer = new StringTokenizer(express, operatChar, true);
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();
            if (StringUtils.isEmpty(currentEle) || currentEle.length() <= 1 || !currentEle.startsWith("@") || !currentEle.contains("."))
            {
                continue;
            }

            // 去掉公式中iec路径的开头符号"@"并去重
            currentEle = currentEle.substring(1);
            if (!iecpaths.contains(currentEle))
            {
                iecpaths.add(currentEle);
            }
        }

        return iecpaths;
    }

    interface BaseOperator<T>
    {
        /**
         * 计算
         * 
         * @param num1
         *            数值1
         * @param num2
         *            数值2
         * @return 计算结果
         */
        T compute(T num1, T num2);

        /**
         * 
         * @return 优先级
         */
        int priority();
    }

    /**
     * 四则运算操作符
     */
    private enum Operator implements BaseOperator<Double>
    {
        /**
         * 加
         */
        PLUS
        {
            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public Double compute(Double num1, Double num2)
            {
                num1 = num1 == null ? 0 : num1;
                num2 = num2 == null ? 0 : num2;
                return DataAsFunc.addDouble(num1, num2);
            }

        },
        /**
         * 减
         */
        MINUS
        {
            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public Double compute(Double num1, Double num2)
            {
                num1 = num1 == null ? 0 : num1;
                num2 = num2 == null ? 0 : num2;
                return DataAsFunc.subDouble(num1, num2);
            }
        },
        /**
         * 乘
         */
        MULTIPLY
        {
            @Override
            public int priority()
            {
                return 2;
            }

            @Override
            public Double compute(Double num1, Double num2)
            {
                num1 = num1 == null ? 0 : num1;
                num2 = num2 == null ? 0 : num2;
                return DataAsFunc.mulDouble(num1, num2);
            }
        },
        /**
         * 除
         */
        DIVIDE
        {
            @Override
            public int priority()
            {
                return 2;
            }

            @Override
            @SuppressWarnings("squid:S3518")
            public Double compute(Double num1, Double num2)
            {
                num1 = num1 == null ? 0 : num1;
                num2 = num2 == null ? 0 : num2;
                return DataAsFunc.divDouble(num1, num2, 2);
            }
        },
        /**
         * 求余
         */
        REMAINDER
        {

            @Override
            public int priority()
            {
                return 2;
            }

            @Override
            @SuppressWarnings("squid:S3518")
            public Double compute(Double num1, Double num2)
            {
                num1 = num1 == null ? 0 : num1;
                num2 = num2 == null ? 0 : num2;
                return num1 % num2;
            }
        },
        /**
         * 括号
         */
        BRACKETS
        {

            @Override
            public int priority()
            {
                return 0;
            }

            @Override
            public Double compute(Double num1, Double num2)
            {
                return new Double(0);
            }

        };
    }

    /**
     * 逻辑操作符
     */
    private enum LogicOperator implements BaseOperator<Boolean>
    {
        /**
         * 与
         */
        AND
        {
            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public Boolean compute(Boolean num1, Boolean num2)
            {
                return Boolean.logicalAnd(num1, num2);
            }
        },
        /**
         * 或
         */
        OR
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public Boolean compute(Boolean num1, Boolean num2)
            {
                return Boolean.logicalOr(num1, num2);
            }
        },

        /**
         * 括号
         */
        BRACKETS

        {

            @Override
            public Boolean compute(Boolean num1, Boolean num2)
            {
                return false;
            }

            @Override
            public int priority()
            {
                return 0;
            }

        };
    }

    /**
     * 逻辑操作符
     */
    private enum OtherOperator implements BaseOperator<String>
    {
        /**
         * 大于
         */
        GREATERTHAN
        {
            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rtData = "false";
                try
                {
                    rtData = Boolean.toString(Double.parseDouble(num1) > Double.parseDouble(num2));
                }
                catch (Exception e)
                {
                    // 如果异常直接返回false
                }

                return rtData;
            }
        },
        /**
         * 小于
         */
        LESSTHAN
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rtData = "false";
                try
                {
                    rtData = Boolean.toString(Double.parseDouble(num1) < Double.parseDouble(num2));
                }
                catch (Exception e)
                {
                    // 如果异常直接返回false
                }

                return rtData;
            }
        },
        /**
         * 大于等于
         */
        GREATER_OR_EQUAL
        {
            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rtData = "false";
                try
                {
                    rtData = Boolean.toString(Double.parseDouble(num1) >= Double.parseDouble(num2));
                }
                catch (Exception e)
                {
                    // 如果异常直接返回false
                }

                return rtData;
            }
        },
        /**
         * 小于等于
         */
        LESS_OR_EQUAL
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rtData = "false";
                try
                {
                    rtData = Boolean.toString(Double.parseDouble(num1) <= Double.parseDouble(num2));
                }
                catch (Exception e)
                {
                    // 如果异常直接返回false
                }

                return rtData;
            }
        },
        /**
         * 等于
         */
        EQUAL
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                if (isNullOrEmpty(num1) || isNullOrEmpty(num2))
                {
                    return "false";
                }
                if (isNum(num1) && isNum(num2))
                {
                    // return Boolean.toString(Double.parseDouble(num1) == Double.parseDouble(num2));
                    // return Boolean.toString(new BigDecimal(num1).compareTo(new BigDecimal(num2)) == 0);
                    return Boolean.toString(DynamicRun.isDoubleEqual(Double.parseDouble(num1), Double.parseDouble(num2)));
                }
                return Boolean.toString(num1.equals(num2));
            }
        },
        /**
         * 不等于
         */
        NOTEQUAL
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                if (isNullOrEmpty(num1) || isNullOrEmpty(num2))
                {
                    return "false";
                }
                if (isNum(num1) && isNum(num2))
                {
                    return Boolean.toString(!DynamicRun.isDoubleEqual(Double.parseDouble(num1), Double.parseDouble(num2)));
                }
                return Boolean.toString(!num1.equals(num2));
            }
        },
        /**
         * 包含
         */
        IN
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rt = "false";
                if (isNullOrEmpty(num1) || isNullOrEmpty(num2))
                {
                    return "false";
                }
                String[] items = num2.substring(1, num2.length() - 1).split(",");
                for (String item : items)
                {
                    if (isNum(num1) && isNum(item))
                    {
                        if (DynamicRun.isDoubleEqual(Double.parseDouble(num1), Double.parseDouble(item)))
                        {
                            rt = "true";
                            break;
                        }
                    }
                    else
                    {
                        if (item != null && num1.trim().equals(item.trim()))
                        {
                            rt = "true";
                            break;
                        }
                    }
                }
                return rt;
            }
        },
        /**
         * 在…之间
         */
        BETWEEN
        {

            @Override
            public int priority()
            {
                return 1;
            }

            @Override
            public String compute(String num1, String num2)
            {
                String rt = "false";
                if (isNullOrEmpty(num1) || isNullOrEmpty(num2))
                {
                    return "false";
                }
                String[] items = num2.substring(1, num2.length() - 1).split(",");
                String startnum = items[0];
                String endnum = items[1];

                if (items.length == 2 && isNum(num1) && isNum(startnum) && isNum(endnum))
                {
                    String startsymbol = num2.substring(0, 1);
                    String endsymbol = num2.substring(num2.length() - 1);

                    // 判断是否大于或者大于等于头（左边是否为闭区间）
                    switch (startsymbol) {
                        case "(":
                            if (Double.parseDouble(num1) <= Double.parseDouble(startnum))
                            {
                                return "false";
                            }
                            break;
                        case "[":
                            if (Double.parseDouble(num1) < Double.parseDouble(startnum))
                            {
                                return "false";
                            }
                            break;
                        default:
                            break;
                    }

                    // 判断是否小于或者小于等于尾头（右边是否为闭区间）
                    switch (endsymbol) {
                        case ")":
                            if (Double.parseDouble(num1) >= Double.parseDouble(endnum))
                            {
                                return "false";
                            }
                            break;
                        case "]":
                            if (Double.parseDouble(num1) > Double.parseDouble(endnum))
                            {
                                return "false";
                            }
                            break;
                        default:
                            break;
                    }

                    rt = "true";
                }

                return rt;
            }
        },

        /**
         * 括号
         */
        BRACKETS

        {

            @Override
            public String compute(String num1, String num2)
            {
                return "false";
            }

            @Override
            public int priority()
            {
                return 0;
            }

        };
    }

    /**
     * 取出最后一个运算符和数字进行计算
     * 
     * @param nums
     *            数字集合
     * @param operators
     *            运算符集合
     * @param <S>
     *            返回类型
     */
    private static <S> void computeExpression(Vector<S> nums, Vector<BaseOperator<S>> operators)
    {
        S num2 = null;
        S num1 = null;
        try
        {
            num2 = nums.remove(nums.size() - 1);
            num1 = nums.remove(nums.size() - 1);
        }
        catch (Exception e)
        {
            if (e instanceof ArrayIndexOutOfBoundsException)
            {
                // 如果异常，将num1赋值为null，交给操作符中的计算方法处理
            }
        }
        S computeResult = operators.remove(operators.size() - 1).compute(num1, num2);
        nums.add(computeResult);
    }

    /**
     * 计算表达式
     * 
     * @param <S>
     *            通用类型
     * @param currentEle
     *            元素项
     * @param nums
     *            元素项容器
     * @param operators
     *            操作符
     * @param computeOper
     *            操作符计算器
     */
    @SuppressWarnings({ "unchecked" })
    private static <S> void startCompute(String currentEle, Vector<S> nums, Vector<BaseOperator<S>> operators, Map<String, BaseOperator<S>> computeOper)
    {
        BaseOperator<S> operator = computeOper.get(currentEle);

        if (operator != null)
        {
            while (!operators.isEmpty() && operators.lastElement().priority() >= operator.priority())
            {
                computeExpression(nums, operators);
            }
            operators.add(operator);
        }
        else
        {
            if ("(".equals(currentEle))
            {
                operators.add((BaseOperator<S>) Operator.BRACKETS);
            }
            else
            {
                while (!operators.lastElement().equals(Operator.BRACKETS))
                {
                    computeExpression(nums, operators);
                }
                operators.remove(operators.size() - 1);
            }
        }
    }

    /**
     * 获取计算操作符
     * 
     * 
     * @return 计算符列表
     */
    private static Map<String, BaseOperator<Double>> getComputeOper()
    {
        return new HashMap<String, BaseOperator<Double>>()
        {
            private static final long serialVersionUID = 7706718608122369958L;
            {
                put("+", Operator.PLUS);
                put("-", Operator.MINUS);
                put("*", Operator.MULTIPLY);
                put("/", Operator.DIVIDE);
                put("%", Operator.REMAINDER);
            }
        };

    }

    /**
     * 获取逻辑计算操作符
     * 
     * 
     * @return 计算符列表
     */
    private static Map<String, BaseOperator<Boolean>> getLogicComputeOper()
    {
        return new HashMap<String, BaseOperator<Boolean>>()
        {
            private static final long serialVersionUID = -1159602816049711418L;

            {
                put("&", LogicOperator.AND);
                put("|", LogicOperator.OR);
            }
        };

    }

    /**
     * 获取其他计算操作符
     * 
     * 
     * @return 计算符列表
     */
    private static Map<String, BaseOperator<String>> getOtherComputeOper()
    {
        return new HashMap<String, BaseOperator<String>>()
        {
            private static final long serialVersionUID = 3184770817813584824L;

            {
                put(">", OtherOperator.GREATERTHAN);
                put("<", OtherOperator.LESSTHAN);
                put("=", OtherOperator.EQUAL);
                put("∈", OtherOperator.IN);
                put("⊂", OtherOperator.BETWEEN);
                put("≠", OtherOperator.NOTEQUAL);
                put("≤", OtherOperator.LESS_OR_EQUAL);
                put("≥", OtherOperator.GREATER_OR_EQUAL);
            }
        };

    }

    /**
     * 判断字符串是否是全数字
     * 
     * @param str
     *            字符串
     * @return 是否是全数字
     */
    private static boolean isNum(String str)
    {
        String numRegex = "^\\d+(\\.\\d+)?$";
        return isNullOrEmpty(str) ? false : Pattern.matches(numRegex, str);

    }

    /**
     * 判断字符串是布尔量
     * 
     * @param exp
     *            字符串
     * @return 是否是全数字
     */
    private static boolean isLogic(String exp)
    {
        return "true".equalsIgnoreCase(exp) || "false".equalsIgnoreCase(exp);

    }

    /**
     * 判断是否包含四则运算表达式
     * 
     * @param exp
     *            表达式
     * @return 是否是四则运算表达式
     */
    public static boolean isFourExpress(String exp)
    {
        return isNullOrEmpty(exp) ? false : !(exp.indexOf("+") == -1 && exp.indexOf("-") == -1 && exp.indexOf("*") == -1 && exp.indexOf("/") == -1 && exp.indexOf("%") == -1);
    }

    /**
     * 判断是否包含关系运算表达式
     * 
     * @param exp
     *            表达式
     * @return 是否是关系运算表达式
     */
    public static boolean isRelationalExpress(String exp)
    {
        return isNullOrEmpty(exp) ? false
                : !(exp.indexOf(">") == -1 && exp.indexOf("<") == -1 && exp.indexOf("≥") == -1 && exp.indexOf("≤") == -1 && exp.indexOf("=") == -1 && exp.indexOf("≠") == -1 && exp.indexOf("∈") == -1
                        && exp.indexOf("⊂") == -1);
    }

    /**
     * 判断是否包含逻辑运算表达式
     * 
     * @param exp
     *            表达式
     * @return 是否是关系运算表达式
     */
    public static boolean isLogicExpress(String exp)
    {
        return isNullOrEmpty(exp) ? false : !(exp.indexOf("&") == -1 && exp.indexOf("|") == -1 && exp.indexOf("!") == -1);
    }

    /**
     * 处理表达式中的求反函数，并将结果拼回表达式
     * 
     * @param exp
     *            表达式
     * @return 处理后的表达式
     */
    private static String invertHandler(String exp)
    {
        try
        {
            String keyWord = "!{";
            while (exp.indexOf(keyWord) != -1)
            {
                int sIndex = exp.indexOf(keyWord) + keyWord.length();
                int eIndex = exp.indexOf("}", sIndex);
                exp = exp.substring(0, exp.indexOf(keyWord)) + Boolean.toString(!logicExpression(exp.substring(sIndex, eIndex))) + exp.substring(eIndex + 1);
            }
        }
        catch (Exception e)
        {
            exp = "false";
        }

        return exp;
    }

    /**
     * 处理表达式中的替换函数，并将结果拼回表达式 REPLACE、{a,b,c|,|;} REPLACE{字符串|原字符串|替换字符串}
     * 
     * @param exp
     *            表达式
     * @return 处理后的表达式
     */
    private static String replaceHandler(String exp)
    {
        String keyWord = "REPLACE{";
        while (exp.indexOf(keyWord) != -1)
        {
            int sIndex = exp.indexOf(keyWord) + keyWord.length();
            int eIndex = exp.indexOf("}", sIndex);
            String[] items = exp.substring(sIndex, eIndex).split("\\|");
            try
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim().replaceAll(items[1].trim(), items[2].trim()) + exp.substring(eIndex + 1);
            }
            catch (Exception e)
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim() + exp.substring(eIndex + 1);
            }

        }
        return exp;
    }

    /**
     * 处理表达式中的包含函数，并将结果拼回表达式 CONTAINS{abc|b} CONTAINS{字符串|包含字符}
     * 
     * @param exp
     *            表达式
     * @return 处理后的表达式
     */
    private static String containsHandler(String exp)
    {
        String keyWord = "CONTAINS{";
        while (exp.indexOf(keyWord) != -1)
        {
            int sIndex = exp.indexOf(keyWord) + keyWord.length();
            int eIndex = exp.indexOf("}", sIndex);
            String[] items = exp.substring(sIndex, eIndex).split("\\|");
            try
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim().contains(items[1].trim()) + exp.substring(eIndex + 1);
            }
            catch (Exception e)
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim() + exp.substring(eIndex + 1);
            }

        }
        return exp;
    }

    /**
     * 按指定字符分割字符串，并返回指定位置数据 SPLIT{a,b,c|,|2} SPLIT{字符串|分割字符串|指定位置}
     * 
     * @param exp
     *            表达式
     * @return 处理后的表达式
     */
    private static String splitHandler(String exp)
    {
        String keyWord = "SPLIT{";
        while (exp.indexOf(keyWord) != -1)
        {
            int sIndex = exp.indexOf(keyWord) + keyWord.length();
            int eIndex = exp.indexOf("}", sIndex);
            String[] items = exp.substring(sIndex, eIndex).split("\\|");
            try
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim().split(items[1].trim())[Integer.parseInt(items[2].trim())] + exp.substring(eIndex + 1);
            }
            catch (Exception e)
            {
                exp = exp.substring(0, exp.indexOf(keyWord)) + items[0].trim() + exp.substring(eIndex + 1);
            }

        }
        return exp;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param exp
     *            字符串
     * @return 是否为空
     */
    private static boolean isNullOrEmpty(String exp)
    {
        return exp == null || exp.isEmpty() ? true : false;
    }

    /**
     * 判断double类型的数据是否相等
     * 
     * @param num1
     *            数据1
     * @param num2
     *            数据2
     * @return 是否相等
     */
    public static boolean isDoubleEqual(double num1, double num2)
    {
        boolean isEqual = false;
        if (BigDecimal.valueOf(num1).compareTo(BigDecimal.valueOf(num2)) == 0)
        {
            isEqual = true;
        }
        return isEqual;
    }

    /**
     * @Title: isDoubleCompare
     * @Description: 两个double比较 如果scale小于0，这不进行小数位缩减
     * @param num1
     *            第一个double
     * @param num2
     *            第二个double
     * @param scale
     *            小数位
     * @return 比较结果
     * @return: int 比较结果
     */
    public static int isDoubleCompare(double num1, double num2, int scale)
    {
        // 转换成BigDecimal
        BigDecimal decimal1 = BigDecimal.valueOf(num1);
        BigDecimal decimal2 = BigDecimal.valueOf(num2);
        if (scale >= 0)
        {
            decimal1 = decimal1.setScale(scale, BigDecimal.ROUND_HALF_UP);
            decimal2 = decimal2.setScale(scale, BigDecimal.ROUND_HALF_UP);
        }

        return decimal1.compareTo(decimal2);
    }

    /**
     * 保留指定小数位
     * 
     * @param value
     * @param len
     * @return
     */
    public static double keepPoint(double value, int len)
    {
        String num = String.format("%." + len + "f", value);
        return Double.parseDouble(num);
    }
}
