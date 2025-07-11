package kleve.PartAndProductManager.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import kleve.PartAndProductManager.utilities.SceneNavigator;

import java.io.IOException;

/**
 * Controller class for the reports view.
 * This class handles user interactions with the report selection buttons and navigation.
 * @author Jonathan Kleve
 */
public class reportMenuController {
    /**
     * Button to exit the reports view and return to the main menu.
     */
    public Button exitButton;

    /**
     * Button to navigate to the all parts with id, name, and stock report.
     */
    public Button goButton1;

    /**
     * Button to navigate to the all products with id, name, and stock report.
     */
    public Button goButton2;

    /**
     * Button to navigate to the all parts and products updated within the last week report.
     */
    public Button goButton3;

    /**
     * Handles the exit button click event.
     * Navigates the user back to the main menu.
     * @param actionEvent The ActionEvent triggered by the button click.
     * @throws IOException If the main menu FXML file cannot be loaded.
     */
    public void onExitButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Handles the go button 1 click event.
     * Navigates the user to the all parts with id, name, and stock report.
     * @param actionEvent The ActionEvent triggered by the button click.
     * @throws IOException If the report FXML file cannot be loaded.
     */
    public void onGoButton1Click(ActionEvent actionEvent) throws IOException {
        SceneNavigator.loadScene(actionEvent, "/kleve/PartAndProductManager/part-report-view.fxml", "Part Report");
    }

    /**
     * Handles the go button 2 click event.
     * Navigates the user to the all products with id, name, and stock report.
     * @param actionEvent The ActionEvent triggered by the button click.
     * @throws IOException If the report FXML file cannot be loaded.
     */
    public void onGoButton2Click(ActionEvent actionEvent) throws IOException {
        SceneNavigator.loadScene(actionEvent, "/kleve/PartAndProductManager/product-report-view.fxml", "Product Report");
    }

    /**
     * Handles the go button 3 click event.
     * Navigates the user to the all parts and products updated within the last week report.
     * @param actionEvent The ActionEvent triggered by the button click.
     * @throws IOException If the report FXML file cannot be loaded.
     */
    public void onGoButton3Click(ActionEvent actionEvent) throws IOException {
        SceneNavigator.loadScene(actionEvent, "/kleve/PartAndProductManager/week-updated-report-view.fxml", "Week Updated Report");
    }
}