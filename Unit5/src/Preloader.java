import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用 FutureTask 顶载稍后需要的數据
 * @author osys
 */
public class Preloader {
    /** 加载产品信息 */
    ProductInfo loadProductInfo() throws DataLoadException {
        return null;
    }

    /** 创建一个FutureTask ，它将在运行时执行给定的Callable */
    private final FutureTask<ProductInfo> future = new FutureTask<>(new Callable<ProductInfo>() {
            @Override
            public ProductInfo call() throws DataLoadException {
                return loadProductInfo();
            }
        });

    private final Thread thread = new Thread(future);

    public void start() { thread.start(); }

    public ProductInfo get()
            throws DataLoadException, InterruptedException {
        try {
            // 为已完成的任务返回结果或抛出异常（Callable执行结果或异常）
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            } else {
                throw LaunderThrowable.launderThrowable(cause);
            }
        }
    }

    interface ProductInfo {
    }
}

/** 数据加载异常 */
class DataLoadException extends Exception { }

/** Throwable 强制转换为 RuntimeException */
class LaunderThrowable {

    /**
     * 将未经检查的 Throwable 抛出。
     *
     * 如果 throwable 是 RuntimeException 则返回 Throwable。
     * 如果 throwable 是 Error 则抛出 Error。
     * 否者抛出 IllegalStateException。
     */
    public static RuntimeException launderThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new IllegalStateException("Not unchecked", throwable);
        }
    }
}
