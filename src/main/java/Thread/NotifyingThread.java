package Thread;

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

    private final void notifyListeners() throws InterruptedException {
        for (ThreadCompleteListener listener : listeners) {
            listener.notifyOfThreadComplete(this);
        }
    }

    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            try {
                notifyListeners();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doRun() { //this gets overridden in the ProducerCommunication-like classes

    }


    public void stopConsumer(){
        running = false;
    }
}
