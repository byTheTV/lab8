package Server.services;

import Common.models.User;
import Server.database.DatabaseManager;
import java.sql.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    public User authenticate(String login, String rawPassword) {
        // Получение соли и хеша из БД
        String sql = "SELECT id, password_hash, salt FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String salt = rs.getString("salt");

                // Хеширование пароля с солью на сервере
                String calculatedHash = hashPassword(rawPassword, salt);

                if (storedHash.equals(calculatedHash)) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setLogin(login);
                    return user;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(salt.getBytes());
        byte[] hashedBytes = md.digest(password.getBytes());
        return bytesToHex(hashedBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}