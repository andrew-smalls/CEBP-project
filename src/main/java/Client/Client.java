package Client;

import Tools.UniqueIdGenerator;
import Vars.ClientStatus;

import Thread.ThreadCompleteListener;
import Thread.NotifyingThread;
public class Client implements ThreadCompleteListener {

    private final static String id = String.valueOf(UniqueIdGenerator.generateID());
    private static ClientStatus clientStatus = ClientStatus.DEAD;
    private NotifyingThread producerThread, consumerThread;
    private PingSender pingSender;
    private String pingTopic="client_pings_topic";
    private String username;
    public Client(String username)
    {
        pingSender=new PingSender();
        clientStatus = ClientStatus.ALIVE;
        this.username = username;
    }

    public void startCommunication() throws InterruptedException {
        startProducerThread();
        startConsumerThread();

    }

    public void startPingThread()
    {
        Message message = new Message();
        message.setUsername(username);
        message.setType(MessageType.PING_MESSAGE);
        message.setContent(String.valueOf(System.currentTimeMillis()));
        pingSender.pingServer(pingTopic,message);
    }

    public void stopPingThread() throws InterruptedException {
        System.out.println("Signal ping to stop now");
        pingSender.cancelPings();
    }

    public void startProducerThread(){
        producerThread = new ProducerCommunication(id, username, "Producer");
        producerThread.addListener(this);
        System.out.println("Starting producer");
        producerThread.start();
    }

    public void startConsumerThread() throws InterruptedException {
        consumerThread = new ConsumerCommunication(id, "Consumer");
        consumerThread.addListener(this);
        System.out.println("Starting consumer");
        consumerThread.start();
        consumerThread.join();
    }

    public ClientStatus getClientStatus()
    {
        return this.clientStatus;
    }


    @Override
    public void notifyOfThreadComplete(Thread thread) throws InterruptedException {
        System.out.println(thread.getName() + " pinged, it ended");
        if(thread.getName().equals("Producer"))
        {
            consumerThread.stopConsumer();
        }
        else if (thread.getName().equals("Consumer"))
        {
            this.clientStatus = ClientStatus.DEAD;
            System.out.println("Status updated");
            System.out.println("Client status (client): " + this.getClientStatus());
        }
    }

    public String getClientId()
    {
        return this.id;
    }

}
