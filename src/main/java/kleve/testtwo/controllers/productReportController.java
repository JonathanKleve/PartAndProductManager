package kleve.testtwo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kleve.testtwo.DAO.ProductDAO;
import kleve.testtwo.datamodel.Product;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the product report view.
 * This class handles the generation and display of a report listing all products
 * with their ID, name, and current stock level. It also includes a timestamp
 * indicating when the report was generated.
 *
 * @author Jonathan Kleve
 */
public class productReportController implements Initializable {

    /**
     * Label to display the date and time when the report was generated.
     */
    public Label dateTimeStampLabel;

    /**
     * Button to navigate back to the report menu.
     */
    public Button backButton;

    /**
     * Table column for the product ID.
     */
    public TableColumn<Product, Integer> idCol;

    /**
     * Table column for the product name.
     */
    public TableColumn<Product, String> nameCol;

    /**
     * Table column for the product stock (inventory level).
     */
    public TableColumn<Product, Integer> stockCol;

    /**
     * Table view to display the list of products in the report.
     */
    public TableView<Product> productTableView;


    /**
     * Handles the "Back" button click event.
     * This method navigates the user back to the main report menu.
     *
     * @param actionEvent The {@link ActionEvent} triggered by the button click.
     * @throws IOException If the FXML file for the reports menu cannot be loaded.
     */
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        SceneNavigator.navigateToReportMenu(actionEvent);
    }

    /**
     * Initializes the {@code productReportController}.
     * This method is automatically called after the FXML file has been loaded.
     * It sets the current date and time on the {@link #dateTimeStampLabel} and
     * populates the {@link #productTableView} with all available products,
     * retrieved from the {@link ProductDAO}.
     *
     * @param url             The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param resourceBundle  The resources used to localize the root object, or {@code null} if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the report generation date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        dateTimeStampLabel.setText("Report Generated: " + formattedDateTime);

        // Populate the table view with product data
        productTableView.setItems(ProductDAO.getAllProducts());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}