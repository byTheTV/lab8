package Server.services;

import Common.models.User;
import Server.database.DatabaseManager;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {

    public User authenticateOrRegister(String login, String rawPassword) {
        try {
            // Попытка аутентификации

            User user = authenticate(login, rawPassword);
            if (user != null) return user;

            // Проверка, не занят ли логин
            if (isUsernameTaken(login)) {
                System.err.println("Логин уже занят: " + login);
                return null;
            }

            // Регистрация
            return register(login, rawPassword);

        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Ошибка: " + e.getMessage());
            return null;
        }
    }

    private boolean isUsernameTaken(String login) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            return stmt.executeQuery().next();
        }
    }

    private User authenticate(String login, String rawPassword)
            throws SQLException, NoSuchAlgorithmException {

        System.out.println("Попытка аутентификации: " + login);
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String inputHash = hashPassword(rawPassword);

                if (storedHash.equals(inputHash)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setLogin(login);
                    return user;
                }
            }
            return null;
        }
    }

    private User register(String login, String rawPassword)
            throws SQLException, NoSuchAlgorithmException {

        System.out.println("Регистрация нового пользователя: " + login);
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String hashedPassword = hashPassword(rawPassword);
            stmt.setString(1, login);
            stmt.setString(2, hashedPassword);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        User user = new User();
                        user.setId(rs.getInt(1));
                        user.setLogin(login);
                        return user;
                    }
                }
            }
            return null;
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}