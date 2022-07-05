import java.util.concurrent.atomic.AtomicInteger;

public class DynamicOrderDeadlock {
    // Warning: 容易产生死锁
    public static void transferMoney(Account fromAccount,
                                     Account toAccount,
                                     DollarAmount amount)
            throws InsufficientFundsException {
        // 获得【借出钱】锁
        synchronized (fromAccount) {
            // 获得【去借钱】锁
            synchronized (toAccount) {
                // 如果【借出钱】的人账户 amount 没这么多钱，那么就抛出【资金不足】Exception
                if (fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else {
                    // 资金充足，交易完成
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
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
