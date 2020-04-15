package com.goldwind.datalogic.business.model;

/**
 * 气象服务配置
 * 
 * @author 张超
 *
 */
public class WeatherConfig
{
    public WeatherConfig(String wfid, String longitude, String latitude)
    {
        this.wfid = wfid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * 风场ID
     */
    private String wfid = "";
    /**
     * 经度
     */
    private String longitude = "";
    /**
     * 纬度
     */
    private String latitude = "";

    public String getWfid()
    {
        return wfid;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }
}
