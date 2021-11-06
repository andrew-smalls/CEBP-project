package Models;

import org.apache.kafka.clients.consumer.internals.AbstractCoordinator;

public class Client {
    private boolean online;
    private String name;
    private String clientId;

    public Client(String name, String clientId) {
        this.online = false;
        this.name = name;
        this.clientId = clientId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getName() {
        return name;
    }


    public String getClientId() {
        return clientId;
    }

    public void sendMessage(MessageQueue messageQueue, Client receiver)
    {

    }

    public void sendTopic(MessageTopic messageTopic, Client receiver)
    {

    }
}