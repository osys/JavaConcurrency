import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class RenderWithTimeBudget {
    private static final Ad DEFAULT_AD = new Ad();
    private static final long TIME_BUDGET = 1000;
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        // 提交任务
        Future<Ad> adFuture = exec.submit(new FetchAdTask());
        // 在等待广告时，进行渲染页面
        Page page = renderPageBody();
        Ad ad;
        try {
            long timeLeft = endNanos - System.nanoTime();
            // 在规定时间内，获取广告。否则默认没有广告
            ad = adFuture.get(timeLeft, NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            // 没有在规定时间内获取到广告，取消任务
            ad = DEFAULT_AD;
            adFuture.cancel(true);
        }
        // 广告渲染
        page.setAd(ad);
        return page;
    }

    Page renderPageBody() { return new Page(); }


    /** 广告 */
    static class Ad {
    }

    /** 页面 */
    static class Page {
        public void setAd(Ad ad) { }
    }

    /** 添加广告的 task */
    static class FetchAdTask implements Callable<Ad> {
        @Override
        public Ad call() {
            return new Ad();
        }
    }

}

