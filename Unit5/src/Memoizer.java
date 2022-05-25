import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * ConcurrentHashMap + FutureTask + Map原子操作 来初始化缓存
 * @author osys
 */
public class Memoizer<A, V> implements Computable<A, V> {
    private final ConcurrentMap<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computer;

    public Memoizer(Computable<A, V> computer) {
        this.computer = computer;
    }

    @Override
    public V compute(final A arg) throws InterruptedException {
        while (true) {
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
                // 将就算返回的值，存入 map（如果存在，就将返回值存入map）
                FutureTask<V> task = new FutureTask<>(eval);
                cacheFuture = cache.putIfAbsent(arg, task);
                // 查看是否存在该 FutureTask，如果存在，那么计算结果
                if (cacheFuture == null) {
                    cacheFuture = task;
                    task.run();
                }
            }
            try {
                // 获取结果并返回
                return cacheFuture.get();
            } catch (CancellationException e) {
                cache.remove(arg, cacheFuture);
            } catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}