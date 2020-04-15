package com.goldwind.datalogic.business.model;

import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.FactorDef.ArrgType;
import com.goldwind.datalogic.utils.FactorDef.FactorType;

/**
 * 计算因子类
 * 
 * @author 冯春源
 *
 */
public class FactorClass
{
    /**
     * 计算因子类型, 0 设备计算（支持单设备和多设备）, 1 组织计算
     */
    private FactorType factorType;
    /**
     * 组织编号(单组织（旧）)
     */
    private String orgId;
    /**
     * 组织编号(多组织（新-以后获取组织编号都从该属性获取，原orgId只是为了兼容以前的版本） ，SOAM V5.2.300.0添加)
     */
    private String[] orgIds;
    /**
     * 设备类型(单组织（旧）)
     */
    private int deviceType;
    /**
     * 设备类型(多组织（新-以后获取组设备类型都从该属性获取，原deviceType只是为了兼容以前的版本），设备类型为A（解析后为-1），表示所有设备类型 ，SOAM V5.2.300.0添加)
     */
    private int[] deviceTypes;
    /**
     * 设备编号(单设备（旧）)
     */
    private String deviceId;
    /**
     * 设备编号(多设备（新-以后获取设备编号都从该属性获取，原deviceId只是为了兼容以前的版本） ，SOAM V5.2.300.0添加)
     */
    private String[] deviceIds;
    /**
     * iec路径
     */
    private String iecPath;
    /**
     * 聚合方式(SUM、AVG、MAX、MIN(不区分大小写)以外的都会判断为不聚合，值为null)
     */
    private ArrgType arrgType;
    /**
     * iec值(数值/关系/逻辑运算值 V5.3.100.0新增)
     */
    private String iecVal;
    /**
     * iec值(数值运算值，后面版本可能废弃,请使用iecVal，计算时调用calculate())
     */
    private double val;
    /**
     * iec值(逻辑运算值，后面版本可能废弃,请使用iecVal，计算时调用calculate())
     */
    private boolean logicVal;
    /**
     * 计算因子公式
     */
    private String formula;

    public FactorType getFactorType()
    {
        return factorType;
    }

    public void setFactorType(FactorType factorType)
    {
        this.factorType = factorType;
    }

    public String getOrgId()
    {
        return orgId;
    }

    public void setOrgId(String orgId)
    {
        this.orgId = orgId;
    }

    public String[] getOrgIds()
    {
        return orgIds;
    }

    public void setOrgIds(String[] orgIds)
    {
        this.orgIds = orgIds;
    }

    public int getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(int deviceType)
    {
        this.deviceType = deviceType;
    }

    public int[] getDeviceTypes()
    {
        return deviceTypes;
    }

    public void setDeviceType(int[] deviceTypes)
    {
        this.deviceTypes = deviceTypes;
    }

