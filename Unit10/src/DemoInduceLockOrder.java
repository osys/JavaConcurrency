import java.util.Random;

public class DemoInduceLockOrder {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1000000;

    public static void main(String[] args) {
        final Random rnd = new Random();
        // 五个账户，每个账户先给它两个小目标 $
        final InduceLockOrder.Account[] accounts = new InduceLockOrder.Account[NUM_ACCOUNTS];
        for (int i = 0; i < accounts.length; i++) {
            InduceLockOrder.Account account = new InduceLockOrder.Account();
            account.setBalance(new InduceLockOrder.DollarAmount(200000000));
            accounts[i] = account;
        }

        class TransferThread extends Thread {
            public void run() {
                // 这五个账户之间发生了多笔不可描述的金钱交易
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    // 随机抽取两位幸运儿，发生金钱关系，金额不大于1000
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    InduceLockOrder.DollarAmount amount =
                            new InduceLockOrder.DollarAmount(rnd.nextInt(1000));
                    try {
                        InduceLockOrder.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                        System.out.println("线程【" + Thread.currentThread().getName() + "】运行第" + i + "次------ " +
                                "账户【" + fromAcct + "】向账户【" + toAcct + "】转账" + amount.getAmount() + "$ ------ " +
                                "账户【" + fromAcct + "】剩下 " + accounts[fromAcct].getBalance().getAmount() + "$ ------ " +
                                "账户【" + toAcct + "】剩下 " + accounts[toAcct].getBalance().getAmount() + "$");
                    } catch (InduceLockOrder.InsufficientFundsException ignored) {
                    }
                }
            }
        }

        // 多线程交易！！！
        for (int i = 0; i < NUM_THREADS; i++) {
            TransferThread transferThread = new TransferThread();
            transferThread.start();
        }
    }
}
