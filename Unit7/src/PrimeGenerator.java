import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 生成素数
 * @author osys
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();

    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger prime = BigInteger.ONE;
        while (!cancelled) {
            // 返回大于此 prime 且可能是素数的第一个整数
            prime = prime.nextProbablePrime();
            synchronized (this) {
                primes.add(prime);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }

    /**
     * 生成【素数】的程序运行一秒钟
     * @return 素数
     */
    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try {
            // 主线程sleep1秒
            SECONDS.sleep(1);
        } finally {
            // 暂停生成素数的线程
            generator.cancel();
        }
        return generator.get();
    }
}
