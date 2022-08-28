import net.jcip.annotations.ThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class StripedMap {
    // 同比策略: buckets[n] 由 locks[n%N_LOCKS] 保护
    private static final int N_LOCKS = 16;
    private final Node[] buckets;
    private final Object[] locks;

    private static class Node {
        Node next;
        Object key;
        Object value;
    }

    /**
     * 分离锁的 map
     * @param numBuckets map 容量
     */
    public StripedMap(int numBuckets) {
        buckets = new Node[numBuckets];
        locks = new Object[N_LOCKS];
        for (int i = 0; i < N_LOCKS; i++) {
            locks[i] = new Object();
        }
    }

    /** hash 值。通过 key 的hash值除以桶个数求余，取绝对值得出 */
    private final int hash(Object key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    public Object get(Object key) {
        int hash = hash(key);
        // locks[hash % N_LOCKS] --> 守护 buckets[hash]
        synchronized (locks[hash % N_LOCKS]) {
            for (Node m = buckets[hash]; m != null; m = m.next) {
                if (m.key.equals(key)) {
                    return m.value;
                }
            }
        }
        return null;
    }

    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            // locks[i % N_LOCKS] --> 守护 buckets[i]
            synchronized (locks[i % N_LOCKS]) {
                buckets[i] = null;
            }
        }
    }
}
