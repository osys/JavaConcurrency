import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ConcurrentPuzzleSolver <P, M> {

    /** 搬箱子谜题 */
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    /** 箱子是否存在该位置 */
    private final ConcurrentMap<P, Boolean> seen;
    /** 可携带结果的闭锁 */
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        // Return ThreadPoolExecutor Object
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<P, Boolean>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) exec;
            // 被拒绝任务，它默默地丢弃被拒绝的任务。
            threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    /** 解决者 */
    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // 阻塞，知道找到一个方案
            PuzzleNode<P, M> solutionPuzzleNode = solution.getValue();
            return (solutionPuzzleNode == null) ? null : solutionPuzzleNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P position, M move, PuzzleNode<P, M> puzzleNode) {
        return new SolverTask(position, move, puzzleNode);
    }

    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            // 已找到一个解决方案，或者该位置曾经到达过
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null) {
                return;
            }
            // 移动位置是否为本位置
            if (!puzzle.isGoal(pos)) {
                // 获取每个合法移动
                for (M move : puzzle.legalMoves(pos)) {
                    exec.execute(
                            // 创建一个新的 task 进行位置移动
                            newTask(puzzle.move(pos, move),
                                    move,
                                    this)
                    );
                }
            } else {
                // 移动位置为本位置
                solution.setValue(this);
            }
        }
    }
}

@ThreadSafe
class ValueLatch <T> {
    @GuardedBy("this")
    private T value = null;
    /** 一种同步辅助，允许一个或多个线程等待，直到在其他线程中执行的一组操作完成。 */
    private final CountDownLatch done = new CountDownLatch(1);

    public boolean isSet() {
        return (done.getCount() == 0);
    }

    public synchronized void setValue(T newValue) {
        if (!isSet()) {
            value = newValue;
            // Decrease ---> 【done.getCount() - 1】
            done.countDown();
        }
    }

    public T getValue() throws InterruptedException {
        // 当前线程等待直到锁存器倒计时到零
        done.await();
        synchronized (this) {
            return value;
        }
    }
}
