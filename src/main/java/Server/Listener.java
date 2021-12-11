package Server;

import Client.Consumer;
import Client.Message;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;

public class Listener implements Runnable{

    private boolean running = true;
    private final String groupId;
    private final String pings_topic;


    public Listener(String groupId, String pings_topic) {
        this.groupId = groupId;
        this.pings_topic = pings_topic;
    }

    @Override
    public void run() {
        String bootstrapServers_sender = "localhost:9092";

        Consumer receiver = new Consumer(bootstrapServers_sender, groupId);
        KafkaConsumer<String, Message> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(pings_topic));

        while (running) {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                System.out.println("Received ping");
            }
        }
        consumer.close();
    }
}
