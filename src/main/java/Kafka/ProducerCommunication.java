package Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProducerCommunication implements Runnable{

    private String id;

    public ProducerCommunication(String id){
        this.id=id;
    }

    @Override
    public void run() {
        String topic = "TestTopic";
        String message = "";

        String bootstrapServers_sender = "localhost:9092";

        Producer sender = new Producer(bootstrapServers_sender,id);
        KafkaProducer<String, String> producer = sender.getProducer();

        producer.initTransactions();
        System.out.println("Transaction initialized, begin transaction");
        try {
            producer.beginTransaction();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inside ProducerCommunication - id: " + id);
            while(true) {
                System.out.println("\n" +
                        "ME:"
                );
                message= br.readLine();
                if(message.equals("exit")){
                    break;
                }
                producer.send(sender.getRecord(topic, "1",message));
            }
            producer.commitTransaction(); //added here, commented below

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
        //producer.commitTransaction();
        producer.close();
    }
}
