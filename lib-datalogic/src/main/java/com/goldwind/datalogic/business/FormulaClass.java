package com.goldwind.datalogic.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsFunc;
import com.goldwind.dataaccess.DynamicRun;
import com.goldwind.dataaccess.StringUtil;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.BusinessDef.ExpressType;
import com.goldwind.datalogic.business.model.FactorClass;
import com.goldwind.datalogic.utils.FactorDef.FactorType;

/**
 * 公式计算类
 * 
 * @author 冯春源
 *
 */
public class FormulaClass
{
    /**
     * 分隔符
     */
    private static String OPERATCHAR = "+-*/%()><≥≤=≠∈⊂&|";

    // 计算公式
    private String formula;

    // 计算因子列表
    private FactorClass[] factorArray = null;

    public FormulaClass(String formula)
    {
        this.formula = formula;
    }

    /**
     * 获取公式中的计算因子列表
     * 
     * @return 计算因子列表
     * @throws DataAsException
     */
    public FactorClass[] getFactorArray() throws DataAsException
    {
        List<FactorClass> elementlist = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(formula, OPERATCHAR, false);
        while (tokenizer.hasMoreTokens())
        {
            // 元素项
            String currentEle = tokenizer.nextToken().trim();
            if (!currentEle.isEmpty() && !isNum(currentEle) && !isBoolean(currentEle))
            {
                // 兼容取反
                if (currentEle.startsWith("!"))
                {
                    currentEle = currentEle.substring(1);
                }

                // 计算因子必须以“{”开头
                if (currentEle.startsWith("{") || (currentEle.startsWith("@") && currentEle.contains(".")))
                {
                    elementlist.add(new FactorClass(currentEle));
                }
            }
        }

        factorArray = new FactorClass[elementlist.size()];
        for (int i = 0; i < elementlist.size(); i++)
        {
            factorArray[i] = elementlist.get(i);
        }
        return factorArray;
    }

    /**
     * 得到计算结果(包含四则运算结果、关系运算结果和逻辑运算结果)
     * 
     * @return 计算结果
     * @throws DataAsException
     *             自定义异常
     */
    public FormulaResult calculate() throws DataAsException
    {
        if (formula == null || formula.isEmpty())
        {
            return null;
        }

        // 公式计算结果
        FormulaResult formulaResult = new FormulaResult();

        // 兼容传入的为true或false
        if (!formula.contains("{") && "true".equalsIgnoreCase(formula))
        {
            // 设置计算类型，3-逻辑运算(取关系/逻辑运算结果)
            formulaResult.setExpressType(ExpressType.LOGIC_OPERATION);
            formulaResult.setLogicResult(true);
            return formulaResult;
        }
        else if (!formula.contains("{") && "false".equalsIgnoreCase(formula))
        {
            // 设置计算类型，3-逻辑运算(取关系/逻辑运算结果)
            formulaResult.setExpressType(ExpressType.LOGIC_OPERATION);
            formulaResult.setLogicResult(false);
            return formulaResult;
        }

        // 兼容传入的为数值
        if (!formula.contains("{") && !DynamicRun.isFourExpress(formula) && !DynamicRun.isRelationalExpress(formula) && !DynamicRun.isLogicExpress(formula))
        {
            // 设置计算类型，3-逻辑运算(取关系/逻辑运算结果)
            formulaResult.setExpressType(ExpressType.ARITHMETIC);
            formulaResult.setArithmeticResult(Double.parseDouble(formula));
            return formulaResult;
        }

        // 公式只有四则运算符号
        if (onlyArithmetic(formula))
        {
            // 设置计算类型，1-四则运算(取四则运算结果)
            formulaResult.setExpressType(ExpressType.ARITHMETIC);
            formulaResult.setArithmeticResult(compute());
        }
        else
        {
            // 包含逻辑运算表达式
            if (DynamicRun.isLogicExpress(formula))
            {
                // 设置计算类型，3-逻辑运算(取关系/逻辑运算结果)
                formulaResult.setExpressType(ExpressType.LOGIC_OPERATION);
                formulaResult.setLogicResult(logicCompute());
            }
            // 包含关系运算表达式
            else if (DynamicRun.isRelationalExpress(formula))
            {
                // 设置计算类型，2-关系运算(取关系/逻辑运算结果)
                formulaResult.setExpressType(ExpressType.RELATIONAL_OPERATION);
                formulaResult.setLogicResult(relationalCompute());
            }
            // 单个计算因子的场合
            else
            {
                try
                {
                    // 设置计算类型，1-四则运算(取四则运算结果)
                    formulaResult.setExpressType(ExpressType.ARITHMETIC);
                    formulaResult.setArithmeticResult(compute());
                }
                catch (Exception e)
                {
                    // 设置计算类型，3-逻辑运算(取关系/逻辑运算结果)
                    formulaResult.setExpressType(ExpressType.LOGIC_OPERATION);
                    formulaResult.setLogicResult(logicCompute());
                }
            }
        }

        return formulaResult;
    }

