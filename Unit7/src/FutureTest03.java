import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 判断一个数是否为素数
 * Created by osys on 2022/08/28 21:48.
 */
public class FutureTest03 {

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        long num = 1000000033L;
        PrimerTask task = new PrimerTask(num);
        Future<Boolean> future = threadPool.submit(task);
        threadPool.shutdown();

        // get result
        boolean result = future.get();
        System.out.format("%d 是否为素数？ %b\n", num, result);

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

}