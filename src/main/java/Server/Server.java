package Server;

public class Server {
    private static final String groupId = "server_group";
    private static final String pings_topic = "client_pings_topic";

    public Server(){}

    public void startPingThread(){
        Listener listener = new Listener(groupId, pings_topic);
        Thread listenerThread = new Thread(listener, "Listener");
        listenerThread.start();
    }


}
