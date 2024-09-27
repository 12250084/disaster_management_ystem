package com.example.project2;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button loginButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button reportDisasterButton;

    @FXML
    private Button accessDisasterButton;

    @FXML
    private Button assessDisasterViewButton;

    @FXML
    public void initialize() {
        // Disable buttons initially
//        dashboardButton.setDisable(true);
//        reportDisasterButton.setDisable(true);
//        accessDisasterButton.setDisable(true);
//        assessDisasterViewButton.setDisable(true);
    }



    @FXML
    public void onloginButtonClick() {

        try {
            // Load the Report Disaster FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent reportDisasterRoot = loader.load();

            // Create a new stage for the Report Disaster view
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
            handleCancel();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }

    @FXML
    public void onReportDisasterButtonClick() {
        try {
            // Load the Report Disaster FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportDisaster.fxml"));
            Parent reportDisasterRoot = loader.load();

            // Create a new stage for the Report Disaster view
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }

    @FXML
    public void onAssessDisasterButtonClick() {
        try {
            // Load the Report Disaster FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisaster.fxml"));
            Parent reportDisasterRoot = loader.load();

            // Create a new stage for the Report Disaster view
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }

    @FXML
    public void onAssessDisasterViewButtonClick() {
        try {
            // Load the Report Disaster FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisasterView.fxml"));
            Parent reportDisasterRoot = loader.load();

            // Create a new stage for the Report Disaster view
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }

    }

    @FXML
    public void onAboutButtonClick() {
        try {
            // Load the Report Disaster FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent reportDisasterRoot = loader.load();

            // Create a new stage for the Report Disaster view
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }

    }
    // Handle cancel button action
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Utility method to close the edit window
    private void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }




}