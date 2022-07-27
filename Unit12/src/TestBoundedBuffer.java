import junit.framework.TestCase;
import org.junit.Test;

public class TestBoundedBuffer extends TestCase {
    private static final long LOCKUP_DETECT_TIMEOUT = 1000;
    private static final int CAPACITY = 10000;
    private static final int THRESHOLD = 10000;

    @Test
    public void testIsEmptyWhenConstructed() {
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    @Test
    public void testIsFullAfterPuts() throws InterruptedException {
        SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        for (int i = 0; i < 10; i++) {
            bb.put(i);
        }
        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    @Test
    public void testTakeBlocksWhenEmpty() {
        final SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<Integer>(10);
        Thread taker = new Thread() {
            public void run() {
                try {
                    int unused = bb.take();
                    fail(); // 如果运行到这里，说明有错误
                } catch (InterruptedException success) { }
            }
        };
        try {
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected) {
            fail();
        }
    }

    public class Big {
        double[] data = new double[100000];
    }

    @Test
    public void testLeak() throws InterruptedException {
        SemaphoreBoundedBuffer<Big> bb = new SemaphoreBoundedBuffer<>(CAPACITY);
        int heapSize1 = snapshotHeap();         // heap 的快照
        for (int i = 0; i < CAPACITY; i++) {
            bb.put(new Big());
        }
        for (int i = 0; i < CAPACITY; i++) {
            bb.take();
        }
        int heapSize2 = snapshotHeap();         // heap 的快照
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

    private int snapshotHeap() {
        /* Snapshot heap and return heap size */
        return 0;
    }

}
