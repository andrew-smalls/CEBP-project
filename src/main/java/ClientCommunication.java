import Client.Client;
import Vars.ClientStatus;

import java.io.IOException;

public class ClientCommunication {

    public static void main(String[] args) {

        Client client = new Client();

        client.startCommunication();

        while (!client.getClientStatus().equals(ClientStatus.DEAD)) //momentan, facem check-ul constant. Ulterior, trebuie sa trimita clientul ping (ALIVE) ->  cand nu mai trimite ping, il declaram DEAD
        {
            //TimeUnit.SECONDS.sleep(2); //daca avem pauza aici, o sa iasa cand scrii "exit". Fara pauza, nu vrea sa iasa
            //System.out.println(client.getClientStatus());
        }

        if(client.getClientStatus().equals(ClientStatus.DEAD))
            System.out.println("Client " + client.getClientId() + " ended communication");
    }



}
