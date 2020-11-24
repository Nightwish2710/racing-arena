package serverdatabase;

public class ServerDBConfig {
    // JDBC H2 driver name and database URL
    public static final String JDBC_DRIVER_H2 = "org.h2.Driver";
    public static final String DB_URL_H2 = "jdbc:h2:~/Racing Arena/ra-test";

    //  Database credentials
    public static final String DB_USER_H2 = "sa";
    public static final String DB_PASS_H2 = " ";

    // Query
    public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS USER_ACCOUNT (id INTEGER not NULL, first VARCHAR(255), last VARCHAR(255), age INTEGER, PRIMARY KEY ( id ))";
}
