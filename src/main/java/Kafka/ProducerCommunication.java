package Kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;

public class ProducerCommunication implements Runnable{

    @Override
    public void run() {
        String topic = "TestTopic";
        String message = "Mesaj de test";

        String bootstrapServers_sender = "localhost:9092";

        Producer sender = new Producer(bootstrapServers_sender);
        KafkaProducer<String, String> producer = sender.getProducer();

        producer.initTransactions();

        try {
            producer.beginTransaction();
            System.out.println("Sending...\n");
            for (int i = 0; i < 100; i++)
                producer.send(sender.getRecord(topic, "1",message));
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            // We can't recover from these exceptions, so our only option is to close the producer and exit.
            producer.close();
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        }
        producer.close();
    }
}
