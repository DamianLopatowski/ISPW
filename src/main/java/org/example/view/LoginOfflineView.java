package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LoginOfflineView {
    private final VBox root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button avantiButton;
    private final Button loginButton;
    private final Label statusLabel;
    private static final Logger LOGGER = Logger.getLogger(LoginOfflineView.class.getName());


    public LoginOfflineView(boolean isInterfaccia1) {
        root = new VBox(15);
        statusLabel = new Label("Inserisci l'admin:");
        usernameField = new TextField();
        avantiButton = new Button("Avanti");

        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci la password");
        loginButton = new Button("Login Offline");

        LOGGER.info("✅ Bottone di login OFFLINE creato.");

        passwordField.setVisible(false);
        loginButton.setVisible(false);

        avantiButton.setOnAction(event -> showPasswordField());

        root.getChildren().addAll(statusLabel, usernameField, avantiButton, passwordField, loginButton);
    }

    private void showPasswordField() {
        if (!usernameField.getText().trim().isEmpty()) {
            statusLabel.setText("Ora inserisci la password:");
            avantiButton.setDisable(true);
            usernameField.setDisable(true);
            passwordField.setVisible(true);
            loginButton.setVisible(true);
        } else {
            statusLabel.setText("⚠️ Inserisci un nome utente prima di proseguire!");
        }
    }

    public VBox getRoot() {
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

    public Button getAvantiButton() {
        return avantiButton;
    }

    public void enableLogin() {
        avantiButton.setDisable(false);
        usernameField.setDisable(false);
        passwordField.setVisible(true);
        loginButton.setVisible(true);
    }

}
