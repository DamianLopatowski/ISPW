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
    private final TextField partitaIvaField;
    private final Label partitaIvaFeedback;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Label emailFeedback;
    private final Label passwordFeedback;
    private final Label confirmPasswordFeedback;
    private final TextField indirizzoField;
    private final Label indirizzoFeedback;
    private final TextField civicoField;
    private final TextField capField;
    private final Label capFeedback;
    private final TextField cittaField;
    private static final String TEXT_RED = "-fx-text-fill: red;";

    public RegistratiCliente1View() {
        root = new VBox(10);
        statusLabel = new Label("Registrazione Cliente");

        usernameField = new TextField();
        usernameField.setPromptText("Inserisci username");

        usernameFeedback = new Label();
        usernameFeedback.setStyle(TEXT_RED);
        usernameFeedback.setVisible(false);
        usernameFeedback.setManaged(false); // Rimuove lo spazio quando non è visibile

        emailField = new TextField();
        emailField.setPromptText("Inserisci email");

        emailFeedback = new Label();
        emailFeedback.setStyle(TEXT_RED);
        emailFeedback.setVisible(false);
        emailFeedback.setManaged(false); // Rimuove lo spazio quando non è visibile

        nomeField = new TextField();
        nomeField.setPromptText("Inserisci nome");

        cognomeField = new TextField();
        cognomeField.setPromptText("Inserisci cognome");

        partitaIvaField = new TextField();
        partitaIvaField.setPromptText("Inserisci Partita IVA");

        partitaIvaFeedback = new Label();
        partitaIvaFeedback.setStyle(TEXT_RED);
        partitaIvaFeedback.setVisible(false);
        partitaIvaFeedback.setManaged(false);

        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci password");

        passwordFeedback = new Label();
        passwordFeedback.setStyle(TEXT_RED);
        passwordFeedback.setVisible(false);
        passwordFeedback.setManaged(false);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Conferma password");

        confirmPasswordFeedback = new Label();
        confirmPasswordFeedback.setStyle(TEXT_RED);
        confirmPasswordFeedback.setVisible(false);
        confirmPasswordFeedback.setManaged(false);

        indirizzoField = new TextField();
        indirizzoField.setPromptText("Inserisci Indirizzo (Via/Piazza...)");

        indirizzoFeedback = new Label();
        indirizzoFeedback.setStyle(TEXT_RED);
        indirizzoFeedback.setVisible(false);
        indirizzoFeedback.setManaged(false);

        civicoField = new TextField();
        civicoField.setPromptText("Inserisci Numero Civico");

        capField = new TextField();
        capField.setPromptText("Inserisci CAP");

        capFeedback = new Label();
        capFeedback.setStyle(TEXT_RED);
        capFeedback.setVisible(false);
        capFeedback.setManaged(false);

        cittaField = new TextField();
        cittaField.setPromptText("Inserisci Città");

        codiceUnivocoField = new TextField();
        codiceUnivocoField.setPromptText("Inserisci codice univoco");

        registratiButton = new Button("Registrati");
        registratiButton.setDisable(true);

        root.getChildren().addAll(
                statusLabel, usernameField, usernameFeedback, emailField, emailFeedback, nomeField, cognomeField, partitaIvaField, partitaIvaFeedback,
                passwordField, passwordFeedback, confirmPasswordField, confirmPasswordFeedback, indirizzoField, indirizzoFeedback, civicoField, capField,
                capFeedback, cittaField, codiceUnivocoField, registratiButton
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

    public TextField getPartitaIvaField() {
        return partitaIvaField;
    }

    public Label getPartitaIvaFeedback() {
        return partitaIvaFeedback;
    }

    public Label getEmailFeedback() {
        return emailFeedback;
    }

    public Label getPasswordFeedback() {
        return passwordFeedback;
    }

    public Label getConfirmPasswordFeedback() {
        return confirmPasswordFeedback;
    }
    public TextField getIndirizzoField() {
        return indirizzoField;
    }
    public Label getIndirizzoFeedback() {
        return indirizzoFeedback;
    }
    public TextField getCivicoField() {
        return civicoField;
    }
    public TextField getCapField() {
        return capField;
    }
    public Label getCapFeedback() {
        return capFeedback;
    }
    public TextField getCittaField() {
        return cittaField;
    }
}
