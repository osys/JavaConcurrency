import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author osys
 */
public class ConnectionDispenser {
    static String DB_URL = "jdbc:mysql://localhost/test";

    private ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        @Override
        public Connection initialValue() {
            try {
                return DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                throw new RuntimeException("无法获取连接, e");
            }
        }
    };

    public Connection getConnection() {
        return connectionHolder.get();
    }

    public void setConnection(ThreadLocal<Connection> connection) {
        this.connectionHolder = connection;
    }
}