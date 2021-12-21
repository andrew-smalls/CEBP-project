package Client;

import Thread.NotifyingThread;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;

public class ConsumerCommunication extends NotifyingThread implements Runnable {

    private final String groupId;
    private String topicName;

    public ConsumerCommunication(String groupId, String threadName, String topicName){
        this.groupId=groupId;
        //System.out.println("Setting name to consumer: " + threadName);
        this.setName(threadName);

        this.topicName = topicName;
    }

    @Override
    public void doRun()
    {


        Consumer receiver=new Consumer(ServerAddress.LOCALHOST.getAddress(), groupId);
        KafkaConsumer<String, Message> consumer = receiver.getConsumer();
        //System.out.println("Subscribing to topic");
        consumer.subscribe(Arrays.asList(topicName));
        //System.out.println("Consumer started with group id: " + groupId);
        //System.out.println("Communication channel: " + topicName);

        while (running)
        {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                for (ConsumerRecord<String, Message> record : records)
                    System.out.println(record.value());
            }
        }

        //System.out.println("Closing consumer");
        consumer.close();

    }

}
