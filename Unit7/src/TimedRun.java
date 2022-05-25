import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 外部线程中，安排中断
 * @author osys
 */
public class TimedRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(Runnable runnable,
                                long timeout,
                                TimeUnit unit) throws InterruptedException {
        Future<?> task = taskExec.submit(runnable);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            // 任务将被取消
        } catch (ExecutionException e) {
            // 任务中抛出异常
            throw LaunderThrowable.launderThrowable(e.getCause());
        } finally {
            // 如果任务已经完成，则不做处理
            // 如果任务未完成，任务将被取消
            task.cancel(true);
        }
    }
}
