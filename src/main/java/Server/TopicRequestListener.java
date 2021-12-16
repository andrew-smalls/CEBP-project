package Server;

import Client.MessageType;
import Client.Producer;
import Thread.NotifyingThread;
import Client.Consumer;
import Client.Message;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.*;

public class TopicRequestListener extends NotifyingThread implements Runnable{
    private final String requestsTopic="topic_requests";
    private String groupId;
    Consumer receiver;
    Producer sender;
    private BlockingQueue<ClientData> clientsList;
    KafkaConsumer<String, Message> consumer;
    KafkaProducer<String,Message> producer;
    private final ExecutorService executorService=Executors.newFixedThreadPool(4);


    public TopicRequestListener(String groupId, BlockingQueue<ClientData> clientsList){
        this.groupId=groupId;
        receiver = new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        consumer = receiver.getConsumer();

        sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
        producer = sender.getProducer();

        this.clientsList=clientsList;
    }

    @Override
    public void doRun() {
            consumer.subscribe(Arrays.asList(requestsTopic));

            while (running)
            {

                ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
                if(records.count() > 0) {
                    // System.out.println("Parsing records for consumer. Nr of records: " + records.count());

                    for (ConsumerRecord<String, Message> record : records) {
                        System.out.print(record.value());
                        // get the message content
                        if(record.value().getType().equals(MessageType.TOPIC_REQUEST_MESSAGE)){
                            TopicRequestHandler handler=new TopicRequestHandler(record.value().getUsername(),
                                    record.value().getContent(),
                                    clientsList,
                                    producer,
                                    sender);
                            executorService.execute(handler);
                        }
                    }
                }
            }

            producer.close();
            consumer.close();
    }

    public void stopRequestListener()
    {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        this.running = false;
    }

}
