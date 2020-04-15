import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import com.goldwind.dataaccess.DataAsDef;
import com.goldwind.dataaccess.file.IniFileOper;

public class DateTimeTest
{

    public static void main(String[] args) throws IOException
    {
        // TODO Auto-generated method stub
        // 读取配置文件中的数据进行数据转发
        IniFileOper oper = new IniFileOper("E:\\Java_Soam_trunk\\DataDisposalService_soam\\target\\config\\pfrdatefile.properties");
        String beginTime = oper.getProperty("application");
        System.out.println(beginTime);

        Pattern p = Pattern.compile(
                "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        System.out.println(p.matcher(beginTime).matches() ? beginTime : "出错了");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DataAsDef.DATETIMEFORMATSTR);
        LocalDateTime localDateTime = LocalDateTime.parse("1999-03-30 00:00:00", dtf);
        String lt = localDateTime.format(dtf);
        System.out.println(localDateTime);
        System.out.println(lt);
        
        localDateTime.plusMinutes(-60);
        System.out.println(localDateTime);
    }

}
