package serverdatabase;

public class ServerDBConfig {
    // JDBC H2 driver name and database URL
    public static final String JDBC_DRIVER_H2 = "org.h2.Driver";
    public static final String DB_URL_H2 = "jdbc:h2:~/Racing Arena/racing-arena-db";

    // database credentials
    public static final String DB_USER_H2 = "sa";
    public static final String DB_PASS_H2 = " ";

    // database constant
    public static final String TABLE_RACER = "RACER_ACCOUNT";
    public static final String TABLE_RACER_username = "username";
    public static final String TABLE_RACER_password = "password";
    public static final String TABLE_RACER_isonline = "isonline";
    public static final String TABLE_RACER_victory = "victory";

    // query statement
    public static final String CREATE_TABLE_RACER = "CREATE TABLE IF NOT EXISTS " + TABLE_RACER
            + " ("+ TABLE_RACER_username + " VARCHAR(11), "
            + TABLE_RACER_password + " VARCHAR(17), "
            + TABLE_RACER_victory + " INTEGER, "
            + TABLE_RACER_isonline + " INTEGER, "
            + "PRIMARY KEY ( " + TABLE_RACER_username + " ))";
}
