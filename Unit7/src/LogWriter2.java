import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class LogWriter2 {
    private final BlockingQueue<String> queue;
    private final LoggerThread2 logger;
    private static final int CAPACITY = 1000;

    private boolean isShutdownRequested = false;

    public LogWriter2(Writer writer) {
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
        this.logger = new LoggerThread2(writer);
    }

    public void start() {
        logger.start();
    }

    public void log(String msg) throws InterruptedException {
        queue.put(msg);
        if (!isShutdownRequested) {
            queue.put(msg);
        } else {
            throw new IllegalStateException("logger is shut down");
        }
    }

    public void setShutdownStatus(boolean status) {
        this.isShutdownRequested = status;
    }

    /**
     * 日记线程 class
     */
    private class LoggerThread2 extends Thread {
        private final PrintWriter writer;

        public LoggerThread2(Writer writer) {
            this.writer = new PrintWriter(writer, true);
        }

        public void run() {
            try {
                while (true) {
                    writer.println(queue.take());
                }
            } catch (InterruptedException ignored) {
            } finally {
                writer.close();
            }
        }
    }
}
