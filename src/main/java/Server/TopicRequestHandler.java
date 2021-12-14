package Server;

import Client.MessageType;
import Client.Message;
import Client.Producer;
import Tools.UniqueIdGenerator;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.concurrent.BlockingQueue;

public class TopicRequestHandler implements Runnable{

    private BlockingQueue<ClientData> clientsList;
    private String caller;
    private String callee;
    private KafkaProducer<String,Message> producer;
    private Producer sender;

    public TopicRequestHandler(String caller,
                               String callee,
                               BlockingQueue<ClientData> clientsList,
                               KafkaProducer<String, Message> producer,
                               Producer sender){
        this.clientsList=clientsList;
        this.caller=caller;
        this.callee=callee;
        this.producer=producer;
        this.sender=sender;
    }

    @Override
    public void run() {
        ClientData client = new ClientData(callee,String.valueOf(System.currentTimeMillis()));
        if(clientsList.contains(client)){
            String requestsTopic="";//fill in
            String topic=String.valueOf(UniqueIdGenerator.generateID());
            Message message=new Message(MessageType.TOPIC_REQUEST_MESSAGE);
            message.setContent(topic);
            producer.send(sender.getRecord(requestsTopic,"1",message));
        }
    }
}
