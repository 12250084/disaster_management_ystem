package com.example.project2;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HomeControllerTest extends ApplicationTest {

    private HomeController homeController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();
        homeController = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Test
    public void testLoginButton() {
        clickOn("#loginButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the Login button click behaves as expected
    }

    @Test
    public void testReportDisasterButton() {
        clickOn("#reportDisasterButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the Report Disaster button click behaves as expected
    }

    @Test
    public void testAssessDisasterButton() {
        clickOn("#accessDisasterButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the Assess Disaster button click behaves as expected
    }

    @Test
    public void testAssessDisasterViewButton() {
        clickOn("#assessDisasterViewButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the Assess Disaster View button click behaves as expected
    }

    @Test
    public void testAboutButton() {
        clickOn("#aboutButton");
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the About button click behaves as expected
    }

    @Test
    public void testHandleCancel() {
        clickOn("#loginButton"); // assuming the login button opens the window to be closed
        WaitForAsyncUtils.waitForFxEvents();
        clickOn("#cancelButton"); // assuming there is a cancel button that triggers handleCancel
        WaitForAsyncUtils.waitForFxEvents();
        // Add assertions to verify that the window is closed as expected
    }

}
