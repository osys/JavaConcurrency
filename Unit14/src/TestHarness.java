import java.util.concurrent.CountDownLatch;

/**
 * 在时序测试中，使用 CountDownLatch 来启动和停止线程
 *
 * CountDownLatch.class ---- 利用它可以实现类似计数器的功能。比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
 * Created by osys on 2022/08/28 21:48.
 */
public class TestHarness {
    /**
     * 使用 CountDownLatch 来启动和停止线程
     * @param nThreads 要启动的线程数
     * @param task 线程任务
     * @return 线程等待，释放时间
     * @throws InterruptedException 当线程等待、休眠或以其他方式被占用，并且线程在活动之前或期间被中断时抛出
     */
    public long timeTasks(int nThreads, final Runnable task)
            throws InterruptedException {
        // 在线程可以通过await()方法之前，计数值为1，需调用countDown()方法的次数为1次
        final CountDownLatch startGate = new CountDownLatch(1);
        // 在线程可以通过await()方法之前，计数值为 nThreads，需调用countDown()方法的次数为 nThreads 次
        final CountDownLatch endGate = new CountDownLatch(nThreads);
        // 通过await()方法需满足：锁计数器为0、线程被中断、或者超过指定的等待时间
        // 每调用countDown()方法，计数器减1

        for (int i = 0; i < nThreads; i++) {
            Thread aThread = new Thread() {
                @Override
                public void run() {
                    try {
                        // 线程等待
                        startGate.await();
                        try {
                            // 启动线程
                            task.run();
                        } finally {
                            // 减少锁存器的计数，如果计数达到零，则释放所有等待线程
                            endGate.countDown();
                        }
                    } catch (InterruptedException ignored) {}
                }
            };
            aThread.start();
        }

        long start = System.nanoTime();
        // startGate 锁计数器(原本定义为1)，现在调用 countDown() 方法，减去1
        // 此时，startGate 的锁计数器为0，可以通过 await() 方法了
        // 因此就可以到达 task.run()
        startGate.countDown();
        // endGate 锁计数器原本定义为nThreads，也就是线程个数。
        // 程序未出现异常情况，需要调用 countDown() 方法 nThreads 次
        // endGate.countDown() 方法在每个线程的 finally {...} 中
        // 因此需要 nThreads 个线程都完成，才能使得 锁计数器=0，这个时候才能通过 endGate.await() 方法
        endGate.await();
        long end = System.nanoTime();
        return end - start;
    }
}