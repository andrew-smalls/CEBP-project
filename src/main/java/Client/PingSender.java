package Client;

import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.concurrent.*;

public class PingSender {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pingHandler;
    Runnable ping;
    KafkaProducer<String, Message> producer;

    public PingSender() {
        producer = Producer.getProducer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
    }

    public void pingServer(String topic, Message message){

         ping=new Runnable() {
            @Override
            public void run() {
                producer.send(Producer.getRecord(topic, "1", message));
            }
         };
        pingHandler = scheduler.scheduleAtFixedRate(ping,100,500, TimeUnit.MILLISECONDS);

    }

    public void cancelPings() throws InterruptedException {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }

    }
}
