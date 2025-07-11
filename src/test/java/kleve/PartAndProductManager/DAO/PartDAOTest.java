package kleve.PartAndProductManager.DAO;

import kleve.PartAndProductManager.datamodel.InHouse;
import kleve.PartAndProductManager.datamodel.Part;
import kleve.PartAndProductManager.datamodel.Outsourced;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.collections.ObservableList;

/**
 * Unit tests (more accurately, integration tests) for the PartDAO class using JUnit 5.
 * This class assumes a working MySQL database connection via the JDBC utility.
 * IMPORTANT: These tests will modify your database. It's recommended to run them on a
 * development or test database, not a production one.
 */
class PartDAOTest {

    private InHouse testInHousePart;
    private Outsourced testOutsourcedPart;

    /**
     * Set up the database connection once before all tests in this class.
     * Annotated with @BeforeAll for JUnit 5. Must be static.
     */
    @BeforeAll
    static void setupAll() {
        JDBC.openConnection(); // Open connection for all tests
        System.out.println("Database connection opened for PartDAOTest suite.");

        UserDAO.userId = 1;
        System.out.println("Dummy UserID set for testing: " + UserDAO.userId);
    }

    /**
     * Clean up and prepare the database before each test method.
     * This is crucial for integration tests to ensure test independence.
     * Annotated with @BeforeEach for JUnit 5.
     */
    @BeforeEach
    void setupEach() {
        // Clear the parts table before each test to ensure a clean state
        try (PreparedStatement statement = JDBC.connection.prepareStatement("DELETE FROM parts")) {
            statement.executeUpdate();
            System.out.println("Parts table cleared before test.");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to clear parts table before test: " + e.getMessage());
        }

        // Initialize fresh test part data for each test
        testInHousePart = new InHouse(0, "Test InHouse Part", 10.50, 5, 1, 10, 101);
        testOutsourcedPart = new Outsourced(0, "Test Outsourced Part", 20.00, 10, 5, 20, "TestCo");
        System.out.println("Test data initialized for current test.");
    }

    /**
     * Test case for adding an InHouse part.
     * Annotated with @Test for JUnit 5.
     */
    @Test
    @DisplayName("1. Test adding an InHouse part successfully")
    void testAddInHousePart() {
        System.out.println("Running testAddInHousePart...");
        PartDAO.addPart(testInHousePart);

        ObservableList<Part> allParts = PartDAO.getAllParts();
        assertNotNull(allParts, "getAllParts should not return null");
        assertFalse(allParts.isEmpty(), "Part list should not be empty after adding");
        assertEquals(1, allParts.size(), "Should contain exactly one part after adding");

        Part retrievedPart = allParts.get(0); // Get the first (and only) part, assuming sequential IDs or just one part
        // testPartId = retrievedPart.getId(); // Storing ID if needed for subsequent (chained) tests, but @BeforeEach cleans up.

        // Assert that the retrieved part's details match the added part's details
        assertEquals(testInHousePart.getName(), retrievedPart.getName(), "Retrieved part name should match");
        assertEquals(testInHousePart.getPrice(), retrievedPart.getPrice(), "Retrieved part price should match");
        assertTrue(retrievedPart instanceof InHouse, "Retrieved part should be an InHouse instance");
        assertEquals(testInHousePart.getMachineId(), ((InHouse) retrievedPart).getMachineId(), "Retrieved machine ID should match");

        System.out.println("TestAddInHousePart Passed.");
    }

    /**
     * Test case for looking up a part by its ID.
     */
    @Test
    @DisplayName("2. Test looking up a part by ID")
    void testLookupPartById() {
        System.out.println("Running testLookupPartById...");
        PartDAO.addPart(testInHousePart); // Add a part first
        // Get the ID that was assigned by the DAO/DB after addition
        int addedId = PartDAO.getAllParts().get(0).getId();

        Part foundPart = PartDAO.getPart(addedId);
        assertNotNull(foundPart, "Part should be found by ID");
        assertEquals(addedId, foundPart.getId(), "Found part ID should match lookup ID");
        assertEquals(testInHousePart.getName(), foundPart.getName(), "Found part name should match original");
        System.out.println("TestLookupPartById Passed.");
    }

