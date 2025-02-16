package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginOfflineView {
    private final VBox root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button avantiButton;
    private final Button loginButton;
    private final Label statusLabel;

    private boolean isEnteringPassword = false;

    public LoginOfflineView() {
        root = new VBox(15);
        usernameField = new TextField();
        passwordField = new PasswordField();
        avantiButton = new Button("Avanti");
        loginButton = new Button("Login Offline");
        statusLabel = new Label("Accesso Offline - Username:");

        root.getChildren().addAll(statusLabel, usernameField, avantiButton);

        avantiButton.setOnAction(event -> showPasswordField());
        loginButton.setOnAction(event -> statusLabel.setText("Verifica credenziali..."));
    }

    private void showPasswordField() {
        if (!isEnteringPassword) {
            isEnteringPassword = true;
            root.getChildren().clear();
            root.getChildren().addAll(statusLabel, passwordField, loginButton);
            statusLabel.setText("Accesso Offline - Password:");
        }
    }

    public VBox getRoot() {
        return root;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getAvantiButton() {
        return avantiButton;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }
}
