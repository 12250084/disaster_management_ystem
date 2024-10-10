package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssessDisasterController {

    public AnchorPane headerPane;
    public Button CloseButton;
    @FXML
    ComboBox<String> disasterTypeComboBox;
    @FXML
    ComboBox<String> locationComboBox;
    @FXML
    ComboBox<String> departmentComboBox;

    @FXML
    TextArea descriptionTextArea;
    @FXML
    ComboBox<String> priorityComboBox; // Assuming the priority is a number
    @FXML
    private Button savePriorityButton;
    @FXML
    Label disasterNo;

    private DatabaseConnection dbConnection;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML

    void initialize() {
        // Track mouse events for moving the window using the top pane
        headerPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        headerPane.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        dbConnection = new DatabaseConnection();
        loadDisasterTypes();
        loadLocations();
        loadPriorities();
        loadDepartments();

        Integer nextDisasterId = getNextDisasterId();
        if (nextDisasterId != null) {
            String formattedId = String.format("%03d", nextDisasterId);
            disasterNo.setText(formattedId);
        } else {
            disasterNo.setText("Error retrieving ID");
        }

    }

    @FXML
    void handleSavePriority(ActionEvent event) {
        // Retrieve selected values
        String selectedDisasterType = disasterTypeComboBox.getValue();
        String selectedLocation = locationComboBox.getValue();
        String description = descriptionTextArea.getText();
        String selectedPriority = priorityComboBox.getValue();
        String selectedDepartment = departmentComboBox.getValue(); // New addition

        // Validate inputs
        if (selectedDisasterType == null || selectedLocation == null || selectedPriority == null || selectedDepartment == null || description.isEmpty()) {
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all required fields.");
            return;
        }

        // Get disaster type ID
        Integer disasterTypeId = getDisasterTypeId(selectedDisasterType);
        if (disasterTypeId == null) {
            showAlert(AlertType.ERROR, "Error", "Disaster type not found.");
            return;
        }

        // Get location ID
        Integer locationId = getLocationId(selectedLocation);
        if (locationId == null) {
            showAlert(AlertType.ERROR, "Error", "Location not found.");
            return;
        }

        // Get Priority ID
        Integer priorityID = getPriorityId(selectedPriority);
        if (priorityID == null) {
            showAlert(AlertType.ERROR, "Error", "Priority not found.");
            return;
        }

        // Get Department ID
        Integer departmentID = getDepartmentId(selectedDepartment);
        if (departmentID == null) {
            showAlert(AlertType.ERROR, "Error", "Department not found.");
            return;
        }

        // Insert into DisasterReports table
        try (Connection conn = dbConnection.getConnection()) {
            // SQL query to insert a record into the disasters table
            String sql = "INSERT INTO disasters (disaster_type_id, location_id, description, priority_id, department_id, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set values for the prepared statement
                pstmt.setInt(1, disasterTypeId);    // disaster_type_id from the disaster type selection
                pstmt.setInt(2, locationId);        // location_id from the location selection
                pstmt.setString(3, description);   // Description of the disaster report
                pstmt.setInt(4, priorityID);        // priority_id from the priority selection
                pstmt.setInt(5, departmentID);      // department_id from the department selection

                // Execute the insert operation
                pstmt.executeUpdate();
                showAlert(AlertType.INFORMATION, "Success", "Disaster report saved successfully.");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to save disaster report.");
        }
    }


    private void loadDisasterTypes() {
        // Clear existing items in the ComboBox
        disasterTypeComboBox.getItems().clear();

        // SQL query to fetch disaster types from the Disasters table
        String sql = "SELECT type_name FROM disaster_types"; // Adjust the column name based on your table structure

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate over the result set and add disaster types to the ComboBox
            while (rs.next()) {
                String disasterType = rs.getString("type_name");
                disasterTypeComboBox.getItems().add(disasterType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load disaster types.");
        }
    }

    private void loadLocations() {
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

    private void loadPriorities() {
        // Clear existing items in the ComboBox
        priorityComboBox.getItems().clear();

        // SQL query to fetch priorities from the priorities table
        String sql = "SELECT  priority_level FROM priorities ORDER BY id DESC"; // Adjust column names as necessary

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate over the result set and add priorities to the ComboBox
            while (rs.next()) {
                String priorityLevel = rs.getString("priority_level");

                // Assuming you want to display the priority level and store the ID
                priorityComboBox.getItems().add(priorityLevel); // Store the ID in the ComboBox
                // You might need to use a custom cell factory if you want to display the level text
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load priorities.");
        }
    }

    private void loadDepartments() {
        // Clear existing items in the ComboBox
        departmentComboBox.getItems().clear();

        // SQL query to fetch departments from the Departments table
        String sql = "SELECT DepartmentName FROM departments"; // Adjust the column name based on your table structure

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Iterate over the result set and add departments to the ComboBox
            while (rs.next()) {
                String departmentName = rs.getString("DepartmentName");
                departmentComboBox.getItems().add(departmentName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load departments.");
        }
    }



    Integer getDisasterTypeId(String disasterTypeName) {
        Integer disasterTypeId = null;
        String query = "SELECT id FROM disaster_types WHERE type_name = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, disasterTypeName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    disasterTypeId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disasterTypeId;
    }


    Integer getLocationId(String locationName) {
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

    Integer getPriorityId(String priorityLevel) {
        Integer priorityId = null;
        String query = "SELECT id FROM priorities WHERE priority_level = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, priorityLevel);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    priorityId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return priorityId;
    }

    private Integer getNextDisasterId() {
        Integer nextDisasterId = null;
        String query = "SELECT MAX(id) AS max_id FROM disasters";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                Integer maxId = rs.getInt("max_id");
                nextDisasterId = (maxId == null) ? 1 : maxId + 1; // Start from 1 if no records exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextDisasterId;
    }
    Integer getDepartmentId(String departmentName) {
        Integer departmentId = null;
        String query = "SELECT DepartmentID FROM departments WHERE DepartmentName = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, departmentName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    departmentId = rs.getInt("DepartmentID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departmentId;
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
