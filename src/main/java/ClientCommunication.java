import Client.Client;
import Vars.ClientStatus;
import Client.ClientMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientCommunication {

    public static void main(String[] args) throws InterruptedException {
        String answer;
        ClientMenu menu=new ClientMenu();
        Client client = new Client();
        //client.startPingThread();
        client.startCommunication();

        System.out.println("Welcome!\n");

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            menu.show();
            try{
                answer=br.readLine();
            }catch (IOException e){
                e.printStackTrace();
                answer="2";
            }
            if(answer.equals("2")){
                break;
            }
            else{

                //System.out.println("Iti dam conversatie mai tarziu\n");
            }
        }


        //client.stopPingThread();
    }

}
