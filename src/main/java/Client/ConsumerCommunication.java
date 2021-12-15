package Client;

import Kafka.CustomTopic;
import Vars.ServerAddress;
import Thread.NotifyingThread;
import org.apache.kafka.clients.admin.AdminClientConfig;
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
        this.setName(threadName);

        this.topicName = topicName;
    }

    @Override
    public void doRun()
    {
        CustomTopic topic = new CustomTopic(topicName);   //"exampleTopic1" // aici luam din lista de conexiuni, topic-ul corespunzator user-ului respectiv

        topic.CreateTopic();

        Consumer receiver=new Consumer(ServerAddress.LOCALHOST.getAddress(), groupId);
        KafkaConsumer<String, Message> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(topic.getName()));
        System.out.println("Consumer started with group id: " + groupId);
        System.out.println("Communication channel: " + topic.getName());

        while (running)
        {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                for (ConsumerRecord<String, Message> record : records)
                    System.out.print(record.value());
            }
        }

        System.out.println("Closing consumer");
        consumer.close();

    }

}
