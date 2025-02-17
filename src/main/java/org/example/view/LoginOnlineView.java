package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.logging.Logger;


public class LoginOnlineView {
    private final GridPane root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Label statusLabel;
    private static final Logger LOGGER = Logger.getLogger(LoginOnlineView.class.getName());


    public LoginOnlineView() {
        root = new GridPane();
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login Online");
        statusLabel = new Label("Accesso Online - Inserisci credenziali:");

        root.add(new Label("Username:"), 0, 0);
        root.add(usernameField, 1, 0);
        root.add(new Label("Password:"), 0, 1);
        root.add(passwordField, 1, 1);
        root.add(loginButton, 1, 2);
        root.add(statusLabel, 1, 3);
    }

    public GridPane getRoot() {
        return root;
    }

    public Button getLoginButton() {
        LOGGER.info("📌 LoginButton richiesto.");
        if (loginButton == null) {
            LOGGER.warning("⚠️ LoginButton è NULL! Controlla l'inizializzazione.");
        }
        return loginButton;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
