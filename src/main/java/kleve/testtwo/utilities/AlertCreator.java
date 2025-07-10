package kleve.testtwo.utilities; // Good choice for a utility package

import javafx.scene.control.Alert;

/**
 * Utility class for creating and displaying standardized JavaFX Alert dialogs.
 */
public class AlertCreator {
    /**
     * Displays a JavaFX Alert dialog with specified type, title, header, and content.
     * The alert will block the application until the user closes it.
     *
     * @param alertType The type of alert to display (e.g., {@code Alert.AlertType.WARNING}, {@code Alert.AlertType.ERROR}, {@code Alert.AlertType.INFORMATION}).
     * @param title The title text for the alert window, displayed in the window's title bar.
     * @param headerText The header text for the alert dialog, usually a short summary or category.
     * @param contentText The main content message for the alert dialog, providing detailed information to the user.
     */
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait(); // This makes the alert modal, blocking user interaction with the main app until closed.
    }
}