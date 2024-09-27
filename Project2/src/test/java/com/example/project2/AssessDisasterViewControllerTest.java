package com.example.project2;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssessDisasterViewControllerTest extends ApplicationTest {

    private AssessDisasterViewController controller;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssessDisasterView.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Setup required before each test
        controller.dbConnection = Mockito.mock(DatabaseConnection.class);
    }

    @Test
    public void testUIInitialization() {
        assertNotNull(controller.disasterTableView);
        assertNotNull(controller.disasterNoColumn);
        assertNotNull(controller.disasterTypeColumn);
        assertNotNull(controller.locationColumn);
        assertNotNull(controller.DepartmentColumn);
    }

    @Test
    public void testLoadDisasterRecords() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(controller.dbConnection.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simulate one row
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("type_name")).thenReturn("Flood");
        when(mockResultSet.getString("location_name")).thenReturn("New York");
        when(mockResultSet.getString("DepartmentName")).thenReturn("Emergency");

        controller.loadDisasterRecords();

        assertEquals(1, controller.disasterRecords.size());
        assertEquals("001", controller.disasterRecords.get(0).getDisasterNo());
        assertEquals("Flood", controller.disasterRecords.get(0).getDisasterType());
    }

    @Test
    public void testHandleRowSelectWithDoubleClick() {
        DisasterRecord mockRecord = new DisasterRecord("001", "Flood", "New York", "Emergency");
        controller.disasterRecords.add(mockRecord);
        controller.disasterTableView.getSelectionModel().select(mockRecord);

        FxRobot robot = new FxRobot();
        robot.clickOn(controller.disasterTableView).clickOn(controller.disasterTableView); // Simulate double click

        WaitForAsyncUtils.waitForFxEvents();

        // Add assertions here depending on the exact behavior expected
    }


    @Test
    public void testShowEditWindowWithIOException() throws IOException {
        DisasterRecord mockRecord = new DisasterRecord("001", "Flood", "New York", "Emergency");
        controller.showEditWindow(mockRecord);

        // Mock FXMLLoader to throw IOException
        FXMLLoader mockLoader = mock(FXMLLoader.class);
        when(mockLoader.load()).thenThrow(IOException.class);

        // Verify if error alert is shown
        verify(controller, times(1)).showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the edit window.");
    }
}