package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.dao.ClienteDAO;
import org.example.service.RegistrazioneException;
import org.example.service.RegistrazioneService;
import org.example.service.NavigationService;
import org.example.view.RegistratiCliente1View;
import org.example.view.RegistratiCliente2View;

import java.util.function.Function;
import java.util.logging.Logger;

public class RegistratiClienteController {
    private static final Logger LOGGER = Logger.getLogger(RegistratiClienteController.class.getName());
    private final Object view;
    private final RegistrazioneService registrazioneService;
    private final NavigationService navigationService;
    private static final String BORDER_GREEN = "-fx-border-color: green; -fx-border-width: 2px;";
    private static final String TEXT_GREEN = "-fx-text-fill: green;";
    private static final String BORDER_RED = "-fx-border-color: red; -fx-border-width: 2px;";
    private static final String TEXT_RED = "-fx-text-fill: red;";



    public RegistratiClienteController(ClienteDAO clienteDAO, NavigationService navigationService, String codiceUnivoco, boolean isInterfaccia1) {
        this.registrazioneService = new RegistrazioneService(clienteDAO, codiceUnivoco);
        this.navigationService = navigationService;

        if (isInterfaccia1) {
            this.view = new RegistratiCliente1View();
            RegistratiCliente1View v = (RegistratiCliente1View) this.view;
            setupListeners(v);
            v.getRegistratiButton().setOnAction(e -> registraCliente(v)); // Aggiunta dell'azione
        } else {
            this.view = new RegistratiCliente2View();
            RegistratiCliente2View v = (RegistratiCliente2View) this.view;
            setupListeners(v);
            v.getRegistratiButton().setOnAction(e -> registraCliente(v)); // Aggiunta dell'azione
        }
    }

    private void aggiornaFeedbackCampo(TextField field, Label feedbackLabel, String valore,
                                       Function<String, Boolean> validazione, String messaggioErrore) {
        if (valore.isEmpty()) {
            field.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            boolean isValid = validazione.apply(valore);

            field.setStyle(isValid ? BORDER_GREEN : BORDER_RED);
            feedbackLabel.setText(isValid ? "" : "❌ " + messaggioErrore);
            feedbackLabel.setStyle(TEXT_RED);
            feedbackLabel.setVisible(!isValid);
            feedbackLabel.setManaged(!isValid);
        }
    }

