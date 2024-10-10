package com.example.project2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import javafx.stage.FileChooser;

public class AssessDisasterViewController {


    public Button refreshButton;
    public Button exportButton;
    public Button CloseButton;
    public AnchorPane headerPane;
    ;
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
    @FXML

    TableColumn<DisasterRecord, String> priorityColumn;

    ObservableList<DisasterRecord> disasterRecords;

    DatabaseConnection dbConnection;

    @FXML
    private double xOffset = 0;
    private double yOffset = 0;

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
        
        dbConnection = new DatabaseConnection();  // Initialize your database connection class

        // Set up the table columns
        disasterNoColumn.setCellValueFactory(new PropertyValueFactory<>("disasterNo"));
        disasterTypeColumn.setCellValueFactory(new PropertyValueFactory<>("disasterType"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        DepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        disasterRecords = FXCollections.observableArrayList();
        disasterTableView.setItems(disasterRecords);

        // Load disaster records from the database
        loadDisasterRecords();

        // Add a listener for row selection
        disasterTableView.setOnMouseClicked(this::handleRowSelect);
        exportButton.setOnAction(event -> exportToCSV());

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
            stage.initStyle(StageStyle.UNDECORATED);
            //stage.setTitle("Edit Disaster");
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
                record.getDescription(),
                record.getPriority(),
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



    @FXML
    void loadDisasterRecords() {
        String query = "SELECT d.id, dt.type_name, l.location_name, dep.DepartmentName, p.priority_level, d.description " +
                "FROM disasters d " +
                "JOIN disaster_types dt ON d.disaster_type_id = dt.id " +
                "JOIN locations l ON d.location_id = l.id " +
                "JOIN departments dep ON d.department_id = dep.DepartmentID " +
                "JOIN priorities p ON d.priority_id = p.id";



        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            disasterRecords.clear();

            while (rs.next()) {
                String disasterNo = String.format("%03d", rs.getInt("id"));  // Add leading zeros for visual purposes
                String disasterType = rs.getString("type_name");
                String location = rs.getString("location_name");
                String department = rs.getString("DepartmentName");
                String priority = rs.getString("priority_level");  // Fetch priority level
                String description = rs.getString("description");

                // Create a new DisasterRecord and add it to the list
                DisasterRecord record = new DisasterRecord(disasterNo, disasterType, location, department,priority,description);
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

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }



    @FXML
    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show save file dialog
        File file = fileChooser.showSaveDialog(getStage());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write the header line
                writer.write("Disaster no.,Disaster Type,Location,Department,Priority");
                writer.newLine();

                // Write each row from the TableView to the CSV
                for (DisasterRecord record : disasterTableView.getItems()) {
                    writer.write(record.getDisasterNo() + "," +
                            record.getDisasterType() + "," +
                            record.getLocation() + "," +
                            record.getDepartment() + "," +
                            record.getPriority());
                    writer.newLine();
                }

                showAlert(Alert.AlertType.INFORMATION, "Success", "Data exported successfully to " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to export data.");
            }
        }
    }

}
