import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 外部线程中，安排中断
 *
 *
 * Created by osys on 2022/08/28 21:48.
 */
public class TimedRun {
    private static final Integer CORE_POOL_SIZE = 100;
    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();
    private static final ScheduledExecutorService EXEC = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, THREAD_FACTORY);

    /**
     * 外部线程中，安排中断
     * @param runnable 任务
     * @param timeout 时间
     * @param unit 时间单位
     */
    public static void timedRun(Runnable runnable,
                                long timeout,
                                TimeUnit unit) throws InterruptedException {
        Future<?> task = EXEC.submit(runnable);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            Thread.currentThread().interrupt();
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

    public static void main(String[] args) throws InterruptedException {
        Thread task = Thread.currentThread();
        TimedRun.timedRun(task, 0, TimeUnit.MILLISECONDS);
        if (task.isInterrupted()) {
            System.out.println("【main】线程执行了一些逻辑。 ------------------【main】计算结果 = 123");
        } else {
            System.out.println("【main】线程执行了一些逻辑。 ==================【main】计算结果 = abc");
        }
    }
}
