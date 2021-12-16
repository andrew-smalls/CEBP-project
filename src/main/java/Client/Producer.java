package Client;

import Kafka.JsonSerializer;
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

    public KafkaProducer<String, Message> getProducer()
    {
        KafkaProducer<String, Message> producer = new KafkaProducer<>(properties);
        return producer;
    }

    //If no partition is specified but a key is present a partition will be chosen using a hash of the key.
    // If neither key nor partition is present a partition will be assigned in a round-robin fashion.
    public static ProducerRecord<String, Message> getRecord(String topic,  String key, Message message) {
        ProducerRecord<String, Message> record = new ProducerRecord<>(topic, key, message);
        return record;
    }

    public void sendMessage(KafkaProducer<String, String> producer, ProducerRecord<String, String> record)
    {
        producer.send(record);
        producer.flush();
    }

    public static Properties createProperties(String bootstrapServers)
    {
        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers); //Assign localhost id
        props.put("acks", "all"); //Set acknowledgements for producer requests.
        props.put("retries", 1); //If the request fails, the producer can automatically retry,
        props.put("batch.size", 16384); //Specify buffer size in config
        props.put("linger.ms", 1); //Reduce the no of requests less than 0
        props.put("buffer.memory", 33554432); //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        props.put("offsets.retention.minutes", 1); //sets retention to 24 hours
        return props;
    }
}
