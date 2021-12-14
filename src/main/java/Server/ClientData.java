package Server;

import java.util.Objects;

public class ClientData {
    private String clientIdentifier;
    private String timestamp;
    private boolean online;
    private String requestsTopic;

    public ClientData(String clientIdentifier, String timestamp) {
        this.clientIdentifier = clientIdentifier;
        this.timestamp = timestamp;
        this.online = true;
        this.requestsTopic=null;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getRequestsTopic() {
        return requestsTopic;
    }

    public void setRequestsTopic(String requestsTopic) {
        this.requestsTopic = requestsTopic;
    }

    @Override
    public String toString() {
        return "ClientData{" +
                "clientIdentifier='" + clientIdentifier + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", online=" + online +
                '}' + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientData that = (ClientData) o;
        return clientIdentifier.equals(that.clientIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdentifier);
    }
}
