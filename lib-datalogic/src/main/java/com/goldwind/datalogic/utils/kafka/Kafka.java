package com.goldwind.datalogic.utils.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.goldwind.dataaccess.Log;



public class Kafka
{
    /**
     * 错误日志
     */
    private static Log LOGGER = Log.getLog(Kafka.class);
    Properties props = new Properties();
    boolean flg = true;

    private String topic;

    KafkaProducer<String, String> producer = null;

    // Producer<String, String> produce = null;

    public Kafka(String servers, String serializer, String compressionType, String topic, String rootpath)
    {
        System.setProperty("java.security.auth.login.config", rootpath);
        this.topic = topic;
        props.put("bootstrap.servers", servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 163840);
        props.put("linger.ms", 3);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("compression.type", compressionType);
        // // 最长响应时间
        // props.put("max.block.ms", 5000);
        // // 等待响应时长
        // props.put("request.timeout.ms", 1000);
        // props.put("serializer.class", "kafka.serializer.DefaultEncoder");
        // props.put("producer.type", "async");
        // props.put("broker.list", servers);

        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");

    }

    public Kafka(String servers, String serializer, String compressionType, String topic)
    {
        this.topic = topic;
        props.put("bootstrap.servers", servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 163840);
        props.put("linger.ms", 3);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", serializer);
        props.put("value.serializer", serializer);
        props.put("compression.type", compressionType);

        // sasl.jaas.config = null

        // // 最长响应时间
        // props.put("max.block.ms", 5000);
        // // 等待响应时长
        // props.put("request.timeout.ms", 1000);
        // props.put("serializer.class", "kafka.serializer.DefaultEncoder");
        // props.put("producer.type", "async");
        // props.put("broker.list", servers);
    }

    /**
     * Kafka的生产者
     * 
     * @param dataList
     *            数据的list
     * @param sync
     *            发送模式
     */
    public boolean KafkaProducer(List<String> dataList, boolean sync)
    {
        if (producer == null)
        {
            producer = new KafkaProducer<>(props);
        }
        try
        {
            for (String data : dataList)
            {
                // 创建发送实例
                ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, data);
                // 判断同步还是异步发送，同步发送能保证数据一定发送到了kafka，但是速度会慢；异步保证速率，但是不能保证数据的完整性
                if (sync)
                {
                    producer.send(record).get();
                }
                else
                {
                    producer.send(record, new Callback()
                    {
                        public void onCompletion(RecordMetadata metadata, Exception e)
                        {
                            // System.out.println("offset:" + metadata.offset() + "\npartition:" + metadata.partition() + "\ntopic:" + metadata.topic() + "\nserializedKeySize:"
                            // + metadata.serializedKeySize() + "\nserializedValueSize:" + metadata.serializedValueSize() + "\n");
                            if (e != null)
                            {
                                LOGGER.error("kafka_send" + e.getClass().getName(), e);
                                flg = false;
                                producer.close(1, TimeUnit.SECONDS);
                                producer = null;
                            }
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("kafka_send" + e.getClass().getName(), e);
            // 当出现异常再关闭kafka连接，减少连接消耗的性能
            flg = false;
            producer.close(1, TimeUnit.SECONDS);
            producer = null;
        }
        return flg;
    }

    /**
     * Kafka的消费者
     * 
     * @param servers
     *            服务器
     * @param serializer
     *            序列化
     * @param timeout
     *            超时时间
     * @param topic
     *            topic名称
     * @return 数据的list
     */
    public List<String> KafkaConsumer(String servers, String serializer, String timeout, String topic)
    {
        List<String> outList = new ArrayList<String>();
        // Properties props = new Properties();
        // props.put("bootstrap.servers", servers);
        // props.put("group.id", "localTest");
        // props.put("enable.auto.commit", "true");
        // props.put("auto.commit.interval.ms", "1000");
        // props.put("auto.offset.reset", "earliest");
        // props.put("session.timeout.ms", "30000");
        // props.put("max.poll.records", "10");
        // props.put("key.deserializer", serializer);
        // props.put("value.deserializer", serializer);
        // KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // consumer.subscribe(Arrays.asList(topic));
        // try
        // {
        // while (true)
        // {
        // ConsumerRecords<String, String> records = consumer.poll(5000);
        // if (records == null || records.isEmpty())
        // {
        // break;
        //
        // }
        // for (ConsumerRecord<String, String> record : records)
        // {
        // // System.out.printf("offset = %d, key = %s, value = %s \r\n", record.offset(), record.key(), record.value());
        // outList.add(record.value());
        // }
        // try
        // {
        // Thread.sleep(100);
        // }
        // catch (Exception e)
        // {
        // LOGGER.error(e);
        // }
        // }
        // }
        // finally
        // {
        // consumer.close();
        // }
        return outList;
    }

    /**
     * kafkaclose
     */
    public void close()
    {
        // 当出现异常再关闭kafka连接，减少连接消耗的性能
        if (producer != null)
        {
            producer.close(1, TimeUnit.SECONDS);
        }
        producer = null;
    }

//    @Test
//    public void KafkaProducerTest()
//    {
//        String servers = "10.200.50.132:9092,10.200.50.133:9092";
//        String serializer = "org.apache.kafka.common.serialization.StringSerializer";
//        String compressionType = "lz4";
//        String topic = "soam_realtime";
//        List<String> dataList = new ArrayList<>();
//        String s = "(tendata|500002125|18.46,9.62,16.05,13.36,5.33,8.38,1.63,22.21,6.7,4.76,2.28,12.78,26.32,0.91,10.27,27.22,8.25,15.49,22.21,7.73,14.98,26.7,16.38,29.28,21.42,14.47,0.01,8.35,21.44,16.41,26.05,20.22,17.87,11.76,14.63,7.43,22.4,14.86,6.68,14.79,27.56,16.03,5.0,23.35,20.74,22.33,11.75,15.34,7.47,23.36,1.66,25.82,1.13,9.23,5.16,13.64,15.34,27.08,28.17,24.64,26.72,11.42,2.29,13.72,17.44,19.82,22.91,9.73,19.48,11.07,12.24,7.74)";
//        for (int i = 0; i < 1000; i++)
//        {
//            dataList.add(s);
//        }
//        long start = System.currentTimeMillis();
//        // boolean b = KafkaProducer(servers, serializer, compressionType, topic, dataList);
//        boolean b = KafkaProducer(dataList, false);
//        long end = System.currentTimeMillis();
//        System.out.println("allDiffTime:" + (end - start));
//        Assert.assertEquals(true, b);
//    }
}
