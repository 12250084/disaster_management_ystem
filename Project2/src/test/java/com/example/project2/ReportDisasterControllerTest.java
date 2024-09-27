package com.example.project2;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportDisasterControllerTest extends ApplicationTest {

    private ReportDisasterController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReportDisaster.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }



    @Test
    public void testInitialize() {
        assertNotNull(controller.disasterTypeComboBox);
        assertNotNull(controller.locationComboBox);
        assertNotNull(controller.locationTextField);
        assertNotNull(controller.descriptionTextArea);
        assertNotNull(controller.submitButton);
        assertNotNull(controller.currentLocationLink);
        assertTrue(controller.disasterTypeComboBox.getItems().isEmpty()); // Ensure items are loaded later
        assertTrue(controller.locationComboBox.getItems().isEmpty()); // Ensure items are loaded later
    }

    @Test
    public void testLoadDisasterTypes() {
        // Simulate loading disaster types from the database
        // You may need to mock DatabaseConnection and the related methods
        controller.loadDisasterTypes();
        assertTrue(controller.disasterTypeComboBox.getItems().size() > 0, "Disaster types should be loaded.");
    }

    @Test
    public void testLoadLocations() {
        // Simulate loading locations from the database
        // You may need to mock DatabaseConnection and the related methods
        controller.loadLocations();
        assertTrue(controller.locationComboBox.getItems().size() > 0, "Locations should be loaded.");
    }

    @Test
    public void testSuccessfulSaveWithValidInputs() {
        // Setting up valid inputs
        clickOn("#disasterTypeComboBox").clickOn("Flood");
        clickOn("#locationComboBox").clickOn("Chicago");
        clickOn("#locationTextField").write("Latitude: 34.0522, Longitude: -118.2437");
        clickOn("#descriptionTextArea").write("This is a sample disaster record.");
        clickOn("#submitButton");

        // Verify success alert
        Alert alert = getAlert();
        assertNotNull(alert);
        assertEquals(Alert.AlertType.INFORMATION, alert.getAlertType());
        assertEquals("Success", alert.getTitle());
        assertEquals("Disaster report saved successfully.", alert.getContentText());
    }

    private Alert getAlert() {
        return null;
    }


}
