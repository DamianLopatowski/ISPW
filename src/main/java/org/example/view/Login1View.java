package org.example.view;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class Login1View {
    private final VBox root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button avantiButton;
    private final Button loginButton;
    private final Label statusLabel;

    public Login1View() {
        root = new VBox(15);
        statusLabel = new Label("Inserisci dati:");
        usernameField = new TextField();
        avantiButton = new Button("Avanti");
        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci la password");
        loginButton = new Button("Login");

        passwordField.setVisible(false);
        loginButton.setVisible(false);

        root.getChildren().addAll(statusLabel, usernameField, avantiButton, passwordField, loginButton);
    }

    public VBox getRoot() {
        return root;
    }

    public Button getAvantiButton() {
        return avantiButton;
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
