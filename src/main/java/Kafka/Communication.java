package Kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Communication {

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {
        String topic = "TestTopic";
        String message = "Mesaj de test";

        String bootstrapServers_sender = "localhost:9092";

        Producer sender = new Producer(bootstrapServers_sender);
        KafkaProducer<String, String> producer = sender.getProducer();
        ProducerRecord<String, String> record = sender.getRecord(topic, message, "1");

        for(int i=0;i<100;i++) {
            sender.sendMessage(producer, record);
            Thread.sleep(1000);
        }

        producer.close();
    }
}
