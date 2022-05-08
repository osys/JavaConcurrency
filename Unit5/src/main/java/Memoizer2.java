import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用 ConcurrentHashMap 来初始化缓存
 * @author osys
 */
public class Memoizer2<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> computer;

    public Memoizer2(Computable<A, V> computer) {
        this.computer = computer;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        // 将就算返回的值，存入 map
        V result = cache.get(arg);
        if (result == null) {
            // 缓存中不存在该值，计算并返回
            result = computer.compute(arg);
            // 将就算返回的值，存入 map
            cache.put(arg, result);
        }
        return result;
    }
}