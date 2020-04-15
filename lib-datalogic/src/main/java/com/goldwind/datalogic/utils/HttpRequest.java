package com.goldwind.datalogic.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.goldwind.dataaccess.Log;
import com.goldwind.datalogic.utils.NetCommDef.MethodType;

public class HttpRequest
{
    /**
     * 输出日志
     */
    private static Log LOGGER = Log.getLog(HttpRequest.class);
    /**
     * mail 登入账号
     */
    private static final String MAIL_PATH = "";

    /**
     * mail 登入密码
     */
    private static final String MAIL_POSSWORD = "";

    /**
     * mail server 端口
     */
    private static final String MAIL_SERVER = "";

    /**
     * ` 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param)
    {
        String result = "";
        BufferedReader in = null;
        try
        {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        // 使用finally块来关闭输入流
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception e2)
            {
                LOGGER.error(e2);
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                LOGGER.error(ex);
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param jsonObject
     *            请求参数，JSONObject的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, JSONObject jsonObject)
    {
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        String result = "";
        try
        {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            // 发送POST请求必须设置如下两行
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new DataOutputStream(conn.getOutputStream());
            // 发送请求参数
            out.write(jsonObject.toString().getBytes("UTF-8"));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result += line;
            }
        }
        catch (Exception e)
        {
            LOGGER.error(e);
        }
        // 使用finally块来关闭输出流、输入流
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
                if (conn != null)
                {
                    conn.disconnect();
                }
            }
            catch (IOException ex)
            {
                LOGGER.error(ex);
            }
        }
        return result;
    }

    /**
     * 短信接口
     * 
     * @param requestUrl
     *            URl
     * @param type
     *            请求类型
     * @param data
     *            内容
     * @return 返回一个json对象
     */
    public static String httpRequest(String requestUrl, MethodType type, String data)
    {
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        String result = null;

        try
        {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(type.getResult());
            httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != data)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                // 注意编码格式，防止中文乱码
                outputStream.write(data.toString().getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();

            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            result = buffer.toString();
        }
        catch (ConnectException ce)
        {
            LOGGER.error(ce);
            System.out.println("Weixin server connection timed out");
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            System.out.println("http request error:{}");
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                LOGGER.error(e);
            }
        }
        return result;
    }

    /**
     * GO++ 调用接口 方法
     * 
     * @param requestUrl
     *            URl
     * @param type
     *            请求类型
     * @param outputStr
     *            发送内容
     * @return 返回一个json对象
     */
    public static JSONObject httpsRequest(String requestUrl, MethodType type, JSONObject outputStr)
    {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;

        try
        {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = { new MyTrustManager() };

            // 初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(type.getResult());
            httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.toString().getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null)
            {
                buffer.append(str);
            }

            bufferedReader.close();
            inputStreamReader.close();

            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        }
        catch (ConnectException ce)
        {
            LOGGER.error(ce);
            System.out.println("Weixin server connection timed out");
        }
        catch (Exception e)
        {
            LOGGER.error(e);
            System.out.println("http request error:{}");
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch (IOException e)
            {
                LOGGER.error(e);
            }
        }
        return jsonObject;
    }

    /**
     * 邮件接口
     * 
     * @param username
     *            用户名
     * @param data
     *            数据
     * @param revicemail
     *            推送的邮箱
     */
    public static void sendMailMessage(String username, String data, String revicemail)
    {
        Session session = initmail();
        try
        {
            MimeMessage message = createMimeMessage(session, MAIL_PATH, revicemail, username, data);
            Transport transport = session.getTransport();
            transport.connect(MAIL_PATH, MAIL_POSSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    /**
     * 返回session
     * 
     * @return session
     */
    private static Session initmail()
    {
        Properties props = new Properties(); // 参数配置
        props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", MAIL_SERVER); // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true"); // 需要请求认证

        Session session = Session.getInstance(props);
        session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log

        return session;
    }

    /**
     * 
     * @param session
     *            session 对象
     * @param sendMail
     *            发件邮件地址
     * @param receiveMail
     *            收邮件地址
     * @param username
     *            用户名
     * @param data
     *            推送内容
     * @return MimeMessagem 对象
     */
    private static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String username, String data)
    {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        try
        {
            // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
            message.setFrom(new InternetAddress(sendMail, username, "UTF-8"));

            // 3. To: 收件人（可以增加多个收件人、抄送、密送）
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, username, "UTF-8"));

            // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
            message.setSubject(" 告警信息", "UTF-8");

            // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
            message.setContent(data, "text/html;charset=UTF-8");

            // 6. 设置发件时间
            message.setSentDate(new Date());

            // 7. 保存设置
            message.saveChanges();
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
        return message;
    }
}