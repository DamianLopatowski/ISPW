package org.example.controllerapplicativo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.example.ApplicationContext;

public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    private final ApplicationContext context;
    private String offlineUsername = "default_user";
    private String offlinePassword = "default_pass";

    public AuthController(ApplicationContext context) {
        this.context = context;
        loadOfflineCredentials();
    }

    private void loadOfflineCredentials() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            offlineUsername = properties.getProperty("username");
            offlinePassword = properties.getProperty("password");
        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento delle credenziali offline.");
        }
    }

    public boolean handleOfflineLogin(String username, String password) {
        return username.equals(offlineUsername) && password.equals(offlinePassword);
    }

    public boolean handleOnlineLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", "Kazik+10");
             PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            LOGGER.severe("Errore nella connessione al database.");
            return false;
        }
    }
}
