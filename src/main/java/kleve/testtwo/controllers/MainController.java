package kleve.testtwo.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import kleve.testtwo.DAO.PartDAO;
import kleve.testtwo.DAO.ProductDAO;
import kleve.testtwo.datamodel.Outsourced;
import kleve.testtwo.datamodel.InHouse;
import kleve.testtwo.datamodel.Part;
import kleve.testtwo.datamodel.Product;
import kleve.testtwo.utilities.AlertCreator;
import kleve.testtwo.utilities.SceneNavigator;

/**
 * This class serves as the main controller for the Inventory Management System application.
 * It manages the primary user interface, which displays lists of parts and products.
 * The controller initializes the table views with data and provides functionality for
 * adding, modifying, deleting, and searching for parts and products, as well as
 * navigating to other application menus like reports.
 *
 * @author Jonathan Kleve
 */
public class MainController implements Initializable {

    /**
     * Button to navigate to the "Add Part" screen.
     */
    public Button partAddButton;

    /**
     * Text field for searching products in the product table.
     */
    public TextField productSearchField;

    /**
     * Text field for searching parts in the part table.
     */
    public TextField partSearchField;

    /**
     * Button to navigate to the "Modify Part" screen for the selected part.
     */
    public Button modifyPartButton;

    /**
     * Button to navigate to the "Add Product" screen.
     */
    public Button productAddButton;

    /**
     * Button to navigate to the "Modify Product" screen for the selected product.
     */
    public Button productModifyButton;

    /**
     * Button to delete the selected part from the part table.
     */
    public Button partDeleteButton;

    /**
     * Button to delete the selected product from the product table.
     */
    public Button productDeleteButton;

    /**
     * Button to navigate to the "Reports" menu.
     */
    public Button reportsButton;

    /**
     * Button to exit the application.
     */
    public Button exitButton;

    /**
     * Table view displaying all available parts.
     */
    public TableView<Part> partTable;

    /**
     * Table column for the inventory (stock) level of parts.
     */
    public TableColumn<Part, Integer> partInvCol;

    /**
     * Table column for the ID of parts.
     */
    public TableColumn<Part, Integer> partIdCol;

    /**
     * Table column for the name of parts.
     */
    public TableColumn<Part, String> partNameCol;

    /**
     * Table column for the price of parts.
     */
    public TableColumn<Part, Double> partPriceCol;

    /**
     * Table view displaying all available products.
     */
    public TableView<Product> productTable;

    /**
     * Table column for the ID of products.
     */
    public TableColumn<Product, Integer> productIdCol;

    /**
     * Table column for the name of products.
     */
    public TableColumn<Product, String> productNameCol;

    /**
     * Table column for the inventory (stock) level of products.
     */
    public TableColumn<Product, Integer> productInvCol;

    /**
     * Table column for the price of products.
     */
    public TableColumn<Product, Double> productPriceCol;

    /**
     * Static variable to hold the {@link Product} selected in the product table.
     * This allows other controllers (e.g., `modifyProductController`) to access the selected product's data.
     */
    public static Product selectedProduct;

    /**
     * Static variable to hold the {@link InHouse} part selected in the part table.
     * This allows other controllers (e.g., `modifyPartController`) to access the selected part's data if it's In-House.
     */
    public static InHouse selectedInHouse;

    /**
     * Static variable to hold the {@link Outsourced} part selected in the part table.
     * This allows other controllers (e.g., `modifyPartController`) to access the selected part's data if it's Outsourced.
     */
    public static Outsourced selectedOutsourced;

