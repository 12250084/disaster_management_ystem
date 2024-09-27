package com.example.project2;

import com.example.project2.AssessDisasterController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AssessDisasterControllerTest extends ApplicationTest {

    private AssessDisasterController controller;

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the controller and load FXML
        // Assumes the FXML file is loaded correctly for this controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisaster.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @Test
    public void testSuccessfulSaveWithValidInputs() {
        // Setting up valid inputs
        clickOn("#disasterTypeComboBox").clickOn("Flood");
        clickOn("#locationComboBox").clickOn("Chicago");
        clickOn("#priorityComboBox").clickOn("High");
        clickOn("#departmentComboBox").clickOn("Fire Department");
        clickOn("#descriptionTextArea").write("This is a sample disaster report.");
        clickOn("#savePriorityButton");

        // Verify success alert
        Alert alert = getAlert();
        assertNotNull(alert);
        assertEquals(Alert.AlertType.INFORMATION, alert.getAlertType());
        assertEquals("Success", alert.getTitle());
        assertEquals("Disaster report saved successfully.", alert.getContentText());
    }

    @Test
    public void testFailedSaveWithMissingInputs() {
        // Setting up inputs with missing disaster type
        clickOn("#locationComboBox").clickOn("City A");
        clickOn("#priorityComboBox").clickOn("High");
        clickOn("#departmentComboBox").clickOn("Health");
        clickOn("#descriptionTextArea").write("This is a sample disaster report.");
        clickOn("#savePriorityButton");

        // Verify validation alert
        Alert alert = getAlert();
        assertNotNull(alert);
        assertEquals(Alert.AlertType.WARNING, alert.getAlertType());
        assertEquals("Validation Error", alert.getTitle());
        assertEquals("Please fill in all required fields.", alert.getContentText());
    }


    private Alert getAlert() {
        return null;
    }
}
