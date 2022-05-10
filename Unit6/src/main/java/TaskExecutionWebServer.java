import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author osys
 */
public class TaskExecutionWebServer {
    /** 线程数 */
    private static final int THEAD_NUM = 100;
    /** 线程池 */
    private static final Executor EXEC = new ThreadPoolExecutor(THEAD_NUM, THEAD_NUM, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(100));

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            EXEC.execute(task);
        }
    }

    /**
     *
     * @param connection
     */
    private static void handleRequest(Socket connection) {
        // 处理逻辑
    }
}
