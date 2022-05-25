import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author osys
 */
public class TimedRun0 {
    public static void main(String[] args) throws InterruptedException {
        TimeRun.timedRun(new RunImpl(), 1, TimeUnit.MILLISECONDS);

        // main 线程
        for (int i = 0; i < 30; i++) {
            System.out.println("---  " + Thread.currentThread().getName() + "  ---");
        }
    }
}


class TimeRun {
    private static final Integer CORE_POOL_SIZE = 100;
    private static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();
    private static final ScheduledExecutorService EXEC = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, THREAD_FACTORY);

    /** 外部线程中，安排中断 */
    public static void timedRun(Runnable runnable, long timeout, TimeUnit unit) {
        // 获取调用者线程
        final Thread task = Thread.currentThread();
        // 在一定时间后中断调用者线程
        EXEC.schedule(task::interrupt, timeout, unit);
        // 启动新线程
        runnable.run();
    }
}

/** 测试 runnable */
class RunImpl implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 30; i++) {
            System.out.println("---  RunImpl ---");
        }
    }
}