package Kafka;

import java.util.ArrayList;
import java.util.List;

public class ObservedSubject implements Subject{  //https://www.journaldev.com/1739/observer-design-pattern-in-java
    private List<Observer> observers;
    private String message;
    private String clientId;
    private boolean changed;
    private final Object MUTEX= new Object();

    public ObservedSubject(){
        this.observers=new ArrayList<>();
    }

    @Override
    public void register(Observer obj)
    {
        if(obj == null) throw new NullPointerException("Null Observer");

        synchronized (MUTEX)
        {
            if(!observers.contains(obj))
                observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX)
        {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        List<Observer> observersLocal = null;
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX)
        {
            if (!changed)
                return;

            observersLocal = new ArrayList<>(this.observers);
            this.changed = false;
        }
        for (Observer obj : observersLocal)
        {
            obj.update();
        }

    }

    @Override
    public Object getUpdate(Observer obj)
    {
        return new String[]{this.message, this.clientId};
    }

    //method to post message to the topic
    public void postMessage(String msg, String clientId)
    {
        //System.out.println("Message Posted to Topic:" + msg + " " + clientId);
        this.message = msg;
        this.clientId = clientId;
        this.changed = true;

        notifyObservers();
    }
}