    /**
     * 判断计算公式是否只有四则运算符号
     * 
     * @param computeExpr
     *            运算表达式
     * 
     * @return 判断结果（true-只有逻辑运算符号 false-还有非逻辑运算符号）
     */
    public static boolean onlyArithmetic(String computeExpr)
    {
        boolean flag = false;

        if ((computeExpr.contains("+") || computeExpr.contains("-") || computeExpr.contains("*") || computeExpr.contains("/") || computeExpr.contains("%"))
                && (!computeExpr.contains(">") && !computeExpr.contains("<") && !computeExpr.contains("≥") && !computeExpr.contains("≤") && !computeExpr.contains("=") && !computeExpr.contains("≠")
                        && !computeExpr.contains("∈") && !computeExpr.contains("⊂") && !computeExpr.contains("&") && !computeExpr.contains("|") && !computeExpr.contains("!")))
        {
            flag = true;
        }

        return flag;
    }

    /**
     * 判断计算公式是否只有逻辑运算符号
     * 
     * @param computeExpr
     *            运算表达式
     * 
     * @return 判断结果（true-只有逻辑运算符号 false-还有非逻辑运算符号）
     */
    public static boolean onlyRelationalOperate(String computeExpr)
    {
        boolean flag = false;

        if ((!computeExpr.contains(">") || computeExpr.contains("<") || computeExpr.contains("≥") || computeExpr.contains("≤") || computeExpr.contains("=") || computeExpr.contains("≠")
                || computeExpr.contains("∈") || computeExpr.contains("⊂"))
                && (!computeExpr.contains("+") && !computeExpr.contains("-") && !computeExpr.contains("*") && !computeExpr.contains("/") && !computeExpr.contains("%") && !computeExpr.contains("&")
                        && !computeExpr.contains("|") && !computeExpr.contains("!")))
        {
            flag = true;
        }

        return flag;
    }

    /**
     * 判断计算公式是否只有逻辑运算符号
     * 
     * @param computeExpr
     *            运算表达式
     * 
     * @return 判断结果（true-只有逻辑运算符号 false-还有非逻辑运算符号）
     */
    public static boolean onlyLogicOperate(String computeExpr)
    {
        boolean flag = false;

        if ((computeExpr.contains("&") || computeExpr.contains("|") || computeExpr.contains("!")) && (!computeExpr.contains(">") && !computeExpr.contains("<") && !computeExpr.contains("≥")
                && !computeExpr.contains("≤") && !computeExpr.contains("=") && !computeExpr.contains("≠") && !computeExpr.contains("∈") && !computeExpr.contains("⊂") && !computeExpr.contains("+")
                && !computeExpr.contains("-") && !computeExpr.contains("*") && !computeExpr.contains("/") && !computeExpr.contains("%")))
        {
            flag = true;
        }

        return flag;
    }

    /**
     * 得到计算结果（四则运算）
     * 
     * @return 计算结果
     * @throws DataAsException
     */
    public double compute() throws DataAsException
    {
        return subCompute(formula);
    }

