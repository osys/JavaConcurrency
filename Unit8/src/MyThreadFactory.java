import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class MyThreadFactory implements ThreadFactory {
    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    public Thread newThread(Runnable runnable) {
        return new MyAppThread(runnable, poolName);
    }
}


class MyAppThread extends Thread {
    /** 线程默认名称 */
    public static final String DEFAULT_NAME = "MyAppThread";
    /** 是否调用 debug 调试生命周期 */
    private static volatile boolean debugLifecycle = false;
    /** 线程名称的一部分：这里使用递增数命名线程 */
    private static final AtomicInteger created = new AtomicInteger();
    /** 运行的线程数 */
    private static final AtomicInteger alive = new AtomicInteger();
    /** 日志 */
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r) {
        this(r, DEFAULT_NAME);
    }

    public MyAppThread(Runnable runnable, String name) {
        // 线程初始化
        super(runnable, name + "-" + created.incrementAndGet());
        // 未捕获的异常处理
        setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                log.log(Level.SEVERE, "未捕获的异常线程：" + t.getName(), e);
            }
        });
    }

    public void run() {
        // 复制 debug 调试断言，以确保始终一致的值。
        boolean debug = debugLifecycle;
        if (debug) log.log(Level.FINE, "Created " + getName());
        try {
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if (debug) log.log(Level.FINE, "Exiting " + getName());
        }
    }

    public static int getThreadsCreated() {
        return created.get();
    }

    public static int getThreadsAlive() {
        return alive.get();
    }

    public static boolean getDebug() {
        return debugLifecycle;
    }

    public static void setDebug(boolean b) {
        debugLifecycle = b;
    }
}
