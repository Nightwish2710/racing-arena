package serverdatabase;

import java.sql.*;

public class ServerDBHelper {
    private Connection conn = null;

    // Creating singleton
    private static ServerDBHelper serverDBHelper = null;
    public static ServerDBHelper getInstance() {
        if (serverDBHelper == null) {
            serverDBHelper = new ServerDBHelper();
        }
        return serverDBHelper;
    }

    public ServerDBHelper() {
        try {
            // Register JDBC driver
            Class.forName(ServerDBConfig.JDBC_DRIVER_H2);

            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(ServerDBConfig.DB_URL_H2,ServerDBConfig.DB_USER_H2,ServerDBConfig.DB_PASS_H2);
            serverDBHelper = this;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDB() {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean isDBOpenning() {
        try {
            return conn!=null && !conn.isClosed();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }

    public void exec (String query) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {}
        }
    }

    public ResultSet execForResult (String query) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {}
        return null;
    }
}
