package Kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class Communication {

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome!\n\n" +
                "Press:\n" +
                "0 - for Producer\n"+
                "1 - for Consumer\n\n"+
                "Answer: "
        );
        int res = Integer.parseInt(br.readLine());
        if(res==0) {
            System.out.println("You are a Producer\n");
            Thread producerThread= new Thread(new ProducerCommunication(),"Producer");
            producerThread.start();
        }
        else if(res==1){
            System.out.println("You are a consumer\n");
            Thread consumerThread = new Thread(new ConsumerCommunication(),"Consumer");
            consumerThread.start();
        }
        else{
            System.out.println("Sorry. Wrong input.\n");
        }
    }
}