    /**
     * 得到指定公式的计算结果（四则运算,传入的参数可以是整个公式:({...}+{...})*2，也可以是部分公式:({...}+{...})）
     * 
     * @param computeExpr
     *            运算表达式
     * @return 计算结果
     * @throws DataAsException
     *             自定义异常
     */
    public double subCompute(String computeExpr) throws DataAsException
    {
        String operatChar = "+-*/%()";

        if (computeExpr == null || computeExpr.isEmpty())
        {
            return 0;
        }
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
                    // 按照【四则运算符】拆分后的【子公式】没有计算因子的场合
                    if (!currentEle.contains("{"))
                    {
                        if (isNum(currentEle))
                        {
                            nums.add(Double.valueOf(currentEle));
                        }
                        else
                        {
                            throw new DataAsException("subCompute Exception! currentEle:" + currentEle + ",value:" + currentEle);
                        }
                    }
                    // 有计算因子的场合
                    else
                    {
                        FactorClass ele = new FactorClass(currentEle);
                        for (int i = 0; i < factorArray.length; i++)
                        {
                            if (factorArray[i].getFactorType() == FactorType.SingleDevice)
                            {
                                if (Arrays.equals(ele.getDeviceIds(), factorArray[i].getDeviceIds()) && ele.getIecPath().equals(factorArray[i].getIecPath()))
                                {
                                    nums.add(Double.valueOf(factorArray[i].getIecVal()));
                                    break;
                                }
                            }
                            else if (factorArray[i].getFactorType() == FactorType.MultipleDevices)
                            {
                                if (Arrays.equals(ele.getOrgIds(), factorArray[i].getOrgIds()) && Arrays.equals(ele.getDeviceTypes(), factorArray[i].getDeviceTypes())
                                        && ele.getIecPath().equals(factorArray[i].getIecPath()) && ele.getArrgType() == factorArray[i].getArrgType())
                                {
                                    nums.add(Double.valueOf(factorArray[i].getIecVal()));
                                    break;
                                }
                            }
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

    /**
     * 得到计算结果（关系运算,但关系运算符号之间不支持括号内的计算，例如：2≥(2>1),(2>1)∈[1,3,4,5]；可以支持不同类型运算间的括号内的计算，例如：(6-2)∈[1,3,4,5]，2019.10.09 fcy）
     * 
     * @throws DataAsException
     * @return 计算结果
     * @throws DataAsException
     *             自定义异常
     */
    public boolean relationalCompute() throws DataAsException
    {
        return subRelationalCompute(formula);
    }

    /**
     * 得到指定公式的计算结果（关系运算,传入的参数可以是整个公式:({...}-{...})∈[1,3,4,5]，也可以是部分公式:({...}-{...})，2019.10.09 fcy）
     * 
     * @param computeExpr
     *            运算表达式
     * @throws DataAsException
     *             自定义异常
     * @return 计算结果
     */
    private boolean subRelationalCompute(String computeExpr) throws DataAsException
    {
        if (computeExpr == null || computeExpr.isEmpty())
        {
            return false;
        }
        String operatChar = "><≥≤=≠∈⊂";
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
        Vector<String> nums = new Vector<String>();
        Vector<BaseOperator<String>> operators = new Vector<BaseOperator<String>>();
        Map<String, BaseOperator<String>> computeOper = getRelationalComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();

            if (!currentEle.isEmpty())
            {
                if (operatChar.indexOf(currentEle) == -1)
                {
                    // 按照【关系运算符】拆分后的【子公式】没有计算因子的场合
                    if (!currentEle.contains("{"))
                    {
                        // 判断【子公式】是否还有四则运算表达式
                        if (DynamicRun.isFourExpress(currentEle))
                        {
                            currentEle = String.valueOf(subCompute(currentEle));
                        }
                        nums.add(currentEle);
                        continue;
                    }

                    // 有计算因子的场合
                    // 判断【子公式】是否还有四则运算表达式
                    if (DynamicRun.isFourExpress(currentEle))
                    {
                        // 得到【子公式】的计算结果（四则运算）
                        nums.add(String.valueOf(subCompute(currentEle)));
                        continue;
                    }
                    else
                    {
                        FactorClass ele = new FactorClass(currentEle);
                        for (int i = 0; i < factorArray.length; i++)
                        {
                            if (factorArray[i].getFactorType() == FactorType.SingleDevice)
                            {
                                if (Arrays.equals(ele.getDeviceIds(), factorArray[i].getDeviceIds()) && ele.getIecPath().equals(factorArray[i].getIecPath()))
                                {
                                    nums.add(factorArray[i].getIecVal());
                                    break;
                                }
                            }
                            else if (factorArray[i].getFactorType() == FactorType.MultipleDevices)
                            {
                                if (Arrays.equals(ele.getOrgIds(), factorArray[i].getOrgIds()) && Arrays.equals(ele.getDeviceTypes(), factorArray[i].getDeviceTypes())
                                        && ele.getIecPath().equals(factorArray[i].getIecPath()) && ele.getArrgType() == factorArray[i].getArrgType())
                                {
                                    nums.add(factorArray[i].getIecVal());
                                    break;
                                }
                            }
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
     * 得到计算结果（逻辑运算）
     * 
     * @throws DataAsException
     *             自定义异常
     * @return 计算结果
     */
    public boolean logicCompute() throws DataAsException
    {
        // 判断计算公式是否只有逻辑运算符号
        if (onlyLogicOperate(formula))
        {
            return subLogicCompute(formula);
        }
        // 得到运算表达式中关系运算的计算结果,并替换回原逻辑运算表达式（例如：false|((1+1)*2>3&(1+2)*2>5)|fasle ——> fasle|true|fasle）
        else
        {

            // 公式切割符（此切割符中不包括“（）”，）
            String operatChar = "&|";

            if (formula == null || formula.isEmpty())
            {
                return false;
            }

            // 逻辑运算表达式（例如：false&(fasle|((1+1)*2>3&(1+2)*2>5))）
            String computeExpr = formula;
            StringTokenizer tokenizer = new StringTokenizer(formula, operatChar, true);
            while (tokenizer.hasMoreTokens())
            {
                String currentEle = tokenizer.nextToken().trim();

                // 判断【子公式】是否包含“取反”操作
                if (currentEle.startsWith("!"))
                {
                    currentEle = currentEle.substring(1);
                }

                // 修正切割后的【子公式】中的括号"("和")"
                if (currentEle.contains("(") || currentEle.contains(")"))
                {
                    // "("的数量
                    int startBrackets = StringUtil.countString(currentEle, "(");
                    // ")"的数量
                    int endBrackets = StringUtil.countString(currentEle, ")");

                    if (currentEle.startsWith("(") && startBrackets > endBrackets)
                    {
                        currentEle = currentEle.substring(startBrackets - endBrackets);
                    }
                    else if (currentEle.endsWith(")") && startBrackets < endBrackets)
                    {
                        currentEle = currentEle.substring(0, currentEle.length() - (endBrackets - startBrackets));
                    }
                }

                // 判断【子公式】不是逻辑符号且还有关系运算表达式
                if (!currentEle.isEmpty() && operatChar.indexOf(currentEle) == -1 && DynamicRun.isRelationalExpress(currentEle))
                {
                    // 得到【子公式】的关系运算的计算结果（关系运算）,并替换回原逻辑运算表达式（false&(false|true)）
                    computeExpr = computeExpr.replace(currentEle, String.valueOf(subRelationalCompute(currentEle)));
                }
            }

            // 计算替换后的逻辑运算表达式（false&(false|true)）
            return subLogicCompute(computeExpr);
        }

    }

    /**
     * 得到指定公式的计算结果（逻辑运算,传入的参数可以是整个公式:{...}&({...}|{...})，也可以是部分公式:{...}|{...}）
     * 
     * @param computeExpr
     *            运算表达式（该表达式中只有逻辑运算符）
     * @return 计算结果
     * @throws DataAsException
     *             自定义异常
     */
    public boolean subLogicCompute(String computeExpr) throws DataAsException
    {
        String operatChar = "&|()";

        if (computeExpr == null || computeExpr.isEmpty())
        {
            return false;
        }
        // formula = invertHandler(replaceHandler(splitHandler(formula)));
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, operatChar, true);
        Vector<Boolean> nums = new Vector<>();
        Vector<BaseOperator<Boolean>> operators = new Vector<>();
        Map<String, BaseOperator<Boolean>> computeOper = getLogicComputeOper();
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();

            // 取反标志
            boolean notFlg = false;
            if (currentEle.startsWith("!"))
            {
                currentEle = currentEle.substring(1);
                notFlg = true;
            }
            if (!currentEle.isEmpty())
            {
                if (operatChar.indexOf(currentEle) == -1)
                {
                    // 按照【关系运算符】拆分后的【子公式】没有计算因子的场合
                    if (!currentEle.contains("{"))
                    {
                        if (isBoolean(currentEle))
                        {
                            boolean logicVal = Boolean.parseBoolean(currentEle);
                            nums.add(notFlg ? !logicVal : logicVal);
                        }
                        else
                        {
                            throw new DataAsException("SubLogicCompute Exception! computeExpr:" + computeExpr + " currentEle:" + currentEle);
                        }
                    }
                    // 有计算因子的场合
                    else
                    {
                        FactorClass ele = new FactorClass(currentEle);
                        for (int i = 0; i < factorArray.length; i++)
                        {
                            if (factorArray[i].getFactorType() == FactorType.SingleDevice)
                            {
                                if (ele.getDeviceId().equals(factorArray[i].getDeviceId()) && ele.getIecPath().equals(factorArray[i].getIecPath()))
                                {
                                    // 判断值是否为非布尔类型
                                    if (!isBoolean(factorArray[i].getIecVal()))
                                    {
                                        throw new DataAsException("SubLogicCompute Exception! currentEle:" + currentEle + ",value:" + factorArray[i].getIecVal());
                                    }

                                    boolean logicVal = Boolean.parseBoolean(factorArray[i].getIecVal());
                                    nums.add(notFlg ? !logicVal : logicVal);
                                    break;
                                }
                            }
                            else if (factorArray[i].getFactorType() == FactorType.MultipleDevices)
                            {
                                if (ele.getOrgId().equals(factorArray[i].getOrgId()) && ele.getDeviceType() == factorArray[i].getDeviceType() && ele.getIecPath().equals(factorArray[i].getIecPath())
                                        && ele.getArrgType() == factorArray[i].getArrgType())
                                {
                                    // 判断值是否为非布尔类型
                                    if (!isBoolean(factorArray[i].getIecVal()))
                                    {
                                        throw new DataAsException("SubLogicCompute Exception! currentEle:" + currentEle + ",value:" + factorArray[i].getIecVal());
                                    }

                                    boolean logicVal = Boolean.parseBoolean(factorArray[i].getIecVal());
                                    nums.add(notFlg ? !logicVal : logicVal);
                                    break;
                                }
                            }
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
        return nums.firstElement();
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
     * 判断字符串是否是true或false
     * 
     * @param str
     *            字符串
     * @return 是否true或false
     */
    private static boolean isBoolean(String str)
    {
        boolean flg = false;
        if ("false".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str))
        {
            flg = true;
        }
        return flg;

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
                while (operators.size() > 0 && !operators.lastElement().equals(Operator.BRACKETS))
                {
                    computeExpression(nums, operators);
                }
                if (operators.size() > 0)
                {
                    operators.remove(operators.size() - 1);
                }
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
                return 0.0;
            }

        };
    }

    /**
     * 关系操作符
     */
    private enum RelationalOperator implements BaseOperator<String>
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
                // try
                // {
                rtData = Boolean.toString(Double.parseDouble(num1) > Double.parseDouble(num2));
                // }
                // catch (Exception e)
                // {
                // // 如果异常直接返回false
                // }

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
                // try
                // {
                rtData = Boolean.toString(Double.parseDouble(num1) < Double.parseDouble(num2));
                // }
                // catch (Exception e)
                // {
                // // 如果异常直接返回false
                // }

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
                // try
                // {
                rtData = Boolean.toString(Double.parseDouble(num1) >= Double.parseDouble(num2));
                // }
                // catch (Exception e)
                // {
                // // 如果异常直接返回false
                // }

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
                // try
                // {
                rtData = Boolean.toString(Double.parseDouble(num1) <= Double.parseDouble(num2));
                // }
                // catch (Exception e)
                // {
                // // 如果异常直接返回false
                // }

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

    // /**
    // * 处理表达式中的求反函数，并将结果拼回表达式
    // *
    // * @param exp
    // * 表达式
    // * @return 处理后的表达式
    // */
    // private static String invertHandler(String exp)
    // {
    // try
    // {
    // String keyWord = "!{";
    // while (exp.indexOf(keyWord) != -1)
    // {
    // int sIndex = exp.indexOf(keyWord) + keyWord.length();
    // int eIndex = exp.indexOf("}", sIndex);
    // exp = exp.substring(0, exp.indexOf(keyWord)) + Boolean.toString(!logicExpression(exp.substring(sIndex, eIndex))) + exp.substring(eIndex + 1);
    // }
    // }
    // catch (Exception e)
    // {
    // exp = "false";
    // }
    //
    // return exp;
    // }

    /**
     * 获取关系计算操作符
     * 
     * 
     * @return 计算符列表
     */
    private static Map<String, BaseOperator<String>> getRelationalComputeOper()
    {
        return new HashMap<String, BaseOperator<String>>()
        {
            private static final long serialVersionUID = 3184770817813584824L;

            {
                put(">", RelationalOperator.GREATERTHAN);
                put("<", RelationalOperator.LESSTHAN);
                put("=", RelationalOperator.EQUAL);
                put("∈", RelationalOperator.IN);
                put("⊂", RelationalOperator.BETWEEN);
                put("≠", RelationalOperator.NOTEQUAL);
                put("≤", RelationalOperator.LESS_OR_EQUAL);
                put("≥", RelationalOperator.GREATER_OR_EQUAL);
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
     * 返回所有的计算因子（【单独的计算因子】、【按照关系运算符分割后的包含计算因子的公式】 或者【 整个公式】(有逻辑运算符或者全部是四则)）
     * 
     * @param computeExpr
     *            运算表达式
     * @return 计算因子List
     * @throws DataAsException
     */
    public static List<String> getRelationalFactorArray(String computeExpr) throws DataAsException
    {
        List<String> returndata = new ArrayList<>();

        if (computeExpr == null || computeExpr.isEmpty())
        {
            return returndata;
        }

        // 按照【所有运算符】拆分后的【子公式】包含计算因子，则记到返回值（单独的计算因子）
        StringTokenizer tokenizer = new StringTokenizer(computeExpr, OPERATCHAR, false);
        while (tokenizer.hasMoreTokens())
        {
            String currentEle = tokenizer.nextToken().trim();

            if (!currentEle.isEmpty() && OPERATCHAR.indexOf(currentEle) == -1 && currentEle.contains("{") && !returndata.contains(currentEle))
            {
                returndata.add(currentEle);
            }
        }

        // 包含关系运算符但不包含逻辑运算符
        if (DynamicRun.isRelationalExpress(computeExpr) && !DynamicRun.isLogicExpress(computeExpr))
        {
            // 关系运算符
            String ralationOperatChar = "><≥≤=≠∈⊂";

            // 按照【关系运算符】拆分后的【子公式】包含计算因子，则记到返回值（按照关系运算符分割后的包含计算因子的公式）
            StringTokenizer ralationTokenizer = new StringTokenizer(computeExpr, ralationOperatChar, false);
            while (ralationTokenizer.hasMoreTokens())
            {
                String currentEle = ralationTokenizer.nextToken().trim();

                if (!currentEle.isEmpty() && ralationOperatChar.indexOf(currentEle) == -1 && currentEle.contains("{") && !returndata.contains(currentEle))
                {
                    returndata.add(currentEle);
                }
            }
        }
        else if (computeExpr.contains("{") && !returndata.contains(computeExpr))
        {
            // 按照包含计算因子的整个公式）
            returndata.add(computeExpr);
        }

        return returndata;
    }
}
