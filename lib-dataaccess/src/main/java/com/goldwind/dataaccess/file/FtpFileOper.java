package com.goldwind.dataaccess.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.goldwind.dataaccess.ArrayOper;
import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.dataaccess.Log;
import com.goldwind.dataaccess.exception.DataAsException;

/**
 * Ftp操作类
 * 
 * @author 谭璟
 *
 */
public class FtpFileOper
{
    /**
     * logger对象
     */
    private static Log LOGGER = Log.getLog(FtpFileOper.class);

    /**
     * 连接ftp
     * 
     * @param addr
     *            服务器IP
     * @param port
     *            服务器port
     * @param username
     *            登入服务器用户名
     * @param password
     *            登入服务器密码
     * @return FTPClient对象
     * @throws DataAsException
     *             Exception Message
     */
    public static FTPClient connect(String addr, int port, String username, String password) throws DataAsException
    {
        // LOGGER.info("【连接文件服务器】addr = " + addr + " , port : " + port + " , username = " + username + " , password = " + password);
        FTPClient ft = new FTPClient();
        try
        {
            // 设置FTP连接超时时间（10s）
            ft.setConnectTimeout(10 * 1000);
            // 设置下载 FTP 文件过程中的超时时间(20s,这个超时不是指下载文件整个过程的超时处理，而是仅针对终端 Socket 从输入流中，每一次可进行读取操作之前陷入阻塞的超时。)
            ft.setDataTimeout(10 * 1000);
            // 连接
            ft.connect(addr, port);
            // 登入
            ft.login(username, password);
            // 设置为被动模式 FTPClient连接server 开通一个端口来传输数据
            ft.enterLocalPassiveMode();
            // 设置文件模式 binary
            ft.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 设置编码格式
            ft.setControlEncoding("GBK");
        }
        catch (Exception e)
        {
            // LOGGER.error("【连接文件服务器失败】", e);
            DataAsExpSet.throwExpByMsg("Failed to connect file server ", new String[] {}, new Object[] {}, e);
        }

        // 检验登陆操作的返回码是否正确,判断server是否可以使用
        if (!FTPReply.isPositiveCompletion(ft.getReplyCode()))
        {
            LOGGER.error("Failure to connect FTP SERVER,SERVER IP[" + addr + "], USER NAME[" + username + "], Ftp response:" + ft.getReplyString());
            closeConnect(ft);
            ft = null;
        }

        return ft;
    }

    /**
     * 
     * 连接ftp
     * 
     * @param addr
     *            服务器IP
     * @param port
     *            服务器port
     * @param username
     *            登入服务器用户名
     * @param password
     *            登入服务器密码
     * @param path
     *            服务器文件路径
     * @return FTPClient对象
     * @throws DataAsException
     *             异常信息
     */
    public static FTPClient connectFtp(String addr, int port, String username, String password, String path) throws DataAsException
    {
        return connect(addr, port, username, password);
    }

    /**
     * 
     * @param ft
     *            ftp对象
     * @throws DataAsException
     *             异常信息
     */
    public static void closeConnect(FTPClient ft) throws DataAsException
    {
        // LOGGER.info("关闭文件服务器连接");
        if (ft == null)
        {
            return;
        }

        try
        {
            // 关闭连接
            ft.logout();
            ft.disconnect();
        }
        catch (IOException e)
        {
            // LOGGER.error("关闭连接失败", e);
            // throw new RuntimeException("Failed to close connection");
            DataAsExpSet.throwExpByMsg("Failed to close connection", new String[] {}, new Object[] {}, e);
        }
    }

