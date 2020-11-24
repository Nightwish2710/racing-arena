package server_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerDBHelper {
    private Connection conn = null;
    private Statement stmt = null;

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
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void exec (String query) {
        try {
            stmt = conn.createStatement();
            stmt.execute(query);

            stmt.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            //finally block used to close resources
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException se2) {
            } // nothing we can do

            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
    }
}
