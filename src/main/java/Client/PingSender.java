package Client;

import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PingSender {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> pingHandler;
    public void pingServer(String topic,String message){
        final Runnable ping=new Runnable() {
            @Override
            public void run() {
                Producer sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));  //Currently, producers are ID'd using an id generator
                KafkaProducer<String, String> producer = sender.getProducer();
                producer.send(sender.getRecord(topic,"1", message));
            }
        };
        pingHandler=scheduler.scheduleAtFixedRate(ping,0,500, TimeUnit.MILLISECONDS);
    }

    public void cancelPings(){
        pingHandler.cancel(true);
    }
}
