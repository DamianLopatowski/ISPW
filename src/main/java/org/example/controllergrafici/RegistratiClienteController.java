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

public class RegistratiClienteController {
    private final Object view;
    private final RegistrazioneService registrazioneService;
    private final NavigationService navigationService;
    private static final String BORDER_GREEN = "-fx-border-color: green; -fx-border-width: 2px;";
    private static final String TEXT_GREEN = "-fx-text-fill: green;";
    private static final String BORDER_RED = "-fx-border-color: red; -fx-border-width: 2px;";
    private static final String TEXT_RED = "-fx-text-fill: red;";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_PASSWORD = "password";
    private static final String FIELD_CONFIRM_PASSWORD = "confirmPassword";



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

    private void nascondiFeedbackSeValido(TextField field, Label feedbackLabel, String fieldType, String password) {
        boolean isValid = false;

        switch (fieldType) {
            case FIELD_USERNAME:
                isValid = registrazioneService.isUsernameValid(field.getText().trim());
                break;
            case FIELD_EMAIL:
                isValid = registrazioneService.isEmailValid(field.getText().trim());
                break;
            case FIELD_PASSWORD:
                isValid = registrazioneService.isPasswordValid(field.getText().trim());
                break;
            case FIELD_CONFIRM_PASSWORD:
                isValid = field.getText().trim().equals(password);
                break;
            default:
                // Se il fieldType non è riconosciuto, logghiamo un warning e lo consideriamo non valido
                System.err.println("⚠ Warning: Tipo di campo non riconosciuto - " + fieldType);
                isValid = false;
                break;
        }

        if (isValid) {
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            feedbackLabel.setVisible(true);
            feedbackLabel.setManaged(true);
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

        // Nascondere feedback quando il campo è valido e perde il focus
        view.getUsernameField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getUsernameField(), view.getUsernameFeedback(), FIELD_USERNAME, "");
            }
        });

        view.getEmailField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getEmailField(), view.getEmailFeedback(), FIELD_EMAIL, "");
            }
        });

        view.getPasswordField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getPasswordField(), view.getPasswordFeedback(), FIELD_PASSWORD, "");
            }
        });

        view.getConfirmPasswordField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getConfirmPasswordField(), view.getConfirmPasswordFeedback(), FIELD_CONFIRM_PASSWORD, view.getPasswordField().getText());
            }
        });

        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCodiceUnivocoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
    }

    private void setupListeners(RegistratiCliente2View view) {
        view.getUsernameField().textProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaFeedbackUsername(view.getUsernameField(), view.getUsernameFeedback(), newVal);
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

        // Nascondere feedback quando il campo è valido e perde il focus
        view.getUsernameField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getUsernameField(), view.getUsernameFeedback(), FIELD_USERNAME, "");
            }
        });

        view.getEmailField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getEmailField(), view.getEmailFeedback(), FIELD_EMAIL, "");
            }
        });

        view.getPasswordField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getPasswordField(), view.getPasswordFeedback(), FIELD_PASSWORD, "");
            }
        });

        view.getConfirmPasswordField().focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!Boolean.TRUE.equals(isFocused)) {
                nascondiFeedbackSeValido(view.getConfirmPasswordField(), view.getConfirmPasswordFeedback(), FIELD_CONFIRM_PASSWORD, view.getPasswordField().getText());
            }
        });
        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCodiceUnivocoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
    }

    private void registraCliente(Object view) {
        System.out.println("📢 Registrazione avviata!");

        String username, nome, cognome, password, confirmPassword, codiceUnivocoInserito, email;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View v = (RegistratiCliente1View) view;
            username = v.getUsernameField().getText().trim();
            nome = v.getNomeField().getText().trim();
            cognome = v.getCognomeField().getText().trim();
            password = v.getPasswordField().getText().trim();
            confirmPassword = v.getConfirmPasswordField().getText().trim();
            codiceUnivocoInserito = v.getCodiceUnivocoField().getText().trim();
            email = v.getEmailField().getText().trim();
        } else {
            RegistratiCliente2View v = (RegistratiCliente2View) view;
            username = v.getUsernameField().getText().trim();
            nome = v.getNomeField().getText().trim();
            cognome = v.getCognomeField().getText().trim();
            password = v.getPasswordField().getText().trim();
            confirmPassword = v.getConfirmPasswordField().getText().trim();
            codiceUnivocoInserito = v.getCodiceUnivocoField().getText().trim();
            email = v.getEmailField().getText().trim();
        }

        try {
            if (!registrazioneService.isCodiceUnivocoValido(codiceUnivocoInserito)) {
                mostraMessaggioErrore("Codice Univoco errato! Tentativi rimasti: " + registrazioneService.getTentativiRimasti());
                if (registrazioneService.isBloccatoPerTroppiTentativi()) {
                    System.exit(0);
                }
                return;
            }

            registrazioneService.registraCliente(username, nome, cognome, password, email);
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
                    !v.getPasswordField().getText().trim().isEmpty() &&
                    !v.getConfirmPasswordField().getText().trim().isEmpty() &&
                    !v.getCodiceUnivocoField().getText().trim().isEmpty();
            v.getRegistratiButton().setDisable(!isFilled);
        } else if (view instanceof RegistratiCliente2View) {
            RegistratiCliente2View v = (RegistratiCliente2View) view;
            isFilled = !v.getUsernameField().getText().trim().isEmpty() &&
                    !v.getEmailField().getText().trim().isEmpty() &&
                    !v.getNomeField().getText().trim().isEmpty() &&
                    !v.getCognomeField().getText().trim().isEmpty() &&
                    !v.getPasswordField().getText().trim().isEmpty() &&
                    !v.getConfirmPasswordField().getText().trim().isEmpty() &&
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
