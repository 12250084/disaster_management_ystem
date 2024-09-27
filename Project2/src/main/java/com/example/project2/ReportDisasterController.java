package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDisasterController {

    @FXML
    ComboBox<String> disasterTypeComboBox;
    @FXML
    ComboBox<String> locationComboBox;
    @FXML
    TextField locationTextField;
    @FXML
    TextArea descriptionTextArea;
    @FXML
    Button submitButton;
    @FXML
    Hyperlink currentLocationLink;

    private DatabaseConnection dbConnection;

    @FXML
    private void initialize() {
        // Initialize database connection
        dbConnection = new DatabaseConnection();

        // Set up disaster type ComboBox
        loadDisasterTypes();

        // Set up location ComboBox
        loadLocations();

        // Set the action for the current location link
        if (currentLocationLink != null) {
            currentLocationLink.setOnAction(this::handleCurrentLocation);
        }
    }

    @FXML

    private void handleSubmit(ActionEvent event) {
        // Retrieve the selected disaster type and other form data
        String selectedDisasterType = disasterTypeComboBox.getValue();
        String locationName = locationComboBox.getValue();
        String description = descriptionTextArea.getText();
        Double latitude = 1.00;  // Ensure these values are set correctly
        Double longitude = 10.00; // Ensure these values are set correctly

        // Validate inputs
        if (selectedDisasterType == null || locationName.isEmpty() || latitude == null || longitude == null) {
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all required fields.");
            return;
        }

        // Get disaster type ID based on the selected type
        Integer disasterTypeId = getDisasterTypeId(selectedDisasterType);
        if (disasterTypeId == null) {
            showAlert(AlertType.ERROR, "Invalid Disaster Type", "The selected disaster type is invalid.");
            return;
        }

        // Get location ID based on the location name
        Integer locationId = getLocationId(locationName);
        if (locationId == null) {
            showAlert(AlertType.ERROR, "Invalid Location", "No matching location found for: " + locationName);
            return;
        }

        // Save data to the database
        try (Connection conn = dbConnection.getConnection()) {
            String sql = "INSERT INTO disaster_reports (disaster_type_id, location_id, description, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, disasterTypeId); // Disaster type ID
                pstmt.setInt(2, locationId); // Location ID
                pstmt.setString(3, description); // Description
                pstmt.setDouble(4, latitude); // Latitude
                pstmt.setDouble(5, longitude); // Longitude

                pstmt.executeUpdate();
                showAlert(AlertType.INFORMATION, "Success", "Disaster report submitted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to submit disaster report.");
        }
    }


    // Method to get disaster type ID based on disaster type
    private Integer getDisasterTypeId(String disasterType) {
        // Implement logic to retrieve disaster type ID from the database
        // Example implementation, replace with actual logic
        try (Connection conn = dbConnection.getConnection()) {
            String sql = "SELECT id FROM disaster_types WHERE type_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, disasterType);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Integer getLocationId(String locationName) {
        Integer locationId = null;
        String query = "SELECT id FROM locations WHERE location_name = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, locationName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    locationId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return locationId;
    }


    @FXML
    private void handleCurrentLocation(ActionEvent event) {
        // Simulate getting current location (e.g., via GPS or API)
        String simulatedLocation = "Latitude: 34.0522, Longitude: -118.2437"; // Example coordinates
        locationTextField.setText(simulatedLocation);
    }

    void loadDisasterTypes() {
        // Clear existing items
        disasterTypeComboBox.getItems().clear();

        // Fetch disaster types from the database and populate the disasterTypeComboBox
        try (Connection conn = dbConnection.getConnection()) {
            String sql = "SELECT type_name FROM disaster_types"; // Adjust the column name if necessary
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet resultSet = pstmt.executeQuery()) {

                while (resultSet.next()) {
                    String disasterType = resultSet.getString("type_name");
                    disasterTypeComboBox.getItems().add(disasterType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load disaster types.");
        }
    }


    void loadLocations() {
        // Load locations from the database and populate the locationComboBox
        try (Connection conn = dbConnection.getConnection()) {
            String sql = "SELECT location_name FROM locations"; // Adjust column names as necessary
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    String locationName = resultSet.getString("location_name");
                    locationComboBox.getItems().add(locationName); // Only show location name
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load locations.");
        }
    }


    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
