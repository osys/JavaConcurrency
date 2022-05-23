import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

/**
 * @author osys
 */
public class BrokenPrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;

    private volatile boolean cancelled = false;

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger prime = BigInteger.ONE;
            while (!cancelled) {
                // 返回大于此 prime 且可能是素数的第一个整数
                prime = prime.nextProbablePrime();
                queue.put(prime);
            }
        } catch (InterruptedException ignored) { }
    }

    public void cancel() {
        cancelled = true;
    }
}

