package com.goldwind.dataaccess.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 文件压缩操作类
 * 
 * @author 曹阳
 *
 */
public class FileCompress// NOSONAR
{
    /**
     * 缓冲区大小
     */
    private static int defBlockSize = 4096;
    /**
     * 压缩等级
     */
    private static int defCompressLevel = 6;

    /**
     * srcFileName
     */
    static String srcFileName = "srcFileName";

    /**
     * 压缩文件
     * 
     * @param srcFileName
     *            压缩文件路径名
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     */
    public static void zipFile(String srcFileName) throws IOException, DataAsException
    {
        int index = srcFileName.lastIndexOf('.');
        String destFileName = "";
        if (index >= 0)
        {
            destFileName = srcFileName.substring(0, index + 1) + "zip";
        }
        else
        {
            destFileName = srcFileName + ".zip";
        }
        zipFile(srcFileName, destFileName);
    }

    /**
     * 压缩文件
     * 
     * @param srcFileName
     *            源文件路径名
     * @param destFileName
     *            目标文件路径名
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     */
    public static void zipFile(String srcFileName, String destFileName) throws IOException, DataAsException
    {
        zipFile(srcFileName, destFileName, defCompressLevel, defBlockSize);
    }

    /**
     * 压缩文件
     * 
     * @param srcFileName
     *            源文件名，包括路径
     * @param destFileName
     *            目标文件名，包括路径
     * @param compressLevel
     *            压缩等级
     * @param blockSize
     *            缓冲区大小
     * @throws IOException
     *             i/o异常
     * @throws DataAsException
     *             自定义异常
     */
    private static void zipFile(String srcFileName, String destFileName, int compressLevel, int blockSize) throws IOException, DataAsException
    {

        String tempFileName = destFileName + DataAsDef.TEMPFILEEXTNAME;
        try (FileOutputStream fos = new FileOutputStream(tempFileName); ZipOutputStream zos = new ZipOutputStream(fos); FileInputStream fis = new FileInputStream(srcFileName);)
        {
            // 如果文件夹不存在，创建文件夹
            File f = new File(destFileName);
            File fParent = f.getParentFile();
            if (!fParent.exists())
            {
                fParent.mkdirs();
            }
            byte[] buf = new byte[blockSize];
            int len;
            // 将目标文件写入临时文件
            File srcFile = new File(srcFileName);
            ZipEntry ze = new ZipEntry(srcFile.getName());
            zos.putNextEntry(ze);
            while ((len = fis.read(buf)) != -1)
            {
                zos.write(buf, 0, len);
                zos.flush();
            }
        }
        catch (Exception exp)
        {
            // 删除临时文件
            FileAssistant.deleteFile(tempFileName);
            DataAsExpSet.throwExpByResCode("FileCompress_zipFile_1", new String[] { srcFileName, "destFileName", "compressLevel", "blockSize" },
                    new Object[] { srcFileName, destFileName, compressLevel, blockSize }, exp);
        }
        finally
        {
            // 将临时文件写入目标文件
            FileAssistant.moveFile(tempFileName, destFileName);
        }
    }

    /**
     * 解压zip文件到当前目录
     * 
     * @param srcFileName
     *            源文件
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 目标文件名
     */
    public static String unzipFile(String srcFileName) throws DataAsException, IOException
    {
        srcFileName = srcFileName.replace("\\", "/");
        int index = srcFileName.lastIndexOf('/');
        if (index < 0)
        {
            DataAsExpSet.throwExpByResCode("FileCompress_unzipFile_1", new String[] { srcFileName }, new Object[] { srcFileName }, null);
        }
        String destDir = srcFileName.substring(0, index);
        return unzipFile(srcFileName, destDir);
    }

    /**
     * 解压zip文件到指定目录
     * 
     * @param srcFileName
     *            源文件
     * @param destDir
     *            目标路径
     * @throws IOException
     *             i/o异常
     * @throws DataAsException
     *             自定义异常
     * @return 目标文件名
     */
    public static String unzipFile(String srcFileName, String destDir) throws DataAsException, IOException
    {
        return unzipFile(srcFileName, destDir, "");
    }

