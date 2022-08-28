import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class LifecycleWebServer {

    private  int THREAD_NUM = 10;
    /** 创建执行器 */
    private final ExecutorService exec = new ThreadPoolExecutor(THREAD_NUM, THREAD_NUM, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(100));

    public void start() throws IOException {
        // 创建 socket 连接
        ServerSocket socket = new ServerSocket(80);
        // ExecutorService 未关闭
        while (!exec.isShutdown()) {
            try {
                // 接受Socket连接
                final Socket conn = socket.accept();
                // 向 ExecutorService 提交任务
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        handleRequest(conn);
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown()) {
                    log("任务提交被拒绝", e);
                }
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    private void log(String msg, Exception e) {
        Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
    }

    /** 处理逻辑 */
    void handleRequest(Socket connection) {
        Request req = readRequest(connection);
        if (isShutdownRequest(req)) {
            stop();
        } else {
            dispatchRequest(req);
        }
    }

    interface Request {
    }

    private Request readRequest(Socket s) {
        return null;
    }

    private void dispatchRequest(Request r) {
    }

    private boolean isShutdownRequest(Request r) {
        return false;
    }
}
