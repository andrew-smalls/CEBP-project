package Client;

import Tools.UniqueIdGenerator;
import Vars.ClientStatus;

import Thread.ThreadCompleteListener;
import Thread.NotifyingThread;
public class Client implements ThreadCompleteListener {

    private final static String id = String.valueOf(UniqueIdGenerator.generateID());
    private static ClientStatus clientStatus = ClientStatus.DEAD;
    private NotifyingThread producerThread, consumerThread;

    public Client()
    {
        clientStatus = ClientStatus.ALIVE;
    }

    public void startCommunication()
    {
        startProducerThread();
        startConsumerThread();
        startPingThread();
    }

    public void startPingThread()
    {

    }

    public void startProducerThread(){
        producerThread = new ProducerCommunication(id, "Producer");
        producerThread.addListener(this);
        System.out.println("Starting producer");
        producerThread.start();
    }

    public void startConsumerThread()
    {
        consumerThread = new ConsumerCommunication(id, "Consumer");
        consumerThread.addListener(this);
        System.out.println("Starting consumer");
        consumerThread.start();
    }

    public ClientStatus getClientStatus()
    {
        return this.clientStatus;
    }


    @Override
    public void notifyOfThreadComplete(Thread thread) {
        System.out.println(thread.getName() + " pinged, it ended");
        if(thread.getName().equals("Producer") || thread.getName().equals("Consumer"))
        {
            consumerThread.stopConsumer();
            this.clientStatus = ClientStatus.DEAD;
            System.out.println("Status updated");
        }
    }

    public String getClientId()
    {
        return this.id;
    }

}
