package com.example.project2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

public class AboutControllerTest extends ApplicationTest {

    private AboutController aboutController;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();

        aboutController = loader.getController();
    }

    @Test
    public void testHandleBackButton() {
        // Click the back button
        clickOn("#backButton");

        // Check that the stage is closed
        Stage stage = (Stage) aboutController.backButton.getScene().getWindow();
        assertFalse(stage.isShowing(), "The About window should be closed");
    }
}
