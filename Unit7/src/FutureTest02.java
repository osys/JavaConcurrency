import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class FutureTest02 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        // task 需要运行 3 秒
        SimpleTask task = new SimpleTask(3_000);
        Future<Double> future = threadPool.submit(task);
        // 发送关闭线程池的指令
        threadPool.shutdown();

        // 在 2 秒之后取消该任务
        cancelTask(future, 2_000);

        try {
            double time = future.get();
            System.out.format("任务运行时间: %.3f s\n", time);
        } catch (CancellationException ex) {
            System.err.println("任务被取消");
        } catch (InterruptedException ex) {
            System.err.println("当前线程被中断");
        } catch (ExecutionException ex) {
            System.err.println("任务执行出错");
        }

    }

    private static void cancelTask(final Future<?> future, final int delay) {
        Runnable cancellation = () -> {
            try {
                Thread.sleep(delay);
                // 取消与 future 关联的正在运行的任务
                future.cancel(true);
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }
        };
        new Thread(cancellation).start();
    }

    private static final class SimpleTask implements Callable<Double> {
        // ms
        private final int sleepTime;

        public SimpleTask(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Double call() throws Exception {
            double begin = System.nanoTime();
            Thread.sleep(sleepTime);
            double end = System.nanoTime();
            double time = (end - begin) / 1E9;
            // 返回任务运行的时间，以 秒 计
            return time;
        }

    }
}
