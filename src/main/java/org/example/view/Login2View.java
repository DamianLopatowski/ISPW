package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class Login2View {
    private final GridPane root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Label statusLabel;
    public Login2View() {
        root = new GridPane();
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");
        statusLabel = new Label("Accesso - Inserisci credenziali:");

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