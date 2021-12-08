package Client;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    private Properties properties;

    public Producer(String bootstrapServers,String transactionId)
    {
        properties = createProperties(bootstrapServers,transactionId);
    }

    public Producer(String bootstrapServers)
    {
        properties = createProperties(bootstrapServers);
    }

    public KafkaProducer<String, String> getProducer()
    {
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
    }

    //If no partition is specified but a key is present a partition will be chosen using a hash of the key.
    // If neither key nor partition is present a partition will be assigned in a round-robin fashion.
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


    public Properties createProperties(String bootstrapServers,String transactionId)
    {
        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers);//Assign localhost id

        props.put("transactional.id", transactionId);

        props.put("acks", "all"); //Set acknowledgements for producer requests.
        props.put("retries", 1); //If the request fails, the producer can automatically retry,
        props.put("batch.size", 16384); //Specify buffer size in config
        props.put("linger.ms", 1); //Reduce the no of requests less than 0
        props.put("buffer.memory", 33554432); //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }

    public Properties createProperties(String bootstrapServers)
    {
        Properties props = new Properties();

        props.put("bootstrap.servers", bootstrapServers); //Assign localhost id
        props.put("acks", "all"); //Set acknowledgements for producer requests.
        props.put("retries", 1); //If the request fails, the producer can automatically retry,
        props.put("batch.size", 16384); //Specify buffer size in config
        props.put("linger.ms", 1); //Reduce the no of requests less than 0
        props.put("buffer.memory", 33554432); //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        return props;
    }
}