    /**
     * Handles the action when the "Add" button under the parts table is clicked.
     * This method loads and displays the "Add Part" view, allowing the user to create a new part.
     *
     * @param actionEvent The event triggered by clicking the "Add Part" button.
     * @throws IOException If the FXML file for the "Add Part" view cannot be loaded.
     */
    public void onPartAddButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.loadScene(actionEvent, "/kleve/testtwo/add-part-view.fxml", "Add Part");
    }

    /**
     * Handles the action when the "Add" button under the products table is clicked.
     * This method loads and displays the "Add Product" view, allowing the user to create a new product.
     *
     * @param actionEvent The event triggered by clicking the "Add Product" button.
     * @throws IOException If the FXML file for the "Add Product" view cannot be loaded.
     */
    public void onProductAddButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.loadScene(actionEvent, "/kleve/testtwo/add-product-view.fxml", "Add Product");
    }

    /**
     * Handles product search functionality when the user presses Enter in the product search field.
     * This method attempts to parse the search text as an integer to search by product ID.
     * If parsing fails, it searches for products by name. The {@link #productTable} is then
     * updated to display only the matching products. A warning alert is shown if no results are found
     * or if the search text contains invalid characters (`%` or `_`).
     * <p>
     * **RUNTIME ERROR FIX:** The original code checked `matchedProducts == null` which was incorrect
     * because `FXCollections.observableArrayList()` returns an empty but non-null list.
     * The condition was changed to `matchedProducts.isEmpty()` for accurate checking.
     * </p>
     *
     * @param actionEvent The event triggered by pressing Enter in the product search field.
     */
    public void onProductSearchText(ActionEvent actionEvent) {
        ObservableList<Product> matchedProducts = FXCollections.observableArrayList();
        String searchText = productSearchField.getText();
        int searchId;

        try {
            searchId = Integer.parseInt(searchText);
            Product p = ProductDAO.getProduct(searchId);
            if (p == null) {
                AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
                return;
            } else {
                matchedProducts.add(p);
            }

        } catch (NumberFormatException e) {
            if (searchText.contains("%") || searchText.contains("_")) {
                AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid input", "Warning", "Search cannot contain % or _ characters.");
                return;
            } else {
                matchedProducts = ProductDAO.getProduct(searchText);
            }
        }

        if (matchedProducts.isEmpty()) { // Fix for Runtime Error: Changed from == null to .isEmpty()
            AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
            return;
        }

        // Refresh product table view with search results
        productTable.setItems(matchedProducts);
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Handles part search functionality when the user presses Enter in the part search field.
     * This method attempts to parse the search text as an integer to search by part ID.
     * If parsing fails, it searches for parts by name. The {@link #partTable} is then
     * updated to display only the matching parts. A warning alert is shown if no results are found
     * or if the search text contains invalid characters (`%` or `_`).
     *
     * @param actionEvent The event triggered by pressing Enter in the part search field.
     */
    public void onPartSearchText(ActionEvent actionEvent) {
        ObservableList<Part> matchedParts = FXCollections.observableArrayList();
        String searchText = partSearchField.getText();
        int searchId;

        try {
            searchId = Integer.parseInt(searchText);
            Part p = PartDAO.getPart(searchId);
            if (p == null) {
                AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
                return;
            } else {
                matchedParts.add(p);
            }
        } catch (NumberFormatException e) {
            if (searchText.contains("%") || searchText.contains("_")) {
                AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid input", "Warning", "Search cannot contain % or _ characters.");
                return;
            } else {
                matchedParts = PartDAO.getPart(searchText);
            }
        }

        if (matchedParts.isEmpty()){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "No Results", "Warning", "No matching results found.");
            return;
        }

        // Refresh part table view with search results
        partTable.setItems(matchedParts);
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Handles the action when the "Modify" button under the parts table is clicked.
     * This method first checks if a part is selected. If not, it shows a warning.
     * If a part is selected, it determines if it's an {@link InHouse} or {@link Outsourced} part,
     * stores the selected part in the appropriate static variable (`selectedInHouse` or `selectedOutsourced`),
     * and then loads and displays the "Modify Part" view.
     *
     * @param actionEvent The event triggered by clicking the "Modify Part" button.
     * @throws IOException If the FXML file for the "Modify Part" view cannot be loaded.
     */
    public void onModifyPartButtonClick(ActionEvent actionEvent) throws IOException {
        Part selectedPartFromTable = partTable.getSelectionModel().getSelectedItem();
        if (selectedPartFromTable == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to modify.");
            return;
        }

        // Determine the concrete type of the selected part and store it statically
        Part actualPart = PartDAO.getPart(selectedPartFromTable.getId()); // Fetch full object to confirm type
        if (actualPart instanceof InHouse) {
            selectedInHouse = (InHouse) actualPart;
            selectedOutsourced = null; // Clear the other static variable
        } else if (actualPart instanceof Outsourced) {
            selectedOutsourced = (Outsourced) actualPart;
            selectedInHouse = null; // Clear the other static variable
        } else {
            // This case should ideally not be reached if PartDAO always returns InHouse or Outsourced
            AlertCreator.showAlert(Alert.AlertType.ERROR, "Error", "Part Type Unknown", "Selected part is neither In-House nor Outsourced.");
            return;
        }
        SceneNavigator.loadScene(actionEvent, "/kleve/testtwo/modify-part-view.fxml", "Modify Part");
    }

    /**
     * Handles the action when the "Modify" button under the products table is clicked.
     * This method first checks if a product is selected. If not, it shows a warning.
     * If a product is selected, it stores the product in the static {@link #selectedProduct} variable
     * and then loads and displays the "Modify Product" view.
     *
     * @param actionEvent The event triggered by clicking the "Modify Product" button.
     * @throws IOException If the FXML file for the "Modify Product" view cannot be loaded.
     */
    public void onModifyProductButtonClick(ActionEvent actionEvent) throws IOException {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a product to modify.");
            return;
        }

        SceneNavigator.loadScene(actionEvent, "/kleve/testtwo/modify-product-view.fxml", "Modify Product");
    }

    /**
     * Handles the action when the "Delete" button under the parts table is clicked.
     * This method first checks if a part is selected. If not, it shows a warning.
     * If a part is selected, it then checks if the part is associated with any products.
     * If it is, a warning is shown, and deletion is prevented.
     * Otherwise, it prompts the user for confirmation. If confirmed, the part is deleted from the database.
     *
     * @param actionEvent The event triggered by clicking the "Delete Part" button.
     */
    public void onPartDeleteButtonClick(ActionEvent actionEvent) {
        Part partToDelete = partTable.getSelectionModel().getSelectedItem();
        if (partToDelete == null) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a part to delete.");
        } else if (!PartDAO.getPartProducts(partToDelete.getId()).isEmpty()) {
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Deletion Error", "Warning", "Part has associated products. Unable to delete.");
            return; // Exit method if part has associated products
        } else {
            // Confirmation alert for deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            alert.setTitle("CONFIRMATION REQUIRED");
            alert.setHeaderText("Warning");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                PartDAO.deletePart(partToDelete.getId());
                // Refresh the table after deletion if PartDAO doesn't automatically update ObservableList
                partTable.setItems(PartDAO.getAllParts());
            }
        }
    }

    /**
     * Handles the action when the "Delete" button under the products table is clicked.
     * This method first checks if a product is selected. If not, it shows a warning.
     * If a product is selected, it then checks if the product has any associated parts.
     * If it does, a warning is shown, and deletion is prevented.
     * Otherwise, it prompts the user for confirmation. If confirmed, the product is deleted from the database.
     * <p>
     * **RUNTIME ERROR FIX:** The original code checked `prodToDel.getAssociatedParts() != null`.
     * Since `getAssociatedParts()` now correctly returns an empty, non-null `ObservableList`
     * if there are no associated parts, the check was changed to `!prodToDel.getAssociatedParts().isEmpty()`
     * to accurately determine if a product has associated parts.
     * </p>
     *
     * @param actionEvent The event triggered by clicking the "Delete Product" button.
     */
    public void onProductDeleteButtonClick(ActionEvent actionEvent) {
        Product prodToDel = productTable.getSelectionModel().getSelectedItem();
        if (prodToDel == null){
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Invalid Selection", "Warning", "Please select a product to delete.");
            return;
        } else if (!prodToDel.getAssociatedParts().isEmpty()) { // Fix for Runtime Error: Changed from != null to ! .isEmpty()
            AlertCreator.showAlert(Alert.AlertType.WARNING, "Deletion Error", "Warning", "Product has associated parts. Unable to delete.");
            return; // Exit method if product has associated parts
        } else {
            // Confirmation alert for deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
            alert.setTitle("CONFIRMATION REQUIRED");
            alert.setHeaderText("Warning");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ProductDAO.deleteProduct(prodToDel.getId());
                // Refresh the table after deletion if ProductDAO doesn't automatically update ObservableList
                productTable.setItems(ProductDAO.getAllProducts());
            }
        }
    }

    /**
     * Handles the action when the "Reports" button is clicked.
     * This method loads and displays the "Report Menu" view.
     *
     * @param actionEvent The event triggered by clicking the "Reports" button.
     * @throws IOException If the FXML file for the "Report Menu" view cannot be loaded.
     */
    public void onReportsButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToReportMenu(actionEvent);
    }

    /**
     * Handles the action when the "Exit" button is clicked.
     * This method terminates the JavaFX application.
     *
     * @param actionEvent The event triggered by clicking the "Exit" button.
     */
    public void onExitButtonClick(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Initializes the controller, populating the part and product table views
     * with data when the main menu is first displayed. This method is automatically
     * called by the JavaFX framework after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the Part TableView
        partTable.setItems(PartDAO.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialize the Product TableView
        productTable.setItems(ProductDAO.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}