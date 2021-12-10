package Client;

import Vars.ServerAddress;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import Thread.NotifyingThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProducerCommunication extends NotifyingThread implements Runnable{

    private String id;

    public ProducerCommunication(String id, String threadName){
        this.id = id;
        this.setName(threadName);
    }

    @Override
    public void doRun() {
        String topic = "TwoConsumers";
        String message = "";
        Producer sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));  //Currently, producers are ID'd using an id generator
        KafkaProducer<String, String> producer = sender.getProducer();


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.println("\n" +
                        "ME:"
                );
                message= br.readLine();
                if(message.equals("exit")){
                    break;
                }
                producer.send(sender.getRecord(topic,"1", message));
            }


        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException  e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            System.out.println("Serious exception encountered, closing producer");
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        } catch (IOException  e) {
            e.printStackTrace();
        }
        System.out.println("Closing producer");
        producer.close();
    }
}
