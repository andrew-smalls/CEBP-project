import Client.Message;
import Client.Producer;
import Server.Server;
import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerCommunication {

    public static void main(String[] args) throws InterruptedException {

        Server server = new Server();

        server.startListenerThread();
        server.startUpdaterThread();

        String answer;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true)
        {
            try{
                answer = br.readLine();
            }catch (IOException e){
                e.printStackTrace();
                answer = "exit";
            }
            if(answer.equals("exit")){
                break;
            }
            else if(answer.equals("info")){
                server.getPulse();
            }
            else if(answer.equals("online")){
                server.getConnectedClients();
            }
            else if(answer.equals("all")){
                server.getListOfClients();
            }
        }

        server.cancelListenerThread();
        server.cancelUpdaterThread();
        KafkaProducer<String, Message> producer = Producer.getProducer(ServerAddress.LOCALHOST.getAddress());
        producer.close();
        Thread.sleep(100);
        System.out.println("Threads active before closing = " + Thread.activeCount());
    }
}
