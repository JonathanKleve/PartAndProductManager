package kleve.testtwo.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kleve.testtwo.DAO.ReportDAO;
import kleve.testtwo.datamodel.ReportItem;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the "Parts and Products Updated Within the Past Week" report view.
 * This class handles the generation and display of a report listing parts and products
 * that have been updated within the last seven days. It populates a table view with
 * the relevant data and includes a date-time stamp for when the report was generated.
 *
 * @author Jonathan Kleve
 */
public class weekUpdatedReportController implements Initializable {

    /**
     * Label to display the date and time when the report was generated.
     */
    public Label dateTimeStampLabel;

    /**
     * Button to navigate back to the report menu.
     */
    public Button backButton;

    /**
     * Table column indicating the type of item (e.g., "Part" or "Product").
     */
    public TableColumn<ReportItem, String> typeCol;

    /**
     * Table column for the ID of the part or product.
     */
    public TableColumn<ReportItem, Integer> idCol;

    /**
     * Table column for the name of the part or product.
     */
    public TableColumn<ReportItem, String> nameCol;

    /**
     * Table column for the stock (inventory) level of the part or product.
     */
    public TableColumn<ReportItem, Integer> stockCol;

    /**
     * Table column for the timestamp of the last update for the part or product.
     */
    public TableColumn<ReportItem, LocalDateTime> lastUpdatedCol;

    /**
     * Table view to display the report items (parts and products updated within the last week).
     */
    public TableView<ReportItem> weekUpdatedTableView;


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
     * Initializes the {@code weekUpdatedReportController}.
     * This method is automatically called after the FXML file has been loaded.
     * It sets the current date and time on the {@link #dateTimeStampLabel} and
     * populates the {@link #weekUpdatedTableView} with items (parts and products)
     * that have been updated within the past week, retrieved from the {@link ReportDAO}.
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

        // Populate the table view with data
        weekUpdatedTableView.setItems(ReportDAO.getItemsUpdatedLastWeek());
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        lastUpdatedCol.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));
    }
}