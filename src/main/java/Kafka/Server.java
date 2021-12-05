package Kafka;

public class Server implements Observer{
    private static Server instance;

    private Subject topic; //a topic of the ConsumerCommunication / ProducerCommunication type
    private static int connectedClients = 0;

    private Server() {
        System.out.println("Server instance created");
    }

    public static  Server getInstance() //Singleton instance of Server class, we don't want more than one Server tracking the clients activity
    {
        if(instance == null)
        {
            synchronized (Server.class) //Synchronized for thread safety ( double checked locking method to reduce time wasted on sync)
            {
                if(instance == null)
                    instance = new Server();
            }
        }
        return instance;
    }

    @Override
    public void update()
    {
        String[] msg = (String[]) topic.getUpdate(this);
        if(msg == null)
        {
            System.out.println("No new message");
        }
        else
        {
            String message = msg[0];
            String clientId = msg[1];
            if(message.equals("connected"))
            {
                connectedClients++;
                System.out.println("Client connected to server::" + clientId);

            }
            else if(message.equals("disconnected"))
            {
                connectedClients--;
                System.out.println("Client disconnected to server::" + clientId);
            }
            System.out.println("Nr of connected clients::" + connectedClients);
        }
    }

    @Override
    public void setSubject(Subject sub)
    {
        this.topic = sub;
    }


    public static void main(String[] args) {
        Server.getInstance();
    }
}
