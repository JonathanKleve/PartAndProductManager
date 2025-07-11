package kleve.PartAndProductManager.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kleve.PartAndProductManager.datamodel.InHouse;
import kleve.PartAndProductManager.datamodel.Outsourced;
import kleve.PartAndProductManager.datamodel.Part;
import kleve.PartAndProductManager.datamodel.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Provides data access operations for {@link Part} objects,
 * including {@link InHouse} and {@link Outsourced} parts.
 * This class handles all CRUD (Create, Read, Update, Delete) operations
 * related to parts in the database.
 *
 * @author Jonathan Kleve
 */
public class PartDAO {

    /**
     * Adds a new {@link InHouse} part to the database.
     * The `create_date`, `created_by`, `last_updated`, and `last_updated_by`
     * fields are automatically set during insertion using the current UTC time
     * and the {@link UserDAO#userId}.
     *
     * @param newPart The {@link InHouse} part object to be added.
     */
    public static void addPart(InHouse newPart) {
        try {
            String sql = "INSERT INTO parts (name, price, stock, min, max, machine_id, create_date, created_by, last_updated, last_updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, newPart.getName());
            ps.setDouble(2, newPart.getPrice());
            ps.setInt(3, newPart.getStock());
            ps.setInt(4, newPart.getMin());
            ps.setInt(5, newPart.getMax());
            ps.setInt(6, newPart.getMachineId());
            ps.setTimestamp(7, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(8, UserDAO.userId);
            ps.setTimestamp(9, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(10, UserDAO.userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new {@link Outsourced} part to the database.
     * The `create_date`, `created_by`, `last_updated`, and `last_updated_by`
     * fields are automatically set during insertion using the current UTC time
     * and the {@link UserDAO#userId}.
     *
     * @param newPart The {@link Outsourced} part object to be added.
     */
    public static void addPart(Outsourced newPart) {
        try {
            String sql = "INSERT INTO parts (name, price, stock, min, max, company_name, create_date, created_by, last_updated, last_updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, newPart.getName());
            ps.setDouble(2, newPart.getPrice());
            ps.setInt(3, newPart.getStock());
            ps.setInt(4, newPart.getMin());
            ps.setInt(5, newPart.getMax());
            ps.setString(6, newPart.getCompanyName());
            ps.setTimestamp(7, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(8, UserDAO.userId);
            ps.setTimestamp(9, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(10, UserDAO.userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single {@link Part} from the database based on its ID.
     * The method determines if the part is {@link InHouse} or {@link Outsourced}
     * based on the database fields and returns the appropriate type.
     *
     * @param partId The unique ID of the part to retrieve.
     * @return The {@link Part} object corresponding to the given ID, or {@code null} if not found or an error occurs.
     */
    public static Part getPart(int partId){
        String sql = "SELECT * FROM parts WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, partId);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                int min = resultSet.getInt("min");
                int max = resultSet.getInt("max");

                int machineId = resultSet.getInt("machine_id");
                String companyName = resultSet.getString("company_name");

                if (machineId != 0) {
                    return new InHouse(partId, name, price, stock, min, max, machineId);
                } else if (companyName != null && !companyName.trim().isEmpty()){
                    return new Outsourced(partId, name, price, stock, min, max, companyName);
                } else {
                    System.out.println("Warning: Part with ID " + partId + " is neither InHouse nor Outsourced properly defined.");
                    return null;
                }
            } else {
                System.out.println("No part found with ID: " + partId);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching part from id: " + e.getMessage());
            return null;
        } finally {
            // Ensure resources are closed to prevent leaks
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves a list of {@link Part} objects from the database whose names
     * partially match the given search string (case-insensitive).
     *
     * @param partName The name or partial name of the part(s) to search for.
     * @return An {@link ObservableList} of {@link Part} objects matching the search criteria.
     * Returns an empty list if no matches are found or an error occurs.
     */
    public static ObservableList<Part> getPart(String partName) {
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM parts WHERE LOWER(name) LIKE ?";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql)) {
            statement.setString(1, "%" + partName.toLowerCase() + "%");

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int stock = resultSet.getInt("stock");
                    int min = resultSet.getInt("min");
                    int max = resultSet.getInt("max");
                    String companyName = resultSet.getString("company_name");
                    int machineId = resultSet.getInt("machine_id");

                    if (companyName != null && !companyName.trim().isEmpty() && machineId == 0) { // Assuming machineId is 0 for Outsourced in DB
                        partList.add(new Outsourced(id, name, price, stock, min, max, companyName));
                    } else { // Assume it's an InHouse part
                        partList.add(new InHouse(id, name, price, stock, min, max, machineId));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving part by name: " + e.getMessage());
            e.printStackTrace();
        }
        return partList;
    }

    /**
     * Retrieves a list of {@link Product} objects that are associated with a specific part.
     * This queries the `product_parts` linking table.
     *
     * @param partId The ID of the part for which to find associated products.
     * @return An {@link ObservableList} of {@link Product} objects associated with the given part.
     * Returns an empty list if no associations are found or an error occurs.
     */
    public static ObservableList<Product> getPartProducts(int partId){
        ObservableList<Product> productList = FXCollections.observableArrayList();
        try (PreparedStatement statement = JDBC.connection.prepareStatement("SELECT * FROM product_parts WHERE part_id = ?")) {
            statement.setInt(1, partId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id"); // Corrected column name to "product_id"
                    // It's assumed ProductDAO.getProduct(productId) exists and retrieves the full Product object.
                    Product product = ProductDAO.getProduct(productId);
                    if (product != null) {
                        productList.add(product);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    /**
     * Retrieves all {@link Part} objects from the database.
     * This method fetches all part IDs and then uses {@link #getPart(int)} to retrieve
     * the full details for each part.
     *
     * @return An {@link ObservableList} containing all {@link Part} objects in the database.
     * Returns an empty list if no parts are found or an error occurs.
     */
    public static ObservableList<Part> getAllParts() {
        ObservableList<Part> partList = FXCollections.observableArrayList();
        String sql = "SELECT id FROM Parts";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int partId = resultSet.getInt("id");
                // Calls getPart(int) to retrieve the full Part object (InHouse or Outsourced)
                partList.add(getPart(partId));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return partList;
    }

    /**
     * Deletes a part from the database based on its ID.
     * @param partId The ID of the part to delete.
     * @return true if a part was successfully deleted, false otherwise.
     */
    public static boolean deletePart(int partId) {
        String sql = "DELETE FROM parts WHERE id = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, partId);
            int rowsAffected = ps.executeUpdate(); // executeUpdate returns the number of rows affected
            if (rowsAffected > 0) {
                System.out.println("Part with ID " + partId + " deleted successfully.");
                return true; // Return true if one or more rows were deleted
            } else {
                System.out.println("Part with ID " + partId + " not found for deletion.");
                return false; // Return false if no rows were deleted (part not found)
            }
        } catch (SQLException e) {
            System.err.println("Error deleting part with ID " + partId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing {@link InHouse} part's details in the database.
     * The `last_updated` and `last_updated_by` fields are automatically updated
     * using the current UTC time and the {@link UserDAO#userId}.
     * The `company_name` field is explicitly set to `null` for an InHouse part.
     *
     * @param part The {@link InHouse} part object with updated information.
     */
    public static void updatePart(InHouse part){
        try {
            String sql = "UPDATE parts SET name = ?, price = ?, stock = ?, min = ?, max = ?, machine_id = ?, last_updated = ?, last_updated_by = ?, company_name = null WHERE id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, part.getName());
            ps.setDouble(2, part.getPrice());
            ps.setInt(3, part.getStock());
            ps.setInt(4, part.getMin());
            ps.setInt(5, part.getMax());
            ps.setInt(6, part.getMachineId());
            ps.setTimestamp(7, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(8, UserDAO.userId);
            ps.setInt(9, part.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing {@link Outsourced} part's details in the database.
     * The `last_updated` and `last_updated_by` fields are automatically updated
     * using the current UTC time and the {@link UserDAO#userId}.
     * The `machine_id` field is explicitly set to `null` for an Outsourced part.
     *
     * @param part The {@link Outsourced} part object with updated information.
     */
    public static void updatePart(Outsourced part){
        try {
            String sql = "UPDATE parts SET name = ?, price = ?, stock = ?, min = ?, max = ?, company_name = ?, last_updated = ?, last_updated_by = ?, machine_id = null WHERE id = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, part.getName());
            ps.setDouble(2, part.getPrice());
            ps.setInt(3, part.getStock());
            ps.setInt(4, part.getMin());
            ps.setInt(5, part.getMax());
            ps.setString(6, part.getCompanyName());
            ps.setTimestamp(7, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(8, UserDAO.userId);
            ps.setInt(9, part.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}