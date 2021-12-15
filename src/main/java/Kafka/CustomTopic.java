package Kafka;

import Vars.ServerAddress;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.TopicConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomTopic
{
    private final String oneDayMS = "86400000";
    private final int defaultPartitions = 12;
    private final short defaultReplicationFactor = 1;


    private String topicName;       //some name
    private String retentionMS;     //300000
    private int partitionsNumber;   //12
    private short replicationFactor;  //1, if you want bigger replication factor -> you need the same nr of brokers (currently 1)


    public CustomTopic(String topicName, String retentionMS, int partitionsNumber, short replicationFactor) {
        this.topicName = topicName;
        this.retentionMS = retentionMS;
        this.partitionsNumber = partitionsNumber;
        this.replicationFactor = replicationFactor;
    }

    public CustomTopic(String topicName) {
        this.topicName = topicName;
        this.retentionMS = oneDayMS;
        this.partitionsNumber = defaultPartitions;
        this.replicationFactor = defaultReplicationFactor;
    }

    public void CreateTopic()
    {
        try(Admin admin = getAdmin())
        {
            //Setup topic we want to creae
            NewTopic topic = new NewTopic(topicName, partitionsNumber, replicationFactor);

            //Config how the topic will behave from now on
            Map<String, String> configMap = new HashMap<>();
            configMap.put(TopicConfig.DELETE_RETENTION_MS_CONFIG, retentionMS);
            configMap.put(TopicConfig.RETENTION_MS_CONFIG, retentionMS);
            topic.configs(configMap);

            //Create the actual topic inside kafka
            CreateTopicsResult result = admin.createTopics(Collections.singleton(topic.configs(configMap)));

            // Call values() to get the result for a specific topic
            KafkaFuture<Void> future = result.values().get(topicName);

            // Call get() to block until the topic creation is complete or has failed
            // if creation failed the ExecutionException wraps the underlying cause.
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Admin getAdmin()
    {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, ServerAddress.LOCALHOST.getAddress());
        Admin admin = Admin.create(props);

        return admin;
    }

    public String getName()
    {
        return topicName;
    }

    @Override
    public String toString() {
        return "CustomTopic{" +
                "topicName='" + topicName + '\'' +
                ", retentionMS='" + retentionMS + '\'' +
                ", partitionsNumber=" + partitionsNumber +
                ", replicationFactor=" + replicationFactor +
                '}';
    }
}