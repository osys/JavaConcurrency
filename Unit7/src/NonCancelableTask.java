import java.util.concurrent.BlockingQueue;

/**
 * 不可取消的任务在退出前保存中断
 *
 *
 * Created by osys on 2022/08/28 21:48.
 */
public class NonCancelableTask {
    public Task getNextTask(BlockingQueue<Task> queue) {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    interrupted = true;
                    // 失败并重试
                }
            }
        } finally {
            if (interrupted) {
                // 中断这个线程
                Thread.currentThread().interrupt();
            }
        }
    }

    interface Task {
    }
}
