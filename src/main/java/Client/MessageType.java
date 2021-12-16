package Client;

public enum MessageType {
    PING_MESSAGE("PING_MESSAGE"),
    TOPIC_REQUEST_MESSAGE("TOPIC_REQUEST_MESSAGE"),
    REGULAR_MESSAGE("REGULAR_MESSAGE"),
    NAME_REQUEST("NAME_REQUEST"),
    VALID_NAME("VALID_NAME"),
    INVALID_NAME("INVALID_NAME");

    private String type;

    private MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}