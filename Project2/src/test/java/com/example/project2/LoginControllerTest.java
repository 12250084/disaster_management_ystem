package com.example.project2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

//@ExtendWith(FxToolkit.class)
public class LoginControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        // Set the scene and stage
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Any additional setup before each test
    }

    @Test
    public void testLoginWithValidCredentials() {
        // Simulate user input
        clickOn("#userNameTextField").write("admin");
        clickOn("#passwordTextField").write("admin123");
        clickOn("#connectionButton");

        // Verify login success
        verifyThat("#loginMessageLabel", hasText("Login successful!"));
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Simulate user input
        clickOn("#userNameTextField").write("wronguser");
        clickOn("#passwordTextField").write("wrongpassword");
        clickOn("#connectionButton");

        // Verify login failure
        verifyThat("#loginMessageLabel", hasText("Invalid username or password."));
    }

    @Test
    public void testCancelButtonClosesWindow() {
        // Simulate clicking the cancel button
        clickOn("#cancelButton");

        // Verify the window is closed
        // Additional implementation may be needed here
    }

    @Test
    public void testRegisterButtonOpensRegistrationForm() {
        // Simulate clicking the register button
        clickOn("#regisButton");

        // Verify the registration form opens
        // Additional implementation may be needed here
    }
}
