import net.jcip.annotations.ThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class CasCounter {
    // CAS
    private SimulatedCAS value;

    public int getValue() {
        return value.get();
    }

    /**
     * 自增
     * 如果自增，使用 value++ 的形式，那么需要对 value 加锁。
     * 这里自增，使用 CAS 的形式，自增失败后进行重试，不采用 value 加锁的形式
     */
    public int increment() {
        int value0;
        int compareAndSwap;
        do {
            // 获取旧值
            value0 = this.value.get();
            // 比较并交换
            // compareAndSwap() 方法 ---- 如果旧值等于期望值()，那么设置新值(newValue)，始终返回旧值
            // 旧值：this.value.get()this.value.get()，期望值：value0，新值：value0 + 1
            // 多线程环境下，可能上面获取的 value0 已经不等于 this.value.get() 了
            compareAndSwap = this.value.compareAndSwap(value0, value0 + 1);
            // value0 != compareAndSwap 为了确保多线程环境下，数据被其它线程修改，当前线程修改失败，可重新尝试修改
        } while (value0 != compareAndSwap);
        // 返回新值
        return value0 + 1;
    }
}