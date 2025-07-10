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
 * This class serves as the controller for the "Modify Product" submenu.
 * It manages the user interface for editing an existing product, including its details
 * and its associated parts. It populates form fields and table views with existing data
 * upon initialization and handles user interactions such as saving changes, canceling,
 * adding/removing associated parts, and searching for parts.
 *
 * @author Jonathan Kleve
 */
public class modifyProductController implements Initializable {

    /**
     * Text field for displaying the product's ID.
     */
    public TextField idField;

    /**
     * Text field for searching parts to associate with the product.
     */
    public TextField partSearchField;

    /**
     * Text field for the product's name.
     */
    public TextField nameField;

    /**
     * Text field for the product's inventory level (stock).
     */
    public TextField invField;

    /**
     * Text field for the product's price.
     */
    public TextField priceField;

    /**
     * Text field for the product's maximum stock level.
     */
    public TextField maxField;

    /**
     * Text field for the product's minimum stock level.
     */
    public TextField minField;

    /**
     * Button to save the modified product details.
     */
    public Button saveButton;

    /**
     * Button to cancel modifications and return to the main menu.
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
     * Table view displaying available parts for association.
     */
    public TableView<Part> partSearchTable;

    /**
     * Table view displaying parts currently associated with the product.
     */
    public TableView<Part> aPartTable;

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
     * An {@link ObservableList} holding the parts currently associated with the product being modified.
     */
    public ObservableList<Part> aParts = FXCollections.observableArrayList();

    /**
     * The {@link Product} object currently selected for modification, passed from the main controller.
     */
    public Product selectedProduct;

    /**
     * Handles the action when the "Save" button is clicked.
     * This method validates the input fields for correct format and logical constraints (e.g., min &lt;= max, min &lt;= inv &lt;= max).
     * If all validations pass, it updates the {@link Product} object in the database and navigates the user back to the main menu.
     *
     * @param actionEvent The event triggered by clicking the save button.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        int inv = 0;
        int max = 0;
        int min = 0;
        double price = 0;
        String name = "";

        // Input validation checks
        name = nameField.getText();
        if (name.isBlank()){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "INPUT ERROR", "Warning", "Name cannot be empty. Please correct and try again.");
            return;
        }

        try {
            inv = Integer.parseInt(invField.getText());
            max = Integer.parseInt(maxField.getText());
            min = Integer.parseInt(minField.getText());
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Numbers must be entered into the Inv, Price, Min, and Max fields. Please correct and try again.");
            return;
        }

        if (min > max) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Min cannot be larger than Max. Please correct and try again.");
            return;
        }

        if (max < inv || inv < min) {
            AlertCreator.showAlert(Alert.AlertType.ERROR, "INPUT ERROR", "Warning", "Inv must be between Min and Max. Please correct and try again.");
            return;
        }

        // Update the product in the database
        Product newProduct = new Product(aParts, selectedProduct.getId(), name, price, inv, min, max);
        ProductDAO.updateProduct(newProduct);

        // Return user to main screen
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
     * Handles the action when the "Add" button (under the part search table) is clicked.
     * This method adds the currently selected {@link Part} from the {@link #partSearchTable}
     * to the {@link #aParts} list, which represents parts associated with the product.
     * An alert is shown if no part is selected.
     * <p>
     * **FUTURE ENHANCEMENT:** This method could be enhanced to prevent adding duplicate
     * associated parts to the product's list.
     * </p>
     *
     * @param actionEvent The event triggered by clicking the add button.
     */
    public void onAddAPartButtonClick(ActionEvent actionEvent) {
        Part selectedPart = partSearchTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to add.");
            return;
        }

        // Check for duplicates before adding
        // FUTURE ENHANCEMENT: Implement this check if desired for business logic.
        // if (aParts.contains(selectedPart)) {
        //     showAlert(Alert.AlertType.INFORMATION, "Duplicate Part", "Information", "This part is already associated with the product.");
        //     return;
        // }

        aParts.add(PartDAO.getPart(selectedPart.getId()));
    }

    /**
     * Handles the action when the "Remove Associated Part" button is clicked.
     * This method prompts the user for confirmation and, if confirmed, removes
     * the selected {@link Part} from the {@link #aParts} list, which represents
     * parts associated with the product. An alert is shown if no part is selected.
     *
     * @param actionEvent The event triggered by clicking the remove button.
     */
    public void onRemoveAPartButtonClick(ActionEvent actionEvent) {
        Part selectedPart = aPartTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to remove.");
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
     * matching results are found or if invalid characters are used in the search.
     *
     * @param actionEvent The event triggered by pressing Enter in the search field.
     */
    public void onPartSearchFieldText(ActionEvent actionEvent) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        String searchText = partSearchField.getText();

        if (searchText.contains("%") || searchText.contains("_")) {
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

        if (matchedParts.isEmpty()) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
        }

        // Refresh tableview with search results
        partSearchTable.setItems(matchedParts);
        // Column factories are typically set once in initialize(), but re-setting here is harmless
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Initializes the controller, populating the form fields with the selected product's data
     * and setting up the table views for part searching and displaying associated parts.
     * This method is called automatically after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if the root object was not localized.
     *
     * <p>
     * **RUNTIME NOTE:** The original code had a comment about a `NullPointerException` if `aParts` was set to `null`
     * before use, suggesting it might lose its `ObservableList` properties. By initializing `aParts` with
     * `FXCollections.observableArrayList()` at its declaration and always ensuring it's not `null` when assigned
     * (e.g., `this.associatedParts = (associatedParts != null) ? associatedParts : FXCollections.observableArrayList();`
     * in the `Product` constructor, and directly fetching into `aParts` here), this issue is avoided.
     * </p>
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the part search table with all available parts
        partSearchTable.setItems(PartDAO.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Get the product selected from the main view
        selectedProduct = MainController.selectedProduct;

        // Populate the product details fields
        idField.setText(String.valueOf(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        invField.setText(String.valueOf(selectedProduct.getStock()));
        priceField.setText(String.valueOf(selectedProduct.getPrice()));
        maxField.setText(String.valueOf(selectedProduct.getMax()));
        minField.setText(String.valueOf(selectedProduct.getMin()));

        // Populate the associated parts table
        // Directly assign the ObservableList returned by getProductParts to aParts
        aParts = ProductDAO.getProductParts(selectedProduct.getId());
        aPartTable.setItems(aParts);
        aPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}