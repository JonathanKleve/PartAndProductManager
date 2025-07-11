package kleve.PartAndProductManager.DAO;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Abstract class for managing JDBC database connections.
 * This class provides static methods for opening and closing connections to a MySQL database.
 * @author Jonathan Kleve
 */
public abstract class JDBC {

    /**
     * The JDBC protocol.
     */
    private static final String protocol = "jdbc";

    /**
     * The database vendor (MySQL).
     */
    private static final String vendor = ":mysql:";

    /**
     * The database location (localhost).
     */
    private static final String location = "//localhost/";

    /**
     * The name of the database.
     */
    private static final String databaseName = "capstone_db";

    /**
     * The complete JDBC URL for the database connection.
     */
    private static final String jdbUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";

    /**
     * The JDBC driver class name.
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * The database username.
     */
    private static final String userName = "sqlUser";

    /**
     * The database password.
     */
    private static final String password = "Passw0rd!";

    /**
     * The database connection object.
     */
    public static Connection connection;

    /**
     * Opens a connection to the MySQL database.
     * This method loads the JDBC driver and establishes a connection using the provided credentials.
     */
    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbUrl, userName, password);
            System.out.println("Connection Successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the database connection.
     * This method closes the current database connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection Closed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}