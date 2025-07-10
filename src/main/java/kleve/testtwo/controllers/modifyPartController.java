package kleve.testtwo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import kleve.testtwo.DAO.PartDAO;
import kleve.testtwo.datamodel.Outsourced;
import kleve.testtwo.datamodel.InHouse;
import kleve.testtwo.datamodel.Part;
import kleve.testtwo.utilities.AlertCreator;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls the "Modify Part" submenu, allowing users to edit the details
 * of an existing {@link Part} (either {@link InHouse} or {@link Outsourced}).
 * It populates the input fields with the selected part's data upon initialization
 * and provides functionality for saving changes, canceling, and dynamically
 * adjusting the input form based on the part type.
 *
 * @author Jonathan Kleve
 */
public class modifyPartController implements Initializable {

    /**
     * Text field for displaying the part's ID. This field is typically non-editable.
     */
    public TextField idField;

    /**
     * Text field for the part's name.
     */
    public TextField nameField;

    /**
     * Text field for the part's inventory level (stock).
     */
    public TextField invField;

    /**
     * Text field for the part's price.
     */
    public TextField priceField;

    /**
     * Text field for the part's maximum stock level.
     */
    public TextField maxField;

    /**
     * Text field for the part's minimum stock level.
     */
    public TextField minField;

    /**
     * Text field that dynamically changes its purpose (Machine ID or Company Name)
     * based on the selected part type (In-House or Outsourced).
     */
    public TextField swapField;

    /**
     * Button to save the modified part details.
     */
    public Button saveButton;

    /**
     * Button to cancel modifications and return to the main menu.
     */
    public Button cancelButton;

    /**
     * Radio button for selecting 'In-House' as the part type.
     */
    public RadioButton inHouseButton;

    /**
     * Radio button for selecting 'Outsourced' as the part type.
     */
    public RadioButton outsourcedButton;

    /**
     * Label that dynamically changes its text ("Machine ID" or "Company Name")
     * to correspond with the {@link #swapField} based on the selected part type.
     */
    public Label swapLabel;

    /**
     * The {@link InHouse} part object selected for modification, if applicable.
     * This will be {@code null} if an {@link Outsourced} part is selected.
     */
    public InHouse selectedInHouse;

    /**
     * The {@link Outsourced} part object selected for modification, if applicable.
     * This will be {@code null} if an {@link InHouse} part is selected.
     */
    public Outsourced selectedOutsourced;

    /**
     * The index of the selected part within the global list (e.g., in {@link PartDAO#getAllParts()}).
     * Note: This field's direct usage might be less critical when using database IDs for updates,
     * but could be relevant for in-memory list operations.
     */
    public int index;

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
     * This method performs comprehensive validation on all input fields:
     * <ul>
     * <li>Ensures the name field is not empty.</li>
     * <li>Validates that inventory, price, min, and max fields contain valid numbers.</li>
     * <li>Checks that the minimum stock is not greater than the maximum stock.</li>
     * <li>Verifies that the current inventory is within the specified min and max range.</li>
     * <li>Based on the selected part type (In-House or Outsourced), it validates the {@link #swapField}
     * content (e.g., integer for Machine ID, non-empty string for Company Name).</li>
     * </ul>
     * If all validations pass, it updates the {@link Part} object in the database
     * (either as {@link InHouse} or {@link Outsourced}) and navigates the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the save button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        int id;
        int inv;
        int max;
        int min;
        double price;
        int machineId;
        String name;
        String companyName;

        name = nameField.getText();
        if (name.isBlank()){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "INPUT ERROR", "Warning", "Name cannot be empty. Please correct and try again.");
            return;
        }

        try {
            id = Integer.parseInt(idField.getText());
            inv = Integer.parseInt(invField.getText());
            max = Integer.parseInt(maxField.getText());
            min = Integer.parseInt(minField.getText());
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Numbers must be entered into the Inv, Price, Min, and Max fields. Please correct and try again.");
            return;
        }

        if (min > max){
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Min cannot be larger than Max. Please correct and try again.");
            return;
        }

        if (max < inv || inv < min){
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Inv must be within Min and Max. Please correct and try again.");
            return;
        }

        if (inHouseButton.isSelected()){
            try {
                machineId = Integer.parseInt(swapField.getText());
            } catch (NumberFormatException e) {
                AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Incorrect data in Machine ID field. Please enter a number.");
                return;
            }
            InHouse newPart = new InHouse(id, name, price, inv, min, max, machineId);
            PartDAO.updatePart(newPart);
        } else { // Outsourced button is selected
            companyName = swapField.getText();
            if (companyName.isBlank()){
                AlertCreator.showAlert(Alert.AlertType.WARNING, "INPUT ERROR", "Warning", "Company Name cannot be empty. Please correct and try again.");
                return;
            } else {
                Outsourced newPart = new Outsourced(id, name, price, inv, min, max, companyName);
                PartDAO.updatePart(newPart);
            }
        }

        // Return user to main menu
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * This method discards any unsaved changes and navigates the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the cancel button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Initializes the controller, populating the form fields with the details of the selected part.
     * It determines whether the selected part is {@link InHouse} or {@link Outsourced}
     * based on the `MainController.selectedInHouse` and `MainController.selectedOutsourced` values,
     * and sets the appropriate radio button and {@link #swapField} accordingly.
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (MainController.selectedInHouse == null) {
            // Selected part is Outsourced
            selectedOutsourced = MainController.selectedOutsourced;
            nameField.setText(selectedOutsourced.getName());
            idField.setText(String.valueOf(selectedOutsourced.getId()));
            invField.setText(String.valueOf(selectedOutsourced.getStock()));
            priceField.setText(String.valueOf(selectedOutsourced.getPrice()));
            maxField.setText(String.valueOf(selectedOutsourced.getMax()));
            minField.setText(String.valueOf(selectedOutsourced.getMin()));
            swapField.setText(selectedOutsourced.getCompanyName());
            outsourcedButton.setSelected(true);
            // The following line might be problematic if getAllParts() is large or if objects are not strictly identical
            // (e.g., if re-fetched from DB). Consider finding by ID for robustness.
            index = PartDAO.getAllParts().indexOf(selectedOutsourced);
            swapLabel.setText("Company Name"); // Ensure label is set correctly on init
        } else {
            // Selected part is In-House
            selectedInHouse = MainController.selectedInHouse;
            nameField.setText(selectedInHouse.getName());
            idField.setText(String.valueOf(selectedInHouse.getId()));
            invField.setText(String.valueOf(selectedInHouse.getStock()));
            priceField.setText(String.valueOf(selectedInHouse.getPrice()));
            maxField.setText(String.valueOf(selectedInHouse.getMax()));
            minField.setText(String.valueOf(selectedInHouse.getMin()));
            swapField.setText(String.valueOf(selectedInHouse.getMachineId()));
            inHouseButton.setSelected(true);
            // Same note as above regarding index.
            index = PartDAO.getAllParts().indexOf(selectedInHouse);
            swapLabel.setText("Machine ID"); // Ensure label is set correctly on init
        }
    }
}