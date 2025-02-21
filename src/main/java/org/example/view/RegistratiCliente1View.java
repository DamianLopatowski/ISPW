package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import org.example.controllergrafici.RegistratiClienteController;

public class RegistratiCliente1View {
    private final ScrollPane scrollPane;
    private final VBox root;
    private final TextField usernameField;
    private Label usernameFeedback; // Non lo aggiungiamo inizialmente
    private final TextField emailField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Stage stage;

    public RegistratiCliente1View(Stage stage, RegistratiClienteController controller) {
        this.stage = stage;
        root = new VBox(10);
        statusLabel = new Label("Registrazione Cliente Offline");

        usernameField = new TextField();
        usernameField.setPromptText("Inserisci username");

        // Label di feedback (inizialmente invisibile)
        usernameFeedback = new Label();
        usernameFeedback.setTextFill(Color.RED);

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

        // Validazione dello username usando il Controller (MVC)
        controller.validaUsername(usernameField, usernameFeedback, root);

        // Aggiungere gli elementi alla root
        root.getChildren().addAll(
                statusLabel, usernameField, emailField, nomeField, cognomeField,
                passwordField, confirmPasswordField, codiceUnivocoField, registratiButton
        );

        // Creazione dello ScrollPane
        scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
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
