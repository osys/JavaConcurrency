import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 用 FutureTask 记录包装器来初始化缓存
 * Created by osys on 2022/08/28 21:48.
 */
public class Memoizer3<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computer;

    public Memoizer3(Computable<A, V> computer) {
        this.computer = computer;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        // 从缓存中获取，查看缓存中是否存在该值
        Future<V> cacheFuture = cache.get(arg);
        if (cacheFuture == null) {
            Callable<V> eval = new Callable<V>() {
                @Override
                public V call() throws InterruptedException {
                    // 缓存中不存在该值，计算并返回
                    return computer.compute(arg);
                }
            };
            // 将Future<V>，存入 map
            FutureTask<V> task = new FutureTask<>(eval);
            cacheFuture = task;
            cache.put(arg, task);
            // 执行 FutureTask，计算结果
            task.run();
        }
        try {
            // 获取结果并返回
            return cacheFuture.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }
}