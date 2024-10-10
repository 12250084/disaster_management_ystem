module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires jbcrypt;


    opens com.example.project2 to javafx.fxml;
    exports com.example.project2;
}