package Client;

import Tools.UniqueIdGenerator;
import Vars.ClientStatus;

import Thread.ThreadCompleteListener;
import Thread.NotifyingThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client implements ThreadCompleteListener {

    private final static String id = String.valueOf(UniqueIdGenerator.generateID());
    private static ClientStatus clientStatus = ClientStatus.DEAD;
    private NotifyingThread producerThread, consumerThread;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client() throws IOException {
        System.out.println("Please enter your username: ");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        this.username = input.readLine();
        clientStatus = ClientStatus.ALIVE;
    }

    public void startCommunication() throws InterruptedException {
        startPingThread();
        startProducerThread();
        startConsumerThread();

    }

    public void startPingThread()
    {

    }

    public void startProducerThread(){
        producerThread = new ProducerCommunication(id, username,"Producer");
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
