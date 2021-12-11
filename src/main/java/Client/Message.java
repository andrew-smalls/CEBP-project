package Client;

import static Client.MessageType.SYSTEM_MESSAGE;

public class Message {
    private MessageType type;

    private String username;
    private String content;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Message(MessageType type) {
        this.type = type;
    }
    public Message() {
        this.type = SYSTEM_MESSAGE;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString(){
        String msg = username + ": "+ content;
        return msg;
    }
}