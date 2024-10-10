package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AboutController {

    public AnchorPane headerPane;
    @FXML
    Button backButton;

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
    }

    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent aboutRoot = loader.load();
        Stage aboutStage = new Stage();
        aboutStage.initStyle(StageStyle.UNDECORATED);
        // aboutStage.setTitle("About");
        aboutStage.setScene(new Scene(aboutRoot));
        aboutStage.show();

        // Close the About window
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
