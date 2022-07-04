import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class BackgroundTask <V> implements Runnable, Future<V> {
    private final FutureTask<V> computation = new Computation();

    /** 计算 task */
    private class Computation extends FutureTask<V> {
        public Computation() {
            // 创建一个 task，该 FutureTask.get() 返回 BackgroundTask.compute() 方法的计算结果
            super(new Callable<V>() {
                public V call() throws Exception {
                    return BackgroundTask.this.compute();
                }
            });
        }

        /** 当此任务转换到状态 isDone 时调用 */
        protected final void done() {
            // GuiExecutor 实例获取计算结果
            GuiExecutor.instance().execute(new Runnable() {
                public void run() {
                    V value = null;
                    Throwable thrown = null;
                    boolean cancelled = false;
                    try {
                        value = BackgroundTask.this.get();
                    } catch (ExecutionException e) {
                        thrown = e.getCause();
                    } catch (CancellationException e) {
                        cancelled = true;
                    } catch (InterruptedException consumed) {
                    } finally {
                        // 在事件线程中调用
                        onCompletion(value, thrown, cancelled);
                    }
                };
            });
        }
    }

    // 在后台线程中调用
    protected abstract V compute() throws Exception;

    // 在事件线程中调用
    protected void onCompletion(V result, Throwable exception,
                                boolean cancelled) {
    }

    protected void onProgress(int current, int max) {
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return computation.cancel(mayInterruptIfRunning);
    }

    public V get() throws InterruptedException, ExecutionException {
        return computation.get();
    }

    public V get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return computation.get(timeout, unit);
    }

    public boolean isCancelled() {
        return computation.isCancelled();
    }

    public boolean isDone() {
        return computation.isDone();
    }

    public void run() {
        computation.run();
    }
}
