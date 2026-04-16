import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification using SHA-256 and random salts.
 */
public class SecurityUtils {

    /**
     * Generates a cryptographically random Base64-encoded salt.
     *
     * @return a Base64-encoded string representing the generated salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password combined with the given salt using SHA-256.
     *
     * @param password the plain-text password to hash
     * @param salt     the Base64-encoded salt to apply before hashing
     * @return a Base64-encoded string of the resulting hash
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verifies a plain-text password against a stored hash by hashing it with the same salt.
     *
     * @param password   the plain-text password to verify
     * @param salt       the Base64-encoded salt used when the password was originally hashed
     * @param storedHash the Base64-encoded hash to compare against
     * @return true if the password matches the stored hash, false otherwise
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) {
        String newHash = hashPassword(password, salt);
        return newHash.equals(storedHash);
    }
}