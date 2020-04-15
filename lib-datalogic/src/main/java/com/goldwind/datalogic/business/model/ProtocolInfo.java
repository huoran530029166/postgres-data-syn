package com.goldwind.datalogic.business.model;

import java.util.HashMap;
import java.util.List;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.business.IecPathDef;
import com.goldwind.datalogic.utils.NetCommDef.WfDeviceType;

/**
 * 协议信息
 * 
 * @author 谭璟
 *
 */
public class ProtocolInfo
{
    /**
     * 日志对象
     */
    private static Log LOGGER = Log.getLog(ProtocolInfo.class);

    /**
     * 协议编号
     */
    private int protocolId;

    /**
     * 设备类型
     */
    private WfDeviceType deviceType;

    /**
     * 维护状态
     */
    private String maintainState;

    /**
     * plc标志
     */
    private int plcFlag;

    /**
     * 历史风速路径
     */
    private ProPath hisWindSpeedPath;

    /**
     * 历史风功率路径
     */
    private ProPath hisWindPowerPath;

    /**
     * 历史风向路径
     */
    private ProPath hisWindVanePath;

    /**
     * 主轮询路径
     */
    private String[] mainPaths;

    /**
     * 保存的瞬态路径
     */
    private String[] saveRealtimePaths;

    /**
     * 变位存储路径
     */
    private String[] changePaths;

    /**
     * 瞬态时间路径在路径数组中的位置
     */
    private int realTimePathPos;

    /**
     * 风机类型
     */
    private String wtType;

    /**
     * 额定功率
     */
    private String standardPower;

    /**
     * 十分钟数据路径列表
     */
    private ProPath[] tenMinutesPaths;

    /**
     * iec对应的propath数据
     */
    private HashMap<String, ProPath> propathMap;

    /**
     * iecPathList(transtype<2 遥信遥控转换使用)
     */
    private List<String> iecPathList;

    public ProtocolInfo(int pProtocolId, ProPath[] pHisDataPaths, String[] pMainPaths, String[] pSaveRealtimePaths, String[] pChangePaths, WfDeviceType pDeviceType, String maintainState, int pPlcFlag,
            String wtType, String standardPower, HashMap<String, ProPath> propathMap, List<String> iecPathList) throws DataAsException
    {
        this.protocolId = pProtocolId;
        this.deviceType = pDeviceType;
        this.maintainState = maintainState;
        this.plcFlag = pPlcFlag;
        this.tenMinutesPaths = pHisDataPaths; // 赋值公开变量,不能使用内部变量
        this.mainPaths = pMainPaths;
        this.saveRealtimePaths = pSaveRealtimePaths;
        this.changePaths = pChangePaths;
        this.realTimePathPos = ArrayOper.findDataInArray(IecPathDef.REALDATATIME, pSaveRealtimePaths);
        this.wtType = wtType;
        this.standardPower = standardPower;
        this.propathMap = propathMap;
        this.setIecPathList(iecPathList);
        if (pDeviceType == WfDeviceType.WindTurbine)
        {
            // 如果是风机,设置历史风速和历史风向的iec路径
            // setIecPath(IecPathDef.getTURBHISWINDSPEED(), hisWindSpeedPath);
            // setIecPath(IecPathDef.getTRUBHISWINDVANE(), hisWindVanePath);
            // setIecPath(IecPathDef.getTRUBHISWINDPOWER(), hisWindPowerPath);

            hisWindSpeedPath = setIecPath(IecPathDef.TURBHISWINDSPEED);
            hisWindVanePath = setIecPath(IecPathDef.TRUBHISWINDVANEABS);
            hisWindPowerPath = setIecPath(IecPathDef.TRUBHISWINDPOWER);
        }
        else if (pDeviceType == WfDeviceType.WindTower)
        {
            // 如果是测风塔,设置历史风速、历史风向和历史风功率路径
            // setIecPath(IecPathDef.getTOWERHISWINDSPEED(), hisWindSpeedPath);
            // setIecPath(IecPathDef.getTOWERHISWINDVANE(), hisWindVanePath);
            // setIecPath(IecPathDef.getTOWERHISWINDPOWER(), hisWindPowerPath);

            hisWindSpeedPath = setIecPath(IecPathDef.TOWERHISWINDSPEEDHUB);
            hisWindVanePath = setIecPath(IecPathDef.TOWERHISWINDVANEHUB);
            hisWindPowerPath = setIecPath(IecPathDef.TOWERHISWINDPOWER);
        }
    }

    public int getProtocolId()
    {
        return protocolId;
    }

    public void setProtocolId(int protocolId)
    {
        this.protocolId = protocolId;
    }

