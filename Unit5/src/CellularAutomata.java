import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 在一个细胞的自动系统中用 CyclicBarrier 协调计算
 * Created by osys on 2022/08/28 21:48.
 */
public class CellularAutomata {
    /** 细胞 */
    private final Board mainBoard;
    /** 关卡 */
    private final CyclicBarrier barrier;
    /** 工作蛋白 */
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        // 细胞
        this.mainBoard = board;
        // Java 虚拟机可用的处理器数量
        int count = Runtime.getRuntime().availableProcessors();
        // 关卡
        this.barrier = new CyclicBarrier(count, new Runnable() {
                            @Override
                            public void run() {
                                // 细胞提交新 value
                                mainBoard.commitNewValues();
                            }
                        });
        // 工作蛋白
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            // 设置子细胞
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    private class Worker implements Runnable {
        /** 细胞 */
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            // 细胞还没转换好
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++) {
                    for (int y = 0; y < board.getMaxY(); y++) {
                        // 设置新的 x，y 值
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }
                try {
                    // 关卡（细胞所有的位置都转换好了，才可继续）---- 所有线程都运行到这里才可以继续
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    return;
                }
            }
        }

        private int computeValue(int x, int y) {
            // 在 (x,y) 里计算新 value
            return 0;
        }
    }

    public void start() {
        // 启动线程
        for (Worker worker : workers) {
            new Thread(worker).start();
        }
        // 等待细胞转换
        mainBoard.waitForConvergence();
    }

    /**
     * 细胞
     */
    interface Board {
        /**
         * x 坐标
         * @return x
         */
        int getMaxX();

        /**
         * y 坐标
         * @return y
         */
        int getMaxY();

        /**
         * x，y 对应的 value
         * @param x x
         * @param y y
         * @return value
         */
        int getValue(int x, int y);

        /**
         * x，y 设置新的 value
         * @param x x
         * @param y y
         * @param value value
         * @return 新 value
         */
        int setNewValue(int x, int y, int value);

        /** 提交新 value */
        void commitNewValues();

        /**
         * 是否转换好
         * @return true/false
         */
        boolean hasConverged();

        /**
         * 等待转换
         */
        void waitForConvergence();

        /**
         * 获取子细胞
         * @param numPartitions 分区
         * @param index 所处分区
         * @return 子细胞
         */
        Board getSubBoard(int numPartitions, int index);
    }
}
