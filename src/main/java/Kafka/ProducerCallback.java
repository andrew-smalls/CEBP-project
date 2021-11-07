package Kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static Kafka.Producer.createProperties;

public class ProducerCallback {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        final Logger logger = LoggerFactory.getLogger(ProducerCallback.class);

        String bootstrapServers = "localhost:9092";
        Properties properties = createProperties(bootstrapServers);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 10; i++) {
            String topic = "First topic";
            String value = "OneTwo" + Integer.toString(i);
            String key = "id_" + Integer.toString(i);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, value);

            logger.info("key" + key);


            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                    if (e == null) {
                        logger.info("Successfully received the details as: \n" +
                                "Topic:" + recordMetadata.topic() + "\n" +
                                "Partition:" + recordMetadata.partition() + "\n" +
                                "Offset" + recordMetadata.offset() + "\n" +
                                "Timestamp" + recordMetadata.timestamp());
                    } else {
                        logger.error("Can't produce,getting error", e);

                    }
                }
            }).get();
        }
        producer.flush();
        producer.close();
    }
}
