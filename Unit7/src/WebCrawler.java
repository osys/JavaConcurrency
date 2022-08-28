import net.jcip.annotations.GuardedBy;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * 网络爬虫
 *
 *
 * Created by osys on 2022/08/28 21:48.
 */
public abstract class WebCrawler {
    /** 线程池：关闭该线程池，能获得正在执行的线程任务 */
    private volatile TrackingExecutor exec;
    /** 要爬取的 url */
    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<URL>();
    /** 已经爬取的 */
    private final ConcurrentMap<URL, Boolean> seen = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 500;
    private static final TimeUnit UNIT = MILLISECONDS;

    public WebCrawler(URL startUrl) {
        urlsToCrawl.add(startUrl);
    }

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());
        for (URL url : urlsToCrawl) {
            submitCrawlTask(url);
        }
        urlsToCrawl.clear();
    }

    /** 关闭线程池，将正在执行的爬虫程序对应的 url 保存起来到【urlsToCrawl】对象中 */
    public synchronized void stop() throws InterruptedException {
        try {
            saveUnCrawled(exec.shutdownNow());
            if (exec.awaitTermination(TIMEOUT, UNIT)) {
                saveUnCrawled(exec.getCancelledTasks());
            }
        } finally {
            exec = null;
        }
    }

    private void saveUnCrawled(List<Runnable> unCrawled) {
        for (Runnable task : unCrawled) {
            urlsToCrawl.add(((CrawlTask) task).getPage());
        }
    }

    /** 启动爬虫task，爬取url */
    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }

    /** 爬虫 */
    private class CrawlTask implements Runnable {
        private final URL url;

        CrawlTask(URL url) {
            this.url = url;
        }

        private int count = 1;

        /** 已经爬取的 url */
        boolean alreadyCrawled() {
            return seen.putIfAbsent(url, true) != null;
        }


        void markUnCrawled() {
            seen.remove(url);
            System.out.printf("marking %s unCrawled%n", url);
        }

        /** 爬取 */
        public void run() {
            for (URL link : processPage(url)) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                submitCrawlTask(link);
            }
        }

        public URL getPage() {
            return url;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private class TrackingExecutor extends AbstractExecutorService {
        private final ExecutorService exec;

        /** 正在执行，且为完成的 runnable */
        private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());

        public TrackingExecutor(ExecutorService exec) {
            this.exec = exec;
        }

        public void shutdown() {
            exec.shutdown();
        }

        public List<Runnable> shutdownNow() {
            return exec.shutdownNow();
        }

        public boolean isShutdown() {
            return exec.isShutdown();
        }

        public boolean isTerminated() {
            return exec.isTerminated();
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return exec.awaitTermination(timeout, unit);
        }

        public List<Runnable> getCancelledTasks() {
            if (!exec.isTerminated()) {
                throw new IllegalStateException("线程池未关闭");
            }
            return new ArrayList<Runnable>(tasksCancelledAtShutdown);
        }

        public void execute(final Runnable runnable) {
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        runnable.run();
                    } finally {
                        // 线程池关闭，且当前线程被标志为中断状态，那么将当前线程添加到 set 集合中
                        if (isShutdown() && Thread.currentThread().isInterrupted()) {
                            tasksCancelledAtShutdown.add(runnable);
                        }
                    }
                }
            });
        }
    }

}