    public WfDeviceType getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(WfDeviceType deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getMaintainState()
    {
        return maintainState;
    }

    public void setMaintainState(String maintainState)
    {
        this.maintainState = maintainState;
    }

    public int getPlcFlag()
    {
        return plcFlag;
    }

    public void setPlcFlag(int plcFlag)
    {
        this.plcFlag = plcFlag;
    }

    public ProPath getHisWindSpeedPath()
    {
        return hisWindSpeedPath;
    }

    public void setHisWindSpeedPath(ProPath hisWindSpeedPath)
    {
        this.hisWindSpeedPath = hisWindSpeedPath;
    }

    public ProPath getHisWindPowerPath()
    {
        return hisWindPowerPath;
    }

    public void setHisWindPowerPath(ProPath hisWindPowerPath)
    {
        this.hisWindPowerPath = hisWindPowerPath;
    }

    public ProPath getHisWindVanePath()
    {
        return hisWindVanePath;
    }

    public void setHisWindVanePath(ProPath hisWindVanePath)
    {
        this.hisWindVanePath = hisWindVanePath;
    }

    public String[] getMainPaths()
    {
        return mainPaths;
    }

    public void setMainPaths(String[] mainPaths)
    {
        this.mainPaths = mainPaths;
    }

    public String[] getSaveRealtimePaths()
    {
        return saveRealtimePaths;
    }

    public void setSaveRealtimePaths(String[] saveRealtimePaths)
    {
        this.saveRealtimePaths = saveRealtimePaths;
    }

    public String[] getChangePaths()
    {
        return changePaths;
    }

    public void setChangePaths(String[] changePaths)
    {
        this.changePaths = changePaths;
    }

    public int getRealTimePathPos()
    {
        return realTimePathPos;
    }

    public void setRealTimePathPos(int realTimePathPos)
    {
        this.realTimePathPos = realTimePathPos;
    }

    public String getWtType()
    {
        return wtType;
    }

    public void setWtType(String wtType)
    {
        this.wtType = wtType;
    }

    public String getStandardPower()
    {
        return standardPower;
    }

    public void setStandardPower(String standardPower)
    {
        this.standardPower = standardPower;
    }

    public ProPath[] getTenMinutesPaths()
    {
        return tenMinutesPaths;
    }

    /**
     * 十分钟 数据 路径，存放在 hashmap中
     * 
     * @param tenMinutesPaths
     *            十分钟路径
     */
    public void setTenMinutesPaths(ProPath[] tenMinutesPaths)
    {
        this.tenMinutesPaths = tenMinutesPaths;
        HashMap<String, Integer> htTenMinutesPaths = new HashMap<>();
        for (int i = 0; i < tenMinutesPaths.length; i++)
        {
            htTenMinutesPaths.put(tenMinutesPaths[i].getIecPath(), 1);
        }
    }

    /**
     * 得到十分钟iec路径的偏移量
     * 
     * @param iecPath
     *            ice路径
     * @return 成功返回偏移量，失败返回-1
     */
    public int getTenMinutesOffset(String iecPath)
    {
        if (tenMinutesPaths != null && iecPath != null)
        {
            for (int i = 0; i < tenMinutesPaths.length; i++)
            {
                if (tenMinutesPaths[i].getIecPath() == iecPath)
                {
                    return Integer.parseInt(tenMinutesPaths[i].getColOffsetVal());
                }
            }
        }
        return -1;
    }

    /**
     * 根据pathname 返回propath对象
     * 
     * @param pathName
     *            路径名称
     * @return propath 对象
     */
    private ProPath setIecPath(String pathName)
    {

        ProPath pathVar = null;
        if (pathName == null)
        {
            return pathVar;
        }

        String tempPathName = pathName.replace("[AVG]", "");
        for (int i = 0; i < tenMinutesPaths.length; i++)
        {
            if (tenMinutesPaths[i].getIecPath().equals(pathName))
            {
                pathVar = tenMinutesPaths[i];
                break;
            }
            else
            {
                String path = tenMinutesPaths[i].getIecPath().replace("[AVG]", "");
                if (path.indexOf(tempPathName + ".") == 0)
                {
                    try
                    {
                        if (pathVar == null || (pathVar.getIecPath().compareTo(tenMinutesPaths[i].getIecPath()) < 0))
                        {
                            pathVar = tenMinutesPaths[i];
                        }
                    }
                    catch (Exception e)
                    {
                        LOGGER.error("ProtocolInfo_setIecPath" + e.getClass().getName(), e);
                    }
                }
            }
        }

        return pathVar;
    }

    public HashMap<String, ProPath> getPropathMap()
    {
        return propathMap;
    }

    public void setPropathMap(HashMap<String, ProPath> propathMap)
    {
        this.propathMap = propathMap;
    }

    public List<String> getIecPathList()
    {
        return iecPathList;
    }

    public void setIecPathList(List<String> iecPathList)
    {
        this.iecPathList = iecPathList;
    }
}
