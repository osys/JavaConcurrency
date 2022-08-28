import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread thread, Throwable throwable) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, "线程异常终止: " + thread.getName(), throwable);
    }
}
