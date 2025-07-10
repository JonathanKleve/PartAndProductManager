package kleve.testtwo.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kleve.testtwo.datamodel.ReportItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Provides data access operations for generating reports.
 * This class focuses on retrieving aggregated or specific data for reporting purposes,
 * rather than direct CRUD operations on single entities.
 *
 * @author Jonathan Kleve
 */
public class ReportDAO {

    /**
     * Retrieves an {@link ObservableList} of {@link ReportItem} objects representing
     * parts and products that have been updated within the last week.
     * The report includes the ID, name, type (Part/Product), current stock level,
     * and the last updated timestamp for each item.
     *
     * @return An {@link ObservableList} of {@link ReportItem} objects detailing
     * items updated in the last seven days. Returns an empty list if no items
     * meet the criteria or if a database error occurs.
     */
    public static ObservableList<ReportItem> getItemsUpdatedLastWeek() {
        ObservableList<ReportItem> displayItems = FXCollections.observableArrayList();
        LocalDateTime oneWeekAgoUtc = LocalDateTime.now(ZoneOffset.UTC).minusWeeks(1);
        Timestamp oneWeekAgoTimestamp = Timestamp.valueOf(oneWeekAgoUtc);

        String partsSql = "SELECT id, name, stock, last_updated FROM parts WHERE last_updated >= ?";
        String productsSql = "SELECT id, name, stock, last_updated FROM products WHERE last_updated >= ?";

        // Query for parts updated in the last week
        try (PreparedStatement stmt = JDBC.connection.prepareStatement(partsSql)) {
            stmt.setTimestamp(1, oneWeekAgoTimestamp);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int stock = rs.getInt("stock");
                    Timestamp lastUpdatedTs = rs.getTimestamp("last_updated");
                    // Convert SQL Timestamp to LocalDateTime, handling potential nulls
                    LocalDateTime lastUpdated = lastUpdatedTs != null ? lastUpdatedTs.toLocalDateTime() : null;

                    displayItems.add(new ReportItem(id, name, "Part", stock, lastUpdated));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching parts for report: " + e.getMessage());
            e.printStackTrace();
        }

        // Query for products updated in the last week
        try (PreparedStatement stmt = JDBC.connection.prepareStatement(productsSql)) {
            stmt.setTimestamp(1, oneWeekAgoTimestamp);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int stock = rs.getInt("stock");
                    Timestamp lastUpdatedTs = rs.getTimestamp("last_updated");
                    // Convert SQL Timestamp to LocalDateTime, handling potential nulls
                    LocalDateTime lastUpdated = lastUpdatedTs != null ? lastUpdatedTs.toLocalDateTime() : null;

                    displayItems.add(new ReportItem(id, name, "Product", stock, lastUpdated));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching products for report: " + e.getMessage());
            e.printStackTrace();
        }
        return displayItems;
    }
}