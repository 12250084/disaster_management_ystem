package com.example.project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssessDisasterViewController {

    @FXML
    TableView<DisasterRecord> disasterTableView;

    @FXML
    TableColumn<DisasterRecord, String> disasterNoColumn;

    @FXML
    TableColumn<DisasterRecord, String> disasterTypeColumn;

    @FXML
    TableColumn<DisasterRecord, String> locationColumn;

    @FXML
    TableColumn<DisasterRecord, String> DepartmentColumn;

    ObservableList<DisasterRecord> disasterRecords;

    DatabaseConnection dbConnection;

    @FXML
    public void initialize() {
        dbConnection = new DatabaseConnection();  // Initialize your database connection class

        // Set up the table columns
        disasterNoColumn.setCellValueFactory(new PropertyValueFactory<>("disasterNo"));
        disasterTypeColumn.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        DepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        disasterRecords = FXCollections.observableArrayList();
        disasterTableView.setItems(disasterRecords);

        // Load disaster records from the database
        loadDisasterRecords();

        // Add a listener for row selection
        disasterTableView.setOnMouseClicked(event -> handleRowSelect(event));
    }
    // Method to handle row selection
    private void handleRowSelect(MouseEvent event) {
        // Check if a row is clicked twice
        if (event.getClickCount() == 2) {
            // Get selected disaster
            DisasterRecord selectedDisaster = disasterTableView.getSelectionModel().getSelectedItem();

            if (selectedDisaster != null) {
                // Open the edit window or display the details
                showEditWindow(selectedDisaster);
            } else {
                Alert alert = new Alert(AlertType.WARNING, "No Disaster Selected");
                alert.show();
            }
        }
    }

    void showEditWindow(DisasterRecord selectedDisasterRecord) {
        Disaster disaster = convertToDisaster(selectedDisasterRecord);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditDisaster.fxml"));
            Parent root = loader.load();
            EditDisasterController controller = loader.getController();
            controller.setDisaster(disaster);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Disaster");
            stage.show();

            closeCurrentStage();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Failed to load the edit window.");
        }
    }

    private Disaster convertToDisaster(DisasterRecord record) {
        return new Disaster(
                record.getDisasterNo(),
                record.getDisasterType(),
                record.getLocation(),
                "",
                "",
                record.getDepartment()
        );
    }

    // Method to get the current stage (AssessDisasterView)
    private Stage getStage() {
        return (Stage) disasterTableView.getScene().getWindow();
    }

    // Method to close the current stage
    private void closeCurrentStage() {
        Stage stage = getStage();
        if (stage != null) {
            stage.close();
        }
    }



    void loadDisasterRecords() {
        String query = "SELECT d.id, dt.type_name, l.location_name,dep.DepartmentName  FROM disasters d JOIN disaster_types dt ON d.disaster_type_id = dt.id JOIN locations l ON d.location_id = l.id JOIN departments dep ON d.department_id =dep.DepartmentID";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            disasterRecords.clear();

            while (rs.next()) {
                String disasterNo = String.format("%03d", rs.getInt("id"));  // Add leading zeros for visual purposes
                String disasterType = rs.getString("type_name");
                String location = rs.getString("location_name");
                String department = rs.getString("DepartmentName");

                // Create a new DisasterRecord and add it to the list
                DisasterRecord record = new DisasterRecord(disasterNo, disasterType, location, department);
                disasterRecords.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to load disaster records.");
        }
    }

    void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
