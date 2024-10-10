package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HomeController {

    public Button aboutButton;
    public AnchorPane headerPane;
    public Button CloseButton;
    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label welcomeText;

    @FXML
    private Label logoutLabel; // Reference to the label

    @FXML
    private Button dashboardButton;

    @FXML
    private Button reportDisasterButton;

    @FXML
    private Button accessDisasterButton;

    @FXML
    private Button assessDisasterViewButton;

    // Initialize the UI state
    @FXML
    private double xOffset = 0;
    private double yOffset = 0;
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

        // Load the image and set it as the graphic for the logout button
        Image logoutImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/logout.png")));
        ImageView imageView = new ImageView(logoutImage);
        imageView.setFitHeight(32);
        imageView.setFitWidth(32);
        logoutButton.setGraphic(imageView);
        // When starting, login button should be enabled, and logout button should be hidden
        loginButton.setDisable(true);
        logoutLabel.setVisible(true);  // Hide label initially
        //logoutButton.setVisible(false);

//        Image loginImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/login.png")));
//        ImageView imageView2 = new ImageView(logoutImage);
//        imageView2.setFitHeight(32);
//        imageView2.setFitWidth(32);
//        logoutButton.setGraphic(imageView2);
        // Optionally, you can also disable other buttons until login
//        dashboardButton.setDisable(true);
//        reportDisasterButton.setDisable(true);
//        accessDisasterButton.setDisable(true);
//        assessDisasterViewButton.setDisable(true);
        dashboardButton.setOnAction(this::handleDashboardButtonClick);
    }

    // Method to handle the button click
    public void handleDashboardButtonClick(ActionEvent event) {
        // Get all open windows
        List<Window> windows = new ArrayList<>(Window.getWindows());

        // Close all windows except the current one
        for (Window window : windows) {
            if (window instanceof Stage && window != ((Node) event.getSource()).getScene().getWindow()) {
                ((Stage) window).close();
            }
        }
    }



    @FXML
    public void onloginButtonClick() {
        try {
            // Load the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            //loginStage.setTitle("Login");
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
            logoutLabel.setVisible(true);  // Show label after login


            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("register.fxml")));
            Stage stage = new Stage();
//            stage.initStyle(StageStyle.DECORATED);
            stage.initStyle(StageStyle.UNDECORATED);

            // Create a new scene
            Scene scene = new Scene(root, 829, 695);

            // Add the CSS file
            scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());

            // Close the home window after showing login window
            handleCancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onReportDisasterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportDisaster.fxml"));
            Parent reportDisasterRoot = loader.load();
            Stage reportDisasterStage = new Stage();
            reportDisasterStage.initStyle(StageStyle.UNDECORATED);
//            reportDisasterStage.setTitle("Report Disaster");
            reportDisasterStage.setScene(new Scene(reportDisasterRoot));
            reportDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAssessDisasterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisaster.fxml"));
            Parent assessDisasterRoot = loader.load();
            Stage assessDisasterStage = new Stage();
            //assessDisasterStage.setTitle("Assess Disaster");
            assessDisasterStage.initStyle(StageStyle.UNDECORATED);
            assessDisasterStage.setScene(new Scene(assessDisasterRoot));
            assessDisasterStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAssessDisasterViewButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisasterView.fxml"));
            Parent assessDisasterViewRoot = loader.load();
            Stage assessDisasterViewStage = new Stage();
            assessDisasterViewStage.initStyle(StageStyle.UNDECORATED);
            //assessDisasterViewStage.setTitle("Assess Disaster View");
            assessDisasterViewStage.setScene(new Scene(assessDisasterViewRoot));
            assessDisasterViewStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAboutButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent aboutRoot = loader.load();
            Stage aboutStage = new Stage();
            aboutStage.initStyle(StageStyle.UNDECORATED);
           // aboutStage.setTitle("About");
            aboutStage.setScene(new Scene(aboutRoot));
            aboutStage.show();
            Stage stage = (Stage) aboutButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle the logout button click
    @FXML
    public void onLogoutButtonClick() {
        // Enable the login button and hide the logout button
        loginButton.setDisable(false);
        logoutButton.setVisible(false);

        // Optionally, disable other buttons after logout
        dashboardButton.setDisable(true);
        reportDisasterButton.setDisable(true);
        accessDisasterButton.setDisable(true);
        assessDisasterViewButton.setDisable(true);
        logoutLabel.setVisible(false);
    }

    // Utility method to close the current window
    private void closeWindow() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    @FXML
    private void handleCloseButton(ActionEvent event) {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close Confirmation");
        alert.setHeaderText("Are you sure you want to close the window?");
        alert.setContentText("Press OK to confirm or Cancel to stay.");

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // Check if OK was clicked
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Close the window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
        // If Cancel was pressed or the dialog was closed, nothing happens
    }

    // Method to simulate successful login
    private void successfulLogin() {
        // Disable the login button and show the logout button
        loginButton.setDisable(true);
        logoutButton.setVisible(true);

        // Enable other buttons after successful login
        dashboardButton.setDisable(false);
        reportDisasterButton.setDisable(false);
        accessDisasterButton.setDisable(false);
        assessDisasterViewButton.setDisable(false);
    }
}
