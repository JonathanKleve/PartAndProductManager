package kleve.PartAndProductManager.utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen; // Corrected import
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Utility class for navigating between JavaFX scenes.
 */
public class SceneNavigator {

    /**
     * Navigates to a new scene based on the provided FXML file path.
     * The new scene replaces the current scene on the existing stage.
     * The new window is centered on the screen.
     *
     * @param event The ActionEvent that triggered the navigation (e.g., button click).
     * @param fxmlPath The path to the FXML file for the target scene (e.g., "/kleve/testtwo/main-menu-view.fxml").
     * @param title The title to set for the new stage.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void loadScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        Parent root = FXMLLoader.load(SceneNavigator.class.getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);

        // Center the window on the screen
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        double windowWidth = scene.getWidth();
        double windowHeight = scene.getHeight();
        stage.setX((screenWidth - windowWidth) / 2);
        stage.setY((screenHeight - windowHeight) / 2);
        stage.show();
    }

    /**
     * Navigates to the main menu.
     *
     * @param event The ActionEvent that triggered the navigation.
     * @throws IOException If the main-menu-view.fxml file cannot be loaded.
     */
    public static void navigateToMainMenu(ActionEvent event) throws IOException {
        loadScene(event, "/kleve/PartAndProductManager/main-menu-view.fxml", "Inventory Management System - Main Menu");
    }

    /**
     * Navigates to the reports menu.
     *
     * @param event The ActionEvent that triggered the navigation.
     * @throws IOException If the report-menu-view.fxml file cannot be loaded.
     */
    public static void navigateToReportMenu(ActionEvent event) throws IOException {
        loadScene(event, "/kleve/PartAndProductManager/report-menu-view.fxml", "Report Menu");
    }
}
