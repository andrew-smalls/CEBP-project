import Models.Kafka;

import java.net.UnknownHostException;

public class Application {
    public static void main(String[] args) throws UnknownHostException {
        Kafka kafka = new Kafka();
        kafka.run();
    }
}
