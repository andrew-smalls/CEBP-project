import Client.Client;
import Vars.ClientStatus;

import java.io.IOException;

public class ClientCommunication {

    public static void main(String[] args) throws InterruptedException, IOException {

        Client client = new Client();

        client.startCommunication();

        if(client.getClientStatus().equals(ClientStatus.DEAD))
            System.out.println("Client " + client.getClientId() + " ended communication");
    }
}
