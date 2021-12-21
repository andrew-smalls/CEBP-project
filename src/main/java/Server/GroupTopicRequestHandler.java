package Server;

import Client.Message;
import Client.MessageType;
import Client.Producer;
import Kafka.CustomTopic;
import Tools.UniqueIdGenerator;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class GroupTopicRequestHandler implements Runnable{

    private BlockingQueue<ClientData> clientsList;
    private String caller;
    private String members;
    private KafkaProducer<String,Message> producer;
    private String groupName;

    public GroupTopicRequestHandler(String caller,
                                    String members,
                                    BlockingQueue<ClientData> clientsList,
                                    KafkaProducer<String, Message> producer,
                                    Producer sender){
        this.clientsList=clientsList;
        this.caller=caller;
        this.members=members;
        this.producer=producer;
    }

    @Override
    public void run() {
        ClientData caller_client = new ClientData(caller,String.valueOf(System.currentTimeMillis()));
        String[] callers = members.split(",");
        this.groupName = callers[0];
        java.util.List<String> list = new ArrayList<String>(Arrays.asList(callers));
        list.remove(0);
        callers = list.toArray(new String[0]);
        ArrayList<ClientData> callee_clients = new ArrayList<>();
        for(String c :callers) {
            callee_clients.add(new ClientData(c, String.valueOf(System.currentTimeMillis())));
        }

        Message message=new Message(MessageType.GROUP_TOPIC_REQUEST_MESSAGE);
        message.setUsername("server");
        String uniqueTopic = String.valueOf(UniqueIdGenerator.generateID());
        CustomTopic topic = new CustomTopic(uniqueTopic);
        topic.CreateTopic();

        Iterator<ClientData> iterator = clientsList.iterator();
        while(iterator.hasNext()){
            ClientData c=iterator.next();
            if(c.equals(caller_client)){
                message.setContent(groupName+","+topic.getName());
                System.out.println("Message content for leader: " + message.getContent());
                producer.send(Producer.getRecord(c.getRequestsTopic(),"1",message));
            }
            for(ClientData callee_client : callee_clients) {
                if (c.equals(callee_client)) {
                    String[] names = members.split(",");

                    message.setContent(groupName + "," + topic.getName());
                    System.out.println("Message content for members: " + message.getContent());
                    producer.send(Producer.getRecord(c.getRequestsTopic(), "1", message));
                }
            }
        }

    }
}
