package DAL;

import static DAL.CloseConnection.close;

import DBConnection.ConnectionManager;
import Model.Stat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class StatsDao {
    private static final String COUNT = "count";
    private static final String MAX_LATENCY = "maxLatency";
    private static final String SUM_LATENCY = "sumLatency";
    protected ConnectionManager connectionManager;

    private static StatsDao instance = null;
    protected StatsDao() {
        connectionManager = new ConnectionManager();
    }
    public static StatsDao getInstance() {
        if(instance == null) {
            instance = new StatsDao();
        }
        return instance;
    }

    public Stat getStat (String server, String operation) throws SQLException {
        Stat stat = new Stat(server, operation);
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        String selectQuery = "SELECT * FROM Stats WHERE URL=? AND operation=?";
        int sum = 0;
        int count = 0;
        int max = 0;
        try {
            connection = connectionManager.getConnection();
            selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setString(1, server);
            selectStmt.setString(2, operation);
            results = selectStmt.executeQuery();

            if(results.next()) {
                sum = results.getInt("sum");
                count = results.getInt("count");
                max = results.getInt("max");
            }

            stat.setSum(sum);
            stat.setCount(count);
            stat.setMax(max);
        } catch (SQLException e) {
            throw e;
        } finally {
            close(connectionManager, connection, selectStmt, results);
        }
        return stat;
    }

    public void addStat(Map<String, String> stat, String url, String operation) throws SQLException{
        Connection connection = null;
        PreparedStatement insertStmt = null;
        String sqlStmt =
            "INSERT INTO Stats (URL, operation, sum, max, count) "
                + "VALUES (?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "sum = ?"
                + ", max = ?"
                + ", count = ?;";

        try {
            connection = connectionManager.getConnection();
            insertStmt = connection.prepareStatement(sqlStmt);
            insertStmt.setString(1, url);
            insertStmt.setString(2, operation);
            insertStmt.setInt(3, Integer.valueOf(stat.get(SUM_LATENCY)));
            insertStmt.setInt(4, Integer.valueOf(stat.get(MAX_LATENCY)));
            insertStmt.setInt(5, Integer.valueOf(stat.get(COUNT)));
            insertStmt.setInt(6, Integer.valueOf(stat.get(SUM_LATENCY)));
            insertStmt.setInt(7, Integer.valueOf(stat.get(MAX_LATENCY)));
            insertStmt.setInt(8, Integer.valueOf(stat.get(COUNT)));
        } catch (SQLException e) {

        } finally {
            try{
                close(connectionManager, connection, insertStmt, null);
            }catch (SQLException EX){

            }
        }
    }
}
