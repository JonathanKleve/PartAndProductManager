package kleve.testtwo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kleve.testtwo.DAO.JDBC;

import java.io.IOException;

/** This class is the main application for this program.
 * It sets up JavaFX and then opens the main menu.
 * JAVADOCS FOLDER LOCATION: in this project under the src folder
 * @author Jonathan Kleve
 * */
public class MainApplication extends Application {
    /** This method calls the test data method and then opens the main menu when the application is launched.
     * @param stage The method needs a stage to open a scene.
     * @throws IOException The method can throw this error as it can fail to locate or load resources when opening a new scene.
     * */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /** The main method for the application.
     * Opens the database connection, launches the JavaFX application, and closes the database connection upon exit.
     * @param args Command-line arguments (not used in this application).
     * */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}