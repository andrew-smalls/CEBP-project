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
        client.startPingThread();


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
            else if(answer.equals("1")){
                String name;
                System.out.println("Who do you want to talk to?\n");
                System.out.println("Type a valid username: ");
                try{
                    name=br.readLine();
                    //client.startCommunication();
                }catch (IOException e){
                    e.printStackTrace();
                }

                //System.out.println("Iti dam conversatie mai tarziu\n");
            }
        }


        client.stopPingThread();
        System.out.println("Done\n");
        System.out.println("Thread nr: "+Thread.activeCount());
        Thread.sleep(1000);
    }

}
