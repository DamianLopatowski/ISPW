package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class RegistratiCliente1View {
    private final ScrollPane scrollPane;
    private final VBox root;
    private final TextField usernameField;
    private final TextField emailField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Stage stage;

    public RegistratiCliente1View(Stage stage) {
        this.stage = stage;

        root = new VBox(15);
        statusLabel = new Label("Registrazione Cliente Offline");

        usernameField = new TextField();
        usernameField.setPromptText("Inserisci username");

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

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean fieldsFilled = !usernameField.getText().trim().isEmpty()
                    && !nomeField.getText().trim().isEmpty()
                    && !cognomeField.getText().trim().isEmpty()
                    && !passwordField.getText().trim().isEmpty();
            registratiButton.setDisable(!fieldsFilled || !passwordField.getText().equals(newVal));
        });

        // Aggiungiamo tutti i campi alla VBox
        root.getChildren().addAll(statusLabel, usernameField, emailField, nomeField, cognomeField, passwordField, confirmPasswordField, codiceUnivocoField, registratiButton);

        // Creiamo lo ScrollPane e aggiungiamo la VBox al suo interno
        scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // Permette il ridimensionamento automatico in larghezza
        scrollPane.setFitToHeight(false); // Permette lo scrolling verticale
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    }

    public ScrollPane getRoot() {
        return scrollPane;
    }

    public TextField getUsernameField() {
        return usernameField;
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
}
