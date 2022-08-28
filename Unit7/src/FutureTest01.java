import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class FutureTest01 {

    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();

        // task 需要运行 3 秒
        SimpleTask task = new SimpleTask(3_000);
        Future<Double> future = threadPool.submit(task);
        threadPool.shutdown(); // 发送关闭线程池的指令

        double time = future.get();
        System.out.format("任务运行时间: %.3f s\n", time);

    }

    private static final class SimpleTask implements Callable<Double> {

        private final int sleepTime; // ms

        public SimpleTask(int sleepTime) {
            this.sleepTime = sleepTime;
        }

        @Override
        public Double call() throws Exception {
            double begin = System.nanoTime();

            Thread.sleep(sleepTime);

            double end = System.nanoTime();
            double time = (end - begin) / 1E9;

            return time; // 返回任务运行的时间，以 秒 计
        }

    }

}