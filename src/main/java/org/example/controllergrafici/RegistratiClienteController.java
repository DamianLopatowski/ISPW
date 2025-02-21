package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.view.RegistratiCliente1View;
import org.example.view.RegistratiCliente2View;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class RegistratiClienteController {
    private final Parent viewRoot;
    private final ClienteDAO clienteDAO;
    private final boolean isOnlineMode;
    private final NavigationService navigationService;
    private final String codiceUnivoco;
    private int tentativiErrati = 0;
    private static final int MAX_TENTATIVI = 3;

    public RegistratiClienteController(Stage stage, ClienteDAO clienteDAO, boolean isInterfaccia1, NavigationService navigationService) {
        this.isOnlineMode = SessionController.getIsOnlineModeStatic();
        this.clienteDAO = clienteDAO;
        this.navigationService = navigationService;
        this.codiceUnivoco = caricaCodiceUnivoco();

        if (isInterfaccia1) {
            RegistratiCliente1View viewOffline = new RegistratiCliente1View(stage);
            this.viewRoot = viewOffline.getRoot();
            viewOffline.getRegistratiButton().setOnAction(e -> registraCliente(viewOffline, true));
        } else {
            RegistratiCliente2View viewOnline = new RegistratiCliente2View(stage);
            this.viewRoot = viewOnline.getRoot();
            viewOnline.getRegistratiButton().setOnAction(e -> registraCliente(viewOnline, false));
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

    private void registraCliente(Object view, boolean isInterfaccia1) {
        String username;
        String nome;
        String cognome;
        String password;
        String confirmPassword;
        String codiceInserito;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View offlineView = (RegistratiCliente1View) view;
            username = offlineView.getUsernameField().getText();
            nome = offlineView.getNomeField().getText();
            cognome = offlineView.getCognomeField().getText();
            password = offlineView.getPasswordField().getText();
            confirmPassword = offlineView.getConfirmPasswordField().getText();
            codiceInserito = offlineView.getCodiceUnivocoField().getText();
        } else {
            RegistratiCliente2View onlineView = (RegistratiCliente2View) view;
            username = onlineView.getUsernameField().getText();
            nome = onlineView.getNomeField().getText();
            cognome = onlineView.getCognomeField().getText();
            password = onlineView.getPasswordField().getText();
            confirmPassword = onlineView.getConfirmPasswordField().getText();
            codiceInserito = onlineView.getCodiceUnivocoField().getText();
        }

        if (username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || codiceInserito.isEmpty()) {
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
        mostraMessaggioSuccesso(isOnlineMode ? "Cliente registrato con successo nel database!" : "Cliente registrato offline!");

        navigationService.navigateToLogin(isInterfaccia1, true);
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
