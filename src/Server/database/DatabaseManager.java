package Server.database;

import java.sql.*;

public class DatabaseManager {
    static {
        try {
            // Явная регистрация драйвера
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    
    private static final String URL = "jdbc:postgresql://localhost:5432/studygroups_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}