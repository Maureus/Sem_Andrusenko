package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseHandler {
    private static final String USERNAME = "ST58211";
    private static final String URL = "jdbc:oracle:thin:@fei-sql1.upceucebny.cz:1521:IDAS";
    private static final String PASSWORD = "andr7265357";
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
