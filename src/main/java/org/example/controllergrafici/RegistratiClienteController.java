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

    private void aggiornaFeedbackUsername(TextField usernameField, Label feedbackLabel, String username) {
        if (username.isEmpty()) {
            usernameField.setStyle("");
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true);
            feedbackLabel.setVisible(true);

            if (registrazioneService.isUsernameValid(username)) {
                usernameField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Username valido");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                usernameField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ Lo username deve avere almeno 8 caratteri");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }

    private void aggiornaFeedbackIndirizzo(TextField indirizzoField, Label feedbackLabel, String indirizzo) {
        if (indirizzo.isEmpty()) {
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true);
            feedbackLabel.setVisible(true);

            if (registrazioneService.isIndirizzoValid(indirizzo)) {
                indirizzoField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Indirizzo valido");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                indirizzoField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ L'indirizzo deve iniziare con 'Via' o 'Piazza'");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }


    private void aggiornaFeedbackPartitaIva(TextField partitaIvaField, Label feedbackLabel, String partitaIva) {
        if (partitaIva.isEmpty()) {
            partitaIvaField.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true);
            feedbackLabel.setVisible(true);

            if (registrazioneService.isPartitaIvaValid(partitaIva)) {
                partitaIvaField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Partita IVA valida");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                partitaIvaField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ La Partita IVA deve contenere 11 cifre numeriche");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }

    private void aggiornaFeedbackEmail(TextField emailField, Label feedbackLabel, String email) {
        if (email.isEmpty()) {
            emailField.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true); // Assicura che sia visibile
            feedbackLabel.setVisible(true);

            if (registrazioneService.isEmailValid(email)) {
                emailField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Email valida");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                emailField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ L'email non è valida");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }

    private void aggiornaFeedbackCap(TextField capField, Label feedbackLabel, String cap) {
        if (cap.isEmpty()) {
            capField.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true);
            feedbackLabel.setVisible(true);

            if (registrazioneService.isCapValid(cap)) {
                capField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ CAP valido");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                capField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ Il CAP deve contenere esattamente 5 cifre numeriche");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }


    private void aggiornaFeedbackPassword(PasswordField passwordField, Label feedbackLabel, String password) {
        if (password.isEmpty()) {
            passwordField.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true); // Rendi visibile il messaggio
            feedbackLabel.setVisible(true);

            if (registrazioneService.isPasswordValid(password)) {
                passwordField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Password valida");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                passwordField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ La password non rispetta i requisiti");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }

    private void aggiornaFeedbackConfirmPassword(PasswordField confirmPasswordField, Label feedbackLabel, String confirmPassword, String password) {
        if (confirmPassword.isEmpty()) {
            confirmPasswordField.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setManaged(true);
            feedbackLabel.setVisible(true);

            if (confirmPassword.equals(password)) {
                confirmPasswordField.setStyle(BORDER_GREEN);
                feedbackLabel.setText("✔ Conferma corretta");
                feedbackLabel.setStyle(TEXT_GREEN);
            } else {
                confirmPasswordField.setStyle(BORDER_RED);
                feedbackLabel.setText("❌ Le password non corrispondono");
                feedbackLabel.setStyle(TEXT_RED);
            }
        }
    }



    private void setupListeners(RegistratiCliente1View view) {
        view.getUsernameField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackUsername(view.getUsernameField(), view.getUsernameFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getPartitaIvaField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackPartitaIva(view.getPartitaIvaField(), view.getPartitaIvaFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getEmailField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackEmail(view.getEmailField(), view.getEmailFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getPasswordField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackPassword(view.getPasswordField(), view.getPasswordFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getConfirmPasswordField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackConfirmPassword(view.getConfirmPasswordField(), view.getConfirmPasswordFeedback(), newVal, view.getPasswordField().getText());
            aggiornaStatoRegistratiButton(view);
        });
        view.getIndirizzoField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackIndirizzo(view.getIndirizzoField(), view.getIndirizzoFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });
        view.getCapField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackCap(view.getCapField(), view.getCapFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getCivicoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCittaField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCodiceUnivocoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
    }

    private void setupListeners(RegistratiCliente2View view) {
        view.getUsernameField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackUsername(view.getUsernameField(), view.getUsernameFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getPartitaIvaField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackPartitaIva(view.getPartitaIvaField(), view.getPartitaIvaFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });


        view.getEmailField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackEmail(view.getEmailField(), view.getEmailFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getPasswordField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackPassword(view.getPasswordField(), view.getPasswordFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getConfirmPasswordField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackConfirmPassword(view.getConfirmPasswordField(), view.getConfirmPasswordFeedback(), newVal, view.getPasswordField().getText());
            aggiornaStatoRegistratiButton(view);
        });
        view.getIndirizzoField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackIndirizzo(view.getIndirizzoField(), view.getIndirizzoFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });
        view.getCapField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackCap(view.getCapField(), view.getCapFeedback(), newVal);
            aggiornaStatoRegistratiButton(view);
        });

        view.getCivicoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCittaField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCodiceUnivocoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
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
