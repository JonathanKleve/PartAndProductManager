package kleve.testtwo.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The `Product` class represents a product that the store sells.
 * Each product can have one or more {@link Part} objects associated with it.
 *
 * @author Jonathan Kleve
 */
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructs a new `Product` object with the specified attributes.
     * Products are comprised of a list of associated parts, a unique ID, a name,
     * a price, the current stock level, a minimum stock threshold, and a maximum stock limit.
     *
     * @param associatedParts An {@link ObservableList} of {@link Part} objects that are associated with this product.
     * @param id The unique identifier for the product.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The current inventory level (number of units) of the product.
     * @param min The minimum acceptable stock level for the product.
     * @param max The maximum acceptable stock level for the product.
     */
    public Product(ObservableList<Part> associatedParts, int id, String name, double price, int stock, int min, int max){
        // Ensure associatedParts is not null to prevent NullPointerExceptions later.
        this.associatedParts = (associatedParts != null) ? associatedParts : FXCollections.observableArrayList();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return The name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the unique identifier of the product.
     *
     * @return The ID of the product.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the current inventory level (stock) of the product.
     *
     * @return The current stock level of the product.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Retrieves the minimum acceptable stock level for the product.
     *
     * @return The minimum stock level of the product.
     */
    public int getMin() {
        return min;
    }

    /**
     * Retrieves the maximum acceptable stock level for the product.
     *
     * @return The maximum stock level of the product.
     */
    public int getMax() {
        return max;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return The price of the product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id The new ID to set for the product.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the current inventory level (stock) of the product.
     *
     * @param stock The new stock level to set for the product.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets the minimum acceptable stock level for the product.
     *
     * @param min The new minimum stock level to set for the product.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maximum acceptable stock level for the product.
     *
     * @param max The new maximum stock level to set for the product.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The new price to set for the product.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the name of the product.
     *
     * @param name The new name to set for the product.
     */
    public void setName(String name) {
        this.name= name;
    }

    /**
     * Retrieves the {@link ObservableList} of {@link Part} objects associated with this product.
     * If the list of associated parts is currently `null`, it initializes an empty `ObservableList`
     * before returning it.
     *
     * @return An {@link ObservableList} of {@link Part} objects associated with the product.
     */
    public ObservableList<Part> getAssociatedParts() {
        if (associatedParts == null) {
            associatedParts = FXCollections.observableArrayList();
        }
        return associatedParts;
    }

    /**
     * Sets the {@link ObservableList} of {@link Part} objects associated with this product.
     *
     * @param associatedParts The new {@link ObservableList} of {@link Part} objects to associate with the product.
     */
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }
}