package no.hvl.dat110.poker;
import java.sql.*;

public class DatabaseManager {
    // UPDATE THESE WITH YOUR PGADMIN DETAILS
    private static final String DB_URL = "jdbc:postgresql://ider-database.westeurope.cloudapp.azure.com:5433/h184536?currentSchema=poker";
    private static final String USER = "h184536";
    private static final String PASS = "pass";

    // Establish connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Register a new player
    public static boolean registerPlayer(String username, String password) {
        String salt = SecurityUtils.generateSalt();
        String hash = SecurityUtils.hashPassword(password, salt);

        String sql = "INSERT INTO players (username, password_hash, salt, chips) VALUES (?, ?, ?, 1000)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, hash);
            pstmt.setString(3, salt);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Login a player and return their chip count (-1 if failed)
    public static int loginPlayer(String username, String password) {
        String sql = "SELECT password_hash, salt, chips FROM players WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                String salt = rs.getString("salt");
                int chips = rs.getInt("chips");

                if (SecurityUtils.verifyPassword(password, salt, storedHash)) {
                    return chips;
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return -1; // Login failed
    }

    // Update chips in database
    public static void updateChips(String username, int newBalance) {
        String sql = "UPDATE players SET chips = ? WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newBalance);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
            System.out.println("Chips updated for " + username);
        } catch (SQLException e) {
            System.err.println("Update error: " + e.getMessage());
        }
    }
}
