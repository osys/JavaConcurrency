import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Semaphore;

@ThreadSafe
public class SemaphoreBoundedBuffer<E> {

    /**
     * Semaphore ---- 计数信号量
     * availableItems ---- 可用items
     * availableSpaces ---- 可用空间
     */
    private final Semaphore availableItems, availableSpaces;

    /** 存放 Element 的数组 */
    @GuardedBy("this")
    private final E[] items;

    /**
     * putPosition ---- 存入数组 items 的位置
     * takePosition ---- 从数组中拿 Element 的位置
     */
    @GuardedBy("this")
    private int putPosition = 0, takePosition = 0;

    public SemaphoreBoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        // 初始化，可用 items = 0，可用空间 spaces = capacity
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull() {
        return availableSpaces.availablePermits() == 0;
    }

    /**
     * 添加一个元素
     * 可用空间 - 1
     * 可用元素 + 1
     */
    public void put(E x) throws InterruptedException {
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    /**
     * 拿出一个元素
     * 可用空间 + 1
     * 可用元素 - 1
     */
    public E take() throws InterruptedException {
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    /**
     * 添加一个元素
     * @param x 元素
     */
    private synchronized void doInsert(E x) {
        int i = putPosition;
        items[i] = x;

        ++i;
        if (i == items.length) {
            putPosition = 0;
        } else {
            putPosition = i;
        }
    }

    private synchronized E doExtract() {
        int i = takePosition;
        E x = items[i];
        items[i] = null;

        ++i;
        if (i == items.length) {
            takePosition = 0;
        } else {
            takePosition = i;
        }
        return x;
    }
}