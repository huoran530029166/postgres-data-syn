package com.goldwind.dataaccess.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * 文件操作类
 *
 * @author 曹阳
 */
public class FileAssistant
{
    static String fileUtilName = "fileName";

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件名+路径
     * @return 是否存在
     */
    public static boolean fileExists(String filePath)
    {
        if (filePath != null)
        {
            return new File(filePath).exists();
        }
        else
        {
            return false;
        }

    }

    /**
     * 文件是否存在
     *
     * @param fileName 文件路径（可以是ftp路径，也可以是普通路径）
     * @param ftp      ftp对象
     * @return 是否存在
     * @throws Exception 异常
     */
    public static boolean bfFileExist(String fileName, FTPClient ftp) throws Exception
    {
        if (isFtpFile(fileName))
        {
            return FtpFileOper.isFTPFileExist(fileName, ftp);
        }
        else
        {
            return fileExists(fileName);
        }
    }

    /**
     * 得到文件修改时间
     *
     * @param fileName 文件全路径（可以是ftp路径，也可以是普通路径）
     * @param ftp      ftp对象
     * @return 文件修改时间
     * @throws DataAsException 异常信息
     */
    public static Calendar getFileUpdTime(String fileName, FTPClient ftp) throws DataAsException
    {
        Calendar val = Calendar.getInstance();
        try
        {
            if (isFtpFile(fileName))
            {
                // 取得FTP上的文件全路径
                String filepath = fileName.substring(fileName.indexOf("/", 10));

                // 取得文件修改时间
                String updatetime = ftp.getModificationTime(filepath).split(" ")[1].split("\\.")[0];
                Date date = new SimpleDateFormat(DataAsDef.TEMPFILENAMEBYTIME).parse(updatetime);
                val.setTime(date);

                // // 修正时间
                // val.add(Calendar.MILLISECOND, val.get(Calendar.ZONE_OFFSET) + val.get(Calendar.DST_OFFSET));
            }
            else
            {
                File file = new File(fileName);
                val.setTimeInMillis(file.lastModified());
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("1", new String[] { fileUtilName }, new Object[] { fileName }, exp);
        }
        return val;
    }

    /**
     * 删除临时文件夹
     *
     * @param dir 文件夹路径
     */
    public static void deleteTempDir(String dir)
    {
        File dir1 = new File(dir);
        if (dir1.isDirectory())
        {
            dir1.delete();
        }
    }

    /**
     * 删除空文件夹
     *
     * @param folder 文件夹路径
     */
    public static void deleteEmptyDirectory(String folder)
    {
        File file = new File(folder);
        if (file.exists())
        {
            File[] subFolder = file.listFiles();
            if (subFolder.length == 0)
            {
                file.delete();
            }
        }
    }

    /**
     * 删除某个目录及目录下的所有子目录和文件
     *
     * @param folderPath 需要删除的文件夹目录路径
     */
    public static void deleteFolder(String folderPath)
    {
        File dir = new File(folderPath);
        if (dir.isDirectory())
        {
            File[] subFile = dir.listFiles();
            // 递归删除目录中的子目录下的所有文件
            for (int i = 0; i < subFile.length; i++)
            {
                deleteFolder(subFile[i].getAbsolutePath());
            }
        }
        // 目录此时为空，可以删除
        dir.delete();
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名，包括路径
     * @throws DataAsException 自定义异常
     */
    public static void deleteFile(String fileName) throws DataAsException
    {
        try
        {
            File f = new File(fileName);
            if (f.exists())
            {
                f.delete();
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_deleteFile_1", new String[] { fileUtilName }, new Object[] { fileName }, exp);
        }
    }

    /**
     * 删除文件()
     *
     * @param fileName 要删除的文件(可以是ftp路径，也可以是普通路径)
     * @param ftp      ftp对象
     * @throws DataAsException 异常
     */
    public static void fileDelete(String fileName, FTPClient ftp) throws DataAsException
    {
        try
        {
            if (isFtpFile(fileName))
            {
                FtpFileOper.deleteFile(fileName, ftp);
            }
            else
            {
                File f = new File(fileName);
                if (f.exists())
                {
                    f.delete();
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_deleteFile_1", new String[] { fileUtilName }, new Object[] { fileName }, exp);
        }
    }

    /**
     * 移动文件
     *
     * @param srcFile  源文件，包括路径
     * @param destFile 目标文件，包括路径
     * @throws DataAsException 自定义异常
     */
    public static void moveFile(String srcFile, String destFile) throws DataAsException
    {
        try
        {
            File fDestFile = new File(destFile);
            File fParentFile = fDestFile.getParentFile();
            if (!fParentFile.exists())
            {
                fParentFile.mkdirs();
            }

            copyFileOperation(srcFile, destFile);
            deleteFile(srcFile);
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_moveFile_1", new String[] { "srcFile", "destFile" }, new Object[] { srcFile, destFile }, exp);
        }
    }

    /**
     * 是否为ftp文件
     *
     * @param fileName 文件名，包括路径
     * @return 是否是ftp文件
     */
    public static boolean isFtpFile(String fileName)
    {
        if (fileName == null || fileName.trim().isEmpty())
        {
            return false;
        }
        return (fileName.toLowerCase().indexOf("ftp://") == 0);
    }

    /**
     * 复制文件
     *
     * @param srcFileName  源文件名，包括路径
     * @param destFileName 目标文件名，包括路径
     * @throws DataAsException 自定义异常
     */
    public static void copyFile(String srcFileName, String destFileName) throws DataAsException
    {
        String tempFileName = "";
        try
        {

            File fDestFile = new File(destFileName);
            File fParentFile = fDestFile.getParentFile();
            if (!fParentFile.exists())
            {
                fParentFile.mkdirs();
            }
            tempFileName = destFileName + DataAsDef.TEMPFILEEXTNAME;
            copyFileOperation(srcFileName, tempFileName);

            File fTempFile = new File(tempFileName);
            fTempFile.renameTo(new File(destFileName));
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_copyFile_1", new String[] { "srcFileName", "destFileName" }, new Object[] { srcFileName, destFileName }, exp);
        }
    }

    /**
     * 复制文件
     *
     * @param oldPath 源文件，包括路径
     * @param newPath 目标文件，包括路径
     * @throws IOException i/o异常
     */
    public static void copyFileOperation(String oldPath, String newPath) throws IOException
    {
        int byteread = 0;
        File oldfile = new File(oldPath);
        if (oldfile.exists())
        {
            try (FileInputStream inStream = new FileInputStream(oldPath); FileOutputStream fs = new FileOutputStream(newPath);)
            {
                byte[] buffer = new byte[1024 * 64];
                while ((byteread = inStream.read(buffer)) != -1)
                {
                    fs.write(buffer, 0, byteread);
                }
            }
        }
    }

    /**
     * 拷贝文件夹
     *
     * @param srcDir       源文件夹
     * @param destDir      目标文件夹
     * @param pExceptDirs  未知
     * @param pExceptFiles 未知
     * @param pKeepFiles   未知
     * @throws DataAsException 自定义异常
     */
    public static void copyDir(String srcDir, String destDir, String[] pExceptDirs, String[] pExceptFiles, String[] pKeepFiles) throws DataAsException
    {
        try
        {
            File srcDir1 = new File(srcDir);
            if (!srcDir1.exists() || !srcDir1.isDirectory())
            {
                return;
            }
            File destDir1 = new File(destDir);
            if (!destDir1.exists() && destDir1.isDirectory())
            {
                destDir1.mkdir();
            }

            // 转换为小写
            String[] exceptDirs = ArrayOper.lowerTrimArray(pExceptDirs);
            String[] exceptFiles = ArrayOper.lowerTrimArray(pExceptFiles);
            String[] keepFiles = ArrayOper.lowerTrimArray(pKeepFiles);

            File[] srcFiles = srcDir1.listFiles();
            for (File srcFile : srcFiles)
            {
                if (srcFile.isDirectory())
                {
                    if (exceptDirs != null && ArrayOper.findDataInArray(srcFile.getName(), exceptDirs) >= 0)
                    {
                        continue;
                    }
                    String srcPath = srcFile.getAbsolutePath();
                    String destPath = srcPath.replace(srcDir, destDir);
                    copyDir(srcPath, destPath, exceptDirs, exceptFiles, keepFiles);
                }
                else
                {
                    File f = new File(destDir + "/" + srcFile.getName());
                    if ((exceptFiles == null || ArrayOper.findDataInArray(srcFile.getName(), exceptFiles) < 0) && (keepFiles == null || ArrayOper.findDataInArray(srcFile.getName(), keepFiles) < 0
                            || !f.exists()))
                    {
                        String destFile = destDir + "/" + srcFile.getName();
                        setFileNotReadOnly(destFile);
                        copyFile(srcFile.getAbsolutePath(), destFile);
                    }
                }

            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_copyDir_1", new String[] { "srcDir", "destDir", "pExceptDirs", "pExceptFiles", "pKeepFiles" },
                    new Object[] { srcDir, destDir, pExceptDirs, pExceptFiles, pKeepFiles }, exp);
        }
    }

    /**
     * 设置文件非只读
     *
     * @param fileName 文件名，包括路径
     * @throws DataAsException 自定义异常
     */
    public static void setFileNotReadOnly(String fileName) throws DataAsException
    {
        try
        {
            File f = new File(fileName);
            if (f.exists())
            {
                if (f.setReadOnly())
                {
                    f.setWritable(true);
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_setFileNotReadOnly_1", new String[] { fileUtilName }, new Object[] { fileName }, exp);
        }
    }

    /**
     * 保存txt文件
     *
     * @param file        写入文件名，包括路径
     * @param datas       字符串数组
     * @param charsetName 字符集
     * @throws Exception 自定义异常
     */
    public static void saveTxtFile(String file, String[] datas, String charsetName) throws Exception
    {

        File f = new File(file);

        File fParentFile = f.getParentFile();
        if (!fParentFile.exists())
        {
            fParentFile.mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, true), charsetName)))
        {

            // fileOut = new FileOutputStream(f, true);
            // outStream = new OutputStreamWriter(fileOut, charsetName);
            // pw = new PrintWriter(outStream);

            if (datas != null)
            {
                for (int i = 0; i < datas.length; i++)
                {
                    pw.append(datas[i] + System.getProperty("line.separator"));
                    if (datas.length - 1 == i)
                    {
                        pw.flush();
                    }
                }
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_saveTxtFile_1", new String[] { "file", "charsetName" }, new Object[] { file, charsetName }, null);
        }
    }

    /**
     * 读取txt文件
     *
     * @param file        文本文件名称，包括路径
     * @param charsetName 字符集
     * @return 文本文件内容字符串
     * @throws DataAsException 自定义异常
     * @throws IOException     i/o异常
     */
    public static String readTxtFile(String file, String charsetName) throws DataAsException, IOException
    {
        StringBuilder sb = new StringBuilder();
        // BufferedReader reader = null;
        // FileInputStream fileInputStream = null;
        // InputStreamReader inputStreamReader = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName)))
        {
            // fileInputStream = new FileInputStream(file);
            // inputStreamReader = new InputStreamReader(new FileInputStream(file), charsetName);
            // reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));

            String str = null;

            while ((str = reader.readLine()) != null)
            {
                if (str.isEmpty())
                {
                    continue;
                }

                if (sb.length() > 0)
                {
                    sb.append("\r\n");
                }

                sb.append(str);
            }
        }
        catch (IOException exp)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_readTxtFile_1", new String[] { "file" }, new Object[] { file }, exp);
        }
        return sb.toString();

    }

    /**
     * 根据系统类型得到上级目录
     *
     * @param fileName 本级目录
     * @return val 上级目录
     * @author Wangdashu
     */
    public static String getUpFilePath(String fileName)
    {
        String fileNamePath = fileName.replace("\\\\", "\\").replace("\\", "/");
        String[] sp = fileNamePath.split("/");
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < sp.length - 1; s++)
        {
            if (s > 0)
            {
                sb = sb.append("/").append(sp[s]);
            }
            else
            {
                sb = sb.append(sp[s]);
            }
        }

        return sb.toString();

    }

    /**
     * 获取文件md5码
     *
     * @param file 文件
     * @return MD5
     * @throws NoSuchAlgorithmException 异常
     * @throws IOException              异常
     */
    public static String getMd5ByFile(File file) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest messageDigest = null;
        FileInputStream fis = null;
        // FileChannel fileChannel = null;
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            // if (file.length() <= Integer.MAX_VALUE)
            // {
            // // 当文件<2G可以直接读取
            // fileChannel = fis.getChannel();
            // MappedByteBuffer byteBuffer =
            // fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            // messageDigest.update(byteBuffer);
            // }
            // else
            // {
            // 当文件>2G需要切割读取
            byte[] buffer = new byte[256 * 1024];
            int numRead;
            while ((numRead = fis.read(buffer)) > 0)
            {
                messageDigest.update(buffer, 0, numRead);
            }
            // }
        }
        finally
        {
            // if (fileChannel != null)
            // {
            // fileChannel.close();
            // fileChannel = null;
            // }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }

        byte[] resultBytes = messageDigest.digest();
        StringBuffer stringbuffer = new StringBuffer(2 * resultBytes.length);
        for (int l = 0; l < resultBytes.length; l++)
        {
            char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            char c0 = hexDigits[(resultBytes[l] & 0xf0) >> 4];// 取字节中高 4 位的数字转换
            // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
            char c1 = hexDigits[resultBytes[l] & 0xf];// 取字节中低 4 位的数字转换
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
        return stringbuffer.toString();
    }

    /**
     * 得到某一路径下的所有文件,包括子文件夹下的文件
     *
     * @param filePath 文件路径
     * @return 路径下的所有文件
     * @throws DataAsException 自定义异常
     */
    public static String[] getSubFile(String filePath) throws DataAsException
    {
        // 路径下的所有文件
        String[] fileList = null;
        ArrayList<String> arrayList = new ArrayList<>(20);
        try
        {
            File file = new File(filePath);
            if (file.exists())
            {
                File[] subFileList = new File(filePath).listFiles();

                // 循环该路径下的所有子文件
                for (int i = 0; i < subFileList.length; i++)
                {
                    // 判断子文件是否是文件夹
                    if (subFileList[i].isDirectory())
                    {
                        // 如果子文件夹不为空,递归子文件夹
                        if (subFileList[i].listFiles().length > 0)
                        {
                            ArrayList<String> userList = new ArrayList<>(20);
                            Collections.addAll(userList, getSubFile(subFileList[i].getAbsolutePath()));
                            // 将数据文件添加到ArrayList中
                            arrayList.addAll(userList);
                        }
                    }
                    else
                    {
                        // 将数据文件添加到ArrayList中
                        arrayList.add(subFileList[i].getAbsolutePath());
                    }
                }
                // 将ArrayList转换为字符串数组
                fileList = arrayList.toArray(new String[arrayList.size()]);
                for (int i = 0; i < subFileList.length; i++)
                {
                    fileList[i] = fileList[i].replace("\\", "/");
                }
            }
        }
        catch (Exception e)
        {
            DataAsExpSet.throwExpByResCode("FileAssistant_getSubFile_1", new String[] { "filePath" }, new Object[] { filePath }, e);
        }
        return fileList;
    }

    /**
     * 获得指定文件的byte数组
     *
     * @param filename 文件全路径 自定义异常
     * @return byte数组
     * @throws IOException i/o异常
     */
    public static byte[] fileToByteArray(String filename) throws IOException
    {
        try (@SuppressWarnings("resource") FileChannel fc = new RandomAccessFile(filename, "r").getChannel())
        {
            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0)
            {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            // 释放
            byteBuffer.clear();
            return result;
        }
    }

    /**
     * 根据文件路径创建父路径文件夹
     *
     * @param filePath 文件路径
     * @param sep      文件路径分隔符
     */
    public static void mkdirsByFilePath(String filePath, String sep)
    {
        filePath = filePath.substring(0, filePath.lastIndexOf(sep) + sep.length());
        File file = new File(filePath);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }
}
