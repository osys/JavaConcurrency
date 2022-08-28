import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 使用信号量来约束容器
 * Created by osys on 2022/08/28 21:48.
 */
public class BoundedHashSet <T> {
    private final Set<T> set;

    /** 信号量 */
    private final Semaphore semaphore;

    public BoundedHashSet(int bound) {
        this.set = Collections.synchronizedSet(new HashSet<>());
        semaphore = new Semaphore(bound);
    }

    public boolean add(T obj) throws InterruptedException {
        // 从这个信号量获取一个许可
        semaphore.acquire();
        boolean wasAdded = false;
        try {
            wasAdded = set.add(obj);
            return wasAdded;
        } finally {
            if (!wasAdded) {
                // 释放许可
                semaphore.release();
            }
        }
    }

    public boolean remove(Object obj) {
        boolean wasRemoved = set.remove(obj);
        if (wasRemoved) {
            // 释放许可
            semaphore.release();
        }
        return wasRemoved;
    }
}
