import java.sql.*;

/**
 * Manages all database operations for the poker application,
 * including player registration, authentication, and chip balance updates.
 */
public class DatabaseManager {
    // UPDATE THESE WITH YOUR PGADMIN DETAILS
    private static final String DB_URL = "jdbc:postgresql://ider-database.westeurope.cloudapp.azure.com:5433/h184536?currentSchema=poker";
    private static final String USER = "h184536";
    private static final String PASS = "pass";

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     *
     * @return a {@link Connection} to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Registers a new player with a hashed password and a starting balance of 1000 chips.
     *
     * @param username the desired username for the new player
     * @param password the plain-text password to hash and store
     * @return true if registration succeeded, false otherwise
     */
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

    /**
     * Authenticates a player and returns their chip balance.
     *
     * @param username the username of the player
     * @param password the plain-text password to verify
     * @return the player's chip balance on success, or -1 if login failed
     */
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

    /**
     * Updates the chip balance for the given player in the database.
     *
     * @param username   the username of the player to update
     * @param newBalance the new chip balance to store
     */
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