    public String getDeviceId()
    {
        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String[] getDeviceIds()
    {
        return deviceIds;
    }

    public void setDeviceId(String[] deviceIds)
    {
        this.deviceIds = deviceIds;
    }

    public String getIecPath()
    {
        return iecPath;
    }

    public void setIecPath(String iecPath)
    {
        this.iecPath = iecPath;
    }

    public ArrgType getArrgType()
    {
        return arrgType;
    }

    public void setArrgType(ArrgType arrgType)
    {
        this.arrgType = arrgType;
    }

    public String getIecVal()
    {
        return iecVal;
    }

    public void setIecVal(String iecVal)
    {
        this.iecVal = iecVal;
    }

    public double getVal()
    {
        return val;
    }

    public void setVal(double val)
    {
        this.val = val;
        this.iecVal = String.valueOf(val);
    }

    public boolean getLogicVal()
    {
        return logicVal;
    }

    public void setLogicVal(boolean logicVal)
    {
        this.logicVal = logicVal;
        this.iecVal = String.valueOf(logicVal);
    }

    public String getFormula()
    {
        return formula;
    }

    public void setFormula(String formula)
    {
        this.formula = formula;
    }

    public FactorClass()
    {

    }

    public FactorClass(String formula) throws DataAsException
    {
        this.formula = formula;
        String[] tmp = formula.split(":");
        String firstAttribute = tmp[0].substring(1).trim();
        // 获取公式中的计算因子列表（新，带"0_"/"1_"前缀）
        if (firstAttribute.startsWith("0_") || firstAttribute.startsWith("1_"))
        {
            // 设备计算（支持单设备和多设备）
            if (firstAttribute.startsWith("0_"))
            {
                this.factorType = FactorType.SingleDevice;
                if (!tmp[0].contains(","))
                {
                    this.deviceId = tmp[0].substring(3);
                }
                this.deviceIds = tmp[0].substring(3).split(",");
            }
            // 组织计算
            else if (firstAttribute.startsWith("1_"))
            {
                this.factorType = FactorType.MultipleDevices;
                if (!tmp[0].contains(","))
                {
                    this.orgId = tmp[0].substring(3);
                }
                this.orgIds = tmp[0].substring(3).split(",");
            }

            // 设备计算/组织计算-四个属性
            if (tmp.length >= 4)
            {
                // 去除各属性前后空格
                tmp[0] = tmp[0].trim();
                tmp[1] = tmp[1].trim();
                tmp[2] = tmp[2].trim();
                tmp[3] = tmp[3].trim();

                if (!tmp[1].contains(","))
                {
                    this.deviceType = Integer.parseInt(tmp[1]);
                }
                this.deviceTypes = strArrayToIntArray(tmp[1].split(","));
                this.iecPath = tmp[2];
                if (tmp[3].length() > 0)
                {
                    switch (tmp[3].substring(0, tmp[3].length() - 1).toLowerCase()) {
                        case "sum":
                            this.arrgType = ArrgType.SUM;
                            break;
                        case "avg":
                            this.arrgType = ArrgType.AVG;
                            break;
                        case "max":
                            this.arrgType = ArrgType.max;
                            break;
                        case "min":
                            this.arrgType = ArrgType.min;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        else
        {
            // 获取公式中的计算因子列表（旧，不带"0_"/"1_"前缀）
            factorClassOld(formula);
        }
    }

    /**
     * 获取公式中的计算因子列表（旧-V5.3.200.0之前 2019.11.22）
     * 
     * @param formula
     *            运算表达式
     * @throws DataAsException
     *             自定义异常
     */
    private void factorClassOld(String formula) throws DataAsException
    {
        String[] tmp = formula.split(":");
        // iec量计算-（格式说明：@WTUR.Other.Wn.I16.StopModeWord）
        if (tmp.length < 2)
        {
            this.factorType = FactorType.IECCOMPUTE;
            this.iecPath = tmp[0].substring(1);
        }
        // 设备计算-两个属性（格式说明：{设备ID:IEC路径}）
        if (tmp.length == 2)
        {
            this.factorType = FactorType.SingleDevice;
            this.deviceId = tmp[0].substring(1);
            deviceIds = new String[1];
            this.deviceIds[0] = tmp[0].substring(1);
            this.iecPath = tmp[1].substring(0, tmp[1].length() - 1);
        }
        // 设备计算-三个属性（格式说明：{设备ID1,设备ID2:IEC路径:聚合方式}）
        else if (tmp.length == 3)
        {
            this.factorType = FactorType.SingleDevice;
            if (!tmp[0].contains(","))
            {
                this.deviceId = tmp[0].substring(1);
            }
            this.deviceIds = tmp[0].substring(1).split(",");
            this.iecPath = tmp[1];
            if (tmp[2].length() > 0)
            {
                switch (tmp[2].substring(0, tmp[2].length() - 1).toLowerCase()) {
                    case "sum":
                        this.arrgType = ArrgType.SUM;
                        break;
                    case "avg":
                        this.arrgType = ArrgType.AVG;
                        break;
                    case "max":
                        this.arrgType = ArrgType.max;
                        break;
                    case "min":
                        this.arrgType = ArrgType.min;
                        break;
                    default:
                        break;
                }
            }
        }
        // 组织计算-四个属性 （格式说明：{组织编号:设备类型:IEC路径:聚合方式}）
        else if (tmp.length >= 4)
        {
            this.factorType = FactorType.MultipleDevices;
            if (!tmp[0].contains(","))
            {
                this.orgId = tmp[0].substring(1);
            }
            this.orgIds = tmp[0].substring(1).split(",");
            if (!tmp[1].contains(","))
            {
                this.deviceType = Integer.parseInt(tmp[1]);
            }
            this.deviceTypes = strArrayToIntArray(tmp[1].split(","));
            this.iecPath = tmp[2];
            if (tmp[3].length() > 0)
            {
                switch (tmp[3].substring(0, tmp[3].length() - 1).toLowerCase()) {
                    case "sum":
                        this.arrgType = ArrgType.SUM;
                        break;
                    case "avg":
                        this.arrgType = ArrgType.AVG;
                        break;
                    case "max":
                        this.arrgType = ArrgType.max;
                        break;
                    case "min":
                        this.arrgType = ArrgType.min;
                        break;
                    default:
                        break;
                }
            }
        }
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
    private static int[] strArrayToIntArray(String[] srcArray) throws DataAsException
    {
        if (srcArray == null)
        {
            return new int[0];
        }
        int[] destArray = new int[srcArray.length];
        for (int i = 0; i < srcArray.length; i++)
        {
            if ("A".equals(srcArray[i].trim()))
            {
                destArray[i] = -1;
            }
            else
            {
                destArray[i] = Integer.parseInt(srcArray[i].trim());
            }
        }
        return destArray;
    }
}
