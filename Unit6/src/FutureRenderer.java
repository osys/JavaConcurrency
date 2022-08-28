import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public abstract class FutureRenderer {
    /** 创建线程池 */
    private final ExecutorService executor = Executors.newCachedThreadPool();

    /** 页面资源渲染 */
    void renderPage(CharSequence source) {
        // 图片信息集合
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        // 创建一个 task，该 task 返回【图片数据】集合
        Callable<List<ImageData>> task =
                new Callable<List<ImageData>>() {
                    @Override
                    public List<ImageData> call() {
                        List<ImageData> result = new ArrayList<>();
                        // 从【图片信息集】中，下载所有的【图片数据】
                        for (ImageInfo imageInfo : imageInfos) {
                            result.add(imageInfo.downloadImage());
                        }
                        return result;
                    }
                };
        // 将 Callable 提交给 Executor
        Future<List<ImageData>> future = executor.submit(task);

        // 文本渲染
        renderText(source);

        try {
            // 如果 Executor 已经将 task 执行完成，返回【图片数据】
            // 那么将【图片数据】进行渲染
            List<ImageData> imageData = future.get();
            for (ImageData data : imageData) {
                renderImage(data);
            }
        } catch (InterruptedException e) {
            // 中断线程
            Thread.currentThread().interrupt();
            // 取消任务
            future.cancel(true);
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
}


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
