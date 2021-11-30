package Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    private Properties properties;

    public Producer(String bootstrapServers)
    {
        properties = createProperties(bootstrapServers);
    }

    public KafkaProducer<String, String> getProducer()
    {
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
    }

    public ProducerRecord<String, String> getRecord(String topic,  String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, message);
        return record;
    }

    public void sendMessage(KafkaProducer<String, String> producer, ProducerRecord<String, String> record)
    {
        producer.send(record);
        producer.flush();
        producer.close();
    }


    public Properties createProperties(String bootstrapServers)
    {
        Properties properties = new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-trx-id123");


        return properties;
    }
}
