package Thread;

import Client.ProducerCommunication;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class NotifyingThread extends Thread implements Runnable { // amin https://stackoverflow.com/questions/702415/how-to-know-if-other-threads-have-finished

    protected boolean running = true;  //controls the consumer, nothing else

    private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

    public final void addListener(final ThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final ThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }

    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }

    public void doRun() { //this gets overridden in the ProducerCommunication-like classes

    }


    public void stopConsumer(){
        running = false;
    }
}
