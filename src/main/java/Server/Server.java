package Server;

public class Server {
    private static final String groupId = "server_group";
    private static final String pingTopic="client_pings_topic";

    public Server(){
    }

    public void startListenerThread(){
        Listener listener = new Listener(groupId, pingTopic);
        Thread listenerThread = new Thread(listener, "Listener");

        System.out.println("Started listener thread");
        listenerThread.start();
    }

    public void onlineUpdate()
    {

    }

}
