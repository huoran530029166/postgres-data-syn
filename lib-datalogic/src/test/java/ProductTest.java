//
// import java.util.Properties;
//
// import org.apache.kafka.clients.producer.Callback;
// import org.apache.kafka.clients.producer.KafkaProducer;
// import org.apache.kafka.clients.producer.ProducerRecord;
// import org.apache.kafka.clients.producer.RecordMetadata;
//
// public class ProductTest
// {
// public static void main(String[] args)
// {
// new Thread(new productor("first")).start();
// // new Thread(new productor("second")).start();
// // new Thread(new productor("third")).start();
// // new Thread(new productor("four")).start();
// // new Thread(new productor("five")).start();
// }
//
// }
//
// class productor implements Runnable
// {
//
// private String top = "";
//
// productor(String top1)
// {
// top = top1;
// }
//
// /*
// * (non Javadoc)
// *
// * @Title: run
// *
// * @Description: TODO
// *
// * @see java.lang.Runnable#run()
// */
// @Override
// public void run()
// {
// Properties props = new Properties();
// // props.put("bootstrap.servers", "10.200.50.132:9092,10.200.50.133:9092");
// props.put("bootstrap.servers", "192.168.41.128:9092");
// props.put("acks", "all");
// props.put("retries", 0);
// props.put("batch.size", 16384);
// props.put("linger.ms", 5);
// props.put("buffer.memory", 33554432);
// props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
// props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
// props.put("compression.type", "lz4");
//
// KafkaProducer<String, String> producer = new KafkaProducer<>(props);
// int j = 0;
// long s = System.currentTimeMillis();
// String realtimedata = top + "(realtimedata|632850901|2018-11-02 00:00:06," + j + ")";
// for (int i = 0; i < 100; i++)
// {
// producer.send(new ProducerRecord<String, String>("test_1", realtimedata), new Callback()
// {
// @Override
// public void onCompletion(RecordMetadata metadata, Exception ex)
// {
// if (ex != null)
// {
// ex.printStackTrace();
// }
// }
// });
// // producer.send(new ProducerRecord<String, String>("test", "652238072|2018-03-09
// //
// 08:34:30.381,72,2018,3,9,8,55,12,5,600,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,81,1500,270,42,0,1106,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,3,111170815,0,2,5187427.000,29562.00,17861.55,17543.93,12.41,36.50,0.00,12874.98,3.88,3692.21,8.04,224.62,0.00,11000.58,9715.25,13993.08,3868.47,5,9.19,9.09,8.85,-39.33,-8.00,0.00,883.32,883.32,883.32,5624.00,170.10,174.33,16.03,16.33,16.35,16.35,0.04,0.00,0.03,0.01,67.50,64.90,66.40,55.70,64.00,59.79,67.95,367.39,362.60,366.10,1050.00,1052.00,1049.00,1145.00,49.97,42.00,0.99,13.50,10.30,3.64,3.24,20.70,21.00,24.39,20.10,20.50,20.00,17.10,16.50,16.20,16.79,17.29,16.70,17.10,16.50,16.20,59.04,59.04,59.15,29.38,29.50,29.48,0.00,0.05,0.00,-0.05,-0.03,-0.07,0.00,0.00,0.00,22113,22113,22113,0.00,713700.00,16.42,708786.93,25.70,32.40,33.79,33.29,34.09,31.10,32.40,34.09,9.60,56.59,50.50,0.00,5.59,565.40,-566.20,0.00,-1.00,0.00,100.00,100.00,100.00,100.00,13.50,10.80,12.30,0.00,0.00,0.00,12.80,25.00,25.79,38.09,35.00,38.09,35.79,37.90,37.29,1171.00,416.60,455.00,38,0.00,0.00,0,0,0,0,12.10,TRUE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,TRUE,TRUE,TRUE,FALSE,FALSE,FALSE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,TRUE,FALSE,FALSE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,FALSE,FALSE,FALSE,TRUE,TRUE,FALSE,TRUE,TRUE,FALSE,TRUE,TRUE,FALSE,TRUE,TRUE,TRUE,FALSE,TRUE,TRUE,TRUE,FALSE,FALSE,TRUE,TRUE,TRUE,TRUE,TRUE,TRUE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,TRUE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,FALSE,0,0,39.33,1443.38,True,False,False,False,False,False,0,0,1,0,2018-03-09
// // 08:55:12,2018-03-05 10:02:00,2018-02-28
// // 19:45:00,1100.00,2.67,2.67,0.00,0.00,5.59,0,0,0,0,0,0,0,1.00,1500,1500,8.80,10.00,16.59,10,0,0.00,0.03,946818,946769,100.00,1500,0.00,39,211,FALSE,FALSE,0,0,4,0,0,128,0,0,3,0,8,0,0.00"));
// j++;
// }
//
// producer.close();
// long e = System.currentTimeMillis();
// System.out.println((e - s) + "ms");
// System.out.println("create data over..." + j);
//
// }
//
// }
