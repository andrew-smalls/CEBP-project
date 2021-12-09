import Client.Client;
import Vars.ClientStatus;

public class ClientCommunication {

    public static void main(String[] args) throws InterruptedException {

        Client client = new Client();

        client.startCommunication();

        if(client.getClientStatus().equals(ClientStatus.DEAD))
            System.out.println("Client " + client.getClientId() + " ended communication");
    }

    public static void doNothing(){ int i = 0; i++;}

}
