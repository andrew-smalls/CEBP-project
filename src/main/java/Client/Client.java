package Client;

import Thread.NotifyingThread;
import Thread.ThreadCompleteListener;
import Tools.UniqueIdGenerator;
import Vars.ClientStatus;
import Vars.ServerAddress;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Client implements ThreadCompleteListener {

    private static final String id = String.valueOf(UniqueIdGenerator.generateID());
    private static ClientStatus clientStatus = ClientStatus.DEAD;
    private NotifyingThread producerThread, consumerThread;
    private ResponseListener responseListenerThread;
    private PingSender pingSender;
    private String pingTopic = "client_pings_topic";
    private String username;
    private String requestsTopic;
    private BlockingQueue<Corespondent> connections;
    KafkaProducer<String, Message> producer;
    Producer sender;
    private Consumer receiverNameCheckConsumer;

    public Client() {
        pingSender = new PingSender();
        clientStatus = ClientStatus.ALIVE;
        //this.username = username;
        this.connections = new LinkedBlockingDeque<>();
        requestsTopic = String.valueOf(System.currentTimeMillis());
    }

    public Client(String username) {
        pingSender = new PingSender();
        clientStatus = ClientStatus.ALIVE;
        this.username = username;
        this.connections = new LinkedBlockingDeque<>();
        requestsTopic = String.valueOf(System.currentTimeMillis());
    }

    public void startCommunication(String corespondent) throws InterruptedException {
        String topic = getCorespondingTopic(corespondent);
        if (topic == null) {
            System.out.println("\nYou don't have a connection with this user\n");
        } else {
            startProducerThread(topic);
            startConsumerThread(topic);
        }
    }

    public boolean checkNameFromServer(String username) {
        KafkaProducer<String, Message> producer;
        sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
        producer = sender.getProducer();

        Message message = new Message();
        message.setType(MessageType.NAME_REQUEST);
        message.setUsername(username);
        message.setContent(requestsTopic);
        producer.send(Producer.getRecord("topic_requests", "1", message));
        producer.close();

        KafkaConsumer<String, Message> consumerNameCheck;
        Consumer receiverNameCheck = new Consumer(String.valueOf(ServerAddress.LOCALHOST.getAddress()), id);
        consumerNameCheck = receiverNameCheck.getConsumer();
        boolean result = false;
        consumerNameCheck.subscribe(Arrays.asList(requestsTopic));
        boolean responseReceived = false;
        while (!responseReceived)
        {

            ConsumerRecords<String, Message> records = consumerNameCheck.poll(Duration.ofMillis(100));
            if (records.count() > 0)
            {
                for (ConsumerRecord<String, Message> record : records)
                {
                    if (record.value().getType().equals(MessageType.VALID_NAME)) {
                        result = true;
                        responseReceived = true;
                        break;
                    }
                    else if (record.value().getType().equals(MessageType.INVALID_NAME))
                    {
                        result = false;
                        responseReceived = true;
                        break;
                     }
                }
             }
        }
        consumerNameCheck.close();

        return result;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void startPingThread()
    {
        Message message = new Message();
        message.setUsername(username);
        message.setType(MessageType.PING_MESSAGE);

        message.setContent(requestsTopic);
        pingSender.pingServer(pingTopic, message);
    }

    public void requestTopic(String corespondentName)
    {
        Message message = new Message();
        message.setUsername(username);
        message.setType(MessageType.TOPIC_REQUEST_MESSAGE);
        message.setContent(corespondentName);
        sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
        producer = sender.getProducer();
        producer.send(Producer.getRecord("topic_requests", "1", message));
        //connections.add(new Corespondent(corespondentName, "pending"));
        producer.close();
    }

    public String getCorespondingTopic(String corespondentName)
    {
        Iterator<Corespondent> corespondent = connections.iterator();
        while(corespondent.hasNext())
        {
            Corespondent tempData = corespondent.next();
            //System.out.println("Correspondent " + tempData);
            //System.out.println("corespondentName is" + corespondentName);

            if(tempData.getName().equals(corespondentName)) {
                //System.out.println("Correspondent topic fetched is " + tempData.getTopic());
                return tempData.getTopic();
            }
        }
        return null;
    }

    public void stopPingThread() throws InterruptedException {
        //System.out.println("Signal ping to stop now");
        pingSender.cancelPings();
    }

    public void startProducerThread(String topicName){
        producerThread = new ProducerCommunication(id, username, "Producer", topicName);
        producerThread.addListener(this);
        //System.out.println("Starting producer");
        producerThread.start();
    }

    public void startConsumerThread(String topicName) throws InterruptedException {
        consumerThread = new ConsumerCommunication(id, "Consumer", topicName);
        consumerThread.addListener(this);
        //System.out.println("Starting consumer");
        consumerThread.start();
        consumerThread.join();
    }

    public void startResponseListenerThread() {
        responseListenerThread = new ResponseListener(id, connections, requestsTopic);
        responseListenerThread.addListener(this);
        responseListenerThread.start();
    }

    public void stopResponseListenerThread() throws InterruptedException {
        responseListenerThread.cancelUpdater();
    }

    public ClientStatus getClientStatus()
    {
        return clientStatus;
    }


    @Override
    public void notifyOfThreadComplete(Thread thread) throws InterruptedException {
        //System.out.println(thread.getName() + " pinged, it ended");
        if(thread.getName().equals("Producer"))
        {
            consumerThread.stopConsumer();
        }
        else if (thread.getName().equals("Consumer"))
        {
            this.clientStatus = ClientStatus.DEAD;
            //System.out.println("Status updated");
            //System.out.println("Client status (client): " + this.getClientStatus());
        }
    }

    public void showActiveConnections()
    {
        Iterator<Corespondent> corespondent = connections.iterator();
        //System.out.println("inside show active connections");
        int i = 0;

        while(corespondent.hasNext())
        {
            try {
                Corespondent tempData = corespondent.next();
                System.out.println("Correspondent " + i++ + ": " + tempData.getName() + ", status: " + tempData.getStatus());
            }catch(Exception e)
            {
                System.out.println("This user disconnected");
            }
        }
    }

    public String getClientId()
    {
        return this.id;
    }

}
