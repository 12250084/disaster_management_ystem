package com.example.project2;

import java.sql.*;

public class DatabaseConnection {
    private final String schemaName = "dbo8";  // Schema name
    public Connection databaseLink;

    public Connection getConnection() {


        String databaseUser = "root";
        String databasePassword = "root";
        String url = "jdbc:mysql://localhost/";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // First, connect to MySQL server to create the schema if it doesn't exist
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

            // Create the schema if it doesn't exist
            Statement statement = databaseLink.createStatement();
            String createSchemaQuery = "CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`";
            statement.executeUpdate(createSchemaQuery);

            // Switch to the newly created schema
            url = "jdbc:mysql://localhost/" + schemaName; // Update URL to use schema
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

            // Create tables within the schema if they don't exist
            createTablesIfNotExist(databaseLink);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }

    // Getter method to retrieve the schema name
    public String getSchemaName() {
        return schemaName;
    }
    private void createTablesIfNotExist(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Step 1: Create 'disaster_types' table first, as it is referenced by other tables
        String createDisasterTypesTable = "CREATE TABLE IF NOT EXISTS `disaster_types` ("
                + "`id` int NOT NULL AUTO_INCREMENT, "
                + "`type_name` varchar(255) NOT NULL, "
                + "PRIMARY KEY (`id`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createDisasterTypesTable);

        // Step 2: Create 'locations' table
        String createLocationsTable = "CREATE TABLE IF NOT EXISTS `locations` ("
                + "`id` int NOT NULL AUTO_INCREMENT, "
                + "`location_name` varchar(255) NOT NULL, "
                + "PRIMARY KEY (`id`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createLocationsTable);

        // Step 3: Create 'priorities' table
        String createPrioritiesTable = "CREATE TABLE IF NOT EXISTS `priorities` ("
                + "`id` int NOT NULL AUTO_INCREMENT, "
                + "`priority_level` varchar(10) NOT NULL, "
                + "PRIMARY KEY (`id`), "
                + "UNIQUE KEY `priority_level` (`priority_level`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createPrioritiesTable);

        // Step 4: Create 'user' table
        String createUserTable = "CREATE TABLE IF NOT EXISTS `user` ("
                + "`userId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT, "
                + "`userName` varchar(45) DEFAULT NULL, "
                + "`password` varchar(500) DEFAULT NULL, "
                + "`firstName` varchar(45) DEFAULT NULL, "
                + "`lastName` varchar(45) DEFAULT NULL, "
                + "`email` varchar(45) DEFAULT NULL, "
                + "PRIMARY KEY (`userId`), "
                + "UNIQUE KEY `userId_UNIQUE` (`userId`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createUserTable);

        // Step 5: Create 'departments' table (this references 'disaster_types')
        String createDepartmentsTable = "CREATE TABLE IF NOT EXISTS `departments` ("
                + "`DepartmentID` int NOT NULL AUTO_INCREMENT, "
                + "`DepartmentName` varchar(255) NOT NULL, "
                + "`DisasterTypeID` int DEFAULT NULL, "
                + "PRIMARY KEY (`DepartmentID`), "
                + "KEY `DisasterTypeID` (`DisasterTypeID`), "
                + "CONSTRAINT `departments_ibfk_1` FOREIGN KEY (`DisasterTypeID`) REFERENCES `disaster_types` (`id`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createDepartmentsTable);

        // Step 6: Create 'disaster_reports' table (this references 'disaster_types' and 'locations')
        String createDisasterReportsTable = "CREATE TABLE IF NOT EXISTS `disaster_reports` ("
                + "`id` int NOT NULL AUTO_INCREMENT, "
                + "`disaster_type_id` int DEFAULT NULL, "
                + "`location_id` int DEFAULT NULL, "
                + "`description` text, "
                + "`latitude` double DEFAULT NULL, "
                + "`longitude` double DEFAULT NULL, "
                + "PRIMARY KEY (`id`), "
                + "KEY `disaster_type_id` (`disaster_type_id`), "
                + "KEY `location_id` (`location_id`), "
                + "CONSTRAINT `disaster_reports_ibfk_1` FOREIGN KEY (`disaster_type_id`) REFERENCES `disaster_types` (`id`), "
                + "CONSTRAINT `disaster_reports_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createDisasterReportsTable);

        // Step 7: Create 'disasters' table (this references 'disaster_types', 'locations', 'priorities', and 'departments')
        String createDisastersTable = "CREATE TABLE IF NOT EXISTS `disasters` ("
                + "`id` int NOT NULL AUTO_INCREMENT, "
                + "`disaster_type_id` int DEFAULT NULL, "
                + "`location_id` int DEFAULT NULL, "
                + "`description` text NOT NULL, "
                + "`priority_id` int DEFAULT NULL, "
                + "`department_id` int DEFAULT NULL, "
                + "`created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (`id`), "
                + "KEY `disaster_type_id` (`disaster_type_id`), "
                + "KEY `location_id` (`location_id`), "
                + "KEY `priority_id` (`priority_id`), "
                + "KEY `department_id` (`department_id`), "
                + "CONSTRAINT `disasters_ibfk_1` FOREIGN KEY (`disaster_type_id`) REFERENCES `disaster_types` (`id`), "
                + "CONSTRAINT `disasters_ibfk_2` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`), "
                + "CONSTRAINT `disasters_ibfk_3` FOREIGN KEY (`priority_id`) REFERENCES `priorities` (`id`), "
                + "CONSTRAINT `disasters_ibfk_4` FOREIGN KEY (`department_id`) REFERENCES `departments` (`DepartmentID`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
        statement.executeUpdate(createDisastersTable);

        // Insert data into 'disaster_types' table first
        insertDataIfEmpty(connection, "disaster_types", "INSERT INTO `disaster_types` VALUES "
                + "(1,'Fire'),(2,'Flood'),(3,'Earthquake'),(4,'Hurricane'),(5,'Tornado'),"
                + "(6,'Landslide'),(7,'Volcano'),(8,'Drought'),(9,'Tsunami'),(10,'Avalanche');");

        // Insert data into 'departments' table after 'disaster_types' table has data
        insertDataIfEmpty(connection, "departments", "INSERT INTO `departments` VALUES "
                + "(1,'Fire Department',1),(2,'Flood Response Unit',2),(3,'Seismic Activity Department',3),"
                + "(4,'Hurricane Response Team',4),(5,'Tornado Response Unit',5),"
                + "(6,'Geological Department',6),(7,'Volcanic Activity Response Team',7),"
                + "(8,'Drought Management Department',8),(9,'Tsunami Response Team',9),"
                + "(10,'Avalanche Response Unit',10);");

        // Insert data into 'locations' table if empty
        insertDataIfEmpty(connection, "locations", "INSERT INTO `locations` VALUES "
                + "(1,'New York City'),(2,'Los Angeles'),(3,'Chicago'),(4,'Houston'),"
                + "(5,'Phoenix'),(6,'Philadelphia'),(7,'San Antonio'),(8,'San Diego'),"
                + "(9,'Dallas'),(10,'San Jose');");

        // Insert data into 'priorities' table if empty
        insertDataIfEmpty(connection, "priorities", "INSERT INTO `priorities` VALUES "
                + "(1,'High'),(3,'Low'),(2,'Medium');");

        // Optionally insert data into 'user' table if empty
        insertDataIfEmpty(connection, "user", "INSERT INTO `user` (userId, userName, password, firstName, lastName, email) VALUES "
                + "(0000000001,'admin','$2a$10$BTmmw5DEzlKyeWuBfKqoEujcDuYeLSfWh5qqj/JkH2At0AbYUN2ki',NULL,NULL,NULL),"
                + "(0000000002,'john','john123','John','Doe','john.doe@example.com');");

        statement.close();
    }

    private void insertDataIfEmpty(Connection connection, String tableName, String insertQuery) throws SQLException {
        // Check if the table is empty
        String checkIfEmpty = "SELECT COUNT(*) AS rowcount FROM `" + tableName + "`;";
        try (Statement checkStatement = connection.createStatement();
             ResultSet resultSet = checkStatement.executeQuery(checkIfEmpty)) {

            resultSet.next();
            int count = resultSet.getInt("rowcount");

            // If the table is empty, insert the initial data
            if (count == 0) {
                try (Statement insertStatement = connection.createStatement()) {
                    insertStatement.executeUpdate(insertQuery);
                }
            }
        }
    }



}
