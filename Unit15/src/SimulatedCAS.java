import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SimulatedCAS {
    @GuardedBy("this")
    private int value;

    public synchronized int get() {
        return value;
    }

    /**
     * 比较并交换。
     * 如果旧值等于期望值，那么设置新值(newValue)。
     * @param expectedValue 期望值
     * @param newValue 新值
     * @return 旧值
     */
    public synchronized int compareAndSwap(int expectedValue,
                                           int newValue) {
        int oldValue = value;
        if (oldValue == expectedValue) {
            value = newValue;
        }
        return oldValue;
    }

    /**
     * 比较并交换。
     * 如果旧值等于期望值，那么设置新值(newValue)。
     * 设置新值成功，返回 true，否则返回 false
     * @param expectedValue 期望值
     * @param newValue 新值
     * @return 新值设置成功，旧值 == 期望值，返回 true
     */
    public synchronized boolean compareAndSet(int expectedValue,
                                              int newValue) {
        return (expectedValue
                == compareAndSwap(expectedValue, newValue));
    }
}
