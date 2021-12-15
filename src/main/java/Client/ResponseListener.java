package Client;
import Server.TopicRequestHandler;
import Thread.NotifyingThread;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.*;

public class ResponseListener extends NotifyingThread implements Runnable{

    private BlockingQueue<Corespondent> connections;
    private Consumer receiver;
    private KafkaConsumer<String, Message> consumer;
    private String requestsTopic;
    private Runnable updater;
    private ExecutorService executorService= Executors.newFixedThreadPool(2);

    public ResponseListener(String groupId, BlockingQueue<Corespondent> connections, String requestsTopic) {
        this.connections = connections;
        receiver=new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        consumer=receiver.getConsumer();
        this.requestsTopic = requestsTopic;
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

                    String[] content = record.value().getContent().split(",");
                    String name = content[0];
                    String topic = content[1];
                    updater = new Runnable() {
                        @Override
                        public void run() {
                            Iterator<Corespondent> iterator = connections.iterator();
                            Corespondent corespondent = new Corespondent(name, "connected");
                            if (connections.contains(corespondent))
                            {
                                corespondent.setStatus("connected");
                            }
                            try {
                                connections.put(corespondent);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    executorService.submit(updater);
                    // get the message content
                }
            }
        }
        consumer.close();
    }

    public void cancelUpdater() throws InterruptedException {
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
