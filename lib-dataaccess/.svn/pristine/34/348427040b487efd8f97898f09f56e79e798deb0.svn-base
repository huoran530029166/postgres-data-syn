/**   
 * Copyright © 2017 金风科技. All rights reserved.
 * 
 * @Title: KryoHolder.java 
 * @Prject: version-mgt-core
 * @Package: com.goldwind.vmgt.core.redis.utils 
 * @Description: kryo对象是线程不安全的,防止并发,采用ThreadLocal,使每个线程维护一个kryo对象
 * @author: czr   
 * @date: 2017年9月15日 下午1:25:05 
 * @version: V1.0   
 */
package com.goldwind.dataaccess.serial.kryo;

import com.esotericsoftware.kryo.Kryo;

/**
 * @ClassName: KryoHolder
 * @Description: kryo对象是线程不安全的,防止并发,采用ThreadLocal,使每个线程维护一个kryo对象
 * @author: czr
 * @date: 2017年9月15日 下午1:25:05
 */
public class KryoHolder
{

    private static ThreadLocal<Kryo> threadLocalKryo = new ThreadLocal<Kryo>()
    {
        protected Kryo initialValue()
        {
            Kryo kryo = new KryoReflectionFactory();
            return kryo;
        };
    };

    public static Kryo get()
    {
        return threadLocalKryo.get();
    }
}
