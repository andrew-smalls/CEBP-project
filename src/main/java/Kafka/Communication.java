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
                "Please enter an id: "
        );
        String id= br.readLine();

        Thread.currentThread().setName("Principal");

        Thread producerThread= new Thread(new ProducerCommunication(id),"Producer");
        producerThread.start();
        ConsumerCommunication consumer=new ConsumerCommunication();
        Thread consumerThread = new Thread(consumer, "Consumer");
        consumerThread.start();

        System.out.println("Am ajuns aici!!\n"+Thread.currentThread().getName());

        while(true){
            if(!producerThread.isAlive()){
                break;
            }
        }

    }
}
