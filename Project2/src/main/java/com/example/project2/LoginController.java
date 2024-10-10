package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;



public class LoginController {

    public Button registerButton;
    @FXML
    private Button cancelButton;
    @FXML
    private  Button regisButton;

    @FXML
    private Label lblLogin;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private AnchorPane headerPane; // FXML ID for the top blue AnchorPane
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


    public  void setCancelButton (){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    public  void OnCancelButton (){
        //dashboard();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();


    }



    public void connectionButton(ActionEvent event) {
        if(!userNameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()){
           validationLogin();

        }else {
            loginMessageLabel.setText("Please enter username and password");
        }

    }

    private void validationLogin() {
        // Instantiate DatabaseConnection
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();

        // Retrieve schema name dynamically
        String schemaName = connectionNow.getSchemaName(); // Get schema name

        // Use the schema name in the SQL query
        String verifyLogin = "SELECT password FROM `" + schemaName + "`.user WHERE username = ?";

        try {
            // Prepare SQL query with the schema and placeholders
            PreparedStatement preparedStatement = connection.prepareStatement(verifyLogin);
            preparedStatement.setString(1, userNameTextField.getText().trim());

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Get the stored hashed password from the database
                String storedHashedPassword = resultSet.getString("password");

                // Use BCrypt to check if the entered password matches the stored hash
                if (BCrypt.checkpw(passwordTextField.getText().trim(), storedHashedPassword)) {
                    loginMessageLabel.setText("Login successful!");
                    dashboard();  // Navigate to the dashboard on successful login
                } else {
                    loginMessageLabel.setText("Invalid username or password.");
                }
            } else {
                loginMessageLabel.setText("Invalid username or password.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    // Method to enable buttons after successful login
//    public void enableButtonsAfterLogin() {
//        dashboardButton.setDisable(false);
//        reportDisasterButton.setDisable(false);
//        accessDisasterButton.setDisable(false);
//        assessDisasterViewButton.setDisable(false);
//    }


    public void registerForm(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent loginRoot = loader.load();
            Stage loginStage = new Stage();
            //loginStage.setTitle("Login");
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(loginRoot));
            loginStage.show();
            setCancelButton();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public void dashboard(){

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root,829,695));
            stage.show();
            setCancelButton();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


//    public void connectionButton(ActionEvent event) {
//        DatabaseConnection connectNow= new DatabaseConnection();
//        Connection connectionDb = connectNow.getConnection();
//
//        String connectionQuery = "SELECT username FROM dbo.user";
//
//        try {
//
//            Statement statement = connectionDb.createStatement();
//            ResultSet queryOutput = statement.executeQuery(connectionQuery);
//
//            while (queryOutput.next()){
//                lblLogin.setText(queryOutput.getString("username"));
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }


}
