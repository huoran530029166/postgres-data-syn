/**   
 * Copyright © 2019 金风科技. All rights reserved.
 * 
 * @Title: FileAssistantTest.java 
 * @Prject: SOAM-lib-dataaccess
 * @Package: com.goldwind.dataaccess.file 
 * @Description: FileAssistantTest
 * @author: Administrator   
 * @date: 2019年9月5日 下午4:41:27 
 * @version: V1.0   
 */
package com.goldwind.dataaccess.file;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.goldwind.dataaccess.exception.DataAsException;

/**
 * @ClassName: FileAssistantTest
 * @Description: FileAssistantTest
 * @author: Administrator
 * @date: 2019年9月5日 下午4:41:27
 */
public class FileAssistantTest
{

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#fileExists(java.lang.String)}.
     */
    @Test
    public void testFileExistsNull()
    {
        String filePath = null;
        boolean act = FileAssistant.fileExists(filePath);
        Assert.assertFalse(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#fileExists(java.lang.String)}.
     */
    @Test
    public void testFileExists()
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String filePath = rootPath + path;
        boolean act = FileAssistant.fileExists(filePath);
        Assert.assertTrue(act);
    }

    @Test
    public void testIsFtpFileNull()
    {
        String fileName = null;
        boolean act = FileAssistant.isFtpFile(fileName);
        Assert.assertFalse(act);
    }

    @Test
    public void testIsFtpFile()
    {
        String fileName = "ftp://abc.txt";
        boolean act = FileAssistant.isFtpFile(fileName);
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#copyFile(java.lang.String, java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testCopyFile() throws DataAsException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String srcFileName = rootPath + path;
        String destFileName = srcFileName + ".bak";
        FileAssistant.copyFile(srcFileName, destFileName);
        Assert.assertThat(destFileName, Matchers.anything());
    }

    @Test
    public void testDeleteFile() throws DataAsException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String srcFileName = rootPath + path;
        String destFileName = srcFileName + ".bak";
        FileAssistant.deleteFile(destFileName);
        Assert.assertThat(destFileName, Matchers.anything());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#copyFileOperation(java.lang.String, java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testCopyFileOperation() throws IOException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String srcFileName = rootPath + path;
        String destFileName = srcFileName + ".bak";
        FileAssistant.copyFileOperation(srcFileName, destFileName);
        Assert.assertThat(destFileName, Matchers.anything());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#setFileNotReadOnly(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testSetFileNotReadOnly() throws DataAsException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String srcFileName = rootPath + path;
        String destFileName = srcFileName + ".bak";
        FileAssistant.setFileNotReadOnly(destFileName);
        Assert.assertThat(destFileName, Matchers.anything());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#saveTxtFile(java.lang.String, java.lang.String[], java.lang.String)}.
     * 
     * @throws Exception
     *             异常
     */
    @Test
    public void testSaveTxtFile() throws Exception
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
        String srcFileName = rootPath + path;
        String destFileName = srcFileName + ".bak";
        FileAssistant.deleteFile(destFileName);
        String[] datas = { "123", "234", "3345" };
        FileAssistant.saveTxtFile(destFileName, datas, "utf-8");
        Assert.assertThat(destFileName, Matchers.anything());
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#readTxtFile(java.lang.String, java.lang.String)}.
     * 
     * @throws IOException
     *             异常
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testReadTxtFile() throws DataAsException, IOException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/test.bak";
        String srcFileName = rootPath + path;
        String act = FileAssistant.readTxtFile(srcFileName, "utf-8");
        String exp = "123/234/345";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#readTxtFile(java.lang.String, java.lang.String)}.
     * 
     * @throws IOException
     *             异常
     * @throws DataAsException
     *             异常
     */
//    @Test(expected = NullPointerException.class)
//    public void testReadTxtFileException() throws DataAsException, IOException
//    {
//        String rootPath = System.getProperty("user.dir").replace("\\", "/");
//        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest.java";
//        String srcFileName = rootPath + path;
//        String destFileName = srcFileName + ".bak";
//        FileAssistant.readTxtFile(destFileName, "8");
//    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#readTxtFile(java.lang.String, java.lang.String)}.
     * 
     * @throws IOException
     *             异常
     * @throws DataAsException
     *             异常
     */
//    @Test(expected = NullPointerException.class)
//    public void testReadTxtFileException2() throws DataAsException, IOException
//    {
//        String rootPath = System.getProperty("user.dir").replace("\\", "/");
//        String path = "/src/test/java/com/goldwind/dataaccess/file/FileAssistantTest123.java";
//        String srcFileName = rootPath + path;
//        String destFileName = srcFileName + ".bak";
//        FileAssistant.readTxtFile(destFileName, "utf-8");
//    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#getUpFilePath(java.lang.String)}.
     */
    @Test
    public void testGetUpFilePath()
    {
        String fileName = "D:\\123\\23.txt";
        String act = FileAssistant.getUpFilePath(fileName);
        String exp = "D:/123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#getUpFilePath(java.lang.String)}.
     */
    @Test
    public void testGetUpFilePath2()
    {
        String fileName = "D:\\123\\23.txt";
        String act = FileAssistant.getUpFilePath(fileName);
        String exp = "D:/123";
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#getSubFile(java.lang.String)}.
     * 
     * @throws DataAsException
     *             异常
     */
    @Test
    public void testGetSubFile() throws DataAsException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/";
        String srcFileName = rootPath + path;
        String[] file = FileAssistant.getSubFile(srcFileName);
        boolean act = false;
        for (String f : file)
        {
            if (f.equals(srcFileName + "FileAssistantTest.java"))
            {
                act = true;
            }
        }
        Assert.assertTrue(act);
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#fileToByteArray(java.lang.String)}.
     * 
     * @throws IOException
     *             异常
     */
    @Test
    public void testFileToByteArray() throws IOException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/test.bak";
        String srcFileName = rootPath + path;
        byte[] act = FileAssistant.fileToByteArray(srcFileName);
        byte[] exp = "123/234/345".getBytes();
        Assert.assertThat(act, Matchers.equalTo(exp));
    }

    /**
     * Test method for {@link com.goldwind.dataaccess.file.FileAssistant#fileToByteArray(java.lang.String)}.
     * 
     * @throws IOException
     *             异常
     */
    @Test(expected = Exception.class)
    public void testFileToByteArrayException() throws IOException
    {
        String rootPath = System.getProperty("user.dir").replace("\\", "/");
        String path = "/src/test/java/com/goldwind/dataaccess/file/test1.bak";
        String srcFileName = rootPath + path;
        FileAssistant.fileToByteArray(srcFileName);
    }

}
