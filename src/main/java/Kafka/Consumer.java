package Kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws ClassNotFoundException {
        Logger logger = LoggerFactory.getLogger(Class.forName(Consumer.class.getName()));
        String bootstrapServers = "localhost:2181";
        String groupId = "CEBP_app";
        String topic = "First topic";

        //Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Arrays.asList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("Key: " + record.key() + ", Value:" + record.value());
                logger.info("Partition:" + record.partition() + ",Offset:" + record.offset());
            }

        }
    }
}
