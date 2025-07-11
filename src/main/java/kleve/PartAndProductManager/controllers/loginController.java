package kleve.PartAndProductManager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import kleve.PartAndProductManager.DAO.UserDAO;
import kleve.PartAndProductManager.utilities.SceneNavigator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Controller class for the login view.
 * This class handles user login, language settings, and appointment alerts.
 * @author Jonathan Kleve
 */
public class loginController implements Initializable {

    /**
     * Flag to indicate the language setting (0 for English, 1 for French).
     */
    public int langFlag = 0;

    /**
     * Label to display the user's time zone.
     */
    public Label zoneLabel;

    /**
     * Text field for entering the username.
     */
    public TextField usernameField;

    /**
     * Text field for entering the password.
     */
    public TextField passwordField;

    /**
     * Label for the username field.
     */
    public Label usernameLabel;

    /**
     * Label for the password field.
     */
    public Label passwordLabel;

    /**
     * Button to initiate the login process.
     */
    public Button loginButton;

    /**
     * Initializes the login controller.
     * Sets the time zone label, language settings, and UI text based on the user's locale.
     * @param url             The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle  The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zoneId = ZoneId.systemDefault();
        zoneLabel.setText("ZoneId: " + zoneId.getId());

        Locale defaultLocale = Locale.getDefault();
        String language = defaultLocale.getLanguage();
        if (Objects.equals(language, "fr")) {
            langFlag = 1;
            usernameLabel.setText("Nom d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginButton.setText("Se connecter");
        }
    }

    /**
     * Handles the login button click event.
     * Validates the user credentials, logs the login activity, and navigates to the main menu.
     * Also checks for upcoming appointments within 15 minutes and displays an alert.
     * @param actionEvent The ActionEvent triggered by the button click.
     * @throws IOException If the main menu FXML file cannot be loaded or if there is an issue writing to the log file.
     */
    public void onLoginButtonClick(ActionEvent actionEvent) throws IOException {
        String testUser = usernameField.getText();
        String testPass = passwordField.getText();

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);

        boolean success = false;

        if (UserDAO.loginUser(testUser, testPass, langFlag)) {
            success = true;
            SceneNavigator.navigateToMainMenu(actionEvent);
        }

        try (FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("Username: " + testUser + " Success: " + success + " " + formattedDateTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}