import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;

/**
 * @author osys
 */
public class ThreadPerTaskWebServer2 implements Executor {

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        // 并发处理请求
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    handleRequest(connection);
                }
            };
            // 为每个任务启动创建一个新线程的 Executor
            new ThreadPerTaskWebServer2().execute(task);
        }
    }

    private static void handleRequest(Socket connection) {
        // 处理逻辑
    }

    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
