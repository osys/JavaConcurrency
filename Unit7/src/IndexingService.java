import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class IndexingService {
    private static final int CAPACITY = 1000;
    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(File root, final FileFilter fileFilter) {
        this.root = root;
        this.queue = new LinkedBlockingQueue<File>(CAPACITY);
        // 创建一个文件过滤器对象，该 root 下的所有文件，会被生产者以这过滤器为条件，将所有文件使用【文件爬虫】添加到队列中
        this.fileFilter = new FileFilter() {
            /**
             * 测试指定的抽象路径名是否应包含在路径名列表中
             * @param pathname 路径名
             * @return true/false
             */
            public boolean accept(File pathname) {
                return pathname.isDirectory() || fileFilter.accept(pathname);
            }
        };
    }

    /** 文件是否已经加入到队列中，false表示未加入 */
    private boolean alreadyIndexed(File f) {
        return false;
    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop() {
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    /**
     * 生产者：文件爬虫
     */
    class CrawlerThread extends Thread {
        public void run() {
            try {
                // 检索文件
                crawl(root);
            } catch (InterruptedException e) {
            } finally {
                while (true) {
                    try {
                        // 将药丸添加到队列中
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) {}
                }
            }
        }

        /**
         * 检索文件
         * @param root 文件/目录
         */
        private void crawl(File root) throws InterruptedException {
            // 【文件集合 + 文件夹集合】 的 path
            File[] entries = root.listFiles(fileFilter);
            // 检索文件 path，并添加到队列中
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory()) {
                        crawl(entry);
                    } else if (!alreadyIndexed(entry)) {
                        queue.put(entry);
                    }
                }
            }
        }
    }

    /**
     * 消费者：对检索出来的文件进行处理
     */
    class IndexerThread extends Thread {

        /** 对检索出来的文件进行处理，知道遇到【药丸】文件 */
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON)
                        break;
                    else
                        indexFile(file);
                }
            } catch (InterruptedException consumed) {
            }
        }

        /** 处理文件 */
        public void indexFile(File file) {
        };
    }
}
