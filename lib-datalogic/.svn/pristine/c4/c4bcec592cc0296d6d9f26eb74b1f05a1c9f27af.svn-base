package com.goldwind.datalogic.business;

import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.model.FactorClass;

import junit.framework.Assert;

public class FormulaClassTest
{

    // 一般场景
    @Test
    public void testCalculate1() throws DataAsException
    {
        FormulaClass logicObject0 = new FormulaClass("1000*{@wfid:104:EMSD.PwrAt.Ra.F32.PlannedValue:SUM}-1000*{@wfid:104:EMSD.PwrAt.Ra.F32.Control:SUM} >10");
        FactorClass[] logicFactors0 = logicObject0.getFactorArray();
        logicFactors0[0].setIecVal("76");
        logicFactors0[1].setIecVal("75");
        FormulaResult formulaResult = logicObject0.calculate();
        Assert.assertEquals(true, formulaResult.getLogicResult());
    }

    // ******************************************逻辑运算测试**********************************************
    @Test
    public void testCalculate2() throws DataAsException
    {
        FormulaClass logicObject = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}&{632801001:FHX.Bool.Rd.b0.0501}");
        FactorClass[] logicFactors = logicObject.getFactorArray();

        logicFactors[0].setLogicVal(false);
        logicFactors[1].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子）
        boolean virtualIecVal1 = logicObject.calculate().getLogicResult();
        Assert.assertEquals(true, virtualIecVal1);
    }

    @Test
    public void testCalculate3() throws DataAsException
    {
        FormulaClass logicObject = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}&{632801001:FHX.Bool.Rd.b0.0501}");
        FactorClass[] logicFactors = logicObject.getFactorArray();

        logicFactors[0].setLogicVal(true);
        logicFactors[1].setLogicVal(false);
        // 得到逻辑计算结果（含计算因子）
        boolean virtualIecVal2 = logicObject.calculate().getLogicResult();
        Assert.assertEquals(false, virtualIecVal2);
    }

    @Test
    public void testCalculate4() throws DataAsException
    {
        FormulaClass logicObject = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}&{632801001:FHX.Bool.Rd.b0.0501}");
        FactorClass[] logicFactors = logicObject.getFactorArray();

        logicFactors[0].setLogicVal(true);
        logicFactors[1].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子）
        boolean virtualIecVal3 = logicObject.calculate().getLogicResult();
        Assert.assertEquals(false, virtualIecVal3);
    }

    @Test
    public void testCalculate5() throws DataAsException
    {
        FormulaClass logicObject = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}&{632801001:FHX.Bool.Rd.b0.0501}");
        FactorClass[] logicFactors = logicObject.getFactorArray();

        logicFactors[0].setLogicVal(false);
        logicFactors[1].setLogicVal(false);
        // 得到逻辑计算结果（含计算因子）
        boolean virtualIecVal4 = logicObject.calculate().getLogicResult();
        Assert.assertEquals(false, virtualIecVal4);
    }

    @Test
    public void testCalculate6() throws DataAsException
    {
        FormulaClass logicObject1 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|false");
        FactorClass[] logicFactors1 = logicObject1.getFactorArray();
        logicFactors1[0].setIecVal("false");
        // 得到逻辑计算结果（计算因子和true/false 混合）
        boolean virtualIecVal5 = logicObject1.calculate().getLogicResult();
        Assert.assertEquals(true, virtualIecVal5);
    }

    @Test
    public void testCalculate7() throws DataAsException
    {
        // 得到逻辑计算结果（不含计算因子）
        FormulaClass logicObject2_1 = new FormulaClass("!false&false|true");
        Assert.assertEquals(true, logicObject2_1.calculate().getLogicResult());
    }

    @Test
    public void testCalculate8() throws DataAsException
    {
        // 得到逻辑计算结果（不含计算因子）
        FormulaClass logicObject2_2 = new FormulaClass("false&(false|true)");
        Assert.assertEquals(false, logicObject2_2.calculate().getLogicResult());
    }

    @Test
    public void testCalculate9() throws DataAsException
    {
        FormulaClass logicObject3 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|2∈[1,3,4,5]|3>2");
        FactorClass[] logicFactors3 = logicObject3.getFactorArray();
        logicFactors3[0].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject3.calculate().getLogicResult());
    }

    @Test
    public void testCalculate10() throws DataAsException
    {
        FormulaClass logicObject4 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|false|1+1=2");
        FactorClass[] logicFactors4 = logicObject4.getFactorArray();
        logicFactors4[0].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject4.calculate().getLogicResult());
    }

    @Test
    public void testCalculate11() throws DataAsException
    {
        FormulaClass logicObject5 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|false|1+1*2≥3");
        FactorClass[] logicFactors5 = logicObject5.getFactorArray();
        logicFactors5[0].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject5.calculate().getLogicResult());
    }

    @Test
    public void testCalculate12() throws DataAsException
    {
        FormulaClass logicObject6 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|false|1+1*2>3");
        FactorClass[] logicFactors6 = logicObject6.getFactorArray();
        logicFactors6[0].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject6.calculate().getLogicResult());
    }

    @Test
    public void testCalculate13() throws DataAsException
    {
        FormulaClass logicObject7 = new FormulaClass("!{632801001:FHX.Bool.Rd.b0.0502}|false|(1+1)*2>3");
        FactorClass[] logicFactors7 = logicObject7.getFactorArray();
        logicFactors7[0].setLogicVal(true);
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject7.calculate().getLogicResult());
    }

    @Test
    public void testCalculate14() throws DataAsException
    {
        FormulaClass logicObject8 = new FormulaClass("false&false|((1+1)*2>3&(1+2)*2>5)");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject8.calculate().getLogicResult());
    }

    @Test
    public void testCalculate15() throws DataAsException
    {
        FormulaClass logicObject14 = new FormulaClass(
                "false&false|(({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5)");
        FactorClass[] logicFactors14 = logicObject14.getFactorArray();
        logicFactors14[0].setIecVal("1");
        logicFactors14[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject14.calculate().getLogicResult());
    }

    @Test
    public void testCalculate16() throws DataAsException
    {
        FormulaClass logicObject8_2 = new FormulaClass("false&false|(1+1)*2>3&(1+2)*2>5");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject8_2.calculate().getLogicResult());
    }

    @Test
    public void testCalculate17() throws DataAsException
    {
        FormulaClass logicObject13 = new FormulaClass(
                "false&false|({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5");
        FactorClass[] logicFactors13 = logicObject13.getFactorArray();
        logicFactors13[0].setIecVal("1");
        logicFactors13[3].setIecVal("2");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject13.calculate().getLogicResult());
    }

    @Test
    public void testCalculate18() throws DataAsException
    {
        FormulaClass logicObject9 = new FormulaClass("false&(false|((1+1)*2>3&(1+2)*2>5))");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject9.calculate().getLogicResult());
    }

    @Test
    public void testCalculate19() throws DataAsException
    {
        FormulaClass logicObject10 = new FormulaClass(
                "false&(false|(({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5))");
        FactorClass[] logicFactors10 = logicObject10.getFactorArray();
        logicFactors10[0].setIecVal("1");
        logicFactors10[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject10.calculate().getLogicResult());
    }

    @Test
    public void testCalculate20() throws DataAsException
    {
        FormulaClass logicObject11 = new FormulaClass(
                "false&(false|({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5)");
        FactorClass[] logicFactors11 = logicObject11.getFactorArray();
        logicFactors11[0].setIecVal("1");
        logicFactors11[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject11.calculate().getLogicResult());
    }

    @Test
    public void testCalculate21() throws DataAsException
    {
        FormulaClass logicObject999 = new FormulaClass("{@wfid:1:MH.PwrAt.Ra.F32:SUM} -{@wfid:104:EMSD.PwrAt.Ra.F32.PlannedValue:sum}≥1000");
        FactorClass[] logicFactors999 = logicObject999.getFactorArray();
        logicFactors999[0].setIecVal("3000");
        logicFactors999[1].setIecVal("2000");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject999.calculate().getLogicResult());
    }

    @Test
    public void testCalculate22() throws DataAsException
    {
        FormulaClass logicObject8 = new FormulaClass("false&false|((1+1)*2>3&(1+2)*2>5)");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject8.calculate().getLogicResult());
    }

    @Test
    public void testCalculate23() throws DataAsException
    {
        FormulaClass logicObject8 = new FormulaClass("false&false|((1+1)*2>3&(1+2)*2>5)");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject8.calculate().getLogicResult());
    }

    @Test
    public void testCalculate24() throws DataAsException
    {
        FormulaClass logicObject14 = new FormulaClass(
                "false&false|(({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5)");
        FactorClass[] logicFactors14 = logicObject14.getFactorArray();
        logicFactors14[0].setIecVal("1");
        logicFactors14[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject14.calculate().getLogicResult());
    }

    @Test
    public void testCalculate25() throws DataAsException
    {
        FormulaClass logicObject8_2 = new FormulaClass("false&false|(1+1)*2>3&(1+2)*2>5");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject8_2.calculate().getLogicResult());
    }

    @Test
    public void testCalculate26() throws DataAsException
    {
        FormulaClass logicObject13 = new FormulaClass(
                "false&false|({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5");
        FactorClass[] logicFactors13 = logicObject13.getFactorArray();
        logicFactors13[0].setIecVal("1");
        logicFactors13[3].setIecVal("2");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject13.calculate().getLogicResult());
    }

    @Test
    public void testCalculate27() throws DataAsException
    {
        FormulaClass logicObject9 = new FormulaClass("false&(false|((1+1)*2>3&(1+2)*2>5))");
        // 得到逻辑计算结果（不含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject9.calculate().getLogicResult());
    }

    @Test
    public void testCalculate28() throws DataAsException
    {
        FormulaClass logicObject10 = new FormulaClass(
                "false&(false|(({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5))");
        FactorClass[] logicFactors10 = logicObject10.getFactorArray();
        logicFactors10[0].setIecVal("1");
        logicFactors10[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject10.calculate().getLogicResult());
    }

    @Test
    public void testCalculate29() throws DataAsException
    {
        FormulaClass logicObject11 = new FormulaClass(
                "false&(false|({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*2>3&({632801001:FHX.Bool.Rd.b0.0502}+{632801002:FHX.Bool.Rd.b0.0502})*2>5)");
        FactorClass[] logicFactors11 = logicObject11.getFactorArray();
        logicFactors11[0].setIecVal("1");
        logicFactors11[3].setIecVal("2");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(false, logicObject11.calculate().getLogicResult());
    }

    @Test
    public void testCalculate30() throws DataAsException
    {
        FormulaClass logicObject999 = new FormulaClass("{@wfid:1:MH.PwrAt.Ra.F32:SUM} -{@wfid:104:EMSD.PwrAt.Ra.F32.PlannedValue:sum}≥1000");
        FactorClass[] logicFactors999 = logicObject999.getFactorArray();
        logicFactors999[0].setIecVal("3000");
        logicFactors999[1].setIecVal("2000");
        // 得到逻辑计算结果（含计算因子，四则、关系、逻辑 混合）
        Assert.assertEquals(true, logicObject999.calculate().getLogicResult());
    }

    // ******************************************四则运算测试**********************************************
    @Test
    public void testCalculate31() throws DataAsException
    {
        String arithmeticFormula1 = "((1.5+1))*2";
        FormulaClass arithmeticObject1 = new FormulaClass(arithmeticFormula1);
        Assert.assertEquals(5.0, arithmeticObject1.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate32() throws DataAsException
    {
        String arithmeticFormula2 = "((1+1))*(2+3)-1";
        FormulaClass arithmeticObject2 = new FormulaClass(arithmeticFormula2);
        Assert.assertEquals(9.0, arithmeticObject2.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate33() throws DataAsException
    {
        String arithmeticFormula3 = "(1+1)*(1.5+3.5)*2-1*5";
        FormulaClass arithmeticObject3 = new FormulaClass(arithmeticFormula3);
        Assert.assertEquals(15.0, arithmeticObject3.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate34() throws DataAsException
    {
        String arithmeticFormula4_1 = "100000+0.01";
        FormulaClass arithmeticObject4_1 = new FormulaClass(arithmeticFormula4_1);
        Assert.assertEquals(100000.01, arithmeticObject4_1.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate35() throws DataAsException
    {
        String arithmeticFormula4_2 = "100000-0.09";
        FormulaClass arithmeticObject4_2 = new FormulaClass(arithmeticFormula4_2);
        Assert.assertEquals(99999.91, arithmeticObject4_2.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate36() throws DataAsException
    {
        String arithmeticFormula4_3 = "2*1.01";
        FormulaClass arithmeticObject4_3 = new FormulaClass(arithmeticFormula4_3);
        Assert.assertEquals(2.02, arithmeticObject4_3.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate37() throws DataAsException
    {
        String arithmeticFormula4_4 = "10/3";
        FormulaClass arithmeticObject4_4 = new FormulaClass(arithmeticFormula4_4);
        Assert.assertEquals(3.33, arithmeticObject4_4.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate38() throws DataAsException
    {
        String arithmeticFormula4_5 = "10%3";
        FormulaClass arithmeticObject4_5 = new FormulaClass(arithmeticFormula4_5);
        Assert.assertEquals(1d, arithmeticObject4_5.calculate().getArithmeticResult());
    }

    @Test
    public void testCalculate39() throws DataAsException
    {
        String arithmeticFormula5 = "({632801001:FHX.Bool.Rd.b0.0502}+{632801001:FHX.Bool.Rd.b0.0502})*({632801002:FHX.Bool.Rd.b0.0502}+{632801003:FHX.Bool.Rd.b0.0502})*2-1*5";
        FormulaClass arithmeticObject5 = new FormulaClass(arithmeticFormula5);
        FactorClass[] arithmeticFactors5 = arithmeticObject5.getFactorArray();
        arithmeticFactors5[0].setIecVal("1");
        arithmeticFactors5[1].setIecVal("1");
        arithmeticFactors5[2].setIecVal("2");
        arithmeticFactors5[3].setIecVal("3");
        Assert.assertEquals(15.0d, arithmeticObject5.calculate().getArithmeticResult());
    }

    // ******************************************关系运算测试**********************************************
    @Test
    public void testCalculate40() throws DataAsException
    {
        String relationalFormula1 = "(1+1)=2";
        FormulaClass relationalObject1 = new FormulaClass(relationalFormula1);
        Assert.assertEquals(true, relationalObject1.calculate().getLogicResult());
    }

    @Test
    public void testCalculate41() throws DataAsException
    {
        String relationalFormula2 = "1∈[1,3,4,5]";
        FormulaClass relationalObject2 = new FormulaClass(relationalFormula2);
        Assert.assertEquals(true, relationalObject2.calculate().getLogicResult());
    }

    @Test
    public void testCalculate42() throws DataAsException
    {
        String relationalFormula3 = "1⊂[1,5]";
        FormulaClass relationalObject3 = new FormulaClass(relationalFormula3);
        Assert.assertEquals(true, relationalObject3.calculate().getLogicResult());
    }

    @Test
    public void testCalculate43() throws DataAsException
    {

        String relationalFormula4 = "5⊂[1,5]";
        FormulaClass relationalObject4 = new FormulaClass(relationalFormula4);
        Assert.assertEquals(true, relationalObject4.calculate().getLogicResult());
    }

    @Test
    public void testCalculate44() throws DataAsException
    {
        String relationalFormula5 = "1⊂(1,5]";
        FormulaClass relationalObject5 = new FormulaClass(relationalFormula5);
        Assert.assertEquals(false, relationalObject5.calculate().getLogicResult());
    }

    @Test
    public void testCalculate45() throws DataAsException
    {
        String relationalFormula6 = "5⊂[1,5)";
        FormulaClass relationalObject6 = new FormulaClass(relationalFormula6);
        Assert.assertEquals(false, relationalObject6.calculate().getLogicResult());
    }

    @Test
    public void testCalculate46() throws DataAsException
    {
        String relationalFormula7 = "2⊂(1,5)";
        FormulaClass relationalObject7 = new FormulaClass(relationalFormula7);
        Assert.assertEquals(true, relationalObject7.calculate().getLogicResult());
    }

    @Test
    public void testCalculate47() throws DataAsException
    {
        String relationalFormula8 = "(2+2)∈[1,3,4,5]";
        FormulaClass relationalObject8 = new FormulaClass(relationalFormula8);
        Assert.assertEquals(true, relationalObject8.calculate().getLogicResult());
    }

    @Test
    public void testCalculate48() throws DataAsException
    {
        String relationalFormula9 = "(2+2)⊂(1,5)";
        FormulaClass relationalObject9 = new FormulaClass(relationalFormula9);
        Assert.assertEquals(true, relationalObject9.calculate().getLogicResult());
    }

    @Test
    public void testCalculate49() throws DataAsException
    {
        String relationalFormula10 = "(2+3)⊂(1,5)";
        FormulaClass relationalObject10 = new FormulaClass(relationalFormula10);
        Assert.assertEquals(false, relationalObject10.calculate().getLogicResult());
    }

    // ******************************************新公式格式（带0_/1_）**********************************************
    @Test
    public void testCalculate50() throws DataAsException
    {
        String newFormula1 = "({0_632801:1:FHX.Bool.Rd.b0.0502:SUM}+{1_632801:1:FHX.Bool.Rd.b0.0502:SUM})*({632801002:FHX.Bool.Rd.b0.0502}+{632801003:FHX.Bool.Rd.b0.0502})*2-1*5";
        FormulaClass newObject1 = new FormulaClass(newFormula1);
        FactorClass[] newFactors1 = newObject1.getFactorArray();
        newFactors1[0].setIecVal("1");
        newFactors1[1].setIecVal("1");
        newFactors1[2].setIecVal("2");
        newFactors1[3].setIecVal("3");
        Assert.assertEquals(15.0d, newObject1.calculate().getArithmeticResult());
    }

    // @Test
    // public void testOnlyArithmetic()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testOnlyRelationalOperate()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testOnlyLogicOperate()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testCompute()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testSubCompute()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testRelationalCompute()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testLogicCompute()
    // {
    // fail("Not yet implemented");
    // }
    //
    // @Test
    // public void testSubLogicCompute()
    // {
    // fail("Not yet implemented");
    // }

}
