import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class DeadlockAvoidance {
    private static Random rnd = new Random();

    public boolean transferMoney(Account fromAccount, Account toAccount, DollarAmount amount,
                                 long timeout, TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {

        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);

        while (true) {
            // 尝试获得【借出钱】锁
            if (fromAccount.lock.tryLock()) {
                try {
                    // 尝试获得【去借钱】锁
                    if (toAccount.lock.tryLock()) {
                        try {
                            // 如果【借出钱】的人账户 amount 没这么多钱，那么就抛出【资金不足】Exception
                            if (fromAccount.getBalance().compareTo(amount) < 0) {
                                throw new InsufficientFundsException();
                            } else {
                                // 资金充足，交易完成
                                fromAccount.debit(amount);
                                toAccount.credit(amount);
                                return true;
                            }
                        } finally {
                            // 释放【去借钱】锁
                            toAccount.lock.unlock();
                        }
                    }
                } finally {
                    // 释放【借出钱】锁
                    fromAccount.lock.unlock();
                }
            }
            if (System.nanoTime() < stopTime) {
                return false;
            }
            NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }

    private static final int DELAY_FIXED = 1;
    private static final int DELAY_RANDOM = 2;

    /** 固定延迟 */
    static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return DELAY_FIXED;
    }

    /** 随机延迟 */
    static long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return DELAY_RANDOM;
    }

    static class DollarAmount implements Comparable<DollarAmount> {
        public int compareTo(DollarAmount other) {
            return 0;
        }

        DollarAmount(int dollars) {
        }
    }

    class Account {
        public Lock lock;

        void debit(DollarAmount d) {
        }

        void credit(DollarAmount d) {
        }

        DollarAmount getBalance() {
            return null;
        }
    }

    class InsufficientFundsException extends Exception {
    }
}