    /**
     * 切换工作目录
     * 
     * @param path
     *            路径
     * @param ft
     *            FTP对象
     * @throws DataAsException
     *             异常信息
     */
    public static void getFilePath(String path, FTPClient ft) throws DataAsException
    {
        // LOGGER.info("切换工作目录-directory : " + path);
        boolean bTemp = objectVaile(ft);

        if (bTemp)
        {
            try
            {
                if (!ft.changeWorkingDirectory(path))
                {
                    ft.makeDirectory(path);
                    // ft.changeWorkingDirectory(path);
                    ft.changeWorkingDirectory(new String(path.getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
                }
            }
            catch (Exception e)
            {
                // LOGGER.error("切换工作目录失败", e);
                // throw new RuntimeException("Failed to switch working directory");
                DataAsExpSet.throwExpByMsg("Failed to switch working directory", new String[] {}, new Object[] {}, e);
            }
        }
    }

    /**
     * 上传
     * 
     * @param file
     *            上传文件
     * @param ft
     *            ftp对象
     * @throws DataAsException
     *             异常
     * @throws IOException
     *             io异常
     */
    public static void upLoadFile(File file, FTPClient ft) throws DataAsException, IOException
    {
        if (file == null)
        {
            // LOGGER.warn("存储的文件为空");
            // throw new RuntimeException("Upload file is empty");
            DataAsExpSet.throwExpByMsg("Upload file is empty", new String[] {}, new Object[] {}, new DataAsException("Upload file is empty"));
        }

        // LOGGER.info("上传文件/文件夹file ： " + file.getName());
        boolean bTemp = objectVaile(ft);

        if (bTemp)
        {
            if (!file.isDirectory())
            {
                saveFile(new File(file.getPath()), ft);
                return;
            }

            getFilePath(file.getName(), ft);
            for (File item : file.listFiles())
            {
                if (!item.isDirectory())
                {
                    saveFile(item, ft);
                    continue;
                }

                upLoadFile(item, ft);
                ft.changeToParentDirectory();
            }
        }
    }

    /**
     * 保存文件
     * 
     * @param file
     *            文件
     * @param ft
     *            ftp对象
     * @throws DataAsException
     *             异常信息
     * @throws IOException
     *             io异常
     */
    public static void saveFile(File file, FTPClient ft) throws DataAsException, IOException
    {
        if (file == null)
        {
            // LOGGER.warn("存储的文件为空");
            // throw new RuntimeException("Stored files are empty");
            DataAsExpSet.throwExpByMsg("Stored files are empty", new String[] {}, new Object[] {}, new DataAsException("Stored files are empty"));
        }

        // LOGGER.info("存储文件file ： " + file.getName());
        boolean bTemp = objectVaile(ft);

        if (bTemp)
        {
            FileInputStream input = new FileInputStream(file);
            ft.storeFile(file.getName(), input);
            input.close();
        }
    }

    /**
     * 下载文件
     * 
     * @param ftpFile
     *            要下载文件
     * @param dstFile
     *            目标文件
     * @param ft
     *            ftp对象
     * @throws DataAsException
     *             自定义异常
     * @throws IOException
     *             io异常
     */
    public static void downLoad(String ftpFile, String dstFile, FTPClient ft) throws DataAsException, IOException
    {
        // LOGGER.info("下载文件到指定目录 ftpFile = " + ftpFile + " , dstFile = " + dstFile);
        if (ftpFile.isEmpty())
        {
            // LOGGER.warn("参数ftpFile为空");
            // throw new RuntimeException("The parameter ftpFile is empty");
            DataAsExpSet.throwExpByMsg("The parameter ftpFile is empty", new String[] {}, new Object[] {}, new DataAsException("The parameter ftpFile is empty"));

        }

        if (dstFile.isEmpty())
        {
            // LOGGER.warn("参数dstFile为空");
            // throw new RuntimeException("The parameter dstFile is empty");
            DataAsExpSet.throwExpByMsg("The parameter dstFile is empty", new String[] {}, new Object[] {}, new DataAsException("The parameter dstFile is empty"));

        }

        boolean bTemp = objectVaile(ft);
        if (bTemp)
        {
            File file = new File(dstFile);
            File dir = file.getParentFile();
            if (dir != null && !dir.exists())
            {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ft.retrieveFile(ftpFile, fos);

            fos.flush();
            fos.close();
        }
    }

    /**
     * 删除文件
     * 
     * @param fileName
     *            要删除的文件
     * @param ft
     *            ftp对象
     * @throws IOException
     *             异常
     */
    public static void deleteFile(String fileName, FTPClient ft) throws IOException
    {
        // LOGGER.info("删除文件 fileName ： " + fileName);
        // boolean bTemp = objectVaile(ft);
        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        // if (bTemp)
        // {
        // if (fileName.isEmpty())
        // {
        // // LOGGER.warn("参数fileName为空");
        // }
        // }

        if (null != ft)
        {
            ft.deleteFile(fileName);
        }
    }

    /**
     * 检查对象是否有效
     * 
     * @param ft
     *            对象
     * @return true为真 false对象为空
     */
    public static boolean objectVaile(FTPClient ft)
    {
        return ft == null ? false : true;
    }

    /**
     * 得到某FTP文件夹下所有子对象，文件或文件夹
     * 
     * @param parent
     *            父目录
     * @param user
     *            用户名
     * @param password
     *            密码
     * @param isDir
     *            是否是目录
     * @return 子对象集合
     * @throws Exception
     *             异常
     */
    public static FTPFile[] getSubObj(String parent, String user, String password, boolean isDir) throws Exception
    {
        FTPClient ft = new FTPClient();
        String dir = parent.substring(6);// 去掉目录中的ftp://
        String[] parts = dir.split("/");
        String ftpIp = parts[0];
        String ftpDir = "";
        if (parts.length > 0)
        {
            for (int i = 1; i < parts.length; i++)
            {
                ftpDir += parts[i] + "/";
            }
        }
        FTPFile[] fArray = null;
        try
        {
            ft.connect(ftpIp);
            ft.login(user, password);
            if (!ftpDir.isEmpty())
            {
                ft.changeWorkingDirectory(ftpDir);
            }
            if (isDir)
            {
                fArray = ft.listDirectories();
            }
            else
            {
                fArray = ft.listFiles();
            }
        }
        finally
        {
            // 断开ftp连接
            if (ft.isConnected())
            {
                FtpFileOper.closeConnect(ft);
            }
        }
        return fArray;
    }

    /**
     * 得到某FTP文件夹下文件夹
     * 
     * @param parent
     *            父目录
     * @param user
     *            用户名
     * @param password
     *            密码
     * @return 子对象集合
     * @throws Exception
     *             异常
     */
    public static List<String> getSubDir(String parent, String user, String password) throws Exception
    {
        List<String> dirName = new ArrayList<String>();
        FTPFile[] ffile = getSubObj(parent, user, password, true);
        for (FTPFile f : ffile)
        {
            dirName.add(f.getName());
        }
        return dirName;
    }

    /**
     * 验证是否是根路径
     * 
     * @param path
     *            路径
     * @return true是 false不是
     */
    public static boolean checkRootPath(String path)
    {
        if (path.lastIndexOf('/') != -1)
        {
            path = path.substring(0, path.lastIndexOf('/'));
        }

        return path.split("/").length <= 3;
    }

    /**
     * 测试文件夹是否存在
     * 
     * @param dirPath
     *            文件夹路径
     * @param user
     *            用户名
     * @param password
     *            密码
     * @return true 存在 false 不存在
     * @throws DataAsException
     *             异常信息
     */
    public static boolean dirExist(String dirPath, String user, String password) throws DataAsException
    {
        int index = 0;
        try
        {
            String tempDir = dirPath;
            if (tempDir.lastIndexOf('/') != -1)
            {
                tempDir = tempDir.substring(0, tempDir.lastIndexOf('/'));
            }

            if (FtpFileOper.checkRootPath(tempDir))
            {
                return true;
            }

            List<String> subDir = FtpFileOper.getSubDir(tempDir.substring(0, tempDir.lastIndexOf("/")), user, password);
            index = ArrayOper.findDataInArray(tempDir.substring(tempDir.lastIndexOf("/") + 1), subDir.toArray(new String[] {}), true);
        }
        catch (Exception exp)
        {
            // if (FtpFileOper.CheckNoDirExp(exp))
            // {
            // return false;
            // }
            DataAsExpSet.throwExpByResCode("FtpFileOper_dirExist", new String[] { "dirPath", "user", "password" }, new Object[] { dirPath, user, password }, exp);
        }
        return (index >= 0);
    }

    /**
     * 检验指定路径的文件是否存在ftp服务器中
     * 
     * @param filePath
     *            指定绝对路径的文件
     * @param ftp
     *            ftp对象
     * @return true 存在 false 不存在
     * @throws Exception
     *             异常
     */
    public static boolean isFTPFileExist(String filePath, FTPClient ftp) throws Exception
    {
        try
        {
            // 提取绝对地址的目录以及文件名
            filePath = filePath.substring(filePath.indexOf('/', 10));
            String dir = filePath.substring(0, filePath.lastIndexOf('/'));
            String file = filePath.substring(filePath.lastIndexOf('/') + 1);

            // 进入文件所在目录，注意编码格式，以能够正确识别中文目录
            ftp.changeWorkingDirectory(new String(dir.getBytes("GBK"), StandardCharsets.ISO_8859_1));

            // 检验文件是否存在
            InputStream is = ftp.retrieveFileStream(new String(file.getBytes("GBK"), StandardCharsets.ISO_8859_1));
            if (is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE)
            {
                return false;
            }

            is.close();
            ftp.completePendingCommand();
            return true;
        }
        finally
        {
            if (ftp != null)
            {
                ftp.disconnect();
            }
        }
    }

    /**
     * 得到某FTP文件夹下名称符合过滤表达式的子文件
     * 
     * @param parentDir
     *            父目录
     * @param filterExp
     *            文件过滤表达式
     * @param user
     *            FTP用户名
     * @param password
     *            FTP密码
     * @return 子文件集合
     * @throws DataAsException
     */
    public static List<String> getSubFile(String parentDir, String filterExp, String user, String password) throws DataAsException
    {
        List<String> rtnVal = new ArrayList<>();
        try
        {
            List<String> fName = new ArrayList<>();
            FTPFile[] ffile = getSubObj(parentDir, user, password, false);
            for (FTPFile f : ffile)
            {
                fName.add(f.getName());
            }
            String[] filterArray = filterExp.split(",");
            for (int i = fName.size() - 1; i >= 0; i--)
            {
                if (filterArray.length <= 1)
                {
                    break;
                }
                boolean flag = false;
                for (int j = 0; j < filterArray.length; j++)
                {
                    String regex = "^" + filterArray[j].replace("*", ".*").replace("?", ".?");
                    if (fName.get(i).matches(regex))
                    {
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                {
                    fName.remove(i);
                }
            }

            for (int i = 0; i < fName.size(); i++)
            {
                rtnVal.add(fName.get(i));
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FtpFileOper_getSubFile_1", new String[] { "parentDir", "filterExp" }, new Object[] { parentDir, filterExp }, exp);
        }
        return rtnVal;
    }

    /**
     * 得到某FTP文件夹下名称符合过滤表达式的子文件
     * 
     * @param parentDir
     *            父目录
     * @param filterExp
     *            文件过滤表达式
     * @param user
     *            FTP用户名
     * @param password
     *            FTP密码
     * @return 子文件集合
     * @throws DataAsException
     *             异常
     */
    public static HashMap<String, Date> getSubDetailFile(String parentDir, String filterExp, String user, String password) throws DataAsException
    {
        HashMap<String, Date> rtnVal = new HashMap<>();
        try
        {
            FTPFile[] ffile = getSubObj(parentDir, user, password, false);
            if (ffile != null)
            {
                String[] filterArray = filterExp.split(",");
                for (int i = 0; i < ffile.length; i++)
                {
                    for (int j = 0; j < filterArray.length; j++)
                    {
                        String regex = "^" + filterArray[j].replace("*", ".*").replace("?", ".?");
                        if (ffile[i].getName().matches(regex))
                        {
                            // 修正时间
                            Calendar val = ffile[i].getTimestamp();
                            if (val == null)
                            {
                                continue;
                            }
                            // val.add(Calendar.MILLISECOND, val.get(Calendar.ZONE_OFFSET) + val.get(Calendar.DST_OFFSET));
                            rtnVal.put(ffile[i].getName(), val.getTime());
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception exp)
        {
            DataAsExpSet.throwExpByResCode("FtpFileOper_getSubDetailFile_1", new String[] { "parentDir", "filterExp" }, new Object[] { parentDir, filterExp }, exp);
        }
        return rtnVal;
    }

}
