package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistratiCliente1View {
    private final VBox root;
    private final TextField usernameField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Stage stage;  // ✅ Aggiunto lo Stage

    public RegistratiCliente1View(Stage stage) {  // ✅ Modificato costruttore
        this.stage = stage;

        root = new VBox(15);
        statusLabel = new Label("Registrazione Cliente Offline");
        usernameField = new TextField();
        usernameField.setPromptText("Inserisci username");
        nomeField = new TextField();
        nomeField.setPromptText("Inserisci nome");
        cognomeField = new TextField();
        cognomeField.setPromptText("Inserisci cognome");
        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci password");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Conferma password");

        registratiButton = new Button("Registrati");
        registratiButton.setDisable(true);

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean fieldsFilled = !usernameField.getText().trim().isEmpty()
                    && !nomeField.getText().trim().isEmpty()
                    && !cognomeField.getText().trim().isEmpty()
                    && !passwordField.getText().trim().isEmpty();
            registratiButton.setDisable(!fieldsFilled || !passwordField.getText().equals(newVal));
        });

        root.getChildren().addAll(statusLabel, usernameField, nomeField, cognomeField, passwordField, confirmPasswordField, registratiButton);
    }

    public VBox getRoot() { return root; }
    public TextField getUsernameField() { return usernameField; }
    public TextField getNomeField() { return nomeField; }
    public TextField getCognomeField() { return cognomeField; }
    public PasswordField getPasswordField() { return passwordField; }
    public PasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public Button getRegistratiButton() { return registratiButton; }
}
