package Kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Communication {

    public static void main(String[] args) throws ClassNotFoundException {
        String topic = "TestTopic";
        String message = "Mesaj de test";

        String bootstrapServers_sender = "localhost:9092";

        Producer sender = new Producer(bootstrapServers_sender);
        KafkaProducer<String, String> producer = sender.getProducer();
        ProducerRecord<String, String> record = sender.getRecord(topic, message, "1");

        sender.sendMessage(producer, record);

        String bootstrapServer_receiver = "localhost:2181";
        String groupId = "CEBP_app";

        Consumer receiver = new Consumer(bootstrapServer_receiver, groupId);
        KafkaConsumer<String, String> consumer = receiver.getConsumer();
        receiver.subscribe(consumer, topic);
        receiver.receiveMessage(consumer);
    }
}
