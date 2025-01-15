package org.example.controller;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.example.view.LoginPersonalView;
import org.example.view.View;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Logger;

public class LoginPersonalController {

    private static final Logger LOGGER = Logger.getLogger(LoginPersonalController.class.getName());

    private final LoginPersonalView view;
    private final boolean isOfflineMode;

    // Credenziali hardcoded per offline
    private static final String OFFLINE_USERNAME = "admin";
    private static final String OFFLINE_PASSWORD = "password";

    private String dbUrl;
    private String dbUsername;
    private String dbPassword;

    public LoginPersonalController(LoginPersonalView view, boolean isOfflineMode) {
        this.view = view;
        this.isOfflineMode = isOfflineMode;

        loadDatabaseConfig();
        setupHandlers();
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
        } catch (IOException e) {
            LOGGER.severe("Errore nel caricamento del file config.properties: " + e.getMessage());
            Platform.runLater(() -> view.getStatusLabel().setText("Errore di configurazione."));
        }
    }

    private void setupHandlers() {
        view.getLoginButton().setOnAction(event -> {
            String username = view.getUsernameField().getText();
            String password = view.getPasswordField().getText();

            if (isOfflineMode) {
                handleOfflineLogin(username, password);
            } else {
                handleOnlineLogin(username, password);
            }
        });
    }

    private void handleOfflineLogin(String username, String password) {
        if (username.equals(OFFLINE_USERNAME) && password.equals(OFFLINE_PASSWORD)) {
            view.getStatusLabel().setText("Accesso offline riuscito!");
            LOGGER.info("Accesso offline eseguito con successo.");
            openGestioneView();
        } else {
            view.getStatusLabel().setText("Credenziali offline errate.");
            LOGGER.warning("Credenziali offline non valide.");
        }
    }

    private void handleOnlineLogin(String username, String password) {
        String query = "SELECT username, password FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Platform.runLater(() -> {
                        view.getStatusLabel().setText("Accesso online riuscito!");
                        LOGGER.info("Accesso online eseguito con successo.");
                        openGestioneView();
                    });
                } else {
                    Platform.runLater(() -> view.getStatusLabel().setText("Credenziali online errate."));
                    LOGGER.warning("Credenziali online non valide.");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Errore durante la connessione al database: " + e.getMessage());
            Platform.runLater(() -> view.getStatusLabel().setText("Errore di connessione al database."));
        }
    }

    private void openGestioneView() {
        LOGGER.info("Navigazione verso la pagina di gestione.");
        Stage stage = (Stage) view.getRoot().getScene().getWindow();
        View mainView = new View();
        GestioneController gestioneController = new GestioneController(stage, mainView);
        stage.getScene().setRoot(gestioneController.getGestioneView().getRoot());
    }
}
