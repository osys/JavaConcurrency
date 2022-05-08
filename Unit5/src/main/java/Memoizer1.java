import net.jcip.annotations.GuardedBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * 尝试使用 HashMap 和同步来初始化缓存
 * @author osys
 */
public class Memoizer1<A, V> implements Computable<A, V> {
    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> computer;

    public Memoizer1(Computable<A, V> computer) {
        this.computer = computer;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        // 从缓存中获取，查看缓存中是否存在该值
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


interface Computable <A, V> {
    V compute(A arg) throws InterruptedException;
}

class ExpensiveFunction implements Computable<String, BigInteger> {
    @Override
    public BigInteger compute(String arg) {
        return new BigInteger(arg);
    }
}