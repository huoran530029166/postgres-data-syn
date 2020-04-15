package com.goldwind.datalogic.businesslogic.eba;

import com.goldwind.datalogic.businesslogic.eba.Common.EbaType;

/**
 * 工厂
 * 
 * @author 谭璟
 *
 */
public class EbaFactory 
{
    
    public AbEbaLossHandle createEbaHandle(EbaType type)
    {
        AbEbaLossHandle ebahandle = null;
        if (type.equals(EbaType.MS))
        {
            ebahandle = new MsEba();
        }
        else
        {
            ebahandle = new RsEba();
        }
        return ebahandle;
    }
}
