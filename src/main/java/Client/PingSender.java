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
    Runnable ping;
    public void pingServer(String topic,String message){
         ping=new Runnable() {
            @Override
            public void run() {
                Producer sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
                KafkaProducer<String, String> producer = sender.getProducer();
                producer.send(sender.getRecord(topic,"1", message));
                System.out.println("Sent ping\n");
            }
        };
        pingHandler=scheduler.scheduleAtFixedRate(ping,100,500, TimeUnit.MILLISECONDS);

    }

    public void cancelPings() throws InterruptedException {
        pingHandler.cancel(true);
        scheduler.shutdown();
        System.out.println("Scheduler is shutdown: "+scheduler.isShutdown()+"\n");

    }
}
