package kleve.testtwo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kleve.testtwo.DAO.PartDAO;
import kleve.testtwo.datamodel.Part;
import kleve.testtwo.utilities.SceneNavigator;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the part report view.
 * This class handles the generation and display of parts with their ID, name, and stock.
 *
 * @author Jonathan Kleve
 */
public class partReportController implements Initializable {

    /**
     * Label to display the date and time when the report was generated.
     */
    public Label dateTimeStampLabel;

    /**
     * Button to navigate back to the report menu.
     */
    public Button backButton;

    /**
     * Table column for the part ID.
     */
    public TableColumn<Part, Integer> idCol;

    /**
     * Table column for the part name.
     */
    public TableColumn<Part, String> nameCol;

    /**
     * Table column for the part stock (inventory level).
     */
    public TableColumn<Part, Integer> stockCol;

    /**
     * Table view to display the list of parts in the report.
     */
    public TableView<Part> partTableView;


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
     * Initializes the {@code partReportController}.
     * This method is automatically called after the FXML file has been loaded.
     * It sets the current date and time on the {@link #dateTimeStampLabel} and
     * populates the {@link #partTableView} with all available parts,
     * retrieved from the {@link PartDAO}.
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

        // Populate the table view with part data
        partTableView.setItems(PartDAO.getAllParts());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}