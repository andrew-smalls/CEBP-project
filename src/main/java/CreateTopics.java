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

public class CreateTopics {

    public static void main(String[] args) throws Exception {

        String name = "andrei_topic";
        int numPartitions = 1;
        short replicationFactor = 1;    //if you want bigger replication factor -> you need the same nr of brokers (currently 1)

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (Admin admin = Admin.create(props))
        {
            String topicName = "expireTopic5";
            int partitions = 12;
            replicationFactor = 1;
            String retentionMS = "300000"; // 6000 ii aprox 2 min

            //Setup topic we want to creae
            NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);

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
        }
    }
}