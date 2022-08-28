import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public abstract class TransformingSequential {

    /** 顺序执行 */
    void processSequentially(List<Element> elements) {
        for (Element e : elements) {
            process(e);
        }
    }

    /** 并行执行 */
    void processInParallel(Executor exec, List<Element> elements) {
        for (final Element e : elements) {
            exec.execute(() -> process(e));
        }
    }

    public abstract void process(Element e);

    /** 顺序递归 */
    public <T> void sequentialRecursive(List<Node<T>> nodes,
                                        Collection<T> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    /** 并行递归 */
    public <T> void parallelRecursive(final Executor exec,
                                      List<Node<T>> nodes,
                                      final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(() -> results.add(n.compute()));
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    /** 执行并行递归，获取结果 */
    public <T> Collection<T> getParallelResults(List<Node<T>> nodes)
            throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

    interface Element {
    }

    interface Node <T> {
        T compute();
        List<Node<T>> getChildren();
    }
}