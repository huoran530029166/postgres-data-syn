
import java.util.ArrayList;
import java.util.List;

/**
 * 损失电量对象
 * 
 * @author 谭璟
 *
 */
public class EnergyUseElecData
{
    /**
     * 损失电量类型
     */
    private int setid;

    /**
     * 理论损失电量
     */
    private double theoryelec;

    /**
     * 实际损失电量
     */
    private double realelec;

    /**
     * 时长
     */
    private int timelength;

    /**
     * 理论损失电量（普通机组计算）
     */
    private double losseletotal;

    /**
     * 按照标杆机组计算损失电量
     */
    private double bmarklosseletotal;

    private String dbname;

    private String wfid;

    private String wtid;

    private String rectime;

    /**
     * 0为一分钟 1为日数据
     */
    private int flag;

    public EnergyUseElecData(String dbname, String wfid, String wtid, int setid, String rectime, double theoryelec, double realelec, int timelength, double losseletotal)
    {
        this.dbname = dbname;
        this.wfid = wfid;
        this.wtid = wtid;
        this.rectime = rectime;
        this.setid = setid;
        this.theoryelec = theoryelec;
        this.realelec = realelec;
        this.timelength = timelength;
        this.losseletotal = losseletotal;
    }

    public int getSetid()
    {
        return setid;
    }

    public void setSetid(int setid)
    {
        this.setid = setid;
    }

    public double getTheoryelec()
    {
        return theoryelec;
    }

    public void setTheoryelec(double theoryelec)
    {
        this.theoryelec = theoryelec;
    }

    public double getRealelec()
    {
        return realelec;
    }

    public void setRealelec(double realelec)
    {
        this.realelec = realelec;
    }

    public int getTimelength()
    {
        return timelength;
    }

    public void setTimelength(int timelength)
    {
        this.timelength = timelength;
    }

    public double getLosseletotal()
    {
        return losseletotal;
    }

    public void setLosseletotal(double losseletotal)
    {
        this.losseletotal = losseletotal;
    }

    public double getBmarklosseletotal()
    {
        return bmarklosseletotal;
    }

    public void setBmarklosseletotal(double bmarklosseletotal)
    {
        this.bmarklosseletotal = bmarklosseletotal;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public List<String> updataall()
    {
        List<String> list = new ArrayList<>();
        if (this.setid != 0)
        {
            String sql = "delete  from " + this.getDbname() + " where wfid = " + this.getWfid() + " and wtid = " + this.getWtid();
            sql += " and rectime = '" + this.getRectime() + "' and setid = '" + this.setid + "'";
            list.add(sql);

            sql = "insert into " + this.getDbname() + " values( " + this.getWfid() + "," + this.getWtid() + "," + this.setid;
            sql += "," + "cast('" + this.getRectime() + "' as timestamp without time zone" + ")" + "," + this.theoryelec;
            sql += "," + this.realelec + "," + this.timelength + "," + this.losseletotal + "," + this.bmarklosseletotal + ")";
            list.add(sql);
        }
        return list;
    }

    public String updata()
    {
        String sql = "";
        sql = "insert into " + this.getDbname() + " values( " + this.getWfid() + "," + this.getWtid() + "," + this.setid;
        sql += "," + "cast('" + this.getRectime() + "' as timestamp without time zone" + ")" + "," + this.theoryelec;
        sql += "," + this.realelec + "," + this.timelength + "," + this.losseletotal + "," + this.bmarklosseletotal + ")";

        return sql;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + setid;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        { 
            return false;
        }
        if (getClass() != obj.getClass())
        { 
            return false;
        }
        EnergyUseElecData other = (EnergyUseElecData) obj;
        if (setid != other.setid)
        { 
            return false;
        }
        return true;
    }

    public String getDbname()
    {
        return dbname;
    }

    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }

    public String getWfid()
    {
        return wfid;
    }

    public void setWfid(String wfid)
    {
        this.wfid = wfid;
    }

    public String getWtid()
    {
        return wtid;
    }

    public void setWtid(String wtid)
    {
        this.wtid = wtid;
    }

    public String getRectime()
    {
        return rectime;
    }

    public void setRectime(String rectime)
    {
        this.rectime = rectime;
    }
}
