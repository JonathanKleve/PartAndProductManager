package kleve.testtwo.datamodel;

import java.time.LocalDateTime;

/**
 * Represents a single item in a report, providing a simplified view of either a {@link Part} or a {@link Product}.
 * This immutable class is designed to hold key information relevant for reporting purposes,
 * such as its ID, name, type (e.g., "Part" or "Product"), current stock level, and the timestamp of its last update.
 *
 * @author Jonathan Kleve
 */
public class ReportItem {
    private final int id;
    private final String name;
    private final String type; // "Part" or "Product"
    private final int stock;
    private final LocalDateTime lastUpdated;

    /**
     * Constructs a new `ReportItem` with the specified details.
     *
     * @param id The unique identifier of the item (either a Part ID or a Product ID).
     * @param name The name of the item.
     * @param type A string indicating the category of the item, typically "Part" or "Product".
     * @param stock The current inventory level of the item.
     * @param lastUpdated The {@link LocalDateTime} timestamp when the item was last updated in the database.
     */
    public ReportItem(int id, String name, String type, int stock, LocalDateTime lastUpdated) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Retrieves the unique identifier of the item.
     *
     * @return The ID of the item.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the type of the item (e.g., "Part" or "Product").
     *
     * @return A string representing the type of the item.
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the current stock level of the item.
     *
     * @return The stock level of the item.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Retrieves the timestamp indicating when the item was last updated.
     *
     * @return The {@link LocalDateTime} when the item was last updated.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}