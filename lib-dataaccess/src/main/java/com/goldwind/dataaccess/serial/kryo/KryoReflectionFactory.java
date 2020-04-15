/**   
 * Copyright © 2017 金风科技. All rights reserved.
 * 
 * @Title: KryoReflectionFactory.java 
 * @Prject: version-mgt-core
 * @Package: com.goldwind.vmgt.core.redis.utils 
 * @Description: kryo配置类
 * @author: czr   
 * @date: 2017年9月15日 下午1:10:58 
 * @version: V1.0   
 */
package com.goldwind.dataaccess.serial.kryo;

//import java.lang.reflect.InvocationHandler;
import java.net.URI;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

//import org.springframework.cglib.proxy.InvocationHandler;

import com.esotericsoftware.kryo.Serializer;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.BitSetSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyListSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptyMapSerializer;
import de.javakaffee.kryoserializers.CollectionsEmptySetSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonListSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonMapSerializer;
import de.javakaffee.kryoserializers.CollectionsSingletonSetSerializer;
import de.javakaffee.kryoserializers.CopyForIterateCollectionSerializer;
import de.javakaffee.kryoserializers.CopyForIterateMapSerializer;
import de.javakaffee.kryoserializers.DateSerializer;
import de.javakaffee.kryoserializers.EnumMapSerializer;
import de.javakaffee.kryoserializers.EnumSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import de.javakaffee.kryoserializers.RegexSerializer;
import de.javakaffee.kryoserializers.SubListSerializers;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.URISerializer;
import de.javakaffee.kryoserializers.UUIDSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

/**
 * @ClassName: KryoReflectionFactory
 * @Description: kryo配置类
 * @author: czr
 * @date: 2017年9月15日 下午1:10:58
 */
public class KryoReflectionFactory extends KryoReflectionFactorySupport
{

    /**
     * @Title:KryoReflectionFactory
     * @Description:构造方法
     */
    public KryoReflectionFactory()
    {

        // 设置是否只能序列化已注册的类
        setRegistrationRequired(false);

        setReferences(true);

        // 注册各种常用类型对象
        register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
        register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
        register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
        register(Pattern.class, new RegexSerializer());
        register(BitSet.class, new BitSetSerializer());
        register(URI.class, new URISerializer());
        register(UUID.class, new UUIDSerializer());
        register(GregorianCalendar.class, new GregorianCalendarSerializer());
        // register(InvocationHandler.class, new JdkProxySerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(this);
        SynchronizedCollectionsSerializer.registerSerializers(this);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Serializer<?> getDefaultSerializer(Class clazz)
    {
        if (EnumSet.class.isAssignableFrom(clazz))
        {
            return new EnumSetSerializer();
        }

        if (EnumMap.class.isAssignableFrom(clazz))
        {
            return new EnumMapSerializer();
        }

        if (Collection.class.isAssignableFrom(clazz))
        {
            return new CopyForIterateCollectionSerializer();
        }

        if (Map.class.isAssignableFrom(clazz))
        {
            return new CopyForIterateMapSerializer();
        }

        if (Date.class.isAssignableFrom(clazz))
        {
            return new DateSerializer(clazz);
        }

        if (SubListSerializers.ArrayListSubListSerializer.canSerialize(clazz) || SubListSerializers.JavaUtilSubListSerializer.canSerialize(clazz))
        {
            return SubListSerializers.createFor(clazz);
        }

        return super.getDefaultSerializer(clazz);
    }

}
