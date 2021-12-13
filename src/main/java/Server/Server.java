package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Server {
    private static final String groupId = "server_group";
    private static final String pingTopic="client_pings_topic";
    private static BlockingQueue<ClientData> clientList;
    private static BlockingQueue<Thread> tasksQueue;
    private ExecutorService  executor;

    public Server(){
        clientList = new LinkedBlockingDeque<>();
        tasksQueue = new LinkedBlockingDeque<>();
        executor = Executors.newFixedThreadPool(2);
    }

    public void startListenerThread(){
        Listener listener = new Listener(groupId, pingTopic, clientList);  //pass queue here and in the updater, needs to be visible to both
        Thread listenerThread = new Thread(listener, "Listener");

        System.out.println("Started listener thread");
        listenerThread.start();
    }

    public void startUpdaterThread() //this should receive a copy of the blockingQueue, in order to keep communication flowing.
    {
        Updater updater = new Updater(clientList);
        Thread updaterThread = new Thread(updater, "Updater");

        System.out.println("Started updater thread");
        updaterThread.start();
    }

    public void startTopicRequestsListener(){
        TopicRequestListener topicRequestListener=new TopicRequestListener(groupId);
    }

    public void parallelism() throws InterruptedException {
        Listener listener = new Listener(groupId, pingTopic, clientList);  //pass queue here and in the updater, needs to be visible to both
        Thread listenerThread = new Thread(listener, "Listener");

        Updater updater = new Updater(clientList);
        Thread updaterThread = new Thread(updater, "Updater");

        tasksQueue.add(listenerThread);
        tasksQueue.add(updaterThread);

        while(tasksQueue.iterator().hasNext())
        {
            Thread task = tasksQueue.take();
            Future future = executor.submit(task);
            if(future.isDone())
            {
                tasksQueue.offer(task);
            }
            System.out.println("Executed task: " + task.getName());
            Thread.sleep(1000);
            //executor.submit(listenerThread);
            //executor.submit(updaterThread);
        }


    }

    public void cancelParalellism() throws InterruptedException {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
