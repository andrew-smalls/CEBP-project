package Server;

public class ClientData {
    private String clientIdentifier;
    private String timestamp;
    private boolean online;

    public ClientData(String clientIdentifier, String timestamp, boolean online) {
        this.clientIdentifier = clientIdentifier;
        this.timestamp = timestamp;
        this.online = online;
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

    @Override
    public String toString() {
        return "ClientData{" +
                "clientIdentifier='" + clientIdentifier + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", online=" + online +
                '}';
    }
}
