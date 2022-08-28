import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 判断一个数是否为素数
 * Created by osys on 2022/08/28 21:48.
 */
public class FutureTest04 {

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        long num = 1000000033L;
        PrimerTask task = new PrimerTask(num);
        Future<Boolean> future = threadPool.submit(task);
        // 发送关闭线程池的指令
        threadPool.shutdown();

        // 在 2 秒之后取消该任务
        cancelTask(future, 2_000);

        try {
            boolean result = future.get();
            System.out.format("%d 是否为素数？ %b\n", num, result);
        } catch (CancellationException ex) {
            System.err.println("任务被取消");
        } catch (InterruptedException ex) {
            System.err.println("当前线程被中断");
        } catch (ExecutionException ex) {
            System.err.println("任务执行出错");
        }
    }

    // 判断一个数是否为素数
    private static final class PrimerTask implements Callable<Boolean> {
        private final long num;
        public PrimerTask(long num) {
            this.num = num;
        }
        @Override
        public Boolean call() {
            double begin = System.nanoTime();
            // i < num 让任务有足够的运行时间
            for (long i = 2; i < num; i++) {
                if (num % i == 0) {
                    return false;
                }
            }
            double end = System.nanoTime();
            double time = (end - begin) / 1E9;
            System.out.format("任务运行时间: %.3f s\n", time);
            return true;
        }

    }

    // 在 delay ms 后取消 task
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
}
