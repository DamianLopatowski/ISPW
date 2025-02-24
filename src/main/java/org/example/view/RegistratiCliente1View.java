package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;

public class RegistratiCliente1View {
    private final ScrollPane scrollPane;
    private final VBox root;
    private final TextField usernameField;
    private final Label usernameFeedback;
    private final TextField emailField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button registratiButton;
    private final Label statusLabel;

    public RegistratiCliente1View() {
        root = new VBox(10);
        statusLabel = new Label("Registrazione Cliente Offline");

        usernameField = new TextField();
        usernameField.setPromptText("Inserisci username");

        usernameFeedback = new Label();
        usernameFeedback.setStyle("-fx-text-fill: red;");

        emailField = new TextField();
        emailField.setPromptText("Inserisci email");

        nomeField = new TextField();
        nomeField.setPromptText("Inserisci nome");

        cognomeField = new TextField();
        cognomeField.setPromptText("Inserisci cognome");

        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Conferma password");

        codiceUnivocoField = new TextField();
        codiceUnivocoField.setPromptText("Inserisci codice univoco");

        registratiButton = new Button("Registrati");
        registratiButton.setDisable(true);

        root.getChildren().addAll(
                statusLabel, usernameField, usernameFeedback, emailField, nomeField, cognomeField,
                passwordField, confirmPasswordField, codiceUnivocoField, registratiButton
        );

        scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
    }

    public ScrollPane getRoot() {
        return scrollPane;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public Label getUsernameFeedback() {
        return usernameFeedback;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getNomeField() {
        return nomeField;
    }

    public TextField getCognomeField() {
        return cognomeField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getCodiceUnivocoField() {
        return codiceUnivocoField;
    }

    public Button getRegistratiButton() {
        return registratiButton;
    }

    public void setRegistratiButtonEnabled(boolean enabled) {
        registratiButton.setDisable(!enabled);
    }
}
