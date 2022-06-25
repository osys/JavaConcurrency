import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p><b>{@link ThreadDeadlock} Description</b>: 在单线程化的 Executor 中死锁的任务。
 * </p>
 * <p>Created by lyh on 2022/06/17 00:11.</p>
 */
public class ThreadDeadlock {
    // single executor
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class LoadFileTask implements Callable<String> {
        private final String fileName;

        public LoadFileTask(String fileName) {
            this.fileName = fileName;
        }

        public String call() throws Exception {
            // 读取文件
            return "";
        }
    }

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();

            return header.get() + page + footer.get();
        }

        private String renderBody() {
            // 页面渲染
            return "";
        }
    }

    public void test() {
        RenderPageTask mainTask = new RenderPageTask();
        exec.submit(mainTask);
    }
}
