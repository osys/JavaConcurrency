import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author osys
 */
public abstract class Renderer {
    private final ExecutorService executor;

    Renderer(ExecutorService executor) {
        // 创建线程池
        this.executor = executor;
    }

    void renderPage(CharSequence source) {
        // 图片信息集合
        final List<ImageInfo> info = scanForImageInfo(source);
        // CompletionService 整合了 Executor 和 BlockingQueue 的功能，
        // 创建一个 CompletionService 对象，将新异步任务的生产与已完成任务的结果消耗相分离
        CompletionService<ImageData> completionService = new ExecutorCompletionService<>(executor);

        for (final ImageInfo imageInfo : info) {
            // 提交任务
            completionService.submit(new Callable<ImageData>() {
                @Override
                public ImageData call() {
                    // 下载图片
                    return imageInfo.downloadImage();
                }
            });
        }

        // 文本渲染
        renderText(source);

        try {
            // 图片渲染
            for (int t = 0, n = info.size(); t < n; t++) {
                // 在 CompletionService 中，task 会被其交给 Executor 进行执行
                // 执行后的的 Future 会放在 BlockingQueue 中，通过 take()、poll() 方法获取
                // 如果 Executor 已经将 task 执行完成，返回【图片数据】
                // 那么将【图片数据】进行渲染
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (InterruptedException e) {
            // 中断线程
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

    /** 图片数据 */
    interface ImageData {
    }

    /** 图片信息 */
    interface ImageInfo {
        /** 下载图片数据 */
        ImageData downloadImage();
    }

    /** 文本渲染 */
    abstract void renderText(CharSequence s);

    /** 扫描图片 */
    abstract List<ImageInfo> scanForImageInfo(CharSequence s);

    /** 图片渲染 */
    abstract void renderImage(ImageData i);


    static class LaunderThrowable {

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
}
