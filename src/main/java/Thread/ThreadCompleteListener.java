package Thread;

public interface ThreadCompleteListener {
    void notifyOfThreadComplete(final Thread thread) throws InterruptedException;
}
