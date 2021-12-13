package Server;

import Thread.NotifyingThread;
import Client.Consumer;
import Client.Message;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;

public class TopicRequestListener extends NotifyingThread implements Runnable{
    private final String requestsTopic="topic_requests";
    private String groupId;
    Consumer receiver;
    KafkaConsumer<String, Message> consumer;

    public TopicRequestListener(String groupId){
        this.groupId=groupId;
        receiver=new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        consumer=receiver.getConsumer();
    }

    @Override
    public void doRun() {
            consumer.subscribe(Arrays.asList(requestsTopic));
            while (running)
            {

                ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
                if(records.count() > 0) {
                    // System.out.println("Parsing records for consumer. Nr of records: " + records.count());

                    for (ConsumerRecord<String, Message> record : records)
                        System.out.print(record.value());
                }
            }
            consumer.close();
    }
}