    /**
     * Test case for looking up a part by its name (exact match).
     */
    @Test
    @DisplayName("3. Test looking up a part by Name (exact match)")
    void testLookupPartByNameExact() {
        System.out.println("Running testLookupPartByNameExact...");
        PartDAO.addPart(testInHousePart); // Add a part first

        ObservableList<Part> foundParts = PartDAO.getPart(testInHousePart.getName());
        assertNotNull(foundParts, "Lookup by name should not return null");
        assertFalse(foundParts.isEmpty(), "Found parts list should not be empty");
        assertEquals(1, foundParts.size(), "Should find exactly one part by exact name");
        assertEquals(testInHousePart.getName(), foundParts.get(0).getName(), "Found part name should match exact name");
        System.out.println("TestLookupPartByNameExact Passed.");
    }

    /**
     * Test case for updating an existing part.
     */
    @Test
    @DisplayName("4. Test updating an existing part")
    void testUpdatePart() {
        System.out.println("Running testUpdatePart...");
        PartDAO.addPart(testInHousePart); // Add original part
        int originalId = PartDAO.getAllParts().get(0).getId(); // Get the ID

        // Create an updated version of the part, ensuring ID is set correctly
        InHouse updatedPart = new InHouse(originalId, "Updated InHouse Part", 12.75, 7, 2, 15, 202);

        PartDAO.updatePart(updatedPart); // Pass ID and the updated Part object

        Part retrievedPart = PartDAO.getPart(originalId);
        assertNotNull(retrievedPart, "Updated part should still be found");
        assertEquals(updatedPart.getName(), retrievedPart.getName(), "Updated part name should match");
        assertEquals(updatedPart.getPrice(), retrievedPart.getPrice(), "Updated part price should match");
        assertEquals(updatedPart.getStock(), retrievedPart.getStock(), "Updated part stock should match");
        assertTrue(retrievedPart instanceof InHouse, "Retrieved part should still be InHouse");
        assertEquals(updatedPart.getMachineId(), ((InHouse) retrievedPart).getMachineId(), "Updated machine ID should match");
        System.out.println("TestUpdatePart Passed.");
    }

    /**
     * Test case for deleting a part.
     */
    @Test
    @DisplayName("5. Test deleting a part successfully")
    void testDeletePart() {
        System.out.println("Running testDeletePart...");
        PartDAO.addPart(testInHousePart); // Add a part to delete
        int partToDeleteId = PartDAO.getAllParts().get(0).getId(); // Get the ID

        boolean isDeleted = PartDAO.deletePart(partToDeleteId);
        assertTrue(isDeleted, "Part should be successfully deleted");

        Part deletedPart = PartDAO.getPart(partToDeleteId);
        assertNull(deletedPart, "Deleted part should no longer be found");
        assertEquals(0, PartDAO.getAllParts().size(), "Part list should be empty after deletion");
        System.out.println("TestDeletePart Passed.");
    }

    /**
     * Test case for retrieving all parts.
     */
    @Test
    @DisplayName("6. Test retrieving all parts")
    void testGetAllParts() {
        System.out.println("Running testGetAllParts...");
        PartDAO.addPart(testInHousePart);
        PartDAO.addPart(testOutsourcedPart); // Add both types of parts

        ObservableList<Part> allParts = PartDAO.getAllParts();
        assertNotNull(allParts, "getAllParts should not return null");
        assertEquals(2, allParts.size(), "Should retrieve both added parts");
        System.out.println("TestGetAllParts Passed.");
    }


    /**
     * Clean up the database connection after all tests in this class are finished.
     * Annotated with @AfterAll for JUnit 5. Must be static.
     */
    @AfterAll
    static void tearDownAll() {
        // This method will run once after all tests in this class have completed.
        JDBC.closeConnection(); // Close connection after all tests
        System.out.println("Database connection closed after PartDAOTest suite.");
    }
}