import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class TimedRun0 {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个新的 task
        FutureTask<String> runImpl = new FutureTask<>(new RunImpl(), "runImpl");
        // 一定时间后，中断主线程，即 将main线程设为中断状态
        TimeRun.timedRun(runImpl, 1, TimeUnit.MILLISECONDS);
        // 调用者执行自己的逻辑
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("【main】线程执行了一些逻辑。 ------------------【main】计算结果 = 123");
        } else {
            System.out.println("【main】线程执行了一些逻辑。 ==================【main】计算结果 = abc");
        }
    }
}


class TimeRun {
    private static final Integer CORE_POOL_SIZE = 100;
    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();
    private static final ScheduledExecutorService EXEC = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, THREAD_FACTORY);

    /**
     * 外部线程中，安排中断
     * @param runnable 任务
     * @param timeout 时间
     * @param unit 时间单位
     */
    public static void timedRun(Runnable runnable, long timeout, TimeUnit unit) {
        // 获取调用者线程
        final Thread task = Thread.currentThread();
        // 执行一个任务，该任务是将【调用者线程】标记为中断状态
        ScheduledFuture<?> schedule = EXEC.schedule(task::interrupt, timeout, unit);
    }
}

/** 测试 runnable */
class RunImpl implements Runnable {
    @Override
    public void run() {
        // System.out.println("【" + Thread.currentThread().getName() + "】线程执行了一些逻辑。");
    }
}