package Client;

import Tools.UniqueIdGenerator;

public class Client {

    private final static String id = String.valueOf(UniqueIdGenerator.generateID());

    public Client()
    {
    }

    public void startPingThread()
    {

    }

    public void startProducerThread(){
        Thread producerThread = new Thread(new ProducerCommunication(id), "Producer");
        producerThread.start();
    }

    public void startConsumerThread()
    {
        ConsumerCommunication consumer = new ConsumerCommunication(id);
        Thread consumerThread = new Thread(consumer, "Consumer");
        consumerThread.start();
    }

}
