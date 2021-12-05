package Kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;

public class ConsumerCommunication implements Runnable{

    private boolean running=true;

    @Override
    public void run() {
        String topic = "TestTopic";
        String bootstrapServers_sender = "localhost:9092";

        System.out.println("Consumer started");

        Consumer receiver=new Consumer(bootstrapServers_sender,"test");
        KafkaConsumer<String, String> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(topic));
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                System.out.println("Parsing records for consumer. Nr of records: " + records.count());

                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("FRIEND: %s\n", record.value());
            }
        }
        consumer.close(); //added
    }

    public void stopConsumer(){
        running=false;
    }
}
