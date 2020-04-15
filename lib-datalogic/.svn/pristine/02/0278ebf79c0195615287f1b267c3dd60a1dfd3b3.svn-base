package com.goldwind.datalogic.business.model;

/**
 * 用户消息订阅推送类型
 * 
 * @author 冯春源
 *
 */
public class NoticeTypeInfo
{
    /**
     * 用户id
     */
    private String userId;
    /**
     * 是否通过app
     */
    private boolean byApp;
    /**
     * 是否通过email
     */
    private boolean byMail;
    /**
     * 是否通过短信
     */
    private boolean bySms;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean isByApp()
    {
        return byApp;
    }

    public void setByApp(boolean byApp)
    {
        this.byApp = byApp;
    }

    public boolean isByMail()
    {
        return byMail;
    }

    public void setByMail(boolean byMail)
    {
        this.byMail = byMail;
    }

    public boolean isBySms()
    {
        return bySms;
    }

    public void setBySms(boolean bySms)
    {
        this.bySms = bySms;
    }

    public NoticeTypeInfo(String userId, String byApp, String byMail, String bySms)
    {
        this.userId = userId;
        this.byApp = ("1".equals(byApp)) ? true : false;
        this.byMail = ("1".equals(byMail)) ? true : false;
        this.bySms = ("1".equals(bySms)) ? true : false;
    }

}
