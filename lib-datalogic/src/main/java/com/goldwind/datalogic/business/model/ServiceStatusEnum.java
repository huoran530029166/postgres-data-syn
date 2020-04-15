package com.goldwind.datalogic.business.model;

public enum ServiceStatusEnum
{
    // ReadConfig(5,"ReadConfig","读取配置文件"),
    LoadMemory(10, "内存同步模块", "LoadMemory"), DataComm(20, "通讯模块", "DataComm"), DataMaintain(30, "数据维护模块", "DataMaintain"), BF(40, "BF文件模块", "BF module"), RealDataSave(70, "实时库存储模块",
            "real data save"), Success(100, "所有服务", "All Service"), Error(999, "启动失败", "Start Failed");

    /**
     * 状态码
     */
    private int code;

    /**
     * 中文模块名称
     */
    private String descrCn;

    /**
     * 英文模块名称
     */
    private String descrEn;

    /**
     * 状态 0 未启动 1启动成功
     */
    private int status;

    /**
     * @return 状态码
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return the descrCn
     */
    public String getDescrCn()
    {
        return descrCn;
    }

    /**
     * @return 英文模块名称
     */
    public String getDescrEn()
    {
        return descrEn;
    }

    private ServiceStatusEnum(int id, String cn, String en)
    {
        code = id;
        descrCn = cn;
        descrEn = en;
    }

    /**
     * @return 启动状态
     */
    public int getStatus()
    {
        return status;
    }

    public void serviceStatusEnum(int status)
    {
        this.status = status;
    }
    // public void setStatus(int status)
    // {
    // this.status = status;
    // }

}
