package Server;

import Client.Consumer;
import Client.Message;
import Client.MessageType;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.*;

public class Listener implements Runnable{

    private boolean running = true;
    private final String groupId;
    private final String pingTopic;
    private BlockingQueue<ClientData> clientList;

    private Runnable timestamper;
    private final ExecutorService executorService = Executors.newFixedThreadPool(16);

    public Listener(String groupId, String pingTopic, BlockingQueue<ClientData> clientList) {
        this.groupId = groupId;
        this.pingTopic = pingTopic;
        this.clientList = clientList;  //initialize a queue that will hold all online users inside
    }

    @Override
    public void run() {

        Consumer receiver = new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        KafkaConsumer<String, Message> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(pingTopic));

        while (running) {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100)); //Duration.ofMillis(100) inainte
            if(records.count() > 0) {

                System.out.println("Received ping");

                //spawn a new thread here that will actually update the queue?
                for (ConsumerRecord<String, Message> record : records) {
                    if(record.value().getType().toString().equals(MessageType.PING_MESSAGE.getType())) //check if the record is actually a ping
                    {
                        System.out.println("Received ping from "  + record.value().getUsername());

                        timestamper = new Runnable()
                        {
                            @Override
                            public void run() {
                                System.out.println("Started new thread to update timestamp fir "  + record.value().getUsername());

                                String clientIdentifier = record.value().getUsername();
                                String timestamp = String.valueOf(System.currentTimeMillis());
                                ClientData clientToAdd = new ClientData(clientIdentifier, timestamp);

                                try {

                                    if (clientList.contains(clientToAdd)) {
                                        clientList.remove(clientToAdd); //remove old timestamp of client, if it exists
                                    }
                                    clientList.put(clientToAdd);        //insert updated timestamp
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                /*
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                 */

                            }
                        };
                        Future resultTimestamper = executorService.submit(timestamper);

                    }

                }

            }



        }
        consumer.close();
    }
    public void cancelTimestamper() throws InterruptedException {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }

    }

}
