package org.example.controllerapplicativo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private String offlineUsername;
    private String offlinePassword;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public AuthController() {  // Rimosso il parametro context
        loadCredentials();
    }

    private void loadCredentials() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            offlineUsername = properties.getProperty("username");
            offlinePassword = properties.getProperty("password");

            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");

        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento del file config.properties: " + e.getMessage());
        }
    }

    public boolean handleOfflineLogin(String username, String password) {
        return username.equals(offlineUsername) && password.equals(offlinePassword);
    }

    public boolean handleOnlineLogin(String username, String password) {
        String query = "SELECT username FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            LOGGER.severe("Errore nella connessione al database: " + e.getMessage());
            return false;
        }
    }
}
