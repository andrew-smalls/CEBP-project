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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Listener implements Runnable{

    private boolean running = true;
    private final String groupId;
    private final String pingTopic;
    private BlockingQueue<ClientData> clientList;  //it's not shared for now

    public Listener(String groupId, String pingTopic, BlockingQueue<ClientData> clientList) {
        this.groupId = groupId;
        this.pingTopic = pingTopic;
        this.clientList = clientList; //new LinkedBlockingDeque<>();  //initialize a queue that will hold all online users inside
    }

    @Override
    public void run() {

        Consumer receiver = new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), groupId);
        KafkaConsumer<String, Message> consumer = receiver.getConsumer();
        consumer.subscribe(Arrays.asList(pingTopic));

        while (running) {
            ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
            if(records.count() > 0) {

                System.out.println("Received ping");

                //spawn a new thread here that will actually update the queue?

                //update lista cu timestamps aici
                //System.out.println("Records count: " + records.count());
                for (ConsumerRecord<String, Message> record : records) {
                    if(record.value().getType().toString().equals(MessageType.PING_MESSAGE.getType())) //check if the record is actually a ping
                    {
                        System.out.println("Received ping from "  + record.value().getUsername());
                        String clientIdentifier = record.value().getUsername();
                        String timestamp = String.valueOf(record.timestamp());
                        ClientData clientToAdd = new ClientData(clientIdentifier, timestamp);

                        try {
                            if(!clientList.contains(clientToAdd)) {
                                clientList.put(clientToAdd);
                            }
                            else
                            {
                                clientList.remove(clientToAdd); //remove old timestamp of client
                                clientList.put(clientToAdd);    //insert updated timestamp
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                System.out.println("Queue: " + clientList);

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
