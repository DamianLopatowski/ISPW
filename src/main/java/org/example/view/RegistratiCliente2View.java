package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.controllergrafici.RegistratiClienteController;

public class RegistratiCliente2View {
    private final VBox root;
    private final TextField usernameField;
    private final Label usernameFeedback;
    private final TextField emailField;
    private final TextField nomeField;
    private final TextField cognomeField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final TextField codiceUnivocoField;
    private final Button avantiButton;
    private final Button indietroButton;
    private final Button registratiButton;
    private final Label statusLabel;
    private final Stage stage;

    private int step = 1;

    public RegistratiCliente2View(Stage stage, RegistratiClienteController controller) {
        this.stage = stage;
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        statusLabel = new Label("Registrazione Cliente Online - Passo 1");

        // Label per il feedback (inizialmente invisibile)
        usernameFeedback = new Label();
        usernameFeedback.setTextFill(Color.RED);

        usernameField = new TextField();
        emailField = new TextField();
        nomeField = new TextField();
        cognomeField = new TextField();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();
        codiceUnivocoField = new TextField();
        codiceUnivocoField.setPromptText("Inserisci codice univoco");

        avantiButton = new Button("Avanti");
        indietroButton = new Button("Indietro");
        registratiButton = new Button("Registrati");

        registratiButton.setDisable(true);

        // 👉 Chiamata alla validazione dello username tramite Controller (MVC)
        controller.aggiungiValidazioneUsername(usernameField, usernameFeedback, root);

        avantiButton.setOnAction(e -> mostraStep(step + 1));
        indietroButton.setOnAction(e -> mostraStep(step - 1));

        mostraStep(1);
    }

    private void mostraStep(int newStep) {
        if (newStep < 1 || newStep > 3) return; // Impedisce step non validi

        root.getChildren().clear();
        step = newStep;

        indietroButton.setDisable(step == 1); // Disabilita "Indietro" solo al primo step
        avantiButton.setDisable(step == 3); // Disabilita "Avanti" solo nell'ultimo step

        switch (step) {
            case 1:
                statusLabel.setText("Passo 1: Inserisci il tuo Username e l email");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Username:", usernameField),
                        usernameFeedback,
                        creaInput("email:", emailField),
                        creaNavigazione());
                break;
            case 2:
                statusLabel.setText("Passo 2: Inserisci Nome e Cognome");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Nome:", nomeField),
                        creaInput("Cognome:", cognomeField),
                        creaNavigazione()
                );
                break;
            case 3:
                statusLabel.setText("Passo 3: Scegli una Password e inserisci il codice univoco");
                root.getChildren().addAll(
                        statusLabel,
                        creaInput("Password:", passwordField),
                        creaInput("Conferma Password:", confirmPasswordField),
                        creaInput("Codice Univoco:", codiceUnivocoField),
                        creaNavigazioneFinale()
                );
                break;
            default:
                statusLabel.setText("Errore: Passo non valido");
                root.getChildren().addAll(statusLabel, creaNavigazione());
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
