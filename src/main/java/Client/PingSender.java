package Client;

import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.concurrent.*;

class DaemonFactory implements ThreadFactory
{
    @Override
    public Thread newThread(Runnable r)
    {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    }
}

public class PingSender {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DaemonFactory());
    private ScheduledFuture<?> pingHandler;
    private static int i;
    Runnable ping;
    public void pingServer(String topic, Message message){

        //message = message + " " + i++;
         ping=new Runnable() {
            @Override
            public void run() {
                i++;
                Producer sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
                KafkaProducer<String, Message> producer = sender.getProducer();
                producer.send(sender.getRecord(topic,"1", message));

                //System.out.println("Sent ping, " + i + "\n");
                //System.out.println("Sent ping\n");
            }
            public void cancel()
            {

            }


         };
        pingHandler = scheduler.scheduleAtFixedRate(ping,100,500, TimeUnit.MILLISECONDS);
        //scheduler.shutdown();

    }

    public void cancelPings() throws InterruptedException {
        pingHandler.cancel(true);
        scheduler.shutdown();
        //System.out.println("Scheduler is shutdown: "+scheduler.isShutdown()+"\n");
    }
}
