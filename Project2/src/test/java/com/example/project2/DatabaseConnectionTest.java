package com.example.project2;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    public void testGetConnection() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.getConnection();

        // Check if the connection is not null
        assertNotNull(connection, "Database connection should not be null");

        try {
            // Check if the connection is valid
            assertTrue(connection.isValid(2), "Database connection should be valid");
        } catch (Exception e) {
            e.printStackTrace();
            fail("An error occurred while checking if the connection is valid: " + e.getMessage());
        } finally {
            try {
                // Clean up: close the connection if it is not null
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("An error occurred while closing the connection: " + e.getMessage());
            }
        }
    }
}
