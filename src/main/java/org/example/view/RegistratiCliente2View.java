package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegistratiCliente2View {
    private final VBox root;
    private final TextField usernameField;
    private final Label usernameFeedback;
    private final TextField emailField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final TextField partitaIvaField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button avantiButton;
    private final Button indietroButton;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Label partitaIvaFeedback;
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


    private int step = 1;

    public RegistratiCliente2View() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        statusLabel = new Label("Registrazione Cliente - Passo 1");

        usernameFeedback = new Label();
        usernameFeedback.setStyle(TEXT_RED);
        usernameFeedback.setVisible(false);
        usernameFeedback.setManaged(false); // Rimuove lo spazio quando non è visibile

        usernameField = new TextField();

        emailField = new TextField();
        emailFeedback = new Label();
        emailFeedback.setStyle(TEXT_RED);
        emailFeedback.setVisible(false);
        emailFeedback.setManaged(false); // Rimuove lo spazio quando non è visibile

        nomeField = new TextField();
        cognomeField = new TextField();

        partitaIvaField = new TextField();
        partitaIvaFeedback = new Label();
        partitaIvaFeedback.setStyle(TEXT_RED);
        partitaIvaFeedback.setVisible(false);
        partitaIvaFeedback.setManaged(false);

        indirizzoField = new TextField();
        indirizzoFeedback = new Label();
        indirizzoFeedback.setStyle(TEXT_RED);
        indirizzoFeedback.setVisible(false);
        indirizzoFeedback.setManaged(false);

        civicoField = new TextField();

        capField = new TextField();
        capFeedback = new Label();
        capFeedback.setStyle(TEXT_RED);
        capFeedback.setVisible(false);
        capFeedback.setManaged(false);

        cittaField = new TextField();

        passwordField = new PasswordField();
        passwordFeedback = new Label();
        passwordFeedback.setStyle(TEXT_RED);
        passwordFeedback.setVisible(false);
        passwordFeedback.setManaged(false);

        confirmPasswordField = new PasswordField();
        confirmPasswordFeedback = new Label();
        confirmPasswordFeedback.setStyle(TEXT_RED);
        confirmPasswordFeedback.setVisible(false);
        confirmPasswordFeedback.setManaged(false);

        codiceUnivocoField = new TextField();

        avantiButton = new Button("Avanti");
        indietroButton = new Button("Indietro");
        registratiButton = new Button("Registrati");

        registratiButton.setDisable(true);

        avantiButton.setOnAction(e -> mostraStep(step + 1));
        indietroButton.setOnAction(e -> mostraStep(step - 1));

        mostraStep(1);
    }

    private void mostraStep(int newStep) {
        if (newStep < 1 || newStep > 3) return;

        root.getChildren().clear();
        step = newStep;

        indietroButton.setDisable(step == 1);
        avantiButton.setDisable(step == 3);

        switch (step) {
            case 1:
                statusLabel.setText("Passo 1: Inserisci il tuo Username e l'email");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Username:", usernameField),
                        usernameFeedback,
                        creaInput("Email:", emailField),
                        emailFeedback,
                        creaNavigazione());
                break;
            case 2:
                statusLabel.setText("Passo 2: Inserisci Nome e Cognome");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Nome:", nomeField),
                        creaInput("Cognome:", cognomeField),
                        creaInput("Partita Iva:", partitaIvaField),
                        partitaIvaFeedback,
                        creaNavigazione()
                );
                break;
            case 3:
                statusLabel.setText("Passo 3: Inserisci Indirizzo");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Indirizzo:", indirizzoField),
                        indirizzoFeedback,
                        creaInput("Civico:", civicoField),
                        creaInput("CAP:", capField),
                        capFeedback,
                        creaInput("Città:", cittaField),
                        creaNavigazione()
                );
                break;

            case 4:
                statusLabel.setText("Passo 3: Scegli una Password e inserisci il codice univoco");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Password:", passwordField),
                        passwordFeedback,
                        creaInput("Conferma Password:", confirmPasswordField),
                        confirmPasswordFeedback,
                        creaInput("Codice Univoco:", codiceUnivocoField),
                        creaNavigazioneFinale()
                );
                break;
            default:
                statusLabel.setText("⚠ Errore: Passo non valido!");
                root.getChildren().add(statusLabel);
                break;
        }
    }

    private HBox creaInput(String labelText, TextField field) {
        Label label = new Label(labelText);
        HBox box = new HBox(10, label, field);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox creaNavigazione() {
        HBox navBox = new HBox(10, indietroButton, avantiButton);
        navBox.setAlignment(Pos.CENTER);
        return navBox;
    }

    private HBox creaNavigazioneFinale() {
        registratiButton.setDisable(false);
        HBox navBox = new HBox(10, indietroButton, registratiButton);
        navBox.setAlignment(Pos.CENTER);
        return navBox;
    }

    public VBox getRoot() {
        return root;
    }
    public TextField getUsernameField() {
        return usernameField;
    }
    public Label getUsernameFeedback() {
        return usernameFeedback;
    }
    public TextField getPartitaIvaField() {
        return partitaIvaField;
    }
    public Label getPartitaIvaFeedback() {
        return partitaIvaFeedback;
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

    public TextField getCodiceUnivocoField() {
        return codiceUnivocoField;
    }
    public Button getRegistratiButton() {
        return registratiButton;
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

}
