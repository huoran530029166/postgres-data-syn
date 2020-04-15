package com.goldwind.datalogic.business.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.exception.DataAsException;
import com.goldwind.datalogic.utils.ControlProcessDef.NetDataTimeType;
import com.goldwind.datalogic.utils.DataAssemble;
import com.goldwind.datalogic.utils.DataParse;
import com.goldwind.datalogic.utils.DataTimeManage;
import com.goldwind.datalogic.utils.NetCommDef.NetDataClassify;
import com.goldwind.datalogic.utils.NetCommDef.NetUpDataType;

/**
 * 网络数据
 * 
 * @author 王瑞博
 *
 */
public class NetDataInfo
{
    /**
     * 数据
     */
    private String data;
    /**
     * 数据时间
     */
    private String dataTime;
    /**
     * 数据类型
     */
    private NetUpDataType netUpDataType;

    /**
     * 数据分类
     */
    private NetDataClassify netDataClassify;

    /**
     * 数据设备id或者数据风场id
     */
    private String devid;

    /**
     * "srcData"常量
     */
    private String srcDataStr = "srcData";

    /**
     * 解析设备
     * 
     * @param pData
     *            原始数据
     * @throws DataAsException
     */
    public NetDataInfo(String pData) throws DataAsException
    {
        data = pData;
        dataTime = "";
        netUpDataType = DataParse.getUpDataType(pData);
        if (netUpDataType != NetUpDataType.WarnEnd)
        {
            String pdata = (String) (DataParse.removeSedimentFlg(pData).get(srcDataStr));
            devid = pdata.split("\\|", -1)[getDevSta(netUpDataType)];
            if (devid.length() > 16)
            {
                devid = null;
            }
        }
        else
        {
            devid = null;
        }
        setNetDataClassify(getDataClassify(netUpDataType));
    }

    public NetDataInfo(String pData, String pDataTime, NetUpDataType pNetUpDataType) throws DataAsException
    {
        data = pData;
        dataTime = pDataTime;
        netUpDataType = pNetUpDataType;
        if (pNetUpDataType != NetUpDataType.WarnEnd && pNetUpDataType != NetUpDataType.MONITORJSON)
        {
            String pdata = (String) (DataParse.removeSedimentFlg(pData).get(srcDataStr));
            devid = pdata.split("\\|")[getDevSta(netUpDataType)];
            if (devid.length() > 16)
            {
                devid = null;
            }
        }
        else
        {
            devid = null;
        }
        setNetDataClassify(getDataClassify(netUpDataType));
    }

    public NetDataInfo(String pData, NetDataTimeType timeType, NetUpDataType pNetUpDataType) throws DataAsException
    {
        dataTime = "";
        Date date = new Date();
        if (timeType == NetDataTimeType.Msel)
        {
            dataTime = new SimpleDateFormat(DataAsDef.DATETIMEMSFORMAT).format(date);
        }
        else if (timeType == NetDataTimeType.Second)
        {
            dataTime = new SimpleDateFormat(DataAsDef.DATETIMEFORMATSTR).format(date);
        }
        else if (timeType == NetDataTimeType.OneMinute)
        {
            dataTime = DataTimeManage.getDataOneMinute(null);
        }
        else if (timeType == NetDataTimeType.TenMinutes)
        {
            dataTime = DataTimeManage.getDataTenMinute(null);
        }
        data = pData;
        netUpDataType = pNetUpDataType;
        if (pNetUpDataType != NetUpDataType.WarnEnd)
        {
            String pdata = (String) (DataParse.removeSedimentFlg(pData).get(srcDataStr));
            devid = pdata.split("\\|")[1];
            if (!devid.matches("[0-9]+"))
            {
                devid = null;
            }
        }
        else
        {
            devid = null;
        }
        setNetDataClassify(getDataClassify(pNetUpDataType));
    }

    /**
     * 得到沉积数据
     * 
     * @return data 趁机数据
     */
    public String getSedimentData()
    {
        // 沉积数据
        if (netUpDataType == NetUpDataType.DevSedimentData || dataTime == null || dataTime.isEmpty())
        {
            return data;
        }
        else
        {
            return DataAssemble.sedimentDataAsm(data, dataTime);
        }
    }

    /**
     * 得到数据类型
     * 
     * @param pNetUpDataType
     *            数据具体类型
     * @return 数据类型
     */
    public NetDataClassify getDataClassify(NetUpDataType pNetUpDataType)
    {
        NetDataClassify pNetDataClassify = NetDataClassify.DBPRODUCT;
        if (pNetUpDataType == NetUpDataType.SqlData || pNetUpDataType == NetUpDataType.UpSqlData || pNetUpDataType == NetUpDataType.FIVEMINDATA || pNetUpDataType == NetUpDataType.NewFiveData
                || pNetUpDataType == NetUpDataType.OLDDEVFIVEDATA || pNetUpDataType == NetUpDataType.OrderLog || pNetUpDataType == NetUpDataType.RunLog)
        {
            pNetDataClassify = NetDataClassify.UNKNOWN;
        }
        else if (pNetUpDataType == NetUpDataType.DevRealtimeData || pNetUpDataType == NetUpDataType.OLDDEVREALTIMEDATA)
        {
            pNetDataClassify = NetDataClassify.RT;
        }
        return pNetDataClassify;
    }

    /**
     * 根据数据类型得到设备id所在位置
     * 
     * @return 设备id所在数据位置
     */
    private int getDevSta(NetUpDataType pNetUpDataType)
    {
        int i = 1;
        switch (pNetUpDataType) {
            case WarnLog:
                i = 5;
                break;
            case ProtectData:
                i = 3;
                break;
            case ProtectCallLog:
                i = 8;
                break;
            case ProtectSetLog:
                i = 9;
                break;
            case GPSData:
                i = 5;
                break;
            default:
                i = 1;
                break;
        }
        return i;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getDataTime()
    {
        return dataTime;
    }

    public void setDataTime(String dataTime)
    {
        this.dataTime = dataTime;
    }

    public NetUpDataType getNetUpDataType()
    {
        return netUpDataType;
    }

    public void setNetUpDataType(NetUpDataType netUpDataType)
    {
        this.netUpDataType = netUpDataType;
    }

    public NetDataClassify getNetDataClassify()
    {
        return netDataClassify;
    }

    public void setNetDataClassify(NetDataClassify netDataClassify)
    {
        this.netDataClassify = netDataClassify;
    }

    public String getDevid()
    {
        return devid;
    }

    public void setDevid(String devid)
    {
        this.devid = devid;
    }
}
