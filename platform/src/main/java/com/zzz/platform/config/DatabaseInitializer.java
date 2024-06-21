package com.zzz.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private final String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS `e-invoice` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci";
    private final String createUserSQL = "CREATE USER IF NOT EXISTS 'e_invoice_user'@'localhost' IDENTIFIED BY 'user_password'";
    private final String grantPrivilegesSQL = "GRANT ALL PRIVILEGES ON `e-invoice`.* TO 'e_invoice_user'@'localhost'";

    @PostConstruct
    public void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement statement = connection.createStatement()) {

            // Create database
            statement.executeUpdate(createDatabaseSQL);
            System.out.println("Database created successfully.");

            // Create user
            statement.executeUpdate(createUserSQL);
            System.out.println("User created successfully.");

            // Grant privileges
            statement.executeUpdate(grantPrivilegesSQL);
            System.out.println("Privileges granted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
