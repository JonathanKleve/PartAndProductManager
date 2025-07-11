package kleve.PartAndProductManager.DAO;

import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides data access operations related to user authentication and management.
 * This class is responsible for interacting with the database to validate user credentials.
 *
 * @author Jonathan Kleve
 */
public class UserDAO {
    /**
     * Stores the ID of the currently logged-in user. This static field is updated
     * upon successful login.
     * <p>
     * Note: While convenient, using a static field for `userId` can lead to issues
     * in multi-threaded environments or when managing multiple concurrent users.
     * Consider passing the user ID as a parameter or using a session management
     * pattern for more robust applications.
     * </p>
     */
    public static Integer userId;

    /**
     * Attempts to log in a user by validating their username and password against the database.
     * If authentication is successful, the {@link #userId} static field is updated with the
     * logged-in user's ID.
     * <p>
     * In case of an invalid login attempt, a warning {@link Alert} is displayed to the user
     * in either English or French, depending on the {@code langFlag}.
     * </p>
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @param langFlag An integer flag to determine the language of the warning alert (1 for French, any other value for English).
     * @return {@code true} if the login is successful (username and password match); {@code false} otherwise.
     */
    public static boolean loginUser(String username, String password, int langFlag) {
        try (PreparedStatement ps = JDBC.connection.prepareStatement("SELECT password, user_id FROM users WHERE user_name = ?")) {
            ps.setString(1, username);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    String dbPassword = resultSet.getString("password");
                    if (password.equals(dbPassword)) {
                        userId = resultSet.getInt("user_id");
                        return true;
                    }
                }
            }
            // If we reach here, login failed (either no user found or password mismatch)
            Alert alert = new Alert(Alert.AlertType.WARNING);
            if (langFlag == 1) { // French
                alert.setTitle("Connexion Invalide");
                alert.setHeaderText("Avertissement");
                alert.setContentText("Nom d'utilisateur ou mot de passe invalide.");
            } else { // English
                alert.setTitle("Invalid Login");
                alert.setHeaderText("Warning");
                alert.setContentText("Invalid username or password.");
            }
            alert.showAndWait();
            return false;
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}