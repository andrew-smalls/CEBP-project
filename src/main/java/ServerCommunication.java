import Server.Server;
import Server.ServerMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerCommunication {

    public static void main(String[] args) throws InterruptedException {

        Server server = new Server();

        server.startListenerThread();
        server.startUpdaterThread();
        server.startTopicRequestsListener();

        String answer;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while(true)
        {
            ServerMenu.show();
            try{
                answer = br.readLine();
            }catch (IOException e){
                e.printStackTrace();
                answer = "exit";
            }
            if(answer.equals("exit")){
                break;
            }
            else if(answer.equals("info")){
                server.getPulse();
            }
            else if(answer.equals("online")){
                server.getConnectedClients();
            }
            else if(answer.equals("all")){
                server.getListOfClients();
            }
        }

        server.cancelListenerThread();
        server.cancelUpdaterThread();
        server.stopTopicRequestsListener();
        Thread.sleep(100);
        System.out.println("Threads active before closing = " + Thread.activeCount());
    }
}
