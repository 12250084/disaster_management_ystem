package com.example.project2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    Button backButton;

    @FXML
    public void handleBackButton(ActionEvent event) {
        // Close the About window
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
}
