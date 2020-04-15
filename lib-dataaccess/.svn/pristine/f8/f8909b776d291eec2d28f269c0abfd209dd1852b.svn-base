/**   
 * Copyright © 2017 金风科技. All rights reserved.
 * 
 * @Title: SerializableUtil.java 
 * @Prject: version-mgt-core
 * @Package: com.goldwind.vmgt.redis.utils 
 * @Description: kryo序列化工具类(高性能序列化)
 * @author: czr   
 * @date: 2017年9月14日 下午1:51:56 
 * @version: V1.0   
 */
package com.goldwind.dataaccess.serial.kryo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.goldwind.dataaccess.Log;

/**
 * @ClassName: SerializableUtil
 * @Description: kryo序列化工具类(高性能序列化)
 * @author: czr
 * @date: 2017年9月14日 下午1:51:56
 */
public class KryoSerializableUtil
{

    /**
     * logger对象
     */
    private static Log LOGGER = Log.getLog(KryoSerializableUtil.class);

    /**
     * 缓存大小
     */
    private static int BUFFER_SIZE = 1024 * 8;

    /**
     * @Title: serialization
     * @Description: 序列化普通对象
     * @param obj
     * @return
     * @return: byte[]
     */
    public static byte[] serialization(Object obj)
    {
        Kryo kryo = KryoHolder.get();

        Output output = null;
        ByteArrayOutputStream bos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            output = new Output(bos, BUFFER_SIZE);
            kryo.writeObject(output, obj);
            output.flush();
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            LOGGER.error("KryoSerializableUtil_serialization_"+e.getClass().getName()+"_"+e.getLocalizedMessage(),e);
        }
        finally
        {
            if (null != output)
            {
                output.close();
            }
            if (null != bos)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("KryoSerializableUtil_serialization1_"+e.getClass().getName()+"_"+e.getLocalizedMessage(),e);
                }
            }
        }
        return null;
    }

    /**
     * @Title: deserialization
     * @Description: 反序列化普通对象
     * @param b
     * @param t
     * @return
     * @return: T
     */
    public static <T> T deserialization(byte[] b, Class<T> t)
    {

        Kryo kryo = KryoHolder.get();

        Input input = null;
        ByteArrayInputStream bis = null;
        try
        {
            bis = new ByteArrayInputStream(b);
            input = new Input(bis, BUFFER_SIZE);
            T object = kryo.readObject(input, t);
            return object;
        }
        catch (Exception e)
        {
            LOGGER.error("KryoSerializableUtil_deserialization_"+e.getClass().getName()+"_"+e.getLocalizedMessage(),e);
        }
        finally
        {
            if (null != input)
            {
                input.close();
            }
            if (null != bis)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    LOGGER.error("KryoSerializableUtil_deserialization1_"+e.getClass().getName()+"_"+e.getLocalizedMessage(),e);
                }
            }
        }
        return null;
    }

    /**
     * @Title: serializationList
     * @Description: 序列化List对象
     * @param obj
     * @param clazz
     * @return
     * @return: byte[]
     */
    public static <T extends Serializable> byte[] serializationList(List<T> obj, Class<T> clazz)
    {

        Kryo kryo = KryoHolder.get();

        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);

        kryo.register(clazz, new JavaSerializer());
        kryo.register(ArrayList.class, serializer);

        Output output = null;
        ByteArrayOutputStream bos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            output = new Output(bos, BUFFER_SIZE);
            kryo.writeObject(output, obj);
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            if (null != output)
            {
                output.close();
            }
            if (null != bos)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    LOGGER.error(e);
                }
            }
        }
        return null;
    }

    /**
     * @Title: deserializationList
     * @Description: 反序列化List对象
     * @param b
     * @param clazz
     * @return
     * @return: List<T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> deserializationList(byte[] b, Class<T> clazz)
    {

        Kryo kryo = KryoHolder.get();

        CollectionSerializer serializer = new CollectionSerializer();
        serializer.setElementClass(clazz, new JavaSerializer());
        serializer.setElementsCanBeNull(false);

        kryo.register(clazz, new JavaSerializer());
        kryo.register(ArrayList.class, serializer);

        Input input = null;
        ByteArrayInputStream bis = null;
        try
        {
            bis = new ByteArrayInputStream(b);
            input = new Input(bis, BUFFER_SIZE);
            return (List<T>) kryo.readObject(input, ArrayList.class, serializer);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            if (null != input)
            {
                input.close();
            }
            if (null != bis)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    LOGGER.error(e);
                }
            }
        }
        return null;
    }

    /**
     * @Title: serializationMap
     * @Description: 序列化Map对象
     * @param obj
     * @param kclazz
     * @param vclazz
     * @return
     * @return: byte[]
     */
    public static <K, V extends Serializable> byte[] serializationMap(Map<K, V> obj, Class<K> kclazz, Class<V> vclazz)
    {

        Kryo kryo = KryoHolder.get();

        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(kclazz, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(vclazz, new JavaSerializer());
        serializer.setValuesCanBeNull(true);

        kryo.register(kclazz, new JavaSerializer());
        kryo.register(vclazz, new JavaSerializer());
        kryo.register(HashMap.class, serializer);

        Output output = null;
        ByteArrayOutputStream bos = null;
        try
        {
            bos = new ByteArrayOutputStream();
            output = new Output(bos, BUFFER_SIZE);
            kryo.writeObject(output, obj);
            return bos.toByteArray();
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            if (null != output)
            {
                output.close();
            }
            if (null != bos)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    LOGGER.error(e);
                }
            }
        }
        return null;
    }

    /**
     * @Title: deserializationMap
     * @Description: 反序列化Map对象
     * @param b
     * @param kclazz
     * @param vclazz
     * @return
     * @return: Map<K,V>
     */
    @SuppressWarnings("unchecked")
    public static <K, V extends Serializable> Map<K, V> deserializationMap(byte[] b, Class<K> kclazz, Class<V> vclazz)
    {

        Kryo kryo = KryoHolder.get();

        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(kclazz, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(vclazz, new JavaSerializer());
        serializer.setValuesCanBeNull(true);

        kryo.register(kclazz, new JavaSerializer());
        kryo.register(vclazz, new JavaSerializer());
        kryo.register(HashMap.class, serializer);

        Input input = null;
        ByteArrayInputStream bis = null;
        try
        {
            bis = new ByteArrayInputStream(b);
            input = new Input(bis, BUFFER_SIZE);
            return (Map<K, V>) kryo.readObject(input, HashMap.class, serializer);
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        finally
        {
            if (null != input)
            {
                input.close();
            }
            if (null != bis)
            {
                try
                {
                    bis.close();
                }
                catch (IOException e)
                {
                    LOGGER.error(e);
                }
            }
        }
        return null;
    }

    /**
     * 
     * @Title: writeToByteArray
     * @Description: 将对象序列化为字节数组（不限制大小）
     * @param obj
     *            对象
     * @return byte数组
     * @return: byte[] 数组
     */
    public static <T> byte[] writeToByteArray(T obj)
    {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        Kryo kryo = KryoHolder.get();
        kryo.writeClassAndObject(output, obj);
        output.flush();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 
     * @Title: readFromByteArray 从byte数组中反序列化为对象
     * @Description: readFromByteArray 从byte数组中反序列化为对象
     * @param byteArray
     *            byte数组
     * @return 对象
     * @return: T 返回对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T readFromByteArray(byte[] byteArray)
    {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);
        Kryo kryo = KryoHolder.get();
        T t = (T) kryo.readClassAndObject(input);
        input.close();
        return t;

    }

    public static void main(String[] args)
    {

        Map<String, CustomItemDto> map = new HashMap<String, CustomItemDto>();
        for (int i = 0; i < 10; i++)
        {
            CustomItemDto val = new CustomItemDto();
            val.setId(Long.parseLong(String.valueOf(i)));
            val.setItemCode("");
            val.setItemDespositPrice(32.45);
            val.setItemMemo(null);
            val.setItemName("张金" + i);
            val.setItemPrice(89.02);
            val.setSort(10);
            map.put(i + "k", val);
        }

        byte[] a = serializationMap(map, String.class, CustomItemDto.class);
        // System.out.println(a.toString());
        Map<String, CustomItemDto> newValue = deserializationMap(a, String.class, CustomItemDto.class);
        // for (int i = 0; i < 10; i++)
        // {
        // System.out.println(newValue.get(i + "k").getItemName());
        // }

        // byte[] d=serialization("h");
        // System.out.println(d.toString());
    }

}

class CustomItemDto implements Serializable
{

    /**
     * @fieldName: serialVersionUID
     * @fieldType: long
     * @Description: TODO
     */
    private static final long serialVersionUID = 5408224874161122204L;

    private Long id;

    private String itemCode;

    private String itemName;

    private String itemMemo;

    private double itemPrice;

    private double itemDespositPrice;

    private Integer sort;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getItemCode()
    {
        return itemCode;
    }

    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemMemo()
    {
        return itemMemo;
    }

    public void setItemMemo(String itemMemo)
    {
        this.itemMemo = itemMemo;
    }

    public double getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public double getItemDespositPrice()
    {
        return itemDespositPrice;
    }

    public void setItemDespositPrice(double itemDespositPrice)
    {
        this.itemDespositPrice = itemDespositPrice;
    }

    public Integer getSort()
    {
        return sort;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

}
