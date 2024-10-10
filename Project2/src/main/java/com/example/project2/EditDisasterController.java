package com.example.project2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditDisasterController {

    public Button CloseButton;
    public AnchorPane headerPane;


    @FXML
    private TextField disasterNoField;

    @FXML
    private ComboBox<String> disasterTypeComboBox;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ComboBox<String> priorityComboBox;

    @FXML
    private ComboBox<String> departmentComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private Disaster selectedDisaster;
    private DatabaseConnection dbConnection;

    @FXML
    private double xOffset = 0;
    private double yOffset = 0;

    // Initialize method to populate ComboBoxes and set initial data
    @FXML
    public void initialize() {
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
    }

    // Method to set the selected disaster data into the fields
    public void setDisaster(Disaster disaster) {
        this.selectedDisaster = disaster;

        // Load all combo box values first
        loadDisasterTypes();
        loadLocations();
        loadPriorities();
        loadDepartments();

        // Now populate fields
        populateFields();
    }

    private void populateFields() {
        if (selectedDisaster != null) {
            disasterNoField.setText(String.valueOf(selectedDisaster.getId()));
            disasterTypeComboBox.setValue(selectedDisaster.getType());
            locationComboBox.setValue(selectedDisaster.getLocation());
            descriptionTextArea.setText(selectedDisaster.getDescription());

            // Get the priority and check if it's null or empty before applying substring
            String disasterPriority = selectedDisaster.getPriority();
            if (disasterPriority != null && !disasterPriority.isEmpty()) {
                disasterPriority = disasterPriority.substring(0, 1).toUpperCase() + disasterPriority.substring(1).toLowerCase();
                priorityComboBox.setValue(disasterPriority);
            } else {
                System.out.println("Disaster priority is null or empty, cannot set in ComboBox.");
            }

            departmentComboBox.setValue(selectedDisaster.getDepartment());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Selected disaster record is not available.");
        }
    }



    // Handle save button action
    @FXML
    private void handleSave() {
        if (disasterTypeComboBox.getValue() == null || locationComboBox.getValue() == null || priorityComboBox.getValue() == null || departmentComboBox.getValue() == null || descriptionTextArea.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all required fields.");
            return;
        }

        Integer disasterTypeId = getDisasterTypeId(disasterTypeComboBox.getValue());
        Integer locationId = getLocationId(locationComboBox.getValue());
        Integer priorityId = getPriorityId(priorityComboBox.getValue());
        Integer departmentId = getDepartmentId(departmentComboBox.getValue());

        if (disasterTypeId == null || locationId == null || priorityId == null || departmentId == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "One or more IDs could not be retrieved.");
            return;
        }

        try (Connection conn = dbConnection.getConnection()) {
            String sql = "UPDATE disasters SET disaster_type_id = ?, location_id = ?, description = ?, priority_id = ?, department_id = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, disasterTypeId);
                pstmt.setInt(2, locationId);
                pstmt.setString(3, descriptionTextArea.getText());
                pstmt.setInt(4, priorityId);
                pstmt.setInt(5, departmentId);
                pstmt.setInt(6, selectedDisaster.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Disaster record updated successfully.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "No Change", "No record was updated.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update disaster record.");
        }

        closeWindow();
    }

    // Handle delete button action
    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this disaster?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try (Connection conn = dbConnection.getConnection()) {
                String sql = "DELETE FROM disasters WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, selectedDisaster.getId());

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Disaster record deleted successfully.");
                    } else {
                        showAlert(Alert.AlertType.WARNING, "No Change", "No record was deleted.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete disaster record.");
            }

            closeWindow();
        }
    }

    // Handle cancel button action
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Utility method to close the edit window
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void loadDisasterTypes() {
        disasterTypeComboBox.getItems().clear();
        String sql = "SELECT type_name FROM disaster_types";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                disasterTypeComboBox.getItems().add(rs.getString("type_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load disaster types.");
        }
    }

    private void loadLocations() {
        locationComboBox.getItems().clear();
        String sql = "SELECT location_name FROM locations";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                locationComboBox.getItems().add(rs.getString("location_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load locations.");
        }
    }

    private void loadPriorities() {
        priorityComboBox.getItems().clear();
        String sql = "SELECT priority_level FROM priorities ORDER BY id DESC";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String priorityLevel = rs.getString("priority_level");
                //System.out.println("Priority Level fetched from DB: " + priorityLevel); // Debugging

                // Populate ComboBox
                priorityComboBox.getItems().add(priorityLevel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load priorities.");
        }
    }





    private void loadDepartments() {
        departmentComboBox.getItems().clear();
        String sql = "SELECT DepartmentName FROM departments";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                departmentComboBox.getItems().add(rs.getString("DepartmentName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load departments.");
        }
    }

    private Integer getDisasterTypeId(String disasterTypeName) {
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

    private Integer getPriorityId(String priorityLevel) {
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


    private Integer getDepartmentId(String departmentName) {
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

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
