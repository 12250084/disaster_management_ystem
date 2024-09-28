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

        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connection = connectionNow.getConnection();

        // Corrected SQL query with placeholders
        String verifyLogin = "SELECT COUNT(1) FROM dbo.user WHERE username = ? AND password = ?";

        try {
            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(verifyLogin);

            // Set parameters for the placeholders
            preparedStatement.setString(1, userNameTextField.getText().trim()); // First placeholder
            preparedStatement.setString(2, passwordTextField.getText().trim()); // Second placeholder

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    loginMessageLabel.setText("Login successful!");
                    //enableButtonsAfterLogin();
                    dashboard();

                } else {
                    loginMessageLabel.setText("Invalid username or password.");
                }
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
