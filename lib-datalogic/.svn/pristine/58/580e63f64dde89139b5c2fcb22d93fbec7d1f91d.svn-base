package com.goldwind.datalogic.socket.model;

import java.util.Observable;

/*
 * NettyHandler被观察者类
 * */
public class NettyHandlerObservable extends Observable
{
    public void changed(String data)
    {
        this.setChanged();
        this.notifyObservers(data);
    }
}
