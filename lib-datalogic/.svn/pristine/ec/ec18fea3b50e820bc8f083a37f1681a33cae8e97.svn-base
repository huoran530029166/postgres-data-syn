import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.goldwind.dataaccess.DataAsExpSet;
import com.goldwind.datalogic.socket.SocketComm;
import com.goldwind.datalogic.utils.NetCommDef;

public class SocketTest
{

    static String ip;
    static int port;

    public static void main(String[] args)
    {
        try (Socket tcpClient = createSocketConn(ip, port);)
        {
            String transData = "(netjsaon|[{jsaon.cpu},{jsaon.mem}])";

            // 发送通讯测试标志
            byte[] result = sendData(tcpClient, transData.getBytes(StandardCharsets.UTF_8));
            String s = new String(result, StandardCharsets.UTF_8);
            System.out.println(s);
        }
        catch (Exception exp)
        {
            // logger.error(DataAsExpSet.getExpMsgByResCode("SocketConn_run_1", new String[] { "ip", "port" }, new Object[] { ip, port }, exp));
        }
    }

    /**
     * 创建socket
     * 
     * @param sendIp
     * @param sendPort
     * @return
     * @throws IOException
     */
    public static Socket createSocketConn(String sendIp, int sendPort) throws IOException
    {

        Socket s = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(sendIp, sendPort);
        // 设置连接超时时长为10s(20171023与三版保持一致).--wrb
        s.connect(socketAddress, 5000);
        s.setSoTimeout(90000);
        return s;
    }

    /**
     * 发送数据
     *
     * @param tcpClient
     *            socket连接
     * @param data
     *            数据
     * @return 返回结果
     * @throws IOException
     *             IO异常
     */
    public static byte[] sendData(Socket tcpClient, byte[] data) throws IOException
    {
        OutputStream out = tcpClient.getOutputStream();
        out.write(data);
        out.flush();
        InputStream in = tcpClient.getInputStream();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] result = new byte[8192];
        int num = 0;
        if ((num = in.read(result)) != -1)
        {
            swapStream.write(result, 0, num);
            return swapStream.toByteArray();
        }
        else
        {
            return null;
        }
    }
}
