package kleve.testtwo.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import kleve.testtwo.DAO.PartDAO;
import kleve.testtwo.datamodel.InHouse;
import kleve.testtwo.datamodel.Outsourced;
import kleve.testtwo.datamodel.Part;
import kleve.testtwo.utilities.AlertCreator;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;

/**
 * This class serves as the controller for the "Add Part" submenu.
 * It manages the user interface for creating a new part, providing input fields
 * for part details and dynamic form elements based on whether the part is
 * In-House or Outsourced. It handles user interactions such as saving the
 * new part and navigating back to the main menu.
 *
 * @author Jonathan Kleve
 */
public class addPartController {
    /**
     * Text field for the part's minimum stock level.
     */
    public TextField minField;

    /**
     * Button to cancel the creation of a new part and return to the main menu.
     */
    public Button cancelButton;

    /**
     * Button to save the new part details to the database.
     */
    public Button saveButton;

    /**
     * Text field that dynamically changes its purpose (Machine ID or Company Name)
     * based on the selected part type (In-House or Outsourced).
     */
    public TextField swapField;

    /**
     * Text field for the part's maximum stock level.
     */
    public TextField maxField;

    /**
     * Text field for the part's price.
     */
    public TextField priceField;

    /**
     * Text field for the part's inventory level (stock).
     */
    public TextField invField;

    /**
     * Text field for the part's name.
     */
    public TextField nameField;

    /**
     * Label that dynamically changes its text ("Machine ID" or "Company Name")
     * to correspond with the {@link #swapField} based on the selected part type.
     */
    public Label swapLabel;

    /**
     * Radio button for selecting 'Outsourced' as the part type.
     */
    public RadioButton outsourcedButton;

    /**
     * Radio button for selecting 'In-House' as the part type.
     */
    public RadioButton inHouseButton;

    /**
     * Handles the action when the "In-House" radio button is clicked.
     * This method updates the {@link #swapLabel} text to "Machine ID" to reflect
     * the requirement for an In-House part.
     *
     * @param actionEvent The event triggered by clicking the radio button.
     */
    public void onInHouseButtonClick(ActionEvent actionEvent) {
        swapLabel.setText("Machine ID");
    }

    /**
     * Handles the action when the "Outsourced" radio button is clicked.
     * This method updates the {@link #swapLabel} text to "Company Name" to reflect
     * the requirement for an Outsourced part.
     *
     * @param actionEvent The event triggered by clicking the radio button.
     */
    public void onOutsourcedButtonClick(ActionEvent actionEvent) {
        swapLabel.setText("Company Name");
    }

    /**
     * Handles the action when the "Save" button is clicked.
     * This method performs comprehensive validation on all input fields for the new part:
     * <ul>
     * <li>Ensures the name field is not empty.</li>
     * <li>Validates that inventory, price, min, and max fields contain valid numbers.</li>
     * <li>Checks that the minimum stock is not greater than the maximum stock.</li>
     * <li>Verifies that the current inventory is within the specified min and max range.</li>
     * <li>Based on the selected part type (In-House or Outsourced), it validates the {@link #swapField}
     * content (e.g., integer for Machine ID, non-empty string for Company Name).</li>
     * </ul>
     * If all validations pass, it creates a new {@link Part} object (with an ID of 0, assuming
     * the database will auto-generate it upon insertion) and adds it to the database.
     * Finally, it navigates the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the save button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        int inv;
        int max;
        int min;
        double price;
        int machineId;
        String name;
        String companyName;

        // Input validation checks
        name = nameField.getText();
        if (name.isBlank()){
            AlertCreator.showAlert(AlertType.WARNING, "INPUT ERROR", "Warning", "Name cannot be empty. Please correct and try again.");
            return;
        }

        try{
            inv = Integer.parseInt(invField.getText());
            price = Double.parseDouble(priceField.getText());
            max = Integer.parseInt(maxField.getText());
            min = Integer.parseInt(minField.getText());
        } catch (NumberFormatException e) {
            AlertCreator.showAlert(AlertType.ERROR, "INPUT ERROR", "Warning", "Numbers must be entered into the Inv, Price, Min, and Max fields. Please correct and try again.");
            return;
        }

        if(min > max){
            AlertCreator.showAlert(AlertType.ERROR, "INPUT ERROR", "Warning", "Min cannot be larger than Max. Please correct and try again.");
            return;
        }

        if (max < inv || inv < min){
            AlertCreator.showAlert(AlertType.ERROR, "INPUT ERROR", "Warning", "Inv must be within Min and Max. Please correct and try again.");
            return;
        }

        // Checks radio button selection and validates/saves swapField content
        if(inHouseButton.isSelected()){
            try{
                machineId = Integer.parseInt(swapField.getText());
            } catch (NumberFormatException e) {
                AlertCreator.showAlert(AlertType.ERROR, "INPUT ERROR", "Warning", "Incorrect data in Machine ID field. Please enter a number.");
                return;
            }
            InHouse newPart = new InHouse(0, name, price, inv, min, max, machineId); // ID 0 for new part to be auto-generated by DB
            PartDAO.addPart(newPart);
        } else { // Outsourced button is selected
            companyName = swapField.getText();
            if (companyName.isBlank()){
                AlertCreator.showAlert(AlertType.WARNING, "INPUT ERROR", "Warning", "Company Name cannot be empty. Please correct and try again.");
                return;
            } else {
                Outsourced newPart = new Outsourced(0, name, price, inv, min, max, companyName); // ID 0 for new part to be auto-generated by DB
                PartDAO.addPart(newPart);
            }
        }

        // Returns user to main screen
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * This method discards any unsaved data for the new part and navigates
     * the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the cancel button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

}