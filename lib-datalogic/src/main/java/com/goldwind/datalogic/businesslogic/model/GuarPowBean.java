package com.goldwind.datalogic.businesslogic.model;

import java.util.ArrayList;
import java.util.List;

public class GuarPowBean
{
  private int guarpowid;
  
  private List<GuarPowDetailBean>detailBeans;
  
    public GuarPowBean()
    {        
        detailBeans=new ArrayList<>();
    }

    /**
     * @return the guarpowid
     */
    public int getGuarpowid()
    {
        return guarpowid;
    }

    /**
     * @return the detailBeans
     */
    public List<GuarPowDetailBean> getDetailBeans()
    {
        return detailBeans;
    }      
    
}
