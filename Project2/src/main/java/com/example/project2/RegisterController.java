package com.example.project2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

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

    @FXML
    private AnchorPane headerPane;

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
    }

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

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Call method to save registration data to the database
        if (saveRegistrationData(username, hashedPassword, firstName, lastName, email)) {
            registrationMessageLabel.setText("Registration successful!");
            dashboard();
        } else {
            registrationMessageLabel.setText("Registration failed. Try again.");
        }
    }

    private boolean saveRegistrationData(String username, String hashedPassword, String firstName, String lastName, String email) {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();

        String insertQuery = "INSERT INTO user (userName, password, firstName, lastName, email) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);  // Store hashed password
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);

            preparedStatement.executeUpdate();
            return true; // Registration successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    private void dashboard() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 829, 695));
            stage.show();
            //setCancelButton();
            // Close the current window
            Stage currentStage = (Stage) registerButton.getScene().getWindow(); // Assuming registerButton was used in the current window
            currentStage.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onloginButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelButton() {
        onloginButtonClick();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
