package com.goldwind.datalogic.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.datalogic.business.model.FormulaElement;

/**
 * 
 * @author 曹阳
 *
 */
public class IndexCalculator
{
    /**
     * 运算符
     */
    private static String OPERATCHAR = "+-*/%()";

    /**
     * 获取公式中的计算元素
     * 
     * @param formula
     *            公式
     * @return 计算元素列表
     */
    public static List<FormulaElement> getFormulaElements(String formula)
    {
        List<FormulaElement> elements = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(formula, OPERATCHAR, false);
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();// 元素项
            if (!currentEle.isEmpty() && !isNum(currentEle))
            {
                elements.add(new FormulaElement(currentEle));
            }
        }
        return elements;
    }

    /**
     * 
     * 根据公式计算上网电量
     * 
     * @param formula
     *            公式
     * @param values
     *            iec量对应值map
     * @return 计算结果
     */
    public static double getTotalOngridEnergy(String formula, List<FormulaElement> values)
    {
        if (formula == null || formula.isEmpty())
        {
            return 0;
        }
        StringTokenizer tokenizer = new StringTokenizer(formula, OPERATCHAR, true);
        Vector<Double> nums = new Vector<Double>();
        Vector<BaseOperator<Double>> operators = new Vector<BaseOperator<Double>>();
        Map<String, BaseOperator<Double>> computeOper = getComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();// 元素项
            if (!currentEle.isEmpty())
            {
                if (OPERATCHAR.indexOf(currentEle) == -1)
                {
                    if (isNum(currentEle))
                    {
                        nums.add(Double.valueOf(currentEle));
                        continue;
                    }
                    FormulaElement ele = new FormulaElement(currentEle);
                    for (FormulaElement fe : values)
                    {
                        if (ele.getDevId().equals(fe.getDevId()) && ele.getIecPath().equals(fe.getIecPath()))
                        {
                            nums.add(fe.getValue());
                        }
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
        if (!nums.isEmpty())
        {
            return ArrayOper.formatDouble2(nums.firstElement());
        }
        else
        {
            return 0;
        }

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
                return num1 + num2;
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
                return num1 - num2;
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
                return num1 * num2;
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
                return num1 / num2;
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
                return 0.0;
            }

        };
    }

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

    // public static void main(String[] args)
    // {
    // Map<String, String> value = new HashMap<>();
    // value.put("a", "10");
    // value.put("b", "20");
    // value.put("c", "30");
    // value.put("d", "40");
    // Map<String, Map<String, String>> values = new HashMap<>();
    // values.put("1001", value);
    // values.put("1002", value);
    // values.put("1003", value);
    // String formula = "{1001:a}*0.88+{1002:a}/2.53-{1003:d}";
    // List<FormulaElement> fl = new ArrayList<>();
    // for (FormulaElement fe : IndexCalculator.getFormulaElements(formula))
    // {
    // System.out.println(fe.getDevId() + "|" + fe.getIecPath());
    // fe.setValue(100.0);
    // fl.add(fe);
    // }
    // System.out.println(IndexCalculator.getTotalOngridEnergy(formula, fl));
    // }
}
