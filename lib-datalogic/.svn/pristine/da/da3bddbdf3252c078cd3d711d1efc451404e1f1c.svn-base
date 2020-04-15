package com.goldwind.datalogic.business.model;

import java.util.Date;

/**
 * 五防数据表
 *
 * @author donglb
 *
 */
public class FiveProtectionRule
{
    private Integer ruleid;

    private Integer protocolid;

    private String iecpath;

    private Date altertime;

    private String userid;

    private Byte optionindex;

    private String rulename;

    private String rulecontent;

    public FiveProtectionRule()
    {

    }

    public FiveProtectionRule(Integer ruleid, Integer protocolid, String iecpath, Date altertime, String userid, Byte optionindex, String rulename, String rulecontent)
    {
        super();
        this.ruleid = ruleid;
        this.protocolid = protocolid;
        this.iecpath = iecpath;
        this.altertime = altertime;
        this.userid = userid;
        this.optionindex = optionindex;
        this.rulename = rulename;
        this.rulecontent = rulecontent;
    }

    public Integer getRuleid()
    {
        return ruleid;
    }

    public void setRuleid(Integer ruleid)
    {
        this.ruleid = ruleid;
    }

    public Integer getProtocolid()
    {
        return protocolid;
    }

    public void setProtocolid(Integer protocolid)
    {
        this.protocolid = protocolid;
    }

    public String getIecpath()
    {
        return iecpath;
    }

    public void setIecpath(String iecpath)
    {
        this.iecpath = iecpath == null ? null : iecpath.trim();
    }

    public Date getAltertime()
    {
        return altertime;
    }

    public void setAltertime(Date altertime)
    {
        this.altertime = altertime;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid == null ? null : userid.trim();
    }

    public Byte getOptionindex()
    {
        return optionindex;
    }

    public void setOptionindex(Byte optionindex)
    {
        this.optionindex = optionindex;
    }

    public String getRulename()
    {
        return rulename;
    }

    public void setRulename(String rulename)
    {
        this.rulename = rulename == null ? null : rulename.trim();
    }

    public String getRulecontent()
    {
        return rulecontent;
    }

    public void setRulecontent(String rulecontent)
    {
        this.rulecontent = rulecontent == null ? null : rulecontent.trim();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        FiveProtectionRule that = (FiveProtectionRule) o;

        if (!protocolid.equals(that.protocolid))
        {
            return false;
        }
        if (!iecpath.equals(that.iecpath))
        {
            return false;
        }
        if (!optionindex.equals(that.optionindex))
        {
            return false;
        }
        return rulename.equals(that.rulename);

    }

    @Override
    public int hashCode()
    {
        int result = protocolid.hashCode();
        result = 31 * result + iecpath.hashCode();
        result = 31 * result + optionindex.hashCode();
        result = 31 * result + rulename.hashCode();
        return result;
    }
}
