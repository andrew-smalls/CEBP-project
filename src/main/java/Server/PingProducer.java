package Server;

import Client.Producer;
import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;

public class PingProducer implements Runnable{
    private static final String topic = "client_pings_topic";


    @Override
    public void run() {

        Producer sender = new Producer(String.valueOf(ServerAddress.LOCALHOST));
        KafkaProducer<String, String> producer = sender.getProducer();

        producer.initTransactions();
    }
}
