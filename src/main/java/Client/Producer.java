package Client;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    private Properties properties;

    public Producer(String bootstrapServers,String transactionId)
    {
        properties = createProperties(bootstrapServers,transactionId);
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


    public Properties createProperties(String bootstrapServers,String transactionId)
    {
        Properties properties = new Properties();
        /*
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
        */


        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", bootstrapServers);

        props.put("transactional.id", transactionId);
        //props.put("client.id", transactionId);

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 1);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //return properties;
        return props;
    }
}
