package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
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

    public RegistratiClienteController(Stage stage, ClienteDAO clienteDAO, NavigationService navigationService, String codiceUnivoco, boolean isInterfaccia1) {
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


    private void setupListeners(RegistratiCliente1View view) {
        view.getUsernameField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getEmailField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getPasswordField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getConfirmPasswordField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCodiceUnivocoField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
    }

    private void setupListeners(RegistratiCliente2View view) {
        view.getUsernameField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getEmailField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getNomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getCognomeField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getPasswordField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
        view.getConfirmPasswordField().textProperty().addListener((obs, oldVal, newVal) -> aggiornaStatoRegistratiButton(view));
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
