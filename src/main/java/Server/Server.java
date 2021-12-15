package Server;

import Thread.ThreadCompleteListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Server implements ThreadCompleteListener {
    private static final String groupId = "server_group";
    private static final String pingTopic="client_pings_topic";
    private static BlockingQueue<ClientData> clientList;
    private Listener listener;
    private Updater updater;
    private TopicRequestListener topicRequestListener;

    public Server(){
        clientList = new LinkedBlockingDeque<>();
    }

    public void startListenerThread(){
        listener = new Listener(groupId, pingTopic, clientList);  //pass queue here and in the updater, needs to be visible to both
        Thread listenerThread = new Thread(listener, "Listener");

        System.out.println("Started listener thread");
        listenerThread.start();
    }

    public void startUpdaterThread() //this should receive a copy of the blockingQueue, in order to keep communication flowing.
    {
        updater = new Updater(clientList);
        Thread updaterThread = new Thread(updater, "Updater");

        System.out.println("Started updater thread");
        updaterThread.start();
    }

    public void startTopicRequestsListener(){
        topicRequestListener=new TopicRequestListener(groupId,clientList);
        topicRequestListener.start();
    }

    public void stopTopicRequestsListener(){
        topicRequestListener.stopRequestListener();
    }

    public void cancelListenerThread() throws InterruptedException {
        listener.cancelTimestamper();
    }

    public void cancelUpdaterThread() {
        updater.cancelUpdater();
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) throws InterruptedException {
        System.out.println(thread.getName() + " pinged, it ended");
        if(thread.getName().equals("Listener") || thread.getName().equals("Updater"))
        {
            //consumerThread.stopConsumer();
        }
    }

    public void getPulse() {
        System.out.println("Server on:" +
                "\nListener thread - " + listener.isAlive() +
                "\nUpdater thread - " + updater.isAlive() +
                "\nThreads active = " + Thread.activeCount());
    }

    public void getConnectedClients() {
        System.out.println("Client list with online clients: ");
        for(ClientData client : clientList)
        {
            if(client.isOnline())
                System.out.println(client.getClientIdentifier());
        }
    }

    public void getListOfClients() {
        System.out.println("Client list with all clients: ");
        for(ClientData client : clientList)
        {
            System.out.println(client.getClientIdentifier());
        }
    }
}
