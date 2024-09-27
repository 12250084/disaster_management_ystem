package com.example.project2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterController {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField confirmPasswordTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label registrationMessageLabel;


    public void registerUser() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String username = userNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();

        // Validate input
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registrationMessageLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registrationMessageLabel.setText("Passwords do not match.");
            return;
        }

        // Additional validation logic (e.g., email format, password strength) can be added here

        // Call method to save registration data to database
        if (saveRegistrationData(username,password, firstName, lastName,email)) {
            registrationMessageLabel.setText("Registration successful!");
            dashboard();
        } else {
            registrationMessageLabel.setText("Registration failed. Try again.");
        }
    }

    private void dashboard() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setScene(new Scene(root,829,695));
            stage.show();
            setCancelButton();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions if necessary
        }
    }
    public void setCancelButton() {

        onloginButtonClick();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    private boolean saveRegistrationData(String username, String password, String firstName, String lastName, String email) {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();

        String insertQuery = "INSERT INTO user (userName, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email); // Note: Passwords should be hashed

            preparedStatement.executeUpdate();
            return true; // Registration successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

}