    private void setupListeners(Object view) {
        if (!(view instanceof RegistratiCliente1View) && !(view instanceof RegistratiCliente2View)) {
            return;
        }

        TextField usernameField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getUsernameField() :
                ((RegistratiCliente2View) view).getUsernameField();
        Label usernameFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getUsernameFeedback() :
                ((RegistratiCliente2View) view).getUsernameFeedback();

        TextField emailField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getEmailField() :
                ((RegistratiCliente2View) view).getEmailField();
        Label emailFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getEmailFeedback() :
                ((RegistratiCliente2View) view).getEmailFeedback();

        TextField partitaIvaField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getPartitaIvaField() :
                ((RegistratiCliente2View) view).getPartitaIvaField();
        Label partitaIvaFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getPartitaIvaFeedback() :
                ((RegistratiCliente2View) view).getPartitaIvaFeedback();

        TextField indirizzoField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getIndirizzoField() :
                ((RegistratiCliente2View) view).getIndirizzoField();
        Label indirizzoFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getIndirizzoFeedback() :
                ((RegistratiCliente2View) view).getIndirizzoFeedback();

        TextField capField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCapField() :
                ((RegistratiCliente2View) view).getCapField();
        Label capFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCapFeedback() :
                ((RegistratiCliente2View) view).getCapFeedback();

        TextField civicoField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCivicoField() :
                ((RegistratiCliente2View) view).getCivicoField();
        TextField cittaField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCittaField() :
                ((RegistratiCliente2View) view).getCittaField();

        PasswordField passwordField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getPasswordField() :
                ((RegistratiCliente2View) view).getPasswordField();
        Label passwordFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getPasswordFeedback() :
                ((RegistratiCliente2View) view).getPasswordFeedback();

        PasswordField confirmPasswordField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getConfirmPasswordField() :
                ((RegistratiCliente2View) view).getConfirmPasswordField();
        Label confirmPasswordFeedback = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getConfirmPasswordFeedback() :
                ((RegistratiCliente2View) view).getConfirmPasswordFeedback();

        TextField nomeField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getNomeField() :
                ((RegistratiCliente2View) view).getNomeField();
        TextField cognomeField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCognomeField() :
                ((RegistratiCliente2View) view).getCognomeField();
        TextField codiceUnivocoField = (view instanceof RegistratiCliente1View) ?
                ((RegistratiCliente1View) view).getCodiceUnivocoField() :
                ((RegistratiCliente2View) view).getCodiceUnivocoField();

        // Listener generico per i campi di testo con validazione
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackCampo(usernameField, usernameFeedback, newVal,
                    registrazioneService::isUsernameValid, "Lo username deve avere almeno 8 caratteri");
            aggiornaStatoRegistratiButton(view);
        });

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(emailField, emailFeedback, newVal,
                        registrazioneService::isEmailValid, "L'email non è valida");
            aggiornaStatoRegistratiButton(view);
    });

        partitaIvaField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(partitaIvaField, partitaIvaFeedback, newVal,
                        registrazioneService::isPartitaIvaValid, "La Partita IVA deve contenere 11 cifre numeriche");
            aggiornaStatoRegistratiButton(view);
        });

        indirizzoField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(indirizzoField, indirizzoFeedback, newVal,
                        registrazioneService::isIndirizzoValid, "L'indirizzo deve iniziare con 'Via' o 'Piazza'");
            aggiornaStatoRegistratiButton(view);
        });

        capField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(capField, capFeedback, newVal,
                        registrazioneService::isCapValid, "Il CAP deve contenere esattamente 5 cifre numeriche");
            aggiornaStatoRegistratiButton(view);
        });

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(passwordField, passwordFeedback, newVal,
                        registrazioneService::isPasswordValid, "La password non rispetta i requisiti");
            aggiornaStatoRegistratiButton(view);
        });

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaFeedbackCampo(confirmPasswordField, confirmPasswordFeedback, newVal,
                        password -> password.equals(passwordField.getText()), "Le password non corrispondono");
            aggiornaStatoRegistratiButton(view);
        });

        // Listener per aggiornare lo stato del pulsante "Registrati"
        nomeField.textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        cognomeField.textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        codiceUnivocoField.textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        civicoField.textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        cittaField.textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
    }


    private void registraCliente(Object view) {
        LOGGER.info("📢 Registrazione avviata!");

        String username;
        String nome;
        String cognome;
        String password;
        String codiceUnivocoInserito;
        String email;
        String partitaIva;
        String indirizzo;
        String civico;
        String cap;
        String citta;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View v = (RegistratiCliente1View) view;
            username = v.getUsernameField().getText().trim();
            nome = v.getNomeField().getText().trim();
            cognome = v.getCognomeField().getText().trim();
            password = v.getPasswordField().getText().trim();
            codiceUnivocoInserito = v.getCodiceUnivocoField().getText().trim();
            email = v.getEmailField().getText().trim();
            partitaIva = v.getPartitaIvaField().getText().trim();
            indirizzo = v.getIndirizzoField().getText().trim();
            civico = v.getCivicoField().getText().trim();
            cap = v.getCapField().getText().trim();
            citta = v.getCittaField().getText().trim();
        } else {
            RegistratiCliente2View v = (RegistratiCliente2View) view;
            username = v.getUsernameField().getText().trim();
            nome = v.getNomeField().getText().trim();
            cognome = v.getCognomeField().getText().trim();
            password = v.getPasswordField().getText().trim();
            codiceUnivocoInserito = v.getCodiceUnivocoField().getText().trim();
            email = v.getEmailField().getText().trim();
            partitaIva = v.getPartitaIvaField().getText().trim();
            indirizzo = v.getIndirizzoField().getText().trim();
            civico = v.getCivicoField().getText().trim();
            cap = v.getCapField().getText().trim();
            citta = v.getCittaField().getText().trim();
        }

        try {
            if (!registrazioneService.isCodiceUnivocoValido(codiceUnivocoInserito)) {
                mostraMessaggioErrore("Codice Univoco errato! Tentativi rimasti: " + registrazioneService.getTentativiRimasti());
                if (registrazioneService.isBloccatoPerTroppiTentativi()) {
                    System.exit(0);
                }
                return;
            }

            registrazioneService.registraCliente(username, nome, cognome, password, email, partitaIva, indirizzo, civico, cap, citta);
            mostraMessaggioSuccesso("Cliente registrato con successo!");

            // Navigazione al login dopo registrazione
            navigationService.navigateToLogin(true, true);

        } catch (RegistrazioneException ex) {
            mostraMessaggioErrore(ex.getMessage());
        }
    }

    private void aggiornaStatoRegistratiButton(Object view) {
        boolean isFilled;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View v = (RegistratiCliente1View) view;
            isFilled = !v.getUsernameField().getText().trim().isEmpty() &&
                    !v.getEmailField().getText().trim().isEmpty() &&
                    !v.getNomeField().getText().trim().isEmpty() &&
                    !v.getCognomeField().getText().trim().isEmpty() &&
                    !v.getPartitaIvaField().getText().trim().isEmpty() &&
                    !v.getPasswordField().getText().trim().isEmpty() &&
                    !v.getConfirmPasswordField().getText().trim().isEmpty() &&
                    !v.getIndirizzoField().getText().trim().isEmpty() &&
                    !v.getCivicoField().getText().trim().isEmpty() &&
                    !v.getCapField().getText().trim().isEmpty() &&
                    !v.getCittaField().getText().trim().isEmpty() &&
                    !v.getCodiceUnivocoField().getText().trim().isEmpty();
            v.getRegistratiButton().setDisable(!isFilled);
        } else if (view instanceof RegistratiCliente2View) {
            RegistratiCliente2View v = (RegistratiCliente2View) view;
            isFilled = !v.getUsernameField().getText().trim().isEmpty() &&
                    !v.getEmailField().getText().trim().isEmpty() &&
                    !v.getNomeField().getText().trim().isEmpty() &&
                    !v.getCognomeField().getText().trim().isEmpty() &&
                    !v.getPartitaIvaField().getText().trim().isEmpty() &&
                    !v.getPasswordField().getText().trim().isEmpty() &&
                    !v.getConfirmPasswordField().getText().trim().isEmpty() &&
                    !v.getIndirizzoField().getText().trim().isEmpty() &&
                    !v.getCivicoField().getText().trim().isEmpty() &&
                    !v.getCapField().getText().trim().isEmpty() &&
                    !v.getCittaField().getText().trim().isEmpty() &&
                    !v.getCodiceUnivocoField().getText().trim().isEmpty();
            v.getRegistratiButton().setDisable(!isFilled);
        }
    }

    private void mostraMessaggioErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraMessaggioSuccesso(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successo");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    public Parent getView() {
        if (view instanceof RegistratiCliente1View) {
            return ((RegistratiCliente1View) view).getRoot();
        } else if (view instanceof RegistratiCliente2View) {
            return ((RegistratiCliente2View) view).getRoot();
        } else {
            return null;
        }
    }

}
