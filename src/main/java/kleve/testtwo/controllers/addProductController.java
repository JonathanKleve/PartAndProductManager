package kleve.testtwo.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kleve.testtwo.DAO.PartDAO;
import kleve.testtwo.DAO.ProductDAO;
import kleve.testtwo.datamodel.Part;
import kleve.testtwo.datamodel.Product;
import kleve.testtwo.utilities.AlertCreator;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class serves as the controller for the "Add Product" submenu.
 * It manages the user interface for creating a new product, including its details
 * and the parts associated with it. It populates table views with available parts
 * and handles user interactions such as saving the new product, canceling,
 * adding/removing associated parts, and searching for parts.
 *
 * @author Jonathan Kleve
 */
public class addProductController implements Initializable {

    /**
     * Text field for searching available parts to associate with the new product.
     */
    public TextField partSearchField;

    /**
     * Text field for the name of the new product.
     */
    public TextField nameField;

    /**
     * Text field for the inventory level (stock) of the new product.
     */
    public TextField invField;

    /**
     * Text field for the price of the new product.
     */
    public TextField priceField;

    /**
     * Text field for the maximum stock level of the new product.
     */
    public TextField maxField;

    /**
     * Text field for the minimum stock level of the new product.
     */
    public TextField minField;

    /**
     * Button to save the new product details to the database.
     */
    public Button saveButton;

    /**
     * Button to cancel the creation of a new product and return to the main menu.
     */
    public Button cancelButton;

    /**
     * Button to add a selected part from the search table to the associated parts list.
     */
    public Button addAPartButton;

    /**
     * Button to remove a selected part from the associated parts list.
     */
    public Button removeAPartButton;

    /**
     * Table view displaying all available parts for potential association with the new product.
     */
    public TableView<Part> partSearchTable;

    /**
     * Table view displaying parts currently selected to be associated with the new product.
     */
    public TableView<Part> aPartTable;

    /**
     * Table column for the ID of parts in the search table.
     */
    public TableColumn<Part, Integer> partIdCol;

    /**
     * Table column for the name of parts in the search table.
     */
    public TableColumn<Part, String> partNameCol;

    /**
     * Table column for the inventory of parts in the search table.
     */
    public TableColumn<Part, Integer> partInvCol;

    /**
     * Table column for the price of parts in the search table.
     */
    public TableColumn<Part, Double> partPriceCol;

    /**
     * Table column for the ID of associated parts.
     */
    public TableColumn<Part, Integer> aPartIdCol;

    /**
     * Table column for the name of associated parts.
     */
    public TableColumn<Part, String> aPartNameCol;

    /**
     * Table column for the inventory of associated parts.
     */
    public TableColumn<Part, Integer> aPartInvCol;

    /**
     * Table column for the price of associated parts.
     */
    public TableColumn<Part, Double> aPartPriceCol;

    /**
     * An {@link ObservableList} holding the parts selected to be associated with the new product.
     */
    public ObservableList<Part> aParts = FXCollections.observableArrayList();

