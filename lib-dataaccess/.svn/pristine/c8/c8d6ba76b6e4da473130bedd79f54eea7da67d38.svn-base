package com.goldwind.dataaccess;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 序列化操作类
 * 
 * @author 曹阳
 *
 */
public class SerialOper
{
    /**
     * 从磁盘读取对象缓存
     * 
     * @param filePath
     *            缓存路径
     * @return 对象
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @throws EOFException
     *             EOF异常
     */
    public static Object readObject(String filePath) throws DataAsException, IOException, EOFException
    {
        FileInputStream fls = null;
        ObjectInputStream ois = null;
        Object obj = null;
        File file = new File(filePath);
        if (file.exists())
        {
            try
            {
                fls = new FileInputStream(filePath);
                ois = new ObjectInputStream(fls);
                obj = ois.readObject();
            }
            catch (EOFException e)
            {
                throw e;
            }
            catch (Exception exp)
            {
                DataAsExpSet.throwExpByResCode("SerialOper_readObject_1", new String[] { "filePath" }, new Object[] { filePath }, exp);
            }
            finally
            {
                if (ois != null)
                {
                    ois.close();
                }
                if (fls != null)
                {
                    fls.close();
                }
            }
        }
        return obj;
    }

    /**
     * 将对象缓存到磁盘
     * 
     * @param obj
     *            对象
     * @param filePath
     *            缓存路径
     * @throws Exception
     *             异常
     */
    public static void writeObject(Object obj, String filePath) throws Exception
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            File file = new File(filePath);
            File fP = file.getParentFile();
            if (!fP.exists())
            {
                fP.mkdirs();
            }

            fos = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("SerialOper_writeObject_1", new String[] { "obj", "filePath" }, new Object[] { obj.toString(), filePath }, exp);
        }
        finally
        {
            if (oos != null)
            {
                oos.close();
            }
            if (fos != null)
            {
                fos.close();
            }
        }
    }

    /**
     * kryo读取对象
     * 
     * @param <T>
     *            对象类型
     * @param filePath
     *            对象文件路径
     * @param clz
     *            对象classz
     * @return 对象
     * @throws DataAsException
     *             自定义异常
     */
    public static <T> Object kryoReadObj(String filePath, Class<T> clz) throws DataAsException
    {
        Kryo kryo = new Kryo();
        Object obj = null;
        Input input = null;
        File file = new File(filePath);
        if (file.exists())
        {
            try
            {
                input = new Input(new FileInputStream(filePath));
                obj = kryo.readObject(input, clz);
            }
            catch (Exception exp)
            {
                DataAsExpSet.throwExpByResCode("SerialOper_readObject_1", new String[] { "filePath" }, new Object[] { filePath }, exp);
            }
            finally
            {
                if (input != null)
                {
                    input.close();
                }
            }
        }
        return obj;
    }

    /**
     * kryo写对象
     * 
     * @param obj
     *            对象
     * @param filePath
     *            对象存储路径
     * @throws DataAsException
     *             自定义异常
     */
    public static void kryoWriteObj(Object obj, String filePath) throws DataAsException
    {
        Kryo kryo = new Kryo();
        Output op = null;
        File file = new File(filePath);
        File fP = file.getParentFile();
        if (!fP.exists())
        {
            fP.mkdirs();
        }
        try
        {
            op = new Output(new FileOutputStream(filePath));
            kryo.writeObject(op, obj);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("SerialOper_writeObject_1", new String[] { "obj", "filePath" }, new Object[] { obj.toString(), filePath }, exp);
        }
        finally
        {
            if (op != null)
            {
                op.close();
            }

        }
    }
}
