import java.util.concurrent.BlockingQueue;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class WorkerThread extends Thread {
    private final BlockingQueue<Runnable> queue;

    public WorkerThread(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    public void run() {
        while (true) {
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
