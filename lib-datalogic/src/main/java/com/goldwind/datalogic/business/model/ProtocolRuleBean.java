package com.goldwind.datalogic.business.model;

/**
 * config.protocolrule对应实体表
 * 
 * @author 33359
 *
 */
public class ProtocolRuleBean
{

    private int protocolId;

    private int stopStatusTtype;

    private int standardPower;

    public int getProtocolId()
    {
        return protocolId;
    }

    public void setProtocolId(int protocolId)
    {
        this.protocolId = protocolId;
    }

    public int getStopStatusTtype()
    {
        return stopStatusTtype;
    }

    public void setStopStatusTtype(int stopStatusTtype)
    {
        this.stopStatusTtype = stopStatusTtype;
    }

    public int getStandardPower()
    {
        return standardPower;
    }

    public void setStandardPower(int standardPower)
    {
        this.standardPower = standardPower;
    }
}
