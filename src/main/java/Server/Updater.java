package Server;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

public class Updater implements Runnable{

    // should get rid of clients with expired timestamps

    private boolean running = true;
    private BlockingQueue<ClientData> clientList;

    public Updater(BlockingQueue<ClientData> clientList) {
        this.clientList = clientList;
    }

    @Override
    public void run() {
        while (running)
        {
            Iterator<ClientData> iterator = clientList.iterator();
            System.out.println("Items in queue at this moment");
            Long currentTimestamp = System.currentTimeMillis();

            while (iterator.hasNext()) {   //go through items of the queue that holds the list of users
                ClientData tempData = iterator.next();
                Long timestamp = Long.valueOf(tempData.getTimestamp());
                Long delta = currentTimestamp - timestamp;

                if(delta > 1)       //compute time since last timestamp delivered through ping. If more than 1 sec passed, should remove the user
                {
                    System.out.println("This user should be removed, inactive for " + delta);
                    System.out.println("Time for reference" + currentTimestamp);
                    System.out.println("Time for client" + timestamp);
                }
                    //System.out.println((iterator.next()));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
