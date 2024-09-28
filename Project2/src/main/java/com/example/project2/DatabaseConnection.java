package com.example.project2;

import java.sql.*;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {

        String schemaName = "dbo";  // Schema name
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

            // Now connect to the created schema (database)
            url = "jdbc:mysql://localhost/" + schemaName;
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

            // Create tables within the schema if they don't exist
           createTablesIfNotExist(databaseLink);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return databaseLink;
    }

    private void createTablesIfNotExist(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // Create the 'user' table if it doesn't exist
        String createUserTable = "CREATE TABLE IF NOT EXISTS `user` ("
                + "`userId` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT, "
                + "`userName` varchar(45) DEFAULT NULL, "
                + "`password` varchar(45) DEFAULT NULL, "
                + "`firstName` varchar(45) DEFAULT NULL, "
                + "`lastName` varchar(45) DEFAULT NULL, "
                + "`email` varchar(45) DEFAULT NULL, "
                + "PRIMARY KEY (`userId`), "
                + "UNIQUE KEY `userId_UNIQUE` (`userId`) "
                + ") ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        // Execute the creation of the 'user' table
        statement.executeUpdate(createUserTable);

        // Check if the 'user' table is empty
        String checkIfEmpty = "SELECT COUNT(*) AS rowcount FROM `user`;";
        try (Statement checkStatement = connection.createStatement();
             ResultSet resultSet = checkStatement.executeQuery(checkIfEmpty)) {

            resultSet.next();
            int count = resultSet.getInt("rowcount");

            // If the table is empty, insert the initial data
            if (count == 0) {
                String insertDataQuery = "INSERT INTO `user` (userId, userName, password, firstName, lastName, email) VALUES "
                        + "(0000000001,'admin','admin123',NULL,NULL,NULL),"
                        + "(0000000002,'fasfasff','123','adfadf','fasfasf','fasfas'),"
                        + "(0000000003,'fasfasf','12345','fadfasf','fasfasff','fasfasf');";

                // Insert the data if the table is empty
                statement.executeUpdate(insertDataQuery);
            }
        }

        statement.close();
    }


}
