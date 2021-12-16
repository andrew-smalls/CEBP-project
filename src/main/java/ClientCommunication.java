import Client.Client;
import Client.ClientMenu;
import Tools.NameReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClientCommunication {



    public static void main(String[] args) throws InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String answer;
        String username = "";
        ClientMenu menu=new ClientMenu();

        System.out.println("Welcome!\n");

        Client client = new Client();
        username = NameReader.readName();
        while(!client.checkNameFromServer(username))
        {
            System.out.println("Invalid name, please choose another name");
            username = NameReader.readName();
        }
        client.setUsername(username);


        client.startPingThread();
        client.startResponseListenerThread();
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
                    client.requestTopic(name);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            else if(answer.equals("3")){
                String corespondent;
                System.out.println("Select chat:\n");
                client.showActiveConnections();
                try{
                    corespondent=br.readLine();
                    client.startCommunication(corespondent);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

        }

        client.stopResponseListenerThread();
        client.stopPingThread();
        Thread.sleep(1000);

    }

}
