package kleve.PartAndProductManager.datamodel;

import java.util.Objects;

/**
 * Represents an abstract base class for parts within the inventory management system.
 * This class provides common attributes and behaviors for all types of parts,
 * such as ID, name, price, stock level, minimum stock, and maximum stock.
 * Specific types of parts (e.g., InHouse, Outsourced) will extend this class.
 *
 * @author Jonathan Kleve
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructs a new Part object with the specified details.
     *
     * @param id The unique identifier for the part.
     * @param name The name of the part.
     * @param price The price per unit of the part.
     * @param stock The current inventory level (stock) of the part.
     * @param min The minimum acceptable stock level for the part.
     * @param max The maximum acceptable stock level for the part.
     */
    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Retrieves the unique identifier of the part.
     *
     * @return The ID of the part.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the part.
     *
     * @param id The new ID to set for the part.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the part.
     *
     * @return The name of the part.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the part.
     *
     * @param name The new name to set for the part.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the price per unit of the part.
     *
     * @return The price of the part.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price per unit of the part.
     *
     * @param price The new price to set for the part.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the current inventory level (stock) of the part.
     *
     * @return The current stock of the part.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the current inventory level (stock) of the part.
     *
     * @param stock The new stock level to set for the part.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Retrieves the minimum acceptable stock level for the part.
     *
     * @return The minimum stock level of the part.
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the minimum acceptable stock level for the part.
     *
     * @param min The new minimum stock level to set for the part.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Retrieves the maximum acceptable stock level for the part.
     *
     * @return The maximum stock level of the part.
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the maximum acceptable stock level for the part.
     *
     * @param max The new maximum stock level to set for the part.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Compares this Part object with another object for equality.
     * Two Part objects are considered equal if they are the same instance
     * or if they are both non-null Part objects and have the same {@code id}.
     *
     * @param o The object to compare with this Part.
     * @return {@code true} if the given object is equal to this Part; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;
        // Two parts are considered equal if their IDs are the same.
        // This assumes 'id' is a unique identifier for parts.
        return id == part.id;
    }

    /**
     * Returns a hash code value for the Part object.
     * The hash code is based on the {@code id} of the part, aligning with the
     * {@code equals} method's definition for consistent behavior in hash-based collections.
     *
     * @return A hash code value for this Part.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a string representation of the Part object.
     * This string includes the part's ID and name, providing a concise summary.
     *
     * @return A string in the format "Part{partId=ID, name='Name'}".
     */
    @Override
    public String toString() {
        return "Part{partId=" + id + ", name='" + name + "'}";
    }
}