    /**
     * 解压zip文件
     * 
     * @param srcFileName
     *            源文件名
     * @param destDir
     *            目标路径
     * @param destName
     *            目标文件名
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 目标文件名
     */
    public static String unzipFile(String srcFileName, String destDir, String destName) throws DataAsException, IOException
    {
        String destFileName = "";
        String firstPath = "";
        String tempFileName = "";
        try (ZipFile zipFile = new ZipFile(srcFileName);)
        {

            File fDestDir = new File(destDir);
            if (!fDestDir.exists())
            {
                fDestDir.mkdirs();
            }
            // zipFile = new ZipFile(srcFileName);
            String directoryPath = "";
            if (destDir == null || destDir.isEmpty())
            {
                directoryPath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
            }
            else
            {
                directoryPath = destDir;
            }

            Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
            boolean first = true;
            while (entryEnum.hasMoreElements())
            {
                ZipEntry zipEntry = null;
                zipEntry = entryEnum.nextElement();
                if (first)
                {
                    firstPath = destDir + "/" + zipEntry.getName();
                    first = false;
                }
                if (zipEntry.isDirectory())
                {
                    buildFile(directoryPath + "/" + zipEntry.getName(), true);
                }
                else
                {
                    if (destName != null && !destName.isEmpty())
                    {
                        destFileName = directoryPath + "/" + destName;
                    }
                    else
                    {
                        destFileName = directoryPath + "/" + zipEntry.getName();
                    }
                    tempFileName = destFileName + DataAsDef.TEMPFILEEXTNAME;
                    File targetFile = buildFile(tempFileName, false);
                    try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile)); InputStream is = zipFile.getInputStream(zipEntry);)
                    {
                        // is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[defBlockSize];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, defBlockSize)) >= 0)
                        {
                            os.write(buffer, 0, readLen);
                            os.flush();
                        }
                    }
                    File file = new File(destFileName);
                    if (file.exists())
                    {
                        file.delete();
                    }
                    FileAssistant.moveFile(tempFileName, destFileName);
                }
            }
        }
        catch (Exception exp)
        {
            FileAssistant.deleteFile(tempFileName);
            DataAsExpSet.throwExpByResCode("FileCompress_unzipFile_2", new String[] { srcFileName, "destDir" }, new Object[] { srcFileName, destDir }, exp);
        }
        return firstPath;
    }

    /**
     * 解压文件流
     * 
     * @param datas
     *            压缩的字节数组
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 解压缩的字节数组
     */
    public static byte[] unZipStream(byte[] datas) throws DataAsException, IOException
    {
        byte[] val = null;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(datas);)
        {
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[1024];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1)
            {
                baos.write(buf, 0, num);
                baos.flush();
            }
            val = baos.toByteArray();
            baos.close();
            gzip.close();
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileCompress_unZipStream_1", null, null, exp);
        }
        return val;
    }

    /**
     * 创建文件
     * 
     * @param fileName
     *            文件名
     * @param isDirectory
     *            是否是路径
     * @return 文件对象
     */
    public static File buildFile(String fileName, boolean isDirectory)
    {
        File target = new File(fileName);
        if (isDirectory)
        {
            target.mkdirs();
        }
        else
        {
            if (!target.getParentFile().exists())
            {
                target.getParentFile().mkdirs();
                target = new File(target.getAbsolutePath());
            }
        }
        return target;
    }

    /**
     * 压缩数据
     * 
     * @param data
     *            字符串
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 压缩后的字节数组
     */
    public static byte[] zipData(String data) throws DataAsException, IOException
    {
        byte[] outData = null;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();)
        {
            byte[] dataBytes = data.getBytes(DataAsDef.getCNCHARSET());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(dataBytes);
            gzip.finish();
            gzip.close();
            outData = bos.toByteArray();
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileCompress_zipData_1", new String[] { "data" }, new Object[] { data }, exp);
        }
        return outData;
    }

    /**
     * 压缩byte数据
     * 
     * @param dataBytes
     *            字符流（必须按照utf8进行转换）
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 压缩后的字节数组
     */
    public static byte[] zipDataStream(byte[] dataBytes) throws DataAsException, IOException
    {
        byte[] outData = null;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();)
        {
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(dataBytes);
            gzip.finish();
            gzip.close();
            outData = bos.toByteArray();
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileCompress_zipDataStream_1", new String[] { "dataBytes" }, new Object[] { dataBytes }, exp);
        }
        return outData;
    }

    /**
     * 解压缩byte数组
     * 
     * @param dataBytes
     *            压缩后的字节数组
     * @throws IOException
     *             i/o异常
     * @throws DataAsException
     *             自定义异常
     * @return 解压后的字符串
     */
    public static String unZipData(byte[] dataBytes) throws IOException, DataAsException
    {
        String outData = null;

        try (ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);)
        {
            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] buf = new byte[defBlockSize];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = gzip.read(buf, 0, buf.length)) != -1)
            {
                baos.write(buf, 0, num);
                baos.flush();
            }
            outData = baos.toString(DataAsDef.getCNCHARSET());
            baos.close();
            gzip.close();

        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileCompress_unZipData_1", null, null, exp);
        }
        return outData;

    }

    /**
     * 解压goldwind文件
     * 
     * @param srcFileName
     *            原文件名
     * @param destDir
     *            需要保存的文件夹
     * @param destName
     *            需要保存的文件名称
     * @return 解压后的文件路径
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             io异常
     */
    public static String unzipGoldwindFile(String srcFileName, String destDir, String destName) throws DataAsException, IOException
    {
        String destFileName = "";
        String tempFileName = "";
        try (ZipFile zipFile = new ZipFile(srcFileName);)
        {

            File fDestDir = new File(destDir);
            if (!fDestDir.exists())
            {
                fDestDir.mkdirs();
            }

            String directoryPath = "";
            if (destDir == null || destDir.isEmpty())
            {
                directoryPath = srcFileName.substring(0, srcFileName.lastIndexOf("."));
            }
            else
            {
                directoryPath = destDir;
            }

            Enumeration<? extends ZipEntry> entryEnum = zipFile.entries();
            if (entryEnum != null)
            {
                ZipEntry zipEntry = null;
                zipEntry = entryEnum.nextElement();
                if (zipEntry.isDirectory())
                {
                    directoryPath = directoryPath + "/" + zipEntry.getName();
                }

                if (zipEntry.getSize() > 0)
                {

                    if (destName != null && !destName.isEmpty())
                    {
                        destFileName = directoryPath + "/" + destName;
                    }
                    else
                    {
                        destFileName = directoryPath + "/" + zipEntry.getName().substring(zipEntry.getName().length() - 19);
                    }
                    tempFileName = destFileName + DataAsDef.TEMPFILEEXTNAME;
                    File targetFile = buildFile(tempFileName, false);
                    try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile)); InputStream is = zipFile.getInputStream(zipEntry);)
                    {
                        byte[] buffer = new byte[defBlockSize];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, defBlockSize)) >= 0)
                        {
                            os.write(buffer, 0, readLen);
                            os.flush();
                        }
                    }
                }
                else
                {
                    buildFile(directoryPath + "/" + zipEntry.getName(), true);
                }
            }
        }
        catch (Exception exp)
        {
            FileAssistant.deleteFile(tempFileName);
            DataAsExpSet.throwExpByResCode("FileCompress_unzipGoldwindFile_1", new String[] { srcFileName, "destDir" }, new Object[] { srcFileName, destDir }, exp);
        }
        finally
        {
            File file = new File(destFileName);
            if (file.exists())
            {
                file.delete();
            }
            FileAssistant.moveFile(tempFileName, destFileName);
        }
        return destFileName;
    }

    /**
     * 校验zip文件
     * 
     * @param inputStream
     *            源文件流
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             i/o异常
     * @return 目标文件名
     */
    public static boolean checkZipFile(ZipInputStream inputStream) throws DataAsException, IOException
    {
        boolean hasfile1 = false;
        boolean hasfile2 = false;
        ZipEntry zipEntry = null;
        while ((zipEntry = inputStream.getNextEntry()) != null)
        {
            String[] dirs = zipEntry.getName().split("/");
            if (dirs.length > 1)
            {
                String checkName = dirs[1];
                if ("shell".equals(checkName))
                {
                    hasfile1 = true;
                }
                else if (checkName.endsWith(".tar.gz"))
                {
                    hasfile2 = true;
                }
            }
            if (hasfile1 && hasfile2)
            {
                return true;
            }
        }
        return false;
    }
}
