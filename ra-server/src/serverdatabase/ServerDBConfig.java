package serverdatabase;

public class ServerDBConfig {
    // JDBC H2 driver name and database URL
    public static final String JDBC_DRIVER_H2 = "org.h2.Driver";
    public static final String DB_URL_H2 = "jdbc:h2:~/Racing Arena/ra-test";

    // Database credentials
    public static final String DB_USER_H2 = "sa";
    public static final String DB_PASS_H2 = " ";

    // Database constant
    public static final String TABLE_RACER = "RACER_ACCOUNT_TEST";
    public static final String TABLE_RACER_id = "id";
    public static final String TABLE_RACER_username = "username";
    public static final String TABLE_RACER_password = "password";
    public static final String TABLE_RACER_victory = "victory";

    // Query
    public static final String CREATE_TABLE_RACER = "CREATE TABLE IF NOT EXISTS " + TABLE_RACER
            + " ("+TABLE_RACER_id + " INTEGER not NULL, "
            + TABLE_RACER_username + " VARCHAR(255), "
            + TABLE_RACER_password + " VARCHAR(255), "
            + TABLE_RACER_victory + " INTEGER)";
            //+ "PRIMARY KEY ( " + TABLE_RACER_id + " ))";
}
