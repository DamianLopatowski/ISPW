package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.service.EmailService;
import org.example.service.NavigationService;
import org.example.view.RegistratiCliente1View;
import org.example.view.RegistratiCliente2View;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistratiClienteController {
    private static final Logger LOGGER = Logger.getLogger(RegistratiClienteController.class.getName());

    private final Parent viewRoot;
    private final ClienteDAO clienteDAO;
    private final boolean isOnlineMode;
    private final NavigationService navigationService;
    private final String codiceUnivoco;
    private int tentativiErrati = 0;
    private static final int MAX_TENTATIVI = 3;

    public RegistratiClienteController(Stage stage, ClienteDAO clienteDAO, NavigationService navigationService, boolean isInterfaccia1) {
        this.isOnlineMode = SessionController.getIsOnlineModeStatic();
        this.clienteDAO = clienteDAO;
        this.navigationService = navigationService;
        this.codiceUnivoco = caricaCodiceUnivoco();

        // 🔹 Usa il parametro `isInterfaccia1` per decidere la view
        if (isInterfaccia1) {
            RegistratiCliente1View offlineView = new RegistratiCliente1View(stage);
            this.viewRoot = offlineView.getRoot();
            offlineView.getRegistratiButton().setOnAction(e -> registraCliente(offlineView));
        } else {
            RegistratiCliente2View onlineView = new RegistratiCliente2View(stage);
            this.viewRoot = onlineView.getRoot();
            onlineView.getRegistratiButton().setOnAction(e -> registraCliente(onlineView));
        }
    }

    public Parent getViewRoot() {
        return viewRoot;
    }

    private String caricaCodiceUnivoco() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            return properties.getProperty("codiceUnivoco", "");
        } catch (IOException e) {
            mostraMessaggioErrore("Errore nel caricamento del codice univoco.");
            return "";
        }
    }

    private void registraCliente(Object view) {
        String username;
        String nome;
        String cognome;
        String password;
        String confirmPassword;
        String codiceInserito;
        String email;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View offlineView = (RegistratiCliente1View) view;
            username = offlineView.getUsernameField().getText();
            nome = offlineView.getNomeField().getText();
            cognome = offlineView.getCognomeField().getText();
            password = offlineView.getPasswordField().getText();
            confirmPassword = offlineView.getConfirmPasswordField().getText();
            codiceInserito = offlineView.getCodiceUnivocoField().getText();
            email = offlineView.getEmailField().getText();
        } else {
            RegistratiCliente2View onlineView = (RegistratiCliente2View) view;
            username = onlineView.getUsernameField().getText();
            nome = onlineView.getNomeField().getText();
            cognome = onlineView.getCognomeField().getText();
            password = onlineView.getPasswordField().getText();
            confirmPassword = onlineView.getConfirmPasswordField().getText();
            codiceInserito = onlineView.getCodiceUnivocoField().getText();
            email = onlineView.getEmailField().getText();
        }

        if (username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || codiceInserito.isEmpty() || email.isEmpty()) {
            mostraMessaggioErrore("Tutti i campi devono essere compilati!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostraMessaggioErrore("Le password non coincidono!");
            return;
        }

        if (!codiceInserito.equals(codiceUnivoco)) {
            tentativiErrati++;
            if (tentativiErrati >= MAX_TENTATIVI) {
                mostraMessaggioErrore("Hai sbagliato il codice univoco per 3 volte. L'app verrà chiusa.");
                System.exit(0);
            } else {
                mostraMessaggioErrore("Codice univoco errato! Tentativi rimasti: " + (MAX_TENTATIVI - tentativiErrati));
            }
            return;
        }

        Cliente nuovoCliente = new Cliente(username, nome, cognome, password);
        clienteDAO.saveCliente(nuovoCliente);

        if (isOnlineMode) {
            LOGGER.log(Level.INFO, "📧 Invio email di conferma a: {0}", email);
            EmailService.sendConfirmationEmail(email, username);
        }

        mostraMessaggioSuccesso(isOnlineMode ? "Cliente registrato con successo nel database! Email inviata." : "Cliente registrato offline!");

        navigationService.navigateToLogin(true, true);
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
}
