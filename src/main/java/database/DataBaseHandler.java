package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseHandler {
    private static final String USERNAME = "";
    private static final String URL = "";
    private static final String PASSWORD = "";
    private Connection dbConnection;

    public DataBaseHandler() {
        try {
            dbConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return dbConnection;
    }
}
