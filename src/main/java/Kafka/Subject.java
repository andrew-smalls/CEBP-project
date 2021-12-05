package Kafka;

public interface Subject { //https://www.journaldev.com/1739/observer-design-pattern-in-java
    //methods to register and unregister observers
    public void register(Observer obj);
    public void unregister(Observer obj);

    //method to notify observers of change
    public void notifyObservers();

    //method to get updates from subject
    public Object getUpdate(Observer obj);
}
