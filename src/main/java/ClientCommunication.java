import Client.Client;
import Client.ConsumerCommunication;
import Client.ProducerCommunication;
import Tools.UniqueIdGenerator;

import java.io.IOException;

public class ClientCommunication {

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException, IOException {

        Client client = new Client();






        while (true) {
            if (!producerThread.isAlive()) {
                System.out.println("Id: " + id + " producer is not alive, breaking");
                consumer.stopConsumer();
                break;
            }

        }
    }
}
