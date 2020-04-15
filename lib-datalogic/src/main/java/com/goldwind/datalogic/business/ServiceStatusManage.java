package com.goldwind.datalogic.business;

import java.util.ArrayList;
import java.util.List;

import com.goldwind.datalogic.business.model.ServiceStatusEnum;

public class ServiceStatusManage
{
    /**
     * 程序启动模块列表
     */
    private static volatile List<ServiceStatusEnum> SERVICESTATUS = new ArrayList<>();

    public static List<ServiceStatusEnum> getServiceStatus()
    {
        return SERVICESTATUS;
    }

    /**
     * 异常信息中文
     * 
     */
    private static String errorMsg;

    public static String getErrorMsg()
    {
        return errorMsg;
    }

    public static void setErrorMsg(String msg)
    {
        errorMsg = msg;
    }

    public static String geteServiceStatus()
    {
        String mString = "";
        for (ServiceStatusEnum s : SERVICESTATUS)
        {
            if (s.getCode() != ServiceStatusEnum.Error.getCode())
            {
                mString += s.getCode() + "-" + s.getDescrEn() + "-" + s.getStatus() + ",";
            }
            else
            {
                mString += s.getCode() + "-" + errorMsg + ",";
            }
        }
        return mString.substring(0, mString.length() - 1);
    }

    public static void setServiceStatus(ServiceStatusEnum sse, int status)
    {

        for (ServiceStatusEnum s : SERVICESTATUS)
        {
            if (s.getCode() == sse.getCode())
            {
                s.serviceStatusEnum(status);
            }
        }
    }

}
