import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class SingleThreadWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        // 顺序执行，接受连接，处理连接
        while (true) {
            Socket connection = socket.accept();
            handleRequest(connection);
        }
    }

    private static void handleRequest(Socket connection) {
        // 处理逻辑
    }
}
