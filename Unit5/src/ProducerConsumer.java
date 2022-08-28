import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 文件搜索应用程序中的生产者和消费者
 * Created by osys on 2022/08/28 21:48.
 */
public class ProducerConsumer {

    /**
     * 文件检索工具类（生产者）
     * 将给定的文件夹，获取里面的文件，添加到队列
     * 将给定的文件，添加到队列
     */
    static class FileCrawler implements Runnable {
        /** 文件阻塞队列 */
        private final BlockingQueue<File> fileQueue;
        /** 文件过滤器 */
        private final FileFilter fileFilter;
        /** 文件 */
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue,
                           final FileFilter fileFilter,
                           File root) {
            this.fileQueue = fileQueue;
            this.root = root;
            this.fileFilter = new FileFilter() {
                /**
                 * 测试指定的抽象路径名是否应包含在路径名列表中
                 * @param pathname 路径名
                 * @return true/false
                 */
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory() || fileFilter.accept(pathname);
                }
            };
        }

        /**
         * 文件是否已经加入到阻塞队列
         * @param file 文件
         * @return false 表示已经加入
         */
        private boolean alreadyIndexed(File file) {
            return false;
        }

        @Override
        public void run() {
            try {
                crawl(root);
            } catch (InterruptedException e) {
                // 中断线程
                Thread.currentThread().interrupt();
            }
        }

        /**
         * 文件检索(将文件添加到队列，文件夹忽略掉)
         * @param root 要被检索的文件
         */
        private void crawl(File root) throws InterruptedException {
            // 获取一个抽象路径名数组(该路径对应可能是文件，也可能是文件夹)
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if (entry.isDirectory()) {              // 如果是路径，继续检索
                        crawl(entry);
                    } else if (!alreadyIndexed(entry)) {    // 文件未加入队列。将文件加入阻塞队列
                        fileQueue.put(entry);
                    }
                }
            }
        }
    }

    /**
     * 消费者，消费文件
     */
    static class Indexer implements Runnable {
        /** 文件阻塞队列 */
        private final BlockingQueue<File> queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    indexFile(queue.take());
                }
            } catch (InterruptedException e) {
                // 中断线程
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file) {
            // 消费文件
        };
    }


    /** 队列大小 */
    private static final int BOUND = 10;
    /** Java 虚拟机可用的处理器数量 */
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    /**
     * 开始桌面搜索
     * @param roots 文件数组
     */
    public static void startIndexing(File[] roots) {
        BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
        FileFilter filter = new FileFilter() {
            /**
             * 测试指定的抽象路径名是否应包含在路径名列表中
             * @param pathname 路径名
             * @return true/false
             */
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };

        // 检索文件，添加到阻塞队列中
        for (File root : roots) {
            new Thread(new FileCrawler(queue, filter, root)).start();
        }

        // 消费者，消费文件
        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }
}
