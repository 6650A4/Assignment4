package DBConnection;

import java.sql.Connection;

import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionManager {


    private static BasicDataSource dss = new BasicDataSource();
    private static final String user = "admin";
    //    private static final String user = "root";
//    private static final String password = "";
    private static final String password = "";
    private static final String hostName = "database-1.cbbidv99xyac.us-east-1.rds.amazonaws.com";
//    private static final String hostName = "34.94.83.65";

    private static final int port = 3306;
    private static final String schema = "SkiAPI";

    static {
        String JDBC_URL = "jdbc:mysql://" + hostName + ":" + port + "/" + schema + "?useSSL=False";
        dss.setDriverClassName("com.mysql.jdbc.Driver");
        dss.setUrl(JDBC_URL);
        dss.setUsername(user);
        dss.setPassword(password);

        dss.setMaxIdle(100);
        dss.setMaxTotal(200);

    }

    public static Connection getConnection() throws SQLException {
        return dss.getConnection();
    }

    public ConnectionManager() {}

    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

//
//    public static void main(String[] args) {
//        ConnectionManager connectionManager = new ConnectionManager();
//        try {
//            connectionManager.getConnection();
//        } catch (Exception ex) {
//
//        }
//    }
}
