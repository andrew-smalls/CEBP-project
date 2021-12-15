package Kafka;

import Client.Message;
import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

public class JsonDeserializer implements Deserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void configure(Map configs, boolean isKey) {
    }

    public Object deserialize(String s, byte[] bytes) {
        Message message = new Message();
        try {

            message = objectMapper.readValue(bytes, Message.class);
        } catch (Exception e) {
            log.error("unable to deserialize {}",bytes,e);
        }
        return message;
    }

}
