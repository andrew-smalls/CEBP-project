package Server;

import Client.MessageType;
import Client.Message;
import Client.Producer;
import Tools.UniqueIdGenerator;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class TopicRequestHandler implements Runnable{

    private BlockingQueue<ClientData> clientsList;
    private String caller;
    private String callee;
    private KafkaProducer<String,Message> producer;

    public TopicRequestHandler(String caller,
                               String callee,
                               BlockingQueue<ClientData> clientsList,
                               KafkaProducer<String, Message> producer,
                               Producer sender){
        this.clientsList=clientsList;
        this.caller=caller;
        this.callee=callee;
        this.producer=producer;
    }

    @Override
    public void run() {
        ClientData callee_client = new ClientData(callee,String.valueOf(System.currentTimeMillis()));
        ClientData caller_client = new ClientData(caller,String.valueOf(System.currentTimeMillis()));
        if(clientsList.contains(callee_client)){
            Message message=new Message(MessageType.TOPIC_REQUEST_MESSAGE);
            message.setUsername("server");
            Iterator<ClientData> iterator = clientsList.iterator();
            String uniqueTopic=String.valueOf(UniqueIdGenerator.generateID()); // topic-ul asta va fi folosit de cei doi clienti pentru conversatie
            while(iterator.hasNext()){
                ClientData c=iterator.next();
                if(c.equals(caller_client)){
                    message.setContent(callee+","+uniqueTopic); // formatul mesajului este: <friend_username>,<topic>
                    producer.send(Producer.getRecord(c.getRequestsTopic(),"1",message)); // trimitem topicul catre clientul apelant
                }
                if(c.equals(callee_client)){
                    message.setContent(caller+","+uniqueTopic);
                    producer.send(Producer.getRecord(c.getRequestsTopic(),"1",message)); // trimitem topicul catre apelat
                }
            }
        }
    }
}
