package Models;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Kafka {

    public void run() throws UnknownHostException {
        Properties config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("bootstrap.servers", "host1:9092,host2:9092");
        config.put("acks", "all");
        new KafkaProducer<Client, Message>(config);
    }
}
