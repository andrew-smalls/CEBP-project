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
    private String username;
    private KafkaProducer<String, Message> producer;

    private String topicName;

    Producer sender;
    public ProducerCommunication(String id, String username, String threadName, String topicName){
        this.id = id;
        this.username = username;
        this.setName(threadName);
        //System.out.println("Setting name to producer: " + threadName);
        this.topicName = topicName;
        sender = new Producer(String.valueOf(ServerAddress.LOCALHOST.getAddress()));
        producer = sender.getProducer();
    }

    @Override
    public void doRun() {
        Message message = new Message();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.println("\n" +
                        "ME:"
                );
                message.setUsername(username);
                message.setContent(br.readLine());
                if(message.getContent().equals("exit")){
                    break;
                }
                producer.send(Producer.getRecord(topicName,"1", message));
            }


        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException  e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            System.out.println("Serious exception encountered, closing producer");
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