    /**
     * Handles the action when the "Save" button is clicked.
     * This method performs comprehensive validation on all input fields for the new product:
     * <ul>
     * <li>Ensures the name field is not empty.</li>
     * <li>Validates that inventory, price, min, and max fields contain valid numbers.</li>
     * <li>Checks that the minimum stock is not greater than the maximum stock.</li>
     * <li>Verifies that the current inventory is within the specified min and max range.</li>
     * </ul>
     * If all validations pass, it creates a new {@link Product} object (with an ID of 0, assuming
     * the database will auto-generate it upon insertion) and adds it to the database,
     * along with its associated parts. Finally, it navigates the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the save button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        int inv;
        int max;
        int min;
        double price;
        String name;

        // Input validation checks
        name = nameField.getText();
        if (name.isBlank()){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "INPUT ERROR", "Warning", "Name cannot be empty. Please correct and try again.");
            return;
        }

        try {
            inv = Integer.parseInt(invField.getText());
            price = Double.parseDouble(priceField.getText());
            max = Integer.parseInt(maxField.getText());
            min = Integer.parseInt(minField.getText());
        } catch (NumberFormatException e) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Numbers must be entered into the Inv, Price, Min, and Max fields. Please correct and try again.");
            return;
        }

        if (min > max){
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Min cannot be larger than Max. Please correct and try again.");
            return;
        }

        if (max < inv || inv < min){
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Inv must be between Min and Max. Please correct and try again.");
            return;
        }

        // Create and add the new product
        Product newProduct = new Product(aParts, 0, name, price, inv, min, max); // ID 0 for new product to be auto-generated by DB
        ProductDAO.addProduct(newProduct);

        // Return user to main menu
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Handles the action when the "Cancel" button is clicked.
     * This method discards any unsaved data for the new product and navigates
     * the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the cancel button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onCancelButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToMainMenu(actionEvent);
    }

    /**
     * Handles the action when the "Add" button (under the part search table) is clicked.
     * This method adds the currently selected {@link Part} from the {@link #partSearchTable}
     * to the {@link #aParts} list, which represents parts to be associated with the new product.
     * An alert is shown if no part is selected.
     * <p>
     * **FUTURE ENHANCEMENT:** This method could be enhanced to check if the selected part
     * is already in the `aParts` list and prevent adding duplicate associations.
     * </p>
     *
     * @param actionEvent The event triggered by clicking the add button.
     */
    public void onAddAPartButtonClick(ActionEvent actionEvent) {
        Part selectedPart = partSearchTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to add.");
            return;
        }

        // FUTURE ENHANCEMENT: Add a check for duplicates before adding to aParts. Need to confirm with customer is this would be desirable.
        // if (aParts.contains(selectedPart)) {
        //     showAlert(Alert.AlertType.INFORMATION, "Duplicate Part", "Information", "This part is already selected for association.");
        //     return;
        // }

        // Use PartDAO.getPart to ensure the full object is added, or simply add `selectedPart` if the table contains full objects.
        aParts.add(PartDAO.getPart(selectedPart.getId()));
    }

    /**
     * Handles the action when the "Remove Associated Part" button is clicked.
     * This method prompts the user for confirmation and, if confirmed, removes
     * the selected {@link Part} from the {@link #aParts} list (parts to be associated with the product).
     * An alert is shown if no part is selected from the associated parts table.
     *
     * @param actionEvent The event triggered by clicking the remove button.
     */
    public void onRemoveAPartButtonClick(ActionEvent actionEvent) {
        // Corrected: Should check selection from aPartTable, not partSearchTable for removal from aParts
        Part selectedPart = aPartTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to remove from associated parts.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        alert.setTitle("CONFIRMATION REQUIRED");
        alert.setHeaderText("Warning");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            aParts.remove(selectedPart);
        }
    }

    /**
     * Handles the action when the user presses Enter in the part search field.
     * This method attempts to search for parts by ID (if the input is numeric)
     * or by name (if the input is text). The {@link #partSearchTable} is then
     * updated to display the matching parts. A warning alert is shown if no
     * matching results are found or if invalid characters (`%` or `_`) are used in the search.
     *
     * @param actionEvent The event triggered by pressing Enter in the search field.
     */
    public void onPartSearchFieldText(ActionEvent actionEvent) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        String searchText = partSearchField.getText();

        if(searchText.contains("%") || searchText.contains("_")) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Input", "Warning", "Search cannot contain % or _ characters.");
            return;
        }

        try {
            int searchId = Integer.parseInt(searchText);
            Part p = PartDAO.getPart(searchId);
            if (p != null) {
                matchedParts.add(p);
            }
        } catch (NumberFormatException e) {
            matchedParts = PartDAO.getPart(searchText); // Search by name
        }

        if (matchedParts.isEmpty()){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
        }

        // Refresh the search results table view
        partSearchTable.setItems(matchedParts);
        // Column factories are typically set once in initialize(), but re-setting here is harmless
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Initializes the controller, setting up the table views before the "Add Product" menu is displayed.
     * This method is automatically called after the FXML file has been loaded.
     * It populates the available parts search table with all existing parts and
     * prepares the associated parts table for displaying selected parts.
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the part search table with all available parts
        partSearchTable.setItems(PartDAO.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize the associated parts table
        aPartTable.setItems(aParts); // `aParts` is already initialized as an empty ObservableList
        aPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}