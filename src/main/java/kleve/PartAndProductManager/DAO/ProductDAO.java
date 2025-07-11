package kleve.PartAndProductManager.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import kleve.PartAndProductManager.datamodel.Part;
import kleve.PartAndProductManager.datamodel.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides data access operations for {@link Product} objects.
 * This class handles all CRUD (Create, Read, Update, Delete) operations
 * related to products in the database, including their associated parts.
 *
 * @author Jonathan Kleve
 */
public class ProductDAO {

    /**
     * Adds a new {@link Product} to the database.
     * This method also handles the insertion of associated parts into the `product_parts`
     * linking table. The `create_date`, `created_by`, `last_updated`, and `last_updated_by`
     * fields for the product are automatically set during insertion using the current UTC time
     * and the {@link UserDAO#userId}.
     *
     * @param newProduct The {@link Product} object to be added.
     */
    public static void addProduct(Product newProduct){
        PreparedStatement ps = null; // Declare outside try-with-resources to use for product_parts
        try {
            // Insert the product itself
            String sql = "INSERT INTO products (name, price, stock, min, max, create_date, created_by, last_updated, last_updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, newProduct.getName());
            ps.setDouble(2, newProduct.getPrice());
            ps.setInt(3, newProduct.getStock());
            ps.setInt(4, newProduct.getMin());
            ps.setInt(5, newProduct.getMax());
            ps.setTimestamp(6, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(7, UserDAO.userId);
            ps.setTimestamp(8, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(9, UserDAO.userId);
            ps.executeUpdate();
            // Closing ps here is problematic if it's reused below. It's better to create a new PreparedStatement
            // for the product_parts insertion or refactor for clearer resource management.

            // Get associated parts and insert into linking table
            // Note: product.getId() might return 0 if the ID is auto-generated and not retrieved after insertion.
            // A more robust solution would be to use Statement.RETURN_GENERATED_KEYS to get the product ID.
            ObservableList<Part> associatedPartList = newProduct.getAssociatedParts();
            int productId = newProduct.getId();

            String productPartSql = "INSERT INTO product_parts (product_id, part_id) VALUES (?, ?)";
            try (PreparedStatement productPartPs = JDBC.connection.prepareStatement(productPartSql)) {
                for (Part part : associatedPartList) {
                    int partId = part.getId();
                    productPartPs.setInt(1, productId);
                    productPartPs.setInt(2, partId);
                    productPartPs.executeUpdate();
                }
            } // productPartPs is closed here automatically
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Ensure the initial PreparedStatement is closed if it was opened.
            // This structure is less ideal than using try-with-resources for each statement.
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves an {@link ObservableList} of {@link Product} objects from the database
     * whose names partially match the given search string (case-insensitive).
     *
     * @param productName The name or partial name of the product(s) to search for.
     * @return An {@link ObservableList} of {@link Product} objects matching the search criteria.
     * Returns an empty list if no matches are found or an error occurs.
     */
    public static ObservableList<Product> getProduct(String productName){
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE ?";

        // Using try-with-resources for PreparedStatement and ResultSet to ensure they are closed
        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql)) {
            statement.setString(1, "%" + productName.toLowerCase() + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int stock = resultSet.getInt("stock");
                    int min = resultSet.getInt("min");
                    int max = resultSet.getInt("max");
                    // Note: This creates a Product object without its associated parts,
                    // as a search by name typically doesn't need to load all associations upfront.
                    Product tempProduct = new Product(null, id, name, price, stock, min, max);
                    productList.add(tempProduct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    /**
     * Retrieves a single {@link Product} from the database based on its ID.
     * This method also fetches and populates the {@link ObservableList} of associated
     * {@link Part}s for the retrieved product by calling {@link #getProductParts(int)}.
     *
     * @param productId The unique ID of the product to retrieve.
     * @return The {@link Product} object corresponding to the given ID, or {@code null} if not found or an error occurs.
     */
    public static Product getProduct(int productId){
        String sql = "SELECT * FROM products WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = JDBC.connection.prepareStatement(sql);
            statement.setInt(1, productId);
            resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");
                int min = resultSet.getInt("min");
                int max = resultSet.getInt("max");

                // Get associated parts for this product
                ObservableList<Part> productParts = getProductParts(productId);
                return new Product(productParts, productId, name, price, stock, min, max);
            } else {
                System.out.println("No product found with ID: " + productId);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching product from id");
            return null;
        } finally {
            // Ensure resources are closed
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves an {@link ObservableList} of {@link Part} objects that are directly
     * associated with a specific {@link Product} from the `product_parts` linking table.
     * Each associated part's full details are fetched using {@link PartDAO#getPart(int)}.
     *
     * @param productId The ID of the product for which to retrieve associated parts.
     * @return An {@link ObservableList} of {@link Part} objects associated with the given product.
     * Returns an empty list if no associations are found or an error occurs.
     */
    public static ObservableList<Part> getProductParts(int productId){
        ObservableList<Part> partList = FXCollections.observableArrayList();
        try (PreparedStatement statement = JDBC.connection.prepareStatement("SELECT part_id FROM product_parts WHERE product_id = ?")) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int partId = resultSet.getInt("part_id");
                    // Assuming PartDAO.getPart(int) correctly retrieves the Part object (InHouse or Outsourced)
                    Part part = PartDAO.getPart(partId);
                    if (part != null) { // Add only if part was successfully retrieved
                        partList.add(part);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return partList;
    }

    /**
     * Retrieves all {@link Product} objects from the database.
     * This method fetches all product IDs and then uses {@link #getProduct(int)} to retrieve
     * the full details for each product, including their associated parts.
     *
     * @return An {@link ObservableList} containing all {@link Product} objects in the database.
     * Returns an empty list if no products are found or an error occurs.
     */
    public static ObservableList<Product> getAllProducts() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT id FROM Products";

        try (PreparedStatement statement = JDBC.connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                // Calls getProduct(int) to retrieve the full Product object with associated parts
                Product product = getProduct(productId);
                if (product != null) { // Add only if product was successfully retrieved
                    productList.add(product);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    /**
     * Deletes a product from the database based on its ID.
     * @param productId The ID of the product to delete.
     * @return true if a product was successfully deleted, false otherwise.
     */
    public static boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = JDBC.connection.prepareStatement(sql)) {
            ps.setInt(1, productId);
            int rowsAffected = ps.executeUpdate(); // executeUpdate returns the number of rows affected
            if (rowsAffected > 0) {
                System.out.println("Product with ID " + productId + " deleted successfully.");
                return true; // Return true if one or more rows were deleted
            } else {
                System.out.println("Product with ID " + productId + " not found for deletion.");
                return false; // Return false if no rows were deleted (product not found)
            }
        } catch (SQLException e) {
            System.err.println("Error deleting product with ID " + productId + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to build a frequency map of items in a list.
     * This is used internally for comparing lists of associated parts while updating a product.
     *
     * @param <T> The type of elements in the list.
     * @param list The list for which to build the frequency map.
     * @return A {@link Map} where keys are the elements from the list and values are their frequencies.
     */
    private static <T> Map<T, Integer> buildFrequencyMap(List<T> list) {
        Map<T, Integer> freqMap = new HashMap<>();
        for (T item : list) {
            freqMap.put(item, freqMap.getOrDefault(item, 0) + 1);
        }
        return freqMap;
    }

    /**
     * Updates an existing {@link Product}'s details in the database, including its associated parts.
     * This method first updates the product's main attributes and then intelligently
     * identifies and applies changes (additions and removals) to the `product_parts` linking table.
     * The `last_updated` and `last_updated_by` fields are automatically updated
     * using the current UTC time and the {@link UserDAO#userId}.
     *
     * @param product The {@link Product} object with updated information and potentially
     * modified associated parts list.
     */
    public static void updateProduct(Product product){
        PreparedStatement ps = null;
        try {
            // Update product's main attributes
            String sql = "UPDATE products SET name = ?, price = ?, stock = ?, min = ?, max = ?, last_updated = ?, last_updated_by = ? WHERE id = ?"; // Use prepared statement for ID too
            ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getStock());
            ps.setInt(4, product.getMin());
            ps.setInt(5, product.getMax());
            ps.setTimestamp(6, Timestamp.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ps.setInt(7, UserDAO.userId);
            ps.setInt(8, product.getId()); // Set product ID as parameter
            ps.executeUpdate();
            // ps.close() is handled by finally block if not using try-with-resources for this statement.

            // Update associated parts
            ObservableList<Part> currentPartsList = getProductParts(product.getId());
            ObservableList<Part> updatedPartsList = product.getAssociatedParts();

            Map<Part, Integer> currentFreqMap = buildFrequencyMap(currentPartsList);
            Map<Part, Integer> updatedFreqMap = buildFrequencyMap(updatedPartsList);

            if (currentFreqMap.equals(updatedFreqMap)) {
                System.out.println("Associated parts are identical. No updates to product_parts table needed.");
                return;
            }
            System.out.println("Associated parts differ. Calculating changes...");

            // Determine parts to remove
            Map<Part, Integer> tempUpdatedFreqMap = new HashMap<>(updatedFreqMap);
            List<Part> partsToRemove = new ArrayList<>();
            for (Part currentPart : currentPartsList) {
                if (tempUpdatedFreqMap.containsKey(currentPart) && tempUpdatedFreqMap.get(currentPart) > 0) {
                    tempUpdatedFreqMap.put(currentPart, tempUpdatedFreqMap.get(currentPart) - 1);
                } else {
                    partsToRemove.add(currentPart);
                }
            }

            // Determine parts to add
            Map<Part, Integer> tempCurrentFreqMap = new HashMap<>(currentFreqMap);
            List<Part> partsToAdd = new ArrayList<>();
            for (Part desiredPart : updatedPartsList) {
                if (tempCurrentFreqMap.containsKey(desiredPart) && tempCurrentFreqMap.get(desiredPart) > 0) {
                    tempCurrentFreqMap.put(desiredPart, tempCurrentFreqMap.get(desiredPart) - 1);
                } else {
                    partsToAdd.add(desiredPart);
                }
            }

            // Perform database operations for removals
            if (!partsToRemove.isEmpty()) {
                System.out.println("Removing " + partsToRemove.size() + " associations from product_parts table:");
                try (PreparedStatement deletePs = JDBC.connection.prepareStatement("DELETE FROM product_parts WHERE product_id = ? AND part_id = ?")) {
                    for (Part part : partsToRemove) {
                        System.out.println("  - Removing: " + part.getName() + " (ID: " + part.getId() + ")");
                        deletePs.setInt(1, product.getId());
                        deletePs.setInt(2, part.getId());
                        deletePs.executeUpdate();
                    }
                }
            } else {
                System.out.println("No associations to remove.");
            }

            // Perform database operations for additions
            if (!partsToAdd.isEmpty()) {
                System.out.println("Adding " + partsToAdd.size() + " associations to product_parts table:");
                try (PreparedStatement insertPs = JDBC.connection.prepareStatement("INSERT INTO product_parts (product_id, part_id) VALUES (?, ?)")) {
                    for (Part part : partsToAdd) {
                        System.out.println("  - Adding: " + part.getName() + " (ID: " + part.getId() + ")");
                        insertPs.setInt(1, product.getId());
                        insertPs.setInt(2, part.getId());
                        insertPs.executeUpdate();
                    }
                }
            } else {
                System.out.println("No new associations to add.");
            }

            System.out.println("Product and associated parts updated successfully.");

        } catch (SQLException e) {
            System.err.println("Database error during product update or part association management:");
            e.printStackTrace();
        } finally {
            // Ensure the initial PreparedStatement for product update is closed
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}