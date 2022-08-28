import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class InduceLockOrder {
    private static final Object tieLock = new Object();

    public static void transferMoney(final Account fromAcct,
                                     final Account toAcct,
                                     final DollarAmount amount)
            throws InsufficientFundsException {
        class Helper {
            public void transfer() throws InsufficientFundsException {
                // 如果【借出钱】的人账户 amount 没这么多钱，那么就抛出【资金不足】Exception
                if (fromAcct.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    // 资金充足，交易完成
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }
        // 【借出钱者】、【借入钱者】分别对应的 Object.hashCode()
        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);

        // 制定获得锁顺序
        if (fromHash < toHash) {
            synchronized (fromAcct) {
                synchronized (toAcct) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAcct) {
                synchronized (fromAcct) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAcct) {
                    synchronized (toAcct) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    static class DollarAmount implements Comparable<DollarAmount> {

        private int amount = 0;

        public DollarAmount(int amount) {
            this.amount = amount;
        }

        /** 增加 */
        public DollarAmount add(DollarAmount d) {
            this.amount = this.amount + d.amount;
            return this;
        }

        /** 减少 */
        public DollarAmount subtract(DollarAmount d) {
            this.amount = this.amount - d.amount;
            return this;
        }

        /** -1, 0, 1  ---- 小于，等于，大于 */
        public int compareTo(DollarAmount dollarAmount) {
            return Integer.compare(this.amount, dollarAmount.amount);
        }

        public int getAmount() {
            return amount;
        }
    }

    static class Account {
        private DollarAmount balance;
        private final int acctNo;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            acctNo = sequence.incrementAndGet();
        }

        /** 将钱借出 */
        void debit(DollarAmount d) {
            balance = balance.subtract(d);
        }

        /** 去借钱 */
        void credit(DollarAmount d) {
            balance = balance.add(d);
        }

        DollarAmount getBalance() {
            return balance;
        }

        public void setBalance(DollarAmount balance) {
            this.balance = balance;
        }

        int getAcctNo() {
            return acctNo;
        }
    }

    static class InsufficientFundsException extends Exception {
    }
}
