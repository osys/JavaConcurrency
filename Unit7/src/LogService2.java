import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author osys
 */
public class LogService2 {
    private  int THREAD_NUM = 10;
    private final ExecutorService exec = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(CAPACITY));
    private final long TIMEOUT = 10000;
    private final TimeUnit UNIT = TimeUnit.MILLISECONDS;
    private final WriteTask writer;
    private final BlockingQueue<String> queue;
    private static final int CAPACITY = 1000;

    public LogService2(Writer writer) {
        this.writer = new WriteTask(writer);
        this.queue = new LinkedBlockingQueue<String>(CAPACITY);
    }

    public void start() {}

    public void stop() throws InterruptedException {
        try{
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, UNIT);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try{
            queue.put(msg);
            exec.execute(writer);
        } catch (RejectedExecutionException ignored) {

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private class WriteTask implements Runnable {

        private final PrintWriter writer;

        public WriteTask(Writer writer) {
            this.writer = (PrintWriter) writer;
        }

        public void close() {
            this.writer.close();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    writer.println(LogService2.this.queue.take());
                }
            } catch (InterruptedException ignored) {
            } finally {
                writer.close();
            }
        }
    }
}