package Server;

import Client.Consumer;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Listener implements Runnable{

    private boolean running = true;
    private final String groupId;
    private final String pingTopic;
    private BlockingQueue<ClientData> clientList;

    public Listener(String groupId, String pingTopic) {
        this.groupId = groupId;
        this.pingTopic = pingTopic;
        this.clientList = new LinkedBlockingDeque<>();  //initialize a queue that will hold all online users inside
    }

    @Override
    public void run() {

        Consumer receiver = new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        KafkaConsumer<String, String> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(pingTopic));

        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                System.out.println("Received ping");
                //update lista cu timestamps aici
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.toString());
                }
                //System.out.println("Queue:");
                //System.out.println(clientList);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        consumer.close();
    }
}
