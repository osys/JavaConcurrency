import java.util.concurrent.BlockingQueue;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class TaskRunnable implements Runnable {
    BlockingQueue<Task> queue;

    @Override
    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // 中断线程
            Thread.currentThread().interrupt();
        }
    }

    void processTask(Task task) {
        // 处理任务
    }

    interface Task {
    }
}
