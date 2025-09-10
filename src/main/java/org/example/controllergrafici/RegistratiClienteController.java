package org.example.controllergrafici;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.bean.ClienteBean;
import org.example.dao.ClienteDAO;
import org.example.exception.RegistrazioneException;
import org.example.service.RegistrazioneService;
import org.example.service.NavigationService;
import org.example.view.RegistratiCliente1View;
import org.example.view.RegistratiCliente2View;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class RegistratiClienteController {
    private static final Logger LOGGER = Logger.getLogger(RegistratiClienteController.class.getName());
    private final Object view;
    private final RegistrazioneService registrazioneService;
    private final NavigationService navigationService;
    private static final String BORDER_GREEN = "-fx-border-color: green; -fx-border-width: 2px;";
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
                                       Predicate<String> validazione, String messaggioErrore)
    {
        if (valore.isEmpty()) {
            field.setStyle(""); // Reset stile
            feedbackLabel.setText("");
            feedbackLabel.setVisible(false);
            feedbackLabel.setManaged(false);
        } else {
            boolean isValid = validazione.test(valore);

            field.setStyle(isValid ? BORDER_GREEN : BORDER_RED);
            feedbackLabel.setText(isValid ? "" : "x " + messaggioErrore);
            feedbackLabel.setStyle(TEXT_RED);
            feedbackLabel.setVisible(!isValid);
            feedbackLabel.setManaged(!isValid);
        }
    }
    private TextField getViewField(Object view, String methodName) {
        try {
            return (TextField) view.getClass().getMethod(methodName).invoke(view);
        } catch (Exception e) {
            LOGGER.warning("Errore nell'accesso al campo: " + methodName);
            return new TextField();
        }
    }

    private Label getViewLabel(Object view, String methodName) {
        try {
            return (Label) view.getClass().getMethod(methodName).invoke(view);
        } catch (Exception e) {
            LOGGER.warning("Errore nell'accesso al label: " + methodName);
            return new Label();
        }
    }


    private void setupListeners(Object view) {
        if (!(view instanceof RegistratiCliente1View) && !(view instanceof RegistratiCliente2View)) {
            return;
        }

        // Inizializza i campi
        TextField[] textFields = {
                getViewField(view, "getUsernameField"),
                getViewField(view, "getEmailField"),
                getViewField(view, "getPartitaIvaField"),
                getViewField(view, "getIndirizzoField"),
                getViewField(view, "getCapField"),
                getViewField(view, "getCivicoField"),
                getViewField(view, "getCittaField"),
                getViewField(view, "getPasswordField"),
                getViewField(view, "getConfirmPasswordField"),
                getViewField(view, "getNomeField"),
                getViewField(view, "getCognomeField"),
                getViewField(view, "getCodiceUnivocoField")
        };

        Label[] feedbackLabels = {
                getViewLabel(view, "getUsernameFeedback"),
                getViewLabel(view, "getEmailFeedback"),
                getViewLabel(view, "getPartitaIvaFeedback"),
                getViewLabel(view, "getIndirizzoFeedback"),
                getViewLabel(view, "getCapFeedback"),
                null, // Il civico non ha feedback
                null, // La città non ha feedback
                getViewLabel(view, "getPasswordFeedback"),
                getViewLabel(view, "getConfirmPasswordFeedback"),
                null, // Nome non ha feedback
                null, // Cognome non ha feedback
                null  // Codice Univoco non ha feedback
        };

        List<Predicate<String>> validators = List.of(
                registrazioneService::isUsernameValid,
                registrazioneService::isEmailValid,
                registrazioneService::isPartitaIvaValid,
                registrazioneService::isIndirizzoValid,
                registrazioneService::isCapValid,
                registrazioneService::isCivicoValid,
                str -> !str.trim().isEmpty(), // Città obbligatoria
                registrazioneService::isPasswordValid,
                password -> password.equals(getViewField(view, "getPasswordField").getText()), // Conferma password
                str -> !str.trim().isEmpty(), // Nome obbligatorio
                str -> !str.trim().isEmpty(), // Cognome obbligatorio
                str -> !str.trim().isEmpty()  // Codice Univoco obbligatorio
        );


        String[] errorMessages = {
                "Lo username deve avere almeno 8 caratteri",
                "L'email non è valida",
                "La Partita IVA deve contenere 11 cifre numeriche",
                "L'indirizzo deve iniziare con 'Via' o 'Piazza'",
                "Il CAP deve contenere esattamente 5 cifre numeriche",
                "Numero civico non valido!",
                "Il campo Città è obbligatorio",
                "La password non rispetta i requisiti",
                "Le password non corrispondono",
                "Il campo Nome è obbligatorio",
                "Il campo Cognome è obbligatorio",
                "Il Codice Univoco è obbligatorio"
        };

        // Applica i listener generici
        for (int i = 0; i < textFields.length; i++) {
            final int index = i;
            textFields[i].textProperty().addListener((obs, oldVal, newVal) -> {
                if (feedbackLabels[index] != null) {
                    aggiornaFeedbackCampo(
                            textFields[index],
                            feedbackLabels[index],
                            newVal,
                            validators.get(index),
                            errorMessages[index]
                    );
                } else {
                    // Anche se non ha una label, va comunque verificata la validità (per attivare il pulsante)
                    boolean isValid = validators.get(index).test(newVal);
                    textFields[index].setStyle(isValid ? BORDER_GREEN : BORDER_RED);
                }
                aggiornaStatoRegistratiButton(view);
            });
        }
    }



    private void registraCliente(Object view) {
        LOGGER.info("Registrazione avviata!");

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

            ClienteBean clienteBean = new ClienteBean();
            clienteBean.setUsername(username);
            clienteBean.setNome(nome);
            clienteBean.setCognome(cognome);
            clienteBean.setPassword(password);
            clienteBean.setEmail(email);
            clienteBean.setPartitaIva(partitaIva);
            clienteBean.setIndirizzo(indirizzo);
            clienteBean.setCivico(civico);
            clienteBean.setCap(cap);
            clienteBean.setCitta(citta);

            registrazioneService.registraCliente(clienteBean);

            mostraMessaggioSuccesso("Cliente registrato con successo!");

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