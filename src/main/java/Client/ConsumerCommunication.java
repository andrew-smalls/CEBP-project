package Client;

import Vars.ServerAddress;
import Thread.NotifyingThread;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;

public class ConsumerCommunication extends NotifyingThread implements Runnable {

    private final String groupId;

    public ConsumerCommunication(String groupId, String threadName){
        this.groupId=groupId;
        this.setName(threadName);
    }

    @Override
    public void doRun() { //this method will call run() and then will call notifyListeners()
        String topic = "TwoConsumers";

        Consumer receiver=new Consumer(ServerAddress.LOCALHOST.getAddress(), groupId);
        KafkaConsumer<String, String> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("Consumer started with group id: "+groupId);

        while (running)
        {

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {
                System.out.println("Parsing records for consumer. Nr of records: " + records.count());

                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("FRIEND: %s\n", record.value());
            }
        }

        System.out.println("Closing consumer");
        consumer.close();

    }

}
